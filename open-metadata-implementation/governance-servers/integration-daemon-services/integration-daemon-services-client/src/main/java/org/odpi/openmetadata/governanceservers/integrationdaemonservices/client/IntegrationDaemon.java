/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.api.IntegrationDaemonAPI;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationServiceSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.*;

import java.util.List;
import java.util.Map;

/**
 * IntegrationDaemon is the client library for the Integration Daemon's REST API.  The integration daemon is an OMAG Server.
 * It runs one-to-many integration services that in turn manage one-to-many integration connectors.  Each integration service
 * focuses on a particular type of third party technology and is paired with an appropriate OMAS.
 *
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
    private final static NullRequestBody  nullRequestBody         = new NullRequestBody();

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
     * @param serviceURLMarker integration service identifier
     * @param connectorName name of a specific connector or null for all connectors
     *
     * @return property map
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public Map<String, Object> getConfigurationProperties(String              userId,
                                                          String              serviceURLMarker,
                                                          String              connectorName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String   methodName = "updateConfigurationProperties";
        final String   serviceNameParameterName = "serviceURLMarker";
        final String   connectorNameParameterName = "connectorName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-services/{2}/connectors/{3}/configuration-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serviceURLMarker, serviceNameParameterName, methodName);
        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        PropertiesResponse restResult = restClient.callPropertiesGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             serviceURLMarker,
                                                                             connectorName);

        return restResult.getProperties();
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param connectorName name of a specific connector or null for all connectors
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConfigurationProperties(String              userId,
                                              String              serviceURLMarker,
                                              String              connectorName,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "updateConfigurationProperties";
        final String   serviceNameParameterName = "serviceURLMarker";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-services/{2}/connectors/configuration-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serviceURLMarker, serviceNameParameterName, methodName);

        ConnectorConfigPropertiesRequestBody requestBody = new ConnectorConfigPropertiesRequestBody();

        requestBody.setConnectorName(connectorName);
        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setConfigurationProperties(configurationProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        serviceURLMarker);
    }


    /**
     * Refresh all connectors running in the integration daemon, regardless of the integration service they belong to.
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshAllServices(String userId) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   methodName = "refreshAllServices";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/refresh";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId);
    }


    /**
     * Refresh the requested connectors running in the requested integration service.
     *
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param connectorName optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshService(String userId,
                               String serviceURLMarker,
                               String connectorName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String   methodName = "refreshService";
        final String   nameParameter = "serviceURLMarker";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-services/{2}/refresh";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serviceURLMarker, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(connectorName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        serviceURLMarker);
    }


    /**
     * Request that the integration service shutdown and recreate its integration connectors.  If a connector name
     * is provided, only that connector is restarted.
     *
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param connectorName optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are restarted.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartService(String userId,
                               String serviceURLMarker,
                               String connectorName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String   methodName = "refreshService";
        final String   nameParameter = "serviceURLMarker";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-service/{2}/restart";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serviceURLMarker, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(connectorName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        serviceURLMarker);
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
     * Return a summary of each of the integration services' status.
     *
     * @param userId calling user
     *
     * @return list of statuses - one for each assigned integration services or
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public List<IntegrationServiceSummary> getIntegrationServicesSummaries(String   userId) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String   methodName = "getIntegrationServicesSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/users/{1}/integration-services/summary";

        invalidParameterHandler.validateUserId(userId, methodName);

        IntegrationServiceSummaryResponse restResult = restClient.callIntegrationServiceStatusGetRESTCall(methodName,
                                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                                          serverName,
                                                                                                          userId);

        return restResult.getIntegrationServiceSummaries();
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
}
