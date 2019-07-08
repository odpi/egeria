/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OMAGServerDataEngineProxyService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();

    private OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VoidResponse setDataEngineProxyConfig(String userId, String serverName, DataEngineProxyConfig dataEngineProxyConfig) {

        final String methodName = "setDataEngineProxyConfig";
        VoidResponse response = new VoidResponse();

        try {

            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            dataEngineProxyConfig.setDataEngineProxyConnection(
                    connectorConfigurationFactory.getDataEngineProxyConnection(
                            serverName,
                            dataEngineProxyConfig.getDataEngineProxyProvider(),
                            serverConfig.getLocalServerURL(),
                            dataEngineProxyConfig.getDataEngineConfig()
                    )
            );

            serverConfig.setDataEngineProxyConfig(dataEngineProxyConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);

        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable  error) {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

    public VoidResponse enableDataEngineProxy(String userId, String serverName) {

        final String methodName = "enableDataEngineProxy";
        VoidResponse response = new VoidResponse();

        try {

            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            DataEngineProxyConfig dataEngineProxyConfig = serverConfig.getDataEngineProxyConfig();
            this.setDataEngineProxyConfig(userId, serverName, dataEngineProxyConfig);

        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable  error) {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

}
