/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

/**
 * An attribute can be removed from an open metadata type (NotificationType's notificationInterval in release 6.3)
 * while instances stored earlier still hold a value for it.  A merge update carries the stored properties
 * forward, and the repository rejects a property its type does not define - so without this filter, every
 * such instance could never be updated again.
 */
public class RemovedAttributeMergeTest
{
    private static final String TYPE_NAME = "NotificationType";

    private final OMRSRepositoryHelper                  repositoryHelper = Mockito.mock(OMRSRepositoryHelper.class);
    private final OpenMetadataAPIGenericHandler<Object> handler;


    public RemovedAttributeMergeTest()
    {
        EntityDef typeDef = new EntityDef();

        typeDef.setName(TYPE_NAME);

        Mockito.when(repositoryHelper.getTypeDefByName(anyString(), eq(TYPE_NAME))).thenReturn(typeDef);
        Mockito.when(repositoryHelper.getAllPropertiesForTypeDef(anyString(), eq(typeDef), anyString()))
               .thenReturn(List.of(attribute("qualifiedName"), attribute("minimumNotificationInterval")));

        handler = new OpenMetadataAPIGenericHandler<>(null,
                                                      Object.class,
                                                      "testService",
                                                      "testServer",
                                                      Mockito.mock(InvalidParameterHandler.class),
                                                      Mockito.mock(RepositoryHandler.class),
                                                      repositoryHelper,
                                                      "localServerUser",
                                                      Mockito.mock(OpenMetadataServerSecurityVerifier.class),
                                                      Mockito.mock(AuditLog.class));
    }


    @Test
    public void testStoredPropertyTheTypeNoLongerDefinesIsDropped()
    {
        InstanceProperties existing = new InstanceProperties();

        existing.setProperty("qualifiedName", value("NotificationType::x"));
        existing.setProperty("minimumNotificationInterval", value(10L));
        existing.setProperty("notificationInterval", value(0L));

        InstanceProperties result = handler.removeUndefinedProperties(existing, TYPE_NAME, "test");

        assertEquals(result.getInstanceProperties().keySet(), java.util.Set.of("qualifiedName", "minimumNotificationInterval"));
        assertNull(result.getPropertyValue("notificationInterval"));
    }


    @Test
    public void testUnknownTypeLeavesPropertiesAlone()
    {
        InstanceProperties existing = new InstanceProperties();

        existing.setProperty("notificationInterval", value(0L));

        Mockito.when(repositoryHelper.getTypeDefByName(anyString(), eq("UnknownType"))).thenReturn(null);

        InstanceProperties result = handler.removeUndefinedProperties(existing, "UnknownType", "test");

        assertSame(result, existing);
        assertEquals(result.getInstanceProperties().keySet(), java.util.Set.of("notificationInterval"));
    }


    @Test
    public void testNullPropertiesAreAllowed()
    {
        assertNull(handler.removeUndefinedProperties(null, TYPE_NAME, "test"));
        Mockito.verify(repositoryHelper, Mockito.never()).getAllPropertiesForTypeDef(anyString(), any(), anyString());
    }


    private static TypeDefAttribute attribute(String name)
    {
        TypeDefAttribute attribute = new TypeDefAttribute();

        attribute.setAttributeName(name);

        return attribute;
    }


    private static PrimitivePropertyValue value(Object primitiveValue)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveValue(primitiveValue);

        return propertyValue;
    }
}
