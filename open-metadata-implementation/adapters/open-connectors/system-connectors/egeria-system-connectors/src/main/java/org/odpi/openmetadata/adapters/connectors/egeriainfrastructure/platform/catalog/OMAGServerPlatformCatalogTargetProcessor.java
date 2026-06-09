/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaInformationSupplyChainDefinition;
import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.EgeriaSolutionComponent;
import org.odpi.openmetadata.adapters.connectors.ExceptionTypeDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.*;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerConnector;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ExceptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityListMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;

import java.util.*;

public class OMAGServerPlatformCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    Map<String, String>         knownServerNameToGUID = new HashMap<>();

    final static String EGERIA_DEPLOYMENT_CATEGORY = "Egeria Deployment";

    final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    final OpenMetadataRelationshipBuilder   relationshipBuilder   = new OpenMetadataRelationshipBuilder();

    /**
     * Principle constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public OMAGServerPlatformCatalogTargetProcessor(CatalogTarget        template,
                                                    CatalogTargetContext catalogTargetContext,
                                                    Connector            connectorToTarget,
                                                    String               connectorName,
                                                    AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

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

        /*
         * Record the start to show correct handover to the catalog target(s).
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OMAGConnectorAuditCode.EGERIA_TARGET_START.getMessageDefinition(connectorName, getCatalogTargetName()));
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
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        /*
         * Record the refresh
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OMAGConnectorAuditCode.EGERIA_TARGET_REFRESH.getMessageDefinition(connectorName, getCatalogTargetName()));
        }

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        try
        {
            /*
             * Get the latest metadata for the platform.
             */
            OpenMetadataRootElement platformElement = assetClient.getAssetByGUID(this.getCatalogTargetElement().getElementHeader().getGUID(),
                                                                                 assetClient.getGetOptions());

            if ((platformElement != null) && (platformElement.getProperties() instanceof SoftwareServerPlatformProperties softwareServerPlatformProperties))
            {
                /*
                 * Now create a connector to the platform to extract is current configuration and status.
                 */
                Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(platformElement.getElementHeader().getGUID(), auditLog);

                if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
                {
                    omagServerPlatformConnector.setDelegatingUserId(integrationContext.getMyUserId());
                    omagServerPlatformConnector.start();

                    /*
                     * Retrieve the actual live configuration and status of the platform and its servers.
                     * This is the ground truth that will be used to update the open metadata elements.
                     */
                    OMAGServerPlatformProperties platformProperties = omagServerPlatformConnector.getPlatformReport();

                    if (platformProperties.getPlatformSecurityConnection() != null)
                    {
                        catalogUsers(platformElement,
                                     softwareServerPlatformProperties.getQualifiedName(),
                                     platformProperties.getPlatformSecurityConnection(),
                                     omagServerPlatformConnector);
                    }

                    /*
                     * This is the list of known servers that the platform has run since starting.  Not all will
                     * necessarily be running now.
                     */
                    if (platformProperties.getOMAGServers() != null)
                    {
                        /*
                         * These maps are scoped by the platform, so it is OK to work with server names.
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
                                knownServerMap.put(this.getServerResourceName(omagServerProperties.getServerName(),
                                                                              omagServerProperties.getOrganizationName()),
                                                   omagServerProperties);
                            }
                        }

                        /*
                         * Before starting on the servers, check that the platform is up to date.
                         */
                        updatePlatform(platformProperties, platformElement);

                        /*
                         * What is catalogued already?
                         */
                        if (platformElement.getHostedITAssets() != null)
                        {
                            /*
                             * Compare the known servers with the servers linked from the platform in the metadata server.
                             * These are updated and needed and are added to the processed server list.
                             */
                            for (RelatedMetadataElementSummary deploymentElement : platformElement.getHostedITAssets())
                            {
                                if ((deploymentElement != null) &&
                                        (deploymentElement.getRelatedElement().getProperties() instanceof AssetProperties assetProperties) &&
                                        (assetProperties.getResourceName() != null))
                                {
                                    /*
                                     * The resource name is used since it may include the organization name along with
                                     * the server name if set up. This is important if multiple workspaces
                                     * are linked in a cohort.  In this circumstance, the same servers will be running, and they are
                                     * distinguished by the organization name. As such, each egeria-workspaces deployment should have a unique
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
                                        updateServer(omagServerProperties, platformProperties, deploymentElement.getRelatedElement().getElementHeader().getGUID(), serverElement.getEndpoint());

                                        /*
                                         * The server name to GUID is saved to aid the management of lineage relationships between the servers.
                                         */
                                        knownServerNameToGUID.put(omagServerProperties.getServerName(), deploymentElement.getRelatedElement().getElementHeader().getGUID());

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
                                    String matchingServerGUID;
                                    if (matchingServer == null)
                                    {
                                        matchingServerGUID = catalogServer(omagServerProperties, platformProperties, platformElement);


                                        /*
                                         * The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}
                                         */
                                        if (auditLog != null)
                                        {
                                            auditLog.logMessage(methodName,
                                                                OMAGConnectorAuditCode.NEW_SERVER.getMessageDefinition(connectorName,
                                                                                                                       omagServerProperties.getServerType(),
                                                                                                                       matchingServerGUID,
                                                                                                                       omagServerProperties.getServerName(),
                                                                                                                       platformProperties.getPlatformURLRoot()));
                                        }
                                    }
                                    else
                                    {
                                        /*
                                         * The server has been catalogued before - but with a different platform.
                                         */
                                        matchingServerGUID = matchingServer.getElementHeader().getGUID();
                                        updateServer(omagServerProperties, platformProperties, matchingServerGUID, matchingServer.getEndpoint());
                                    }

                                    knownServerNameToGUID.put(omagServerProperties.getServerName(), matchingServerGUID);

                                    /*
                                     * Now connect the server to the platform
                                     */
                                    integrationContext.getAssetClient().deployITAsset(matchingServerGUID,
                                                                                      platformElement.getElementHeader().getGUID(),
                                                                                      new MakeAnchorOptions(integrationContext.getAssetClient().getMetadataSourceOptions()),
                                                                                      null);
                                }


                            }
                        }
                    }

                    omagServerPlatformConnector.disconnect();

                    /*
                     * Now all the servers are catalogued and up to date, loop through them all and test that
                     * the lineage relationships between them are still correct.
                     */
                    checkServerLineage();
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                     error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     error.getMessage()));
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
        SearchOptions searchOptions = assetClient.getSearchOptions(startFrom, integrationContext.getMaxPageSize());
        searchOptions.setSkipClassifiedElements(List.of(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName));
        List<OpenMetadataRootElement> softwareServers = assetClient.findAssets(null, searchOptions);

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
                         * Not all servers are Egeria servers, and so the deployedImplementationType is used
                         * to determine which servers to link.
                         */
                        // Todo link servers together
                        if (EgeriaDeployedImplementationType.VIEW_SERVER.getDeployedImplementationType().equals(softwareServerProperties.getDeployedImplementationType()))
                        {
                            Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(softwareServer.getElementHeader().getGUID(), auditLog);

                            if (connector instanceof ViewServerConnector viewServerConnector)
                            {
                                viewServerConnector.setDelegatingUserId(integrationContext.getMyUserId());
                                viewServerConnector.start();

                                OMAGServerConfig serverConfig = viewServerConnector.getResolvedOMAGServerConfig();

                                if (serverConfig != null)
                                {
                                    catalogAuditLogConnector(serverConfig, softwareServer);
                                }

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

                                if (serverConfig != null)
                                {
                                    catalogAuditLogConnector(serverConfig, softwareServer);
                                }

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

                                if (serverConfig != null)
                                {
                                    catalogAuditLogConnector(serverConfig, softwareServer);
                                }

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
                                if (serverConfig != null)
                                {
                                    catalogAuditLogConnector(serverConfig, softwareServer);
                                }

                                metadataAccessServerConnector.disconnect();
                            }
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

            startFrom = startFrom + integrationContext.getMaxPageSize();
            searchOptions.setStartFrom(startFrom);
            softwareServers = assetClient.findAssets(null, searchOptions);
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
     * Catalog users from the platform security connector.
     *
     * @param platformElement current platform element
     * @param platformQualifiedName unique name of the platform
     * @param platformSecurityConnection platform security connector
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException property server exception
     * @throws UserNotAuthorizedException user not authorized exception
     */
    private void catalogUsers(OpenMetadataRootElement     platformElement,
                              String                      platformQualifiedName,
                              OMAGConnectorProperties     platformSecurityConnection,
                              OMAGServerPlatformConnector omagServerPlatformConnector) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        String secretsCollectionGUID = this.getPlatformSecretsCollectionGUID(platformElement, platformQualifiedName, platformSecurityConnection);

        List<String> userIdentityList = new ArrayList<>();

        OpenMetadataStore  openMetadataStore  = integrationContext.getOpenMetadataStore();
        UserIdentityClient userIdentityClient = integrationContext.getUserIdentityClient();
        SearchOptions searchOptions = userIdentityClient.getSearchOptions(0, integrationContext.getMaxPageSize());
        List<OpenMetadataRootElement> userElements = userIdentityClient.findUserIdentities(null, searchOptions);

        /*
         * Iterate through the user identity elements and add a SecurityListClassification to each one that is defined in the secrets store.
         */
        while (userElements != null)
        {
            for (OpenMetadataRootElement userElement : userElements)
            {
                if ((userElement != null) && (userElement.getProperties() instanceof UserIdentityProperties userIdentityProperties))
                {
                    userIdentityList.add(userIdentityProperties.getUserId());

                    UserAccount userAccount = omagServerPlatformConnector.getUserAccount(userIdentityProperties.getUserId());

                    if (userAccount != null)
                    {
                        SecurityListMembershipProperties properties = new SecurityListMembershipProperties();

                        properties.setSecurityGroups(userAccount.getSecurityGroups());
                        properties.setSecurityRoles(userAccount.getSecurityRoles());

                        if (userElement.getElementHeader().getSecurityListMembership() != null)
                        {
                            openMetadataStore.reclassifyMetadataElementInStore(userElement.getElementHeader().getGUID(),
                                                                               OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                               openMetadataStore.getUpdateOptions(false),
                                                                               classificationBuilder.getNewElementProperties(properties));
                        }
                        else
                        {
                            openMetadataStore.classifyMetadataElementInStore(userElement.getElementHeader().getGUID(),
                                                                           OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                           openMetadataStore.getMetadataSourceOptions(),
                                                                           classificationBuilder.getNewElementProperties(properties));
                        }

                        if (secretsCollectionGUID != null)
                        {
                            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.USER_ACCOUNT_RELATIONSHIP.typeName,
                                                                           secretsCollectionGUID,
                                                                           userElement.getElementHeader().getGUID(),
                                                                           openMetadataStore.getMakeAnchorOptions(false),
                                                                           null);
                        }
                    }
                    else
                    {
                        if (userElement.getElementHeader().getSecurityListMembership() != null)
                        {
                            openMetadataStore.declassifyMetadataElementInStore(userElement.getElementHeader().getGUID(),
                                                                               OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                               openMetadataStore.getMetadataSourceOptions());
                        }

                        /*
                         * Create or update an exception for the userIdentity
                         */
                        RelatedMetadataElementSummary noUserAccountException = null;
                        if (userElement.getExceptions() != null)
                        {
                            for (RelatedMetadataElementSummary userException : userElement.getExceptions())
                            {
                                if ((userException != null) && (userException.getRelatedElement().getElementHeader().getGUID().equals(ExceptionTypeDefinition.MISSING_USER_ACCOUNT.getGUID())))
                                {
                                    noUserAccountException = userException;
                                }
                            }
                        }

                        ExceptionProperties exceptionProperties = new ExceptionProperties();

                        exceptionProperties.setLabel(userIdentityProperties.getUserId());
                        exceptionProperties.setDescription("No user account found for user " + userIdentityProperties.getUserId() + " in OMAG server platform " + platformElement.getElementHeader().getGUID());
                        exceptionProperties.setLastReviewTime(new Date());
                        exceptionProperties.setReviewDate(integrationContext.getNextScheduledRefreshTime());

                        if (noUserAccountException == null)
                        {
                            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName,
                                                                           userElement.getElementHeader().getGUID(),
                                                                           ExceptionTypeDefinition.MISSING_USER_ACCOUNT.getGUID(),
                                                                           openMetadataStore.getMakeAnchorOptions(false),
                                                                           relationshipBuilder.getNewElementProperties(exceptionProperties));
                        }
                        else
                        {
                            openMetadataStore.updateRelatedElementsInStore(noUserAccountException.getRelationshipHeader().getGUID(),
                                                                           openMetadataStore.getUpdateOptions(true),
                                                                           relationshipBuilder.getNewElementProperties(exceptionProperties));
                        }
                    }
                }
            }

            searchOptions.setStartFrom(searchOptions.getStartFrom() + integrationContext.getMaxPageSize());

            userElements = userIdentityClient.findUserIdentities(null, searchOptions);
        }

        List<String> userList = omagServerPlatformConnector.getUserList(UserAccountStatus.AVAILABLE, null);

        if (userList != null)
        {
            for (String userId : userList)
            {
                if (userId != null)
                {
                    if (! userIdentityList.contains(userId))
                    {
                        /*
                         * The user does not have a UserIdentity element - this may indicate that there is an unnecessary
                         * userAccount.
                         */
                        ExceptionProperties exceptionProperties = new ExceptionProperties();

                        exceptionProperties.setLabel(userId);
                        exceptionProperties.setDescription("No user identity found for user " + userId + " in OMAG server platform " + platformElement.getElementHeader().getGUID());
                        exceptionProperties.setLastReviewTime(new Date());
                        exceptionProperties.setReviewDate(integrationContext.getNextScheduledRefreshTime());

                        /*
                         * Create or update an exception for the userIdentity
                         */
                        RelatedMetadataElementSummary noUserIdentityException = null;
                        if (platformElement.getExceptions() != null)
                        {
                            for (RelatedMetadataElementSummary userException : platformElement.getExceptions())
                            {
                                if ((userException != null) &&
                                        (userException.getRelatedElement().getElementHeader().getGUID().equals(ExceptionTypeDefinition.MISSING_USER_IDENTITY.getGUID())) &&
                                        (userException.getRelationshipProperties() instanceof ExceptionProperties properties) &&
                                        (userId.equals(properties.getLabel())))
                                {
                                    noUserIdentityException = userException;
                                }
                            }
                        }

                        if (noUserIdentityException == null)
                        {
                            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName,
                                                                           platformElement.getElementHeader().getGUID(),
                                                                           ExceptionTypeDefinition.MISSING_USER_IDENTITY.getGUID(),
                                                                           openMetadataStore.getMakeAnchorOptions(false),
                                                                           relationshipBuilder.getNewElementProperties(exceptionProperties));
                        }
                        else
                        {
                            openMetadataStore.updateRelatedElementsInStore(noUserIdentityException.getRelationshipHeader().getGUID(),
                                                                           openMetadataStore.getUpdateOptions(true),
                                                                           relationshipBuilder.getNewElementProperties(exceptionProperties));
                        }
                    }

                    OpenMetadataUserAccount userAccount = omagServerPlatformConnector.getUserAccount(userId);

                    if ((userAccount.getSecretNames() != null) && (userAccount.getSecretNames().contains(SecretsStoreCollectionProperty.CLEAR_PASSWORD.name)))
                    {
                        /*
                         * The user does not have a UserIdentity element - this may indicate that there is an unnecessary
                         * userAccount.
                         */
                        ExceptionProperties exceptionProperties = new ExceptionProperties();

                        exceptionProperties.setLabel(userId);
                        exceptionProperties.setDescription("User " + userId + " in OMAG server platform " + platformElement.getElementHeader().getGUID() + " has a clear password in the user directory.");
                        exceptionProperties.setLastReviewTime(new Date());
                        exceptionProperties.setReviewDate(integrationContext.getNextScheduledRefreshTime());

                        /*
                         * Create or update an exception for the userIdentity
                         */
                        RelatedMetadataElementSummary cleatTextPasswordException = null;
                        if (platformElement.getExceptions() != null)
                        {
                            for (RelatedMetadataElementSummary userException : platformElement.getExceptions())
                            {
                                if ((userException != null) &&
                                        (userException.getRelatedElement().getElementHeader().getGUID().equals(ExceptionTypeDefinition.CLEAR_TEXT_PASSWORD.getGUID())) &&
                                        (userException.getRelationshipProperties() instanceof ExceptionProperties properties) &&
                                        (userId.equals(properties.getLabel())))
                                {
                                    cleatTextPasswordException = userException;
                                }
                            }
                        }

                        if (cleatTextPasswordException == null)
                        {
                            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName,
                                                                           platformElement.getElementHeader().getGUID(),
                                                                           ExceptionTypeDefinition.CLEAR_TEXT_PASSWORD.getGUID(),
                                                                           openMetadataStore.getMakeAnchorOptions(false),
                                                                           relationshipBuilder.getNewElementProperties(exceptionProperties));
                        }
                        else
                        {
                            openMetadataStore.updateRelatedElementsInStore(cleatTextPasswordException.getRelationshipHeader().getGUID(),
                                                                           openMetadataStore.getUpdateOptions(true),
                                                                           relationshipBuilder.getNewElementProperties(exceptionProperties));
                        }
                    }
                }
            }
        }
    }


    /**
     * Navigate to the secrets collection through the platform security connector.
     *
     * @param platformElement current platform element
     * @param platformQualifiedName unique name of the platform
     * @param platformSecurityConnection platform security connector
     * @return secretesCollectionGUID (could be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException metadata store error
     * @throws UserNotAuthorizedException authorization error
     */
    private String getPlatformSecretsCollectionGUID(OpenMetadataRootElement platformElement,
                                                    String                  platformQualifiedName,
                                                    OMAGConnectorProperties platformSecurityConnection) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if ((platformSecurityConnection != null) && (platformSecurityConnection.getNestedConnectors() != null))
        {
            for (OMAGConnectorProperties nestedConnector : platformSecurityConnection.getNestedConnectors())
            {
                if ((nestedConnector != null) && (nestedConnector.getConfigurationProperties() != null))
                {
                    String secretsStoreFileName = nestedConnector.getNetworkAddress();
                    Object secretsStoreCollectionName = nestedConnector.getConfigurationProperties().get(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName());
                    String secretsCollectionGUID = null;

                    if ((secretsStoreCollectionName != null) && (secretsStoreFileName != null))
                    {
                        AssetClient fileClient = integrationContext.getAssetClient(OpenMetadataType.KEY_STORE_FILE.typeName);

                        List<OpenMetadataRootElement> secretsFiles = fileClient.findAssets(secretsStoreFileName,
                                                                                           fileClient.getSearchOptions());

                        if (secretsFiles != null)
                        {
                            for (OpenMetadataRootElement secretsFile : secretsFiles)
                            {
                                if ((secretsFile != null) && (secretsFile.getSupportedDataSets() != null))
                                {
                                    for (RelatedMetadataElementSummary dataSet : secretsFile.getSupportedDataSets())
                                    {
                                        if ((dataSet != null) &&
                                                (propertyHelper.isTypeOf(dataSet.getRelatedElement().getElementHeader(), OpenMetadataType.SECRETS_COLLECTION.typeName)) &&
                                                (dataSet.getRelatedElement().getProperties() instanceof SecretsCollectionProperties secretsCollectionProperties))
                                        {
                                            if (secretsStoreCollectionName.equals(secretsCollectionProperties.getResourceName()))
                                            {
                                                secretsCollectionGUID = dataSet.getRelatedElement().getElementHeader().getGUID();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    /*
                     * Attach the secrets collection to the platform.
                     */
                    this.attachSecretsCollectionToPlatform(platformElement, platformQualifiedName, secretsCollectionGUID);

                    return secretsCollectionGUID;
                }
            }
        }

        return null;
    }


    /**
     * Link the secrets connection to the platform via the user authentication and authorization managers.
     *
     * @param platformElement current platform element
     * @param platformQualifiedName unique name of the platform
     * @param secretsCollectionGUID unique identifier of the secrets collection (maybe null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException metadata store error
     * @throws UserNotAuthorizedException authorization error
     */
    private void attachSecretsCollectionToPlatform(OpenMetadataRootElement platformElement,
                                                   String                  platformQualifiedName,
                                                   String                  secretsCollectionGUID) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient();

        String userAuthenticationManagerGUID = null;
        String userAuthorizationManagerGUID = null;

        if ((platformElement.getCapabilities() != null) && (platformElement.getProperties() instanceof SoftwareServerPlatformProperties softwareServerPlatformProperties))
        {
            for (RelatedMetadataElementSummary capability : platformElement.getCapabilities())
            {
                if (capability != null)
                {
                    if (propertyHelper.isTypeOf(capability.getRelatedElement().getElementHeader(), OpenMetadataType.USER_AUTHENTICATION_MANAGER.typeName))
                    {
                        userAuthenticationManagerGUID = capability.getRelatedElement().getElementHeader().getGUID();
                    }
                    else if (propertyHelper.isTypeOf(capability.getRelatedElement().getElementHeader(), OpenMetadataType.AUTHORIZATION_MANAGER.typeName))
                    {
                        userAuthorizationManagerGUID = capability.getRelatedElement().getElementHeader().getGUID();
                    }
                }
            }
        }

        /*
         * Set up an authentication manager
         */
        if (userAuthenticationManagerGUID == null)
        {
            UserAuthenticationManagerProperties properties = new UserAuthenticationManagerProperties();

            properties.setQualifiedName(platformQualifiedName + "_UserTokenManager");
            properties.setDisplayName("User Token Manager");
            properties.setDescription("Manages user logons and generates bearer tokens for the platform.");
            properties.setDeployedImplementationType(DeployedImplementationType.USER_AUTHENTICATION_MANAGER.getDeployedImplementationType());
            properties.setDeploymentStatus(DeploymentStatus.ACTIVE);

            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setAnchorGUID(platformElement.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(platformElement.getElementHeader().getGUID());
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);

            userAuthenticationManagerGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                              null,
                                                                                              properties,
                                                                                              null);
        }

        /*
         * Set up an authorization manager
         */
        if (userAuthorizationManagerGUID == null)
        {
            AuthorizationManagerProperties properties = new AuthorizationManagerProperties();

            properties.setQualifiedName(platformQualifiedName + "_UserAccessManager");
            properties.setDisplayName("User Access Manager");
            properties.setDescription("Manages authorization requests for the platform.");
            properties.setDeployedImplementationType(DeployedImplementationType.PLATFORM_SECURITY_CONNECTOR.getDeployedImplementationType());
            properties.setURL(DeployedImplementationType.PLATFORM_SECURITY_CONNECTOR.getWikiLink());
            properties.setDeploymentStatus(DeploymentStatus.ACTIVE);

            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setAnchorGUID(platformElement.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(platformElement.getElementHeader().getGUID());
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);

            userAuthorizationManagerGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                             null,
                                                                                             properties,
                                                                                             null);
        }

        if (secretsCollectionGUID != null)
        {
            CapabilityAssetUseProperties properties = new CapabilityAssetUseProperties();
            properties.setUseType(CapabilityAssetUseType.USES);
            softwareCapabilityClient.addCapabilityAssetUse(userAuthenticationManagerGUID,
                                                           secretsCollectionGUID,
                                                           softwareCapabilityClient.getMakeAnchorOptions(false),
                                                           properties);

            softwareCapabilityClient.addCapabilityAssetUse(userAuthorizationManagerGUID,
                                                           secretsCollectionGUID,
                                                           softwareCapabilityClient.getMakeAnchorOptions(false),
                                                           properties);
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
     * Return the url of the server.
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
            return EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.ENGINE_HOST.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                 ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponentGUID();
        }

        return EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getSolutionComponentGUID();
    }


    /**
     * Update the platform element in open metadata with the latest values from the platform.
     *
     * @param platformProperties values extracted from the platform
     * @param platformElement    element in open metadata
     * @throws InvalidParameterException  invalid parameter
     * @throws PropertyServerException    no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void updatePlatform(OMAGServerPlatformProperties platformProperties,
                                OpenMetadataRootElement      platformElement) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        /*
         * Note: platformProperties.getPlatformOrganization() return null if the organizationName is null in application.properties whereas platformProperties.getPlatformPublicProperties().getOrganizationName() returns an empty string.
         * platformProperties.getPlatformName() always returns "OMAG Server Platform" whereas platformProperties.getPlatformPublicProperties().getDisplayName() returns the platform.name in application.properties.
         */
        SoftwareServerPlatformProperties softwareServerPlatformProperties = new SoftwareServerPlatformProperties();

        String platformDisplayName = platformProperties.getPlatformPublicProperties().getDisplayName();
        String platformOrganization = platformProperties.getPlatformPublicProperties().getOrganizationName();

        if (platformOrganization.isBlank())
        {
            platformOrganization = null;
        }


        if ((platformDisplayName == null) || (platformDisplayName.isBlank()))
        {
            platformDisplayName = platformProperties.getDefaultPlatformName();
        }

        if (platformOrganization != null)
        {
            softwareServerPlatformProperties.setQualifiedName(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformOrganization + "::" + platformDisplayName);
            softwareServerPlatformProperties.setResourceName(platformOrganization + "." + platformDisplayName);
        }
        else
        {
            softwareServerPlatformProperties.setQualifiedName(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformDisplayName);
            softwareServerPlatformProperties.setResourceName(platformDisplayName);
        }

        softwareServerPlatformProperties.setDisplayName(platformDisplayName);
        softwareServerPlatformProperties.setDescription(platformProperties.getPlatformPublicProperties().getDescription());
        softwareServerPlatformProperties.setURL(this.getURL(null));
        softwareServerPlatformProperties.setIdentifier(platformProperties.getDefaultPlatformName());
        softwareServerPlatformProperties.setNamespacePath(platformOrganization);
        softwareServerPlatformProperties.setVersionIdentifier(platformProperties.getPlatformBuildProperties().getVersion());
        softwareServerPlatformProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);
        softwareServerPlatformProperties.setDeploymentStatus(DeploymentStatus.ACTIVE);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("platformURLRoot", platformProperties.getPlatformURLRoot());
        additionalProperties.put("organizationName", platformProperties.getPlatformPublicProperties().getOrganizationName());
        additionalProperties.put("buildTime", platformProperties.getPlatformBuildProperties().getTime().toString());

        softwareServerPlatformProperties.setAdditionalProperties(additionalProperties);

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        assetClient.updateAsset(platformElement.getElementHeader().getGUID(),
                                assetClient.getUpdateOptions(true),
                                softwareServerPlatformProperties);

        if (platformElement.getServerEndpoints() != null)
        {
            for (RelatedMetadataElementSummary endpoint : platformElement.getServerEndpoints())
            {
                if (endpoint != null)
                {
                    checkAndUpdateEndpoint(platformProperties.getPlatformURLRoot(), endpoint);
                }
            }
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
        final String methodName = "catalogServer";

        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        String templateGUID = this.getTemplateGUID(omagServerProperties.getServerType());

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getName(), platformProperties.getPlatformURLRoot());
        placeholderProperties.put(PlaceholderProperty.SERVER_NAME.getName(), omagServerProperties.getServerName());
        placeholderProperties.put(PlaceholderProperty.IDENTIFIER.getName(), omagServerProperties.getServerName());
        placeholderProperties.put(PlaceholderProperty.RESOURCE_NAME.getName(), getServerResourceName(omagServerProperties.getServerName(), omagServerProperties.getOrganizationName()));
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
                                                             OpenMetadataProperty.IDENTIFIER.name,
                                                             omagServerProperties.getServerName());

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
                                                                              null,
                                                                              placeholderProperties,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              false);

        /*
         * Allow this connector to override values from the template.
         */
        updateServer(omagServerProperties, platformProperties, serverGUID, null);

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
                inventoryCatalogProperties.setDeploymentStatus(DeploymentStatus.ACTIVE);

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
                metadataCollectionProperties.setDeployedImplementationType(ElementOriginCategory.LOCAL_COHORT.getDisplayName());
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
                metadataCollectionProperties.setContentStatus(ContentStatus.ACTIVE);

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
     * @param platformProperties   operational details of the platform
     * @param serverGUID           metadata details of the server
     * @param endpoint             endpoint details of the server
     * @throws InvalidParameterException  invalid parameter
     * @throws PropertyServerException    no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void updateServer(OMAGServerProperties          omagServerProperties,
                              OMAGServerPlatformProperties  platformProperties,
                              String                        serverGUID,
                              RelatedMetadataElementSummary endpoint) throws InvalidParameterException,
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
        softwareServerProperties.setIdentifier(omagServerProperties.getServerName());
        softwareServerProperties.setVersionIdentifier(platformProperties.getPlatformBuildProperties().getVersion());

        softwareServerProperties.setNamespacePath(omagServerProperties.getOrganizationName());
        softwareServerProperties.setDeployedImplementationType(omagServerProperties.getServerType());
        softwareServerProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);
        softwareServerProperties.setURL(this.getURL(omagServerProperties.getServerType()));

        softwareServerProperties.setIdentifier(omagServerProperties.getServerName());
        softwareServerProperties.setResourceName(this.getServerResourceName(omagServerProperties.getServerName(),
                                                                            omagServerProperties.getOrganizationName()));

        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("organizationName", omagServerProperties.getOrganizationName());
        additionalProperties.put("serverId", omagServerProperties.getServerId());
        additionalProperties.put("serverName", omagServerProperties.getServerName());
        additionalProperties.put("platformURLRoot", platformProperties.getPlatformURLRoot());

        softwareServerProperties.setAdditionalProperties(additionalProperties);

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER.typeName);

        assetClient.updateAsset(serverGUID,
                                assetClient.getUpdateOptions(true),
                                softwareServerProperties);

        checkAndUpdateEndpoint(platformProperties.getPlatformURLRoot(), endpoint);

        checkAndLinkGovernanceServerCapabilities(omagServerProperties, serverGUID);
    }


    /**
     * Add the definition of the server's audit log destinations used to distribute audit log events.
     * At the moment this just deals with kafka audit log destination.  This is to support Jacquard's audit log products.
     * ToDo Later versions could add other destination types.
     *
     * @param serverConfig server's configuration
     * @param softwareServer server's metadata description
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void catalogAuditLogConnector(OMAGServerConfig        serverConfig,
                                          OpenMetadataRootElement softwareServer) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String formula = "severity in [Error, Exception, Activity, Action, Decision, Security, Cohort]";

        if ((softwareServer != null) &&
                (softwareServer.getProperties() != null) &&
                (softwareServer.getProperties() instanceof SoftwareServerProperties softwareServerProperties))
        {
            if (softwareServer.getLineageLinkage() != null)
            {
                for (RelatedMetadataElementSummary lineageLink : softwareServer.getLineageLinkage())
                {
                    if ((lineageLink != null) && (lineageLink.getRelationshipProperties() instanceof DataFlowProperties dataFlowProperties) &&
                            (formula.equals(dataFlowProperties.getFormula())))
                    {
                        /*
                         * The relationship is in place.
                         * ToDo In a future iteration we may want to validate that the relationship is still correct.
                         */
                        return;
                    }
                }
            }

            /*
             * Create the relationship between the software server and the audit log topic.
             * First, we need to discover if the topic exists - and for this we need its name.
             * The topic name is the networkAddress attribute in the endpoint of the embedded audit log connection.
             */
            if ((serverConfig != null) &&
                    (serverConfig.getRepositoryServicesConfig() != null) &&
                    (serverConfig.getRepositoryServicesConfig().getAuditLogConnections() != null))
            {
                String topicName = null;
                String topicQualifiedName = null;
                Map<String, Object> configurationProperties = null;
                for (Connection auditLogConnection : serverConfig.getRepositoryServicesConfig().getAuditLogConnections())
                {
                    if ((auditLogConnection != null) &&
                            (auditLogConnection.getQualifiedName() != null) &&
                            (auditLogConnection instanceof VirtualConnection virtualConnection) &&
                            (virtualConnection.getEmbeddedConnections() != null))
                    {
                        for (EmbeddedConnection embeddedConnection : virtualConnection.getEmbeddedConnections())
                        {
                            /*
                             * This test is probably more picky than it needs to be.  However, these definitions are
                             * created by an administrator, possibly by hand, and so it is well to be cautious.
                             */
                            if ((embeddedConnection != null) &&
                                    (embeddedConnection.getEmbeddedConnection() != null) &&
                                    (embeddedConnection.getEmbeddedConnection().getEndpoint() != null) &&
                                    (embeddedConnection.getEmbeddedConnection().getEndpoint().getNetworkAddress() != null) &&
                                    (embeddedConnection.getEmbeddedConnection().getConnectorType() != null) &&
                                    (EgeriaOpenConnectorDefinition.KAFKA_TOPIC_CONNECTOR.getConnectorProviderClassName().equals(embeddedConnection.getEmbeddedConnection().getConnectorType().getConnectorProviderClassName())))
                            {
                                /*
                                 * This is a kafka topic connector.  We need to get the topic name and qualified name.
                                 */
                                topicName = embeddedConnection.getEmbeddedConnection().getEndpoint().getNetworkAddress();
                                topicQualifiedName = auditLogConnection.getQualifiedName();
                                configurationProperties = embeddedConnection.getEmbeddedConnection().getConfigurationProperties();

                                break;
                            }
                        }
                    }
                }

                if ((topicName != null) && (topicQualifiedName != null))
                {
                    String topicGUID = null;
                    AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.TOPIC.typeName);
                    List<OpenMetadataRootElement> elements = assetClient.getAssetsByName(topicQualifiedName,
                                                                                         assetClient.getQueryOptions());


                    if (elements != null)
                    {
                        for (OpenMetadataRootElement element : elements)
                        {
                            if ((element != null) && (element.getProperties() instanceof TopicProperties))
                            {
                                topicGUID = element.getElementHeader().getGUID();
                                break;
                            }
                        }
                    }

                    DataFlowProperties dataFlowProperties = new DataFlowProperties();

                    dataFlowProperties.setFormula(formula);
                    dataFlowProperties.setLabel("audit log events");
                    dataFlowProperties.setDescription("Audit log events from server " + softwareServerProperties.getDisplayName());
                    dataFlowProperties.setISCQualifiedName(EgeriaInformationSupplyChainDefinition.OPEN_METADATA_OBSERVABILITY.getQualifiedName());

                    if (topicGUID == null)
                    {
                        /*
                         * The topic needs to be created.  We will add the lineage link to the server at the same time.
                         */
                        TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

                        templateOptions.setAnchorGUID(null);
                        templateOptions.setIsOwnAnchor(true);
                        templateOptions.setAnchorScopeGUIDs(null);

                        templateOptions.setParentGUID(softwareServer.getElementHeader().getGUID());
                        templateOptions.setParentAtEnd1(true);
                        templateOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);

                        Map<String, String> placementProperties = new HashMap<>();

                        placementProperties.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), getKafkaHostIdentifier(configurationProperties));
                        placementProperties.put(PlaceholderProperty.PORT_NUMBER.getName(), getKafkaPortNumber(configurationProperties));
                        placementProperties.put(PlaceholderProperty.SERVER_NAME.getName(), "Default Apache Atlas");
                        placementProperties.put(PlaceholderProperty.DESCRIPTION.getName(), "Apache Kafka topic distributing audit log events from the OMAG Servers.");
                        placementProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), softwareServerProperties.getVersionIdentifier());
                        placementProperties.put(KafkaPlaceholderProperty.FULL_TOPIC_NAME.getName(), topicName);
                        placementProperties.put(KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getName(), topicName);
                        placementProperties.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");
                        topicGUID = assetClient.createAssetFromTemplate(templateOptions,
                                                                        KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateGUID(),
                                                                        null,
                                                                        null,
                                                                        placementProperties,
                                                                        dataFlowProperties);

                        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

                        ImplementedByProperties implementedByProperties = new ImplementedByProperties();

                        implementedByProperties.setRole("running instance");
                        implementedByProperties.setDescription("Topic usage discovered by " + connectorName + ".");

                        governanceDefinitionClient.linkDesignToImplementation(EgeriaSolutionComponent.AUDIT_LOG_TOPIC.getGUID(),
                                                                              topicGUID,
                                                                              new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()),
                                                                              implementedByProperties);
                    }
                    else
                    {
                        /*
                         * The topic is defined, we will add a new lineage relationship if it is missing.
                         */
                        String lineageRelationshipGUID = null;
                        if (softwareServer.getLineageLinkage() != null)
                        {
                            for (RelatedMetadataElementSummary lineageLink : softwareServer.getLineageLinkage())
                            {
                                if ((lineageLink != null) &&
                                        (! lineageLink.getRelatedElementAtEnd1()) &&
                                        (lineageLink.getRelatedElement().getElementHeader().getGUID().equals(topicGUID)))
                                {
                                    lineageRelationshipGUID = lineageLink.getRelationshipHeader().getGUID();
                                    break;
                                }
                            }
                        }

                        /*
                         * Add the missing lineage relationship.
                         */
                        if (lineageRelationshipGUID == null)
                        {
                            LineageClient lineageClient = integrationContext.getLineageClient();

                            lineageClient.linkLineage(softwareServer.getElementHeader().getGUID(),
                                                      topicGUID,
                                                      OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                      lineageClient.getMakeAnchorOptions(false),
                                                      dataFlowProperties);
                        }
                    }
                }
            }
        }
    }


    /**
     * Retrieve the host identifier from the bootstrap server information.
     *
     * @param configurationProperties configuration properties from topic connector
     * @return host identifier
     */
    private String getKafkaHostIdentifier(Map<String, Object> configurationProperties)
    {
        String bootstrapServer = getBootstrapServer(configurationProperties);
        if (bootstrapServer != null)
        {
            return bootstrapServer.substring(0, bootstrapServer.lastIndexOf(":"));
        }

        return null;
    }


    /**
     * Retrieve the port number from the bootstrap server information.
     *
     * @param configurationProperties configuration properties from topic connector
     * @return port number
     */
    private String getKafkaPortNumber(Map<String, Object> configurationProperties)
    {
        String bootstrapServer = getBootstrapServer(configurationProperties);
        if (bootstrapServer != null)
        {
            return bootstrapServer.substring(bootstrapServer.lastIndexOf(":") + 1);
        }

        return null;
    }


    /**
     * Retrieve the bootstrap server information from the configuration properties.
     * It contains the host identifier and port number.
     *
     * @param configurationProperties configuration properties from topic connector
     * @return bootstrap server information
     */
    private String getBootstrapServer(Map<String, Object> configurationProperties)
    {
        Object properties = configurationProperties.get("producer");
        if (properties instanceof Map<?, ?> map)
        {
            return (String) map.get("bootstrap.servers");
        }

        properties = configurationProperties.get("consumer");
        if (properties instanceof Map<?, ?> map)
        {
            return (String) map.get("bootstrap.servers");
        }

        return null;
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
                     * First, identify the server capabilities that are already connected to the server element
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
                     * First, identify the server capabilities that are already connected to the server element
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
     * Link the server to all the cohorts it is a member of.
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
                 * the cohort.  There is one software capability for each cohort, so the qualified name includes
                 * both the server name and the cohort.
                 */
                CohortMemberProperties cohortMemberProperties = new CohortMemberProperties();

                cohortMemberProperties.setQualifiedName(OpenMetadataType.COHORT_MEMBER.typeName + "::" + serverQualifiedName + "_cohortMember_" + cohort.getCohortName());
                cohortMemberProperties.setDisplayName("Cohort member of " + cohort.getCohortName() + " for server " + omagServerProperties.getServerName() + ".");
                cohortMemberProperties.setCategory(EGERIA_DEPLOYMENT_CATEGORY);
                cohortMemberProperties.setDeploymentStatus(DeploymentStatus.ACTIVE);

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
     * Return all the network addresses for a set of nested connectors.
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
         * configuration properties. There is probably only one connection, but the code allows for multiple
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
         * The platform does not have its secrets store explicitly defined, so use the one provided to this connector.
         */
        return super.getStringConfigurationProperty(OMAGServerPlatformConfigurationProperty.SECRETS_STORE.getName(), this.getConfigurationProperties());
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
         * configuration properties. There is probably only one connection, but the code allows for multiple
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
         * The platform does not have its secrets store explicitly defined, so use the one provided to this connector.
         */
        return super.getStringConfigurationProperty(OMAGServerPlatformConfigurationProperty.SECRETS_STORE.getName(), this.getConfigurationProperties());
    }
}
