/* SPDX-License-Identifier: Apache-2.0 */
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
 * Validate that the AssetDerivedSchemaElement can function as a facade for its bean.
 */
public class TestAssetDerivedSchemaElement
{
    private ElementType          type                 = new ElementType();
    private List<Classification> classifications      = new ArrayList<>();
    private Map<String, Object>  additionalProperties = new HashMap<>();

    private AssetMeanings        assetMeanings        = new MockAssetMeanings(null,
                                                                              23,
                                                                              50);

    private AssetSchemaImplementationQueries schemaImplementationQueries    = new MockAssetSchemaImplementationQueries(null,
                                                                                      12,
                                                                                      56);


    /**
     * Default constructor
     */
    public TestAssetDerivedSchemaElement()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetDerivedSchemaElement getTestObject()
    {
        DerivedSchemaElement testBean = new DerivedSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        testBean.setFormula("TestFormula");

        return new AssetDerivedSchemaElement(testBean, assetMeanings, schemaImplementationQueries);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetDerivedSchemaElement getDifferentObject()
    {
        DerivedSchemaElement testBean = new DerivedSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        testBean.setFormula("TestFormula");

        return new AssetDerivedSchemaElement(testBean, assetMeanings, schemaImplementationQueries);
    }



    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetDerivedSchemaElement getAnotherDifferentObject()
    {
        DerivedSchemaElement testBean = new DerivedSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        testBean.setFormula("TestDifferentFormula");

        return new AssetDerivedSchemaElement(testBean, assetMeanings, schemaImplementationQueries);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetDerivedSchemaElement getYetAnotherDifferentObject()
    {
        DerivedSchemaElement testBean = new DerivedSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        testBean.setFormula("TestFormula");

        return new AssetDerivedSchemaElement(new AssetSummary(new Asset()), testBean, assetMeanings, schemaImplementationQueries);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetDerivedSchemaElement getAndYetAnotherDifferentObject()
    {
        DerivedSchemaElement testBean = new DerivedSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        testBean.setFormula("TestFormula");

        return new AssetDerivedSchemaElement(new AssetSummary(new Asset()), testBean, assetMeanings, null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetDerivedSchemaElement  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getVersionNumber().equals("TestVersionNumber"));
        assertTrue(resultObject.getUsage().equals("TestUsage"));
        assertTrue(resultObject.getEncodingStandard().equals("TestEncodingStandard"));
        assertTrue(resultObject.getAuthor().equals("TestAuthor"));
        assertTrue(resultObject.getAssetMeanings() != null);

        assertTrue(resultObject.getQueries() != null);
        assertTrue(resultObject.getFormula().equals("TestFormula"));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetDerivedSchemaElement  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getAssetMeanings() == null);
        assertTrue(nullObject.getAuthor() == null);
        assertTrue(nullObject.getEncodingStandard() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getVersionNumber() == null);

        assertTrue(nullObject.getFormula() == null);
        assertTrue(nullObject.getQueries() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        DerivedSchemaElement          nullBean;
        AssetDerivedSchemaElement     nullObject;
        AssetDerivedSchemaElement     nullTemplate;
        AssetDescriptor               parentAsset;

        nullBean = null;
        nullObject = new AssetDerivedSchemaElement(nullBean, null, null);
        validateNullObject(nullObject);

        nullBean = new DerivedSchemaElement();
        nullObject = new AssetDerivedSchemaElement(nullBean, null, null);
        validateNullObject(nullObject);

        nullBean = new DerivedSchemaElement(null);
        nullObject = new AssetDerivedSchemaElement(nullBean, null, null);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetDerivedSchemaElement(parentAsset, nullBean, null, null);
        validateNullObject(nullObject);

        nullBean = new DerivedSchemaElement();
        nullObject = new AssetDerivedSchemaElement(parentAsset, nullBean, null, null);
        validateNullObject(nullObject);

        nullBean = new DerivedSchemaElement(null);
        nullObject = new AssetDerivedSchemaElement(parentAsset, nullBean, null, null);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetDerivedSchemaElement(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = new AssetDerivedSchemaElement(parentAsset, nullBean, null,  null);;
        nullObject = new AssetDerivedSchemaElement(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the link is properly managed
     */
    @Test public void testMeaningsQueries()
    {
        AssetMeanings                    meanings                    = new MockAssetMeanings(null, 23, 60);
        AssetSchemaImplementationQueries schemaImplementationQueries = new MockAssetSchemaImplementationQueries(null,
                                                                                                                12,
                                                                                                                56);
        AssetDerivedSchemaElement        testObject                  = new AssetDerivedSchemaElement(null, null, null);

        assertTrue(testObject.getAssetMeanings() == null);
        assertTrue(testObject.getQueries() == null);

        testObject = new AssetDerivedSchemaElement(null, meanings, schemaImplementationQueries);

        assertTrue(testObject.getAssetMeanings() != null);
        assertTrue(testObject.getQueries() != null);
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

        AssetDerivedSchemaElement  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
        assertFalse(getTestObject().equals(getYetAnotherDifferentObject()));
        assertFalse(getTestObject().equals(getAndYetAnotherDifferentObject()));
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
        validateResultObject(new AssetDerivedSchemaElement(null, getTestObject()));

        validateResultObject((AssetDerivedSchemaElement) getTestObject().cloneAssetSchemaElement(null));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("DerivedSchemaElement"));
    }
}
