/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;


import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.OMAGErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.properties.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.properties.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;

import java.util.List;

/**
 * OMAGServerOperationalServices will provide support to start, manage and stop services in the OMAG Server.
 */
public class OMAGServerOperationalServices
{
    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerInstanceMap          instanceMap = new OMAGServerInstanceMap();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();

    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public VoidResponse activateWithStoredConfig(String userId,
                                                 String serverName)
    {
        final String methodName = "activateWithStoredConfig";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            activateWithSuppliedConfig(userId, serverName, configStore.getServerConfig(serverName, methodName));
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId  user that is issuing the request
     * @param configuration  properties used to initialize the services
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public VoidResponse activateWithSuppliedConfig(String           userId,
                                                   String           serverName,
                                                   OMAGServerConfig configuration)
    {
        final String methodName = "activateWithSuppliedConfig";

        VoidResponse response = new VoidResponse();

        try
        {
            OMAGOperationalServicesInstance instance = validateServerName(serverName, methodName);

            errorHandler.validateUserId(userId, serverName, methodName);

            if (configuration == null)
            {
                OMAGErrorCode errorCode    = OMAGErrorCode.NULL_SERVER_CONFIG;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        errorMessage,
                                                        errorCode.getSystemAction(),
                                                        errorCode.getUserAction());
            }

            /*
             * Validate the server name from the configuration document matches the server name passed in the request
             * and is all is well, save the configuration document to the config store and the server instance.
             */
            validateConfigServerName(serverName, configuration.getLocalServerName(), methodName);
            configStore.saveServerConfig(serverName, methodName, configuration);
            instance.setOperationalConfiguration(configuration);

            /*
             * Extract details of any running instances for this server
             */
            OMRSOperationalServices  operationalRepositoryServices     = instance.getOperationalRepositoryServices();
            List<AccessServiceAdmin> operationalAccessServiceAdminList = instance.getOperationalAccessServiceAdminList();

            /*
             * Shut down any running instances for this server
             */
            deactivateRunningServiceInstances(operationalRepositoryServices,
                                              operationalAccessServiceAdminList,
                                              false);

            /*
             * Ready to start services
             * Initialize the open metadata repository services first since the access services depend on it/.
             */
            RepositoryServicesConfig repositoryServicesConfig = configuration.getRepositoryServicesConfig();

            if (repositoryServicesConfig == null)
            {
                OMAGErrorCode errorCode    = OMAGErrorCode.NULL_REPOSITORY_CONFIG;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
            }

            operationalRepositoryServices = new OMRSOperationalServices(configuration.getLocalServerName(),
                                                                        configuration.getLocalServerType(),
                                                                        configuration.getOrganizationName(),
                                                                        configuration.getLocalServerUserId(),
                                                                        configuration.getLocalServerURL(),
                                                                        configuration.getMaxPageSize());

            /*
             * Save the instance of the OMRS and then initialize it.  The initialization will optionally set up the
             * connector to the local repository, initialize the enterprise repository services (used by
             * the access services) and connect to the server's cohorts.   It is controlled by the settings in the
             * repository services configuration document.  The OMRS instance is saved since it needs to be called for
             * shutdown.
             */
            instance.setOperationalRepositoryServices(operationalRepositoryServices);
            operationalRepositoryServices.initialize(repositoryServicesConfig);

            /*
             * Now initialize the configured open metadata access services.  Each access service has an Admin object
             * that controls its start up and shutdown.  The configuration service just needs to create the
             * appropriate admin object (specified in the configuration) and initialize it with its own configuration
             * document.  The admin object then does the rest.  The admin objects are stored in the instance since
             * they also need to be called for shutdown.
             */
            List<AccessServiceConfig> accessServiceConfigList  = configuration.getAccessServicesConfig();
            OMRSTopicConnector        enterpriseTopicConnector = operationalRepositoryServices.getEnterpriseOMRSTopicConnector();

            if (accessServiceConfigList != null)
            {
                for (AccessServiceConfig  accessServiceConfig : accessServiceConfigList)
                {
                    if (accessServiceConfig != null)
                    {
                        String    accessServiceAdminClassName = accessServiceConfig.getAccessServiceAdminClass();

                        if (accessServiceAdminClassName != null)
                        {
                            try
                            {
                                AccessServiceAdmin
                                        accessServiceAdmin = (AccessServiceAdmin)Class.forName(accessServiceAdminClassName).newInstance();

                                accessServiceAdmin.initialize(accessServiceConfig,
                                                              enterpriseTopicConnector,
                                                              operationalRepositoryServices.getEnterpriseOMRSRepositoryConnector(accessServiceConfig.getAccessServiceName()),
                                                              operationalRepositoryServices.getAuditLog(accessServiceConfig.getAccessServiceId(),
                                                                                                        accessServiceConfig.getAccessServiceName(),
                                                                                                        accessServiceConfig.getAccessServiceDescription(),
                                                                                                        accessServiceConfig.getAccessServiceWiki()),
                                                              configuration.getLocalServerUserId());
                                operationalAccessServiceAdminList.add(accessServiceAdmin);
                            }
                            catch (Throwable  error)
                            {
                                OMAGErrorCode errorCode    = OMAGErrorCode.BAD_ACCESS_SERVICE_ADMIN_CLASS;
                                String        errorMessage = errorCode.getErrorMessageId()
                                                           + errorCode.getFormattedErrorMessage(serverName,
                                                                                                accessServiceAdminClassName,
                                                                                                accessServiceConfig.getAccessServiceName());

                                throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                                          this.getClass().getName(),
                                                                          methodName,
                                                                          errorMessage,
                                                                          errorCode.getSystemAction(),
                                                                          errorCode.getUserAction());
                            }
                        }
                        else
                        {
                            OMAGErrorCode errorCode    = OMAGErrorCode.NULL_ACCESS_SERVICE_ADMIN_CLASS;
                            String        errorMessage = errorCode.getErrorMessageId()
                                                       + errorCode.getFormattedErrorMessage(serverName,
                                                                                            accessServiceConfig.getAccessServiceName());

                            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                                      this.getClass().getName(),
                                                                      methodName,
                                                                      errorMessage,
                                                                      errorCode.getSystemAction(),
                                                                      errorCode.getUserAction());
                        }
                    }
                }
            }


