/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIInvalidParameterException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UINotAuthorizedException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIConfigurationErrorException;
/**
 * ConfigurationManagementClient is able to issue calls to an UI Server PPlatform that manages configuration
 * for UI Servers.
 */
public class ConfigurationManagementClient
{
    private String                  serverName;               /* Initialized in constructor */
    private String                  serverPlatformRootURL;    /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    private UIAdminServicesRESTClient restClient;               /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws UIInvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConfigurationManagementClient(String serverName,
                                         String serverPlatformRootURL) throws UIInvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

            this.serverName = serverName;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new UIAdminServicesRESTClient(serverName, serverPlatformRootURL);
        }
        catch (InvalidParameterException error)
        {
            throw new UIInvalidParameterException(error);
        }
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws UIInvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConfigurationManagementClient(String serverName,
                                         String serverPlatformRootURL,
                                         String userId,
                                         String password) throws UIInvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

            this.serverName = serverName;
            this.serverPlatformRootURL = serverPlatformRootURL;

            this.restClient = new UIAdminServicesRESTClient(serverName, serverPlatformRootURL, userId, password);
        }
        catch (InvalidParameterException error)
        {
            throw new UIInvalidParameterException(error);
        }
    }


    /**
     * Set up the configuration properties for an UI Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param uiServerConfig  configuration for the server
     *
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIConfigurationErrorException there is a problem using the supplied configuration or
     * @throws UIInvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    public void setUIServerConfig(String           userId,
                                    UIServerConfig uiServerConfig) throws UINotAuthorizedException,
                                                                              UIConfigurationErrorException,
                                                                              UIInvalidParameterException
    {
        final String methodName = "deployUIServerConfig";

        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/configuration";


        try
        {
            VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                      serverPlatformRootURL + urlTemplate,
                                                                      uiServerConfig,
                                                                      userId,
                                                                      serverName);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
        }
        catch (InvalidParameterException  error)
        {
            throw new UIInvalidParameterException(error);
        }
        catch (UserNotAuthorizedException  error)
        {
            throw new UINotAuthorizedException(error);
        }
        catch (PropertyServerException  error)
        {
            throw new UIConfigurationErrorException(error);
        }
    }
}
