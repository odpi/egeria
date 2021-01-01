/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * The OMRSRESTRepositoryConnector is a connector to a remote Apache Atlas repository (or any other metadata repository
 * that supports the OMRS REST APIs).  This is the connector used by the EnterpriseOMRSRepositoryConnector to make a direct call
 * to another open metadata repository.
 */
public class OMRSRESTRepositoryConnector extends OMRSRepositoryConnector
{
    private OMRSRESTMetadataCollection  metadataCollection   = null;
    private RepositoryErrorException    metadataCollectionException = null;

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public OMRSRESTRepositoryConnector()
    {
        /*
         * Nothing to do (yet !)
         */
    }


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    @Override
    public void setMetadataCollectionId(String     metadataCollectionId)
    {
        super.setMetadataCollectionId(metadataCollectionId);

        try
        {
            /*
             * Initialize the metadata collection.
             */
            metadataCollection = new OMRSRESTMetadataCollection(this,
                                                                super.serverName,
                                                                super.repositoryName,
                                                                repositoryHelper,
                                                                repositoryValidator,
                                                                metadataCollectionId);
        }
        catch (RepositoryErrorException  error)
        {
            metadataCollectionException = error;
            metadataCollection = null;
        }
    }


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataInstanceStore - metadata information retrieved from the metadata repository.
     * @throws RepositoryErrorException no metadata collection
     */
    @Override
    public OMRSMetadataCollection getMetadataCollection() throws RepositoryErrorException
    {
        if (metadataCollection == null)
        {
            if (metadataCollectionException != null)
            {
                throw metadataCollectionException;
            }
            else
            {
                final String methodName = "getMetadataCollection";

                throw new OMRSLogicErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(super.serverName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }

        return metadataCollection;
    }
}