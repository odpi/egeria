/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.connectormanager;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * OMRSConnectionConsumer provides the interfaces for a connection consumer.  This is a component that needs to
 * maintain a current list of connections to all the repositories in the open metadata repository cohort.
 */
public interface OMRSConnectionConsumer
{
    /**
     * Pass details of the connection for one of the remote repositories registered in a connected
     * open metadata repository cohort.
     *
     * @param cohortName name of the cohort adding the remote connection.
     * @param remoteServerName name of the remote server for this connection.
     * @param remoteServerType type of the remote server.
     * @param owningOrganizationName name of the organization the owns the remote server.
     * @param metadataCollectionId Unique identifier for the metadata collection
     * @param metadataCollectionName Display name for the metadata collection
     * @param remoteConnection Connection object providing properties necessary to create an
     *                         OMRSRepositoryConnector for the remote repository.
     * @throws ConnectionCheckedException there are invalid properties in the Connection
     * @throws ConnectorCheckedException there is a problem initializing the Connector
     */
    void addRemoteConnection(String         cohortName,
                             String         remoteServerName,
                             String         remoteServerType,
                             String         owningOrganizationName,
                             String         metadataCollectionId,
                             String         metadataCollectionName,
                             Connection     remoteConnection) throws ConnectionCheckedException, ConnectorCheckedException;


    /**
     * Pass details of the connection for the repository that has left one of the open metadata repository cohorts.
     *
     * @param cohortName name of the cohort removing the remote connection.
     * @param metadataCollectionId Unique identifier for the metadata collection.
     */
    void removeRemoteConnection(String         cohortName,
                                String         metadataCollectionId);


    /**
     * Remove all the remote connections for the requested open metadata repository cohort.
     *
     * @param cohortName name of the cohort
     */
    void removeCohort(String   cohortName);
}
