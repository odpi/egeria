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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * DarwinProductDependencyManagerConnector maintains the coarse-grained lineage that is implied by the finer-grained
 * lineage beneath it, working upwards from the most detailed level on each refresh:
 * <ul>
 *     <li><b>Schema elements to data assets.</b>  A DataMapping relationship between two schema elements shows data
 *     being copied from one to the other.  Where the two schema elements belong to different data assets, data flows
 *     from the first asset to the second, so a DataFlow relationship is maintained between the assets.</li>
 *     <li><b>Data assets to software servers.</b>  A software server hosts software capabilities (through the
 *     SupportedSoftwareCapability relationship), and a capability owns data assets (through the CapabilityAssetUse
 *     relationship with a useType of OWNS).  Where the data lineage leads - directly or through intermediate
 *     elements - from an asset owned by one server's capability to an asset owned by another's, data flows from
 *     the first server to the second, so a DataFlow relationship is maintained between the servers.</li>
 *     <li><b>Data assets to digital products.</b>  The assets that are members of a digital product (through the
 *     CollectionMembership relationship) are the start of paths followed downstream through the data lineage.  A path
 *     may pass through any number of intermediate elements - processes, assets that belong to no product - but every
 *     relationship on it must belong to the same information supply chain (the iscQualifiedName of the lineage
 *     relationship).  When the path reaches an asset that is a member of another product, that product depends on
 *     the product the path started from, through that information supply chain, so a DigitalProductDependency
 *     relationship is maintained between the products.  The path stops there: the dependency on anything further
 *     downstream belongs to the product just reached.</li>
 * </ul>
 * The information supply chain of the finer-grained relationship is carried up onto the coarser one, and because
 * every one of these relationship types is multi-link there is one relationship per information supply chain
 * between the same two elements.  Each level is reconciled with the relationships in the repository the same way.
 * Relationships this connector created itself - recognized by the createdBy in their header - are removed when the
 * finer-grained lineage no longer supports them, and missing ones are created.  Relationships asserted by external
 * users take precedence and are never removed: one whose information supply chain is not set is given the supply
 * chain of the first finer-grained lineage that proves it.
 * <p>
 * A DigitalProductDependency asserted by an external user that no lineage path proves is recorded as an exception.
 * The connector has its own ExceptionType, created on first use, and links it to each dependent digital product that
 * has unproven dependencies with an Exception relationship whose affectedRelationships property lists the unproven
 * DigitalProductDependency relationships.  The exception is updated as the list changes and removed once nothing on
 * it is left.
 * <p>
 * This connector works across the whole open metadata ecosystem rather than through catalog targets, which is why
 * it extends IntegrationConnectorBase rather than DynamicIntegrationConnectorBase.  Each refresh reconciles from a
 * fresh snapshot, so nothing is carried between refreshes but the identity of the exception type.
 */
public class DarwinProductDependencyManagerConnector extends IntegrationConnectorBase
{
    private static final String exceptionTypeName          = "UnprovenDigitalProductDependency";
    private static final String exceptionTypeQualifiedName = OpenMetadataType.EXCEPTION_TYPE.typeName + "::" + exceptionTypeName;
    private static final String exceptionTypeDisplayName   = "Unproven digital product dependency";
    private static final String exceptionTypeSummary       = "A digital product has a DigitalProductDependency relationship, asserted by an " +
                                                                     "external user, that is not backed up by the data lineage between the assets " +
                                                                     "of the two products.";
    private static final String exceptionTypeDescription   = "The Darwin Product Dependency Manager derives the dependencies between digital " +
                                                                     "products from the data lineage between the assets that are members of the " +
                                                                     "products, following each information supply chain in turn.  A " +
                                                                     "DigitalProductDependency relationship asserted by an external user is " +
                                                                     "expected to be provable the same way.  Where it is not, the dependent " +
                                                                     "product is linked to this exception type and the affectedRelationships " +
                                                                     "property of the Exception relationship lists the relationships that could " +
                                                                     "not be proved.  Either the lineage is incomplete, the information supply " +
                                                                     "chain named on the relationship is wrong, or the dependency does not exist.";

    /*
     * Recorded on every Exception relationship this connector creates.
     */
    private static final String unprovenExceptionLabel       = "unproven dependencies";
    private static final String unprovenExceptionDescription = "The DigitalProductDependency relationships listed in affectedRelationships " +
                                                                       "were asserted by external users and are not supported by the data " +
                                                                       "lineage between the assets of the two products for the information " +
                                                                       "supply chain named on the relationship.";
    private static final String unprovenExceptionNotes       = "Raised automatically by the Darwin Product Dependency Manager.  It is " +
                                                                       "updated on each refresh as the list of unproven relationships " +
                                                                       "changes, and removed once the list is empty.";

