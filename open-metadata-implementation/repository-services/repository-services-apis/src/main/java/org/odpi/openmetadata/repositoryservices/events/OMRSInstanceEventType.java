/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSInstanceEventType defines the different types of instance events in the open metadata repository services
 * protocol.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSInstanceEventType implements Serializable
{
    UNKNOWN_INSTANCE_EVENT          (0,  "UnknownInstanceEvent",
                                         "An event that is not recognized by the local server."),
    NEW_ENTITY_EVENT                (1,  "NewEntityEvent",
                                         "A new entity has been created."),
    UPDATED_ENTITY_EVENT            (2,  "UpdatedEntityEvent",
                                         "An existing entity has been updated."),
    UNDONE_ENTITY_EVENT             (3,  "UndoneEntityEvent",
                                         "An update to an entity has been undone."),
    CLASSIFIED_ENTITY_EVENT         (4,  "ClassifiedEntityEvent",
                                         "A new classification has been added to an entity."),
    DECLASSIFIED_ENTITY_EVENT       (5,  "DeclassifiedEntityEvent",
                                         "A classification has been removed from an entity."),
    RECLASSIFIED_ENTITY_EVENT       (6,  "ReclassifiedEntityEvent",
                                         "An existing classification has been changed on an entity."),
    DELETED_ENTITY_EVENT            (7,  "DeletedEntityEvent",
                                         "An existing entity has been deleted.  This is a soft delete. " +
                                          "This means it is still in the repository " +
                                          "but it is no longer returned on queries."),
    PURGED_ENTITY_EVENT             (8,  "PurgedEntityEvent",
                                         "A deleted entity has been permanently removed from the repository. " +
                                          "This request can not be undone."),
    RESTORED_ENTITY_EVENT           (9,  "RestoredEntityEvent",
                                         "A deleted entity has been restored to the state it was before it was deleted."),
    RE_IDENTIFIED_ENTITY_EVENT      (10, "ReIdentifiedEntityEvent",
                                         "The guid of an existing entity has been changed to a new value."),
    RETYPED_ENTITY_EVENT            (11, "ReTypedEntityEvent",
                                         "An existing entity has had its type changed."),
    RE_HOMED_ENTITY_EVENT           (12, "ReHomedEntityEvent",
                                         "An existing entity has changed home repository."),
    REFRESH_ENTITY_REQUEST          (13, "RefreshEntityRequestEvent",
                                         "The local repository is requesting that an entity from another repository's " +
                                          "metadata collection is " +
                                          "refreshed so the local repository can create a reference copy."),
    REFRESHED_ENTITY_EVENT          (14, "RefreshedEntityEvent",
                                         "A remote repository in the cohort has sent entity details in response " +
                                          "to a refresh request."),
    NEW_RELATIONSHIP_EVENT          (15, "NewRelationshipEvent",
                                         "A new relationship has been created."),
    UPDATED_RELATIONSHIP_EVENT      (16, "UpdateRelationshipEvent",
                                         "An existing relationship has been updated."),
    UNDONE_RELATIONSHIP_EVENT       (17, "UndoneRelationshipEvent",
                                         "An earlier change to a relationship has been undone."),
    DELETED_RELATIONSHIP_EVENT      (18, "DeletedRelationshipEvent",
                                         "An existing relationship has been deleted.  This is a soft delete. " +
                                          "This means it is still in the repository " +
                                          "but it is no longer returned on queries."),
    PURGED_RELATIONSHIP_EVENT       (19, "PurgedRelationshipEvent",
                                         "A deleted relationship has been permanently removed from the repository. " +
                                          "This request can not be undone."),
    RESTORED_RELATIONSHIP_EVENT     (20, "RestoredRelationshipEvent",
                                         "A deleted relationship has been restored to the state it was before it was deleted."),
    RE_IDENTIFIED_RELATIONSHIP_EVENT(21, "ReIdentifiedRelationshipEvent",
                                         "The guid of an existing relationship has changed."),
    RETYPED_RELATIONSHIP_EVENT      (22, "ReTypedRelationshipEvent",
                                         "An existing relationship has had its type changed."),
    RE_HOMED_RELATIONSHIP_EVENT     (23, "ReHomedRelationshipEvent",
                                         "An existing relationship has changed home repository."),
    REFRESH_RELATIONSHIP_REQUEST    (24, "RefreshRelationshipRequestEvent",
                                         "A repository has requested the home repository of a relationship send " +
                                                "details of hte relationship so " +
                                                "the local repository can create a reference copy of the instance."),
    REFRESHED_RELATIONSHIP_EVENT    (25, "RefreshedRelationshipEvent",
                                         "The local repository is refreshing the information about a relationship for the " +
                                          "other repositories in the cohort."),
    BATCH_INSTANCES_EVENT           (26, "BatchInstancesEvent",
                                         "The local repository is passing a batch of instances to the other " +
                                                 "repositories in the cohort.  They may be new, or updated and include " +
                                                 "instances originating from different repositories."),
    INSTANCE_ERROR_EVENT            (99, "InstanceErrorEvent",
                                         "An error has been detected in the exchange of instances between members of the cohort.")
    ;


    private static final long serialVersionUID = 1L;

    private  int    ordinal;
    private  String name;
    private  String description;


    /**
     * Default Constructor sets up the specific values for this instance of the enum.
     *
     * @param ordinal int identifier used for indexing based on the enum.
     * @param name string name used for messages that include the enum.
     * @param description default description for the enum value used when natural resource
     *                   bundle is not available.
     */
    OMRSInstanceEventType(int ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the enum value.  This is used when natural resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSInstanceEventType{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
