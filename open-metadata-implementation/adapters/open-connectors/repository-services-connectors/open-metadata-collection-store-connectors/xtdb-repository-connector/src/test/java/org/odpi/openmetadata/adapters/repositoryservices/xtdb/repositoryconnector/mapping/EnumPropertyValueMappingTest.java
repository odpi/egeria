/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test the mappings of various property values.
 */
public class EnumPropertyValueMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testValid() {

        Integer value = 5;

        EnumPropertyValue epv = new EnumPropertyValue();
        epv.setSymbolicName("TEST");
        epv.setOrdinal(value);
        epv.setDescription("test");

        Integer comparison = EnumPropertyValueMapping.getEnumPropertyValueForComparison(epv);
        assertNotNull(comparison);
        assertEquals(comparison, value, "Enum's ordinal values are expected to be identical after conversion.");

    }

    @Test
    void testEmpty() {
        EnumPropertyValue epv = new EnumPropertyValue();
        Integer comparison = EnumPropertyValueMapping.getEnumPropertyValueForComparison(epv);
        assertEquals(comparison, Integer.valueOf(99), "Empty enum ordinal value is expected to default to 99.");
    }

    @Test
    void testInstanceProvenanceType() {

        InstanceProvenanceType ipt = EnumPropertyValueMapping.getInstanceProvenanceTypeFromOrdinal(connector, 1);
        assertNotNull(ipt);
        int ordinal = EnumPropertyValueMapping.getOrdinalForInstanceProvenanceType(ipt);
        assertEquals(ordinal, 1, "InstanceProvenanceType ordinal is expected to be identical after conversion.");

        assertNull(EnumPropertyValueMapping.getInstanceProvenanceTypeFromOrdinal(connector, null));
        assertNull(EnumPropertyValueMapping.getOrdinalForInstanceProvenanceType(null));

    }

    @Test
    void testInstanceStatus() {

        InstanceStatus is = EnumPropertyValueMapping.getInstanceStatusFromOrdinal(connector, 1);
        assertNotNull(is);
        int ordinal = EnumPropertyValueMapping.getOrdinalForInstanceStatus(is);
        assertEquals(ordinal, 1, "InstanceStatus ordinal is expected to be identical after conversion.");

        assertNull(EnumPropertyValueMapping.getInstanceStatusFromOrdinal(connector, null));
        assertNull(EnumPropertyValueMapping.getOrdinalForInstanceStatus(null));

    }

}
