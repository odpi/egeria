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
 * Validate that the AssetNoteLog can function as a facade for its bean.
 */
public class TestAssetNoteLog
{
    private ElementType                 type            = new ElementType();
    private List<ElementClassification> classifications = new ArrayList<>();
    private AssetNotes                  notes           = null;


    /**
     * Default constructor
     */
    public TestAssetNoteLog()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetNoteLog getTestObject()
    {
        NoteLog testObject = new NoteLog();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setDescription("TestDescription");
        testObject.setDisplayName("TestDisplayName");

        return new AssetNoteLog(testObject, notes);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetNoteLog getDifferentObject()
    {
        NoteLog testObject = new NoteLog();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setDescription("TestDescription");
        testObject.setDisplayName("TestDisplayName");

        return new AssetNoteLog(testObject, notes);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetNoteLog getAnotherDifferentObject()
    {
        NoteLog testObject = new NoteLog();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setDescription("TestDifferentDescription");
        testObject.setDisplayName("TestDisplayName");

        return new AssetNoteLog(testObject, notes);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetNoteLog getYetAnotherDifferentObject()
    {
        NoteLog testObject = new NoteLog();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setDescription("TestDescription");
        testObject.setDisplayName("TestDisplayName");

        return new AssetNoteLog(testObject, new MockAssetNotes(null, 4, 10));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetNoteLog  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));

        assertTrue(resultObject.getNotes() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetNoteLog  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getDisplayName() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        NoteLog            nullBean;
        AssetNoteLog       nullObject;
        AssetNoteLog       nullTemplate;
        AssetDescriptor    parentAsset;

        nullBean = null;
        nullObject = new AssetNoteLog(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new NoteLog();
        nullObject = new AssetNoteLog(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new NoteLog(null);
        nullObject = new AssetNoteLog(nullBean, null);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetNoteLog(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new NoteLog();
        nullObject = new AssetNoteLog(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new NoteLog(null);
        nullObject = new AssetNoteLog(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetNoteLog(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the comment replies are correctly managed
     */
    @Test public void  testNotes()
    {
        AssetDescriptor     parentAsset         = new AssetSummary(new Asset());

        NoteLog             testBean = new NoteLog();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setDisplayName("TestName");
        testBean.setDescription("TestDescription");


        AssetNotes assetNotes = new MockAssetNotes( parentAsset,
                                                    15,
                                                    50);
        AssetNoteLog testTemplate = new AssetNoteLog(testBean, assetNotes);

        assertTrue(testTemplate.getNotes() != null);

        testTemplate = new AssetNoteLog(parentAsset, testBean, assetNotes);

        assertTrue(testTemplate.getNotes() != null);

        AssetNoteLog testObject = new AssetNoteLog(parentAsset, testTemplate);

        assertTrue(testObject.getNotes() != null);
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

        AssetNoteLog  sameObject = getTestObject();
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
        validateResultObject(new AssetNoteLog(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("NoteLog"));
    }
}
