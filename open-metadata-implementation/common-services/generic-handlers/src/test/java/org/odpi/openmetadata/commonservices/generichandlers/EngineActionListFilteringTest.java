/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Building an engine action reads its action targets and requesters, and the caller may not be allowed to read
 * one of them.  That refusal used to fail the whole query, so a single such engine action hid every other engine
 * action on its page - and an engine host sweeping for missed engine actions every few seconds hit the same refusal
 * on every pass, for ever.  The unreadable engine action is now left out and the rest of the page is returned.
 */
public class EngineActionListFilteringTest
{
    private static final String UNREADABLE_GUID = "unreadable-engine-action";


    @Test
    public void testUnreadableEngineActionIsLeftOutOfActiveList() throws Exception
    {
        OMRSRepositoryHelper repositoryHelper  = mock(OMRSRepositoryHelper.class);
        RepositoryHandler    repositoryHandler = mock(RepositoryHandler.class);

        when(repositoryHelper.addEnumPropertyToInstance(anyString(), any(), anyString(), anyString(), anyString(), anyInt(), anyString()))
                .thenAnswer(invocation ->
                            {
                                InstanceProperties properties = new InstanceProperties();
                                EnumPropertyValue  enumValue  = new EnumPropertyValue();

                                enumValue.setOrdinal(invocation.getArgument(5));
                                properties.setProperty(invocation.getArgument(2), enumValue);

                                return properties;
                            });

        when(repositoryHandler.findEntities(anyString(), anyString(), any(), any(), any(), any(), any(), any(), any(),
                                            anyBoolean(), anyBoolean(), anyInt(), anyInt(), any(), anyString()))
                .thenReturn(List.of(this.getEntity("first-engine-action"),
                                    this.getEntity(UNREADABLE_GUID),
                                    this.getEntity("third-engine-action")));

        EngineActionHandler<String> handler = new EngineActionHandler<>(null,
                                                                        String.class,
                                                                        "testService",
                                                                        "testServer",
                                                                        mock(InvalidParameterHandler.class),
                                                                        repositoryHandler,
                                                                        repositoryHelper,
                                                                        "testServerUser",
                                                                        null,
                                                                        null)
        {
            /*
             * Stands in for reading the engine action's targets: one of them belongs to an element the caller
             * cannot read.
             */
            @Override
            public String getEngineAction(String userId, EntityDetail primaryEntity, Date effectiveTime, String methodName) throws UserNotAuthorizedException
            {
                if (UNREADABLE_GUID.equals(primaryEntity.getGUID()))
                {
                    throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ANCHOR_ACCESS.getMessageDefinition(userId,
                                                                                                                         "Read",
                                                                                                                         "DigitalProductFamily",
                                                                                                                         "anchor-guid"),
                                                         this.getClass().getName(),
                                                         methodName,
                                                         userId);
                }

                return primaryEntity.getGUID();
            }
        };

        List<String> activeEngineActions = handler.getActiveEngineActions("engineUser", 0, 10, null, "testUnreadableEngineActionIsLeftOutOfActiveList");

        assertEquals(activeEngineActions, List.of("first-engine-action", "third-engine-action"));
    }


    /**
     * The engine host's sweep asks only for the engine actions approved to run on its own engine - both
     * selections are pushed into the repository query rather than applied to every active engine action.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testApprovedEngineActionsQueryIsScopedToEngine() throws Exception
    {
        OMRSRepositoryHelper repositoryHelper  = mock(OMRSRepositoryHelper.class);
        RepositoryHandler    repositoryHandler = mock(RepositoryHandler.class);

        when(repositoryHelper.addEnumPropertyToInstance(anyString(), any(), anyString(), anyString(), anyString(), anyInt(), anyString()))
                .thenAnswer(invocation ->
                            {
                                InstanceProperties properties = new InstanceProperties();
                                EnumPropertyValue  enumValue  = new EnumPropertyValue();

                                enumValue.setOrdinal(invocation.getArgument(5));
                                properties.setProperty(invocation.getArgument(2), enumValue);

                                return properties;
                            });
        when(repositoryHelper.addStringPropertyToInstance(anyString(), any(), anyString(), anyString(), anyString()))
                .thenAnswer(invocation ->
                            {
                                InstanceProperties     properties  = new InstanceProperties();
                                PrimitivePropertyValue stringValue = new PrimitivePropertyValue();

                                stringValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                                stringValue.setPrimitiveValue(invocation.getArgument(3));
                                properties.setProperty(invocation.getArgument(2), stringValue);

                                return properties;
                            });

        ArgumentCaptor<SearchProperties> searchPropertiesCaptor = ArgumentCaptor.forClass(SearchProperties.class);

        when(repositoryHandler.findEntities(anyString(), anyString(), any(), searchPropertiesCaptor.capture(), any(), any(), any(), any(), any(),
                                            anyBoolean(), anyBoolean(), anyInt(), anyInt(), any(), anyString()))
                .thenReturn(List.of(this.getEntity("approved-engine-action")));

        EngineActionHandler<String> handler = new EngineActionHandler<>(null,
                                                                        String.class,
                                                                        "testService",
                                                                        "testServer",
                                                                        mock(InvalidParameterHandler.class),
                                                                        repositoryHandler,
                                                                        repositoryHelper,
                                                                        "testServerUser",
                                                                        null,
                                                                        null)
        {
            @Override
            public String getEngineAction(String userId, EntityDetail primaryEntity, Date effectiveTime, String methodName)
            {
                return primaryEntity.getGUID();
            }
        };

        assertEquals(handler.getApprovedEngineActions("engineUser", "engine-guid", 0, 10, null, "test"),
                     List.of("approved-engine-action"));

        SearchProperties searchProperties = searchPropertiesCaptor.getValue();

        assertEquals(searchProperties.getMatchCriteria(), MatchCriteria.ALL);

        boolean engineMatched = false;
        boolean statusMatched = false;

        for (PropertyCondition condition : searchProperties.getConditions())
        {
            if (OpenMetadataProperty.EXECUTOR_ENGINE_GUID.name.equals(condition.getProperty()))
            {
                engineMatched = "engine-guid".equals(((PrimitivePropertyValue) condition.getValue()).getPrimitiveValue());
            }
            else if (condition.getNestedConditions() != null)
            {
                List<PropertyCondition> statuses = condition.getNestedConditions().getConditions();

                statusMatched = (statuses.size() == 1) &&
                                (((EnumPropertyValue) statuses.get(0).getValue()).getOrdinal() == ActivityStatus.APPROVED.getOrdinal());
            }
        }

        assertTrue(engineMatched, "Query not scoped to the governance engine: " + searchProperties);
        assertTrue(statusMatched, "Query not scoped to APPROVED engine actions: " + searchProperties);
    }


    private EntityDetail getEntity(String guid)
    {
        EntityDetail entityDetail = new EntityDetail();

        entityDetail.setGUID(guid);

        return entityDetail;
    }
}
