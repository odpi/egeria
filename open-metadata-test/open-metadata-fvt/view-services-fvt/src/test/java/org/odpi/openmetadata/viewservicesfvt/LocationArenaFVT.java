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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.CyberLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.SecureLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LocationArenaFVT covers every request of the Location Arena OMVS: the lifecycle of locations, the adjacent,
 * nested and known location relationships (each checked through the location's own retrieve request, from
 * both ends), and the three location classifications.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocationArenaFVT
{
    private static final ViewServiceClient locationArena = new ViewServiceClient(ViewServiceDescription.LOCATION_ARENA);

    private static final String DESCRIPTION = "Location created by " + LocationArenaFVT.class.getSimpleName();

    private static String campusQualifiedName = null;
    private static String campusGUID          = null;
    private static String buildingGUID        = null;
    private static String neighbourGUID       = null;
    private static String copyGUID            = null;


    /**
     * Create locations and read one back with its properties.
     */
    @Test
    @Order(1)
    void createAndRetrieveLocations()
    {
        campusQualifiedName = ViewServicesFvtTestSupport.qualifiedName("Location", "campus");
        campusGUID          = createLocation(campusQualifiedName, "Campus");
        buildingGUID        = createLocation(ViewServicesFvtTestSupport.qualifiedName("Location", "building"), "Building 1");
        neighbourGUID       = createLocation(ViewServicesFvtTestSupport.qualifiedName("Location", "neighbour"), "Neighbouring campus");

        LocationProperties properties = properties(retrieve(campusGUID));

        assertEquals(campusQualifiedName, properties.getQualifiedName());
        assertEquals("Campus", properties.getDisplayName());
        assertEquals(DESCRIPTION, properties.getDescription());
        assertEquals("site", properties.getCategory());
    }


    /**
     * A merge update changes only the properties it supplies.
     */
    @Test
    @Order(2)
    void updateLocation()
    {
        LocationProperties properties = new LocationProperties();

        properties.setDescription("Updated campus description");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        locationArena.post("/locations/" + campusGUID + "/update", requestBody, BooleanResponse.class);

        LocationProperties updated = properties(retrieve(campusGUID));

        assertEquals("Updated campus description", updated.getDescription());
        assertEquals("Campus", updated.getDisplayName(), "a merge update lost the display name");
        assertEquals("site", updated.getCategory(), "a merge update lost the category");
    }


    /**
     * A location created from a template copies the template, with the replacement properties overriding it.
     */
    @Test
    @Order(3)
    void createLocationFromTemplate()
    {
        String copyQualifiedName = ViewServicesFvtTestSupport.qualifiedName("Location", "copy");

        LocationProperties replacementProperties = new LocationProperties();

        replacementProperties.setQualifiedName(copyQualifiedName);
        replacementProperties.setDisplayName("Copied campus");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(campusGUID);
        requestBody.setReplacementProperties(replacementProperties);

        copyGUID = locationArena.create("/locations/from-template", requestBody);

        assertNotEquals(campusGUID, copyGUID);

        LocationProperties copy = properties(retrieve(copyGUID));

        assertEquals(copyQualifiedName, copy.getQualifiedName());
        assertEquals("Copied campus", copy.getDisplayName());
        assertEquals("site", copy.getCategory(), "the template's category was not copied");
    }


    /**
     * Retrieve by name finds exactly the campus; a search finds every location this run created.
     */
    @Test
    @Order(4)
    void findLocations()
    {
        FilterRequestBody filterRequestBody = new FilterRequestBody();

        filterRequestBody.setFilter(campusQualifiedName);

        List<OpenMetadataRootElement> byName = locationArena.elements("/locations/by-name", filterRequestBody);

        assertEquals(1, byName.size(), "by-name should return just the campus");
        assertEquals(campusGUID, byName.get(0).getElementHeader().getGUID());

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::Location::");

        List<String> found = locationArena.elements("/locations/by-search-string", searchRequestBody)
                                          .stream().map(element -> element.getElementHeader().getGUID()).toList();

        assertTrue(found.containsAll(List.of(campusGUID, buildingGUID, neighbourGUID, copyGUID)),
                   "by-search-string missed some of this run's locations: " + found);
    }


    /**
     * Adjacent locations are peers: each sees the other.
     */
    @Test
    @Order(5)
    void adjacentLocations()
    {
        AdjacentLocationProperties properties = new AdjacentLocationProperties();

        properties.setDescription("Across the road");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        locationArena.call("/locations/" + campusGUID + "/adjacent-locations/" + neighbourGUID + "/attach", requestBody);

        assertTrue(relatedGUIDs(campusGUID, OpenMetadataRootElement::getPeerLocations).contains(neighbourGUID),
                   "the campus does not list its adjacent location");
        assertTrue(relatedGUIDs(neighbourGUID, OpenMetadataRootElement::getPeerLocations).contains(campusGUID),
                   "the neighbour does not list its adjacent location");

        locationArena.call("/locations/" + campusGUID + "/adjacent-locations/" + neighbourGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(relatedGUIDs(campusGUID, OpenMetadataRootElement::getPeerLocations).contains(neighbourGUID),
                    "detach did not remove the adjacent location");
    }


    /**
     * A nested location is listed by the location it is nested in, and lists that location as its grouping.
     */
    @Test
    @Order(6)
    void nestedLocations()
    {
        NestedLocationProperties properties = new NestedLocationProperties();

        properties.setLabel("contains");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        locationArena.call("/locations/" + campusGUID + "/nested-locations/" + buildingGUID + "/attach", requestBody);

        assertTrue(relatedGUIDs(campusGUID, OpenMetadataRootElement::getNestedLocations).contains(buildingGUID),
                   "the campus does not list the building as a nested location");
        assertTrue(relatedGUIDs(buildingGUID, OpenMetadataRootElement::getGroupingLocations).contains(campusGUID),
                   "the building does not list the campus as its grouping location");

        locationArena.call("/locations/" + campusGUID + "/nested-locations/" + buildingGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(relatedGUIDs(campusGUID, OpenMetadataRootElement::getNestedLocations).contains(buildingGUID),
                    "detach did not remove the nested location");
    }


    /**
     * A known location links a resource (any referenceable - here the copied location) to where it is found.
     */
    @Test
    @Order(7)
    void knownLocations()
    {
        KnownLocationProperties properties = new KnownLocationProperties();

        properties.setDescription("Kept in the building");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        locationArena.call("/elements/" + copyGUID + "/known-locations/" + buildingGUID + "/attach", requestBody);

        assertTrue(relatedGUIDs(copyGUID, OpenMetadataRootElement::getKnownLocations).contains(buildingGUID),
                   "the resource does not list its known location");
        assertTrue(relatedGUIDs(buildingGUID, OpenMetadataRootElement::getLocalResources).contains(copyGUID),
                   "the location does not list the resource found there");

        /*
         * The detach request is accepted, but see knownLocationDetachRemovesRelationship: it does not remove the
         * relationship.
         */
        locationArena.call("/elements/" + copyGUID + "/known-locations/" + buildingGUID + "/detach", new DeleteRelationshipRequestBody());
    }


    /**
     * Detaching a known location removes the relationship.
     */
    @Test
    @Order(8)
    void knownLocationDetachRemovesRelationship()
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new KnownLocationProperties());

        locationArena.call("/elements/" + neighbourGUID + "/known-locations/" + buildingGUID + "/attach", requestBody);

        assertTrue(relatedGUIDs(neighbourGUID, OpenMetadataRootElement::getKnownLocations).contains(buildingGUID),
                   "the resource does not list its known location");

        locationArena.call("/elements/" + neighbourGUID + "/known-locations/" + buildingGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(relatedGUIDs(neighbourGUID, OpenMetadataRootElement::getKnownLocations).contains(buildingGUID),
                    "detach did not remove the known location");
    }


    /**
     * Each location classification is added with its properties and then removed.
     */
    @Test
    @Order(9)
    void locationClassifications()
    {
        FixedLocationProperties fixedLocationProperties = new FixedLocationProperties();
        fixedLocationProperties.setPostalAddress("1 Campus Way");
        fixedLocationProperties.setTimeZone("Europe/London");

        classifyThenDeclassify("/fixed-location", fixedLocationProperties, OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName);

        CyberLocationProperties cyberLocationProperties = new CyberLocationProperties();
        cyberLocationProperties.setNetworkAddress("10.0.0.1");

        classifyThenDeclassify("/cyber-location", cyberLocationProperties, OpenMetadataType.CYBER_LOCATION_CLASSIFICATION.typeName);

        SecureLocationProperties secureLocationProperties = new SecureLocationProperties();
        secureLocationProperties.setLevel("restricted");

        classifyThenDeclassify("/secure-location", secureLocationProperties, OpenMetadataType.SECURE_LOCATION_CLASSIFICATION.typeName);
    }


    /**
     * Delete every location this test created.
     */
    @Test
    @Order(10)
    void deleteLocations()
    {
        for (String guid : List.of(copyGUID, neighbourGUID, buildingGUID, campusGUID))
        {
            locationArena.call("/locations/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(locationArena, "/locations/" + guid + "/retrieve");
        }
    }


    private static String createLocation(String qualifiedName,
                                          String displayName)
    {
        LocationProperties properties = new LocationProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setDescription(DESCRIPTION);
        properties.setCategory("site");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return locationArena.create("/locations", requestBody);
    }


    private static OpenMetadataRootElement retrieve(String locationGUID)
    {
        OpenMetadataRootElement element = locationArena.element("/locations/" + locationGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "location " + locationGUID + " is missing");
        assertEquals(locationGUID, element.getElementHeader().getGUID());

        return element;
    }


    private static LocationProperties properties(OpenMetadataRootElement element)
    {
        assertTrue(element.getProperties() instanceof LocationProperties, "retrieve returned " + element.getProperties());

        return (LocationProperties) element.getProperties();
    }


    private static List<String> relatedGUIDs(String                                                              locationGUID,
                                             Function<OpenMetadataRootElement, List<RelatedMetadataElementSummary>> relationships)
    {
        List<RelatedMetadataElementSummary> related = relationships.apply(retrieve(locationGUID));

        if (related == null)
        {
            return List.of();
        }

        return related.stream().map(summary -> summary.getRelatedElement().getElementHeader().getGUID()).toList();
    }


    private static List<String> locationKinds(String locationGUID)
    {
        List<ElementClassification> locationKinds = retrieve(locationGUID).getElementHeader().getLocationKinds();

        if (locationKinds == null)
        {
            return List.of();
        }

        return locationKinds.stream().map(ElementClassification::getClassificationName).toList();
    }


    private static void classifyThenDeclassify(String                   request,
                                               ClassificationProperties properties,
                                               String                   classificationName)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        locationArena.call("/locations/" + campusGUID + request, requestBody);

        assertTrue(locationKinds(campusGUID).contains(classificationName),
                   request + " did not add the " + classificationName + " classification: " + locationKinds(campusGUID));

        locationArena.call("/locations/" + campusGUID + request + "/remove", new DeleteClassificationRequestBody());

        assertFalse(locationKinds(campusGUID).contains(classificationName),
                    request + "/remove did not remove the " + classificationName + " classification");
    }
}
