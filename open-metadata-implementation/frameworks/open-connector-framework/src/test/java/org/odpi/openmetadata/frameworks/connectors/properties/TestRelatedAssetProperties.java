/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test that ConnectedAssetProperties operate correctly
 */
public class TestRelatedAssetProperties
{

    /**
     * Validate the behaviour of related asset properties generated from the default constructor
     */
    @Test public void  testDefaultConstructor()
    {
        RelatedAssetProperties testObject = new MockRelatedAssetProperties();

        assertTrue(testObject.getAssetSummary() == null);
        assertTrue(testObject.getAssetDetail() == null);
        assertTrue(testObject.getAssetUniverse() == null);

        try
        {
            testObject.refresh();
        }
        catch (Throwable exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);
    }


    /**
     * Validate the behaviour of related asset properties generated from the typical constructor
     */
    @Test public void  testNormalConstructor()
    {
        AssetSummary           connectedAsset = new AssetSummary(new Asset());
        RelatedAsset           relatedAsset   = new RelatedAsset(new Asset(), null);
        RelatedAssetProperties testObject     = new MockRelatedAssetProperties(connectedAsset, relatedAsset);

        assertTrue(testObject.getAssetSummary() == null);
        assertTrue(testObject.getAssetDetail() == null);
        assertTrue(testObject.getAssetUniverse() == null);

        try
        {
            testObject.refresh();
        }
        catch (Throwable exc)
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
        RelatedAssetProperties templateObject = new MockRelatedAssetProperties();
        RelatedAssetProperties testObject = new MockRelatedAssetProperties(templateObject);

        assertTrue(testObject.getAssetSummary() == null);
        assertTrue(testObject.getAssetDetail() == null);
        assertTrue(testObject.getAssetUniverse() == null);

        try
        {
            testObject.refresh();
        }
        catch (Throwable exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);


        templateObject = new MockRelatedAssetProperties();

        try
        {
            templateObject.refresh();
        }
        catch (Throwable exc)
        {
            assertTrue(false);
        }

        testObject = new MockRelatedAssetProperties(templateObject);

        assertTrue(testObject.getAssetSummary() != null);
        assertTrue(testObject.getAssetDetail() != null);
        assertTrue(testObject.getAssetUniverse() != null);

    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(new MockRelatedAssetProperties().toString().contains("RelatedAssetProperties"));
    }

}
