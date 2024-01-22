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
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineConnectionAndEndpointHandlerTest {

    private static final String USER = "user";
    private static final String ASSET_GUID = "assetGUID";
    private static final String CONNECTION_GUID = "connectionGUID";
    private static final String ENDPOINT_GUID = "endpointGUID";
    private static final String CONNECTOR_TYPE_GUID = "connectorTypeGUID";
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
    private static final String CONNECTOR_TYPE = " ConnectorType";
    private static final String GET_PROPER_CONNECTOR_TYPE_METHOD_NAME = "getProperConnectorType";
    private static final String UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME = "upsertConnectionAndEndpoint";
    private static final String UPDATE_ENDPOINT_METHOD_NAME = "updateEndpoint";
    private static final String CONNECTOR_PROVIDER_CLASS_NAME = "ConnectorType";
    private static final String ACCESS_INFORMATION = "Access information to connect to the actual asset: ";
    private static final String CONNECTOR_TYPE_GUID_PARAMETER_NAME = "connectorTypeGUID";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;
    @Mock
    private ConnectionHandler<Connection> connectionHandler;
    @Mock
    private EndpointHandler<Endpoint> endpointHandler;
    @Mock
    private ConnectorTypeHandler<ConnectorType> connectorTypeHandler;

    @Spy
    @InjectMocks
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @Test
    void insertConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EndpointBuilder mockedEndpointBuilder = mockEndpointBuilder();

        mockDataEngineCommonHandler(true, mockedEndpointBuilder);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, ASSET_GUID, OpenMetadataType.CSV_FILE.typeName,
                PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, OpenMetadataType.CSV_FILE.typeName);

        verify(connectorTypeHandler, times(1)).findConnectorTypes(USER, OpenMetadataType.CSV_FILE.typeName,
                SEARCH_STRING_PARAMETER_NAME, START_FROM, PAGE_SIZE, false, false, null, GET_PROPER_CONNECTOR_TYPE_METHOD_NAME);

        String connectionQualifiedName = getConnectionQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).findEntity(USER, connectionQualifiedName, OpenMetadataType.CONNECTION_TYPE_NAME);

        String connectorTypeQualifiedName = getConnectorTypeQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        verify(connectorTypeHandler, times(1)).getConnectorTypeForConnection(USER, EXTERNAL_SOURCE_GUID,
                                                                             EXTERNAL_SOURCE_NAME, null, connectorTypeQualifiedName, connectorTypeQualifiedName,
                                                                             null, OpenMetadataType.CSV_FILE.typeName, null, CONNECTOR_PROVIDER_CLASS_NAME,
                                                                             OpenMetadataType.CONNECTOR_FRAMEWORK_NAME_DEFAULT, OpenMetadataType.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT,
                                                                             null, null, null, null,
                                                                             null, null, null, null,
                                                                             null, false, false, null, UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME);

        String endpointQualifiedName = getEndpointQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        String description = ACCESS_INFORMATION + NETWORK_ADDRESS;
        verify(endpointHandler, times(1)).createEndpoint(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                null, endpointQualifiedName, endpointQualifiedName, description, NETWORK_ADDRESS, PROTOCOL,
                null, null, null, null, null, null, null, UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME);

        verify(connectionHandler, times(1)).createConnection(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                                                             ASSET_GUID, ASSET_GUID, null, connectionQualifiedName, connectionQualifiedName, null, null,
                                                             null, null, null, null, null,
                                                             OpenMetadataType.CONNECTION_TYPE_NAME, null, CONNECTOR_TYPE_GUID, CONNECTOR_TYPE_GUID_PARAMETER_NAME, ENDPOINT_GUID,
                                                             ENDPOINT_GUID, null, null, false, false, null, UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME);
    }

    @Test
    void updateConnectionAndEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EndpointBuilder mockedEndpointBuilder = mockEndpointBuilder();

        mockDataEngineCommonHandler(false, mockedEndpointBuilder);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(ASSET_QUALIFIED_NAME, ASSET_GUID, OpenMetadataType.CSV_FILE.typeName,
                PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, ASSET_QUALIFIED_NAME, OpenMetadataType.CSV_FILE.typeName);

        verify(connectorTypeHandler, times(1)).findConnectorTypes(USER, OpenMetadataType.CSV_FILE.typeName,
                SEARCH_STRING_PARAMETER_NAME, START_FROM, PAGE_SIZE, false, false, null, GET_PROPER_CONNECTOR_TYPE_METHOD_NAME);

        String connectionQualifiedName = getConnectionQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).findEntity(USER, connectionQualifiedName, OpenMetadataType.CONNECTION_TYPE_NAME);

        String endpointQualifiedName = getEndpointQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).findEntity(USER, endpointQualifiedName, OpenMetadataType.ENDPOINT_TYPE_NAME);

        String description = ACCESS_INFORMATION + NETWORK_ADDRESS;
        verify(endpointHandler, times(1)).updateEndpoint(USER, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, ENDPOINT_GUID, ENDPOINT_GUID, endpointQualifiedName, endpointQualifiedName,
                description, NETWORK_ADDRESS, PROTOCOL, null, null, null,
                null, true, null, null, false, false, null, UPDATE_ENDPOINT_METHOD_NAME);
    }

    /**
     *  Issue -> https://github.com/mockito/mockito/issues/1066
     *  Unable to properly inject generics; connectionHandler mock is also used for endpointHandler even though a separate mock is defined
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
        dataEngineConnectionAndEndpointHandler.removeConnection(USER, CONNECTION_GUID, DeleteSemantic.SOFT,
                EXTERNAL_SOURCE_NAME, EXTERNAL_SOURCE_GUID);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(CONNECTION_GUID, OpenMetadataProperty.GUID.name, methodName);
        verify(connectionHandler, times(1)).removeConnection(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                CONNECTION_GUID, CONNECTION_GUID, false, false, null, methodName);
    }

    @Test
    void removeEndpoint() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        String methodName = "removeEndpoint";
        dataEngineConnectionAndEndpointHandler.removeEndpoint(USER, ENDPOINT_GUID, DeleteSemantic.SOFT, EXTERNAL_SOURCE_NAME, EXTERNAL_SOURCE_GUID);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(ENDPOINT_GUID, OpenMetadataProperty.GUID.name, methodName);
        verify(endpointHandler, times(1)).removeEndpoint(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                ENDPOINT_GUID, ENDPOINT_GUID, false, false, null, methodName);
    }

    private void mockDataEngineCommonHandler(boolean insert, EndpointBuilder endpointBuilder)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        EntityDetail asset = new EntityDetail();
        asset.setGUID(ASSET_GUID);
        when(dataEngineCommonHandler.findEntity(USER, ASSET_QUALIFIED_NAME, OpenMetadataType.CSV_FILE.typeName)).thenReturn(Optional.of(asset));

        ConnectorType connectorType = new ConnectorType();
        connectorType.setConnectorFrameworkName(OCF);
        when(connectorTypeHandler.findConnectorTypes(USER, OpenMetadataType.CSV_FILE.typeName, SEARCH_STRING_PARAMETER_NAME, START_FROM,
                PAGE_SIZE, false, false, null, GET_PROPER_CONNECTOR_TYPE_METHOD_NAME)).thenReturn(Collections.singletonList(connectorType));

        String connectionQualifiedName = getConnectionQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        if(insert) {
            when(dataEngineCommonHandler.findEntity(USER, connectionQualifiedName, OpenMetadataType.CONNECTION_TYPE_NAME)).thenReturn(Optional.empty());

            String connectorTypeQualifiedName = getConnectorTypeQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
            when(connectorTypeHandler.getConnectorTypeForConnection(USER, EXTERNAL_SOURCE_GUID,
                                                                    EXTERNAL_SOURCE_NAME, null, connectorTypeQualifiedName, connectorTypeQualifiedName,
                                                                    null, OpenMetadataType.CSV_FILE.typeName, null, CONNECTOR_PROVIDER_CLASS_NAME,
                                                                    OpenMetadataType.CONNECTOR_FRAMEWORK_NAME_DEFAULT, OpenMetadataType.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT,
                                                                    null, null, null, null,
                                                                    null, null, null, null,
                                                                    null, false, false, null, UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME)).thenReturn(CONNECTOR_TYPE_GUID);

            String endpointQualifiedName = getEndpointQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
            String description = ACCESS_INFORMATION + NETWORK_ADDRESS;
            when(endpointHandler.createEndpoint(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, null,
                    endpointQualifiedName, endpointQualifiedName, description, NETWORK_ADDRESS, PROTOCOL,
                    null, null, null, null, null, null, null,
                    UPSERT_CONNECTION_AND_ENDPOINT_METHOD_NAME)).thenReturn(ENDPOINT_GUID);

            return;
        }

        Optional<EntityDetail> connection = Optional.of(new EntityDetail());
        when(dataEngineCommonHandler.findEntity(USER, connectionQualifiedName, OpenMetadataType.CONNECTION_TYPE_NAME)).thenReturn(connection);

        EntityDetail endpointEntityDetail = new EntityDetail();
        endpointEntityDetail.setGUID(ENDPOINT_GUID);
        Optional<EntityDetail> endpoint = Optional.of(endpointEntityDetail);
        String endpointQualifiedName = getEndpointQualifiedName(OpenMetadataType.CSV_FILE.typeName, ASSET_QUALIFIED_NAME);
        when(dataEngineCommonHandler.findEntity(USER, endpointQualifiedName, OpenMetadataType.ENDPOINT_TYPE_NAME)).thenReturn(endpoint);

        doNothing().when(endpointHandler).updateBeanInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                ENDPOINT_GUID, OpenMetadataProperty.GUID.name, OpenMetadataType.ENDPOINT_TYPE_GUID, OpenMetadataType.ENDPOINT_TYPE_NAME,
                null, false, UPDATE_ENDPOINT_METHOD_NAME);
    }


    private EndpointBuilder mockEndpointBuilder() {
        EndpointBuilder mockedEndpointBuilder = Mockito.mock(EndpointBuilder.class);
        doReturn(mockedEndpointBuilder).when(dataEngineConnectionAndEndpointHandler)
                .getEndpointBuilder(PROTOCOL, NETWORK_ADDRESS, getEndpointQualifiedName(ASSET_QUALIFIED_NAME, OpenMetadataType.CSV_FILE.typeName));
        return mockedEndpointBuilder;
    }

    private String getConnectionQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + CONNECTION;
    }

    private String getEndpointQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + ENDPOINT;
    }

    private String getConnectorTypeQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + ":" + assetQualifiedName + CONNECTOR_TYPE;
    }

}
