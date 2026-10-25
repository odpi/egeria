/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import org.odpi.openmetadata.adapters.connectors.darwin.controls.DarwinConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.darwin.ffdc.DarwinAuditCode;
import org.odpi.openmetadata.adapters.connectors.darwin.ffdc.DarwinErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ExceptionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DarwinProductDependencyManagerConnector maintains the coarse-grained lineage that is implied by the finer-grained
 * lineage beneath it, working upwards from the most detailed level on each refresh:
 * <ul>
 *     <li><b>Schema elements to the data fabric.</b>  A DataMapping relationship between two schema elements - or between a
 *     schema element and a process - shows data being copied from one to the other.  Where the two ends belong to
 *     different data fabric elements (a schema element belongs to the data asset it is anchored to; a process is its
 *     own), data flows from the first to the second, so a DataFlow relationship is maintained between them.</li>
 *     <li><b>The data fabric to the system fabric.</b>  An item of IT infrastructure supports software capabilities
 *     (through the SupportedSoftwareCapability relationship), and a capability uses data assets and processes (through
 *     the CapabilityAssetUse relationship - with a useType of OWNS for a data asset, and any useType for a process).
 *     Where the lineage leads - directly or through intermediate elements - from an element used by one item's
 *     capabilities to an element used by another's, a relationship is maintained between the two items: a DataFlow
 *     for data lineage, a ProcessCall where a ProcessCall reaches the element, and a ControlFlow for a path of
 *     ControlFlow relationships.</li>
 *     <li><b>Data assets to digital products.</b>  The assets that are members of a digital product (through the
 *     CollectionMembership relationship) are the start of paths followed downstream through the data lineage.  A path
 *     may pass through any number of intermediate elements - processes, assets that belong to no product - but every
 *     relationship on it must belong to the same information supply chain (the iscQualifiedName of the lineage
 *     relationship).  When the path reaches an asset that is a member of another product, that product depends on
 *     the product the path started from, through that information supply chain, so a DigitalProductDependency
 *     relationship is maintained between the products.  The path stops there: the dependency on anything further
 *     downstream belongs to the product just reached.</li>
 * </ul>
 * Each of these is a boundary between two layers of lineage (see LineageBoundary): a lineage path in the lower layer
 * between elements with different owners proves a relationship between the owners in the upper layer.  The lineage
 * of each lower layer is loaded once per refresh into a LineageGraph, and a LineageWalker follows the paths through
 * it using the boundary's owner function.  The layers are worked through from the bottom up, and the derived
 * relationships of each layer are written before the layer is read by the ones above it.
 * <p>
 * The information supply chain of the finer-grained relationship is carried up onto the coarser one, and because
 * every one of these relationship types is multi-link there is one relationship per information supply chain
 * between the same two elements.  Each level is reconciled with the relationships in the repository the same way.
 * Relationships this connector created itself - recognized by the createdBy in their header - are removed when the
 * finer-grained lineage no longer supports them, and missing ones are created.  Relationships asserted by external
 * users take precedence and are never removed: one whose information supply chain is not set is given the supply
 * chain of the first finer-grained lineage that proves it.
 * <p>
 * When following the data fabric's lineage, a data set and the content it is a view over (DataSetContent) are treated
 * as the same element, so a product whose member is an abstract data set reaches the lineage attached to the physical
 * elements beneath it.
 * <p>
 * A relationship that names no information supply chain is treated as "supply chain unknown": it can join a path of any
 * supply chain, and a path whose relationships name none proves a relationship of any supply chain in the layer
 * above.  Once a path's supply chain is known - from a relationship on it or from the external relationship it proves
 * - the untagged external relationships on it are given that supply chain at the end of the refresh: filled in where
 * one supply chain is known for them, and copied once for each supply chain where there are several.
 * <p>
 * An externally asserted relationship that the lineage beneath it does not prove is recorded as an exception, provided
 * the layer beneath is catalogued for both of its ends - otherwise the missing detail is no evidence against it, and it
 * is only counted as unchecked.  The connector has an ExceptionType for each relationship type, created on first use,
 * and links it to each element with unproven relationships of that type - the element the data or control reaches, or
 * the dependent product - with an Exception relationship whose affectedRelationships property lists the unproven
 * relationships.  The exception is updated as the list changes and removed once nothing on it is left.
 * <p>
 * A relationship this connector created that someone else has since updated has been adopted.  It is treated from then
 * on as asserted by an external user: it is no longer updated or removed, and is validated like any other external
 * relationship.
 * <p>
 * This connector works across the whole open metadata ecosystem rather than through catalog targets, which is why
 * it extends IntegrationConnectorBase rather than DynamicIntegrationConnectorBase.  Each refresh reconciles from a
 * fresh snapshot, so nothing is carried between refreshes but the identities of the exception types.
 */
public class DarwinProductDependencyManagerConnector extends IntegrationConnectorBase
{
    /*
     * Recorded on every Exception relationship this connector creates.
     */
    /*
     * Recorded on the copies of untagged relationships this connector makes for each of their supply chains.  The
     * derivation is how the copies are told apart from this connector's other relationships of the same type; copies
     * made by earlier releases carried it in the label instead.  A copy carries the label of the relationship it
     * copies, since that summarizes the data that flows.
     */
    private static final String supplyChainCopyDerivation  = "supply chain inferred";
    private static final String supplyChainCopyDescription = "This relationship is a copy, made by the Darwin Product Dependency Manager, of a " +
                                                                     "relationship between the same two elements that names no information " +
                                                                     "supply chain.  That relationship lies on the lineage paths of several " +
                                                                     "information supply chains, so rather than choose one, Darwin records it " +
                                                                     "once for each.  The copy is removed automatically when the lineage no " +
                                                                     "longer places the relationship on this supply chain's paths.";

    private static final String unprovenExceptionNotes = "Raised automatically by the Darwin Product Dependency Manager.  It is " +
                                                                 "updated on each refresh as the list of unproven relationships " +
                                                                 "changes, and removed once the list is empty.";

    /**
     * The data lineage relationship types where the data flows from end 2 to end 1.  In every other data lineage
     * relationship - DataFlow, ProcessCall, LineageMapping, UltimateDestination and any new subtype - the data
     * flows from end 1 to end 2.
     */
    private static final List<String> reverseFlowLineageTypes = List.of(OpenMetadataType.ULTIMATE_SOURCE_RELATIONSHIP.typeName);

    private       int                        maxLineageDepth    = DarwinConfigurationProperty.DEFAULT_MAX_LINEAGE_DEPTH;
    private final Map<ExceptionKind, String> exceptionTypeGUIDs = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(DarwinProductDependencyManagerConnector.class);


    /**
     * The kinds of coarse-grained relationship that this connector maintains.  Each names the boundary between layers
     * it is derived across, the relationship type, the derivation and description recorded on the relationships the
     * connector creates, and - for the audit log - why the relationship exists.  The derivation also tells this
     * connector's relationships of one kind from its relationships of another kind of the same type.  Earlier releases
     * recorded this in the label, which is for summarizing the data that flows, so a relationship of this connector's
     * with no derivation and the kind's legacy label is recognized too, and moved over to the derivation.
     */
    private enum LinkKind
    {
        /**
         * A DataFlow between two data fabric elements - data assets and processes - derived from the DataMapping
         * relationships between their schema elements (or between a data asset's schema elements and a process).
         */
        ASSET_LINEAGE(LineageBoundary.SCHEMA_DETAIL_TO_DATA_FABRIC,
                      OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                      "derived from data mappings",
                      "derived from data mappings",
                      "This data flow was derived by the Darwin Product Dependency Manager from the DataMapping relationships " +
                              "between the schema elements of the two data assets, or between a data asset's schema elements " +
                              "and a process.  It is removed automatically if the data mappings no longer support it.",
                      "the schema elements of the two elements are linked by DataMapping relationships"),

        /**
         * A DataFlow between two items of IT infrastructure, derived from the data lineage between the data assets
         * and processes that their software capabilities use.  Earlier releases labelled these "derived from asset
         * lineage", from when only data assets counted.
         */
        SERVER_DATA_FLOW(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC,
                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                         "derived from data fabric lineage",
                         "derived from asset lineage",
                         "This data flow was derived by the Darwin Product Dependency Manager from the data lineage between the " +
                                 "data assets and processes used by the software capabilities of the two items of IT " +
                                 "infrastructure.  It is removed automatically if that lineage no longer supports it.",
                         "the data assets and processes used by the software capabilities of the two items of infrastructure are linked by data lineage"),

        /**
         * A ProcessCall between two items of IT infrastructure, derived from the process calls between the
         * processes that their software capabilities use.
         */
        SERVER_PROCESS_CALL(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC,
                            OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                            "derived from process calls",
                            "derived from process calls",
                            "This process call was derived by the Darwin Product Dependency Manager from the ProcessCall " +
                                    "relationships between the processes used by the software capabilities of the two items of IT " +
                                    "infrastructure.  It is removed automatically if that lineage no longer supports it.",
                            "a process used by the software capabilities of one item of infrastructure calls a process used by the other's"),

        /**
         * A ControlFlow between two items of IT infrastructure, derived from the control flow between the processes
         * that their software capabilities use.
         */
        SERVER_CONTROL_FLOW(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC,
                            OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                            "derived from process control flow",
                            "derived from process control flow",
                            "This control flow was derived by the Darwin Product Dependency Manager from the ControlFlow " +
                                    "relationships between the processes used by the software capabilities of the two items of IT " +
                                    "infrastructure.  It is removed automatically if that lineage no longer supports it.",
                            "control passes from a process used by the software capabilities of one item of infrastructure to a process used by the other's"),

        /**
         * A DigitalProductDependency between two digital products, derived from the lineage between their assets.
         */
        PRODUCT_DEPENDENCY(LineageBoundary.DATA_FABRIC_TO_DATA_MESH,
                           OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                           "derived from lineage",
                           "derived from lineage",
                           "This dependency was derived by the Darwin Product Dependency Manager from the data lineage between " +
                                   "the assets that are members of the two digital products.  It is removed automatically if the " +
                                   "lineage no longer supports it.",
                           "the assets of the two digital products are linked by lineage");

        final LineageBoundary boundary;
        final String          typeName;
        final String          derivation;
        final String          legacyLabel;
        final String          description;
        final String          reason;

        LinkKind(LineageBoundary boundary, String typeName, String derivation, String legacyLabel, String description, String reason)
        {
            this.boundary    = boundary;
            this.typeName    = typeName;
            this.derivation  = derivation;
            this.legacyLabel = legacyLabel;
            this.description = description;
            this.reason      = reason;
        }
    }


    /**
     * The exception types this connector records unproven relationships against - one per relationship type.  An
     * externally asserted relationship that the lineage beneath it does not prove is listed in the
     * affectedRelationships property of an Exception relationship between the element that depends on it and the
     * exception type for its relationship type.
     */
    private enum ExceptionKind
    {
        /**
         * Unproven DigitalProductDependency relationships, recorded against the dependent product.
         */
        DIGITAL_PRODUCT_DEPENDENCY(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                   "UnprovenDigitalProductDependency",
                                   "Unproven digital product dependency",
                                   "A digital product has a DigitalProductDependency relationship, asserted by an external user, that " +
                                           "is not backed up by the data lineage between the assets of the two products.",
                                   "The Darwin Product Dependency Manager derives the dependencies between digital products from the " +
                                           "data lineage between the assets that are members of the products, following each " +
                                           "information supply chain in turn.  A DigitalProductDependency relationship asserted by an " +
                                           "external user is expected to be provable the same way.  Where it is not, the dependent " +
                                           "product is linked to this exception type and the affectedRelationships property of the " +
                                           "Exception relationship lists the relationships that could not be proved.  Either the " +
                                           "lineage is incomplete, the information supply chain named on the relationship is wrong, or " +
                                           "the dependency does not exist.",
                                   "unproven dependencies",
                                   "The DigitalProductDependency relationships listed in affectedRelationships were asserted by external " +
                                           "users and are not supported by the data lineage between the assets of the two products for " +
                                           "the information supply chain named on the relationship."),

