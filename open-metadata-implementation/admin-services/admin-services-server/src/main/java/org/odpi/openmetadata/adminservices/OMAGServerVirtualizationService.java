/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.VirtualizationConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class OMAGServerVirtualizationService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private static final String defaultOutTopicName = "OutTopic";
    private static final String defaultInTopicName = "InTopic";

    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VoidResponse setVirtualizerConfig(String userId, String serverName, VirtualizationConfig virtualizationConfig){
        String methodName = "setVirtualizationConfig";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();

            virtualizationConfig.setIvOutTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(
                            defaultOutTopicName,
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                            virtualizationConfig.getIvOutTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getConfigurationProperties()
                    )
            );

            virtualizationConfig.setIvInTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(
                            defaultInTopicName,
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                            virtualizationConfig.getIvInTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getConfigurationProperties()
                    )
            );

            serverConfig.setVirtualizationConfig(virtualizationConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);

        } catch (OMAGInvalidParameterException e){
            log.error("Invalid configuration! ", e);
        }

        return response;
    }

    public VoidResponse enableVirtualizationService(String userId, String serverName) {

        final String methodName = "enableVirtualizationService";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);
            VirtualizationConfig virtualizationConfig = serverConfig.getVirtualizationConfig();
            this.setVirtualizerConfig(userId, serverName, virtualizationConfig);
        } catch (OMAGInvalidParameterException e) {
            errorHandler.captureInvalidParameterException(response, e);
        }
        return response;
    }

}
