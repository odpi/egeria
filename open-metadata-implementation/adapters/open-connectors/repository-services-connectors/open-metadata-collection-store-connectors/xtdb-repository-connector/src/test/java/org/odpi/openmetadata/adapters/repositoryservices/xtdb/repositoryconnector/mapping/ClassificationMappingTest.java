/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Test the mappings of various property values.
 */
public class ClassificationMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testFromEgeria() {
        try {

            String guid = "abc";
            String propertyName = "source";
            String propertyValue = "here";
            String classificationName = "Confidentiality";
            XtdbDocument.Builder builder = XtdbDocument.builder(guid);
            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties properties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());

            Classification test = helper.getNewClassification(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    classificationName,
                    "Referenceable",
                    ClassificationOrigin.ASSIGNED,
                    null,
                    properties);

            List<Classification> classifications = new ArrayList<>();
            classifications.add(test);

            ClassificationMapping cm = new ClassificationMapping(connector,
                    classifications);
            cm.addToXtdbDoc(builder);

            XtdbDocument doc = builder.build();

            Object candidate = doc.get(EntitySummaryMapping.N_CLASSIFICATIONS);
            assertTrue(candidate instanceof List);
            List<?> classificationNames = (List<?>) candidate;
            assertEquals(classificationNames.size(), 1);
            assertEquals(classificationNames.get(0), classificationName);

            String namespaceForClassification = ClassificationMapping.getNamespaceForClassification(EntitySummaryMapping.N_CLASSIFICATIONS, classificationName);
            String propertiesNamespace = ClassificationMapping.getNamespaceForProperties(namespaceForClassification);
            InstancePropertyValue retrieved = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(connector, doc, propertiesNamespace, propertyName);
            assertNotNull(retrieved);
            assertEquals(retrieved, properties.getPropertyValue(propertyName));

            candidate = InstancePropertyValueMapping.getValueForComparison(connector, retrieved);
            assertTrue(candidate instanceof String);
            assertEquals(candidate, propertyValue);

            String retrievedName = ClassificationMapping.getClassificationNameFromNamespace(EntitySummaryMapping.N_CLASSIFICATIONS, namespaceForClassification);
            assertEquals(retrievedName, classificationName, "Expected parsed classification name to be identical to original classification name.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testClassificationOrigin() {
        assertNull(ClassificationMapping.getClassificationOriginFromSymbolicName(connector, "non-existent"), "Expected a non-existent classification origin to return null.");
    }

}
