/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.utilities.serverconfig;

import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.CohortMemberConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationConnectorConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ServerConfig creates configuration documents that used by servers to define the subsystems and connectors
 * that should be started when the server starts up.
 */
public class ServerConfig
{
    private static final String serverSecurityConnectorProviderClassName  = "org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityProvider";
    private static final String defaultAPIIntegrationConnectorProvider    = "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider";
    private static final String defaultInfraIntegrationConnectorProvider  = "org.odpi.openmetadata.devprojects.connectors.integration.egeria.EgeriaCataloguerIntegrationProvider";
    private static final String defaultTopicIntegrationConnectorProvider  = "org.odpi.openmetadata.adapters.connectors.integration.kafka.KafkaMonitorIntegrationProvider";
    private static final String eventsDisplayConnectorProviderClassName   = "org.odpi.openmetadata.devprojects.connectors.auditlog.eventdisplay.EventDisplayAuditLogStoreProvider";
    private static final String defaultAuditLogConnectorProvider          = "org.odpi.openmetadata.devprojects.connectors.auditlog.eventdisplay.EventDisplayAuditLogStoreProvider";
    private static final String defaultCohort                             = "dojoCohort";
    private static final String eventBusURLRoot                           = "localhost:9092"; // set to null to turn off all eventing to Kafka topics
    private static final String organizationName                          = "Coco Pharmaceuticals";
    private static final String systemUserId                              = "cocoMDS1npa";
    private static final int    maxPageSize = 600;

    private final String platformURLRoot;
    private final String clientUserId;


    /**
     * Set up the parameters for the utility.
     *
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     */
    private ServerConfig(String platformURLRoot,
                         String clientUserId)
    {
        this.platformURLRoot = platformURLRoot;
        this.clientUserId    = clientUserId;
    }


