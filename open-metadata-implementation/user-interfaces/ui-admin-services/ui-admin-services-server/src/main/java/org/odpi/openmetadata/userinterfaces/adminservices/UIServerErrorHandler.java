/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoint;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UIServerErrorHandler provides common error handling routines for the ui admin services
 */
class UIServerErrorHandler
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
    void validateUserId(String userId,
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
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param methodName  method being called
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException null server name
     */
    void validateServerName(String serverName,
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
    void validateConfigServerName(String serverName,
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
     * Validate that the connection is not null.
     *
     * @param connection  connection passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws InvalidParameterException the connection is null
     */
    void validateConnection(Connection connection,
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
    void validateConnection(Connection connection,
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
     * Validate the UI server config
     * @param serverName serverName - local server name
     * @param uiServerConfig the UI server config to validate
     * @param methodName the method name for diagnostics
     * @throws InvalidParameterException the ui configuration is not valid
     */
    public void validateUIconfiguration(String serverName,
                                        UIServerConfig uiServerConfig,
                                        String methodName) throws InvalidParameterException{

        validateMetadataServerName(serverName, methodName, uiServerConfig.getMetadataServerName());
        validateMetadataServerURL(serverName, methodName, uiServerConfig.getMetadataServerURL());
        List<GovernanceServerEndpoint> governanceServerEndpoints = uiServerConfig.getGovernanceServerEndpoints();
        Set<String> governanceServernames = new HashSet<>();
        for (GovernanceServerEndpoint governanceServerEndpoint:governanceServerEndpoints) {
            String governanceServiceName = governanceServerEndpoint.getGovernanceServiceName();
            validateGovernanceServiceName(governanceServiceName,serverName, methodName);
            validateGovernanceServerURL(governanceServerEndpoint.getServerRootURL(), serverName, methodName);
            validateGovernanceServerName(governanceServerEndpoint.getServerName(), serverName, methodName);
            if (governanceServernames.contains(governanceServiceName)) {
                // more than one definition of the same governance server type
                UIAdminErrorCode errorCode    = UIAdminErrorCode.DUPLICATE_GOVERNANCE_SERVERS;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName,governanceServiceName);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        "governanceServiceName");

            } else {
                governanceServernames.add(governanceServiceName);
            }
        }
    }
    /**
     * Validate that the governance server name is not null and matches a governance server.
     *
     * @param governanceServiceName  governance name passed on the request
     * @param serverName  name of this server
     * @param methodName  method receiving the call
     * @throws InvalidParameterException the governance server name is not valid
     */
    public void validateGovernanceServerName(String governanceServiceName,
                                              String serverName,
                                              String methodName) throws InvalidParameterException
    {
        if (governanceServiceName == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_GOVERNANCE_SERVER_NAME;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),"governanceServiceName");
        }
    }
    /*
     * Validate that the governance server URL is not null and looks like a URL
     *
     * @param governanceServiceName  governance server URL passed on the request
     * @param serverName  name of this server
     * @param methodName  method receiving the call
     * @throws InvalidParameterException the governance server URL is not valid
     */
    public void validateGovernanceServerURL(String governanceServiceURL,
                                              String serverName,
                                              String methodName) throws InvalidParameterException
    {
        if (governanceServiceURL == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_GOVERNANCE_SERVER_URL;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),"governanceServiceURL");
        }
        if (!isURLValid(governanceServiceURL)) {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.INVALID_GOVERNANCE_SERVER_URL;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName,governanceServiceURL);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),"governanceServiceURL");

        }
    }
    /**
     * Validate that the governance service name is not valid and matches a governance service. The governance service name is the type of governance server that is being configured.
     *
     * @param governanceServiceName  governance service name passed on the request
     * @param serverName  name of this server
     * @param methodName  method receiving the call
     * @throws InvalidParameterException the governance service name is not valid
     */
    public void validateGovernanceServiceName(String governanceServiceName,
                                               String serverName,
                                               String methodName) throws InvalidParameterException
    {
        if (governanceServiceName == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_GOVERNANCE_SERVICE_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),"governanceServiceName");
        }
       if (!isGovernanceServiceNameValid(governanceServiceName)) {
           UIAdminErrorCode errorCode    = UIAdminErrorCode.INVALID_GOVERNANCE_SERVICE_NAME;
           String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,methodName,governanceServiceName,allGovernanceServices);

           throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                   this.getClass().getName(),
                   methodName,
                   errorMessage,
                   errorCode.getSystemAction(),
                   errorCode.getUserAction(),
                   "governanceServiceName");
       }
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
     * check that the supplied governance service name exists in the GovernanceServicesDescription enum.
     * @param candidateGovernanceServerName
     * @return true is the supplied name matches a governance service URL marker
     */
    private boolean isGovernanceServiceNameValid(String candidateGovernanceServerName) {
        boolean isValid = false;
        Set<String> servicesURLMarkers = GovernanceServicesDescription.getGovernanceServersURLMarkers();
        if (servicesURLMarkers.contains(candidateGovernanceServerName)) {
            isValid =true;
        }
        return isValid;
    }
}
