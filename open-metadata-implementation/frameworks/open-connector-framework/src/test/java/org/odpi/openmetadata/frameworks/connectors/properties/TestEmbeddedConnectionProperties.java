/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaLink;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetSchemaLink can function as a facade for its bean.
 */
public class TestEmbeddedConnectionProperties
{
    private Connection          connection = new Connection();
    private Map<String, Object> arguments  = new HashMap<>();


    /**
     * Default constructor
     */
    public TestEmbeddedConnectionProperties()
    {
        connection.setQualifiedName("TestConnectionName");
        arguments.put("Property", "TestPropertyValue");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private EmbeddedConnectionProperties getTestObject()
    {
        EmbeddedConnection testBean = new EmbeddedConnection();

        testBean.setDisplayName("TestDisplayName");
        testBean.setEmbeddedConnection(connection);
        testBean.setArguments(arguments);

        return new EmbeddedConnectionProperties(testBean);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private EmbeddedConnectionProperties getDifferentObject()
    {
        AssetSummary  parentAsset = new AssetSummary(new Asset());
        EmbeddedConnection testBean = new EmbeddedConnection();

        testBean.setDisplayName("TestDisplayName");
        testBean.setEmbeddedConnection(connection);
        testBean.setArguments(arguments);

        return new EmbeddedConnectionProperties(parentAsset, testBean);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private EmbeddedConnectionProperties getAnotherDifferentObject()
    {
        EmbeddedConnection testBean = new EmbeddedConnection();

        testBean.setDisplayName("TestDifferentDisplayName");
        testBean.setEmbeddedConnection(connection);
        testBean.setArguments(arguments);

        return new EmbeddedConnectionProperties(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(EmbeddedConnectionProperties  resultObject)
    {
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getConnectionProperties().equals(new ConnectionProperties(connection)));
        assertTrue(resultObject.getArguments().equals(new AdditionalProperties(arguments)));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(EmbeddedConnectionProperties  nullObject)
    {
        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getArguments() == null);
        assertTrue(nullObject.getConnectionProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        EmbeddedConnection           nullBean;
        EmbeddedConnectionProperties nullObject;
        EmbeddedConnectionProperties nullTemplate;
        AssetDescriptor              parentAsset;

        nullBean = null;
        nullObject = new EmbeddedConnectionProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new EmbeddedConnection();
        nullObject = new EmbeddedConnectionProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new EmbeddedConnection(null);
        nullObject = new EmbeddedConnectionProperties(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new EmbeddedConnectionProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new EmbeddedConnection();
        nullObject = new EmbeddedConnectionProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new EmbeddedConnection(null);
        nullObject = new EmbeddedConnectionProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new EmbeddedConnectionProperties(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Validate that the link GUIDs and properties are properly managed
     */
    @Test public void testLinkObjects()
    {
        List<String>        linkGUIDs      = new ArrayList<>();
        linkGUIDs.add("TestGUID1");
        linkGUIDs.add("TestGUID2");

        Map<String, Object> linkProperties = new HashMap<>();
        linkProperties.put("PropertyName", "PropertyValue");

        AssetSummary        parentAsset    = new AssetSummary(new Asset());
        SchemaLink          testObject     = new SchemaLink();

        testObject.setLinkGUID("TestLinkGUID");
        testObject.setLinkName("TestLinkName");
        testObject.setLinkType("TestLinkType");
        testObject.setLinkedAttributeGUIDs(linkGUIDs);
        testObject.setLinkProperties(linkProperties);

        AssetSchemaLink assetSchemaLinkTemplate = new AssetSchemaLink(parentAsset, testObject);

        assertTrue(assetSchemaLinkTemplate.getLinkedAttributeGUIDs() != null);
        assertTrue(assetSchemaLinkTemplate.getLinkProperties() != null);

        AssetSchemaLink assetSchemaLink = new AssetSchemaLink(parentAsset, assetSchemaLinkTemplate);

        assertTrue(assetSchemaLink.getLinkedAttributeGUIDs() != null);
        assertTrue(assetSchemaLink.getLinkProperties() != null);

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

        EmbeddedConnectionProperties  sameObject = getTestObject();
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
        validateResultObject(new EmbeddedConnectionProperties(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("EmbeddedConnection"));
    }
}
