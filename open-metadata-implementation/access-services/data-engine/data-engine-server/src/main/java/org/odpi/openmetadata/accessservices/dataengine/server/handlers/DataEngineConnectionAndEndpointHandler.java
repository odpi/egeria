/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.Connection;
import org.odpi.openmetadata.accessservices.dataengine.model.ConnectorType;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Endpoint;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.EndpointBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;


/**
 * DataEngineConnectionAndEndpointHandler manages Connection and Endpoint objects from the property server. It runs server-side
 * in the DataEngine OMAS and creates Connection and Endpoint entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineConnectionAndEndpointHandler {

    private static final Logger log = LoggerFactory.getLogger(DataEngineConnectionAndEndpointHandler.class);
    private static final int START_FROM = 0;
    private static final int PAGE_SIZE = 10;
    private static final String SEARCH_STRING_PARAMETER_NAME = "searchString";
    private static final String OCF = "Open Connector Framework (OCF)";
    private static final String COLON = ":";
    private static final String CONNECTION = " Connection";
    private static final String ENDPOINT = " Endpoint";
    private static final String CONNECTOR_TYPE = " ConnectorType";
    private static final String ASSET_GUID = "assetGUID";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String TYPE_NAME = "typeName";
    private static final String EXTERNAL_SOURCE_GUID = "externalSourceGuid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String NETWORK_ADDRESS = "networkAddress";
    private static final String CONNECTOR_TYPE_GUID_PARAMETER_NAME = "connectorTypeGUID";
    private static final String ENDPOINT_GUID_PARAMETER_NAME = "endpointGUID";
    private static final String CONNECTION_CREATED = "A new Connection for asset [{}] was created. Connection qualified name is [{}] " +
            "and GUID is [{}]. The Connection has relationships to an Endpoint [{}] and a ConnectorType [{}].";
    private static final String ENDPOINT_UPDATED = "The existing Endpoint for asset [{}] was updated. Endpoint qualified name is [{}].";
    private static final String ASSET_NOT_FOUND = "[{}] asset could not be found. Connection and Endpoint creation is aborted.";
    private static final String PROPER_CONNECTOR_TYPE_NOT_FOUND = "A proper ConnectorType for the asset type name [{}] " +
            "could not be found. Connection and Endpoint creation is aborted for asset [{}].";
    private static final String PROPER_CONNECTOR_TYPE_FOUND = "A proper ConnectorType for the asset type name [{}] was found: [{}].";
    private static final String EXISTING_ENDPOINT_NOT_FOUND = "Existing Endpoint [{}] for asset [{}] was not found and could not be updated.";
    private static final String ACCESS_INFORMATION = "Access information to connect to the actual asset: ";
    public static final String CONNECTION_GUID_PARAMETER_NAME = "connectionGUID";


    private final InvalidParameterHandler invalidParameterHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final String serviceName;
    private final String serverName;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final ConnectionHandler<Connection> connectionHandler;
    private final EndpointHandler<Endpoint> endpointHandler;
    private final ConnectorTypeHandler<ConnectorType> connectorTypeHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param serviceName             service name
     * @param serverName              server name
     * @param dataEngineCommonHandler provides common Data Engine OMAS utilities
     * @param connectionHandler       provides utilities specific for manipulating Connection
     * @param endpointHandler         provides utilities specific for manipulating Endpoint
     * @param connectorTypeHandler    provides utilities specific for manipulating ConnectorType
     */
    public DataEngineConnectionAndEndpointHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                                                  String serviceName, String serverName, DataEngineCommonHandler dataEngineCommonHandler,
                                                  ConnectionHandler<Connection> connectionHandler, EndpointHandler<Endpoint> endpointHandler,
                                                  ConnectorTypeHandler<ConnectorType> connectorTypeHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.connectionHandler = connectionHandler;
        this.endpointHandler = endpointHandler;
        this.connectorTypeHandler = connectorTypeHandler;
    }

    /**
     * Constructs an Endpoint linked to a Connection, which in turn links it to provided asset
     *
     * @param assetQualifiedName asset qualified name
     * @param assetGUID          asset GUID
     * @param assetTypeName      asset type name
     * @param protocol           property of Endpoint
     * @param networkAddress     property of Endpoint
     * @param externalSourceGUID external source guid
     * @param externalSourceName external source name
     * @param userID             user id
     *
     * @throws InvalidParameterException  if invalid parameters
     * @throws PropertyServerException    if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public void upsertConnectionAndEndpoint(String assetQualifiedName, String assetGUID, String assetTypeName, String protocol,
                                            String networkAddress, String externalSourceGUID, String externalSourceName,
                                            String userID)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        final String methodName = "upsertConnectionAndEndpoint";
        if (StringUtils.isEmpty(networkAddress)) {
            return;
        }
        validateParameters(assetQualifiedName, assetTypeName, externalSourceGUID, externalSourceName, userID, methodName);
        Optional<EntityDetail> existingAsset = dataEngineCommonHandler.findEntity(userID, assetQualifiedName, assetTypeName);
        if (existingAsset.isEmpty()) {
            log.debug(ASSET_NOT_FOUND, assetQualifiedName);
            return;
        }

        Optional<ConnectorType> optionalConnectorType = getProperConnectorType(assetTypeName, userID);
        if (optionalConnectorType.isEmpty()) {
            log.debug(PROPER_CONNECTOR_TYPE_NOT_FOUND, assetTypeName, assetQualifiedName);
            return;
        }

        ConnectorType connectorType = optionalConnectorType.get();
        log.debug(PROPER_CONNECTOR_TYPE_FOUND, assetTypeName, connectorType.getQualifiedName());

        String connectionQualifiedName = getConnectionQualifiedName(assetTypeName, assetQualifiedName);
        Optional<EntityDetail> existingConnection = dataEngineCommonHandler.findEntity(userID, connectionQualifiedName,
                CONNECTION_TYPE_NAME);
        if (existingConnection.isEmpty()) {
            createConnectionAndRelatedEntities(assetQualifiedName, assetGUID, assetTypeName, protocol, networkAddress,
                    externalSourceGUID, externalSourceName, userID, methodName, connectorType, connectionQualifiedName);
        } else {
            updateEndpoint(protocol, networkAddress, assetTypeName, assetQualifiedName, externalSourceGUID,
                    externalSourceName, userID);
        }
    }

    private void createConnectionAndRelatedEntities(String assetQualifiedName, String assetGUID, String assetTypeName, String protocol, String networkAddress, String externalSourceGUID, String externalSourceName, String userID, String methodName, ConnectorType connectorType, String connectionQualifiedName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String connectorProviderClassName = connectorType.getClass().getSimpleName();
        String connectorTypeQualifiedName = getConnectorTypeQualifiedName(assetTypeName, assetQualifiedName);
        Date now = dataEngineCommonHandler.getNow();
        String connectorTypeGUID = connectorTypeHandler.getConnectorTypeForConnection(userID, externalSourceGUID,
                externalSourceName, null, connectorTypeQualifiedName, connectorTypeQualifiedName,
                null, assetTypeName, null, connectorProviderClassName,
                OpenMetadataAPIMapper.CONNECTOR_FRAMEWORK_NAME_DEFAULT, OpenMetadataAPIMapper.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT,
                null, null, null, null,
                null, null, null, null,
                null,false, false, now, methodName);

        String endpointQualifiedName = getEndpointQualifiedName(assetTypeName, assetQualifiedName);
        String endpointGUID = endpointHandler.createEndpoint(userID, externalSourceGUID, externalSourceName, null,
                endpointQualifiedName, endpointQualifiedName, ACCESS_INFORMATION + networkAddress, networkAddress,
                protocol, null, null, null, null, null,
                null, now, methodName);

        String connectionGUID = connectionHandler.createConnection(userID, externalSourceGUID, externalSourceName, assetGUID, ASSET_GUID,
                null, connectionQualifiedName, connectionQualifiedName, null, null,
                null, null, null, null, null,
                OpenMetadataAPIMapper.CONNECTION_TYPE_NAME, null, connectorTypeGUID, CONNECTOR_TYPE_GUID_PARAMETER_NAME,
                endpointGUID, ENDPOINT_GUID_PARAMETER_NAME, null, null,false,
                false, now, methodName);

        log.debug(CONNECTION_CREATED, assetQualifiedName, connectionQualifiedName, connectionGUID, endpointGUID, connectorTypeGUID);
    }

    private String getConnectorTypeQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + ":" + assetQualifiedName + CONNECTOR_TYPE;
    }

    private String getConnectionQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + CONNECTION;
    }

    private Optional<ConnectorType> getProperConnectorType(String assetTypeName, String userId)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "getProperConnectorType";

        List<ConnectorType> connectorTypes = connectorTypeHandler.findConnectorTypes(userId, assetTypeName,
                SEARCH_STRING_PARAMETER_NAME, START_FROM, PAGE_SIZE, false, false, dataEngineCommonHandler.getNow(),
                methodName);
        if(CollectionUtils.isEmpty(connectorTypes)) {
            return Optional.empty();
        }

        return  connectorTypes.stream().filter(connectorType -> OCF.equals(connectorType.getConnectorFrameworkName())).findAny();
    }

    /**
     * Remove the connection
     *
     * @param userId             the name of the calling user
     * @param connectionGUID     unique identifier of the connection to be removed
     * @param deleteSemantic     the delete semantic
     * @param externalSourceName the external data engine
     * @param externalSourceGUID the external data engine GUID
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeConnection(String userId, String connectionGUID, DeleteSemantic deleteSemantic, String externalSourceName,
                                 String externalSourceGUID) throws FunctionNotSupportedException, InvalidParameterException,
            PropertyServerException, UserNotAuthorizedException {
        final String methodName = "removeConnection";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, GUID_PROPERTY_NAME, methodName);

        connectionHandler.removeConnection(userId, externalSourceGUID, externalSourceName, connectionGUID,
                CONNECTION_GUID_PARAMETER_NAME, false, false, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Remove the endpoint
     *
     * @param userId             the name of the calling user
     * @param endpointGUID     unique identifier of the endpoint to be removed
     * @param deleteSemantic     the delete semantic
     * @param externalSourceName the external data engine
     * @param externalSourceGUID the external data engine GUID
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeEndpoint(String userId, String endpointGUID, DeleteSemantic deleteSemantic, String externalSourceName,
                               String externalSourceGUID) throws FunctionNotSupportedException, InvalidParameterException,
            PropertyServerException, UserNotAuthorizedException {
        final String methodName = "removeEndpoint";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, GUID_PROPERTY_NAME, methodName);

        endpointHandler.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpointGUID,
                ENDPOINT_GUID_PARAMETER_NAME, false, false, dataEngineCommonHandler.getNow(), methodName);
    }

    private void updateEndpoint(String protocol, String networkAddress, String assetTypeName, String assetQualifiedName,
                                String externalSourceGUID, String externalSourceName, String userID)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        final String methodName = "updateEndpoint";
        String endpointQualifiedName = getEndpointQualifiedName(assetTypeName, assetQualifiedName);
        Optional<EntityDetail> existingEndpoint = dataEngineCommonHandler.findEntity(userID, endpointQualifiedName,
                ENDPOINT_TYPE_NAME);

        if (existingEndpoint.isEmpty()) {
            log.debug(EXISTING_ENDPOINT_NOT_FOUND, endpointQualifiedName, assetQualifiedName);
            return;
        }

        String endpointGUID = existingEndpoint.get().getGUID();
        String description = ACCESS_INFORMATION + networkAddress;
        endpointHandler.updateEndpoint(userID, externalSourceGUID, externalSourceName, endpointGUID, ENDPOINT_GUID_PARAMETER_NAME,
                endpointQualifiedName, endpointQualifiedName, description, networkAddress,
                protocol, null, null, null,
                null, true, null, null, false,
                false, dataEngineCommonHandler.getNow(), methodName);

        log.debug(ENDPOINT_UPDATED, assetQualifiedName, endpointQualifiedName);
    }

    private String getEndpointQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + ENDPOINT;
    }

    EndpointBuilder getEndpointBuilder(String protocol, String networkAddress, String qualifiedName) {
        return new EndpointBuilder(protocol, networkAddress, qualifiedName, ENDPOINT_TYPE_GUID,
                ENDPOINT_TYPE_NAME, repositoryHelper, serviceName, serverName);
    }

    private void validateParameters(String qualifiedName, String typeName, String externalSourceGuid, String externalSourceName,
                                    String userId, String methodName) throws InvalidParameterException {
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME, methodName);
        invalidParameterHandler.validateName(typeName, TYPE_NAME, methodName);
        invalidParameterHandler.validateName(externalSourceGuid, EXTERNAL_SOURCE_GUID, methodName);
        invalidParameterHandler.validateName(externalSourceName, EXTERNAL_SOURCE_NAME, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
    }

}
