/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.Connection;
import org.odpi.openmetadata.accessservices.dataengine.model.Endpoint;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineConnectionAndEndpointHandlerTest {

    private static final String USER = "user";
    private static final String METHOD = "method";
    private static final String ASSET_QUALIFIED_NAME = "qualifiedName";
    private static final String CONNECTION_QUALIFIED_NAME = ASSET_QUALIFIED_NAME + "::connection" ;
    private static final String ENDPOINT_QUALIFIED_NAME = CONNECTION_QUALIFIED_NAME + "::endpoint" ;
    private static final String EXTERNAL_SOURCE_GUID = "externalSourceGuid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String NETWORK_ADDRESS = "networkAddress";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;
    @Mock
    private ReferenceableHandler<Connection> connectionHandler;
    @Mock
    private ReferenceableHandler<Endpoint> endpointHandler;

    @InjectMocks
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @Test
    void insertConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataEngineCommonHandler(true);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME, NETWORK_ADDRESS,
                EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, CONNECTION_QUALIFIED_NAME, CONNECTION_TYPE_NAME);
        verify(connectionHandler, times(1)).createBeanInRepository(eq(USER), eq(EXTERNAL_SOURCE_GUID),
                eq(EXTERNAL_SOURCE_NAME), eq(CONNECTION_TYPE_GUID), eq(CONNECTION_TYPE_NAME), eq(CONNECTION_QUALIFIED_NAME),
                eq(QUALIFIED_NAME_PROPERTY_NAME), any(), eq(METHOD));
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_TO_ASSET_TYPE_NAME), eq(CONNECTION_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ENDPOINT_QUALIFIED_NAME, ENDPOINT_TYPE_NAME);
//        Commented verification cannot be performed because of mockito bug: unable to properly inject generics
//        Issue -> https://github.com/mockito/mockito/issues/1066
//        verify(endpointHandler, times(1)).createBeanInRepository(eq(USER), eq(EXTERNAL_SOURCE_GUID),
//                eq(EXTERNAL_SOURCE_NAME), eq(ENDPOINT_TYPE_GUID), eq(ENDPOINT_TYPE_NAME), eq(ENDPOINT_QUALIFIED_NAME),
//                eq(QUALIFIED_NAME_PROPERTY_NAME), any(), eq(METHOD));
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_ENDPOINT_TYPE_NAME), eq(ENDPOINT_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());
    }

    @Test
    void updateConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataEngineCommonHandler(true);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME, NETWORK_ADDRESS,
                EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, CONNECTION_QUALIFIED_NAME, CONNECTION_TYPE_NAME);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_TO_ASSET_TYPE_NAME), eq(CONNECTION_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ENDPOINT_QUALIFIED_NAME, ENDPOINT_TYPE_NAME);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_ENDPOINT_TYPE_NAME), eq(ENDPOINT_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());
    }

    private void mockDataEngineCommonHandler(boolean insert)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        EntityDetail asset = new EntityDetail();
        when(dataEngineCommonHandler.findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME)).thenReturn(Optional.of(asset));

        if(insert){
            when(dataEngineCommonHandler.findEntity(USER, CONNECTION_QUALIFIED_NAME, CONNECTION_TYPE_NAME)).thenReturn(Optional.empty());
            when(dataEngineCommonHandler.findEntity(USER, ENDPOINT_QUALIFIED_NAME, ENDPOINT_TYPE_NAME)).thenReturn(Optional.empty());
            return;
        }

        Optional<EntityDetail> connection = Optional.of(new EntityDetail());
        when(dataEngineCommonHandler.findEntity(USER, CONNECTION_QUALIFIED_NAME, CONNECTION_TYPE_NAME)).thenReturn(connection);

        Optional<EntityDetail> endpoint = Optional.of(new EntityDetail());
        when(dataEngineCommonHandler.findEntity(USER, ENDPOINT_QUALIFIED_NAME, ENDPOINT_TYPE_NAME)).thenReturn(endpoint);
    }

}
