/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.client.rest.AdminServicesRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.configuration.properties.BasicServerProperties;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerConfigurationClient provides common services to configure an OMAG Server.
 */
public class OMAGServerConfigurationClient
{
    /**
     * Name of the server being configured.
     */
    protected String serverName;               /* Initialized in constructor */

    /**
     * URL of the platform that can pass on administration requests to the configuration document store.
     */
    protected String serverPlatformRootURL;    /* Initialized in constructor */

    /**
     * UserId initiating the request from another server.
     */
    protected final String delegatingUserId;   /* Initialized in the constructor */

    /**
     * Handler for validating parameters passed on requests.
     */
    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * REST client for calling the Administration Services on the platform.
     */
    protected AdminServicesRESTClient restClient;               /* Initialized in constructor */

    protected NullRequestBody nullRequestBody = new NullRequestBody();


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public OMAGServerConfigurationClient(String   serverName,
                                         String   serverPlatformRootURL,
                                         String   secretStoreProvider,
                                         String   secretStoreLocation,
                                         String   secretStoreCollection,
                                         String   delegatingUserId,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (with security and secrets store)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

            this.serverName            = serverName;
            this.serverPlatformRootURL = serverPlatformRootURL;
            this.delegatingUserId = delegatingUserId;

            this.restClient = new AdminServicesRESTClient(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public OMAGServerConfigurationClient(String                             serverPlatformRootURL,
                                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                         String                             delegatingUserId,
                                         AuditLog                           auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (used by another connector)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

            this.serverPlatformRootURL = serverPlatformRootURL;
            this.delegatingUserId = delegatingUserId;

            this.restClient = new AdminServicesRESTClient(serverName, serverPlatformRootURL, secretsStoreConnectorMap, auditLog);
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Remove the configuration for the server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearOMAGServerConfig() throws UserNotAuthorizedException,
                                               InvalidParameterException,
                                               OMAGConfigurationErrorException
    {
        final String methodName  = "clearOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/configuration?delegatingUserId={1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          delegatingUserId);
    }


    /**
     * Return the derived server type that is classified based on the configuration values.
     *
     * @return server classification description
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public ServerTypeClassificationSummary getServerTypeClassification() throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "getServerTypeClassification";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/server-type-classification?delegatingUserId={1}";

        ServerTypeClassificationResponse response = restClient.callServerClassificationGetRESTCall(methodName,
                                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                                   serverName,
                                                                                                   delegatingUserId);

        return response.getServerTypeClassification();
    }


    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  Typically, this is the URL root of the OMAG Server Platform
     * Where the server is deployed to.  However, it may be a DNS name - particularly if the server is
     * deployed to multiple platforms for high availability (HA).
     * The default value is "<a href="https://localhost:9443">"https://localhost:9443"</a>".
     *
     * @param serverURLRoot  String url.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerURLRoot(String    serverURLRoot) throws UserNotAuthorizedException,
                                                                 InvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName    = "setServerURLRoot";
        final String parameterName = "serverURLRoot";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/server-url-root-for-caller?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverURLRoot, parameterName, methodName);

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(serverURLRoot);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the open metadata out topic and
     * the local repositories' event mapper.
     * When the event bus is configured, it is used only on future configuration.  It does not affect
     * existing configuration.
     * If openMetadataOutTopic is null, a default connection for this topic is created.  It can be removed using
     * clearOpenMetadataOutTopic.
     *
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEventBus(String              connectorProvider,
                            String              topicURLRoot,
                            Map<String, Object> configurationProperties) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "setEventBus";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/event-bus?delegatingUserId={1}&connectorProvider={2}&topicURLRoot={3}";

        Map<String, Object> requestBody = configurationProperties;
        if (requestBody == null)
        {
            requestBody = new HashMap<>();
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId,
                                        connectorProvider,
                                        topicURLRoot);
    }


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.
     * The default value is derived from the type of server that is being configured.  If this
     * method is called, it overrides the default value.
     *
     * @param serverType  short description for the type of server.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerType(String    serverType) throws UserNotAuthorizedException,
                                                           InvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "setServerType";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/server-type?delegatingUserId={1}&typeName={2}";

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId,
                                        serverType);
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param organizationName  String name of the organization.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setOrganizationName(String    organizationName) throws UserNotAuthorizedException,
                                                                       InvalidParameterException,
                                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "setOrganizationName";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/organization-name?delegatingUserId={1}&name={2}";

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId,
                                        organizationName);
    }


    /**
     * Set up the description of this server. The default value is null.
     *
     * @param description  String description of the server
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerDescription(String   description) throws UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName  = "setServerDescription";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/server-description?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        description,
                                        serverName,
                                        delegatingUserId,
                                        description);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).  It is also used in the header of HTTP requests if the password is also set.
     *
     * @param serverUserId  String user that the server will use on connections and requests not associated with an end user.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerUserId(String serverUserId) throws UserNotAuthorizedException,
                                                            InvalidParameterException,
                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "setServerUserId";
        final String parameterName = "serverUserId";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/server-user-id?delegatingUserId={1}&id={2}";

        invalidParameterHandler.validateName(serverUserId, parameterName, methodName);

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId,
                                        serverUserId);
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.  The value is validated server side.
     *
     * @param maxPageSize  max number of elements that can be returned on a request.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setMaxPageSize(int     maxPageSize) throws UserNotAuthorizedException,
                                                           InvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "setMaxPageSize";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/max-page-size?delegatingUserId={1}&limit={2}";

        if (maxPageSize < 0)
        {
           throw new InvalidParameterException(OMAGAdminErrorCode.BAD_MAX_PAGE_SIZE.getMessageDefinition(serverName, Integer.toString(maxPageSize)),
                                               this.getClass().getName(),
                                               methodName,
                                               "maxPageSize");
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        delegatingUserId,
                                        Integer.toString(maxPageSize));
    }


    /**
     * Set up the basic server properties in a single request.
     *
     * @param organizationName  String name of the organization.
     * @param serverDescription  String description of the server
     * @param serverUserId  String user that the server will use on connections and requests not associated with an end user.
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param serverURLRoot  String url.
     * @param maxPageSize  max number of elements that can be returned on a request.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setBasicServerProperties(String organizationName,
                                         String serverDescription,
                                         String serverUserId,
                                         String secretsStoreProvider,
                                         String secretsStoreLocation,
                                         String secretsStoreCollection,
                                         String serverURLRoot,
                                         int    maxPageSize) throws UserNotAuthorizedException,
                                                                    InvalidParameterException,
                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "setBasicServerProperties";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/server-properties?delegatingUserId={1}";

        ServerPropertiesRequestBody requestBody = new ServerPropertiesRequestBody();

        requestBody.setOrganizationName(organizationName);
        requestBody.setLocalServerDescription(serverDescription);
        requestBody.setLocalServerUserId(serverUserId);
        requestBody.setSecretsStoreProvider(secretsStoreProvider);
        requestBody.setSecretsStoreLocation(secretsStoreLocation);
        requestBody.setSecretsStoreCollection(secretsStoreCollection);
        requestBody.setLocalServerURL(serverURLRoot);
        requestBody.setMaxPageSize(maxPageSize);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Return the basic server properties in a single request.
     *
     * @return basic server properties
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public BasicServerProperties getBasicServerProperties() throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "getBasicServerProperties";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/server-properties?delegatingUserId={1}";

        BasicServerPropertiesResponse response = restClient.callBasicServerPropertiesGetRESTCall(methodName,
                                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                                 serverName,
                                                                                                 delegatingUserId);

        return response.getBasicServerProperties();
    }


    /**
     * Set up the default audit log for the server.  This adds the console audit log destination.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setDefaultAuditLog() throws UserNotAuthorizedException,
                                            InvalidParameterException,
                                            OMAGConfigurationErrorException
    {
        final String methodName  = "setDefaultAuditLog";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/default?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addConsoleAuditLogDestination(List<String> supportedSeverities) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "addConsoleAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/console?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Add an audit log destination that creates slf4j records.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addSLF4JAuditLogDestination(List<String> supportedSeverities) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName  = "addSLF4JAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/slf4j?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addFileAuditLogDestination(List<String> supportedSeverities) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "addFileAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/files?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @param jdbcConnectionString connection string used to connect to the JDBC data base
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addJDBCAuditLogDestination(List<String> supportedSeverities,
                                           String       jdbcConnectionString) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName  = "addFileAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/jdbc?delegatingUserId={1}&connectionString={2}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId,
                                        jdbcConnectionString);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param directoryName optional directory name
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addFileAuditLogDestination(String       directoryName,
                                           List<String> supportedSeverities) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "addFileAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/files?delegatingUserId={1}&directoryName={2}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId,
                                        directoryName);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addEventTopicAuditLogDestination(List<String> supportedSeverities) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "addEventTopicAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/event-topic?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param topicName name of topic for audit log entries
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addEventTopicAuditLogDestination(String       topicName,
                                                 List<String> supportedSeverities) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "addEventTopicAuditLogDestination(with topicName)";
        final String topicNameParameter = "topicName";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/event-topic?delegatingUserId={1}&topicName={2}";

        invalidParameterHandler.validateName(topicName, topicNameParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        serverName,
                                        delegatingUserId,
                                        topicName);
    }


    /**
     * Add an audit log destination that is defined by the supplied connection object.
     *
     * @param connection connection object that defines the audit log destination
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addAuditLogDestination(Connection connection) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "addAuditLogDestination";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/connection?delegatingUserId={1}";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Replace an audit log destination connection identified by the supplied audit log destination connection name with the
     * supplied audit log destination connection.
     *
     * @param suppliedConnectionName the qualified name of the audit log destination to update.
     * @param connection connection object that replaces the existing one.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void updateAuditLogDestination(String     suppliedConnectionName,
                                          Connection connection) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "UpdateAuditLogDestination";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/connection/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        suppliedConnectionName,
                                        delegatingUserId);
    }


    /**
     * Delete an audit log destination that is defined by the supplied audit log destination connection name
     *
     * @param connectionName the qualified name of the audit log destination connection to delete.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAuditLogDestination(String connectionName) throws UserNotAuthorizedException,
                                                                       InvalidParameterException,
                                                                       OMAGConfigurationErrorException
    {
        final String methodName    = "clearAuditLogDestination";
        final String parameterName = "connectionName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/audit-log-destinations/connection/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(connectionName, parameterName, methodName);

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          connectionName,
                                          delegatingUserId);
    }


    /**
     * Set up all audit log destinations for this server.  Any existing definitions are overwritten
     *
     * @param auditLogDestinations list of connections
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setAuditLogDestinations(List<Connection> auditLogDestinations) throws UserNotAuthorizedException,
                                                                                      InvalidParameterException,
                                                                                      OMAGConfigurationErrorException
    {
        final String methodName    = "setAuditLogDestinations";
        final String parameterName = "auditLogDestinations";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/audit-log-destinations?delegatingUserId={1}";

        if (auditLogDestinations != null)
        {
            for (Connection connection : auditLogDestinations)
            {
                invalidParameterHandler.validateConnection(connection, parameterName, methodName);
            }
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        auditLogDestinations,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Clears all audit log destinations for this server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAuditLogDestinations() throws UserNotAuthorizedException,
                                                   InvalidParameterException,
                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "clearAuditLogDestinations";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/audit-log-destinations?delegatingUserId={1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          delegatingUserId);
    }


    /**
     * Override the default server security connector.
     *
     * @param connection connection object that defines the server security connector
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerSecurityConnection(Connection connection) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "setConfigurationStoreConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/security/connection?delegatingUserId={1}";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Clear the connection object for the server security connection which means the
     * server does no authorization checks when a request is made to this server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearServerSecurityConnection() throws UserNotAuthorizedException,
                                                       InvalidParameterException,
                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "clearServerSecurityConnection";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/security/connection?delegatingUserId={1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          delegatingUserId);
    }


    /**
     * Return the connection object for the server security connector.  Null is returned if the server
     * does not have a server security connection.
     *
     * @return server security connection
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Connection getServerSecurityConnection() throws UserNotAuthorizedException,
                                                           InvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "getServerSecurityConnection";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/security/connection?delegatingUserId={1}";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             delegatingUserId);

        return restResult.getConnection();
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param serverConfig configuration for the server
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setOMAGServerConfig(OMAGServerConfig serverConfig) throws UserNotAuthorizedException,
                                                                          OMAGConfigurationErrorException,
                                                                          InvalidParameterException
    {
        final String methodName    = "setOMAGServerConfig";
        final String parameterName = "serverConfig";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/configuration?delegatingUserId={1}";

        invalidParameterHandler.validateObject(serverConfig, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverConfig,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void deployOMAGServerConfig(String destinationPlatformURLRoot) throws UserNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException,
                                                                                 InvalidParameterException
    {
        final String methodName    = "deployOMAGServerConfig";
        final String parameterName = "destinationPlatformURLRoot";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/configuration/deploy?delegatingUserId={1}";

        invalidParameterHandler.validateName(destinationPlatformURLRoot, parameterName, methodName);

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(destinationPlatformURLRoot);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @return OMAGServerConfig properties
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public OMAGServerConfig getOMAGServerConfig() throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         OMAGConfigurationErrorException
    {
        final String methodName  = "getOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/configuration?delegatingUserId={1}";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         serverName,
                                                                                         delegatingUserId);

        return restResult.getOMAGServerConfig();
    }


    /**
     * Return the complete set of configuration properties in use by a running instance of the server.
     *
     * @return OMAGServerConfig properties
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public OMAGServerConfig getOMAGServerInstanceConfig() throws UserNotAuthorizedException,
                                                                 InvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "getOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/instance/configuration?delegatingUserId={1}";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         serverName,
                                                                                         delegatingUserId);

        return restResult.getOMAGServerConfig();
    }
}
