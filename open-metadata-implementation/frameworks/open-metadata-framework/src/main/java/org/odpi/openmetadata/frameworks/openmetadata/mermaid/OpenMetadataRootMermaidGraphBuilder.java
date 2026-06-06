/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootHierarchy;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedBy;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.List;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's attributed
 * metadata element.
 */
public class OpenMetadataRootMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param openMetadataRootElement content
     * @param maxNodeCount             maximum nodes linked by a particular relationship to an element to include in the graph
     */
    public OpenMetadataRootMermaidGraphBuilder(OpenMetadataRootElement openMetadataRootElement,
                                               int                     maxNodeCount)
    {
        super(maxNodeCount);

        /*
         * Add the graph title
         */
        String currentNodeName    = openMetadataRootElement.getElementHeader().getGUID();
        String currentDisplayName = currentNodeName;

        if (openMetadataRootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            currentDisplayName = this.getNodeDisplayName(openMetadataRootElement.getElementHeader(),
                                                         referenceableProperties);
        }

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: ");
        mermaidGraph.append(openMetadataRootElement.getElementHeader().getType().getTypeName());
        if (openMetadataRootElement instanceof OpenMetadataRootHierarchy)
        {
            mermaidGraph.append(" Hierarchy - ");
        }
        else
        {
            mermaidGraph.append(" - ");
        }
        if (currentDisplayName != null)
        {
            mermaidGraph.append(currentDisplayName);
            mermaidGraph.append(" [");
        }
        else
        {
            mermaidGraph.append(currentNodeName);
            mermaidGraph.append(" [");
        }

        mermaidGraph.append(openMetadataRootElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        /*
         * Add the graph content
         */
        this.addElementToMermaidGraph(openMetadataRootElement);
    }


    /**
     * Construct a mermaid markdown graph.
     *
     * @param searchString             title
     * @param openMetadataRootElements content
     * @param maxNodeCount             maximum nodes linked by a particular relationship to an element to include in the graph
     */
    public OpenMetadataRootMermaidGraphBuilder(String                        searchString,
                                               List<OpenMetadataRootElement> openMetadataRootElements,
                                               int                           maxNodeCount)
    {
        super(maxNodeCount);

        /*
         * Add the graph title
         */
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Results matching: ");
        mermaidGraph.append(searchString);
        mermaidGraph.append("\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        /*
         * Add the graph content
         */
        if (openMetadataRootElements != null)
        {
            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                this.addElementToMermaidGraph(openMetadataRootElement);
            }
        }
    }


    /**
     * Add the content of an open metadata element to the mermaid graph.
     *
     * @param relatedByNode content
     */
    protected void addRelatedByToMermaidGraph(String    startingElementGUID,
                                              RelatedBy relatedByNode)
    {
        if ((relatedByNode != null) && (relatedByNode.getAssociatedElements() != null))
        {
            for (String propertyName : relatedByNode.getAssociatedElements().keySet())
            {
                if (relatedByNode.getAssociatedElements().get(propertyName) != null)
                {
                    super.appendNewMermaidNode(relatedByNode.getAssociatedElements().get(propertyName),
                                               VisualStyle.GOVERNANCE_ACTOR);

                    super.appendMermaidThinLine(null,
                                                startingElementGUID,
                                                propertyName,
                                                relatedByNode.getAssociatedElements().get(propertyName).getElementHeader().getGUID());
                }
            }
        }
    }


    /**
     * Add the content of an open metadata element to the mermaid graph.
     *
     * @param openMetadataRootElement content
     */
    protected void addElementToMermaidGraph(OpenMetadataRootElement openMetadataRootElement)
    {
        if (openMetadataRootElement != null)
        {
            String currentDisplayName = openMetadataRootElement.getElementHeader().getGUID();

            if (openMetadataRootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
            {
                currentDisplayName = this.getNodeDisplayName(openMetadataRootElement.getElementHeader(),
                                                             referenceableProperties);
            }

            /*
             * Add details of the primary node.
             */
            super.appendNewMermaidNode(openMetadataRootElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       openMetadataRootElement.getElementHeader().getType().getTypeName(),
                                       openMetadataRootElement.getProperties(),
                                       getVisualStyleForClassifications(openMetadataRootElement.getElementHeader(),
                                                                        VisualStyle.ANCHOR_ELEMENT));

            /*
             * Add related by details
             */
            addRelatedByToMermaidGraph(openMetadataRootElement.getElementHeader().getGUID(), openMetadataRootElement.getRelatedBy());

            /*
             * Area 0
             */
            super.addRelatedElementSummaries(openMetadataRootElement.getSampleData(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSourcesOfSampleData(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getTemplateCreatedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getSourcedFromTemplate(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getTemplatesForCataloguing(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getTemplateUses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getSearchKeywords(), VisualStyle.TAG, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getKeywordElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getActionSource(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getRequestedActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getActionCause(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getRelatedActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getActionTargets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getActionsForTarget(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getReferencingElements(), VisualStyle.EXTERNAL_REFERENCE, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getAlsoKnownAs(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getEquivalentElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getResourceList(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getResourceListUsers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDescribes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getProvidesMoreInformation(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getPropertyFacets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getFacetedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getMemberOfCollections(), VisualStyle.COLLECTION, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getCollectionMembers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getKnownLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getLocalResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPeerLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getNestedLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getGroupingLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getServerEndpoints(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getServerForEndpoint(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDeployedTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getHostedITAssets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getStorageVolumes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getProvidesStorageFor(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCapabilityConsumedAssets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConsumedByCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getHostedByDeployedITInfrastructure(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCohortMembership(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getRegisteredWithCohorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getVisibleInNetworks(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getVisibleEndpoints(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            /*
             * Area 1
             */
            super.addRelatedElementSummaries(openMetadataRootElement.getUserIdentities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getUserProfile(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getContactDetails(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getContacts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getMyFollowers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMyPeers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getSuperTeam(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSubTeams(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getProfilesForAsset(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssetsUsingProfile(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getPerformsRoles(), VisualStyle.GOVERNANCE_ACTOR, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getRolePerformers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getContributionRecord(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getContributorProfile(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getRelevantToScopes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getScopedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssignmentScope(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssignedActors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDependentProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDependsOnProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getManagingProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getManagedProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getLikes(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getLikedElement(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getInformalTags(), VisualStyle.TAG, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getTaggedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getReviews(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getReviewedElement(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getComments(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getCommentedOnElement(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAnsweredQuestions(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAcceptedAnswers(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCrowdSourcedContributions(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getCrowdSourcingContributors(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getNoteLogs(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getNoteLogSubjects(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getNoteLogEntries(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPresentInNoteLogs(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            /*
             * Area 2
             */
            super.addRelatedElementSummary(openMetadataRootElement.getConnectorType(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getEndpoint(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConnectedAssets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConnections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getEmbeddedConnections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getParentConnections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getSupportedDataSets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDataSetContent(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getAPIEndpoints(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupportedAPIs(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getParentProcesses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getChildProcesses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getPorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPortOwningProcesses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPortDelegatingFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getPortDelegatingTo(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getHomeFolder(),null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getNestedFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getLinkedFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getLinkedFolders(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getParentFolder(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getNestedFolders(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getLinkedMediaFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedLogs(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedLogSubjects(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getArchiveContents(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPackagedInArchiveFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getReportOriginator(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getGeneratedReports(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getReportSubjects(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPriorReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getFollowOnReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            /*
             * Area 3
             */
            super.addRelatedElementSummaries(openMetadataRootElement.getRelatedTerms(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getUsedInContexts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getContextRelevantTerms(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getMeaningForDataElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMeanings(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getSemanticDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSemanticallyAssociatedDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getSupplementaryProperties(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getSupplementsElement(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            /*
             * Area 4
             */

            super.addRelatedElementSummaries(openMetadataRootElement.getGovernedBy(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getGovernedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getPeerGovernanceDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupportedGovernanceDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupportingGovernanceDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getUserAccounts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConfiguredInSecurityCollections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSecurityAccessControls(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDefinedInSecretsCollection(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSecurityLists(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getListedInSecretsCollection(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedSecurityLists(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getUsedInAccessControls(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getControlsZones(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getInheritsFromZone(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getNestedSubjectAreas(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getBroaderSubjectArea(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getMetrics(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMeasurements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getMonitoredThrough(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMonitoredResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getInterestingNotificationTypes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSubscribers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getExcludedFromRequirements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getExceptions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCalledFromGovernanceEngines(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupportedGovernanceServices(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedGovernanceActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPredefinedTargetForAction(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getTriggeredFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getFirstStep(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDependedOnProcessSteps(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getFollowOnProcessSteps(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupportsGovernanceActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getGovernanceActionExecutor(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getSupportedIntegrationConnectors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getIncludedInIntegrationGroups(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCatalogTargets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getRefreshedByConnectors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getPeerDuplicateOrigin(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPeerDuplicatePartner(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getConsolidatedDuplicateOrigin(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConsolidatedDuplicateResult(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getIncidentReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getImpactedResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getLicenses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getLicensedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getCertifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getCertifiedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            /*
             * Area 5
             */
            super.addRelatedElementSummary(openMetadataRootElement.getSchemaType(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getDescribesStructure(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getParentSchemaElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSchemaOptions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSchemaAttributes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getMapFromElement(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getMapToElement(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getQueries(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getContainsOperations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getAPIHeader(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getAPIRequest(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getAPIResponse(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getLinkedToPrimaryKey(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getForeignKeys(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getVertices(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getEdges(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDescribedByDataValueSpecifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDataValueSpecifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssignedToDataValueSpecifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssignedDataValueSpecifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getSuperDataValueSpecification(), null,VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSubDataValueSpecifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMadeOfDataClasses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPartOfDataClasses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getValidValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getValidValueConsumers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getReferenceValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssignedItems(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMatchingValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConsistentValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getValidValueMembers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMemberOfValidValueSets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getValidValueImplementations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getCanonicalValidValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSpecificationProperties(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSpecificationPropertyUsers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getDataStructureDefinition(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getUsedInCertifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getContainsDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPartOfDataStructures(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDataDescription(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDescribesDataFor(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getParentDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getNestedDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getLinkedToDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getLinkedFromDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getDerivedFromDataStructure(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getEquivalentSchemaType(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getDerivedFromDataField(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getEquivalentSchemaAttribute(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getRelatedDesignPatterns(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConsumedDesignPatterns(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConsumingDesignPatterns(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSpecializedDesignPattern(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getGeneralizedDesignPattern(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            /*
             * Area 6
             */
            super.addRelatedElementSummaries(openMetadataRootElement.getReportedAnnotations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getFromSurveyReport(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAnnotationExtensions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getPreviousAnnotations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedAnnotations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAnnotationSubjects(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAnnotationMatches(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getMatchedByAnnotations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getResourceProfileData(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getResourceProfileAnnotations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getRequestForActionTargets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getIdentifiedByRequestForActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);


            /*
             * Area 7
             */
            super.addRelatedElementSummaries(openMetadataRootElement.getUsedByDigitalProducts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getUsesDigitalProducts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getAgreementItems(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAgreementContents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAgreementActors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getInvolvedInAgreements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getContracts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getAgreementsForContract(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDigitalSubscribers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDigitalSubscriptions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getUsesDigitalServices(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getConsumingBusinessCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDependsOnBusinessCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupportsBusinessCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getSupplyTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSupplyFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getNestedSolutionComponents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getUsedInSolutionComponents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getInteractingWithSolutionComponents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getInteractingWithActors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummary(openMetadataRootElement.getSolutionComponent(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSolutionPorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getWiredTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummary(openMetadataRootElement.getAlignsToPort(), null, VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getDelegationPorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDerivedFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getImplementedBy(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getUsedInImplementationOf(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getImplementationResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getDescribesDesignOf(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(openMetadataRootElement.getSolutionDesigns(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.addRelatedElementSummaries(openMetadataRootElement.getLineageLinkage(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);


            /*
             * Everything else
             */

            super.addRelatedElementSummaries(openMetadataRootElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
        }
    }
}
