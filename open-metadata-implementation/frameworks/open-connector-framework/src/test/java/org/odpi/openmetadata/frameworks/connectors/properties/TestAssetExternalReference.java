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
 * Validate that the AssetExternalReference can function as a facade for its bean.
 */
public class TestAssetExternalReference
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();

    private Referenceable        scope                = new ExternalReference();


    /**
     * Default constructor
     */
    public TestAssetExternalReference()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetExternalReference getTestObject()
    {
        ExternalReference testBean = new ExternalReference();

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

        return new AssetExternalReference(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetExternalReference getDifferentObject()
    {
        ExternalReference testBean = new ExternalReference();

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

        return new AssetExternalReference(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetExternalReference getAnotherDifferentObject()
    {
        ExternalReference testBean = new ExternalReference();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDisplayName");
        testBean.setOrganization("TestDifferentOrg");
        testBean.setVersion("TestVersion");
        testBean.setResourceDescription("TestResourceDescription");
        testBean.setURI("TestURI");
        testBean.setLinkDescription("TestLinkDescription");
        testBean.setReferenceId("TestReferenceId");

        return new AssetExternalReference(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetExternalReference  resultObject)
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
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetExternalReference  nullObject)
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
        assertTrue(nullObject.getReferenceId() == null);
        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getOrganization() == null);
        assertTrue(nullObject.getURI() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ExternalReference         nullBean;
        AssetExternalReference    nullObject;
        AssetExternalReference    nullTemplate;
        AssetDescriptor           parentAsset;

        nullBean = null;
        nullObject = new AssetExternalReference(nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalReference();
        nullObject = new AssetExternalReference(nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalReference(null);
        nullObject = new AssetExternalReference(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetExternalReference(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalReference();
        nullObject = new AssetExternalReference(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalReference(null);
        nullObject = new AssetExternalReference(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetExternalReference(parentAsset, nullTemplate);
        validateNullObject(nullObject);
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

        AssetExternalReference  sameObject = getTestObject();
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
        validateResultObject(new AssetExternalReference(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ExternalReference"));
    }
}
