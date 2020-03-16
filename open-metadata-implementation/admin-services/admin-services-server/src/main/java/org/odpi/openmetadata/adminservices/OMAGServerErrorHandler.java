/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistration;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;

/**
 * OMAGServerErrorHandler provides common error handling routines for the admin services
 */
public class OMAGServerErrorHandler
{
    /**
     * Default constructor
     */
    public OMAGServerErrorHandler()
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
        if (("".equals(userId)) || (userId == null))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_USER_NAME;
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
     * @throws OMAGInvalidParameterException null server name
     */
    void validateServerName(String serverName,
                            String methodName) throws OMAGInvalidParameterException
    {
        /*
         * If the local server name is still null then save the server name in the configuration.
         */
        if ("".equals(serverName) || (serverName == null))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_LOCAL_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
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
     * @param clientConfig  client config
     * @param methodName  method receiving the call
     * @throws OMAGInvalidParameterException null server name
     */
    void validateOMAGServerClientConfig(String                 serverName,
                                        OMAGServerClientConfig clientConfig,
                                        String                 methodName) throws OMAGInvalidParameterException
    {
        /*
         * If the client config is null then this is an error.
         */
        if (clientConfig == null)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_CLIENT_CONFIG;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }

        validateServerName(serverName, methodName);
        validateOMAGServerServiceRootURL(clientConfig.getOMAGServerPlatformRootURL(), serverName, methodName);
    }



    /**
     * Make sure the event bus properties are set before allowing configuration that is dependent on it to
     * be set.
     *
     * @param serverName  name of the server
     * @param serverConfig  existing configuration
     * @param methodName  calling method
     * @return the event bus config for this server
     * @throws OMAGConfigurationErrorException there is no event bus information
     */
    EventBusConfig validateEventBusIsSet(String           serverName,
                                         OMAGServerConfig serverConfig,
                                         String           methodName) throws OMAGConfigurationErrorException
    {
        EventBusConfig   eventBusConfig = null;

        if (serverConfig != null)
        {
            eventBusConfig = serverConfig.getEventBusConfig();
        }

        if (eventBusConfig == null)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NO_EVENT_BUS_SET;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }

