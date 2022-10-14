/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.connectormanager;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;

/**
 * OMRSConnectorConsumer provides the interfaces for a connector consumer.  This is a component that needs to
 * maintain a current list of connectors to all the remote repositories in the open metadata repository cohorts that
 * the local server is a member of.
 */
public interface OMRSConnectorConsumer
{
    /**
     * Pass the connector for the local repository to the connector consumer.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection
     * @param localConnector OMRSRepositoryConnector object for the local repository.
     */
    void setLocalConnector(String                       metadataCollectionId,
                           LocalOMRSRepositoryConnector localConnector);


    /**
     * Pass the connector to one of the remote repositories in the metadata repository cohort.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection
     * @param remoteConnector OMRSRepositoryConnector object providing access to the remote repository.
     */
    void addRemoteConnector(String                  metadataCollectionId,
                            OMRSRepositoryConnector remoteConnector);


    /**
     * Pass the metadata collection id for a repository that has just left the metadata repository cohort.
     *
     * @param metadataCollectionId identifier of the metadata collection that is no longer available.
     */
    void removeRemoteConnector(String  metadataCollectionId);


    /**
     * Call disconnect on all registered connectors and stop calling them.  The OMRS is about to shutdown.
     */
    void disconnectAllConnectors();
}
