/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * Verify how the types defined through the API reach cohort members that were not there when they were defined
 * or changed.  The home server announces them again when a member connects to the cohort, and a member that
 * receives a later version of a type it holds brings its copy up to date.
 */
public class CohortTypeCatchUpTest
{
    private static final String COHORT_NAME          = "testCohort";
    private static final String LOCAL_COLLECTION_ID  = "5d3b8a2c-0f1e-4d6a-9b7c-3e2f1a0b9c8d";
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
     * Only the homed types are announced - attribute types first, then each type after the types it depends on.
     */
    @Test
    public void testHomedTypesAreAnnouncedInOrder()
    {
        EnumDef   homedEnum    = this.getEnumDef(LOCAL_COLLECTION_ID);
        EntityDef archiveType  = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e801", "Referenceable", null, "archive-guid", 1);
        EntityDef homedSubtype = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e802", "Dessert", "Recipe", LOCAL_COLLECTION_ID, 1);
        EntityDef homedType    = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e803", "Recipe", "Referenceable", LOCAL_COLLECTION_ID, 1);

        contentManager.addAttributeTypeDef("unittest", homedEnum);
        contentManager.addTypeDef("unittest", archiveType);
        contentManager.addTypeDef("unittest", homedSubtype);
        contentManager.addTypeDef("unittest", homedType);

        contentManager.announceHomedTypes(COHORT_NAME);

        InOrder inOrder = inOrder(outboundEventManager);

        inOrder.verify(outboundEventManager).processNewAttributeTypeDefEvent(eq(COHORT_NAME), eq(LOCAL_COLLECTION_ID), any(), any(), any(), eq(homedEnum));
        inOrder.verify(outboundEventManager).processNewTypeDefEvent(eq(COHORT_NAME), eq(LOCAL_COLLECTION_ID), any(), any(), any(), eq(homedType));
        inOrder.verify(outboundEventManager).processNewTypeDefEvent(eq(COHORT_NAME), eq(LOCAL_COLLECTION_ID), any(), any(), any(), eq(homedSubtype));
        verify(outboundEventManager, never()).processNewTypeDefEvent(any(), any(), any(), any(), any(), eq(archiveType));
    }


    /**
     * A later version of a type held locally is applied as an update built from the differences.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testLaterVersionIsAppliedAsUpdate() throws Exception
    {
        EntityDef version1 = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e804", "Recipe", null, REMOTE_COLLECTION_ID, 1);
        EntityDef version2 = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e804", "Recipe", null, REMOTE_COLLECTION_ID, 2);

        version2.setDescription("Updated while this server was away");

        contentManager.addTypeDef("unittest", version1);

        when(metadataCollection.updateTypeDef(anyString(), any(TypeDefPatch.class)))
                .thenAnswer(invocation -> new OMRSRepositoryPropertiesUtilities().applyPatch("test",
                                                                                             version1,
                                                                                             invocation.getArgument(1),
                                                                                             "updateTypeDef"));

        contentManager.processNewTypeDefEvent(COHORT_NAME, REMOTE_COLLECTION_ID, "remoteServer", "type", "org", version2);

        ArgumentCaptor<TypeDefPatch> patch = ArgumentCaptor.forClass(TypeDefPatch.class);

        verify(metadataCollection).updateTypeDef(anyString(), patch.capture());
        assertEquals(patch.getValue().getApplyToVersion(), 1L);
        assertEquals(patch.getValue().getUpdateToVersion(), 2L);

        TypeDef cachedTypeDef = contentManager.getTypeDefByName("Recipe");

        assertEquals(cachedTypeDef.getVersion(), 2L);
        assertEquals(cachedTypeDef.getDescription(), "Updated while this server was away");
    }


    /**
     * The same or an earlier version of a type held locally is ignored, as before.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testSameVersionIsIgnored() throws Exception
    {
        EntityDef version2 = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e805", "Recipe", null, REMOTE_COLLECTION_ID, 2);
        EntityDef version1 = this.getEntityDef("0a1b2c3d-4e5f-4061-8293-a4b5c6d7e805", "Recipe", null, REMOTE_COLLECTION_ID, 1);

        contentManager.addTypeDef("unittest", version2);

        contentManager.processNewTypeDefEvent(COHORT_NAME, REMOTE_COLLECTION_ID, "remoteServer", "type", "org", version2);
        contentManager.processNewTypeDefEvent(COHORT_NAME, REMOTE_COLLECTION_ID, "remoteServer", "type", "org", version1);

        verify(metadataCollection, never()).updateTypeDef(anyString(), any(TypeDefPatch.class));
        verify(metadataCollection, never()).addTypeDef(anyString(), any(TypeDef.class));
        assertEquals(contentManager.getTypeDefByName("Recipe").getVersion(), 2L);
    }


    /**
     * Return an entity type.
     *
     * @param guid unique identifier
     * @param name type name
     * @param superTypeName name of the supertype or null
     * @param origin origin of the type
     * @param version version number
     * @return entity type
     */
    private EntityDef getEntityDef(String guid,
                                   String name,
                                   String superTypeName,
                                   String origin,
                                   long   version)
    {
        EntityDef entityDef = new EntityDef();

        entityDef.setGUID(guid);
        entityDef.setName(name);
        entityDef.setVersion(version);
        entityDef.setVersionName(version + ".0");
        entityDef.setOrigin(origin);
        entityDef.setValidInstanceStatusList(List.of(InstanceStatus.ACTIVE, InstanceStatus.DELETED));

        if (superTypeName != null)
        {
            TypeDefLink superType = new TypeDefLink();

            superType.setName(superTypeName);
            entityDef.setSuperType(superType);
        }

        return entityDef;
    }


    /**
     * Return an enum type.
     *
     * @param origin origin of the enum
     * @return enum type
     */
    private EnumDef getEnumDef(String origin)
    {
        EnumDef enumDef = new EnumDef();

        enumDef.setGUID("1f2e3d4c-5b6a-4798-8a7b-6c5d4e3f2a1b");
        enumDef.setName("CuisineType");
        enumDef.setVersion(1);
        enumDef.setVersionName("1.0");
        enumDef.setOrigin(origin);

        return enumDef;
    }
}
