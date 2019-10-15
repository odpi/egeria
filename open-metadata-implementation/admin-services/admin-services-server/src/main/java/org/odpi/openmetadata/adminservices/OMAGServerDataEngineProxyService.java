/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

            final String endpointGUID          = UUID.randomUUID().toString();
            final String connectionGUID        = UUID.randomUUID().toString();
            final String endpointDescription   = "Data Engine native endpoint.";
            final String connectionDescription = "Data Engine native connection.";

            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            Connection templateConnection = dataEngineProxyConfig.getDataEngineProxyConnection();
            Endpoint templateEndpoint = templateConnection.getEndpoint();

            String endpointName = "DataEngineNative.Endpoint." + serverName;

            Endpoint endpoint = new Endpoint(templateEndpoint);
            endpoint.setType(this.getEndpointType());
            endpoint.setGUID(endpointGUID);
            endpoint.setQualifiedName(endpointName);
            endpoint.setDisplayName(endpointName);
            endpoint.setDescription(endpointDescription);

            String connectionName = "DataEngineNative.Connection." + serverName;

            Connection connection = new Connection(templateConnection);
            connection.setType(this.getConnectionType());
            connection.setGUID(connectionGUID);
            connection.setQualifiedName(connectionName);
            connection.setDisplayName(connectionName);
            connection.setDescription(connectionDescription);
            connection.setEndpoint(endpoint);

            dataEngineProxyConfig.setDataEngineProxyConnection(connection);

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
        } catch (Throwable  error) {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

    /**
     * Return the standard type for an endpoint.
     *
     * @return ElementType object
     */
    private ElementType getEndpointType() {
        ElementType elementType = Endpoint.getEndpointType();
        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);
        return elementType;
    }

    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    private ElementType getConnectionType() {
        ElementType elementType = Connection.getConnectionType();
        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);
        return elementType;
    }

}
