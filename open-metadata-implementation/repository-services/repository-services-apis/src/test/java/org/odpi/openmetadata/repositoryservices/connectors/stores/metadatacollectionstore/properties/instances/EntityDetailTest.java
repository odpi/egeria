/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * EntityDetailTest provides test of EntityDetail
 */
public class EntityDetailTest
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
    private List<Classification>   classifications        = new ArrayList<>();
    private InstanceProperties     entityProperties       = new InstanceProperties();



    public EntityDetailTest()
    {
        Classification  classification = new Classification();

        classification.setName("TestClassification");
        classifications.add(classification);

        PrimitivePropertyValue             propertyValue = new PrimitivePropertyValue();
        Map<String, InstancePropertyValue> propertyMap   = new HashMap<>();
        propertyMap.put("propertyName", propertyValue);
        entityProperties.setInstanceProperties(propertyMap);
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private EntityDetail   getTestObject()
    {
        EntityDetail testObject = new EntityDetail();

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
        testObject.setClassifications(classifications);
        testObject.setProperties(entityProperties);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(EntityDetail   testObject)
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
        assertTrue(testObject.getClassifications().equals(classifications));
        assertTrue(testObject.getProperties().equals(entityProperties));
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateSummaryObject(EntitySummary   testObject)
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
        assertTrue(testObject.getClassifications().equals(classifications));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        EntityDetail testObject = new EntityDetail();

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
        assertTrue(testObject.getClassifications() == null);

        testObject.setClassifications(new ArrayList<>());
        assertTrue(testObject.getClassifications() == null);

        EntityDetail anotherTestObject = getTestObject();

        validateObject(new EntityDetail(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, EntityDetail.class));
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
            validateSummaryObject(objectMapper.readValue(jsonString, EntitySummary.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Validate retrieval of properties
     */
    @Test public void testProperties()
    {
        EntityDetail   testObject = getTestObject();

        assertTrue(testObject.getProperties().equals(entityProperties));

        testObject.setProperties(null);

        assertTrue(testObject.getProperties() == null);

        InstanceProperties instanceProperties = new InstanceProperties();
        testObject.setProperties(instanceProperties);

        assertTrue(testObject.getProperties() == null);

        instanceProperties.setInstanceProperties(new HashMap<>());
        testObject.setProperties(instanceProperties);

        assertTrue(testObject.getProperties() == null);
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("EntityDetail"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        EntityDetail testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        EntityDetail  differentObject = getTestObject();

        differentObject.setCreatedBy("DifferentAuthor");

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setMetadataCollectionId("DifferentHomeId");

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setClassifications(null);

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setProperties(null);

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
