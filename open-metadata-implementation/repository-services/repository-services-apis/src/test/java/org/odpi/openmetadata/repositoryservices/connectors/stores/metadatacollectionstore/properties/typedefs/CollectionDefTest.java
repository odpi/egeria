/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * CollectionDefTest provides test of CollectionDef
 */
public class CollectionDefTest
{
    private long                       version               = 6L;
    private String                     versionName           = "TestVersion";
    private AttributeTypeDefCategory   category              = AttributeTypeDefCategory.COLLECTION;
    private String                     guid                  = "TestGUID";
    private String                     name                  = "TestName";
    private String                     description           = "TestDescription";
    private String                     descriptionGUID       = "TestDescriptionGUID";
    private CollectionDefCategory      collectionDefCategory = CollectionDefCategory.OM_COLLECTION_ARRAY;
    private int                        argumentCount         = CollectionDefCategory.OM_COLLECTION_ARRAY.getArgumentCount();
    private List<PrimitiveDefCategory> argumentTypes         = new ArrayList<>();


    public CollectionDefTest()
    {
        argumentTypes.add(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE);
    }

    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private CollectionDef   getTestObject()
    {
        CollectionDef testObject = new CollectionDef();

        testObject.setVersion(version);
        testObject.setVersionName(versionName);
        testObject.setCategory(category);
        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setDescription(description);
        testObject.setDescriptionGUID(descriptionGUID);
        testObject.setCollectionDefCategory(collectionDefCategory);
        testObject.setArgumentCount(argumentCount);
        testObject.setArgumentTypes(argumentTypes);

        return testObject;
    }


    private void validateObject(CollectionDef   testObject)
    {
        assertTrue(testObject.getVersion() == version);
        assertTrue(testObject.getVersionName().equals(versionName));
        assertTrue(testObject.getCategory().equals(category));
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getDescription().equals(description));
        assertTrue(testObject.getDescriptionGUID().equals(descriptionGUID));
        assertTrue(testObject.getArgumentTypes().equals(argumentTypes));
        assertTrue(testObject.getArgumentCount() == argumentCount);
        assertTrue(testObject.getCollectionDefCategory() == collectionDefCategory);

    }


    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        CollectionDef testObject = new CollectionDef();

        assertTrue(testObject.getVersion() == 0L);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == AttributeTypeDefCategory.COLLECTION);
        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertNull(testObject.getArgumentTypes());
        assertTrue(testObject.getArgumentCount() == 0);
        assertNull(testObject.getCollectionDefCategory());


        testObject = new CollectionDef(CollectionDefCategory.OM_COLLECTION_MAP);

        assertTrue(testObject.getVersion() == 0L);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == AttributeTypeDefCategory.COLLECTION);
        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertTrue(testObject.getArgumentCount() > 0);
        assertFalse(testObject.getArgumentTypes() == null);
        assertTrue(testObject.getCollectionDefCategory() == CollectionDefCategory.OM_COLLECTION_MAP);

        testObject = new CollectionDef(CollectionDefCategory.OM_COLLECTION_ARRAY);

        assertTrue(testObject.getVersion() == 0L);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == AttributeTypeDefCategory.COLLECTION);
        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertTrue(testObject.getArgumentCount() > 0);
        assertFalse(testObject.getArgumentTypes() == null);
        assertTrue(testObject.getCollectionDefCategory() == CollectionDefCategory.OM_COLLECTION_ARRAY);

        testObject = new CollectionDef(CollectionDefCategory.OM_COLLECTION_ARRAY);

        assertTrue(testObject.getVersion() == 0L);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == AttributeTypeDefCategory.COLLECTION);
        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertTrue(testObject.getArgumentCount() > 0);
        assertFalse(testObject.getArgumentTypes() == null);
        assertTrue(testObject.getCollectionDefCategory() == CollectionDefCategory.OM_COLLECTION_ARRAY);

        CollectionDef anotherTestObject = getTestObject();

        validateObject(new CollectionDef(anotherTestObject));

        anotherTestObject.setArgumentTypes(new ArrayList<>());
        assertNull(anotherTestObject.getArgumentTypes());

    }

    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testCloneFromSubclass()
    {
        AttributeTypeDef testObject = this.getTestObject();

        validateObject((CollectionDef) testObject.cloneFromSubclass());
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
            validateObject(objectMapper.readValue(jsonString, CollectionDef.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through super class
         */
        AttributeTypeDef testObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(testObject);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject((CollectionDef) objectMapper.readValue(jsonString, AttributeTypeDef.class));
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
        assertTrue(getTestObject().toString().contains("CollectionDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        CollectionDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        testObject.setDescription("NewDescription");

        assertFalse(getTestObject().equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("AString"));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        CollectionDef testObject = getTestObject();
        CollectionDef anotherObject = getTestObject();
        anotherObject.setGUID("DifferentGUID");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
