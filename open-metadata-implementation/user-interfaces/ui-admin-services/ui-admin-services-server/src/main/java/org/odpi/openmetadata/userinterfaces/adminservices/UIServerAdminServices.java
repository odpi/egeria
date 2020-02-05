/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.openmetadata.userinterface.adminservices.rest.UIServerConfigResponse;
import org.odpi.openmetadata.adminservices.OMAGServerExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UIServerAdminServices provides part of the server-side implementation of the administrative interface for
 * an User Interface (UI) Server.  In particular, this class supports the configuration
 * of the server name, server type and organization name.  It also supports the setting up of the
 * Open Metadata Repository Services' local repository and cohort.
 */
public class UIServerAdminServices
{
    private static final Logger log = LoggerFactory.getLogger(UIServerAdminServices.class);

    private UIServerAdminStoreServices configStore = new UIServerAdminStoreServices();
    private UIServerErrorHandler errorHandler = new UIServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();
    /*
     * =============================================================
     * Configure server - basic options using defaults
     */


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param name  String name of the organization.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    public VoidResponse setOrganizationName(String userId,
                                            String serverName,
                                            String name)
    {
        final String methodName = "setOrganizationName";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ("".equals(name))
            {
                name = null;
            }

            if (name == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's owning organization's name.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's owning organization's name to " + name + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setOrganizationName(name);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverUserId  String user is for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    public VoidResponse setServerUserId(String userId,
                                        String serverName,
                                        String serverUserId)
    {
        final String methodName = "setLocalServerUserId";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerName(serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ("".equals(serverUserId))
            {
                serverUserId = null;
            }

            if (serverUserId == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's userId.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's userId to " + serverUserId + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerUserId(serverUserId);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverPassword  String password for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    public VoidResponse setServerPassword(String userId,
                                          String serverName,
                                          String serverPassword)
    {
        final String methodName = "setLocalServerPassword";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerName(serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ("".equals(serverPassword))
            {
                serverPassword = null;
            }

            if (serverPassword == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's password.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's password to " + serverPassword + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerPassword(serverPassword);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param maxPageSize - max number of elements that can be returned on a request.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or maxPageSize parameter.
     */
    public VoidResponse setMaxPageSize(String  userId,
                                       String  serverName,
                                       int     maxPageSize)
    {
        final String methodName = "setMaxPageSize";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            if (maxPageSize > 0)
            {
                UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

                List<String>  configAuditTrail = serverConfig.getAuditTrail();

                if (configAuditTrail == null)
                {
                    configAuditTrail = new ArrayList<>();
                }

                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for maximum page size to " + maxPageSize + ".");

                serverConfig.setAuditTrail(configAuditTrail);
                serverConfig.setMaxPageSize(maxPageSize);

                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
            else
            {
                UIAdminErrorCode errorCode = UIAdminErrorCode.BAD_MAX_PAGE_SIZE;
                String        errorMessage = errorCode.getErrorMessageId()
                                           + errorCode.getFormattedErrorMessage(serverName, Integer.toString(maxPageSize));

                throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        errorMessage,
                                                        errorCode.getSystemAction(),
                                                        errorCode.getUserAction());
            }
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


//
//    /**
//     * Set up the root URL for this server that is used to construct full URL paths to calls for
//     * this server's REST interfaces that is used by other members of the cohorts that this server
//     * connects to.
//     *
//     * The default value is "localhost:8080".
//     *
//     * ServerURLRoot is used during the configuration of the local repository.  If called
//     * after the local repository is configured, it has no effect.
//     *
//     * @param userId  user that is issuing the request.
//     * @param serverName  local server name.
//     * @param url  String url.
//     * @return void response or
//     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
//     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
//     */
//    public VoidResponse setServerURLRoot(String userId,
//                                         String serverName,
//                                         String url)
//    {
//        final String methodName = "setServerURLRoot";
//
//        log.debug("Calling method: " + methodName);
//
//        VoidResponse response = new VoidResponse();
//
//        try
//        {
//            errorHandler.validateUserId(userId, serverName, methodName);
//            errorHandler.validateServerName(serverName, methodName);
//
//            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
//
//            List<String>  configAuditTrail          = serverConfig.getAuditTrail();
//
//            if (configAuditTrail == null)
//            {
//                configAuditTrail = new ArrayList<>();
//            }
//
//            if ("".equals(url))
//            {
//                url = null;
//            }
//
//            if (url == null)
//            {
//                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's URL root.");
//            }
//            else
//            {
//                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's URL root to " + url + ".");
//            }
//
//            serverConfig.setAuditTrail(configAuditTrail);
//
//            configStore.saveServerConfig(serverName, methodName, serverConfig);
//        }
//        catch (OMAGInvalidParameterException error)
//        {
//            exceptionHandler.captureInvalidParameterException(response, error);
//        }
//        catch (OMAGNotAuthorizedException error)
//        {
//            exceptionHandler.captureNotAuthorizedException(response, error);
//        }
//        catch (Throwable  error)
//        {
//            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
//        }
//
//        log.debug("Returning from method: " + methodName + " with response: " + response.toString());
//
//        return response;
//    }
//
//
    /*
     * =============================================================
     * Configure server - advanced options overriding defaults
     */



    /**
     * Set up the configuration properties for an UI Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param uiServerConfig  configuration for the server
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or UIServerConfig parameter.
     */
    public VoidResponse setUIServerConfig(String           userId,
                                          String         serverName,
                                          UIServerConfig uiServerConfig)
    {
        final String methodName = "setUIServerConfig";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateConfigServerName(serverName,uiServerConfig.getLocalServerName(),methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            if (uiServerConfig == null)
            {
                UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_SERVER_CONFIG;
                String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        errorMessage,
                                                        errorCode.getSystemAction(),
                                                        errorCode.getUserAction());
            }
            errorHandler.validateUIconfiguration(serverName,uiServerConfig,methodName);

            List<String>  configAuditTrail = uiServerConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " deployed configuration for server.");

            uiServerConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, uiServerConfig);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }

//TODO Support deploying one UI config to another UI server tracked in issue #1685. Uncomment and correct the following code
//    /**
//     * Push the configuration for the server to another UI Server Platform.
//     *
//     * @param userId  user that is issuing the request
//     * @param serverName  local server name
//     * @param destinationPlatform  location of the platform where the config is to be deployed to
//     * @return void response or
//     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
//     * UIConfigurationErrorException there is a problem using the supplied configuration or
//     * OMAGInvalidParameterException invalid serverName or destinationPlatform parameter.
//     */
//    public VoidResponse deployUIServerConfig(String         userId,
//                                               String         serverName,
//                                               URLRequestBody destinationPlatform)
//    {
//        final String methodName = "deployUIServerConfig";
//
//        log.debug("Calling method: " + methodName);
//
//        VoidResponse response = new VoidResponse();
//
//        try
//        {
//            errorHandler.validateServerName(serverName, methodName);
//            errorHandler.validateUserId(userId, serverName, methodName);
//
//            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
//
//            String  serverURLRoot = serverConfig.getLocalServerURL();
//
//            if ((destinationPlatform != null) && (destinationPlatform.getUrlRoot() != null))
//            {
//                serverURLRoot = destinationPlatform.getUrlRoot();
//            }
//
//            ConfigurationManagementClient client = new ConfigurationManagementClient(serverName,
//                                                                                     serverURLRoot);
//
//            client.setUIServerConfig(userId, serverConfig);
//        }
//        catch (OMAGInvalidParameterException error)
//        {
//            exceptionHandler.captureInvalidParameterException(response, error);
//        }
//        catch (OMAGNotAuthorizedException error)
//        {
//            exceptionHandler.captureNotAuthorizedException(response, error);
//        }
//        catch (OMAGConfigurationErrorException error)
//        {
//            exceptionHandler.captureConfigurationErrorException(response, error);
//        }
//        catch (Throwable  error)
//        {
//            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
//        }
//
//        log.debug("Returning from method: " + methodName + " with response: " + response.toString());
//
//        return response;
//    }


    /*
     * =============================================================
     * Query current configuration
     */

    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return UIServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public UIServerConfigResponse getStoredConfiguration(String userId,
                                                         String serverName)
    {
        final String methodName = "getStoredConfiguration";

        log.debug("Calling method: " + methodName);

        UIServerConfigResponse response = new UIServerConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            response.setUIServerConfig(configStore.getServerConfig(userId, serverName, methodName));
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