            /*
             * Save the list of running access services to the instance and then add the instance to the instance map.
             * The instance information can then be retrieved for shutdown or other management requests.
             */
            instance.setOperationalAccessServiceAdminList(operationalAccessServiceAdminList);
            instanceMap.setNewInstance(serverName, instance);

            /*
             * The enterprise topic passes OMRS Events from the cohort to the listening access services.
             * During the access services start up, they registered listeners with the enterprise topic.
             * Starting the enterprise topic will start the flow of events to the registered access services.
             */
            if (enterpriseTopicConnector != null)
            {
                try
                {
                    enterpriseTopicConnector.start();
                }
                catch (Throwable  error)
                {
                    OMAGErrorCode errorCode    = OMAGErrorCode.ENTERPRISE_TOPIC_START_FAILED;
                    String        errorMessage = errorCode.getErrorMessageId()
                                               + errorCode.getFormattedErrorMessage(serverName, error.getMessage());

                    throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                              this.getClass().getName(),
                                                              methodName,
                                                              errorMessage,
                                                              errorCode.getSystemAction(),
                                                              errorCode.getUserAction());
                }
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Shutdown any running services.
     *
     * @param operationalRepositoryServices a running Open Metadata Repository Services (OMRS) instance or null
     * @param operationalAccessServiceAdminList a list of running Open Metadata Access Services (OMAS) admin instances or null or empty list
     * @param permanentDeactivation should the OMRS disconnect permanently from its cohorts?
     */
    private void deactivateRunningServiceInstances(OMRSOperationalServices  operationalRepositoryServices,
                                                   List<AccessServiceAdmin> operationalAccessServiceAdminList,
                                                   boolean                  permanentDeactivation)
    {
        /*
         * Shutdown the access services
         */
        if ((operationalAccessServiceAdminList != null))
        {
            for (AccessServiceAdmin  accessServiceAdmin : operationalAccessServiceAdminList)
            {
                if (accessServiceAdmin != null)
                {
                    accessServiceAdmin.shutdown();
                }
            }
        }

        /*
         * Terminate the OMRS
         */
        if (operationalRepositoryServices != null)
        {
            operationalRepositoryServices.disconnect(permanentDeactivation);
        }
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse deactivateTemporarily(String  userId,
                                              String  serverName)
    {
        final String methodName = "deactivateTemporarily";

        VoidResponse response = new VoidResponse();

        try
        {
            OMAGOperationalServicesInstance instance = validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(instance.getOperationalRepositoryServices(),
                                              instance.getOperationalAccessServiceAdminList(),
                                              false);

            instanceMap.removeInstance(serverName);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts
     * and delete the server's configuration.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse deactivatePermanently(String  userId,
                                              String  serverName)
    {
        final String methodName = "deactivatePermanently";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGOperationalServicesInstance instance = validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(instance.getOperationalRepositoryServices(),
                                              instance.getOperationalAccessServiceAdminList(),
                                              true);

            instanceMap.removeInstance(serverName);

            /*
             * Delete the configuration for this server
             */
            configStore.saveServerConfig(serverName, methodName, null);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /*
     * Services on running instances
     */

    /*
     * =============================================================
     * Query current configuration
     */


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public OMAGServerConfigResponse getActiveConfiguration(String userId,
                                                           String serverName)
    {
        final String methodName = "getActiveConfiguration";

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            OMAGOperationalServicesInstance instance = validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            response.setOMAGServerConfig(instance.getOperationalConfiguration());
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /*
     * =============================================================
     * Private methods
     */


    /**
     * Validate that the server name is not null and retrieve an operational services instance for the server.
     * If no operational services instance is stored in the instance map it means this server is not running and
     * a new operational services instance is created.  The resulting operational services instance is returned.
     *
     * @param serverName  serverName passed on a request
     * @param methodName  method being called
     * @return OMAGOperationalServicesInstance object
     * @throws OMAGInvalidParameterException null server name
     */
    private OMAGOperationalServicesInstance validateServerName(String serverName,
                                                               String methodName) throws OMAGInvalidParameterException
    {
        /*
         * If the local server name is still null then save the server name in the configuration.
         */
        if (serverName == null)
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.NULL_LOCAL_SERVER_NAME;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
        else
        {
            OMAGOperationalServicesInstance instance = instanceMap.getInstance(serverName);

            if (instance == null)
            {
                instance = new OMAGOperationalServicesInstance();
            }

            return instance;
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
    private void validateConfigServerName(String serverName,
                                          String configServerName,
                                          String methodName) throws OMAGConfigurationErrorException
    {
        if (! serverName.equals(configServerName))
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.INCOMPATIBLE_SERVER_NAMES;
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

}
