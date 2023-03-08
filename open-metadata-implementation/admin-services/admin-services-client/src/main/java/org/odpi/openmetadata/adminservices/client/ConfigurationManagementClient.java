/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigsResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Set;


/**
 * ConfigurationManagementClient is able to issue calls to an OMAG Server Platform that manages configuration
 * for OMAG Servers.  This includes storing and retrieving configuration and deploying it between platforms.
 */
public class ConfigurationManagementClient
{
    private String adminUserId;              /* Initialized in constructor */
    private String serverPlatformRootURL;    /* Initialized in constructor */

    private InvalidParameterHandler         invalidParameterHandler = new InvalidParameterHandler();
    private AdminServicesRESTClient         restClient;               /* Initialized in constructor */

    private static final String NULL_SERVER_NAME = "<*>";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ConfigurationManagementClient(String adminUserId,
                                         String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, NULL_SERVER_NAME, methodName);

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
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ConfigurationManagementClient(String adminUserId,
                                         String serverPlatformRootURL,
                                         String userId,
                                         String password) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, NULL_SERVER_NAME, methodName);

            this.adminUserId           = adminUserId;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new AdminServicesRESTClient(NULL_SERVER_NAME, serverPlatformRootURL, userId, password);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Return all the OMAG Server configurations that are stored on this platform.
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
        final String methodName  = "getAllServerConfigurations";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/configurations";

        OMAGServerConfigsResponse restResult = restClient.callGetAllServerConfigurationsRESTCall(methodName,
                                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                                 adminUserId);

        return restResult.getOMAGServerConfigs();
    }



    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param serverConfig configuration for the server
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws OMAGInvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void setOMAGServerConfig(OMAGServerConfig serverConfig) throws OMAGNotAuthorizedException,
                                                                          OMAGConfigurationErrorException,
                                                                          OMAGInvalidParameterException
    {
        final String methodName  = "setOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverConfig,
                                        adminUserId,
                                        serverConfig.getLocalServerName());
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param serverName                 local server name
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws OMAGInvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void deployOMAGServerConfig(String serverName,
                                       String destinationPlatformURLRoot) throws OMAGNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException,
                                                                                 OMAGInvalidParameterException
    {
        final String methodName  = "deployOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration/deploy";

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
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getOMAGServerConfig(String serverName) throws OMAGNotAuthorizedException,
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
}
