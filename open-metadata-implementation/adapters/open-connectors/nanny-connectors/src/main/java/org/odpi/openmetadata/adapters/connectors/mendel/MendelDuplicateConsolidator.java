/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel;

import org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipEndCardinality;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MendelDuplicateConsolidator combines a cluster of validated peer duplicates into a single consolidated element,
 * using survivorship rules to decide what the consolidated element contains.
 * <ul>
 *     <li>The properties come from the latest version of the cluster's members.  A property that only an earlier
 *     version supplies is added, so nothing that a member knows about is lost.</li>
 *     <li>The qualified name is derived rather than inherited - the original with the ISO-8601 time of the
 *     merge appended - because a qualified name is unique and the members still hold theirs.</li>
 *     <li>The relationships of all of the members are combined.  A relationship is skipped when adding it would
 *     break the cardinality rules of its type - and where two members conflict on a relationship that the type
 *     only permits one of, the one from the latest member wins.</li>
 * </ul>
 * The consolidated element carries the ConsolidatedDuplicate classification with a status of VALIDATED, and is
 * linked to each of its members with a ConsolidatedDuplicateLink relationship.  That combination causes the
 * retrieval processing to return the consolidated element in place of the members.
 */
public class MendelDuplicateConsolidator
{
    private final IntegrationContext integrationContext;
    private final PropertyHelper     propertyHelper;
    private final String             connectorName;
    private final AuditLog           auditLog;

    /**
     * The relationships that describe the duplicate processing itself are not copied to the consolidated element.
     */
    private static final List<String> excludedRelationshipTypes = List.of(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                          OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName);

    /**
     * Cache of the relationship type definitions used to test the cardinality rules.
     */
    private final Map<String, OpenMetadataRelationshipDef> relationshipTypeDefs = new HashMap<>();


    /**
     * Constructor.
     *
     * @param integrationContext context giving access to open metadata
     * @param propertyHelper helper for working with element properties
     * @param connectorName name of the calling connector - used in messages
     * @param auditLog logging destination
     */
    public MendelDuplicateConsolidator(IntegrationContext integrationContext,
                                       PropertyHelper     propertyHelper,
                                       String             connectorName,
                                       AuditLog           auditLog)
    {
        this.integrationContext = integrationContext;
        this.propertyHelper     = propertyHelper;
        this.connectorName      = connectorName;
        this.auditLog           = auditLog;
    }


    /**
     * Consolidate a cluster of validated peer duplicates into a single element.  Nothing happens if the cluster has
     * already been consolidated.
     *
     * @param members the elements in the cluster
     *
     * @throws Exception the consolidation failed - reported by the caller
     */
    public void consolidateCluster(List<OpenMetadataElementStub> members) throws Exception
    {
        final String methodName = "consolidateCluster";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        /*
         * Retrieve the full elements because the stubs in the duplicate links carry no properties or versions.
         */
        List<OpenMetadataElement> clusterMembers = new ArrayList<>();

        for (OpenMetadataElementStub member : members)
        {
            if (member != null)
            {
                if (this.isAlreadyConsolidated(member.getGUID()))
                {
                    return;
                }

                OpenMetadataElement clusterMember = openMetadataStore.getMetadataElementByGUID(member.getGUID(), this.getRawGetOptions());

                if (clusterMember != null)
                {
                    clusterMembers.add(clusterMember);
                }
            }
        }

        if (clusterMembers.size() < 2)
        {
            return;
        }

        /*
         * Order the members so that the latest is first.  This is the order that the survivorship rules work in:
         * the latest value of anything wins, and the earlier members only fill in the gaps.
         */
        clusterMembers.sort(Comparator.comparing(this::getLastUpdateTime, Comparator.nullsLast(Comparator.reverseOrder())));

        OpenMetadataElement latestMember = clusterMembers.get(0);

        String consolidatedElementGUID = this.createConsolidatedElement(latestMember, clusterMembers);

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            /*
             * The purpose-built call is used rather than creating the relationship directly, so that the
             * stewardship API owns how a consolidated element is tied to the elements it was built from.
             */
            classificationExplorerClient.linkConsolidatedDuplicateToSourceElement(consolidatedElementGUID,
                                                                                   clusterMember.getElementGUID(),
                                                                                   null,
                                                                                   this.getRawMakeAnchorOptions(classificationExplorerClient));
        }

        this.combineRelationships(consolidatedElementGUID, clusterMembers);

