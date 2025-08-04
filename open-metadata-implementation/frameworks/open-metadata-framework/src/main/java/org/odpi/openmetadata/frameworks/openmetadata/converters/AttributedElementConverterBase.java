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
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @throws PropertyServerException problem in converter
     */
    protected void addRelationshipsToBean(Class<B>                     beanClass,
                                          List<RelatedMetadataElement> relatedMetadataElements,
                                          AttributedMetadataElement    attributedMetadataElement) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<String> processedRelationshipTypes = new ArrayList<>();

            /*
             * Area 0
             */

            attributedMetadataElement.setSearchKeywords(super.getRelatedElements(beanClass, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setKeywordElements(super.getRelatedElements(beanClass, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setExternalReferences(super.getRelatedElements(beanClass, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setReferencingElements(super.getRelatedElements(beanClass, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setAlsoKnownAs(super.getRelatedElements(beanClass, OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setEquivalentElements(super.getRelatedElements(beanClass, OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setRecognizedExternalIdentifiers(super.getRelatedElements(beanClass, OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setIdentifierScopedTo(super.getRelatedElements(beanClass, OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setResourceList(super.getRelatedElements(beanClass, OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setResourceListUsers(super.getRelatedElements(beanClass, OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);

            attributedMetadataElement.setPropertyFacets(super.getRelatedElements(beanClass, OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setFacetedElements(super.getRelatedElements(beanClass, OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName);

            attributedMetadataElement.setCollectionMembers(super.getRelatedElements(beanClass, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMemberOfCollections(super.getRelatedElements(beanClass, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

            /*
             * Area 1
             */

            attributedMetadataElement.setContactDetails(super.getRelatedElements(beanClass, OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setContacts(super.getRelatedElements(beanClass, OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelevantToScope(super.getRelatedElements(beanClass, OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setScopedElements(super.getRelatedElements(beanClass, OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName);

            attributedMetadataElement.setAssignmentScope(super.getRelatedElements(beanClass, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssignedActors(super.getRelatedElements(beanClass, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setCommissionedElements(super.getRelatedElements(beanClass, OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCommissionedBy(super.getRelatedElements(beanClass, OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName);

            attributedMetadataElement.setLikes(super.getRelatedElements(beanClass, OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLikedElement(super.getRelatedElement(beanClass, OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName);

            attributedMetadataElement.setInformalTags(super.getRelatedElements(beanClass, OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setTaggedElements(super.getRelatedElements(beanClass, OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName);

            attributedMetadataElement.setReviews(super.getRelatedElements(beanClass, OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setReviewedElement(super.getRelatedElement(beanClass, OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName);

            attributedMetadataElement.setComments(super.getRelatedElements(beanClass, OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCommentedOnElement(super.getRelatedElement(beanClass, OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName);

            /*
             * Area 3
             */

            attributedMetadataElement.setTerms(super.getRelatedElements(beanClass, OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentGlossary(super.getRelatedElement(beanClass, OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName);

            attributedMetadataElement.setCategories(super.getRelatedElements(beanClass, OpenMetadataType.CATEGORY_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssociatedGlossaries(super.getRelatedElements(beanClass, OpenMetadataType.CATEGORY_HIERARCHY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CATEGORY_HIERARCHY_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(beanClass, OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(beanClass, OpenMetadataType.SYNONYM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.SYNONYM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(beanClass, OpenMetadataType.ANTONYM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ANTONYM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(beanClass, OpenMetadataType.PREFERRED_TERM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.PREFERRED_TERM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(beanClass, OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP.typeName);

            attributedMetadataElement.setRelatedTerms(super.getRelatedElements(beanClass, OpenMetadataType.ISA_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ISA_RELATIONSHIP.typeName);

            attributedMetadataElement.setMeanings(super.getRelatedElements(beanClass, OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMeaningForDataElements(super.getRelatedElements(beanClass, OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setSemanticDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSemanticallyAssociatedDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName);

            attributedMetadataElement.setSupplementaryProperties(super.getRelatedElements(beanClass, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupplementsElement(super.getRelatedElement(beanClass, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName);

            /*
             * Area 4
             */

            attributedMetadataElement.setGovernedBy(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setGovernedElements(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName);

            attributedMetadataElement.setPeerGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName);
            attributedMetadataElement.setPeerGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName);
            attributedMetadataElement.setPeerGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setSupportingGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName);
            attributedMetadataElement.setSupportingGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSupportedGovernanceDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setMetrics(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMeasuredDefinitions(super.getRelatedElements(beanClass, OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName);

            attributedMetadataElement.setLicenses(super.getRelatedElements(beanClass, OpenMetadataType.LICENSE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLicensedElements(super.getRelatedElements(beanClass, OpenMetadataType.LICENSE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.LICENSE_RELATIONSHIP.typeName);

            attributedMetadataElement.setCertifications(super.getRelatedElements(beanClass, OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCertifiedElements(super.getRelatedElements(beanClass, OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setAgreementActors(super.getRelatedElements(beanClass, OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setInvolvedInAgreements(super.getRelatedElements(beanClass, OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName);

            attributedMetadataElement.setAgreementItems(super.getRelatedElements(beanClass, OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAgreementContents(super.getRelatedElements(beanClass, OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName);

            attributedMetadataElement.setContracts(super.getRelatedElements(beanClass, OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAgreementsForContract(super.getRelatedElements(beanClass, OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName);


            /*
             * Area 5
             */
            attributedMetadataElement.setSchemaOptions(super.getRelatedElements(beanClass, OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName);

            attributedMetadataElement.setRootSchemaType(super.getRelatedElement(beanClass, OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setDescribesStructureForAsset(super.getRelatedElement(beanClass, OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setSchemaAttributes(super.getRelatedElements(beanClass, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

            attributedMetadataElement.setSchemaAttributes(super.getRelatedElements(beanClass, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

            attributedMetadataElement.setExternalSchemaType(super.getRelatedElement(beanClass, OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setMapToElement(super.getRelatedElement(beanClass, OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setMapFromElement(super.getRelatedElement(beanClass, OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName);

            attributedMetadataElement.setQueries(super.getRelatedElements(beanClass, OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setParentSchemaElements(super.getRelatedElements(beanClass, OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);

            attributedMetadataElement.setForeignKeys(super.getRelatedElements(beanClass, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setLinkedToPrimaryKey(super.getRelatedElement(beanClass, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName);

            attributedMetadataElement.setVertices(super.getRelatedElements(beanClass, OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setEdges(super.getRelatedElements(beanClass, OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName);

            attributedMetadataElement.setValidValues(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setValidValueConsumers(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setReferenceValues(super.getRelatedElements(beanClass, OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setAssignedItems(super.getRelatedElements(beanClass, OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setMatchingValues(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName);

            attributedMetadataElement.setConsistentValues(super.getRelatedElements(beanClass, OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName);

            attributedMetadataElement.setAssociatedValues(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName);

            attributedMetadataElement.setValidValueMembers(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setMemberOfValidValueSets(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName);

            attributedMetadataElement.setValidValueImplementations(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setCanonicalValidValues(super.getRelatedElements(beanClass, OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName);

            attributedMetadataElement.setSpecificationProperties(super.getRelatedElements(beanClass, OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setSpecificationPropertyUsers(super.getRelatedElements(beanClass, OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName);

            /*
             * Area 7
             */
            attributedMetadataElement.setUsesDigitalProducts(super.getRelatedElements(beanClass, OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setUsedByDigitalProducts(super.getRelatedElements(beanClass, OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName);

            attributedMetadataElement.setDigitalSubscriptions(super.getRelatedElements(beanClass, OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setDigitalSubscribers(super.getRelatedElements(beanClass, OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName);

            attributedMetadataElement.setImplementedBy(super.getRelatedElements(beanClass, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setDerivedFrom(super.getRelatedElements(beanClass, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName);

            attributedMetadataElement.setImplementationResources(super.getRelatedElements(beanClass, OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName, relatedMetadataElements, false));
            attributedMetadataElement.setUsedInImplementationOf(super.getRelatedElements(beanClass, OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName, relatedMetadataElements, true));
            processedRelationshipTypes.add(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.ULTIMATE_SOURCE.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ULTIMATE_SOURCE.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.ULTIMATE_DESTINATION.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.ULTIMATE_DESTINATION.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName);
            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);

            attributedMetadataElement.setLineageLinkage(super.getRelatedElements(beanClass, OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, relatedMetadataElements));
            processedRelationshipTypes.add(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName);


            /*
             * Everything else
             */
            attributedMetadataElement.setOtherRelatedElements(super.getOtherRelatedElements(beanClass,
                                                                                            relatedMetadataElements,
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
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(relatedMetadataElement)";

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
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>                 beanClass,
                        OpenMetadataElement      element,
                        OpenMetadataRelationship relationship,
                        String                   methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity, relationship)";

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
            this.addRelationshipsToBean(beanClass, relationships, bean);
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
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>                     beanClass,
                               RelatedMetadataElement       primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(beanClass, relationships, bean);
            bean.setRelatedBy(super.getRelatedBy(beanClass, primaryElement, methodName));
        }

        return returnBean;
    }
}
