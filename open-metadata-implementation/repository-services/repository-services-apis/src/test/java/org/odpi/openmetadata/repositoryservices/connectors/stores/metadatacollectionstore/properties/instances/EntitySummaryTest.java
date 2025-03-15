/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * EntitySummaryTest provides test of EntitySummary
 */
public class EntitySummaryTest
{
    private InstanceType           type                   = new InstanceType();
    private String                 createdBy              = "TestAuthor";
    private String                 updatedBy              = "TestEditor";
    private Date                   createTime             = new Date(23);
    private Date                   updateTime             = new Date(45);
    private long                   version                = 30L;
    private InstanceStatus         currentStatus          = InstanceStatus.UNKNOWN;
    private InstanceStatus         statusOnDelete         = InstanceStatus.UNKNOWN;
    private InstanceProvenanceType instanceProvenanceType = InstanceProvenanceType.CONTENT_PACK;
    private String                 metadataCollectionId   = "TestHomeId";
    private String                 guid                   = "TestInstanceGUID";
    private String                 instanceURL            = "TestInstanceURL";
    private List<Classification>   classifications        = new ArrayList<>();



    public EntitySummaryTest()
    {
        Classification  classification = new Classification();

        classification.setName("TestClassification");
        classifications.add(classification);
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private EntitySummary   getTestObject()
    {
        EntitySummary testObject = new EntitySummary();

        testObject.setType(type);
        testObject.setCreatedBy(createdBy);
        testObject.setUpdatedBy(updatedBy);
        testObject.setCreateTime(createTime);
        testObject.setUpdateTime(updateTime);
        testObject.setVersion(version);
        testObject.setStatus(currentStatus);
        testObject.setStatusOnDelete(statusOnDelete);
        testObject.setInstanceProvenanceType(instanceProvenanceType);
        testObject.setMetadataCollectionId(metadataCollectionId);
        testObject.setGUID(guid);
        testObject.setInstanceURL(instanceURL);
        testObject.setClassifications(classifications);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(EntitySummary   testObject)
    {
        assertTrue(testObject.getType().equals(type));
        assertTrue(testObject.getCreatedBy().equals(createdBy));
        assertTrue(testObject.getUpdatedBy().equals(updatedBy));
        assertTrue(testObject.getCreateTime().equals(createTime));
        assertTrue(testObject.getUpdateTime().equals(updateTime));
        assertTrue(testObject.getVersion() == version);
        assertTrue(testObject.getStatus().equals(currentStatus));
        assertTrue(testObject.getStatusOnDelete().equals(statusOnDelete));
        assertTrue(testObject.getInstanceProvenanceType().equals(instanceProvenanceType));
        assertTrue(testObject.getMetadataCollectionId().equals(metadataCollectionId));
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getInstanceURL().equals(instanceURL));
        assertTrue(testObject.getClassifications().equals(classifications));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        EntitySummary testObject = new EntitySummary();

        assertTrue(testObject.getType() == null);
        assertTrue(testObject.getCreatedBy() == null);
        assertTrue(testObject.getUpdatedBy() == null);
        assertTrue(testObject.getCreateTime() == null);
        assertTrue(testObject.getUpdateTime() == null);
        assertTrue(testObject.getVersion() == 0L);
        assertTrue(testObject.getStatus() == null);
        assertTrue(testObject.getStatusOnDelete() == null);
        assertTrue(testObject.getInstanceProvenanceType() == null);
        assertTrue(testObject.getMetadataCollectionId() == null);
        assertTrue(testObject.getGUID() == null);
        assertTrue(testObject.getInstanceURL() == null);
        assertTrue(testObject.getClassifications() == null);

        testObject.setClassifications(new ArrayList<>());
        assertTrue(testObject.getClassifications() == null);

        EntitySummary anotherTestObject = getTestObject();

        validateObject(new EntitySummary(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, EntitySummary.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through super class
         */
        InstanceAuditHeader testObject = getTestObject();

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
            validateObject((EntitySummary)objectMapper.readValue(jsonString, InstanceAuditHeader.class));
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
        assertTrue(getTestObject().toString().contains("EntitySummary"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        EntitySummary testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        EntitySummary  differentObject = getTestObject();

        differentObject.setCreatedBy("DifferentAuthor");

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setMetadataCollectionId("DifferentHomeId");

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setClassifications(null);

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        InstanceHeader testObject = getTestObject();
        InstanceHeader anotherObject = getTestObject();
        anotherObject.setCreatedBy("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
