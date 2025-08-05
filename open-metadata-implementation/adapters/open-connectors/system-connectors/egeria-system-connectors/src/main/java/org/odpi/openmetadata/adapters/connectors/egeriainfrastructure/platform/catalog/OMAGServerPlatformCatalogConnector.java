/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.OMAGServerPlatformProperties;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.OMAGServerProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OMAGServerPlatformCatalogConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    List<PlatformDetails> monitoredPlatforms = new ArrayList<>();
    String                clientUserId = null;


    /**
     * PlatformDetails acts as a cache of knowledge about a particular platform.
     */
    static class PlatformDetails
    {
        String                      platformRootURL       = null;
        String                      platformGUID          = null;
        String                      platformDisplayName   = null;
        OMAGServerPlatformConnector platformConnector     = null;

        @Override
        public String toString()
        {
            return "PlatformDetails{" +
                    ", platformGUID='" + platformGUID + '\'' +
                    ", platformDisplayName='" + platformDisplayName + '\'' +
                    '}';
        }
    }



    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        /*
         * This is the user id to call the Egeria OMAG Server Platforms to extract information from.
         */
        if (connectionBean.getUserId() != null)
        {
            clientUserId = connectionBean.getUserId();
        }
        else
        {
            throw new ConnectorCheckedException(OMAGConnectorErrorCode.NULL_CLIENT_USER_ID.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

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
                                                                                                   clientUserId,
                                                                                                   monitoredPlatforms.toString()));
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

        AssetClient assetClient = integrationContext.getAssetClient();
        try
        {
            /*
             * Ensure we have all the known OMAG Server Platforms
             */
            SearchOptions searchOptions = assetClient.getSearchOptions(0, integrationContext.getMaxPageSize());
            searchOptions.setMetadataElementTypeName(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

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

            for (PlatformDetails platformDetails : monitoredPlatforms)
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
                        omagServerPlatformConnector.start();

                        PlatformDetails platformDetails = new PlatformDetails();

                        platformDetails.platformGUID        = elementHeader.getGUID();
                        platformDetails.platformConnector   = omagServerPlatformConnector;
                        platformDetails.platformDisplayName = displayName;
                        platformDetails.platformRootURL     = omagServerPlatformConnector.getConnection().getEndpoint().getAddress();

                        monitoredPlatforms.add(platformDetails);
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
     * This is the point where the platforms are queried for servers.
     */
    private synchronized void processPlatforms()
    {
        final String methodName = "processPlatforms";

        for (PlatformDetails platformDetails : monitoredPlatforms)
        {
            if (platformDetails != null)
            {
                try
                {
                    OMAGServerPlatformProperties platformProperties = platformDetails.platformConnector.getPlatformReport();

                    if (platformProperties.getOMAGServers() != null)
                    {
                        Map<String, OMAGServerProperties> knownServerMap   = new HashMap<>();
                        List<String>                      processedServers = new ArrayList<>();

                        /*
                         * Extract the known servers into a map for convenient processing.
                         */
                        for (OMAGServerProperties omagServerProperties : platformProperties.getOMAGServers())
                        {
                            if (omagServerProperties != null)
                            {
                                knownServerMap.put(omagServerProperties.getServerName(), omagServerProperties);
                            }
                        }

                        /*
                         * Compare the known servers with the servers linked from the platform in the metadata server.
                         * These are assumed to be correct and are added to the processed server list,
                         */
                        int startFrom = 0;
                        List<OpenMetadataRootElement> deployedSoftwareServers = integrationContext.getAssetClient().getDeployedITAssets(platformDetails.platformGUID, null);

                        while (deployedSoftwareServers != null)
                        {
                            for (OpenMetadataRootElement deploymentElement : deployedSoftwareServers)
                            {
                                if ((deploymentElement != null) &&
                                        (deploymentElement.getProperties() instanceof AssetProperties assetProperties) &&
                                        (assetProperties.getDisplayName() != null))
                                {
                                    OMAGServerProperties omagServerProperties = knownServerMap.get(assetProperties.getDisplayName());

                                    if (omagServerProperties != null)
                                    {
                                        processedServers.add(assetProperties.getDisplayName());
                                    }
                                }
                            }

                            startFrom               = startFrom + integrationContext.getMaxPageSize();
                            deployedSoftwareServers = integrationContext.getAssetClient().getDeployedITAssets(platformDetails.platformGUID, null);
                        }

                        /*
                         * Now do through the known servers that were not linked to the platform.
                         */
                        for (OMAGServerProperties omagServerProperties : knownServerMap.values())
                        {
                            if (omagServerProperties != null)
                            {
                                String qualifiedName = this.getServerQualifiedName(omagServerProperties.getServerId(), omagServerProperties.getServerName());

                                if (!processedServers.contains(omagServerProperties.getServerName()))
                                {
                                    /*
                                     * This is a new server.  Has it been catalogued before - maybe with a different platform?
                                     */
                                    String matchingServerGUID = null;

                                    List<OpenMetadataRootElement> softwareServerElements = integrationContext.getAssetClient().getAssetsByName(qualifiedName, null);

                                    if (softwareServerElements != null)
                                    {
                                        for (OpenMetadataRootElement softwareServerElement : softwareServerElements)
                                        {
                                            if (softwareServerElement != null)
                                            {
                                                matchingServerGUID = softwareServerElement.getElementHeader().getGUID();
                                                break;
                                            }
                                        }
                                    }

                                    /*
                                     * This server has not been catalogued before.
                                     */
                                    if (matchingServerGUID == null)
                                    {
                                        matchingServerGUID = catalogServer(omagServerProperties, platformProperties);


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

                                    /*
                                     * Now connect the server to the platform
                                     */
                                    integrationContext.getAssetClient().deployITAsset(matchingServerGUID,
                                                                                      platformDetails.platformGUID,
                                                                                      integrationContext.getAssetClient().getMetadataSourceOptions(),
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
     * Create a metadata element to represent the server.
     *
     * @param omagServerProperties details of the server
     * @param platformProperties details of the platform
     * @return unique identifier of the server
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private String catalogServer(OMAGServerProperties         omagServerProperties,
                                 OMAGServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        String templateGUID = this.getTemplateGUID(omagServerProperties.getServerType());

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), platformProperties.getPlatformURLRoot());
        placeholderProperties.put(PlaceholderProperty.SERVER_NAME.getName(), omagServerProperties.getServerName());
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), super.getStringConfigurationProperty(OMAGServerPlatformConfigurationProperty.SECRETS_STORE.getName(), connectionBean.getConfigurationProperties()));
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), super.getStringConfigurationProperty(OMAGServerPlatformConfigurationProperty.SECRETS_STORE.getName(), connectionBean.getConfigurationProperties()));
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), platformProperties.getPlatformOrigin());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), omagServerProperties.getDescription());
        placeholderProperties.put(PlaceholderProperty.CONNECTION_USER_ID.getName(), omagServerProperties.getUserId());

        return openMetadataAccess.getMetadataElementFromTemplate(OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                                 null,
                                                                 true,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 templateGUID,
                                                                 null,
                                                                 placeholderProperties,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 false);
    }
}
