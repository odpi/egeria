/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.rest;

import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyErrorCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyException;
import org.odpi.openmetadata.governanceservers.dataengineproxy.processor.DataEngineProxyService;

public class DataEngineProxyServerInstance extends OMAGServerServiceInstance {

    DataEngineProxyService dataEngineInstanceHandler;

    public DataEngineProxyServerInstance(String serverName,
                                         String serviceName,
                                         int maxPageSize,
                                         DataEngineProxyService dataEngineProxyService) {
        super(serverName, serviceName, maxPageSize);
        this.dataEngineInstanceHandler = dataEngineProxyService;
    }

    /**
     * Return the DataEngineProxyService.
     *
     * @return DataEngineProxyService.
     */
    DataEngineProxyService getDataEngineProxyService() throws DataEngineProxyException {
        final String methodName = "getDataEngineProxyService";
        if (this.dataEngineInstanceHandler == null) {
            DataEngineProxyErrorCode errorCode = DataEngineProxyErrorCode.ERROR_INITIALIZING_CONNECTION;
            String errorMessage = errorCode.getMessageDefinition(serverName).toString();
            throw new DataEngineProxyException(errorCode.getMessageDefinition().getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getMessageDefinition().getSystemAction(),
                    errorCode.getMessageDefinition().getUserAction());
        }
        return dataEngineInstanceHandler;
    }
}
