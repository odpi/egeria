/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaDeployedImplementationType;
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
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
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
     * This is the point where the platforms are queried for servers.  The aim is to catalog all servers only once,
     * irrespective of how many platforms it is deployed to.  Hence, the server does no have platform information
     * included in its properties.
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
                        /*
                         * These maps are scoped by the platform so it is ok to work with server names.
                         */
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
                        AssetClient platformClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);
                        OpenMetadataRootElement platformElement = platformClient.getAssetByGUID(platformDetails.platformGUID,
                                                                                                platformClient.getGetOptions());

                        if ((platformElement != null) && (platformElement.getHostedITAssets() != null))
                        {
                            for (RelatedMetadataElementSummary deploymentElement : platformElement.getHostedITAssets())
                            {
                                if ((deploymentElement != null) &&
                                        (deploymentElement.getRelatedElement().getProperties() instanceof AssetProperties assetProperties) &&
                                        (assetProperties.getDisplayName() != null))
                                {
                                    String serverDisplayName = assetProperties.getDisplayName();

                                    OMAGServerProperties omagServerProperties = knownServerMap.get(serverDisplayName);

                                    if (omagServerProperties != null)
                                    {
                                        processedServers.add(serverDisplayName);
                                    }
                                }
                            }
                        }

                        /*
                         * Now do through the known servers that were not linked to the platform.
                         */
                        for (OMAGServerProperties omagServerProperties : knownServerMap.values())
                        {
                            if (omagServerProperties != null)
                            {
                                String qualifiedName = this.getServerQualifiedName(omagServerProperties.getServerType(),
                                                                                   omagServerProperties.getServerId(),
                                                                                   omagServerProperties.getServerName());

                                if (! processedServers.contains(omagServerProperties.getServerName()))
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
     * @param serverName unique name for the server.
     * @return composite qualified name
     */
    private String getServerQualifiedName(String serverType,
                                          String serverId,
                                          String serverName)
    {
        return serverType + "::" + this.getServerResourceName(serverId, serverName);
    }


    /**
     * Construct the resource name for a server.
     *
     * @param serverId unique identifier for the server
     * @param serverName unique name for the server.
     * @return composite qualified name
     */
    private String getServerResourceName(String serverId,
                                         String serverName)
    {
        return  serverName + " [" + serverId + "]";
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
        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        String templateGUID = this.getTemplateGUID(omagServerProperties.getServerType());

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), platformProperties.getPlatformURLRoot());
        placeholderProperties.put(PlaceholderProperty.SERVER_NAME.getName(), omagServerProperties.getServerName());
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), this.getSecretsStorePathName(platformElement));
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), omagServerProperties.getServerType() + "::" + omagServerProperties.getServerName());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), platformProperties.getPlatformOrigin());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), omagServerProperties.getDescription());
        placeholderProperties.put(PlaceholderProperty.CONNECTION_USER_ID.getName(), omagServerProperties.getUserId());

        /*
         * Replacement properties are used to override the standard naming conventions for software servers and to
         * ensure this connector is able to match the operational configuration with the values in open metadata.
         * It is also an opportunity to test this feature.
         */
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               this.getServerQualifiedName(omagServerProperties.getServerType(),
                                                                                                           omagServerProperties.getServerId(),
                                                                                                           omagServerProperties.getServerName()));

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             omagServerProperties.getServerName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             omagServerProperties.getServerType());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             this.getServerResourceName(omagServerProperties.getServerId(), omagServerProperties.getServerName()));

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CATEGORY.name,
                                                             EGERIA_DEPLOYMENT_CATEGORY);

        return openMetadataAccess.getMetadataElementFromTemplate(OpenMetadataType.SOFTWARE_SERVER.typeName,
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
    }



    private String getSecretsStorePathName(OpenMetadataRootElement platformElement) throws InvalidParameterException,
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
}
