/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.properties.ServerServicesStatus;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * OMAGServerOperationsClient provides services to start and stop an OMAG Server.
 */
public class OMAGServerOperationsClient
{
    protected String adminUserId;              /* Initialized in constructor */
    protected String serverName;               /* Initialized in constructor */
    protected String serverPlatformRootURL;    /* Initialized in constructor */

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
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
    public OMAGServerOperationsClient(String adminUserId,
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
    public OMAGServerOperationsClient(String adminUserId,
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


    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @return success message
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public String activateWithStoredConfig() throws OMAGNotAuthorizedException,
                                                    OMAGInvalidParameterException,
                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "activateWithStoredConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/instance";

        SuccessMessageResponse restResult = restClient.callSuccessMessagePostRESTCall(methodName,
                                                                                      serverPlatformRootURL + urlTemplate,
                                                                                      null,
                                                                                      adminUserId,
                                                                                      serverName);

        return restResult.getSuccessMessage();
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param configuration  properties used to initialize the services
     * @return success message
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public String activateWithSuppliedConfig(OMAGServerConfig configuration) throws OMAGNotAuthorizedException,
                                                                                    OMAGInvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "activateWithSuppliedConfig";
        final String parameterName = "configuration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/instance/configuration";

        try
        {
            invalidParameterHandler.validateObject(configuration, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        SuccessMessageResponse restResult = restClient.callSuccessMessagePostRESTCall(methodName,
                                                                                      serverPlatformRootURL + urlTemplate,
                                                                                      configuration,
                                                                                      adminUserId,
                                                                                      serverName);

        return restResult.getSuccessMessage();
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void deactivateTemporarily() throws OMAGNotAuthorizedException,
                                               OMAGInvalidParameterException,
                                               OMAGConfigurationErrorException
    {
        final String methodName  = "deactivateTemporarily";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/instance";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */

    public void deactivatePermanently() throws OMAGNotAuthorizedException,
                                               OMAGInvalidParameterException,
                                               OMAGConfigurationErrorException
    {
        final String methodName  = "deactivatePermanently";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @return configuration properties used to initialize the server or null if not running
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public OMAGServerConfig getActiveConfiguration() throws OMAGNotAuthorizedException,
                                                            OMAGInvalidParameterException,
                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "getActiveConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/instance/configuration";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         adminUserId,
                                                                                         serverName);

        return restResult.getOMAGServerConfig();
    }


    /**
     * Return the status of a running server (use platform services to find out if the server is running).
     *
     * @return status of the server
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public ServerServicesStatus getServerStatus() throws OMAGNotAuthorizedException,
                                                         OMAGInvalidParameterException,
                                                         OMAGConfigurationErrorException
    {
        final String methodName  = "getServerStatus";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/instance/status";

        OMAGServerStatusResponse restResult = restClient.callOMAGServerStatusGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         adminUserId,
                                                                                         serverName);

        return restResult.getServerStatus();
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param fileName name of the open metadata archive file.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addOpenMetadataArchiveFile(String fileName) throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "fileName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/instance/open-metadata-archives/file";

        try
        {
            invalidParameterHandler.validateName(fileName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        fileName,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param connection connection for the open metadata archive.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addOpenMetadataArchive(Connection connection) throws OMAGNotAuthorizedException,
                                                                     OMAGInvalidParameterException,
                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/instance/open-metadata-archives/connection";

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
}
