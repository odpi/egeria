/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.OLSSimplifiedAccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerConfigOpenLineage provides the configuration services for the Open Lineage Server.
 */
public class OMAGServerConfigOpenLineage {
    private final OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();

    private static final String serviceName = GovernanceServicesDescription.LINEAGE_WAREHOUSE_SERVICES.getServiceName();
    private static final String defaultInTopicName = "inTopic";


    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();

    /**
     * Set the Open Lineage Config
     *
     * @param userId                  user that is issuing the request.
     * @param serverName              local server name.
     * @param openLineageServerConfig Config for the Open Lineage Services
     * @return void response
     **/
    public VoidResponse setOpenLineageConfig(String userId, String serverName, OpenLineageServerConfig openLineageServerConfig) {

        String methodName = "setOpenLineageConfig";
        VoidResponse response = new VoidResponse();

        try {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(openLineageServerConfig.getLineageGraphConnection(), "lineageGraphConnection", serverName, methodName);
            OLSSimplifiedAccessServiceConfig accessServiceConfig = openLineageServerConfig.getAccessServiceConfig();
            errorHandler.validatePropertyNotNull(accessServiceConfig, "accessServiceConfig", serverName, methodName);
            errorHandler.validatePropertyNotNull(accessServiceConfig.getServerName(), "accessServiceConfig.serverName", serverName, methodName);
            errorHandler.validatePropertyNotNull(accessServiceConfig.getServerPlatformUrlRoot(), "accessServiceConfig.serverPlatformUrlRoot", serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);

            serverConfig.setOpenLineageServerConfig(openLineageServerConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);

            List<String> configAuditTrail = serverConfig.getAuditTrail();
            if (configAuditTrail == null)
                configAuditTrail = new ArrayList<>();

            configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for open lineage services.");
            serverConfig.setAuditTrail(configAuditTrail);
        } catch (OMAGInvalidParameterException e) {
            exceptionHandler.captureInvalidParameterException(response, e);
        } catch (Exception e) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, e);
        }

        return response;
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @return void response
     */
    public VoidResponse removeOpenLineageConfig(String userId, String serverName) {
        final String methodName = "shutdown";

        VoidResponse response = new VoidResponse();

        try {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
                configAuditTrail = new ArrayList<>();

            configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setOpenLineageServerConfig(null);

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
