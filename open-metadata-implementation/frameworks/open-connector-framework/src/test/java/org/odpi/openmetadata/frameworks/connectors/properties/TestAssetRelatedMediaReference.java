/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetRelatedMediaReference can function as a facade for its bean.
 */
public class TestAssetRelatedMediaReference
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();

    private List<RelatedMediaUsage>  relatedMediaUsages = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestAssetRelatedMediaReference()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetRelatedMediaReference getTestObject()
    {
        RelatedMediaReference testBean = new RelatedMediaReference();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDisplayName");
        testBean.setOrganization("TestOrg");
        testBean.setVersion("TestVersion");
        testBean.setResourceDescription("TestResourceDescription");
        testBean.setURI("TestURI");
        testBean.setLinkDescription("TestLinkDescription");
        testBean.setReferenceId("TestReferenceId");

        testBean.setMediaType(RelatedMediaType.VIDEO);
        testBean.setMediaUsageList(relatedMediaUsages);

        return new AssetRelatedMediaReference(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetRelatedMediaReference getDifferentObject()
    {
        RelatedMediaReference testBean = new RelatedMediaReference();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDisplayName");
        testBean.setOrganization("TestOrg");
        testBean.setVersion("TestVersion");
        testBean.setResourceDescription("TestResourceDescription");
        testBean.setURI("TestURI");
        testBean.setLinkDescription("TestLinkDescription");
        testBean.setReferenceId("TestReferenceId");

        testBean.setMediaType(RelatedMediaType.VIDEO);
        testBean.setMediaUsageList(relatedMediaUsages);

        return new AssetRelatedMediaReference(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetRelatedMediaReference getAnotherDifferentObject()
    {
        RelatedMediaReference testBean = new RelatedMediaReference();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDisplayName");
        testBean.setOrganization("TestOrg");
        testBean.setVersion("TestVersion");
        testBean.setResourceDescription("TestResourceDescription");
        testBean.setURI("TestURI");
        testBean.setLinkDescription("TestLinkDescription");
        testBean.setReferenceId("TestReferenceId");

        testBean.setMediaType(RelatedMediaType.AUDIO);
        testBean.setMediaUsageList(relatedMediaUsages);

        return new AssetRelatedMediaReference(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetRelatedMediaReference  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getOrganization().equals("TestOrg"));
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getResourceDescription().equals("TestResourceDescription"));
        assertTrue(resultObject.getLinkDescription().equals("TestLinkDescription"));
        assertTrue(resultObject.getURI().equals("TestURI"));
        assertTrue(resultObject.getReferenceId().equals("TestReferenceId"));
        assertTrue(resultObject.getVersion().equals("TestVersion"));

        assertTrue(resultObject.getMediaType().equals(RelatedMediaType.VIDEO));
        assertTrue(resultObject.getMediaUsageList() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetRelatedMediaReference  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getResourceDescription() == null);
        assertTrue(nullObject.getLinkDescription() == null);
        assertTrue(nullObject.getVersion() == null);
        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getOrganization() == null);
        assertTrue(nullObject.getURI() == null);

        assertTrue(nullObject.getMediaUsageList() == null);
        assertTrue(nullObject.getMediaType() == null);

    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        RelatedMediaReference         nullBean;
        AssetRelatedMediaReference    nullObject;
        AssetRelatedMediaReference    nullTemplate;
        AssetDescriptor           parentAsset;

        nullBean = null;
        nullObject = new AssetRelatedMediaReference(nullBean);
        validateNullObject(nullObject);

        nullBean = new RelatedMediaReference();
        nullObject = new AssetRelatedMediaReference(nullBean);
        validateNullObject(nullObject);

        nullBean = new RelatedMediaReference(null);
        nullObject = new AssetRelatedMediaReference(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetRelatedMediaReference(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new RelatedMediaReference();
        nullObject = new AssetRelatedMediaReference(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new RelatedMediaReference(null);
        nullObject = new AssetRelatedMediaReference(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetRelatedMediaReference(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that a populated media usage list can be retrieved
     */
    @Test public void testMediaUsageList()
    {
        List<RelatedMediaUsage>  relatedMediaUsages = new ArrayList<>();

        relatedMediaUsages.add(RelatedMediaUsage.USAGE_GUIDANCE);

        RelatedMediaReference  relatedMediaReferenceBean = new RelatedMediaReference();

        relatedMediaReferenceBean.setMediaUsageList(relatedMediaUsages);

        AssetRelatedMediaReference  testObject = new AssetRelatedMediaReference(relatedMediaReferenceBean);

        List<RelatedMediaUsage>  testList = testObject.getMediaUsageList();

        assertTrue(testList != null);
        assertTrue(testList.get(0) == RelatedMediaUsage.USAGE_GUIDANCE);

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

        AssetRelatedMediaReference  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
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
        validateResultObject(new AssetRelatedMediaReference(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("RelatedMediaReference"));
    }
}
