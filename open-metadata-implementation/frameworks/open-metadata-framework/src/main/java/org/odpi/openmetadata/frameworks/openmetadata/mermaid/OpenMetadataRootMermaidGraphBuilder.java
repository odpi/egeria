/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;


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
     */
    public OpenMetadataRootMermaidGraphBuilder(OpenMetadataRootElement openMetadataRootElement)
    {
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
        mermaidGraph.append(" - ");
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
     * Add the content of an open metadata element to the mermaid graph.
     *
     * @param openMetadataRootElement content
     */
    protected void addElementToMermaidGraph(OpenMetadataRootElement openMetadataRootElement)
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
                                   getVisualStyleForClassifications(openMetadataRootElement.getElementHeader(),
                                                                    VisualStyle.ANCHOR_ELEMENT));

        /*
         * Area 0
         */
        super.addRelatedElementSummaries(openMetadataRootElement.getSampleData(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSourcesOfSampleData(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getTemplateCreatedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getSourcedFromTemplate(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getTemplatesForCataloguing(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedImplementationTypes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        
        super.addRelatedElementSummaries(openMetadataRootElement.getSearchKeywords(), VisualStyle.TAG, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getKeywordElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getActionSource(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getRequestedActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getActionCause(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getRelatedActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getActionTargets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getActionsForTarget(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getReferencingElements(), VisualStyle.EXTERNAL_REFERENCE, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAlsoKnownAs(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getEquivalentElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getRecognizedExternalIdentifiers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getIdentifierScopedTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getResourceList(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getResourceListUsers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPropertyFacets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getFacetedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getMemberOfCollections(), VisualStyle.COLLECTION, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCollectionMembers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getKnownLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLocalResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPeerLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getNestedLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getGroupingLocations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getServerEndpoints(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getServerForEndpoint(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getDeployedTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getHostedITAssets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getStorageVolumes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getHostsUsingStorageVolume(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getCapabilityConsumedAssets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getConsumedByCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedSoftwareCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCapabilityHostedBy(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getVisibleInNetworks(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getVisibleEndpoints(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        /*
         * Area 1
         */
        super.addRelatedElementSummaries(openMetadataRootElement.getUserIdentities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getUserProfile(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getContactDetails(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getContacts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPeerPersons(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getSuperTeam(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSubTeams(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getProfilesForAsset(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssetsUsingProfile(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPerformsRoles(), VisualStyle.GOVERNANCE_ACTOR, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getRolePerformers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getContributionRecord(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getContributorProfile(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getRelevantToScope(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getScopedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssignmentScope(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssignedActors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getDependentProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDependsOnProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getManagingProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getManagedProjects(), VisualStyle.PROJECT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getLikes(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getLikedElement(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getInformalTags(), VisualStyle.TAG, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getTaggedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getReviews(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getReviewedElement(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getComments(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getCommentedOnElement(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAnsweredQuestions(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAcceptedAnswers(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getCrowdSourcedContributions(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCrowdSourcingContributors(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getNoteLogs(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getNoteLogSubjects(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getNoteLogEntries(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPresentInNoteLogs(), VisualStyle.FEEDBACK, openMetadataRootElement.getElementHeader().getGUID());

        /*
         * Area 2
         */
        super.addRelatedElementSummary(openMetadataRootElement.getConnectorType(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
        super.addRelatedElementSummary(openMetadataRootElement.getEndpoint(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID(), LineStyle.NORMAL);
        super.addRelatedElementSummaries(openMetadataRootElement.getConnectedAssets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getConnections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getEmbeddedConnections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getParentConnections(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedDataSets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDataSetContent(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAPIEndpoints(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedAPIs(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getParentProcesses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getChildProcesses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPortOwningProcesses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPortDelegatingFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPortDelegatingTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getHomeFolder(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getNestedFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLinkedFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLinkedFolders(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getParentFolder(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getNestedFolders(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getLinkedMediaFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedLogs(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedLogSubjects(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getArchiveContents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPackagedInArchiveFiles(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getReportOriginator(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getGeneratedReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getReportSubjects(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPriorReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getFollowOnReports(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        /*
         * Area 3
         */
        super.addRelatedElementSummaries(openMetadataRootElement.getRelatedTerms(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInContexts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getContextRelevantTerms(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getMeaningForDataElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMeanings(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSemanticDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSemanticallyAssociatedDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSupplementaryProperties(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getSupplementsElement(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        /*
         * Area 4
         */

        super.addRelatedElementSummaries(openMetadataRootElement.getGovernedBy(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getGovernedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPeerGovernanceDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedGovernanceDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportingGovernanceDefinitions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedSecurityGroups(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInAccessControls(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getControlsZones(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getInheritsFromZone(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getNestedSubjectAreas(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getBroaderSubjectArea(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getMetrics(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMeasurements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getMonitoredThrough(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMonitoredResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getInterestingNotificationTypes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSubscribers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getCalledFromGovernanceEngines(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedGovernanceServices(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedGovernanceActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPredefinedTargetForAction(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getTriggeredFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getFirstStep(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDependedOnProcessSteps(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getFollowOnProcessSteps(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportsGovernanceActions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getGovernanceActionExecutor(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getLicenses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLicensedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getCertifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCertifiedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        /*
         * Area 5
         */
        super.addRelatedElementSummary(openMetadataRootElement.getRootSchemaType(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getDescribesStructureForAsset(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getParentSchemaElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSchemaOptions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSchemaAttributes(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getMapFromElement(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getMapToElement(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getQueries(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getLinkedToPrimaryKey(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getForeignKeys(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getVertices(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getEdges(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getDescribedByDataClass(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getDataClassDefinition(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssignedToDataClass(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssignedDataClasses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getSuperDataClass(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSubDataClasses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMadeOfDataClasses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPartOfDataClasses(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getValidValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getValidValueConsumers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getReferenceValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssignedItems(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMatchingValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getConsistentValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getValidValueMembers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMemberOfValidValueSets(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getValidValueImplementations(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCanonicalValidValues(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSpecificationProperties(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSpecificationPropertyUsers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getDataStructureDefinition(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInCertifications(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getContainsDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getPartOfDataStructures(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getParentDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getNestedDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLinkedToDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLinkedFromDataFields(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getDerivedFromDataStructure(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getEquivalentSchemaType(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getDerivedFromDataField(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getEquivalentSchemaAttribute(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        /*
         * Area 6
         */

        /*
         * Area 7
         */
        super.addRelatedElementSummaries(openMetadataRootElement.getUsedByDigitalProducts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getUsesDigitalProducts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());


        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementItems(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementContents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementActors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getInvolvedInAgreements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getContracts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementsForContract(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getDigitalSubscribers(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDigitalSubscriptions(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getUsesDigitalServices(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getConsumingBusinessCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDependsOnBusinessCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportsBusinessCapabilities(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSupplyTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupplyFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getNestedSolutionComponents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInSolutionComponents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getInteractingWithSolutionComponents(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getInteractingWithActors(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getSolutionComponent(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSolutionPorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getWiredTo(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getAlignsToPort(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDelegationPorts(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getDescribesSolutionPortData(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getSolutionPortSchema(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getDerivedFrom(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getImplementedBy(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInImplementationOf(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getImplementationResources(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getDescribesDesignOf(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSolutionDesigns(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getLineageLinkage(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());


        /*
         * Everything else
         */

        super.addRelatedElementSummaries(openMetadataRootElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, openMetadataRootElement.getElementHeader().getGUID());

    }
}
