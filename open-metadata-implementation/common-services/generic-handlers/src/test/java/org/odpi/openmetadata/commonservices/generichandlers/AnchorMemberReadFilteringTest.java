/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.testng.Assert.assertEquals;

/**
 * A search that finds several members of one anchor checks each member against the anchor.  The check used to be
 * made once per anchor: if the first member passed on zones of its own, the anchor was taken as validated and a
 * later member - one with no zones of its own, which is judged on its anchor's zones - was returned without ever
 * being checked, even when the anchor's zones hid it from the caller.
 */
public class AnchorMemberReadFilteringTest
{
    private static final String ELEMENT_TYPE = "TestElement";
    private static final String ANCHOR_GUID  = "anchor";


    @Test
    public void testEachMemberOfAnAnchorIsChecked() throws Exception
    {
        EntityDetail anchor        = getEntity(ANCHOR_GUID);
        EntityDetail ownZoneMember = getEntity("member-with-own-zones");
        EntityDetail hiddenMember  = getEntity("member-judged-on-anchor");

        RepositoryHandler repositoryHandler = Mockito.mock(RepositoryHandler.class, (InvocationOnMock invocation) ->
        {
            if ("findEntities".equals(invocation.getMethod().getName()))
            {
                return List.of(anchor);
            }

            return List.class.isAssignableFrom(invocation.getMethod().getReturnType()) ? null : Mockito.RETURNS_DEFAULTS.answer(invocation);
        });

        OMRSRepositoryHelper               repositoryHelper        = Mockito.mock(OMRSRepositoryHelper.class, AnchorMemberReadFilteringTest::answerRepositoryHelper);
        InvalidParameterHandler            invalidParameterHandler = Mockito.mock(InvalidParameterHandler.class);
        OpenMetadataServerSecurityVerifier securityVerifier        = Mockito.mock(OpenMetadataServerSecurityVerifier.class);

        Mockito.when(invalidParameterHandler.getMaxPagingSize()).thenReturn(100);

        /*
         * The member with its own zones passes; the member judged on its anchor's zones is refused.
         */
        doThrow(new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ANCHOR_ACCESS.getMessageDefinition("testUser",
                                                                                                                             "Read",
                                                                                                                             ELEMENT_TYPE,
                                                                                                                             ANCHOR_GUID),
                                                this.getClass().getName(),
                                                "test",
                                                "testUser"))
                .when(securityVerifier).validateUserForAnchorMemberRead(anyString(),
                                                                         any(),
                                                                         argThat(entity -> (entity != null) && hiddenMember.getGUID().equals(entity.getGUID())),
                                                                         anyBoolean(),
                                                                         any(),
                                                                         anyString(),
                                                                         anyString());

        OpenMetadataAPIGenericHandler<Object> handler = new OpenMetadataAPIGenericHandler<>(null,
                                                                                            Object.class,
                                                                                            "testService",
                                                                                            "testServer",
                                                                                            invalidParameterHandler,
                                                                                            repositoryHandler,
                                                                                            repositoryHelper,
                                                                                            "localServerUser",
                                                                                            securityVerifier,
                                                                                            Mockito.mock(AuditLog.class));

        List<EntityDetail> visible = handler.validateEntitiesAndAnchorsForRead("testUser",
                                                                               List.of(ownZoneMember, hiddenMember),
                                                                               false,
                                                                               false,
                                                                               null,
                                                                               "test");

        assertEquals(visible, List.of(ownZoneMember));
    }


    private static EntityDetail getEntity(String guid)
    {
        EntityDetail           entity                = new EntityDetail();
        InstanceType           type                  = new InstanceType();
        Classification         anchorsClassification = new Classification();
        InstanceProperties     anchorsProperties     = new InstanceProperties();
        PrimitivePropertyValue anchorGUIDValue       = new PrimitivePropertyValue();

        type.setTypeDefName(ELEMENT_TYPE);
        anchorGUIDValue.setPrimitiveValue(ANCHOR_GUID);
        anchorsProperties.setProperty(OpenMetadataProperty.ANCHOR_GUID.name, anchorGUIDValue);
        anchorsClassification.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
        anchorsClassification.setProperties(anchorsProperties);

        entity.setGUID(guid);
        entity.setType(type);
        entity.setClassifications(new ArrayList<>(List.of(anchorsClassification)));

        return entity;
    }


    private static Object answerRepositoryHelper(InvocationOnMock invocation) throws Throwable
    {
        Object[] args = invocation.getArguments();

        switch (invocation.getMethod().getName())
        {
            case "getClassificationFromEntity":
                if ((args[1] instanceof EntitySummary entity) && (entity.getClassifications() != null))
                {
                    for (Classification classification : entity.getClassifications())
                    {
                        if (classification.getName().equals(args[2]))
                        {
                            return classification;
                        }
                    }
                }
                return null;

            case "getStringProperty":
                if (args[2] instanceof InstanceProperties properties)
                {
                    InstancePropertyValue value = properties.getPropertyValue((String)args[1]);

                    if (value instanceof PrimitivePropertyValue primitiveValue)
                    {
                        return primitiveValue.getPrimitiveValue();
                    }
                }
                return null;
        }

        if (List.class.isAssignableFrom(invocation.getMethod().getReturnType()))
        {
            return null;
        }

        return Mockito.RETURNS_DEFAULTS.answer(invocation);
    }
}
