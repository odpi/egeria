/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * RepositoryProxyConfigurationClient provides the configuration services for a repository proxy.
 * This is a server that translates between the API/events of a third party metadata repository and the
 * Open Metadata Repository Services (OMRS) API/events.
 */
public class RepositoryProxyConfigurationClient extends CohortMemberConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public RepositoryProxyConfigurationClient(String   serverName,
                                              String   serverPlatformRootURL,
                                              String   secretStoreProvider,
                                              String   secretStoreLocation,
                                              String   secretStoreCollection,
                                              String   delegatingUserId,
                                              AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, delegatingUserId, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public RepositoryProxyConfigurationClient(String                             serverPlatformRootURL,
                                              Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                              String                             delegatingUserId,
                                              AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverPlatformRootURL, secretsStoreConnectorMap, delegatingUserId, auditLog);
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */


    /**
     * Provide the connection to the third party metadata server repository connector that maps
     * the OMRS RepositoryConnector API to the third party metadata server API.
     *
     * @param connection  connection to the OMRS repository connector.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setRepositoryProxyConnection(Connection connection) throws UserNotAuthorizedException,
                                                                           InvalidParameterException,
                                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "setRepositoryConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/local-repository/mode/repository-proxy/connection?delegatingUserId={1}";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Provide the connection to the third party metadata server repository connector that maps
     * the OMRS RepositoryConnector API to the third party repository API.
     *
     * @param connectorProvider    connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setRepositoryProxyConnection(String              connectorProvider,
                                             Map<String, Object> additionalProperties) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setRepositoryConnection";
        final String parameterName = "connectorProvider";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/local-repository/mode/repository-proxy/details?delegatingUserId={1}" +
                "@connectorProvider={2}";

        invalidParameterHandler.validateName(connectorProvider, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        additionalProperties,
                                        serverName,
                                        delegatingUserId,
                                        connectorProvider);
    }


    /**
     * Provide the connection to the third party repository's event mapper.  The default value is null which
     * means no event mapper.  An event mapper is needed if the third party repository has additional APIs that can change
     * the metadata in its repository without going through the open metadata and governance services.
     * The event mapper detects changes to the third party repository and converts them to OMRS Events.
     *
     * @param connection  connection to the OMRS repository event mapper.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEventMapperConnection(Connection  connection) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName  = "setEventMapperConnection";
        final String parameterName = "connection";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/event-mapper-connection?delegatingUserId={1}";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the third party repository has additional APIs that can change
     * the metadata in its repository without going through the open metadata and governance services.
     * The event mapper detects changes to the third party repository and converts them to OMRS Events.
     *
     * @param connectorProvider           Java class name of the connector provider for the OMRS repository event mapper.
     * @param eventSource                 topic name or URL to the native event source.
     * @param additionalProperties        additional properties for the event mapper connection
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEventMapperConnection(String               connectorProvider,
                                         String               eventSource,
                                         Map<String, Object>  additionalProperties) throws UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "setEventMapperConnection";
        final String connectorProviderParameterName = "connectorProvider";
        final String eventSourceParameterName = "eventSource";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/event-mapper-details" +
                "?delegatingUserId={1}&connectorProvider={2}&eventSource={3}";

        invalidParameterHandler.validateName(connectorProvider, connectorProviderParameterName, methodName);
        invalidParameterHandler.validateName(eventSource, eventSourceParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        additionalProperties,
                                        serverName,
                                        delegatingUserId,
                                        connectorProvider,
                                        eventSource);
    }
}
