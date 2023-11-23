/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests the mappings of entity detail objects.
 */
public class EntityDetailMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testValid() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            EntityDetail detail = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Referenceable");

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "a-qualified-name",
                    this.getClass().getName());
            detail.setProperties(ip);

            EntityDetailMapping egeria = new EntityDetailMapping(connector, detail);
            XtdbDocument doc = egeria.toXTDB();
            assertNotNull(doc);
            assertEquals(doc.getId(), EntityDetailMapping.getReference(detail.getGUID()), "XTDB document ID is expected to be identical to a prefixed GUID of the Egeria object.");

            EntityDetailMapping xtdb = new EntityDetailMapping(connector, doc);
            EntityDetail retrieved = xtdb.toEgeria();
            assertNotNull(retrieved);
            assertEquals(retrieved, detail, "EntityDetail is expected to be identical after being retrieved back from conversion.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNull() {

        EntityDetailMapping egeria = new EntityDetailMapping(connector, (EntityDetail) null);
        XtdbDocument doc = egeria.toXTDB();
        assertNull(doc);

        EntityDetailMapping xtdb = new EntityDetailMapping(connector, (XtdbDocument) null);
        EntityDetail detail = xtdb.toEgeria();
        assertNull(detail);

    }

}
