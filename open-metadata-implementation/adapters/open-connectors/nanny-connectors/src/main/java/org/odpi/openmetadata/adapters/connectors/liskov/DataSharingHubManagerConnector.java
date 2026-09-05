/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;


import org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovAuditCode;
import org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingHubProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The Data Sharing Hub Manager Connector is an integration connector detects each data sharing hub collection
 * as it is created and adds them as a catalog target.  The DataSharingHubManagerTargetProcessor then monitors the
 * data stores that are members of the data sharing hub collection, building an abstracted Data Dictionary that represents
 * the data in the data sharing hub.
 */
public class DataSharingHubManagerConnector extends DynamicIntegrationConnectorBase
{
    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It will monitor the data sharing hubs that are linked as catalog targets, maintaining a data dictionary
     * for each one.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        logRecord(methodName, LiskovAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                      integrationContext.getMetadataAccessServer(),
                                                                                      integrationContext.getMetadataAccessServerPlatformURLRoot()));
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        try
        {
            Set<String> knownCatalogTargets = new HashSet<>();

            AssetClient assetClient = integrationContext.getAssetClient();
            int startFrom = 0;

            List<OpenMetadataRootElement> catalogTargetList  = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                             assetClient.getQueryOptions(startFrom, integrationContext.getMaxPageSize()));

            while (catalogTargetList != null)
            {
                for (OpenMetadataRootElement catalogTarget : catalogTargetList)
                {
                    if (catalogTarget != null)
                    {
                        knownCatalogTargets.add(catalogTarget.getElementHeader().getGUID());
                    }
                }

                startFrom         = startFrom + integrationContext.getMaxPageSize();
                catalogTargetList = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                  assetClient.getQueryOptions(startFrom, integrationContext.getMaxPageSize()));
            }

            /*
             * Process any new data sharing hubs.
             */
            CollectionClient collectionClient = integrationContext.getCollectionClient(OpenMetadataType.DATA_SHARING_HUB.typeName);
            startFrom = 0;

            List<OpenMetadataRootElement> dataSharingHubs = collectionClient.findCollections(null,
                                                                                             collectionClient.getSearchOptions(startFrom, integrationContext.getMaxPageSize()));

            while (dataSharingHubs != null)
            {
                for (OpenMetadataRootElement dataSharingHub : dataSharingHubs)
                {
                    if ((dataSharingHub != null) && (! knownCatalogTargets.contains(dataSharingHub.getElementHeader().getGUID())) && (dataSharingHub.getProperties() instanceof DataSharingHubProperties dataSharingHubProperties))
                    {
                        /*
                         * This is a new data sharing hub.  Add it as a catalog target.
                         */
                        logRecord(methodName, LiskovAuditCode.NEW_DATA_HUB.getMessageDefinition(connectorName,
                                                                                                dataSharingHubProperties.getDisplayName(),
                                                                                                dataSharingHub.getElementHeader().getGUID()));

                        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();
                        catalogTargetProperties.setCatalogTargetName(dataSharingHubProperties.getDisplayName() + "(" + dataSharingHub.getElementHeader().getGUID() + ")");
                        assetClient.addCatalogTarget(integrationContext.getIntegrationConnectorGUID(),
                                                     dataSharingHub.getElementHeader().getGUID(),
                                                     assetClient.getMakeAnchorOptions(false),
                                                     catalogTargetProperties);
                    }
                }

                startFrom = startFrom + integrationContext.getMaxPageSize();
                dataSharingHubs  = collectionClient.findCollections(null,
                                                             collectionClient.getSearchOptions(startFrom, integrationContext.getMaxPageSize()));
            }
        }
        catch (Exception error)
        {
            logRecord(methodName,
                      LiskovAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                error.getClass().getName(),
                                                                                methodName,
                                                                                error.getMessage()));
        }

        super.refresh();
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext   specialized context for this catalog target
     * @param connectorToTarget      connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException  a problem with setting up the catalog target.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget, CatalogTargetContext catalogTargetContext, Connector connectorToTarget) throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new DataSharingHubManagerTargetProcessor(retrievedCatalogTarget,
                                                            catalogTargetContext,
                                                            connectorToTarget,
                                                            connectorName,
                                                            auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(LiskovErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        logRecord(methodName, LiskovAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName,
                                                                                      integrationContext.getMetadataAccessServer(),
                                                                                      integrationContext.getMetadataAccessServerPlatformURLRoot()));

        super.disconnect();
    }
}
