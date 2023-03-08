/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

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
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public RepositoryProxyConfigurationClient(String adminUserId,
                                              String serverName,
                                              String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public RepositoryProxyConfigurationClient(String adminUserId,
                                              String serverName,
                                              String serverPlatformRootURL,
                                              String connectionUserId,
                                              String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setRepositoryProxyConnection(Connection connection) throws OMAGNotAuthorizedException,
                                                                           OMAGInvalidParameterException,
                                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "setRepositoryConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/repository-proxy/connection";

        try
        {
            invalidParameterHandler.validateConnection(connection, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Provide the connection to the third party metadata server repository connector that maps
     * the OMRS RepositoryConnector API to the third party repository API.
     *
     * @param connectorProvider    connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setRepositoryProxyConnection(String              connectorProvider,
                                             Map<String, Object> additionalProperties) throws OMAGNotAuthorizedException,
                                                                                              OMAGInvalidParameterException,
                                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setRepositoryConnection";
        final String parameterName = "connectorProvider";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/repository-proxy/details" +
                "?connectorProvider={2}";

        try
        {
            invalidParameterHandler.validateName(connectorProvider, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        additionalProperties,
                                        adminUserId,
                                        serverName,
                                        connectorProvider);
    }


    /**
     * Provide the connection to the third party repository's event mapper.  The default value is null which
     * means no event mapper.  An event mapper is needed if the third party repository has additional APIs that can change
     * the metadata in its repository without going through the open metadata and governance services.
     * The event mapper detects changes to the third party repository and converts them to OMRS Events.
     *
     * @param connection  connection to the OMRS repository event mapper.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEventMapperConnection(Connection  connection) throws OMAGNotAuthorizedException,
                                                                        OMAGInvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName  = "setEventMapperConnection";
        final String parameterName = "connection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/event-mapper-connection";

        try
        {
            invalidParameterHandler.validateConnection(connection, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        adminUserId,
                                        serverName);
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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEventMapperConnection(String               connectorProvider,
                                         String               eventSource,
                                         Map<String, Object>  additionalProperties) throws OMAGNotAuthorizedException,
                                                                                           OMAGInvalidParameterException,
                                                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "setEventMapperConnection";
        final String connectorProviderParameterName = "connectorProvider";
        final String eventSourceParameterName = "eventSource";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/event-mapper-details" +
                "?connectorProvider={2}&eventSource={3}";

        try
        {
            invalidParameterHandler.validateName(connectorProvider, connectorProviderParameterName, methodName);
            invalidParameterHandler.validateName(eventSource, eventSourceParameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        additionalProperties,
                                        adminUserId,
                                        serverName,
                                        connectorProvider,
                                        eventSource);
    }
}
