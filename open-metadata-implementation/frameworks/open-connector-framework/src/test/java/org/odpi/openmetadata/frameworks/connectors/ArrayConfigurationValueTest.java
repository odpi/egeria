/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify that an array configuration property or request parameter is read the same way whether it is stored as
 * a list, an array or a comma-separated string.
 */
public class ArrayConfigurationValueTest
{
    /**
     * A list - which is how a configuration property declared as array&lt;string&gt; is usually stored - gives its
     * values unchanged.  Splitting its toString() on commas gave "[a", " b" and " c]".
     */
    @Test public void testListValue()
    {
        assertEquals(ConnectorBase.getArrayValue(List.of("survey-folder", "survey-all-folders", "survey-folder-and-files")),
                     List.of("survey-folder", "survey-all-folders", "survey-folder-and-files"));
    }


    /**
     * An array gives its values unchanged.
     */
    @Test public void testArrayValue()
    {
        assertEquals(ConnectorBase.getArrayValue(new String[] {"a", "b"}), List.of("a", "b"));
    }


    /**
     * A comma-separated string is split, and each value trimmed.
     */
    @Test public void testCommaSeparatedValue()
    {
        assertEquals(ConnectorBase.getArrayValue("a, b ,c"), List.of("a", "b", "c"));
    }


    /**
     * A list that has been converted to a string gives the same values as the list.
     */
    @Test public void testStringifiedListValue()
    {
        assertEquals(ConnectorBase.getArrayValue(List.of("a", "b", "c").toString()), List.of("a", "b", "c"));
    }


    /**
     * Empty values are dropped, and a missing value gives an empty list.
     */
    @Test public void testEmptyValues()
    {
        assertEquals(ConnectorBase.getArrayValue("a,, ,b"), List.of("a", "b"));
        assertTrue(ConnectorBase.getArrayValue(null).isEmpty());
        assertTrue(ConnectorBase.getArrayValue("").isEmpty());
    }


    /**
     * Values that are not strings are converted to strings.
     */
    @Test public void testNonStringValues()
    {
        assertEquals(ConnectorBase.getArrayValue(List.of(1, 2)), List.of("1", "2"));
    }
}
