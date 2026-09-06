/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;

/**
 * BitolFilesReceiverIntegrationConnector monitors file directories for Bitol documents: Open Data Contract Standard
 * (ODCS) data contracts and Open Data Product Standard (ODPS) data products.  The directories are typically git
 * checkouts, or drop folders, where teams publish their contracts and product descriptors.  A directory is identified
 * either by the network address of the connection's endpoint, or by a FileFolder catalog target.  On each refresh, the
 * connector scans each directory tree for YAML and JSON files whose kind is DataContract or DataProduct and publishes
 * the new and changed documents to the Bitol listeners registered in the same integration daemon (for example the
 * cataloguer and store connectors).
 */
public class BitolFilesReceiverIntegrationConnector extends DynamicIntegrationConnectorBase
{
    private BitolDirectoryScanner endpointScanner = null;


    /**
     * Default constructor
     */
    public BitolFilesReceiverIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.  This picks up the optional
     * directory named in the connection's endpoint.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        Endpoint endpoint = connectionBean.getEndpoint();

        if ((endpoint != null) && (endpoint.getNetworkAddress() != null))
        {
            String sourceName = OpenMetadataType.ENDPOINT.typeName + "::" + OpenMetadataProperty.NETWORK_ADDRESS.name;

            endpointScanner = new BitolDirectoryScanner(new File(stripFilePrefix(endpoint.getNetworkAddress())), sourceName);

            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DIRECTORY_MONITORED.getMessageDefinition(connectorName,
                                                                                                            endpointScanner.getDirectory().getPath(),
                                                                                                            sourceName));
        }
    }


    /**
     * Remove the "file://" prefix used on some path names.
     *
     * @param pathName path name from an endpoint or catalog target
     * @return plain path name
     */
    static String stripFilePrefix(String pathName)
    {
        if (pathName.startsWith("file:///"))
        {
            return pathName.substring(7);
        }
        else if (pathName.startsWith("file://"))
        {
            return pathName.substring(7);
        }

        return pathName;
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * The endpoint's directory is scanned and then the catalog targets are refreshed by the dynamic base class, which calls
     * refresh() on each catalog target processor.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        if (endpointScanner != null)
        {
            endpointScanner.scanAndPublish(integrationContext, auditLog, connectorName);
        }

        super.refresh();
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).  FileFolder catalog
     * targets are scanned for Bitol documents; other catalog targets are ignored.
     *
     * @param retrievedCatalogTarget the catalog target information retrieved from the open metadata repository
     * @param catalogTargetContext specialized integration context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException problem setting up the processor
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException,
                                                                                                                      UserNotAuthorizedException
    {
        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader(),
                                    DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getAssociatedTypeName()))
        {
            return new BitolFilesCatalogTargetProcessor(retrievedCatalogTarget,
                                                        catalogTargetContext,
                                                        connectorToTarget,
                                                        connectorName,
                                                        auditLog);
        }

        return new RequestedCatalogTarget(retrievedCatalogTarget, catalogTargetContext, connectorToTarget);
    }
}
