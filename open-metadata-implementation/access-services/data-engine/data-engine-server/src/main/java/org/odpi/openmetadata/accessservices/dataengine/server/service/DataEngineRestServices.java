/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The DataEngineRestServices provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This service provide the functionality to create processes, ports and wire relationships.
 */
public class DataEngineRestServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineRestServices.class);

    private DataEngineErrorHandler exceptionUtil;
    private DataEngineInstanceHandler instanceHandler;

    /**
     * Default constructor
     */
    public DataEngineRestServices() {
        exceptionUtil = new DataEngineErrorHandler();
        instanceHandler = new DataEngineInstanceHandler();
    }

    /**
     * Create the process with ProcessPort relationships
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return the unique identifier (guid) of the created process
     */
    public GUIDResponse createProcess(String userId, String serverName, ProcessRequestBody processRequestBody) {
        log.debug("Calling method: " + "createProcess");

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = new ProcessHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            if (processRequestBody == null) {
                return null;
            }

            String processName = processRequestBody.getName();
            String description = processRequestBody.getDescription();
            String latestChange = processRequestBody.getLatestChange();
            List<String> zoneMembership = processRequestBody.getZoneMembership();
            String parentProcessGuid = processRequestBody.getParentProcessGuid();
            String displayName = processRequestBody.getDisplayName();
            List<String> ports = processRequestBody.getPorts();

            String processGuid = processHandler.createProcess(userId, processName, description, latestChange,
                    zoneMembership, displayName, parentProcessGuid);

            processHandler.addProcessPortRelationships(userId, processGuid, ports);

            response.setGuid(processGuid);

        } catch (TypeErrorException | EntityNotKnownException | FunctionNotSupportedException | StatusNotSupportedException | InvalidParameterException
                | PropertyErrorException | ClassificationErrorException | RepositoryErrorException
                |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);

        } catch (UserNotAuthorizedException |
                PropertyServerException e) {
            exceptionUtil.captureDataEngineException(response, e);
        }
        return response;
    }

    /**
     * Create the deployed api with an asset wire relationship
     *
     * @param serverName             name of server instance to call
     * @param userId                 the name of the calling user
     * @param deployedAPIRequestBody properties of the deployed api
     *
     * @return the unique identifier (guid) of the created deployed api
     */
    public GUIDResponse createDeployedAPI(String userId, String serverName,
                                          DeployedAPIRequestBody deployedAPIRequestBody) {
        log.debug("Calling method: " + "createDeployedAPI");

        GUIDResponse response = new GUIDResponse();

        try {
            DeployedAPIHandler deployedAPIHandler = new DeployedAPIHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            if (deployedAPIRequestBody == null) {
                return null;
            }

            String assetGuid = deployedAPIRequestBody.getAssetGuid();
            String name = deployedAPIRequestBody.getName();
            String description = deployedAPIRequestBody.getDescription();
            String latestChange = deployedAPIRequestBody.getLatestChange();
            List<String> zoneMembership = deployedAPIRequestBody.getZoneMembership();

            String deployedAPIGuid = deployedAPIHandler.createDeployedAPI(userId, name, description, latestChange,
                    zoneMembership);

            response.setGuid(deployedAPIGuid);

            deployedAPIHandler.addAssetWireRelationship(userId, deployedAPIGuid, assetGuid);

        } catch (TypeErrorException | StatusNotSupportedException | InvalidParameterException | PropertyErrorException |
                ClassificationErrorException | RepositoryErrorException |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException |
                EntityNotKnownException | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (UserNotAuthorizedException |
                PropertyServerException e) {
            exceptionUtil.captureDataEngineException(response, e);
        }
        return response;
    }

    /**
     * Create the port with a port interface relationship
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPort(String userId, String serverName, PortRequestBody portRequestBody) {
        final String methodName = "createPort";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            PortHandler portHandler = new PortHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            if (portRequestBody == null) {
                return null;
            }

            String portGuid = portHandler.createPort(userId, portRequestBody.getDisplayName());

            response.setGuid(portGuid);

            portHandler.addPortInterfaceRelationship(userId, portGuid, portRequestBody.getDeployedAPIGuid());

        } catch (TypeErrorException | StatusNotSupportedException | InvalidParameterException | PropertyErrorException |
                ClassificationErrorException | RepositoryErrorException |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException |
                EntityNotKnownException | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);

        } catch (UserNotAuthorizedException |
                PropertyServerException e) {
            exceptionUtil.captureDataEngineException(response, e);
        }
        return response;
    }

    /**
     * Creates ProcessPort relationships for an existing Process.
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portListRequestBody guids of ports and process
     *
     * @return the unique identifier (guid) of the updated process entity
     */
    public GUIDResponse addPortsToProcess(String userId, String serverName, PortListRequestBody portListRequestBody) {
        log.debug("Calling method: " + "adPortsToProcess");

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = new ProcessHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            if (portListRequestBody == null) {
                return null;
            }

            String processGuid = portListRequestBody.getProcessGuid();
            List<String> ports = portListRequestBody.getPorts();

            processHandler.addProcessPortRelationships(userId, processGuid, ports);

            response.setGuid(processGuid);
        } catch (TypeErrorException | StatusNotSupportedException | InvalidParameterException | PropertyErrorException |
                RepositoryErrorException | org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException |
                EntityNotKnownException | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (UserNotAuthorizedException |
                PropertyServerException e) {
            exceptionUtil.captureDataEngineException(response, e);
        }
        return response;
    }
}