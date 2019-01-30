/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.ReportHandler;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exceptions.PropertyServerException;

/**
 * AssetLineageInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetCatalogAdmin class.
 */
class AssetLineageInstanceHandler
{
    private static AssetLineageServicesInstanceMap   instanceMap = new AssetLineageServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    AssetLineageInstanceHandler() {
        new AssetLineageOMASRegistration();
    }


    /**
     * Retrieve the report creator for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return reportCreator for exclusive use by the requested instance
     * @throws PropertyServerException no instance for this server
     */
    ReportHandler getReportCreator(String serverName) throws PropertyServerException {
        AssetLineageServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getReportHandler();
        } else {
            final String methodName = "getReportCreator";

            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.SERVICE_NOT_INITIALIZED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new PropertyServerException(this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
