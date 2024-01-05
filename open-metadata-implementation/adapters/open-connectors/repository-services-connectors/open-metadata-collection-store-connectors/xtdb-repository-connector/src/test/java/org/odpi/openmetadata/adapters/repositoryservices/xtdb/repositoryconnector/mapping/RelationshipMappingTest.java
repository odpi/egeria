/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn.AddEntityProxy;
import xtdb.api.XtdbDocument;
import xtdb.api.IXtdbDatasource;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests the mappings of relationship objects.
 */
public class RelationshipMappingTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testValid() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            Relationship relationship = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "SemanticAssignment");

            InstanceProperties properties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "description",
                    "some description",
                    this.getClass().getName());
            relationship.setProperties(properties);

            EntityProxy one = getEntityProxy(helper, "Referenceable", "some-referenceable");
            EntityProxy two = getEntityProxy(helper, "GlossaryTerm", "some-glossary-term");

            AddEntityProxy.transact(connector, one);
            AddEntityProxy.transact(connector, two);

            relationship.setEntityOneProxy(one);
            relationship.setEntityTwoProxy(two);

            IXtdbDatasource db = connector.getXtdbAPI().db();

            RelationshipMapping egeria = new RelationshipMapping(connector, relationship);
            XtdbDocument doc = egeria.toXTDB();
            assertNotNull(doc);
            assertEquals(doc.getId(), RelationshipMapping.getReference(relationship.getGUID()), "XTDB document ID is expected to be identical to a prefixed GUID of the Egeria object.");

            RelationshipMapping xtdb = new RelationshipMapping(connector, doc, db);
            Relationship retrieved = xtdb.toEgeria();
            assertNotNull(retrieved);
            assertEquals(retrieved, relationship, "Relationship is expected to be identical after being retrieved back from conversion.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private EntityProxy getEntityProxy(OMRSRepositoryHelper helper,
                                       String type,
                                       String name) throws Exception {

        EntityDetail detail = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                MockConnection.METADATA_COLLECTION_ID,
                MockConnection.METADATA_COLLECTION_NAME,
                InstanceProvenanceType.LOCAL_COHORT,
                MockConnection.USERNAME,
                type);

        InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                null,
                "qualifiedName",
                name,
                this.getClass().getName());
        detail.setProperties(ip);

        return helper.getNewEntityProxy(MockConnection.SOURCE_NAME,
                detail);

    }

    @Test
    void testNull() {

        IXtdbDatasource db = connector.getXtdbAPI().db();

        RelationshipMapping egeria = new RelationshipMapping(connector, null);
        XtdbDocument doc = egeria.toXTDB();
        assertNull(doc);

        RelationshipMapping xtdb = new RelationshipMapping(connector, null, db);
        Relationship relationship = xtdb.toEgeria();
        assertNull(relationship);

    }

}
