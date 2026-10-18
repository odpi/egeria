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
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringMapResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ValidMetadataValueListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ValidMetadataValueResponse;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValueDetail;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.AttributeTypeDefListResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TypeDefGalleryResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TypeDefListResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TypeDefResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ValidMetadataFVT covers every request of the Valid Metadata OMVS: the valid values for open metadata
 * properties (simple values, map names and map values, and consistent values between properties), the open
 * metadata type queries, and specification properties.
 * <br>
 * The valid values are set up for property names that are unique to this run, so a run against a repository
 * holding a previous run's values is not affected by them.  The clear requests are checked to report success
 * when they remove something, to refuse a value that is not there, and (for clear-map-name) to remove the map
 * name's values along with it.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ValidMetadataFVT
{
    private static final ViewServiceClient validMetadata = new ViewServiceClient(ViewServiceDescription.VALID_METADATA);
    private static final ViewServiceClient locationArena = new ViewServiceClient(ViewServiceDescription.LOCATION_ARENA);

    /*
     * Property names unique to this run.
     */
    private static final String PROPERTY_NAME     = "viewServicesFvtLevel" + OMAGPlatformExtension.RUN_ID;
    private static final String MAP_PROPERTY_NAME = "viewServicesFvtRegions" + OMAGPlatformExtension.RUN_ID;
    private static final String TYPE_NAME         = OpenMetadataType.ASSET.typeName;
    private static final String MAP_NAME          = "region";

    private static final String SPECIFICATION_PROPERTY_NAME = "viewServicesFvtPlaceholder" + OMAGPlatformExtension.RUN_ID;

    private static String subjectGUID               = null;
    private static String otherSubjectGUID          = null;
    private static String specificationPropertyGUID = null;


    /**
     * Valid values for a simple property: one specific to a type and one for every type.  They validate, can be
     * read back individually and in the list for the property, and setting one up again updates it.
     */
    @Test
    @Order(1)
    void validValuesForAProperty()
    {
        validMetadata.call("/setup-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME, validValue("Gold", "Gold level", 1));
        validMetadata.call("/setup-value/" + PROPERTY_NAME, validValue("Silver", "Silver level", 2));

        assertTrue(validate("/validate-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&actualValue=Gold"));
        assertTrue(validate("/validate-value/" + PROPERTY_NAME + "?actualValue=Silver"));
        assertFalse(validate("/validate-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&actualValue=Bronze"),
                    "a value that was never set up is reported as valid");

        ValidMetadataValue gold = getValue("/get-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&preferredValue=Gold");

        assertNotNull(gold, "get-value did not return Gold");
        assertEquals("Gold", gold.getPreferredValue());
        assertEquals("Gold level", gold.getDisplayName());

        List<String> values = preferredValues(validMetadata.get("/get-valid-metadata-values/" + PROPERTY_NAME,
                                                                ValidMetadataValueListResponse.class).getElements());

        assertTrue(values.containsAll(List.of("Gold", "Silver")), "get-valid-metadata-values returned " + values);
        assertEquals(2, values.size(), "get-valid-metadata-values returned " + values);

        /*
         * Setting up an existing value again updates it.
         */
        validMetadata.call("/setup-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME, validValue("Gold", "Gold standard", 1));

        assertEquals("Gold standard",
                     getValue("/get-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&preferredValue=Gold").getDisplayName(),
                     "setting up an existing value did not update it");
    }


    /**
     * Valid map names and the valid values for one of those names.
     */
    @Test
    @Order(2)
    void validValuesForAMap()
    {
        validMetadata.call("/setup-map-name/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME, validValue(MAP_NAME, "Region", 0));
        validMetadata.call("/setup-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME, validValue("EMEA", "Europe, Middle East and Africa", 1));
        validMetadata.call("/setup-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME, validValue("APAC", "Asia Pacific", 2));

        assertTrue(validate("/validate-map-name/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&mapName=" + MAP_NAME));
        assertFalse(validate("/validate-map-name/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&mapName=zone"));
        assertTrue(validate("/validate-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&actualValue=EMEA"));
        assertTrue(validate("/validate-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&actualValue=APAC"));
        assertFalse(validate("/validate-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&actualValue=LATAM"));

        ValidMetadataValue mapName = getValue("/get-map-name/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&mapName=" + MAP_NAME);

        assertNotNull(mapName, "get-map-name did not return the map name");
        assertEquals(MAP_NAME, mapName.getPreferredValue());

        ValidMetadataValue emea = getValue("/get-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&preferredValue=EMEA");

        assertNotNull(emea, "get-map-value did not return EMEA");
        assertEquals("Europe, Middle East and Africa", emea.getDisplayName());

        /*
         * The list for the property holds the map name, and a group for the map name holding its values.
         */
        List<ValidMetadataValueDetail> values = validMetadata.get("/get-valid-metadata-values/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME,
                                                                  ValidMetadataValueListResponse.class).getElements();

        assertNotNull(values);
        assertTrue(preferredValues(values).contains(MAP_NAME), "the map name is not listed: " + preferredValues(values));

        ValidMetadataValueDetail group = values.stream().filter(value -> value.getValidMapNameValues() != null).findFirst().orElse(null);

        assertNotNull(group, "there is no group of values for the map name");
        assertEquals(MAP_NAME, group.getDisplayName());
        assertEquals(List.of("APAC", "EMEA"), group.getValidMapNameValues().stream().map(ValidMetadataValue::getPreferredValue).sorted().toList());
    }


    /**
     * Consistent values link a value of one property to a value of another.
     */
    @Test
    @Order(3)
    void consistentValues()
    {
        validMetadata.call("/" + PROPERTY_NAME + "/consistent-metadata-values/" + MAP_PROPERTY_NAME
                                   + "?typeName1=" + TYPE_NAME + "&preferredValue1=Gold"
                                   + "&typeName2=" + TYPE_NAME + "&mapName2=" + MAP_NAME + "&preferredValue2=EMEA",
                           new NullRequestBody());

        List<String> consistent = preferredValues(validMetadata.get("/" + PROPERTY_NAME + "/consistent-metadata-values?typeName=" + TYPE_NAME + "&preferredValue=Gold",
                                                                    ValidMetadataValueListResponse.class).getElements());

        assertEquals(List.of("EMEA"), consistent, "Gold should be consistent with EMEA");
    }


    /**
     * The clear requests: each reports success when it removes a value, the value no longer validates, and
     * clearing a value that is not there is refused.  Clearing a map name also removes its values.
     */
    @Test
    @Order(4)
    void clearValidValues()
    {
        String clearAPAC = "/clear-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&preferredValue=APAC";

        validMetadata.call(clearAPAC, new DeleteElementRequestBody());

        assertFalse(validate("/validate-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&actualValue=APAC"),
                    "APAC is still valid after it was cleared");
        assertTrue(validate("/validate-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&actualValue=EMEA"),
                   "clearing APAC removed EMEA too");

        validMetadata.postForFailure(clearAPAC, new DeleteElementRequestBody());

        String clearMapName = "/clear-map-name/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&mapName=" + MAP_NAME;

        validMetadata.call(clearMapName, new DeleteElementRequestBody());

        assertFalse(validate("/validate-map-name/" + MAP_PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&mapName=" + MAP_NAME),
                    "the map name is still valid after it was cleared");
        assertFalse(validate("/validate-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&actualValue=EMEA"),
                    "clearing the map name did not remove its values");
        assertNull(getValue("/get-map-value/" + MAP_PROPERTY_NAME + "/" + MAP_NAME + "?typeName=" + TYPE_NAME + "&preferredValue=EMEA"),
                   "get-map-value still returns a value of a cleared map name");

        validMetadata.postForFailure(clearMapName, new DeleteElementRequestBody());

        validMetadata.call("/clear-value/" + PROPERTY_NAME + "?preferredValue=Silver", new DeleteElementRequestBody());

        assertFalse(validate("/validate-value/" + PROPERTY_NAME + "?actualValue=Silver"), "Silver is still valid after it was cleared");
        assertTrue(validate("/validate-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&actualValue=Gold"), "clearing Silver removed Gold too");

        validMetadata.call("/clear-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&preferredValue=Gold", new DeleteElementRequestBody());

        assertFalse(validate("/validate-value/" + PROPERTY_NAME + "?typeName=" + TYPE_NAME + "&actualValue=Gold"));

        validMetadata.postForFailure("/clear-value/" + PROPERTY_NAME + "?preferredValue=Silver", new DeleteElementRequestBody());

        List<ValidMetadataValueDetail> remaining = validMetadata.get("/get-valid-metadata-values/" + PROPERTY_NAME,
                                                                     ValidMetadataValueListResponse.class).getElements();

        assertTrue((remaining == null) || remaining.isEmpty(), "values remain after every value was cleared: " + preferredValues(remaining));
    }


    /**
     * The open metadata type queries return the types loaded from the types archive.
     */
    @Test
    @Order(5)
    void openMetadataTypes()
    {
        TypeDefGalleryResponse gallery = validMetadata.get("/open-metadata-types", TypeDefGalleryResponse.class);

        assertTrue(typeNames(gallery.getTypeDefs()).containsAll(List.of(OpenMetadataType.ASSET.typeName,
                                                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                        OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName)));
        assertFalse(gallery.getAttributeTypeDefs().isEmpty());

        assertCategory("/open-metadata-types/entity-defs", OpenMetadataTypeDefCategory.ENTITY_DEF, OpenMetadataType.ASSET.typeName);
        assertCategory("/open-metadata-types/relationship-defs", OpenMetadataTypeDefCategory.RELATIONSHIP_DEF, OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName);
        assertCategory("/open-metadata-types/classification-defs", OpenMetadataTypeDefCategory.CLASSIFICATION_DEF, OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);

        List<OpenMetadataAttributeTypeDef> enums = validMetadata.get("/open-metadata-types/attribute-defs?category=" + OpenMetadataAttributeTypeDefCategory.ENUM.name(),
                                                                     AttributeTypeDefListResponse.class).getAttributeTypeDefs();

        assertNotNull(enums);
        assertFalse(enums.isEmpty());
        assertTrue(enums.stream().allMatch(attributeTypeDef -> attributeTypeDef.getCategory() == OpenMetadataAttributeTypeDefCategory.ENUM),
                   "attribute-defs?category=ENUM returned a type that is not an enum");
        assertTrue(validMetadata.get("/open-metadata-types/attribute-defs", AttributeTypeDefListResponse.class).getAttributeTypeDefs().size() > enums.size(),
                   "attribute-defs with no category should return more than the enums");

        List<String> subTypes = typeNames(validMetadata.get("/open-metadata-types/sub-types/" + OpenMetadataType.DATA_SET.typeName,
                                                            TypeDefListResponse.class).getTypeDefList().getTypeDefs());

        assertTrue(subTypes.contains(OpenMetadataType.TABULAR_DATA_SET.typeName), "sub-types of DataSet: " + subTypes);
        assertFalse(subTypes.contains(OpenMetadataType.ASSET.typeName), "a supertype is listed as a sub-type");

        List<String> relationships = typeNames(validMetadata.get("/open-metadata-types/" + OpenMetadataType.VALID_VALUE_DEFINITION.typeName + "/attached-relationships",
                                                                 TypeDefListResponse.class).getTypeDefList().getTypeDefs());

        assertTrue(relationships.contains(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName), "relationships for ValidValueDefinition: " + relationships);

        List<String> classifications = typeNames(validMetadata.get("/open-metadata-types/" + OpenMetadataType.LOCATION.typeName + "/attached-classifications",
                                                                   TypeDefListResponse.class).getTypeDefList().getTypeDefs());

        assertTrue(classifications.contains(OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName), "classifications for Location: " + classifications);

        OpenMetadataTypeDef asset = validMetadata.get("/open-metadata-types/name/" + OpenMetadataType.ASSET.typeName, TypeDefResponse.class).getTypeDef();

        assertNotNull(asset);
        assertEquals(OpenMetadataType.ASSET.typeName, asset.getName());
        assertEquals(OpenMetadataType.ASSET.typeGUID, asset.getGUID());
    }


    /**
     * A specification property is set up on an element, found by GUID, name, type and search, attached to a
     * second element and detached from it, and deleted.
     */
    @Test
    @Order(6)
    void specificationProperties()
    {
        Map<String, String> typeNames = validMetadata.get("/specification-properties/type-names", StringMapResponse.class).getStringMap();

        assertNotNull(typeNames);
        assertEquals(SpecificationPropertyType.values().length, typeNames.size());
        assertTrue(typeNames.values().stream().anyMatch(value -> value.startsWith(SpecificationPropertyType.PLACEHOLDER_PROPERTY.getPropertyType())),
                   "type-names does not describe placeholder properties: " + typeNames);

        subjectGUID      = createLocation("specification-subject");
        otherSubjectGUID = createLocation("specification-other-subject");

        PlaceholderPropertyType placeholderProperty = new PlaceholderPropertyType();

        placeholderProperty.setSpecificationPropertyType(SpecificationPropertyType.PLACEHOLDER_PROPERTY);
        placeholderProperty.setName(SPECIFICATION_PROPERTY_NAME);
        placeholderProperty.setDescription("Placeholder created by " + ValidMetadataFVT.class.getSimpleName());
        placeholderProperty.setDataType("string");
        placeholderProperty.setExample("example value");

        specificationPropertyGUID = validMetadata.create("/elements/" + subjectGUID + "/specification-properties", placeholderProperty);

        OpenMetadataRootElement element = validMetadata.element("/specification-properties/" + specificationPropertyGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element);
        assertTrue(element.getProperties() instanceof ValidValueDefinitionProperties, "retrieve returned " + element.getProperties());

        ValidValueDefinitionProperties properties = (ValidValueDefinitionProperties) element.getProperties();

        assertEquals(SPECIFICATION_PROPERTY_NAME, properties.getPreferredValue());
        assertEquals("string", properties.getDataType());

        String assignment = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName;

        assertEquals(List.of(subjectGUID), MetadataExpertProbe.linkedGUIDs(specificationPropertyGUID, assignment),
                     "the specification property is not attached to its element");

        FilterRequestBody filterRequestBody = new FilterRequestBody();

        filterRequestBody.setFilter(SPECIFICATION_PROPERTY_NAME);

        assertEquals(List.of(specificationPropertyGUID), guids(validMetadata.elements("/specification-properties/by-name", filterRequestBody)));

        assertTrue(guids(validMetadata.elements("/specification-properties/by-type?specificationPropertyType=" + SpecificationPropertyType.PLACEHOLDER_PROPERTY.name(),
                                                new ResultsRequestBody())).contains(specificationPropertyGUID),
                   "by-type did not return the placeholder property");

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(SPECIFICATION_PROPERTY_NAME);

        assertTrue(guids(validMetadata.elements("/specification-properties/by-search-string", searchRequestBody)).contains(specificationPropertyGUID),
                   "by-search-string did not return the placeholder property");

        SpecificationPropertyAssignmentProperties assignmentProperties = new SpecificationPropertyAssignmentProperties();

        assignmentProperties.setPropertyName(SpecificationPropertyType.PLACEHOLDER_PROPERTY.getPropertyType());

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(assignmentProperties);

        validMetadata.call("/elements/" + otherSubjectGUID + "/specification-properties/" + specificationPropertyGUID + "/attach", requestBody);

        assertTrue(MetadataExpertProbe.linkedGUIDs(specificationPropertyGUID, assignment).containsAll(List.of(subjectGUID, otherSubjectGUID)),
                   "attach did not link the specification property to the second element");

        validMetadata.call("/elements/" + otherSubjectGUID + "/specification-properties/" + specificationPropertyGUID + "/detach", new DeleteRelationshipRequestBody());

        assertEquals(List.of(subjectGUID), MetadataExpertProbe.linkedGUIDs(specificationPropertyGUID, assignment),
                     "detach did not remove the link to the second element");

        validMetadata.call("/specification-properties/" + specificationPropertyGUID + "/delete", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(validMetadata, "/specification-properties/" + specificationPropertyGUID + "/retrieve");

        for (String guid : List.of(subjectGUID, otherSubjectGUID))
        {
            locationArena.call("/locations/" + guid + "/delete", new DeleteElementRequestBody());
        }
    }


    /**
     * The type-names request lists, for each kind of specification property, the bean class to send when
     * setting one up.  A specification property sent with that class name is accepted.
     */
    @Test
    @Order(7)
    void specificationPropertyTypeNamesAreAccepted()
    {
        Map<String, String> typeNames = validMetadata.get("/specification-properties/type-names", StringMapResponse.class).getStringMap();

        String beanClass = SpecificationPropertyType.PLACEHOLDER_PROPERTY.getBeanClass();

        assertTrue(typeNames.containsKey(beanClass), "type-names does not list " + beanClass);

        String elementGUID = createLocation("specification-type-names");

        Map<String, Object> requestBody = Map.of("class", beanClass,
                                                 "specificationPropertyType", SpecificationPropertyType.PLACEHOLDER_PROPERTY.name(),
                                                 "name", SPECIFICATION_PROPERTY_NAME + "-byTypeName");

        try
        {
            String guid = validMetadata.create("/elements/" + elementGUID + "/specification-properties", requestBody);

            validMetadata.call("/specification-properties/" + guid + "/delete", new DeleteElementRequestBody());
        }
        finally
        {
            locationArena.call("/locations/" + elementGUID + "/delete", new DeleteElementRequestBody());
        }
    }


    private static ValidMetadataValue validValue(String preferredValue,
                                                 String displayName,
                                                 int    ordinal)
    {
        ValidMetadataValue validMetadataValue = new ValidMetadataValue();

        validMetadataValue.setPreferredValue(preferredValue);
        validMetadataValue.setDisplayName(displayName);
        validMetadataValue.setDescription("Set up by " + ValidMetadataFVT.class.getSimpleName());
        validMetadataValue.setOrdinal(ordinal);

        return validMetadataValue;
    }


    private static boolean validate(String path)
    {
        return validMetadata.get(path, BooleanResponse.class).getFlag();
    }


    private static ValidMetadataValue getValue(String path)
    {
        return validMetadata.get(path, ValidMetadataValueResponse.class).getElement();
    }


    private static List<String> preferredValues(List<ValidMetadataValueDetail> values)
    {
        List<String> preferredValues = new ArrayList<>();

        if (values != null)
        {
            for (ValidMetadataValueDetail value : values)
            {
                if (value.getPreferredValue() != null)
                {
                    preferredValues.add(value.getPreferredValue());
                }
            }
        }

        return preferredValues;
    }


    private static List<String> typeNames(List<OpenMetadataTypeDef> typeDefs)
    {
        assertNotNull(typeDefs);

        return typeDefs.stream().map(OpenMetadataTypeDef::getName).toList();
    }


    private static void assertCategory(String                      path,
                                       OpenMetadataTypeDefCategory category,
                                       String                      expectedTypeName)
    {
        List<OpenMetadataTypeDef> typeDefs = validMetadata.get(path, TypeDefListResponse.class).getTypeDefList().getTypeDefs();

        assertTrue(typeNames(typeDefs).contains(expectedTypeName), path + " does not include " + expectedTypeName);
        assertTrue(typeDefs.stream().allMatch(typeDef -> typeDef.getCategory() == category), path + " returned a type that is not a " + category);
    }


    private static List<String> guids(List<OpenMetadataRootElement> elements)
    {
        return elements.stream().map(element -> element.getElementHeader().getGUID()).toList();
    }


    private static String createLocation(String name)
    {
        LocationProperties properties = new LocationProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Location", name));
        properties.setDisplayName(name);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return locationArena.create("/locations", requestBody);
    }
}
