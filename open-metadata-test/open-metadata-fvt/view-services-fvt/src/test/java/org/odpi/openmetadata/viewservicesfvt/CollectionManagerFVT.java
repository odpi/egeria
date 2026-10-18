/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeploymentStatusFilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeploymentStatusSearchString;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CollectionMemberStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootHierarchy;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SavedQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionKindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.EditingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.ResultsSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.ScopingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.SkillSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.SmartQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.StagingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssociatedSkillSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataSpecProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementItemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.ContractLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CollectionManagerFVT covers every request of the Collection Manager OMVS through its own URL marker:
 * creating, finding, updating and deleting collections (including from a template), managing their membership
 * and hierarchy, the collection classifications, and the specialist relationships for resource lists, data
 * descriptions, smart queries, skill sets, agreements, contracts and subscribers.
 * <br>
 * The elements at the far end of some relationships (a person, a saved query and a cited document) are not
 * collections, so they are created with Actor Manager, Asset Maker and External Links.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CollectionManagerFVT
{
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    private static final ViewServiceClient actorManager      = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    private static final ViewServiceClient externalLinks     = new ViewServiceClient(ViewServiceDescription.EXTERNAL_LINKS);

    private static final String CATEGORY = ViewServicesFvtTestSupport.qualifiedName("CollectionCategory", "collection-manager");

    private static final List<String> collectionsToDelete = new ArrayList<>();

    private static String collectionGUID = null;
    private static String personGUID     = null;


    /**
     * A new collection can be retrieved with the properties it was created with, and is found by name, category
     * and search string.  An update shows on the next retrieve.
     */
    @Test
    @Order(1)
    void collectionLifecycle()
    {
        CollectionProperties properties = collectionProperties(new CollectionProperties(), "main");

        properties.setPurpose("Hold elements for the collection manager FVT");

        collectionGUID = createCollection(properties);

        OpenMetadataRootElement collection = retrieve(collectionGUID);

        CollectionProperties retrieved = assertInstanceOf(CollectionProperties.class, collection.getProperties());

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals("Hold elements for the collection manager FVT", retrieved.getPurpose());
        assertEquals(CATEGORY, retrieved.getCategory());

        CollectionProperties update = new CollectionProperties();

        update.setDescription("Updated by the collection manager FVT");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        assertTrue(collectionManager.post("/collections/" + collectionGUID + "/update", updateRequestBody, BooleanResponse.class).getFlag(),
                   "updateCollection reported that nothing changed");

        retrieved = assertInstanceOf(CollectionProperties.class, retrieve(collectionGUID).getProperties());

        assertEquals("Updated by the collection manager FVT", retrieved.getDescription());
        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName(), "a merge update should keep the qualified name");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/collections/by-name", byName), collectionGUID),
                   "getCollectionsByName did not return the collection");

        FilterRequestBody byCategory = new FilterRequestBody();

        byCategory.setFilter(CATEGORY);

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/collections/by-collection-category", byCategory), collectionGUID),
                   "getCollectionsByCategory did not return the collection");

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/collections/by-search-string", search), collectionGUID),
                   "findCollections did not return the collection");
    }


    /**
     * A collection created from a template takes the template's properties, with the placeholder in its
     * qualified name replaced.
     */
    @Test
    @Order(2)
    void collectionFromTemplate()
    {
        CollectionProperties templateProperties = collectionProperties(new CollectionProperties(), "template");

        templateProperties.setQualifiedName(templateProperties.getQualifiedName() + "::~{copyName}~");
        templateProperties.setDescription("Template for the collection manager FVT");

        String templateGUID = createCollection(templateProperties);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setPlaceholderPropertyValues(Map.of("copyName", "copy1"));

        String copyGUID = collectionManager.create("/collections/from-template", requestBody);

        collectionsToDelete.add(copyGUID);

        assertNotEquals(templateGUID, copyGUID);

        CollectionProperties copy = assertInstanceOf(CollectionProperties.class, retrieve(copyGUID).getProperties());

        assertEquals(templateProperties.getQualifiedName().replace("~{copyName}~", "copy1"), copy.getQualifiedName());
        assertEquals("Template for the collection manager FVT", copy.getDescription());
    }


    /**
     * An element added to a collection is listed as a member and in the hierarchy; its membership properties
     * can be updated; once removed it is no longer a member.
     */
    @Test
    @Order(3)
    void collectionMembership()
    {
        String memberGUID = createCollection(collectionProperties(new CollectionProperties(), "member"));

        CollectionMembershipProperties membershipProperties = new CollectionMembershipProperties();

        membershipProperties.setMembershipRationale("Nested for the FVT");
        membershipProperties.setMembershipStatus(CollectionMemberStatus.PROPOSED);

        NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

        attach.setProperties(membershipProperties);

        collectionManager.call("/collections/" + collectionGUID + "/members/" + memberGUID + "/attach", attach);

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/collections/" + collectionGUID + "/members", new ResultsRequestBody()), memberGUID),
                   "getCollectionMembers does not include the new member");

        OpenMetadataRootElement hierarchy = collectionManager.element("/collections/" + collectionGUID + "/hierarchy", new ResultsRequestBody());

        assertNotNull(hierarchy, "getCollectionHierarchy returned nothing");
        assertEquals(collectionGUID, hierarchy.getElementHeader().getGUID());

        OpenMetadataRootHierarchy rootHierarchy = assertInstanceOf(OpenMetadataRootHierarchy.class, hierarchy);

        assertNotNull(rootHierarchy.getOpenMetadataRootHierarchies(), "the hierarchy has no nested collections");
        assertTrue(rootHierarchy.getOpenMetadataRootHierarchies().stream().anyMatch(nested -> memberGUID.equals(nested.getElementHeader().getGUID())),
                   "the hierarchy does not include the nested collection");

        CollectionMembershipProperties updatedMembership = new CollectionMembershipProperties();

        updatedMembership.setMembershipStatus(CollectionMemberStatus.VALIDATED);

        UpdateRelationshipRequestBody update = new UpdateRelationshipRequestBody();

        update.setMergeUpdate(true);
        update.setProperties(updatedMembership);

        collectionManager.call("/collections/" + collectionGUID + "/members/" + memberGUID + "/update", update);

        RelatedMetadataElementSummary membership = RelatedElementChecks.relationship(retrieve(collectionGUID),
                                                                                     OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                     memberGUID);

        assertNotNull(membership, "the collection does not show its member");

        CollectionMembershipProperties retrievedMembership = assertInstanceOf(CollectionMembershipProperties.class, membership.getRelationshipProperties());

        assertEquals(CollectionMemberStatus.VALIDATED, retrievedMembership.getMembershipStatus());
        assertEquals("Nested for the FVT", retrievedMembership.getMembershipRationale(), "a merge update should keep the rationale");

        collectionManager.call("/collections/" + collectionGUID + "/members/" + memberGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.contains(collectionManager.elements("/collections/" + collectionGUID + "/members", new ResultsRequestBody()), memberGUID),
                    "getCollectionMembers still includes the removed member");
    }


    /**
     * A collection attached to an element as a resource list is returned by getAttachedCollections until it is
     * detached.
     */
    @Test
    @Order(4)
    void resourceList()
    {
        String parentGUID = createCollection(collectionProperties(new CollectionProperties(), "resource-list-user"));

        ResourceListProperties resourceListProperties = new ResourceListProperties();

        resourceListProperties.setResourceUse("FVT reference");

        NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

        attach.setProperties(resourceListProperties);

        collectionManager.call("/metadata-elements/" + parentGUID + "/collections/" + collectionGUID + "/attach", attach);

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/metadata-elements/" + parentGUID + "/collections", new ResultsRequestBody()), collectionGUID),
                   "getAttachedCollections does not include the attached collection");

        collectionManager.call("/metadata-elements/" + parentGUID + "/collections/" + collectionGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.contains(collectionManager.elements("/metadata-elements/" + parentGUID + "/collections", new ResultsRequestBody()), collectionGUID),
                    "getAttachedCollections still includes the detached collection");
    }


    /**
     * A data specification linked to an element as its data description is visible from the element until it is
     * detached.
     */
    @Test
    @Order(5)
    void dataDescription()
    {
        String dataSpecGUID = createCollection(collectionProperties(new DataSpecProperties(), "data-spec"));

        DataDescriptionProperties dataDescriptionProperties = new DataDescriptionProperties();

        dataDescriptionProperties.setLabel("FVT description");

        NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

        attach.setProperties(dataDescriptionProperties);

        collectionManager.call("/metadata-elements/" + collectionGUID + "/data-descriptions/" + dataSpecGUID + "/attach", attach);

        assertTrue(RelatedElementChecks.isLinked(retrieve(collectionGUID), OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName, dataSpecGUID),
                   "the element does not show its data description");

        collectionManager.call("/metadata-elements/" + collectionGUID + "/data-descriptions/" + dataSpecGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(collectionGUID), OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName, dataSpecGUID),
                    "the element still shows the detached data description");
    }


    /**
     * The editing, scoping, staging and collection-kind classifications show on the collection once set and are
     * gone once cleared.
     */
    @Test
    @Order(6)
    void collectionClassifications()
    {
        EditingCollectionProperties editing = new EditingCollectionProperties();
        editing.setDescription("Editing for the FVT");

        ScopingCollectionProperties scoping = new ScopingCollectionProperties();
        scoping.setDescription("Scoping for the FVT");

        StagingCollectionProperties staging = new StagingCollectionProperties();
        staging.setDescription("Staging for the FVT");

        classifyAndClear("/is-editing-collection", editing, "/is-editing-collection/delete", new MetadataSourceRequestBody(),
                         OpenMetadataType.EDITING_COLLECTION_CLASSIFICATION.typeName);
        classifyAndClear("/is-scoping-collection", scoping, "/is-scoping-collection/delete", new MetadataSourceRequestBody(),
                         OpenMetadataType.SCOPING_COLLECTION_CLASSIFICATION.typeName);
        classifyAndClear("/is-staging-collection", staging, "/is-staging-collection/delete", new MetadataSourceRequestBody(),
                         OpenMetadataType.STAGING_COLLECTION_CLASSIFICATION.typeName);
        classifyAndClear("/collection-kind", new CollectionKindProperties(), "/collection-kind/remove", new DeleteClassificationRequestBody(),
                         OpenMetadataType.COLLECTION_KIND_CLASSIFICATION.typeName);
    }


    /**
     * A results set linked to the saved query that populates it shows the query until it is detached.
     */
    @Test
    @Order(7)
    void smartQuery()
    {
        String resultsSetGUID = createCollection(collectionProperties(new ResultsSetProperties(), "results-set"));

        SavedQueryProperties savedQueryProperties = new SavedQueryProperties();

        savedQueryProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SavedQuery", "collection-manager"));
        savedQueryProperties.setDisplayName("FVT saved query");
        savedQueryProperties.setQueryURL("https://example.com/query");

        String savedQueryGUID = assetMaker.create("/assets", ownAnchor(savedQueryProperties));

        try
        {
            SmartQueryProperties smartQueryProperties = new SmartQueryProperties();

            smartQueryProperties.setLabel("FVT smart query");

            NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

            attach.setProperties(smartQueryProperties);

            collectionManager.call("/collections/results-sets/" + resultsSetGUID + "/smart-query/" + savedQueryGUID + "/attach", attach);

            assertTrue(RelatedElementChecks.isLinked(retrieve(resultsSetGUID), OpenMetadataType.SMART_QUERY_RELATIONSHIP.typeName, savedQueryGUID),
                       "the results set does not show its smart query");

            collectionManager.call("/collections/results-sets/" + resultsSetGUID + "/smart-query/" + savedQueryGUID + "/detach", new DeleteRelationshipRequestBody());

            assertFalse(RelatedElementChecks.isLinked(retrieve(resultsSetGUID), OpenMetadataType.SMART_QUERY_RELATIONSHIP.typeName, savedQueryGUID),
                        "the results set still shows the detached smart query");
        }
        finally
        {
            assetMaker.call("/assets/" + savedQueryGUID + "/delete", new DeleteElementRequestBody());
        }
    }


    /**
     * A skill set associated with an actor is visible from the skill set until it is detached.
     */
    @Test
    @Order(8)
    void associatedSkillSet()
    {
        String skillSetGUID = createCollection(collectionProperties(new SkillSetProperties(), "skill-set"));

        AssociatedSkillSetProperties associatedSkillSetProperties = new AssociatedSkillSetProperties();

        associatedSkillSetProperties.setLabel("FVT skills");

        NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

        attach.setProperties(associatedSkillSetProperties);

        collectionManager.call("/actors/" + person() + "/associated-skill-sets/" + skillSetGUID + "/attach", attach);

        assertTrue(RelatedElementChecks.isLinked(retrieve(skillSetGUID), OpenMetadataType.ASSOCIATED_SKILL_SET_RELATIONSHIP.typeName, person()),
                   "the skill set does not show the actor it is associated with");

        collectionManager.call("/actors/" + person() + "/associated-skill-sets/" + skillSetGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(skillSetGUID), OpenMetadataType.ASSOCIATED_SKILL_SET_RELATIONSHIP.typeName, person()),
                    "the skill set still shows the detached actor");
    }


    /**
     * An agreement's actors, items and contracts are linked, updated and detached through both forms of the
     * detach requests.
     */
    @Test
    @Order(9)
    void agreements()
    {
        AgreementProperties agreementProperties = collectionProperties(new AgreementProperties(), "agreement");
        String              agreementGUID       = createCollection(agreementProperties);

        /*
         * Agreement actor - linked, then detached by its relationship GUID.
         */
        AgreementActorProperties agreementActorProperties = new AgreementActorProperties();

        agreementActorProperties.setAgreementPartyName("FVT party");

        NewRelationshipRequestBody actorRequest = new NewRelationshipRequestBody();

        actorRequest.setProperties(agreementActorProperties);

        String agreementActorGUID = collectionManager.create("/collections/agreements/" + agreementGUID + "/agreement-actors/" + person() + "/attach", actorRequest);

        RelatedMetadataElementSummary agreementActor = RelatedElementChecks.relationship(retrieve(agreementGUID),
                                                                                         OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName,
                                                                                         person());

        assertNotNull(agreementActor, "the agreement does not show its actor");
        assertEquals(agreementActorGUID, agreementActor.getRelationshipHeader().getGUID());

        collectionManager.call("/collection/agreements/agreement-actors/" + agreementActorGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(agreementGUID), OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName, person()),
                    "the agreement still shows the detached actor");

        /*
         * Agreement item - linked, updated and detached by relationship GUID, then linked again and detached by
         * the GUIDs of its ends.
         */
        String itemGUID = createCollection(collectionProperties(new CollectionProperties(), "agreement-item"));

        AgreementItemProperties agreementItemProperties = new AgreementItemProperties();

        agreementItemProperties.setAgreementItemId("item-1");

        NewRelationshipRequestBody itemRequest = new NewRelationshipRequestBody();

        itemRequest.setProperties(agreementItemProperties);

        String agreementItemGUID = collectionManager.create("/collections/agreements/" + agreementGUID + "/agreement-items/" + itemGUID + "/attach", itemRequest);

        AgreementItemProperties updatedItemProperties = new AgreementItemProperties();

        updatedItemProperties.setRestrictions(Map.of("region", "EU only"));

        UpdateRelationshipRequestBody itemUpdate = new UpdateRelationshipRequestBody();

        itemUpdate.setMergeUpdate(true);
        itemUpdate.setProperties(updatedItemProperties);

        collectionManager.call("/agreement-items/" + agreementItemGUID + "/update", itemUpdate);

        RelatedMetadataElementSummary agreementItem = RelatedElementChecks.relationship(retrieve(agreementGUID),
                                                                                        OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                                        itemGUID);

        assertNotNull(agreementItem, "the agreement does not show its item");

        AgreementItemProperties retrievedItem = assertInstanceOf(AgreementItemProperties.class, agreementItem.getRelationshipProperties());

        assertEquals("item-1", retrievedItem.getAgreementItemId(), "a merge update should keep the item id");
        assertEquals(Map.of("region", "EU only"), retrievedItem.getRestrictions());

        collectionManager.call("/agreement-items/" + agreementItemGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(agreementGUID), OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, itemGUID),
                    "the agreement still shows the item detached by relationship GUID");

        collectionManager.create("/collections/agreements/" + agreementGUID + "/agreement-items/" + itemGUID + "/attach", itemRequest);

        assertTrue(RelatedElementChecks.isLinked(retrieve(agreementGUID), OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, itemGUID),
                   "the agreement does not show its relinked item");

        collectionManager.call("/collections/agreements/" + agreementGUID + "/agreement-items/" + itemGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(agreementGUID), OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, itemGUID),
                    "the agreement still shows the item detached by its ends");

        /*
         * Contract link to a cited document.
         */
        CitedDocumentProperties citedDocumentProperties = new CitedDocumentProperties();

        citedDocumentProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("CitedDocument", "contract"));
        citedDocumentProperties.setDisplayName("FVT contract");

        String contractGUID = externalLinks.create("/external-references", ownAnchor(citedDocumentProperties));

        try
        {
            ContractLinkProperties contractLinkProperties = new ContractLinkProperties();

            contractLinkProperties.setContractId("contract-1");

            NewRelationshipRequestBody contractRequest = new NewRelationshipRequestBody();

            contractRequest.setProperties(contractLinkProperties);

            collectionManager.call("/collections/agreements/" + agreementGUID + "/contract-links/" + contractGUID + "/attach", contractRequest);

            assertTrue(RelatedElementChecks.isLinked(retrieve(agreementGUID), OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName, contractGUID),
                       "the agreement does not show its contract");

            collectionManager.call("/collections/agreements/" + agreementGUID + "/contract-links/" + contractGUID + "/detach", new DeleteRelationshipRequestBody());

            assertFalse(RelatedElementChecks.isLinked(retrieve(agreementGUID), OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName, contractGUID),
                        "the agreement still shows the detached contract");
        }
        finally
        {
            externalLinks.call("/external-references/" + contractGUID + "/delete", new DeleteElementRequestBody());
        }
    }


    /**
     * A subscriber linked to a digital subscription is visible from the subscription until it is detached.
     */
    @Test
    @Order(10)
    void subscribers()
    {
        DigitalSubscriptionProperties subscriptionProperties = collectionProperties(new DigitalSubscriptionProperties(), "subscription");

        subscriptionProperties.setSupportLevel("Best efforts");

        String subscriptionGUID = createCollection(subscriptionProperties);

        DigitalSubscriberProperties subscriberProperties = new DigitalSubscriberProperties();

        subscriberProperties.setSubscriberId("subscriber-1");

        NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

        attach.setProperties(subscriberProperties);

        collectionManager.call("/subscribers/" + person() + "/subscriptions/" + subscriptionGUID + "/attach", attach);

        assertTrue(RelatedElementChecks.isLinked(retrieve(subscriptionGUID), OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName, person()),
                   "the subscription does not show its subscriber");

        collectionManager.call("/subscribers/" + person() + "/subscriptions/" + subscriptionGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(subscriptionGUID), OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName, person()),
                    "the subscription still shows the detached subscriber");
    }


    /**
     * Digital products are found by search string and category, filtered by deployment status.
     */
    @Test
    @Order(11)
    void digitalProducts()
    {
        DigitalProductProperties productProperties = collectionProperties(new DigitalProductProperties(), "product");

        productProperties.setProductName("FVT product");
        productProperties.setDeploymentStatus(DeploymentStatus.ACTIVE);

        String productGUID = createCollection(productProperties);

        DeploymentStatusSearchString search = new DeploymentStatusSearchString();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());
        search.setDeploymentStatusList(List.of(DeploymentStatus.ACTIVE));

        List<OpenMetadataRootElement> found = collectionManager.elements("/digital-products/by-search-string", search);

        assertTrue(RelatedElementChecks.contains(found, productGUID), "findDigitalProducts did not return the active product");
        assertTrue(found.stream().allMatch(element -> element.getProperties() instanceof DigitalProductProperties),
                   "findDigitalProducts returned an element that is not a digital product");

        search.setDeploymentStatusList(List.of(DeploymentStatus.DISABLED));

        assertFalse(RelatedElementChecks.contains(collectionManager.elements("/digital-products/by-search-string", search), productGUID),
                    "findDigitalProducts returned an active product for a search for disabled products");

        DeploymentStatusFilterRequestBody byCategory = new DeploymentStatusFilterRequestBody();

        byCategory.setFilter(CATEGORY);
        byCategory.setDeploymentStatusList(List.of(DeploymentStatus.ACTIVE));

        found = collectionManager.elements("/digital-products/by-category", byCategory);

        assertTrue(RelatedElementChecks.contains(found, productGUID), "getDigitalProductByCategory did not return the product");
        assertFalse(RelatedElementChecks.contains(found, collectionGUID), "getDigitalProductByCategory returned a collection that is not a product");
    }


    /**
     * Every collection the tests created can be deleted, after which it can no longer be retrieved.
     */
    @Test
    @Order(99)
    void deleteCollections()
    {
        collectionsToDelete.add(0, collectionGUID);

        for (String guid : collectionsToDelete)
        {
            collectionManager.call("/collections/" + guid + "/delete", new DeleteElementRequestBody());

            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + guid + "/retrieve");
        }

        collectionsToDelete.clear();
        collectionGUID = null;
    }


    @AfterAll
    static void deletePerson()
    {
        if (personGUID != null)
        {
            actorManager.call("/actor-profiles/" + personGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + personGUID + "/retrieve");
            personGUID = null;
        }
    }


    /**
     * Return the person used at the far end of actor relationships, creating it on first use.
     */
    private static String person()
    {
        if (personGUID == null)
        {
            PersonProperties personProperties = new PersonProperties();

            personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "collection-manager"));
            personProperties.setDisplayName("Collection manager FVT person");

            personGUID = actorManager.create("/actor-profiles", ownAnchor(personProperties));
        }

        return personGUID;
    }


    private void classifyAndClear(String                         classifyPath,
                                  org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties properties,
                                  String                         clearPath,
                                  Object                         clearRequestBody,
                                  String                         classificationName)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        collectionManager.call("/collections/" + collectionGUID + classifyPath, requestBody);

        assertTrue(RelatedElementChecks.classificationNames(retrieve(collectionGUID).getElementHeader()).contains(classificationName),
                   classifyPath + " did not add " + classificationName);

        collectionManager.call("/collections/" + collectionGUID + clearPath, clearRequestBody);

        assertFalse(RelatedElementChecks.classificationNames(retrieve(collectionGUID).getElementHeader()).contains(classificationName),
                    clearPath + " did not remove " + classificationName);
    }


    private static <T extends CollectionProperties> T collectionProperties(T      properties,
                                                                           String name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getClass().getSimpleName(), name));
        properties.setDisplayName("Collection manager FVT " + name);
        properties.setCategory(CATEGORY);

        return properties;
    }


    private static NewElementRequestBody ownAnchor(OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static String createCollection(CollectionProperties properties)
    {
        String guid = collectionManager.create("/collections", ownAnchor(properties));

        if (collectionGUID != null)
        {
            collectionsToDelete.add(guid);
        }

        return guid;
    }


    private static OpenMetadataRootElement retrieve(String guid)
    {
        OpenMetadataRootElement element = collectionManager.element("/collections/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "collection " + guid + " is missing");

        return element;
    }
}
