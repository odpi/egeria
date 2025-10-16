/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OSSUnityCatalogInsideCatalogSyncConnector synchronizes metadata within a specific catalog between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogInsideCatalogSyncConnector extends IntegrationConnectorBase implements CatalogTargetIntegrator,
                                                                                                   OpenMetadataEventListener
{
    private List<String> defaultExcludeSchemaNames   = null;
    private List<String> defaultIncludeSchemaNames   = null;
    private List<String> defaultExcludeTableNames    = null;
    private List<String> defaultIncludeTableNames    = null;
    private List<String> defaultExcludeFunctionNames = null;
    private List<String> defaultIncludeFunctionNames = null;
    private List<String> defaultExcludeVolumeNames   = null;
    private List<String> defaultIncludeVolumeNames   = null;
    private List<String> defaultExcludeModelNames   = null;
    private List<String> defaultIncludeModelNames   = null;

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

        if (connectionBean.getEndpoint() != null)
        {
            auditLog.logMessage(methodName,
                                UCAuditCode.IGNORING_ENDPOINT.getMessageDefinition(connectorName));
        }

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof OSSUnityCatalogResourceConnector)
                {
                    auditLog.logMessage(methodName,
                                        UCAuditCode.IGNORING_ENDPOINT.getMessageDefinition(connectorName));
                }
            }
        }

        this.defaultExcludeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                             connectionBean.getConfigurationProperties());
        this.defaultIncludeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                             connectionBean.getConfigurationProperties());
        this.defaultExcludeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getName(),
                                                                            connectionBean.getConfigurationProperties());
        this.defaultIncludeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_TABLE_NAMES.getName(),
                                                                            connectionBean.getConfigurationProperties());
        this.defaultExcludeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getName(),
                                                                               connectionBean.getConfigurationProperties());
        this.defaultIncludeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_FUNCTION_NAMES.getName(),
                                                                               connectionBean.getConfigurationProperties());
        this.defaultExcludeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getName(),
                                                                             connectionBean.getConfigurationProperties());
        this.defaultIncludeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_VOLUME_NAMES.getName(),
                                                                             connectionBean.getConfigurationProperties());
        this.defaultExcludeModelNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_MODEL_NAMES.getName(),
                                                                            connectionBean.getConfigurationProperties());
        this.defaultIncludeModelNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_MODEL_NAMES.getName(),
                                                                            connectionBean.getConfigurationProperties());
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
     * Process an event that was published by the Asset Manager OMAS.  This connector is only interested in
     * glossaries, glossary categories and glossary terms.   The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Unity Catalog (UC).
     *
     * @param event event object
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
                if ((elementHeader.getOrigin().getOriginCategory() == ElementOriginCategory.EXTERNAL_SOURCE) &&
                        (lastUpdateTime.after(lastRefreshCompleteTime)) &&
                        (! lastUpdateUser.equals(integrationContext.getMyUserId())) &&
                        (elementHeader.getOrigin().getHomeMetadataCollectionName() != null) &&
                        ((propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SOFTWARE_CAPABILITY.typeName)) ||
                                (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.ASSET.typeName)) ||
                                (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SCHEMA_ELEMENT.typeName))))
                {
                    AssetClient assetClient = integrationContext.getAssetClient();

                    int startFrom = 0;

                    List<OpenMetadataRootElement> catalogTargetList = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                                    assetClient.getQueryOptions(startFrom, integrationContext.getMaxPageSize()));

                    while (catalogTargetList != null)
                    {
                        for (OpenMetadataRootElement catalogTarget : catalogTargetList)
                        {
                            if ((catalogTarget != null) &&
                                    (super.isActive()) &&
                                    (catalogTarget.getRelatedBy().getRelationshipProperties() instanceof CatalogTargetProperties catalogTargetProperties) &&
                                    (elementHeader.getOrigin().getHomeMetadataCollectionName().equals(catalogTargetProperties.getMetadataSourceQualifiedName())))
                            {
                                Connector catalogTargetConnector = null;

                                if (propertyHelper.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.ASSET.typeName))
                                {
                                    catalogTargetConnector = integrationContext.getConnectedAssetContext().getConnectorForAsset(catalogTarget.getElementHeader().getGUID(), auditLog);
                                }

                                CatalogTarget newCatalogTarget = new CatalogTarget(catalogTargetProperties, catalogTarget);
                                RequestedCatalogTarget requestedCatalogTarget = new RequestedCatalogTarget(newCatalogTarget,
                                                                                                           integrationContext.getCatalogTargetContext(catalogTargetProperties),
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
            if ((requestedCatalogTarget.getConfigurationProperties() != null) &&
                    (requestedCatalogTarget.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()) != null))
            {
                try
                {
                    String catalogName = requestedCatalogTarget.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()).toString();
                    String catalogQualifiedName = requestedCatalogTarget.getMetadataSourceQualifiedName();
                    String catalogGUID = requestedCatalogTarget.getConfigurationProperties().get(OpenMetadataProperty.GUID.name).toString();

                    Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID(), auditLog);

                    OSSUnityCatalogResourceConnector assetConnector = (OSSUnityCatalogResourceConnector) connector;

                    assetConnector.setUCInstanceName(connectorName + "::" + requestedCatalogTarget.getCatalogTargetName());
                    assetConnector.start();

                    String ucServerEndpoint = this.getNetworkAddress(assetConnector);

                    PermittedSynchronization permittedSynchronization = integrationContext.getPermittedSynchronization();

                    if (requestedCatalogTarget.getPermittedSynchronization() != null)
                    {
                        permittedSynchronization = requestedCatalogTarget.getPermittedSynchronization();
                    }

                    Map<String, String> ucFullNameToEgeriaGUID = new HashMap<>();

                    ucFullNameToEgeriaGUID.put(catalogName, requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID());

                    this.refreshCatalog(catalogName,
                                        catalogGUID,
                                        catalogQualifiedName,
                                        ucFullNameToEgeriaGUID,
                                        permittedSynchronization,
                                        assetConnector,
                                        ucServerEndpoint,
                                        requestedCatalogTarget.getTemplates(),
                                        requestedCatalogTarget.getConfigurationProperties());

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
                super.throwMissingConfigurationProperty(connectorName,
                                                        requestedCatalogTarget.getCatalogTargetName(),
                                                        UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                                                        methodName);
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
     * Synchronize a catalog.
     *
     * @param catalogName the catalog target name
     * @param catalogGUID guid of the catalog
     * @param catalogQualifiedName name of the catalog
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     */
    private void refreshCatalog(String                           catalogName,
                                String                           catalogGUID,
                                String                           catalogQualifiedName,
                                Map<String, String>              ucFullNameToEgeriaGUID,
                                PermittedSynchronization         targetPermittedSynchronization,
                                OSSUnityCatalogResourceConnector ucConnector,
                                String                           ucServerEndpoint,
                                Map<String, String>              templates,
                                Map<String, Object>              configurationProperties)
    {
        final String methodName = "refreshCatalog(" + catalogQualifiedName + ")";

        List<String> excludeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                              configurationProperties,
                                                                              defaultExcludeSchemaNames);
        List<String> includeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                              configurationProperties,
                                                                              defaultIncludeSchemaNames);
        List<String> excludeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getName(),
                                                                             configurationProperties,
                                                                             defaultExcludeTableNames);
        List<String> includeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_TABLE_NAMES.getName(),
                                                                             configurationProperties,
                                                                             defaultIncludeTableNames);
        List<String> excludeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getName(),
                                                                                configurationProperties,
                                                                                defaultExcludeFunctionNames);
        List<String> includeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_FUNCTION_NAMES.getName(),
                                                                                configurationProperties,
                                                                                defaultIncludeFunctionNames);
        List<String> excludeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getName(),
                                                                              configurationProperties,
                                                                              defaultExcludeVolumeNames);
        List<String> includeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_VOLUME_NAMES.getName(),
                                                                              configurationProperties,
                                                                              defaultIncludeVolumeNames);
        List<String> excludeModelNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_MODEL_NAMES.getName(),
                                                                              configurationProperties,
                                                                              defaultExcludeModelNames);
        List<String> includeModelNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_MODEL_NAMES.getName(),
                                                                              configurationProperties,
                                                                              defaultIncludeModelNames);

        try
        {
            OSSUnityCatalogInsideCatalogSyncSchema syncSchema = new OSSUnityCatalogInsideCatalogSyncSchema(connectorName,
                                                                                                           integrationContext,
                                                                                                           catalogName,
                                                                                                           catalogGUID,
                                                                                                           catalogQualifiedName,
                                                                                                           ucFullNameToEgeriaGUID,
                                                                                                           targetPermittedSynchronization,
                                                                                                           ucConnector,
                                                                                                           ucServerEndpoint,
                                                                                                           templates,
                                                                                                           configurationProperties,
                                                                                                           excludeSchemaNames,
                                                                                                           includeSchemaNames,
                                                                                                           auditLog);

            ucFullNameToEgeriaGUID.putAll(syncSchema.refresh());

            OSSUnityCatalogInsideCatalogSyncVolumes syncVolumes = new OSSUnityCatalogInsideCatalogSyncVolumes(connectorName,
                                                                                                              integrationContext,
                                                                                                              catalogName,
                                                                                                              catalogGUID,
                                                                                                              catalogQualifiedName,
                                                                                                              ucFullNameToEgeriaGUID,
                                                                                                              targetPermittedSynchronization,
                                                                                                              ucConnector,
                                                                                                              ucServerEndpoint,
                                                                                                              templates,
                                                                                                              configurationProperties,
                                                                                                              excludeVolumeNames,
                                                                                                              includeVolumeNames,
                                                                                                              auditLog);

            ucFullNameToEgeriaGUID.putAll(syncVolumes.refresh());

            OSSUnityCatalogInsideCatalogSyncTables syncTables = new OSSUnityCatalogInsideCatalogSyncTables(connectorName,
                                                                                                           integrationContext,
                                                                                                           catalogName,
                                                                                                           catalogGUID,
                                                                                                           catalogQualifiedName,
                                                                                                           ucFullNameToEgeriaGUID,
                                                                                                           targetPermittedSynchronization,
                                                                                                           ucConnector,
                                                                                                           ucServerEndpoint,
                                                                                                           templates,
                                                                                                           configurationProperties,
                                                                                                           excludeTableNames,
                                                                                                           includeTableNames,
                                                                                                           auditLog);

            ucFullNameToEgeriaGUID.putAll(syncTables.refresh());

            OSSUnityCatalogInsideCatalogSyncFunctions syncFunctions = new OSSUnityCatalogInsideCatalogSyncFunctions(connectorName,
                                                                                                                    integrationContext,
                                                                                                                    catalogName,
                                                                                                                    catalogGUID,
                                                                                                                    catalogQualifiedName,
                                                                                                                    ucFullNameToEgeriaGUID,
                                                                                                                    targetPermittedSynchronization,
                                                                                                                    ucConnector,
                                                                                                                    ucServerEndpoint,
                                                                                                                    templates,
                                                                                                                    configurationProperties,
                                                                                                                    excludeFunctionNames,
                                                                                                                    includeFunctionNames,
                                                                                                                    auditLog);

            ucFullNameToEgeriaGUID.putAll(syncFunctions.refresh());

            OSSUnityCatalogInsideCatalogSyncRegisteredModels syncRegisteredModels = new OSSUnityCatalogInsideCatalogSyncRegisteredModels(connectorName,
                                                                                                                                         integrationContext,
                                                                                                                                         catalogName,
                                                                                                                                         catalogGUID,
                                                                                                                                         catalogQualifiedName,
                                                                                                                                         ucFullNameToEgeriaGUID,
                                                                                                                                         targetPermittedSynchronization,
                                                                                                                                         ucConnector,
                                                                                                                                         ucServerEndpoint,
                                                                                                                                         templates,
                                                                                                                                         configurationProperties,
                                                                                                                                         excludeModelNames,
                                                                                                                                         includeModelNames,
                                                                                                                                         auditLog);

            ucFullNameToEgeriaGUID.putAll(syncRegisteredModels.refresh());
        }
        catch (Exception exception)
        {
            auditLog.logMessage(methodName,
                                UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                      exception.getClass().getName(),
                                                                                      methodName,
                                                                                      exception.getMessage()));
        }
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
}
