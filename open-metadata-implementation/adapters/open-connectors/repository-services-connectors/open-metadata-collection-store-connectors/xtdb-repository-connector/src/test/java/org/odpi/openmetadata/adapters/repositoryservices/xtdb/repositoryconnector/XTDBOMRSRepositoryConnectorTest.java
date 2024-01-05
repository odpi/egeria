/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn.*;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.HomeEntityException;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogDestination;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Tests the repository connector itself.
 */
public class XTDBOMRSRepositoryConnectorTest
{

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testStart() {

        Map<String, String> luceneConfig = new HashMap<>();
        try {
            Path dir = Files.createTempDirectory("lucene");
            luceneConfig.put("db-dir", dir.toString());
        } catch (IOException e) {
            e.printStackTrace();
            assertNull(e, "Unable to create temporary directory for connector config.");
        }

        Map<String, Object> xtdbConfig = new HashMap<>();
        xtdbConfig.put(Constants.XTDB_LUCENE, luceneConfig);

        Map<String, Object> config = new HashMap<>();
        config.put(XTDBOMRSRepositoryConnectorProvider.XTDB_CONFIG, xtdbConfig);
        config.put(XTDBOMRSRepositoryConnectorProvider.LUCENE_REGEXES, true);
        config.put(XTDBOMRSRepositoryConnectorProvider.SYNCHRONOUS_INDEX, true);

        Connection tmpConnection = new Connection();
        tmpConnection.setDisplayName("Mock XTDB Connection");
        tmpConnection.setDescription("A pretend XTDB connection.");
        ConnectorType connectorType = new ConnectorType();
        connectorType.setConnectorProviderClassName("org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnectorProvider");
        tmpConnection.setConnectorType(connectorType);
        tmpConnection.setConfigurationProperties(config);

        ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();
        ConnectorBroker broker = new ConnectorBroker();

        Connector auditLogConnector = null;
        try {
            auditLogConnector = broker.getConnector(connectorConfigurationFactory.getDefaultAuditLogConnection());
            auditLogConnector.start();
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            e.printStackTrace();
            assertNull(e, "Unable to get or start audit log via the broker.");
        }

        List<OMRSAuditLogStore> auditLogDestinations = new ArrayList<>();
        auditLogDestinations.add((OMRSAuditLogStore)auditLogConnector);
        OMRSAuditLogDestination destination = new OMRSAuditLogDestination("ConnectorTest", "XTDB", "ODPi", auditLogDestinations);
        OMRSAuditLog auditLog = new OMRSAuditLog(destination, -1, ComponentDevelopmentStatus.SAMPLE, "ConnectorTest", "Testing of the connector", null);
        OMRSRepositoryContentManager contentManager = new OMRSRepositoryContentManager(MockConnection.USERNAME, auditLog);

        try {
            Object candidate = broker.getConnector(tmpConnection);
            assertTrue(candidate instanceof XTDBOMRSRepositoryConnector);
            XTDBOMRSRepositoryConnector tmp = (XTDBOMRSRepositoryConnector) candidate;
            tmp.setAuditLog(auditLog);
            tmp.setRepositoryHelper(new OMRSRepositoryContentHelper(contentManager));
            tmp.setRepositoryValidator(new OMRSRepositoryContentValidator(contentManager));
            tmp.setMetadataCollectionId(MockConnection.METADATA_COLLECTION_ID);
            tmp.setMetadataCollectionName(MockConnection.METADATA_COLLECTION_NAME);
            tmp.start();
            tmp.disconnect();
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            e.printStackTrace();
            assertNull(e, "Unable to get connector via the broker.");
        }

    }

