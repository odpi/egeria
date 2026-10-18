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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.GraphEdgeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.GraphVertexProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeChoiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.StructSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APISchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaTypeListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SchemaMakerFVT covers every request of the Schema Maker OMVS (a generic view service, called here through its
 * own "schema-maker" URL marker): the create/update/find/retrieve/delete lifecycle of schema types and schema
 * attributes, each of the relationships it links and detaches between schema elements (nested attributes,
 * attribute-for-schema, foreign keys, linked external schema types, map from/to types, graph edges, query
 * targets, schema, relational DB schemas, API operations/headers/requests/responses and schema type options)
 * and the three classifications it adds and removes (primary key, type-embedded attribute, calculated value).
 * <br>
 * Every element is created as its own anchor so that each link is between independent elements, and every link
 * is checked by reading the element back and finding the relationship to the other element, then checked again
 * after it is detached.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SchemaMakerFVT
{
    private static final ViewServiceClient schemaMaker = new ViewServiceClient(ViewServiceDescription.SCHEMA_MAKER);

    private static final String SCHEMA_TYPES      = "/schema-types/";
    private static final String SCHEMA_ATTRIBUTES = "/schema-attributes/";

    /*
     * Everything created, in the order it is deleted at the end.
     */
    private static final List<String> createdSchemaTypes      = new ArrayList<>();
    private static final List<String> createdSchemaAttributes = new ArrayList<>();

    private static String structTypeGUID     = null;
    private static String attributeGUID      = null;


    /**
     * Create a struct schema type, read it back, update it, find it by name and search string, and copy it
     * from a template.
     */
    @Test
    @Order(1)
    void schemaTypeLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("StructSchemaType", "customer");

        structTypeGUID = createSchemaType(structType("customer"));

        OpenMetadataRootElement element = DesignFvtSupport.assertReadable(schemaMaker,
                                                                         schemaTypeRetrieve(structTypeGUID),
                                                                         OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName,
                                                                         qualifiedName);
        assertEquals("Customer record", ((ReferenceableProperties)element.getProperties()).getDisplayName());

        StructSchemaTypeProperties updateProperties = new StructSchemaTypeProperties();

        updateProperties.setDescription("Updated by SchemaMakerFVT");
        updateProperties.setUsage("Customer master data");

        BooleanResponse updated = schemaMaker.post(SCHEMA_TYPES + structTypeGUID + "/update",
                                                   DesignFvtSupport.update(updateProperties),
                                                   BooleanResponse.class);
        assertTrue(updated.getFlag(), "updateSchemaType reported no change");

        element = schemaMaker.element(schemaTypeRetrieve(structTypeGUID), new GetRequestBody());

        StructSchemaTypeProperties readBack = assertInstanceOf(StructSchemaTypeProperties.class, element.getProperties());

        assertEquals("Updated by SchemaMakerFVT", readBack.getDescription());
        assertEquals("Customer master data", readBack.getUsage());
        assertEquals("Customer record", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(schemaMaker.elements("/schema-types/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        structTypeGUID, "getSchemaTypesByName");
        DesignFvtSupport.assertContains(schemaMaker.elements("/schema-types/by-search-string", DesignFvtSupport.runSearch()),
                                        structTypeGUID, "findSchemaTypes");

        StructSchemaTypeProperties copyProperties = new StructSchemaTypeProperties();
        String                     copyName       = ViewServicesFvtTestSupport.qualifiedName("StructSchemaType", "customer-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = schemaMaker.create("/schema-types/from-template", DesignFvtSupport.fromTemplate(structTypeGUID, copyProperties));

        createdSchemaTypes.add(copyGUID);
        assertNotEquals(structTypeGUID, copyGUID);

        OpenMetadataRootElement copy = DesignFvtSupport.assertReadable(schemaMaker,
                                                                      schemaTypeRetrieve(copyGUID),
                                                                      OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName,
                                                                      copyName);
        assertEquals("Customer record", ((ReferenceableProperties)copy.getProperties()).getDisplayName(),
                     "the copy should take the template's display name");
    }


    /**
     * Create a schema attribute, read it back, update it, find it by name and search string, and copy it from a
     * template.
     */
    @Test
    @Order(2)
    void schemaAttributeLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("SchemaAttribute", "customer-name");

        attributeGUID = createSchemaAttribute(attribute(new SchemaAttributeProperties(), "customer-name"));

        DesignFvtSupport.assertReadable(schemaMaker,
                                        schemaAttributeRetrieve(attributeGUID),
                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                        qualifiedName);

        SchemaAttributeProperties updateProperties = new SchemaAttributeProperties();

        updateProperties.setDescription("Updated by SchemaMakerFVT");
        updateProperties.setNativeClass("java.lang.String");

        BooleanResponse updated = schemaMaker.post(SCHEMA_ATTRIBUTES + attributeGUID + "/update",
                                                   DesignFvtSupport.update(updateProperties),
                                                   BooleanResponse.class);
        assertTrue(updated.getFlag(), "updateSchemaAttribute reported no change");

        SchemaAttributeProperties readBack = assertInstanceOf(SchemaAttributeProperties.class,
                                                              schemaMaker.element(schemaAttributeRetrieve(attributeGUID), new GetRequestBody()).getProperties());

        assertEquals("Updated by SchemaMakerFVT", readBack.getDescription());
        assertEquals("java.lang.String", readBack.getNativeClass());
        assertEquals("customer-name", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(schemaMaker.elements("/schema-attributes/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        attributeGUID, "getSchemaAttributesByName");
        DesignFvtSupport.assertContains(schemaMaker.elements("/schema-attributes/by-search-string", DesignFvtSupport.runSearch()),
                                        attributeGUID, "findSchemaAttributes");

        SchemaAttributeProperties copyProperties = new SchemaAttributeProperties();
        String                    copyName       = ViewServicesFvtTestSupport.qualifiedName("SchemaAttribute", "customer-name-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = schemaMaker.create("/schema-attributes/from-template", DesignFvtSupport.fromTemplate(attributeGUID, copyProperties));

        createdSchemaAttributes.add(copyGUID);
        assertNotEquals(attributeGUID, copyGUID);

        DesignFvtSupport.assertReadable(schemaMaker,
                                        schemaAttributeRetrieve(copyGUID),
                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                        copyName);
    }


    /**
     * NestedSchemaAttribute links two schema attributes, with its position.
     */
    @Test
    @Order(3)
    void nestedSchemaAttributes()
    {
        String childGUID = createSchemaAttribute(attribute(new SchemaAttributeProperties(), "customer-first-name"));

        NestedSchemaAttributeProperties properties = new NestedSchemaAttributeProperties();

        properties.setPosition(3);

        String path = SCHEMA_ATTRIBUTES + attributeGUID + "/nested-schema-attributes/" + childGUID;

        schemaMaker.call(path + "/attach", relationship(properties));

        RelatedMetadataElementSummary link = DesignFvtSupport.assertLinked(schemaMaker,
                                                                           schemaAttributeRetrieve(attributeGUID),
                                                                           OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                                           childGUID);
        assertEquals(3, assertInstanceOf(NestedSchemaAttributeProperties.class, link.getRelationshipProperties()).getPosition());

        DesignFvtSupport.assertLinked(schemaMaker,
                                      schemaAttributeRetrieve(childGUID),
                                      OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                      attributeGUID);

        schemaMaker.call(path + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(schemaMaker,
                                         schemaAttributeRetrieve(attributeGUID),
                                         OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                         childGUID);
    }


    /**
     * AttributeForSchema links a complex schema type to its attributes.
     */
    @Test
    @Order(4)
    void attributeForSchema()
    {
        AttributeForSchemaProperties properties = new AttributeForSchemaProperties();

        properties.setPosition(1);

        String path = SCHEMA_TYPES + structTypeGUID + "/attribute-for-schema/" + attributeGUID;

        schemaMaker.call(path + "/attach", relationship(properties));

        RelatedMetadataElementSummary link = DesignFvtSupport.assertLinked(schemaMaker,
                                                                           schemaTypeRetrieve(structTypeGUID),
                                                                           OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                           attributeGUID);
        assertEquals(1, assertInstanceOf(AttributeForSchemaProperties.class, link.getRelationshipProperties()).getPosition());

        DesignFvtSupport.assertLinked(schemaMaker,
                                      schemaAttributeRetrieve(attributeGUID),
                                      OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                      structTypeGUID);

        schemaMaker.call(path + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(schemaMaker,
                                         schemaTypeRetrieve(structTypeGUID),
                                         OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                         attributeGUID);
    }


    /**
     * A relational column is classified as a primary key, another column links to it as a foreign key, and both
     * are removed again.
     */
    @Test
    @Order(5)
    void primaryAndForeignKeys()
    {
        String primaryKeyColumnGUID = createSchemaAttribute(attribute(new RelationalColumnProperties(), "customer-id"));
        String foreignKeyColumnGUID = createSchemaAttribute(attribute(new RelationalColumnProperties(), "order-customer-id"));

        PrimaryKeyProperties primaryKeyProperties = new PrimaryKeyProperties();

        primaryKeyProperties.setDisplayName("customer_pk");

        schemaMaker.call("/relational-columns/" + primaryKeyColumnGUID + "/primary-key", classification(primaryKeyProperties));

        ElementClassification primaryKey = readSchemaAttribute(primaryKeyColumnGUID).getElementHeader().getPrimaryKey();

        assertNotNull(primaryKey, "addPrimaryKeyClassification did not classify the column");
        assertEquals("customer_pk", assertInstanceOf(PrimaryKeyProperties.class, primaryKey.getClassificationProperties()).getDisplayName());

        ForeignKeyProperties foreignKeyProperties = new ForeignKeyProperties();

        foreignKeyProperties.setDisplayName("order_customer_fk");

        String path = SCHEMA_ATTRIBUTES + primaryKeyColumnGUID + "/foreign-keys/" + foreignKeyColumnGUID;

        schemaMaker.call(path + "/attach", relationship(foreignKeyProperties));

        RelatedMetadataElementSummary link = DesignFvtSupport.assertLinked(schemaMaker,
                                                                           schemaAttributeRetrieve(primaryKeyColumnGUID),
                                                                           OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                                           foreignKeyColumnGUID);
        assertEquals("order_customer_fk", assertInstanceOf(ForeignKeyProperties.class, link.getRelationshipProperties()).getDisplayName());

        schemaMaker.call(path + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(schemaMaker,
                                         schemaAttributeRetrieve(primaryKeyColumnGUID),
                                         OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                         foreignKeyColumnGUID);

        schemaMaker.call("/relational-columns/" + primaryKeyColumnGUID + "/primary-key/remove", new DeleteClassificationRequestBody());

        assertNull(readSchemaAttribute(primaryKeyColumnGUID).getElementHeader().getPrimaryKey(),
                   "removePrimaryKeyClassification left the classification on the column");
    }


    /**
     * TypeEmbeddedAttribute and CalculatedValue classify a schema attribute and are removed again.
     */
    @Test
    @Order(6)
    void typeEmbeddedAttributeAndCalculatedValue()
    {
        TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties = new TypeEmbeddedAttributeProperties();

        typeEmbeddedAttributeProperties.setSchemaTypeName(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);
        typeEmbeddedAttributeProperties.setDataType("string");

        schemaMaker.call(SCHEMA_ATTRIBUTES + attributeGUID + "/type-embedded-attribute", classification(typeEmbeddedAttributeProperties));

        ElementClassification typeEmbedded = readSchemaAttribute(attributeGUID).getElementHeader().getSchemaType();

        assertNotNull(typeEmbedded, "addTypeEmbeddedAttribute did not classify the attribute");
        assertEquals("string", assertInstanceOf(TypeEmbeddedAttributeProperties.class, typeEmbedded.getClassificationProperties()).getDataType());

        schemaMaker.call(SCHEMA_ATTRIBUTES + attributeGUID + "/type-embedded-attribute/remove", new DeleteClassificationRequestBody());

        assertNull(readSchemaAttribute(attributeGUID).getElementHeader().getSchemaType(),
                   "removeTypeEmbeddedAttribute left the classification on the attribute");

        CalculatedValueProperties calculatedValueProperties = new CalculatedValueProperties();

        calculatedValueProperties.setFormula("upper(name)");
        calculatedValueProperties.setFormulaType("SQL");

        schemaMaker.call(SCHEMA_ATTRIBUTES + attributeGUID + "/calculated-value", classification(calculatedValueProperties));

        ElementClassification calculatedValue = readSchemaAttribute(attributeGUID).getElementHeader().getCalculatedValue();

        assertNotNull(calculatedValue, "addCalculatedValue did not classify the attribute");
        assertEquals("upper(name)", assertInstanceOf(CalculatedValueProperties.class, calculatedValue.getClassificationProperties()).getFormula());

        schemaMaker.call(SCHEMA_ATTRIBUTES + attributeGUID + "/calculated-value/remove", new DeleteClassificationRequestBody());

        assertNull(readSchemaAttribute(attributeGUID).getElementHeader().getCalculatedValue(),
                   "removeCalculatedValue left the classification on the attribute");
    }


    /**
     * The relationships from any schema element to a schema type: linked external schema type, map from and
     * map to element types, and schema type options.  Each is checked from the schema element.
     */
    @Test
    @Order(7)
    void schemaElementToSchemaTypeLinks()
    {
        String mapKeyTypeGUID = createSchemaType(structType("map-key"));
        String choiceGUID     = createSchemaType(schemaType(new SchemaTypeChoiceProperties(), "customer-choice"));

        linkAndDetach("/schema-elements/" + attributeGUID + "/linked-external-schema-types/" + structTypeGUID,
                      schemaAttributeRetrieve(attributeGUID), OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName, structTypeGUID);

        linkAndDetach("/schema-elements/" + attributeGUID + "/map-from-element-types/" + mapKeyTypeGUID,
                      schemaAttributeRetrieve(attributeGUID), OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName, mapKeyTypeGUID);

        linkAndDetach("/schema-elements/" + attributeGUID + "/map-to-element-types/" + structTypeGUID,
                      schemaAttributeRetrieve(attributeGUID), OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName, structTypeGUID);

        linkAndDetach("/schema-elements/" + choiceGUID + "/schema-type-options/" + structTypeGUID,
                      schemaTypeRetrieve(choiceGUID), OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName, structTypeGUID);
    }


    /**
     * linkExternalSchemaType has its own path below /schema-elements, which also works for a schema type.
     */
    @Test
    @Order(8)
    void linkedExternalSchemaTypeFromSchemaElementPath()
    {
        String externalTypeGUID = createSchemaType(structType("external"));

        linkAndDetach("/schema-elements/" + structTypeGUID + "/linked-external-schema-types/" + externalTypeGUID,
                      schemaTypeRetrieve(structTypeGUID), OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName, externalTypeGUID);
    }


    /**
     * GraphEdgeLink links a graph edge to a graph vertex; DerivedSchemaTypeQueryTarget links two schema
     * elements; Schema links any referenceable to its schema type.
     */
    @Test
    @Order(9)
    void graphEdgesQueryTargetsAndSchemas()
    {
        String edgeGUID   = createSchemaAttribute(attribute(new GraphEdgeProperties(), "places-order"));
        String vertexGUID = createSchemaAttribute(attribute(new GraphVertexProperties(), "customer-vertex"));

        linkAndDetach("/graph-edges/" + edgeGUID + "/graph-vertices/" + vertexGUID,
                      schemaAttributeRetrieve(edgeGUID), OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName, vertexGUID);

        String derivedGUID = createSchemaAttribute(attribute(new SchemaAttributeProperties(), "derived-customer-name"));

        linkAndDetach("/schema-elements/" + derivedGUID + "/query-targets/" + attributeGUID,
                      schemaAttributeRetrieve(derivedGUID), OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName, attributeGUID);
    }


    /**
     * Schema links any referenceable to the schema type that describes its structure; detach removes it.
     */
    @Test
    @Order(10)
    void schemas()
    {
        String describedGUID = createSchemaAttribute(attribute(new SchemaAttributeProperties(), "described-element"));

        linkAndDetach("/elements/" + describedGUID + "/schema-types/" + structTypeGUID,
                      schemaAttributeRetrieve(describedGUID), OpenMetadataType.SCHEMA_RELATIONSHIP.typeName, structTypeGUID);
    }


    /**
     * Until the detachSchema defect is fixed, the Schema relationship can still be created, and the detach
     * request is still called (and reports success), so both requests are exercised.  The attach is checked;
     * the detach's result is not, because it does not work - see {@link #schemas()}.
     */
    @Test
    @Order(11)
    void schemaAttachWhileDetachIsBroken()
    {
        String describedGUID = createSchemaAttribute(attribute(new SchemaAttributeProperties(), "described-element-2"));

        schemaMaker.call("/elements/" + describedGUID + "/schema-types/" + structTypeGUID + "/attach", new NewRelationshipRequestBody());
        DesignFvtSupport.assertLinked(schemaMaker, schemaAttributeRetrieve(describedGUID), OpenMetadataType.SCHEMA_RELATIONSHIP.typeName, structTypeGUID);

        schemaMaker.call("/elements/" + describedGUID + "/schema-types/" + structTypeGUID + "/detach", new DeleteRelationshipRequestBody());
    }


    /**
     * RelationalDBSchema links a database schema type list to a relational database schema type.
     */
    @Test
    @Order(12)
    void relationalDBSchemas()
    {
        String listGUID   = createSchemaType(schemaType(new RelationalDBSchemaTypeListProperties(), "database-schemas"));
        String schemaGUID = createSchemaType(schemaType(new RelationalDBSchemaTypeProperties(), "sales-schema"));

        linkAndDetach("/relational-db-schema-type-lists/" + listGUID + "/relational-db-schemas/" + schemaGUID,
                      schemaTypeRetrieve(listGUID), OpenMetadataType.RELATIONAL_DB_SCHEMA.typeName, schemaGUID);
    }


    /**
     * An API schema type has operations, and an operation has header, request and response schema types.
     */
    @Test
    @Order(13)
    void apiOperations()
    {
        String apiGUID       = createSchemaType(schemaType(new APISchemaTypeProperties(), "customer-api"));
        String operationGUID = createSchemaType(schemaType(new APIOperationProperties(), "get-customer"));

        linkAndDetach("/api-schema-types/" + apiGUID + "/api-operations/" + operationGUID,
                      schemaTypeRetrieve(apiGUID), OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeName, operationGUID);

        String headerGUID   = createSchemaType(structType("api-header"));
        String requestGUID  = createSchemaType(structType("api-request"));
        String responseGUID = createSchemaType(structType("api-response"));

        linkAndDetach("/api-operations/" + operationGUID + "/api-headers/" + headerGUID,
                      schemaTypeRetrieve(operationGUID), OpenMetadataType.API_HEADER_RELATIONSHIP.typeName, headerGUID);
        linkAndDetach("/api-operations/" + operationGUID + "/api-requests/" + requestGUID,
                      schemaTypeRetrieve(operationGUID), OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName, requestGUID);
        linkAndDetach("/api-operations/" + operationGUID + "/api-responses/" + responseGUID,
                      schemaTypeRetrieve(operationGUID), OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName, responseGUID);
    }


    /**
     * Delete everything this class created; deleted elements are no longer returned or found.
     */
    @Test
    @Order(99)
    void deleteEverything()
    {
        for (String guid : createdSchemaAttributes)
        {
            DesignFvtSupport.deleteAndCheck(schemaMaker, SCHEMA_ATTRIBUTES + guid, schemaAttributeRetrieve(guid));
        }

        for (String guid : createdSchemaTypes)
        {
            DesignFvtSupport.deleteAndCheck(schemaMaker, SCHEMA_TYPES + guid, schemaTypeRetrieve(guid));
        }

        if (attributeGUID != null)
        {
            DesignFvtSupport.assertNotContains(schemaMaker.elements("/schema-attributes/by-search-string", DesignFvtSupport.runSearch()),
                                               attributeGUID, "findSchemaAttributes after delete");
        }

        if (structTypeGUID != null)
        {
            DesignFvtSupport.assertNotContains(schemaMaker.elements("/schema-types/by-search-string", DesignFvtSupport.runSearch()),
                                               structTypeGUID, "findSchemaTypes after delete");
        }
    }


    /*
     * Helpers
     */

    private static void linkAndDetach(String path,
                                      String retrievePath,
                                      String relationshipTypeName,
                                      String otherEndGUID)
    {
        schemaMaker.call(path + "/attach", new NewRelationshipRequestBody());
        DesignFvtSupport.assertLinked(schemaMaker, retrievePath, relationshipTypeName, otherEndGUID);

        schemaMaker.call(path + "/detach", new DeleteRelationshipRequestBody());
        DesignFvtSupport.assertNotLinked(schemaMaker, retrievePath, relationshipTypeName, otherEndGUID);
    }


    private static String createSchemaType(ReferenceableProperties properties)
    {
        String guid = schemaMaker.create("/schema-types", DesignFvtSupport.newElement(properties));

        createdSchemaTypes.add(0, guid);

        return guid;
    }


    private static String createSchemaAttribute(SchemaAttributeProperties properties)
    {
        String guid = schemaMaker.create("/schema-attributes", DesignFvtSupport.newElement(properties));

        createdSchemaAttributes.add(0, guid);

        return guid;
    }


    private static StructSchemaTypeProperties structType(String name)
    {
        StructSchemaTypeProperties properties = schemaType(new StructSchemaTypeProperties(), name);

        properties.setDisplayName("Customer record");

        return properties;
    }


    private static <T extends ReferenceableProperties> T schemaType(T      properties,
                                                                     String name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getTypeName(), name));
        properties.setDisplayName(name);

        return properties;
    }


    private static SchemaAttributeProperties attribute(SchemaAttributeProperties properties,
                                                       String                    name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getTypeName(), name));
        properties.setDisplayName(name);

        return properties;
    }


    private static NewRelationshipRequestBody relationship(RelationshipBeanProperties properties)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    private static NewClassificationRequestBody classification(ClassificationBeanProperties properties)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    private static OpenMetadataRootElement readSchemaAttribute(String guid)
    {
        OpenMetadataRootElement element = schemaMaker.element(schemaAttributeRetrieve(guid), new GetRequestBody());

        assertNotNull(element, "schema attribute " + guid + " is missing");

        return element;
    }


    private static String schemaTypeRetrieve(String guid)
    {
        return SCHEMA_TYPES + guid + "/retrieve";
    }


    private static String schemaAttributeRetrieve(String guid)
    {
        return SCHEMA_ATTRIBUTES + guid + "/retrieve";
    }
}
