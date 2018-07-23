/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Verify the StarRating enum contains unique ordinals, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class TestOMRSAuditingComponent
{
    private List<Integer> existingOrdinals = null;

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(int  ordinal)
    {
        Integer       newOrdinal = ordinal;

        if (existingOrdinals.contains(newOrdinal))
        {
            return false;
        }
        else
        {
            existingOrdinals.add(newOrdinal);
            return true;
        }
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testEnumValues()
    {
        existingOrdinals = new ArrayList<>();

        OMRSAuditingComponent  testValue;

        testValue = OMRSAuditingComponent.UNKNOWN;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() == null);

        testValue = OMRSAuditingComponent.AUDIT_LOG;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.OPERATIONAL_SERVICES;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.REPOSITORY_EVENT_MANAGER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.REST_SERVICES;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.REST_REPOSITORY_CONNECTOR;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.METADATA_HIGHWAY_MANAGER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.COHORT_MANAGER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.COHORT_REGISTRY;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.REGISTRY_STORE;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.EVENT_PUBLISHER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.EVENT_LISTENER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);


        testValue = OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);

        testValue = OMRSAuditingComponent.ARCHIVE_MANAGER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);


        testValue = OMRSAuditingComponent.ENTERPRISE_CONNECTOR_MANAGER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);


        testValue = OMRSAuditingComponent.ENTERPRISE_REPOSITORY_CONNECTOR;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);


        testValue = OMRSAuditingComponent.LOCAL_REPOSITORY_CONNECTOR;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);


        testValue = OMRSAuditingComponent.TYPEDEF_MANAGER;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);


        testValue = OMRSAuditingComponent.INSTANCE_EVENT_PROCESSOR;
        assertTrue(isUniqueOrdinal(testValue.getComponentId()));
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);
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
            jsonString = objectMapper.writeValueAsString(OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, OMRSAuditingComponent.class) == OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER);
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
        assertTrue(OMRSAuditingComponent.TYPEDEF_MANAGER.toString().contains("OMRSAuditingComponent"));
    }
}
