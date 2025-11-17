/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSMetadataCollectionManager;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessor;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSInstanceEventProcessor;

import java.util.List;


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
     * Returns the repository helper for this repository.
     *
     * @return repository helper
     */
    OMRSRepositoryHelper getRepositoryHelper();


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


    /**
     * Set up the list of metadata collection identifiers from the remote members of a connected cohort.
     *
     * @param cohortName name of cohort
     * @param remoteCohortMetadataCollectionIds list of metadata collection ids
     */
    void setRemoteCohortMetadataCollectionIds(String       cohortName,
                                              List<String> remoteCohortMetadataCollectionIds);


    /**
     * Is the metadata collection id for an active member of the cohort.  This is used to set up the
     * provenance type correctly (between LOCAL_COHORT and DEREGISTERED_REPOSITORY).
     *
     * @param metadataCollectionId id to test
     * @return boolean flag meaning that the metadata collection is recognized
     */
    boolean isActiveCohortMember(String metadataCollectionId);
}
