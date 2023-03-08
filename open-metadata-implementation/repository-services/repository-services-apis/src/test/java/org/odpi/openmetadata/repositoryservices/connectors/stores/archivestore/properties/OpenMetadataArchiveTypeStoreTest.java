/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * OpenMetadataArchiveTypeStore stores details of changes to the open metadata types.
 * Validate that it can be created, cloned, serialized and compared.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveTypeStoreTest
{
    private List<AttributeTypeDef> attributeTypeDefs = new ArrayList<>();
    private List<TypeDefPatch>     typeDefPatches    = new ArrayList<>();
    private List<TypeDef>          newTypeDefs       = new ArrayList<>();


    /**
     * Constructor sets up the complex types
     */
    public OpenMetadataArchiveTypeStoreTest()
    {
        attributeTypeDefs.add(new PrimitiveDef());
        typeDefPatches.add(new TypeDefPatch());
        newTypeDefs.add(new EntityDef());
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private OpenMetadataArchiveTypeStore   getTestObject()
    {
        OpenMetadataArchiveTypeStore testObject = new OpenMetadataArchiveTypeStore();

        testObject.setAttributeTypeDefs(attributeTypeDefs);
        testObject.setNewTypeDefs(newTypeDefs);
        testObject.setTypeDefPatches(typeDefPatches);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(OpenMetadataArchiveTypeStore   testObject)
    {
        assertTrue(testObject.getAttributeTypeDefs().equals(attributeTypeDefs));
        assertTrue(testObject.getNewTypeDefs().equals(newTypeDefs));
        assertTrue(testObject.getTypeDefPatches().equals(typeDefPatches));
    }



    /**
     * Validate that the constructors set up the correct properties
     */
    @Test
    public void testConstructors()
    {
        OpenMetadataArchiveTypeStore testObject = new OpenMetadataArchiveTypeStore();

        assertTrue(testObject.getAttributeTypeDefs() == null);
        assertTrue(testObject.getNewTypeDefs() == null);
        assertTrue(testObject.getTypeDefPatches() == null);

        testObject.setAttributeTypeDefs(new ArrayList<>());
        testObject.setNewTypeDefs(new ArrayList<>());
        testObject.setTypeDefPatches(new ArrayList<>());

        assertTrue(testObject.getAttributeTypeDefs() == null);
        assertTrue(testObject.getNewTypeDefs() == null);
        assertTrue(testObject.getTypeDefPatches() == null);

        OpenMetadataArchiveTypeStore anotherTestObject = getTestObject();

        validateObject(new OpenMetadataArchiveTypeStore(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, OpenMetadataArchiveTypeStore.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through super class
         */
        OpenMetadataArchiveElementHeader testObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(testObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject((OpenMetadataArchiveTypeStore)objectMapper.readValue(jsonString, OpenMetadataArchiveElementHeader.class));
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
        assertTrue(getTestObject().toString().contains("OpenMetadataArchiveTypeStore"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        OpenMetadataArchiveTypeStore testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        OpenMetadataArchiveTypeStore  differentObject = getTestObject();

        differentObject.setAttributeTypeDefs(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setNewTypeDefs(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setTypeDefPatches(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        OpenMetadataArchiveTypeStore testObject = getTestObject();
        OpenMetadataArchiveTypeStore anotherObject = getTestObject();
        anotherObject.setTypeDefPatches(null);

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
