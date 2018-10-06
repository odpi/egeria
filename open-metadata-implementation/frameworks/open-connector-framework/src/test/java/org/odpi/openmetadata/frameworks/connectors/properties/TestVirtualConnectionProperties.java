/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the VirtualConnectionProperties can function as a facade for its bean.
 */
public class TestVirtualConnectionProperties
{
    private ElementType              type                 = new ElementType();
    private List<Classification>     classifications      = new ArrayList<>();
    private Map<String, Object>      additionalProperties = new HashMap<>();
    private Map<String, Object>      securedProperties    = new HashMap<>();
    private ConnectorType            connectorType        = new ConnectorType();
    private Endpoint                 endpoint             = new Endpoint();
    private List<EmbeddedConnection> embeddedConnections  = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestVirtualConnectionProperties()
    {
        type.setElementTypeName("TestType");
        embeddedConnections.add(new EmbeddedConnection());
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private VirtualConnectionProperties getTestObject()
    {
        VirtualConnection testObject = new VirtualConnection();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDescription");
        testObject.setConnectorType(connectorType);
        testObject.setEndpoint(endpoint);
        testObject.setSecuredProperties(securedProperties);

        testObject.setEmbeddedConnections(embeddedConnections);

        return new VirtualConnectionProperties(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private VirtualConnectionProperties getDifferentObject()
    {
        VirtualConnection testObject = new VirtualConnection();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDescription");
        testObject.setConnectorType(connectorType);
        testObject.setEndpoint(endpoint);
        testObject.setSecuredProperties(securedProperties);

        testObject.setEmbeddedConnections(embeddedConnections);

        return new VirtualConnectionProperties(testObject);
    }


    /**
     * Set up an example object to test. A property from the subclass is different.
     *
     * @return filled in object
     */
    private VirtualConnectionProperties getAnotherDifferentObject()
    {
        VirtualConnection testObject = new VirtualConnection();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDifferentDescription");
        testObject.setConnectorType(connectorType);
        testObject.setEndpoint(endpoint);
        testObject.setSecuredProperties(securedProperties);

        testObject.setEmbeddedConnections(new ArrayList<EmbeddedConnection>());

        return new VirtualConnectionProperties(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(VirtualConnectionProperties  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getConnectorType().getConnectorTypeBean().equals(connectorType));
        assertTrue(resultObject.getEndpoint().getEndpointBean().equals(endpoint));
        assertTrue(resultObject.getSecuredProperties() == null);
        assertTrue(resultObject.getEmbeddedConnections() != null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(VirtualConnectionProperties  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectorType() == null);
        assertTrue(nullObject.getEndpoint() == null);
        assertTrue(nullObject.getSecuredProperties() == null);
        assertTrue(nullObject.getEmbeddedConnections() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        VirtualConnection           nullBean;
        VirtualConnectionProperties nullObject;
        VirtualConnectionProperties nullTemplate;
        AssetDescriptor             parentAsset;

        nullBean = null;
        nullObject = new VirtualConnectionProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new VirtualConnection();
        nullObject = new VirtualConnectionProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new VirtualConnection(null);
        nullObject = new VirtualConnectionProperties(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new VirtualConnectionProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new VirtualConnection();
        nullObject = new VirtualConnectionProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new VirtualConnection(null);
        nullObject = new VirtualConnectionProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new VirtualConnectionProperties(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new VirtualConnectionProperties(nullTemplate);
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

        VirtualConnectionProperties  sameObject = getTestObject();
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
        validateResultObject(new VirtualConnectionProperties(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("VirtualConnection"));
    }
}
