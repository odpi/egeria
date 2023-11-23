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
 * Tests the mappings of entity summary objects.
 */
public class EntitySummaryMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testValid() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            EntitySummary summary = helper.getSkeletonEntitySummary(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Referenceable");

            InstanceProperties properties = helper.addIntPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "level",
                    3,
                    this.getClass().getName());
            Classification classification = helper.getNewClassification(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Confidentiality",
                    "Referenceable",
                    ClassificationOrigin.ASSIGNED,
                    null,
                    properties);
            List<Classification> classifications = new ArrayList<>();
            classifications.add(classification);
            summary.setClassifications(classifications);

            EntitySummaryMapping egeria = new EntitySummaryMapping(connector, summary);
            XtdbDocument doc = egeria.toXTDB();
            assertNotNull(doc);
            assertEquals(doc.getId(), EntitySummaryMapping.getReference(summary.getGUID()), "XTDB document ID is expected to be identical to a prefixed GUID of the Egeria object.");

            EntitySummaryMapping xtdb = new EntitySummaryMapping(connector, doc);
            EntitySummary retrieved = xtdb.toEgeria();
            assertNotNull(retrieved);
            assertEquals(retrieved, summary, "EntitySummary is expected to be identical after being retrieved back from conversion.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNull() {

        EntitySummaryMapping egeria = new EntitySummaryMapping(connector, (EntitySummary) null);
        XtdbDocument doc = egeria.toXTDB();
        assertNull(doc);

        EntitySummaryMapping xtdb = new EntitySummaryMapping(connector, (XtdbDocument) null);
        EntitySummary summary = xtdb.toEgeria();
        assertNull(summary);

    }

}
