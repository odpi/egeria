/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.dataengine.rest.GUIDResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The DataEngineRestServices provides the server-side implementation of the Data Engine Open Metadata Assess Service (OMAS).
 * This service provide the functionality to create processes with input and output relationships.
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
     * Create the process with input/output relationships
     *
     * @param serverName         name of server instance to call.
     * @param userId             the name of the calling user.
     * @param processRequestBody properties of the process
     * @return the unique identifier (guid) of the created process
     */
    public GUIDResponse createProcess(String userId, String serverName, ProcessRequestBody processRequestBody) {
        final String methodName = "createProcess";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = new ProcessHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName),
                    instanceHandler.getMetadataCollection(serverName));

            if (processRequestBody == null) {
                return null;
            }

            String processName = processRequestBody.getName();
            List<String> inputs = processRequestBody.getInputs();
            List<String> outputs = processRequestBody.getOutputs();
            String description = processRequestBody.getDescription();
            String latestChange = processRequestBody.getLatestChange();
            String zoneMembership = processRequestBody.getZoneMembership();
            String parentProcessGuid = processRequestBody.getParentProcessGuid();
            String displayName = processRequestBody.getDisplayName();

          //  createTestDataSets(userId, processHandler);

            String processGuid = processHandler.createProcess(userId, processName, description, latestChange,
                    zoneMembership, displayName, parentProcessGuid);

            processHandler.addInputRelationships(userId, processGuid, inputs);
            processHandler.addOutputRelationships(userId, processGuid, outputs);

            response.setGuid(processGuid);

        } catch (TypeErrorException | EntityNotKnownException | StatusNotSupportedException | InvalidParameterException
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

    //TODO remove when igc replicator can replicate DataSets
    private void createTestDataSets(String userId, ProcessHandler processHandler)
            throws UserNotAuthorizedException, TypeErrorException, ClassificationErrorException,
            StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException {

        String dataSetGuid = processHandler.createDataSet(userId, "dataSet1");
        String dataSetGuid2 = processHandler.createDataSet(userId, "dataSet2");
        String dataSetGuid3 = processHandler.createDataSet(userId, "dataSet3");
        String dataSetGuid4 = processHandler.createDataSet(userId, "dataSet4");
    }
}