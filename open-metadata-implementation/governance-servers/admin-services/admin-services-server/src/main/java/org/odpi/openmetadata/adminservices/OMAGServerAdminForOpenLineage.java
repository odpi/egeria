/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OMAGServerAdminForOpenLineage {

    private final OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private static final String defaultInTopicName = "omas.open-metadata.access-services.AssetLineage.outTopic";


    public VoidResponse enableOpenLineageService(String userId, String serverName) {

        final String methodName = "enableOpenLineageService";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);
            OpenLineageConfig openLineageConfig = serverConfig.getOpenLineageConfig();
            this.setOpenLineageConfig(userId, serverName, openLineageConfig);
        } catch (OMAGInvalidParameterException e) {
            e.printStackTrace();
        }
        return response;
    }

    public VoidResponse setOpenLineageConfig(String userId, String serverName, OpenLineageConfig openLineageConfig) {
        String methodName = "setOpenLineageConfig";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditLog = serverConfig.getAuditTrail();

            if (configAuditLog == null) {
                configAuditLog = new ArrayList<>();
            }

            if (openLineageConfig == null) {
                configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for open lineage services.");
            } else {
                configAuditLog.add(new Date().toString() + " " + userId + " updated configuration for open lineage services.");
            }

            serverConfig.setAuditTrail(configAuditLog);
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();
            openLineageConfig.setALOutTopicConnection(
                    connectorConfigurationFactory.getDefaultEventBusConnection(
                            defaultInTopicName,
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server",
                            openLineageConfig.getALOutTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getAdditionalProperties()));



            serverConfig.setOpenLineageConfig(openLineageConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        } catch (OMAGInvalidParameterException e) {

        }
        return response;
    }



}
