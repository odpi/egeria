/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.VirtualizationConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class OMAGServerVirtualizationService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private static final String defaultOutTopicName = "OutTopic";
    private static final String defaultInTopicName = "InTopic";

    private OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();


    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VoidResponse setVirtualizerConfig(String userId, String serverName, VirtualizationConfig virtualizationConfig)
    {
        String methodName = "setVirtualizationConfig";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();

            virtualizationConfig.setVirtualizerInboundTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                            virtualizationConfig.getVirtualizerInboundTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getConfigurationProperties()
                    )
            );

            virtualizationConfig.setVirtualizerOutboundTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                            virtualizationConfig.getVirtualizerOutboundTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getConfigurationProperties()
                    )
            );

            virtualizationConfig.setVirtualizationSolutionConnection(
                    connectorConfigurationFactory.getVirtualizationSolutionConnection(
                            virtualizationConfig.getVirtualizationProvider(),
                            virtualizationConfig.getVirtualizationSolutionConfig()
                    )
            );

            serverConfig.setVirtualizationConfig(virtualizationConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

    public VoidResponse enableVirtualizationService(String userId, String serverName) {

        final String methodName = "enableVirtualizationService";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            VirtualizationConfig virtualizationConfig = serverConfig.getVirtualizationConfig();
            this.setVirtualizerConfig(userId, serverName, virtualizationConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

}