    /**
     * The data lineage relationship types where the data flows from end 2 to end 1.  In every other data lineage
     * relationship - DataFlow, ProcessCall, LineageMapping, UltimateDestination and any new subtype - the data
     * flows from end 1 to end 2.
     */
    private static final List<String> reverseFlowLineageTypes = List.of(OpenMetadataType.ULTIMATE_SOURCE_RELATIONSHIP.typeName);

    private int    maxLineageDepth   = DarwinConfigurationProperty.DEFAULT_MAX_LINEAGE_DEPTH;
    private String exceptionTypeGUID = null;

    private static final Logger log = LoggerFactory.getLogger(DarwinProductDependencyManagerConnector.class);


    /**
     * The kinds of coarse-grained relationship that this connector maintains.  Each names the relationship type,
     * the label and description recorded on the relationships the connector creates, and - for the audit log - why
     * the relationship exists.
     */
    private enum LinkKind
    {
        /**
         * A DataFlow between two data assets, derived from the DataMapping relationships between their schema elements.
         */
        ASSET_LINEAGE(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                      "derived from data mappings",
                      "This data flow was derived by the Darwin Product Dependency Manager from the DataMapping relationships " +
                              "between the schema elements of the two data assets.  It is removed automatically if the data " +
                              "mappings no longer support it.",
                      "the schema elements of the two data assets are linked by DataMapping relationships"),

        /**
         * A DataFlow between two software servers, derived from the lineage between the data assets that their
         * capabilities own.
         */
        SERVER_LINEAGE(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                       "derived from asset lineage",
                       "This data flow was derived by the Darwin Product Dependency Manager from the lineage between the data " +
                               "assets owned by the software capabilities of the two software servers.  It is removed " +
                               "automatically if that lineage no longer supports it.",
                       "the data assets owned by the software capabilities of the two servers are linked by lineage"),

        /**
         * A DigitalProductDependency between two digital products, derived from the lineage between their assets.
         */
        PRODUCT_DEPENDENCY(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                           "derived from lineage",
                           "This dependency was derived by the Darwin Product Dependency Manager from the data lineage between " +
                                   "the assets that are members of the two digital products.  It is removed automatically if the " +
                                   "lineage no longer supports it.",
                           "the assets of the two digital products are linked by lineage");

        final String typeName;
        final String label;
        final String description;
        final String reason;

        LinkKind(String typeName, String label, String description, String reason)
        {
            this.typeName    = typeName;
            this.label       = label;
            this.description = description;
            this.reason      = reason;
        }
    }


    /**
     * The identity of one of the relationships this connector maintains: the elements at its two ends and, because
     * every one of these relationship types is multi-link, the information supply chain that tells the relationships
     * between the same two elements apart.  For a DataFlow, end 1 is the supplier and end 2 the consumer; for a
     * DigitalProductDependency, end 1 is the dependent product and end 2 the product it depends on.  The information
     * supply chain may be null, which is a relationship derived from finer-grained lineage that belongs to no
     * information supply chain.
     *
     * @param end1GUID unique identifier of the element at end 1
     * @param end2GUID unique identifier of the element at end 2
     * @param iscQualifiedName information supply chain that the relationship belongs to - may be null
     */
    record LinkKey(String end1GUID,
                   String end2GUID,
                   String iscQualifiedName)
    {
    }


    /**
     * An element reached while following the lineage downstream from a product's asset.
     *
     * @param elementGUID unique identifier of the element reached
     * @param iscQualifiedName information supply chain that the path belongs to - may be null
     * @param depth number of lineage relationships followed to reach it
     */
    private record LineageStep(String elementGUID,
                               String iscQualifiedName,
                               int    depth)
    {
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
    }


    /**
     * The tallies for one refresh, reported in the audit log as each step completes.
     */
    private static class RefreshCounts
    {
        int        assetLineageDerived  = 0;
        int        serverLineageDerived = 0;
        LinkCounts lineage              = new LinkCounts();

        int        products             = 0;
        int        dependenciesDerived  = 0;
        LinkCounts dependencies         = new LinkCounts();
        int        abandonedPaths       = 0;

        int        exceptionsRaised     = 0;
        int        exceptionsUpdated    = 0;
        int        exceptionsCleared    = 0;
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
     * Bring the coarse-grained lineage into line with the finer-grained lineage beneath it, working upwards: data
     * mappings between schema elements to data flows between assets, lineage between assets to data flows between
     * servers, and lineage between assets to dependencies between digital products.  The asset-level data flows are
     * written to the repository before the product dependencies are derived, so that the products see them.
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
             * The exception type is needed before any exception can be recorded; retrieving (or creating) it first
             * means a failure to do so stops the refresh before anything has been changed.
             */
            this.getExceptionTypeGUID();