        auditLog.logMessage(methodName,
                            MendelAuditCode.DUPLICATES_CONSOLIDATED.getMessageDefinition(connectorName,
                                                                                         consolidatedElementGUID,
                                                                                         Integer.toString(clusterMembers.size()),
                                                                                         latestMember.getType().getTypeName()));
    }


    /**
     * Determine whether an element is already linked to a consolidated element.
     *
     * @param elementGUID unique identifier of the element to test
     * @return boolean flag
     *
     * @throws Exception the retrieval failed - reported by the caller
     */
    private boolean isAlreadyConsolidated(String elementGUID) throws Exception
    {
        RelatedMetadataElementList consolidatedLinks = integrationContext.getOpenMetadataStore().getRelatedMetadataElements(elementGUID,
                                                                                                                             0,
                                                                                                                             OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName,
                                                                                                                             this.getRawQueryOptions(0));

        return (consolidatedLinks != null) && (consolidatedLinks.getElementList() != null) && (! consolidatedLinks.getElementList().isEmpty());
    }


    /**
     * Create the consolidated element from the merged properties of the cluster's members.
     *
     * @param latestMember the most recently updated member - it supplies the type of the consolidated element
     * @param clusterMembers the members of the cluster, latest first
     * @return unique identifier of the new consolidated element
     *
     * @throws Exception the creation failed - reported by the caller
     */
    private String createConsolidatedElement(OpenMetadataElement       latestMember,
                                             List<OpenMetadataElement> clusterMembers) throws Exception
    {
        final String methodName = "createConsolidatedElement";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        /*
         * Start with the latest member's properties and fill in any property that only the earlier members supply.
         */
        ElementProperties consolidatedProperties = new ElementProperties();

        /*
         * The property names that have been taken are tracked here rather than read back from the properties
         * being built: getPropertyValueMap() answers null while the set is empty, and returns a copy once it
         * is not, so it can neither be tested nor added to directly.
         */
        Set<String> propertyNamesTaken = new HashSet<>();

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            if (clusterMember.getElementProperties() != null)
            {
                Map<String, PropertyValue> memberProperties = clusterMember.getElementProperties().getPropertyValueMap();

                if (memberProperties != null)
                {
                    for (Map.Entry<String, PropertyValue> memberProperty : memberProperties.entrySet())
                    {
                        if (propertyNamesTaken.add(memberProperty.getKey()))
                        {
                            consolidatedProperties.setProperty(memberProperty.getKey(), memberProperty.getValue());
                        }
                    }
                }
            }
        }

        /*
         * The qualified name is the one exception to taking the latest member's value.  A qualified name is
         * unique, and the members are still there holding theirs, so the consolidated element is given a
         * derived one: the original with the time of the merge appended.  Without this the repository
         * rejects the new element as a duplicate of the very elements it is consolidating.
         */
        String originalQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        consolidatedProperties,
                                                                        methodName);

        if (originalQualifiedName != null)
        {
            /*
             * The time is written in ISO-8601 rather than the JVM's default date format: it sorts
             * chronologically as text, carries no spaces or locale-dependent timezone abbreviation, and can
             * be parsed by anything that later needs to know when the merge happened.
             */
            consolidatedProperties = propertyHelper.addStringProperty(consolidatedProperties,
                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                       originalQualifiedName + "_merged:" + Instant.now());
        }

        /*
         * The consolidated element must carry a validated ConsolidatedDuplicate classification, otherwise the
         * retrieval processing ignores it and continues to return the members separately.
         */
        ElementProperties classificationProperties = propertyHelper.addIntProperty(null,
                                                                                   OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                                   StatusIdentifier.VALIDATED.getOrdinal());

        classificationProperties = propertyHelper.addStringProperty(classificationProperties,
                                                                    OpenMetadataProperty.SOURCE.name,
                                                                    connectorName);

        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName,
                                   new NewElementProperties(classificationProperties));

        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setForDuplicateProcessing(true);

        return openMetadataStore.createMetadataElementInStore(latestMember.getType().getTypeName(),
                                                              newElementOptions,
                                                              initialClassifications,
                                                              new NewElementProperties(consolidatedProperties),
                                                              null);
    }


    /**
     * Copy the relationships of the cluster's members onto the consolidated element.  The members are processed
     * latest first, so where the cardinality rules only allow one relationship, it is the latest member's
     * relationship that is used.
     *
     * @param consolidatedElementGUID unique identifier of the consolidated element
     * @param clusterMembers the members of the cluster, latest first
     *
     * @throws Exception the retrieval or creation failed - reported by the caller
     */
    private void combineRelationships(String                    consolidatedElementGUID,
                                      List<OpenMetadataElement> clusterMembers) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        /*
         * These record what has already been attached to the consolidated element, so that the cardinality rules
         * are applied across all of the members rather than one member at a time.
         */
        Set<String> attachedPairs        = new HashSet<>();
        Set<String> singletonEndsUsed    = new HashSet<>();

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            Set<String> memberGUIDs = new HashSet<>();

            for (OpenMetadataElement member : clusterMembers)
            {
                memberGUIDs.add(member.getElementGUID());
            }

            int startFrom = 0;

            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(clusterMember.getElementGUID(),
                                                                                                       0,
                                                                                                       null,
                                                                                                       this.getRawQueryOptions(startFrom));

            while ((relatedElements != null) && (relatedElements.getElementList() != null) && (! relatedElements.getElementList().isEmpty()))
            {
                for (RelatedMetadataElement relatedElement : relatedElements.getElementList())
                {
                    this.combineRelationship(consolidatedElementGUID, relatedElement, memberGUIDs, attachedPairs, singletonEndsUsed);
                }

                startFrom = startFrom + integrationContext.getMaxPageSize();

                relatedElements = openMetadataStore.getRelatedMetadataElements(clusterMember.getElementGUID(),
                                                                                0,
                                                                                null,
                                                                                this.getRawQueryOptions(startFrom));
            }
        }
    }


    /**
     * Copy one of a member's relationships onto the consolidated element, if the cardinality rules allow it.
     *
     * @param consolidatedElementGUID unique identifier of the consolidated element
     * @param relatedElement the member's relationship
     * @param memberGUIDs the members of the cluster - relationships between members are not copied
     * @param attachedPairs the pairs of elements already linked by each type of relationship
     * @param singletonEndsUsed the relationship types that have used up their single permitted end
     *
     * @throws Exception the creation failed - reported by the caller
     */
    private void combineRelationship(String                 consolidatedElementGUID,
                                     RelatedMetadataElement relatedElement,
                                     Set<String>            memberGUIDs,
                                     Set<String>            attachedPairs,
                                     Set<String>            singletonEndsUsed) throws Exception
    {
        if ((relatedElement == null) || (relatedElement.getType() == null) || (relatedElement.getElement() == null))
        {
            return;
        }

        String relationshipTypeName = relatedElement.getType().getTypeName();
        String otherElementGUID     = relatedElement.getElement().getElementGUID();

        /*
         * The duplicate processing relationships are not copied, and neither are the relationships between the
         * members of this cluster - they describe the members' relationship to each other, not to the world.
         */
        if ((excludedRelationshipTypes.contains(relationshipTypeName)) || (memberGUIDs.contains(otherElementGUID)))
        {
            return;
        }

        /*
         * The consolidated element takes the place of the member, so it sits at the end of the relationship that
         * the member was at.  getElementAtEnd1() describes the *other* element, so the member - and therefore the
         * consolidated element - is at the opposite end.
         */
        boolean consolidatedElementAtEnd1 = ! relatedElement.getElementAtEnd1();

        String end1GUID = consolidatedElementAtEnd1 ? consolidatedElementGUID : otherElementGUID;
        String end2GUID = consolidatedElementAtEnd1 ? otherElementGUID : consolidatedElementGUID;

        /*
         * Never create the same relationship between the same two elements twice.
         */
        String pairKey = relationshipTypeName + "::" + end1GUID + "::" + end2GUID;

        if (! attachedPairs.add(pairKey))
        {
            return;
        }

        /*
         * Where the type only permits one relationship at the consolidated element's end, the first one wins.
         * The members are processed latest first, so that is the latest member's relationship.
         */
        if (this.isSingletonEnd(relationshipTypeName, consolidatedElementAtEnd1))
        {
            String singletonKey = relationshipTypeName + "::" + (consolidatedElementAtEnd1 ? "1" : "2");

            if (! singletonEndsUsed.add(singletonKey))
            {
                return;
            }
        }

        integrationContext.getOpenMetadataStore().createRelatedElementsInStore(relationshipTypeName,
                                                                                end1GUID,
                                                                                end2GUID,
                                                                                this.getRawMakeAnchorOptions(integrationContext.getOpenMetadataStore()),
                                                                                this.getRelationshipProperties(relatedElement));
    }


    /**
     * Determine whether a relationship type only permits the element at the requested end to have one relationship
     * of this type.  The cardinality of an end describes how many elements can be attached at that end, so the
     * limit on the element at end one is the cardinality recorded on end two, and the other way around.
     *
     * @param relationshipTypeName name of the relationship type
     * @param atEnd1 is the element of interest at end one?
     * @return boolean flag - true means only one relationship of this type is permitted
     *
     * @throws Exception the type could not be retrieved - reported by the caller
     */
    private boolean isSingletonEnd(String  relationshipTypeName,
                                   boolean atEnd1) throws Exception
    {
        OpenMetadataRelationshipDef relationshipDef = this.getRelationshipTypeDef(relationshipTypeName);

        if (relationshipDef == null)
        {
            return false;
        }

        if (atEnd1)
        {
            return (relationshipDef.getEndDef2() != null) &&
                    (relationshipDef.getEndDef2().getAttributeCardinality() == OpenMetadataRelationshipEndCardinality.AT_MOST_ONE);
        }

        return (relationshipDef.getEndDef1() != null) &&
                (relationshipDef.getEndDef1().getAttributeCardinality() == OpenMetadataRelationshipEndCardinality.AT_MOST_ONE);
    }


    /**
     * Retrieve the definition of a relationship type, caching it for the other relationships of the same type.
     *
     * @param relationshipTypeName name of the relationship type
     * @return type definition or null if it is not a relationship type
     *
     * @throws Exception the type could not be retrieved - reported by the caller
     */
    private OpenMetadataRelationshipDef getRelationshipTypeDef(String relationshipTypeName) throws Exception
    {
        if (relationshipTypeDefs.containsKey(relationshipTypeName))
        {
            return relationshipTypeDefs.get(relationshipTypeName);
        }

        OpenMetadataTypeDef         typeDef         = integrationContext.getOpenMetadataTypesClient().getTypeDefByName(false, true, relationshipTypeName);
        OpenMetadataRelationshipDef relationshipDef = null;

        if (typeDef instanceof OpenMetadataRelationshipDef retrievedRelationshipDef)
        {
            relationshipDef = retrievedRelationshipDef;
        }

        relationshipTypeDefs.put(relationshipTypeName, relationshipDef);

        return relationshipDef;
    }


    /**
     * Return the properties to use on the copied relationship.
     *
     * @param relatedElement the member's relationship
     * @return properties or null if the relationship has none
     */
    private NewElementProperties getRelationshipProperties(RelatedMetadataElement relatedElement)
    {
        if (relatedElement.getRelationshipProperties() == null)
        {
            return null;
        }

        return new NewElementProperties(relatedElement.getRelationshipProperties());
    }


    /**
     * Return the options to use when writing a relationship, asking for the elements named at each end to be
     * taken literally rather than resolved to whatever their duplicates combine into.
     * <br>
     * Without this, a member of a cluster that is already being combined is resolved on the way in: the link
     * meant for the second member is created against the surviving element instead, and once the consolidated
     * element is the survivor the link degenerates into one from that element to itself.
     *
     * @param client client whose metadata source options are being built on
     * @return options
     */
    private MakeAnchorOptions getRawMakeAnchorOptions(ConnectorContextClientBase client)
    {
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(client.getMetadataSourceOptions());

        makeAnchorOptions.setForDuplicateProcessing(true);

        return makeAnchorOptions;
    }


    /**
     * Return query options that ask for the duplicates as they are stored, rather than combined.  Reading a
     * combined view here would report a member's relationships as the whole cluster's, so a cluster that has
     * already been consolidated would look as though each member had one link per member.
     *
     * @param startFrom where to start the page
     * @return query options
     */
    private QueryOptions getRawQueryOptions(int startFrom)
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(true);
        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(integrationContext.getMaxPageSize());

        return queryOptions;
    }


    /**
     * Return get options that ask for the element as it is stored, rather than combined with its duplicates.
     *
     * @return get options
     */
    private GetOptions getRawGetOptions()
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForDuplicateProcessing(true);

        return getOptions;
    }


    /**
     * Return the time that an element was last changed, so that the members of a cluster can be ordered.
     *
     * @param element element to test
     * @return update time, creation time if it has never been updated, or null if neither is available
     */
    private Date getLastUpdateTime(OpenMetadataElement element)
    {
        if (element.getVersions() != null)
        {
            if (element.getVersions().getUpdateTime() != null)
            {
                return element.getVersions().getUpdateTime();
            }

            return element.getVersions().getCreateTime();
        }

        return null;
    }
}
