/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * Generates test data of type Process, and triggers requests via client for aforementioned type
 */
public class ProcessSetupService {

    /**
     * Creates a simple process or updates one.
     *
     * @param userId the user which creates the data engine
     * @param dataEngineClient the data engine client that is used to create the external data engine
     * @param process process to upsert. If null, a default will be used
     *
     * @return process
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Process createOrUpdateSimpleProcess(String userId, DataEngineClient dataEngineClient, Process process)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(process == null) {
            process = getDefaultProcess();
        }
        dataEngineClient.createOrUpdateProcess(userId, process);
        return process;
    }

    private Process getDefaultProcess() {
        Process process;
        process = new Process();
        process.setQualifiedName("simple-process-qualified-name");
        process.setDisplayName("simple-process-display-name");
        process.setName("simple-process-name");
        process.setDescription("simple-process-description");
        process.setOwner("simple-process-owner");
        return process;
    }

}
