/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.CountResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorSearchMatches;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.EndMatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnchorSearchMatchesListResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnchorSearchMatchesResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.AttachedClassificationsResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.FindRelationshipRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.FindRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.HistoryRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataClassificationRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataElementRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewRelatedElementsRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataDeleteRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataElementResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataElementsResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataGraphResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipListResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataTemplateRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.ReHomeRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.ReIdentifyRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.ReTypeRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.RelatedMetadataElementListResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.UniqueNameRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdateEffectivityDatesRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdatePropertiesRequestBody;
import org.odpi.openmetadata.viewservices.metadataexpert.rest.MatchCriteriaListResponse;
import org.odpi.openmetadata.viewservices.metadataexpert.rest.PropertyComparisonOperatorListResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MetadataExpertFVT covers the Metadata Expert OMVS's requests for metadata elements, classifications and
 * relationships: the generic, type-agnostic open metadata store API.  It works with Location elements (a
 * campus, a building nested in it and a room anchored to the campus), and checks each request's effect by
 * reading the element or relationship back through the same service.  The type definition requests are in
 * MetadataExpertTypesFVT.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetadataExpertFVT
{
    private static final ViewServiceClient metadataExpert = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);
    private static final PropertyHelper    propertyHelper = new PropertyHelper();

    private static final String LOCATION         = OpenMetadataType.LOCATION.typeName;
    private static final String NESTED_LOCATION  = OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName;
    private static final String FIXED_LOCATION   = OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName;
    private static final String QUALIFIED_NAME   = OpenMetadataProperty.QUALIFIED_NAME.name;
    private static final String DESCRIPTION      = OpenMetadataProperty.DESCRIPTION.name;
    private static final String LABEL            = OpenMetadataProperty.LABEL.name;

    private static String campusQualifiedName = null;
    private static String campusGUID          = null;
    private static String buildingGUID        = null;
    private static String roomGUID            = null;
    private static String copyGUID            = null;
    private static String mediaGUID           = null;
    private static String relationshipGUID    = null;


    /**
     * Create elements, and read them back by GUID and by unique name.
     */
    @Test
    @Order(1)
    void createAndRetrieveElements()
    {
        campusQualifiedName = ViewServicesFvtTestSupport.qualifiedName("Location", "expert-campus");
        campusGUID          = createElement(LOCATION, campusQualifiedName, "first");
        buildingGUID        = createElement(LOCATION, ViewServicesFvtTestSupport.qualifiedName("Location", "expert-building"), "building");

        OpenMetadataElement campus = getElement(campusGUID);

        assertNotNull(campus);
        assertEquals(LOCATION, campus.getType().getTypeName());
        assertEquals(campusQualifiedName, stringProperty(campus, QUALIFIED_NAME));
        assertEquals("first", stringProperty(campus, DESCRIPTION));

        UniqueNameRequestBody uniqueNameRequestBody = new UniqueNameRequestBody();

        uniqueNameRequestBody.setName(campusQualifiedName);
        uniqueNameRequestBody.setNamePropertyName(QUALIFIED_NAME);

        OpenMetadataElement byName = metadataExpert.post("/metadata-elements/by-unique-name", uniqueNameRequestBody, OpenMetadataElementResponse.class).getElement();

        assertNotNull(byName, "by-unique-name did not find the campus");
        assertEquals(campusGUID, byName.getElementGUID());

        assertEquals(campusGUID, metadataExpert.post("/metadata-elements/guid-by-unique-name", uniqueNameRequestBody, GUIDResponse.class).getGUID());
    }


    /**
     * An element created from a template copies the template's properties, with the replacement properties
     * overriding them.
     */
    @Test
    @Order(2)
    void createElementFromTemplate()
    {
        String copyQualifiedName = ViewServicesFvtTestSupport.qualifiedName("Location", "expert-copy");

        OpenMetadataTemplateRequestBody requestBody = new OpenMetadataTemplateRequestBody();

        requestBody.setTypeName(LOCATION);
        requestBody.setTemplateGUID(campusGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setReplacementProperties(propertyHelper.addStringProperty(null, QUALIFIED_NAME, copyQualifiedName));

        copyGUID = metadataExpert.create("/metadata-elements/from-template", requestBody);

        assertNotEquals(campusGUID, copyGUID);

        OpenMetadataElement copy = getElement(copyGUID);

        assertNotNull(copy);
        assertEquals(copyQualifiedName, stringProperty(copy, QUALIFIED_NAME));
        assertEquals("first", stringProperty(copy, DESCRIPTION), "the template's description was not copied");
    }


    /**
     * Updating an element creates a new version: the history shows both versions, and undo restores the first.
     */
    @Test
    @Order(3)
    void updateHistoryAndUndo()
    {
        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(propertyHelper.addStringProperty(null, DESCRIPTION, "second"));

        metadataExpert.post("/metadata-elements/" + campusGUID + "/update-properties", requestBody, BooleanResponse.class);

        OpenMetadataElement campus = getElement(campusGUID);

        assertEquals("second", stringProperty(campus, DESCRIPTION));
        assertEquals(campusQualifiedName, stringProperty(campus, QUALIFIED_NAME), "a merge update lost the qualified name");

        List<OpenMetadataElement> history = metadataExpert.post("/metadata-elements/" + campusGUID + "/history",
                                                                new HistoryRequestBody(),
                                                                OpenMetadataElementsResponse.class).getElements();

        assertNotNull(history);

        List<String> descriptions = new ArrayList<>();

        for (OpenMetadataElement version : history)
        {
            descriptions.add(stringProperty(version, DESCRIPTION));
        }

        assertTrue(descriptions.containsAll(List.of("first", "second")), "history should hold both versions: " + descriptions);

        metadataExpert.call("/metadata-elements/" + campusGUID + "/undo", new MetadataSourceRequestBody());

        assertEquals("first", stringProperty(getElement(campusGUID), DESCRIPTION), "undo did not restore the previous version");
    }


    /**
     * The effectivity dates of an element can be changed.
     */
    @Test
    @Order(4)
    void updateElementEffectivity()
    {
        Date effectiveFrom = new Date(System.currentTimeMillis() - 86_400_000L);
        Date effectiveTo   = new Date(System.currentTimeMillis() + (365L * 86_400_000L));

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        metadataExpert.call("/metadata-elements/" + buildingGUID + "/update-effectivity", requestBody);

        OpenMetadataElement building = getElement(buildingGUID);

        assertEquals(effectiveFrom, building.getEffectiveFromTime());
        assertEquals(effectiveTo, building.getEffectiveToTime());
    }


    /**
     * A classification is added, updated, has its effectivity changed, shows a history, and is removed.
     */
    @Test
    @Order(5)
    void classifications()
    {
        String classificationPath = "/metadata-elements/" + campusGUID + "/classifications/" + FIXED_LOCATION;

        NewOpenMetadataClassificationRequestBody requestBody = new NewOpenMetadataClassificationRequestBody();

        requestBody.setProperties(new NewElementProperties(propertyHelper.addStringProperty(null, "postalAddress", "1 Campus Way")));

        metadataExpert.call(classificationPath, requestBody);

        assertEquals("1 Campus Way", classificationProperty(campusGUID, "postalAddress"));

        UpdatePropertiesRequestBody updateRequestBody = new UpdatePropertiesRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(propertyHelper.addStringProperty(null, "timeZone", "Europe/London"));

        metadataExpert.call(classificationPath + "/update-properties", updateRequestBody);

        assertEquals("Europe/London", classificationProperty(campusGUID, "timeZone"));
        assertEquals("1 Campus Way", classificationProperty(campusGUID, "postalAddress"), "a merge update lost the postal address");

        Date effectiveFrom = new Date(System.currentTimeMillis() - 86_400_000L);

        UpdateEffectivityDatesRequestBody effectivityRequestBody = new UpdateEffectivityDatesRequestBody();

        effectivityRequestBody.setEffectiveFrom(effectiveFrom);

        metadataExpert.call(classificationPath + "/update-effectivity", effectivityRequestBody);

        assertEquals(effectiveFrom, classification(campusGUID).getEffectiveFromTime());

        List<AttachedClassification> history = metadataExpert.post(classificationPath + "/history",
                                                                   new HistoryRequestBody(),
                                                                   AttachedClassificationsResponse.class).getClassifications();

        assertNotNull(history);
        assertTrue(history.size() >= 2, "the classification history should hold the versions created by its updates: " + history.size());
        assertTrue(history.stream().allMatch(version -> FIXED_LOCATION.equals(version.getClassificationName())));

        metadataExpert.call(classificationPath + "/delete", new MetadataSourceRequestBody());

        assertNull(classification(campusGUID), "the classification is still there after it was deleted");
    }


    /**
     * A relationship is created and read back through every relationship query, updated, undone, has its
     * effectivity changed and shows its history.
     */
    @Test
    @Order(6)
    void relationships()
    {
        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody();

        requestBody.setTypeName(NESTED_LOCATION);
        requestBody.setMetadataElement1GUID(campusGUID);
        requestBody.setMetadataElement2GUID(buildingGUID);
        requestBody.setProperties(new NewElementProperties(propertyHelper.addStringProperty(null, LABEL, "contains")));

        relationshipGUID = metadataExpert.create("/related-elements", requestBody);

        OpenMetadataRelationship relationship = getRelationship(relationshipGUID);

        assertNotNull(relationship);
        assertEquals(NESTED_LOCATION, relationship.getRelationshipType().getTypeName());
        assertEquals(campusGUID, relationship.getElementGUIDAtEnd1());
        assertEquals(buildingGUID, relationship.getElementGUIDAtEnd2());
        assertEquals("contains", relationshipLabel(relationshipGUID));

        assertTrue(relatedGUIDs("/related-elements/" + campusGUID + "/any-type").contains(buildingGUID));
        assertTrue(relatedGUIDs("/related-elements/" + campusGUID + "/type/" + NESTED_LOCATION).contains(buildingGUID));
        assertTrue(relatedGUIDs("/related-elements/" + buildingGUID + "/type/" + NESTED_LOCATION + "?startingAtEnd=2").contains(campusGUID),
                   "starting at end 2 from the building should find the campus at end 1");
        assertFalse(relatedGUIDs("/related-elements/" + buildingGUID + "/type/" + NESTED_LOCATION + "?startingAtEnd=1").contains(campusGUID),
                    "the building is not at end 1 of the relationship, so starting at end 1 should not find the campus");

        assertEquals(List.of(relationshipGUID), relationshipGUIDs("/metadata-elements/" + campusGUID + "/linked-by-any-type/to-elements/" + buildingGUID));
        assertEquals(List.of(relationshipGUID), relationshipGUIDs("/metadata-elements/" + campusGUID + "/linked-by-type/" + NESTED_LOCATION + "/to-elements/" + buildingGUID));

        UpdatePropertiesRequestBody updateRequestBody = new UpdatePropertiesRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(propertyHelper.addStringProperty(null, LABEL, "includes"));

        metadataExpert.call("/related-elements/" + relationshipGUID + "/update-properties", updateRequestBody);

        assertEquals("includes", relationshipLabel(relationshipGUID));

        List<OpenMetadataRelationship> history = metadataExpert.post("/relationships/" + relationshipGUID + "/history",
                                                                     new HistoryRequestBody(),
                                                                     OpenMetadataRelationshipListResponse.class).getRelationshipList().getRelationships();

        assertTrue(history.size() >= 2, "the relationship history should hold both versions");

        metadataExpert.call("/related-elements/" + relationshipGUID + "/undo", new MetadataSourceRequestBody());

        assertEquals("contains", relationshipLabel(relationshipGUID), "undo did not restore the previous version of the relationship");
    }


    /**
     * The effectivity dates of a relationship can be changed.
     */
    @Test
    @Order(7)
    void updateRelationshipEffectivity()
    {
        Date effectiveFrom = new Date(System.currentTimeMillis() - 86_400_000L);

        UpdateEffectivityDatesRequestBody effectivityRequestBody = new UpdateEffectivityDatesRequestBody();

        effectivityRequestBody.setEffectiveFrom(effectiveFrom);

        metadataExpert.call("/related-elements/" + relationshipGUID + "/update-effectivity", effectivityRequestBody);

        assertEquals(effectiveFrom, getRelationship(relationshipGUID).getEffectiveFromTime());
    }


    /**
     * Find and count relationships by search conditions.  The count with pushDown=false (the default) must agree
     * with the find; with pushDown=true the repository counts, which here (no access restrictions) gives the same
     * answer.
     */
    @Test
    @Order(8)
    void findAndCountRelationships()
    {
        FindRelationshipRequestBody requestBody = new FindRelationshipRequestBody();

        requestBody.setRelationshipTypeName(NESTED_LOCATION);
        requestBody.setEnd1EntityGUIDs(List.of(campusGUID));
        requestBody.setEndMatchCriteria(EndMatchCriteria.BOTH);

        assertFindAndCountAgree(requestBody);
    }


    /**
     * With no end match criteria the find defaults it from the end GUIDs supplied; the count must give the same
     * answer as the find.
     */
    @Test
    @Order(9)
    void countRelationshipsWithDefaultEndMatchCriteria()
    {
        FindRelationshipRequestBody requestBody = new FindRelationshipRequestBody();

        requestBody.setRelationshipTypeName(NESTED_LOCATION);
        requestBody.setEnd1EntityGUIDs(List.of(campusGUID));

        assertFindAndCountAgree(requestBody);
    }


    private static void assertFindAndCountAgree(FindRelationshipRequestBody requestBody)
    {
        List<OpenMetadataRelationship> found = metadataExpert.post("/relationships/by-search-conditions",
                                                                   requestBody,
                                                                   OpenMetadataRelationshipListResponse.class).getRelationshipList().getRelationships();

        assertNotNull(found);
        assertEquals(List.of(relationshipGUID), found.stream().map(OpenMetadataRelationship::getRelationshipGUID).toList());

        assertEquals(found.size(), metadataExpert.post("/relationships/by-search-conditions/count", requestBody, CountResponse.class).getCount(),
                     "the count with the default pushDown does not agree with the find");
        assertEquals(found.size(), metadataExpert.post("/relationships/by-search-conditions/count?pushDown=false", requestBody, CountResponse.class).getCount(),
                     "the count with pushDown=false does not agree with the find");
        assertEquals(found.size(), metadataExpert.post("/relationships/by-search-conditions/count?pushDown=true", requestBody, CountResponse.class).getCount(),
                     "the count with pushDown=true does not agree with the find");
    }


    /**
     * A relationship can be given a new GUID, retyped to its supertype, deleted and restored, and detached.
     */
    @Test
    @Order(10)
    void reIdentifyReTypeDeleteAndRestoreRelationship()
    {
        String newRelationshipGUID = UUID.randomUUID().toString();

        ReIdentifyRequestBody reIdentifyRequestBody = new ReIdentifyRequestBody();

        reIdentifyRequestBody.setNewGUID(newRelationshipGUID);

        metadataExpert.call("/related-elements/" + relationshipGUID + "/re-identify", reIdentifyRequestBody);

        assertNotNull(getRelationship(newRelationshipGUID), "the relationship can not be found by its new GUID");
        assertRelationshipGone(relationshipGUID);

        relationshipGUID = newRelationshipGUID;

        ReTypeRequestBody reTypeRequestBody = new ReTypeRequestBody();

        reTypeRequestBody.setNewTypeName(OpenMetadataType.LABELED_RELATIONSHIP.typeName);

        metadataExpert.call("/related-elements/" + relationshipGUID + "/re-type", reTypeRequestBody);

        assertEquals(OpenMetadataType.LABELED_RELATIONSHIP.typeName, getRelationship(relationshipGUID).getRelationshipType().getTypeName());

        metadataExpert.call("/related-elements/" + relationshipGUID + "/delete", new OpenMetadataDeleteRequestBody());

        assertRelationshipGone(relationshipGUID);

        metadataExpert.call("/related-elements/" + relationshipGUID + "/restore", new MetadataSourceRequestBody());

        assertNotNull(getRelationship(relationshipGUID), "restore did not bring the relationship back");

        metadataExpert.call("/related-elements/" + campusGUID + "/" + OpenMetadataType.LABELED_RELATIONSHIP.typeName + "/" + buildingGUID + "/detach-all",
                            new OpenMetadataDeleteRequestBody());

        assertRelationshipGone(relationshipGUID);
        assertTrue(relationshipGUIDs("/metadata-elements/" + campusGUID + "/linked-by-any-type/to-elements/" + buildingGUID).isEmpty());
    }


    /**
     * Re-homing a relationship moves its home to another metadata collection, and is carried out by the
     * repository of the cohort member that is to become the new home.  This suite runs a single metadata access
     * store with no cohort, so there is no repository to take over the instance and the enterprise repository
     * services refuse the request (OMRS-REPOSITORY-400-056).
     */
    @Test
    @Order(11)
    void reHomeRelationship()
    {
        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody();

        requestBody.setTypeName(NESTED_LOCATION);
        requestBody.setMetadataElement1GUID(campusGUID);
        requestBody.setMetadataElement2GUID(buildingGUID);

        String guid = metadataExpert.create("/related-elements", requestBody);

        assertReHomeRefused("/related-elements/" + guid + "/re-home");

        metadataExpert.call("/related-elements/" + guid + "/delete", new OpenMetadataDeleteRequestBody());
    }


    /**
     * Elements anchored to the campus are found by the three anchor searches, and in the anchored elements
     * graph.
     */
    @Test
    @Order(12)
    void anchoredElements()
    {
        String roomQualifiedName = ViewServicesFvtTestSupport.qualifiedName("Location", "expert-room");

        NewOpenMetadataElementRequestBody requestBody = new NewOpenMetadataElementRequestBody();

        requestBody.setTypeName(LOCATION);
        requestBody.setIsOwnAnchor(false);
        requestBody.setAnchorGUID(campusGUID);
        requestBody.setAnchorScopeGUIDs(List.of(buildingGUID));
        requestBody.setParentGUID(campusGUID);
        requestBody.setParentRelationshipTypeName(NESTED_LOCATION);
        requestBody.setParentAtEnd1(true);
        requestBody.setProperties(new NewElementProperties(propertyHelper.addStringProperty(null, QUALIFIED_NAME, roomQualifiedName)));

        roomGUID = metadataExpert.create("/metadata-elements", requestBody);

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(roomQualifiedName);

        AnchorSearchMatches forAnchor = metadataExpert.post("/metadata-elements/by-search-string/for-anchor/" + campusGUID,
                                                            searchRequestBody,
                                                            AnchorSearchMatchesResponse.class).getElement();

        assertNotNull(forAnchor, "for-anchor found nothing");
        assertEquals(campusGUID, forAnchor.getElementGUID());
        assertEquals(List.of(roomGUID), guids(forAnchor.getMatchingElements()));

        AttachedClassification anchors = classification(roomGUID, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        assertNotNull(anchors, "the room has no Anchors classification");

        String anchorDomainName = propertyHelper.getStringProperty("MetadataExpertFVT",
                                                                   OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                   anchors.getClassificationProperties(),
                                                                   "anchoredElements");

        assertNotNull(anchorDomainName, "the room's Anchors classification has no anchor domain");

        List<String> anchorScopeGUIDs = propertyHelper.getStringArrayProperty("MetadataExpertFVT",
                                                                              OpenMetadataProperty.ANCHOR_SCOPE_GUIDS.name,
                                                                              anchors.getClassificationProperties(),
                                                                              "anchoredElements");

        assertEquals(List.of(buildingGUID), anchorScopeGUIDs, "the room's Anchors classification does not hold its anchor scope");

        assertAnchorMatches("/metadata-elements/by-search-string/in-anchor-domain/" + anchorDomainName, searchRequestBody);

        OpenMetadataElement graph = metadataExpert.post("/metadata-elements/" + campusGUID + "/with-anchored-elements",
                                                        new ResultsRequestBody(),
                                                        OpenMetadataGraphResponse.class).getElement();

        assertNotNull(graph);
        assertEquals(campusGUID, graph.getElementGUID());
        assertTrue(guids(((org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementGraph) graph).getAnchoredElements()).contains(roomGUID),
                   "the anchored elements graph does not include the room");
    }


    /**
     * The room's Anchors classification names the building as its anchor scope, so a search in that scope finds it.
     */
    @Test
    @Order(13)
    void findElementsInAnchorScope()
    {
        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.qualifiedName("Location", "expert-room"));

        assertAnchorMatches("/metadata-elements/by-search-string/in-anchor-scope/" + buildingGUID, searchRequestBody);
    }


    /**
     * Elements are found by search string and by search conditions, and counted - with pushDown=false (the
     * default) the count agrees with the find.
     */
    @Test
    @Order(14)
    void findAndCountElements()
    {
        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::Location::expert-");
        searchRequestBody.setMetadataElementTypeName(LOCATION);

        List<String> found = guids(metadataExpert.post("/metadata-elements/by-search-string", searchRequestBody, OpenMetadataElementsResponse.class).getElements());

        assertTrue(found.containsAll(List.of(campusGUID, buildingGUID, copyGUID, roomGUID)), "by-search-string returned " + found);

        FindRequestBody findRequestBody = new FindRequestBody();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null, QUALIFIED_NAME, campusQualifiedName, PropertyComparisonOperator.EQ));
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        findRequestBody.setMetadataElementTypeName(LOCATION);
        findRequestBody.setSearchProperties(searchProperties);

        List<String> byCondition = guids(metadataExpert.post("/metadata-elements/by-search-conditions", findRequestBody, OpenMetadataElementsResponse.class).getElements());

        assertEquals(List.of(campusGUID), byCondition);

        assertEquals(1L, metadataExpert.post("/metadata-elements/by-search-conditions/count", findRequestBody, CountResponse.class).getCount());
        assertEquals(1L, metadataExpert.post("/metadata-elements/by-search-conditions/count?pushDown=false", findRequestBody, CountResponse.class).getCount());
        assertEquals(1L, metadataExpert.post("/metadata-elements/by-search-conditions/count?pushDown=true", findRequestBody, CountResponse.class).getCount());

        /*
         * A wider search: every Location of this run whose qualified name contains "expert-".
         */
        SearchProperties likeProperties = new SearchProperties();

        likeProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                      QUALIFIED_NAME,
                                                                      ViewServicesFvtTestSupport.runSearchString() + "::Location::expert-",
                                                                      PropertyComparisonOperator.LIKE));
        likeProperties.setMatchCriteria(MatchCriteria.ALL);

        findRequestBody.setSearchProperties(likeProperties);

        List<String> byLike = guids(metadataExpert.post("/metadata-elements/by-search-conditions", findRequestBody, OpenMetadataElementsResponse.class).getElements());

        assertTrue(byLike.containsAll(List.of(campusGUID, buildingGUID, copyGUID, roomGUID)), "LIKE search returned " + byLike);
        assertEquals(byLike.size(),
                     metadataExpert.post("/metadata-elements/by-search-conditions/count?pushDown=false", findRequestBody, CountResponse.class).getCount(),
                     "the count with pushDown=false does not agree with the find");
        assertEquals(byLike.size(),
                     metadataExpert.post("/metadata-elements/by-search-conditions/count?pushDown=true", findRequestBody, CountResponse.class).getCount(),
                     "the count with pushDown=true does not agree with the find");
    }


    /**
     * The search vocabulary: the comparison operators and match criteria a search condition can use.
     */
    @Test
    @Order(15)
    void searchVocabulary()
    {
        List<PropertyComparisonOperator> operators = metadataExpert.get("/metadata-search/property-comparison-operator-values",
                                                                        PropertyComparisonOperatorListResponse.class).getValues();

        assertNotNull(operators);
        assertEquals(List.of(PropertyComparisonOperator.values()), operators);

        List<MatchCriteria> matchCriteria = metadataExpert.get("/metadata-search/match-criteria-values", MatchCriteriaListResponse.class).getValues();

        assertNotNull(matchCriteria);
        assertEquals(List.of(MatchCriteria.values()), matchCriteria);
    }


    /**
     * Publishing and withdrawing an element change its zone membership.  This deployment defines no publish or
     * default zones, so the element's visibility is unchanged: both requests succeed and leave it readable.
     */
    @Test
    @Order(16)
    void publishAndWithdraw()
    {
        metadataExpert.call("/metadata-elements/" + copyGUID + "/publish", new MetadataSourceRequestBody());

        assertNotNull(getElement(copyGUID), "the element is not readable after it was published");

        metadataExpert.call("/metadata-elements/" + copyGUID + "/withdraw", new MetadataSourceRequestBody());

        assertNotNull(getElement(copyGUID), "the element is not readable after it was withdrawn");
    }


    /**
     * An element can be given a new GUID, and retyped to a subtype.  Re-homing is refused for the same reason
     * as re-homing a relationship (see reHomeRelationship).
     */
    @Test
    @Order(17)
    void reIdentifyReTypeAndReHomeElement()
    {
        String newGUID = UUID.randomUUID().toString();

        ReIdentifyRequestBody reIdentifyRequestBody = new ReIdentifyRequestBody();

        reIdentifyRequestBody.setNewGUID(newGUID);

        metadataExpert.call("/metadata-elements/" + copyGUID + "/re-identify", reIdentifyRequestBody);

        assertNull(getElementUnchecked(copyGUID), "the element can still be read with its old GUID");

        OpenMetadataElement reIdentified = getElement(newGUID);

        assertNotNull(reIdentified, "the element can not be found by its new GUID");
        assertEquals(ViewServicesFvtTestSupport.qualifiedName("Location", "expert-copy"), stringProperty(reIdentified, QUALIFIED_NAME));

        copyGUID = newGUID;

        mediaGUID = createElement(OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                  ViewServicesFvtTestSupport.qualifiedName("ExternalReference", "expert-media"),
                                  "media");

        ReTypeRequestBody reTypeRequestBody = new ReTypeRequestBody();

        reTypeRequestBody.setNewTypeName(OpenMetadataType.RELATED_MEDIA.typeName);

        metadataExpert.call("/metadata-elements/" + mediaGUID + "/re-type", reTypeRequestBody);

        OpenMetadataElement media = getElement(mediaGUID);

        assertEquals(OpenMetadataType.RELATED_MEDIA.typeName, media.getType().getTypeName());
        assertEquals("media", stringProperty(media, DESCRIPTION), "re-typing lost the element's properties");

        assertReHomeRefused("/metadata-elements/" + mediaGUID + "/re-home");
    }


    /**
     * An archived element is no longer returned, and can be restored.
     */
    @Test
    @Order(19)
    void archiveAndRestore()
    {
        DeleteElementRequestBody archiveRequestBody = new DeleteElementRequestBody();

        archiveRequestBody.setArchiveProcess(MetadataExpertFVT.class.getSimpleName());

        metadataExpert.call("/metadata-elements/" + mediaGUID + "/archive", archiveRequestBody);

        assertNull(getElementUnchecked(mediaGUID), "the archived element is still returned");

        metadataExpert.call("/metadata-elements/" + mediaGUID + "/restore", new MetadataSourceRequestBody());

        assertNotNull(getElementUnchecked(mediaGUID), "restore did not bring back the archived element");
    }


    /**
     * A deleted element is gone, and can be restored.
     */
    @Test
    @Order(18)
    void deleteAndRestore()
    {
        metadataExpert.call("/metadata-elements/" + copyGUID + "/delete", new OpenMetadataDeleteRequestBody());

        assertNull(getElementUnchecked(copyGUID), "the deleted element is still returned");

        metadataExpert.call("/metadata-elements/" + copyGUID + "/restore", new MetadataSourceRequestBody());

        assertNotNull(getElementUnchecked(copyGUID), "restore did not bring back the deleted element");
    }


    /**
     * Delete everything this test created.
     */
    @Test
    @Order(20)
    void deleteElements()
    {
        for (String guid : List.of(mediaGUID, copyGUID, roomGUID, buildingGUID, campusGUID))
        {
            if (getElementUnchecked(guid) != null)
            {
                metadataExpert.call("/metadata-elements/" + guid + "/delete", new OpenMetadataDeleteRequestBody());
            }

            assertNull(getElementUnchecked(guid), "element " + guid + " is still returned after it was deleted");
        }
    }


    private static String createElement(String typeName,
                                        String qualifiedName,
                                        String description)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, QUALIFIED_NAME, qualifiedName);

        properties = propertyHelper.addStringProperty(properties, DESCRIPTION, description);

        NewOpenMetadataElementRequestBody requestBody = new NewOpenMetadataElementRequestBody();

        requestBody.setTypeName(typeName);
        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(new NewElementProperties(properties));

        return metadataExpert.create("/metadata-elements", requestBody);
    }


    private static OpenMetadataElement getElement(String guid)
    {
        return metadataExpert.post("/metadata-elements/" + guid, new GetRequestBody(), OpenMetadataElementResponse.class).getElement();
    }


    /**
     * Read an element, returning null whether the service reports it missing or returns nothing.
     */
    private static OpenMetadataElement getElementUnchecked(String guid)
    {
        OpenMetadataElementResponse response = metadataExpert.postUnchecked("/metadata-elements/" + guid, new GetRequestBody(), OpenMetadataElementResponse.class);

        assertNotNull(response);

        return (response.getRelatedHTTPCode() == 200) ? response.getElement() : null;
    }


    private static String stringProperty(OpenMetadataElement element,
                                         String              propertyName)
    {
        return propertyHelper.getStringProperty("MetadataExpertFVT", propertyName, element.getElementProperties(), "stringProperty");
    }


    private static AttachedClassification classification(String elementGUID,
                                                         String classificationName)
    {
        OpenMetadataElement element = getElement(elementGUID);

        assertNotNull(element);

        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if (classificationName.equals(classification.getClassificationName()))
                {
                    return classification;
                }
            }
        }

        return null;
    }


    private static AttachedClassification classification(String elementGUID)
    {
        return classification(elementGUID, FIXED_LOCATION);
    }


    private static String classificationProperty(String elementGUID,
                                                 String propertyName)
    {
        AttachedClassification classification = classification(elementGUID);

        assertNotNull(classification, "the " + FIXED_LOCATION + " classification is missing");

        return propertyHelper.getStringProperty("MetadataExpertFVT", propertyName, classification.getClassificationProperties(), "classificationProperty");
    }


    private static OpenMetadataRelationship getRelationship(String guid)
    {
        return metadataExpert.post("/relationships/by-guid/" + guid, new GetRequestBody(), OpenMetadataRelationshipResponse.class).getElement();
    }


    private static void assertRelationshipGone(String guid)
    {
        OpenMetadataRelationshipResponse response = metadataExpert.postUnchecked("/relationships/by-guid/" + guid,
                                                                                 new GetRequestBody(),
                                                                                 OpenMetadataRelationshipResponse.class);

        assertNotNull(response);
        assertTrue((response.getRelatedHTTPCode() != 200) || (response.getElement() == null),
                   "relationship " + guid + " is still returned");
    }


    private static String relationshipLabel(String guid)
    {
        return propertyHelper.getStringProperty("MetadataExpertFVT", LABEL, getRelationship(guid).getRelationshipProperties(), "relationshipLabel");
    }


    private static List<String> relatedGUIDs(String path)
    {
        RelatedMetadataElementListResponse response = metadataExpert.post(path, new ResultsRequestBody(), RelatedMetadataElementListResponse.class);
        List<String>                       guids    = new ArrayList<>();

        if ((response.getRelatedElementList() != null) && (response.getRelatedElementList().getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : response.getRelatedElementList().getElementList())
            {
                guids.add(relatedMetadataElement.getElement().getElementGUID());
            }
        }

        return guids;
    }


    private static List<String> relationshipGUIDs(String path)
    {
        OpenMetadataRelationshipListResponse response = metadataExpert.post(path, new ResultsRequestBody(), OpenMetadataRelationshipListResponse.class);

        if ((response.getRelationshipList() == null) || (response.getRelationshipList().getRelationships() == null))
        {
            return List.of();
        }

        return response.getRelationshipList().getRelationships().stream().map(OpenMetadataRelationship::getRelationshipGUID).toList();
    }


    private static List<String> guids(List<? extends OpenMetadataElement> elements)
    {
        if (elements == null)
        {
            return List.of();
        }

        return elements.stream().map(OpenMetadataElement::getElementGUID).toList();
    }


    private static void assertAnchorMatches(String                  path,
                                            SearchStringRequestBody requestBody)
    {
        List<AnchorSearchMatches> matches = metadataExpert.post(path, requestBody, AnchorSearchMatchesListResponse.class).getElements();

        assertNotNull(matches, path + " found nothing");

        AnchorSearchMatches campusMatches = matches.stream().filter(match -> campusGUID.equals(match.getElementGUID())).findFirst().orElse(null);

        assertNotNull(campusMatches, path + " did not return the campus as an anchor with matches");
        assertEquals(List.of(roomGUID), guids(campusMatches.getMatchingElements()), path + " did not return the room as the match");
    }


    /**
     * Re-homing needs a second repository in a cohort (see reHomeRelationship), so it is refused here.
     */
    private static void assertReHomeRefused(String path)
    {
        ReHomeRequestBody requestBody = new ReHomeRequestBody();

        requestBody.setNewHomeMetadataCollectionId(UUID.randomUUID().toString());
        requestBody.setNewHomeMetadataCollectionName("viewServicesFvtOtherCollection");

        String errorMessage = metadataExpert.postForFailure(path, requestBody).path("exceptionErrorMessage").asText();

        assertTrue(errorMessage.contains("OMRS-REPOSITORY-400-056"), path + " was refused for an unexpected reason: " + errorMessage);
    }
}
