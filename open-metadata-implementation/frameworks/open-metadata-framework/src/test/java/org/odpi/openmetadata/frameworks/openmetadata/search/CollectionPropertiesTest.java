/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

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
 * read back through the property helper, and is written with a type name the type system knows.  A new attribute
 * of any of these types then works without further code.
 */
public class CollectionPropertiesTest
{
    private static final String SOURCE = "CollectionPropertiesTest";
    private static final String METHOD = "test";

    private final PropertyHelper helper = new PropertyHelper();


    @Test public void testStringArray()
    {
        ElementProperties properties = helper.addStringArrayProperty(null, "values", List.of("a", "b"));

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

        ElementProperties properties = helper.addIntArrayProperty(null, "values", values);

        assertEquals(properties.getPropertyValue("values").getTypeName(), "array<int>");
        assertEquals(helper.getIntArrayProperty(SOURCE, "values", properties, METHOD), values);
        assertEquals(helper.removeIntArrayProperty(SOURCE, "values", properties, METHOD), values);
        assertNull(properties.getPropertyValue("values"));
    }


    @Test public void testTypedMaps()
    {
        Date now = new Date();

        ElementProperties properties = helper.addStringMapProperty(null, "strings", Map.of("k", "v"));
        properties = helper.addBooleanMapProperty(properties, "booleans", Map.of("k", true));
        properties = helper.addIntMapProperty(properties, "ints", Map.of("k", 3));
        properties = helper.addLongMapProperty(properties, "longs", Map.of("k", 4L));
        properties = helper.addDoubleMapProperty(properties, "doubles", Map.of("k", 5.5));
        properties = helper.addDateMapProperty(properties, "dates", Map.of("k", now));

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


    /**
     * There is no map&lt;string, array&lt;string&gt;&gt; attribute type: the type archive declares a property of that
     * data type as map&lt;string,object&gt;.  The value used to be written as "map&lt;string,array&lt;string&gt;" -
     * missing its closing bracket, and naming a type the server could not find.
     */
    @Test public void testStringArrayStringMap()
    {
        Map<String, List<String>> values = Map.of("k", List.of("a", "b"));

        ElementProperties properties = helper.addStringArrayStringMap(null, "values", values);

        assertEquals(properties.getPropertyValue("values").getTypeName(), "map<string,object>");
        assertEquals(helper.getListStringMapFromProperty(SOURCE, "values", properties, METHOD), values);
    }


    /**
     * A map&lt;string,object&gt; holds lists, maps and enums as well as primitives, and gives them back as java
     * values with their element types named correctly.
     */
    @Test public void testObjectMapWithNestedCollections()
    {
        Map<String, Object> values = new HashMap<>();

        values.put("strings", List.of("a", "b"));
        values.put("ints", List.of(1, 2, 3));
        values.put("mixed", List.of("a", 1));
        values.put("nestedStrings", Map.of("inner", "value"));
        values.put("nestedMixed", Map.of("inner", "value", "count", 1));
        values.put("status", ElementStatus.ACTIVE);

        ElementProperties properties = helper.addMapProperty(null, "configurationProperties", values);

        MapTypePropertyValue map = (MapTypePropertyValue) properties.getPropertyValue("configurationProperties");

        assertEquals(map.getTypeName(), "map<string,object>");
        assertEquals(map.getMapValues().getPropertyValue("strings").getTypeName(), "array<string>");
        assertEquals(map.getMapValues().getPropertyValue("ints").getTypeName(), "array<int>");
        assertEquals(map.getMapValues().getPropertyValue("mixed").getTypeName(), "array<object>",
                     "a mixed list was named after the type of its last element");
        assertEquals(map.getMapValues().getPropertyValue("nestedStrings").getTypeName(), "map<string,string>",
                     "a map of strings was named map<string,object>");
        assertEquals(map.getMapValues().getPropertyValue("nestedMixed").getTypeName(), "map<string,object>");

        Map<String, Object> readBack = helper.getMapFromProperty(SOURCE, "configurationProperties", properties, METHOD);

        assertEquals(readBack.get("strings"), List.of("a", "b"));
        assertEquals(readBack.get("ints"), List.of(1, 2, 3));
        assertEquals(readBack.get("mixed"), List.of("a", 1));
        assertEquals(readBack.get("nestedStrings"), Map.of("inner", "value"));
        assertEquals(readBack.get("status"), ElementStatus.ACTIVE.name());
    }


    /**
     * A map read through the map view can be written back unchanged - the shapes getElementPropertiesAsMap gives
     * are the shapes addPropertyMap accepts.
     */
    @Test public void testMapReadBackCanBeWrittenAgain()
    {
        Map<String, Object> values = Map.of("list", List.of("x", "y"), "map", Map.of("k", 1));

        ElementProperties first  = helper.addMapProperty(null, "props", values);
        Map<String, Object> once = helper.getMapFromProperty(SOURCE, "props", first, METHOD);

        ElementProperties second  = helper.addMapProperty(null, "props", once);
        Map<String, Object> twice = helper.getMapFromProperty(SOURCE, "props", second, METHOD);

        assertEquals(twice, once);
        assertTrue(twice.get("list") instanceof List);
    }
}
