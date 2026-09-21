/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * PropertyHelperTypeAndTemplateTest covers two more pieces of {@link PropertyHelper} that everything else
 * is built on: deciding whether an element is of a given type, and resolving the placeholders in a catalog
 * template.
 * <br><br>
 * Both fail quietly when they are wrong.  A type test that says no where it should say yes makes a
 * connector skip an element it was supposed to process, and a placeholder that is not substituted is
 * carried into the catalogue as part of a qualified name, where it looks like a naming mistake rather than
 * an unresolved template.
 */
public class PropertyHelperTypeAndTemplateTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Build a header for an element of the supplied type with the supplied supertypes.
     *
     * @param typeName the element's own type
     * @param superTypeNames its supertypes, nearest first
     * @return header
     */
    private ElementControlHeader getHeader(String       typeName,
                                           List<String> superTypeNames)
    {
        ElementType          elementType = new ElementType();
        ElementControlHeader header      = new ElementControlHeader();

        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(superTypeNames);
        header.setType(elementType);

        return header;
    }


    /**
     * An element is of its own type and of every type it inherits from.  This is what lets a connector ask
     * for "an Asset" and be given a CSVFile.
     */
    @Test
    public void anElementIsOfItsOwnTypeAndOfItsSupertypes()
    {
        ElementControlHeader header = getHeader("CSVFile", List.of("DataFile", "DataStore", "Asset", "Referenceable", "OpenMetadataRoot"));

        assertTrue(propertyHelper.isTypeOf(header, "CSVFile"), "an element is of its own type");
        assertTrue(propertyHelper.isTypeOf(header, "DataFile"));
        assertTrue(propertyHelper.isTypeOf(header, "Asset"));
        assertTrue(propertyHelper.isTypeOf(header, "OpenMetadataRoot"), "including the type at the top of the model");

        assertFalse(propertyHelper.isTypeOf(header, "Database"), "an element is not of a sibling type");
        assertFalse(propertyHelper.isTypeOf(header, "CSVFILE"), "the test is case sensitive");
    }


    /**
     * An element with no supertypes recorded is still of its own type.  A header built by hand - by a
     * connector, or by a test - often has the type name and nothing else.
     */
    @Test
    public void anElementWithNoSupertypesIsStillOfItsOwnType()
    {
        ElementControlHeader header = getHeader("Asset", null);

        assertTrue(propertyHelper.isTypeOf(header, "Asset"));
        assertFalse(propertyHelper.isTypeOf(header, "Referenceable"),
                    "without its supertypes an element cannot be shown to be one of them");
    }


    /**
     * No expected type means any type will do, and is the way a caller says "do not filter".  Answering
     * false here would make every unfiltered request return nothing.
     */
    @Test
    public void noExpectedTypeMatchesAnything()
    {
        ElementControlHeader header = getHeader("Asset", null);

        assertTrue(propertyHelper.isTypeOf(header, (String) null));
        assertTrue(propertyHelper.isTypeOf(header, (List<String>) null));
        assertTrue(propertyHelper.isTypeOf(null, (String) null),
                   "no expected type matches anything, including nothing");
    }


    /**
     * An element with no type cannot be shown to be of a type, and neither can no element.  Both come back
     * false rather than throwing, because these are reached while walking a graph that may hold a proxy
     * with no type filled in.
     */
    @Test
    public void anElementWithNoTypeIsNotOfAnyNamedType()
    {
        assertFalse(propertyHelper.isTypeOf(null, "Asset"));
        assertFalse(propertyHelper.isTypeOf(new ElementControlHeader(), "Asset"));
    }


    /**
     * The list form matches when any one of the types matches, and an empty list matches nothing - unlike a
     * null list, which matches everything.  The two are easy to conflate and mean opposite things.
     */
    @Test
    public void theListFormMatchesAnyOfTheTypesAndAnEmptyListMatchesNothing()
    {
        ElementControlHeader header = getHeader("CSVFile", List.of("DataFile", "Asset"));

        assertTrue(propertyHelper.isTypeOf(header, List.of("Database", "DataFile")));
        assertFalse(propertyHelper.isTypeOf(header, List.of("Database", "Topic")));
        assertFalse(propertyHelper.isTypeOf(header, new ArrayList<>()),
                    "an empty list of expected types matches nothing, where a null list matches everything");
    }


    /**
     * A property that is nothing but a placeholder becomes the value, keeping its type - this is how a
     * template's qualified name is replaced wholesale.
     */
    @Test
    public void aPropertyThatIsOnlyAPlaceholderBecomesTheValue()
    {
        Map<String, String> placeholders = Map.of("serverName", "coco-mds1");

        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("~{serverName}~", placeholders),
                     "coco-mds1");
    }


    /**
     * A placeholder inside a longer string is replaced where it stands, at the start, in the middle and at
     * the end.  The end case goes down its own path in the implementation, because splitting a string on a
     * separator it ends with drops the trailing empty piece.
     */
    @Test
    public void aPlaceholderIsReplacedWhereverItAppears()
    {
        Map<String, String> placeholders = Map.of("serverName", "coco-mds1");

        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("Egeria:Server:~{serverName}~", placeholders),
                     "Egeria:Server:coco-mds1",
                     "a placeholder at the end of a string should be replaced");
        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("~{serverName}~:metadata-store", placeholders),
                     "coco-mds1:metadata-store");
        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("Egeria:~{serverName}~:metadata-store", placeholders),
                     "Egeria:coco-mds1:metadata-store");
    }


    /**
     * Every occurrence of a placeholder is replaced, not just the first.
     */
    @Test
    public void everyOccurrenceOfAPlaceholderIsReplaced()
    {
        Map<String, String> placeholders = Map.of("serverName", "coco-mds1");

        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("~{serverName}~ and ~{serverName}~", placeholders),
                     "coco-mds1 and coco-mds1");
    }


    /**
     * Several placeholders in one string are each replaced with their own value.
     */
    @Test
    public void severalPlaceholdersAreEachReplaced()
    {
        Map<String, String> placeholders = new LinkedHashMap<>();

        placeholders.put("hostName", "localhost");
        placeholders.put("portNumber", "9443");

        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("https://~{hostName}~:~{portNumber}~", placeholders),
                     "https://localhost:9443");
    }


    /**
     * A placeholder nobody supplied a value for is left as it stands rather than being emptied.  That is
     * what makes an unresolved template visible in the catalogue instead of producing an element with a
     * gap in its name.
     */
    @Test
    public void anUnsuppliedPlaceholderIsLeftAlone()
    {
        Map<String, String> placeholders = Map.of("serverName", "coco-mds1");

        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("Egeria:~{missingName}~", placeholders),
                     "Egeria:~{missingName}~");
    }


    /**
     * A string with no placeholders in it, a null string, and a string with nowhere to get values from all
     * come back exactly as they went in.  Every property of every template goes through this method, so
     * most calls are this case.
     */
    @Test
    public void aStringWithNothingToReplaceComesBackUnchanged()
    {
        Map<String, String> placeholders = Map.of("serverName", "coco-mds1");

        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("Egeria:Server:coco-mds1", placeholders),
                     "Egeria:Server:coco-mds1");
        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("~{serverName}~", null),
                     "~{serverName}~",
                     "with no values supplied the placeholder has to stay");
        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders("~{serverName}~", new LinkedHashMap<>()),
                     "~{serverName}~");
        assertEquals(propertyHelper.replacePrimitiveStringWithPlaceholders(null, placeholders), null);
    }
}
