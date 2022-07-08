/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.rest;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyException;
import org.odpi.openmetadata.governanceservers.dataengineproxy.model.ProcessLoadResponse;
import org.odpi.openmetadata.governanceservers.dataengineproxy.processor.DataEngineProxyService;

public class DataEngineProxyRestService {

    private final DataEngineProxyInstanceHandler instanceHandler = new DataEngineProxyInstanceHandler();

    public ProcessLoadResponse load(String serverName, String userId) {
        String serviceOperationName = "DataEngineProxy";
        try {
            DataEngineProxyService dataEngineProxyService = instanceHandler.getDataEngineProxyService(userId, serverName, serviceOperationName);
            dataEngineProxyService.load();
        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException | DataEngineProxyException e) {
            ProcessLoadResponse response = new ProcessLoadResponse();
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionClassName(e.getClass().getName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setExceptionSystemAction(e.getReportedSystemAction());
            response.setExceptionUserAction(e.getReportedUserAction());
            return response;
        }
        return new ProcessLoadResponse();

    }

    public ProcessLoadResponse getProcessChanges(String serverName, String userId, String processId) {
        String serviceOperationName = "DataEngineProxy";
        try {
            DataEngineProxyService dataEngineProxyService = instanceHandler.getDataEngineProxyService(userId, serverName, serviceOperationName);
            dataEngineProxyService.pollProcessChanges(processId);
        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException | DataEngineProxyException e) {
            ProcessLoadResponse response = new ProcessLoadResponse();
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionClassName(e.getClass().getName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setExceptionSystemAction(e.getReportedSystemAction());
            response.setExceptionUserAction(e.getReportedUserAction());
            return response;
        }
        return new ProcessLoadResponse();
    }
}