            RefreshCounts       counts       = new RefreshCounts();
            Map<String, String> elementNames = new HashMap<>();

            /*
             * Level one - schema elements up to data assets.
             */
            Set<LinkKey> assetLineage = this.deriveAssetLineage(elementNames, counts);

            /*
             * Level two - data assets up to software servers.  The asset-level data flows derived a moment ago have
             * not reached the repository yet, so they are passed in alongside what the repository holds.
             */
            Set<LinkKey> serverLineage = this.deriveServerLineage(assetLineage, elementNames, counts);

            /*
             * Both levels are DataFlow relationships, and the reconciliation removes any DataFlow this connector
             * created that is not in the set it is given, so the two are reconciled together.
             */
            Set<LinkKey> derivedLineage = new HashSet<>(assetLineage);

            derivedLineage.addAll(serverLineage);

            this.reconcileLinks(LinkKind.ASSET_LINEAGE, derivedLineage, serverLineage, elementNames, counts.lineage);

            logRecord(methodName,
                      DarwinAuditCode.LINEAGE_REFRESH_COMPLETE.getMessageDefinition(connectorName,
                                                                                    Integer.toString(counts.assetLineageDerived),
                                                                                    Integer.toString(counts.serverLineageDerived),
                                                                                    Integer.toString(counts.lineage.created),
                                                                                    Integer.toString(counts.lineage.removed),
                                                                                    Integer.toString(counts.lineage.supplyChainSet)));

            /*
             * Level three - data assets up to digital products.  This reads the lineage from the repository, which
             * now includes the asset-level data flows written above.
             */
            Map<String, Set<String>> productsByMember = new HashMap<>();

            this.indexProductMembers(productsByMember, elementNames, counts);

            Set<LinkKey> derivedDependencies = this.deriveDependencies(productsByMember, counts);

            Map<String, List<String>> unprovenByProduct = this.reconcileLinks(LinkKind.PRODUCT_DEPENDENCY,
                                                                              derivedDependencies,
                                                                              null,
                                                                              elementNames,
                                                                              counts.dependencies);

