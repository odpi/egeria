/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.auditlog.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.classifier.ServerTypeClassifier;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.admin.OCFMetadataOperationalServices;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.dataplatformservices.admin.DataPlatformOperationalServices;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.server.DiscoveryServerOperationalServices;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.dataengineproxy.admin.DataEngineProxyOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageServerOperationalServices;
import org.odpi.openmetadata.governanceservers.stewardshipservices.admin.StewardshipOperationalServices;
import org.odpi.openmetadata.governanceservers.virtualizationservices.admin.VirtualizationOperationalServices;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.securityofficerservices.registration.SecurityOfficerOperationalServices;
import org.odpi.openmetadata.securitysyncservices.registration.SecuritySyncOperationalServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerOperationalServices will provide support to start, manage and stop services in the OMAG Server.
 */
public class OMAGServerOperationalServices
{
    private OMAGServerOperationalInstanceHandler instanceHandler = new OMAGServerOperationalInstanceHandler(CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    private OMAGServerPlatformInstanceMap  platformInstanceMap = new OMAGServerPlatformInstanceMap();

    private OMAGServerAdminStoreServices   configStore  = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the list of open metadata and governance servers using the stored configuration information.
     * The code works through the list, starting each server in turn.  It stops if one of the servers fails to
     * start and returns the error.  Otherwise it continues through the list, returning
     *
     * @param userId  user that is issuing the request
     * @param serverNames  list of server names
     * @return success message or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateServerListWithStoredConfig(String       userId,
                                                                     List<String> serverNames)
    {
        String                 startUpMessage = null;
        SuccessMessageResponse response = new SuccessMessageResponse();

        response.setRelatedHTTPCode(200);

        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    response = activateWithStoredConfig(userId, serverName.trim());

                    if (response.getRelatedHTTPCode() == 200)
                    {
                        String serverStartUpMessage = "OMAG Server '" + serverName + "' successful start , with message: " +
                                response.getSuccessMessage() + System.lineSeparator();
                        if (startUpMessage == null)
                        {
                            startUpMessage = serverStartUpMessage;
                        }
                        else
                        {
                            startUpMessage += serverStartUpMessage;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            startUpMessage = "No OMAG servers listed in startup configuration";
        }

        if (response.getRelatedHTTPCode() == 200)
        {
            response.setSuccessMessage(startUpMessage);
        }

        return response;
    }


    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return success message response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithStoredConfig(String userId,
                                                           String serverName)
    {
        final String methodName = "activateWithStoredConfig";

        SuccessMessageResponse response = new SuccessMessageResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            response = activateWithSuppliedConfig(userId, serverName, configStore.getServerConfig(userId, serverName, methodName));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
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
     * @return success message response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithSuppliedConfig(String           userId,
                                                             String           serverName,
                                                             OMAGServerConfig configuration)
    {
        final String methodName                = "activateWithSuppliedConfig";

        List<String> activatedServiceList = new ArrayList<>();

        SuccessMessageResponse response = new SuccessMessageResponse();

        try
        {
            /*
             * Check that a serverName and userId is supplied
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            /*
             * Validate the content of the configuration document.
             */
            ServerTypeClassifier serverTypeClassifier = new ServerTypeClassifier(serverName, configuration);
            ServerTypeClassification serverTypeClassification = serverTypeClassifier.getServerType();


            int maxPageSize = configuration.getMaxPageSize();

            /*
             * Save the configuration document to the config store.
             */
            configStore.saveServerConfig(serverName, methodName, configuration);

            /*
             * Validate that the server is not running already.  If it is running it should be shutdown.
             */
            if (instanceHandler.isServerActive(userId, serverName))
            {
                this.deactivateTemporarily(userId, serverName);
            }

            /*
             * The instance saves the operational services objects for this server instance so they can be retrieved
             * in response to subsequent REST calls for the server
             */
            OMAGOperationalServicesInstance instance = new OMAGOperationalServicesInstance(serverName,
                                                                                           CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName(),
                                                                                           maxPageSize);

            /*
             * Save the configuration that is going to be used to start the server.
             */
            instance.setOperationalConfiguration(configuration);

            /*
             * Ready to start services
             */

            /*
             * Initialize the open metadata repository services first since other services depend on it.
             * (Even the governance servers need the audit log.)
             */
            OMRSOperationalServices         operationalRepositoryServices;

            operationalRepositoryServices = new OMRSOperationalServices(configuration.getLocalServerName(),
                                                                        configuration.getLocalServerType(),
                                                                        configuration.getOrganizationName(),
                                                                        configuration.getLocalServerUserId(),
                                                                        configuration.getLocalServerPassword(),
                                                                        configuration.getLocalServerURL(),
                                                                        configuration.getMaxPageSize());
            activatedServiceList.add(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName());
            operationalRepositoryServices.initializeAuditLog(configuration.getRepositoryServicesConfig(),
                                                             serverTypeClassification.getServerTypeName());

            /*
             * Save the instance of the OMRS and then initialize it.  OMRS has 2 modes of initialization.
             * Firstly for a basic server such as a governance server, just the audit log is initialized.
             * For a metadata server and conformance test server, initialization will optionally set up the
             * connector to the local repository, initialize the enterprise repository services (used by
             * the access services) and connect to the server's cohorts.   It is controlled by the settings in the
             * repository services configuration document.  The OMRS instance is saved since it needs to be called for
             * shutdown.
             */
            instance.setOperationalRepositoryServices(operationalRepositoryServices);
            if ((ServerTypeClassification.METADATA_SERVER.equals(serverTypeClassification)) ||
                (ServerTypeClassification.REPOSITORY_PROXY.equals(serverTypeClassification)) ||
                (ServerTypeClassification.CONFORMANCE_SERVER.equals(serverTypeClassification)))
            {
                operationalRepositoryServices.initializeMetadataServer(configuration.getRepositoryServicesConfig());

                /*
                 * Set up the server instance - ensure it is active and the security has been set up correctly.
                 */
                OpenMetadataServerSecurityVerifier securityVerifier =
                        platformInstanceMap.startUpServerInstance(configuration.getLocalServerUserId(),
                                                                  serverName,
                                                                  operationalRepositoryServices.getAuditLog(CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceCode(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceName(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDescription(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceWiki()),
                                                                  configuration.getServerSecurityConnection());

                /*
                 * Pass the resulting security verify to the repository services.  It will be set up in the local
                 * repository (if there is a local repository in this server).
                 */
                operationalRepositoryServices.setSecurityVerifier(securityVerifier);

                /*
                 * Next initialize the Open Connector Framework (OCF) metadata services.  These services are only initialized
                 * if the enterprise repository services are enabled.
                 */
                OMRSRepositoryConnector enterpriseRepositoryConnector = operationalRepositoryServices.getEnterpriseOMRSRepositoryConnector(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName());

                if (enterpriseRepositoryConnector != null)
                {
                    OCFMetadataOperationalServices operationalOCFMetadataServices;

                    operationalOCFMetadataServices = new OCFMetadataOperationalServices(configuration.getLocalServerName(),
                                                                                        enterpriseRepositoryConnector,
                                                                                        operationalRepositoryServices.getAuditLog(
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceCode(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceDescription(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceWiki()),
                                                                                        configuration.getLocalServerUserId(),
                                                                                        maxPageSize);

                    instance.setOperationalOCFMetadataServices(operationalOCFMetadataServices);
                    activatedServiceList.add(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName());
                }

                /*
                 * Now initialize the configured open metadata access services.  Each access service has an Admin object
                 * that controls its start up and shutdown.  The configuration service just needs to create the
                 * appropriate admin object (specified in the configuration) and initialize it with its own configuration
                 * document.  The admin object then does the rest.  The admin objects are stored in the instance since
                 * they also need to be called for shutdown.
                 */
                OMRSTopicConnector        enterpriseTopicConnector = operationalRepositoryServices.getEnterpriseOMRSTopicConnector();

                initializeAccessServices(instance,
                                         configuration.getAccessServicesConfig(),
                                         operationalRepositoryServices,
                                         enterpriseTopicConnector,
                                         configuration.getLocalServerUserId(),
                                         serverName,
                                         activatedServiceList);

                /*
                 * Initialize the Open Metadata Conformance Suite Services.  This runs the Open Metadata TestLabs that are
                 * part of the ODPi Egeria Conformance Program.
                 */
                if (ServerTypeClassification.CONFORMANCE_SERVER.equals(serverTypeClassification))
                {
                    ConformanceSuiteOperationalServices
                            operationalConformanceSuiteServices = new ConformanceSuiteOperationalServices(configuration.getLocalServerName(),
                                                                                                          configuration.getLocalServerUserId(),
                                                                                                          configuration.getLocalServerPassword(),
                                                                                                          configuration.getMaxPageSize());
                    instance.setOperationalConformanceSuiteServices(operationalConformanceSuiteServices);
                    operationalConformanceSuiteServices.initialize(configuration.getConformanceSuiteConfig(),
                                                                   enterpriseTopicConnector,
                                                                   operationalRepositoryServices.getEnterpriseConnectorManager(),
                                                                   operationalRepositoryServices.getAuditLog(
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceCode(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceDescription(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName());
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
                        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.ENTERPRISE_TOPIC_START_FAILED;
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
            else /* governance servers */
            {
                /*
                 * Set up the server instance - ensure it is active and the security has been set up correctly.
                 */
                platformInstanceMap.startUpServerInstance(configuration.getLocalServerUserId(),
                                                          serverName,
                                                          operationalRepositoryServices.getAuditLog(
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceCode(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceName(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDescription(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceWiki()),
                                                          configuration.getServerSecurityConnection());

                /*
                 * Initialize the Data Platform Services.
                 */
                if (ServerTypeClassification.DATA_PLATFORM_SERVER.equals(serverTypeClassification))
                {
                    DataPlatformOperationalServices dataPlatformOperationalServices
                            = new DataPlatformOperationalServices(configuration.getLocalServerName(),
                                                                  configuration.getLocalServerUserId(),
                                                                  configuration.getLocalServerType(),
                                                                  configuration.getLocalServerURL());

                    instance.setOperationalDataPlatformServices(dataPlatformOperationalServices);
                    dataPlatformOperationalServices.initialize(configuration.getDataPlatformServicesConfig(),
                                                               operationalRepositoryServices.getAuditLog(
                                                                       GovernanceServicesDescription.DATA_PLATFORM_SERVICES.getServiceCode(),
                                                                       GovernanceServicesDescription.DATA_PLATFORM_SERVICES.getServiceName(),
                                                                       GovernanceServicesDescription.DATA_PLATFORM_SERVICES.getServiceDescription(),
                                                                       GovernanceServicesDescription.DATA_PLATFORM_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.DATA_PLATFORM_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Data Engine Proxy Services.
                 */
                else if (ServerTypeClassification.DATA_ENGINE_PROXY.equals(serverTypeClassification))
                {
                    DataEngineProxyOperationalServices operationalDataEngineProxyServices
                            = new DataEngineProxyOperationalServices(configuration.getLocalServerName(),
                                                                     configuration.getLocalServerUserId(),
                                                                     configuration.getLocalServerPassword());

                    instance.setOperationalDataEngineProxyServices(operationalDataEngineProxyServices);
                    operationalDataEngineProxyServices.initialize(configuration.getDataEngineProxyConfig(),
                                                                  operationalRepositoryServices.getAuditLog(
                                                                          GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceCode(),
                                                                          GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                                          GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceDescription(),
                                                                          GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Discovery Engine Services.  This is a governance server for running automated metadata discovery.
                 */
                else if (ServerTypeClassification.DISCOVERY_SERVER.equals(serverTypeClassification))
                {
                    DiscoveryServerOperationalServices operationalDiscoveryServer
                            = new DiscoveryServerOperationalServices(configuration.getLocalServerName(),
                                                                     configuration.getLocalServerUserId(),
                                                                     configuration.getLocalServerPassword(),
                                                                     configuration.getMaxPageSize());

                    instance.setOperationalDiscoveryServer(operationalDiscoveryServer);
                    operationalDiscoveryServer.initialize(configuration.getDiscoveryEngineServicesConfig(),
                                                          operationalRepositoryServices.getAuditLog(
                                                                  GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceCode(),
                                                                  GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceName(),
                                                                  GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceDescription(),
                                                                  GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Open Lineage Services.  This is a governance server for the storing and querying of asset lineage.
                 */
                else if (ServerTypeClassification.OPEN_LINEAGE_SERVER.equals(serverTypeClassification))
                {
                    OpenLineageServerOperationalServices
                            operationalOpenLineageServer = new OpenLineageServerOperationalServices(configuration.getLocalServerName(),
                                                                                                    configuration.getLocalServerUserId(),
                                                                                                    configuration.getLocalServerPassword(),
                                                                                                    configuration.getMaxPageSize());
                    instance.setOpenLineageOperationalServices(operationalOpenLineageServer);
                    operationalOpenLineageServer.initialize(configuration.getOpenLineageServerConfig(),
                                                            operationalRepositoryServices.getAuditLog(
                                                                    GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceCode(),
                                                                    GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                                    GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceDescription(),
                                                                    GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Security Officer Services.  This is a governance server for maintaining the configuration
                 * in security officer engines.
                 */
                else if (ServerTypeClassification.SECURITY_OFFICER_SERVER.equals(serverTypeClassification))
                {
                    SecurityOfficerOperationalServices operationalSecurityOfficer = new SecurityOfficerOperationalServices(configuration.getLocalServerName(),
                                                                                                                           configuration.getLocalServerType(),
                                                                                                                           configuration.getOrganizationName(),
                                                                                                                           configuration.getLocalServerUserId(),
                                                                                                                           configuration.getLocalServerURL(),
                                                                                                                           configuration.getMaxPageSize());
                    instance.setOperationalSecurityOfficerService(operationalSecurityOfficer);
                    operationalSecurityOfficer.initialize(configuration.getSecurityOfficerConfig(),
                                                          operationalRepositoryServices.getAuditLog(
                                                                  GovernanceServicesDescription.SECURITY_OFFICER_SERVICES.getServiceCode(),
                                                                  GovernanceServicesDescription.SECURITY_OFFICER_SERVICES.getServiceName(),
                                                                  GovernanceServicesDescription.SECURITY_OFFICER_SERVICES.getServiceDescription(),
                                                                  GovernanceServicesDescription.SECURITY_OFFICER_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.SECURITY_OFFICER_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Security Sync Services.  This is a governance server for maintaining the configuration
                 * in security oriented governance engines.
                 */
                else if (ServerTypeClassification.SECURITY_SYNC_SERVER.equals(serverTypeClassification))
                {
                    SecuritySyncOperationalServices operationalSecuritySync
                            = new SecuritySyncOperationalServices(configuration.getLocalServerName(),
                                                                  configuration.getLocalServerType(),
                                                                  configuration.getOrganizationName(),
                                                                  configuration.getLocalServerUserId(),
                                                                  configuration.getLocalServerURL(),
                                                                  configuration.getMaxPageSize());

                    instance.setOperationalSecuritySyncServices(operationalSecuritySync);
                    operationalSecuritySync.initialize(configuration.getSecuritySyncConfig(),
                                                       operationalRepositoryServices.getAuditLog(
                                                               GovernanceServicesDescription.SECURITY_SYNC_SERVICES.getServiceCode(),
                                                               GovernanceServicesDescription.SECURITY_SYNC_SERVICES.getServiceName(),
                                                               GovernanceServicesDescription.SECURITY_SYNC_SERVICES.getServiceDescription(),
                                                               GovernanceServicesDescription.SECURITY_SYNC_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.SECURITY_SYNC_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Stewardship Engine Services.  This is a governance daemon for running automated stewardship actions.
                 */
                else if (ServerTypeClassification.STEWARDSHIP_SERVER.equals(serverTypeClassification))
                {
                    StewardshipOperationalServices
                            operationalStewardshipServices = new StewardshipOperationalServices(configuration.getLocalServerName(),
                                                                                                configuration.getLocalServerUserId(),
                                                                                                configuration.getMaxPageSize());
                    instance.setOperationalStewardshipServices(operationalStewardshipServices);
                    operationalStewardshipServices.initialize(configuration.getStewardshipEngineServicesConfig(),
                                                              operationalRepositoryServices.getAuditLog(
                                                                      GovernanceServicesDescription.STEWARDSHIP_SERVICES.getServiceCode(),
                                                                      GovernanceServicesDescription.STEWARDSHIP_SERVICES.getServiceName(),
                                                                      GovernanceServicesDescription.STEWARDSHIP_SERVICES.getServiceDescription(),
                                                                      GovernanceServicesDescription.STEWARDSHIP_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.STEWARDSHIP_SERVICES.getServiceName());
                }

                /*
                 * Initialize the Virtualization Services.
                 */
                else if (ServerTypeClassification.VIRTUALIZER_SERVER.equals(serverTypeClassification))
                {
                    VirtualizationOperationalServices operationalVirtualizationServices
                            = new VirtualizationOperationalServices(configuration.getLocalServerName(),
                                                                    configuration.getLocalServerType(),
                                                                    configuration.getOrganizationName(),
                                                                    configuration.getLocalServerUserId(),
                                                                    configuration.getLocalServerURL());

                    instance.setOperationalVirtualizationServices(operationalVirtualizationServices);
                    operationalVirtualizationServices.initialize(configuration.getVirtualizationConfig(),
                                                                 operationalRepositoryServices.getAuditLog(
                                                                         GovernanceServicesDescription.VIRTUALIZATION_SERVICES.getServiceCode(),
                                                                         GovernanceServicesDescription.VIRTUALIZATION_SERVICES.getServiceName(),
                                                                         GovernanceServicesDescription.VIRTUALIZATION_SERVICES.getServiceDescription(),
                                                                         GovernanceServicesDescription.VIRTUALIZATION_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.VIRTUALIZATION_SERVICES.getServiceName());
                }
            }

            String successMessage = new Date().toString() + " " + serverName + " is running the following services: " + activatedServiceList.toString();
            response.setSuccessMessage(successMessage);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }


    /**
     * Start up the access services.
     *
     * @param instance server instance
     * @param accessServiceConfigList configured access services
     * @param operationalRepositoryServices repository services
     * @param enterpriseTopicConnector events from the cohort
     * @param localServerUserId servers userId
     * @param serverName server name
     * @param activatedServiceList list of services (subsystems) running in the server
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private void initializeAccessServices(OMAGOperationalServicesInstance instance,
                                          List<AccessServiceConfig>       accessServiceConfigList,
                                          OMRSOperationalServices         operationalRepositoryServices,
                                          OMRSTopicConnector              enterpriseTopicConnector,
                                          String                          localServerUserId,
                                          String                          serverName,
                                          List<String>                    activatedServiceList) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeAccessServices";

        List<AccessServiceAdmin>        operationalAccessServiceAdminList = instance.getOperationalAccessServiceAdminList();
        if (accessServiceConfigList != null)
        {
            for (AccessServiceConfig  accessServiceConfig : accessServiceConfigList)
            {
                /*
                 * Connected Asset OMAS has been removed but may be present in some configuration documents.
                 */
                if ((accessServiceConfig != null) && (accessServiceConfig.getAccessServiceId() != AccessServiceDescription.CONNECTED_ASSET_OMAS.getAccessServiceCode()))
                {
                    String    accessServiceAdminClassName = accessServiceConfig.getAccessServiceAdminClass();

                    if (accessServiceAdminClassName != null)
                    {
                        OMRSAuditLog auditLog = operationalRepositoryServices.getAuditLog(accessServiceConfig.getAccessServiceId(),
                                                                                          accessServiceConfig.getAccessServiceName(),
                                                                                          accessServiceConfig.getAccessServiceDescription(),
                                                                                          accessServiceConfig.getAccessServiceWiki());
                        try
                        {
                            AccessServiceAdmin accessServiceAdmin = (AccessServiceAdmin)Class.forName(accessServiceAdminClassName).newInstance();

                            accessServiceAdmin.initialize(accessServiceConfig,
                                                          enterpriseTopicConnector,
                                                          operationalRepositoryServices.getEnterpriseOMRSRepositoryConnector(accessServiceConfig.getAccessServiceName()),
                                                          auditLog,
                                                          localServerUserId);
                            operationalAccessServiceAdminList.add(accessServiceAdmin);
                            activatedServiceList.add(accessServiceConfig.getAccessServiceName() + " OMAS");
                        }
                        catch (OMAGConfigurationErrorException  error)
                        {
                            OMAGAdminAuditCode  auditCode = OMAGAdminAuditCode.SERVICE_INSTANCE_FAILURE;
                            auditLog.logException(methodName,
                                                  auditCode.getLogMessageId(),
                                                  auditCode.getSeverity(),
                                                  auditCode.getFormattedLogMessage(accessServiceConfig.getAccessServiceName(),
                                                                                   error.getMessage()),
                                                  accessServiceConfig.toString(),
                                                  auditCode.getSystemAction(),
                                                  auditCode.getUserAction(),
                                                  error);
                            throw error;
                        }
                        catch (Throwable  error)
                        {
                            OMAGAdminAuditCode  auditCode = OMAGAdminAuditCode.SERVICE_INSTANCE_FAILURE;
                            auditLog.logException(methodName,
                                                  auditCode.getLogMessageId(),
                                                  auditCode.getSeverity(),
                                                  auditCode.getFormattedLogMessage(error.getMessage(),
                                                                                   accessServiceConfig.getAccessServiceName(),
                                                                                   error.getMessage()),
                                                  accessServiceConfig.toString(),
                                                  auditCode.getSystemAction(),
                                                  auditCode.getUserAction(),
                                                  error);

                            OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.BAD_ACCESS_SERVICE_ADMIN_CLASS;
                            String        errorMessage = errorCode.getErrorMessageId()
                                                       + errorCode.getFormattedErrorMessage(serverName,
                                                                                            accessServiceAdminClassName,
                                                                                            accessServiceConfig.getAccessServiceName());

                            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                                      this.getClass().getName(),
                                                                      methodName,
                                                                      errorMessage,
                                                                      errorCode.getSystemAction(),
                                                                      errorCode.getUserAction(),
                                                                      error);
                        }
                    }
                    else
                    {
                        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.NULL_ACCESS_SERVICE_ADMIN_CLASS;
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
    }


    /**
     * Shutdown any running services for a specific server instance.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param methodName calling method
     * @param instance a list of the running services
     * @param permanentDeactivation should the server be permanently disconnected
     * @throws InvalidParameterException one of the services detected an invalid parameter
     * @throws PropertyServerException one of the services had problems shutting down
     */
    private void deactivateRunningServiceInstances(String                          userId,
                                                   String                          serverName,
                                                   String                          methodName,
                                                   OMAGOperationalServicesInstance instance,
                                                   boolean                         permanentDeactivation) throws InvalidParameterException,
                                                                                                                 PropertyServerException
    {
        /*
         * Shutdown the access services
         */
        if (instance.getOperationalAccessServiceAdminList() != null)
        {
            for (AccessServiceAdmin accessServiceAdmin : instance.getOperationalAccessServiceAdminList())
            {
                if (accessServiceAdmin != null)
                {
                    accessServiceAdmin.shutdown();
                }
            }
        }

        /*
         * Shutdown the OCF metadata management services
         */
        if (instance.getOperationalOCFMetadataServices() != null)
        {
            instance.getOperationalOCFMetadataServices().shutdown();
        }

        /*
         * Shutdown the discovery engine
         */
        if (instance.getOperationalDiscoveryServer() != null)
        {
            instance.getOperationalDiscoveryServer().terminate();
        }

        /*
         * Shutdown the open lineage services
         */
        if (instance.getOpenLineageOperationalServices() != null)
        {
             instance.getOpenLineageOperationalServices().disconnect(permanentDeactivation);
        }

        /*
         * Shutdown the security sync
         */
        if (instance.getOperationalSecuritySyncServices() != null)
        {
            instance.getOperationalSecuritySyncServices().disconnect();
        }

        /*
         * Shutdown the security officer
         */
        if (instance.getOperationalSecurityOfficerService() != null)
        {
            instance.getOperationalSecurityOfficerService().disconnect();
        }

        /*
         * Shutdown the virtualizer
         */
        if (instance.getOperationalVirtualizationServices() != null)
        {
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
         * Shutdown the data platform services
         */
        if (instance.getOperationalDataPlatformServices() != null)
        {
            instance.getOperationalDataPlatformServices().disconnect(permanentDeactivation);
        }

        /*
         * Shutdown the conformance test suite
         */
        if (instance.getOperationalConformanceSuiteServices() != null)
        {
            instance.getOperationalConformanceSuiteServices().terminate(permanentDeactivation);
        }

        /*
         * Terminate the OMRS
         */
        if (instance.getOperationalRepositoryServices() != null)
        {
            instance.getOperationalRepositoryServices().disconnect(permanentDeactivation);
        }

        instanceHandler.removeServerServiceInstance(serverName);

        platformInstanceMap.shutdownServerInstance(userId, serverName, methodName);
    }


    /**
     * Temporarily deactivate the open metadata and governance servers in th supplied list.
     *
     * @param userId  user that is issuing the request
     * @param serverNames list of server names
     */
    public void deactivateTemporarilyServerList(String        userId,
                                                List<String>  serverNames)
    {
        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    deactivateTemporarily(userId, serverName);
                }
            }
        }
    }


    /**
     * Temporarily deactivate any open metadata and governance services for the requested server.
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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(userId,
                                              serverName,
                                              methodName,
                                              instanceHandler.getServerServiceInstance(userId, serverName, methodName),
                                              false);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(userId,
                                              serverName,
                                              methodName,
                                              instanceHandler.getServerServiceInstance(userId, serverName, methodName),
                                              true);

            /*
             * Delete the configuration for this server
             */
            configStore.saveServerConfig(serverName, methodName, null);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
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
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);

            response.setOMAGServerConfig(instance.getOperationalConfiguration());
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateFileName(fileName, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory   = new ConnectorConfigurationFactory();
            Connection newOpenMetadataArchive = configurationFactory.getOpenMetadataArchiveFileConnection(fileName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);
            OMRSOperationalServices         repositoryServicesInstance = instance.getOperationalRepositoryServices();

            repositoryServicesInstance.addOpenMetadataArchive(newOpenMetadataArchive, fileName);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }
}
