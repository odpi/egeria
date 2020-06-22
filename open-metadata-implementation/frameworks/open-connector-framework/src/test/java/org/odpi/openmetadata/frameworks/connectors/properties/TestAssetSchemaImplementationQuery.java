/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaImplementationQuery;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetSchemaImplementationQuery can function as a facade for its bean.
 */
public class TestAssetSchemaImplementationQuery
{
    private SchemaAttribute      schemaAttribute     = new SchemaAttribute();
    private AssetSchemaAttribute queryTargetElement  = null;
    private AssetSchemaType      attributeSchemaType = null;


    /**
     * Default constructor
     */
    public TestAssetSchemaImplementationQuery()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetSchemaImplementationQuery getTestObject()
    {
        SchemaImplementationQuery testBean = new SchemaImplementationQuery();

        testBean.setQueryId("5");
        testBean.setQueryType("TestQueryType");
        testBean.setQuery("TestQuery");
        testBean.setQueryTargetGUID("TestGUID");

        return new AssetSchemaImplementationQuery(testBean, queryTargetElement);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetSchemaImplementationQuery getDifferentObject()
    {
        AssetSummary              parentAsset = new AssetSummary(new Asset());
        SchemaImplementationQuery testBean    = new SchemaImplementationQuery();

        testBean.setQueryId("5");
        testBean.setQueryType("TestQueryType");
        testBean.setQuery("TestQuery");
        testBean.setQueryTargetGUID("TestGUID");

        return new AssetSchemaImplementationQuery(parentAsset, testBean, queryTargetElement);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetSchemaImplementationQuery getAnotherDifferentObject()
    {
        SchemaImplementationQuery testBean = new SchemaImplementationQuery();

        testBean.setQueryId("5");
        testBean.setQueryType("TestQueryType");
        testBean.setQuery("TestDifferentQuery");
        testBean.setQueryTargetGUID("TestGUID");

        return new AssetSchemaImplementationQuery(testBean, queryTargetElement);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetSchemaImplementationQuery getYetAnotherDifferentObject()
    {
        SchemaImplementationQuery testBean        = new SchemaImplementationQuery();
        SchemaAttribute           queryTargetBean = new SchemaAttribute();
        queryTargetBean.setAttributeName("TestAttributeName");

        testBean.setQueryId("5");
        testBean.setQueryType("TestQueryType");
        testBean.setQuery("TestQuery");
        testBean.setQueryTargetGUID("TestGUID");

        return new AssetSchemaImplementationQuery(testBean, new AssetSchemaAttribute(null, queryTargetBean));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetSchemaImplementationQuery resultObject)
    {
        assertTrue("5".equals(resultObject.getQueryId()));
        assertTrue(resultObject.getQueryType().equals("TestQueryType"));
        assertTrue(resultObject.getQuery().equals("TestQuery"));
        assertTrue(resultObject.getQueryTargetElement() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetSchemaImplementationQuery nullObject)
    {
        assertTrue(nullObject.getQueryType() == null);
        assertTrue(nullObject.getQuery() == null);
        assertTrue(nullObject.getQueryId() == null);
        assertTrue(nullObject.getQueryTargetElement() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaImplementationQuery      nullBean;
        AssetSchemaImplementationQuery nullObject;
        AssetSchemaImplementationQuery nullTemplate;
        AssetDescriptor                parentAsset;

        nullBean = null;
        nullObject = new AssetSchemaImplementationQuery(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new SchemaImplementationQuery();
        nullObject = new AssetSchemaImplementationQuery(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new SchemaImplementationQuery(null);
        nullObject = new AssetSchemaImplementationQuery(nullBean, null);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetSchemaImplementationQuery(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new SchemaImplementationQuery();
        nullObject = new AssetSchemaImplementationQuery(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new SchemaImplementationQuery(null);
        nullObject = new AssetSchemaImplementationQuery(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetSchemaImplementationQuery(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the SchemaImplementationQuery replies are correctly managed
     */
    @Test public void  testSchemaType()
    {
        AssetDescriptor parentAsset = new AssetSummary(new Asset());

        SchemaAttribute      schemaAttribute = new SchemaAttribute();
        AssetSchemaAttribute queryTarget     = new AssetSchemaAttribute(schemaAttribute);

        SchemaImplementationQuery testBean = new SchemaImplementationQuery();
        testBean.setQueryTargetGUID("TestGUID");

        AssetSchemaImplementationQuery
                                       testTemplate = new AssetSchemaImplementationQuery((SchemaImplementationQuery)null, null);
        AssetSchemaImplementationQuery testObject;

        assertTrue(testTemplate.getQueryTargetElement() == null);

        testTemplate = new AssetSchemaImplementationQuery(testBean, queryTarget);

        assertTrue(testTemplate.getQueryTargetElement() != null);

        testTemplate = new AssetSchemaImplementationQuery(parentAsset, testBean, queryTarget);

        assertTrue(testTemplate.getQueryTargetElement() != null);


        testObject = new AssetSchemaImplementationQuery(parentAsset, testTemplate);

        assertTrue(testObject.getQueryTargetElement() != null);

        testObject = new AssetSchemaImplementationQuery(parentAsset, null);

        assertTrue(testObject.getQueryTargetElement() == null);

        testTemplate = new AssetSchemaImplementationQuery(parentAsset, testBean, null);
        testObject = new AssetSchemaImplementationQuery(parentAsset, testTemplate);

        assertTrue(testObject.getQueryTargetElement() == null);
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

        AssetSchemaImplementationQuery sameObject = getTestObject();
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
        validateResultObject(new AssetSchemaImplementationQuery(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaImplementationQuery"));
    }
}
