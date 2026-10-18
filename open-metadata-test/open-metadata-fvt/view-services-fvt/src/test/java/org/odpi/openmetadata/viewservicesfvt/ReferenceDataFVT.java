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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ConsistentValidValuesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceDataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueAssociationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueMemberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValuesAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValuesImplementationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValuesMappingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ReferenceDataFVT covers every request of the Reference Data OMVS: the lifecycle of valid value definitions
 * (a code table of countries and its members), and every relationship a valid value definition takes part in.
 * Each attach is checked from the other end with the Metadata Expert's generic relationship query, and each
 * detach is checked the same way.  The element that valid values are assigned to and implemented by is a
 * location created through the Location Arena.
 * <br>
 * The Reference Data service works with ReferenceDataValue elements (a subtype of ValidValueDefinition), so the
 * properties sent are ReferenceDataValueProperties.  (Plain ValidValueDefinitionProperties, as in the service's
 * .http examples, create a ValidValueDefinition that the service's own retrieve request then refuses with
 * OMAG-REPOSITORY-HANDLER-404-001 because it is not a ReferenceDataValue.)
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReferenceDataFVT
{
    private static final ViewServiceClient referenceData = new ViewServiceClient(ViewServiceDescription.REFERENCE_DATA);
    private static final ViewServiceClient locationArena = new ViewServiceClient(ViewServiceDescription.LOCATION_ARENA);

    private static String countriesQualifiedName = null;
    private static String countriesGUID          = null;
    private static String franceGUID             = null;
    private static String frGUID                 = null;
    private static String copyGUID               = null;
    private static String elementGUID            = null;


    /**
     * Create a valid value set, two values and the element they describe, and read the set back.
     */
    @Test
    @Order(1)
    void createAndRetrieveValidValueDefinitions()
    {
        countriesQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ValidValueDefinition", "countries");
        countriesGUID          = createValidValue(countriesQualifiedName, "Countries", null);
        franceGUID             = createValidValue(ViewServicesFvtTestSupport.qualifiedName("ValidValueDefinition", "france"), "France", "France");
        frGUID                 = createValidValue(ViewServicesFvtTestSupport.qualifiedName("ValidValueDefinition", "fr"), "FR", "FR");

        LocationProperties locationProperties = new LocationProperties();

        locationProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Location", "reference-data-subject"));
        locationProperties.setDisplayName("Office");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(locationProperties);

        elementGUID = locationArena.create("/locations", requestBody);

        ValidValueDefinitionProperties properties = retrieve(countriesGUID);

        assertEquals(countriesQualifiedName, properties.getQualifiedName());
        assertEquals("Countries", properties.getDisplayName());
        assertEquals("string", properties.getDataType());
        assertEquals("Country of residence", properties.getUsage());

        assertEquals("France", retrieve(franceGUID).getPreferredValue());
    }


    /**
     * A merge update changes only the properties it supplies.
     */
    @Test
    @Order(2)
    void updateValidValueDefinition()
    {
        ValidValueDefinitionProperties properties = new ReferenceDataValueProperties();

        properties.setScope("Europe");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        referenceData.post("/valid-value-definitions/" + countriesGUID + "/update", requestBody, BooleanResponse.class);

        ValidValueDefinitionProperties updated = retrieve(countriesGUID);

        assertEquals("Europe", updated.getScope());
        assertEquals("Country of residence", updated.getUsage(), "a merge update lost the usage");
    }


    /**
     * A valid value definition created from a template copies the template's properties.
     */
    @Test
    @Order(3)
    void createValidValueDefinitionFromTemplate()
    {
        String copyQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ValidValueDefinition", "germany");

        ValidValueDefinitionProperties replacementProperties = new ReferenceDataValueProperties();

        replacementProperties.setQualifiedName(copyQualifiedName);
        replacementProperties.setDisplayName("Germany");
        replacementProperties.setPreferredValue("Germany");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(franceGUID);
        requestBody.setReplacementProperties(replacementProperties);

        copyGUID = referenceData.create("/valid-value-definitions/from-template", requestBody);

        assertNotEquals(franceGUID, copyGUID);

        ValidValueDefinitionProperties copy = retrieve(copyGUID);

        assertEquals(copyQualifiedName, copy.getQualifiedName());
        assertEquals("Germany", copy.getPreferredValue());
        assertEquals("Country of residence", copy.getUsage(), "the template's usage was not copied");
    }


    /**
     * Retrieve by name finds exactly the code table; a search finds this run's definitions.
     */
    @Test
    @Order(4)
    void findValidValueDefinitions()
    {
        FilterRequestBody filterRequestBody = new FilterRequestBody();

        filterRequestBody.setFilter(countriesQualifiedName);

        List<OpenMetadataRootElement> byName = referenceData.elements("/valid-value-definitions/by-name", filterRequestBody);

        assertEquals(1, byName.size(), "by-name should return just the countries set");
        assertEquals(countriesGUID, byName.get(0).getElementHeader().getGUID());

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::ValidValueDefinition::");

        List<String> found = referenceData.elements("/valid-value-definitions/by-search-string", searchRequestBody)
                                          .stream().map(element -> element.getElementHeader().getGUID()).toList();

        assertTrue(found.containsAll(List.of(countriesGUID, franceGUID, frGUID, copyGUID)),
                   "by-search-string missed some of this run's valid value definitions: " + found);
        assertFalse(found.contains(elementGUID));
    }


    /**
     * Members of a valid value set are listed by the set's own retrieve request.
     */
    @Test
    @Order(5)
    void validValueMembers()
    {
        ValidValueMemberProperties properties = new ValidValueMemberProperties();

        properties.setIsDefaultValue(true);

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        referenceData.call("/valid-values/" + countriesGUID + "/members/" + franceGUID + "/attach", requestBody);

        List<RelatedMetadataElementSummary> members = referenceData.element("/valid-value-definitions/" + countriesGUID + "/retrieve", new GetRequestBody()).getValidValueMembers();

        assertNotNull(members, "the set lists no members");
        assertTrue(members.stream().anyMatch(member -> franceGUID.equals(member.getRelatedElement().getElementHeader().getGUID())),
                   "the set does not list France as a member");

        referenceData.call("/valid-values/" + countriesGUID + "/members/" + franceGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(MetadataExpertProbe.linkedGUIDs(countriesGUID, OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName).contains(franceGUID),
                    "detach did not remove the member");
    }


    /**
     * The relationships between two valid values, and between a valid value and an element.
     */
    @Test
    @Order(6)
    void validValueRelationships()
    {
        ValidValueAssociationProperties associationProperties = new ValidValueAssociationProperties();
        associationProperties.setAssociationName("capital-city");

        attachThenDetach("/valid-values/" + franceGUID + "/associated-valid-values/" + copyGUID,
                         associationProperties, franceGUID, copyGUID,
                         OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName);

        attachThenDetach("/valid-values/" + franceGUID + "/consistent-valid-values/" + frGUID,
                         new ConsistentValidValuesProperties(), franceGUID, frGUID,
                         OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName);

        ValidValuesMappingProperties mappingProperties = new ValidValuesMappingProperties();
        mappingProperties.setAssociationDescription("ISO 3166 alpha-2 code");
        mappingProperties.setConfidence(100);

        attachThenDetach("/valid-values/" + franceGUID + "/mapped-valid-values/" + frGUID,
                         mappingProperties, franceGUID, frGUID,
                         OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName);

        ValidValuesAssignmentProperties assignmentProperties = new ValidValuesAssignmentProperties();
        assignmentProperties.setStrictRequirement(true);

        attachThenDetach("/elements/" + elementGUID + "/valid-values-assignment/" + countriesGUID,
                         assignmentProperties, elementGUID, countriesGUID,
                         OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName);

        ReferenceValueAssignmentProperties referenceValueProperties = new ReferenceValueAssignmentProperties();
        referenceValueProperties.setAttributeName("country");
        referenceValueProperties.setConfidence(90);

        attachThenDetach("/elements/" + elementGUID + "/reference-value-assignment/" + franceGUID,
                         referenceValueProperties, elementGUID, franceGUID,
                         OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName);
    }


    /**
     * A valid values implementation is attached (returning the relationship's GUID), updated, and detached by
     * that GUID; then attached again and detached by the GUIDs of its ends.
     */
    @Test
    @Order(7)
    void validValueImplementations()
    {
        String implementationType = OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName;

        ValidValuesImplementationProperties properties = new ValidValuesImplementationProperties();

        properties.setSymbolicName("FRANCE");
        properties.setImplementationValue("250");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String relationshipGUID = referenceData.create("/valid-values/" + franceGUID + "/implementation/elements/" + elementGUID + "/attach", requestBody);

        assertEquals(List.of(relationshipGUID), MetadataExpertProbe.relationshipGUIDs(franceGUID, implementationType),
                     "attach did not return the GUID of the relationship it created");
        assertTrue(MetadataExpertProbe.linkedGUIDs(elementGUID, implementationType).contains(franceGUID));
        assertEquals("250", MetadataExpertProbe.relationshipStringProperty(relationshipGUID, "implementationValue"));

        ValidValuesImplementationProperties updatedProperties = new ValidValuesImplementationProperties();

        updatedProperties.setImplementationValue("FRA");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(updatedProperties);

        referenceData.call("/valid-value-implementations/" + relationshipGUID + "/update", updateRequestBody);

        assertEquals("FRA", MetadataExpertProbe.relationshipStringProperty(relationshipGUID, "implementationValue"));
        assertEquals("FRANCE", MetadataExpertProbe.relationshipStringProperty(relationshipGUID, "symbolicName"),
                     "a merge update lost the symbolic name");

        referenceData.call("/valid-value-implementations/" + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(MetadataExpertProbe.relationshipGUIDs(franceGUID, implementationType).isEmpty(),
                   "detach by relationship GUID did not remove the implementation");

        referenceData.create("/valid-values/" + franceGUID + "/implementation/elements/" + elementGUID + "/attach", requestBody);

        assertEquals(1, MetadataExpertProbe.relationshipGUIDs(franceGUID, implementationType).size());

        referenceData.call("/valid-values/" + franceGUID + "/implementation/elements/" + elementGUID + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(MetadataExpertProbe.relationshipGUIDs(franceGUID, implementationType).isEmpty(),
                   "detach by element GUIDs did not remove the implementation");
    }


    /**
     * Delete everything this test created.
     */
    @Test
    @Order(8)
    void deleteValidValueDefinitions()
    {
        for (String guid : List.of(copyGUID, frGUID, franceGUID, countriesGUID))
        {
            referenceData.call("/valid-value-definitions/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(referenceData, "/valid-value-definitions/" + guid + "/retrieve");
        }

        locationArena.call("/locations/" + elementGUID + "/delete", new DeleteElementRequestBody());
    }


    private static String createValidValue(String qualifiedName,
                                           String displayName,
                                           String preferredValue)
    {
        ValidValueDefinitionProperties properties = new ReferenceDataValueProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setPreferredValue(preferredValue);
        properties.setDataType("string");
        properties.setUsage("Country of residence");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return referenceData.create("/valid-value-definitions", requestBody);
    }


    private static ValidValueDefinitionProperties retrieve(String validValueDefinitionGUID)
    {
        OpenMetadataRootElement element = referenceData.element("/valid-value-definitions/" + validValueDefinitionGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "valid value definition " + validValueDefinitionGUID + " is missing");
        assertEquals(validValueDefinitionGUID, element.getElementHeader().getGUID());
        assertTrue(element.getProperties() instanceof ValidValueDefinitionProperties, "retrieve returned " + element.getProperties());

        return (ValidValueDefinitionProperties) element.getProperties();
    }


    private static void attachThenDetach(String                 pathPrefix,
                                         RelationshipProperties properties,
                                         String                 end1GUID,
                                         String                 end2GUID,
                                         String                 relationshipTypeName)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        referenceData.call(pathPrefix + "/attach", requestBody);

        assertTrue(MetadataExpertProbe.linkedGUIDs(end1GUID, relationshipTypeName).contains(end2GUID),
                   pathPrefix + "/attach did not create a " + relationshipTypeName + " relationship");
        assertTrue(MetadataExpertProbe.linkedGUIDs(end2GUID, relationshipTypeName).contains(end1GUID),
                   pathPrefix + "/attach: the " + relationshipTypeName + " relationship is not visible from the other end");

        referenceData.call(pathPrefix + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(MetadataExpertProbe.linkedGUIDs(end1GUID, relationshipTypeName).contains(end2GUID),
                    pathPrefix + "/detach did not remove the " + relationshipTypeName + " relationship");
    }
}