            this.maintainExceptions(unprovenByProduct, elementNames, counts);

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
                                                                            Integer.toString(counts.exceptionsRaised),
                                                                            Integer.toString(counts.exceptionsUpdated),
                                                                            Integer.toString(counts.exceptionsCleared)));
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
     * Level one - the data flows between data assets implied by the data mappings between their schema elements.
     */


    /**
     * Derive a data flow between two data assets for every DataMapping relationship whose ends belong to different
     * data assets.  The mapping's source is the supplier and its target the consumer.  An end belongs to the data
     * asset it is anchored to - or is the asset itself, if a mapping has been made directly between assets.
     *
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @param counts tallies for the refresh
     * @return the data flows the mappings support
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private Set<LinkKey> deriveAssetLineage(Map<String, String> elementNames,
                                            RefreshCounts       counts) throws Exception
    {
        final String methodName = "deriveAssetLineage";

        Set<LinkKey>        assetLineage = new HashSet<>();
        Map<String, String> owningAssets = new HashMap<>();

        for (OpenMetadataRelationship dataMapping : this.getAllRelationshipsOfType(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, true))
        {
            String sourceAssetGUID = this.getOwningDataAssetGUID(dataMapping.getElementGUIDAtEnd1(), owningAssets, elementNames);
            String targetAssetGUID = this.getOwningDataAssetGUID(dataMapping.getElementGUIDAtEnd2(), owningAssets, elementNames);

            if ((sourceAssetGUID != null) && (targetAssetGUID != null) && (! sourceAssetGUID.equals(targetAssetGUID)))
            {
                assetLineage.add(new LinkKey(sourceAssetGUID,
                                             targetAssetGUID,
                                             propertyHelper.getStringProperty(connectorName,
                                                                              OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                              dataMapping.getRelationshipProperties(),
                                                                              methodName)));
            }
        }

        counts.assetLineageDerived = assetLineage.size();

        return assetLineage;
    }


    /**
     * Return the data asset that an element belongs to: the element itself if it is a data asset, otherwise the
     * element it is anchored to if that is a data asset, otherwise null.  The answer is cached because a data
     * asset's schema elements typically appear in many mappings.
     *
     * @param elementGUID element at one end of a data mapping
     * @param owningAssets cache of answers - added to
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @return unique identifier of the owning data asset or null
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
            if (propertyHelper.isTypeOf(element, OpenMetadataType.DATA_ASSET.typeName))
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
     * Level two - the data flows between software servers implied by the lineage between the data assets that
     * their capabilities own.
     */


    /**
     * Derive a data flow between two software servers wherever the data lineage leads from a data asset owned by
     * one server's capability to a data asset owned by another's.  The lineage is followed downstream the same way
     * as for products: a path may pass through any number of intermediate elements, keeps to one information
     * supply chain, and stops at the first asset owned by any server.  The asset-level data flows derived on this
     * refresh are followed too, since they are on their way to the repository but not there yet.
     *
     * @param assetLineage the data flows between assets derived on this refresh
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @param counts tallies for the refresh
     * @return the data flows between servers that the asset lineage supports
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private Set<LinkKey> deriveServerLineage(Set<LinkKey>        assetLineage,
                                             Map<String, String> elementNames,
                                             RefreshCounts       counts) throws Exception
    {
        Map<String, Set<String>>  serversByOwnedAsset = this.indexOwnedAssets(elementNames);
        Map<String, Set<LinkKey>> pendingFlowsByEnd1  = new HashMap<>();
        Set<LinkKey>              serverLineage       = new HashSet<>();

        for (LinkKey assetFlow : assetLineage)
        {
            pendingFlowsByEnd1.computeIfAbsent(assetFlow.end1GUID(), guid -> new HashSet<>()).add(assetFlow);
        }

        for (Map.Entry<String, Set<String>> ownedAsset : serversByOwnedAsset.entrySet())
        {
            for (String supplierServerGUID : ownedAsset.getValue())
            {
                this.followLineageDownstream(LinkKind.SERVER_LINEAGE,
                                             supplierServerGUID,
                                             ownedAsset.getKey(),
                                             serversByOwnedAsset,
                                             pendingFlowsByEnd1,
                                             serverLineage,
                                             counts);
            }
        }

        counts.serverLineageDerived = serverLineage.size();

        return serverLineage;
    }


    /**
     * Build the index from each data asset to the software servers whose capabilities own it.  A server is at end 1
     * of SupportedSoftwareCapability with its capabilities at end 2; a capability is at end 1 of CapabilityAssetUse
     * with the assets at end 2, and only the uses whose useType is OWNS count.
     *
     * @param elementNames map from element GUID to qualified name, for the audit log - added to
     * @return map from asset GUID to the GUIDs of the servers that own it
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private Map<String, Set<String>> indexOwnedAssets(Map<String, String> elementNames) throws Exception
    {
        final String methodName = "indexOwnedAssets";

        Map<String, Set<String>> serversByOwnedAsset = new HashMap<>();

        for (OpenMetadataElement server : this.getAllElementsOfType(OpenMetadataType.SOFTWARE_SERVER.typeName))
        {
            String serverGUID = server.getElementGUID();

            this.recordName(server, elementNames);

            for (RelatedMetadataElement capability : this.getAllRelatedElements(serverGUID,
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
                    if ((assetUse.getElement() != null) &&
                            (propertyHelper.isTypeOf(assetUse.getElement(), OpenMetadataType.DATA_ASSET.typeName)) &&
                            (CapabilityAssetUseType.OWNS.name().equals(propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                                                                                  OpenMetadataProperty.USE_TYPE.name,
                                                                                                                  assetUse.getRelationshipProperties(),
                                                                                                                  methodName))))
                    {
                        this.recordName(assetUse.getElement(), elementNames);

                        serversByOwnedAsset.computeIfAbsent(assetUse.getElement().getElementGUID(), guid -> new HashSet<>()).add(serverGUID);
                    }
                }
            }
        }

        return serversByOwnedAsset;
    }


    /* ==============================================================================
     * Level three - the dependencies between digital products implied by the lineage between their assets.
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
     * Derive the dependencies between products from the data lineage downstream of each product's assets.
     *
     * @param productsByMember map from asset GUID to the GUIDs of the products it belongs to
     * @param counts tallies for the refresh
     * @return the dependencies the lineage supports
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private Set<LinkKey> deriveDependencies(Map<String, Set<String>> productsByMember,
                                            RefreshCounts            counts) throws Exception
    {
        Set<LinkKey> derivedDependencies = new HashSet<>();

        for (Map.Entry<String, Set<String>> member : productsByMember.entrySet())
        {
            for (String providerProductGUID : member.getValue())
            {
                this.followLineageDownstream(LinkKind.PRODUCT_DEPENDENCY,
                                             providerProductGUID,
                                             member.getKey(),
                                             productsByMember,
                                             null,
                                             derivedDependencies,
                                             counts);
            }
        }

        counts.dependenciesDerived = derivedDependencies.size();

        return derivedDependencies;
    }


    /**
     * Follow the data lineage downstream from one asset of one group - a digital product or a software server -
     * until it reaches the assets of other groups.  This is a breadth-first walk in which each path keeps to a
     * single information supply chain: the first relationship out of the asset fixes the supply chain for the
     * path, and only relationships with the same supply chain are followed after that.  A path stops when it
     * reaches an asset that belongs to any group - the relationship with whatever lies beyond belongs to that
     * group rather than this one - or when it has gone on for longer than the configured depth.
     * <p>
     * The relationship derived is oriented by its kind.  Data flows from the starting group to the group reached,
     * so a data flow has the starting group at end 1 (the supplier); a product dependency has the group reached at
     * end 1 (the dependent product) and the starting group at end 2.
     *
     * @param kind the kind of relationship being derived
     * @param startGroupGUID the group whose asset the walk starts from
     * @param memberGUID the asset the walk starts from
     * @param groupsByMember map from asset GUID to the GUIDs of the groups it belongs to
     * @param pendingFlowsByEnd1 data flows derived on this refresh that are not yet in the repository, keyed by
     *                           their supplier - null if there are none
     * @param derivedLinks the relationships found so far - added to
     * @param counts tallies for the refresh
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private void followLineageDownstream(LinkKind                  kind,
                                         String                    startGroupGUID,
                                         String                    memberGUID,
                                         Map<String, Set<String>>  groupsByMember,
                                         Map<String, Set<LinkKey>> pendingFlowsByEnd1,
                                         Set<LinkKey>              derivedLinks,
                                         RefreshCounts             counts) throws Exception
    {
        Deque<LineageStep> pathsToFollow = new ArrayDeque<>();
        Set<String>        visited       = new HashSet<>();

        this.queueDownstreamNeighbours(memberGUID, null, true, 0, pendingFlowsByEnd1, pathsToFollow);

        while (! pathsToFollow.isEmpty())
        {
            LineageStep step = pathsToFollow.poll();

            /*
             * The same element may be reached along different supply chains, and each is a different path, so the
             * visited set is keyed by both.  This is also what stops a cycle in the lineage being followed for ever.
             */
            if (! visited.add(step.elementGUID() + "|" + step.iscQualifiedName()))
            {
                continue;
            }

            Set<String> groupsReached = groupsByMember.get(step.elementGUID());

            if (groupsReached != null)
            {
                for (String groupReachedGUID : groupsReached)
                {
                    /*
                     * Lineage between two assets of the same group is not a relationship of the group with itself.
                     */
                    if (! groupReachedGUID.equals(startGroupGUID))
                    {
                        if (kind == LinkKind.PRODUCT_DEPENDENCY)
                        {
                            derivedLinks.add(new LinkKey(groupReachedGUID, startGroupGUID, step.iscQualifiedName()));
                        }
                        else
                        {
                            derivedLinks.add(new LinkKey(startGroupGUID, groupReachedGUID, step.iscQualifiedName()));
                        }
                    }
                }

                continue;
            }

            if (step.depth() >= maxLineageDepth)
            {
                counts.abandonedPaths++;

                if (log.isDebugEnabled())
                {
                    log.debug("Abandoning lineage path from " + memberGUID + " at " + step.elementGUID() +
                                      " for information supply chain " + step.iscQualifiedName() + " after " + step.depth() + " steps");
                }

                continue;
            }

            this.queueDownstreamNeighbours(step.elementGUID(), step.iscQualifiedName(), false, step.depth(), pendingFlowsByEnd1, pathsToFollow);
        }
    }


    /**
     * Add the elements immediately downstream of an element to the paths still to follow.
     *
     * @param elementGUID the element whose downstream neighbours are wanted
     * @param iscQualifiedName the information supply chain the path belongs to
     * @param anySupplyChain true at the start of a walk, when the first relationship fixes the supply chain of the path
     * @param depth number of lineage relationships followed to reach this element
     * @param pendingFlowsByEnd1 data flows derived on this refresh that are not yet in the repository, keyed by
     *                           their supplier - null if there are none
     * @param pathsToFollow queue to add to
     *
     * @throws Exception a retrieval failed - reported by the caller
     */
    private void queueDownstreamNeighbours(String                    elementGUID,
                                           String                    iscQualifiedName,
                                           boolean                   anySupplyChain,
                                           int                       depth,
                                           Map<String, Set<LinkKey>> pendingFlowsByEnd1,
                                           Deque<LineageStep>        pathsToFollow) throws Exception
    {
        final String methodName = "queueDownstreamNeighbours";

        if ((pendingFlowsByEnd1 != null) && (pendingFlowsByEnd1.get(elementGUID) != null))
        {
            for (LinkKey pendingFlow : pendingFlowsByEnd1.get(elementGUID))
            {
                if ((anySupplyChain) || (Objects.equals(iscQualifiedName, pendingFlow.iscQualifiedName())))
                {
                    pathsToFollow.add(new LineageStep(pendingFlow.end2GUID(), pendingFlow.iscQualifiedName(), depth + 1));
                }
            }
        }

        for (RelatedMetadataElement lineageRelationship : this.getAllRelatedElements(elementGUID,
                                                                                      0,
                                                                                      OpenMetadataType.DATA_LINEAGE_RELATIONSHIP.typeName,
                                                                                      true))
        {
            if ((lineageRelationship.getElement() != null) && (this.isDownstream(lineageRelationship)))
            {
                String relationshipSupplyChain = propertyHelper.getStringProperty(connectorName,
                                                                                  OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                  lineageRelationship.getRelationshipProperties(),
                                                                                  methodName);

                if ((anySupplyChain) || (Objects.equals(iscQualifiedName, relationshipSupplyChain)))
                {
                    pathsToFollow.add(new LineageStep(lineageRelationship.getElement().getElementGUID(),
                                                      relationshipSupplyChain,
                                                      depth + 1));
                }
            }
        }
    }


    /**
     * Determine whether the related element is downstream of the element the relationship was retrieved from.
     * Data flows from end 1 to end 2 of a data lineage relationship, except for the UltimateSource relationship
     * where it flows from end 2 (the source) to end 1.
     *
     * @param lineageRelationship relationship and the element at its far end
     * @return true if the data flows towards the related element
     */
    private boolean isDownstream(RelatedMetadataElement lineageRelationship)
    {
        boolean reverseFlow = false;

        for (String reverseFlowType : reverseFlowLineageTypes)
        {
            if (propertyHelper.isTypeOf(lineageRelationship, reverseFlowType))
            {
                reverseFlow = true;
                break;
            }
        }

        /*
         * The related element is at end 2 when getElementAtEnd1 is false.  Data reaches end 2 on a forward flow
         * and end 1 on a reverse flow.
         */
        if (lineageRelationship.getElementAtEnd1())
        {
            return reverseFlow;
        }

        return ! reverseFlow;
    }


    /* ==============================================================================
     * Reconciliation - bringing the stored relationships of a type into line with the derived ones.  The same
     * procedure serves every level.
     */


    /**
     * Compare the derived relationships of a kind with the relationships of that type in the repository.
     * Relationships asserted by external users are considered first, because they take precedence over this
     * connector's own: an externally asserted relationship that the finer-grained lineage proves claims the derived
     * relationship, and an own relationship for the same one is then redundant and removed.  Own relationships that
     * the finer-grained lineage no longer supports are removed, and derived relationships that nothing records are
     * created.
     *
     * @param kind the kind of relationship, which gives the type name and the wording for the audit log
     * @param derivedLinks every relationship of this type that the finer-grained lineage supports
     * @param secondaryLinks the subset of derivedLinks that a second kind accounts for, where two kinds share a
     *                       type (the server-level data flows among the asset-level ones) - null if there is none
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for this kind
     * @return the unproven externally asserted relationships, keyed by the GUID of the element at end 1
     *
     * @throws Exception a retrieval or update failed - reported by the caller
     */
    private Map<String, List<String>> reconcileLinks(LinkKind            kind,
                                                     Set<LinkKey>        derivedLinks,
                                                     Set<LinkKey>        secondaryLinks,
                                                     Map<String, String> elementNames,
                                                     LinkCounts          counts) throws Exception
    {
        final String methodName = "reconcileLinks";

        String myUserId = integrationContext.getMyUserId();

        Set<LinkKey>                   claimed           = new HashSet<>();
        List<OpenMetadataRelationship> ownRelationships  = new ArrayList<>();
        Map<String, List<String>>      unprovenByEnd1    = new HashMap<>();

        /*
         * Pass one - the relationships asserted by external users.
         */
        for (OpenMetadataRelationship relationship : this.getAllRelationshipsOfType(kind.typeName, kind != LinkKind.PRODUCT_DEPENDENCY))
        {
            if (this.isCreatedByMe(relationship, myUserId))
            {
                ownRelationships.add(relationship);
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
                    this.recordUnproven(kind, unprovenByEnd1, relationship, counts);
                }
                else
                {
                    claimed.add(match);

                    if (match.iscQualifiedName() != null)
                    {
                        this.setSupplyChain(kind, relationship, match.iscQualifiedName(), elementNames, counts);
                    }
                }
            }
            else if (derivedLinks.contains(key))
            {
                claimed.add(key);
            }
            else
            {
                this.recordUnproven(kind, unprovenByEnd1, relationship, counts);
            }
        }

        /*
         * Pass two - the relationships this connector created on earlier refreshes.  One is kept if the finer-grained
         * lineage still supports it and nothing else has claimed it; otherwise it goes.
         */
        for (OpenMetadataRelationship relationship : ownRelationships)
        {
            LinkKey key = this.getLinkKey(relationship, methodName);

            if ((! derivedLinks.contains(key)) || (! claimed.add(key)))
            {
                this.removeLink(kind, relationship, key, elementNames, counts);
            }
        }

        /*
         * Pass three - the relationships that nothing records.
         */
        for (LinkKey key : this.sortForDeterminism(derivedLinks))
        {
            if (! claimed.contains(key))
            {
                LinkKind keyKind = kind;

                if ((secondaryLinks != null) && (secondaryLinks.contains(key)))
                {
                    keyKind = LinkKind.SERVER_LINEAGE;
                }

                this.createLink(keyKind, key, elementNames, counts);
            }
        }

        return unprovenByEnd1;
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
     * Add an externally asserted product dependency that the lineage does not prove to the list for the dependent
     * product, which is where the exception is recorded.  An externally asserted data flow that the finer-grained
     * lineage does not account for is simply the ordinary lineage of the repository - most data flows are not
     * derived from anything - so nothing is recorded for it.
     *
     * @param kind the kind of relationship
     * @param unprovenByEnd1 unproven relationships keyed by end 1 GUID - added to
     * @param relationship the unproven relationship
     * @param counts tallies for this kind
     */
    private void recordUnproven(LinkKind                  kind,
                                Map<String, List<String>> unprovenByEnd1,
                                OpenMetadataRelationship  relationship,
                                LinkCounts                counts)
    {
        if (kind == LinkKind.PRODUCT_DEPENDENCY)
        {
            counts.unproven++;

            unprovenByEnd1.computeIfAbsent(relationship.getElementGUIDAtEnd1(), guid -> new ArrayList<>()).add(relationship.getRelationshipGUID());
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

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                        iscQualifiedName);

        UpdateOptions updateOptions = openMetadataStore.getUpdateOptions(true);

        this.setOwningMetadataCollection(updateOptions, relationship);

        openMetadataStore.updateRelatedElementsInStore(relationship.getRelationshipGUID(), updateOptions, properties);

        counts.supplyChainSet++;

        String assertedBy = null;

        if (relationship.getVersions() != null)
        {
            assertedBy = relationship.getVersions().getCreatedBy();
        }

        DarwinAuditCode auditCode = DarwinAuditCode.LINEAGE_SUPPLY_CHAIN_SET;

        if (kind == LinkKind.PRODUCT_DEPENDENCY)
        {
            auditCode = DarwinAuditCode.SUPPLY_CHAIN_SET;
        }

        logRecord(methodName,
                  auditCode.getMessageDefinition(connectorName,
                                                 relationship.getRelationshipGUID(),
                                                 this.getElementName(relationship.getElementGUIDAtEnd1(), elementNames),
                                                 this.getElementName(relationship.getElementGUIDAtEnd2(), elementNames),
                                                 assertedBy,
                                                 iscQualifiedName));
    }


    /**
     * Create a relationship for a derived one that nothing records.
     *
     * @param kind the kind of relationship, which gives the type name and the label and description to record on it
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
                                                      OpenMetadataProperty.LABEL.name,
                                                      kind.label);

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

        DarwinAuditCode auditCode = DarwinAuditCode.LINEAGE_REMOVED;

        if (kind == LinkKind.PRODUCT_DEPENDENCY)
        {
            auditCode = DarwinAuditCode.DEPENDENCY_REMOVED;
        }

        logRecord(methodName,
                  auditCode.getMessageDefinition(connectorName,
                                                 relationship.getRelationshipGUID(),
                                                 this.getElementName(key.end1GUID(), elementNames),
                                                 this.getElementName(key.end2GUID(), elementNames),
                                                 key.iscQualifiedName()));
    }


    /* ==============================================================================
     * The exceptions for the externally asserted dependencies the lineage does not prove.
     */


    /**
     * Bring the Exception relationships hanging off this connector's exception type into line with the unproven
     * dependencies found on this refresh.  There is one Exception relationship per dependent product; its
     * affectedRelationships property lists the product's unproven DigitalProductDependency relationships.  Only the
     * Exception relationships this connector created are touched.
     *
     * @param unprovenByProduct unproven relationships keyed by dependent product GUID
     * @param elementNames map from element GUID to qualified name, for the audit log
     * @param counts tallies for the refresh
     *
     * @throws Exception a retrieval or update failed - reported by the caller
     */
    private void maintainExceptions(Map<String, List<String>> unprovenByProduct,
                                    Map<String, String>       elementNames,
                                    RefreshCounts             counts) throws Exception
    {
        final String methodName = "maintainExceptions";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        String            myUserId          = integrationContext.getMyUserId();

        /*
         * The exception type is at end 2 of the Exception relationship; the products are at end 1.
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

            String       productGUID = existingException.getElement().getElementGUID();
            List<String> unproven    = unprovenByProduct.remove(productGUID);

            if (unproven == null)
            {
                openMetadataStore.deleteRelationshipInStore(existingException.getRelationshipGUID());

                counts.exceptionsCleared++;

                logRecord(methodName,
                          DarwinAuditCode.EXCEPTION_CLEARED.getMessageDefinition(connectorName,
                                                                                 existingException.getRelationshipGUID(),
                                                                                 this.getElementName(productGUID, elementNames)));
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

                    counts.exceptionsUpdated++;

                    logRecord(methodName,
                              DarwinAuditCode.UNPROVEN_DEPENDENCIES.getMessageDefinition(connectorName,
                                                                                         existingException.getRelationshipGUID(),
                                                                                         this.getElementName(productGUID, elementNames),
                                                                                         Integer.toString(unproven.size()),
                                                                                         unproven.toString()));
                }
            }
        }

        /*
         * Whatever is left has no exception yet.
         */
        for (Map.Entry<String, List<String>> newException : unprovenByProduct.entrySet())
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.LABEL.name,
                                                                            unprovenExceptionLabel);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.DESCRIPTION.name,
                                                          unprovenExceptionDescription);

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

            counts.exceptionsRaised++;

            logRecord(methodName,
                      DarwinAuditCode.UNPROVEN_DEPENDENCIES.getMessageDefinition(connectorName,
                                                                                 exceptionGUID,
                                                                                 this.getElementName(newException.getKey(), elementNames),
                                                                                 Integer.toString(newException.getValue().size()),
                                                                                 newException.getValue().toString()));
        }
    }


    /**
     * Return the unique identifier of this connector's exception type, creating the exception type if it does not
     * exist.  The exception type is looked up by its qualified name, so an exception type created by an earlier
     * run - or by a different instance of this connector - is reused.
     *
     * @return unique identifier of the exception type
     *
     * @throws Exception the exception type could not be retrieved or created - reported by the caller
     */
    private synchronized String getExceptionTypeGUID() throws Exception
    {
        final String methodName = "getExceptionTypeGUID";

        if (exceptionTypeGUID != null)
        {
            return exceptionTypeGUID;
        }

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.EXCEPTION_TYPE.typeName);

        List<OpenMetadataRootElement> existingTypes = governanceDefinitionClient.getGovernanceDefinitionsByName(exceptionTypeQualifiedName,
                                                                                                                governanceDefinitionClient.getQueryOptions());

        if (existingTypes != null)
        {
            for (OpenMetadataRootElement existingType : existingTypes)
            {
                if ((existingType != null) && (existingType.getElementHeader() != null))
                {
                    exceptionTypeGUID = existingType.getElementHeader().getGUID();

                    return exceptionTypeGUID;
                }
            }
        }

        ExceptionTypeProperties exceptionTypeProperties = new ExceptionTypeProperties();

        exceptionTypeProperties.setQualifiedName(exceptionTypeQualifiedName);
        exceptionTypeProperties.setIdentifier(exceptionTypeName);
        exceptionTypeProperties.setDisplayName(exceptionTypeDisplayName);
        exceptionTypeProperties.setSummary(exceptionTypeSummary);
        exceptionTypeProperties.setDescription(exceptionTypeDescription);

        NewElementOptions newElementOptions = new NewElementOptions(governanceDefinitionClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        exceptionTypeGUID = governanceDefinitionClient.createGovernanceDefinition(newElementOptions,
                                                                                  null,
                                                                                  exceptionTypeProperties,
                                                                                  null);

        logRecord(methodName,
                  DarwinAuditCode.NEW_EXCEPTION_TYPE.getMessageDefinition(connectorName,
                                                                          exceptionTypeName,
                                                                          exceptionTypeGUID));

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
                log.debug("Element " + elementGUID + " at the end of a data mapping could not be retrieved: " + notFound.getMessage());
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
