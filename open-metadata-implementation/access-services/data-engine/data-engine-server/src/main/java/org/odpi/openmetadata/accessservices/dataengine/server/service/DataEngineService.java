/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.rest.GUIDResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DataEngineService provides the server-side implementation of the Data Engine Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to create process and add relationships.
 */
public class DataEngineService {

    private static final DataEngineInstanceHandler instanceHandler = new DataEngineInstanceHandler();
    private static final Logger log = LoggerFactory.getLogger(DataEngineService.class);

    public DataEngineService() {
    }

    public GUIDResponse createProcess(String userId, String serverName, String processName, String displayName) {
        final String methodName = "createProcess";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = new ProcessHandler(instanceHandler.getAccessServiceName(),
                    instanceHandler.getRepositoryConnector(serverName), instanceHandler.getMetadataCollection(serverName));

            response.setGuid(processHandler.createProcess(userId, processName, displayName));
        } catch (Exception e) {
            //TODO exception handling
            e.printStackTrace();
        }
        return response;
    }
}