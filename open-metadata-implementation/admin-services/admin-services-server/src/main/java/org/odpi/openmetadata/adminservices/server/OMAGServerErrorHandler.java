/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
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
        if ((userId == null) || (userId.length() == 0))
        {
            throw new OMAGNotAuthorizedException(OMAGAdminErrorCode.NULL_USER_NAME.getMessageDefinition(serverName),
                                                 this.getClass().getName(),
                                                 methodName);
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
        if ((serverName == null) || (serverName.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_LOCAL_SERVER_NAME.getMessageDefinition(),
                                                    this.getClass().getName(),
                                                    methodName);
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
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_CLIENT_CONFIG.getMessageDefinition(serverName, methodName),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        validateServerName(serverName, methodName);
        validateOMAGServerServiceRootURL(clientConfig.getOMAGServerPlatformRootURL(), serverName, methodName);
        validateOMAGServerName(clientConfig.getOMAGServerPlatformRootURL(), serverName, methodName);
    }


    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param integrationServiceConfig  integration service config
     * @param methodName  method receiving the call
     * @throws OMAGInvalidParameterException null server name
     */
    void validateIntegrationServiceConfig(String                        serverName,
                                          IntegrationServiceRequestBody integrationServiceConfig,
                                          String                        methodName) throws OMAGInvalidParameterException
    {
        final String connectorConfigPropertyName = "integrationServerConfig.connectorConfig";
        final String connectorNamePropertyName = "integrationServerConfig.connectorConfig.connectorName";

        this.validateOMAGServerClientConfig(serverName, integrationServiceConfig, methodName);

        for (IntegrationConnectorConfig connectorConfig : integrationServiceConfig.getIntegrationConnectorConfigs())
        {
            this.validatePropertyNotNull(connectorConfig, connectorConfigPropertyName, serverName, methodName);
            this.validatePropertyNotNull(connectorConfig.getConnectorName(), connectorNamePropertyName, serverName, methodName);
            this.validateServerConnection(connectorConfig.getConnection(), serverName, methodName);
        }
    }


    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param integrationGroupConfig  integration service config
     * @param methodName  method receiving the call
     * @throws OMAGInvalidParameterException null server name
     */
    void validateIntegrationGroupConfig(String                 serverName,
                                        IntegrationGroupConfig integrationGroupConfig,
                                        String                 methodName) throws OMAGInvalidParameterException
    {
        final String qualifiedNamePropertyName = "integrationGroupConfig.integrationGroupQualifiedName";

        this.validateOMAGServerClientConfig(serverName, integrationGroupConfig, methodName);
        this.validatePropertyNotNull(integrationGroupConfig.getIntegrationGroupQualifiedName(), qualifiedNamePropertyName, serverName, methodName);
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
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NO_EVENT_BUS_SET.getMessageDefinition(serverName),
                                                      this.getClass().getName(),
                                                      methodName);
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
    void validateAccessServiceIsRegistered(AccessServiceRegistrationEntry registration,
                                           String                    serviceURLMarker,
                                           String                    serverName,
                                           String                    methodName) throws OMAGConfigurationErrorException
    {
        if (registration == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.ACCESS_SERVICE_NOT_RECOGNIZED.getMessageDefinition(serverName, serviceURLMarker),
                                                      this.getClass().getName(),
                                                      methodName);
        }
        else if (registration.getAccessServiceOperationalStatus() != ServiceOperationalStatus.ENABLED)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.ACCESS_SERVICE_NOT_ENABLED.getMessageDefinition(serverName, serviceURLMarker),
                                                      this.getClass().getName(),
                                                      methodName);
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
    void validateViewServiceIsRegistered(ViewServiceRegistrationEntry registration,
                                         String                  serviceURLMarker,
                                         String                  serverName,
                                         String                  methodName) throws OMAGConfigurationErrorException
    {
        if (registration == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.VIEW_SERVICE_NOT_RECOGNIZED.getMessageDefinition(serverName, serviceURLMarker),
                                                      this.getClass().getName(),
                                                      methodName);
        }
        else if (registration.getViewServiceOperationalStatus() != ServiceOperationalStatus.ENABLED)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.VIEW_SERVICE_NOT_ENABLED.getMessageDefinition(serverName, serviceURLMarker),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Check that a requested engine service is registered with this server before allowing the engine service to be configured
     * into the configuration document
     *
     * @param registration registration record for the engine service.
     * @param serviceURLMarker string used in URL for this engine service
     * @param serverName name of the server being configured
     * @param methodName calling method
     * @throws OMAGConfigurationErrorException resulting exception if the engine service is not supported.
     */
    void validateEngineServiceIsRegistered(EngineServiceRegistrationEntry registration,
                                           String                    serviceURLMarker,
                                           String                    serverName,
                                           String                    methodName) throws OMAGConfigurationErrorException
    {
        if (registration == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.ENGINE_SERVICE_NOT_RECOGNIZED.getMessageDefinition(serverName, serviceURLMarker),
                                                      this.getClass().getName(),
                                                      methodName);
        }
        else if (registration.getEngineServiceOperationalStatus() != ServiceOperationalStatus.ENABLED)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.ENGINE_SERVICE_NOT_ENABLED.getMessageDefinition(serverName, serviceURLMarker),
                                                      this.getClass().getName(),
                                                      methodName);
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
        if ((accessServiceRootURL == null) || (accessServiceRootURL.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_ACCESS_SERVICE_ROOT_URL.getMessageDefinition(serverService, serverName, accessServiceName),
                                                    this.getClass().getName(),
                                                    serverService);
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
        if ((accessServiceServerName == null) || (accessServiceServerName.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_ACCESS_SERVICE_SERVER_NAME.getMessageDefinition(serverService, serverName, accessServiceName),
                                                    this.getClass().getName(),
                                                    serverService);
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
        if ((cohortName == null) || (cohortName.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_COHORT_NAME.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName);
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
        throw new OMAGInvalidParameterException(OMAGAdminErrorCode.COHORT_NOT_KNOWN.getMessageDefinition(serverName, cohortName),
                                                this.getClass().getName(),
                                                methodName);
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
        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.COHORT_TOPIC_STRANGE.getMessageDefinition(serverName, cohortName),
                                                  this.getClass().getName(),
                                                  methodName);
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
        if ((fileName == null) || (fileName.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_FILE_NAME.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName);
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
        if ((name == null) || (name.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_METADATA_COLLECTION_NAME.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName);
        }
    }


    /**
     * Validate that the connection is not null.  This is used for server specific requests
     *
     * @param connection  connection passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the connection is null
     */
    void validateServerConnection(Connection connection,
                                  String     serverName,
                                  String     methodName) throws OMAGInvalidParameterException
    {
        if (connection == null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_CONNECTION.getMessageDefinition(serverName, methodName),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        tryConnection(connection, methodName);
    }


    /**
     * Validate that the connection is not null.  This is used for OMAG Platform Requests
     *
     * @param connection  connection passed on the request
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the connection is null
     */
    void validatePlatformConnection(Connection connection,
                                    String     methodName) throws OMAGInvalidParameterException
    {
        if (connection == null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_PLATFORM_CONNECTION.getMessageDefinition(methodName),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        tryConnection(connection, methodName);
    }


    /**
     * Check that the connection is valid
     *
     * @param connection  connection passed on the request
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the connection is null
     */
    private void tryConnection(Connection connection,
                               String     methodName) throws OMAGInvalidParameterException
    {

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            connectorBroker.getConnector(connection);
        }
        catch (ConnectionCheckedException | ConnectorCheckedException  error)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.BAD_CONNECTION.getMessageDefinition(methodName,
                                                                                                           connection.toString(),
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage(),
                                                                                                           error.getReportedSystemAction(),
                                                                                                           error.getReportedUserAction()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
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
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.MISSING_CONFIGURATION_PROPERTY.getMessageDefinition(serverName, propertyName),
                                                    this.getClass().getName(),
                                                    methodName);
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
        if ((omagServerServiceRootURL == null) || (omagServerServiceRootURL.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_OMAG_SERVER_ROOT_URL.getMessageDefinition(serverService, serverName),
                                                    this.getClass().getName(),
                                                    serverService);
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
        if ((omagServerName == null) || (omagServerName.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_OMAG_SERVER_NAME.getMessageDefinition(serverService, serverName),
                                                    this.getClass().getName(),
                                                    serverService);
        }
    }


    /**
     * Validate that the server name of the server where an access service resides is not null.
     *
     * @param serviceURLMarker  remote server name passed on the request
     * @param serverName  server name for this server
     * @param serverService name of the service being configured
     * @throws OMAGInvalidParameterException the name is null
     */
    public void validateServiceURLMarker(String serviceURLMarker, String serverName, String serverService) throws OMAGInvalidParameterException
    {
        if ((serviceURLMarker == null) || (serviceURLMarker.length() == 0))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_SERVICE_URL_MARKER.getMessageDefinition(serverService, serverName),
                                                    this.getClass().getName(),
                                                    serverService);
        }
    }
}
