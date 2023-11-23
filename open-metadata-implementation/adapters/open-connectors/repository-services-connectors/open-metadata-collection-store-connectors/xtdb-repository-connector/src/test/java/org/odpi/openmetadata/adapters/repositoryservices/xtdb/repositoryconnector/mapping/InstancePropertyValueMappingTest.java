/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Test the mappings of various property values.
 */
public class InstancePropertyValueMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    private static final String docId = InstancePropertyValueMappingTest.class.getName();

    @Test
    void testPrimitive() {
        try {

            String propertyName = "startDate";
            String namespace = EntityDetailMapping.ENTITY_PROPERTIES_NS;

            Date now = new Date();
            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            InstanceProperties properties = helper.addDatePropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    now,
                    this.getClass().getName());

            InstancePropertyValue ipv = properties.getPropertyValue(propertyName);
            Object comparison = InstancePropertyValueMapping.getValueForComparison(connector, ipv);

            assertTrue(comparison instanceof Date);
            assertEquals(comparison, now, "Dates are expected to be identical after conversion.");

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef type = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Project");
            InstancePropertyValueMapping.addInstancePropertyValueToDoc(connector,
                    helper.getNewInstanceType(MockConnection.SOURCE_NAME, type),
                    builder,
                    propertyName,
                    ipv);

            InstancePropertyValue retrieved = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(connector,
                    builder.build(),
                    namespace,
                    propertyName);

            assertEquals(retrieved, ipv, "Stored primitive values are expected to be identical when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testEnum() {
        try {

            String propertyName = "mediaType";
            String namespace = EntityDetailMapping.ENTITY_PROPERTIES_NS;

            int value = 3;

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            InstanceProperties properties = helper.addEnumPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    value,
                    "Video",
                    "The media is a video recording.",
                    this.getClass().getName());

            InstancePropertyValue ipv = properties.getPropertyValue(propertyName);
            Object comparison = InstancePropertyValueMapping.getValueForComparison(connector, ipv);

            assertTrue(comparison instanceof Integer);
            assertEquals(comparison, value, "Enum ordinals are expected to be identical after conversion.");

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef type = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "RelatedMedia");
            InstancePropertyValueMapping.addInstancePropertyValueToDoc(connector,
                    helper.getNewInstanceType(MockConnection.SOURCE_NAME, type),
                    builder,
                    propertyName,
                    ipv);

            InstancePropertyValue retrieved = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(connector,
                    builder.build(),
                    namespace,
                    propertyName);

            assertEquals(retrieved, ipv, "Stored enum values are expected to be identical when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testArray() {
        try {

            String propertyName = "minutes";
            String namespace = EntityDetailMapping.ENTITY_PROPERTIES_NS;

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
                        propertyName,
                        value,
                        this.getClass().getName());
                InstancePropertyValue ipv = properties.getPropertyValue(propertyName);
                apv.setArrayValue(i, ipv);
            }

            Object comparison = InstancePropertyValueMapping.getValueForComparison(connector, apv);
            assertTrue(comparison instanceof List);
            assertEquals(comparison, values, "Array values are expected to be identical after conversion.");

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef type = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Meeting");
            InstancePropertyValueMapping.addInstancePropertyValueToDoc(connector,
                    helper.getNewInstanceType(MockConnection.SOURCE_NAME, type),
                    builder,
                    propertyName,
                    apv);

            InstancePropertyValue retrieved = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(connector,
                    builder.build(),
                    namespace,
                    propertyName);

            assertEquals(retrieved, apv, "Stored array values are expected to be identical when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testMap() {
        try {

            String propertyName = "additionalProperties";
            String namespace = EntityDetailMapping.ENTITY_PROPERTIES_NS;

            Map<String, Object> map = new HashMap<>();
            map.put("one", 1);
            map.put("two", 2);

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            MapPropertyValue mpv = new MapPropertyValue();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = (Integer) entry.getValue();
                InstanceProperties properties = helper.addIntPropertyToInstance(this.getClass().getName(),
                        null,
                        propertyName,
                        value,
                        this.getClass().getName());
                InstancePropertyValue ipv = properties.getPropertyValue(propertyName);
                mpv.setMapValue(key, ipv);
            }

            Object comparison = InstancePropertyValueMapping.getValueForComparison(connector, mpv);
            assertTrue(comparison instanceof Map);
            assertEquals(comparison, map, "Map values are expected to be identical after conversion.");

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef type = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Referenceable");
            InstancePropertyValueMapping.addInstancePropertyValueToDoc(connector,
                    helper.getNewInstanceType(MockConnection.SOURCE_NAME, type),
                    builder,
                    propertyName,
                    mpv);

            InstancePropertyValue retrieved = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(connector,
                    builder.build(),
                    namespace,
                    propertyName);

            assertEquals(retrieved, mpv, "Stored map values are expected to be identical when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testStruct() {
        try {

            Map<String, Object> map = new HashMap<>();
            map.put("one", 1);
            map.put("two", 2);

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            StructPropertyValue spv = new StructPropertyValue();
            InstanceProperties properties = new InstanceProperties();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = (Integer) entry.getValue();
                properties = helper.addIntPropertyToInstance(this.getClass().getName(),
                        properties,
                        key,
                        value,
                        this.getClass().getName());
            }
            spv.setAttributes(properties);

            Object comparison = InstancePropertyValueMapping.getValueForComparison(connector, spv);
            assertTrue(comparison instanceof Map);
            assertEquals(comparison, map, "Struct values are expected to be identical after conversion.");

            // TODO: currently no examples of Struct properties on any type definition, so no further tests
            //  to realistically carry out

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNull() {
        try {

            String propertyName = "qualifiedName";
            String namespace = EntityDetailMapping.ENTITY_PROPERTIES_NS;

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            XtdbDocument.Builder builder = XtdbDocument.builder(docId);
            TypeDef type = helper.getTypeDefByName(MockConnection.SOURCE_NAME, "Referenceable");
            InstancePropertyValueMapping.addInstancePropertyValueToDoc(connector,
                    helper.getNewInstanceType(MockConnection.SOURCE_NAME, type),
                    builder,
                    propertyName,
                    null);

            InstancePropertyValue retrieved = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(connector,
                    builder.build(),
                    namespace,
                    propertyName);

            assertNull(retrieved, "Stored null values are expected to be null when retrieved back again.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
