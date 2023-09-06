/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.platform;


import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerPlatformConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationConnectorConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.reports.EgeriaReport;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.MetadataHighwayServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.properties.CohortConnectionStatus;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerInstanceHistory;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * EgeriaPlatformReport illustrates the use of the Platform Services, Admin Services and Repository Services to pull
 * together a report of an OMAG Server Platform's services and active servers.
 */
public class EgeriaPlatformReport
{
    private final String       serverOfInterest;
    private final String       platformURLRoot;
    private final String       clientUserId;
    private final EgeriaReport report;

    /**
     * Set up the parameters for the sample.
     *
     * @param serverOfInterest server to restrict the results
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     * @throws IOException problem writing file
     */
    private EgeriaPlatformReport(String serverOfInterest,
                                 String platformURLRoot,
                                 String clientUserId) throws IOException
    {
        final String reportFileName  = "egeria-platform-report.md";

        this.serverOfInterest = serverOfInterest;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId = clientUserId;

        report = new EgeriaReport(reportFileName);
    }


    /**
     * This runs the sample.
     */
    private void run()
    {
        int indentLevel = 0;

        try
        {
            /*
             * This client is from the platform services module and queries the runtime state of the platform and the servers that are running on it.
             */
            PlatformServicesClient platformServicesClient = new PlatformServicesClient("EgeriaPlatform", platformURLRoot);

            /*
             * This outputs the report title
             */
            final String reportTitle = "Platform report for: ";

            report.printReportTitle(indentLevel, reportTitle + platformURLRoot);

            int detailIndentLevel = indentLevel + 1;

            report.printReportSubheading(detailIndentLevel, "Platform deployment");

            /*
             * This is the first call to the platform and determines the version of the software.
             * If the platform is not running, or the remote service is not an OMAG Server Platform,
             * the report utility fails at this point.
             */
            String platformOrigin = platformServicesClient.getPlatformOrigin(clientUserId);

            report.printReportLine(detailIndentLevel + 1, "Egeria version", platformOrigin.replace("\n", ""));

            /*
             * These clients are from the admin services module. The platform configuration client manages the configuration of the platform.
             * The configuration management client manages and moves configuration documents for OMAG Servers.  It will be able to
             * work with all configuration documents that are visible to the platform - not just those servers intended to
             * run on this platform.
             */
            OMAGServerPlatformConfigurationClient platformConfigurationClient   = new OMAGServerPlatformConfigurationClient(clientUserId, platformURLRoot);
            ConfigurationManagementClient         configurationManagementClient = new ConfigurationManagementClient(clientUserId, platformURLRoot);

            /*
             * Extract information about the connector that manages the configuration document store.
             * This is where the configuration for the OMAG Servers is maintained.
             */
            report.printConnection(detailIndentLevel + 1,
                                   "Configuration document store connector",
                                   platformConfigurationClient.getConfigurationStoreConnection());

            /*
             * Extract information about the connector that manages the authorization of requests to the platform.
             */
            report.printConnection(detailIndentLevel + 1,
                                   "Platform security connector",
                                   platformServicesClient.getPlatformSecurityConnection(clientUserId));

            /*
             * List the registered services
             */
            report.printReportSubheading(detailIndentLevel, "Registered services");

            report.printRegisteredServices(detailIndentLevel + 1, platformServicesClient.getAccessServices(clientUserId));
            report.printRegisteredServices(detailIndentLevel + 1, platformServicesClient.getEngineServices(clientUserId));
            report.printRegisteredServices(detailIndentLevel + 1, platformServicesClient.getIntegrationServices(clientUserId));
            report.printRegisteredServices(detailIndentLevel + 1, platformServicesClient.getViewServices(clientUserId));

            /*
             * Collect server details.
             */
            Map<String, OMAGServerDetails> serverDetailsMap = new HashMap<>();

            if (serverOfInterest == null)
            {
                report.printReportSubheading(detailIndentLevel, "Platform servers");

                /*
                 * Output requested for all servers
                 */
                Set<OMAGServerConfig> configuredServers = null;

                try
                {
                    configuredServers = configurationManagementClient.getAllServerConfigurations();
                }
                catch (Exception exception)
                {
                    // assume no configurations
                }

                if (configuredServers != null)
                {
                    for (OMAGServerConfig serverConfig : configuredServers)
                    {
                        OMAGServerDetails currentDetails = serverDetailsMap.get(serverConfig.getLocalServerName());

                        if (currentDetails == null)
                        {
                            currentDetails = new OMAGServerDetails(serverConfig.getLocalServerName());
                        }

                        currentDetails.setConfiguration(serverConfig);

                        serverDetailsMap.put(serverConfig.getLocalServerName(), currentDetails);
                    }
                }

                List<String> serverList = platformServicesClient.getKnownServers(clientUserId);

                if (serverList != null)
                {
                    for (String serverName : serverList)
                    {
                        if (serverName != null)
                        {
                            ServerStatus platformServerStatus = platformServicesClient.getServerStatus(clientUserId, serverName);

                            if (platformServerStatus != null)
                            {
                                OMAGServerDetails currentDetails = serverDetailsMap.get(serverName);

                                if (currentDetails == null)
                                {
                                    currentDetails = new OMAGServerDetails(serverName);
                                }

                                currentDetails.setServerStartTime(platformServerStatus.getServerStartTime());
                                currentDetails.setServerEndTime(platformServerStatus.getServerEndTime());
                                currentDetails.setServerHistory(platformServerStatus.getServerHistory());
                            }
                        }
                    }
                }

                serverList = platformServicesClient.getActiveServers(clientUserId);

                if (serverList != null)
                {
                    for (String serverName : serverList)
                    {
                        if (serverName != null)
                        {
                            /*
                             * This client provides specific details of a running server - it is provided by the Admin Services mon
                             */
                            PlatformServicesClient serverOperationsClient = new PlatformServicesClient(serverName, platformURLRoot);

                            ServerServicesStatus adminServerStatus = serverOperationsClient.getActiveServerStatus(clientUserId, serverName);

                            if (adminServerStatus != null)
                            {
                                OMAGServerDetails currentDetails = serverDetailsMap.get(serverName);

                                if (currentDetails == null)
                                {
                                    currentDetails = new OMAGServerDetails(serverName);
                                }

                                currentDetails.setServerActiveStatus(adminServerStatus.getServerActiveStatus());
                                currentDetails.setServerType(adminServerStatus.getServerType());
                                currentDetails.setServices(adminServerStatus.getServices());
                            }
                        }
                    }
                }
            }
            else
            {
                report.printReportSubheading(detailIndentLevel, "Server of interest");

                OMAGServerDetails serverDetails = new OMAGServerDetails(serverOfInterest);

                OMAGServerConfigurationClient configurationClient = new OMAGServerConfigurationClient(clientUserId, serverOfInterest, platformURLRoot);

                OMAGServerConfig serverConfig = configurationClient.getOMAGServerConfig();

                if (serverConfig != null)
                {
                    serverDetails.setConfiguration(serverConfig);
                }

                try
                {
                    ServerStatus platformServerStatus = platformServicesClient.getServerStatus(clientUserId, serverOfInterest);

                    if (platformServerStatus != null)
                    {
                        serverDetails.setServerStartTime(platformServerStatus.getServerStartTime());
                        serverDetails.setServerEndTime(platformServerStatus.getServerEndTime());
                        serverDetails.setServerHistory(platformServerStatus.getServerHistory());
                    }
                }
                catch (InvalidParameterException serverNotRunningException)
                {
                    // nothing to do - simply that the server is not running
                }

                try
                {
                    PlatformServicesClient serverOperationsClient = new PlatformServicesClient(serverOfInterest, platformURLRoot);

                    ServerServicesStatus adminServerStatus = serverOperationsClient.getActiveServerStatus(clientUserId, serverOfInterest);

                    if (adminServerStatus != null)
                    {
                        serverDetails.setServerActiveStatus(adminServerStatus.getServerActiveStatus());
                        serverDetails.setServerType(adminServerStatus.getServerType());
                        serverDetails.setServices(adminServerStatus.getServices());
                    }
                }
                catch (Exception serverNotRunningException)
                {
                    // nothing to do - simply that the server is not running
                }

                serverDetailsMap.put(serverOfInterest, serverDetails);
            }


            /*
             * Add runtime information about the cohorts.
             */
            for (OMAGServerDetails serverDetails : serverDetailsMap.values())
            {
                if (serverDetails != null)
                {
                    MetadataHighwayServicesClient metadataHighwayServicesClient = new MetadataHighwayServicesClient(serverDetails.getServerName(), platformURLRoot);

                    try
                    {
                        List<CohortDescription> cohorts = metadataHighwayServicesClient.getCohortDescriptions(clientUserId);

                        if (cohorts != null)
                        {
                            for (CohortDescription cohortDescription : cohorts)
                            {
                                if (cohortDescription != null)
                                {
                                    OMAGServerDetails.OMAGCohortDetails cohortDetails = serverDetails.getCohortDetails(cohortDescription.getCohortName());

                                    cohortDetails.setConnectionStatus(cohortDescription.getConnectionStatus());

                                    cohortDetails.setLocalRegistration(metadataHighwayServicesClient.getLocalRegistration(clientUserId,
                                                                                                                          cohortDescription.getCohortName()));

                                    cohortDetails.setRemoteRegistrations(metadataHighwayServicesClient.getRemoteRegistrations(clientUserId,
                                                                                                                              cohortDescription.getCohortName()));
                                }
                            }
                        }
                    }
                    catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException serverNotRunningException)
                    {
                        // nothing to do - simply that the server is not running
                    }
                }
            }

            /*
             * Now all the details about the servers is assembled, it can be printed out.
             */
            if (serverDetailsMap.isEmpty())
            {
                report.printReportLine(detailIndentLevel + 1, "None");
            }
            else
            {
                for (OMAGServerDetails serverDetails : serverDetailsMap.values())
                {
                    if (serverDetails != null)
                    {
                        serverDetails.printServer(detailIndentLevel + 1);
                    }
                }
            }

            report.closeReport();
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * OMAGServerDetails provides a cache to assemble details about a server.  It is initialized through a
     * series of set method calls that pass information retrieved from the OMAG Server Platform.  It extracts the
     * interesting values that are to form part of the server report.  Once all that is known about the server has
     * been assembled
     */
    private class OMAGServerDetails
    {
        private final String                    serverName;
        private String                          serverType           = null;
        private OMAGServerConfig                configuration        = null;
        private ServerActiveStatus              serverActiveStatus   = ServerActiveStatus.UNKNOWN;
        private Date                            serverStartTime      = null;
        private Date                            serverEndTime        = null;
        private List<OMAGServerInstanceHistory> serverHistory        = null;

        private final Map<String, OMAGServiceDetails> serviceDetailsMap = new HashMap<>();
        private final Map<String, OMAGCohortDetails>  cohortDetailsMap  = new HashMap<>();


        /**
         * Constructor assumes the server name is known.
         *
         * @param serverName name of the subject of this object
         */
        OMAGServerDetails(String serverName)
        {
            this.serverName = serverName;
        }


        /**
         * Retrieve the name of the server.
         *
         * @return string name
         */
        String getServerName()
        {
            return serverName;
        }


        /**
         * The configuration identifies the server and the services that it runs.
         *
         * @param configuration configuration document for the server
         */
        void setConfiguration(OMAGServerConfig configuration)
        {
            this.configuration = configuration;

            if (configuration != null)
            {
                serverType = configuration.getLocalServerType();

                if (configuration.getRepositoryServicesConfig() != null)
                {
                    if (configuration.getRepositoryServicesConfig().getCohortConfigList() != null)
                    {
                        for (CohortConfig cohortConfig : configuration.getRepositoryServicesConfig().getCohortConfigList())
                        {
                            OMAGCohortDetails currentDetails = this.getCohortDetails(cohortConfig.getCohortName());

                            if (cohortConfig.getCohortRegistryConnection() != null)
                            {
                                currentDetails.setConnection("Cohort Registry Store", cohortConfig.getCohortRegistryConnection());
                            }

                            if (cohortConfig.getCohortOMRSRegistrationTopicConnection() != null)
                            {
                                currentDetails.setConnection("Cohort Registration Topic", cohortConfig.getCohortOMRSRegistrationTopicConnection());
                            }

                            if (cohortConfig.getCohortOMRSTypesTopicConnection() != null)
                            {
                                currentDetails.setConnection("Cohort Types Topic", cohortConfig.getCohortOMRSTypesTopicConnection());
                            }

                            if (cohortConfig.getCohortOMRSInstancesTopicConnection() != null)
                            {
                                currentDetails.setConnection("Cohort Instances Topic", cohortConfig.getCohortOMRSInstancesTopicConnection());
                            }

                            if (cohortConfig.getCohortOMRSTopicConnection() != null)
                            {
                                currentDetails.setConnection("Cohort OMRS Topic (deprecated)", cohortConfig.getCohortOMRSTopicConnection());
                            }
                        }
                    }
                }

                if (configuration.getAccessServicesConfig() != null)
                {
                    for (AccessServiceConfig accessServiceConfig : configuration.getAccessServicesConfig())
                    {
                        if (accessServiceConfig != null)
                        {
                            OMAGServiceDetails currentDetails = serviceDetailsMap.get(accessServiceConfig.getAccessServiceFullName());

                            if (currentDetails == null)
                            {
                                currentDetails = new OMAGServiceDetails(accessServiceConfig.getAccessServiceFullName());
                            }

                            currentDetails.setServiceOptions(accessServiceConfig.getAccessServiceOptions());
                            currentDetails.setConnection("InTopic", accessServiceConfig.getAccessServiceInTopic());
                            currentDetails.setConnection("OutTopic", accessServiceConfig.getAccessServiceInTopic());

                            serviceDetailsMap.put(accessServiceConfig.getAccessServiceFullName(), currentDetails);
                        }
                    }
                }

                if (configuration.getEngineHostServicesConfig() != null)
                {
                    OMAGServiceDetails currentDetails = serviceDetailsMap.get(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName());

                    if (currentDetails == null)
                    {
                        currentDetails = new OMAGServiceDetails(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName());
                    }

                    currentDetails.setPartnerService(configuration.getEngineHostServicesConfig().getOMAGServerName(),
                                                     configuration.getEngineHostServicesConfig().getOMAGServerPlatformRootURL(),
                                                     AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName());

                    if (configuration.getEngineHostServicesConfig().getEngineServiceConfigs() != null)
                    {
                        for (EngineServiceConfig engineServiceConfig : configuration.getEngineHostServicesConfig().getEngineServiceConfigs())
                        {
                            if (engineServiceConfig != null)
                            {
                                OMAGServiceDetails nestedDetails = serviceDetailsMap.get(engineServiceConfig.getEngineServiceFullName());

                                if (nestedDetails == null)
                                {
                                    nestedDetails = new OMAGServiceDetails(engineServiceConfig.getEngineServiceFullName());
                                }

                                nestedDetails.setServiceOptions(engineServiceConfig.getEngineServiceOptions());
                                nestedDetails.setPartnerService(engineServiceConfig.getOMAGServerName(),
                                                                engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                                engineServiceConfig.getEngineServicePartnerOMAS());

                                if (engineServiceConfig.getEngines() != null)
                                {
                                    for (EngineConfig engineConfig : engineServiceConfig.getEngines())
                                    {
                                        if (engineConfig != null)
                                        {
                                            OMAGServiceDetails engineServiceDetails = new OMAGServiceDetails(engineConfig.getEngineQualifiedName());

                                            engineServiceDetails.setServiceId(engineConfig.getEngineId());
                                            engineServiceDetails.setServiceUserId(engineConfig.getEngineUserId());

                                            nestedDetails.addNestedService(engineConfig.getEngineQualifiedName(), engineServiceDetails);
                                        }
                                    }
                                }

                                serviceDetailsMap.put(engineServiceConfig.getEngineServiceFullName(), currentDetails);
                            }
                        }
                    }
                }

                if (configuration.getIntegrationServicesConfig() != null)
                {
                    for (IntegrationServiceConfig integrationServiceConfig : configuration.getIntegrationServicesConfig())
                    {
                        if (integrationServiceConfig != null)
                        {
                            OMAGServiceDetails currentDetails = serviceDetailsMap.get(integrationServiceConfig.getIntegrationServiceFullName());

                            if (currentDetails == null)
                            {
                                currentDetails = new OMAGServiceDetails(integrationServiceConfig.getIntegrationServiceFullName());
                            }

                            currentDetails.setServiceOptions(integrationServiceConfig.getIntegrationServiceOptions());
                            currentDetails.setPartnerService(integrationServiceConfig.getOMAGServerName(),
                                                             integrationServiceConfig.getOMAGServerPlatformRootURL(),
                                                             integrationServiceConfig.getIntegrationServicePartnerOMAS());

                            if (integrationServiceConfig.getIntegrationConnectorConfigs() != null)
                            {
                                for (IntegrationConnectorConfig connectorConfig : integrationServiceConfig.getIntegrationConnectorConfigs())
                                {
                                    if (connectorConfig != null)
                                    {
                                        String nestedServiceName = "Integration Connector: " + connectorConfig.getConnectorName();

                                        OMAGServiceDetails connectorConfigDetails = new OMAGServiceDetails(nestedServiceName);

                                        connectorConfigDetails.setServiceUserId(connectorConfig.getConnectorUserId());
                                        connectorConfigDetails.setServiceId(connectorConfig.getConnectorId());
                                        connectorConfigDetails.setConnection("Integration Connector Implementation", connectorConfig.getConnection());

                                        currentDetails.addNestedService(nestedServiceName, connectorConfigDetails);
                                    }
                                }
                            }

                            serviceDetailsMap.put(integrationServiceConfig.getIntegrationServiceFullName(), currentDetails);
                        }
                    }
                }

                if (configuration.getViewServicesConfig() != null)
                {
                    for (ViewServiceConfig viewServiceConfig : configuration.getViewServicesConfig())
                    {
                        if (viewServiceConfig != null)
                        {
                            OMAGServiceDetails currentDetails = serviceDetailsMap.get(viewServiceConfig.getViewServiceFullName());

                            if (currentDetails == null)
                            {
                                currentDetails = new OMAGServiceDetails(viewServiceConfig.getViewServiceFullName());
                            }

                            currentDetails.setServiceOptions(viewServiceConfig.getViewServiceOptions());
                            currentDetails.setPartnerService(viewServiceConfig.getOMAGServerName(),
                                                             viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                             null);

                            serviceDetailsMap.put(viewServiceConfig.getViewServiceFullName(), currentDetails);
                        }
                    }
                }

                if (configuration.getDataEngineProxyConfig() != null)
                {
                    OMAGServiceDetails currentDetails = serviceDetailsMap.get(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName());

                    if (currentDetails == null)
                    {
                        currentDetails = new OMAGServiceDetails(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName());
                    }

                    currentDetails.setPartnerService(configuration.getDataEngineProxyConfig().getAccessServiceServerName(),
                                                     configuration.getDataEngineProxyConfig().getAccessServiceRootURL(),
                                                     AccessServiceDescription.DATA_ENGINE_OMAS.getAccessServiceFullName());

                    serviceDetailsMap.put(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                          currentDetails);
                }

                if (configuration.getOpenLineageServerConfig() != null)
                {
                    OMAGServiceDetails currentDetails = serviceDetailsMap.get(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName());

                    if (currentDetails == null)
                    {
                        currentDetails = new OMAGServiceDetails(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName());
                    }

                    currentDetails.setPartnerService(configuration.getOpenLineageServerConfig().getAccessServiceConfig().getServerName(),
                                                     configuration.getOpenLineageServerConfig().getAccessServiceConfig().getServerPlatformUrlRoot(),
                                                     AccessServiceDescription.ASSET_LINEAGE_OMAS.getAccessServiceFullName());

                    serviceDetailsMap.put(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                          currentDetails);
                }
            }
        }


        /**
         * The server type show where the server fits in the architecture.
         *
         * @param serverType name of the server type
         */
        void setServerType(String serverType)
        {
            this.serverType = serverType;
        }


        /**
         * Is the server stopped, starting or running?
         *
         * @param serverActiveStatus what is the server status (null if not running)
         */
        void setServerActiveStatus(ServerActiveStatus serverActiveStatus)
        {
            this.serverActiveStatus = serverActiveStatus;
        }


        /**
         * The time that the server last started.
         *
         * @param serverStartTime date/time or null
         */
        void setServerStartTime(Date serverStartTime)
        {
            this.serverStartTime = serverStartTime;
        }


        /**
         * The time that the server shut down - will be null if server started or never run.
         *
         * @param serverEndTime date/time or null
         */
        void setServerEndTime(Date serverEndTime)
        {
            this.serverEndTime = serverEndTime;
        }


        /**
         * This lists the times that the server has run on the platform instance.
         *
         * @param serverHistory server history list
         */
        void setServerHistory(List<OMAGServerInstanceHistory> serverHistory)
        {
            this.serverHistory = serverHistory;
        }


        /**
         * List the services that are currently running - null if the server is not running.
         *
         * @param services list of server status
         */
        void setServices(List<OMAGServerServiceStatus> services)
        {
            if (services != null)
            {
                for (OMAGServerServiceStatus serviceStatus : services)
                {
                    OMAGServiceDetails currentDetails = serviceDetailsMap.get(serviceStatus.getServiceName());

                    if (currentDetails == null)
                    {
                        currentDetails = new OMAGServiceDetails(serviceStatus.getServiceName());
                    }

                    currentDetails.setServiceStatus(serviceStatus.getServiceStatus());

                    serviceDetailsMap.put(serviceStatus.getServiceName(), currentDetails);
                }
            }
        }


        /**
         * Return the cohort details for the named cohort.
         *
         * @param cohortName name of the cohort.
         *
         * @return corresponding details
         */
        OMAGCohortDetails getCohortDetails(String cohortName)
        {
            OMAGCohortDetails cohortDetails = cohortDetailsMap.get(cohortName);

            if (cohortDetails == null)
            {
                cohortDetails = new OMAGCohortDetails(cohortName);

                cohortDetailsMap.put(cohortName, cohortDetails);
            }

            return cohortDetails;
        }

        /**
         * Output the details that have been collected about the server.
         *
         * @param indentLevel amount of white space to add before each line of the report
         */
        void printServer(int indentLevel) throws IOException
        {
            report.printReportSubheading(indentLevel,"Server: " +  serverName);

            int detailIndentLevel = indentLevel + 1;

            report.printReportLine(detailIndentLevel, "Type", serverType);

            if (configuration != null)
            {
                report.printReportLine(detailIndentLevel, "Description", configuration.getLocalServerDescription());
                report.printReportLine(detailIndentLevel, "UserId", configuration.getLocalServerUserId());

                if (configuration.getServerSecurityConnection() != null)
                {
                    report.printConnection(detailIndentLevel, "Security Connector", configuration.getServerSecurityConnection());
                }

                if (configuration.getRepositoryServicesConfig() != null)
                {
                    if (configuration.getRepositoryServicesConfig().getLocalRepositoryConfig() != null)
                    {
                        report.printReportSubheading(detailIndentLevel,"Local Repository");

                        if (configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryMode() != null)
                        {
                            report.printReportLine(detailIndentLevel + 1,
                                                   "Local Repository Mode",
                                                   configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryMode().getName());
                        }

                        if (configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryLocalConnection() != null)
                        {
                            report.printConnection(detailIndentLevel + 1,
                                                   "Local Repository Connector",
                                                   configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryLocalConnection());
                        }

                        if (configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getEventMapperConnection() != null)
                        {
                            report.printConnection(detailIndentLevel + 1,
                                                   "Local Repository Event Mapper Connector",
                                                   configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getEventMapperConnection());
                        }

                        if (configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryRemoteConnection() != null)
                        {
                            report.printConnection(detailIndentLevel + 1,
                                                   "Local Repository Remote Connector",
                                                   configuration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryRemoteConnection());
                        }
                    }
                }
            }

            report.printReportSubheading(detailIndentLevel,"Runtime Status");

            if (serverStartTime != null)
            {
                report.printReportLine(detailIndentLevel + 1,"Last Start Time", serverStartTime.toString());
            }

            if (serverEndTime != null)
            {
                report.printReportLine(detailIndentLevel + 1,"Last End Time", serverEndTime.toString());
            }

            if ((serverActiveStatus != null) && (serverActiveStatus != ServerActiveStatus.UNKNOWN))
            {
                report.printReportLine(detailIndentLevel + 1,"Server Active Status", serverActiveStatus.getName());
            }

            if (serverHistory != null)
            {
                report.printReportSubheading(detailIndentLevel + 1,"History");

                for (OMAGServerInstanceHistory instanceHistory : serverHistory)
                {
                    if (instanceHistory.getStartTime() != null)
                    {
                        report.printReportLine(detailIndentLevel + 2,"Start Time", instanceHistory.getStartTime().toString());
                    }
                    if (instanceHistory.getEndTime() != null)
                    {
                        report.printReportLine(detailIndentLevel + 2,"End Time", instanceHistory.getEndTime().toString());
                    }
                }
            }

            report.printReportSubheading(detailIndentLevel,"Services");

            for (OMAGServiceDetails serviceDetails : serviceDetailsMap.values())
            {
                serviceDetails.printService(detailIndentLevel + 1);
            }

            if (! cohortDetailsMap.isEmpty())
            {
                report.printReportSubheading(detailIndentLevel, "Cohorts");

                for (OMAGCohortDetails cohortDetails : cohortDetailsMap.values())
                {
                    cohortDetails.printCohort(detailIndentLevel + 1);
                }
            }
        }


        /**
         * OMAGServiceDetails caches details about a particular service.
         */
        private class OMAGServiceDetails
        {
            private final String                    serviceName;
            private String                          serviceId          = null;
            private String                          serviceUserId      = null;
            private ServerActiveStatus              serviceStatus      = ServerActiveStatus.UNKNOWN;
            private Map<String, Object>             serviceOptions     = null;
            private String                          partnerServerName  = null;
            private String                          partnerURLRoot     = null;
            private String                          partnerServiceName = null;
            private Map<String, Connection>         connectors         = null;
            private Map<String, OMAGServiceDetails> nestedServices     = null;


            /**
             * Constructor requires the service name.
             *
             * @param serviceName display name of the service.
             */
            OMAGServiceDetails(String serviceName)
            {
                this.serviceName = serviceName;
            }


            /**
             * Set up the unique identifier for the service.
             *
             * @param serviceId string Id
             */
            void setServiceId(String serviceId)
            {
                this.serviceId = serviceId;
            }


            /**
             * Set up the user id assigned to this service.
             *
             * @param serviceUserId user id
             */
            void setServiceUserId(String serviceUserId)
            {
                this.serviceUserId = serviceUserId;
            }


            /**
             * Set up the current status.
             *
             * @param serviceStatus server instance status enum value
             */
            void setServiceStatus(ServerActiveStatus serviceStatus)
            {
                this.serviceStatus = serviceStatus;
            }


            /**
             * Save the service options as printable strings.
             *
             * @param serviceOptions configured service options
             */
            void setServiceOptions(Map<String, Object> serviceOptions)
            {
                this.serviceOptions = serviceOptions;
            }


            /**
             * Set up details of the partner service that this service is dependent on.
             *
             * @param partnerServerName name of the partner server
             * @param partnerURLRoot URL for the hosting platform
             * @param partnerServiceName service that is called in the partner server
             */
            void setPartnerService(String partnerServerName,
                                   String partnerURLRoot,
                                   String partnerServiceName)
            {
                this.partnerServerName = partnerServerName;
                this.partnerURLRoot = partnerURLRoot;
                this.partnerServiceName = partnerServiceName;
            }


            /**
             * Add details of a connector configured for this service.
             *
             * @param connectorName name/label for the connection
             * @param connection configuration information
             */
            void setConnection(String     connectorName,
                               Connection connection)
            {
                if (connection != null)
                {
                    if (connectors == null)
                    {
                        connectors = new HashMap<>();
                    }

                    Connection existingConnection = connectors.put(connectorName, connection);

                    if (existingConnection != null)
                    {
                        System.out.println("Error: two connectors of the same name: " + connectorName + " in service: " + serviceName);
                        System.out.println("       existing connection: " + existingConnection);
                        System.out.println("       new connection: " + connection);
                    }
                }
            }


            /**
             * Add details of a service that is nested in this service.
             *
             * @param serviceName nested service name
             * @param serviceDetails description of the nested service
             */
            void addNestedService(String             serviceName,
                                  OMAGServiceDetails serviceDetails)
            {
                if (nestedServices == null)
                {
                    nestedServices = new HashMap<>();
                }

                OMAGServiceDetails existingService = nestedServices.put(serviceName, serviceDetails);

                if (existingService != null)
                {
                    System.out.println("Error: two nested services of same name: " + serviceName + " in service: " + this.serviceName);
                    System.out.println("       existing nested service: " + existingService);
                    System.out.println("       new nested service: " + serviceDetails);
                }
            }


            /**
             * Print out details of the service that have been collected from the different APIs.
             *
             * @param indentLevel spacing for the service
             *
             * @throws IOException problem writing to the report
             */
            void printService(int indentLevel) throws IOException
            {
                report.printReportSubheading(indentLevel, "Service: " + serviceName);

                int detailIndentLevel = indentLevel + 1;

                if (serviceId != null)
                {
                    report.printReportLine(detailIndentLevel, "Service Id", serviceId);
                }

                if (serviceUserId != null)
                {
                    report.printReportLine(detailIndentLevel, "Service UserId", serviceUserId);
                }

                if ((serviceStatus != null) && (serviceStatus != ServerActiveStatus.UNKNOWN))
                {
                    report.printReportLine(detailIndentLevel, "Service Status", serviceStatus.getName());
                }

                if (partnerServerName != null)
                {
                    report.printReportSubheading(detailIndentLevel, "Partner Service:");
                    report.printReportLine(detailIndentLevel + 1, "Partner Server", partnerServerName);
                    report.printReportLine(detailIndentLevel + 1, "Partner URL root", partnerURLRoot);
                    report.printReportLine(detailIndentLevel + 1, "Calling Service Name", partnerServiceName);
                }

                if (serviceOptions != null)
                {
                    report.printReportSubheading(detailIndentLevel, "Service Options");
                    for (String optionName : serviceOptions.keySet())
                    {
                        if (optionName != null)
                        {
                            report.printReportLine(detailIndentLevel + 1, optionName, serviceOptions.get(optionName).toString());
                        }
                    }
                }

                if (nestedServices != null)
                {
                    report.printReportSubheading(detailIndentLevel, "Nested Services");

                    for (OMAGServiceDetails nestedService : nestedServices.values())
                    {
                        if (nestedService != null)
                        {
                            nestedService.printService(detailIndentLevel + 1);
                        }
                    }
                }

                if (connectors != null)
                {
                    if (connectors.size() > 1)
                    {
                        report.printReportSubheading(detailIndentLevel, "Connectors");
                        detailIndentLevel = detailIndentLevel + 1;
                    }

                    for (String connectorName : connectors.keySet())
                    {
                        report.printConnection(detailIndentLevel, connectorName, connectors.get(connectorName));
                    }
                }
            }
        }


        /**
         * OMAGCohortDetails caches details about a particular cohort.
         */
        private class OMAGCohortDetails
        {
            private final String             cohortName;
            private CohortConnectionStatus   connectionStatus    = null;
            private Map<String, Connection>  connectors          = null;
            private MemberRegistration       localRegistration   = null;
            private List<MemberRegistration> remoteRegistrations = null;


            /**
             * Constructor requires the cohort name.
             *
             * @param cohortName display name of the cohort.
             */
            OMAGCohortDetails(String cohortName)
            {
                this.cohortName = cohortName;
            }


            /**
             * Set up the current status of the server's connection to the cohort.
             *
             * @param connectionStatus enum
             */
            void setConnectionStatus(CohortConnectionStatus connectionStatus)
            {
                this.connectionStatus = connectionStatus;
            }


            /**
             * Add details of a connector configured for this cohort.
             *
             * @param connectorName name/label for the connection
             * @param connection configuration information
             */
            void setConnection(String     connectorName,
                               Connection connection)
            {
                if (connection != null)
                {
                    if (connectors == null)
                    {
                        connectors = new HashMap<>();
                    }

                    Connection existingConnection = connectors.put(connectorName, connection);

                    if (existingConnection != null)
                    {
                        System.out.println("Error: two connectors of the same name: " + connectorName + " in service: " + cohortName);
                        System.out.println("       existing connection: " + existingConnection);
                        System.out.println("       new connection: " + connection);
                    }
                }
            }


            /**
             * Set up information about the server's information that it sends out when it registers with a cohort.
             *
             * @param localRegistration local registration details
             */
            void setLocalRegistration(MemberRegistration localRegistration)
            {
                this.localRegistration = localRegistration;
            }


            /**
             * Set up the list of responses that this server has received from the other members of the cohort.
             *
             * @param remoteRegistrations list of responses from remote cohort members
             */
            void setRemoteRegistrations(List<MemberRegistration> remoteRegistrations)
            {
                this.remoteRegistrations = remoteRegistrations;
            }


            /**
             * Print out details of the cohort that have been collected from the different APIs.
             *
             * @param indentLevel spacing for the cohort content
             *
             * @throws IOException problem writing to the report
             */
            void printCohort(int indentLevel) throws IOException
            {
                report.printReportSubheading(indentLevel, "Cohort: " + cohortName);

                int detailIndentLevel = indentLevel + 1;

                if (connectionStatus != null)
                {
                    report.printReportLine(detailIndentLevel, "Cohort Connection Status", connectionStatus.getStatusName());
                }

                if (connectors != null)
                {
                    if (connectors.size() > 1)
                    {
                        report.printReportSubheading(detailIndentLevel, "Cohort Connectors");
                        detailIndentLevel = detailIndentLevel + 1;
                    }

                    for (String connectorName : connectors.keySet())
                    {
                        report.printConnection(detailIndentLevel, connectorName, connectors.get(connectorName));
                    }
                }

                if (localRegistration != null)
                {
                    report.printReportSubheading(detailIndentLevel, "Local registration to " + cohortName);

                    report.printReportLine(detailIndentLevel + 1, "Metadata Collection Id", localRegistration.getMetadataCollectionId());
                    report.printReportLine(detailIndentLevel + 1, "Metadata Collection Name", localRegistration.getMetadataCollectionName());

                    if (localRegistration.getRegistrationTime() != null)
                    {
                        report.printReportLine(detailIndentLevel + 1, "First registration time with cohort", localRegistration.getRegistrationTime().toString());
                    }
                }

                if (remoteRegistrations != null)
                {
                    report.printReportSubheading(detailIndentLevel, "Registrations received from members of " + cohortName);

                    for (MemberRegistration remoteRegistration : remoteRegistrations)
                    {
                        if (remoteRegistration != null)
                        {
                            report.printReportSubheading(detailIndentLevel + 1, "Registration from " + remoteRegistration.getServerName());

                            report.printReportLine(detailIndentLevel + 2, "Metadata Collection Id", localRegistration.getMetadataCollectionId());
                            report.printReportLine(detailIndentLevel + 2, "Metadata Collection Name", localRegistration.getMetadataCollectionName());
                            report.printReportLine(detailIndentLevel + 2, "Server Type", localRegistration.getServerType());
                            report.printReportLine(detailIndentLevel + 2, "Organization", localRegistration.getOrganizationName());

                            if (remoteRegistration.getRegistrationTime() != null)
                            {
                                report.printReportLine(detailIndentLevel + 2, "First registration time with this member", remoteRegistration.getRegistrationTime().toString());
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Main program that controls the operation of the platform report.  The parameters are passed space separated.
     * They are used to override the report's default values.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. server name,
     */
    public static void main(String[] args)
    {
        String  serverName = null; // means all servers
        String  platformURLRoot = "https://localhost:9443";
        String  clientUserId = "garygeeke";

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
            serverName = args[2];
        }

        System.out.println("===============================");
        System.out.println("OMAG Server Platform Report:    " + new Date());
        System.out.println("===============================");
        System.out.println("Running against platform: " + platformURLRoot);
        if (serverName != null)
        {
            System.out.println("Focused on server: " + serverName);
        }
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSL();

        try
        {
            EgeriaPlatformReport report = new EgeriaPlatformReport(serverName, platformURLRoot, clientUserId);

            report.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
