/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.mockito.invocation.InvocationOnMock;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Verify how getRelationshipsForEntity() decides whether a stored relationship is of the requested type.
 * <br>
 * The decision has to be delegated to the repository validator, because a request for a supertype must
 * return instances of its subtypes.  This store used to compare the requested type GUID to the stored
 * relationship's own type GUID with equals(), which returns instances of exactly one type and silently
 * drops every subtype - so a caller asking for ResourceConnection would never see an AssetConnection.
 * Every other search in this store already delegates; this one did not, and nothing covered it: the
 * conformance suite only ever calls this method with a null type GUID.
 */
public class GetRelationshipsForEntityTypeFilterTest
{
    private static final String USER_ID           = "testUser";
    private static final String REPOSITORY_NAME   = "testRepository";
    private static final String COLLECTION_ID     = "testMetadataCollection";
    private static final String ENTITY_GUID       = "8a7f4a4e-4a05-4a4a-8a51-4d3cbbd0f1a1";
    private static final String RELATIONSHIP_GUID = "3b6d1c2f-9e0a-4a1b-8f2c-6d5e4c3b2a19";

    private static final String SUPERTYPE_GUID = "b5ec1608-7415-4b06-b9fb-7a10591a3cd1";
    private static final String SUBTYPE_GUID   = "e777d660-8dbe-453e-8b83-903771f054c0";
    private static final String OTHER_GUID     = "11112222-3333-4444-5555-666677778888";

    private OMRSRepositoryHelper          repositoryHelper;
    private OMRSRepositoryValidator       repositoryValidator;
    private InMemoryOMRSMetadataCollection metadataCollection;


    /**
     * Build a metadata collection holding one relationship of the subtype, linked to the test entity.
     * The helper and validator are stubbed so that the only decision under test is the type filter.
     *
     * @throws Exception unexpected
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        repositoryHelper    = mock(OMRSRepositoryHelper.class);
        repositoryValidator = mock(OMRSRepositoryValidator.class);

        InMemoryOMRSRepositoryConnector parentConnector = mock(InMemoryOMRSRepositoryConnector.class);

        metadataCollection = new InMemoryOMRSMetadataCollection(parentConnector,
                                                                REPOSITORY_NAME,
                                                                repositoryHelper,
                                                                repositoryValidator,
                                                                COLLECTION_ID);

        /*
         * The relationship is attached to the entity, so the only thing left to decide is its type.
         */
        when(repositoryHelper.relatedEntity(anyString(), anyString(), any(Relationship.class))).thenReturn(true);

        /*
         * Paging and sequencing are not under test - hand back whatever survived the filter.
         */
        when(repositoryHelper.formatRelationshipResults(any(), anyInt(), any(), any(), anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        metadataCollection.saveRelationshipReferenceCopy(USER_ID, getSubtypeRelationship());
    }


    /**
     * Return a relationship of the subtype, linking the test entity to another.
     *
     * @return relationship ready to store
     */
    private Relationship getSubtypeRelationship()
    {
        InstanceType instanceType = new InstanceType();

        instanceType.setTypeDefGUID(SUBTYPE_GUID);
        instanceType.setTypeDefName("AssetConnection");

        Relationship relationship = new Relationship();

        relationship.setGUID(RELATIONSHIP_GUID);
        relationship.setType(instanceType);
        relationship.setStatus(InstanceStatus.ACTIVE);
        relationship.setMetadataCollectionId(COLLECTION_ID);
        relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
        relationship.setEntityOneProxy(getProxy(ENTITY_GUID));
        relationship.setEntityTwoProxy(getProxy("f1f6c1c4-1d1e-4a2a-9b3c-2f5a6d7e8c90"));

        return relationship;
    }


    /**
     * Return a minimal entity proxy.
     *
     * @param guid unique identifier
     * @return proxy
     */
    private EntityProxy getProxy(String guid)
    {
        InstanceType instanceType = new InstanceType();

        instanceType.setTypeDefGUID("896d14c2-7522-4f6c-8519-757711943fe6");
        instanceType.setTypeDefName("DataSet");

        EntityProxy entityProxy = new EntityProxy();

        entityProxy.setGUID(guid);
        entityProxy.setType(instanceType);
        entityProxy.setStatus(InstanceStatus.ACTIVE);
        entityProxy.setMetadataCollectionId(COLLECTION_ID);
        entityProxy.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        return entityProxy;
    }


    /**
     * A request for the supertype must return the stored subtype instance.  This is the case that the
     * exact GUID comparison used to lose.
     *
     * @throws Exception unexpected
     */
    @Test
    public void requestForSupertypeReturnsSubtypeInstance() throws Exception
    {
        when(repositoryValidator.verifyInstanceType(eq(REPOSITORY_NAME), eq(SUPERTYPE_GUID), any(Relationship.class)))
                .thenReturn(true);

        List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(USER_ID,
                                                                                        ENTITY_GUID,
                                                                                        SUPERTYPE_GUID,
                                                                                        0,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        0);

        assertNotNull(relationships, "a subtype instance was dropped by a request for its supertype");
        assertEquals(relationships.size(), 1);
        assertEquals(relationships.get(0).getGUID(), RELATIONSHIP_GUID);
    }


    /**
     * A request for an unrelated type must still return nothing.
     *
     * @throws Exception unexpected
     */
    @Test
    public void requestForUnrelatedTypeReturnsNothing() throws Exception
    {
        when(repositoryValidator.verifyInstanceType(eq(REPOSITORY_NAME), eq(OTHER_GUID), any(Relationship.class)))
                .thenReturn(false);

        assertNull(metadataCollection.getRelationshipsForEntity(USER_ID,
                                                                ENTITY_GUID,
                                                                OTHER_GUID,
                                                                0,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                0),
                   "a relationship of an unrelated type was returned");
    }


    /**
     * A null type GUID must continue to match every relationship, without consulting the type system.
     *
     * @throws Exception unexpected
     */
    @Test
    public void requestWithNoTypeReturnsEverything() throws Exception
    {
        when(repositoryValidator.verifyInstanceType(eq(REPOSITORY_NAME), eq(null), any(Relationship.class)))
                .thenReturn(true);

        List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(USER_ID,
                                                                                        ENTITY_GUID,
                                                                                        null,
                                                                                        0,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        0);

        assertNotNull(relationships, "a null type GUID did not match every relationship");
        assertEquals(relationships.size(), 1);
    }
}
