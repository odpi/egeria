/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Verify the StarRating enum contains unique ordinals, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class TestStarRating
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
        Integer       newOrdinal = ordinal;

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
    @Test public void testStarRatingValues()
    {
        existingOrdinals = new ArrayList<>();

        StarRating  testValue;

        testValue = StarRating.NO_RECOMMENDATION;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = StarRating.ONE_STAR;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = StarRating.TWO_STARS;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = StarRating.THREE_STARS;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = StarRating.FOUR_STARS;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = StarRating.FIVE_STARS;

        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);
    }



    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(StarRating.FOUR_STARS);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, StarRating.class) == StarRating.FOUR_STARS);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(StarRating.FIVE_STARS.toString().contains("StarRating"));
    }
}
