/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Test the mappings of various property values.
 */
public class ArrayPropertyValueMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testStrings() {

        List<String> values = new ArrayList<>();
        values.add("one");
        values.add("two");
        OMRSRepositoryHelper helper = connector.getRepositoryHelper();
        ArrayPropertyValue apv = new ArrayPropertyValue();
        apv.setArrayCount(values.size());
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            InstanceProperties properties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "property",
                    value,
                    this.getClass().getName());
            InstancePropertyValue ipv = properties.getPropertyValue("property");
            apv.setArrayValue(i, ipv);
        }
        List<Object> comparison = ArrayPropertyValueMapping.getArrayPropertyValueForComparison(connector, apv);
        assertEquals(comparison, values, "Array property value is expected to match the original List after conversion.");

    }

    @Test
    void testEmpty() {
        ArrayPropertyValue apv = new ArrayPropertyValue();
        List<Object> comparison = ArrayPropertyValueMapping.getArrayPropertyValueForComparison(connector, apv);
        assertNull(comparison, "Empty array property value is expected to be null after conversion.");
    }

}
