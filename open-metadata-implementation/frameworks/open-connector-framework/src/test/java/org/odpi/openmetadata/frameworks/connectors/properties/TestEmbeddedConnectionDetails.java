/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the EmbeddedConnectionDetails can function as a facade for its bean.
 */
public class TestEmbeddedConnectionDetails
{
    private Connection          connection = new Connection();
    private Map<String, Object> arguments  = new HashMap<>();


    /**
     * Default constructor
     */
    public TestEmbeddedConnectionDetails()
    {
        connection.setQualifiedName("TestConnectionName");
        arguments.put("Property", "TestPropertyValue");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private EmbeddedConnectionDetails getTestObject()
    {
        EmbeddedConnection testBean = new EmbeddedConnection();

        testBean.setDisplayName("TestDisplayName");
        testBean.setEmbeddedConnection(connection);
        testBean.setArguments(arguments);

        return new EmbeddedConnectionDetails(testBean);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private EmbeddedConnectionDetails getDifferentObject()
    {
        EmbeddedConnection testBean    = new EmbeddedConnection();

        testBean.setDisplayName("TestName");
        testBean.setEmbeddedConnection(connection);
        testBean.setArguments(arguments);

        return new EmbeddedConnectionDetails(testBean);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private EmbeddedConnectionDetails getAnotherDifferentObject()
    {
        EmbeddedConnection testBean = new EmbeddedConnection();

        testBean.setDisplayName("TestDifferentDisplayName");
        testBean.setEmbeddedConnection(connection);
        testBean.setArguments(arguments);

        return new EmbeddedConnectionDetails(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(EmbeddedConnectionDetails resultObject)
    {
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getConnectionProperties().equals(new ConnectionDetails(connection)));
        assertTrue(resultObject.getArguments().equals(new HashMap<>(arguments)));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(EmbeddedConnectionDetails nullObject)
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
        EmbeddedConnection        nullBean;
        EmbeddedConnectionDetails nullObject;
        EmbeddedConnectionDetails nullTemplate;
        AssetDescriptor           parentAsset;

        nullBean = null;
        nullObject = new EmbeddedConnectionDetails(nullBean);
        validateNullObject(nullObject);

        nullBean = new EmbeddedConnection();
        nullObject = new EmbeddedConnectionDetails(nullBean);
        validateNullObject(nullObject);

        nullBean = new EmbeddedConnection(null);
        nullObject = new EmbeddedConnectionDetails(nullBean);
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

        EmbeddedConnectionDetails sameObject = getTestObject();
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
        validateResultObject(new EmbeddedConnectionDetails(getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("EmbeddedConnection"));
    }
}
