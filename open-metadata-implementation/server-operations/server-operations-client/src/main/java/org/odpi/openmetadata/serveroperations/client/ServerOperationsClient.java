/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.serveroperations.client;


import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.serveroperations.client.rest.ServerOperationsRESTClient;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;


import java.util.List;

/**
 * ServerOperationsClient is the client for issuing queries about an OMAG Server
 */
public class ServerOperationsClient
{
    private final ServerOperationsRESTClient restClient;                 /* Initialized in constructor */

    private final String                     platformRootURL;            /* Initialized in constructor */
    protected     AuditLog                   auditLog;                   /* Initialized in constructor */

    private final String                     retrieveURLTemplatePrefix   = "/open-metadata/server-operations/users/{1}/server-platform";

    private final InvalidParameterHandler    invalidParameterHandler     = new InvalidParameterHandler();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAG Platform
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ServerOperationsClient(String   platformName,
                                  String   platformRootURL,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new ServerOperationsRESTClient(platformName, platformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ServerOperationsClient(String platformName,
                                  String platformRootURL) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new ServerOperationsRESTClient(platformName, platformRootURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the platform
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ServerOperationsClient(String   platformName,
                                  String   platformRootURL,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new ServerOperationsRESTClient(platformName, platformRootURL, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param platformName name of the server to connect to
     * @param platformRootURL the network address of the platform
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ServerOperationsClient(String platformName,
                                  String platformRootURL,
                                  String userId,
                                  String password) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new ServerOperationsRESTClient(platformName, platformRootURL, userId, password);
    }


    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Retrieve the server status
     *
     * @param userId calling user
     * @param serverName the name of the server
     *
     * @return The server status
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerStatus getServerStatus(String userId,
                                        String serverName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getServerStatus";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/"+serverName+"/status";

        ServerStatusResponse restResult = restClient.callServerStatusGetRESTCall(methodName, urlTemplate, userId);

        ServerStatus serverStatus = new ServerStatus();
        serverStatus.setServerName(restResult.getServerName());
        serverStatus.setIsActive(restResult.isActive());
        serverStatus.setServerStartTime(restResult.getServerStartTime());
        serverStatus.setServerEndTime(restResult.getServerEndTime());
        serverStatus.setServerHistory(restResult.getServerHistory());

        return serverStatus;
    }


    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId calling user
     * @param serverName server to start
     * @return configuration properties used to initialize the server or null if not running
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public OMAGServerConfig getActiveConfiguration(String userId,
                                                   String serverName) throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException
    {
        final String methodName  = "getActiveConfiguration";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/configuration";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName, urlTemplate, userId, serverName);

        return restResult.getOMAGServerConfig();
    }



    /**
     * Return the status of a running server (use platform services to find out if the server is running).
     *
     * @param userId calling user
     * @param serverName server to start
     * @return status of the server
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public ServerServicesStatus getActiveServerStatus(String userId,
                                                      String serverName) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException
    {
        final String methodName  = "getActiveServerStatus";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/status";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        OMAGServerStatusResponse restResult = restClient.callOMAGServerStatusGetRESTCall(methodName, urlTemplate, userId, serverName);

        return restResult.getServerStatus();
    }


    /**
     * Retrieve a list of the active services on a server
     *
     * @param userId calling user
     * @param serverName name of the server
     *
     * @return List of service names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServices(String   userId,
                                          String   serverName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getActiveServices";
        final String serverNameParameter = "serverName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/"+serverName+"/services";

        ServerServicesListResponse restResult = restClient.callServiceListGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServerServicesList();
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId calling user
     * @param serverName server to start
     * @param fileName name of the open metadata archive file.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveFile(String userId,
                                           String serverName,
                                           String fileName) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "fileName";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/open-metadata-archives/file";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateName(fileName, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, fileName, userId, serverName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId calling user
     * @param serverName server to start
     * @param connection connection for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchive(String     userId,
                                       String     serverName,
                                       Connection connection) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "connection";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/open-metadata-archives/connection";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, connection, userId, serverName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId calling user
     * @param serverName server to start
     * @param openMetadataArchive openMetadataArchive for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveContent(String              userId,
                                              String              serverName,
                                              OpenMetadataArchive openMetadataArchive) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveContent";
        final String parameterName = "openMetadataArchive";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/open-metadata-archives/archive-content";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateObject(openMetadataArchive, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, openMetadataArchive, userId, serverName);
    }
}
