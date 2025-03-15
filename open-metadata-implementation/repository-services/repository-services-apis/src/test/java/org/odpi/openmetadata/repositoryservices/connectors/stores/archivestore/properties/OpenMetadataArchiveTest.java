/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * OpenMetadataArchive stores details of changes to the open metadata types.
 * Validate that it can be created, cloned, serialized and compared.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveTest
{
    private OpenMetadataArchiveProperties    archiveProperties    = new OpenMetadataArchiveProperties();
    private OpenMetadataArchiveTypeStore     archiveTypeStore     = new OpenMetadataArchiveTypeStore();
    private OpenMetadataArchiveInstanceStore archiveInstanceStore = new OpenMetadataArchiveInstanceStore();


    /**
     * Constructor sets up the complex types
     */
    public OpenMetadataArchiveTest()
    {
        archiveProperties.setOriginatorLicense("TestLicense");
        archiveTypeStore.setTypeDefPatches(new ArrayList<>());
        archiveInstanceStore.setEntities(new ArrayList<>());
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private OpenMetadataArchive   getTestObject()
    {
        OpenMetadataArchive testObject = new OpenMetadataArchive();

        testObject.setArchiveProperties(archiveProperties);
        testObject.setArchiveInstanceStore(archiveInstanceStore);
        testObject.setArchiveTypeStore(archiveTypeStore);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(OpenMetadataArchive   testObject)
    {
        assertTrue(testObject.getArchiveProperties().equals(archiveProperties));
        assertTrue(testObject.getArchiveInstanceStore().equals(archiveInstanceStore));
        assertTrue(testObject.getArchiveTypeStore().equals(archiveTypeStore));
    }



    /**
     * Validate that the constructors set up the correct properties
     */
    @Test
    public void testConstructors()
    {
        OpenMetadataArchive testObject = new OpenMetadataArchive();

        assertTrue(testObject.getArchiveProperties() == null);
        assertTrue(testObject.getArchiveInstanceStore() == null);
        assertTrue(testObject.getArchiveTypeStore() == null);

        OpenMetadataArchive anotherTestObject = getTestObject();

        validateObject(new OpenMetadataArchive(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, OpenMetadataArchive.class));
        }
        catch (Exception   exc)
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
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject((OpenMetadataArchive)objectMapper.readValue(jsonString, OpenMetadataArchiveElementHeader.class));
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
        assertTrue(getTestObject().toString().contains("OpenMetadataArchive"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        OpenMetadataArchive testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        OpenMetadataArchive  differentObject = getTestObject();

        differentObject.setArchiveProperties(null);

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setArchiveInstanceStore(null);

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setArchiveTypeStore(null);

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        OpenMetadataArchive testObject = getTestObject();
        OpenMetadataArchive anotherObject = getTestObject();
        anotherObject.setArchiveTypeStore(null);

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
