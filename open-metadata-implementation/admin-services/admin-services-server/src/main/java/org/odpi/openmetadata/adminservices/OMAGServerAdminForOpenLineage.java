/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OMAGServerAdminForOpenLineage {
    private final OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();

    private static final String defaultALOutTopicName = "omas.open-metadata.access-services.AssetLineage.outTopic";

    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();

    public VoidResponse enableOpenLineageService(String userId, String serverName) {
        final String methodName = "enableOpenLineageService";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            OpenLineageServerConfig openLineageServerConfig = serverConfig.getOpenLineageServerConfig();
            this.setOpenLineageConfig(userId, serverName, openLineageServerConfig);
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (Throwable error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }


    public VoidResponse setOpenLineageConfig(String userId, String serverName, OpenLineageServerConfig openLineageServerConfig) {
        String methodName = "setOpenLineageConfig";
        VoidResponse response = new VoidResponse();

        try {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();
            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();
            openLineageServerConfig.setInTopicConnection(
                    connectorConfigurationFactory.getDefaultEventBusConnection(defaultALOutTopicName,
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server",
                            openLineageServerConfig.getInTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getConfigurationProperties())
            );

            serverConfig.setOpenLineageServerConfig(openLineageServerConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);


            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null) {
                configAuditTrail = new ArrayList<>();
            }

            if (openLineageServerConfig == null) {
                configAuditTrail.add(
                        new Date().toString() + " " + userId + " removed configuration for open lineage services.");
            } else {
                configAuditTrail.add(
                        new Date().toString() + " " + userId + " updated configuration for open lineage services.");
            }

            serverConfig.setAuditTrail(configAuditTrail);
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (Throwable error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }


}
