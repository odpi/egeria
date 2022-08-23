/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSMetadataCollectionManager;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessor;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSInstanceEventProcessor;


/**
 * OMRSLocalRepository is an interface used by the OMRS components to retrieve information about the local
 * repository, to register listeners and to get access to the connector for the local repository.
 */
public interface OMRSLocalRepository extends OMRSMetadataCollectionManager
{
    /**
     * Returns the unique identifier (guid) of the local repository's metadata collection.
     *
     * @return String guid
     */
    String getMetadataCollectionId();


    /**
     * Returns the display name of the local repository's metadata collection.
     *
     * @return String guid
     */
    String getMetadataCollectionName();


    /**
     * Returns the Connection to the local repository that can be used by remote servers to create
     * an OMRS repository connector to call this server in order to access the local repository.
     *
     * @return Connection object
     */
    Connection getLocalRepositoryRemoteConnection();


    /**
     * Return the event manager that the local repository uses to
     *
     * @return outbound repository event manager
     */
    OMRSRepositoryEventManager getOutboundRepositoryEventManager();


    /**
     * Return the TypeDef event processor that should be passed all incoming TypeDef events received
     * from the cohorts that this server is a member of.
     *
     * @return OMRSTypeDefEventProcessor for the local repository.
     */
    OMRSTypeDefEventProcessor getIncomingTypeDefEventProcessor();


    /**
     * Return the instance event processor that should be passed all incoming instance events received
     * from the cohorts that this server is a member of.
     *
     * @return OMRSInstanceEventProcessor for the local repository.
     */
    LocalOMRSInstanceEventProcessor getIncomingInstanceEventProcessor();


    /**
     * Return the local server name.  This is used for outbound events.
     *
     * @return String name
     */
    String getLocalServerName();


    /**
     * Return the local server type.  This is used for outbound events.
     *
     * @return String name
     */
    String getLocalServerType();


    /**
     * Return the name of the organization that owns this local repository.
     *
     * @return String name
     */
    String getOrganizationName();
}
