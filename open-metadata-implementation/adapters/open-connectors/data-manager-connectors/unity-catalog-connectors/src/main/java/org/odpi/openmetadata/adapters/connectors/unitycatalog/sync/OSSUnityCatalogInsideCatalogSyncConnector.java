/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * OSSUnityCatalogInsideCatalogSyncConnector synchronizes metadata within a specific catalog between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogInsideCatalogSyncConnector extends CatalogIntegratorConnector implements CatalogTargetIntegrator
{
    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException
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
        this.refreshCatalogTargets(this);
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

        if (DeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName().equals(requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName()))
        {
            if ((requestedCatalogTarget.getConfigurationProperties() != null) &&
                    (requestedCatalogTarget.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()) != null))
            {
                try
                {
                    String catalogName = requestedCatalogTarget.getConfigurationProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()).toString();

                    Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID());

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
                                        DeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
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
