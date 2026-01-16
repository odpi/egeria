/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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
public class IntegrationDaemon
{
    private final IntegrationDaemonServicesRESTClient restClient;               /* Initialized in the constructor */
    private final String                              serverName;               /* Initialized in the constructor */
    private final String                              serverPlatformRootURL;    /* Initialized in the constructor */
    private final String                              delegatingUserId;         /* Initialized in the constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create a new client with bearer token authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationDaemon(String   serverName,
                             String   serverPlatformRootURL,
                             String   secretStoreProvider,
                             String   secretStoreLocation,
                             String   secretStoreCollection,
                             String   delegatingUserId,
                             AuditLog auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.delegatingUserId = delegatingUserId;

        this.restClient = new IntegrationDaemonServicesRESTClient(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
    }


    /**
     * Create a new client with bearer token authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationDaemon(String                             serverName,
                             String                             serverPlatformRootURL,
                             Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                             String                             delegatingUserId,
                             AuditLog                           auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.delegatingUserId = delegatingUserId;

        this.restClient = new IntegrationDaemonServicesRESTClient(serverName, serverPlatformRootURL, secretsStoreConnectorMap, auditLog);
    }

    /**
     * Validate the connector and return its connector type.
     *
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type and other capabilities for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorReport validateConnector(String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "validateConnector";
        final String   nameParameter = "connectorProviderClassName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/validate-connector/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          connectorProviderClassName,
                                                                                          delegatingUserId);

        return restResult.getConnectorReport();
    }


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param connectorName name of a specific connector or null for all connectors
     *
     * @return property map
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public Map<String, Object> getConfigurationProperties(String connectorName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "getConfigurationProperties";
        final String   connectorNameParameterName = "connectorName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/{1}/configuration-properties?delegatingUserId={2}";

        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        PropertiesResponse restResult = restClient.callPropertiesGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             connectorName,
                                                                             delegatingUserId);

        return restResult.getProperties();
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConfigurationProperties(String              connectorName,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "updateConfigurationProperties";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/configuration-properties?delegatingUserId={1}";

        ConnectorConfigPropertiesRequestBody requestBody = new ConnectorConfigPropertiesRequestBody();

        requestBody.setConnectorName(connectorName);
        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setConfigurationProperties(configurationProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param networkAddress new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateEndpointNetworkAddress(String              connectorName,
                                             String              networkAddress) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   methodName = "updateEndpointNetworkAddress";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/{1}/endpoint-network-address?delegatingUserId={2}";

        StringRequestBody requestBody = new StringRequestBody();

        requestBody.setString(networkAddress);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        connectorName,
                                        delegatingUserId);
    }

    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param connection new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConnectorConnection(String     connectorName,
                                          Connection connection) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "updateConnectorConnection";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/{1}/connection?delegatingUserId={2}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        connectorName,
                                        delegatingUserId);
    }


    /**
     * Issue a refresh() request on a specific connector
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnector(String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String   methodName = "refreshConnector";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/refresh?delegatingUserId={1}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(connectorName);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Issue a refresh() request on a connector running in the integration daemon.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnectors() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        final String   methodName = "refreshConnectors";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/refresh?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        new NameRequestBody(),
                                        serverName,
                                        delegatingUserId);
    }



    /**
     * Issue a restart() request on a specific connector
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnector(String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String   methodName = "restartConnector";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/restart?delegatingUserId={1}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(connectorName);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }



    /**
     * Issue a restart() request on a connector running in the integration daemon.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnectors() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        final String   methodName = "restartConnectors";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/integration-connectors/restart?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        new NameRequestBody(),
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Return a summary of each of the integration services' and integration groups' status.
     *
     * @return list of statuses - one for each assigned integration service or integration group
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public IntegrationDaemonStatus getIntegrationDaemonStatus() throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   methodName = "getIntegrationDaemonStatus";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon/status?delegatingUserId={1}";

        IntegrationDaemonStatusResponse restResult = restClient.callIntegrationDaemonStatusGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       delegatingUserId);

        return restResult.getIntegrationDaemonStatus();
    }


    /**
     * Retrieve the description and status of the requested integration group.
     *
     * @param integrationGroupName qualifiedName of the integration group to target
     *
     * @return integration group summary
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public IntegrationGroupSummary getIntegrationGroupSummary(String integrationGroupName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "getIntegrationGroupSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon-services/integration-groups/{1}/summary?delegatingUserId={2}";

        IntegrationGroupSummaryResponse restResult = restClient.callIntegrationGroupSummaryGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       integrationGroupName,
                                                                                                       delegatingUserId);

        return restResult.getIntegrationGroupSummary();
    }


    /**
     * Retrieve the description and status of each integration group assigned to the Integration Daemon.
     *
     * @return list of integration group summaries
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<IntegrationGroupSummary> getIntegrationGroupSummaries() throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   methodName = "getIntegrationGroupSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon-services/integration-groups/summary?delegatingUserId={1}";

        IntegrationGroupSummariesResponse restResult = restClient.callIntegrationGroupSummariesGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           delegatingUserId);


        return restResult.getIntegrationGroupSummaries();
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param integrationGroupName qualifiedName of the integration group to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the integration group.
     */
    public  void refreshConfig(String integrationGroupName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "refreshConfig";
        final String   integrationGroupParameterName = "integrationGroupName";
        final String   urlTemplate = "/servers/{0}/open-metadata/integration-daemon-services/integration-groups/{1}/refresh-config?delegatingUserId={2}";

        invalidParameterHandler.validateName(integrationGroupName, integrationGroupParameterName, methodName);

        restClient.callVoidGetRESTCall(methodName,
                                       serverPlatformRootURL + urlTemplate,
                                       serverName,
                                       integrationGroupName,
                                       delegatingUserId);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException a problem processing the request
     */
    public void publishOpenLineageEvent(OpenLineage.RunEvent event) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "publishOpenLineageEvent";
        final String eventParameter = "event";
        final String urlTemplate = "/servers/{0}/open-metadata/integration-daemon/publish-open-lineage-event?delegatingUserId={1}";

        invalidParameterHandler.validateObject(event, eventParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        event,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException a problem processing the request
     */
    public void publishOpenLineageEvent(String event) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "publishOpenLineageEvent";
        final String eventParameter = "event";
        final String urlTemplate = "/servers/{0}/open-metadata/integration-daemon/publish-open-lineage-event?delegatingUserId={1}";

        invalidParameterHandler.validateObject(event, eventParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        event,
                                        serverName,
                                        delegatingUserId);
    }
}
