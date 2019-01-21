/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance
{
    private OMRSRepositoryConnector repositoryConnector;
    private OMRSMetadataCollection  metadataCollection;
    private String                  serverName;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetLineageServicesInstance(OMRSRepositoryConnector repositoryConnector) throws NewInstanceException
    {
        final String methodName = "new ServiceInstance";

        if (repositoryConnector != null)
        {
            try
            {
                this.repositoryConnector = repositoryConnector;
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();

                AssetLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
            }
            catch (Throwable error)
            {
                AssetLineageErrorCode errorCode    = AssetLineageErrorCode.OMRS_NOT_INITIALIZED;
                String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

                throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());

            }
        }
        else
        {
            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.OMRS_NOT_INITIALIZED;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());

        }
    }


    /**
     * Return the server name.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    public String getServerName() throws NewInstanceException
    {
        final String methodName = "getServerName";

        if (serverName != null)
        {
            return serverName;
        }
        else
        {
            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.OMRS_NOT_AVAILABLE;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());
        }
    }


    /**
     * Return the local metadata collection for this server.
     *
     * @return OMRSMetadataCollection object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public OMRSMetadataCollection getMetadataCollection() throws PropertyServerException
    {
        final String methodName = "getMetadataCollection";

        if ((repositoryConnector != null) && (metadataCollection != null) && (repositoryConnector.isActive()))
        {
            return metadataCollection;
        }
        else
        {
            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.OMRS_NOT_AVAILABLE;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public OMRSRepositoryConnector  getRepositoryConnector() throws PropertyServerException
    {
        final String methodName = "getRepositoryConnector";

        if ((repositoryConnector != null) && (metadataCollection != null) && (repositoryConnector.isActive()))
        {
            return repositoryConnector;
        }
        else
        {
            AssetLineageErrorCode errorCode    = AssetLineageErrorCode.OMRS_NOT_AVAILABLE;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        AssetLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
