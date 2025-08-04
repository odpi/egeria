/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.api.IntegrationDaemonAPI;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.*;

import java.util.List;
import java.util.Map;
import io.openlineage.client.OpenLineage;

/**
 * IntegrationDaemon is the client library for the Integration Daemon's REST API.  The integration daemon is an OMAG Server.
 * It runs one-to-many integration services that in turn manage one-to-many integration connectors.  Each integration service
 * focuses on a particular type of third party technology and is paired with an appropriate OMAS.
 * The refresh commands are used to instruct the connectors running in the integration daemon to verify the consistency
 * of the metadata in the third party technology against the values in open metadata.  All connectors are requested
 * to refresh when the integration daemon first starts.  Then refresh is called on the schedule defined in the configuration
 * and lastly as a result of calls to this API.
 */
public class IntegrationDaemon implements IntegrationDaemonAPI
{
    private final IntegrationDaemonServicesRESTClient restClient;               /* Initialized in constructor */
    private final String                              serverName;
    private final String                              serverPlatformRootURL;

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
    public IntegrationDaemon(String   serverName,
                             String   serverPlatformRootURL,
                             AuditLog auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new IntegrationDaemonServicesRESTClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationDaemon(String serverName,
                             String serverPlatformRootURL) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new IntegrationDaemonServicesRESTClient(serverName, serverPlatformRootURL);
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
    public IntegrationDaemon(String   serverName,
                             String   serverPlatformRootURL,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new IntegrationDaemonServicesRESTClient(serverName, serverPlatformRootURL, userId, password, auditLog);
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
    public IntegrationDaemon(String serverName,
                             String serverPlatformRootURL,
                             String userId,
                             String password) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new IntegrationDaemonServicesRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector or null for all connectors
     *
     * @return property map
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public Map<String, Object> getConfigurationProperties(String              userId,
                                                          String              connectorName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String   methodName = "getConfigurationProperties";
        final String   connectorNameParameterName = "connectorName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/{2}/configuration-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        PropertiesResponse restResult = restClient.callPropertiesGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             connectorName);

        return restResult.getProperties();
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector or null for all connectors
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConfigurationProperties(String              userId,
                                              String              connectorName,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "updateConfigurationProperties";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/configuration-properties";

        invalidParameterHandler.validateUserId(userId, methodName);

        ConnectorConfigPropertiesRequestBody requestBody = new ConnectorConfigPropertiesRequestBody();

        requestBody.setConnectorName(connectorName);
        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setConfigurationProperties(configurationProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector or null for all connectors
     * @param networkAddress new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateEndpointNetworkAddress(String              userId,
                                             String              connectorName,
                                             String              networkAddress) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   methodName = "updateEndpointNetworkAddress";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/{2}/endpoint-network-address";

        invalidParameterHandler.validateUserId(userId, methodName);

        StringRequestBody requestBody = new StringRequestBody();

        requestBody.setString(networkAddress);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        connectorName);
    }

    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector or null for all connectors
     * @param connection new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConnectorConnection(String     userId,
                                          String     connectorName,
                                          Connection connection) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "updateConnectorConnection";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/{2}/connection";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        userId,
                                        connectorName);
    }


    /**
     * Issue a refresh() request on a specific connector
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnector(String userId,
                                 String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String   methodName = "refreshConnector";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/refresh";

        invalidParameterHandler.validateUserId(userId, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(connectorName);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId);
    }



    /**
     * Issue a refresh() request on a connector running in the integration daemon.
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnectors(String userId) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String   methodName = "refreshConnectors";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/refresh";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        new NameRequestBody(),
                                        serverName,
                                        userId);
    }



    /**
     * Issue a restart() request on a specific connector
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnector(String userId,
                                 String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String   methodName = "restartConnector";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/restart";

        invalidParameterHandler.validateUserId(userId, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(connectorName);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId);
    }



    /**
     * Issue a restart() request on a connector running in the integration daemon.
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnectors(String userId) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String   methodName = "restartConnectors";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-connectors/restart";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        new NameRequestBody(),
                                        serverName,
                                        userId);
    }


    /**
     * Return a summary of each of the integration services' and integration groups' status.
     *
     * @param userId calling user
     *
     * @return list of statuses - one for each assigned integration service or integration group
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public IntegrationDaemonStatus getIntegrationDaemonStatus(String   userId) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String   methodName = "getIntegrationDaemonStatus";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/status";

        invalidParameterHandler.validateUserId(userId, methodName);

        IntegrationDaemonStatusResponse restResult = restClient.callIntegrationDaemonStatusGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId);

        return restResult.getIntegrationDaemonStatus();
    }


    /**
     * Retrieve the description and status of the requested integration group.
     *
     * @param userId calling user
     * @param integrationGroupName qualifiedName of the integration group to target
     *
     * @return integration group summary
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public IntegrationGroupSummary getIntegrationGroupSummary(String userId,
                                                              String integrationGroupName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "getIntegrationGroupSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon-services/users/{1}/integration-groups/{2}/summary";

        invalidParameterHandler.validateUserId(userId, methodName);

        IntegrationGroupSummaryResponse restResult = restClient.callIntegrationGroupSummaryGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       integrationGroupName);

        return restResult.getIntegrationGroupSummary();
    }


    /**
     * Retrieve the description and status of each integration group assigned to the Integration Daemon.
     *
     * @param userId calling user
     * @return list of integration group summaries
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<IntegrationGroupSummary> getIntegrationGroupSummaries(String userId) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "getIntegrationGroupSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon-services/users/{1}/integration-groups/summary";

        invalidParameterHandler.validateUserId(userId, methodName);

        IntegrationGroupSummariesResponse restResult = restClient.callIntegrationGroupSummariesGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId);


        return restResult.getIntegrationGroupSummaries();
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param userId identifier of calling user
     * @param integrationGroupName qualifiedName of the integration group to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the integration group.
     */
    public  void refreshConfig(String userId,
                               String integrationGroupName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "refreshConfig";
        final String   integrationGroupParameterName = "integrationGroupName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon-services/users/{1}/integration-groups/{2}/refresh-config";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(integrationGroupName, integrationGroupParameterName, methodName);

        restClient.callVoidGetRESTCall(methodName,
                                       serverPlatformRootURL + urlTemplate,
                                       serverName,
                                       userId,
                                       integrationGroupName);
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
