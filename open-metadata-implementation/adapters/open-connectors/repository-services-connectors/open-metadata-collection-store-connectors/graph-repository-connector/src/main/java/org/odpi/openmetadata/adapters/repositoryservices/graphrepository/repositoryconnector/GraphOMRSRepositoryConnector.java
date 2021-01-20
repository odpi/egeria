/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

/**
 * The GraphOMRSRepositoryConnector is a connector to a local open metadata repository that uses a graph store
 * for its persistence.
 */
public class GraphOMRSRepositoryConnector extends OMRSRepositoryConnector
{
    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public GraphOMRSRepositoryConnector()
    {
        /*
         * Nothing to do (yet !)
         */
    }


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    @Override
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        String methodName = "setMetadataCollectionId";

        super.metadataCollectionId = metadataCollectionId;

        try
        {

            if (metadataCollectionId != null)
            {
                /*
                 * Initialize the metadata collection only once the connector is properly set up.
                 */
                super.metadataCollection = new GraphOMRSMetadataCollection(this, super.serverName,
                                                                           repositoryHelper, repositoryValidator,
                                                                           metadataCollectionId, auditLog,
                                                                           connectionBean.getConfigurationProperties());
            }

        }
        catch (Throwable   error)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(repositoryName),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }
    }
}