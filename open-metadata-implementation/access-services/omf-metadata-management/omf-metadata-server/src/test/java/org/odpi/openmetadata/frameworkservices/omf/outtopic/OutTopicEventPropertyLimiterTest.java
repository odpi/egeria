/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.outtopic;

import org.odpi.openmetadata.frameworks.openmetadata.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;


/**
 * Checks that the properties of an out topic event are kept small enough for the event bus.
 */
public class OutTopicEventPropertyLimiterTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Ordinary properties are sent unchanged - the same object, so nothing is copied for the usual event.
     */
    @Test
    public void testSmallPropertiesUnchanged()
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, "qualifiedName", "Report::1");

        properties = propertyHelper.addStringArrayProperty(properties, "createdElements", getGUIDs(10));

        List<OutTopicEventPropertyLimiter.ShortenedProperty> shortened = new ArrayList<>();

        assertSame(new OutTopicEventPropertyLimiter().limitProperties(properties, shortened), properties);
        assertTrue(shortened.isEmpty());
    }


    /**
     * A connector activity report's list of 2,075 element GUIDs - the event that was too large for Kafka - is
     * cut to the GUIDs that fit, in order, and the other properties are untouched.
     */
    @Test
    public void testLargeArrayIsShortened()
    {
        List<String>      guids      = getGUIDs(2075);
        ElementProperties properties = propertyHelper.addStringProperty(null, "qualifiedName", "Report::1");

        properties = propertyHelper.addStringArrayProperty(properties, "createdElements", guids);

        List<OutTopicEventPropertyLimiter.ShortenedProperty> shortened = new ArrayList<>();
        ElementProperties limited = new OutTopicEventPropertyLimiter().limitProperties(properties, shortened);

        ArrayTypePropertyValue array = (ArrayTypePropertyValue) limited.getPropertyValue("createdElements");

        assertTrue(array.getArrayCount() > 0);
        assertTrue(array.getArrayCount() < 2075);
        assertTrue(OutTopicEventPropertyLimiter.estimateSize(array) <= OutTopicEventPropertyLimiter.DEFAULT_MAXIMUM_PROPERTY_SIZE);
        assertEquals(((PrimitiveTypePropertyValue) array.getArrayValues().getPropertyValue("0")).getPrimitiveValue(), guids.get(0));
        assertEquals(((PrimitiveTypePropertyValue) array.getArrayValues().getPropertyValue(Integer.toString(array.getArrayCount() - 1))).getPrimitiveValue(),
                     guids.get(array.getArrayCount() - 1));

        assertEquals(limited.getPropertyValue("qualifiedName"), properties.getPropertyValue("qualifiedName"));

        assertEquals(shortened.size(), 1);
        assertEquals(shortened.get(0).propertyName(), "createdElements");
        assertEquals(shortened.get(0).valuesKept(), array.getArrayCount());

        assertEquals(((ArrayTypePropertyValue) properties.getPropertyValue("createdElements")).getArrayCount(), 2075,
                     "the original properties must not be changed");
    }


    /**
     * Large maps and strings are shortened too.
     */
    @Test
    public void testLargeMapAndStringAreShortened()
    {
        Map<String, String> map = new HashMap<>();

        for (String guid : getGUIDs(2000))
        {
            map.put(guid, guid);
        }

        ElementProperties properties = propertyHelper.addStringMapProperty(null, "additionalProperties", map);

        properties = propertyHelper.addStringProperty(properties, "description", "x".repeat(200_000));

        List<OutTopicEventPropertyLimiter.ShortenedProperty> shortened = new ArrayList<>();
        ElementProperties limited = new OutTopicEventPropertyLimiter(10_000).limitProperties(properties, shortened);

        MapTypePropertyValue shortenedMap = (MapTypePropertyValue) limited.getPropertyValue("additionalProperties");

        assertTrue(shortenedMap.getMapElementCount() > 0);
        assertTrue(shortenedMap.getMapElementCount() < 2000);
        assertTrue(OutTopicEventPropertyLimiter.estimateSize(shortenedMap) <= 10_000);

        String description = (String) ((PrimitiveTypePropertyValue) limited.getPropertyValue("description")).getPrimitiveValue();

        assertTrue(description.length() < 10_000);
        assertEquals(shortened.size(), 2);
    }


    /**
     * Null properties pass through.
     */
    @Test
    public void testNullProperties()
    {
        assertNull(new OutTopicEventPropertyLimiter().limitProperties(null, new ArrayList<>()));
    }


    private List<String> getGUIDs(int count)
    {
        List<String> guids = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            guids.add(UUID.randomUUID().toString());
        }

        return guids;
    }
}
