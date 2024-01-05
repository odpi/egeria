/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test the mappings of instance headers.
 */
public class InstanceHeaderMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testGuidReference() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            EntityDetail entity = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Referenceable");
            String docId = InstanceHeaderMapping.getGuidReference(connector, entity);
            assertNotNull(docId);
            assertTrue(docId.startsWith(EntitySummaryMapping.INSTANCE_REF_PREFIX));

            Relationship relationship = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "SemanticAssignment");
            docId = InstanceHeaderMapping.getGuidReference(connector, relationship);
            assertNotNull(docId);
            assertTrue(docId.startsWith(RelationshipMapping.INSTANCE_REF_PREFIX));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testGuidFromReference() {

        String guid = "123456";
        String reference = "e_" + guid;
        assertEquals(InstanceHeaderMapping.trimGuidFromReference(reference), guid, "GUID is expected to be the reference without a prefix.");

    }

}