        /**
         * Unproven DataFlow relationships - in the data fabric or the system fabric - recorded against the element
         * that receives the data.
         */
        DATA_FLOW(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, "UnprovenDataFlow", "Unproven data flow"),

        /**
         * Unproven ControlFlow relationships in the system fabric, recorded against the element that control passes to.
         */
        CONTROL_FLOW(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, "UnprovenControlFlow", "Unproven control flow"),

        /**
         * Unproven ProcessCall relationships in the system fabric, recorded against the element that is called.
         */
        PROCESS_CALL(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName, "UnprovenProcessCall", "Unproven process call");


        final String relationshipTypeName;
        final String identifier;
        final String qualifiedName;
        final String displayName;
        final String summary;
        final String description;
        final String exceptionLabel;
        final String exceptionDescription;


        /**
         * Constructor for an exception type with its own wording.
         *
         * @param relationshipTypeName the type of the unproven relationships
         * @param identifier identifier of the exception type - also the end of its qualified name
         * @param displayName display name of the exception type
         * @param summary summary of the exception type
         * @param description description of the exception type
         * @param exceptionLabel label recorded on each Exception relationship
         * @param exceptionDescription description recorded on each Exception relationship
         */
        ExceptionKind(String relationshipTypeName,
                      String identifier,
                      String displayName,
                      String summary,
                      String description,
                      String exceptionLabel,
                      String exceptionDescription)
        {
            this.relationshipTypeName = relationshipTypeName;
            this.identifier           = identifier;
            this.qualifiedName        = OpenMetadataType.EXCEPTION_TYPE.typeName + "::" + identifier;
            this.displayName          = displayName;
            this.summary              = summary;
            this.description          = description;
            this.exceptionLabel       = exceptionLabel;
            this.exceptionDescription = exceptionDescription;
        }


        /**
         * Constructor for the exception type of a lineage relationship in the data fabric or system fabric.
         *
         * @param relationshipTypeName the type of the unproven relationships
         * @param identifier identifier of the exception type - also the end of its qualified name
         * @param displayName display name of the exception type
         */
        ExceptionKind(String relationshipTypeName,
                      String identifier,
                      String displayName)
        {
            this(relationshipTypeName,
                 identifier,
                 displayName,
                 "An element is at the end of a " + relationshipTypeName + " relationship, asserted by an external user, that is " +
                         "not backed up by the finer-grained lineage beneath it.",
                 "The Darwin Product Dependency Manager derives the lineage at each layer from the layer beneath it: the data " +
                         "flows between data assets and processes from the data mappings between their schema elements, and the " +
                         "data flows, process calls and control flows between items of IT infrastructure from the lineage between " +
                         "the data assets and processes that their software capabilities use.  A " + relationshipTypeName +
                         " relationship asserted by an external user is expected to be provable the same way wherever the layer " +
                         "beneath is catalogued for both of its ends.  Where it is not, the element at end 2 of the relationship is " +
                         "linked to this exception type and the affectedRelationships property of the Exception relationship lists " +
                         "the relationships that could not be proved.  Either the finer-grained lineage is incomplete, the " +
                         "information supply chain named on the relationship is wrong, or the relationship does not reflect what " +
                         "happens.",
                 "unproven lineage",
                 "The " + relationshipTypeName + " relationships listed in affectedRelationships were asserted by external users " +
                         "and are not supported by the finer-grained lineage beneath them for the information supply chain named " +
                         "on the relationship.");
        }


        /**
         * Return the exception kind for the relationships of a type.
         *
         * @param relationshipTypeName type of relationship
         * @return exception kind
         */
        static ExceptionKind forRelationshipType(String relationshipTypeName)
        {
            for (ExceptionKind exceptionKind : ExceptionKind.values())
            {
                if (exceptionKind.relationshipTypeName.equals(relationshipTypeName))
                {
                    return exceptionKind;
                }
            }

            throw new IllegalArgumentException("No exception kind for relationship type " + relationshipTypeName);
        }
    }


    /**
     * The tallies for the reconciliation of one relationship type.
     */
    private static class LinkCounts
    {
        int created        = 0;
        int removed        = 0;
        int supplyChainSet = 0;
        int unproven       = 0;
        int unchecked      = 0;
    }


    /**
     * The tallies for the information supply chains given to the untagged relationships in the layers beneath.
     */
    private static class SupplyChainCounts
    {
        int filledIn      = 0;
        int copiesCreated = 0;
        int copiesRemoved = 0;
    }


    /**
     * The information supply chains found for the untagged lower-layer relationships during a refresh.  A path's
     * supply chain is known when a relationship on it names one, or when the path proves an externally asserted
     * relationship that names one; every untagged relationship on the path is then a candidate for that supply chain.
     * The relationships whose supply chain this connector filled in earlier in the same refresh are remembered too,
     * since the copy of them in the relationships retrieved for the walk is out of date.
     */
    private static class SupplyChainCandidates
    {
        final Map<String, Set<String>> supplyChainsByRelationship = new HashMap<>();
        final Set<String>              setThisRefresh             = new HashSet<>();


        /**
         * Record a supply chain as a candidate for some untagged relationships.
         *
         * @param untaggedEdges the untagged relationships
         * @param iscQualifiedName the supply chain - nothing is recorded if it is null
         */
        void add(Set<LineageEdge> untaggedEdges,
                 String           iscQualifiedName)
        {
            if (iscQualifiedName != null)
            {
                for (LineageEdge untaggedEdge : untaggedEdges)
                {
                    supplyChainsByRelationship.computeIfAbsent(untaggedEdge.relationshipGUID(), guid -> new HashSet<>()).add(iscQualifiedName);
                }
            }
        }
    }


    /**
     * The identity of a copy of an untagged relationship made for one information supply chain.
     *
     * @param typeName type of the relationship
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param iscQualifiedName supply chain of the copy
     */
    private record SupplyChainCopyKey(String typeName,
                                      String end1GUID,
                                      String end2GUID,
                                      String iscQualifiedName)
    {
    }


    /**
     * The tallies for the maintenance of the exceptions.
     */
    private static class ExceptionCounts
    {
        int raised  = 0;
        int updated = 0;
        int cleared = 0;
    }


    /**
     * The tallies for one refresh, reported in the audit log as each step completes.
     */
    private static class RefreshCounts
    {
        int        assetLineageDerived  = 0;
        int        serverLineageDerived = 0;
        LinkCounts assetLineage         = new LinkCounts();
        LinkCounts serverLineage        = new LinkCounts();

        int        products             = 0;
        int        dependenciesDerived  = 0;
        LinkCounts dependencies         = new LinkCounts();
        int        abandonedPaths       = 0;

        ExceptionCounts lineageExceptions = new ExceptionCounts();
        ExceptionCounts productExceptions = new ExceptionCounts();

        SupplyChainCounts supplyChains = new SupplyChainCounts();
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected during start up
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        int configuredDepth = super.getIntConfigurationProperty(DarwinConfigurationProperty.MAX_LINEAGE_DEPTH.getName(),
                                                                connectionBean.getConfigurationProperties());

        /*
         * Zero is the value returned when the property is not configured, and a negative depth is meaningless,
         * so both fall back to the default.
         */
        if (configuredDepth > 0)
        {
            maxLineageDepth = configuredDepth;
        }

