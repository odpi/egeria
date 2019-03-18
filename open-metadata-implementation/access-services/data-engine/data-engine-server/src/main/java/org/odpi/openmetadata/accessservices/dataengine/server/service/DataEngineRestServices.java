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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The DataEngineRestServices provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This service provide the functionality to create processes, ports and wire relationships.
 */
public class DataEngineRestServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineRestServices.class);
    private final String DEBUG_LOG_MESSAGE = "Calling method: %s";

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
     * Create the process with ports and wires
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return the unique identifier (guid) of the created process
     */
    public GUIDResponse createProcess(String userId, String serverName, ProcessRequestBody processRequestBody) {
        log.debug(String.format(DEBUG_LOG_MESSAGE, "createProcess"));

        if (processRequestBody == null) {
            return null;
        }

        String processName = processRequestBody.getName();
        String description = processRequestBody.getDescription();
        String latestChange = processRequestBody.getLatestChange();
        List<String> zoneMembership = processRequestBody.getZoneMembership();
        String displayName = processRequestBody.getDisplayName();
        List<String> ports = processRequestBody.getPorts();
        List<String> deployedApis = processRequestBody.getDeployedApis();
        List<String> assets = processRequestBody.getAssets();
        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = new ProcessHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            String processGuid = processHandler.createProcess(userId, processName, description, latestChange,
                    zoneMembership, displayName);

            createPortsAndWiresForAssets(userId, serverName, description, latestChange, zoneMembership, assets,
                    processHandler, processGuid);

            createPortsAndWiresForDeployedApis(userId, serverName, deployedApis, processHandler, processGuid);

            processHandler.addProcessPortRelationships(userId, processGuid, ports);

            response.setGuid(processGuid);

        } catch (TypeErrorException | EntityNotKnownException | FunctionNotSupportedException |
                StatusNotSupportedException | InvalidParameterException | PropertyErrorException |
                ClassificationErrorException | RepositoryErrorException |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);

        } catch (UserNotAuthorizedException | PropertyServerException e) {
            exceptionUtil.captureDataEngineException(response, e);
        }
        return response;
    }

    /**
     * Create the deployed api with an AssetWire relationship
     *
     * @param serverName             name of server instance to call
     * @param userId                 the name of the calling user
     * @param deployedAPIRequestBody properties of the deployed api
     *
     * @return the unique identifier (guid) of the created deployed api
     */
    public GUIDResponse createDeployedAPI(String userId, String serverName,
                                          DeployedAPIRequestBody deployedAPIRequestBody) {
        log.debug(String.format(DEBUG_LOG_MESSAGE, "createDeployedAPI"));

        if (deployedAPIRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            DeployedAPIHandler deployedAPIHandler = new DeployedAPIHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

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
     * Create the port with a PortInterface relationship
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPort(String userId, String serverName, PortRequestBody portRequestBody) {
        log.debug(String.format(DEBUG_LOG_MESSAGE, "createPort"));

        if (portRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            PortHandler portHandler = new PortHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            String portGuid = portHandler.createPort(userId, portRequestBody.getDisplayName());
            response.setGuid(portGuid);

            portHandler.addPortInterfaceRelationship(userId, portGuid, portRequestBody.getDeployedAPIGuid());

        } catch (TypeErrorException | StatusNotSupportedException | InvalidParameterException | PropertyErrorException |
                ClassificationErrorException | RepositoryErrorException |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException |
                EntityNotKnownException | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);

        } catch (UserNotAuthorizedException | PropertyServerException e) {
            exceptionUtil.captureDataEngineException(response, e);
        }
        return response;
    }

    /**
     * Create ProcessPort relationships for an existing Process
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param portListRequestBody guids of ports and process
     *
     * @return the unique identifier (guid) of the updated process entity
     */
    public GUIDResponse addPortsToProcess(String userId, String serverName, String processGuid,
                                          PortListRequestBody portListRequestBody) {
        log.debug(String.format(DEBUG_LOG_MESSAGE, "adPortsToProcess"));
        if (portListRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = new ProcessHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

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

    private void createPortsAndWiresForDeployedApis(String userId, String serverName, List<String> deployedApis,
                                                    ProcessHandler processHandler, String processGuid) throws
                                                                                                       UserNotAuthorizedException,
                                                                                                       TypeErrorException,
                                                                                                       ClassificationErrorException,
                                                                                                       StatusNotSupportedException,
                                                                                                       org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                                                       InvalidParameterException,
                                                                                                       RepositoryErrorException,
                                                                                                       PropertyErrorException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       EntityNotKnownException,
                                                                                                       PropertyServerException {
        List<String> createdPorts = createPorts(userId, serverName, deployedApis);
        processHandler.addProcessPortRelationships(userId, processGuid, createdPorts);
    }

    private void createPortsAndWiresForAssets(String userId, String serverName, String description,
                                              String latestChange, List<String> zoneMembership, List<String> assets,
                                              ProcessHandler processHandler, String processGuid) throws
                                                                                                 UserNotAuthorizedException,
                                                                                                 TypeErrorException,
                                                                                                 ClassificationErrorException,
                                                                                                 StatusNotSupportedException,
                                                                                                 org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 RepositoryErrorException,
                                                                                                 PropertyErrorException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 EntityNotKnownException,
                                                                                                 PropertyServerException {
        List<String> createdDeployedAPIs = createDeployedAPIs(userId, serverName, description, latestChange,
                zoneMembership, assets);
        List<String> createdPorts = createPorts(userId, serverName, createdDeployedAPIs);
        processHandler.addProcessPortRelationships(userId, processGuid, createdPorts);
    }

    private List<String> createPorts(String userId, String serverName, List<String> deployedApis) throws
                                                                                                  UserNotAuthorizedException,
                                                                                                  TypeErrorException,
                                                                                                  ClassificationErrorException,
                                                                                                  StatusNotSupportedException,
                                                                                                  org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                                                  InvalidParameterException,
                                                                                                  RepositoryErrorException,
                                                                                                  PropertyErrorException,
                                                                                                  FunctionNotSupportedException,
                                                                                                  EntityNotKnownException,
                                                                                                  PropertyServerException {
        if (CollectionUtils.isEmpty(deployedApis)) {
            return null;
        }

        List<String> createdPorts = new ArrayList<>();
        PortHandler portHandler = new PortHandler(instanceHandler.getAccessServiceName(),
                instanceHandler.getRepositoryConnector(serverName),
                instanceHandler.getMetadataCollection(serverName));

        for (String deployedApi : deployedApis) {
            String portGuid = portHandler.createPort(userId, " Port for " + deployedApi);
            portHandler.addPortInterfaceRelationship(userId, portGuid, deployedApi);
            createdPorts.add(portGuid);
        }

        return createdPorts;
    }

    private List<String> createDeployedAPIs(String userId, String serverName, String description, String latestChange,
                                            List<String> zoneMembership, List<String> assets) throws
                                                                                              UserNotAuthorizedException,
                                                                                              TypeErrorException,
                                                                                              ClassificationErrorException,
                                                                                              StatusNotSupportedException,
                                                                                              org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              RepositoryErrorException,
                                                                                              PropertyErrorException,
                                                                                              FunctionNotSupportedException,
                                                                                              EntityNotKnownException,
                                                                                              PropertyServerException {

        if (CollectionUtils.isEmpty(assets)) {
            return null;
        }

        List<String> createdDeployedApis = new ArrayList<>();
        DeployedAPIHandler deployedAPIHandler = new DeployedAPIHandler(instanceHandler.getAccessServiceName(),
                instanceHandler.getRepositoryConnector(serverName),
                instanceHandler.getMetadataCollection(serverName));

        for (String asset : assets) {
            String deployedApiGuid = deployedAPIHandler.createDeployedAPI(userId, " DeployedAPI for " + asset,
                    null, null, zoneMembership);
            deployedAPIHandler.addAssetWireRelationship(userId, asset, deployedApiGuid);
            createdDeployedApis.add(deployedApiGuid);
        }
        return createdDeployedApis;
    }
}