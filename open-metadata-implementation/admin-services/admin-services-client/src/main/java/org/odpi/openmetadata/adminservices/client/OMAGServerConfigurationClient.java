/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.client.rest.AdminServicesRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.properties.BasicServerProperties;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerConfigurationClient provides common services to configure an OMAG Server.
 */
public class OMAGServerConfigurationClient
{
    /**
     * Callers user id to pass on each request to the config document store.
     */
    protected String adminUserId;              /* Initialized in constructor */

    /**
     * Name of the server being configured.
     */
    protected String serverName;               /* Initialized in constructor */

    /**
     * URL of the platform that can pass on administration requests to the configuration document store.
     */
    protected String serverPlatformRootURL;    /* Initialized in constructor */

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
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public OMAGServerConfigurationClient(String adminUserId,
                                         String serverName,
                                         String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);
            invalidParameterHandler.validateUserId(adminUserId, methodName);

            this.adminUserId           = adminUserId;
            this.serverName            = serverName;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new AdminServicesRESTClient(serverName, serverPlatformRootURL);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }
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
    public OMAGServerConfigurationClient(String adminUserId,
                                         String serverName,
                                         String serverPlatformRootURL,
                                         String connectionUserId,
                                         String connectionPassword) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);
            invalidParameterHandler.validateUserId(adminUserId, methodName);

            this.adminUserId           = adminUserId;
            this.serverName            = serverName;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new AdminServicesRESTClient(serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Remove the configuration for the server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearOMAGServerConfig() throws OMAGNotAuthorizedException,
                                               OMAGInvalidParameterException,
                                               OMAGConfigurationErrorException
    {
        final String methodName  = "clearOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Return the derived server type that is classified based on the configuration values.
     *
     * @return server classification description
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public ServerTypeClassificationSummary getServerTypeClassification() throws OMAGNotAuthorizedException,
                                                                                OMAGInvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "getServerTypeClassification";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/server-type-classification";


        ServerTypeClassificationResponse response = restClient.callServerClassificationGetRESTCall(methodName,
                                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                                   adminUserId,
                                                                                                   serverName);

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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerURLRoot(String    serverURLRoot) throws OMAGNotAuthorizedException,
                                                                 OMAGInvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName    = "setServerURLRoot";
        final String parameterName = "serverURLRoot";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/server-url-root-for-caller";

        try
        {
            invalidParameterHandler.validateName(serverURLRoot, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(serverURLRoot);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repository's event mapper.
     *
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEventBus(String              connectorProvider,
                            String              topicURLRoot,
                            Map<String, Object> configurationProperties) throws OMAGNotAuthorizedException,
                                                                                OMAGInvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "setEventBus";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/event-bus?connectorProvider={2}&topicURLRoot={3}";

        Map<String, Object> requestBody = configurationProperties;
        if (requestBody == null)
        {
            requestBody = new HashMap<>();
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerType(String    serverType) throws OMAGNotAuthorizedException,
                                                           OMAGInvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "setServerType";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/server-type?typeName={2}";

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        serverType);
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param organizationName  String name of the organization.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setOrganizationName(String    organizationName) throws OMAGNotAuthorizedException,
                                                                       OMAGInvalidParameterException,
                                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "setOrganizationName";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/organization-name?name={2}";

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        organizationName);
    }


    /**
     * Set up the description of this server. The default value is null.
     *
     * @param description  String description of the server
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerDescription(String   description) throws OMAGNotAuthorizedException,
                                                                  OMAGInvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName  = "setServerDescription";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/server-description";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        description,
                                        adminUserId,
                                        serverName,
                                        description);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).  It is also used in the header of HTTP requests if the password is also set.
     *
     * @param serverUserId  String user that the server will use on connections and requests not associated with an end user.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerUserId(String serverUserId) throws OMAGNotAuthorizedException,
                                                            OMAGInvalidParameterException,
                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "setServerUserId";
        final String parameterName = "serverUserId";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/server-user-id?id={2}";

        try
        {
            invalidParameterHandler.validateName(serverUserId, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        serverUserId);
    }


    /**
     * Set up the password to use in the header of HTTP requests with the server's userId.
     *
     * @param serverPassword  String password that the server will use on connections.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerPassword(String serverPassword) throws OMAGNotAuthorizedException,
                                                                OMAGInvalidParameterException,
                                                                OMAGConfigurationErrorException
    {
        final String methodName  = "setServerPassword";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/server-user-password?password={2}";

        NullRequestBody requestBody = new NullRequestBody();

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        serverPassword);
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.  The value is validated server side.
     *
     * @param maxPageSize  max number of elements that can be returned on a request.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setMaxPageSize(int     maxPageSize) throws OMAGNotAuthorizedException,
                                                           OMAGInvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "setMaxPageSize";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/max-page-size?limit={2}";

        if (maxPageSize < 0)
        {
           throw new OMAGInvalidParameterException(OMAGAdminErrorCode.BAD_MAX_PAGE_SIZE.getMessageDefinition(serverName, Integer.toString(maxPageSize)),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName,
                                        Integer.toString(maxPageSize));
    }


    /**
     * Set up the basic server properties in a single request.
     *
     * @param organizationName  String name of the organization.
     * @param serverDescription  String description of the server
     * @param serverUserId  String user that the server will use on connections and requests not associated with an end user.
     * @param serverPassword  String password that the server will use on connections.
     * @param serverURLRoot  String url.
     * @param maxPageSize  max number of elements that can be returned on a request.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setBasicServerProperties(String organizationName,
                                         String serverDescription,
                                         String serverUserId,
                                         String serverPassword,
                                         String serverURLRoot,
                                         int    maxPageSize) throws OMAGNotAuthorizedException,
                                                                    OMAGInvalidParameterException,
                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "setBasicServerProperties";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/server-properties";

        ServerPropertiesRequestBody requestBody = new ServerPropertiesRequestBody();

        requestBody.setOrganizationName(organizationName);
        requestBody.setLocalServerDescription(serverDescription);
        requestBody.setLocalServerUserId(serverUserId);
        requestBody.setLocalServerPassword(serverPassword);
        requestBody.setLocalServerURL(serverURLRoot);
        requestBody.setMaxPageSize(maxPageSize);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Return the basic server properties in a single request.
     *
     * @return basic server properties
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public BasicServerProperties getBasicServerProperties() throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "getBasicServerProperties";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/server-properties";

        BasicServerPropertiesResponse response = restClient.callBasicServerPropertiesGetRESTCall(methodName,
                                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                                 adminUserId,
                                                                                                 serverName);

        return response.getBasicServerProperties();
    }


    /**
     * Set up the default audit log for the server.  This adds the console audit log destination.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setDefaultAuditLog() throws OMAGNotAuthorizedException,
                                            OMAGInvalidParameterException,
                                            OMAGConfigurationErrorException
    {
        final String methodName  = "setDefaultAuditLog";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/default";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addConsoleAuditLogDestination(List<String> supportedSeverities) throws OMAGNotAuthorizedException,
                                                                                       OMAGInvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "addConsoleAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/console";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add an audit log destination that creates slf4j records.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addSLF4JAuditLogDestination(List<String> supportedSeverities) throws OMAGNotAuthorizedException,
                                                                                     OMAGInvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName  = "addSLF4JAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/slf4j";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addFileAuditLogDestination(List<String> supportedSeverities) throws OMAGNotAuthorizedException,
                                                                                    OMAGInvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "addFileAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/files";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @param jdbcConnectionString connection string used to connect to the JDBC data base
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addJDBCAuditLogDestination(List<String> supportedSeverities,
                                           String       jdbcConnectionString) throws OMAGNotAuthorizedException,
                                                                                     OMAGInvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName  = "addFileAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/jdbc?connectionString={2}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName,
                                        jdbcConnectionString);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param directoryName optional directory name
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addFileAuditLogDestination(String       directoryName,
                                           List<String> supportedSeverities) throws OMAGNotAuthorizedException,
                                                                                    OMAGInvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "addFileAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/files?directoryName={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName,
                                        directoryName);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addEventTopicAuditLogDestination(List<String> supportedSeverities) throws OMAGNotAuthorizedException,
                                                                                          OMAGInvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "addEventTopicAuditLogDestination";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/event-topic";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param topicName name of topic for audit log entries
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addEventTopicAuditLogDestination(String       topicName,
                                                 List<String> supportedSeverities) throws OMAGNotAuthorizedException,
                                                                                          OMAGInvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "addEventTopicAuditLogDestination(with topicName)";
        final String topicNameParameter = "topicName";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/event-topic?topicName={2}";

        try
        {
            invalidParameterHandler.validateName(topicName, topicNameParameter, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        supportedSeverities,
                                        adminUserId,
                                        serverName,
                                        topicName);
    }


    /**
     * Add an audit log destination that is defined by the supplied connection object.
     *
     * @param connection connection object that defines the audit log destination
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addAuditLogDestination(Connection connection) throws OMAGNotAuthorizedException,
                                                                     OMAGInvalidParameterException,
                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "addAuditLogDestination";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/connection";

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
     * Replace an audit log destination connection identified by the supplied audit log destination connection name with the
     * supplied audit log destination connection.
     *
     * @param suppliedConnectionName the qualified name of the audit log destination to update.
     * @param connection connection object that replaces the existing one.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void updateAuditLogDestination(String     suppliedConnectionName,
                                          Connection connection) throws OMAGNotAuthorizedException,
                                                                        OMAGInvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "UpdateAuditLogDestination";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/connection/{2}";

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
                                        serverName,
                                        suppliedConnectionName);
    }


    /**
     * Delete an audit log destination that is defined by the supplied audit log destination connection name
     *
     * @param connectionName the qualified name of the audit log destination connection to delete.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAuditLogDestination(String connectionName) throws OMAGNotAuthorizedException,
                                                                       OMAGInvalidParameterException,
                                                                       OMAGConfigurationErrorException
    {
        final String methodName    = "clearAuditLogDestination";
        final String parameterName = "connectionName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations/connection/{2}";

        try
        {
            invalidParameterHandler.validateName(connectionName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        adminUserId,
                                        serverName,
                                        connectionName);
    }



    /**
     * Set up all audit log destinations for this server.  Any existing definitions are overwritten
     *
     * @param auditLogDestinations list of connections
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setAuditLogDestinations(List<Connection> auditLogDestinations) throws OMAGNotAuthorizedException,
                                                                                      OMAGInvalidParameterException,
                                                                                      OMAGConfigurationErrorException
    {
        final String methodName    = "setAuditLogDestinations";
        final String parameterName = "auditLogDestinations";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations";

        try
        {
            if (auditLogDestinations != null)
            {
                for (Connection connection : auditLogDestinations)
                {
                    invalidParameterHandler.validateConnection(connection, parameterName, methodName);
                }
            }
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }


        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        auditLogDestinations,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Clears all audit log destinations for this server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAuditLogDestinations() throws OMAGNotAuthorizedException,
                                                   OMAGInvalidParameterException,
                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "clearAuditLogDestinations";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/audit-log-destinations";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Override the default server security connector.
     *
     * @param connection connection object that defines the server security connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setServerSecurityConnection(Connection connection) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "setConfigurationStoreConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/security/connection";

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
     * Clear the connection object for the server security connection which means the
     * server does no authorization checks when a request is made to this server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearServerSecurityConnection() throws OMAGNotAuthorizedException,
                                                       OMAGInvalidParameterException,
                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "clearServerSecurityConnection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/security/connection";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Return the connection object for the server security connector.  Null is returned if the server
     * does not have a server security connection.
     *
     * @return server security connection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Connection getServerSecurityConnection() throws OMAGNotAuthorizedException,
                                                           OMAGInvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "getServerSecurityConnection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/security/connection";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             adminUserId,
                                                                             serverName);

        return restResult.getConnection();
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param serverConfig configuration for the server
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setOMAGServerConfig(OMAGServerConfig serverConfig) throws OMAGNotAuthorizedException,
                                                                          OMAGConfigurationErrorException,
                                                                          OMAGInvalidParameterException
    {
        final String methodName    = "setOMAGServerConfig";
        final String parameterName = "serverConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";

        try
        {
            invalidParameterHandler.validateObject(serverConfig, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void deployOMAGServerConfig(String destinationPlatformURLRoot) throws OMAGNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException,
                                                                                 OMAGInvalidParameterException
    {
        final String methodName    = "deployOMAGServerConfig";
        final String parameterName = "destinationPlatformURLRoot";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration/deploy";

        try
        {
            invalidParameterHandler.validateName(destinationPlatformURLRoot, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(destinationPlatformURLRoot);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public OMAGServerConfig getOMAGServerConfig() throws OMAGNotAuthorizedException,
                                                         OMAGInvalidParameterException,
                                                         OMAGConfigurationErrorException
    {
        final String methodName  = "getOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         adminUserId,
                                                                                         serverName);

        return restResult.getOMAGServerConfig();
    }


    /**
     * Return the complete set of configuration properties in use by a running instance of the server.
     *
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public OMAGServerConfig getOMAGServerInstanceConfig() throws OMAGNotAuthorizedException,
                                                                 OMAGInvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "getOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/instance/configuration";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         adminUserId,
                                                                                         serverName);

        return restResult.getOMAGServerConfig();
    }

}
