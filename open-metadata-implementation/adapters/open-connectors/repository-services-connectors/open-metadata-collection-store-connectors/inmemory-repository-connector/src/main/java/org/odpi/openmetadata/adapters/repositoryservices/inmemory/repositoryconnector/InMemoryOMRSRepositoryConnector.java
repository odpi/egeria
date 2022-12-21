/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * The InMemoryOMRSRepositoryConnector is a connector to a local in memory repository.  It is used for test,
 * small scale fixed or temporary repositories where the initial content comes from open metadata archives and
 * other members of connected open metadata repository cohorts.
 */
public class InMemoryOMRSRepositoryConnector extends OMRSRepositoryConnector
{
    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public InMemoryOMRSRepositoryConnector()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Set up the unique id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    @Override
    public void setMetadataCollectionId(String     metadataCollectionId)
    {
        super.metadataCollectionId = metadataCollectionId;

        if (metadataCollectionId != null)
        {
            /*
             * Initialize the metadata collection only once the connector is properly set up.
             */
            super.metadataCollection = new InMemoryOMRSMetadataCollection(this,
                                                                          super.serverName,
                                                                          repositoryHelper,
                                                                          repositoryValidator,
                                                                          metadataCollectionId);
        }
    }
}