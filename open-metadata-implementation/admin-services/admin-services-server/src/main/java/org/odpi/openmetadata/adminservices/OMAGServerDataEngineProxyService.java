/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerDataEngineProxyService supports the configuration requests for Data Engine Proxies.
 */
public class OMAGServerDataEngineProxyService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();

    private OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();

    /**
     * Store the provided Data Engine Proxy configuration
     *
     * @param userId                user that is issuing the request
     * @param serverName            local server name
     * @param dataEngineProxyConfig configuration for the data engine proxy
     * @return void response
     */
    public VoidResponse setDataEngineProxyConfig(String userId, String serverName, DataEngineProxyConfig dataEngineProxyConfig) {

        final String methodName = "setDataEngineProxyConfig";
        VoidResponse response = new VoidResponse();

        try {

            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            serverConfig.setDataEngineProxyConfig(dataEngineProxyConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);

        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Exception error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

    /**
     * Remove Data Engine Proxy from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     */
    public VoidResponse deleteDataEngineProxy(String userId, String serverName) {

        final String methodName = "deleteDataEngineProxy";
        VoidResponse response = new VoidResponse();

        try {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<String> configAuditTrail = serverConfig.getAuditTrail();
            if (configAuditTrail == null) {
                configAuditTrail = new ArrayList<>();
            }
            configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serverName + ".");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setDataEngineProxyConfig(null);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Exception error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

}
