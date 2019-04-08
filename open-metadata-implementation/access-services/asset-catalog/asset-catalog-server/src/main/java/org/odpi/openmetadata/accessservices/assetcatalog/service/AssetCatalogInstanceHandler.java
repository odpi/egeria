/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * AssetCatalogInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetCatalogAdmin class.
 */
class AssetCatalogInstanceHandler {
    private static AssetCatalogServicesInstanceMap instanceMap = new AssetCatalogServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    AssetCatalogInstanceHandler() {
        AssetCatalogRegistration.registerAccessService();
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSMetadataCollection getMetadataCollection(String serverName) throws PropertyServerException {
        AssetCatalogServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance == null) {
            throwServiceNotInitializedException(serverName, "getMetadataCollection");
        }

        return instance.getMetadataCollection();
    }

    private OMRSRepositoryHelper throwServiceNotInitializedException(String serverName, String getRepositoryHelper) throws PropertyServerException {
        final String methodName = getRepositoryHelper;

        AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

        throw new PropertyServerException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
}
