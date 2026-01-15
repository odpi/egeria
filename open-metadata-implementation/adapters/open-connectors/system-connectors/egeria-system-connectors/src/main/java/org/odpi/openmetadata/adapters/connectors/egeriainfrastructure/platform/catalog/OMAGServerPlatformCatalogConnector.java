/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.*;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerConnector;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaSolutionComponent;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OMAGServerPlatformCatalogConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    Map<String, PlatformDetails> monitoredPlatforms = new HashMap<>();

    final static String EGERIA_DEPLOYMENT_CATEGORY = "Egeria Deployment";


    /**
     * PlatformDetails acts as a cache of knowledge about a particular platform.
     */
    static class PlatformDetails
    {
        String                      platformRootURL       = null;
        String                      platformGUID          = null;
        String                      platformDisplayName   = null;
        OMAGServerPlatformConnector platformConnector     = null;

        Map<String, String>         knownServerNameToGUID = new HashMap<>();

        @Override
        public String toString()
        {
            return "PlatformDetails{" +
                    "platformRootURL='" + platformRootURL + '\'' +
                    ", platformGUID='" + platformGUID + '\'' +
                    ", platformDisplayName='" + platformDisplayName + '\'' +
                    ", platformConnector=" + platformConnector +
                    ", knownServerNameToGUID=" + knownServerNameToGUID +
                    '}';
        }
    }



    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            integrationContext.registerListener(this);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(OMAGConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
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
                                OMAGConnectorAuditCode.EGERIA_CONNECTOR_START.getMessageDefinition(connectorName,
                                                                                                   monitoredPlatforms.toString()));
        }

        try
        {
            AssetClient   assetClient   = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);
            SearchOptions searchOptions = assetClient.getSearchOptions(0, integrationContext.getMaxPageSize());

            /*
             * Ignore the templates
             */
            searchOptions.setSkipClassifiedElements(List.of(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName));

            List<OpenMetadataRootElement> softwarePlatforms = assetClient.findAssets(null, searchOptions);

            /*
             * This is the bootstrap situation - create a platform for the local metadata store.
             */
            if ((softwarePlatforms == null) || (softwarePlatforms.isEmpty()))
            {
                catalogPlatform(integrationContext.getMetadataAccessServerPlatformURLRoot());
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                 error.getClass().getName(),
                                                                                                 methodName,
                                                                                                 error.getMessage()));
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

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);
        try
        {
            /*
             * Ensure we have all the known OMAG Server Platforms
             */
            SearchOptions searchOptions = assetClient.getSearchOptions(0, integrationContext.getMaxPageSize());

            List<OpenMetadataRootElement> softwarePlatforms = assetClient.findAssets(null, searchOptions);

            while (softwarePlatforms != null)
            {
                for (OpenMetadataRootElement softwarePlatform : softwarePlatforms)
                {
                    if (softwarePlatform != null)
                    {
                        if (softwarePlatform.getProperties() instanceof SoftwareServerPlatformProperties softwareServerPlatformProperties)
                        {
                            this.assessElementForMonitoring(softwarePlatform.getElementHeader(),
                                                            softwareServerPlatformProperties.getDisplayName(),
                                                            softwareServerPlatformProperties.getDeployedImplementationType());
                        }
                    }
                }

                searchOptions.setStartFrom(searchOptions.getStartFrom() + integrationContext.getMaxPageSize());
                softwarePlatforms = assetClient.findAssets(null, searchOptions);
            }


            /*
             * The monitored platforms list has been built up from the platforms catalogued in open metadata.
             * Now it is time the catalog the servers running on these platforms.
             */
            processPlatforms();

            /*
             * Now all of the servers are catalogued and up to date, loop through them all and test that
             * the lineage relationships between them are still correct.
             */
            checkServerLineage();
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                 error.getClass().getName(),
                                                                                                 methodName,
                                                                                                 error.getMessage()));
        }
    }


    /**
     * Called each time an event that is published by the OMF, it is looking for Software Server Platforms
     * to add to monitoredPlatforms.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName))
        {
            String deployedImplementationType = propertyHelper.getStringProperty(connectorName,
                                                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                 event.getElementProperties(),
                                                                                 methodName);
            String displayName = propertyHelper.getStringProperty(connectorName,
                                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                                event.getElementProperties(),
                                                                methodName);

            assessElementForMonitoring(event.getElementHeader(),
                                       displayName,
                                       deployedImplementationType);
        }
    }


    /**
     * If the element is a software server platform, and it is not already being monitored then it is added to monitored platforms.
     *
     * @param elementHeader unique id nd type of element
     * @param displayName display name of platform
     * @param deployedImplementationType deployed implementation type
     */
    private synchronized void assessElementForMonitoring(ElementHeader elementHeader,
                                                         String        displayName,
                                                         String        deployedImplementationType)
    {
        final String methodName = "assessElementForMonitoring";

        if ((EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType().equals(deployedImplementationType)) &&
            (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName)) &&
                (!propertyHelper.isClassified(elementHeader, OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName)))
        {
            /*
             * Element is an OMAG Server Platform. Is this a new platform?
             */
            boolean alreadyMonitored = false;

            for (PlatformDetails platformDetails : monitoredPlatforms.values())
            {
                if (elementHeader.getGUID().equals(platformDetails.platformGUID))
                {
                    alreadyMonitored = true;
                    break;
                }
            }

            if (! alreadyMonitored)
            {
                try
                {
                    Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(elementHeader.getGUID(), auditLog);

                    if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
                    {
                        omagServerPlatformConnector.setDelegatingUserId(integrationContext.getMyUserId());
                        omagServerPlatformConnector.start();

                        PlatformDetails platformDetails = new PlatformDetails();

                        platformDetails.platformGUID        = elementHeader.getGUID();
                        platformDetails.platformConnector   = omagServerPlatformConnector;
                        platformDetails.platformDisplayName = displayName;
                        platformDetails.platformRootURL     = omagServerPlatformConnector.getConnection().getEndpoint().getNetworkAddress();

                        monitoredPlatforms.put(platformDetails.platformRootURL, platformDetails);
                    }
                }
                catch (Exception error)
                {
                    super.logExceptionRecord(methodName,
                                             OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                             error);
                }
            }
        }
    }


    /**
     * This is the point where the platforms are queried for servers.  The aim is to catalog each server only once,
     * even if it is deployed to multiple platforms.  Hence, the server does not have platform information
     * included in its properties.
     */
    private synchronized void processPlatforms()
    {
        final String methodName = "processPlatforms";

        for (PlatformDetails platformDetails : monitoredPlatforms.values())
        {
            if (platformDetails != null)
            {
                try
                {
                    /*
                     * Retrieve the actual live configuration and status of the platform and its servers.
                     * This is the ground truth that will be used to update the open metadata elements.
                     */
                    OMAGServerPlatformProperties platformProperties = platformDetails.platformConnector.getPlatformReport();

                    /*
                     * This is the list of known servers that the platform has run since starting.  Not all will
                     * necessarily be running now.
                     */
                    if (platformProperties.getOMAGServers() != null)
                    {
                        /*
                         * These maps are scoped by the platform, so it is ok to work with server names.
                         */
                        Map<String, OMAGServerProperties> knownServerMap   = new HashMap<>();
                        List<String>                      processedServers = new ArrayList<>();
                        String                            platformURLRoot  = null;

                        /*
                         * Extract the known servers into a map for convenient processing.
                         */
                        for (OMAGServerProperties omagServerProperties : platformProperties.getOMAGServers())
                        {
                            if (omagServerProperties != null)
                            {
                                knownServerMap.put(this.getServerResourceName(omagServerProperties.getServerName(),
                                                                              omagServerProperties.getOrganizationName()),
                                                   omagServerProperties);

                                /*
                                 * Check that the egeriaEndpoint has not changed.  This change is a typical precursor to connecting the
                                 * workspace into a cohort.  This means there will be multiple instances of this connector
                                 * running and the connections need to be updated away from local host.
                                 */
                                if (omagServerProperties instanceof OMAGMetadataStoreProperties omagMetadataStoreProperties)
                                {
                                    if ((omagMetadataStoreProperties.getRemoteRepositoryConnector() != null) &&
                                            (omagMetadataStoreProperties.getRemoteRepositoryConnector().getNetworkAddress() != null))
                                    {
                                         String[] split = omagMetadataStoreProperties.getRemoteRepositoryConnector().getNetworkAddress().split("/servers/");

                                         platformURLRoot = split[0];
                                    }
                                }
                            }
                        }

                        /*
                         * If egeriaEndpoint (in application.properties) has changed, update the monitored platform map.
                         * The subsequent cataloguing effort will fix the network addresses in the catalogued servers.
                         */
                        if ((platformURLRoot != null) && (! platformURLRoot.equals(platformDetails.platformRootURL)))
                        {
                            monitoredPlatforms.put(platformDetails.platformRootURL, null);
                            platformDetails.platformRootURL = platformURLRoot;
                            monitoredPlatforms.put(platformDetails.platformRootURL, platformDetails);
                            platformProperties.setPlatformURLRoot(platformURLRoot);
                        }

                        /*
                         * Retrieve the platform definition from the metadata repository.
                         */
                        AssetClient platformClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);
                        OpenMetadataRootElement platformElement = platformClient.getAssetByGUID(platformDetails.platformGUID,
                                                                                                platformClient.getGetOptions());

                        if ((platformElement != null) && (platformElement.getHostedITAssets() != null))
                        {
                            /*
                             * Before starting on the servers, check that the platform is up-to-date.
                             */
                            updatePlatform(platformProperties, platformElement);

                            /*
                             * Compare the known servers with the servers linked from the platform in the metadata server.
                             * These are updated and needed and are added to the processed server list,
                             */
                            for (RelatedMetadataElementSummary deploymentElement : platformElement.getHostedITAssets())
                            {
                                if ((deploymentElement != null) &&
                                        (deploymentElement.getRelatedElement().getProperties() instanceof AssetProperties assetProperties) &&
                                        (assetProperties.getResourceName() != null))
                                {
                                    /*
                                     * The resource name is used since it may include the organization name
                                     * with the server name if set up - this is important if multiple workspaces
                                     * are linked in a cohort - the same servers will be running, and they are
                                     * distinguished by the organization name - each egeria-workspaces should have a unique
                                     * organization name set up in both the application.properties and in the server config.
                                     */
                                    String serverResourceName = assetProperties.getResourceName();

                                    OMAGServerProperties omagServerProperties = knownServerMap.get(serverResourceName);

                                    /*
                                     * The catalogued server element is also known to the real platform, and so they can be synchronized.
                                     * Notice that we don't delete servers that are not recognized.  This is because servers are
                                     * dynamic and it may come back.  If a server is permanently removed from the platform,
                                     * then it should be manually removed from open metadata.
                                     */
                                    if (omagServerProperties != null)
                                    {
                                        AssetClient serverClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER.typeName);

                                        OpenMetadataRootElement serverElement = serverClient.getAssetByGUID(deploymentElement.getRelatedElement().getElementHeader().getGUID(),
                                                                                                            serverClient.getGetOptions());
                                        updateServer(omagServerProperties, platformProperties, deploymentElement.getRelatedElement().getElementHeader().getGUID(), serverElement.getEndpoint(), platformDetails);

                                        /*
                                         * The server name to GUID is saved to aid the management of lineage relationships between the servers.
                                         */
                                        platformDetails.knownServerNameToGUID.put(omagServerProperties.getServerName(), deploymentElement.getRelatedElement().getElementHeader().getGUID());

                                        processedServers.add(serverResourceName);
                                    }
                                }
                            }
                        }

                        /*
                         * Now go through the known servers that were not linked to the platform.
                         */
                        for (OMAGServerProperties omagServerProperties : knownServerMap.values())
                        {
                            if (omagServerProperties != null)
                            {
                                String qualifiedName = this.getServerQualifiedName(platformProperties.getPlatformURLRoot(),
                                                                                   omagServerProperties.getServerType(),
                                                                                   omagServerProperties.getServerName(),
                                                                                   omagServerProperties.getOrganizationName());

                                if (! processedServers.contains(this.getServerResourceName(omagServerProperties.getServerName(),
                                                                                           omagServerProperties.getOrganizationName())))
                                {
                                    /*
                                     * This is a new server.  Has it been catalogued before - maybe with a different platform?
                                     */
                                    OpenMetadataRootElement matchingServer = null;

                                    List<OpenMetadataRootElement> softwareServerElements = integrationContext.getAssetClient().getAssetsByName(qualifiedName, null);

                                    if (softwareServerElements != null)
                                    {
                                        for (OpenMetadataRootElement softwareServerElement : softwareServerElements)
                                        {
                                            if (softwareServerElement != null)
                                            {
                                                matchingServer = softwareServerElement;
                                                break;
                                            }
                                        }
                                    }

                                    /*
                                     * This server has not been catalogued before.
                                     */
                                    String matchingServerGUID = null;
                                    if (matchingServer == null)
                                    {
                                        matchingServerGUID = catalogServer(omagServerProperties, platformProperties, platformElement);


                                        /*
                                         * The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}
                                         */
                                        auditLog.logMessage(methodName,
                                                            OMAGConnectorAuditCode.NEW_SERVER.getMessageDefinition(connectorName,
                                                                                                                   omagServerProperties.getServerType(),
                                                                                                                   matchingServerGUID,
                                                                                                                   omagServerProperties.getServerName(),
                                                                                                                   platformProperties.getPlatformURLRoot()));
                                    }
                                    else
                                    {
                                        /*
                                         * The server has been catalogued before - but with a different platform.
                                         */
                                        matchingServerGUID = matchingServer.getElementHeader().getGUID();
                                        updateServer(omagServerProperties, platformProperties, matchingServerGUID, matchingServer.getEndpoint(), platformDetails);
                                    }

                                    platformDetails.knownServerNameToGUID.put(omagServerProperties.getServerName(), matchingServerGUID);

                                    /*
                                     * Now connect the server to the platform
                                     */
                                    integrationContext.getAssetClient().deployITAsset(matchingServerGUID,
                                                                                      platformDetails.platformGUID,
                                                                                      new MakeAnchorOptions(integrationContext.getAssetClient().getMetadataSourceOptions()),
                                                                                      null);
                                }


                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    super.logExceptionRecord(methodName,
                                             OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName + "::" + platformDetails.platformDisplayName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                             error);
                }
            }
        }
    }


    /**
     * Check that the lineage linkage between servers is correct.  This lineage uses the ProcessCall
     * lineage relationship to show which servers call which servers.
     *
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void checkServerLineage() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "checkServerLineage";

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER.typeName);

        int startFrom = 0;

        List<OpenMetadataRootElement> softwareServers = assetClient.findAssets(null,
                                                                               assetClient.getSearchOptions(startFrom,
                                                                                                            integrationContext.getMaxPageSize()));

        while (softwareServers != null)
        {
            for (OpenMetadataRootElement softwareServer : softwareServers)
            {
                if ((softwareServer != null) &&
                        (softwareServer.getProperties() instanceof SoftwareServerProperties softwareServerProperties) &&
                        (softwareServer.getElementHeader().getTemplate() == null))
                {
                    try
                    {
                        /*
                         * Not all servers are Egeria servers and so the deployedImplementationType is used
                         * to determine which servers to link.
                         */
                        // todo
                        if (EgeriaDeployedImplementationType.VIEW_SERVER.getDeployedImplementationType().equals(softwareServerProperties.getDeployedImplementationType()))
                        {
                            Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(softwareServer.getElementHeader().getGUID(), auditLog);

                            if (connector instanceof ViewServerConnector viewServerConnector)
                            {
                                viewServerConnector.setDelegatingUserId(integrationContext.getMyUserId());
                                viewServerConnector.start();

                                OMAGServerConfig serverConfig = viewServerConnector.getResolvedOMAGServerConfig();

                                viewServerConnector.disconnect();
                            }
                        }
                        else if (EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getDeployedImplementationType().equals(softwareServerProperties.getDeployedImplementationType()))
                        {
                            Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(softwareServer.getElementHeader().getGUID(), auditLog);

                            if (connector instanceof IntegrationDaemonConnector integrationDaemonConnector)
                            {
                                integrationDaemonConnector.setDelegatingUserId(integrationContext.getMyUserId());
                                integrationDaemonConnector.start();

                                OMAGServerConfig serverConfig = integrationDaemonConnector.getResolvedOMAGServerConfig();

                                integrationDaemonConnector.disconnect();
                            }
                        }
                        else if (EgeriaDeployedImplementationType.ENGINE_HOST.getDeployedImplementationType().equals(softwareServerProperties.getDeployedImplementationType()))
                        {
                            Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(softwareServer.getElementHeader().getGUID(), auditLog);

                            if (connector instanceof EngineHostConnector engineHostConnector)
                            {
                                engineHostConnector.setDelegatingUserId(integrationContext.getMyUserId());
                                engineHostConnector.start();

                                OMAGServerConfig serverConfig = engineHostConnector.getResolvedOMAGServerConfig();

                                engineHostConnector.disconnect();
                            }
                        }
                        else if (EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getDeployedImplementationType().equals(softwareServerProperties.getDeployedImplementationType()))
                        {
                            Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(softwareServer.getElementHeader().getGUID(), auditLog);

                            if (connector instanceof MetadataAccessServerConnector metadataAccessServerConnector)
                            {
                                metadataAccessServerConnector.setDelegatingUserId(integrationContext.getMyUserId());
                                metadataAccessServerConnector.start();

                                OMAGServerConfig serverConfig = metadataAccessServerConnector.getResolvedOMAGServerConfig();

                                metadataAccessServerConnector.disconnect();
                            }
                        }
                    }
                    catch (Exception error)
                    {
                        super.logExceptionRecord(methodName,
                                                 OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                 error);
                    }
                }
            }

            startFrom = startFrom + integrationContext.getMaxPageSize();
            softwareServers = assetClient.findAssets(null,
                                                     assetClient.getSearchOptions(startFrom,
                                                                                  integrationContext.getMaxPageSize()));
        }
    }

    /**
     * Construct the qualified name for a server.
     *
     * @param platformURLRoot network address of the platform
     * @param serverType type for the server
     * @param serverName unique name for the server.
     * @return composite qualified name
     */
    private String getServerQualifiedName(String platformURLRoot,
                                          String serverType,
                                          String serverName,
                                          String organizationName)
    {
        if ((organizationName == null) || (organizationName.isBlank()) || ("null".equals(organizationName)))
        {
            return serverType + "::" + platformURLRoot + "::" + serverName;
        }
        else
        {
            return serverType + "::" + platformURLRoot + "::" + organizationName + "." + serverName;
        }
    }


    /**
     * Construct the qualified name for a server.
     *
     * @param platformURLRoot network address of the platform
     * @param serverName unique name for the server.
     * @param organizationName owning organization
     * @return composite qualified name
     */
    private String getServerDisplayName(String platformURLRoot,
                                        String serverName,
                                        String organizationName)
    {
        if ((organizationName == null) || (organizationName.isBlank()) || ("null".equals(organizationName)))
        {
            return serverName + " on " + platformURLRoot;
        }
        else
        {
            return serverName + " from " + organizationName + " on " + platformURLRoot;
        }
    }


    /**
     * Construct the resource name for a server.
     *
     * @param organizationName owning organization
     * @param serverName unique name for the server.
     * @return composite qualified name
     */
    private String getServerResourceName(String serverName,
                                         String organizationName)
    {
        if ((organizationName == null) || (organizationName.isBlank()) || ("null".equals(organizationName)))
        {
            return serverName;
        }
        else
        {
            return organizationName + "." + serverName;
        }
    }


    /**
     * Return the templateGUID for the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getTemplateGUID(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.INTEGRATION_DAEMON_TEMPLATE.getTemplateGUID();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.ENGINE_HOST_TEMPLATE.getTemplateGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType) ||
                ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.METADATA_ACCESS_SERVER_TEMPLATE.getTemplateGUID();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.VIEW_SERVER_TEMPLATE.getTemplateGUID();
        }
        else
        {
            return EgeriaSoftwareServerTemplateDefinition.OMAG_SERVER_PLATFORM_TEMPLATE.getTemplateGUID();
        }
    }


    /**
     * Return the url for the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getURL(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/integration-daemon/";
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/engine-host/";
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/metadata-access-store/";
        }
        else if (ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/metadata-access-point/";
        }
        else if (ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/metadata-access-server/";
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/view-server/";
        }
        else
        {
            return "https://egeria-project.org/concepts/omag-server-platform/";
        }
    }



    /**
     * Return the unique identifier of the solution component for the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getSolutionComponentGUID(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return EgeriaSolutionComponent.INTEGRATION_DAEMON.getGUID();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return EgeriaSolutionComponent.ENGINE_HOST.getGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType) ||
                ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaSolutionComponent.METADATA_ACCESS_STORE.getGUID();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaSolutionComponent.VIEW_SERVER.getGUID();
        }

        return EgeriaSolutionComponent.SERVER_PLATFORM.getGUID();
    }



    /**
     * Create a metadata element to represent the local platform.
     *
     * @param platformURLRoot location of the platform
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void catalogPlatform(String platformURLRoot) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        String templateGUID = this.getTemplateGUID(null);

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getName(), platformURLRoot);
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_NAME.getName(), "Local OMAG Server Platform");
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_DESCRIPTION.getName(), "OMAG Server Platform running on " + platformURLRoot + ".");
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_USER_ID.getName(), null);
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), super.getSecretsLocation(SecretsStorePurpose.REST_BEARER_TOKEN.getName()));
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), super.getSecretsCollectionName(SecretsStorePurpose.REST_BEARER_TOKEN.getName()));
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(PlaceholderProperty.ORGANIZATION_NAME.getName(), null);

        String platformQualifiedName = EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformURLRoot;

        /*
         * Replacement properties are used to override the standard naming conventions for software servers and to
         * ensure this connector is able to match the operational configuration with the values in open metadata.
         * It is also an opportunity to test this feature.
         */
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               platformQualifiedName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CATEGORY.name,
                                                             EGERIA_DEPLOYMENT_CATEGORY);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.URL.name,
                                                             this.getURL(null));

        String platformGUID = openMetadataAccess.getMetadataElementFromTemplate(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                                                              null,
                                                                              true,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              templateGUID,
                                                                              elementProperties,
                                                                              placeholderProperties,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              false);

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        String solutionComponentGUID = this.getSolutionComponentGUID(null);

        if (solutionComponentGUID != null)
        {
            ImplementedByProperties implementedByProperties = new ImplementedByProperties();

            implementedByProperties.setRole("running instance");
            implementedByProperties.setDescription("Server instance discovered by " + connectorName + ".");

            governanceDefinitionClient.linkDesignToImplementation(solutionComponentGUID, platformGUID, new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()), implementedByProperties);
        }
    }


    /**
     * Update the platform element in open metadata with the latest values from the platform.
     *
     * @param platformProperties values extracted from the platform
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void updatePlatform(OMAGServerPlatformProperties platformProperties,
                                OpenMetadataRootElement      platformElement) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        SoftwareServerPlatformProperties softwareServerPlatformProperties = new SoftwareServerPlatformProperties();

        if (platformProperties.getPlatformOrganization() != null)
        {
            softwareServerPlatformProperties.setQualifiedName(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformProperties.getPlatformOrganization() + "::" + platformProperties.getPlatformURLRoot());
        }
        else
        {
            softwareServerPlatformProperties.setQualifiedName(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformProperties.getPlatformURLRoot());
        }

        softwareServerPlatformProperties.setDisplayName(platformProperties.getPlatformPublicProperties().getDisplayName());
        softwareServerPlatformProperties.setDescription(platformProperties.getPlatformPublicProperties().getDescription());
        softwareServerPlatformProperties.setURL(this.getURL(null));
        softwareServerPlatformProperties.setNamespace(platformProperties.getPlatformOrganization());
        softwareServerPlatformProperties.setVersionIdentifier(platformProperties.getPlatformBuildProperties().getVersion());
        softwareServerPlatformProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("organizationName", platformProperties.getPlatformPublicProperties().getOrganizationName());
        additionalProperties.put("buildTime", platformProperties.getPlatformBuildProperties().getTime().toString());

        softwareServerPlatformProperties.setAdditionalProperties(additionalProperties);

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        assetClient.updateAsset(platformElement.getElementHeader().getGUID(),
                                assetClient.getUpdateOptions(true),
                                softwareServerPlatformProperties);

        checkAndUpdateEndpoint(platformProperties.getPlatformURLRoot(), platformElement.getEndpoint());
    }


    /**
     * Create a metadata element to represent the server.
     *
     * @param omagServerProperties operational details of the server
     * @param platformProperties  operational details of the platform
     * @param platformElement metadata details of the platform
     * @return unique identifier of the server
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private String catalogServer(OMAGServerProperties         omagServerProperties,
                                 OMAGServerPlatformProperties platformProperties,
                                 OpenMetadataRootElement      platformElement) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "catalogServer";

        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        String templateGUID = this.getTemplateGUID(omagServerProperties.getServerType());

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getName(), platformProperties.getPlatformURLRoot());
        placeholderProperties.put(PlaceholderProperty.SERVER_NAME.getName(), omagServerProperties.getServerName());
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), this.getPlatformSecretsStorePathName(platformElement));
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), this.getPlatformSecretsStoreCollectionName(platformElement));
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), platformProperties.getPlatformOrigin());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), omagServerProperties.getDescription());
        placeholderProperties.put(PlaceholderProperty.CONNECTION_USER_ID.getName(), null);
        placeholderProperties.put(PlaceholderProperty.ORGANIZATION_NAME.getName(), omagServerProperties.getOrganizationName());

        String serverQualifiedName = this.getServerQualifiedName(platformProperties.getPlatformURLRoot(),
                                                                 omagServerProperties.getServerType(),
                                                                 omagServerProperties.getServerName(),
                                                                 omagServerProperties.getOrganizationName());
        /*
         * Replacement properties are used to override the standard naming conventions for software servers and to
         * ensure this connector is able to match the operational configuration with the values in open metadata.
         * It is also an opportunity to test this feature.
         */
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               serverQualifiedName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             this.getServerDisplayName(platformProperties.getPlatformURLRoot(),
                                                                                       omagServerProperties.getServerName(),
                                                                                       omagServerProperties.getOrganizationName()));

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             omagServerProperties.getServerType());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             this.getServerResourceName(omagServerProperties.getServerName(),
                                                                                        omagServerProperties.getOrganizationName()));

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CATEGORY.name,
                                                             EGERIA_DEPLOYMENT_CATEGORY);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.URL.name,
                                                             this.getURL(omagServerProperties.getServerType()));

        String serverGUID = openMetadataAccess.getMetadataElementFromTemplate(OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                                              null,
                                                                              true,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              templateGUID,
                                                                              elementProperties,
                                                                              placeholderProperties,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              false);



        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        String solutionComponentGUID = this.getSolutionComponentGUID(omagServerProperties.getServerType());

        if (solutionComponentGUID != null)
        {
            ImplementedByProperties implementedByProperties = new ImplementedByProperties();

            implementedByProperties.setRole("running instance");
            implementedByProperties.setDescription("Server instance discovered by " + connectorName + ".");

            governanceDefinitionClient.linkDesignToImplementation(solutionComponentGUID, serverGUID, new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()), implementedByProperties);
        }

        if (omagServerProperties instanceof OMAGMetadataStoreProperties omagMetadataStoreProperties)
        {
            if (omagMetadataStoreProperties.getLocalMetadataCollectionId() != null)
            {
                AssetClient              assetClient              = integrationContext.getAssetClient();
                SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient();

                InventoryCatalogProperties inventoryCatalogProperties = new InventoryCatalogProperties();

                inventoryCatalogProperties.setQualifiedName(OpenMetadataType.INVENTORY_CATALOG.typeName + "::" + serverQualifiedName + "_openMetadataRepository");
                inventoryCatalogProperties.setDisplayName("Local repository capability for server " + omagMetadataStoreProperties.getServerName() + ".");
                inventoryCatalogProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);

                NewElementOptions newElementOptions = new NewElementOptions(softwareCapabilityClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(serverGUID);
                newElementOptions.setParentGUID(serverGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

                String localRepositoryGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                               null,
                                                                                               inventoryCatalogProperties,
                                                                                               null);

                MetadataCollectionProperties metadataCollectionProperties = new MetadataCollectionProperties();

                metadataCollectionProperties.setQualifiedName(OpenMetadataType.METADATA_COLLECTION.typeName + "::" + omagMetadataStoreProperties.getServerName() + " [" + omagMetadataStoreProperties.getServerId() + "]");
                metadataCollectionProperties.setDeployedImplementationType(ElementOriginCategory.LOCAL_COHORT.getName());
                metadataCollectionProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);

                if (omagMetadataStoreProperties.getLocalMetadataCollectionName() != null)
                {
                    metadataCollectionProperties.setDisplayName(omagMetadataStoreProperties.getLocalMetadataCollectionName());
                }
                else
                {
                    metadataCollectionProperties.setDisplayName("Metadata collection for server " + omagMetadataStoreProperties.getServerName());
                }

                metadataCollectionProperties.setDescription("This is the metadata belonging to (homed on) server " + omagMetadataStoreProperties.getServerName() + ".");

                CapabilityAssetUseProperties capabilityAssetUseProperties = new CapabilityAssetUseProperties();

                capabilityAssetUseProperties.setUseType(CapabilityAssetUseType.OWNS);
                newElementOptions.setParentGUID(localRepositoryGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName);

                assetClient.createAsset(newElementOptions,
                                        null,
                                        metadataCollectionProperties,
                                        capabilityAssetUseProperties);
            }
            else
            {
                auditLog.logMessage(methodName, OMAGConnectorAuditCode.NULL_METADATA_COLLECTION_ID.getMessageDefinition(connectorName,
                                                                                                                        omagServerProperties.getServerName(),
                                                                                                                        omagServerProperties.getServerType()));
            }
        }

        addCohorts(serverGUID, serverQualifiedName, omagServerProperties, omagServerProperties.getCohorts());
        checkAndLinkGovernanceServerCapabilities(omagServerProperties, serverGUID);


        return serverGUID;
    }


    /**
     * Create a metadata element to represent the server.
     *
     * @param omagServerProperties operational details of the server
     * @param platformProperties  operational details of the platform
     * @param serverGUID metadata details of the server
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void updateServer(OMAGServerProperties          omagServerProperties,
                              OMAGServerPlatformProperties  platformProperties,
                              String                        serverGUID,
                              RelatedMetadataElementSummary endpoint,
                              PlatformDetails               platformDetails) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        SoftwareServerProperties softwareServerProperties = new SoftwareServerProperties();

        softwareServerProperties.setQualifiedName(this.getServerQualifiedName(platformProperties.getPlatformURLRoot(),
                                                                              omagServerProperties.getServerType(),
                                                                              omagServerProperties.getServerName(),
                                                                              omagServerProperties.getOrganizationName()));
        softwareServerProperties.setDisplayName(this.getServerDisplayName(platformProperties.getPlatformURLRoot(),
                                                                          omagServerProperties.getServerName(),
                                                                          omagServerProperties.getOrganizationName()));
        softwareServerProperties.setVersionIdentifier(platformProperties.getPlatformBuildProperties().getVersion());

        softwareServerProperties.setNamespace(omagServerProperties.getOrganizationName());
        softwareServerProperties.setDeployedImplementationType(omagServerProperties.getServerType());
        softwareServerProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);
        softwareServerProperties.setURL(this.getURL(omagServerProperties.getServerType()));

        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("organizationName", omagServerProperties.getOrganizationName());
        additionalProperties.put("serverId", omagServerProperties.getServerId());
        additionalProperties.put("serverName", omagServerProperties.getServerName());

        softwareServerProperties.setAdditionalProperties(additionalProperties);

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER.typeName);

        assetClient.updateAsset(serverGUID,
                                assetClient.getUpdateOptions(true),
                                softwareServerProperties);

        checkAndUpdateEndpoint(platformDetails.platformRootURL, endpoint);

        checkAndLinkGovernanceServerCapabilities(omagServerProperties, serverGUID);
    }


    /**
     * Integration Daemons and Engine Hosts are controlled by SoftwareCapability entities.  This method connects the
     * server definitions to the configured software capabilities.  It is using calls to the live servers
     * to extract the list of running capabilities so only make the calls if the servers are running.
     *
     *
     * @param omagServerProperties properties from the live server
     * @param serverGUID unique identifier of the metadata element for the server
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void checkAndLinkGovernanceServerCapabilities(OMAGServerProperties omagServerProperties,
                                                          String               serverGUID) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        if (omagServerProperties.getServerActiveStatus() == ServerActiveStatus.RUNNING)
        {
            /*
             * Engine hosts are connected to governance engines.
             */
            if (omagServerProperties instanceof OMAGEngineHostProperties engineHostProperties)
            {
                AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER.typeName);

                OpenMetadataRootElement serverElement = assetClient.getAssetByGUID(serverGUID, assetClient.getGetOptions());

                List<String> processedEngines = new ArrayList<>();

                if (serverElement != null)
                {
                    /*
                     * First identify the server capabilities that are already connected to the server element
                     * - that those that should not be connected to the server element because they are longer defined.
                     */
                    if (serverElement.getCapabilities() != null)
                    {
                        for (RelatedMetadataElementSummary softwareCapability : serverElement.getCapabilities())
                        {
                            /*
                             * Ignore software capabilities that are not governance engines.
                             */
                            if ((softwareCapability != null) && (propertyHelper.isTypeOf(softwareCapability.getRelatedElement().getElementHeader(),
                                                                                         OpenMetadataType.GOVERNANCE_ENGINE.typeName)))
                            {
                                /*
                                 * Loop through the running engines to see if it matches the linked governance engine.
                                 */
                                if (engineHostProperties.getGovernanceEngineSummaries() != null)
                                {
                                    for (GovernanceEngineSummary governanceEngineSummary : engineHostProperties.getGovernanceEngineSummaries())
                                    {
                                        if ((governanceEngineSummary != null) &&
                                                (governanceEngineSummary.getGovernanceEngineGUID() != null) &&
                                                (governanceEngineSummary.getGovernanceEngineGUID().equals(softwareCapability.getRelatedElement().getElementHeader().getGUID())))
                                        {
                                            /*
                                             * All is right - remember that this engine is processed.
                                             */
                                            processedEngines.add(governanceEngineSummary.getGovernanceEngineGUID());
                                        }
                                    }
                                }

                                if (!processedEngines.contains(softwareCapability.getRelatedElement().getElementHeader().getGUID()))
                                {
                                    /*
                                     * The software capability is no longer running.
                                     */
                                    assetClient.detachSoftwareCapability(serverElement.getElementHeader().getGUID(), softwareCapability.getRelatedElement().getElementHeader().getGUID(), assetClient.getDeleteOptions(false));
                                }
                            }
                        }
                    }

                    /*
                     * Attach the software capabilities that are missing from the server element.
                     */
                    if (engineHostProperties.getGovernanceEngineSummaries() != null)
                    {
                        for (GovernanceEngineSummary governanceEngineSummary : engineHostProperties.getGovernanceEngineSummaries())
                        {
                            if ((governanceEngineSummary != null) &&
                                    (governanceEngineSummary.getGovernanceEngineGUID() != null) &&
                                    (!processedEngines.contains(governanceEngineSummary.getGovernanceEngineGUID())))
                            {
                                assetClient.linkSoftwareCapability(serverGUID, governanceEngineSummary.getGovernanceEngineGUID(), new MakeAnchorOptions(assetClient.getMetadataSourceOptions()), null);

                                processedEngines.add(governanceEngineSummary.getGovernanceEngineGUID());
                            }
                        }
                    }
                }
            }

            /*
             * Integration Daemons are connected to integration groups.
             */
            else if (omagServerProperties instanceof OMAGIntegrationDaemonProperties omagIntegrationDaemonProperties)
            {
                AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER.typeName);

                OpenMetadataRootElement serverElement = assetClient.getAssetByGUID(serverGUID, assetClient.getGetOptions());

                List<String> processedGroups = new ArrayList<>();

                if (serverElement != null)
                {
                    /*
                     * First identify the server capabilities that are already connected to the server element
                     * - that those that should not be connected to the server element because they are longer defined.
                     */
                    if (serverElement.getCapabilities() != null)
                    {
                        for (RelatedMetadataElementSummary softwareCapability : serverElement.getCapabilities())
                        {
                            /*
                             * Ignore software capabilities that are not governance engines.
                             */
                            if ((softwareCapability != null) && (propertyHelper.isTypeOf(softwareCapability.getRelatedElement().getElementHeader(),
                                                                                         OpenMetadataType.INTEGRATION_GROUP.typeName)))
                            {
                                /*
                                 * Loop through the running engines to see if it matches the linked governance engine.
                                 */
                                if (omagIntegrationDaemonProperties.getIntegrationGroups() != null)
                                {
                                    for (OMAGIntegrationGroupProperties integrationGroupProperties : omagIntegrationDaemonProperties.getIntegrationGroups())
                                    {
                                        if ((integrationGroupProperties != null) &&
                                                (integrationGroupProperties.getIntegrationGroupGUID() != null) &&
                                                (integrationGroupProperties.getIntegrationGroupGUID().equals(softwareCapability.getRelatedElement().getElementHeader().getGUID())))
                                        {
                                            /*
                                             * All is right - remember that this engine is processed.
                                             */
                                            processedGroups.add(integrationGroupProperties.getIntegrationGroupGUID());
                                        }
                                    }
                                }

                                if (!processedGroups.contains(softwareCapability.getRelatedElement().getElementHeader().getGUID()))
                                {
                                    /*
                                     * The software capability is no longer running.
                                     */
                                    assetClient.detachSoftwareCapability(serverElement.getElementHeader().getGUID(), softwareCapability.getRelatedElement().getElementHeader().getGUID(), assetClient.getDeleteOptions(false));
                                }
                            }
                        }
                    }

                    /*
                     * Attach the software capabilities that are missing from the server element.
                     */
                    if (omagIntegrationDaemonProperties.getIntegrationGroups() != null)
                    {
                        for (OMAGIntegrationGroupProperties integrationGroupProperties : omagIntegrationDaemonProperties.getIntegrationGroups())
                        {
                            if ((integrationGroupProperties != null) &&
                                    (integrationGroupProperties.getIntegrationGroupGUID() != null) &&
                                    (!processedGroups.contains(integrationGroupProperties.getIntegrationGroupGUID())))
                            {
                                assetClient.linkSoftwareCapability(serverGUID, integrationGroupProperties.getIntegrationGroupGUID(), new MakeAnchorOptions(assetClient.getMetadataSourceOptions()), null);

                                processedGroups.add(integrationGroupProperties.getIntegrationGroupGUID());
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Check that the endpoint matches the platform URL root - update it if it does not.
     *
     * @param platformURLRoot latest platformURL root (probably from egeriaEndpoint in application.properties
     * @param endpoint current endpoint element
     *
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void checkAndUpdateEndpoint(String                        platformURLRoot,
                                        RelatedMetadataElementSummary endpoint) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        if ((endpoint != null) && (endpoint.getRelatedElement().getProperties() instanceof EndpointProperties endpointProperties))
        {
            if (! platformURLRoot.equals(endpointProperties.getNetworkAddress()))
            {
                EndpointClient endpointClient = integrationContext.getEndpointClient();

                EndpointProperties properties = new EndpointProperties();

                endpointProperties.setNetworkAddress(platformURLRoot);

                endpointClient.updateEndpoint(endpoint.getRelatedElement().getElementHeader().getGUID(),
                                              endpointClient.getUpdateOptions(true),
                                              properties);
            }
        }
    }


    /**
     * Link the server to all of the cohorts it is a member of.
     *
     * @param serverGUID unique identifier of the entity for the server
     * @param serverQualifiedName qualified name of the server
     * @param omagServerProperties description of the server
     * @param cohorts list of cohorts that this server is a member of
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void addCohorts(String                      serverGUID,
                            String                      serverQualifiedName,
                            OMAGServerProperties        omagServerProperties,
                            List<OMAGCohortProperties>  cohorts) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        SoftwareCapabilityClient       softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient();
        MetadataRepositoryCohortClient cohortClient             = integrationContext.getMetadataRepositoryCohortClient();

        if (cohorts != null)
        {
            for (OMAGCohortProperties cohort : cohorts)
            {
                OpenMetadataRootElement cohortElement = null;
                String                  cohortGUID;

                List<OpenMetadataRootElement> cohortElements = cohortClient.getMetadataRepositoryCohortsByName(cohort.getCohortName(), cohortClient.getQueryOptions());

                if (cohortElements != null)
                {
                    for (OpenMetadataRootElement element : cohortElements)
                    {
                        if (element != null)
                        {
                            cohortElement = element;
                            break;
                        }
                    }
                }

                if (cohortElement != null)
                {
                    cohortGUID = cohortElement.getElementHeader().getGUID();

                    if (cohortElement.getCohortMembership() != null)
                    {
                        for (RelatedMetadataElementSummary cohortMember : cohortElement.getCohortMembership())
                        {
                            if (cohortMember != null)
                            {
                                if (cohortMember.getRelatedElement().getElementHeader().getGUID().equals(serverGUID))
                                {
                                    /*
                                     * This cohort is registered.
                                     */
                                    break;
                                }
                            }
                        }
                    }
                }
                else
                {
                    NewElementOptions newElementOptions = new NewElementOptions(cohortClient.getMetadataSourceOptions());

                    newElementOptions.setIsOwnAnchor(true);

                    MetadataRepositoryCohortProperties cohortProperties = new MetadataRepositoryCohortProperties();

                    cohortProperties.setQualifiedName(OpenMetadataType.METADATA_REPOSITORY_COHORT.typeName + "::" + cohort.getCohortName());
                    cohortProperties.setDisplayName(cohort.getCohortName());
                    cohortProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);
                    cohortProperties.setCohortTopics(this.getAllNetworkAddresses(cohort.getConnectors(), "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider"));

                    cohortGUID = cohortClient.createMetadataRepositoryCohort(newElementOptions,
                                                                             null,
                                                                             cohortProperties,
                                                                             null);
                }

                /*
                 * The server's membership of the cohort is represented by a CohortMember software capability linked to
                 * the cohort.  There is one software capability for each cohort so the qualified name includes
                 * both the server name and the cohort.
                 */
                CohortMemberProperties cohortMemberProperties = new CohortMemberProperties();

                cohortMemberProperties.setQualifiedName(OpenMetadataType.COHORT_MEMBER.typeName + "::" + serverQualifiedName + "_cohortMember_" + cohort.getCohortName());
                cohortMemberProperties.setDisplayName("Cohort member of " + cohort.getCohortName() + " for server " + omagServerProperties.getServerName() + ".");
                cohortMemberProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);

                NewElementOptions newElementOptions = new NewElementOptions(softwareCapabilityClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(serverGUID);
                newElementOptions.setParentGUID(serverGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

                String cohortMemberGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                            null,
                                                                                            cohortMemberProperties,
                                                                                            null);

                /*
                 * Connect the cohort member to the cohort.
                 */
                MetadataCohortPeerProperties metadataCohortPeerProperties = null;

                if (cohort.getLocalRegistration() != null)
                {
                    metadataCohortPeerProperties = new MetadataCohortPeerProperties();
                    metadataCohortPeerProperties.setRegistrationDate(cohort.getLocalRegistration().getRegistrationTime());
                }

                cohortClient.linkCohortToMember(cohortGUID, cohortMemberGUID, new MakeAnchorOptions(cohortClient.getMetadataSourceOptions()), metadataCohortPeerProperties);
            }
        }
    }


    /**
     * Return all of the network addresses for a set of nested connectors.
     *
     * @param connectors list of nested connectors
     * @return list of network addresses
     */
    private List<String> getAllNetworkAddresses(List<OMAGConnectorProperties>  connectors,
                                                String                         connectorType)
    {
        List<String> networkAddresses = new ArrayList<>();

        if (connectors != null)
        {
            for (OMAGConnectorProperties connector : connectors)
            {
                if (connector != null)
                {
                    if ((connector.getNetworkAddress() != null) &&
                            ((connectorType == null) ||
                                    ((connector.getConnectorType()) != null && (connectorType.equals(connector.getConnectorType().getConnectorProviderClassName())))))
                    {
                        networkAddresses.add(connector.getNetworkAddress());
                    }

                    List<String> nestedAddresses = this.getAllNetworkAddresses(connector.getNestedConnectors(), connectorType);

                    if (nestedAddresses != null)
                    {
                        networkAddresses.addAll(nestedAddresses);
                    }
                }
            }
        }

        if (! networkAddresses.isEmpty())
        {
            return networkAddresses;
        }

        return null;
    }


    /**
     * Locate the platforms security secrets store.
     *
     * @param platformElement platform description
     * @return path name or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private String getPlatformSecretsStorePathName(OpenMetadataRootElement platformElement) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        /*
         * It is possible that the platform has its own secrets store.  This is configured in its connection's
         * configuration properties. There is probably only one connection but the code allows for multiple
         * and returns the first secrets store path name it finds.
         */
        if ((platformElement != null) && (platformElement.getConnections() != null))
        {
            for (RelatedMetadataElementSummary connection : platformElement.getConnections())
            {
                if ((connection != null) &&
                        (connection.getRelatedElement().getProperties() instanceof ConnectionProperties connectionProperties) &&
                        (connectionProperties.getConfigurationProperties() != null) &&
                        (connectionProperties.getConfigurationProperties().get(PlaceholderProperty.SECRETS_STORE.getName()) != null))
                {
                    return connectionProperties.getConfigurationProperties().get(PlaceholderProperty.SECRETS_STORE.getName()).toString();
                }
            }
        }

        /*
         * The platform does not have its secrets store explicitly defined so use the one provided to this connector.
         */
        return super.getStringConfigurationProperty(OMAGServerPlatformConfigurationProperty.SECRETS_STORE.getName(), connectionBean.getConfigurationProperties());
    }



    /**
     * Locate the platforms security secrets store collection.
     *
     * @param platformElement platform description
     * @return path name or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private String getPlatformSecretsStoreCollectionName(OpenMetadataRootElement platformElement) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        /*
         * It is possible that the platform has its own secrets store.  This is configured in its connection's
         * configuration properties. There is probably only one connection but the code allows for multiple
         * and returns the first secrets store path name it finds.
         */
        if ((platformElement != null) && (platformElement.getConnections() != null))
        {
            for (RelatedMetadataElementSummary connection : platformElement.getConnections())
            {
                if ((connection != null) &&
                        (connection.getRelatedElement().getProperties() instanceof ConnectionProperties connectionProperties) &&
                        (connectionProperties.getConfigurationProperties() != null) &&
                        (connectionProperties.getConfigurationProperties().get(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName()) != null))
                {
                    return connectionProperties.getConfigurationProperties().get(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName()).toString();
                }
            }
        }

        /*
         * The platform does not have its secrets store explicitly defined so use the one provided to this connector.
         */
        return super.getStringConfigurationProperty(OMAGServerPlatformConfigurationProperty.SECRETS_STORE.getName(), connectionBean.getConfigurationProperties());
    }
}
