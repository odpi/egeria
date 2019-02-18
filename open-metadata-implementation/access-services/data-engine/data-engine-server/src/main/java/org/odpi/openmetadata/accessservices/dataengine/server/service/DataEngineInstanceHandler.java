/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * DataEngineInstanceHandler retrieves information from the instance map for the access service instances.
 * The instance map is thread-safe. Instances are added and removed by the DataEngineAdmin class.
 */
class DataEngineInstanceHandler {
    private final DataEngineErrorHandler dataEngineErrorHandler = new DataEngineErrorHandler();

    private static DataEngineServicesInstanceMap instanceMap = new DataEngineServicesInstanceMap();
    private static AccessServiceDescription myDescription = AccessServiceDescription.DATA_ENGINE_OMAS;

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
            dataEngineErrorHandler.handlePropertyServerException(DataEngineErrorCode.SERVICE_NOT_INITIALIZED,
                    "getMetadataCollection");
            return null;
        }
    }

    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return repository connector for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSRepositoryConnector getRepositoryConnector(String serverName) throws PropertyServerException {
        DataEngineServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRepositoryConnector();
        } else {
            dataEngineErrorHandler.handlePropertyServerException(DataEngineErrorCode.SERVICE_NOT_INITIALIZED,
                    "getRepositoryConnector");
            return null;
        }
    }

    /**
     * Return the Data Engine's official Access Service Name
     *
     * @return String name
     */
    String getAccessServiceName() {
        return myDescription.getAccessServiceName();
    }
}

