/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefInUseException;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Verify what the content manager does with a delete event from the cohort.  If the local repository still uses
 * the type it keeps it, and announces it to the cohort again so that the members that have already deleted it -
 * including the member that asked for the delete - add it back.  If the local repository deletes it, nothing is
 * announced.
 */
public class DeletedTypeEventTest
{
    private static final String COHORT_NAME         = "testCohort";
    private static final String LOCAL_COLLECTION_ID = "5d3b8a2c-0f1e-4d6a-9b7c-3e2f1a0b9c8d";
    private static final String REMOTE_COLLECTION_ID = "8e7d6c5b-4a39-4281-9f0e-1d2c3b4a5968";

    private OMRSRepositoryContentManager contentManager;
    private OMRSMetadataCollection       metadataCollection;
    private OMRSRepositoryEventManager   outboundEventManager;


    /**
     * Build a content manager connected to a stubbed local repository and outbound event manager.
     *
     * @throws Exception unexpected
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        AuditLog auditLog = new AuditLog(null, 1, ComponentDevelopmentStatus.IN_DEVELOPMENT, null, null, null);

        contentManager       = new OMRSRepositoryContentManager("testserver", auditLog);
        metadataCollection   = mock(OMRSMetadataCollection.class);
        outboundEventManager = mock(OMRSRepositoryEventManager.class);

        LocalOMRSRepositoryConnector localConnector = mock(LocalOMRSRepositoryConnector.class);

        when(localConnector.getMetadataCollection()).thenReturn(metadataCollection);
        when(localConnector.getMetadataCollectionId()).thenReturn(LOCAL_COLLECTION_ID);
        when(localConnector.getLocalServerName()).thenReturn("localServer");

        contentManager.setupEventProcessor(localConnector, outboundEventManager);
    }


    /**
     * A TypeDef the local repository still uses is kept and announced again.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testTypeDefInUseIsReannounced() throws Exception
    {
        EntityDef entityDef = this.getEntityDef();

        contentManager.addTypeDef("unittest", entityDef);

        doThrow(this.getTypeDefInUseException(entityDef.getName(), entityDef.getGUID()))
                .when(metadataCollection).deleteTypeDef(anyString(), anyString(), anyString());

        contentManager.processDeletedTypeDefEvent(COHORT_NAME, REMOTE_COLLECTION_ID, "remoteServer", "type", "org",
                                                  entityDef.getGUID(), entityDef.getName());

        assertNotNull(contentManager.getTypeDefByName(entityDef.getName()));
        verify(outboundEventManager).processNewTypeDefEvent(eq(COHORT_NAME), eq(LOCAL_COLLECTION_ID), eq("localServer"),
                                                            any(), any(), eq(entityDef));
    }


    /**
     * A TypeDef the local repository deletes is removed and not announced.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testUnusedTypeDefIsDeleted() throws Exception
    {
        EntityDef entityDef = this.getEntityDef();

        contentManager.addTypeDef("unittest", entityDef);

        contentManager.processDeletedTypeDefEvent(COHORT_NAME, REMOTE_COLLECTION_ID, "remoteServer", "type", "org",
                                                  entityDef.getGUID(), entityDef.getName());

        assertNull(contentManager.getTypeDefByName(entityDef.getName()));
        verify(outboundEventManager, never()).processNewTypeDefEvent(any(), any(), any(), any(), any(), any());
    }


    /**
     * An enum that a type in the local repository still uses is kept and announced again.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testEnumDefInUseIsReannounced() throws Exception
    {
        EnumDef enumDef = new EnumDef();

        enumDef.setGUID("1f2e3d4c-5b6a-4798-8a7b-6c5d4e3f2a1b");
        enumDef.setName("CuisineType");
        enumDef.setVersion(1);
        enumDef.setVersionName("1.0");

        contentManager.addAttributeTypeDef("unittest", enumDef);

        doThrow(this.getTypeDefInUseException(enumDef.getName(), enumDef.getGUID()))
                .when(metadataCollection).deleteAttributeTypeDef(anyString(), anyString(), anyString());

        contentManager.processDeletedAttributeTypeDefEvent(COHORT_NAME, REMOTE_COLLECTION_ID, "remoteServer", "type", "org",
                                                           enumDef.getGUID(), enumDef.getName());

        assertNotNull(contentManager.getAttributeTypeDefByName(enumDef.getName()));
        verify(outboundEventManager).processNewAttributeTypeDefEvent(eq(COHORT_NAME), eq(LOCAL_COLLECTION_ID), eq("localServer"),
                                                                     any(), any(), eq(enumDef));
    }


    /**
     * Return a version 1 entity type.
     *
     * @return entity type
     */
    private EntityDef getEntityDef()
    {
        EntityDef entityDef = new EntityDef();

        entityDef.setGUID("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e8f9");
        entityDef.setName("Recipe");
        entityDef.setVersion(1);
        entityDef.setVersionName("1.0");
        entityDef.setValidInstanceStatusList(List.of(InstanceStatus.ACTIVE, InstanceStatus.DELETED));

        return entityDef;
    }


    /**
     * Return the exception the local repository throws for a type it still uses.
     *
     * @param typeName name of the type
     * @param typeGUID unique identifier of the type
     * @return exception
     */
    private TypeDefInUseException getTypeDefInUseException(String typeName,
                                                           String typeGUID)
    {
        return new TypeDefInUseException(OMRSErrorCode.TYPEDEF_IN_USE.getMessageDefinition(typeName, typeGUID, "localRepository"),
                                         this.getClass().getName(),
                                         "deleteTypeDef");
    }
}
