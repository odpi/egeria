/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.ITInfrastructureEventListener;
import org.odpi.openmetadata.accessservices.itinfrastructure.events.ITInfrastructureOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.OMAGServerPlatformProperties;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.OMAGServerProperties;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.infrastructure.connector.InfrastructureIntegratorConnector;

import java.util.*;

public class OMAGServerPlatformCatalogConnector extends InfrastructureIntegratorConnector implements ITInfrastructureEventListener
{
    List<PlatformDetails> monitoredPlatforms = new ArrayList<>();
    String                clientUserId = "garygeeke";


    final IntegrationDaemonProvider    integrationDaemonProvider    = new IntegrationDaemonProvider();
    final EngineHostProvider           engineHostProvider           = new EngineHostProvider();
    final MetadataAccessServerProvider metadataAccessServerProvider = new MetadataAccessServerProvider();
    final ViewServerProvider           viewServerProvider           = new ViewServerProvider();
    final OMAGServerProvider           omagServerProvider           = new OMAGServerProvider();


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

        try
        {
            /*
             * Ensure we have all the known OMAG Server Platforms
             */
            int startFrom = 0;

            List<SoftwareServerPlatformElement> softwarePlatforms = super.getContext().findSoftwareServerPlatforms(".*",
                                                                                                                   new Date(),
                                                                                                                   startFrom,
                                                                                                                   super.getContext().getMaxPageSize());

            while (softwarePlatforms != null)
            {
                for (SoftwareServerPlatformElement softwarePlatform : softwarePlatforms)
                {
                    if (softwarePlatform != null)
                    {
                        this.assessElementForMonitoring(softwarePlatform.getElementHeader(),
                                                        softwarePlatform.getProperties().getName(),
                                                        softwarePlatform.getProperties().getDeployedImplementationType());
                    }
                }

                startFrom = startFrom + super.getContext().getMaxPageSize();
                softwarePlatforms = super.getContext().findSoftwareServerPlatforms(".*",
                                                                                   new Date(),
                                                                                   startFrom,
                                                                                   super.getContext().getMaxPageSize());
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

            throw new ConnectorCheckedException(OMAGConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Called each time an event that is published by the IT Infrastructure OMAS, it is looking for Software Server Platforms to add to monitoredPlatforms.
     */
    @Override
    public void processEvent(ITInfrastructureOutTopicEvent event)
    {
        if ((event.getElementProperties() != null) &&
            (event.getElementProperties().get(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name) != null))
        {
            Object nameProperty = event.getElementProperties().get(OpenMetadataProperty.NAME.name);

            if (nameProperty == null)
            {
                assessElementForMonitoring(event.getElementHeader(),
                                           null,
                                           event.getElementProperties().get(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name).toString());
            }
            else
            {
                assessElementForMonitoring(event.getElementHeader(),
                                           nameProperty.toString(),
                                           event.getElementProperties().get(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name).toString());
            }
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

        if ((DeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType().equals(deployedImplementationType)) &&
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
                    Connector connector = super.getContext().getConnectedAssetContext().getConnectorToAsset(elementHeader.getGUID());

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
                    auditLog.logException(methodName,
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
     *
     * @throws Exception unable to connector to platform
     */
    private synchronized void processPlatforms() throws Exception
    {
        for (PlatformDetails platformDetails : monitoredPlatforms)
        {
            if (platformDetails != null)
            {
                OMAGServerPlatformProperties platformProperties = platformDetails.platformConnector.getPlatformReport();

                if (platformProperties.getOMAGServers() != null)
                {
                    Map<String, OMAGServerProperties> knownServerMap = new HashMap<>();
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
                    List<DeploymentElement> deployedSoftwareServers = super.getContext().getDeployedITAssets(platformDetails.platformGUID,
                                                                                                               new Date(),
                                                                                                               startFrom,
                                                                                                               super.getContext().getMaxPageSize());

                    while (deployedSoftwareServers != null)
                    {
                        for (DeploymentElement deploymentElement : deployedSoftwareServers)
                        {
                            if ((deploymentElement != null) &&
                                    (deploymentElement.getAssetElement() != null) &&
                                    (deploymentElement.getAssetElement().getProperties() != null) &&
                                    (deploymentElement.getAssetElement().getProperties().getName() != null))
                            {
                                OMAGServerProperties omagServerProperties = knownServerMap.get(deploymentElement.getAssetElement().getProperties().getName());

                                if (omagServerProperties != null)
                                {
                                    processedServers.add(deploymentElement.getAssetElement().getProperties().getName());
                                }
                            }
                        }

                        startFrom = startFrom + super.getContext().getMaxPageSize();
                        deployedSoftwareServers = super.getContext().getDeployedITAssets(platformDetails.platformGUID,
                                                                                         new Date(),
                                                                                         startFrom,
                                                                                         super.getContext().getMaxPageSize());
                    }

                    /*
                     * Now do through the known servers that were not linked to the platform.
                     */
                    for (OMAGServerProperties omagServerProperties : knownServerMap.values())
                    {
                        if (omagServerProperties != null)
                        {
                            String qualifiedName = this.getServerQualifiedName(omagServerProperties.getServerId(), omagServerProperties.getServerName());

                            if (! processedServers.contains(omagServerProperties.getServerName()))
                            {
                                /*
                                 * This is a new server.  Has it been catalogued before - maybe with a different platform?
                                 */
                                String matchingServerGUID = null;

                                List<SoftwareServerElement> softwareServerElements = super.getContext().getSoftwareServersByName(qualifiedName,
                                                                                                                                 null,
                                                                                                                                 0,
                                                                                                                                 0);

                                if (softwareServerElements != null)
                                {
                                    for (SoftwareServerElement softwareServerElement : softwareServerElements)
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
                                }

                                /*
                                 * Now connect the server to the platform
                                 */
                                super.getContext().deployITAsset(matchingServerGUID, platformDetails.platformGUID, null);
                            }
                        }
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
     * @param omagServerProperties server configuration
     * @param platformOrigin version of the platform
     * @return properties
     */
    private SoftwareServerProperties getSoftwareServerProperties(OMAGServerProperties omagServerProperties,
                                                                 String               platformOrigin)
    {
        SoftwareServerProperties softwareServerProperties = new SoftwareServerProperties();

        softwareServerProperties.setQualifiedName(this.getServerQualifiedName(omagServerProperties.getServerId(), omagServerProperties.getServerName()));
        softwareServerProperties.setName(omagServerProperties.getServerName());
        softwareServerProperties.setResourceDescription(omagServerProperties.getDescription());
        softwareServerProperties.setSoftwareServerSource("Egeria Community");
        softwareServerProperties.setSoftwareServerVersion(platformOrigin);
        softwareServerProperties.setSoftwareServerUserId(omagServerProperties.getUserId());
        softwareServerProperties.setDeployedImplementationType(this.getServerDeployedImplementationType(omagServerProperties.getServerType()));

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("organizationName", omagServerProperties.getOrganizationName());
        additionalProperties.put("serverId", omagServerProperties.getServerId());
        additionalProperties.put("maxPageSize", Integer.toString(omagServerProperties.getMaxPageSize()));
        additionalProperties.put("serverType", omagServerProperties.getServerType());

        softwareServerProperties.setAdditionalProperties(additionalProperties);

        return softwareServerProperties;
    }


    /**
     * Return the deployed implementation type for the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getServerDeployedImplementationType(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return DeployedImplementationType.INTEGRATION_DAEMON.getDeployedImplementationType();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return DeployedImplementationType.ENGINE_HOST.getDeployedImplementationType();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType) ||
                 ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                 ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return DeployedImplementationType.METADATA_ACCESS_SERVER.getDeployedImplementationType();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return DeployedImplementationType.VIEW_SERVER.getDeployedImplementationType();
        }
        else
        {
            return DeployedImplementationType.OMAG_SERVER.getDeployedImplementationType();
        }
    }


    /**
     * Return the appropriate connector type GUID for this type of server.
     *
     * @param serverType server type
     * @return unique identifier of the template
     */
    private String getServerConnectorTypeGUID(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return integrationDaemonProvider.getConnectorType().getGUID();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return engineHostProvider.getConnectorType().getGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType) ||
                 ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                 ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return metadataAccessServerProvider.getConnectorType().getGUID();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return viewServerProvider.getConnectorType().getGUID();
        }
        else
        {
            return omagServerProvider.getConnectorType().getGUID();
        }
    }


    private String catalogServer(OMAGServerProperties         omagServerProperties,
                                 OMAGServerPlatformProperties platformProperties) throws ConnectorCheckedException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        SoftwareServerProperties softwareServerProperties = this.getSoftwareServerProperties(omagServerProperties,
                                                                                             platformProperties.getPlatformOrigin());


        EndpointProperties endpointProperties = new EndpointProperties();

        endpointProperties.setQualifiedName(softwareServerProperties.getQualifiedName() + "_endpoint");
        endpointProperties.setName(softwareServerProperties.getName() + " endpoint");
        endpointProperties.setAddress(platformProperties.getPlatformURLRoot());

        ConnectionProperties serverConnectionProperties = new ConnectionProperties();

        serverConnectionProperties.setQualifiedName(softwareServerProperties.getQualifiedName() + "_connection");
        serverConnectionProperties.setDisplayName(softwareServerProperties.getName() + " connection");

        Map<String,Object> configProperties = new HashMap<>();
        configProperties.put(OMAGServerPlatformConfigurationProperty.SERVER_NAME.getName(), omagServerProperties.getServerName());
        serverConnectionProperties.setConfigurationProperties(configProperties);
        serverConnectionProperties.setUserId(connectionProperties.getUserId()); // default userId

        String serverGUID     = super.getContext().createSoftwareServer(softwareServerProperties);
        String connectionGUID = super.getContext().createConnection(serverConnectionProperties);
        String endpointGUID   = super.getContext().createEndpoint(serverGUID, endpointProperties);

        super.getContext().setupEndpoint(connectionGUID, endpointGUID);
        super.getContext().setupConnectorType(connectionGUID, this.getServerConnectorTypeGUID(omagServerProperties.getServerType()));
        super.getContext().setupAssetConnection(serverGUID, null, connectionGUID);

        return serverGUID;
    }
}
