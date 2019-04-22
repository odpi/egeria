/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServersDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.discoveryserver.server.DiscoveryServerOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices;
import org.odpi.openmetadata.governanceservers.stewardshipservices.admin.StewardshipOperationalServices;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.securitysyncservices.registration.SecuritySyncOperationalServices;
import org.odpi.openmetadata.governanceservers.virtualizationservices.admin.VirtualizationOperationalServices;

import java.util.ArrayList;
import java.util.Date;
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

            response = activateWithSuppliedConfig(userId, serverName, configStore.getServerConfig(serverName, methodName));
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
        final String methodName                = "activateWithSuppliedConfig";
        final String REPOSITORY_SERVICES       = "Repository Services";


        List<String> activatedServiceList = new ArrayList<>();

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
             * Shut down any running instances for this server
             */
            deactivateRunningServiceInstances(instance, false);

            /*
             * Ready to start services
             *
             * First verify that there are services configured.
             */
            RepositoryServicesConfig  repositoryServicesConfig  = configuration.getRepositoryServicesConfig();
            List<AccessServiceConfig> accessServiceConfigList   = configuration.getAccessServicesConfig();
            ConformanceSuiteConfig    conformanceSuiteConfig    = configuration.getConformanceSuiteConfig();
            DiscoveryServerConfig     discoveryServerConfig     = configuration.getDiscoveryServerConfig();
            OpenLineageConfig         openLineageConfig         = configuration.getOpenLineageConfig();
            SecuritySyncConfig        securitySyncConfig        = configuration.getSecuritySyncConfig();
            StewardshipServicesConfig stewardshipServicesConfig = configuration.getStewardshipServicesConfig();
            VirtualizationConfig      virtualizationConfig      = configuration.getVirtualizationConfig();

            if ((repositoryServicesConfig == null) &&
                (accessServiceConfigList == null) &&
                (conformanceSuiteConfig == null) &&
                (discoveryServerConfig == null) &&
                (openLineageConfig == null) &&
                (securitySyncConfig == null) &&
                (stewardshipServicesConfig == null) &&
                (virtualizationConfig == null))
            {
                OMAGErrorCode errorCode    = OMAGErrorCode.EMPTY_CONFIGURATION;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
            }

            /*
             * Initialize the open metadata repository services first since other services depend on it.
             * (Even the governance daemons need the audit log.)
             */
            if (repositoryServicesConfig == null)
            {
                /*
                 * To get here, then another service is configured but not the repository services.
                 */
                OMAGErrorCode errorCode    = OMAGErrorCode.NULL_REPOSITORY_CONFIG;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
            }

            OMRSOperationalServices         operationalRepositoryServices;

            operationalRepositoryServices = new OMRSOperationalServices(configuration.getLocalServerName(),
                                                                        configuration.getLocalServerType(),
                                                                        configuration.getOrganizationName(),
                                                                        configuration.getLocalServerUserId(),
                                                                        configuration.getLocalServerPassword(),
                                                                        configuration.getLocalServerURL(),
                                                                        configuration.getMaxPageSize());
            activatedServiceList.add(REPOSITORY_SERVICES);

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
            OMRSTopicConnector        enterpriseTopicConnector = operationalRepositoryServices.getEnterpriseOMRSTopicConnector();


            List<AccessServiceAdmin>        operationalAccessServiceAdminList = instance.getOperationalAccessServiceAdminList();
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
                                activatedServiceList.add(accessServiceConfig.getAccessServiceName() + " OMAS");
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
             * Initialize the Open Metadata Conformance Suite Services.  This runs the Open Metadata TestLabs that are
             * part of the ODPi Egeria Conformance Program.
             */
            if (conformanceSuiteConfig != null)
            {
                ConformanceSuiteOperationalServices
                        operationalConformanceSuiteServices = new ConformanceSuiteOperationalServices(configuration.getLocalServerName(),
                                                                                                      configuration.getLocalServerUserId(),
                                                                                                      configuration.getLocalServerPassword(),
                                                                                                      configuration.getMaxPageSize());
                instance.setOperationalConformanceSuiteServices(operationalConformanceSuiteServices);
                operationalConformanceSuiteServices.initialize(conformanceSuiteConfig,
                                                               enterpriseTopicConnector,
                                                               operationalRepositoryServices.getEnterpriseConnectorManager(),
                                                               operationalRepositoryServices.getAuditLog(
                                                                       GovernanceServersDescription.CONFORMANCE_SUITE_SERVICES.getServiceCode(),
                                                                       GovernanceServersDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                                       GovernanceServersDescription.CONFORMANCE_SUITE_SERVICES.getServiceDescription(),
                                                                       GovernanceServersDescription.CONFORMANCE_SUITE_SERVICES.getServiceWiki()));

                activatedServiceList.add(GovernanceServersDescription.CONFORMANCE_SUITE_SERVICES.getServiceName());
            }

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

            /*
             * Now start the Governance Daemons.
             */

            /*
             * Initialize the Discovery Engine Services.  This is a governance daemon for running automated metadata discovery.
             */
            if (discoveryServerConfig != null)
            {
                DiscoveryServerOperationalServices
                        operationalDiscoveryServer = new DiscoveryServerOperationalServices(configuration.getLocalServerName(),
                                                                                            configuration.getLocalServerUserId(),
                                                                                            configuration.getLocalServerPassword(),
                                                                                            configuration.getMaxPageSize());
                instance.setOperationalDiscoveryServer(operationalDiscoveryServer);
                operationalDiscoveryServer.initialize(discoveryServerConfig,
                                                      operationalRepositoryServices.getAuditLog(
                                                              GovernanceServersDescription.DISCOVERY_ENGINE_SERVICES.getServiceCode(),
                                                              GovernanceServersDescription.DISCOVERY_ENGINE_SERVICES.getServiceName(),
                                                              GovernanceServersDescription.DISCOVERY_ENGINE_SERVICES.getServiceDescription(),
                                                              GovernanceServersDescription.DISCOVERY_ENGINE_SERVICES.getServiceWiki()));

                activatedServiceList.add(GovernanceServersDescription.DISCOVERY_ENGINE_SERVICES.getServiceName());
            }

            /*
             * Initialize the Open Lineage Services.  This is a governance daemon for the storage and querying of asset lineage.
             */
            if (openLineageConfig != null)
            {
                OpenLineageOperationalServices openLineageOperationalServices = new OpenLineageOperationalServices(configuration.getLocalServerName(),
                        configuration.getLocalServerType(),
                        configuration.getOrganizationName(),
                        configuration.getLocalServerUserId(),
                        configuration.getLocalServerURL(),
                        configuration.getMaxPageSize());
                instance.setOpenLineageOperationalServices(openLineageOperationalServices);
                openLineageOperationalServices.initialize(openLineageConfig,
                        operationalRepositoryServices.getAuditLog(GovernanceServersDescription.OPEN_LINEAGE_SERVICES.getServiceCode(),
                                GovernanceServersDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                GovernanceServersDescription.OPEN_LINEAGE_SERVICES.getServiceDescription(),
                                GovernanceServersDescription.OPEN_LINEAGE_SERVICES.getServiceWiki()));

                activatedServiceList.add(GovernanceServersDescription.OPEN_LINEAGE_SERVICES.getServiceName());
            }

            /*
             * Initialize the Security Sync Services.  This is a governance daemon for maintaining the configuration
             * in security oriented governance engines.
             */
            if (securitySyncConfig != null)
            {
                SecuritySyncOperationalServices operationalSecuritySync = new SecuritySyncOperationalServices(configuration.getLocalServerName(),
                                                                                                              configuration.getLocalServerType(),
                                                                                                              configuration.getOrganizationName(),
                                                                                                              configuration.getLocalServerUserId(),
                                                                                                              configuration.getLocalServerURL(),
                                                                                                              configuration.getMaxPageSize());
                instance.setOperationalSecuritySyncServices(operationalSecuritySync);
                operationalSecuritySync.initialize(securitySyncConfig,
                                                   operationalRepositoryServices.getAuditLog(GovernanceServersDescription.SECURITY_SYNC_SERVICES.getServiceCode(),
                                                                                             GovernanceServersDescription.SECURITY_SYNC_SERVICES.getServiceName(),
                                                                                             GovernanceServersDescription.SECURITY_SYNC_SERVICES.getServiceDescription(),
                                                                                             GovernanceServersDescription.SECURITY_SYNC_SERVICES.getServiceWiki()));

                activatedServiceList.add(GovernanceServersDescription.SECURITY_SYNC_SERVICES.getServiceName());
            }




            /*
             * Initialize the Virtualization Services.
             */
            if (virtualizationConfig != null) {
                VirtualizationOperationalServices operationalVirtualizationServices = new VirtualizationOperationalServices(configuration.getLocalServerName(),
                                                                                                                            configuration.getLocalServerType(),
                                                                                                                            configuration.getOrganizationName(),
                                                                                                                            configuration.getLocalServerUserId(),
                                                                                                                            configuration.getLocalServerURL());

                instance.setOperationalVirtualizationServices(operationalVirtualizationServices);
                operationalVirtualizationServices.initialize(virtualizationConfig,
                                                             operationalRepositoryServices.getAuditLog(
                                                                     GovernanceServersDescription.VIRTUALIZATION_SERVICES.getServiceCode(),
                                                                     GovernanceServersDescription.VIRTUALIZATION_SERVICES.getServiceName(),
                                                                     GovernanceServersDescription.VIRTUALIZATION_SERVICES.getServiceDescription(),
                                                                     GovernanceServersDescription.VIRTUALIZATION_SERVICES.getServiceWiki()));

                activatedServiceList.add(GovernanceServersDescription.VIRTUALIZATION_SERVICES.getServiceName());
            }


            /*
             * Initialize the Stewardship Services.  This is a governance daemon for running automated stewardship actions.
             */
            if (stewardshipServicesConfig != null)
            {
                StewardshipOperationalServices
                        operationalStewardshipServices = new StewardshipOperationalServices(configuration.getLocalServerName(),
                                                                                            configuration.getLocalServerUserId(),
                                                                                            configuration.getMaxPageSize());
                instance.setOperationalStewardshipServices(operationalStewardshipServices);
                operationalStewardshipServices.initialize(stewardshipServicesConfig,
                                                          operationalRepositoryServices.getAuditLog(
                                                              GovernanceServersDescription.STEWARDSHIP_SERVICES.getServiceCode(),
                                                              GovernanceServersDescription.STEWARDSHIP_SERVICES.getServiceName(),
                                                              GovernanceServersDescription.STEWARDSHIP_SERVICES.getServiceDescription(),
                                                              GovernanceServersDescription.STEWARDSHIP_SERVICES.getServiceWiki()));

                activatedServiceList.add(GovernanceServersDescription.STEWARDSHIP_SERVICES.getServiceName());
            }

            response.setSuccessMessage(new Date().toString() + " " + serverName + " is running the following services: " + activatedServiceList.toString());
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
     * @param instance a list of the running services
     * @param permanentDeactivation should the service be permanently disconnected
     */
    private void deactivateRunningServiceInstances(OMAGOperationalServicesInstance instance,
                                                   boolean                         permanentDeactivation)
    {
        /*
         * Shutdown the access services
         */
        if (instance.getOperationalAccessServiceAdminList() != null)
        {
            for (AccessServiceAdmin  accessServiceAdmin : instance.getOperationalAccessServiceAdminList())
            {
                if (accessServiceAdmin != null)
                {
                    accessServiceAdmin.shutdown();
                }
            }
        }

        /*
         * Shutdown the discovery engine
         */
        if (instance.getOperationalDiscoveryServer() != null)
        {
            instance.getOperationalDiscoveryServer().terminate(permanentDeactivation);
        }

        /*
         * Shutdown the security sync
         */
        if (instance.getOperationalSecuritySyncServices() != null)
        {
            instance.getOperationalSecuritySyncServices().disconnect(permanentDeactivation);
        }


        /*
         * Shutdown the virtualizer
         */
        if (instance.getOperationalVirtualizationServices() != null){
            instance.getOperationalVirtualizationServices().disconnect(permanentDeactivation);
        }


        /*
         * Shutdown the stewardship services
         */
        if (instance.getOperationalStewardshipServices() != null)
        {
            instance.getOperationalStewardshipServices().terminate(permanentDeactivation);
        }

        /*
         * Terminate the OMRS
         */
        if (instance.getOperationalRepositoryServices() != null)
        {
            instance.getOperationalRepositoryServices().disconnect(permanentDeactivation);
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

            deactivateRunningServiceInstances(instance, false);

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

            deactivateRunningServiceInstances(instance, true);

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
     * =============================================================
     * Services on running instances
     */

    /*
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


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    public VoidResponse addOpenMetadataArchiveFile(String userId,
                                                   String serverName,
                                                   String fileName)
    {
        final String methodName = "addOpenMetadataArchiveFile";

        VoidResponse response = new VoidResponse();

        try
        {
            OMAGOperationalServicesInstance instance = validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateFileName(fileName, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory   = new ConnectorConfigurationFactory();
            Connection newOpenMetadataArchive = configurationFactory.getOpenMetadataArchiveFileConnection(fileName);

            OMRSOperationalServices  repositoryServicesInstance = instance.getOperationalRepositoryServices();

            repositoryServicesInstance.addOpenMetadataArchive(newOpenMetadataArchive);
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
