/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.outtopic;

import org.odpi.openmetadata.accessservices.communityprofile.server.PersonalProfilesHandler;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommunityProfilePublisher is responsible for publishing events about personalProfiles.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds events to the Community Profile OMAS
 * out topic.
 */
public class CommunityProfilePublisher
{
    private static final String personalProfileTypeName                  = "PersonalProfile";

    private static final Logger log = LoggerFactory.getLogger(CommunityProfilePublisher.class);

    private Connection              communityProfileOutTopic;
    private OMRSRepositoryHelper    repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private PersonalProfilesHandler profilesHandler;
    private String                  serviceName;


    /**
     * The constructor is given the connection to the out topic for Community Profile OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param communityProfileOutTopic  connection to the out topic
     * @param repositoryConnector  provides methods for retrieving metadata instances
     * @param repositoryHelper  provides methods for working with metadata instances
     * @param repositoryValidator  provides validation of metadata instance
     * @param serviceName  name of this service.
     */
    public CommunityProfilePublisher(Connection              communityProfileOutTopic,
                                     OMRSRepositoryConnector repositoryConnector,
                                     OMRSRepositoryHelper    repositoryHelper,
                                     OMRSRepositoryValidator repositoryValidator,
                                     String                  serviceName)
    {
        this.communityProfileOutTopic = communityProfileOutTopic;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.profilesHandler = new PersonalProfilesHandler(serviceName, repositoryConnector);
        this.serviceName = serviceName;
    }


    /**
     * Determine whether a new entity is an PersonalProfile.  If it is then publish an Community Profile Event about it.
     *
     * @param entity - entity object that has just been created.
     */
    public void processNewEntity(EntityDetail entity)
    {
        String personalProfileType = getPersonalProfileType(entity);

        if (personalProfileType != null)
        {
            this.processNewPersonalProfile(this.getPersonalProfile(entity));
        }
    }


    /**
     * Determine whether an updated entity is an PersonalProfile.  If it is then publish an Community Profile Event about it.
     *
     * @param entity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   entity)
    {
        String personalProfileType = getPersonalProfileType(entity);

        if (personalProfileType != null)
        {
            this.processUpdatedPersonalProfile(this.getPersonalProfile(entity));
        }
    }


    /**
     * Determine whether an updated entity is an PersonalProfile.  If it is then publish an Community Profile Event about it.
     *
     * @param originalEntity - original values for entity object - available when basic property updates have
     *                       occurred on the entity.
     * @param newEntity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   originalEntity,
                                     EntityDetail   newEntity)
    {
        String personalProfileType = getPersonalProfileType(newEntity);

        if (personalProfileType != null)
        {
            this.processUpdatedPersonalProfile(this.getPersonalProfile(originalEntity),
                                     this.getPersonalProfile(newEntity));
        }
    }


    /**
     * Determine whether a deleted entity is an PersonalProfile.  If it is then publish an Community Profile Event about it.
     *
     * @param entity - entity object that has just been deleted.
     */
    public void processDeletedEntity(EntityDetail   entity)
    {
        String personalProfileType = getPersonalProfileType(entity);

        if (personalProfileType != null)
        {
            this.processDeletedPersonalProfile(this.getPersonalProfile(entity));
        }
    }


    /**
     * Determine whether a restored entity is an PersonalProfile.  If it is then publish an Community Profile Event about it.
     *
     * @param entity - entity object that has just been restored.
     */
    public void processRestoredEntity(EntityDetail   entity)
    {
        String personalProfileType = getPersonalProfileType(entity);

        if (personalProfileType != null)
        {
            this.processRestoredPersonalProfile(this.getPersonalProfile(entity));
        }
    }


    /**
     * Determine whether a new relationship is related to an PersonalProfile.
     * If it is then publish an Community Profile Event about it.
     *
     * @param relationship - relationship object that has just been created.
     */
    public void processNewRelationship(Relationship relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an PersonalProfile.
     * If it is then publish an Community Profile Event about it.
     *
     * @param relationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an PersonalProfile.
     * If it is then publish an Community Profile Event about it.
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
     * Determine whether a deleted relationship is related to an PersonalProfile.
     * If it is then publish an Community Profile Event about it.
     *
     * @param relationship - relationship object that has just been deleted.
     */
    public void processDeletedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether a restored relationship is related to an PersonalProfile.
     * If it is then publish an Community Profile Event about it.
     *
     * @param relationship - relationship object that has just been restored.
     */
    public void processRestoredRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Return the name of the PersonalProfile type if this entity has a type that inherits from PersonalProfile.
     *
     * @param entity - entity to test
     * @return String containing PersonalProfile type name, or null if not an PersonalProfile.
     */
    private String getPersonalProfileType(EntityDetail  entity)
    {
        final   String   methodName = "getPersonalProfileType";

        if (repositoryValidator.isATypeOf(serviceName, entity, personalProfileTypeName, methodName))
        {
            InstanceType entityType = entity.getType();

            if (entityType != null)
            {
                return entityType.getTypeDefName();
            }
        }

        return null;
    }


    /**
     * Convert an entity into a personal profile object.
     *
     * @param entity object from the metadata repository
     * @return object used in events
     */
    private PersonalProfile getPersonalProfile(EntityDetail   entity)
    {
        // TODO
        return null;
    }


    /**
     * Publish event about a new personalProfile.
     *
     * @param personalProfile - personalProfile to report on.
     */
    private void processNewPersonalProfile(PersonalProfile   personalProfile)
    {
        log.info("Community Profile Event => New PersonalProfile: " + personalProfile.toString());
    }


    /**
     * Publish event about an updated personalProfile.
     *
     * @param personalProfile - personalProfile to report on.
     */
    private void processUpdatedPersonalProfile(PersonalProfile   personalProfile)
    {
        log.info("Community Profile Event => Updated PersonalProfile: " + personalProfile.toString());
    }


    /**
     * Publish event about an updated personalProfile.
     *
     * @param originalPersonalProfile - original values.
     * @param newPersonalProfile - personalProfile to report on.
     */
    private void processUpdatedPersonalProfile(PersonalProfile   originalPersonalProfile,
                                     PersonalProfile   newPersonalProfile)
    {
        log.info("Community Profile Event => Original PersonalProfile: " + originalPersonalProfile.toString());
        log.info("Community Profile Event => Updated PersonalProfile: "  + newPersonalProfile.toString());
    }


    /**
     * Publish event about a deleted personalProfile.
     *
     * @param personalProfile - personalProfile to report on.
     */
    private void processDeletedPersonalProfile(PersonalProfile   personalProfile)
    {
        log.info("Community Profile Event => Deleted PersonalProfile: " + personalProfile.toString());
    }


    /**
     * Publish event about a restored personalProfile.
     *
     * @param personalProfile - personalProfile to report on.
     */
    private void processRestoredPersonalProfile(PersonalProfile personalProfile)
    {
        log.info("Community Profile Event => Restored PersonalProfile: " + personalProfile.toString());
    }
}
