/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test that ConnectedAssetProperties operate correctly
 */
public class TestConnectedAssetProperties
{
    /**
     * Validate the behaviour of related asset properties generated from the default constructor
     */
    @Test public void  testDefaultConstructor()
    {
        ConnectedAssetProperties testObject = new MockConnectedAssetProperties();

        assertTrue(testObject.getAssetSummary() == null);
        assertTrue(testObject.getAssetDetail() == null);
        assertTrue(testObject.getAssetUniverse() == null);

        try
        {
            testObject.refresh();
        }
        catch (Exception exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);
    }



    /**
     * Validate the behaviour of related asset properties generated from the clone constructor
     */
    @Test public void  testCloneConstructor()
    {
        ConnectedAssetProperties templateObject = new MockConnectedAssetProperties();
        ConnectedAssetProperties testObject = new MockConnectedAssetProperties(templateObject);

        assertTrue(testObject.getAssetSummary() == null);
        assertTrue(testObject.getAssetDetail() == null);
        assertTrue(testObject.getAssetUniverse() == null);

        try
        {
            testObject.refresh();
        }
        catch (Exception exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);


        templateObject = new MockConnectedAssetProperties();

        try
        {
            templateObject.refresh();
        }
        catch (Exception exc)
        {
            assertTrue(false);
        }

        testObject = new MockConnectedAssetProperties(templateObject);

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);

    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(new MockConnectedAssetProperties().toString().contains("ConnectedAssetProperties"));
    }

}
