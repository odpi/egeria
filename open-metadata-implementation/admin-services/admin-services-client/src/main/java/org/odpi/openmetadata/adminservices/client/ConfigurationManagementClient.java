/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;


/**
 * ConfigurationManagementClient is able to issue calls to an OMAG Server Platform that manages configuration
 * for OMAG Servers.  This includes storing and retrieving configuration and deploying it between platforms.
 */
public class ConfigurationManagementClient
{
    private String serverName;               /* Initialized in constructor */
    private String serverPlatformRootURL;    /* Initialized in constructor */

    private InvalidParameterHandler         invalidParameterHandler = new InvalidParameterHandler();
    private AdminServicesRESTClient         restClient;               /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ConfigurationManagementClient(String serverName,
                                         String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

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
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ConfigurationManagementClient(String serverName,
                                         String serverPlatformRootURL,
                                         String userId,
                                         String password) throws OMAGInvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

            this.serverName            = serverName;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new AdminServicesRESTClient(serverName, serverPlatformRootURL, userId, password);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }

    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param userId       user that is issuing the request
     * @param serverConfig configuration for the server
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws OMAGInvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void setOMAGServerConfig(String           userId,
                                    OMAGServerConfig serverConfig) throws OMAGNotAuthorizedException,
                                                                          OMAGConfigurationErrorException,
                                                                          OMAGInvalidParameterException
    {
        final String methodName  = "setOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverConfig,
                                        userId,
                                        serverName);
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param userId                     user that is issuing the request
     * @param serverName                 local server name
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws OMAGInvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void deployOMAGServerConfig(String userId,
                                       String serverName,
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
                                        userId,
                                        serverName);
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId     user that is issuing the request
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getOMAGServerConfig(String userId,
                                                String serverName) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         userId,
                                                                                         serverName);

        return restResult.getOMAGServerConfig();
    }
}
