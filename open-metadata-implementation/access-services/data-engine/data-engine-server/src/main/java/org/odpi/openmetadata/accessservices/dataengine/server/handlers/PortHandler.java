/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.PortImplementationPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.PortPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * PortHandler manages Port objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates port entities with wire relationships through the OMRSRepositoryConnector.
 */
public class PortHandler {
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the port handler with a link to the property server's connector, a link to the metadata collection
     * and this access service's official name.
     *
     * @param serviceName name of this service
     */
    public PortHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }

    public String createPortImplementation(String userId, String displayName, PortType portType) throws
                                                                                                 org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        final String methodName = "createPortImplementation";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(userId, displayName, methodName);

        //TODO buildQualifiedName
        PortImplementationPropertiesBuilder builder = new PortImplementationPropertiesBuilder(displayName + ":: " +
                "qualifiedName", displayName, portType, repositoryHelper, serviceName, serverName);
        InstanceProperties properties = builder.getInstanceProperties(methodName);

        return repositoryHandler.createEntity(userId, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, properties, methodName);
    }

    /**
     * Create PortInterface relationships between a Port and the corresponding DeployedAPI
     *
     * @param userId         the name of the calling user
     * @param portGUID       the unique identifier of the port
     * @param schemaTypeGUID the unique identifier of the deployed api
     */
    public void addPortSchemaRelationship(String userId, String portGUID, String schemaTypeGUID) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {

        final String methodName = "addPortSchemaRelationship";

        validateRelationshipParameters(userId, portGUID, schemaTypeGUID, methodName);

        repositoryHandler.createRelationship(userId, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, portGUID,
                schemaTypeGUID, null, methodName);
    }

    public String createPortAlias(String displayName, String userId) throws
                                                                     org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException {
        final String methodName = "createPortAlias";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(userId, displayName, methodName);

        //TODO buildQualifiedName
        PortPropertiesBuilder builder = new PortPropertiesBuilder(displayName +
                ":: qualifiedName", displayName, repositoryHelper, serviceName, serverName);
        InstanceProperties properties = builder.getInstanceProperties(methodName);

        return repositoryHandler.createEntity(userId, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID,
                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, properties, methodName);
    }

    public void addPortDelegationRelationship(String userId, String firstPortGUID, String secondPortGUID) throws
                                                                                                          InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException {
        final String methodName = "addPortDelegationRelationship";

        validateRelationshipParameters(userId, firstPortGUID, secondPortGUID, methodName);

        repositoryHandler.createRelationship(userId, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUIUD, firstPortGUID,
                secondPortGUID, null, methodName);
    }

    private void validateRelationshipParameters(String userId, String firstGUID, String secondGUID,
                                                String methodName) throws InvalidParameterException {

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(firstGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(secondGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
    }
}