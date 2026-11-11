/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis;

import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPISchema;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


/**
 * Verify which values the OpenAPI cataloguer links to a shared bean schema type.  Linking (rather than copying the
 * bean's fields under every value) is what keeps the catalogue to one copy of each bean, and what stops a bean that
 * refers to itself, such as OMAGConnectorProperties.nestedConnectors, from being expanded without end.
 */
public class BeanReferencesTest
{
    private static final String PREFIX = "#/components/schemas/";

    private static OpenAPISchema refTo(String name)
    {
        OpenAPISchema schema = new OpenAPISchema();

        schema.set$ref(PREFIX + name);

        return schema;
    }

    private static OpenAPISchema typed(String type)
    {
        OpenAPISchema schema = new OpenAPISchema();

        schema.setType(type);

        return schema;
    }

    private static OpenAPISchema allOf(OpenAPISchema... parts)
    {
        OpenAPISchema schema = new OpenAPISchema();

        schema.setAllOf(List.of(parts));

        return schema;
    }

    private static OpenAPISchema oneOf(OpenAPISchema... alternatives)
    {
        OpenAPISchema schema = new OpenAPISchema();

        schema.setOneOf(List.of(alternatives));

        return schema;
    }

    private static OpenAPISchema objectWith(String fieldName, OpenAPISchema fieldSchema)
    {
        OpenAPISchema schema = typed("object");

        schema.setProperties(Map.of(fieldName, fieldSchema));

        return schema;
    }

    private static OpenAPISchema arrayOf(OpenAPISchema items)
    {
        OpenAPISchema schema = typed("array");

        schema.setItems(items);

        return schema;
    }

    /**
     * Components: a self-referencing OMAGConnectorProperties bean, an enum, an empty object, and an array component.
     * Connection has two subclasses, written as allOf: VirtualConnection adds a field of its own, and
     * PlainConnection adds none.
     */
    private static Map<String, OpenAPISchema> components()
    {
        OpenAPISchema connection = typed("object");

        connection.setProperties(Map.of("displayName", typed("string"),
                                        "nestedConnectors", arrayOf(refTo("OMAGConnectorProperties"))));

        OpenAPISchema status = typed("string");

        status.setEnumValues(List.of("ACTIVE", "DELETED"));

        Map<String, OpenAPISchema> components = new HashMap<>();

        components.put("OMAGConnectorProperties", connection);
        components.put("ElementStatus", status);
        components.put("Empty", typed("object"));
        components.put("ConnectorPropertiesList", arrayOf(refTo("OMAGConnectorProperties")));
        components.put("Connection", objectWith("qualifiedName", typed("string")));
        components.put("VirtualConnection", allOf(refTo("Connection"), objectWith("embeddedConnections", arrayOf(refTo("Connection")))));
        components.put("PlainConnection", allOf(refTo("Connection")));
        components.put("StatusOnly", allOf(refTo("ElementStatus")));

        return components;
    }


    @Test
    public void testDirectBeanReference()
    {
        assertEquals(BeanReferences.getReferencedBeans(refTo("OMAGConnectorProperties"), components()), List.of("OMAGConnectorProperties"));
    }


    @Test
    public void testArrayOfBeans()
    {
        assertEquals(BeanReferences.getReferencedBeans(arrayOf(refTo("OMAGConnectorProperties")), components()), List.of("OMAGConnectorProperties"));
    }


    @Test
    public void testSelfReferenceLinksBackToTheSameBean()
    {
        OpenAPISchema nestedConnectors = components().get("OMAGConnectorProperties").getProperties().get("nestedConnectors");

        assertEquals(BeanReferences.getReferencedBeans(nestedConnectors, components()), List.of("OMAGConnectorProperties"));
    }


    @Test
    public void testArrayComponentResolvesToItsElementBean()
    {
        assertEquals(BeanReferences.getReferencedBeans(refTo("ConnectorPropertiesList"), components()), List.of("OMAGConnectorProperties"));
    }


