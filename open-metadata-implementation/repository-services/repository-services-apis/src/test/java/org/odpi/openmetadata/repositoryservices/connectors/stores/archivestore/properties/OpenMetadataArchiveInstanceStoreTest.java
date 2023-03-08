/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * OpenMetadataArchiveInstanceStore stores details of changes to the open metadata types.
 * Validate that it can be created, cloned, serialized and compared.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveInstanceStoreTest
{
    private List<EntityDetail>                  entities        = new ArrayList<>();
    private List<Relationship>                  relationships   = new ArrayList<>();
    private List<ClassificationEntityExtension> classifications = new ArrayList<>();


    /**
     * Constructor sets up the complex types
     */
    public OpenMetadataArchiveInstanceStoreTest()
    {
        entities.add(new EntityDetail());
        relationships.add(new Relationship());
        classifications.add(new ClassificationEntityExtension());
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private OpenMetadataArchiveInstanceStore   getTestObject()
    {
        OpenMetadataArchiveInstanceStore testObject = new OpenMetadataArchiveInstanceStore();

        testObject.setEntities(entities);
        testObject.setClassifications(classifications);
        testObject.setRelationships(relationships);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(OpenMetadataArchiveInstanceStore   testObject)
    {
        assertTrue(testObject.getEntities().equals(entities));
        assertTrue(testObject.getClassifications().equals(classifications));
        assertTrue(testObject.getRelationships().equals(relationships));
    }



    /**
     * Validate that the constructors set up the correct properties
     */
    @Test
    public void testConstructors()
    {
        OpenMetadataArchiveInstanceStore testObject = new OpenMetadataArchiveInstanceStore();

        assertTrue(testObject.getEntities() == null);
        assertTrue(testObject.getClassifications() == null);
        assertTrue(testObject.getRelationships() == null);

        testObject.setEntities(new ArrayList<>());
        testObject.setClassifications(new ArrayList<>());
        testObject.setRelationships(new ArrayList<>());

        assertTrue(testObject.getEntities() == null);
        assertTrue(testObject.getClassifications() == null);
        assertTrue(testObject.getRelationships() == null);

        OpenMetadataArchiveInstanceStore anotherTestObject = getTestObject();

        validateObject(new OpenMetadataArchiveInstanceStore(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, OpenMetadataArchiveInstanceStore.class));
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
            validateObject((OpenMetadataArchiveInstanceStore)objectMapper.readValue(jsonString, OpenMetadataArchiveElementHeader.class));
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
        assertTrue(getTestObject().toString().contains("OpenMetadataArchiveInstanceStore"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        OpenMetadataArchiveInstanceStore testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        OpenMetadataArchiveInstanceStore  differentObject = getTestObject();

        differentObject.setEntities(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setClassifications(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setRelationships(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        OpenMetadataArchiveInstanceStore testObject = getTestObject();
        OpenMetadataArchiveInstanceStore anotherObject = getTestObject();
        anotherObject.setRelationships(null);

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
