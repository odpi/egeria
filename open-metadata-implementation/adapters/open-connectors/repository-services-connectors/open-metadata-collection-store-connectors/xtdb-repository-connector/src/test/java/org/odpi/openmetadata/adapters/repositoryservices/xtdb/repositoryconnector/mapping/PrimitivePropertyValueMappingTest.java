/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.*;

/**
 * Test the mappings of various property values.
 */
public class PrimitivePropertyValueMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testDate() {

        Date now = new Date();
        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        InstanceProperties properties = helper.addDatePropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "property",
                now,
                this.getClass().getName());

        PrimitivePropertyValue ppv = (PrimitivePropertyValue) properties.getPropertyValue("property");
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);

        assertTrue(comparison instanceof Date);
        assertEquals(comparison, now, "Dates are expected to be identical after conversion.");

    }

    @Test
    void testString() {

        String test = "test";
        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        InstanceProperties properties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "property",
                test,
                this.getClass().getName());

        PrimitivePropertyValue ppv = (PrimitivePropertyValue) properties.getPropertyValue("property");
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);

        assertTrue(comparison instanceof String);
        assertEquals(comparison, test, "Strings are expected to be identical after conversion.");

    }

    @Test
    void testBoolean() {

        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        InstanceProperties properties = helper.addBooleanPropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "property",
                true,
                this.getClass().getName());

        PrimitivePropertyValue ppv = (PrimitivePropertyValue) properties.getPropertyValue("property");
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);

        assertTrue(comparison instanceof Boolean);
        assertTrue((Boolean)comparison, "Booleans are expected to be identical after conversion.");

    }

    @Test
    void testFloat() {

        float test = 123.456f;
        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        InstanceProperties properties = helper.addFloatPropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "property",
                test,
                this.getClass().getName());

        PrimitivePropertyValue ppv = (PrimitivePropertyValue) properties.getPropertyValue("property");
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);

        assertTrue(comparison instanceof Float);
        assertEquals(comparison, test, "Floats are expected to be identical after conversion.");

    }

    @Test
    void testLong() {

        long test = 123456L;
        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        InstanceProperties properties = helper.addLongPropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "property",
                test,
                this.getClass().getName());

        PrimitivePropertyValue ppv = (PrimitivePropertyValue) properties.getPropertyValue("property");
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);

        assertTrue(comparison instanceof Long);
        assertEquals(comparison, test, "Longs are expected to be identical after conversion.");

    }

    @Test
    void testInt() {

        int test = -123;
        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        InstanceProperties properties = helper.addIntPropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "property",
                test,
                this.getClass().getName());

        PrimitivePropertyValue ppv = (PrimitivePropertyValue) properties.getPropertyValue("property");
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);

        assertTrue(comparison instanceof Integer);
        assertEquals(comparison, test, "Integers are expected to be identical after conversion.");

    }

    @Test
    void testEmpty() {
        PrimitivePropertyValue ppv = new PrimitivePropertyValue();
        Object comparison = PrimitivePropertyValueMapping.getPrimitiveValueForComparison(ppv);
        assertNull(comparison, "Empty primitive values are expected to be null after conversion.");
    }

}
