/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OSSUnityCatalogInsideCatalogSyncConnector synchronizes metadata within a specific catalog between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogInsideCatalogSyncConnector extends CatalogIntegratorConnector implements CatalogTargetIntegrator,
                                                                                                     AssetManagerEventListener
{
    private List<String> defaultExcludeSchemaNames   = null;
    private List<String> defaultIncludeSchemaNames   = null;
    private List<String> defaultExcludeTableNames    = null;
    private List<String> defaultIncludeTableNames    = null;
    private List<String> defaultExcludeFunctionNames = null;
    private List<String> defaultIncludeFunctionNames = null;
    private List<String> defaultExcludeVolumeNames   = null;
    private List<String> defaultIncludeVolumeNames   = null;

    private Date lastRefreshCompleteTime = null;
    
    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        if (connectionProperties.getEndpoint() != null)
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
                                                                             connectionProperties.getConfigurationProperties(),
                                                                             null);
        this.defaultIncludeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                             connectionProperties.getConfigurationProperties(),
                                                                             null);
        this.defaultExcludeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getName(),
                                                                            connectionProperties.getConfigurationProperties(),
                                                                            null);
        this.defaultIncludeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_TABLE_NAMES.getName(),
                                                                            connectionProperties.getConfigurationProperties(),
                                                                            null);
        this.defaultExcludeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getName(),
                                                                               connectionProperties.getConfigurationProperties(),
                                                                               null);
        this.defaultIncludeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_FUNCTION_NAMES.getName(),
                                                                               connectionProperties.getConfigurationProperties(),
                                                                               null);
        this.defaultExcludeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getName(),
                                                                             connectionProperties.getConfigurationProperties(),
                                                                             null);
        this.defaultIncludeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_VOLUME_NAMES.getName(),
                                                                             connectionProperties.getConfigurationProperties(),
                                                                             null);
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
        if ((! super.getContext().isListenerRegistered()) &&
                (lastRefreshCompleteTime != null) &&
                (super.getContext().getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                (super.getContext().getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
                 * the processEvent() method is called.
                 */
                super.getContext().registerListener(this);
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
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        final String methodName = "processEvent";

        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events and proceeding with event processing
         * at this time causes elements to be processed multiple times.
         */
        if (! integrationContext.isRefreshInProgress())
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
                        (! lastUpdateUser.equals(super.getContext().getMyUserId())) &&
                        (elementHeader.getOrigin().getHomeMetadataCollectionName() != null) &&
                        ((propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SOFTWARE_CAPABILITY.typeName)) ||
                                (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.ASSET.typeName)) ||
                                (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SCHEMA_ELEMENT.typeName))))
                {
                    int startFrom = 0;

                    List<CatalogTarget> catalogTargetList = integrationContext.getCatalogTargets(startFrom, integrationContext.getMaxPageSize());

                    while (catalogTargetList != null)
                    {
                        for (CatalogTarget catalogTarget : catalogTargetList)
                        {
                            if ((catalogTarget != null) &&
                                    (super.isActive()) &&
                                    (elementHeader.getOrigin().getHomeMetadataCollectionName().equals(catalogTarget.getMetadataSourceQualifiedName())))
                            {
                                boolean savedExternalSourceIsHome        = integrationContext.getExternalSourceIsHome();
                                String  savedMetadataSourceQualifiedName = integrationContext.getMetadataSourceQualifiedName();

                                integrationContext.setMetadataSourceQualifiedName(catalogTarget.getMetadataSourceQualifiedName());
                                integrationContext.setExternalSourceIsHome(true);

                                RequestedCatalogTarget requestedCatalogTarget = new RequestedCatalogTarget(catalogTarget, null);

                                Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

                                if (catalogTarget.getConfigurationProperties() != null)
                                {
                                    if (configurationProperties == null)
                                    {
                                        configurationProperties = new HashMap<>();
                                    }

                                    configurationProperties.putAll(catalogTarget.getConfigurationProperties());
                                }

                                requestedCatalogTarget.setConfigurationProperties(configurationProperties);

                                if (propertyHelper.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.ASSET.typeName))
                                {
                                    requestedCatalogTarget.setCatalogTargetConnector(integrationContext.getConnectedAssetContext().getConnectorToAsset(catalogTarget.getCatalogTargetElement().getGUID(),
                                                                                                                                                       auditLog));
                                }

                                auditLog.logMessage(methodName,
                                                    OIFAuditCode.REFRESHING_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                                                requestedCatalogTarget.getCatalogTargetName()));
                                this.integrateCatalogTarget(requestedCatalogTarget);

                                integrationContext.setExternalSourceIsHome(savedExternalSourceIsHome);
                                integrationContext.setMetadataSourceQualifiedName(savedMetadataSourceQualifiedName);
                            }
                        }

                        startFrom         = startFrom + integrationContext.getMaxPageSize();
                        catalogTargetList = integrationContext.getCatalogTargets(startFrom, integrationContext.getMaxPageSize());
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

        if (UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName().equals(requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName()))
        {
            if ((requestedCatalogTarget.getConfigurationProperties() != null) &&
                    (requestedCatalogTarget.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()) != null))
            {
                try
                {
                    String catalogName = requestedCatalogTarget.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()).toString();

                    Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID(), auditLog);

                    OSSUnityCatalogResourceConnector assetConnector = (OSSUnityCatalogResourceConnector) connector;

                    assetConnector.setUCInstanceName(connectorName + ":" + requestedCatalogTarget.getCatalogTargetName());
                    assetConnector.start();

                    String ucServerEndpoint = this.getNetworkAddress(assetConnector);

                    PermittedSynchronization permittedSynchronization = this.getContext().getPermittedSynchronization();

                    if (requestedCatalogTarget.getPermittedSynchronization() != null)
                    {
                        permittedSynchronization = requestedCatalogTarget.getPermittedSynchronization();
                    }

                    Map<String, String> ucFullNameToEgeriaGUID = new HashMap<>();

                    ucFullNameToEgeriaGUID.put(catalogName, requestedCatalogTarget.getCatalogTargetElement().getGUID());

                    this.refreshCatalog(requestedCatalogTarget.getCatalogTargetName(),
                                        catalogName,
                                        ucFullNameToEgeriaGUID,
                                        permittedSynchronization,
                                        assetConnector,
                                        ucServerEndpoint,
                                        requestedCatalogTarget.getTemplateProperties(),
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
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName(),
                                        UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                        connectorName,
                                        methodName);
        }
    }


    /**
     * Synchronize a catalog.
     *
     * @param catalogTargetName the catalog target name
     * @param catalogName name of the catalog
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     */
    private void refreshCatalog(String                           catalogTargetName,
                                String                           catalogName,
                                Map<String, String>              ucFullNameToEgeriaGUID,
                                PermittedSynchronization         targetPermittedSynchronization,
                                OSSUnityCatalogResourceConnector ucConnector,
                                String                           ucServerEndpoint,
                                Map<String, String>              templates,
                                Map<String, Object>              configurationProperties)
    {
        final String methodName = "refreshCatalog(" + catalogName + ")";

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

        try
        {
            OSSUnityCatalogInsideCatalogSyncSchema syncSchema = new OSSUnityCatalogInsideCatalogSyncSchema(connectorName,
                                                                                                           this.getContext(),
                                                                                                           catalogTargetName,
                                                                                                           catalogName,
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
                                                                                                              this.getContext(),
                                                                                                              catalogTargetName,
                                                                                                              catalogName,
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
                                                                                                           this.getContext(),
                                                                                                           catalogTargetName,
                                                                                                           catalogName,
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
                                                                                                                    this.getContext(),
                                                                                                                    catalogTargetName,
                                                                                                                    catalogName,
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
