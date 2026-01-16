/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OSSUnityCatalogInsideCatalogSyncConnector synchronizes metadata within a specific catalog between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogInsideCatalogSyncCatalogTargetProcessor extends CatalogTargetProcessorBase implements OpenMetadataEventListener
{
    private List<String> defaultExcludeSchemaNames   = null;
    private List<String> defaultIncludeSchemaNames   = null;


    private OSSUnityCatalogResourceConnector ucResourceConnector = null;
    private Date lastRefreshCompleteTime = null;

    /**
     * Primary constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public OSSUnityCatalogInsideCatalogSyncCatalogTargetProcessor(CatalogTarget        template,
                                                                  CatalogTargetContext catalogTargetContext,
                                                                  Connector            connectorToTarget,
                                                                  String               connectorName,
                                                                  AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        this.defaultExcludeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                             this.getConfigurationProperties());
        this.defaultIncludeSchemaNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                             this.getConfigurationProperties());


        if (this.connectorToTarget instanceof OSSUnityCatalogResourceConnector ossUnityCatalogResourceConnector)
        {
            ucResourceConnector = ossUnityCatalogResourceConnector;
            ossUnityCatalogResourceConnector.setUCInstanceName(connectorName + "::" + this.getCatalogTargetName());
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if (UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName().equals(this.getCatalogTargetElement().getElementHeader().getType().getTypeName()))
        {
            if ((this.getConfigurationProperties() != null) &&
                (this.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()) != null))
            {
                try
                {
                    String catalogName = this.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()).toString();
                    String catalogGUID = this.getConfigurationProperties().get(OpenMetadataProperty.GUID.name).toString();

                    String ucServerEndpoint = this.getNetworkAddress();

                    Map<String, String> ucFullNameToEgeriaGUID = new HashMap<>();

                    ucFullNameToEgeriaGUID.put(catalogName, this.getCatalogTargetElement().getElementHeader().getGUID());

                    this.refreshCatalog(catalogName,
                                        catalogGUID,
                                        integrationContext.getMetadataSourceQualifiedName(),
                                        ucFullNameToEgeriaGUID,
                                        integrationContext.getPermittedSynchronization(),
                                        ucResourceConnector,
                                        ucServerEndpoint,
                                        this.getTemplates(),
                                        this.getConfigurationProperties());
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
                super.throwMissingConnectionInfo(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                                                 methodName);
            }
        }
        else
        {
            super.throwWrongTypeOfCatalogTarget(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                                methodName);
        }

        lastRefreshCompleteTime = new Date();
    }


    /**
     * Synchronize a catalog.
     *
     * @param catalogName the catalog target name
     * @param catalogGUID guid of the catalog
     * @param metadataSourceQualifiedName name of the metadata collection for this server
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     */
    private void refreshCatalog(String                           catalogName,
                                String                           catalogGUID,
                                String                           metadataSourceQualifiedName,
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

        try
        {
            OSSUnityCatalogInsideCatalogSyncSchema syncSchema = new OSSUnityCatalogInsideCatalogSyncSchema(connectorName,
                                                                                                           integrationContext,
                                                                                                           catalogName,
                                                                                                           catalogGUID,
                                                                                                           metadataSourceQualifiedName,
                                                                                                           ucFullNameToEgeriaGUID,
                                                                                                           targetPermittedSynchronization,
                                                                                                           ucConnector,
                                                                                                           ucServerEndpoint,
                                                                                                           templates,
                                                                                                           configurationProperties,
                                                                                                           excludeSchemaNames,
                                                                                                           includeSchemaNames,
                                                                                                           auditLog);

            ucFullNameToEgeriaGUID.putAll(syncSchema.refresh(catalogGUID,
                                                             OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                             null));

            ucFullNameToEgeriaGUID.putAll(syncSchema.refreshChildren(catalogGUID,
                                                                     OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                     null));
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
     * Process an event that was published by the OMF.   The listener is only registered if metadata is flowing
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
                /*
                 * Is this event of interest?  Could it have been processed before?  Is it part of this catalog?
                 */
                if ((lastUpdateTime.after(lastRefreshCompleteTime)) &&
                        (! lastUpdateUser.equals(integrationContext.getMyUserId())) &&
                        (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.ASSET.typeName)))
                {
                    String catalogName = this.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()).toString();
                    String resourceName = propertyHelper.getStringProperty(connectorName,
                                                                           OpenMetadataProperty.RESOURCE_NAME.name,
                                                                           event.getElementProperties(),
                                                                           methodName);

                    if ((resourceName != null) && (resourceName.contains(catalogName)))
                    {
                        refresh();
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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
