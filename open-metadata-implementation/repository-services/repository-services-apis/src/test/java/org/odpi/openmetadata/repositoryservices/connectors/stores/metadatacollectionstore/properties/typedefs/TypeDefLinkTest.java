/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * TypeDefLinkTest provides test of TypeDefLink
 */
public class TypeDefLinkTest
{
    protected  String        guid               = "TestGUID";
    protected  String        name               = "TestName";
    protected  TypeDefStatus status             = TypeDefStatus.RENAMED_TYPEDEF;
    protected  String        replacedByTypeGUID = "TestReplacedByGUID";
    protected  String        replacedByTypeName = "TestReplacedByName";



    public TypeDefLinkTest()
    {
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private TypeDefLink   getTestObject()
    {
        TypeDefLink testObject = new TypeDefLink();

        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setStatus(status);
        testObject.setReplacedByTypeGUID(replacedByTypeGUID);
        testObject.setReplacedByTypeName(replacedByTypeName);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(TypeDefLink   testObject)
    {
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getStatus().equals(status));
        assertTrue(testObject.getReplacedByTypeGUID().equals(replacedByTypeGUID));
        assertTrue(testObject.getReplacedByTypeName().equals(replacedByTypeName));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        TypeDefLink testObject = new TypeDefLink();

        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getStatus());
        assertNull(testObject.getReplacedByTypeGUID());
        assertNull(testObject.getReplacedByTypeName());

        TypeDefLink anotherTestObject = getTestObject();

        validateObject(new TypeDefLink(anotherTestObject));
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
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, TypeDefLink.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("TypeDefLink"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        TypeDefLink testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        TypeDefLink  differentObject = new TypeDefLink(guid, name);

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setReplacedByTypeGUID("DifferentHomeId");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        TypeDefLink testObject = getTestObject();
        TypeDefLink anotherObject = getTestObject();
        anotherObject.setGUID("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
