/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class OMAGServerDataPlatformService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();

    private static final String defaultDataPlatformInTopicName = "omas.dataplatform.inTopic";
    private static final String defaultInTopicName = "InTopic";

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VoidResponse setDataPlatformServiceConfig(String userId, String serverName, DataPlatformServicesConfig dataPlatformServicesConfig)
    {
        String methodName = "setDataPlatformServiceConfig";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();

            if (dataPlatformServicesConfig.getDataPlatformOmasInTopicName()==null) {
                dataPlatformServicesConfig.setDataPlatformOmasInTopic(
                        connectorConfigurationFactory.getDefaultEventBusConnection(
                                eventBusConfig.getConnectorProvider(),
                                eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                defaultDataPlatformInTopicName,
                                UUID.randomUUID().toString(),
                                eventBusConfig.getConfigurationProperties()
                        )
                );
                dataPlatformServicesConfig.setDataPlatformOmasInTopicName(
                        dataPlatformServicesConfig.getDataPlatformOmasInTopic().getEndpoint().getAddress());
            } else {
                dataPlatformServicesConfig.setDataPlatformOmasInTopic(
                        connectorConfigurationFactory.getDefaultEventBusConnection(
                                eventBusConfig.getConnectorProvider(),
                                eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                dataPlatformServicesConfig.getDataPlatformOmasInTopicName(),
                                UUID.randomUUID().toString(),
                                eventBusConfig.getConfigurationProperties()
                        )
                );
            }
            serverConfig.setDataPlatformServicesConfig(dataPlatformServicesConfig);
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

    public VoidResponse enableDataPlatformService(String userId, String serverName) {

        final String methodName = "enableDataPlatformService";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            DataPlatformServicesConfig dataPlatformServicesConfig = serverConfig.getDataPlatformServicesConfig();
            this.setDataPlatformServiceConfig(userId, serverName, dataPlatformServicesConfig);
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
