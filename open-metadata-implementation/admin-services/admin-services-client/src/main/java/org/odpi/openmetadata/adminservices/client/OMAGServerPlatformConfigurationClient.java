/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.client.rest.AdminServicesRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigsResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Set;


/**
 * OMAGServerPlatformConfigurationClient provides services to configure an OMAG Server Platform.
 * There are two aspects to this. Firstly setting up the connector for the configuration
 * document store.  Then secondly, configuring the platform security connector.
 */
public class OMAGServerPlatformConfigurationClient
{
    protected String adminUserId;              /* Initialized in constructor */
    protected String serverPlatformRootURL;    /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final AdminServicesRESTClient restClient;               /* Initialized in constructor */

    private static final String NULL_SERVER_NAME = "<*>";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public OMAGServerPlatformConfigurationClient(String adminUserId,
                                                 String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, methodName);
            invalidParameterHandler.validateUserId(adminUserId, methodName);

            this.adminUserId           = adminUserId;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new AdminServicesRESTClient(NULL_SERVER_NAME, serverPlatformRootURL);
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
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public OMAGServerPlatformConfigurationClient(String adminUserId,
                                                 String serverPlatformRootURL,
                                                 String connectionUserId,
                                                 String connectionPassword) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, methodName);
            invalidParameterHandler.validateUserId(adminUserId, methodName);

            this.adminUserId           = adminUserId;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new AdminServicesRESTClient(NULL_SERVER_NAME, serverPlatformRootURL, connectionUserId, connectionPassword);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Override the default server configuration document.
     *
     * @param defaultServerConfig values to include in every new configured server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setDefaultOMAGServerConfig(OMAGServerConfig defaultServerConfig) throws OMAGNotAuthorizedException,
                                                                                        OMAGInvalidParameterException,
                                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "setDefaultOMAGServerConfig";
        final String parameterName = "defaultServerConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/stores/default-configuration-document";

        try
        {
            invalidParameterHandler.validateObject(defaultServerConfig, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        defaultServerConfig,
                                        adminUserId);
    }


    /**
     * Return the default server configuration document.
     *
     * @return connection response
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public OMAGServerConfig getDefaultOMAGServerConfig() throws OMAGNotAuthorizedException,
                                                                OMAGInvalidParameterException,
                                                                OMAGConfigurationErrorException
    {
        final String methodName  = "getDefaultOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/stores/default-configuration-document";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         adminUserId);

        return restResult.getOMAGServerConfig();
    }


    /**
     * Clear the default configuration document.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearDefaultServerConfig() throws OMAGNotAuthorizedException,
                                                  OMAGInvalidParameterException,
                                                  OMAGConfigurationErrorException
    {
        final String methodName  = "clearDefaultServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/stores/default-configuration-document";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId);
    }


    /**
     * Override the default implementation or configuration of the configuration document store.
     *
     * @param connection connection object that defines the configuration document store
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setConfigurationStoreConnection(Connection connection) throws OMAGNotAuthorizedException,
                                                                              OMAGInvalidParameterException,
                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setConfigurationStoreConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/stores/connection";

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
                                        adminUserId);
    }


    /**
     * Clear the connection object for the configuration store which means the platform uses the default store.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearConfigurationStoreConnection() throws OMAGNotAuthorizedException,
                                                           OMAGInvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName  = "clearConfigurationStoreConnection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/stores/connection";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId);
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server is
     * using the default store with the default configuration.
     *
     * @return Platform security connection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Connection getConfigurationStoreConnection() throws OMAGNotAuthorizedException,
                                                               OMAGInvalidParameterException,
                                                               OMAGConfigurationErrorException
    {
        final String methodName  = "getConfigurationStoreConnection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/stores/connection";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             adminUserId);

        return restResult.getConnection();
    }


    /**
     * Set up a platform security connector.  This connector provides additional authorization
     * checks on API requests to the platform.
     *
     * @param connection connection object that defines the platform security connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    @Deprecated
    public void setPlatformSecurityConnection(Connection connection) throws OMAGNotAuthorizedException,
                                                                            OMAGInvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "setPlatformSecurityConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/platform/security/connection";

        try
        {
            invalidParameterHandler.validateConnection(connection, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        PlatformSecurityRequestBody requestBody = new PlatformSecurityRequestBody();

        requestBody.setPlatformSecurityConnection(connection);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId);
    }


    /**
     * Clear the connection object for platform security.  This means there is no platform security set up
     * and there will be no authorization checks within the platform.  All security will have to
     * come from the surrounding deployment environment.
     * This is the default state.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    @Deprecated
    public void clearPlatformSecurityConnection() throws OMAGNotAuthorizedException,
                                                         OMAGInvalidParameterException,
                                                         OMAGConfigurationErrorException
    {
        final String methodName  = "clearPlatformSecurityConnection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/platform/security/connection";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId);
    }


    /**
     * Return the connection object for platform security connector.  Null is returned if no platform security
     * has been set up.
     *
     * @return Platform security connection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    @Deprecated
    public Connection getPlatformSecurityConnection() throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getPlatformSecurityConnection";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/platform/security/connection";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                   adminUserId);

        return restResult.getConnection();
    }



    /**
     * Return all the OMAG Server configurations that are stored on this platform
     *
     * @return the OMAG Server configurations that are stored on this platform
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Set<OMAGServerConfig> getAllServerConfigurations() throws OMAGNotAuthorizedException,
                                                                     OMAGConfigurationErrorException,
                                                                     OMAGInvalidParameterException
    {
        final String methodName  = "getPlatformServerConfigs";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/configurations";

        OMAGServerConfigsResponse restResult = restClient.callGetAllServerConfigurationsRESTCall(methodName,
                                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                                 adminUserId);

        return restResult.getOMAGServerConfigs();
    }
}
