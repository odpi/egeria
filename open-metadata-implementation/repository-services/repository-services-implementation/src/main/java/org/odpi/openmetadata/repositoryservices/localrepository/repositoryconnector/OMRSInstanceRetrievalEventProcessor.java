/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;


/**
 * OMRSInstanceRetrievalEventProcessor defines the interface used by the Enterprise OMRS Repository Connector
 * to pass instance metadata retrieved from remote open metadata repository connectors.
 */
public interface OMRSInstanceRetrievalEventProcessor
{
    /**
     * Pass an entity that has been retrieved from a remote open metadata repository, so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param entity the retrieved entity.
     */
    void processRetrievedEntitySummary(String        sourceName,
                                       String        metadataCollectionId,
                                       EntitySummary entity);


    /**
     * Pass an entity that has been retrieved from a remote open metadata repository, so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param entity the retrieved entity.
     */
    void processRetrievedEntityDetail(String        sourceName,
                                      String        metadataCollectionId,
                                      EntityDetail  entity);


    /**
     * Pass a relationship that has been retrieved from a remote open metadata repository, so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param relationship the retrieved relationship
     */
    void processRetrievedRelationship(String         sourceName,
                                      String         metadataCollectionId,
                                      Relationship   relationship);

}
