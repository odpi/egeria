/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
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

    private static final int START_FROM = 0;
    private static final int PAGE_SIZE = 10;
    private static final String SEARCH_STRING_PARAMETER_NAME = "searchString";
    private static final String OCF = "Open Connector Framework (OCF)";
    private static final String COLON = ":";
    private static final String CONNECTION = " Connection";
    private static final String ASSET_GUID = "assetGUID";
    private static final String ENDPOINT = " Endpoint";
    public static final String GUID = "guid";
    public static final String QUALIFIED_NAME = "qualifiedName";
    public static final String TYPE_NAME = "typeName";
    public static final String EXTERNAL_SOURCE_GUID = "externalSourceGuid";
    public static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    public static final String PROTOCOL = "protocol";
    public static final String NETWORK_ADDRESS = "networkAddress";

    private final InvalidParameterHandler invalidParameterHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final String serviceName;
    private final String serverName;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final ConnectionHandler<Connection> connectionHandler;
    private final ReferenceableHandler<Endpoint> endpointHandler;
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
                                                  ConnectionHandler<Connection> connectionHandler, ReferenceableHandler<Endpoint> endpointHandler,
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
        validateParameters(assetQualifiedName, assetTypeName, protocol, networkAddress, externalSourceGUID, externalSourceName,
                userID);
        Optional<EntityDetail> existingAsset = dataEngineCommonHandler.findEntity(userID, assetQualifiedName, assetTypeName);
        if (existingAsset.isEmpty()) {
            return;
        }

        Optional<ConnectorType> properConnectorType = getProperConnectorType(assetTypeName, userID);
        if (properConnectorType.isEmpty()) {
            return;
        }

        String connectionQualifiedName = getConnectionQualifiedName(assetTypeName, assetQualifiedName);
        Optional<EntityDetail> existingConnection = dataEngineCommonHandler.findEntity(userID, connectionQualifiedName,
                CONNECTION_TYPE_NAME);
        if (existingConnection.isEmpty()) {
            String connectorTypeClassName = properConnectorType.get().getClass().getSimpleName();
            connectionHandler.addAssetConnection(userID, externalSourceGUID, externalSourceName, assetGUID, ASSET_GUID,
                    assetTypeName, assetQualifiedName, true, null,
                    connectorTypeClassName, networkAddress, protocol, null,
                    null, null, methodName);
        } else {
            updateEndpoint(protocol, networkAddress, assetTypeName, assetQualifiedName, externalSourceGUID,
                    externalSourceName, userID);
        }
    }

    private String getConnectionQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + CONNECTION;
    }

    private Optional<ConnectorType> getProperConnectorType(String assetTypeName, String userId)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "getProperConnectorType";

        List<ConnectorType> connectorTypes = connectorTypeHandler.findConnectorTypes(userId, assetTypeName,
                SEARCH_STRING_PARAMETER_NAME, START_FROM, PAGE_SIZE, methodName);
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
     * @param externalSourceName the external data engine
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeConnection(String userId, String connectionGUID, DeleteSemantic deleteSemantic, String externalSourceName) throws
                                                                                                                                 FunctionNotSupportedException,
                                                                                                                                 InvalidParameterException,
                                                                                                                                 PropertyServerException,
                                                                                                                                 UserNotAuthorizedException {
        final String methodName = "removeConnection";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, GUID_PROPERTY_NAME, methodName);

        dataEngineCommonHandler.removeEntity(userId, connectionGUID, CONNECTION_TYPE_NAME, externalSourceName);
    }

    /**
     * Remove the endpoint
     *
     * @param userId             the name of the calling user
     * @param endpointGUID     unique identifier of the endpoint to be removed
     * @param externalSourceName the external data engine
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeEndpoint(String userId, String endpointGUID, DeleteSemantic deleteSemantic, String externalSourceName) throws
                                                                                                                             FunctionNotSupportedException,
                                                                                                                             InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException {
        final String methodName = "removeEndpoint";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, GUID_PROPERTY_NAME, methodName);

        dataEngineCommonHandler.removeEntity(userId, endpointGUID, ENDPOINT_TYPE_NAME, externalSourceName);
    }

    private void updateEndpoint(String protocol, String networkAddress, String assetTypeName, String assetQualifiedName,
                                String externalSourceGUID, String externalSourceName, String userID)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        final String methodName = "updateEndpoint";
        String endpointQualifiedName = getEndpointQualifiedName(assetTypeName, assetQualifiedName);
        Optional<EntityDetail> existingEndpoint = dataEngineCommonHandler.findEntity(userID, endpointQualifiedName,
                ENDPOINT_TYPE_NAME);
        if (existingEndpoint.isPresent()) {
            EndpointBuilder endpointBuilder = getEndpointBuilder(protocol, networkAddress, endpointQualifiedName);

            String endpointGUID = existingEndpoint.get().getGUID();
            endpointHandler.updateBeanInRepository(userID, externalSourceGUID, externalSourceName,
                    endpointGUID, GUID, ENDPOINT_TYPE_GUID, ENDPOINT_TYPE_NAME,
                    endpointBuilder.getInstanceProperties(methodName), false, methodName);
        }
    }

    private String getEndpointQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + ENDPOINT;
    }

    EndpointBuilder getEndpointBuilder(String protocol, String networkAddress, String qualifiedName) {
        return new EndpointBuilder(protocol, networkAddress, qualifiedName, ENDPOINT_TYPE_GUID,
                ENDPOINT_TYPE_NAME, repositoryHelper, serviceName, serverName);
    }

    private void validateParameters(String qualifiedName, String typeName, String protocol, String networkAddress,
                                    String externalSourceGuid, String externalSourceName, String userId)
            throws InvalidParameterException {

        final String methodName = "validateParameters";
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME, methodName);
        invalidParameterHandler.validateName(typeName, TYPE_NAME, methodName);
        invalidParameterHandler.validateName(protocol, PROTOCOL, methodName);
        invalidParameterHandler.validateName(networkAddress, NETWORK_ADDRESS, methodName);
        invalidParameterHandler.validateName(externalSourceGuid, EXTERNAL_SOURCE_GUID, methodName);
        invalidParameterHandler.validateName(externalSourceName, EXTERNAL_SOURCE_NAME, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
    }

}
