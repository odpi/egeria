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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
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

    public String createPortImplementation(String userId, String qualifiedName, String displayName,
                                           PortType portType) throws
                                                              org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException {
        final String methodName = "createPortImplementation";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(userId, qualifiedName, methodName);
        invalidParameterHandler.validateName(userId, displayName, methodName);

        PortImplementationPropertiesBuilder builder = new PortImplementationPropertiesBuilder(qualifiedName,
                displayName, portType, repositoryHelper, serviceName, serverName);
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

    public String createPortAlias(String userId, String qualifiedName, String displayName) throws
                                                                                           org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException {
        final String methodName = "createPortAlias";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(userId, displayName, methodName);
        invalidParameterHandler.validateName(userId, qualifiedName, methodName);

        PortPropertiesBuilder builder = new PortPropertiesBuilder(qualifiedName, displayName, repositoryHelper,
                serviceName, serverName);
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


    /**
     * Return the properties from a discovery engine definition.  The discovery engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId        identifier of calling user
     * @param qualifiedName unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition.
     *
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException one of the parameters is
     * null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public String getPortByQualifiedName(String userId, String qualifiedName) throws
                                                                              org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException {
        final String methodName = "getPortByQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);


        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, PortPropertiesMapper.PORT_TYPE_GUID,
                PortPropertiesMapper.PORT_TYPE_NAME, methodName);

        return retrievedEntity.getGUID();
    }
}