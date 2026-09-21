/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * PropertyHelperPropertiesTest covers the part of {@link PropertyHelper} that every converter and handler
 * in the platform goes through: packing a java value into {@link ElementProperties} and reading it back out
 * again.
 * <br><br>
 * The behaviour worth pinning down here is not the happy path - it is what these methods do when something
 * does not line up.  A getter whose property is of a different category does not complain: it returns the
 * empty value for the type it was asked for.  That is a deliberate choice - a repository can hold an older
 * version of a type - but it means a converter that reads a property under the wrong name, or with the
 * wrong getter, produces an element with a missing value rather than an error.  The tests below state that
 * behaviour explicitly so it cannot drift into an exception (breaking callers who rely on the empty value)
 * or into a silently different default.
 */
public class PropertyHelperPropertiesTest
{
    private static final String SOURCE_NAME = "PropertyHelperPropertiesTest";
    private static final String METHOD_NAME = "testMethod";

    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * A null properties object is created on demand, and an existing one is added to in place rather than
     * copied.  Callers chain these calls and rely on both halves of that.
     */
    @Test
    public void addToNullPropertiesCreatesThemAndAddToExistingPropertiesReturnsTheSameObject()
    {
        ElementProperties created = propertyHelper.addStringProperty(null, "qualifiedName", "widget-1");

        assertNotNull(created, "adding a property to null properties should create a properties object");
        assertEquals(created.getPropertyCount(), 1);

        ElementProperties added = propertyHelper.addStringProperty(created, "displayName", "Widget");

        assertSame(added, created, "adding to an existing properties object should not copy it");
        assertEquals(created.getPropertyCount(), 2);
    }


    /**
     * A null value adds nothing.  This is what lets a converter call the add methods for every property of
     * a bean without testing each one first, and it is why adding to null properties can return null.
     */
    @Test
    public void aNullValueAddsNothing()
    {
        assertNull(propertyHelper.addStringProperty(null, "qualifiedName", null),
                   "adding a null value to null properties should leave it null");
        assertNull(propertyHelper.addDateProperty(null, "createTime", null));
        assertNull(propertyHelper.addStringArrayProperty(null, "zoneMembership", null));
        assertNull(propertyHelper.addStringMapProperty(null, "additionalProperties", null));

        ElementProperties properties = propertyHelper.addStringProperty(null, "qualifiedName", "widget-1");

        propertyHelper.addStringProperty(properties, "displayName", null);
        propertyHelper.addDateProperty(properties, "createTime", null);

        assertEquals(properties.getPropertyCount(), 1, "a null value should not add a property");
    }


    /**
     * An empty collection adds nothing either - an empty list is not a value, and storing it would make an
     * element that was never given a value indistinguishable from one that was given an empty one.
     */
    @Test
    public void anEmptyCollectionAddsNothing()
    {
        assertNull(propertyHelper.addStringArrayProperty(null, "zoneMembership", new ArrayList<>()));
        assertNull(propertyHelper.addStringMapProperty(null, "additionalProperties", new LinkedHashMap<>()));
    }


