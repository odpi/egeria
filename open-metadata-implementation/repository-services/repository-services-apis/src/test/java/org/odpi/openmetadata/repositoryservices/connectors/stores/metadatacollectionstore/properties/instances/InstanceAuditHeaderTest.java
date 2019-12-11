/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * InstanceAuditHeaderTest provides test of InstanceAuditHeader
 */
public class InstanceAuditHeaderTest
{
    private InstanceType                type                   = new InstanceType();
    private InstanceProvenanceType      instanceProvenanceType = InstanceProvenanceType.LOCAL_COHORT;
    private String                      metadataCollectionId   = "TestMetadataCollectionId";
    private String                      metadataCollectionName = "TestMetadataCollectionName";
    private String                      replicatedBy           = "TestReplicatedBy";
    private String                      instanceLicense        = "TestInstanceLicense";
    private String                      createdBy              = "TestAuthor";
    private String                      updatedBy              = "TestEditor";
    private Date                        createTime             = new Date(23);
    private Date                        updateTime             = new Date(45);
    private List<String>                maintainedBy           = new ArrayList<>();
    private long                        version                = 30L;
    private InstanceStatus              currentStatus          = InstanceStatus.UNKNOWN;
    private InstanceStatus              statusOnDelete         = InstanceStatus.UNKNOWN;
    private Map<String, Serializable>   mappingProperties      = new HashMap<>();


    public InstanceAuditHeaderTest()
    {
        maintainedBy.add("TestMaintainer");
        mappingProperties.put("TestProperty", "TestValue");
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private InstanceAuditHeader   getTestObject()
    {
        InstanceAuditHeader testObject = new InstanceAuditHeaderMock();

        testObject.setHeaderVersion(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
        testObject.setType(type);
        testObject.setInstanceProvenanceType(instanceProvenanceType);
        testObject.setMetadataCollectionId(metadataCollectionId);
        testObject.setMetadataCollectionName(metadataCollectionName);
        testObject.setReplicatedBy(replicatedBy);
        testObject.setInstanceLicense(instanceLicense);
        testObject.setCreatedBy(createdBy);
        testObject.setMaintainedBy(maintainedBy);
        testObject.setUpdatedBy(updatedBy);
        testObject.setCreateTime(createTime);
        testObject.setUpdateTime(updateTime);
        testObject.setVersion(version);
        testObject.setStatus(currentStatus);
        testObject.setStatusOnDelete(statusOnDelete);
        testObject.setMappingProperties(mappingProperties);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(InstanceAuditHeader   testObject)
    {
        assertTrue(testObject.getHeaderVersion() == InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
        assertTrue(testObject.getType().equals(type));
        assertTrue(testObject.getInstanceProvenanceType().equals(instanceProvenanceType));
        assertTrue(testObject.getMetadataCollectionId().equals(metadataCollectionId));
        assertTrue(testObject.getMetadataCollectionName().equals(metadataCollectionName));
        assertTrue(testObject.getReplicatedBy().equals(replicatedBy));
        assertTrue(testObject.getInstanceLicense().equals(instanceLicense));
        assertTrue(testObject.getCreatedBy().equals(createdBy));
        assertTrue(testObject.getMaintainedBy().equals(maintainedBy));
        assertTrue(testObject.getUpdatedBy().equals(updatedBy));
        assertTrue(testObject.getCreateTime().equals(createTime));
        assertTrue(testObject.getUpdateTime().equals(updateTime));
        assertTrue(testObject.getVersion() == version);
        assertTrue(testObject.getStatus().equals(currentStatus));
        assertTrue(testObject.getStatusOnDelete().equals(statusOnDelete));
        assertTrue(testObject.getMappingProperties().equals(mappingProperties));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        InstanceAuditHeader testObject = new InstanceAuditHeaderMock();

        assertTrue(testObject.getType() == null);
        assertTrue(testObject.getInstanceProvenanceType() == null);
        assertTrue(testObject.getMetadataCollectionId() == null);
        assertTrue(testObject.getMetadataCollectionName() == null);
        assertTrue(testObject.getReplicatedBy() == null);
        assertTrue(testObject.getInstanceLicense() == null);
        assertTrue(testObject.getCreatedBy() == null);
        assertTrue(testObject.getMaintainedBy() == null);
        assertTrue(testObject.getUpdatedBy() == null);
        assertTrue(testObject.getCreateTime() == null);
        assertTrue(testObject.getUpdateTime() == null);
        assertTrue(testObject.getVersion() == 0L);
        assertTrue(testObject.getStatus() == null);
        assertTrue(testObject.getStatusOnDelete() == null);
        assertTrue(testObject.getMappingProperties() == null);

        testObject.setMaintainedBy(new ArrayList<>());
        assertTrue(testObject.getMaintainedBy() == null);

        testObject.setMappingProperties(new HashMap<>());
        assertTrue(testObject.getMappingProperties() == null);

        InstanceAuditHeader anotherTestObject = getTestObject();

        validateObject(new InstanceAuditHeaderMock(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, InstanceAuditHeaderMock.class));
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
        assertTrue(getTestObject().toString().contains("InstanceAuditHeader"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        InstanceAuditHeader testObject = getTestObject();

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

        InstanceAuditHeader testObject = getTestObject();
        InstanceAuditHeader anotherObject = getTestObject();
        anotherObject.setCreatedBy("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
