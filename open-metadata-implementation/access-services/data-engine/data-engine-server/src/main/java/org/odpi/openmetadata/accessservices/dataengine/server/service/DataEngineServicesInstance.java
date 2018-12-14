/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.exception.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.exception.NewInstanceException;
import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.dataengine.server.util.DataEngineErrorHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * DataEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DataEngineServicesInstance {
    private final DataEngineErrorHandler dataEngineErrorHandler = new DataEngineErrorHandler();

    private OMRSRepositoryConnector repositoryConnector;
    private OMRSMetadataCollection metadataCollection;
    private String serverName;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DataEngineServicesInstance(OMRSRepositoryConnector repositoryConnector) throws NewInstanceException {
        final String methodName = "new ServiceInstance";

        if (repositoryConnector != null) {
            try {
                this.repositoryConnector = repositoryConnector;
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();

                DataEngineServicesInstanceMap.setNewInstanceForJVM(serverName, this);
            } catch (Throwable error) {
                dataEngineErrorHandler.handleNewInstanceException(DataEngineErrorCode.OMRS_NOT_INITIALIZED, methodName);
            }
        } else {
            dataEngineErrorHandler.handleNewInstanceException(DataEngineErrorCode.OMRS_NOT_INITIALIZED, methodName);

        }
    }

    /**
     * Return the server name.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    public String getServerName() throws NewInstanceException {
        final String methodName = "getServerName";

        if (serverName != null) {
            return serverName;
        } else {
            dataEngineErrorHandler.handleNewInstanceException(DataEngineErrorCode.OMRS_NOT_AVAILABLE, methodName);
            return null;
        }
    }

    /**
     * Return the local metadata collection for this server.
     *
     * @return OMRSMetadataCollection object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSMetadataCollection getMetadataCollection() throws PropertyServerException {
        final String methodName = "getMetadataCollection";

        if (repositoryConnector != null && metadataCollection != null && repositoryConnector.isActive()) {
            return metadataCollection;
        } else {
            dataEngineErrorHandler.handlePropertyServerException(DataEngineErrorCode.OMRS_NOT_AVAILABLE, methodName);
            return null;
        }
    }

    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSRepositoryConnector getRepositoryConnector() throws PropertyServerException {
        final String methodName = "getRepositoryConnector";

        if (repositoryConnector != null && metadataCollection != null && repositoryConnector.isActive()) {
            return repositoryConnector;
        } else {
            dataEngineErrorHandler.handlePropertyServerException(DataEngineErrorCode.OMRS_NOT_AVAILABLE, methodName);
            return null;
        }
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        DataEngineServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
