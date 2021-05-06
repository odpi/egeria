/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROTOCOL_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;


/**
 * DataEngineConnectionAndEndpointHandler manages Connection and Endpoint objects from the property server. It runs server-side
 * in the DataEngine OMAS and creates Connection and Endpoint entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineConnectionAndEndpointHandler {

    private final InvalidParameterHandler invalidParameterHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final String serviceName;
    private final String serverName;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final ReferenceableHandler<Connection> connectionHandler;
    private final ReferenceableHandler<Endpoint> endpointHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param serviceName service name
     * @param serverName server name
     * @param dataEngineCommonHandler provides common Data Engine Omas utilities
     * @param connectionHandler provides utilities specific for manipulating Connection
     * @param endpointHandler provides utilities specific for manipulating Endpoint
     */
    public DataEngineConnectionAndEndpointHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                                                  String serviceName, String serverName, DataEngineCommonHandler dataEngineCommonHandler,
                                                  ReferenceableHandler<Connection> connectionHandler,
                                                  ReferenceableHandler<Endpoint> endpointHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.connectionHandler = connectionHandler;
        this.endpointHandler = endpointHandler;
    }

    /**
     * Constructs an Endpoint linked to a Connection, which in turn links it to provided asset
     *
     * @param assetQualifiedName asset qualified name
     * @param assetTypeName asset type name
     * @param networkAddress property of Endpoint
     * @param externalSourceGuid external source guid
     * @param externalSourceName external source name
     * @param userId user id
     * @param methodName method name
     *
     * @throws InvalidParameterException if invalid parameters
     * @throws PropertyServerException if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public void upsertConnectionAndEndpoint(String assetQualifiedName, String assetTypeName, String protocol, String networkAddress,
                                            String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        validateParameters(assetQualifiedName, assetTypeName, protocol, networkAddress, externalSourceGuid, externalSourceName,
                userId, methodName);
        Optional<EntityDetail> optionalAsset = dataEngineCommonHandler.findEntity(userId, assetQualifiedName, assetTypeName);
        if(!optionalAsset.isPresent()){
            return;
        }

        String connectionQualifiedName = CONNECTION_TYPE_NAME + "::" + protocol + "::" + networkAddress;
        String connectionGuid = upsertConnection(externalSourceGuid, externalSourceName, userId, methodName, optionalAsset.get(),
                connectionQualifiedName);

        String endpointQualifiedName = ENDPOINT_TYPE_NAME + "::" + protocol + "::" + networkAddress;
        upsertEndpoint(protocol, networkAddress, externalSourceGuid, externalSourceName, userId, methodName, connectionGuid,
                endpointQualifiedName);
    }

    private String upsertConnection(String externalSourceGuid, String externalSourceName, String userId, String methodName,
                                    EntityDetail asset, String connectionQualifiedName)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String connectionGuid;
        Optional<EntityDetail> optionalConnection = dataEngineCommonHandler.findEntity(userId, connectionQualifiedName, CONNECTION_TYPE_NAME);
        if(!optionalConnection.isPresent()){
            connectionGuid = createConnection(connectionQualifiedName, externalSourceGuid, externalSourceName, userId, methodName);
        }else {
            connectionGuid = optionalConnection.get().getGUID();
        }
        dataEngineCommonHandler.upsertExternalRelationship(userId, connectionGuid, asset.getGUID(),
                CONNECTION_TO_ASSET_TYPE_NAME, CONNECTION_TYPE_NAME, externalSourceName, null);
        return connectionGuid;
    }

    private String createConnection(String qualifiedName, String externalSourceGuid, String externalSourceName, String userId,
                                  String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(qualifiedName, CONNECTION_TYPE_GUID, CONNECTION_TYPE_NAME,
                repositoryHelper, serviceName, serverName);
        return connectionHandler.createBeanInRepository(userId, externalSourceGuid, externalSourceName, CONNECTION_TYPE_GUID,
                CONNECTION_TYPE_NAME, qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, connectionBuilder, methodName);
    }

    private void upsertEndpoint(String protocol, String networkAddress, String externalSourceGuid, String externalSourceName,
                                String userId, String methodName, String connectionGuid, String endpointQualifiedName)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String endpointGuid;
        Optional<EntityDetail> optionalEndpoint = dataEngineCommonHandler.findEntity(userId, endpointQualifiedName, ENDPOINT_TYPE_NAME);
        if(optionalEndpoint.isPresent()){
            updateEndpoint(optionalEndpoint.get(), protocol, networkAddress, externalSourceGuid, externalSourceName, userId, methodName);
            endpointGuid = optionalEndpoint.get().getGUID();
        } else {
            endpointGuid = createEndpoint(protocol, networkAddress, endpointQualifiedName, externalSourceGuid,
                    externalSourceName, userId, methodName);
        }
        dataEngineCommonHandler.upsertExternalRelationship(userId, endpointGuid, connectionGuid,
                CONNECTION_ENDPOINT_TYPE_NAME, ENDPOINT_TYPE_NAME, externalSourceName, null);
    }

    private String createEndpoint(String protocol, String networkAddress, String qualifiedName, String externalSourceGuid,
                                  String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        EndpointBuilder endpointBuilder = new EndpointBuilder(protocol, networkAddress, qualifiedName, ENDPOINT_TYPE_GUID,
                ENDPOINT_TYPE_NAME, repositoryHelper, serviceName, serverName);
        return endpointHandler.createBeanInRepository(userId, externalSourceGuid, externalSourceName, ENDPOINT_TYPE_GUID,
                ENDPOINT_TYPE_NAME, qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, endpointBuilder, methodName);
    }

    private void updateEndpoint(EntityDetail endpoint,String protocol, String networkAddress, String externalSourceGuid,
                                String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        PrimitivePropertyValue protocolAsPropertyValue = new PrimitivePropertyValue();
        protocolAsPropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        protocolAsPropertyValue.setPrimitiveValue(protocol);

        PrimitivePropertyValue networkAddressAsPropertyValue = new PrimitivePropertyValue();
        networkAddressAsPropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        networkAddressAsPropertyValue.setPrimitiveValue(networkAddress);

        InstanceProperties properties = endpoint.getProperties();
        properties.setProperty(PROTOCOL_PROPERTY_NAME, protocolAsPropertyValue);
        properties.setProperty(NETWORK_ADDRESS_PROPERTY_NAME, networkAddressAsPropertyValue);

        endpointHandler.updateBeanInRepository(userId, externalSourceGuid, externalSourceName, endpoint.getGUID(),
                "guid", ENDPOINT_TYPE_GUID, ENDPOINT_TYPE_NAME, properties, false, methodName);
    }

    private void validateParameters(String qualifiedName, String typeName,String protocol, String networkAddress,
                                    String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException {

        invalidParameterHandler.validateName(qualifiedName, "qualifiedName", methodName);
        invalidParameterHandler.validateName(typeName, "typeName", methodName);
        invalidParameterHandler.validateName(protocol, "protocol", methodName);
        invalidParameterHandler.validateName(networkAddress, "networkAddress", methodName);
        invalidParameterHandler.validateName(externalSourceGuid, "externalSourceGuid", methodName);
        invalidParameterHandler.validateName(externalSourceName, "externalSourceName", methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
    }

}