    /**
     * Retrieve the version of the platform.  This fails if the platform is not running or the endpoint is populated by a service that is not an
     * OMAG Server Platform.
     *
     * @return platform version or null
     */
    private String getPlatformOrigin()
    {
        try
        {
            /*
             * This client is from the platform services module and queries the runtime state of the platform and the servers that are running on it.
             */
            PlatformServicesClient platformServicesClient = new PlatformServicesClient("MyPlatform", platformURLRoot);

            /*
             * This is the first call to the platform and determines the version of the software.
             * If the platform is not running, or the remote service is not an OMAG Server Platform,
             * the utility fails at this point.
             */
            return platformServicesClient.getPlatformOrigin(clientUserId);
        }
        catch (Exception error)
        {
            System.out.println("\n\nThere was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.out.println("Ensure the platform URl is correct and the platform is running");
        }

        return null;
    }



    /**
     * Set up the defaults that are used whenever a topic is configured for a server.
     * Check that the eventBusURLRoot is correct for your Kafka installation.
     *
     * @param client client responsible for the server configuration
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    private void setEventBus(OMAGServerConfigurationClient client) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        if (eventBusURLRoot != null)
        {
            Map<String, Object> configurationProperties = new HashMap<>();
            Map<String, Object> producerProperties      = new HashMap<>();
            Map<String, Object> consumerProperties      = new HashMap<>();

            producerProperties.put("bootstrap.servers", eventBusURLRoot);
            consumerProperties.put("bootstrap.servers", eventBusURLRoot);
            consumerProperties.put("auto.commit.interval.ms", "1");

            configurationProperties.put("producer", producerProperties);
            configurationProperties.put("consumer", consumerProperties);

            client.setEventBus(null, // use default - kafka
                               null, // use default - egeria.omag
                               configurationProperties);
        }
    }


    /**
     * Configure a security connector.  The default connector uses the Coco Pharmaceutical users and rules.
     * Change the serverSecurityConnectorProviderClassName to null to turn off security in new servers.
     * Alternatively change the class name for a different connector.
     *
     * @param client client responsible for the server configuration
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    private void setSecuritySecurityConnector(OMAGServerConfigurationClient client) throws OMAGNotAuthorizedException,
                                                                                           OMAGInvalidParameterException,
                                                                                           OMAGConfigurationErrorException
    {
        if (serverSecurityConnectorProviderClassName != null)
        {
            Connection    connection    = new Connection();
            ConnectorType connectorType = new ConnectorType();

            connectorType.setConnectorProviderClassName(serverSecurityConnectorProviderClassName);
            connection.setConnectorType(connectorType);

            client.setServerSecurityConnection(connection);
        }
    }


    /**
     * Create a new metadata access store OMAG server with an in-memory repository.
     * If the event bus is not set up, the access services are configured without in and out topics.
     *
     * @param serverName name of the new server
     */
    private void createMetadataStore(String serverName)
    {
        try
        {
            System.out.println("Configuring metadata store: " + serverName);

            MetadataAccessStoreConfigurationClient client = new MetadataAccessStoreConfigurationClient(clientUserId, serverName, platformURLRoot);

            client.setServerDescription("Metadata Access Store called " + serverName + " running on platform " + platformURLRoot);
            client.setServerUserId(systemUserId);
            client.setServerType(null); // Let the admin service set up the server types
            client.setOrganizationName(organizationName);
            client.setMaxPageSize(maxPageSize);

            this.setSecuritySecurityConnector(client);
            client.setDefaultAuditLog();

            client.setServerURLRoot(platformURLRoot);
            this.setEventBus(client);

            client.setInMemLocalRepository();
            client.setLocalMetadataCollectionName(serverName + "'s metadata collection");

            List<String> assetLookupZones = new ArrayList<>();

            assetLookupZones.add("data-lake");
            assetLookupZones.add("personal-files");

            List<String> personalFilesZone = new ArrayList<>();

            personalFilesZone.add("personal-files");

            List<String> maintenanceZones = new ArrayList<>();

            maintenanceZones.add("quarantine");
            maintenanceZones.add("trash-can");
            maintenanceZones.add("data-lake");

            List<String> newDataZone = new ArrayList<>();

            newDataZone.add("quarantine");

            Map<String, Object> accessServiceOptions = new HashMap<>();


            if (eventBusURLRoot != null)
            {
                accessServiceOptions.put("SupportedZones", assetLookupZones);
                accessServiceOptions.put("DefaultZones", personalFilesZone);

                client.configureAccessService("asset-consumer", accessServiceOptions);

                accessServiceOptions.put("KarmaPointPlateau", 10);

                client.configureAccessService("community-profile", accessServiceOptions);

                accessServiceOptions = new HashMap<>();
                accessServiceOptions.put("SupportedZones", maintenanceZones);
                accessServiceOptions.put("DefaultZones", newDataZone);
                accessServiceOptions.put("PublishZones", assetLookupZones);

                client.configureAccessService("asset-owner", accessServiceOptions);
                client.configureAccessService("data-manager", accessServiceOptions);
                client.configureAccessService("asset-manager", accessServiceOptions);
                client.configureAccessService("governance-program", accessServiceOptions);
                client.configureAccessService("digital-architecture", accessServiceOptions);
                client.configureAccessService("it-infrastructure", accessServiceOptions);
            }
            else // configure the access services without in and out topics
            {
                accessServiceOptions.put("SupportedZones", assetLookupZones);
                accessServiceOptions.put("DefaultZones", personalFilesZone);

                client.configureAccessServiceNoTopics("asset-consumer", accessServiceOptions);

                accessServiceOptions.put("KarmaPointPlateau", 10);

                client.configureAccessServiceNoTopics("community-profile", accessServiceOptions);

                accessServiceOptions = new HashMap<>();
                accessServiceOptions.put("SupportedZones", maintenanceZones);
                accessServiceOptions.put("DefaultZones", newDataZone);
                accessServiceOptions.put("PublishZones", assetLookupZones);

                client.configureAccessServiceNoTopics("asset-owner", accessServiceOptions);
                client.configureAccessServiceNoTopics("data-manager", accessServiceOptions);
                client.configureAccessServiceNoTopics("asset-manager", accessServiceOptions);
                client.configureAccessServiceNoTopics("governance-program", accessServiceOptions);
                client.configureAccessServiceNoTopics("digital-architecture", accessServiceOptions);
                client.configureAccessServiceNoTopics("it-infrastructure", accessServiceOptions);
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Print out the audit log connections.
     *
     * @param client client used to retrieve the audit log destinations
     */
    private void printAuditLogDestinations(OMAGServerConfigurationClient client)
    {
        try
        {
            List<Connection> auditLogConnections = client.getOMAGServerConfig().getRepositoryServicesConfig().getAuditLogConnections();

            if (auditLogConnections == null)
            {
                System.out.println("Error: no audit log destinations");

            }
            else
            {
                System.out.println("Audit log destination are:");

                for (Connection connection : auditLogConnections)
                {
                    if (connection != null)
                    {
                        System.out.print("| " + connection.getDisplayName() + " | ");
                        if (connection.getConfigurationProperties() != null)
                        {
                            System.out.print("supported severities: " + connection.getConfigurationProperties().get(OMRSAuditLogStoreProviderBase.supportedSeveritiesProperty));
                        }

                        System.out.println(" | ");
                    }
                }
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when printing audit log connections.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Set up the supportedSeverities property in the audit log destination connection.
     *
     * @param supportedSeverities list of supported severities
     * @param auditLogDestination connection object
     */
    private void setSupportedAuditLogSeverities(List<String> supportedSeverities,
                                                Connection   auditLogDestination)
    {
        if (supportedSeverities != null)
        {
            Map<String, Object> configurationProperties = auditLogDestination.getConfigurationProperties();

            if (configurationProperties == null)
            {
                configurationProperties = new HashMap<>();
            }

            configurationProperties.put(OMRSAuditLogStoreProviderBase.supportedSeveritiesProperty, supportedSeverities);
            auditLogDestination.setConfigurationProperties(configurationProperties);
        }
    }


    /**
     * Add the audit log connector that logs the event payload and turn off event logging in the
     * default console audit log.
     *
     * @param serverName server name to upgrade
     * @param connectorProviderClassName optional connector provider name (or use the default
     *                                   value defined in eventsDisplayConnectorProviderClassName)
     */
    private void logEventContents(String serverName,
                                  String connectorProviderClassName)
    {
        final String egeriaConsoleAuditLogImplementation = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider";

        /*
         * Create a list of severities for the existing console audit log destination that
         * removes the EVENT severity. (It already ignores the TRACE and PERFMON severities).
         */
        List<AuditLogRecordSeverityLevel> supportedSeverityDefinitions = Arrays.asList(AuditLogRecordSeverityLevel.values());
        List<String>                      consoleSupportedSeverities   = new ArrayList<>();

        for (AuditLogRecordSeverityLevel severityDefinition : supportedSeverityDefinitions)
        {
            if ((! AuditLogRecordSeverityLevel.EVENT.equals(severityDefinition)) &&
                (! AuditLogRecordSeverityLevel.TRACE.equals(severityDefinition)) &&
                (! AuditLogRecordSeverityLevel.PERFMON.equals(severityDefinition)))
            {
                consoleSupportedSeverities.add(severityDefinition.getName());
            }
        }

        try
        {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(clientUserId, serverName, platformURLRoot);

            /*
             * This is the list of configured audit log connections.  Locating the console audit log connection
             * can be tricky because different organizations may set up the connection objects with different
             * parameters.  In this example, the audit log destinations using the default implementation of the
             * console audit log destination are updated.
             */
            List<Connection> auditLogConnections = client.getOMAGServerConfig().getRepositoryServicesConfig().getAuditLogConnections();

            if (auditLogConnections != null)
            {
                for (Connection auditLogConnection : auditLogConnections)
                {
                    if (auditLogConnection != null)
                    {
                        ConnectorType auditLogConnectorType = auditLogConnection.getConnectorType();

                        if ((auditLogConnectorType != null) &&
                            (auditLogConnectorType.getConnectorProviderClassName().equals(egeriaConsoleAuditLogImplementation)))
                        {
                            /*
                             * This is a console audit log connector.
                             */
                            this.setSupportedAuditLogSeverities(consoleSupportedSeverities, auditLogConnection);
                            client.updateAuditLogDestination(auditLogConnection.getQualifiedName(), auditLogConnection);
                        }
                    }
                }
            }

            /*
             * Create a connection object for the new connector
             */
            Connection    eventDisplayConnection = new Connection();
            ConnectorType connectorType = new ConnectorType();

            if (connectorProviderClassName == null)
            {
                connectorType.setConnectorProviderClassName(eventsDisplayConnectorProviderClassName);
            }
            else
            {
                connectorType.setConnectorProviderClassName(connectorProviderClassName);
            }

            eventDisplayConnection.setConnectorType(connectorType);
            eventDisplayConnection.setQualifiedName("Egeria:Sample:AuditLog:DisplayEventPayloadsOnConsole");
            eventDisplayConnection.setDisplayName("Display Event Payloads On Console Audit Log Destination");

            List<String> eventDisplaySupportedSeverities = new ArrayList<>();
            eventDisplaySupportedSeverities.add(AuditLogRecordSeverityLevel.EVENT.getName());
            this.setSupportedAuditLogSeverities(eventDisplaySupportedSeverities, eventDisplayConnection);

            client.addAuditLogDestination(eventDisplayConnection);

            printAuditLogDestinations(client);
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when updating audit log connections.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Create a new integration daemon OMAG Server.  It needs to know the name of the metadata server that it will connect to to access
     * the open metadata ecosystem.
     *
     * @param serverName name for new integration daemon
     */
    private void createIntegrationDaemon(String serverName)
    {
        try
        {
            System.out.println("Configuring integration daemon: " + serverName);

            IntegrationDaemonConfigurationClient client = new IntegrationDaemonConfigurationClient(clientUserId, serverName, platformURLRoot);

            client.setServerDescription("Integration daemon called " + serverName + " running on platform " + platformURLRoot);
            client.setServerUserId(systemUserId);
            client.setServerType(null); // Let the admin service set up the server types
            client.setOrganizationName(organizationName);
            client.setMaxPageSize(maxPageSize);

            this.setSecuritySecurityConnector(client);
            client.setDefaultAuditLog();
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }



    /**
     * Add a new Integrator Connector to an Integration Daemon server.
     *
     * @param serverName integration daemon server name
     * @param metadataStoreName name of metadata store
     * @param serviceURLMarker URL for the integration service
     * @param connectorProviderClassName  connector provider name
     * @param networkAddress optional network address
     */
    private void addIntegrationConnector(String serverName,
                                         String metadataStoreName,
                                         String serviceURLMarker,
                                         String connectorProviderClassName,
                                         String networkAddress)
    {
        Connection    connection    = new Connection();
        ConnectorType connectorType = new ConnectorType();
        Endpoint      endpoint      = new Endpoint();

        connectorType.setConnectorProviderClassName(connectorProviderClassName);
        connection.setConnectorType(connectorType);

        /*
         * The endpoint is only set up if the network address is supplied
         */
        if (networkAddress != null)
        {
            endpoint.setAddress(networkAddress);
            connection.setEndpoint(endpoint);
        }

        IntegrationConnectorConfig integrationConnectorConfig = new IntegrationConnectorConfig();

        integrationConnectorConfig.setConnectorId(UUID.randomUUID().toString());
        integrationConnectorConfig.setConnectorName("Integration Connector for " + connectorProviderClassName);
        integrationConnectorConfig.setConnectorUserId(systemUserId);
        integrationConnectorConfig.setConnection(connection);
        integrationConnectorConfig.setRefreshTimeInterval(1);
        integrationConnectorConfig.setMetadataSourceQualifiedName("Egeria Catalog Thyself Demo");
        integrationConnectorConfig.setPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);

        try
        {
            IntegrationDaemonConfigurationClient client = new IntegrationDaemonConfigurationClient(clientUserId, serverName, platformURLRoot);

            IntegrationServiceConfig serviceConfiguration = client.getIntegrationServiceConfiguration(serviceURLMarker);

            serviceConfiguration.setOMAGServerName(metadataStoreName);
            serviceConfiguration.setOMAGServerPlatformRootURL(platformURLRoot);
            serviceConfiguration.setDefaultPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);

            List<IntegrationConnectorConfig> connectorList = serviceConfiguration.getIntegrationConnectorConfigs();

            if (connectorList == null)
            {
                connectorList = new ArrayList<>();
            }

            connectorList.add(integrationConnectorConfig);

            serviceConfiguration.setIntegrationConnectorConfigs(connectorList);

            client.configureIntegrationService(serviceConfiguration);
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Add a server to the named cohort.  This will fail if the event bus is not set up.
     *
     * @param serverName name of server to update
     * @param cohortName name of cohort to join
     */
    private void addCohortMember(String serverName,
                                 String cohortName)
    {
        try
        {
            CohortMemberConfigurationClient client = new CohortMemberConfigurationClient(clientUserId, serverName, platformURLRoot);

            client.addCohortRegistration(cohortName, null);
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Add an open metadata archive to the startup list of the name server.
     *
     * @param serverName name of server to update
     * @param archiveFileName name of cohort to join
     */
    private void addStartupArchive(String serverName,
                                   String archiveFileName)
    {
        try
        {
            MetadataAccessStoreConfigurationClient client = new MetadataAccessStoreConfigurationClient(clientUserId, serverName, platformURLRoot);

            client.addStartUpOpenMetadataArchiveFile(archiveFileName);
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Update a server's userId.
     *
     * @param serverName name of server to update
     * @param userId new userId
     */
    private void updateServerUserId(String serverName,
                                    String userId)
    {
        try
        {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(clientUserId, serverName, platformURLRoot);

            client.setServerUserId(userId);
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Delete a server.
     *
     * @param serverName name of server
     */
    private void deleteServer(String serverName)
    {
        try
        {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(clientUserId, serverName, platformURLRoot);

            client.clearOMAGServerConfig();
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Run the requested command.
     *
     * @param mode command
     * @param options list of options - first is server name
     */
    private void runCommand(String   mode,
                            String[] options)
    {

        if ("create-metadata-store".equals(mode))
        {
            if (options.length > 0)
            {
                this.createMetadataStore(options[0]);
            }
            else
            {
                System.out.println("  Error: include a server name");
            }
        }
        else if ("create-integration-daemon".equals(mode))
        {
            if (options.length > 0)
            {
                this.createIntegrationDaemon(options[0]);
            }
            else
            {
                System.out.println("  Error: include a server name for the integration daemon and the name of the metadata store that it is to connect to.");
            }
        }
        else if ("log-event-contents".equals(mode))
        {
            if (options.length == 1)
            {
                this.logEventContents(options[0], defaultAuditLogConnectorProvider);
            }
            else if (options.length > 1)
            {
                this.logEventContents(options[0], options[1]);
            }
            else
            {
                System.out.println("  Error: include a server name");
            }
        }
        else if ("add-topic-connector".equals(mode))
        {
            final String serviceURLMarker = "topic-integrator";

            if (options.length == 2)
            {
                this.addIntegrationConnector(options[0],
                                             options[1],
                                             serviceURLMarker,
                                             defaultTopicIntegrationConnectorProvider,
                                             eventBusURLRoot);
            }
            else if (options.length == 3)
            {
                this.addIntegrationConnector(options[0], options[1], serviceURLMarker, options[2], null);
            }
            else if (options.length > 3)
            {
                this.addIntegrationConnector(options[0], options[1], serviceURLMarker, options[2], options[3]);
            }
            else
            {
                System.out.println("  Error: include both integration daemon and metadata server name with 'add-topic-connector' request");
            }
        }
        else if ("add-api-connector".equals(mode))
        {
            final String serviceURLMarker = "api-integrator";

            if (options.length == 2)
            {
                this.addIntegrationConnector(options[0],
                                             options[1],
                                             serviceURLMarker,
                                             defaultAPIIntegrationConnectorProvider,
                                             null);
            }
            else if (options.length == 3)
            {
                this.addIntegrationConnector(options[0], options[1], serviceURLMarker, options[2], null);
            }
            else if (options.length > 3)
            {
                this.addIntegrationConnector(options[0], options[1], serviceURLMarker, options[2], options[3]);
            }
            else
            {
                System.out.println("  Error: include both integration daemon and metadata server name with 'add-api-connector' request");
            }
        }
        else if ("add-infra-connector".equals(mode))
        {
            final String serviceURLMarker = "infrastructure-integrator";

            if (options.length == 2)
            {
                this.addIntegrationConnector(options[0],
                                             options[1],
                                             serviceURLMarker,
                                             defaultInfraIntegrationConnectorProvider,
                                             null);
            }
            else if (options.length == 3)
            {
                this.addIntegrationConnector(options[0], options[1], serviceURLMarker, options[2], null);
            }
            else if (options.length > 3)
            {
                this.addIntegrationConnector(options[0], options[1], serviceURLMarker, options[2], options[3]);
            }
            else
            {
                System.out.println("  Error: include both integration daemon and metadata server name with 'add-infra-connector' request");
            }
        }
        else if ("add-cohort-member".equals(mode))
        {
            if (options.length == 1)
            {
                this.addCohortMember(options[0], defaultCohort);
            }
            else if (options.length > 1)
            {
                this.addCohortMember(options[0], options[1]);
            }
            else
            {
                System.out.println("  Error: include a server name");
            }
        }
        else if ("add-startup-archive".equals(mode))
        {
            if (options.length > 1)
            {
                this.addStartupArchive(options[0], options[1]);
            }
            else
            {
                System.out.println("  Error: include a server name and an archive file name");
            }
        }
        else if ("update-server-user-id".equals(mode))
        {
            if (options.length > 1)
            {
                this.updateServerUserId(options[0], options[1]);
            }
            else
            {
                System.out.println("  Error: include a server name and new user id");
            }
        }
        else if ("delete-server".equals(mode))
        {
            if (options.length > 0)
            {
                this.deleteServer(options[0]);
            }
            else
            {
                System.out.println("  Error: include a server name");
            }
        }
        else
        {
            System.out.println("  Error: use a valid command");
        }
    }


    /**
     * Main program that controls the operation of the platform report.  The parameters are passed space separated.
     * The  parameters are used to override the report's default values. If mode is set to "interactive"
     * the caller is prompted for a command and one to many server names.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. mode 4. server name 5. server name ...
     */
    public static void main(String[] args)
    {
        final String interactiveMode = "interactive";
        final String endInteractiveMode = "exit";

        String       platformURLRoot = "https://localhost:9443";
        String       clientUserId = "garygeeke";
        String       mode = interactiveMode;

        if (args.length > 0)
        {
            platformURLRoot = args[0];
        }

        if (args.length > 1)
        {
            clientUserId = args[1];
        }

        if (args.length > 2)
        {
            mode = args[2];
        }

        System.out.println("==================================");
        System.out.println("OMAG Server Configuration Utility:    " + new Date().toString());
        System.out.println("==================================");
        System.out.print("Running against platform: " + platformURLRoot);

        ServerConfig utility = new ServerConfig(platformURLRoot, clientUserId);

        HttpHelper.noStrictSSL();

        String platformOrigin = utility.getPlatformOrigin();

        if (platformOrigin != null)
        {
            System.out.print(" - " + platformOrigin);
        }
        else
        {
            System.out.println();
            System.exit(-1);
        }

        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        try
        {
            if (interactiveMode.equals(mode))
            {
                while (! endInteractiveMode.equals(mode))
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter a command along with the server name and any optional parameters. Press enter to execute request.");
                    System.out.println("  - create-metadata-store     <serverName>  ");
                    System.out.println("  - create-integration-daemon <serverName>  ");
                    System.out.println("  - add-api-connector         <serverName> <metadataStoreServerName> <optionalConnectorProviderClassName> <optionalNetworkAddress>");
                    System.out.println("  - add-infra-connector       <serverName> <metadataStoreServerName> <optionalConnectorProviderClassName> <optionalNetworkAddress>");
                    System.out.println("  - add-topic-connector       <serverName> <metadataStoreServerName> <optionalConnectorProviderClassName> <optionalNetworkAddress>");
                    System.out.println("  - log-event-contents        <serverName> <optionalConnectorProviderClassName> ");
                    System.out.println("  - add-cohort-member         <serverName> <optionalCohortName> ");
                    System.out.println("  - add-startup-archive       <serverName> <archiveFileName> ");
                    System.out.println("  - update-server-user-id     <serverName> <newUserId> ");
                    System.out.println("  - delete-server             <serverName>  ");
                    System.out.println("  - exit  \n");

                    String   command = br.readLine();
                    String[] commandWords = command.split(" ");

                    if (commandWords.length > 0)
                    {
                        mode = commandWords[0];

                        if (commandWords.length > 1)
                        {
                            utility.runCommand(mode,  Arrays.copyOfRange(commandWords, 1, commandWords.length));
                        }
                    }

                    System.out.println();
                }
            }
            else
            {
                utility.runCommand(mode, Arrays.copyOfRange(args, 3, args.length));
            }
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }
}