    /**
     * Every primitive survives the round trip with the value it went in with.
     */
    @Test
    public void primitivePropertiesRoundTrip()
    {
        Date              createTime = new Date();
        ElementProperties properties = new ElementProperties();

        propertyHelper.addStringProperty(properties, "qualifiedName", "widget-1");
        propertyHelper.addIntProperty(properties, "domainIdentifier", 42);
        propertyHelper.addLongProperty(properties, "rowCount", 9_000_000_000L);
        propertyHelper.addFloatProperty(properties, "confidence", 0.75F);
        propertyHelper.addBooleanProperty(properties, "isDeprecated", true);
        propertyHelper.addDateProperty(properties, "createTime", createTime);

        assertEquals(propertyHelper.getStringProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME), "widget-1");
        assertEquals(propertyHelper.getIntProperty(SOURCE_NAME, "domainIdentifier", properties, METHOD_NAME), 42);
        assertEquals(propertyHelper.getLongProperty(SOURCE_NAME, "rowCount", properties, METHOD_NAME), 9_000_000_000L);
        assertEquals(propertyHelper.getFloatProperty(SOURCE_NAME, "confidence", properties, METHOD_NAME), 0.75F);
        assertTrue(propertyHelper.getBooleanProperty(SOURCE_NAME, "isDeprecated", properties, METHOD_NAME));
        assertEquals(propertyHelper.getDateProperty(SOURCE_NAME, "createTime", properties, METHOD_NAME), createTime);
    }


    /**
     * A date is stored as a timestamp rather than as a Date, so it has to come back as the same instant -
     * to the millisecond, not to the second.
     */
    @Test
    public void aDateKeepsItsMilliseconds()
    {
        Date              awkwardTime = new Date(1_758_412_345_678L);
        ElementProperties properties  = propertyHelper.addDateProperty(null, "createTime", awkwardTime);

        assertEquals(propertyHelper.getDateProperty(SOURCE_NAME, "createTime", properties, METHOD_NAME).getTime(),
                     1_758_412_345_678L);
    }


    /**
     * An enum is stored by its symbolic name, not by an ordinal, so renumbering an enum cannot change what a
     * stored value means.
     */
    @Test
    public void anEnumRoundTripsBySymbolicName()
    {
        ElementProperties properties = propertyHelper.addEnumProperty(null,
                                                                      "activityStatus",
                                                                      "ActivityStatus",
                                                                      "IN_PROGRESS");

        assertEquals(propertyHelper.getEnumPropertySymbolicName(SOURCE_NAME, "activityStatus", properties, METHOD_NAME),
                     "IN_PROGRESS");
    }


    /**
     * An array keeps the order it was given.  The values are stored in a map keyed by index, and a map has no
     * order, so the order only survives because the getter reassembles the array using those indices.
     */
    @Test
    public void aStringArrayKeepsItsOrder()
    {
        List<String> zones = List.of("quarantine", "data-lake", "sandbox", "clinical-trials", "personal-files");

        ElementProperties properties = propertyHelper.addStringArrayProperty(null, "zoneMembership", zones);

        assertEquals(propertyHelper.getStringArrayProperty(SOURCE_NAME, "zoneMembership", properties, METHOD_NAME),
                     zones,
                     "a string array should come back in the order it went in");
    }


    /**
     * A string map round trips with all of its entries.
     */
    @Test
    public void aStringMapRoundTrips()
    {
        Map<String, String> additionalProperties = new LinkedHashMap<>();

        additionalProperties.put("department", "cardiology");
        additionalProperties.put("costCentre", "4711");

        ElementProperties properties = propertyHelper.addStringMapProperty(null,
                                                                           "additionalProperties",
                                                                           additionalProperties);

        assertEquals(propertyHelper.getStringMapFromProperty(SOURCE_NAME, "additionalProperties", properties, METHOD_NAME),
                     additionalProperties);
    }


    /**
     * Reading a property that is there, but of a different category, gives the empty value for the type
     * asked for rather than an exception.  A converter that reaches for the wrong getter therefore produces
     * an element with a missing value and no complaint, which is why this is worth stating.
     */
    @Test
    public void readingAPropertyAsTheWrongTypeGivesTheEmptyValue()
    {
        ElementProperties properties = new ElementProperties();

        propertyHelper.addIntProperty(properties, "domainIdentifier", 42);
        propertyHelper.addStringProperty(properties, "qualifiedName", "widget-1");

        assertNull(propertyHelper.getStringProperty(SOURCE_NAME, "domainIdentifier", properties, METHOD_NAME),
                   "an int read as a string should be null, not the number as text");
        assertEquals(propertyHelper.getIntProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME), 0);
        assertEquals(propertyHelper.getLongProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME), 0L);
        assertEquals(propertyHelper.getFloatProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME), 0F);
        assertEquals(propertyHelper.getBooleanProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME), false);
        assertNull(propertyHelper.getDateProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME));
        assertNull(propertyHelper.getStringArrayProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME));
        assertNull(propertyHelper.getStringMapFromProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME));
        assertNull(propertyHelper.getEnumPropertySymbolicName(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME));
    }


    /**
     * The same empty values come back for a property that is not there at all, and for null properties.  A
     * converter is handed null properties whenever an element has none.
     */
    @Test
    public void readingAMissingPropertyGivesTheEmptyValue()
    {
        ElementProperties empty = new ElementProperties();

        assertNull(propertyHelper.getStringProperty(SOURCE_NAME, "qualifiedName", empty, METHOD_NAME));
        assertEquals(propertyHelper.getIntProperty(SOURCE_NAME, "domainIdentifier", empty, METHOD_NAME), 0);
        assertEquals(propertyHelper.getBooleanProperty(SOURCE_NAME, "isDeprecated", empty, METHOD_NAME), false);

        assertNull(propertyHelper.getStringProperty(SOURCE_NAME, "qualifiedName", null, METHOD_NAME));
        assertEquals(propertyHelper.getIntProperty(SOURCE_NAME, "domainIdentifier", null, METHOD_NAME), 0);
        assertEquals(propertyHelper.getBooleanProperty(SOURCE_NAME, "isDeprecated", null, METHOD_NAME), false);
        assertNull(propertyHelper.getDateProperty(SOURCE_NAME, "createTime", null, METHOD_NAME));
        assertNull(propertyHelper.getStringArrayProperty(SOURCE_NAME, "zoneMembership", null, METHOD_NAME));
    }


    /**
     * A remove returns the value and takes the property out, leaving the rest alone.  Converters use this to
     * take the properties they understand and then treat whatever is left as the extended properties of a
     * subtype they do not, so a remove that returned the value without removing it would duplicate every
     * property into the extended set.
     */
    @Test
    public void aRemoveReturnsTheValueAndTakesThePropertyOut()
    {
        ElementProperties properties = new ElementProperties();

        propertyHelper.addStringProperty(properties, "qualifiedName", "widget-1");
        propertyHelper.addStringProperty(properties, "displayName", "Widget");
        propertyHelper.addIntProperty(properties, "domainIdentifier", 42);

        assertEquals(propertyHelper.removeStringProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME),
                     "widget-1");
        assertEquals(properties.getPropertyCount(), 2, "the removed property should be gone");
        assertNull(propertyHelper.getStringProperty(SOURCE_NAME, "qualifiedName", properties, METHOD_NAME),
                   "the removed property should not be readable");
        assertEquals(propertyHelper.getStringProperty(SOURCE_NAME, "displayName", properties, METHOD_NAME),
                     "Widget",
                     "removing one property should leave the others alone");

        assertNull(propertyHelper.removeStringProperty(SOURCE_NAME, "notThere", properties, METHOD_NAME));
        assertEquals(properties.getPropertyCount(), 2, "removing a property that is not there should change nothing");

        assertNull(propertyHelper.removeStringProperty(SOURCE_NAME, "qualifiedName", null, METHOD_NAME),
                   "removing from null properties should be null rather than a failure");
    }


    /**
     * A remove of the wrong type leaves the property in place.  The remove only happens when the get
     * succeeds, so a converter reaching for the wrong getter leaves the property behind and it ends up in
     * the extended properties - the visible symptom of the silent behaviour above.
     */
    @Test
    public void aRemoveOfTheWrongTypeLeavesThePropertyInPlace()
    {
        ElementProperties properties = propertyHelper.addIntProperty(null, "domainIdentifier", 42);

        assertNull(propertyHelper.removeStringProperty(SOURCE_NAME, "domainIdentifier", properties, METHOD_NAME));
        assertEquals(properties.getPropertyCount(), 1);
        assertEquals(propertyHelper.getIntProperty(SOURCE_NAME, "domainIdentifier", properties, METHOD_NAME), 42);
    }


    /**
     * The map view unwraps primitives to their java values and hands anything else back as the property
     * value it is.  Callers that display or index properties generically rely on the first half; callers
     * that then look for a map or an array rely on the second.
     */
    @Test
    public void thePropertiesMapUnwrapsPrimitivesAndPassesTheRestThrough()
    {
        ElementProperties properties = new ElementProperties();

        propertyHelper.addStringProperty(properties, "qualifiedName", "widget-1");
        propertyHelper.addIntProperty(properties, "domainIdentifier", 42);
        propertyHelper.addStringArrayProperty(properties, "zoneMembership", List.of("sandbox"));

        Map<String, Object> asMap = propertyHelper.getElementPropertiesAsMap(properties);

        assertEquals(asMap.size(), 3);
        assertEquals(asMap.get("qualifiedName"), "widget-1");
        assertEquals(asMap.get("domainIdentifier"), 42);
        assertTrue(asMap.get("zoneMembership") instanceof ArrayTypePropertyValue,
                   "a non-primitive should come through as its property value, not as a java collection");

        assertNull(propertyHelper.getElementPropertiesAsMap(null));
    }
}
