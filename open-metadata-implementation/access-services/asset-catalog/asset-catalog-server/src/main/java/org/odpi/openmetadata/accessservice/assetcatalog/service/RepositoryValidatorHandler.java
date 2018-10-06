/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.service;

import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class RepositoryValidatorHandler {

    private OMRSRepositoryConnector repositoryConnector;

    public RepositoryValidatorHandler(OMRSRepositoryConnector repositoryConnector) {
        this.repositoryConnector = repositoryConnector;
    }

    public OMRSMetadataCollection getMetadataCollection() throws PropertyServerException {
        String className = this.getClass().getName();
        String methodName = "getMetadataCollection";

        if (repositoryConnector == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }

        if (!repositoryConnector.isActive()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.OMRS_NOT_AVAILABLE;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        try {
            return repositoryConnector.getMetadataCollection();
        } catch (Throwable error) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_METADATA_COLLECTION;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
}
