/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.PortPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


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
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                   name of this service
     * @param serverName                    name of the local server
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param repositoryHandler             manages calls to the repository services
     * @param repositoryHelper              provides utilities for manipulating the repository services objects
     * @param dataEngineRegistrationHandler provides calls for retrieving external data engine guid
     */
    public PortHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                       DataEngineRegistrationHandler dataEngineRegistrationHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
    }

    /**
     * Create the port implementation
     *
     * @param userId             the name of the calling user
     * @param portImplementation the port implementation values
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createPortImplementation(String userId, PortImplementation portImplementation,
                                           String externalSourceName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException {
        return createPort(userId, portImplementation.getQualifiedName(), portImplementation.getDisplayName(),
                portImplementation.getPortType(), PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME,
                externalSourceName);
    }

    /**
     * Create the port alias
     *
     * @param userId             the name of the calling user
     * @param portAlias          the port alias values
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createPortAlias(String userId, PortAlias portAlias, String externalSourceName) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        return createPort(userId, portAlias.getQualifiedName(), portAlias.getDisplayName(), portAlias.getPortType(),
                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, externalSourceName);
    }

    /**
     * Update the port implementation
     *
     * @param userId             the name of the calling user
     * @param portGUID           the unique identifier of the port
     * @param portImplementation the port implementation new values
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updatePortImplementation(String userId, String portGUID, PortImplementation portImplementation) throws
                                                                                                                InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException {
        updatePort(userId, portGUID, portImplementation.getQualifiedName(), portImplementation.getDisplayName(),
                portImplementation.getPortType(), PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Update the port alias
     *
     * @param userId    the name of the calling user
     * @param portGUID  the unique identifier of the port
     * @param portAlias the port alias new values
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updatePortAlias(String userId, String portGUID, PortAlias portAlias) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException {
        updatePort(userId, portGUID, portAlias.getQualifiedName(), portAlias.getDisplayName(), portAlias.getPortType(),
                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
    }

    /**
     * Create the port
     *
     * @param userId             the name of the calling user
     * @param qualifiedName      the qualifiedName name of the port
     * @param displayName        the display name of the port
     * @param portType           the type of the port
     * @param entityTpeName      the type name
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the port in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String createPort(String userId, String qualifiedName, String displayName, PortType portType,
                              String entityTpeName, String externalSourceName)
            throws InvalidParameterException, UserNotAuthorizedException,
                   PropertyServerException {
        final String methodName = "createPort";

        InstanceProperties properties = buildPortInstanceProperties(userId, qualifiedName, displayName, portType,
                methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId,
                externalSourceName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTpeName);
        return repositoryHandler.createExternalEntity(userId, entityTypeDef.getGUID(), entityTypeDef.getName(),
                externalSourceGUID, externalSourceName, properties, methodName);
    }

    /**
     * Update the port
     *
     * @param userId         the name of the calling user
     * @param portGUID       the guid of the port to be updated
     * @param qualifiedName  the qualifiedName name of the port
     * @param displayName    the display name of the port
     * @param portType       the type of the port
     * @param entityTypeName the type name
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void updatePort(String userId, String portGUID, String qualifiedName, String displayName, PortType portType,
                            String entityTypeName) throws InvalidParameterException, UserNotAuthorizedException,
                                                          PropertyServerException {
        final String methodName = "updatePort";

        InstanceProperties properties = buildPortInstanceProperties(userId, qualifiedName, displayName, portType,
                methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        repositoryHandler.updateEntity(userId, portGUID, entityTypeDef.getGUID(), entityTypeDef.getName(), properties,
                methodName);
    }

    /**
     * Create a PortSchema relationship between a Port and the corresponding SchemaType. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId             the name of the calling user
     * @param portGUID           the unique identifier of the port
     * @param schemaTypeGUID     the unique identifier of the schema type
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addPortSchemaRelationship(String userId, String portGUID, String schemaTypeGUID,
                                          String externalSourceName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException {

        final String methodName = "addPortSchemaRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId,
                externalSourceName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME);
        createRelationship(userId, portGUID, schemaTypeGUID, PortPropertiesMapper.PORT_TYPE_NAME, relationshipTypeDef,
                methodName, externalSourceGUID, externalSourceName);

    }

    /**
     * Retrieve the schema type that is linked to the port
     *
     * @param userId   the name of the calling user
     * @param portGUID the unique identifier of the port
     *
     * @return The unique identifier for the retrieved schema type or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String findSchemaTypeForPort(String userId, String portGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException {
        final String methodName = "findSchemaTypeForPort";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId, portGUID,
                PortPropertiesMapper.PORT_TYPE_NAME, relationshipTypeDef.getGUID(), relationshipTypeDef.getName(),
                methodName);

        if (entity == null) {
            return null;
        }

        return entity.getGUID();
    }

    /**
     * Create a PortDelegation relationship between two ports. Verifies that the relationship is not present
     * before creating it
     *
     * @param userId             the name of the calling user
     * @param portGUID           the unique identifier of the source port
     * @param portType           the type of the source port
     * @param delegatesTo        the unique identifier of the target port
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addPortDelegationRelationship(String userId, String portGUID, PortType portType, String delegatesTo,
                                              String externalSourceName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException {

        final String methodName = "addPortDelegationRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(delegatesTo, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId,
                externalSourceName);

        EntityDetail delegatedPort = getPortEntityDetailByQualifiedName(userId, delegatesTo);
        String delegatedPortType = getPortType(delegatedPort);

        if (delegatedPortType.equalsIgnoreCase(portType.getName())) {
            TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId,
                    PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME);

            createRelationship(userId, portGUID, delegatedPort.getGUID(), PortPropertiesMapper.PORT_TYPE_NAME,
                    relationshipTypeDef,
                    methodName, externalSourceGUID, externalSourceName);
        } else {
            throwInvalidParameterException(portGUID, methodName, delegatedPort, delegatedPortType);
        }
    }

    /**
     * Remove the port
     *
     * @param userId         the name of the calling user
     * @param portGUID       the unique identifier of the port to be removed
     * @param entityTypeName the type name
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removePort(String userId, String portGUID, String entityTypeName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException {
        final String methodName = "removePort";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        repositoryHandler.removeEntity(userId, portGUID, entityTypeDef.getGUID(), entityTypeDef.getName(),
                null, null, methodName);
    }

    /**
     * Find out if the Port Implementation object is already stored in the repository. It uses the fully qualified name
     * to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return unique identifier of the process or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String findPortImplementation(String userId, String qualifiedName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException {
        return findPort(userId, qualifiedName, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Find out if the Port Alias object is already stored in the repository. It uses the fully qualified name
     * to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return unique identifier of the process or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String findPortAlias(String userId, String qualifiedName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException {
        return findPort(userId, qualifiedName, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
    }

    /**
     * Find out if the Port object is already stored in the repository. It uses the fully qualified name
     * to retrieve the entity
     *
     * @param userId         the name of the calling user
     * @param qualifiedName  the qualifiedName name of the process to be searched
     * @param entityTypeName the type name
     *
     * @return unique identifier of the process or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findPort(String userId, String qualifiedName, String entityTypeName) throws
                                                                                        InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException {
        final String methodName = "findPort";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, entityTypeDef.getGUID(),
                entityTypeDef.getName(), methodName);

        if (retrievedEntity == null) {
            return null;
        }

        return retrievedEntity.getGUID();
    }


    private EntityDetail getPortEntityDetailByQualifiedName(String userId, String qualifiedName) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        final String methodName = "getPortEntityDetailByQualifiedName";

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, PortPropertiesMapper.PORT_TYPE_NAME);
        return repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, entityTypeDef.getGUID(),
                entityTypeDef.getName(), methodName);
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

    private void createRelationship(String userId, String firstGUID, String secondGUID, String firstEntityTypeName,
                                    TypeDef relationshipTypeDef, String methodName, String externalSourceGUID,
                                    String externalSourceName)
            throws UserNotAuthorizedException, PropertyServerException {

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, firstGUID,
                firstEntityTypeName, secondGUID, relationshipTypeDef.getGUID(), relationshipTypeDef.getName(),
                methodName);

        if (relationship == null) {
            repositoryHandler.createExternalRelationship(userId, relationshipTypeDef.getGUID(), externalSourceGUID,
                    externalSourceName, firstGUID, secondGUID, null, methodName);
        }
    }

    private InstanceProperties buildPortInstanceProperties(String userId, String qualifiedName, String displayName,
                                                           PortType portType, String methodName) throws
                                                                                                 InvalidParameterException {
        validatePortParameters(userId, qualifiedName, displayName, methodName);

        PortPropertiesBuilder builder = new PortPropertiesBuilder(qualifiedName, displayName, portType,
                repositoryHelper, serviceName, serverName);
        return builder.getInstanceProperties(methodName);
    }

    private void validatePortParameters(String userId, String qualifiedName, String displayName, String methodName) throws
                                                                                                                    InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(displayName, PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
    }

    private void throwInvalidParameterException(String portGUID, String methodName, EntityDetail delegatedPort,
                                                String delegatedPortType) throws InvalidParameterException {
        DataEngineErrorCode errorCode = DataEngineErrorCode.INVALID_PORT_TYPE;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(delegatedPort.getGUID(), portGUID);

        throw new InvalidParameterException(errorCode.getHttpErrorCode(), this.getClass().getName(), methodName,
                errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), delegatedPortType);
    }
}