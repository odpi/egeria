/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.egeria;


import org.odpi.openmetadata.accessservices.itinfrastructure.api.ITInfrastructureEventListener;
import org.odpi.openmetadata.accessservices.itinfrastructure.events.ITInfrastructureOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ProcessStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DeploymentElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareServerElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareServerPlatformElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.ServerAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineHostServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adapters.connectors.integration.egeria.ffdc.EgeriaInfrastructureConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.egeria.ffdc.EgeriaInfrastructureConnectorErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.integrationservices.infrastructure.connector.InfrastructureIntegratorConnector;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * EgeriaCataloguerIntegrationConnector catalogues a deployment of Egeria.
 */
public class EgeriaCataloguerIntegrationConnector extends InfrastructureIntegratorConnector implements ITInfrastructureEventListener
{
    private static final String ENCRYPTED_FILE_BASED_SERVER_CONFIG_STORE_PROVIDER          = "org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile.EncryptedFileBasedServerConfigStoreProvider";
    private static final String IN_MEMORY_OPEN_METADATA_TOPIC_PROVIDER                     = "org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider";
    private static final String KAFKA_OPEN_METADATA_TOPIC_PROVIDER                         = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider";
    private static final String FILE_BASED_OPEN_METADATA_ARCHIVE_STORE_PROVIDER            = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider";
    private static final String CONSOLE_AUDIT_LOG_STORE_PROVIDER                           = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider";
    private static final String EVENT_TOPIC_AUDIT_LOG_STORE_PROVIDER                       = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic.EventTopicAuditLogStoreProvider";
    private static final String FILE_BASED_AUDIT_LOG_STORE_PROVIDER                        = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider";
    private static final String SLF_4_J_AUDIT_LOG_STORE_PROVIDER                           = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.slf4j.SLF4JAuditLogStoreProvider";
    private static final String FILE_BASED_REGISTRY_STORE_PROVIDER                         = "org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider";
    private static final String GRAPH_OMRS_REPOSITORY_CONNECTOR_PROVIDER                   = "org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSRepositoryConnectorProvider";
    private static final String IN_MEMORY_OMRS_REPOSITORY_CONNECTOR_PROVIDER               = "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider";
    private static final String READ_ONLY_OMRS_REPOSITORY_CONNECTOR_PROVIDER               = "org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector.ReadOnlyOMRSRepositoryConnectorProvider";
    private static final String OMRS_REST_REPOSITORY_CONNECTOR_PROVIDER                    = "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider";
    private static final String OMRS_TOPIC_PROVIDER                                        = "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider";

    private final Map<String, String> serverMap = new HashMap<>();
    private final Map<String, String> connectorProviderToAsset = new HashMap<>();


    List<PlatformDetails> monitoredPlatforms = new ArrayList<>();
    String                clientUserId = "garygeeke";


    /**
     * PlatformDetails acts as a cache of knowledge about a particular platform.
     */
    static class PlatformDetails
    {
        String platformRootURL = null;
        String platformGUID = null;
        String platformTypeName = null;
        String platformQualifiedName = null;
        String platformDisplayName = null;
        String capabilityGUID = null;

        @Override
        public String toString()
        {
            return "PlatformDetails{" +
                           "platformRootURL='" + platformRootURL + '\'' +
                           ", platformGUID='" + platformGUID + '\'' +
                           ", platformTypeName='" + platformTypeName + '\'' +
                           ", platformQualifiedName='" + platformQualifiedName + '\'' +
                           ", platformDisplayName='" + platformDisplayName + '\'' +
                           ", capabilityGUID='" + capabilityGUID + '\'' +
                           '}';
        }
    }



