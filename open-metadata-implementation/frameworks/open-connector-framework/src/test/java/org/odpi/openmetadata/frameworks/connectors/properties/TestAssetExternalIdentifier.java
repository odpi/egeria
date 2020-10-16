/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetExternalIdentifier can function as a facade for its bean.
 */
public class TestAssetExternalIdentifier
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();

    private Referenceable        scope                = new ExternalReference();


    /**
     * Default constructor
     */
    public TestAssetExternalIdentifier()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetExternalIdentifier getTestObject()
    {
        ExternalIdentifier testBean = new ExternalIdentifier();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setKeyPattern(KeyPattern.STABLE_KEY);
        testBean.setIdentifier("TestIdentifier");
        testBean.setDescription("TestDescription");
        testBean.setScopeDescription("TestScopeDescription");
        testBean.setScope(scope);
        testBean.setSource("TestSource");
        testBean.setUsage("TestUsage");

        return new AssetExternalIdentifier(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetExternalIdentifier getDifferentObject()
    {
        ExternalIdentifier testBean = new ExternalIdentifier();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setKeyPattern(KeyPattern.STABLE_KEY);
        testBean.setIdentifier("TestIdentifier");
        testBean.setDescription("TestDescription");
        testBean.setScopeDescription("TestScopeDescription");
        testBean.setScope(scope);
        testBean.setSource("TestSource");
        testBean.setUsage("TestUsage");

        return new AssetExternalIdentifier(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetExternalIdentifier getAnotherDifferentObject()
    {
        ExternalIdentifier testBean = new ExternalIdentifier();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setKeyPattern(KeyPattern.STABLE_KEY);
        testBean.setIdentifier("TestDifferentIdentifier");
        testBean.setDescription("TestDescription");
        testBean.setScopeDescription("TestScopeDescription");
        testBean.setScope(scope);
        testBean.setSource("TestSource");
        testBean.setUsage("TestUsage");

        return new AssetExternalIdentifier(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetExternalIdentifier  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getSource().equals("TestSource"));
        assertTrue(resultObject.getUsage().equals("TestUsage"));
        assertTrue(resultObject.getScopeDescription().equals("TestScopeDescription"));
        assertTrue(resultObject.getIdentifier().equals("TestIdentifier"));
        assertTrue(resultObject.getScope() != null);
        assertTrue(resultObject.getKeyPattern().equals(KeyPattern.STABLE_KEY));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetExternalIdentifier  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getIdentifier() == null);
        assertTrue(nullObject.getKeyPattern() == null);
        assertTrue(nullObject.getScope() == null);
        assertTrue(nullObject.getScopeDescription() == null);
        assertTrue(nullObject.getSource() == null);
        assertTrue(nullObject.getUsage() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ExternalIdentifier         nullBean;
        AssetExternalIdentifier    nullObject;
        AssetExternalIdentifier    nullTemplate;
        AssetDescriptor            parentAsset;

        nullBean = null;
        nullObject = new AssetExternalIdentifier(nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalIdentifier();
        nullObject = new AssetExternalIdentifier(nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalIdentifier(null);
        nullObject = new AssetExternalIdentifier(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetExternalIdentifier(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalIdentifier();
        nullObject = new AssetExternalIdentifier(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ExternalIdentifier(null);
        nullObject = new AssetExternalIdentifier(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetExternalIdentifier(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the scope is properly managed
     */
    @Test public void testScope()
    {
        ExternalReference  scopeBean = new ExternalReference();
        scopeBean.setQualifiedName("TestScopeName");

        ExternalIdentifier externalIdentifier = new ExternalIdentifier();
        externalIdentifier.setQualifiedName("TestName");
        externalIdentifier.setScope(scopeBean);

        AssetExternalIdentifier testObject = new AssetExternalIdentifier(externalIdentifier);
        AssetReferenceable      scope = testObject.getScope();

        assertTrue(scope.getQualifiedName().equals("TestScopeName"));
        assertTrue(scope.getReferenceableBean().getQualifiedName().equals("TestScopeName"));
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

        AssetExternalIdentifier  sameObject = getTestObject();
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
        validateResultObject(new AssetExternalIdentifier(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ExternalIdentifier"));
    }
}
