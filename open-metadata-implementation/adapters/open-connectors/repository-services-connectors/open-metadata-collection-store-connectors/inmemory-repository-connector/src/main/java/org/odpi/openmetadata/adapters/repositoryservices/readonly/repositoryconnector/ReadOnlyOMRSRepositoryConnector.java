/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector;


import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnector;

/**
 * The ReadOnlyOMRSRepositoryConnector is a connector to a local in memory repository.  It is used for test,
 * small scale fixed or temporary repositories where the initial content comes from open metadata archives and
 * other members of connected open metadata repository cohorts.
 */
public class ReadOnlyOMRSRepositoryConnector extends InMemoryOMRSRepositoryConnector
{
    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public ReadOnlyOMRSRepositoryConnector()
    {
        /*
         * Nothing to do.
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
        super.metadataCollectionId = metadataCollectionId;

        if (metadataCollectionId != null)
        {
            /*
             * Initialize the metadata collection only once the connector is properly set up.
             */
            super.metadataCollection = new ReadOnlyOMRSMetadataCollection(this,
                                                                          super.serverName,
                                                                          repositoryHelper,
                                                                          repositoryValidator,
                                                                          metadataCollectionId);
        }
    }
}