    @Test
    void testDates() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            EntityDetail original = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "GlossaryTerm");

            Date now = new Date();
            original.setUpdateTime(now);

            assertEquals(original.getUpdateTime(), now, "Expected retrieving the same property should give identical results.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testEntityLifecycle() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            EntityDetail original = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "GlossaryTerm");

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-term",
                    this.getClass().getName());
            original.setProperties(ip);

            InstanceProperties classificationProperties = helper.addIntPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "level",
                    3,
                    this.getClass().getName());

            Classification classification = helper.getNewClassification(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Confidentiality",
                    "GlossaryTerm",
                    ClassificationOrigin.ASSIGNED,
                    null,
                    classificationProperties);
            List<Classification> classifications = new ArrayList<>();
            classifications.add(classification);
            original.setClassifications(classifications);

            // Create
            EntityDetail result = AddEntity.transact(connector, original);
            assertEquals(result, original, "Expected resulting entity to be identical to sent entity.");

            // Read
            EntityDetail retrieved = new GetEntity(connector, original.getGUID(), null).asDetail();
            assertEquals(retrieved, original, "Expected retrieved entity to be identical to the entity sent to be stored.");

            EntityProxy proxy = new GetEntity(connector, original.getGUID(), null).asProxy();
            EntityProxy asProxy = helper.getNewEntityProxy(MockConnection.SOURCE_NAME, retrieved);
            assertEquals(proxy, asProxy, "Expected entity, when retrieved as a proxy, to be identical to the proxy representation of the original entity.");

            EntitySummary summary = new GetEntity(connector, original.getGUID(), null).asSummary();
            EntitySummary asSummary = new EntitySummary(retrieved);
            assertEquals(summary, asSummary, "Expected entity, when retrieved as a summary, to be identical to the summary representation of the original entity.");

            // Update
            InstanceProperties ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "displayName",
                    "some-term",
                    this.getClass().getName());
            ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    ip2,
                    "qualifiedName",
                    "some-term",
                    this.getClass().getName());

            EntityDetail updated = UpdateEntityProperties.transact(connector, MockConnection.USERNAME, retrieved.getGUID(), ip2);
            assertNotNull(updated, "Expected entity resulting from the update to be non-null.");
            assertNotNull(updated.getProperties(), "Expected updated entity to have properties.");
            assertEquals(updated.getProperties().getInstanceProperties().get("displayName").valueAsString(), "some-term", "Expected resulting entity from update to have the updated displayName.");

            retrieved = new GetEntity(connector, original.getGUID(), null).asDetail();
            assertEquals(retrieved, updated, "Expected retrieved updated entity to be identical to the updated entity sent to be stored.");

            List<EntityDetail> previousVersions = new GetEntityHistory(connector, original.getGUID(), null, null, 0, 100, HistorySequencingOrder.BACKWARDS).execute();
            assertNotNull(previousVersions, "Expected there to be some previous versions of the entity.");
            assertEquals(previousVersions.size(), 2, "Two previous versions of the entity were expected.");

            // Restore
            EntityDetail previous = UndoEntityUpdate.transact(connector, MockConnection.USERNAME, original.getGUID());
            EntityDetail rolledBack = new EntityDetail(original);
            rolledBack.setVersion(3L);
            rolledBack.setUpdatedBy(MockConnection.USERNAME);
            rolledBack.setUpdateTime(previous.getUpdateTime());
            List<String> maintainers = new ArrayList<>();
            maintainers.add(MockConnection.USERNAME);
            rolledBack.setMaintainedBy(maintainers);
            assertEquals(previous, rolledBack, "Expected the previous version of the entity to be identical to the original version (except for versions and modification details).");

            retrieved = new GetEntity(connector, original.getGUID(), null).asDetail();
            assertEquals(retrieved, previous, "Expected retrieved restored entity to be identical to the restored entity that was returned.");

            // Purge
            PurgeEntity.transactWithoutValidation(connector, original.getGUID());

            retrieved = new GetEntity(connector, original.getGUID(), null).asDetail();
            assertNull(retrieved, "Expected the entity to no longer exist after it has been purged.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testRelationshipLifecycle() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            Relationship original = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "SemanticAssignment");

            InstanceProperties assetProperties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-asset",
                    this.getClass().getName());

            EntityProxy asset = helper.getNewEntityProxy(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Asset",
                    assetProperties,
                    null);

            AddEntityProxy.transact(connector, asset);

            InstanceProperties termProperties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-term",
                    this.getClass().getName());

            EntityProxy term = helper.getNewEntityProxy(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "GlossaryTerm",
                    termProperties,
                    null);

            AddEntityProxy.transact(connector, term);

            original.setEntityOneProxy(asset);
            original.setEntityTwoProxy(term);

            InstanceProperties relationshipProperties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "steward",
                    MockConnection.USERNAME,
                    this.getClass().getName());

            original.setProperties(relationshipProperties);

            // Create
            Relationship result = AddRelationship.transact(connector, original, original.getEntityOneProxy().getGUID(), original.getEntityTwoProxy().getGUID());
            assertEquals(result, original, "Expected resulting relationship to be identical to sent relationship.");

            // Read
            Relationship retrieved = new GetRelationship(connector, original.getGUID(), null).execute();
            assertEquals(retrieved, original, "Expected retrieved relationship to be identical to the relationship sent to be stored.");

            // Update
            InstanceProperties ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "steward",
                    MockConnection.USERNAME,
                    this.getClass().getName());
            ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    ip2,
                    "description",
                    "some-description",
                    this.getClass().getName());

            Relationship updated = UpdateRelationshipProperties.transact(connector, MockConnection.USERNAME, retrieved.getGUID(), ip2);
            assertNotNull(updated, "Expected entity resulting from the update to be non-null.");
            assertNotNull(updated.getProperties(), "Expected updated entity to have properties.");
            assertEquals(updated.getProperties().getInstanceProperties().get("description").valueAsString(), "some-description", "Expected resulting entity from update to have the updated displayName.");

            retrieved = new GetRelationship(connector, original.getGUID(), null).execute();
            assertEquals(retrieved, updated, "Expected retrieved updated relationship to be identical to the updated relationship sent to be stored.");

            List<Relationship> previousVersions = new GetRelationshipHistory(connector, original.getGUID(), null, null, 0, 100, HistorySequencingOrder.BACKWARDS).execute();
            assertNotNull(previousVersions, "Expected there to be some previous versions of the relationship.");
            assertEquals(previousVersions.size(), 2, "Two previous versions of the relationship were expected.");

            // Restore
            Relationship previous = UndoRelationshipUpdate.transact(connector, MockConnection.USERNAME, original.getGUID());
            Relationship rolledBack = new Relationship(original);
            rolledBack.setVersion(3L);
            rolledBack.setUpdatedBy(MockConnection.USERNAME);
            rolledBack.setUpdateTime(previous.getUpdateTime());
            List<String> maintainers = new ArrayList<>();
            maintainers.add(MockConnection.USERNAME);
            rolledBack.setMaintainedBy(maintainers);
            assertEquals(previous, rolledBack, "Expected the previous version of the relationship to be identical to the original version (except for versions and modification details).");

            retrieved = new GetRelationship(connector, original.getGUID(), null).execute();
            assertEquals(retrieved, previous, "Expected retrieved restored relationship to be identical to the restored relationship that was returned.");

            // Purge
            PurgeRelationship.transactWithoutValidation(connector, original.getGUID());

            retrieved = new GetRelationship(connector, original.getGUID(), null).execute();
            assertNull(retrieved, "Expected the relationship to no longer exist after it has been purged.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testEntityFind() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            EntityDetail profile1 = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "ActorProfile");

            InstanceProperties ip1 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-profile-1",
                    this.getClass().getName());
            profile1.setProperties(ip1);

            EntityDetail result = AddEntity.transact(connector, profile1);
            assertNotNull(result, "Expected an ActorProfile to be created.");

            EntityDetail profile2 = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "ActorProfile");

            InstanceProperties ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-profile-2",
                    this.getClass().getName());
            profile2.setProperties(ip2);

            result = AddEntity.transact(connector, profile2);
            assertNotNull(result, "Expected an ActorProfile to be created.");

            List<EntityDetail> results = new FindEntities(connector,
                    "5a2f38dc-d69d-4a6f-ad26-ac86f118fa35",
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertEquals(results.size(), 2, "Expected precisely two search results.");

            results = new FindEntities(connector,
                    null,
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertTrue(results.size() >= 2, "Expected at least two search results.");

            results = new FindEntitiesByPropertyValue(connector,
                    "5a2f38dc-d69d-4a6f-ad26-ac86f118fa35",
                    helper.getStartsWithRegex("some"),
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertEquals(results.size(), 2, "Expected precisely two search results.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testRelationshipFind() {
        try {

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            EntityDetail category1 = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "GlossaryCategory");

            InstanceProperties ip1 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-category-1",
                    this.getClass().getName());
            category1.setProperties(ip1);

            EntityDetail result = AddEntity.transact(connector, category1);
            assertNotNull(result, "Expected a category to be created.");

            EntityDetail category2 = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "GlossaryCategory");

            InstanceProperties ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-category-2",
                    this.getClass().getName());
            category2.setProperties(ip2);

            result = AddEntity.transact(connector, category2);
            assertNotNull(result, "Expected a category to be created.");

            EntityDetail glossary = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Glossary");

            InstanceProperties ip3 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-glossary",
                    this.getClass().getName());
            glossary.setProperties(ip3);

            result = AddEntity.transact(connector, glossary);
            assertNotNull(result, "Expected a glossary to be created.");

            Relationship one = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "CategoryAnchor");

            one.setEntityOneProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, glossary));
            one.setEntityTwoProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, category1));

            Relationship first = AddRelationship.transact(connector, one, one.getEntityOneProxy().getGUID(), one.getEntityTwoProxy().getGUID());
            assertNotNull(first, "Expected a CategoryAnchor relationship to be created.");

            Relationship two = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "CategoryHierarchyLink");

            two.setEntityOneProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, category1));
            two.setEntityTwoProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, category2));

            Relationship second = AddRelationship.transact(connector, two, two.getEntityOneProxy().getGUID(), two.getEntityTwoProxy().getGUID());
            assertNotNull(second, "Expected a CategoryHierarchyLink relationship to be created.");

            Relationship three = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "CategoryHierarchyLink");

            three.setEntityOneProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, category2));
            three.setEntityTwoProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, category1));
            three.setStatus(InstanceStatus.DELETED);

            Relationship third = AddRelationship.transact(connector, three, three.getEntityOneProxy().getGUID(), three.getEntityTwoProxy().getGUID());
            assertNotNull(third, "Expected a CategoryHierarchyLink relationship to be created.");
            Relationship retrieved = new GetRelationship(connector, three.getGUID(), null).execute();
            assertNotNull(retrieved, "Expected to be able to retrieve the relationship back again.");

            List<Relationship> results = new GetRelationshipsForEntity(connector,
                    category1.getGUID(),
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertEquals(results.size(), 2, "Expected precisely two search results.");

            results = new GetRelationshipsForEntity(connector,
                    category1.getGUID(),
                    "71e4b6fb-3412-4193-aff3-a16eccd87e8e",
                    0,
                    null,
                    null,
                    null,
                    null,
                    100,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");

            List<String> typeGuids = new ArrayList<>();
            typeGuids.add("71e4b6fb-3412-4193-aff3-a16eccd87e8e");
            typeGuids.add("c628938e-815e-47db-8d1c-59bb2e84e028");
            results = new FindRelationships(connector,
                    null,
                    typeGuids,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    0,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertEquals(results.size(), 3, "Expected precisely three search results.");

            results = new FindRelationships(connector,
                    "c628938e-815e-47db-8d1c-59bb2e84e028",
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    0,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertEquals(results.size(), 2, "Expected precisely two search results.");

            results = new FindRelationships(connector,
                    null,
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    0,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected some search results.");
            assertTrue(results.size() >= 3, "Expected at least three search results.");

            // Note that this should not return any results, as there are no properties (let alone
            // any string properties) on the CategoryAnchor relationship type being searched.
            results = new FindRelationshipsByPropertyValue(connector,
                    "c628938e-815e-47db-8d1c-59bb2e84e028",
                    helper.getStartsWithRegex("some"),
                    0,
                    null,
                    null,
                    null,
                    null,
                    100,
                    MockConnection.USERNAME).getResults();
            assertNotNull(results, "Expected non-null search results.");
            assertEquals(results.size(), 0, "Expected precisely zero search results.");

            InstanceGraph graph = new GetEntityNeighborhood(connector,
                    glossary.getGUID(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    5).execute();
            assertNotNull(graph, "Expected some graph query results.");
            assertEquals(graph.getRelationships().size(), 2, "Expected precisely two relationship results.");
            assertEquals(graph.getEntities().size(), 3, "Expected precisely three entity results.");

            graph = new GetLinkingEntities(connector,
                    glossary.getGUID(),
                    category2.getGUID(),
                    null,
                    null).execute();
            assertNotNull(graph, "Expected some graph query results.");
            assertEquals(graph.getRelationships().size(), 2, "Expected precisely two relationship results.");
            assertEquals(graph.getEntities().size(), 3, "Expected precisely three entity results.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testReferenceCopies() {
        try {

            String externalMetadataCollectionId = "999";
            String externalMetadataCollectionName = "Imaginary external metadata collection";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            EntityDetail entityRC = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "ActorProfile");

            InstanceProperties ip1 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "external-profile-X",
                    this.getClass().getName());
            entityRC.setProperties(ip1);

            SaveEntityReferenceCopy.transact(connector, entityRC);

            EntityDetail retrieved = new GetEntity(connector, entityRC.getGUID(), null).asDetail();
            assertEquals(retrieved, entityRC, "Expected retrieved reference copy to be identical to saved reference copy.");

            EntityDetail entityRCUpdate = new EntityDetail(entityRC);
            entityRCUpdate.setVersion(2L);
            entityRCUpdate.setUpdateTime(new Date());
            entityRCUpdate.setUpdatedBy(MockConnection.USERNAME);

            SaveEntityReferenceCopy.transact(connector, entityRCUpdate);

            retrieved = new GetEntity(connector, entityRC.getGUID(), null).asDetail();
            assertEquals(retrieved, entityRCUpdate, "Expected retrieved reference copy to be identical to saved reference copy after update.");

            EntityDetail entityInvalid = new EntityDetail(entityRCUpdate);
            entityInvalid.setVersion(3L);
            entityInvalid.setMetadataCollectionId(MockConnection.METADATA_COLLECTION_ID);
            entityInvalid.setMetadataCollectionName(MockConnection.METADATA_COLLECTION_NAME);

            assertThrows(HomeEntityException.class, () -> SaveEntityReferenceCopy.transact(connector, entityInvalid));

            EntityDetail category1 = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "GlossaryCategory");

            InstanceProperties ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-category-1",
                    this.getClass().getName());
            category1.setProperties(ip2);

            EntityDetail glossary = helper.getSkeletonEntity(MockConnection.SOURCE_NAME,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "Glossary");

            InstanceProperties ip3 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    "qualifiedName",
                    "some-glossary",
                    this.getClass().getName());
            glossary.setProperties(ip3);

            Relationship relationshipRC = helper.getSkeletonRelationship(MockConnection.SOURCE_NAME,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    MockConnection.USERNAME,
                    "CategoryAnchor");

            relationshipRC.setEntityOneProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, glossary));
            relationshipRC.setEntityTwoProxy(helper.getNewEntityProxy(MockConnection.SOURCE_NAME, category1));

            SaveRelationshipReferenceCopy.transact(connector, relationshipRC);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
