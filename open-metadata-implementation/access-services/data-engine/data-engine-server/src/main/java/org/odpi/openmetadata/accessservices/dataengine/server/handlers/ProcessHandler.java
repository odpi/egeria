/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

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
                                String owner, OwnerType ownerType) throws
                                                                   org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException {

        final String methodName = "createProcess";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);


        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(qualifiedName, processName, displayName,
                description, owner, ownerType, zoneMembership, latestChange, formula, null, null, repositoryHelper,
                serverName, serviceName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        return repositoryHandler.createEntity(userId, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, properties, InstanceStatus.DRAFT, methodName);
    }


    /**
     * Create ProcessPort relationships between a Process asset and the corresponding Ports
     *
     * @param userId      the name of the calling user
     * @param processGUID the unique identifier of the process
     * @param portGUID    the unique identifier of the port
     */
    public void addProcessPortRelationship(String userId, String processGUID, String portGUID) throws
                                                                                               InvalidParameterException,
                                                                                               org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException,
                                                                                               org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException {
        final String methodName = "addProcessPortRelationship";


        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(portGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        repositoryHandler.createRelationship(userId, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, processGUID,
                portGUID, null, methodName);
    }

    /**
     * Update the process instance status to
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
                                                                                               org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException {

        final String methodName = "createProcess";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(guid, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        repositoryHandler.updateEntityStatus(userId, guid, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, instanceStatus, methodName);
    }

}