    /**
     * Default constructor
     */
    public EgeriaCataloguerIntegrationConnector()
    {
        super();

        connectorProviderToAsset.put(ENCRYPTED_FILE_BASED_SERVER_CONFIG_STORE_PROVIDER, "JSONFile");
        connectorProviderToAsset.put(IN_MEMORY_OPEN_METADATA_TOPIC_PROVIDER, "Topic");
        connectorProviderToAsset.put(KAFKA_OPEN_METADATA_TOPIC_PROVIDER, "KafkaTopic");
        connectorProviderToAsset.put(FILE_BASED_OPEN_METADATA_ARCHIVE_STORE_PROVIDER, "JSONFile");
        connectorProviderToAsset.put(CONSOLE_AUDIT_LOG_STORE_PROVIDER, null);
        connectorProviderToAsset.put(EVENT_TOPIC_AUDIT_LOG_STORE_PROVIDER, "KafkaTopic");
        connectorProviderToAsset.put(FILE_BASED_AUDIT_LOG_STORE_PROVIDER, "DataFolder");
        connectorProviderToAsset.put(SLF_4_J_AUDIT_LOG_STORE_PROVIDER, null);
        connectorProviderToAsset.put(FILE_BASED_REGISTRY_STORE_PROVIDER, "JSONFile");
        connectorProviderToAsset.put(GRAPH_OMRS_REPOSITORY_CONNECTOR_PROVIDER, "MetadataRepository");
        connectorProviderToAsset.put(IN_MEMORY_OMRS_REPOSITORY_CONNECTOR_PROVIDER, "MetadataRepository");
        connectorProviderToAsset.put(READ_ONLY_OMRS_REPOSITORY_CONNECTOR_PROVIDER, "MetadataRepository");
        connectorProviderToAsset.put(OMRS_REST_REPOSITORY_CONNECTOR_PROVIDER, null);
        connectorProviderToAsset.put(OMRS_TOPIC_PROVIDER, null);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        /*
         * This is the user id to call the Egeria OMAG Server Platforms to extract information from.
         */
        if (connectionProperties.getUserId() != null)
        {
            clientUserId = connectionProperties.getUserId();
        }

        try
        {
            super.getContext().registerListener(this);
            super.getContext().setInfrastructureManagerIsHome(false);

            /*
             * Populate the monitored platforms with the catalogued SoftwareServerPlatforms.
             */
            List<SoftwareServerPlatformElement> cataloguedPlatforms = super.getContext().findSoftwareServerPlatforms(DeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType(), null, 0, 0);

            if (cataloguedPlatforms != null)
            {
                for (SoftwareServerPlatformElement platform : cataloguedPlatforms)
                {
                    assessElementForMonitoring(platform.getElementHeader().getGUID(),
                                               platform.getElementHeader().getType().getTypeName(),
                                               platform.getElementHeader().getType().getSuperTypeNames());
                }
            }
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(EgeriaInfrastructureConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                 error.getClass().getName(),
                                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }


        /*
         * Record the start
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                EgeriaInfrastructureConnectorAuditCode.EGERIA_CONNECTOR_START.getMessageDefinition(connectorName, clientUserId, monitoredPlatforms.toString()));
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * This method performs two sweeps. It first retrieves the topics from the event broker (Kafka) and validates that are in the
     * catalog - adding or updating them if necessary. The second sweep is to ensure that all the topics catalogued
     * actually exist in the event broker.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            /*
             * The monitored platforms list is built up from the platforms catalogued in open metadata.  It is possible that the endpoint is
             * not known - or the platform is not an OMAG Server Platform.
             */
            for (PlatformDetails platformDetails : monitoredPlatforms)
            {
                SoftwareServerPlatformElement element = super.getContext().getSoftwareServerPlatformByGUID(platformDetails.platformGUID);

                if (element != null)
                {
                    if (element.getProperties().getDeployedImplementationType().equals(DeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType()))
                    {
                        platformDetails.platformQualifiedName = element.getProperties().getQualifiedName();
                        platformDetails.platformDisplayName = element.getProperties().getName();

                        List<EndpointElement> endpoints = super.getContext().getEndpointsForInfrastructure(platformDetails.platformGUID, 0, 0);

                        if (endpoints != null)
                        {
                            for (EndpointElement endpoint : endpoints)
                            {
                                if (endpoint.getEndpointProperties().getAddress() != null)
                                {
                                    String platformOrigin = this.testPlatformURL(platformDetails.platformDisplayName, endpoint.getEndpointProperties().getAddress());

                                    if (platformOrigin != null)
                                    {
                                        platformDetails.platformRootURL = endpoint.getEndpointProperties().getAddress();

                                        SoftwareServerPlatformProperties softwareServerPlatformProperties = new SoftwareServerPlatformProperties();

                                        softwareServerPlatformProperties.setSoftwareServerPlatformVersion(platformOrigin);
                                        super.getContext().updateSoftwareServerPlatform(platformDetails.platformGUID,
                                                                                        true,

                                                                                softwareServerPlatformProperties);

                                        break;
                                    }
                                }
                            }
                        }

                        /*
                         * If the endpoint is set up in the platform details it means that platform has been identified as an OMAG Server Platform
                         * with a callable URL.  The next step is to catalog the configured servers and link the server entity of those that are
                         * known to the platform to the platform entity with the "DeployedOn" relationship.
                         */
                        if (platformDetails.platformRootURL != null)
                        {
                            ConfigurationManagementClient configurationManagementClient = new ConfigurationManagementClient(clientUserId,
                                                                                                                            platformDetails.platformRootURL);

                            /*
                             * Retrieve all configured servers
                             */
                            Set<OMAGServerConfig> configuredServers = configurationManagementClient.getAllServerConfigurations();

                            if (configuredServers != null)
                            {
                                for (OMAGServerConfig serverConfig : configuredServers)
                                {
                                    if (serverConfig != null)
                                    {
                                        this.catalogServer(serverConfig);
                                    }
                                }
                            }

                            /*
                             * Servers may be configured but not destined to run on the platform.  Add the DeployedOn relationship between the
                             * platform and a server only if the server is known to the platform - this means it has been run on the platform.
                             */
                            PlatformServicesClient platformServicesClient = new PlatformServicesClient(platformDetails.platformDisplayName,
                                                                                                       platformDetails.platformRootURL);

                            List<String> serverList = platformServicesClient.getKnownServers(clientUserId);

                            if (serverList != null)
                            {
                                List<DeploymentElement> linkedServers = super.getContext().getDeployedITAssets(platformDetails.platformGUID, null, 0, 0);

                                if (linkedServers != null)
                                {
                                    for (DeploymentElement deploymentElement : linkedServers)
                                    {
                                        if (! serverList.contains(deploymentElement.getAssetElement().getProperties().getName()))
                                        {
                                            String serverGUID = serverMap.get(deploymentElement.getAssetElement().getProperties().getName());

                                            if (serverGUID != null)
                                            {
                                                super.getContext().deployITAsset(serverGUID, platformDetails.platformGUID, null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            EgeriaInfrastructureConnectorAuditCode.UNKNOWN_PLATFORM.getMessageDefinition(connectorName,
                                                                                                                         platformDetails.platformDisplayName,
                                                                                                                         platformDetails.platformGUID));


                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      EgeriaInfrastructureConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                      error);


            }

            throw new ConnectorCheckedException(EgeriaInfrastructureConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                 error.getClass().getName(),
                                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Test the endpoint linked to the platform.
     *
     * @param platformName Name of the platform
     * @param candidateURL URL to test
     * @return server origin (or null if platform is not running - or not a platform
     */
    private String testPlatformURL(String platformName,
                                   String candidateURL)
    {
        final String methodName = "testPlatformURL";

        try
        {
            PlatformServicesClient platformServicesClient = new PlatformServicesClient(platformName, candidateURL);

            return platformServicesClient.getPlatformOrigin(clientUserId);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      EgeriaInfrastructureConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                      error);
            }
        }

        return null;
    }


    /**
     * Add details of the configured servers to open metadata.
     *
     * @param serverConfig configuration for a single server.
     */
    private void catalogServer(OMAGServerConfig serverConfig)
    {
        final String methodName = "catalogServer";

        try
        {
            String serverGUID = serverMap.get(serverConfig.getLocalServerName());

            if (serverGUID == null)
            {
                /*
                 * The catalog entry for the server is not in the server map - was it created by a previous run of this connector?
                 */
                List<SoftwareServerElement> softwareServerElements = super.getContext().getSoftwareServersByName(serverConfig.getLocalServerName(), null, 0, 0);

                if (softwareServerElements != null)
                {
                    for (SoftwareServerElement softwareServerElement : softwareServerElements)
                    {
                        String qualifiedName = this.getServerQualifiedName(serverConfig.getLocalServerId(), serverConfig.getLocalServerName());

                        if ((serverConfig.getLocalServerName().equals(softwareServerElement.getProperties().getName())) &&
                            (qualifiedName.equals(softwareServerElement.getProperties().getQualifiedName())))
                        {
                            /*
                             * The server is already catalogued - use the guid.
                             */
                            serverGUID = softwareServerElement.getElementHeader().getGUID();

                            serverMap.put(serverConfig.getLocalServerName(), serverGUID);
                        }
                    }
                }
            }

            if (serverGUID == null)
            {
                /*
                 * No catalog entry exists for the server.
                 */
                SoftwareServerProperties softwareServerProperties = this.getSoftwareServerProperties(serverConfig);

                serverGUID = super.getContext().createSoftwareServer(softwareServerProperties);

                if (serverGUID != null)
                {
                    serverMap.put(serverConfig.getLocalServerName(), serverGUID);
                }
            }
            else
            {
                /*
                 * Update the existing entry with the current values from the server config.
                 */
                SoftwareServerProperties softwareServerProperties = this.getSoftwareServerProperties(serverConfig);

                super.getContext().updateSoftwareServer(serverGUID, true, softwareServerProperties);
            }

            /*
             * Walk through the contents of the server to catalog the services.
             */
            catalogRepositoryServices(serverConfig, serverGUID);
            catalogAccessServices(serverConfig, serverGUID);
            catalogEngineServices(serverConfig, serverGUID);
            catalogIntegrationServices(serverConfig, serverGUID);
            catalogViewServices(serverConfig, serverGUID);

            super.getContext().publishSoftwareServer(serverGUID);

        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      EgeriaInfrastructureConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Every server has the repository services enabled.
     *
     * @param serverConfig server configuration
     * @param serverGUID unique identifier of the server entity
     */
    private void catalogRepositoryServices(OMAGServerConfig serverConfig,
                                           String           serverGUID) throws ConnectorCheckedException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        String serviceType = "SoftwareService";
        String serviceGUID = this.createSoftwareService(serverGUID,
                                                        serverConfig.getLocalServerName(),
                                                        serviceType,
                                                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceCode(),
                                                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(),
                                                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceDescription(),
                                                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceDevelopmentStatus(),
                                                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceURLMarker(),
                                                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceWiki());


        /*
         * Catalog the connectors
         */
        RepositoryServicesConfig  repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

        List<CohortConfig> cohortConfigs = repositoryServicesConfig.getCohortConfigList();

        if (cohortConfigs != null)
        {
            for (CohortConfig cohortConfig : cohortConfigs)
            {
                Connection connection = cohortConfig.getCohortOMRSRegistrationTopicConnection();

                if (connection != null)
                {
                    createConnector(serviceGUID,
                                    CommonServicesDescription.REPOSITORY_SERVICES.getServiceName() + ":" + cohortConfig.getCohortName(),
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentName() + ":Registration",
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentDescription(),
                                    cohortConfig.getCohortOMRSRegistrationTopicConnection());
                }

                connection = cohortConfig.getCohortOMRSTypesTopicConnection();

                if (connection != null)
                {
                    createConnector(serviceGUID,
                                    CommonServicesDescription.REPOSITORY_SERVICES.getServiceName() + ":" + cohortConfig.getCohortName(),
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentName() + ":Types",
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentDescription(),
                                    cohortConfig.getCohortOMRSTypesTopicConnection());
                }

                connection = cohortConfig.getCohortOMRSInstancesTopicConnection();

                if (connection != null)
                {
                    createConnector(serviceGUID,
                                    CommonServicesDescription.REPOSITORY_SERVICES.getServiceName() + ":" + cohortConfig.getCohortName(),
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentName() + ":Instances",
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentDescription(),
                                    cohortConfig.getCohortOMRSInstancesTopicConnection());
                }

                connection = cohortConfig.getCohortOMRSTopicConnection();

                if (connection != null)
                {
                    createConnector(serviceGUID,
                                    CommonServicesDescription.REPOSITORY_SERVICES.getServiceName() + ":" + cohortConfig.getCohortName(),
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentName(),
                                    OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentDescription(),
                                    cohortConfig.getCohortOMRSTopicConnection());
                }

                connection = cohortConfig.getCohortRegistryConnection();

                if (connection != null)
                {
                    createConnector(serviceGUID,
                                    CommonServicesDescription.REPOSITORY_SERVICES.getServiceName() + ":" + cohortConfig.getCohortName() + ":Registry",
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.COHORT_REGISTRY.getComponentName(),
                                    OMRSAuditingComponent.COHORT_REGISTRY.getComponentDescription(),
                                    cohortConfig.getCohortRegistryConnection());
                }
            }
        }
    }


    /**
     * Access services are found in metadata access servers.
     *
     * @param serverConfig server configuration
     * @param serverGUID unique identifier of the server entity
     */
    private void catalogAccessServices(OMAGServerConfig serverConfig,
                                       String           serverGUID) throws ConnectorCheckedException,
                                                                           InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<AccessServiceConfig> accessServiceConfigs = serverConfig.getAccessServicesConfig();

        if (accessServiceConfigs != null)
        {
            for (AccessServiceConfig accessServiceConfig : accessServiceConfigs)
            {
                String serviceGUID = this.createSoftwareService(serverGUID,
                                                                serverConfig.getLocalServerName(),
                                                                "MetadataAccessService",
                                                                accessServiceConfig.getAccessServiceId(),
                                                                accessServiceConfig.getAccessServiceFullName(),
                                                                accessServiceConfig.getAccessServiceDescription(),
                                                                accessServiceConfig.getAccessServiceDevelopmentStatus(),
                                                                accessServiceConfig.getAccessServiceURLMarker(),
                                                                accessServiceConfig.getAccessServiceWiki());

                if (accessServiceConfig.getAccessServiceInTopic() != null)
                {
                    createConnector(serviceGUID,
                                    accessServiceConfig.getAccessServiceFullName(),
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.OMAS_IN_TOPIC.getComponentName(),
                                    OMRSAuditingComponent.OMAS_IN_TOPIC.getComponentDescription(),
                                    accessServiceConfig.getAccessServiceInTopic());
                }

                if (accessServiceConfig.getAccessServiceOutTopic() != null)
                {
                    createConnector(serviceGUID,
                                    accessServiceConfig.getAccessServiceFullName(),
                                    serverConfig.getLocalServerName(),
                                    OMRSAuditingComponent.OMAS_OUT_TOPIC.getComponentName(),
                                    OMRSAuditingComponent.OMAS_OUT_TOPIC.getComponentDescription(),
                                    accessServiceConfig.getAccessServiceOutTopic());
                }
            }
        }
    }


    /**
     * Engine services are found in engine hosts.
     *
     * @param serverConfig server configuration
     * @param serverGUID unique identifier of the server entity
     */
    private void catalogEngineServices(OMAGServerConfig serverConfig,
                                       String           serverGUID) throws ConnectorCheckedException,
                                                                           InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

        if ((engineHostServicesConfig != null) && (engineHostServicesConfig.getEngineServiceConfigs() != null))
        {
            for (EngineServiceConfig engineServiceConfig : engineHostServicesConfig.getEngineServiceConfigs())
            {
                this.createSoftwareService(serverGUID,
                                           serverConfig.getLocalServerName(),
                                           "EngineHostingService",
                                           engineServiceConfig.getEngineServiceId(),
                                           engineServiceConfig.getEngineServiceFullName(),
                                           engineServiceConfig.getEngineServiceDescription(),
                                           engineServiceConfig.getEngineServiceDevelopmentStatus(),
                                           engineServiceConfig.getEngineServiceURLMarker(),
                                           engineServiceConfig.getEngineServiceWiki());
            }
        }
    }


    /**
     * Integration services are found in integration daemons.
     *
     * @param serverConfig server configuration
     * @param serverGUID unique identifier of the server entity
     */
    private void catalogIntegrationServices(OMAGServerConfig serverConfig,
                                            String           serverGUID) throws ConnectorCheckedException,
                                                                                InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        List<IntegrationServiceConfig> integrationServiceConfigs = serverConfig.getIntegrationServicesConfig();

        if (integrationServiceConfigs != null)
        {
            for (IntegrationServiceConfig integrationServiceConfig : integrationServiceConfigs)
            {
                this.createSoftwareService(serverGUID,
                                           serverConfig.getLocalServerName(),
                                           "MetadataIntegrationService",
                                           integrationServiceConfig.getIntegrationServiceId(),
                                           integrationServiceConfig.getIntegrationServiceFullName(),
                                           integrationServiceConfig.getIntegrationServiceDescription(),
                                           integrationServiceConfig.getIntegrationServiceDevelopmentStatus(),
                                           integrationServiceConfig.getIntegrationServiceURLMarker(),
                                           integrationServiceConfig.getIntegrationServiceWiki());
            }
        }
    }


    /**
     * View services are found in view servers.
     *
     * @param serverConfig server configuration
     * @param serverGUID unique identifier of the server entity
     */
    private void catalogViewServices(OMAGServerConfig serverConfig,
                                     String           serverGUID) throws ConnectorCheckedException,
                                                                         InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        List<ViewServiceConfig> viewServiceConfigs = serverConfig.getViewServicesConfig();

        if (viewServiceConfigs != null)
        {
            for (ViewServiceConfig viewServiceConfig : viewServiceConfigs)
            {
                this.createSoftwareService(serverGUID,
                                           serverConfig.getLocalServerName(),
                                           "UserViewService",
                                           viewServiceConfig.getViewServiceId(),
                                           viewServiceConfig.getViewServiceFullName(),
                                           viewServiceConfig.getViewServiceDescription(),
                                           viewServiceConfig.getViewServiceDevelopmentStatus(),
                                           viewServiceConfig.getViewServiceURLMarker(),
                                           viewServiceConfig.getViewServiceWiki());
            }
        }
    }


    /**
     * Create a connector and optional data asset.
     *
     * @param serviceGUID unique identifier of the owning service
     * @param serviceName name of the owning service
     * @param serverName name of hosting server
     * @param connectorName name of connector
     * @param connectorDescription description of connector
     * @param connectorConnection connector used to create connector
     * @throws ConnectorCheckedException no context
     * @throws InvalidParameterException bad parameters passed
     * @throws UserNotAuthorizedException problem with user Id
     * @throws PropertyServerException problem with open metadata repository
     */
    private void createConnector(String     serviceGUID,
                                 String     serviceName,
                                 String     serverName,
                                 String     connectorName,
                                 String     connectorDescription,
                                 Connection connectorConnection) throws ConnectorCheckedException,
                                                                        InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        ProcessProperties processProperties = new ProcessProperties();

        processProperties.setQualifiedName(serverName + ":" + serviceName + ":" + connectorName);
        processProperties.setName(connectorName);
        processProperties.setResourceDescription(connectorDescription);
        processProperties.setImplementationLanguage("Java");

        String              connectorProviderClassName = connectorConnection.getConnectorType().getConnectorProviderClassName();
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("connectorProviderClass", connectorProviderClassName);

        processProperties.setAdditionalProperties(additionalProperties);
        String connectorGUID = super.getContext().createProcess(ProcessStatus.ACTIVE, processProperties);

        ServerAssetUseProperties serverAssetUseProperties = new ServerAssetUseProperties();

        serverAssetUseProperties.setUseType(ServerAssetUseType.OWNS);
        super.getContext().createServerAssetUse(serviceGUID, connectorGUID, serverAssetUseProperties);

        String resourceAssetType = connectorProviderToAsset.get(connectorProviderClassName);

        if (resourceAssetType != null)
        {
            DataAssetProperties assetProperties = new DataAssetProperties();

            assetProperties.setTypeName(resourceAssetType);
            assetProperties.setQualifiedName(resourceAssetType + ":" + connectorConnection.getEndpoint().getAddress());
            assetProperties.setName(connectorConnection.getEndpoint().getAddress());

            String dataAssetGUID = super.getContext().createDataAsset(assetProperties);

            super.getContext().setupProcessCall(connectorGUID, dataAssetGUID, null, new Date());
        }

        if (connectorConnection instanceof VirtualConnection)
        {
            VirtualConnection virtualConnection = (VirtualConnection)connectorConnection;

            if (virtualConnection.getEmbeddedConnections() != null)
            {
                for (EmbeddedConnection embeddedConnection : virtualConnection.getEmbeddedConnections())
                {
                    if ((embeddedConnection != null) && (embeddedConnection.getEmbeddedConnection() != null))
                    {
                        /*
                         * Embedded connections are assumed to be topic connectors
                         */
                        createConnector(connectorGUID,
                                        serviceName + ":" + connectorName,
                                        serverName,
                                        OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR.getComponentName(),
                                        OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR.getComponentDescription(),
                                        embeddedConnection.getEmbeddedConnection());
                    }
                }
            }
        }
    }


    /**
     * Construct the qualified name for a server.
     *
     * @param serverId unique identifier for the server
     * @param serverName unique name ofr the server.
     * @return composite qualified name
     */
    private String getServerQualifiedName(String serverId,
                                          String serverName)
    {
        return serverName + " [" + serverId + "]";
    }


    /**
     * Use the values from the server configuration to set up the properties for a software server entity.
     *
     * @param serverConfig server configuration
     * @return properties
     */
    private SoftwareServerProperties getSoftwareServerProperties(OMAGServerConfig serverConfig)
    {
        SoftwareServerProperties softwareServerProperties = new SoftwareServerProperties();

        softwareServerProperties.setQualifiedName(this.getServerQualifiedName(serverConfig.getLocalServerId(), serverConfig.getLocalServerName()));
        softwareServerProperties.setName(serverConfig.getLocalServerName());
        softwareServerProperties.setResourceDescription(serverConfig.getLocalServerDescription());
        softwareServerProperties.setSoftwareServerSource("Egeria");
        softwareServerProperties.setSoftwareServerVersion(serverConfig.getVersionId());
        softwareServerProperties.setSoftwareServerUserId(serverConfig.getLocalServerUserId());

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("organizationName", serverConfig.getOrganizationName());
        additionalProperties.put("serverId", serverConfig.getLocalServerId());
        additionalProperties.put("serverURL", serverConfig.getLocalServerURL());
        additionalProperties.put("maxPageSize", Integer.toString(serverConfig.getMaxPageSize()));

        if (serverConfig.getAuditTrail() != null)
        {
            additionalProperties.put("auditTrail", serverConfig.getAuditTrail().toString());
        }

        softwareServerProperties.setAdditionalProperties(additionalProperties);

        return softwareServerProperties;
    }




    /**
     * Create an entity for a software service.
     *
     * @param serverGUID unique identifier of the server entity
     * @param serverName name of the server
     * @param serviceType type of service to catalog
     * @param serviceCode component code of service
     * @param serviceFullName full name of service
     * @param serviceDescription description of service
     * @param developmentStatus status of service
     * @param serviceURLMarker segment of the URL in API that identifies this component
     * @param wikiURL url of wiki for design and diagnosis
     * @return unique identifier of new service
     * @throws ConnectorCheckedException no context
     * @throws InvalidParameterException bad parameters passed
     * @throws UserNotAuthorizedException problem with user id
     * @throws PropertyServerException problem with open metadata repository
     */
    String createSoftwareService(String                     serverGUID,
                                 String                     serverName,
                                 String                     serviceType,
                                 int                        serviceCode,
                                 String                     serviceFullName,
                                 String                     serviceDescription,
                                 ComponentDevelopmentStatus developmentStatus,
                                 String                     serviceURLMarker,
                                 String                     wikiURL) throws ConnectorCheckedException,
                                                                            InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        SoftwareCapabilityProperties properties = new SoftwareCapabilityProperties();

        if (serviceType != null)
        {
            properties.setTypeName(serviceType);
        }
        else
        {
            properties.setTypeName("SoftwareService");
        }

        properties.setQualifiedName(serverName + ":" + serviceFullName);
        properties.setResourceName(serviceFullName);
        properties.setResourceDescription(serviceDescription);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("componentCode", Integer.toString(serviceCode));
        additionalProperties.put("componentWikiURL", wikiURL);
        additionalProperties.put("developmentStatus", developmentStatus.getName());
        additionalProperties.put("serviceURLMarker", serviceURLMarker);

        properties.setAdditionalProperties(additionalProperties);

        String serviceGUID = super.getContext().createSoftwareCapability(null, properties);

        super.getContext().deployCapability(serviceGUID, serverGUID, null);

        return serviceGUID;
    }


    /**
     * Called each time an event that is published by the IT Infrastructure OMAS, it is looking for Software Server Platforms to add to monitoredPlatforms.
     */
    public void processEvent(ITInfrastructureOutTopicEvent event)
    {
        assessElementForMonitoring(event.getElementHeader().getGUID(),
                                   event.getElementHeader().getType().getTypeName(),
                                   event.getElementHeader().getType().getSuperTypeNames());
    }


    /**
     * If the element is a software server platform, and it is not already being monitored then it is added to monitored platforms.
     *
     * @param elementGUID unique id of element
     * @param elementTypeName type name of element
     * @param elementSuperTypes super types of element
     */
    private void assessElementForMonitoring(String       elementGUID,
                                            String       elementTypeName,
                                            List<String> elementSuperTypes)
    {
        if ((OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName.equals(elementTypeName)) ||
                ((elementSuperTypes != null) && (elementSuperTypes.contains(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName))))
        {
            /*
             * Element is a software server platform. Is this a new platform?
             */
            boolean alreadyMonitored = false;

            for (PlatformDetails platformDetails : monitoredPlatforms)
            {
                if (elementGUID.equals(platformDetails.platformGUID))
                {
                    alreadyMonitored = true;
                    break;
                }
            }

            /*
             * All new platforms are added to monitored platforms
             */
            if (! alreadyMonitored)
            {
                PlatformDetails platformDetails = new PlatformDetails();

                platformDetails.platformGUID = elementGUID;
                platformDetails.platformTypeName = elementTypeName;

                monitoredPlatforms.add(platformDetails);
            }
        }
    }


    /**
     * Shutdown monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public synchronized void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                EgeriaInfrastructureConnectorAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
