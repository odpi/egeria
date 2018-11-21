/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * ConnectedAssetInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ConnectedAssetAdmin class.
 */
class ConnectedAssetInstanceHandler
{
    private static ConnectedAssetServicesInstanceMap   instanceMap = new ConnectedAssetServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    ConnectedAssetInstanceHandler() {
        ConnectedAssetRegistration.registerAccessService();
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSMetadataCollection getMetadataCollection(String serverName) throws PropertyServerException
    {
        ConnectedAssetServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getMetadataCollection();
        }
        else
        {
            final String methodName = "getMetadataCollection";

            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.SERVICE_NOT_INITIALIZED;
            String                  errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return repository connector for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSRepositoryConnector getRepositoryConnector(String serverName) throws PropertyServerException
    {
        ConnectedAssetServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getRepositoryConnector();
        }
        else
        {
            final String methodName = "getRepositoryConnector";

            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.SERVICE_NOT_INITIALIZED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
