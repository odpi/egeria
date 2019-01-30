/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

import org.odpi.openmetadata.accessservices.assetlineage.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.exceptions.PropertyServerException;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * AssetLineageInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetLineageAdmin class.
 */
class AssetLineageInstanceHandler
{
    private static AssetLineageServicesInstanceMap instanceMap   = new AssetLineageServicesInstanceMap();
    private static AccessServiceDescription         myDescription = AccessServiceDescription.ASSET_CONSUMER_OMAS;

    /**
     * Default constructor registers the access service
     */
    AssetLineageInstanceHandler() {
        AssetLineageRegistration.registerAccessService();
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
        AssetLineageServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getMetadataCollection();
        }
        else
        {
            final String methodName = "getMetadataCollection";

            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return the Governance Engine's official Access Service Name
     *
     * @return String name
     */
    public String  getAccessServiceName()
    {
        return myDescription.getAccessServiceName();
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
        AssetLineageServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getRepositoryConnector();
        }
        else
        {
            final String methodName = "getRepositoryConnector";

            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.SERVICE_NOT_INITIALIZED;
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
