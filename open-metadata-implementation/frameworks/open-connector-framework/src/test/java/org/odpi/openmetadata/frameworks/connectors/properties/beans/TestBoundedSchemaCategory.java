/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Verify the BoundedSchemaCategory enum contains unique ordinals, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class TestBoundedSchemaCategory
{
    private List<Integer> existingOrdinals = null;

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(int  ordinal)
    {
        Integer       newOrdinal = new Integer(ordinal);

        if (existingOrdinals.contains(newOrdinal))
        {
            return false;
        }
        else
        {
            existingOrdinals.add(newOrdinal);
            return true;
        }
    }


    /**
     * Validated the values of the enum.
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testSchemaTypeValues()
    {
        existingOrdinals = new ArrayList<>();

        BoundedSchemaCategory testValue;

        testValue = BoundedSchemaCategory.UNKNOWN;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = BoundedSchemaCategory.ARRAY;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = BoundedSchemaCategory.SET;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);
    }



    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(BoundedSchemaCategory.SET);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, BoundedSchemaCategory.class) == BoundedSchemaCategory.SET);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testToString()
    {
        assertTrue(BoundedSchemaCategory.ARRAY.toString().contains("BoundedSchemaCategory"));
    }
}
