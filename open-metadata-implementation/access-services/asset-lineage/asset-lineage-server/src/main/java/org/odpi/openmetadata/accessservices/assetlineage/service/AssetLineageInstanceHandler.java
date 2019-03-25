/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.service;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * AssetLineageInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetLineageAdmin class.
 */
class AssetLineageInstanceHandler {
    private static AssetLineageServicesInstanceMap instanceMap = new AssetLineageServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    AssetLineageInstanceHandler() {
        new AssetLineageOMASRegistration();
    }

    
    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSMetadataCollection getMetadataCollection(String serverName) throws PropertyServerException {
        AssetLineageServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance == null) {
            throwServiceNotInitializedException(serverName, "getMetadataCollection");
        }

        return instance.getMetadataCollection();
    }

    private OMRSRepositoryHelper throwServiceNotInitializedException(String serverName, String getRepositoryHelper) throws PropertyServerException {
        final String methodName = getRepositoryHelper;

        AssetLineageErrorCode errorCode = AssetLineageErrorCode.SERVICE_NOT_INITIALIZED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

        throw new PropertyServerException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
}
