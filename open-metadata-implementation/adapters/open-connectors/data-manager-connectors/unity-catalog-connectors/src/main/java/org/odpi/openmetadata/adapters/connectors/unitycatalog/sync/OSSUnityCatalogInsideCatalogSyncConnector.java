/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.CatalogInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationGovernanceContext;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OSSUnityCatalogInsideCatalogSyncConnector synchronizes metadata within a specific catalog between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogInsideCatalogSyncConnector extends CatalogIntegratorConnector implements CatalogTargetIntegrator
{
    final PropertyHelper propertyHelper = new PropertyHelper();

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
            try
            {
                Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID());

                OSSUnityCatalogResourceConnector assetConnector = (OSSUnityCatalogResourceConnector) connector;

                assetConnector.start();

                catalogCatalog(requestedCatalogTarget.getCatalogTargetElement(),
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
     * @param configurationProperties configuration properties
     * @param assetConnector connector to the database server
     * @throws ConnectorCheckedException unrecoverable error
     */
    private void catalogCatalog(ElementStub ucServerGUID,
                                Map<String, Object> configurationProperties,
                                OSSUnityCatalogResourceConnector assetConnector) throws ConnectorCheckedException
    {
        final String methodName = "catalogCatalog";

        IntegrationGovernanceContext integrationGovernanceContext = this.getContext().getIntegrationGovernanceContext();
        OpenMetadataAccess           openMetadataAccess           = integrationGovernanceContext.getOpenMetadataAccess();

    }


    /**
     * Retrieve the endpoint from the asset connection.
     *
     * @param assetConnector asset connector
     * @return endpoint or null
     */
    private String getNetworkAddress(OSSUnityCatalogResourceConnector assetConnector)
    {
        ConnectionProperties assetConnection = assetConnector.getConnection();

        if (assetConnection != null)
        {
            EndpointProperties endpointProperties = assetConnection.getEndpoint();

            if (endpointProperties != null)
            {
                return endpointProperties.getAddress();
            }
        }

        return null;
    }


    /**
     * Set up the element properties for a software capability that is to represent a UC Catalog.
     *
     * @param catalogQualifiedName qualified name computed from the network address and the name of the catalog.
     * @param ucCatalog catalog information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementPropertiesForCatalog(String      catalogQualifiedName,
                                                             CatalogInfo ucCatalog)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               catalogQualifiedName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.NAME.name,
                                                             ucCatalog.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             ucCatalog.getComment());

        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                ucCatalog.getProperties());

        return elementProperties;
    }


    /**
     * Populate and return the external identifier properties for a UC catalog.
     *
     * @param ucCatalog values from Unity Catalog
     * @return external identifier properties
     */
    private ExternalIdentifierProperties getExternalIdentifierProperties(CatalogInfo ucCatalog)
    {
        ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

        externalIdentifierProperties.setExternalIdentifier(ucCatalog.getName());
        externalIdentifierProperties.setExternalIdentifierSource(DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());
        externalIdentifierProperties.setExternalInstanceCreationTime(new Date(ucCatalog.getCreated_at()));
        externalIdentifierProperties.setExternalInstanceLastUpdateTime(new Date(ucCatalog.getUpdated_at()));

        return externalIdentifierProperties;
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
