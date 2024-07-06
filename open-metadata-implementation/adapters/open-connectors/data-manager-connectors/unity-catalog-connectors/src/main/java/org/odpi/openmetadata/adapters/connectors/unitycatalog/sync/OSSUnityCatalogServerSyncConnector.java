/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
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
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationGovernanceContext;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTargetProperties;
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
 * OSSUnityCatalogServerSyncConnector synchronizes metadata between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogServerSyncConnector extends CatalogIntegratorConnector implements CatalogTargetIntegrator
{
    final PropertyHelper propertyHelper = new PropertyHelper();

    String defaultFriendshipGUID = null;

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

                assetConnector.start();

                catalogCatalogs(ucServerGUID,
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
    private void catalogCatalogs(String                           ucServerGUID,
                                 Map<String, Object>              configurationProperties,
                                 OSSUnityCatalogResourceConnector assetConnector) throws ConnectorCheckedException
    {
        final String methodName = "catalogCatalogs";

        IntegrationGovernanceContext integrationGovernanceContext = this.getContext().getIntegrationGovernanceContext();
        OpenMetadataAccess           openMetadataAccess           = integrationGovernanceContext.getOpenMetadataAccess();

        try
        {
            String  ucServerEndpoint = this.getNetworkAddress(assetConnector);

            List<CatalogInfo>  ucCatalogs = assetConnector.listCatalogs();

            if (ucCatalogs != null)
            {
                for (CatalogInfo ucCatalog : ucCatalogs)
                {
                    if (ucCatalog != null)
                    {
                        this.getContext().setMetadataSourceQualifiedName(null);

                        String ucCatalogQualifiedName = "UnityCatalog:" + ucServerEndpoint + ":" + ucCatalog.getName();

                        OpenMetadataElement ucCatalogElement = openMetadataAccess.getMetadataElementByUniqueName(ucCatalogQualifiedName,
                                                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name);
                        String ucCatalogGUID;
                        if (ucCatalogElement != null)
                        {
                            ucCatalogGUID = ucCatalogElement.getElementGUID();
                            /*
                             * Element already catalogued. Update if necessary.
                             */
                            openMetadataAccess.updateMetadataElementInStore(ucCatalogElement.getElementGUID(),
                                                                            false,
                                                                            this.getElementPropertiesForCatalog(ucCatalogQualifiedName, ucCatalog));

                            this.getContext().setMetadataSourceQualifiedName(ucCatalogGUID, ucCatalogQualifiedName);
                            this.getContext().updateExternalIdentifier(ucCatalogGUID, OpenMetadataType.CATALOG.typeName, this.getExternalIdentifierProperties(ucCatalog));
                            this.getContext().confirmSynchronization(ucCatalogGUID, OpenMetadataType.CATALOG.typeName, ucCatalog.getName());
                        }
                        else
                        {
                            ucCatalogGUID = openMetadataAccess.createMetadataElementInStore(OpenMetadataType.CATALOG.typeName,
                                                                                            ElementStatus.ACTIVE,
                                                                                            this.getElementPropertiesForCatalog(ucCatalogQualifiedName, ucCatalog));

                            this.getContext().setMetadataSourceQualifiedName(ucCatalogGUID, ucCatalogQualifiedName);
                            this.getContext().addExternalIdentifier(ucCatalogGUID,
                                                                    OpenMetadataType.CATALOG.typeName,
                                                                    this.getExternalIdentifierProperties(ucCatalog));
                            addCatalogTarget(ucServerGUID, ucCatalogQualifiedName, ucCatalog.getName(), configurationProperties);

                        }
                    }
                }
            }

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

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());

        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                ucCatalog.getProperties());

        return elementProperties;
    }


    /**
     * Add a catalog target relationship between the UC server's asset and the connector that is able to
     * catalog inside a UC catalog.  This will start the cataloging of the datasets within this UC catalog.
     *
     * @param ucServerGUID unique identifier of the server asset - this is null if the UC Server was passed as an endpoint nor a catalog target
     * @param ucCatalogQualifiedName qualified name of the UC Catalog's software capability - becomes metadataSourceQualifiedName
     * @param ucCatalogName name of the catalog - may be used as a placeholder property
     * @param configurationProperties configuration properties for this server
     *
     * @throws ConnectorCheckedException the connector should shut down
     * @throws InvalidParameterException error from call to the metadata store
     * @throws PropertyServerException error from call to the metadata store
     * @throws UserNotAuthorizedException error from call to the metadata store
     */
    private void addCatalogTarget(String              ucServerGUID,
                                  String              ucCatalogQualifiedName,
                                  String              ucCatalogName,
                                  Map<String, Object> configurationProperties) throws ConnectorCheckedException,
                                                                                      InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "addCatalogTarget";

        if (ucServerGUID != null)
        {
            String friendshipConnectorGUID = getFriendshipGUID(configurationProperties);

            if (friendshipConnectorGUID != null)
            {
                CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

                catalogTargetProperties.setCatalogTargetName(ucCatalogName);
                catalogTargetProperties.setMetadataSourceQualifiedName( ucCatalogQualifiedName);

                Map<String, Object> targetConfigurationProperties = new HashMap<>();

                targetConfigurationProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), ucCatalogName);

                catalogTargetProperties.setConfigurationProperties(targetConfigurationProperties);

                String relationshipGUID = getContext().addCatalogTarget(defaultFriendshipGUID, ucServerGUID, catalogTargetProperties);

                auditLog.logMessage(methodName,
                                    UCAuditCode.NEW_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                        relationshipGUID,
                                                                                        friendshipConnectorGUID,
                                                                                        ucServerGUID,
                                                                                        ucCatalogName));
            }
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
