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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine OMAS
 * and creates process entities with input/output relationships through the OMRSRepositoryConnector.
 */
public class ProcessHandler {
    private static final Logger log = LoggerFactory.getLogger(ProcessHandler.class);

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the process handler with a link to the property server's connector, a link to the metadata collection
     * and this access service's official name.
     *
     * @param serviceName name of this service
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
     * @param processName    the name of the process
     * @param description    the description of the process
     * @param latestChange   the description for the latest change done for the asset
     * @param zoneMembership the list of zones of the process
     * @param displayName    the display name of the process
     *
     * @return the guid of the created process
     **/
    public String createProcess(String userId, String processName, String description, String latestChange,
                                List<String> zoneMembership, String displayName, String formula, String owner,
                                OwnerType ownerType) throws
                                                     org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException {

        final String methodName = "createProcess";
        invalidParameterHandler.validateUserId(userId, methodName);

        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(processName + "::" +
                "qualifiedName", processName, displayName, description, owner, ownerType, zoneMembership,
                latestChange, formula, null, null, repositoryHelper, serverName,
                serviceName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        return repositoryHandler.createEntity(userId, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, properties, methodName);
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
}
