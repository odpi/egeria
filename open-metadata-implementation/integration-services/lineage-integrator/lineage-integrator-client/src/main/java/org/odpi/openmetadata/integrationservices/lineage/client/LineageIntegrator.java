/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.lineage.client;

import io.openlineage.client.OpenLineage;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.lineage.api.LineageIntegratorAPI;

/**
 * LineageIntegrator is the client library for the Lineage Integrator OMIS's REST API.
 */
public class LineageIntegrator implements LineageIntegratorAPI
{
    private final FFDCRESTClient restClient;               /* Initialized in constructor */
    private final String         serverName;
    private final String         serverPlatformRootURL;

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageIntegrator(String   serverName,
                             String   serverPlatformRootURL,
                             AuditLog auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new FFDCRESTClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageIntegrator(String serverName,
                             String serverPlatformRootURL) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new FFDCRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageIntegrator(String   serverName,
                             String   serverPlatformRootURL,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new FFDCRESTClient(serverName, serverPlatformRootURL, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageIntegrator(String serverName,
                             String serverPlatformRootURL,
                             String userId,
                             String password) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new FFDCRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there is a problem processing the request
     */
    @Override
    public ConnectorReport validateConnector(String userId,
                                             String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "validateConnector";
        final String nameParameter = "connectorProviderClassName";
        final String urlTemplate = "/servers/{0}/open-metadata/integration-services/lineage-integrator/users/{1}/validate-connector";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          userId,
                                                                                          connectorProviderClassName);

        return restResult.getConnectorReport();
    }



    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param userId calling user
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException there is a problem processing the request
     */
    @Override
    public void publishOpenLineageEvent(String               userId,
                                        OpenLineage.RunEvent event) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "publishOpenLineageEvent";
        final String eventParameter = "event";
        final String urlTemplate = "/servers/{0}/open-metadata/integration-services/lineage-integrator/users/{1}/publish-open-lineage-event";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(event, eventParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        event,
                                        serverName,
                                        userId);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param userId calling user
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException there is a problem processing the request
     */
    @Override
    public void publishOpenLineageEvent(String userId,
                                        String event) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "publishOpenLineageEvent";
        final String eventParameter = "event";
        final String urlTemplate = "/servers/{0}/open-metadata/integration-services/lineage-integrator/users/{1}/publish-open-lineage-event";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(event, eventParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        event,
                                        serverName,
                                        userId);
    }
}