        logRecord(methodName,
                  DarwinAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                          integrationContext.getMetadataAccessServer(),
                                                                          integrationContext.getMetadataAccessServerPlatformURLRoot(),
                                                                          Integer.toString(maxLineageDepth)));
    }


    /**
     * Bring the coarse-grained lineage into line with the finer-grained lineage beneath it, working upwards across
     * the boundaries between the layers: data mappings between schema elements to data flows between assets in the
     * data fabric, then lineage in the data fabric to data flows between servers in the system fabric and to
     * dependencies between digital products in the data mesh.  The data fabric's derived data flows are written to
     * the repository before the data fabric is read, so that the layers above see them.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is unable to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        try
        {
            /*
             * The product exception type is needed on every refresh; retrieving (or creating) it first means a failure
             * to do so stops the refresh before anything has been changed.  The other exception types are created
             * when they are first needed.
             */
            this.getExceptionTypeGUID(ExceptionKind.DIGITAL_PRODUCT_DEPENDENCY, true);

            RefreshCounts                                 counts       = new RefreshCounts();
            Map<String, String>                           elementNames = new HashMap<>();
            LineageWalker                                 walker       = new LineageWalker(maxLineageDepth);
            Map<ExceptionKind, Map<String, List<String>>> unproven     = new HashMap<>();
            SupplyChainCandidates                         candidates   = new SupplyChainCandidates();

            /*
             * Schema detail up to the data fabric.  The data flows are written before the data fabric is read, so
             * that the layers above see them.  The coverage is the data fabric elements whose schema detail is
             * catalogued - only a relationship between two of them can be shown to be unproven.
             */
            Map<String, List<OpenMetadataRelationship>> schemaDetail       = new HashMap<>();
            Set<String>                                 dataFabricCoverage = new HashSet<>();
            LineageDerivation                           assetLineage       = this.deriveAssetLineage(walker, schemaDetail, dataFabricCoverage, elementNames, counts);

            this.addUnproven(unproven,
                             LinkKind.ASSET_LINEAGE,
                             this.reconcileLinks(LinkKind.ASSET_LINEAGE, assetLineage, dataFabricCoverage, candidates, elementNames, counts.assetLineage));

            /*
             * The data fabric is read once - after the data flows above are written - and serves both of the layers
             * that sit on it.  Each boundary builds its graph from the relationship types it follows.
             */
            Map<String, List<OpenMetadataRelationship>> dataFabric = new HashMap<>();

            /*
             * Data fabric up to the system fabric.  One boundary proves three relationship types, and each is
             * reconciled on its own - including the types for which nothing was derived, so that this connector's
             * relationships of that type that the lineage no longer supports are removed.
             */
            Set<String>       systemFabricCoverage = new HashSet<>();
            LineageDerivation systemLineage        = this.deriveSystemLineage(walker, dataFabric, systemFabricCoverage, elementNames, counts);

            for (LinkKind kind : LinkKind.values())
            {
                if (kind.boundary == LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC)
                {
                    this.addUnproven(unproven,
                                     kind,
                                     this.reconcileLinks(kind,
                                                         systemLineage,
                                                         systemFabricCoverage,
                                                         candidates,
                                                         elementNames,
                                                         counts.serverLineage));
                }
            }

            /*
             * Every lineage exception type is maintained, even with nothing unproven, so that cleared exceptions go.
             */
            for (ExceptionKind exceptionKind : ExceptionKind.values())
            {
                if (exceptionKind != ExceptionKind.DIGITAL_PRODUCT_DEPENDENCY)
                {
                    this.maintainExceptions(exceptionKind,
                                            unproven.getOrDefault(exceptionKind, new HashMap<>()),
                                            elementNames,
                                            counts.lineageExceptions);
                }
            }

            logRecord(methodName,
                      DarwinAuditCode.LINEAGE_REFRESH_COMPLETE.getMessageDefinition(connectorName,
                                                                                    Integer.toString(counts.assetLineageDerived),
                                                                                    Integer.toString(counts.serverLineageDerived),
                                                                                    Integer.toString(counts.assetLineage.created + counts.serverLineage.created),
                                                                                    Integer.toString(counts.assetLineage.removed + counts.serverLineage.removed),
                                                                                    Integer.toString(counts.assetLineage.supplyChainSet + counts.serverLineage.supplyChainSet),
                                                                                    Integer.toString(counts.assetLineage.unproven + counts.serverLineage.unproven),
                                                                                    Integer.toString(counts.assetLineage.unchecked + counts.serverLineage.unchecked),
                                                                                    Integer.toString(counts.lineageExceptions.raised),
                                                                                    Integer.toString(counts.lineageExceptions.updated),
                                                                                    Integer.toString(counts.lineageExceptions.cleared)));

            /*
             * Data fabric up to the data mesh.  The coverage is the products with at least one asset among their members.
             */
            Map<String, Set<String>> productsByMember = new HashMap<>();
            Set<String>              meshCoverage     = new HashSet<>();

            this.indexProductMembers(productsByMember, elementNames, counts);

            for (Set<String> products : productsByMember.values())
            {
                meshCoverage.addAll(products);
            }

            LineageDerivation derivedDependencies = this.deriveDependencies(walker, dataFabric, productsByMember, counts);

            Map<String, List<String>> unprovenByProduct = this.reconcileLinks(LinkKind.PRODUCT_DEPENDENCY,
                                                                              derivedDependencies,
                                                                              meshCoverage,
                                                                              candidates,
                                                                              elementNames,
                                                                              counts.dependencies);

            this.maintainExceptions(ExceptionKind.DIGITAL_PRODUCT_DEPENDENCY, unprovenByProduct, elementNames, counts.productExceptions);

            /*
             * Finally, the untagged relationships on the paths whose supply chain is now known are given it - filled
             * in where only one supply chain is known for them, and copied for each supply chain where there are
             * several.
             */
            this.maintainSupplyChains(candidates, List.of(schemaDetail, dataFabric), elementNames, counts.supplyChains);

            logRecord(methodName,
                      DarwinAuditCode.SUPPLY_CHAIN_REFRESH_COMPLETE.getMessageDefinition(connectorName,
                                                                                         Integer.toString(counts.supplyChains.filledIn),
                                                                                         Integer.toString(counts.supplyChains.copiesCreated),
                                                                                         Integer.toString(counts.supplyChains.copiesRemoved)));

            counts.abandonedPaths = walker.getAbandonedPaths();

            if (counts.abandonedPaths > 0)
            {
                logRecord(methodName,
                          DarwinAuditCode.LINEAGE_DEPTH_EXCEEDED.getMessageDefinition(connectorName,
                                                                                      Integer.toString(counts.abandonedPaths),
                                                                                      Integer.toString(maxLineageDepth)));
            }

            logRecord(methodName,
                      DarwinAuditCode.REFRESH_COMPLETE.getMessageDefinition(connectorName,
                                                                            Integer.toString(counts.products),
                                                                            Integer.toString(counts.dependenciesDerived),
                                                                            Integer.toString(counts.dependencies.created),
                                                                            Integer.toString(counts.dependencies.removed),
                                                                            Integer.toString(counts.dependencies.supplyChainSet),
                                                                            Integer.toString(counts.dependencies.unproven),
                                                                            Integer.toString(counts.dependencies.unchecked),
                                                                            Integer.toString(counts.productExceptions.raised),
                                                                            Integer.toString(counts.productExceptions.updated),
                                                                            Integer.toString(counts.productExceptions.cleared)));
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               DarwinAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                         error.getClass().getName(),
                                                                                         methodName,
                                                                                         error.getMessage()),
                               error);

            throw new ConnectorCheckedException(DarwinErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /* ==============================================================================
     * Schema detail to data fabric - the data flows between data assets implied by the data mappings between their
     * schema elements.
     */


    /**
     * Derive a data flow between two data fabric elements for every DataMapping relationship whose ends belong to
     * different elements.  The mapping's source is the supplier and its target the consumer.  A schema attribute
     * belongs to the data asset it is anchored to; a data asset or a process at the end of a mapping is its own owner.
     *
     * @param walker walker for this refresh
     * @param schemaDetail the relationships of the schema detail retrieved on this refresh, by type - added to
     * @param coverage the data fabric elements whose schema detail takes part in a data mapping - added to
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @param counts tallies for the refresh
     * @return the data flows the mappings support
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private LineageDerivation deriveAssetLineage(LineageWalker                               walker,
                                                 Map<String, List<OpenMetadataRelationship>> schemaDetail,
                                                 Set<String>                                 coverage,
                                                 Map<String, String>                         elementNames,
                                                 RefreshCounts                               counts) throws Exception
    {
        LineageBoundary     boundary     = LinkKind.ASSET_LINEAGE.boundary;
        LineageGraph        mappings     = this.loadLineageGraph(boundary, schemaDetail);
        Map<String, String> owningAssets = new HashMap<>();
        LineageOwners       owners       = elementGUID -> this.getOwningDataAssets(elementGUID, owningAssets, elementNames);

        /*
         * Both ends of every mapping are covered, whether or not the mapping proves anything.
         */
        for (String supplierGUID : mappings.getSupplierGUIDs())
        {
            coverage.addAll(owners.getOwners(supplierGUID));

            for (LineageEdge mapping : mappings.getDownstreamEdges(supplierGUID))
            {
                coverage.addAll(owners.getOwners(mapping.consumerGUID()));
            }
        }

        LineageDerivation assetLineage = walker.deriveLinks(boundary, mappings, owners, mappings.getSupplierGUIDs());

        counts.assetLineageDerived = assetLineage.getLinks(LinkKind.ASSET_LINEAGE.typeName).size();

        return assetLineage;
    }


    /**
     * The owner function for schema detail: the data asset or process an element belongs to, as a set.
     *
     * @param elementGUID element at one end of a data mapping
     * @param owningAssets cache of answers - added to
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @return the owning data asset or process, or an empty set if there is none
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private Set<String> getOwningDataAssets(String              elementGUID,
                                            Map<String, String> owningAssets,
                                            Map<String, String> elementNames) throws Exception
    {
        String owningAssetGUID = this.getOwningDataAssetGUID(elementGUID, owningAssets, elementNames);

        if (owningAssetGUID == null)
        {
            return Set.of();
        }

        return Set.of(owningAssetGUID);
    }


    /**
     * Return the data fabric element that an element at the end of a data mapping belongs to: the element itself if
     * it is a data asset or a process, otherwise the element it is anchored to if that is a data asset, otherwise
     * null.  The answer is cached because a data asset's schema elements typically appear in many mappings.
     *
     * @param elementGUID element at one end of a data mapping
     * @param owningAssets cache of answers - added to
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @return unique identifier of the owning data asset or process, or null
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private String getOwningDataAssetGUID(String              elementGUID,
                                          Map<String, String> owningAssets,
                                          Map<String, String> elementNames) throws Exception
    {
        final String methodName = "getOwningDataAssetGUID";

        if (owningAssets.containsKey(elementGUID))
        {
            return owningAssets.get(elementGUID);
        }

        String              owningAssetGUID = null;
        OpenMetadataElement element         = this.getLineageElementByGUID(elementGUID);

        if (element != null)
        {
            if ((propertyHelper.isTypeOf(element, OpenMetadataType.DATA_ASSET.typeName)) ||
                    (propertyHelper.isTypeOf(element, OpenMetadataType.PROCESS.typeName)))
            {
                owningAssetGUID = elementGUID;

                this.recordName(element, elementNames);
            }
            else
            {
                AttachedClassification anchors = propertyHelper.getClassification(element, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

                if (anchors != null)
                {
                    String anchorGUID = propertyHelper.getStringProperty(connectorName,
                                                                         OpenMetadataProperty.ANCHOR_GUID.name,
                                                                         anchors.getClassificationProperties(),
                                                                         methodName);

                    if ((anchorGUID != null) && (! anchorGUID.equals(elementGUID)))
                    {
                        OpenMetadataElement anchor = this.getLineageElementByGUID(anchorGUID);

                        if ((anchor != null) && (propertyHelper.isTypeOf(anchor, OpenMetadataType.DATA_ASSET.typeName)))
                        {
                            owningAssetGUID = anchorGUID;

                            this.recordName(anchor, elementNames);
                        }
                    }
                }
            }
        }

        owningAssets.put(elementGUID, owningAssetGUID);

        return owningAssetGUID;
    }


    /* ==============================================================================
     * Data fabric to system fabric - the data flows, process calls and control flows between items of IT
     * infrastructure implied by the lineage between the data assets and processes that their capabilities use.
     */


    /**
     * Derive the relationships between items of IT infrastructure wherever the lineage leads from a data fabric
     * element used by one item's software capabilities to an element used by another's.  A path may pass through any
     * number of intermediate elements, keeps to one information supply chain, and stops at the first element used by
     * any item of infrastructure.  Data lineage proves a DataFlow - or a ProcessCall, where a ProcessCall reaches the
     * element - and ControlFlow forms paths of its own that prove a ControlFlow.
     *
     * @param walker walker for this refresh
     * @param dataFabric the relationships of the data fabric retrieved so far on this refresh, by type - added to
     * @param coverage the items of infrastructure whose capabilities use at least one data fabric element - added to
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @param counts tallies for the refresh
     * @return the relationships between infrastructure that the lineage supports
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private LineageDerivation deriveSystemLineage(LineageWalker                               walker,
                                                  Map<String, List<OpenMetadataRelationship>> dataFabric,
                                                  Set<String>                                 coverage,
                                                  Map<String, String>                         elementNames,
                                                  RefreshCounts                               counts) throws Exception
    {
        LineageBoundary          boundary                    = LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC;
        Map<String, Set<String>> infrastructureByUsedElement = this.indexInfrastructureUse(elementNames);

        for (Set<String> infrastructure : infrastructureByUsedElement.values())
        {
            coverage.addAll(infrastructure);
        }

        LineageDerivation systemLineage = walker.deriveLinks(boundary,
                                                             this.loadLineageGraph(boundary, dataFabric),
                                                             elementGUID -> infrastructureByUsedElement.getOrDefault(elementGUID, Set.of()),
                                                             infrastructureByUsedElement.keySet());

        for (String typeName : systemLineage.getLinkTypeNames())
        {
            counts.serverLineageDerived = counts.serverLineageDerived + systemLineage.getLinks(typeName).size();
        }

        return systemLineage;
    }


    /**
     * Build the index from each data fabric element to the items of IT infrastructure whose software capabilities use
     * it.  An item of infrastructure is at end 1 of SupportedSoftwareCapability with its capabilities at end 2; a
     * capability is at end 1 of CapabilityAssetUse with the assets at end 2.  A data asset counts when the useType is
     * OWNS.  A process counts whatever the useType, since the capability that runs a process is rarely recorded as
     * owning it.
     *
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @return map from data fabric element GUID to the GUIDs of the infrastructure that uses it
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private Map<String, Set<String>> indexInfrastructureUse(Map<String, String> elementNames) throws Exception
    {
        final String methodName = "indexInfrastructureUse";

        Map<String, Set<String>> infrastructureByUsedElement = new HashMap<>();

        for (OpenMetadataElement infrastructure : this.getAllElementsOfType(OpenMetadataType.IT_INFRASTRUCTURE.typeName))
        {
            String infrastructureGUID = infrastructure.getElementGUID();

            this.recordName(infrastructure, elementNames);

            for (RelatedMetadataElement capability : this.getAllRelatedElements(infrastructureGUID,
                                                                                 1,
                                                                                 OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                                 false))
            {
                if (capability.getElement() == null)
                {
                    continue;
                }

                for (RelatedMetadataElement assetUse : this.getAllRelatedElements(capability.getElement().getElementGUID(),
                                                                                   1,
                                                                                   OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                                                   false))
                {
                    if (assetUse.getElement() == null)
                    {
                        continue;
                    }

                    boolean isUsed = false;

                    if (propertyHelper.isTypeOf(assetUse.getElement(), OpenMetadataType.PROCESS.typeName))
                    {
                        isUsed = true;
                    }
                    else if ((propertyHelper.isTypeOf(assetUse.getElement(), OpenMetadataType.DATA_ASSET.typeName)) &&
                            (CapabilityAssetUseType.OWNS.name().equals(propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                                                                                  OpenMetadataProperty.USE_TYPE.name,
                                                                                                                  assetUse.getRelationshipProperties(),
                                                                                                                  methodName))))
                    {
                        isUsed = true;
                    }

                    if (isUsed)
                    {
                        this.recordName(assetUse.getElement(), elementNames);

                        infrastructureByUsedElement.computeIfAbsent(assetUse.getElement().getElementGUID(), guid -> new HashSet<>()).add(infrastructureGUID);
                    }
                }
            }
        }

        return infrastructureByUsedElement;
    }


    /* ==============================================================================
     * Data fabric to data mesh - the dependencies between digital products implied by the lineage between their
     * assets.
     */


    /**
     * Build the index from each asset to the digital products it is a member of.  An asset may be a member of more
     * than one product.  Only the assets among a product's members are indexed: the lineage that this connector
     * follows is the data lineage between assets.
     *
     * @param productsByMember map from asset GUID to the GUIDs of the products it belongs to - filled in
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @param counts tallies for the refresh
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private void indexProductMembers(Map<String, Set<String>> productsByMember,
                                     Map<String, String>      elementNames,
                                     RefreshCounts            counts) throws Exception
    {
        for (OpenMetadataElement product : this.getAllElementsOfType(OpenMetadataType.DIGITAL_PRODUCT.typeName))
        {
            counts.products++;

            String productGUID = product.getElementGUID();

            this.recordName(product, elementNames);

            /*
             * The product is at end 1 of CollectionMembership; the members are at end 2.
             */
            for (RelatedMetadataElement member : this.getAllRelatedElements(productGUID,
                                                                             1,
                                                                             OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                             false))
            {
                if ((member.getElement() != null) && (propertyHelper.isTypeOf(member.getElement(), OpenMetadataType.ASSET.typeName)))
                {
                    productsByMember.computeIfAbsent(member.getElement().getElementGUID(), guid -> new HashSet<>()).add(productGUID);
                }
            }
        }
    }


    /**
     * Derive the dependencies between products from the data lineage downstream of each product's assets.  A path may
     * pass through any number of intermediate elements - processes, or assets that belong to no product - but keeps
     * to one information supply chain.  When it reaches an asset of another product, that product depends on the
     * product the path started from, and the path stops: the dependency on anything further downstream belongs to
     * the product just reached.
     *
     * @param walker walker for this refresh
     * @param dataFabric the relationships of the data fabric retrieved so far on this refresh, by type - added to
     * @param productsByMember map from asset GUID to the GUIDs of the products it belongs to
     * @param counts tallies for the refresh
     * @return the dependencies the lineage supports
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private LineageDerivation deriveDependencies(LineageWalker                               walker,
                                                 Map<String, List<OpenMetadataRelationship>> dataFabric,
                                                 Map<String, Set<String>>                    productsByMember,
                                                 RefreshCounts                               counts) throws Exception
    {
        LineageBoundary boundary = LinkKind.PRODUCT_DEPENDENCY.boundary;

        LineageDerivation derivedDependencies = walker.deriveLinks(boundary,
                                                                   this.loadLineageGraph(boundary, dataFabric),
                                                                   elementGUID -> productsByMember.getOrDefault(elementGUID, Set.of()),
                                                                   productsByMember.keySet());

        counts.dependenciesDerived = derivedDependencies.getLinks(LinkKind.PRODUCT_DEPENDENCY.typeName).size();

        return derivedDependencies;
    }


    /* ==============================================================================
     * Loading the lineage of a layer.
     */


    /**
     * Load the lineage of the lower layer of a boundary: every relationship of the boundary's lower-layer types
     * (and their subtypes), oriented from supplier to consumer; the DataMapping relationships between a table and a
     * process, where the boundary follows them; and the equivalences the boundary follows.  Two boundaries share the data fabric, so the
     * relationships retrieved for one are kept for the other in the supplied map.
     *
     * @param boundary the boundary whose lower layer is wanted
     * @param retrievedRelationships the relationships retrieved so far for this layer on this refresh, by the type
     *                               they were retrieved for - added to
     * @return graph of the layer's lineage
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private LineageGraph loadLineageGraph(LineageBoundary                             boundary,
                                          Map<String, List<OpenMetadataRelationship>> retrievedRelationships) throws Exception
    {
        final String methodName = "loadLineageGraph";

        LineageGraph graph = new LineageGraph();

        for (String relationshipTypeName : boundary.lowerLayerRelationshipTypeNames)
        {
            List<OpenMetadataRelationship> relationships = retrievedRelationships.get(relationshipTypeName);

            if (relationships == null)
            {
                relationships = this.getAllRelationshipsOfType(relationshipTypeName, true);

                retrievedRelationships.put(relationshipTypeName, relationships);
            }

            for (OpenMetadataRelationship relationship : relationships)
            {
                String iscQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                           OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                           relationship.getRelationshipProperties(),
                                                                           methodName);
                String typeName         = (relationship.getType() == null) ? relationshipTypeName : relationship.getType().getTypeName();

                if (propertyHelper.isTypeOf(relationship, reverseFlowLineageTypes))
                {
                    graph.addEdge(new LineageEdge(relationship.getRelationshipGUID(),
                                                  typeName,
                                                  relationship.getElementGUIDAtEnd2(),
                                                  relationship.getElementGUIDAtEnd1(),
                                                  iscQualifiedName));
                }
                else
                {
                    graph.addEdge(new LineageEdge(relationship.getRelationshipGUID(),
                                                  typeName,
                                                  relationship.getElementGUIDAtEnd1(),
                                                  relationship.getElementGUIDAtEnd2(),
                                                  iscQualifiedName));
                }
            }
        }

        if (boundary.tableMappingRelationshipTypeName != null)
        {
            /*
             * A table's lineage to and from its processes is recorded with DataMapping relationships at the level of
             * the table.  These are followed too, so that a data set over a table connects to it precisely.
             */
            List<OpenMetadataRelationship> mappings = retrievedRelationships.get(boundary.tableMappingRelationshipTypeName);

            if (mappings == null)
            {
                mappings = this.getAllRelationshipsOfType(boundary.tableMappingRelationshipTypeName, true);

                retrievedRelationships.put(boundary.tableMappingRelationshipTypeName, mappings);
            }

            for (OpenMetadataRelationship mapping : mappings)
            {
                if (((propertyHelper.isTypeOf(mapping.getElementAtEnd1(), OpenMetadataType.RELATIONAL_TABLE.typeName)) &&
                             (propertyHelper.isTypeOf(mapping.getElementAtEnd2(), OpenMetadataType.PROCESS.typeName))) ||
                        ((propertyHelper.isTypeOf(mapping.getElementAtEnd1(), OpenMetadataType.PROCESS.typeName)) &&
                                 (propertyHelper.isTypeOf(mapping.getElementAtEnd2(), OpenMetadataType.RELATIONAL_TABLE.typeName))))
                {
                    graph.addEdge(new LineageEdge(mapping.getRelationshipGUID(),
                                                  boundary.tableMappingRelationshipTypeName,
                                                  mapping.getElementGUIDAtEnd1(),
                                                  mapping.getElementGUIDAtEnd2(),
                                                  propertyHelper.getStringProperty(connectorName,
                                                                                   OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                   mapping.getRelationshipProperties(),
                                                                                   methodName)));
                }
            }
        }

        if (boundary.equivalenceRelationshipTypeName != null)
        {
            /*
             * A data set is a view over its content (data set at end 1, content at end 2), so a walk treats the two as
             * the same element - for the supply chain named on the relationship, if it names one.
             */
            List<OpenMetadataRelationship> equivalences = retrievedRelationships.get(boundary.equivalenceRelationshipTypeName);

            if (equivalences == null)
            {
                equivalences = this.getAllRelationshipsOfType(boundary.equivalenceRelationshipTypeName, true);

                retrievedRelationships.put(boundary.equivalenceRelationshipTypeName, equivalences);
            }

            for (OpenMetadataRelationship equivalence : equivalences)
            {
                graph.addEquivalence(equivalence.getElementGUIDAtEnd1(),
                                     equivalence.getElementGUIDAtEnd2(),
                                     propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                      equivalence.getRelationshipProperties(),
                                                                      methodName));
            }
        }

        return graph;
    }


    /* ==============================================================================
     * Reconciliation - bringing the stored relationships of a kind into line with the derived ones.  The same
     * procedure serves every boundary.
     */


    /**
     * Compare the derived relationships of a kind with the relationships of that type in the repository.
     * Relationships asserted by external users are considered first, because they take precedence over this
     * connector's own: an externally asserted relationship that the finer-grained lineage proves claims the derived
     * relationship, and an own relationship for the same one is then redundant and removed.  Own relationships that
     * the finer-grained lineage no longer supports are removed, and derived relationships that nothing records are
     * created.
     * <p>
     * Several kinds share a relationship type - the asset-level and server-level data flows are both DataFlow
     * relationships - so this connector's own relationships are told apart by the derivation each kind records on them.
     * Only this kind's own relationships are reconciled; the relationships asserted by external users are considered
     * by every kind of their type, since any of them may be proved at any level.
     *
     * An external relationship that the finer-grained lineage does not prove is only reported as unproven when it
     * belongs to this kind's layer and the layer beneath is catalogued for both of its ends (both are in the
     * coverage).  Otherwise the absence of finer-grained lineage is no evidence against it, and it is counted as
     * unchecked.
     * <p>
     * A path whose relationships name no supply chain has an unknown supply chain, so it proves an external
     * relationship that names one as well as one that does not.  The supply chain of such a path is then known from
     * the external relationship, and the untagged relationships on it become candidates for it - as do the untagged
     * relationships on every path whose supply chain is known from a tagged relationship on it.
     *
     * @param kind the kind of relationship, which gives the type name, derivation and the wording for the audit log
     * @param derivation the relationships derived across the kind's boundary, with the untagged relationships on their paths
     * @param coverage the upper-layer elements for which the layer beneath is catalogued
     * @param candidates the supply chains found for untagged lower-layer relationships - added to
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for this kind
     * @return the unproven externally asserted relationships, keyed by the GUID of the element that depends on them
     *
     * @throws Exception a retrieval or update failed - reported by the caller
     */
    private Map<String, List<String>> reconcileLinks(LinkKind              kind,
                                                     LineageDerivation     derivation,
                                                     Set<String>           coverage,
                                                     SupplyChainCandidates candidates,
                                                     Map<String, String>   elementNames,
                                                     LinkCounts            counts) throws Exception
    {
        final String methodName = "reconcileLinks";

        Set<LinkKey> derivedLinks = derivation.getLinks(kind.typeName);

        for (LinkKey derivedLink : derivedLinks)
        {
            candidates.add(derivation.getUntaggedEdges(kind.typeName, derivedLink), derivedLink.iscQualifiedName());
        }

        String myUserId = integrationContext.getMyUserId();

        Set<LinkKey>                   claimed           = new HashSet<>();
        List<OpenMetadataRelationship> ownRelationships  = new ArrayList<>();
        Map<String, List<String>>      unprovenByElement = new HashMap<>();

        /*
         * Pass one - the relationships asserted by external users.
         */
        for (OpenMetadataRelationship relationship : this.getAllRelationshipsOfType(kind.typeName, kind != LinkKind.PRODUCT_DEPENDENCY))
        {
            if (this.isMaintainedByMe(relationship, myUserId))
            {
                if (this.hasDerivation(relationship, kind.derivation, kind.legacyLabel))
                {
                    ownRelationships.add(relationship);
                }

                continue;
            }

            LinkKey key = this.getLinkKey(relationship, methodName);

            if (key.iscQualifiedName() == null)
            {
                /*
                 * The external user did not say which information supply chain the relationship belongs to.  If the
                 * finer-grained lineage proves it through any supply chain, the first one is filled in.  A derived
                 * relationship that no other relationship has claimed yet is preferred, so that the external
                 * relationship stands in for a relationship this connector would otherwise create.
                 */
                LinkKey match = this.findDerivedLinkForPair(derivedLinks, claimed, key);

                if (match == null)
                {
                    this.recordUnproven(kind, coverage, unprovenByElement, relationship, counts);
                }
                else
                {
                    claimed.add(match);

                    if (match.iscQualifiedName() != null)
                    {
                        this.setSupplyChain(kind, relationship, match.iscQualifiedName(), elementNames, counts);

                        candidates.setThisRefresh.add(relationship.getRelationshipGUID());
                    }
                }
            }
            else if (derivedLinks.contains(key))
            {
                claimed.add(key);
            }
            else
            {
                /*
                 * A path of unknown supply chain between the same elements proves it too, and its supply chain is
                 * now known.
                 */
                LinkKey unknownSupplyChainKey = new LinkKey(key.end1GUID(), key.end2GUID(), null);

                if (derivedLinks.contains(unknownSupplyChainKey))
                {
                    claimed.add(unknownSupplyChainKey);
                    claimed.add(key);

                    candidates.add(derivation.getUntaggedEdges(kind.typeName, unknownSupplyChainKey), key.iscQualifiedName());
                }
                else
                {
                    this.recordUnproven(kind, coverage, unprovenByElement, relationship, counts);
                }
            }
        }

        /*
         * Pass two - the relationships this connector created on earlier refreshes.  One is kept if the finer-grained
         * lineage still supports it and nothing else has claimed it; otherwise it goes.
         */
        for (OpenMetadataRelationship relationship : ownRelationships)
        {
            LinkKey key = this.getLinkKey(relationship, methodName);

            if ((! derivedLinks.contains(key)) || (this.isSubsumed(key, derivedLinks, claimed)) || (! claimed.add(key)))
            {
                this.removeLink(kind, relationship, key, elementNames, counts);
            }
            else if (this.getDerivation(relationship) == null)
            {
                /*
                 * Created by an earlier release, which recorded the derivation in the label.
                 */
                this.moveToDerivation(relationship, key.iscQualifiedName(), kind.derivation, kind.description, null);
            }
        }

        /*
         * Pass three - the relationships that nothing records.
         */
        for (LinkKey key : this.sortForDeterminism(derivedLinks))
        {
            if ((! claimed.contains(key)) && (! this.isSubsumed(key, derivedLinks, claimed)))
            {
                this.createLink(kind, key, elementNames, counts);
            }
        }

        return unprovenByElement;
    }


    /**
     * Is a relationship of unknown supply chain made redundant by one of known supply chain between the same two
     * elements?  A path whose relationships name no supply chain could belong to any supply chain, so where the same
     * elements are also linked for a known supply chain - by a derived relationship or by an external one - an
     * untagged relationship between them adds nothing.
     *
     * @param key the relationship to test
     * @param derivedLinks the relationships the finer-grained lineage supports
     * @param claimed the derived relationships recorded by external relationships, and the external relationships' keys
     * @return true if the relationship is untagged and a tagged one links the same elements
     */
    private boolean isSubsumed(LinkKey      key,
                               Set<LinkKey> derivedLinks,
                               Set<LinkKey> claimed)
    {
        if (key.iscQualifiedName() != null)
        {
            return false;
        }

        for (LinkKey other : derivedLinks)
        {
            if ((other.iscQualifiedName() != null) && (other.end1GUID().equals(key.end1GUID())) && (other.end2GUID().equals(key.end2GUID())))
            {
                return true;
            }
        }

        for (LinkKey other : claimed)
        {
            if ((other.iscQualifiedName() != null) && (other.end1GUID().equals(key.end1GUID())) && (other.end2GUID().equals(key.end2GUID())))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Find a derived relationship between the same two elements as the supplied key, whatever its information
     * supply chain.  One that no relationship has claimed is preferred; failing that, any.  The candidates are
     * considered in a fixed order so that the choice is the same on every refresh.
     *
     * @param derivedLinks the relationships the finer-grained lineage supports
     * @param claimed the derived relationships already recorded by a relationship
     * @param key the elements to match on
     * @return matching derived relationship or null
     */
    private LinkKey findDerivedLinkForPair(Set<LinkKey> derivedLinks,
                                           Set<LinkKey> claimed,
                                           LinkKey      key)
    {
        LinkKey firstMatch = null;

        for (LinkKey candidate : this.sortForDeterminism(derivedLinks))
        {
            if ((candidate.end1GUID().equals(key.end1GUID())) && (candidate.end2GUID().equals(key.end2GUID())))
            {
                if (! claimed.contains(candidate))
                {
                    return candidate;
                }

                if (firstMatch == null)
                {
                    firstMatch = candidate;
                }
            }
        }

        return firstMatch;
    }


    /**
     * Order the derived relationships so that, where the same choice comes up on successive refreshes, the same
     * answer is given.  Null supply chains sort last.
     *
     * @param derivedLinks set to order
     * @return ordered list
     */
    private List<LinkKey> sortForDeterminism(Set<LinkKey> derivedLinks)
    {
        List<LinkKey> ordered = new ArrayList<>(derivedLinks);

        ordered.sort(Comparator.comparing(LinkKey::end1GUID)
                               .thenComparing(LinkKey::end2GUID)
                               .thenComparing(LinkKey::iscQualifiedName, Comparator.nullsLast(Comparator.naturalOrder())));

        return ordered;
    }


    /**
     * Deal with an externally asserted relationship that the finer-grained lineage does not prove.  A relationship
     * whose ends are not both elements of this kind's layer belongs to another layer (or straddles two), and is left
     * to that layer.  Otherwise it is unproven if the layer beneath is catalogued for both ends, and is added to the
     * list for the element that depends on it - the element the data or control reaches, which is end 2 of a lineage
     * relationship and end 1 (the dependent product) of a DigitalProductDependency.  If the layer beneath is not
     * catalogued for both ends, the relationship is unchecked.
     *
     * @param kind the kind of relationship
     * @param coverage the upper-layer elements for which the layer beneath is catalogued
     * @param unprovenByElement unproven relationships keyed by the GUID of the element that depends on them - added to
     * @param relationship the relationship the lineage does not prove
     * @param counts tallies for this kind
     */
    private void recordUnproven(LinkKind                  kind,
                                Set<String>               coverage,
                                Map<String, List<String>> unprovenByElement,
                                OpenMetadataRelationship  relationship,
                                LinkCounts                counts)
    {
        if ((! propertyHelper.isTypeOf(relationship.getElementAtEnd1(), kind.boundary.upperLayerElementTypeNames)) ||
                (! propertyHelper.isTypeOf(relationship.getElementAtEnd2(), kind.boundary.upperLayerElementTypeNames)))
        {
            return;
        }

        if ((coverage.contains(relationship.getElementGUIDAtEnd1())) && (coverage.contains(relationship.getElementGUIDAtEnd2())))
        {
            counts.unproven++;

            String dependentElementGUID = kind.boundary.consumerAtEnd1 ? relationship.getElementGUIDAtEnd1() : relationship.getElementGUIDAtEnd2();

            unprovenByElement.computeIfAbsent(dependentElementGUID, guid -> new ArrayList<>()).add(relationship.getRelationshipGUID());
        }
        else
        {
            counts.unchecked++;
        }
    }


    /**
     * Add the unproven relationships of one kind to those found so far for its exception kind.  Several kinds share
     * an exception kind - the data flows in the data fabric and in the system fabric - and their lists are merged.
     *
     * @param unproven unproven relationships found so far, by exception kind and then by dependent element - added to
     * @param kind the kind of relationship the new list is for
     * @param unprovenByElement unproven relationships of that kind keyed by dependent element
     */
    private void addUnproven(Map<ExceptionKind, Map<String, List<String>>> unproven,
                             LinkKind                                      kind,
                             Map<String, List<String>>                     unprovenByElement)
    {
        Map<String, List<String>> unprovenForExceptionKind = unproven.computeIfAbsent(ExceptionKind.forRelationshipType(kind.typeName),
                                                                                       exceptionKind -> new HashMap<>());

        for (Map.Entry<String, List<String>> entry : unprovenByElement.entrySet())
        {
            unprovenForExceptionKind.computeIfAbsent(entry.getKey(), guid -> new ArrayList<>()).addAll(entry.getValue());
        }
    }


    /**
     * Extract the identity of a relationship.
     *
     * @param relationship relationship from the repository
     * @param methodName calling method, for error reporting
     * @return key
     */
    private LinkKey getLinkKey(OpenMetadataRelationship relationship,
                               String                   methodName)
    {
        return new LinkKey(relationship.getElementGUIDAtEnd1(),
                           relationship.getElementGUIDAtEnd2(),
                           propertyHelper.getStringProperty(connectorName,
                                                            OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                            relationship.getRelationshipProperties(),
                                                            methodName));
    }


    /**
     * Was this relationship created by this connector?  The connector runs under its own userId, which is recorded
     * in the createdBy of every relationship it creates, so that is how its own work is told apart from the
     * assertions of external users.
     *
     * @param relationship relationship to test
     * @param myUserId this connector's userId
     * @return true if this connector created it
     */
    private boolean isCreatedByMe(ElementControlHeader relationship,
                                  String               myUserId)
    {
        return (relationship.getVersions() != null) && (myUserId.equals(relationship.getVersions().getCreatedBy()));
    }


    /**
     * Is this one of the relationships this connector maintains?  It must have been created by this connector, and
     * not updated since by anyone else.  A relationship that someone else has updated has been adopted: it is treated
     * from then on as asserted by an external user - this connector no longer updates or removes it, and validates it
     * against the lineage beneath it like any other external relationship.  This protects a person's edits from being
     * lost when the lineage that the relationship was derived from changes.
     *
     * @param relationship relationship to test
     * @param myUserId this connector's userId
     * @return true if this connector maintains it
     */
    private boolean isMaintainedByMe(ElementControlHeader relationship,
                                     String               myUserId)
    {
        if (! this.isCreatedByMe(relationship, myUserId))
        {
            return false;
        }

        String updatedBy = relationship.getVersions().getUpdatedBy();

        return (updatedBy == null) || (myUserId.equals(updatedBy));
    }


    /**
     * Fill in the information supply chain of an externally asserted relationship that did not name one.
     *
     * @param kind the kind of relationship, for the audit log
     * @param relationship relationship to update
     * @param iscQualifiedName supply chain to set
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for this kind
     *
     * @throws Exception the update failed - reported by the caller
     */
    private void setSupplyChain(LinkKind                 kind,
                                OpenMetadataRelationship relationship,
                                String                   iscQualifiedName,
                                Map<String, String>      elementNames,
                                LinkCounts               counts) throws Exception
    {
        final String methodName = "setSupplyChain";

        this.updateSupplyChain(relationship, iscQualifiedName);

        counts.supplyChainSet++;

        String assertedBy = null;

        if (relationship.getVersions() != null)
        {
            assertedBy = relationship.getVersions().getCreatedBy();
        }

        if (kind == LinkKind.PRODUCT_DEPENDENCY)
        {
            logRecord(methodName,
                      DarwinAuditCode.SUPPLY_CHAIN_SET.getMessageDefinition(connectorName,
                                                                            relationship.getRelationshipGUID(),
                                                                            this.getElementName(relationship.getElementGUIDAtEnd1(), elementNames),
                                                                            this.getElementName(relationship.getElementGUIDAtEnd2(), elementNames),
                                                                            assertedBy,
                                                                            iscQualifiedName));
        }
        else
        {
            logRecord(methodName,
                      DarwinAuditCode.LINEAGE_SUPPLY_CHAIN_SET.getMessageDefinition(connectorName,
                                                                                    kind.typeName,
                                                                                    relationship.getRelationshipGUID(),
                                                                                    this.getElementName(relationship.getElementGUIDAtEnd1(), elementNames),
                                                                                    this.getElementName(relationship.getElementGUIDAtEnd2(), elementNames),
                                                                                    assertedBy,
                                                                                    iscQualifiedName));
        }
    }


    /**
     * Set the information supply chain on a relationship - on behalf of its home metadata collection if this
     * repository does not own it.
     *
     * @param relationship relationship to update
     * @param iscQualifiedName supply chain to set
     *
     * @throws Exception the update failed - reported by the caller
     */
    private void updateSupplyChain(OpenMetadataRelationship relationship,
                                   String                   iscQualifiedName) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                        iscQualifiedName);

        UpdateOptions updateOptions = openMetadataStore.getUpdateOptions(true);

        this.setOwningMetadataCollection(updateOptions, relationship);

        openMetadataStore.updateRelatedElementsInStore(relationship.getRelationshipGUID(), updateOptions, properties);
    }


    /* ==============================================================================
     * Supply chains for the untagged relationships in the layers beneath.
     */


    /**
     * Give the untagged lower-layer relationships the supply chains found for them on this refresh.  Only the
     * relationships asserted by external users are changed - this connector's own derived relationships take their
     * supply chain from the lineage beneath them, so they follow on a later refresh.
     * <ul>
     *     <li>A relationship with one candidate supply chain has it filled in, on behalf of its home metadata
     *     collection if it is not local.</li>
     *     <li>A relationship with several candidate supply chains is left untagged: the first one found is no more
     *     correct than the others.  Instead, this connector records a copy of it for each supply chain.</li>
     * </ul>
     * The copies are this connector's own relationships, recognized by their derivation, and each carries the label of
     * the relationship it copies.  A copy that someone else has updated has been adopted, and is not touched; nor is a
     * copy made again where someone else's relationship already records it.  A copy that is no longer wanted
     * is removed, and a duplicate of one that is wanted is removed too.
     *
     * @param candidates the supply chains found for untagged lower-layer relationships
     * @param retrievedRelationships the relationships of the lower layers retrieved on this refresh, by type
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for the supply chains
     *
     * @throws Exception a retrieval or update failed - reported by the caller
     */
    private void maintainSupplyChains(SupplyChainCandidates                             candidates,
                                      List<Map<String, List<OpenMetadataRelationship>>> retrievedRelationships,
                                      Map<String, String>                               elementNames,
                                      SupplyChainCounts                                 counts) throws Exception
    {
        final String methodName = "maintainSupplyChains";

        String                                myUserId            = integrationContext.getMyUserId();
        Map<String, OpenMetadataRelationship> relationshipsByGUID = new HashMap<>();
        List<OpenMetadataRelationship>        existingCopies      = new ArrayList<>();

        for (Map<String, List<OpenMetadataRelationship>> layer : retrievedRelationships)
        {
            for (List<OpenMetadataRelationship> relationships : layer.values())
            {
                for (OpenMetadataRelationship relationship : relationships)
                {
                    relationshipsByGUID.put(relationship.getRelationshipGUID(), relationship);
                }
            }
        }

        /*
         * The same relationship can be retrieved for more than one layer - the DataMapping relationships of the schema
         * detail are followed between tables and processes in the data fabric too - so the copies are found among
         * the distinct relationships.
         */
        for (OpenMetadataRelationship relationship : relationshipsByGUID.values())
        {
            if ((this.isMaintainedByMe(relationship, myUserId)) &&
                    (this.hasDerivation(relationship, supplyChainCopyDerivation, supplyChainCopyDerivation)))
            {
                existingCopies.add(relationship);
            }
        }

        /*
         * Each wanted copy carries the label of the relationship it copies.
         */
        Map<SupplyChainCopyKey, String> wantedCopies = new HashMap<>();

        for (Map.Entry<String, Set<String>> candidate : candidates.supplyChainsByRelationship.entrySet())
        {
            OpenMetadataRelationship relationship = relationshipsByGUID.get(candidate.getKey());

            if ((relationship == null) ||
                    (this.getTypeName(relationship) == null) ||
                    (this.isMaintainedByMe(relationship, myUserId)) ||
                    (candidates.setThisRefresh.contains(candidate.getKey())) ||
                    (this.getLinkKey(relationship, methodName).iscQualifiedName() != null))
            {
                continue;
            }

            String typeName = this.getTypeName(relationship);

            if (candidate.getValue().size() == 1)
            {
                String iscQualifiedName = candidate.getValue().iterator().next();

                this.updateSupplyChain(relationship, iscQualifiedName);

                counts.filledIn++;

                logRecord(methodName,
                          DarwinAuditCode.LINEAGE_SUPPLY_CHAIN_SET.getMessageDefinition(connectorName,
                                                                                        typeName,
                                                                                        relationship.getRelationshipGUID(),
                                                                                        this.getElementName(relationship.getElementGUIDAtEnd1(), elementNames),
                                                                                        this.getElementName(relationship.getElementGUIDAtEnd2(), elementNames),
                                                                                        this.getCreatedBy(relationship),
                                                                                        iscQualifiedName));
            }
            else
            {
                String label = propertyHelper.getStringProperty(connectorName,
                                                                OpenMetadataProperty.LABEL.name,
                                                                relationship.getRelationshipProperties(),
                                                                methodName);

                for (String iscQualifiedName : candidate.getValue())
                {
                    wantedCopies.put(new SupplyChainCopyKey(typeName,
                                                            relationship.getElementGUIDAtEnd1(),
                                                            relationship.getElementGUIDAtEnd2(),
                                                            iscQualifiedName),
                                     label);
                }
            }
        }

        /*
         * A copy that someone else's relationship already records - for example a copy of this connector's that a person
         * has since adopted - is not wanted again.
         */
        for (OpenMetadataRelationship relationship : relationshipsByGUID.values())
        {
            String iscQualifiedName = this.getLinkKey(relationship, methodName).iscQualifiedName();

            if ((iscQualifiedName != null) && (! this.isMaintainedByMe(relationship, myUserId)))
            {
                wantedCopies.remove(new SupplyChainCopyKey(this.getTypeName(relationship),
                                                           relationship.getElementGUIDAtEnd1(),
                                                           relationship.getElementGUIDAtEnd2(),
                                                           iscQualifiedName));
            }
        }

        OpenMetadataStore       openMetadataStore = integrationContext.getOpenMetadataStore();
        Set<SupplyChainCopyKey> keptCopies        = new HashSet<>();

        for (OpenMetadataRelationship existingCopy : existingCopies)
        {
            SupplyChainCopyKey copyKey = new SupplyChainCopyKey(this.getTypeName(existingCopy),
                                                                existingCopy.getElementGUIDAtEnd1(),
                                                                existingCopy.getElementGUIDAtEnd2(),
                                                                this.getLinkKey(existingCopy, methodName).iscQualifiedName());

            if ((wantedCopies.containsKey(copyKey)) && (keptCopies.add(copyKey)))
            {
                if (this.getDerivation(existingCopy) == null)
                {
                    /*
                     * Made by an earlier release, which recorded the derivation in the label.
                     */
                    this.moveToDerivation(existingCopy, copyKey.iscQualifiedName(), supplyChainCopyDerivation, supplyChainCopyDescription, wantedCopies.get(copyKey));
                }

                continue;
            }

            openMetadataStore.deleteRelationshipInStore(existingCopy.getRelationshipGUID());

            counts.copiesRemoved++;

            logRecord(methodName,
                      DarwinAuditCode.SUPPLY_CHAIN_COPY_REMOVED.getMessageDefinition(connectorName,
                                                                                     copyKey.typeName(),
                                                                                     existingCopy.getRelationshipGUID(),
                                                                                     this.getElementName(copyKey.end1GUID(), elementNames),
                                                                                     this.getElementName(copyKey.end2GUID(), elementNames),
                                                                                     copyKey.iscQualifiedName()));
        }

        List<SupplyChainCopyKey> copiesToCreate = new ArrayList<>(wantedCopies.keySet());

        copiesToCreate.removeAll(keptCopies);
        copiesToCreate.sort(Comparator.comparing(SupplyChainCopyKey::typeName)
                                      .thenComparing(SupplyChainCopyKey::end1GUID)
                                      .thenComparing(SupplyChainCopyKey::end2GUID)
                                      .thenComparing(SupplyChainCopyKey::iscQualifiedName));

        for (SupplyChainCopyKey copyKey : copiesToCreate)
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                            copyKey.iscQualifiedName());

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.DERIVATION.name,
                                                          supplyChainCopyDerivation);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.LABEL.name,
                                                          wantedCopies.get(copyKey));

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.DESCRIPTION.name,
                                                          supplyChainCopyDescription);

            String copyGUID = openMetadataStore.createRelatedElementsInStore(copyKey.typeName(),
                                                                             copyKey.end1GUID(),
                                                                             copyKey.end2GUID(),
                                                                             new MakeAnchorOptions(openMetadataStore.getMetadataSourceOptions()),
                                                                             openMetadataStore.getNewElementProperties(null, null, properties));

            counts.copiesCreated++;

            logRecord(methodName,
                      DarwinAuditCode.SUPPLY_CHAIN_COPY_CREATED.getMessageDefinition(connectorName,
                                                                                     copyKey.typeName(),
                                                                                     this.getElementName(copyKey.end1GUID(), elementNames),
                                                                                     this.getElementName(copyKey.end2GUID(), elementNames),
                                                                                     copyKey.iscQualifiedName(),
                                                                                     copyGUID));
        }
    }


    /**
     * Was this relationship derived by the named rule?  The rule is recorded in the derivation, or - by earlier
     * releases of this connector, before relationships had a derivation - in the label.
     *
     * @param relationship relationship
     * @param derivation the derivation the rule records
     * @param legacyLabel the label earlier releases recorded for the rule
     * @return boolean
     */
    private boolean hasDerivation(OpenMetadataRelationship relationship,
                                  String                   derivation,
                                  String                   legacyLabel)
    {
        final String methodName = "hasDerivation";

        String relationshipDerivation = this.getDerivation(relationship);

        if (relationshipDerivation != null)
        {
            return derivation.equals(relationshipDerivation);
        }

        return legacyLabel.equals(propertyHelper.getStringProperty(connectorName,
                                                                   OpenMetadataProperty.LABEL.name,
                                                                   relationship.getRelationshipProperties(),
                                                                   methodName));
    }


    /**
     * Return the derivation of a relationship.
     *
     * @param relationship relationship
     * @return derivation - null if it has none
     */
    private String getDerivation(OpenMetadataRelationship relationship)
    {
        final String methodName = "getDerivation";

        return propertyHelper.getStringProperty(connectorName,
                                                OpenMetadataProperty.DERIVATION.name,
                                                relationship.getRelationshipProperties(),
                                                methodName);
    }


    /**
     * Move one of this connector's relationships created by an earlier release - which recorded the rule that derived
     * it in the label - over to the derivation.  The relationship's properties are replaced, so the label is cleared
     * unless a label for the data is supplied.
     *
     * @param relationship relationship to update
     * @param iscQualifiedName its information supply chain
     * @param derivation the derivation to record
     * @param description the description to record
     * @param label the label to record - null for none
     *
     * @throws Exception the update failed - reported by the caller
     */
    private void moveToDerivation(OpenMetadataRelationship relationship,
                                  String                   iscQualifiedName,
                                  String                   derivation,
                                  String                   description,
                                  String                   label) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                        iscQualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DERIVATION.name, derivation);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, description);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.LABEL.name, label);

        openMetadataStore.updateRelatedElementsInStore(relationship.getRelationshipGUID(),
                                                       openMetadataStore.getUpdateOptions(false),
                                                       properties);
    }


    /**
     * Return the type name of a relationship.
     *
     * @param relationship relationship
     * @return type name - null if the relationship carries no type
     */
    private String getTypeName(OpenMetadataRelationship relationship)
    {
        if (relationship.getType() == null)
        {
            return null;
        }

        return relationship.getType().getTypeName();
    }


    /**
     * Return the user that created a relationship.
     *
     * @param relationship relationship
     * @return userId - null if it is not recorded
     */
    private String getCreatedBy(OpenMetadataRelationship relationship)
    {
        if (relationship.getVersions() == null)
        {
            return null;
        }

        return relationship.getVersions().getCreatedBy();
    }


    /**
     * Create a relationship for a derived one that nothing records.
     *
     * @param kind the kind of relationship, which gives the type name and the derivation and description to record on it
     * @param key the relationship
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for this kind
     *
     * @throws Exception the create failed - reported by the caller
     */
    private void createLink(LinkKind            kind,
                            LinkKey             key,
                            Map<String, String> elementNames,
                            LinkCounts          counts) throws Exception
    {
        final String methodName = "createLink";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                        key.iscQualifiedName());

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DERIVATION.name,
                                                      kind.derivation);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      kind.description);

        String relationshipGUID = openMetadataStore.createRelatedElementsInStore(kind.typeName,
                                                                                 key.end1GUID(),
                                                                                 key.end2GUID(),
                                                                                 new MakeAnchorOptions(openMetadataStore.getMetadataSourceOptions()),
                                                                                 openMetadataStore.getNewElementProperties(null, null, properties));

        counts.created++;

        if (kind == LinkKind.PRODUCT_DEPENDENCY)
        {
            logRecord(methodName,
                      DarwinAuditCode.DEPENDENCY_CREATED.getMessageDefinition(connectorName,
                                                                              this.getElementName(key.end1GUID(), elementNames),
                                                                              this.getElementName(key.end2GUID(), elementNames),
                                                                              key.iscQualifiedName(),
                                                                              relationshipGUID));
        }
        else
        {
            logRecord(methodName,
                      DarwinAuditCode.LINEAGE_CREATED.getMessageDefinition(connectorName,
                                                                           kind.typeName,
                                                                           this.getElementName(key.end1GUID(), elementNames),
                                                                           this.getElementName(key.end2GUID(), elementNames),
                                                                           key.iscQualifiedName(),
                                                                           relationshipGUID,
                                                                           kind.reason));
        }
    }


    /**
     * Remove a relationship that this connector created and the finer-grained lineage no longer supports.
     *
     * @param kind the kind of relationship, for the audit log
     * @param relationship relationship to remove
     * @param key its identity
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for this kind
     *
     * @throws Exception the delete failed - reported by the caller
     */
    private void removeLink(LinkKind                 kind,
                            OpenMetadataRelationship relationship,
                            LinkKey                  key,
                            Map<String, String>      elementNames,
                            LinkCounts               counts) throws Exception
    {
        final String methodName = "removeLink";

        integrationContext.getOpenMetadataStore().deleteRelationshipInStore(relationship.getRelationshipGUID());

        counts.removed++;

        if (kind == LinkKind.PRODUCT_DEPENDENCY)
        {
            logRecord(methodName,
                      DarwinAuditCode.DEPENDENCY_REMOVED.getMessageDefinition(connectorName,
                                                                              relationship.getRelationshipGUID(),
                                                                              this.getElementName(key.end1GUID(), elementNames),
                                                                              this.getElementName(key.end2GUID(), elementNames),
                                                                              key.iscQualifiedName()));
        }
        else
        {
            logRecord(methodName,
                      DarwinAuditCode.LINEAGE_REMOVED.getMessageDefinition(connectorName,
                                                                           kind.typeName,
                                                                           relationship.getRelationshipGUID(),
                                                                           this.getElementName(key.end1GUID(), elementNames),
                                                                           this.getElementName(key.end2GUID(), elementNames),
                                                                           key.iscQualifiedName()));
        }
    }


    /* ==============================================================================
     * The exceptions for the externally asserted relationships the lineage beneath them does not prove.
     */


    /**
     * Bring the Exception relationships hanging off one of this connector's exception types into line with the
     * unproven relationships found on this refresh.  There is one Exception relationship per dependent element; its
     * affectedRelationships property lists the element's unproven relationships of the exception type's relationship
     * type.  Only the Exception relationships this connector created are touched.  An exception type that does not
     * exist yet is only created when there is something to record against it.
     *
     * @param exceptionKind the exception type to maintain
     * @param unprovenByElement unproven relationships keyed by dependent element GUID
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for the exceptions
     *
     * @throws Exception a retrieval or update failed - reported by the caller
     */
    private void maintainExceptions(ExceptionKind             exceptionKind,
                                    Map<String, List<String>> unprovenByElement,
                                    Map<String, String>       elementNames,
                                    ExceptionCounts           counts) throws Exception
    {
        final String methodName = "maintainExceptions";

        String exceptionTypeGUID = this.getExceptionTypeGUID(exceptionKind, ! unprovenByElement.isEmpty());

        if (exceptionTypeGUID == null)
        {
            /*
             * No exception type means no exceptions to clear, and nothing to record.
             */
            return;
        }

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        String            myUserId          = integrationContext.getMyUserId();

        /*
         * The exception type is at end 2 of the Exception relationship; the dependent elements are at end 1.
         */
        for (RelatedMetadataElement existingException : this.getAllRelatedElements(exceptionTypeGUID,
                                                                                    2,
                                                                                    OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName,
                                                                                    false))
        {
            if ((existingException.getElement() == null) || (! this.isCreatedByMe(existingException, myUserId)))
            {
                continue;
            }

            String       elementGUID = existingException.getElement().getElementGUID();
            List<String> unproven    = unprovenByElement.remove(elementGUID);

            if (unproven == null)
            {
                openMetadataStore.deleteRelationshipInStore(existingException.getRelationshipGUID());

                counts.cleared++;

                if (exceptionKind == ExceptionKind.DIGITAL_PRODUCT_DEPENDENCY)
                {
                    logRecord(methodName,
                              DarwinAuditCode.EXCEPTION_CLEARED.getMessageDefinition(connectorName,
                                                                                     existingException.getRelationshipGUID(),
                                                                                     this.getElementName(elementGUID, elementNames)));
                }
                else
                {
                    logRecord(methodName,
                              DarwinAuditCode.LINEAGE_EXCEPTION_CLEARED.getMessageDefinition(connectorName,
                                                                                             existingException.getRelationshipGUID(),
                                                                                             this.getElementName(elementGUID, elementNames),
                                                                                             exceptionKind.relationshipTypeName));
                }
            }
            else
            {
                List<String> recorded = propertyHelper.getStringArrayProperty(connectorName,
                                                                              OpenMetadataProperty.AFFECTED_RELATIONSHIPS.name,
                                                                              existingException.getRelationshipProperties(),
                                                                              methodName);

                if ((recorded == null) || (! new HashSet<>(recorded).equals(new HashSet<>(unproven))))
                {
                    ElementProperties properties = propertyHelper.addStringArrayProperty(null,
                                                                                         OpenMetadataProperty.AFFECTED_RELATIONSHIPS.name,
                                                                                         unproven);

                    properties = propertyHelper.addDateProperty(properties,
                                                                OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                                new Date());

                    openMetadataStore.updateRelatedElementsInStore(existingException.getRelationshipGUID(),
                                                                   openMetadataStore.getUpdateOptions(true),
                                                                   properties);

                    counts.updated++;

                    this.logUnproven(exceptionKind, existingException.getRelationshipGUID(), elementGUID, unproven, elementNames);
                }
            }
        }

        /*
         * Whatever is left has no exception yet.
         */
        for (Map.Entry<String, List<String>> newException : unprovenByElement.entrySet())
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.LABEL.name,
                                                                            exceptionKind.exceptionLabel);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.DESCRIPTION.name,
                                                          exceptionKind.exceptionDescription);

            properties = propertyHelper.addStringArrayProperty(properties,
                                                               OpenMetadataProperty.AFFECTED_RELATIONSHIPS.name,
                                                               newException.getValue());

            properties = propertyHelper.addDateProperty(properties,
                                                        OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                        new Date());

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.STEWARD.name,
                                                          myUserId);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                          OpenMetadataType.USER_IDENTITY.typeName);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                          OpenMetadataProperty.USER_ID.name);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.NOTES.name,
                                                          unprovenExceptionNotes);

            String exceptionGUID = openMetadataStore.createRelatedElementsInStore(OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName,
                                                                                  newException.getKey(),
                                                                                  exceptionTypeGUID,
                                                                                  new MakeAnchorOptions(openMetadataStore.getMetadataSourceOptions()),
                                                                                  openMetadataStore.getNewElementProperties(null, null, properties));

            counts.raised++;

            this.logUnproven(exceptionKind, exceptionGUID, newException.getKey(), newException.getValue(), elementNames);
        }
    }


    /**
     * Log that an exception has been recorded, or updated, for unproven relationships.
     *
     * @param exceptionKind the exception type
     * @param exceptionGUID unique identifier of the Exception relationship
     * @param elementGUID the element the exception is recorded against
     * @param unproven the unproven relationships
     * @param elementNames map from element GUID to qualified name, for the audit log
     */
    private void logUnproven(ExceptionKind       exceptionKind,
                             String              exceptionGUID,
                             String              elementGUID,
                             List<String>        unproven,
                             Map<String, String> elementNames)
    {
        final String methodName = "logUnproven";

        if (exceptionKind == ExceptionKind.DIGITAL_PRODUCT_DEPENDENCY)
        {
            logRecord(methodName,
                      DarwinAuditCode.UNPROVEN_DEPENDENCIES.getMessageDefinition(connectorName,
                                                                                 exceptionGUID,
                                                                                 this.getElementName(elementGUID, elementNames),
                                                                                 Integer.toString(unproven.size()),
                                                                                 unproven.toString()));
        }
        else
        {
            logRecord(methodName,
                      DarwinAuditCode.UNPROVEN_LINEAGE.getMessageDefinition(connectorName,
                                                                            exceptionGUID,
                                                                            this.getElementName(elementGUID, elementNames),
                                                                            Integer.toString(unproven.size()),
                                                                            exceptionKind.relationshipTypeName,
                                                                            unproven.toString()));
        }
    }


    /**
     * Return the unique identifier of one of this connector's exception types, creating the exception type if it
     * does not exist and creation is requested.  The exception type is looked up by its qualified name, so an
     * exception type created by an earlier run - or by a different instance of this connector - is reused.
     *
     * @param exceptionKind the exception type wanted
     * @param createIfMissing true to create the exception type if it does not exist
     * @return unique identifier of the exception type, or null if it does not exist and was not created
     *
     * @throws Exception the exception type could not be retrieved or created - reported by the caller
     */
    private synchronized String getExceptionTypeGUID(ExceptionKind exceptionKind,
                                                     boolean       createIfMissing) throws Exception
    {
        final String methodName = "getExceptionTypeGUID";

        String exceptionTypeGUID = exceptionTypeGUIDs.get(exceptionKind);

        if (exceptionTypeGUID != null)
        {
            return exceptionTypeGUID;
        }

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.EXCEPTION_TYPE.typeName);
        QueryOptions               queryOptions               = governanceDefinitionClient.getQueryOptions();

        queryOptions.setGraphQueryDepth(0); // Only the GUID of a matching exception type is used.

        List<OpenMetadataRootElement> existingTypes = governanceDefinitionClient.getGovernanceDefinitionsByName(exceptionKind.qualifiedName, queryOptions);

        if (existingTypes != null)
        {
            for (OpenMetadataRootElement existingType : existingTypes)
            {
                if ((existingType != null) && (existingType.getElementHeader() != null))
                {
                    exceptionTypeGUID = existingType.getElementHeader().getGUID();

                    exceptionTypeGUIDs.put(exceptionKind, exceptionTypeGUID);

                    return exceptionTypeGUID;
                }
            }
        }

        if (! createIfMissing)
        {
            return null;
        }

        ExceptionTypeProperties exceptionTypeProperties = new ExceptionTypeProperties();

        exceptionTypeProperties.setQualifiedName(exceptionKind.qualifiedName);
        exceptionTypeProperties.setIdentifier(exceptionKind.identifier);
        exceptionTypeProperties.setDisplayName(exceptionKind.displayName);
        exceptionTypeProperties.setSummary(exceptionKind.summary);
        exceptionTypeProperties.setDescription(exceptionKind.description);

        NewElementOptions newElementOptions = new NewElementOptions(governanceDefinitionClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        exceptionTypeGUID = governanceDefinitionClient.createGovernanceDefinition(newElementOptions,
                                                                                  null,
                                                                                  exceptionTypeProperties,
                                                                                  null);

        exceptionTypeGUIDs.put(exceptionKind, exceptionTypeGUID);

        logRecord(methodName,
                  DarwinAuditCode.NEW_EXCEPTION_TYPE.getMessageDefinition(connectorName,
                                                                          exceptionKind.identifier,
                                                                          exceptionTypeGUID,
                                                                          exceptionKind.relationshipTypeName));

        return exceptionTypeGUID;
    }


    /* ==============================================================================
     * Retrieval helpers.
     */


    /**
     * Retrieve an element that may only be visible to lineage.  A relationship end that points at an element that
     * has gone is reported as null rather than as a failure: the relationship is not this connector's to mend, and
     * one dangling end should not stop the rest of the refresh.
     *
     * @param elementGUID unique identifier of the element
     * @return element or null
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private OpenMetadataElement getLineageElementByGUID(String elementGUID) throws Exception
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForLineage(true);

        try
        {
            return integrationContext.getOpenMetadataStore().getMetadataElementByGUID(elementGUID, getOptions);
        }
        catch (InvalidParameterException notFound)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Element " + forLog(elementGUID) + " at the end of a data mapping could not be retrieved: " + forLog(notFound.getMessage()));
            }

            return null;
        }
    }


    /**
     * Retrieve every element of a type, page by page.
     *
     * @param typeName type of element
     * @return list - empty if there are none
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private List<OpenMetadataElement> getAllElementsOfType(String typeName) throws Exception
    {
        List<OpenMetadataElement> elements          = new ArrayList<>();
        OpenMetadataStore         openMetadataStore = integrationContext.getOpenMetadataStore();
        int                       pageSize          = integrationContext.getMaxPageSize();
        int                       startFrom         = 0;

        List<OpenMetadataElement> page = openMetadataStore.findMetadataElements(typeName, null, null, null, startFrom, pageSize);

        while ((page != null) && (! page.isEmpty()))
        {
            for (OpenMetadataElement element : page)
            {
                if (element != null)
                {
                    elements.add(element);
                }
            }

            startFrom = startFrom + pageSize;

            page = openMetadataStore.findMetadataElements(typeName, null, null, null, startFrom, pageSize);
        }

        return elements;
    }


    /**
     * Retrieve every relationship of a type, page by page.
     *
     * @param typeName type of relationship
     * @param forLineage is this a lineage query, which also returns the relationships only visible to lineage?
     * @return list - empty if there are none
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private List<OpenMetadataRelationship> getAllRelationshipsOfType(String  typeName,
                                                                     boolean forLineage) throws Exception
    {
        List<OpenMetadataRelationship> relationships     = new ArrayList<>();
        OpenMetadataStore              openMetadataStore = integrationContext.getOpenMetadataStore();
        int                            startFrom         = 0;

        OpenMetadataRelationshipList page = openMetadataStore.findRelationshipsBetweenMetadataElements(typeName,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       this.getQueryOptions(startFrom, forLineage));

        while ((page != null) && (page.getRelationships() != null) && (! page.getRelationships().isEmpty()))
        {
            for (OpenMetadataRelationship relationship : page.getRelationships())
            {
                if (relationship != null)
                {
                    relationships.add(relationship);
                }
            }

            startFrom = startFrom + integrationContext.getMaxPageSize();

            page = openMetadataStore.findRelationshipsBetweenMetadataElements(typeName,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              this.getQueryOptions(startFrom, forLineage));
        }

        return relationships;
    }


    /**
     * Retrieve every element related to an element through a type of relationship, page by page.
     *
     * @param elementGUID starting element
     * @param startingAtEnd which end the starting element is at (0 is either end; 1 is end 1; 2 is end 2)
     * @param relationshipTypeName type of relationship
     * @param forLineage is this a lineage query, which also returns the elements only visible to lineage?
     * @return list - empty if there are none
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private List<RelatedMetadataElement> getAllRelatedElements(String  elementGUID,
                                                               int     startingAtEnd,
                                                               String  relationshipTypeName,
                                                               boolean forLineage) throws Exception
    {
        List<RelatedMetadataElement> relatedElements   = new ArrayList<>();
        OpenMetadataStore            openMetadataStore = integrationContext.getOpenMetadataStore();
        int                          startFrom         = 0;

        RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                       startingAtEnd,
                                                                                       relationshipTypeName,
                                                                                       this.getQueryOptions(startFrom, forLineage));

        while ((page != null) && (page.getElementList() != null) && (! page.getElementList().isEmpty()))
        {
            for (RelatedMetadataElement relatedElement : page.getElementList())
            {
                if (relatedElement != null)
                {
                    relatedElements.add(relatedElement);
                }
            }

            startFrom = startFrom + integrationContext.getMaxPageSize();

            page = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                startingAtEnd,
                                                                relationshipTypeName,
                                                                this.getQueryOptions(startFrom, forLineage));
        }

        return relatedElements;
    }


    /**
     * Return the query options for one page of a retrieval.
     *
     * @param startFrom where to start the page
     * @param forLineage is this a lineage query?
     * @return query options
     */
    private QueryOptions getQueryOptions(int     startFrom,
                                         boolean forLineage)
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForLineage(forLineage);
        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(integrationContext.getMaxPageSize());

        return queryOptions;
    }


    /**
     * Remember an element's qualified name for the audit log.
     *
     * @param element element retrieved from the repository
     * @param elementNames map from element GUID to qualified name - added to
     */
    private void recordName(OpenMetadataElement element,
                            Map<String, String> elementNames)
    {
        final String methodName = "recordName";

        elementNames.put(element.getElementGUID(),
                         propertyHelper.getStringProperty(connectorName,
                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                          element.getElementProperties(),
                                                          methodName));
    }


    /**
     * Name an element for the audit log.  The qualified name is used where it is known; an element that has gone
     * since it was indexed - or was never indexed, if a relationship was attached to something unexpected - is
     * named by its unique identifier.
     *
     * @param elementGUID unique identifier of the element
     * @param elementNames map from element GUID to qualified name
     * @return name
     */
    private String getElementName(String              elementGUID,
                                  Map<String, String> elementNames)
    {
        String elementName = elementNames.get(elementGUID);

        if (elementName != null)
        {
            return elementName + " (" + elementGUID + ")";
        }

        return elementGUID;
    }


    /**
     * Name the metadata collection that owns an instance if this repository does not.  An instance that
     * arrived from a content pack, or from another repository in the cohort, can only be changed on behalf of
     * whoever owns it, and its home metadata collection is recorded in its own header.  Nothing is set for an
     * instance this repository owns.
     *
     * @param metadataSourceOptions options to fill in
     * @param elementHeader header of the instance being changed
     */
    private void setOwningMetadataCollection(MetadataSourceOptions metadataSourceOptions,
                                             ElementControlHeader  elementHeader)
    {
        if ((elementHeader != null) && (elementHeader.getOrigin() != null))
        {
            ElementOriginCategory originCategory = elementHeader.getOrigin().getOriginCategory();

            if ((originCategory != null) && (originCategory != ElementOriginCategory.LOCAL_COHORT))
            {
                metadataSourceOptions.setExternalSourceGUID(elementHeader.getOrigin().getHomeMetadataCollectionId());
                metadataSourceOptions.setExternalSourceName(elementHeader.getOrigin().getHomeMetadataCollectionName());
            }
        }
    }


    /**
     * Make a value from the metadata safe to write to the log: a line break in a stored value could forge log entries.
     *
     * @param value value from the metadata
     * @return the value with its line breaks replaced - null if the value is null
     */
    private static String forLog(String value)
    {
        if (value == null)
        {
            return null;
        }

        return value.replace('\r', '_').replace('\n', '_');
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        logRecord(methodName,
                  DarwinAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName,
                                                                          integrationContext.getMetadataAccessServer(),
                                                                          integrationContext.getMetadataAccessServerPlatformURLRoot()));

        super.disconnect();
    }
}
