/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * OMRSRepositoryEventMapper is the interface to a connector that is converting events received from
 * a non-native local metadata repository into OMRS compliant repository events.  It is used when the Open Metadata
 * and Governance Server is being used as a RepositoryProxy, or if the local metadata repository has
 * additional APIs that mean metadata can be changed without going through the OMRS Repository Connectors.
 */
public interface OMRSRepositoryEventMapper
{
    /**
     * Pass additional information to the connector needed to process events.
     *
     * @param repositoryEventMapperName repository event mapper name used for the source of the OMRS events.
     * @param repositoryConnector this is the connector to the local repository that the event mapper is processing
     *                            events from.  The repository connector is used to retrieve additional information
     *                            necessary to fill out the OMRS Events.
     */
    void initialize(String                  repositoryEventMapperName,
                    OMRSRepositoryConnector repositoryConnector);


    /**
     * Set up a repository helper object for the repository connector to use.
     *
     * @param repositoryHelper helper object for building TypeDefs and metadata instances.
     */
    void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper);


    /**
     * Set up a repository validator for the repository connector to use.
     *
     * @param repositoryValidator validator object to check the validity of TypeDefs and metadata instances.
     */
    void setRepositoryValidator(OMRSRepositoryValidator repositoryValidator);


    /**
     * Set up the name of the server where the metadata collection resides.
     *
     * @param serverName String name
     */
    void  setServerName(String serverName);


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    void setServerType(String serverType);


    /**
     * Set up the name of the organization that runs/owns the server.
     *
     * @param organizationName String organization name
     */
    void setOrganizationName(String organizationName);


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param serverUserId string user id
     */
    void setServerUserId(String serverUserId);


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    void setMetadataCollectionId(String metadataCollectionId);


    /**
     * Set up the repository event processor for this connector to use.  The connector should pass
     * each typeDef or instance metadata change reported by its metadata repository's metadata on to the
     * repository event processor.
     *
     * @param repositoryEventProcessor listener responsible for distributing notifications of local
     *                                changes to metadata types and instances to the rest of the
     *                                open metadata repository cohort.
     */
    void setRepositoryEventProcessor(OMRSRepositoryEventProcessor repositoryEventProcessor);
}
