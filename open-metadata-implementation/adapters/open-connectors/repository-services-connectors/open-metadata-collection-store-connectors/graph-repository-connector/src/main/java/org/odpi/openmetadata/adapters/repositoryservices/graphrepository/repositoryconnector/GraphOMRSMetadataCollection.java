/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.NotImplementedRuntimeException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * The GraphOMRSMetadataCollection provides a local open metadata repository that uses a graph store as its
 * persistence layer.
 */
public class GraphOMRSMetadataCollection extends OMRSMetadataCollectionBase
{

    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection Id.
     *
     * @param parentConnector - connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName - name of the repository - used for logging.
     * @param repositoryHelper - class used to build type definitions and instances.
     * @param repositoryValidator - class used to validate type definitions and instances.
     * @param metadataCollectionId - unique Identifier of the metadata collection Id.
     */
    public GraphOMRSMetadataCollection(GraphOMRSRepositoryConnector parentConnector,
                                       String                       repositoryName,
                                       OMRSRepositoryHelper         repositoryHelper,
                                       OMRSRepositoryValidator      repositoryValidator,
                                       String                       metadataCollectionId)
    {
        /*
         * The metadata collection Id is the unique Id for the metadata collection.  It is managed by the super class.
         */
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        /*
         * Save parentConnector since this has the connection information and access to the metadata about the
         * metadata cluster.
         */
        this.parentConnector = parentConnector;

        /*
         * This is a temporary implementation to allow the structural implementation of the connectors to
         * be committed before the metadata collection implementation is complete.
         */
        throw new NotImplementedRuntimeException("GraphOMRSMetadataCollection", "constructor", "DG-18");
    }
}