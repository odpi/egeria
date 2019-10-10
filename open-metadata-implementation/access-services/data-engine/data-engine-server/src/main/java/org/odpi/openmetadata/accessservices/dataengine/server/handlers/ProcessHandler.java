/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine OMAS
 * and creates process entities and relationships through the OMRSRepositoryConnector.
 */
public class ProcessHandler {
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
    public ProcessHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                          RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }

    /**
     * Create the process
     *
     * @param userId         the name of the calling user
     * @param qualifiedName  the qualifiedName name of the process
     * @param processName    the name of the process
     * @param description    the description of the process
     * @param latestChange   the description for the latest change done for the process
     * @param zoneMembership the list of zones of the process
     * @param displayName    the display name of the process
     * @param formula        the formula for the process
     * @param owner          the name of the owner for this process
     * @param ownerType      the type of the owner for this process
     *
     * @return unique identifier of the process in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createProcess(String userId, String qualifiedName, String processName, String description,
                                String latestChange, List<String> zoneMembership, String displayName, String formula,
                                String owner, OwnerType ownerType) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException {
        final String methodName = "createProcess";

        validateProcessParameters(userId, qualifiedName, methodName);

        InstanceProperties properties = buildProcessInstanceProperties(qualifiedName, processName, description,
                latestChange, zoneMembership, displayName, formula, owner, ownerType, methodName);

        return repositoryHandler.createEntity(userId, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, properties, InstanceStatus.DRAFT, methodName);
    }

    /**
     * Update the process
     *
     * @param userId         the name of the calling user
     * @param processGUID    the guid of the process to be updated
     * @param qualifiedName  the qualifiedName name of the process
     * @param processName    the name of the process
     * @param description    the description of the process
     * @param latestChange   the description for the latest change done for the process
     * @param zoneMembership the list of zones of the process
     * @param displayName    the display name of the process
     * @param formula        the formula for the process
     * @param owner          the name of the owner for this process
     * @param ownerType      the type of the owner for this process
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updateProcess(String userId, String processGUID, String qualifiedName, String processName,
                              String description, String latestChange, List<String> zoneMembership, String displayName,
                              String formula, String owner, OwnerType ownerType) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException {
        final String methodName = "updateProcess";

        validateProcessParameters(userId, qualifiedName, methodName);

        InstanceProperties properties = buildProcessInstanceProperties(qualifiedName, processName, description,
                latestChange, zoneMembership, displayName, formula, owner, ownerType, methodName);

        repositoryHandler.updateEntity(userId, processGUID, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, properties, methodName);
    }

    /**
     * Find out if the Process object is already stored in the repository. It uses the fully qualified name
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
    public String findProcess(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                          PropertyServerException,
                                                                          InvalidParameterException {
        final String methodName = "findProcess";

        invalidParameterHandler.validateUserId(userId, methodName);

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties,
                ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName);

        if (retrievedEntity == null) {
            return null;
        }

        return retrievedEntity.getGUID();
    }

    /**
     * Create ProcessPort relationships between a Process asset and the corresponding Ports. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId      the name of the calling user
     * @param processGUID the unique identifier of the process
     * @param portGUID    the unique identifier of the port
     */
    public void addProcessPortRelationship(String userId, String processGUID, String portGUID) throws
                                                                                               InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException {
        final String methodName = "addProcessPortRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(portGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, processGUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, portGUID, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, methodName);

        if (relationship == null) {
            repositoryHandler.createRelationship(userId, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, processGUID,
                    portGUID, null, methodName);
        }
    }

    /**
     * Update the process instance status
     *
     * @param userId         the name of the calling user
     * @param guid           the guid name of the process
     * @param instanceStatus the status of the process
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updateProcessStatus(String userId, String guid, InstanceStatus instanceStatus) throws
                                                                                               InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException {

        final String methodName = "createProcess";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(guid, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        repositoryHandler.updateEntityStatus(userId, guid, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, instanceStatus, methodName);
    }

    /**
     * Retrieve all port objects of portTypeName that are connected to the process
     *
     * @param userId       the name of the calling user
     * @param processGUID  the unigue identifier of the process
     * @param portTypeName the port type name to search for. Can be PortAlias or PortImplementation
     *
     * @return A set of unique identifiers for the retrieved ports or an empty set
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Set<String> getPortsForProcess(String userId, String processGUID, String portTypeName) throws
                                                                                                  InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException {
        final String methodName = "getPortsForProcess";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForRelationshipType(userId, processGUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, 0, 0, methodName);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().map(InstanceHeader::getGUID).collect(Collectors.toSet());
    }

    private InstanceProperties buildProcessInstanceProperties(String qualifiedName, String processName,
                                                              String description, String latestChange,
                                                              List<String> zoneMembership, String displayName,
                                                              String formula, String owner, OwnerType ownerType,
                                                              String methodName) throws
                                                                                 InvalidParameterException {

        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(qualifiedName, processName, displayName,
                description, owner, ownerType, zoneMembership, latestChange, formula, null, null, repositoryHelper,
                serverName, serviceName);

        return builder.getInstanceProperties(methodName);
    }

    private void validateProcessParameters(String userId, String qualifiedName, String methodName) throws
                                                                                                   InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
    }
}
