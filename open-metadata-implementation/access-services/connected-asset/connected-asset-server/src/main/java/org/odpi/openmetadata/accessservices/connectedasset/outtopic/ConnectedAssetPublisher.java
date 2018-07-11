/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.outtopic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * ConnectedAssetPublisher is responsible for publishing events about connected assets.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds events to the Connected Asset OMAS
 * out topic.
 */
public class ConnectedAssetPublisher
{
    private static final Logger log = LoggerFactory.getLogger(ConnectedAssetPublisher.class);

    private Connection              assetConsumerOutTopic;
    private OMRSRepositoryHelper    repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String                  componentName;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic - connection to the out topic
     * @param repositoryHelper - provides methods for working with metadata instances
     * @param repositoryValidator - provides validation of metadata instance
     * @param componentName - name of component
     */
    public ConnectedAssetPublisher(Connection              assetConsumerOutTopic,
                                   OMRSRepositoryHelper    repositoryHelper,
                                   OMRSRepositoryValidator repositoryValidator,
                                   String                  componentName)
    {
        this.assetConsumerOutTopic = assetConsumerOutTopic;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
    }


    /**
     * Determine whether a new entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been created.
     */
    public void processNewEntity(EntityDetail entity)
    {
        // todo
    }


    /**
     * Determine whether an updated entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   entity)
    {
        // todo
    }


    /**
     * Determine whether an updated entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param originalEntity - original values for entity object - available when basic property updates have
     *                       occurred on the entity.
     * @param newEntity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   originalEntity,
                                     EntityDetail   newEntity)
    {
        // todo
    }


    /**
     * Determine whether a deleted entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been deleted.
     */
    public void processDeletedEntity(EntityDetail   entity)
    {
        // todo
    }


    /**
     * Determine whether a restored entity is an Asset.  If it is then publish an Asset Consumer Event about it.
     *
     * @param entity - entity object that has just been restored.
     */
    public void processRestoredEntity(EntityDetail   entity)
    {
        // todo
    }


    /**
     * Determine whether a new relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been created.
     */
    public void processNewRelationship(Relationship relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param originalRelationship  - original values of relationship.
     * @param newRelationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   originalRelationship,
                                           Relationship   newRelationship)
    {
        // todo
    }


    /**
     * Determine whether a deleted relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been deleted.
     */
    public void processDeletedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether a restored relationship is related to an Asset.
     * If it is then publish an Asset Consumer Event about it.
     *
     * @param relationship - relationship object that has just been restored.
     */
    public void processRestoredRelationship(Relationship   relationship)
    {
        // todo
    }
}
