/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.watchdogaction.api.WatchdogActionAPI;
import org.odpi.openmetadata.engineservices.watchdogaction.client.rest.WatchdogActionRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * WatchdogActionClient is a client-side library for calling a specific watchdog action engine with an engine host server.
 */
public class WatchdogActionClient implements WatchdogActionAPI
{
    private final String                  serverName;               /* Initialized in constructor */
    private final String                  serverPlatformRootURL;    /* Initialized in constructor */
    private final String                  watchdogActionEngineName;      /* Initialized in constructor */
    private final WatchdogActionRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a watchdog action engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the watchdog action engine is running.
     * @param serverName the name of the engine host server where the watchdog action engine is running
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param watchdogActionEngineName the unique name of the watchdog action engine.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public WatchdogActionClient(String serverPlatformRootURL,
                                String serverName,
                                String localServerSecretsStoreProvider,
                                String localServerSecretsStoreLocation,
                                String localServerSecretsStoreCollection,
                                String watchdogActionEngineName) throws InvalidParameterException
    {
        this.serverPlatformRootURL      = serverPlatformRootURL;
        this.serverName                 = serverName;
        this.watchdogActionEngineName   = watchdogActionEngineName;

        this.restClient = new WatchdogActionRESTClient(serverName, serverPlatformRootURL, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, null);
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    @Override
    public ConnectorReport validateConnector(String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "validateConnector";
        final String   nameParameter = "connectorProviderClassName";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/watchdog-action/validate-connector";

        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          connectorProviderClassName);

        return restResult.getConnectorReport();
    }
}
