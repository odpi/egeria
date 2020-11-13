/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.SecurityOfficerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OMAGServerSecurityOfficerService
{
    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();

    private static final String defaultOutTopicName = "OutTopic";
    private static final String defaultInTopicName = "open-metadata.access-services.SecurityOfficer.outTopic";

    private static final String outputTopic = "open-metadata.security-officer-server.";
    private static final String defaultOutTopic = ".outTopic";

    public VoidResponse setSecurityOfficerConfig(String userId, String serverName, SecurityOfficerConfig securityOfficerConfig)
    {
        String methodName = "setSecurityOfficerConfig";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null) {
                configAuditTrail = new ArrayList<>();
            }

            if (securityOfficerConfig == null) {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for Security Officer Service.");
            } else {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for Security Officer Service.");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();
            if(securityOfficerConfig != null && securityOfficerConfig.getSecurityOfficerServerInTopicName() != null) {
                securityOfficerConfig.setSecurityOfficerServerInTopic(
                        connectorConfigurationFactory.getDefaultEventBusConnection(
                                eventBusConfig.getConnectorProvider(),
                                eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                securityOfficerConfig.getSecurityOfficerServerInTopicName(),
                                UUID.randomUUID().toString(),
                                eventBusConfig.getConfigurationProperties()));
            }

            if (securityOfficerConfig != null) {
                securityOfficerConfig.setSecurityOfficerServerOutTopic(
                        connectorConfigurationFactory.getDefaultEventBusConnection(
                                eventBusConfig.getConnectorProvider(),
                                eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                getOutputTopicName(securityOfficerConfig.getSecurityOfficerServerOutTopicName()),
                                serverConfig.getLocalServerId(),
                                eventBusConfig.getConfigurationProperties()));
            }

            serverConfig.setSecurityOfficerConfig(securityOfficerConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        return response;
    }

    private String getOutputTopicName(String securityServerType) {
        if (securityServerType != null) {
            return outputTopic + securityServerType + defaultOutTopic;
        }

        return outputTopic + "SecurityOfficerServer" + defaultOutTopic;
    }

    public VoidResponse enableSecurityOfficerService(String userId, String serverName)
    {

        final String methodName = "enableSecurityOfficerService";
        VoidResponse response = new VoidResponse();

        try
        {
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            SecurityOfficerConfig securityOfficerConfig = serverConfig.getSecurityOfficerConfig();
            this.setSecurityOfficerConfig(userId, serverName, securityOfficerConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }

        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

}