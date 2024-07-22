/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OSSUnityCatalogServerSyncConnector synchronizes metadata between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogServerSyncConnector extends CatalogIntegratorConnector implements CatalogTargetIntegrator
{
    String       defaultFriendshipGUID  = null;
    List<String> defaultExcludeCatalogs = new ArrayList<>();
    List<String> defaultIncludeCatalogs = new ArrayList<>();

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

        if (connectionProperties.getConfigurationProperties() != null)
        {
            defaultFriendshipGUID = this.getFriendshipGUID(connectionProperties.getConfigurationProperties());
            defaultExcludeCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_CATALOG_NAMES.getName(),
                                                                         connectionProperties.getConfigurationProperties(),
                                                                         null);
            defaultIncludeCatalogs = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName(),
                                                                         connectionProperties.getConfigurationProperties(),
                                                                         null);
        }

        if (defaultFriendshipGUID != null)
        {
            auditLog.logMessage(methodName,
                                UCAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                 defaultFriendshipGUID));
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
                                        this.getContext().getPermittedSynchronization(),
                                        null,
                                        connectionProperties.getConfigurationProperties(),
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
            String ucServerGUID = requestedCatalogTarget.getCatalogTargetElement().getGUID();
            try
            {
                Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(ucServerGUID);

                OSSUnityCatalogResourceConnector assetConnector = (OSSUnityCatalogResourceConnector) connector;

                assetConnector.setUCInstanceName(connectorName + ":" + requestedCatalogTarget.getCatalogTargetName());
                assetConnector.start();

                PermittedSynchronization permittedSynchronization = this.getContext().getPermittedSynchronization();

                if (requestedCatalogTarget.getPermittedSynchronization() != null)
                {
                    permittedSynchronization = requestedCatalogTarget.getPermittedSynchronization();
                }

                catalogCatalogs(ucServerGUID,
                                requestedCatalogTarget.getCatalogTargetName(),
                                permittedSynchronization,
                                requestedCatalogTarget.getTemplateProperties(),
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
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName(),
                                        DeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
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
                                                                                                this.getContext(),
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
            friendshipGUID = connectionProperties.getConfigurationProperties().get(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
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
}
