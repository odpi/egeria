/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * OSSUnityCatalogServerSyncConnector synchronizes metadata between Unity Catalog and the Open Metadata Ecosystem.
 */
public class    OSSUnityCatalogServerSyncConnector extends IntegrationConnectorBase implements CatalogTargetIntegrator,
                                                                                               OpenMetadataEventListener
{
    String       defaultFriendshipGUID  = null;
    List<String> defaultExcludeCatalogs = new ArrayList<>();
    List<String> defaultIncludeCatalogs = new ArrayList<>();
    private Date lastRefreshCompleteTime = null;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        if (connectionBean.getConfigurationProperties() != null)
        {
            defaultFriendshipGUID = this.getFriendshipGUID(connectionBean.getConfigurationProperties());
            defaultExcludeCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_CATALOG_NAMES.getName(),
                                                                         connectionBean.getConfigurationProperties());
            defaultIncludeCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName(),
                                                                         connectionBean.getConfigurationProperties());
        }

        if (defaultFriendshipGUID != null)
        {
            auditLog.logMessage(methodName,
                                UCAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                 defaultFriendshipGUID));
        }
    }


    /**
     * Merge the templates supplied on the catalog target relationship with the default templates.
     *
     * @param catalogTargetTemplates supplied templates
     * @return template properties
     */
    private Map<String, String> getTemplates(Map<String, String> catalogTargetTemplates)
    {
        Map<String, String> templateProperties = new HashMap<>();

        /*
         * Add the default templates first
         */
        for (UnityCatalogTemplateType templateType : UnityCatalogTemplateType.values())
        {
            templateProperties.put(templateType.getTemplateName(), templateType.getTemplateGUID());
        }

        /*
         * Override templates supplied for the catalog target
         */
        if (catalogTargetTemplates != null)
        {
            templateProperties.putAll(catalogTargetTemplates);
        }

        return templateProperties;
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof OSSUnityCatalogResourceConnector unityCatalogResourceConnector)
                {
                    try
                    {
                        if (! unityCatalogResourceConnector.isActive())
                        {
                            unityCatalogResourceConnector.start();
                        }

                        catalogCatalogs(null,
                                        "endpoint",
                                        integrationContext.getPermittedSynchronization(),
                                        this.getTemplates(null),
                                        connectionBean.getConfigurationProperties(),
                                        unityCatalogResourceConnector);
                    }
                    catch (ConnectorCheckedException exception)
                    {
                        throw exception;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    exception.getClass().getName(),
                                                                                                    methodName,
                                                                                                    exception.getMessage()),
                                              exception);
                    }
                }
            }
        }

        this.refreshCatalogTargets(this);

        /*
         * Once the connector has completed a single refresh, it registered a listener with open metadata
         * to handle updates.  The delay in registering the listener is for efficiency-sake in that it
         * reduces the number of events coming in from updates to the open metadata ecosystem when the connector
         * is performing its first synchronization from Unity Catalog (UC) to Egeria.
         *
         * A listener is registered only if metadata is flowing from the open metadata ecosystem to Unity Catalog (UC).
         */
        if ((integrationContext.noListenerRegistered()) &&
                (lastRefreshCompleteTime != null) &&
                (integrationContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                (integrationContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
                 * the processEvent() method is called.
                 */
                integrationContext.registerListener(this);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          UCAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(UCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }

        lastRefreshCompleteTime = new Date();
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        final String methodName = "integrateCatalogTarget";

        if (UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName().equals(requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getType().getTypeName()))
        {
            String ucServerGUID = requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID();
            try
            {
                Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(ucServerGUID, auditLog);

                OSSUnityCatalogResourceConnector assetConnector = (OSSUnityCatalogResourceConnector) connector;

                assetConnector.setUCInstanceName(connectorName + "::" + requestedCatalogTarget.getCatalogTargetName());
                assetConnector.start();

                PermittedSynchronization permittedSynchronization = integrationContext.getPermittedSynchronization();

                if (requestedCatalogTarget.getPermittedSynchronization() != null)
                {
                    permittedSynchronization = requestedCatalogTarget.getPermittedSynchronization();
                }

                catalogCatalogs(ucServerGUID,
                                requestedCatalogTarget.getCatalogTargetName(),
                                permittedSynchronization,
                                this.getTemplates(requestedCatalogTarget.getTemplates()),
                                requestedCatalogTarget.getConfigurationProperties(),
                                assetConnector);

                assetConnector.disconnect();
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            exception.getClass().getName(),
                                                                                            methodName,
                                                                                            exception.getMessage()),
                                      exception);
            }
        }
        else
        {
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getType().getTypeName(),
                                        UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                        connectorName,
                                        methodName);
        }
    }



    /**
     * Catalog a catalog retrieved from the requested UC server.
     *
     * @param ucServerGUID unique identifier of the entity representing the Unity Catalog Server
     * @param catalogTargetName name of the target
     * @param permittedSynchronization direction of metadata exchange
     * @param templateProperties names of the templates
     * @param configurationProperties configuration properties
     * @param assetConnector connector to the database server
     * @throws ConnectorCheckedException unrecoverable error
     */
    private void catalogCatalogs(String                           ucServerGUID,
                                 String                           catalogTargetName,
                                 PermittedSynchronization         permittedSynchronization,
                                 Map<String, String>              templateProperties,
                                 Map<String, Object>              configurationProperties,
                                 OSSUnityCatalogResourceConnector assetConnector) throws ConnectorCheckedException
    {
        final String methodName = "catalogCatalogs";

        try
        {
            String ucServerEndpoint = this.getNetworkAddress(assetConnector);
            String friendshipConnectorGUID = getFriendshipGUID(configurationProperties);

            List<String> excludedCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_CATALOG_NAMES.getName(),
                                                                                 configurationProperties,
                                                                                 defaultExcludeCatalogs);

            List<String> includedCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName(),
                                                                                 configurationProperties,
                                                                                 defaultIncludeCatalogs);

            OSSUnityCatalogServerSyncCatalog syncCatalog = new OSSUnityCatalogServerSyncCatalog(connectorName,
                                                                                                integrationContext,
                                                                                                catalogTargetName,
                                                                                                ucServerGUID,
                                                                                                friendshipConnectorGUID,
                                                                                                permittedSynchronization,
                                                                                                assetConnector,
                                                                                                ucServerEndpoint,
                                                                                                templateProperties,
                                                                                                configurationProperties,
                                                                                                excludedCatalogs,
                                                                                                includedCatalogs,
                                                                                                auditLog);

            syncCatalog.refresh();
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                        exception.getClass().getName(),
                                                                                        methodName,
                                                                                        exception.getMessage()),
                                  exception);
        }
    }


    /**
     * Extract the friendship GUID from the configuration properties - or use the default.
     *
     * @param configurationProperties configuration properties for connection to UC
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = connectionBean.getConfigurationProperties().get(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
        }

        return friendshipGUID;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }

    /**
     * Process an event that was published by the Asset Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events and proceeding with event processing
         * at this time causes elements to be processed multiple times.
         */
        if (integrationContext.noRefreshInProgress())
        {
            /*
             * Call the appropriate registered module that matches the type.  Notice that multiple modules can be registered for the same type.
             */
            ElementHeader elementHeader = event.getElementHeader();

            Date lastUpdateTime = elementHeader.getVersions().getUpdateTime();
            String lastUpdateUser = elementHeader.getVersions().getUpdatedBy();

            if (lastUpdateTime == null)
            {
                lastUpdateTime = elementHeader.getVersions().getCreateTime();
                lastUpdateUser = elementHeader.getVersions().getCreatedBy();
            }

            try
            {
                if ((lastUpdateTime.after(lastRefreshCompleteTime)) &&
                    (! lastUpdateUser.equals(integrationContext.getMyUserId())) &&
                    (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.DATA_ACCESS_MANAGER.typeName)))
                {
                    AssetClient assetClient = integrationContext.getAssetClient();

                    /*
                     * This is a new catalog object.  Is it connected to one of the server that are catalog targets?
                     */
                    int startFrom = 0;

                    List<OpenMetadataRootElement> catalogTargetList = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                                    assetClient.getQueryOptions(startFrom, integrationContext.getMaxPageSize()));

                    while (catalogTargetList != null)
                    {
                        for (OpenMetadataRootElement catalogTarget : catalogTargetList)
                        {
                            if ((catalogTarget != null) && (super.isActive()) &&
                                    (catalogTarget.getRelatedBy() != null) &&
                                    (catalogTarget.getRelatedBy().getRelationshipProperties() instanceof CatalogTargetProperties catalogTargetProperties) &&
                                    (isCatalogForTargetServer(elementHeader.getGUID(), catalogTarget.getElementHeader().getGUID())))
                            {
                                Connector catalogTargetConnector = null;

                                catalogTargetProperties.setConfigurationProperties(super.combineConfigurationProperties(catalogTargetProperties.getConfigurationProperties()));

                                if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.ASSET.typeName))
                                {
                                    catalogTargetConnector = integrationContext.getConnectedAssetContext().getConnectorForAsset(catalogTarget.getElementHeader().getGUID(), auditLog);
                                }

                                CatalogTarget newCatalogTarget = new CatalogTarget(catalogTargetProperties, catalogTarget);
                                RequestedCatalogTarget requestedCatalogTarget = new RequestedCatalogTarget(newCatalogTarget,
                                                                                                           integrationContext.getCatalogTargetContext(newCatalogTarget),
                                                                                                           catalogTargetConnector);

                                auditLog.logMessage(methodName,
                                                    OIFAuditCode.REFRESHING_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                                                requestedCatalogTarget.getCatalogTargetName()));
                                this.integrateCatalogTarget(requestedCatalogTarget);
                            }
                        }

                        startFrom         = startFrom + integrationContext.getMaxPageSize();
                        catalogTargetList = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                          assetClient.getQueryOptions(startFrom, integrationContext.getMaxPageSize()));
                    }
                }
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            exception.getClass().getName(),
                                                                                            methodName,
                                                                                            exception.getMessage()),
                                      exception);
            }
        }
    }


    private boolean isCatalogForTargetServer(String catalogGUID,
                                             String serverGUID)
    {
        final String methodName = "isCatalogForTargetServer";

        /*
         * This is a new catalog object.  Is it connected to one of the server that are catalog targets?
         */
        try
        {
            int startFrom = 0;

            RelatedMetadataElementList relatedServers = integrationContext.getOpenMetadataStore().getRelatedMetadataElements(catalogGUID,
                                                                                                                             2,
                                                                                                                             OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                                                                             startFrom,
                                                                                                                             integrationContext.getMaxPageSize());

            while ((relatedServers != null) && (relatedServers.getElementList() != null))
            {
                for (RelatedMetadataElement relatedServer : relatedServers.getElementList())
                {
                    if ((relatedServer != null) && (serverGUID.equals(relatedServer.getElement().getElementGUID())))
                    {
                        return true;
                    }
                }

                startFrom      = startFrom + integrationContext.getMaxPageSize();
                relatedServers = integrationContext.getOpenMetadataStore().getRelatedMetadataElements(catalogGUID,
                                                                                                      2,
                                                                                                      OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                                                      startFrom,
                                                                                                      integrationContext.getMaxPageSize());

            }
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                        exception.getClass().getName(),
                                                                                        methodName,
                                                                                        exception.getMessage()),
                                  exception);
        }

        return false;
    }
}
