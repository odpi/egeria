/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetsearch.admin.serviceinstances;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;

/**
 * AssetSearchViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AssetSearchViewServicesInstance extends OMVSServiceInstance
{
    private final AssetCatalog assetCatalog;
    /**
     * Set up the objects for the asset search view service
     * @param serverName name of the server
     * @param auditLog audit log
     * @param localServerUserId local server userId
     * @param maxPageSize maximum page size, for use with paging requests
     * @param metadataServerName  metadata server name
     * @param metadataServerURL metadata server URL
     */
    public AssetSearchViewServicesInstance(String serverName,
                                OMRSAuditLog auditLog,
                                String       localServerUserId,
                                int          maxPageSize,
                                String       metadataServerName,
                                String       metadataServerURL) throws InvalidParameterException {
        super(serverName, ViewServiceDescription.ASSET_SEARCH.getViewServiceName(), auditLog, localServerUserId, maxPageSize, metadataServerName,
                metadataServerURL);
        this.assetCatalog = new AssetCatalog(metadataServerName, metadataServerURL);
    }

    public String getViewServiceName()
    {
        return ViewServiceDescription.ASSET_SEARCH.getViewServiceName();
    }

    public AssetCatalog getAssetCatalogClient() {
        return assetCatalog;
    }
}