        return eventBusConfig;
    }


    /**
     * Check that a requested access service is registered with this server before allowing the access service to be configured
     * into the configuration document
     *
     * @param registration registration record for the access service.
     * @param serviceURLMarker string used in URL for this access service
     * @param serverName name of the server being configured
     * @param methodName calling method
     * @throws OMAGConfigurationErrorException resulting exception if the access service is not supported.
     */
    void validateAccessServiceIsRegistered(AccessServiceRegistration registration,
                                           String                    serviceURLMarker,
                                           String                    serverName,
                                           String                    methodName) throws OMAGConfigurationErrorException
    {
        if (registration == null)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.ACCESS_SERVICE_NOT_RECOGNIZED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, serviceURLMarker);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }
        else if (registration.getAccessServiceOperationalStatus() != ServiceOperationalStatus.ENABLED)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.ACCESS_SERVICE_NOT_ENABLED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, serviceURLMarker);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }
    }

    /**
     * Check that a requested view service is registered with this server before allowing the view service to be configured
     * into the configuration document
     *
     * @param registration registration record for the view service.
     * @param serviceURLMarker string used in URL for this view service
     * @param serverName name of the server being configured
     * @param methodName calling method
     * @throws OMAGConfigurationErrorException resulting exception if the view service is not supported.
     */
    void validateViewServiceIsRegistered(ViewServiceRegistration registration,
                                         String                  serviceURLMarker,
                                         String                  serverName,
                                         String                  methodName) throws OMAGConfigurationErrorException
    {
        if (registration == null)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.VIEW_SERVICE_NOT_RECOGNIZED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, serviceURLMarker);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }
        else if (registration.getViewServiceOperationalStatus() != ServiceOperationalStatus.ENABLED)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.VIEW_SERVICE_NOT_ENABLED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, serviceURLMarker);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }
    }


    /**
     * Validate that the root URL of the server where an access service resides is not null.
     *
     * @param accessServiceRootURL  remote server name passed on the request
     * @param accessServiceName  name of access service that needs to be contacted
     * @param serverName  server name for this server
     * @param serverService name of the service being configured
     * @throws OMAGInvalidParameterException the root URL is null
     */
    void validateAccessServiceRootURL(String  accessServiceRootURL,
                                      String  accessServiceName,
                                      String  serverName,
                                      String  serverService) throws OMAGInvalidParameterException
    {
        if ((accessServiceRootURL == null) || ("".equals(accessServiceRootURL)))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_ACCESS_SERVICE_ROOT_URL;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverService, serverName, accessServiceName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    serverService,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Validate that the server name of the server where an access service resides is not null.
     *
     * @param accessServiceServerName  remote server name passed on the request
     * @param accessServiceName  name of access service that needs to be contacted
     * @param serverName  server name for this server
     * @param serverService name of the service being configured
     * @throws OMAGInvalidParameterException the name is null
     */
    void validateAccessServiceServerName(String  accessServiceServerName,
                                         String  accessServiceName,
                                         String  serverName,
                                         String  serverService) throws OMAGInvalidParameterException
    {
        if ((accessServiceServerName == null) || ("".equals(accessServiceServerName)))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_ACCESS_SERVICE_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverService, serverName, accessServiceName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    serverService,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Validate that the cohort name is not null.
     *
     * @param cohortName  cohortName passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the cohort name is null
     */
    void validateCohortName(String  cohortName,
                            String  serverName,
                            String  methodName) throws OMAGInvalidParameterException
    {
        if (("".equals(cohortName)) || (cohortName == null))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_COHORT_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Retrieve the cohort configuration for the named cohort or throw exception if it is not
     * found.
     *
     * @param serverName requested server
     * @param serverConfig corresponding configuration
     * @param cohortName requested cohort
     * @param methodName calling method
     * @return configuration for requested cohort
     * @throws OMAGInvalidParameterException cohort is not configured
     */
    CohortConfig validateCohortIsSet(String           serverName,
                                     OMAGServerConfig serverConfig,
                                     String           cohortName,
                                     String           methodName) throws OMAGInvalidParameterException
    {
        RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

        if (repositoryServicesConfig != null)
        {
            List<CohortConfig> cohortConfigs = repositoryServicesConfig.getCohortConfigList();

            if (cohortConfigs != null)
            {
                for (CohortConfig cohortConfig : cohortConfigs)
                {
                    if (cohortConfig != null)
                    {
                        if (cohortName.equals(cohortConfig.getCohortName()))
                        {
                            return cohortConfig;
                        }
                    }
                }
            }
        }

        /*
         * If we get here then the cohort is not configured for this server
         */
        OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.COHORT_NOT_KNOWN;
        String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, cohortName);

        throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
    }


    /**
     * Log that the cohort topic name can not be changed because the format of the topic connection
     * is unexpected.
     *
     * @param cohortName name of the cohort
     * @param serverName name of the server
     * @param methodName calling method
     *
     * @throws OMAGConfigurationErrorException resulting error (always returned)
     */
    void logNoCohortTopicChange(String cohortName,
                                String serverName,
                                String methodName) throws OMAGConfigurationErrorException
    {
        OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.COHORT_TOPIC_STRANGE;
        String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, cohortName);

        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
    }



    /**
     * Validate that the file name is not null.
     *
     * @param fileName  fileName passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the cohort name is null
     */
    void validateFileName(String  fileName,
                          String  serverName,
                          String  methodName) throws OMAGInvalidParameterException
    {
        if (("".equals(fileName)) || (fileName == null))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_FILE_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Validate that the metadata collection name is not null.
     *
     * @param name  name passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the cohort name is null
     */
    void validateMetadataCollectionName(String  name,
                                        String  serverName,
                                        String  methodName) throws OMAGInvalidParameterException
    {
        if (("".equals(name)) || (name == null))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_METADATA_COLLECTION_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
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
     * @throws OMAGInvalidParameterException the connection is null
     */
    void validateConnection(Connection connection,
                            String     serverName,
                            String     methodName) throws OMAGInvalidParameterException
    {
        if (connection == null)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_CONNECTION;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
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
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the connection is null
     */
    void validateConnection(Connection connection,
                            String     methodName) throws OMAGInvalidParameterException
    {
        if (connection == null)
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_PLATFORM_CONNECTION;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }

    /**
     * Validate that an essential OMAG server configuration property has been set.
     *
     * @param property      The property that should be present in the OMAG server configuration.
     * @param propertyName  The name of the property that should be present in the OMAG server configuration.
     * @param serverName    server name for this server.
     * @param methodName    method called.
     * @throws OMAGInvalidParameterException The property is null.
     */
    void validatePropertyNotNull(Object property,
                                 String propertyName,
                                 String serverName,
                                 String methodName) throws OMAGInvalidParameterException
    {
        if (property == null)
        {
            OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.MISSING_CONFIGURATION_PROPERTY;

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, propertyName),
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }

    /**
     * Validate that the root URL of the server where an access service resides is not null.
     *
     * @param omagServerServiceRootURL  remote server name passed on the request
     * @param serverName  server name for this server
     * @param serverService name of the service being configured
     * @throws OMAGInvalidParameterException the root URL is null
     */
    void validateOMAGServerServiceRootURL(String  omagServerServiceRootURL,
                                          String  serverName,
                                          String  serverService) throws OMAGInvalidParameterException
    {
        if ((omagServerServiceRootURL == null) || ("".equals(omagServerServiceRootURL)))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_OMAG_SERVER_ROOT_URL;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverService, serverName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    serverService,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Validate that the server name of the server where an access service resides is not null.
     *
     * @param omagServerName  remote server name passed on the request
     * @param serverName  server name for this server
     * @param serverService name of the service being configured
     * @throws OMAGInvalidParameterException the name is null
     */
    public void validateOMAGServerName(String omagServerName, String serverName, String serverService)  throws OMAGInvalidParameterException
    {
        if ((omagServerName == null) || ("".equals(omagServerName)))
        {
            OMAGAdminErrorCode errorCode    = OMAGAdminErrorCode.NULL_OMAG_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverService, serverName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    serverService,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }
}
