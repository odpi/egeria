/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.Connection;
import org.odpi.openmetadata.accessservices.dataengine.model.Endpoint;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ConnectionBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.EndpointBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineConnectionAndEndpointHandlerTest {

    private static final String USER = "user";
    private static final String METHOD = "method";
    private static final String ASSET_GUID = "assetGuid";
    private static final String CONNECTION_GUID = "connectionGuid";
    private static final String ENDPOINT_GUID = "endpointGuid";
    private static final String CONNECTION_TO_ASSET_GUID = "connectionToAssetGuid";
    private static final String CONNECTION_ENDPOINT_GUID = "connectionEndpointGuid";
    private static final String ASSET_QUALIFIED_NAME = "qualifiedName";
    private static final String EXTERNAL_SOURCE_GUID = "externalSourceGuid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String PROTOCOL = "protocol";
    private static final String NETWORK_ADDRESS = "networkAddress";
    private static final String CONNECTION_QUALIFIED_NAME = CONNECTION_TYPE_NAME + "::" + PROTOCOL + "::" + NETWORK_ADDRESS ;
    private static final String ENDPOINT_QUALIFIED_NAME = ENDPOINT_TYPE_NAME + "::" + PROTOCOL + "::" + NETWORK_ADDRESS ;

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

    @Spy
    @InjectMocks
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @Test
    void insertConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ConnectionBuilder mockedConnectionBuilder = mockConnectionBuilder();
        EndpointBuilder mockedEndpointBuilder = mockEndpointBuilder();

        mockDataEngineCommonHandler(true, mockedConnectionBuilder, mockedEndpointBuilder);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME, PROTOCOL,
                NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, CONNECTION_QUALIFIED_NAME, CONNECTION_TYPE_NAME);
        verify(connectionHandler, times(1)).createBeanInRepository(USER, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, CONNECTION_TYPE_GUID, CONNECTION_TYPE_NAME, CONNECTION_QUALIFIED_NAME,
                QUALIFIED_NAME_PROPERTY_NAME, mockedConnectionBuilder, METHOD);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, CONNECTION_GUID, ASSET_GUID,
                CONNECTION_TO_ASSET_TYPE_NAME, CONNECTION_TYPE_NAME, EXTERNAL_SOURCE_NAME, null);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ENDPOINT_QUALIFIED_NAME, ENDPOINT_TYPE_NAME);
        verify(endpointHandler, times(1)).createBeanInRepository(USER, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, ENDPOINT_TYPE_GUID, ENDPOINT_TYPE_NAME, ENDPOINT_QUALIFIED_NAME,
                QUALIFIED_NAME_PROPERTY_NAME, mockedEndpointBuilder, METHOD);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_ENDPOINT_TYPE_NAME), eq(ENDPOINT_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());
    }

    @Test
    void updateConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ConnectionBuilder mockedConnectionBuilder = mockConnectionBuilder();
        EndpointBuilder mockedEndpointBuilder = mockEndpointBuilder();

        mockDataEngineCommonHandler(false, mockedConnectionBuilder, mockedEndpointBuilder);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME, PROTOCOL,
                NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, CONNECTION_QUALIFIED_NAME, CONNECTION_TYPE_NAME);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_TO_ASSET_TYPE_NAME), eq(CONNECTION_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ENDPOINT_QUALIFIED_NAME, ENDPOINT_TYPE_NAME);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(eq(USER), any(), any(),
                eq(CONNECTION_ENDPOINT_TYPE_NAME), eq(ENDPOINT_TYPE_NAME), eq(EXTERNAL_SOURCE_NAME), any());
    }


    /**
     *  Issue -> https://github.com/mockito/mockito/issues/1066
     *  Unable to properly inject generics; connectionHandler mock is also used for endpointHandler even though a separate mock is defined
     *
     *  Fix: set mocked objects as field values via reflection
     */
    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field connectionHandlerField = DataEngineConnectionAndEndpointHandler.class.getDeclaredField("connectionHandler");
        boolean connectionHandlerIsAccessible = connectionHandlerField.isAccessible();
        connectionHandlerField.setAccessible(true);
        connectionHandlerField.set(dataEngineConnectionAndEndpointHandler, connectionHandler);
        connectionHandlerField.setAccessible(connectionHandlerIsAccessible);

        Field endpointHandlerField = DataEngineConnectionAndEndpointHandler.class.getDeclaredField("endpointHandler");
        boolean endpointHandlerIsAccessible = endpointHandlerField.isAccessible();
        endpointHandlerField.setAccessible(true);
        endpointHandlerField.set(dataEngineConnectionAndEndpointHandler, endpointHandler);
        endpointHandlerField.setAccessible(endpointHandlerIsAccessible);
    }

    private void mockDataEngineCommonHandler(boolean insert, ConnectionBuilder connectionBuilder, EndpointBuilder endpointBuilder)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        EntityDetail asset = new EntityDetail();
        asset.setGUID(ASSET_GUID);
        when(dataEngineCommonHandler.findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME)).thenReturn(Optional.of(asset));

        when(connectionHandler.createBeanInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, CONNECTION_TYPE_GUID,
                CONNECTION_TYPE_NAME, CONNECTION_QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, connectionBuilder, METHOD))
                .thenReturn(CONNECTION_GUID);
        when(endpointHandler.createBeanInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, ENDPOINT_TYPE_GUID,
                ENDPOINT_TYPE_NAME, ENDPOINT_QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, endpointBuilder, METHOD))
                .thenReturn(ENDPOINT_GUID);

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

    private ConnectionBuilder mockConnectionBuilder() {
        ConnectionBuilder mockedConnectionBuilder = Mockito.mock(ConnectionBuilder.class);
        doReturn(mockedConnectionBuilder).when(dataEngineConnectionAndEndpointHandler)
                .getConnectionBuilder(CONNECTION_QUALIFIED_NAME);
        return mockedConnectionBuilder;
    }

    private EndpointBuilder mockEndpointBuilder() {
        EndpointBuilder mockedEndpointBuilder = Mockito.mock(EndpointBuilder.class);
        doReturn(mockedEndpointBuilder).when(dataEngineConnectionAndEndpointHandler)
                .getEndpointBuilder(PROTOCOL, NETWORK_ADDRESS, ENDPOINT_QUALIFIED_NAME);
        return mockedEndpointBuilder;
    }

}
