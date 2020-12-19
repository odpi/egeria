/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestAssetFeedback
{
    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetFeedback getTestObject()
    {
        Asset assetBean = new Asset();
        assetBean.setQualifiedName("TestName");

        AssetSummary parentAsset = new AssetSummary(assetBean);

        return new AssetFeedback(parentAsset,
                                 new MockAssetInformalTags(parentAsset, 1, 5),
                                 null,
                                 null,
                                 null);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetFeedback getDifferentObject()
    {
        Asset assetBean = new Asset();
        assetBean.setQualifiedName("TestName");

        AssetSummary parentAsset = new AssetSummary(assetBean);

        return new AssetFeedback(null,
                                 new MockAssetInformalTags(parentAsset, 1, 5),
                                 null,
                                 null,
                                 null);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetFeedback getAnotherDifferentObject()
    {
        Asset assetBean = new Asset();
        assetBean.setQualifiedName("TestName");

        AssetSummary parentAsset = new AssetSummary(assetBean);

        return new AssetFeedback(parentAsset,
                                 new MockAssetInformalTags(parentAsset, 1, 5),
                                 new MockAssetLikes(parentAsset, 5 , 100),
                                 null,
                                 null);
    }


    @Test public void testAssetFeedback()
   {
       AssetSummary parentAsset = new AssetSummary(new Asset());

       AssetFeedback assetFeedback = new AssetFeedback(parentAsset,
                                                       null,
                                                       null,
                                                       null,
                                                       null);

       assertTrue(assetFeedback.getInformalTags() == null);
       assertTrue(assetFeedback.getLikes() == null);
       assertTrue(assetFeedback.getRatings() == null);
       assertTrue(assetFeedback.getComments() == null);

       assetFeedback = new AssetFeedback(parentAsset,
                                         new MockAssetInformalTags(parentAsset, 0, 0),
                                         new MockAssetLikes(parentAsset, 0, 0),
                                         new MockAssetRatings(parentAsset, 0, 0),
                                         new MockAssetComments(parentAsset, 0, 0));

       assertTrue(assetFeedback.getInformalTags() != null);
       assertTrue(assetFeedback.getLikes() != null);
       assertTrue(assetFeedback.getRatings() != null);
       assertTrue(assetFeedback.getComments() != null);

       AssetFeedback assetFeedbackClone = new AssetFeedback(parentAsset, assetFeedback);

       assertTrue(assetFeedbackClone.getInformalTags() != null);
       assertTrue(assetFeedbackClone.getLikes() != null);
       assertTrue(assetFeedbackClone.getRatings() != null);
       assertTrue(assetFeedbackClone.getComments() != null);

   }

    /**
     * Validate the subclass constructor works all of the way up the inheritance hierarchy.
     */
    @Test public void testSubclassInitialization()
    {
        new AssetFeedback(null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        AssetFeedback sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("AssetFeedback"));
    }

}
