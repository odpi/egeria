/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;


/**
 * DataEngineInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DataEngineAdmin class.
 */
public class DataEngineInstanceHandler {
    private static DataEngineServicesInstanceMap instanceMap = new DataEngineServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    DataEngineInstanceHandler() {
        DataEngineRegistration.registerAccessService();
    }

    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSMetadataCollection getMetadataCollection(String serverName) throws PropertyServerException {
        DataEngineServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getMetadataCollection();
        } else {
            final String methodName = "getMetadataCollection";

            DataEngineErrorCode errorCode = DataEngineErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
}

