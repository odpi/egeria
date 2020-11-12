/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetComment can function as a facade for its bean.
 */
public class TestAssetComment
{
    private ElementType                 type            = new ElementType();
    private List<ElementClassification> classifications = new ArrayList<>();
    private AssetCommentReplies         commentReplies  = null;


    /**
     * Default constructor
     */
    public TestAssetComment()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetComment getTestObject()
    {
        Comment testObject = new Comment();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestUser");
        testObject.setCommentType(CommentType.USAGE_EXPERIENCE);
        testObject.setCommentText("TestText");

        return new AssetComment(testObject, commentReplies);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetComment getDifferentObject()
    {
        Comment testObject = new Comment();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestUser");
        testObject.setCommentType(CommentType.USAGE_EXPERIENCE);
        testObject.setCommentText("TestText");

        return new AssetComment(testObject, commentReplies);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetComment getAnotherDifferentObject()
    {
        Comment testObject = new Comment();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestDifferentUser");
        testObject.setCommentType(CommentType.USAGE_EXPERIENCE);
        testObject.setCommentText("TestText");

        return new AssetComment(testObject, commentReplies);
    }

    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetComment getYetAnotherDifferentObject()
    {
        Comment testObject = new Comment();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestDifferentUser");
        testObject.setCommentType(CommentType.USAGE_EXPERIENCE);
        testObject.setCommentText("TestText");

        return new AssetComment(testObject, new MockAssetCommentReplies(null, 4, 8));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetComment  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getUser().equals("TestUser"));
        assertTrue(resultObject.getCommentType().equals(CommentType.USAGE_EXPERIENCE));
        assertTrue(resultObject.getCommentText().equals("TestText"));

        assertTrue(resultObject.getCommentReplies() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetComment  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getUser() == null);
        assertTrue(nullObject.getCommentText() == null);
        assertTrue(nullObject.getCommentText() == null);
        assertTrue(nullObject.getCommentReplies() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Comment            nullBean;
        AssetComment       nullObject;
        AssetComment       nullTemplate;
        AssetDescriptor    parentAsset;

        nullBean = null;
        nullObject = new AssetComment(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Comment();
        nullObject = new AssetComment(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Comment(null);
        nullObject = new AssetComment(nullBean, null);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetComment(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Comment();
        nullObject = new AssetComment(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Comment(null);
        nullObject = new AssetComment(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetComment(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the comment replies are correctly managed
     */
    @Test public void  testCommentReplies()
    {
        AssetDescriptor     parentAsset         = new AssetSummary(new Asset());

        Comment             testBean = new Comment();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setUser("TestUser");
        testBean.setCommentType(CommentType.USAGE_EXPERIENCE);
        testBean.setCommentText("TestText");


        AssetCommentReplies assetCommentReplies = new MockAssetCommentReplies( parentAsset,
                                                                               15,
                                                                               50);
        AssetComment testTemplate = new AssetComment(testBean, assetCommentReplies);

        assertTrue(testTemplate.getCommentReplies() != null);

        testTemplate = new AssetComment(parentAsset, testBean, assetCommentReplies);

        assertTrue(testTemplate.getCommentReplies() != null);

        AssetComment testObject = new AssetComment(parentAsset, testTemplate);

        assertTrue(testObject.getCommentReplies() != null);
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

        AssetComment  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
        assertFalse(getTestObject().equals(getYetAnotherDifferentObject()));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new AssetComment(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Comment"));
    }
}
