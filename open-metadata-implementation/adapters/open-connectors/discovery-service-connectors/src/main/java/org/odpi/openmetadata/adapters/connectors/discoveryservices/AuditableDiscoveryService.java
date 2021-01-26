/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;


import java.util.ArrayList;
import java.util.List;

/**
 * AuditableDiscoveryService is a base class for discovery services that wish to use the audit log.
 */
public abstract class AuditableDiscoveryService extends DiscoveryService implements AuditLoggingComponent
{
    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Log that no asset has been returned to the discovery service.  It is unable to proceed without this basic information.
     *
     * @param assetGUID the unique identifier of the asset from the discovery context
     * @param methodName calling method
     *
     * @throws ConnectorCheckedException resulting exception
     */
    protected void logNoAsset(String    assetGUID,
                              String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(DiscoveryServiceErrorCode.NO_ASSET.getMessageDefinition(assetGUID,
                                                                                                    discoveryServiceName),
                                             this.getClass().getName(),
                                             methodName);
    }


    /**
     * Log that the discovery service can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetType type of the asset
     * @param supportedAssetType supported asset types
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void logWrongTypeOfAsset(String    assetGUID,
                                       String    assetType,
                                       String    supportedAssetType,
                                       String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(DiscoveryServiceErrorCode.INVALID_ASSET_TYPE.getMessageDefinition(assetGUID,
                                                                                                              assetType,
                                                                                                              discoveryServiceName,
                                                                                                              supportedAssetType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the type name for the asset.  An exception is thrown if the type name is not available.
     *
     * @param asset asset universe
     * @param methodName calling method
     * @return asset's type name
     * @throws ConnectorCheckedException resulting exception
     */
    protected String getAssetTypeName(AssetUniverse     asset,
                                      String            methodName) throws ConnectorCheckedException
    {
        AssetElementType elementType = asset.getType();

        if (elementType != null)
        {
            return elementType.getElementTypeName();
        }

        throw new ConnectorCheckedException(DiscoveryServiceErrorCode.NO_ASSET_TYPE.getMessageDefinition(asset.toString(), discoveryServiceName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the network address of this asset.
     *
     * @param asset asset to extract address from
     * @return the list of network addresses
     */
    protected List<String> getNetworkAddresses(AssetUniverse asset)
    {
        if (asset != null)
        {
            AssetConnections assetConnections = asset.getConnections();

            if (assetConnections != null)
            {
                List<String>  networkAddresses = new ArrayList<>();

                while (assetConnections.hasNext())
                {
                    ConnectionProperties connectionProperties = assetConnections.next();

                    if (connectionProperties != null)
                    {
                        EndpointProperties endpointProperties = connectionProperties.getEndpoint();

                        if (endpointProperties != null)
                        {
                            if (endpointProperties.getAddress() != null)
                            {
                                /*
                                 * Only add one copy of a specific address.
                                 */
                                if (! networkAddresses.contains(endpointProperties.getAddress()))
                                {
                                    networkAddresses.add(endpointProperties.getAddress());
                                }
                            }
                        }
                    }
                }

                if (! networkAddresses.isEmpty())
                {
                    return networkAddresses;
                }
            }
        }

        return null;
    }
}
