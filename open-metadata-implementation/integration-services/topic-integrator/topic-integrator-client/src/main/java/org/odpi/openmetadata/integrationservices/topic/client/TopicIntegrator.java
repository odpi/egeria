/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.topic.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.integrationservices.topic.api.TopicIntegratorAPI;

/**
 * TopicIntegrator is the client library for the Topic Integrator OMIS's REST API.
 */
public class TopicIntegrator implements TopicIntegratorAPI
{
    private FFDCRESTClient restClient;               /* Initialized in constructor */
    private String         serverName;
    private String         serverPlatformRootURL;

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public TopicIntegrator(String   serverName,
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
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public TopicIntegrator(String serverName,
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
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public TopicIntegrator(String   serverName,
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
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public TopicIntegrator(String serverName,
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
     * @return connector type for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorType validateConnector(String userId,
                                           String connectorProviderClassName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "validateConnector";
        final String   nameParameter = "connectorProviderClassName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-services/topic-integrator/users/{1}/validate-connector";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorTypeResponse restResult = restClient.callConnectorTypeGetRESTCall(methodName,
                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   connectorProviderClassName);

        return restResult.getConnectorType();
    }
}
