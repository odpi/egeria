/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test that ConnectedAssetDetails operate correctly
 */
public class TestConnectedAssetDetails
{
    /**
     * Validate the behaviour of related asset properties generated from the default constructor
     */
    @Test public void  testDefaultConstructor()
    {
        ConnectedAssetDetails testObject = new MockConnectedAssetDetails();

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
        ConnectedAssetDetails templateObject = new MockConnectedAssetDetails();
        ConnectedAssetDetails testObject     = new MockConnectedAssetDetails(templateObject);

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


        templateObject = new MockConnectedAssetDetails();

        try
        {
            templateObject.refresh();
        }
        catch (Exception exc)
        {
            assertTrue(false);
        }

        testObject = new MockConnectedAssetDetails(templateObject);

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);

    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(new MockConnectedAssetDetails().toString().contains("ConnectedAssetDetails"));
    }

}