    @Test
    public void testNonBeansAreCataloguedInline()
    {
        assertNull(BeanReferences.getReferencedBeans(refTo("ElementStatus"), components()));
        assertNull(BeanReferences.getReferencedBeans(refTo("Empty"), components()));
        assertNull(BeanReferences.getReferencedBeans(arrayOf(refTo("ElementStatus")), components()));
        assertNull(BeanReferences.getReferencedBeans(typed("string"), components()));
        assertNull(BeanReferences.getReferencedBeans(arrayOf(typed("object")), components()));
        assertNull(BeanReferences.getReferencedBeans(refTo("Missing"), components()));
        assertNull(BeanReferences.getReferencedBeans(null, components()));
        assertNull(BeanReferences.getReferencedBeans(refTo("OMAGConnectorProperties"), null));
    }


    @Test
    public void testComponentName()
    {
        assertEquals(BeanReferences.getComponentName(PREFIX + "OMAGConnectorProperties"), "OMAGConnectorProperties");
        assertEquals(BeanReferences.getComponentName("OMAGConnectorProperties"), "OMAGConnectorProperties");
    }


    @Test
    public void testSubclassesAreBeans()
    {
        assertTrue(BeanReferences.isBean("VirtualConnection", components()));
        assertTrue(BeanReferences.isBean("PlainConnection", components()));
        assertFalse(BeanReferences.isBean("StatusOnly", components()));
        assertEquals(BeanReferences.getReferencedBeans(refTo("PlainConnection"), components()), List.of("PlainConnection"));
    }


    @Test
    public void testSubclassHoldsOnlyItsOwnFields()
    {
        OpenAPISchema virtualConnection = components().get("VirtualConnection");

        assertEquals(BeanReferences.getOwnFields(virtualConnection).keySet(), java.util.Set.of("embeddedConnections"));
        assertEquals(BeanReferences.getSuperclassName(virtualConnection, components()), "Connection");
        assertTrue(BeanReferences.getOwnFields(components().get("PlainConnection")).isEmpty());
        assertNull(BeanReferences.getSuperclassName(components().get("Connection"), components()));
        assertNull(BeanReferences.getSuperclassName(components().get("StatusOnly"), components()));
    }


    @Test
    public void testChoiceOfBeans()
    {
        OpenAPISchema choice = oneOf(refTo("Connection"), refTo("VirtualConnection"));

        assertEquals(BeanReferences.getReferencedBeans(choice, components()), List.of("Connection", "VirtualConnection"));
        assertEquals(BeanReferences.getReferencedBeans(arrayOf(choice), components()), List.of("Connection", "VirtualConnection"));
        assertEquals(BeanReferences.getReferencedBeans(oneOf(refTo("Connection")), components()), List.of("Connection"));
        assertEquals(BeanReferences.getReferencedBeans(allOf(refTo("Connection")), components()), List.of("Connection"));
        assertNull(BeanReferences.getReferencedBeans(oneOf(refTo("Connection"), typed("string")), components()));
        assertNull(BeanReferences.getReferencedBeans(oneOf(refTo("Connection"), refTo("ElementStatus")), components()));
    }


    @Test
    public void testChoiceNaming()
    {
        List<String> polymorphic = List.of("VirtualConnection", "Connection", "PlainConnection");
        List<String> unrelated   = List.of("OMAGConnectorProperties", "Connection");

        List<String> subclasses  = List.of("VirtualConnection", "PlainConnection");

        assertEquals(BeanReferences.getChoiceRoot(polymorphic, components()), "Connection");
        assertEquals(BeanReferences.getChoiceRoot(subclasses, components()), "Connection");
        assertNull(BeanReferences.getChoiceRoot(unrelated, components()));
        assertTrue(BeanReferences.getChoiceName(polymorphic, components()).startsWith("Connection::"));
        assertTrue(BeanReferences.getChoiceName(subclasses, components()).startsWith("Connection::"));
        assertTrue(BeanReferences.getChoiceName(unrelated, components()).startsWith("OneOf::"));
        assertEquals(BeanReferences.getChoiceName(polymorphic, components()),
                     BeanReferences.getChoiceName(List.of("Connection", "PlainConnection", "VirtualConnection"), components()));
        assertNotEquals(BeanReferences.getChoiceName(polymorphic, components()),
                        BeanReferences.getChoiceName(List.of("Connection", "VirtualConnection"), components()));
    }
}
