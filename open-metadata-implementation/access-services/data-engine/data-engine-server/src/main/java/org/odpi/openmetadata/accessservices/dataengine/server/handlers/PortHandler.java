/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.PortPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.utils.RegexEscapeUtil;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.springframework.util.StringUtils;


/**
 * PortHandler manages Port objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates port entities with wire relationships through the OMRSRepositoryConnector.
 */
public class PortHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public PortHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }

    /**
     * Create the port implementation
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException the bean properties are
     * invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createPortImplementation(String userId, String qualifiedName, String displayName,
                                           PortType portType) throws
                                                              org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException {
        return createPortEntity(userId, qualifiedName, displayName, portType,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Create a PortSchema relationship between a Port and the corresponding SchemaType
     *
     * @param userId         the name of the calling user
     * @param portGUID       the unique identifier of the port
     * @param schemaTypeGUID the unique identifier of the schema type
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
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

    /**
     * Create the Port Alias entity with a PortDelegation relationship
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     * @param delegatesTo   the qualified name of the delegated port
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException the bean properties are
     * invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createPortAliasWithDelegation(String userId, String qualifiedName, String displayName,
                                                PortType portType, String delegatesTo) throws
                                                                                       org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException {
        String methodName = "createPortAliasWithDelegation";

        String portAliasGUID = createPortEntity(userId, qualifiedName, displayName, portType,
                PortPropertiesMapper.PORT_ALIAS_TYPE_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);

        if (!StringUtils.isEmpty(delegatesTo)) {
            EntityDetail delegatedPort = getPortEntityDetailByQualifiedName(userId, delegatesTo);
            String delegatedPortType = getPortType(delegatedPort);
            if (delegatedPortType.equalsIgnoreCase(portType.getName())) {
                addPortDelegationRelationship(userId, portAliasGUID, delegatedPort.getGUID());
            } else {
                DataEngineErrorCode errorCode = DataEngineErrorCode.INVALID_PORT_TYPE;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(delegatedPort.getGUID(), portAliasGUID);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(), this.getClass().getName(), methodName,
                        errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), delegatedPortType);
            }
        }
        return portAliasGUID;
    }

    /**
     * Create a PortDelegation relationship between two ports
     *
     * @param userId         the name of the calling user
     * @param firstPortGUID  the unique identifier of the source port
     * @param secondPortGUID the unique identifier of the target port
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void addPortDelegationRelationship(String userId, String firstPortGUID, String secondPortGUID) throws
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

    private EntityDetail getPortEntityDetailByQualifiedName(String userId, String qualifiedName) throws
                                                                                           org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException {
        final String methodName = "getPortEntityDetailByQualifiedName";

        qualifiedName = RegexEscapeUtil.escapeSpecialGraphRegexCharacters(qualifiedName);

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);


        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        return repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, PortPropertiesMapper.PORT_TYPE_GUID,
                PortPropertiesMapper.PORT_TYPE_NAME, methodName);
    }

    private String getPortType(EntityDetail delegatedPort) {
        if (delegatedPort == null) {
            return null;
        }

        InstanceProperties instanceProperties = delegatedPort.getProperties();
        if (instanceProperties == null) {
            return null;
        }

        EnumPropertyValue portTypeValue =
                (EnumPropertyValue) delegatedPort.getProperties().getPropertyValue(PortPropertiesMapper.PORT_TYPE_PROPERTY_NAME);
        if (portTypeValue == null) {
            return null;
        }

        return portTypeValue.getSymbolicName();
    }

    private String createPortEntity(String userId, String qualifiedName, String displayName, PortType portType,
                                    String entityTypeGUID, String entityTpeName) throws
                                                                                 org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException {
        final String methodName = "createPortEntity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(displayName, PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        PortPropertiesBuilder builder = new PortPropertiesBuilder(qualifiedName, displayName, portType,
                repositoryHelper,
                serviceName, serverName);
        InstanceProperties properties = builder.getInstanceProperties(methodName);

        return repositoryHandler.createEntity(userId, entityTypeGUID, entityTpeName, properties, methodName);
    }

}