/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefMock;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * InstancePropertyValueTest provides test of InstancePropertyValue
 */
public class InstancePropertyValueTest
{
    private   InstancePropertyCategory   category = InstancePropertyCategory.PRIMITIVE;
    private   String                     typeGUID  = "TestTypeGUID";
    private   String                     typeName  = "TestTypeName";


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private InstancePropertyValue   getTestObject()
    {
        InstancePropertyValue testObject = new InstancePropertyValueMock();

        testObject.setTypeGUID(typeGUID);
        testObject.setTypeName(typeName);
        testObject.setInstancePropertyCategory(category);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(InstancePropertyValue   testObject)
    {
        assertTrue(testObject.getTypeGUID().equals(typeGUID));
        assertTrue(testObject.getTypeName().equals(typeName));
        assertTrue(testObject.getInstancePropertyCategory().equals(category));
    }


    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        InstancePropertyValueMock testObject = new InstancePropertyValueMock();

        assertTrue(testObject.getInstancePropertyCategory() == null);
        assertTrue(testObject.getTypeGUID() == null);
        assertTrue(testObject.getTypeName() == null);

        testObject = new InstancePropertyValueMock(InstancePropertyCategory.PRIMITIVE);

        assertTrue(testObject.getTypeGUID() == null);
        assertTrue(testObject.getTypeName() == null);
        assertTrue(testObject.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE);

        InstancePropertyValue anotherTestObject = getTestObject();

        validateObject(new InstancePropertyValueMock(anotherTestObject));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, InstancePropertyValueMock.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("InstancePropertyValue"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        InstancePropertyValue testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        InstancePropertyValue testObject = getTestObject();
        InstancePropertyValue anotherObject = getTestObject();
        anotherObject.setTypeGUID("DifferentGUID");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
