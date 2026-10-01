/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Verify that a classification from an open metadata archive ends up on the entity it classifies.
 * <br>
 * When the archive manager loads a content pack it saves each entity as a reference copy, and then hands each
 * classification extension to the local repository through saveClassificationReferenceCopy() - the entity proxy
 * variant, since a classification extension carries a proxy of the entity it is attached to.  A classification added to an existing entity
 * in a content pack (for example Promise, or Template) was not found on the entity when read back from a running
 * platform.  These tests store an entity the way the loader does and read it back after each variant of the call.
 * <br>
 * The repository helper is stubbed to behave like the real one in the way that matters here: addClassificationToEntity()
 * returns an updated copy and leaves the entity it was given unchanged.
 */
public class ArchiveClassificationReferenceCopyTest
{
    private static final String USER_ID         = "testUser";
    private static final String REPOSITORY_NAME = "testRepository";
    private static final String COLLECTION_ID   = "c9f5e7a4-1c3d-4b2a-9e8f-7d6c5b4a3210";
    private static final String ARCHIVE_ID      = "09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String ENTITY_GUID     = "eea4d48e-da9b-45b3-9c9a-9a8d8d73062b";

    private InMemoryOMRSMetadataCollection metadataCollection;


    /**
     * Build a metadata collection with a real repository helper for the classification handling and a stub
     * validator, then store the classified entity as an archive reference copy.
     *
     * @throws Exception unexpected
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        InMemoryOMRSRepositoryConnector parentConnector = mock(InMemoryOMRSRepositoryConnector.class);
        OMRSRepositoryHelper            repositoryHelper = mock(OMRSRepositoryHelper.class);

        when(repositoryHelper.addClassificationToEntity(anyString(), any(EntityDetail.class), any(Classification.class), anyString()))
                .thenAnswer(invocation -> {
                    EntityDetail updated = new EntityDetail((EntityDetail) invocation.getArgument(1));

                    updated.setClassifications(this.addToList(updated.getClassifications(), invocation.getArgument(2)));

                    return updated;
                });
        when(repositoryHelper.addClassificationToEntity(anyString(), any(EntityProxy.class), any(Classification.class), anyString()))
                .thenAnswer(invocation -> {
                    EntityProxy updated = new EntityProxy((EntityProxy) invocation.getArgument(1));

                    updated.setClassifications(this.addToList(updated.getClassifications(), invocation.getArgument(2)));

                    return updated;
                });
        when(repositoryHelper.getNewEntityProxy(anyString(), any(EntityDetail.class)))
                .thenAnswer(invocation -> {
                    EntityProxy proxy = new EntityProxy();

                    this.fill(proxy);

                    return proxy;
                });

        metadataCollection = new InMemoryOMRSMetadataCollection(parentConnector,
                                                                REPOSITORY_NAME,
                                                                repositoryHelper,
                                                                mock(OMRSRepositoryValidator.class),
                                                                COLLECTION_ID);

        metadataCollection.saveEntityReferenceCopy(USER_ID, getEntity());
    }


    /**
     * The archive manager's route: the classification is saved through the entity proxy variant.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testClassificationSavedThroughProxyIsOnEntity() throws Exception
    {
        metadataCollection.saveClassificationReferenceCopy(USER_ID, getEntityProxy(), getClassification());

        this.assertPromiseOnEntity();
    }


    /**
     * The control: the classification is saved through the entity detail variant.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testClassificationSavedThroughEntityIsOnEntity() throws Exception
    {
        metadataCollection.saveClassificationReferenceCopy(USER_ID, getEntity(), getClassification());

        this.assertPromiseOnEntity();
    }


    /**
     * Retrieve the entity and check that the Promise classification is attached.
     *
     * @throws Exception unexpected
     */
    private void assertPromiseOnEntity() throws Exception
    {
        EntityDetail entity = metadataCollection.getEntityDetail(USER_ID, ENTITY_GUID);

        assertNotNull(entity);
        assertNotNull(entity.getClassifications(), "the entity has no classifications at all");
        assertEquals(entity.getClassifications().size(), 1);
        assertTrue(entity.getClassifications().get(0).getName().equals("Promise"));
    }


    private List<Classification> addToList(List<Classification> existing,
                                           Classification       newClassification)
    {
        List<Classification> updated = new ArrayList<>();

        if (existing != null)
        {
            updated.addAll(existing);
        }

        updated.add(newClassification);

        return updated;
    }


    private EntityDetail getEntity()
    {
        EntityDetail entity = new EntityDetail();

        this.fill(entity);

        return entity;
    }


    private EntityProxy getEntityProxy()
    {
        EntityProxy entityProxy = new EntityProxy();

        this.fill(entityProxy);

        return entityProxy;
    }


    private void fill(EntitySummary entity)
    {
        InstanceType instanceType = new InstanceType();

        instanceType.setTypeDefGUID("b83f3d42-f3f7-4155-ae65-58fb44ea7644");
        instanceType.setTypeDefName("SolutionComponent");

        entity.setGUID(ENTITY_GUID);
        entity.setType(instanceType);
        entity.setStatus(InstanceStatus.ACTIVE);
        entity.setMetadataCollectionId(ARCHIVE_ID);
        entity.setMetadataCollectionName("CoreContentPack");
        entity.setInstanceProvenanceType(InstanceProvenanceType.CONTENT_PACK);
        entity.setCreatedBy("Egeria Project");
        entity.setCreateTime(new Date(1775025949989L));
        entity.setVersion(1790769209690L);
    }


    private Classification getClassification()
    {
        InstanceType instanceType = new InstanceType();

        instanceType.setTypeDefGUID("bb8da6cc-7868-4f13-808e-faf78e77ec40");
        instanceType.setTypeDefName("Promise");

        Classification classification = new Classification();

        classification.setName("Promise");
        classification.setType(instanceType);
        classification.setStatus(InstanceStatus.ACTIVE);
        classification.setClassificationOrigin(ClassificationOrigin.ASSIGNED);
        classification.setMetadataCollectionId(ARCHIVE_ID);
        classification.setMetadataCollectionName("CoreContentPack");
        classification.setInstanceProvenanceType(InstanceProvenanceType.CONTENT_PACK);
        classification.setCreatedBy("Egeria Project");
        classification.setCreateTime(new Date(1775025949989L));
        classification.setVersion(1790769209690L);

        return classification;
    }
}
