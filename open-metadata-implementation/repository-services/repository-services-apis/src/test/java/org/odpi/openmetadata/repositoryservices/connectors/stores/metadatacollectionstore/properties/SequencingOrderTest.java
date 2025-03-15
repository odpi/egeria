/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Verify the SequencingOrder enum contains unique ordinals, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class SequencingOrderTest
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
    @Test public void testEnumValues()
    {
        existingOrdinals = new ArrayList<>();

        SequencingOrder  testValue;

        testValue = SequencingOrder.ANY;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.GUID;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.CREATION_DATE_RECENT;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.CREATION_DATE_OLDEST;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.LAST_UPDATE_RECENT;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.LAST_UPDATE_OLDEST;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.PROPERTY_ASCENDING;
        assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
        assertTrue(testValue.getName() != null);
        assertTrue(testValue.getDescription() != null);

        testValue = SequencingOrder.PROPERTY_DESCENDING;
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
            jsonString = objectMapper.writeValueAsString(SequencingOrder.CREATION_DATE_OLDEST);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, SequencingOrder.class) == SequencingOrder.CREATION_DATE_OLDEST);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(SequencingOrder.LAST_UPDATE_OLDEST.toString().contains("SequencingOrder"));
    }
}
