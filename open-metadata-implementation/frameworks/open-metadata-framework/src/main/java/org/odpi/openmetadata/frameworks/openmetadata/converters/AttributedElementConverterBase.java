/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AttributedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * AttributedElementConverterBase provides common methods for transferring relevant properties from an Open Metadata Element
 * object into a bean that inherits from AttributedMetadataElement.
 */
public class AttributedElementConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AttributedElementConverterBase(PropertyHelper propertyHelper,
                                          String         serviceName,
                                          String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Summarize the relationships that have no special processing by the subtype.
     *
     * @param relatedMetadataElements elements to summarize
     * @throws PropertyServerException problem in converter
     */
    protected void addRelationshipsToBean(List<RelatedMetadataElement> relatedMetadataElements,
                                          AttributedMetadataElement    attributedMetadataElement) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<String> processedRelationshipTypes = new ArrayList<>();

            /*
             * Area 0
             */

            attributedMetadataElement.setSampleData(super.getRelatedElements(OpenMetadataType.SAMPLE_DATA_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSourcesOfSampleData(super.getRelatedElements(OpenMetadataType.SAMPLE_DATA_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SAMPLE_DATA_RELATIONSHIP.typeName);

            attributedMetadataElement.setSourcedFromTemplate(super.getRelatedElement(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setTemplateCreatedElements(super.getRelatedElements(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName);

            attributedMetadataElement.setTemplatesForCataloguing(super.getRelatedElements(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedImplementationTypes(super.getRelatedElements(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName);

            attributedMetadataElement.setSearchKeywords(super.getRelatedElements(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setKeywordElements(super.getRelatedElements(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setRequestedActions(super.getRelatedElements(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setActionSource(super.getRelatedElement(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);

            attributedMetadataElement.setTrackedActions(super.getRelatedElements(OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setActionSponsors(super.getRelatedElements(OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedActions(super.getRelatedElements(OpenMetadataType.ACTIONS_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setActionCause(super.getRelatedElements(OpenMetadataType.ACTIONS_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ACTIONS_RELATIONSHIP.typeName);

            attributedMetadataElement.setActionTargets(super.getRelatedElements(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setActionsForTarget(super.getRelatedElements(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName);

            attributedMetadataElement.setExternalReferences(super.getRelatedElements(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setReferencingElements(super.getRelatedElements(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setAlsoKnownAs(super.getRelatedElements(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setEquivalentElements(super.getRelatedElements(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setRecognizedExternalIdentifiers(super.getRelatedElements(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setIdentifierScopedTo(super.getRelatedElements(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setResourceList(super.getRelatedElements(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setResourceListUsers(super.getRelatedElements(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);

            attributedMetadataElement.setPropertyFacets(super.getRelatedElements(OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setFacetedElements(super.getRelatedElements(OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName);

            attributedMetadataElement.setCollectionMembers(super.getRelatedElements(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMemberOfCollections(super.getRelatedElements(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

            attributedMetadataElement.setServerEndpoints(super.getRelatedElements(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setEndpointForServer(super.getRelatedElement(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName);

            attributedMetadataElement.setDeployedTo(super.getRelatedElements(OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setHostedITAssets(super.getRelatedElements(OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName);

            attributedMetadataElement.setStorageVolumes(super.getRelatedElements(OpenMetadataType.ATTACHED_STORAGE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setHostsUsingStorageVolume(super.getRelatedElements(OpenMetadataType.ATTACHED_STORAGE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_STORAGE_RELATIONSHIP.typeName);

            attributedMetadataElement.setCapabilityConsumedAssets(super.getRelatedElements(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setConsumedByCapabilities(super.getRelatedElements(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName);

            attributedMetadataElement.setSupportedSoftwareCapabilities(super.getRelatedElements(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setHostedBy(super.getRelatedElements(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

            attributedMetadataElement.setVisibleInNetworks(super.getRelatedElements(OpenMetadataType.VISIBLE_ENDPOINT.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setVisibleEndpoints(super.getRelatedElements(OpenMetadataType.VISIBLE_ENDPOINT.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VISIBLE_ENDPOINT.typeName);

            /*
             * Area 1
             */

            attributedMetadataElement.setContactDetails(super.getRelatedElements(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setContacts(super.getRelatedElements(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelevantToScope(super.getRelatedElements(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setScopedElements(super.getRelatedElements(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName);

            attributedMetadataElement.setAssignmentScope(super.getRelatedElements(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssignedActors(super.getRelatedElements(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setCommissionedElements(super.getRelatedElements(OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCommissionedBy(super.getRelatedElements(OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName);

            attributedMetadataElement.setLikes(super.getRelatedElements(OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLikedElement(super.getRelatedElement(OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName);

            attributedMetadataElement.setInformalTags(super.getRelatedElements(OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setTaggedElements(super.getRelatedElements(OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName);

            attributedMetadataElement.setReviews(super.getRelatedElements(OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setReviewedElement(super.getRelatedElement(OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName);

            attributedMetadataElement.setComments(super.getRelatedElements(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCommentedOnElement(super.getRelatedElement(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName);

            /*
             * Area 2
             */

            attributedMetadataElement.setConnectorType(super.getRelatedElement(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setConnections(super.getRelatedElements(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setEndpoint(super.getRelatedElement(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setConnections(super.getRelatedElements(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

            attributedMetadataElement.setConnectedAssets(super.getRelatedElements(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setConnections(super.getRelatedElements(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName);

            attributedMetadataElement.setEmbeddedConnections(super.getRelatedElements(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentConnections(super.getRelatedElements(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName);



            attributedMetadataElement.setDataSetContent(super.getRelatedElements(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedDataSets(super.getRelatedElements(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setAPIEndpoints(super.getRelatedElements(OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedAPIs(super.getRelatedElements(OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName);

            attributedMetadataElement.setChildProcesses(super.getRelatedElements(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentProcesses(super.getRelatedElements(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);

            attributedMetadataElement.setPorts(super.getRelatedElements(OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setPortOwningProcesses(super.getRelatedElements(OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName);

            attributedMetadataElement.setPortDelegatingTo(super.getRelatedElements(OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setPortDelegatingFrom(super.getRelatedElements(OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setNestedFiles(super.getRelatedElements(OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setHomeFolder(super.getRelatedElement(OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName);

            attributedMetadataElement.setLinkedFiles(super.getRelatedElements(OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLinkedFolders(super.getRelatedElements(OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName);

            attributedMetadataElement.setParentFolder(super.getRelatedElement(OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setNestedFolders(super.getRelatedElements(OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName);

            attributedMetadataElement.setLinkedMediaFiles(super.getRelatedElements(OpenMetadataType.LINKED_MEDIA_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLinkedMediaFiles(super.getRelatedElements(OpenMetadataType.LINKED_MEDIA_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.LINKED_MEDIA_RELATIONSHIP.typeName);

            attributedMetadataElement.setTopicsForSubscribers(super.getRelatedElements(OpenMetadataType.TOPIC_SUBSCRIBERS_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setTopicSubscribers(super.getRelatedElements(OpenMetadataType.TOPIC_SUBSCRIBERS_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.TOPIC_SUBSCRIBERS_RELATIONSHIP.typeName);

            attributedMetadataElement.setAssociatedLogs(super.getRelatedElements(OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssociatedLogSubjects(super.getRelatedElements(OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName);

            attributedMetadataElement.setLocalMetadataCollection(super.getRelatedElement(OpenMetadataType.COHORT_MEMBER_METADATA_COLLECTION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCohortMember(super.getRelatedElement(OpenMetadataType.COHORT_MEMBER_METADATA_COLLECTION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.COHORT_MEMBER_METADATA_COLLECTION_RELATIONSHIP.typeName);

            attributedMetadataElement.setArchiveContents(super.getRelatedElement(OpenMetadataType.ARCHIVE_CONTENTS_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setPackagedInArchiveFiles(super.getRelatedElements(OpenMetadataType.ARCHIVE_CONTENTS_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ARCHIVE_CONTENTS_RELATIONSHIP.typeName);

            /*
             * Area 3
             */

            attributedMetadataElement.setTerms(super.getRelatedElements(OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentGlossary(super.getRelatedElement(OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName);

            attributedMetadataElement.setGlossaryCategories(super.getRelatedElements(OpenMetadataType.CATEGORY_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssociatedGlossaries(super.getRelatedElements(OpenMetadataType.CATEGORY_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CATEGORY_HIERARCHY_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(OpenMetadataType.SYNONYM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.SYNONYM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(OpenMetadataType.ANTONYM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ANTONYM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(OpenMetadataType.PREFERRED_TERM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.PREFERRED_TERM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(OpenMetadataType.ISA_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ISA_RELATIONSHIP.typeName);

            attributedMetadataElement.setUsedInContexts(super.getRelatedElements(OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setContextRelevantTerms(super.getRelatedElements(OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName);

            attributedMetadataElement.setMeanings(super.getRelatedElements(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMeaningForDataElements(super.getRelatedElements(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setSemanticDefinitions(super.getRelatedElements(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSemanticallyAssociatedDefinitions(super.getRelatedElements(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName);

            attributedMetadataElement.setSupplementaryProperties(super.getRelatedElements(OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupplementsElement(super.getRelatedElement(OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName);

            /*
             * Area 4
             */

            attributedMetadataElement.setGovernedBy(super.getRelatedElements(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setGovernedElements(super.getRelatedElements(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName);

            attributedMetadataElement.setPeerGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName);
            attributedMetadataElement.setPeerGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName);
            attributedMetadataElement.setPeerGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setSupportingGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName);
            attributedMetadataElement.setSupportingGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedGovernanceDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setMetrics(super.getRelatedElements(OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMeasuredDefinitions(super.getRelatedElements(OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName);

            attributedMetadataElement.setLicenses(super.getRelatedElements(OpenMetadataType.LICENSE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLicensedElements(super.getRelatedElements(OpenMetadataType.LICENSE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.LICENSE_RELATIONSHIP.typeName);

            attributedMetadataElement.setCertifications(super.getRelatedElements(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCertifiedElements(super.getRelatedElements(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setAgreementActors(super.getRelatedElements(OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setInvolvedInAgreements(super.getRelatedElements(OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName);

            attributedMetadataElement.setAgreementItems(super.getRelatedElements(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAgreementContents(super.getRelatedElements(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName);

            attributedMetadataElement.setContracts(super.getRelatedElements(OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAgreementsForContract(super.getRelatedElements(OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName);


            /*
             * Area 5
             */
            attributedMetadataElement.setSchemaOptions(super.getRelatedElements(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName);

            attributedMetadataElement.setRootSchemaType(super.getRelatedElement(OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setDescribesStructureForAsset(super.getRelatedElement(OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setSchemaAttributes(super.getRelatedElements(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

            attributedMetadataElement.setSchemaAttributes(super.getRelatedElements(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

            attributedMetadataElement.setExternalSchemaType(super.getRelatedElement(OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setMapToElement(super.getRelatedElement(OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setMapFromElement(super.getRelatedElement(OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setQueries(super.getRelatedElements(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);

            attributedMetadataElement.setForeignKeys(super.getRelatedElements(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLinkedToPrimaryKey(super.getRelatedElement(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName);

            attributedMetadataElement.setVertices(super.getRelatedElements(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setEdges(super.getRelatedElements(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setValidValues(super.getRelatedElements(OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setValidValueConsumers(super.getRelatedElements(OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setReferenceValues(super.getRelatedElements(OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssignedItems(super.getRelatedElements(OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setMatchingValues(super.getRelatedElements(OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName);

            attributedMetadataElement.setConsistentValues(super.getRelatedElements(OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName);

            attributedMetadataElement.setAssociatedValues(super.getRelatedElements(OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setValidValueMembers(super.getRelatedElements(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMemberOfValidValueSets(super.getRelatedElements(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName);

            attributedMetadataElement.setValidValueImplementations(super.getRelatedElements(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCanonicalValidValues(super.getRelatedElements(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName);

            attributedMetadataElement.setSpecificationProperties(super.getRelatedElements(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSpecificationPropertyUsers(super.getRelatedElements(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName);

            /*
             * Area 7
             */
            attributedMetadataElement.setUsesDigitalProducts(super.getRelatedElements(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setUsedByDigitalProducts(super.getRelatedElements(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName);

            attributedMetadataElement.setDigitalSubscriptions(super.getRelatedElements(OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setDigitalSubscribers(super.getRelatedElements(OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName);

            attributedMetadataElement.setImplementedBy(super.getRelatedElements(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setDerivedFrom(super.getRelatedElements(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName);

            attributedMetadataElement.setImplementationResources(super.getRelatedElements(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setUsedInImplementationOf(super.getRelatedElements(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.ULTIMATE_SOURCE.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ULTIMATE_SOURCE.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.ULTIMATE_DESTINATION.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ULTIMATE_DESTINATION.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName);


            /*
             * Everything else
             */
            attributedMetadataElement.setOtherRelatedElements(super.getOtherRelatedElements(relatedMetadataElements,
                                                                                            processedRelationshipTypes));
        }
    }


    /**
     * Using the supplied relatedMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement relatedMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the relatedMetadataElement supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, relatedMetadataElement.getElement(), methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relatedMetadataElement, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an element and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>                 beanClass,
                        OpenMetadataElement      element,
                        OpenMetadataRelationship relationship,
                        String                   methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, element, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relationship, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(relationships, bean);
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>                     beanClass,
                               RelatedMetadataElement       primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(relationships, bean);
            bean.setRelatedBy(super.getRelatedBy(beanClass, primaryElement, methodName));
        }

        return returnBean;
    }
}
