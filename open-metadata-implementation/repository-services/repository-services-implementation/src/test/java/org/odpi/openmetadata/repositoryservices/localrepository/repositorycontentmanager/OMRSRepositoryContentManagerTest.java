/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OMRSRepositoryContentManagerTest
{
    @Test
    public void testGetInstanceType() throws TypeErrorException
    {
        AuditLog auditLog = new AuditLog(null, 1, ComponentDevelopmentStatus.IN_DEVELOPMENT, null, null, null);
        OMRSRepositoryContentManager testSubject = new OMRSRepositoryContentManager("testserver", auditLog);

        List<InstanceStatus> validInstanceStatusList = new LinkedList<>();
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DELETED);

        String entityDefGUID = UUID.randomUUID().toString();

        EntityDef entityDef = new EntityDef();
        entityDef.setName("EntityType1");
        entityDef.setVersion(1);
        entityDef.setVersionName("1.0");
        entityDef.setGUID(entityDefGUID);
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        testSubject.addTypeDef("unittest", entityDef);

        InstanceType instanceType = testSubject.getInstanceType("unittest", TypeDefCategory.ENTITY_DEF, "EntityType1", "testGetInstanceType");

        assertEquals(instanceType.getTypeDefName(), "EntityType1");
        assertEquals(instanceType.getTypeDefGUID(), entityDefGUID);
    }


    /**
     * The instance types and supertype chains are worked out lazily and cached.  Now that a type can change
     * while the server is running, a change must replace what was cached for it.
     *
     * @throws TypeErrorException unexpected
     */
    @Test
    public void testDerivedCachesFollowTypeChanges() throws TypeErrorException
    {
        OMRSRepositoryContentManager testSubject = this.getContentManager();

        EntityDef superType1 = this.getEntityDef("SuperType1", null, 1);
        EntityDef superType2 = this.getEntityDef("SuperType2", null, 1);
        EntityDef subType    = this.getEntityDef("SubType", superType1, 1);

        testSubject.addTypeDef("unittest", superType1);
        testSubject.addTypeDef("unittest", superType2);
        testSubject.addTypeDef("unittest", subType);

        assertEquals(testSubject.getInstanceType("unittest", TypeDefCategory.ENTITY_DEF, "SubType", "test").getTypeDefVersion(), 1L);
        assertEquals(testSubject.getSuperTypes("unittest", "SubType", "test").get(0).getName(), "SuperType1");

        EntityDef updatedSubType = this.getEntityDef("SubType", superType2, 2);

        updatedSubType.setGUID(subType.getGUID());
        testSubject.updateTypeDef("unittest", updatedSubType);

        assertEquals(testSubject.getInstanceType("unittest", TypeDefCategory.ENTITY_DEF, "SubType", "test").getTypeDefVersion(), 2L);
        assertEquals(testSubject.getSuperTypes("unittest", "SubType", "test").get(0).getName(), "SuperType2");
    }


    /**
     * Types are added and deleted by several threads while others read the type system.  With unsynchronized
     * maps this fails with ConcurrentModificationException, or loops or loses entries inside HashMap.
     *
     * @throws Exception a reader or writer failed
     */
    @Test
    public void testConcurrentTypeChanges() throws Exception
    {
        final int threadCount = 4;
        final int iterations  = 500;

        OMRSRepositoryContentManager testSubject = this.getContentManager();

        EntityDef superType = this.getEntityDef("SharedSuperType", null, 1);

        testSubject.addTypeDef("unittest", superType);

        ExecutorService      executor = Executors.newFixedThreadPool(threadCount * 2);
        List<Future<?>>      results  = new ArrayList<>();

        for (int thread = 0; thread < threadCount; thread++)
        {
            final int writerNumber = thread;

            results.add(executor.submit(() ->
            {
                for (int i = 0; i < iterations; i++)
                {
                    EntityDef entityDef = this.getEntityDef("Writer" + writerNumber + "Type" + i, superType, 1);

                    testSubject.addTypeDef("unittest", entityDef);
                    testSubject.deleteTypeDef("unittest", entityDef.getGUID(), entityDef.getName());
                }

                return null;
            }));

            results.add(executor.submit(() ->
            {
                for (int i = 0; i < iterations; i++)
                {
                    testSubject.getKnownTypeDefs();
                    testSubject.getActiveTypeDefGallery();
                    testSubject.getAllTypeDefsForProperty("unittest", "sharedProperty", "test");
                    testSubject.getInstanceType("unittest", TypeDefCategory.ENTITY_DEF, "SharedSuperType", "test");
                    testSubject.getSuperTypes("unittest", "SharedSuperType", "test");
                }

                return null;
            }));
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(60, TimeUnit.SECONDS));

        for (Future<?> result : results)
        {
            result.get();
        }

        assertEquals(testSubject.getKnownTypeDefs().size(), 1);
        assertEquals(testSubject.getAllTypeDefsForProperty("unittest", "sharedProperty", "test").size(), 1);
    }


    /**
     * Return a content manager with no local repository.
     *
     * @return content manager
     */
    private OMRSRepositoryContentManager getContentManager()
    {
        AuditLog auditLog = new AuditLog(null, 1, ComponentDevelopmentStatus.IN_DEVELOPMENT, null, null, null);

        return new OMRSRepositoryContentManager("testserver", auditLog);
    }


    /**
     * Return an entity type with one string attribute called sharedProperty.
     *
     * @param name type name
     * @param superType supertype or null
     * @param version version number
     * @return entity type
     */
    private EntityDef getEntityDef(String    name,
                                   EntityDef superType,
                                   long      version)
    {
        List<InstanceStatus> validInstanceStatusList = new LinkedList<>();
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DELETED);

        EntityDef entityDef = new EntityDef();
        entityDef.setName(name);
        entityDef.setVersion(version);
        entityDef.setVersionName(version + ".0");
        entityDef.setGUID(UUID.randomUUID().toString());
        entityDef.setValidInstanceStatusList(validInstanceStatusList);

        if (superType != null)
        {
            entityDef.setSuperType(new TypeDefLink(superType));
        }
        else
        {
            PrimitiveDef stringDef = new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);

            stringDef.setGUID("b34a64b9-554a-42b1-8f8a-7d5c2339f9c4");
            stringDef.setName("string");

            TypeDefAttribute attribute = new TypeDefAttribute();

            attribute.setAttributeName("sharedProperty");
            attribute.setAttributeType(stringDef);

            entityDef.setPropertiesDefinition(List.of(attribute));
        }

        return entityDef;
    }
}
