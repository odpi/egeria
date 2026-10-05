/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Verify that every collection attribute type the open metadata types define - array&lt;string&gt;,
 * array&lt;int&gt;, and map&lt;string, X&gt; for string, boolean, int, long, double, date and object, plus the
 * map&lt;string, array&lt;string&gt;&gt; data type that is stored as map&lt;string,object&gt; - can be written and
 * read back through the repository helper, and is written with the type name the type system knows.  A new
 * attribute of any of these types then works without further code.
 */
public class CollectionPropertiesTest
{
    private static final String SOURCE = "CollectionPropertiesTest";
    private static final String METHOD = "test";

    private final OMRSRepositoryPropertiesUtilities helper = new OMRSRepositoryPropertiesUtilities();


    @Test public void testStringArray()
    {
        InstanceProperties properties = helper.addStringArrayPropertyToInstance(SOURCE, null, "values", List.of("a", "b"), METHOD);

        assertEquals(properties.getPropertyValue("values").getTypeName(), "array<string>");
        assertEquals(helper.getStringArrayProperty(SOURCE, "values", properties, METHOD), List.of("a", "b"));
    }


    /**
     * array&lt;int&gt; is a defined attribute type that had no helper methods at all.
     */
    @Test public void testIntArray()
    {
        List<Integer> values = new ArrayList<>();

        for (int index = 0; index < 12; index++)
        {
            values.add(index * 10);
        }

        InstanceProperties properties = helper.addIntArrayPropertyToInstance(SOURCE, null, "values", values, METHOD);

        assertEquals(properties.getPropertyValue("values").getTypeName(), "array<int>");
        assertEquals(helper.getIntArrayProperty(SOURCE, "values", properties, METHOD), values);
        assertEquals(helper.removeIntArrayProperty(SOURCE, "values", properties, METHOD), values);
        assertNull(properties.getPropertyValue("values"));
    }


    @Test public void testTypedMaps()
    {
        Date now = new Date();

        InstanceProperties properties = helper.addStringMapPropertyToInstance(SOURCE, null, "strings", Map.of("k", "v"), METHOD);
        properties = helper.addBooleanMapPropertyToInstance(SOURCE, properties, "booleans", Map.of("k", true), METHOD);
        properties = helper.addIntMapPropertyToInstance(SOURCE, properties, "ints", Map.of("k", 3), METHOD);
        properties = helper.addLongMapPropertyToInstance(SOURCE, properties, "longs", Map.of("k", 4L), METHOD);
        properties = helper.addDoubleMapPropertyToInstance(SOURCE, properties, "doubles", Map.of("k", 5.5), METHOD);
        properties = helper.addDateMapPropertyToInstance(SOURCE, properties, "dates", Map.of("k", now), METHOD);

        assertEquals(properties.getPropertyValue("strings").getTypeName(), "map<string,string>");
        assertEquals(properties.getPropertyValue("booleans").getTypeName(), "map<string,boolean>");
        assertEquals(properties.getPropertyValue("ints").getTypeName(), "map<string,int>");
        assertEquals(properties.getPropertyValue("longs").getTypeName(), "map<string,long>");
        assertEquals(properties.getPropertyValue("doubles").getTypeName(), "map<string,double>");
        assertEquals(properties.getPropertyValue("dates").getTypeName(), "map<string,date>");

        assertEquals(helper.getStringMapFromProperty(SOURCE, "strings", properties, METHOD), Map.of("k", "v"));
        assertEquals(helper.getBooleanMapFromProperty(SOURCE, "booleans", properties, METHOD), Map.of("k", true));
        assertEquals(helper.getIntegerMapFromProperty(SOURCE, "ints", properties, METHOD), Map.of("k", 3));
        assertEquals(helper.getLongMapFromProperty(SOURCE, "longs", properties, METHOD), Map.of("k", 4L));
        assertEquals(helper.getDoubleMapFromProperty(SOURCE, "doubles", properties, METHOD), Map.of("k", 5.5));
        assertEquals(helper.getDateMapFromProperty(SOURCE, "dates", properties, METHOD), Map.of("k", now));
    }


    @Test public void testStringArrayStringMap()
    {
        Map<String, List<String>> values = Map.of("k", List.of("a", "b"));

        InstanceProperties properties = helper.addStringArrayStringMapPropertyToInstance(SOURCE, null, "values", values, METHOD);

        assertEquals(properties.getPropertyValue("values").getTypeName(), "map<string,object>");
        assertEquals(helper.getStringArrayStringMapFromProperty(SOURCE, "values", properties, METHOD), values);
    }


    /**
     * A map&lt;string,object&gt; holds lists and maps as well as primitives.  They used to be stored as primitives of
     * unknown type wrapping the java object, and read back - when they were arrays or structs - as an index-keyed
     * map or a raw property value.
     */
    @Test public void testObjectMapWithNestedCollections()
    {
        List<String> longList = new ArrayList<>();

        for (int index = 0; index < 12; index++)
        {
            longList.add("value-" + index);
        }

        Map<String, Object> values = new HashMap<>();

        values.put("strings", List.of("survey-folder-and-files", "survey-all-folders"));
        values.put("ints", List.of(1, 2, 3));
        values.put("mixed", List.of("a", 1));
        values.put("longList", longList);
        values.put("nested", Map.of("inner", "value"));
        values.put("count", 2);

        InstanceProperties properties = helper.addMapPropertyToInstance(SOURCE, null, "configurationProperties", values, METHOD);

        MapPropertyValue mapPropertyValue = (MapPropertyValue) properties.getPropertyValue("configurationProperties");

        assertEquals(mapPropertyValue.getTypeName(), "map<string,object>");
        assertTrue(mapPropertyValue.getMapValues().getPropertyValue("strings") instanceof ArrayPropertyValue);
        assertEquals(mapPropertyValue.getMapValues().getPropertyValue("strings").getTypeName(), "array<string>");
        assertEquals(mapPropertyValue.getMapValues().getPropertyValue("ints").getTypeName(), "array<int>");
        assertEquals(mapPropertyValue.getMapValues().getPropertyValue("mixed").getTypeName(), "array<object>");
        assertTrue(mapPropertyValue.getMapValues().getPropertyValue("nested") instanceof MapPropertyValue);

        Map<String, Object> readBack = helper.getMapFromProperty(SOURCE, "configurationProperties", properties, METHOD);

        assertEquals(readBack.get("strings"), List.of("survey-folder-and-files", "survey-all-folders"));
        assertEquals(readBack.get("ints"), List.of(1, 2, 3));
        assertEquals(readBack.get("mixed"), List.of("a", 1));
        assertEquals(readBack.get("longList"), longList);
        assertEquals(readBack.get("nested"), Map.of("inner", "value"));
        assertEquals(readBack.get("count"), 2);
    }
}
