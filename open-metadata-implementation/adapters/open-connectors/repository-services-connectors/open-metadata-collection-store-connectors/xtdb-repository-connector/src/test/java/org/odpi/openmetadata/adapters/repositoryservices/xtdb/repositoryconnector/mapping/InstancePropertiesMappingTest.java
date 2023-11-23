/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Test the mappings of multiple property values.
 */
public class InstancePropertiesMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    private static final String docId = InstancePropertiesMappingTest.class.getName();

    @Test
    void testValid() {
        try {

            String propertyName1 = "qualifiedName";
            String propertyValue1 = "a-qualified-name";
            String propertyName2 = "additionalProperties";
            Map<String, Object> propertyValue2 = new HashMap<>();
            propertyValue2.put("one", "a");
            propertyValue2.put("two", "b");

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            InstanceProperties properties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName1,
                    propertyValue1,
                    this.getClass().getName());

            properties = helper.addMapPropertyToInstance(MockConnection.SOURCE_NAME,
                    properties,
                    propertyName2,
                    propertyValue2,
                    this.getClass().getName());

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef typeDef = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Referenceable");
            InstanceType type = helper.getNewInstanceType(MockConnection.SOURCE_NAME, typeDef);
            InstancePropertiesMapping.addToDoc(connector,
                    builder,
                    type,
                    properties);

            InstanceProperties retrieved = InstancePropertiesMapping.getFromDoc(connector,
                    type,
                    builder.build());

            assertEquals(retrieved, properties, "Stored instance properties are expected to be identical when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testEmpty() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            InstanceProperties properties = new InstanceProperties();

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef typeDef = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Referenceable");
            InstanceType type = helper.getNewInstanceType(MockConnection.SOURCE_NAME, typeDef);
            InstancePropertiesMapping.addToDoc(connector,
                    builder,
                    type,
                    properties);

            InstanceProperties retrieved = InstancePropertiesMapping.getFromDoc(connector,
                    type,
                    builder.build());

            assertEquals(retrieved, properties, "Empty instance properties are expected to be empty when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNull() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef typeDef = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Referenceable");
            InstanceType type = helper.getNewInstanceType(MockConnection.SOURCE_NAME, typeDef);
            InstancePropertiesMapping.addToDoc(connector,
                    builder,
                    type,
                    null);

            InstanceProperties retrieved = InstancePropertiesMapping.getFromDoc(connector,
                    type,
                    builder.build());

            assertEquals(retrieved, new InstanceProperties(), "Null instance properties are expected to be empty when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
