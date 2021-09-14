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
import org.odpi.openmetadata.accessservices.dataengine.model.ConnectorType;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Endpoint;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.EndpointBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineConnectionAndEndpointHandlerTest {

    private static final String USER = "user";
    private static final String ASSET_GUID = "assetGUID";
    private static final String CONNECTION_GUID = "connectionGUID";
    private static final String ENDPOINT_GUID = "endpointGUID";
    private static final String ASSET_QUALIFIED_NAME = "qualifiedName";
    private static final String EXTERNAL_SOURCE_GUID = "externalSourceGUID";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String PROTOCOL = "protocol";
    private static final String NETWORK_ADDRESS = "networkAddress";
    private static final int START_FROM = 0;
    private static final int PAGE_SIZE = 10;
    private static final String SEARCH_STRING_PARAMETER_NAME = "searchString";
    private static final String OCF = "Open Connector Framework (OCF)";
    private static final String COLON = ":";
    private static final String CONNECTION = " Connection";
    private static final String ENDPOINT = " Endpoint";
    public static final String GET_PROPER_CONNECTOR_TYPE_METHOD_NAME = "getProperConnectorType";
    public static final String UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME = "upsertConnectionAndEndpoint";
    public static final String UPDATE_ENDPOINT_METHOD_NAME = "updateEndpoint";
    public static final String CONNECTOR_PROVIDER_CLASS_NAME = "ConnectorType";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;
    @Mock
    private ConnectionHandler<Connection> connectionHandler;
    @Mock
    private ReferenceableHandler<Endpoint> endpointHandler;
    @Mock
    private ConnectorTypeHandler<ConnectorType> connectorTypeHandler;

    @Spy
    @InjectMocks
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @Test
    void insertConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EndpointBuilder mockedEndpointBuilder = mockEndpointBuilder();

        mockDataEngineCommonHandler(true, mockedEndpointBuilder);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, ASSET_GUID, CSV_FILE_TYPE_NAME,
                PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME);

        verify(connectorTypeHandler, times(1)).findConnectorTypes(USER, CSV_FILE_TYPE_NAME,
                SEARCH_STRING_PARAMETER_NAME, START_FROM, PAGE_SIZE, GET_PROPER_CONNECTOR_TYPE_METHOD_NAME);

        String connectionQualifiedName = getConnectionQualifiedName(CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).findEntity(USER, connectionQualifiedName, CONNECTION_TYPE_NAME);

        verify(connectionHandler, times(1)).addAssetConnection(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                ASSET_GUID, ASSET_GUID, CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME, true, null,
                CONNECTOR_PROVIDER_CLASS_NAME, NETWORK_ADDRESS, PROTOCOL, null,
                null, null, UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME);
    }

    @Test
    void updateConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EndpointBuilder mockedEndpointBuilder = mockEndpointBuilder();

        mockDataEngineCommonHandler(false, mockedEndpointBuilder);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, ASSET_GUID, CSV_FILE_TYPE_NAME,
                PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME);

        verify(connectorTypeHandler, times(1)).findConnectorTypes(USER, CSV_FILE_TYPE_NAME,
                SEARCH_STRING_PARAMETER_NAME, START_FROM, PAGE_SIZE, GET_PROPER_CONNECTOR_TYPE_METHOD_NAME);

        String connectionQualifiedName = getConnectionQualifiedName(CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).findEntity(USER, connectionQualifiedName, CONNECTION_TYPE_NAME);

        String endpointQualifiedName = getEndpointQualifiedName(CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).findEntity(USER, endpointQualifiedName, ENDPOINT_TYPE_NAME);

        verify(endpointHandler, times(1)).updateBeanInRepository(USER, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, ENDPOINT_GUID, GUID_PROPERTY_NAME, ENDPOINT_TYPE_GUID, ENDPOINT_TYPE_NAME,
                null, false, UPDATE_ENDPOINT_METHOD_NAME);
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

    @Test
    void removeConnection() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        String methodName = "removeConnection";
        dataEngineConnectionAndEndpointHandler.removeConnection(USER, CONNECTION_GUID, DeleteSemantic.SOFT, EXTERNAL_SOURCE_NAME);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);
       verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
       verify(invalidParameterHandler, times(1)).validateGUID(CONNECTION_GUID, GUID_PROPERTY_NAME, methodName);
        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, CONNECTION_GUID, CONNECTION_TYPE_NAME, EXTERNAL_SOURCE_NAME);
    }

    @Test
    void removeEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        String methodName = "removeEndpoint";
        dataEngineConnectionAndEndpointHandler.removeEndpoint(USER, ENDPOINT_GUID, DeleteSemantic.SOFT, EXTERNAL_SOURCE_NAME);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(ENDPOINT_GUID, GUID_PROPERTY_NAME, methodName);
        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, ENDPOINT_GUID, ENDPOINT_TYPE_NAME, EXTERNAL_SOURCE_NAME);
    }

    private void mockDataEngineCommonHandler(boolean insert, EndpointBuilder endpointBuilder)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        EntityDetail asset = new EntityDetail();
        asset.setGUID(ASSET_GUID);
        when(dataEngineCommonHandler.findEntity(USER, ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME)).thenReturn(Optional.of(asset));

        ConnectorType connectorType = new ConnectorType();
        connectorType.setConnectorFrameworkName(OCF);
        when(connectorTypeHandler.findConnectorTypes(USER, CSV_FILE_TYPE_NAME, SEARCH_STRING_PARAMETER_NAME, START_FROM,
                PAGE_SIZE, GET_PROPER_CONNECTOR_TYPE_METHOD_NAME)).thenReturn(Collections.singletonList(connectorType));

        String connectionQualifiedName = getConnectionQualifiedName(CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME);
        if(insert) {
            when(dataEngineCommonHandler.findEntity(USER, connectionQualifiedName, CONNECTION_TYPE_NAME)).thenReturn(Optional.empty());
            doNothing().when(connectionHandler).addAssetConnection(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                    ASSET_GUID, ASSET_GUID, CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME, true, null,
                    CONNECTOR_PROVIDER_CLASS_NAME, NETWORK_ADDRESS, PROTOCOL, null,
                    null, null, UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME);
            return;
        }

        Optional<EntityDetail> connection = Optional.of(new EntityDetail());
        when(dataEngineCommonHandler.findEntity(USER, connectionQualifiedName, CONNECTION_TYPE_NAME)).thenReturn(connection);

        EntityDetail endpointEntityDetail = new EntityDetail();
        endpointEntityDetail.setGUID(ENDPOINT_GUID);
        Optional<EntityDetail> endpoint = Optional.of(endpointEntityDetail);
        String endpointQualifiedName = getEndpointQualifiedName(CSV_FILE_TYPE_NAME, ASSET_QUALIFIED_NAME);
        when(dataEngineCommonHandler.findEntity(USER, endpointQualifiedName, ENDPOINT_TYPE_NAME)).thenReturn(endpoint);

        doNothing().when(endpointHandler).updateBeanInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                ENDPOINT_GUID, GUID_PROPERTY_NAME, ENDPOINT_TYPE_GUID, ENDPOINT_TYPE_NAME,
                null, false, UPDATE_ENDPOINT_METHOD_NAME);
    }


    private EndpointBuilder mockEndpointBuilder() {
        EndpointBuilder mockedEndpointBuilder = Mockito.mock(EndpointBuilder.class);
        doReturn(mockedEndpointBuilder).when(dataEngineConnectionAndEndpointHandler)
                .getEndpointBuilder(PROTOCOL, NETWORK_ADDRESS, getEndpointQualifiedName(ASSET_QUALIFIED_NAME, CSV_FILE_TYPE_NAME));
        return mockedEndpointBuilder;
    }

    private String getConnectionQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + CONNECTION;
    }

    private String getEndpointQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + ENDPOINT;
    }

}
