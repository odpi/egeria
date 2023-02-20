/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

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
}
