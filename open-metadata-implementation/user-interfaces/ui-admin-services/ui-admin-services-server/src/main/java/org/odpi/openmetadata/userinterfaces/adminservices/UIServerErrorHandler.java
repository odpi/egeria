/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceOperationalStatus;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceRegistration;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * UIServerErrorHandler provides common error handling routines for the ui admin services
 */
public class UIServerErrorHandler
{

    private static final String allGovernanceServices = getAllValidGovernanceServerURLMarkers();

    private static String getAllValidGovernanceServerURLMarkers() {
        String allServerNamesStr = "";
        Set<String> allGovernanceServerURLMarkers = GovernanceServicesDescription.getGovernanceServersURLMarkers();
        for (String urlMarker:allGovernanceServerURLMarkers) {
            allServerNamesStr= allServerNamesStr + " " + urlMarker;
        }
        return allServerNamesStr;
    }

    /**
     * Default constructor
     */
    public UIServerErrorHandler()
    {
    }


    /**
     * Validate that the user id is not null.
     *
     * @param userId  user name passed on the request
     * @param serverName  name of this server
     * @param methodName  method receiving the call
     * @throws OMAGNotAuthorizedException no userId provided
     */
    public void validateUserId(String userId,
                        String serverName,
                        String methodName) throws OMAGNotAuthorizedException
    {
        if (userId == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_USER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction());
        }
    }



    /**
     * Validate that the server name is not null.
     *
     * @param serverName  serverName passed on a request
     * @param methodName  method being called
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException null server name
     */
    public void validateServerName(String serverName,
                            String methodName) throws InvalidParameterException
    {
        /*
         * If the local server name is still null then save the server name in the configuration.
         */
        if (serverName == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_LOCAL_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    "serverName");
        }
    }


    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param configServerName serverName passed in config (should match request name)
     * @param methodName  method being called
     * @throws OMAGConfigurationErrorException incompatible server names
     */
    public void validateConfigServerName(String serverName,
                                  String configServerName,
                                  String methodName) throws OMAGConfigurationErrorException
    {
        if (!serverName.equals(configServerName))
        {
            UIAdminErrorCode errorCode = UIAdminErrorCode.INCOMPATIBLE_SERVER_NAMES;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,
                                                                                                            configServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());

        }
    }
    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param metadataServerName  metadata server name passed on a request
     * @param configServerName serverName passed in config (should match request name)
     * @param methodName  method being called
     * @throws OMAGConfigurationErrorException incompatible server names
     */
    public void validateConfigMetadataServerName(String metadataServerName,
                                         String configServerName,
                                         String methodName) throws OMAGConfigurationErrorException
    {
        if (!metadataServerName.equals(configServerName))
        {
            UIAdminErrorCode errorCode = UIAdminErrorCode.NULL_METADATA_SERVER_NAME;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(metadataServerName,
                    configServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
    }
    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param metadataServerURL server URL passed on a request
     * @param configServerName serverName passed in config
     * @param methodName  method being called
     * @throws OMAGConfigurationErrorException incompatible server names
     */
    public void validateConfigMetadataServeURL(String metadataServerURL,
                                                 String configServerName,
                                                 String methodName) throws OMAGConfigurationErrorException
    {
        if (!metadataServerURL.equals(configServerName))
        {
            UIAdminErrorCode errorCode = UIAdminErrorCode.NULL_METADATA_SERVER_URL;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(metadataServerURL,
                    configServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
    }

    /**
     * Validate that the connection is not null.
     *
     * @param connection  connection passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws InvalidParameterException the connection is null
     */
    public void validateConnection(Connection connection,
                            String     serverName,
                            String     methodName) throws InvalidParameterException
    {
        if (connection == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_CONNECTION;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    "Connection");
        }
    }


    /**
     * Validate that the connection is not null.
     *
     * @param connection  connection passed on the request
     * @param methodName  method called
     * @throws InvalidParameterException the connection is null
     */
    public void validateConnection(Connection connection,
                            String     methodName) throws InvalidParameterException
    {
        if (connection == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_PLATFORM_CONNECTION;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    "connection");
        }
    }

    /**
     * Validate the metadata server name is not null
     * @param serverName local UI server name
     * @param methodName method name
     * @param metadataServerName - metadata server name to check
     * @throws InvalidParameterException thrown is the metadata server name is null
     */
    public void validateMetadataServerName(String serverName, String methodName, String metadataServerName) throws InvalidParameterException{
        if (metadataServerName == null) {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_METADATA_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    "metadataServerName");
        }
    }
    /**
     * Validate the metadata server URL is not null or does not look like a URL
     * @param serverName local UI server name
     * @param methodName method name
     * @param metadataServerURL - metadata server URL to check
     * @throws InvalidParameterException thrown is the metadata server URL is not valid
     */
    public void validateMetadataServerURL(String serverName, String methodName, String metadataServerURL) throws InvalidParameterException{

        if (metadataServerURL == null) {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_METADATA_SERVER_URL;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    "metadataServerURL");
        }
        if (!isURLValid(metadataServerURL)) {
            UIAdminErrorCode errorCode = UIAdminErrorCode.INVALID_METADATA_SERVER_URL;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName, metadataServerURL);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),"metadataServerURL");
        }
    }

    /**
     * Validate the open lineage server name is not null
     * @param serverName local UI server name
     * @param methodName method name
     * @param openLineageServerName - open lineage server name to check
     * @throws OMAGConfigurationErrorException open lineage server name is null
     */
    public void validateLineageServerName(String serverName, String methodName, String openLineageServerName) throws OMAGConfigurationErrorException{
        if (openLineageServerName == null) {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_OPEN_LINEAGE_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
    /**
     * Validate the open lineage server URL is not null or does not look like a URL
     * @param serverName local UI server name
     * @param methodName method name
     * @param openLineageServerURL - open lineage server URL to check
     * @throws OMAGConfigurationErrorException open lineage serverURL is null or invlaid
     */
    public void validateLineageServerURL(String serverName, String methodName, String openLineageServerURL) throws OMAGConfigurationErrorException{

        if (openLineageServerURL == null) {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_OPEN_LINEAGE_SERVER_URL;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        if (!isURLValid(openLineageServerURL)) {
            UIAdminErrorCode errorCode = UIAdminErrorCode.INVALID_OPEN_LINEAGE_SERVER_URL;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName, openLineageServerURL);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Validate the UI server config
     * @param serverName serverName - local server name
     * @param uiServerConfig the UI server config to validate
     * @param methodName the method name for diagnostics
     * @throws InvalidParameterException the ui configuration is not valid
     */
    public void validateUIconfiguration(String serverName,
                                        UIServerConfig uiServerConfig,
                                        String methodName) throws InvalidParameterException {

        validateMetadataServerName(serverName, methodName, uiServerConfig.getMetadataServerName());
        validateMetadataServerURL(serverName, methodName, uiServerConfig.getMetadataServerURL());
    }
    /**
     * A non-null string that is expected to be a URL is passed to be validated
     * @param candidateURL candidate url to check for validity
     * @return true is valid
     */
   private boolean isURLValid(String candidateURL) {
        boolean isValid = false;
            try {
                new URL(candidateURL);
                isValid = true;
            } catch (MalformedURLException e) {
                // catch url error
            }
        return isValid;
    }

    /**
     * Check that the view service is registered for the server, by checking the supplied registration, and throwing the appropriate
     * exception if not valid.
     *
     * @param registration registration
     * @param serviceURLMarker service URI marker
     * @param serverName server name
     * @param methodName caller
     * @throws OMAGConfigurationErrorException configuration error occurred because the view service registration was not valid.
     */
    public void validateViewServiceIsRegistered(ViewServiceRegistration registration, String serviceURLMarker, String serverName, String methodName) throws OMAGConfigurationErrorException {
        if (registration == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.VIEW_SERVICE_NOT_RECOGNIZED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, serviceURLMarker);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        else if (registration.getViewServiceOperationalStatus() != ViewServiceOperationalStatus.ENABLED)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.VIEW_SERVICE_NOT_ENABLED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, serviceURLMarker);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

    }
}
