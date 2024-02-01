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
    /**
     * An event that is not recognized by the local server.
     */
    UNKNOWN_INSTANCE_EVENT           (0,  "UnknownInstanceEvent",
                                          "An event that is not recognized by the local server."),

    /**
     * A new entity has been created.
     */
    NEW_ENTITY_EVENT                 (1,  "NewEntityEvent",
                                          "A new entity has been created."),

    /**
     * An existing entity has been updated.
     */
    UPDATED_ENTITY_EVENT             (2,  "UpdatedEntityEvent",
                                          "An existing entity has been updated."),

    /**
     * An update to an entity has been undone.
     */
    UNDONE_ENTITY_EVENT              (3,  "UndoneEntityEvent",
                                          "An update to an entity has been undone."),
    /**
     * A new classification has been added to an entity.
     */
    CLASSIFIED_ENTITY_EVENT          (4,  "ClassifiedEntityEvent",
                                          "A new classification has been added to an entity."),

    /**
     * A classification has been removed from an entity.
     */
    DECLASSIFIED_ENTITY_EVENT        (5,  "DeclassifiedEntityEvent",
                                          "A classification has been removed from an entity."),

    /**
     * An existing classification has been changed on an entity.
     */
    RECLASSIFIED_ENTITY_EVENT        (6,  "ReclassifiedEntityEvent",
                                          "An existing classification has been changed on an entity."),

    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository but it is no longer returned on queries.
     */
    DELETED_ENTITY_EVENT             (7,  "DeletedEntityEvent",
                                          "An existing entity has been deleted.  This is a soft delete. " +
                                           "This means it is still in the repository " +
                                           "but it is no longer returned on queries."),

    /**
     * A deleted entity has been permanently removed from the repository. This request can not be undone.
     */
    PURGED_ENTITY_EVENT              (8,  "PurgedEntityEvent",
                                          "A deleted entity has been permanently removed from the repository. " +
                                           "This request can not be undone."),

    /**
     * A deleted entity has been restored to the state it was before it was deleted.
     */
    RESTORED_ENTITY_EVENT            (9,  "RestoredEntityEvent",
                                          "A deleted entity has been restored to the state it was before it was deleted."),

    /**
     * The guid of an existing entity has been changed to a new value.
     */
    RE_IDENTIFIED_ENTITY_EVENT       (10, "ReIdentifiedEntityEvent",
                                          "The guid of an existing entity has been changed to a new value."),

    /**
     * An existing entity has had its type changed.
     */
    RETYPED_ENTITY_EVENT             (11, "ReTypedEntityEvent",
                                          "An existing entity has had its type changed."),

    /**
     * An existing entity has changed home repository.
     */
    RE_HOMED_ENTITY_EVENT            (12, "ReHomedEntityEvent",
                                          "An existing entity has changed home repository."),

    /**
     * The local repository is requesting that an entity from another repository's metadata collection is refreshed
     * so the local repository can create a reference copy.
     */
    REFRESH_ENTITY_REQUEST           (13, "RefreshEntityRequestEvent",
                                          "The local repository is requesting that an entity from another repository's " +
                                           "metadata collection is " +
                                           "refreshed so the local repository can create a reference copy."),

    /**
     * A remote repository in the cohort has sent entity details in response to a refresh request.
     */
    REFRESHED_ENTITY_EVENT           (14, "RefreshedEntityEvent",
                                          "A remote repository in the cohort has sent entity details in response to a refresh request."),

    /**
     * A new relationship has been created.
     */
    NEW_RELATIONSHIP_EVENT           (15, "NewRelationshipEvent",
                                          "A new relationship has been created."),

    /**
     * An existing relationship has been updated.
     */
    UPDATED_RELATIONSHIP_EVENT       (16, "UpdateRelationshipEvent",
                                          "An existing relationship has been updated."),

    /**
     * An earlier change to a relationship has been undone.
     */
    UNDONE_RELATIONSHIP_EVENT        (17, "UndoneRelationshipEvent",
                                          "An earlier change to a relationship has been undone."),

    /**
     * An existing relationship has been deleted.  This is a soft delete.
     * This means it is still in the repository but it is no longer returned on queries.
     */
    DELETED_RELATIONSHIP_EVENT       (18, "DeletedRelationshipEvent",
                                          "An existing relationship has been deleted.  This is a soft delete. " +
                                           "This means it is still in the repository " +
                                           "but it is no longer returned on queries."),

    /**
     * A deleted relationship has been permanently removed from the repository. This request can not be undone.
     */
    PURGED_RELATIONSHIP_EVENT        (19, "PurgedRelationshipEvent",
                                          "A deleted relationship has been permanently removed from the repository. " +
                                           "This request can not be undone."),

    /**
     * A deleted relationship has been restored to the state it was before it was deleted.
     */
    RESTORED_RELATIONSHIP_EVENT      (20, "RestoredRelationshipEvent",
                                          "A deleted relationship has been restored to the state it was before it was deleted."),

    /**
     * The guid of an existing relationship has changed.
     */
    RE_IDENTIFIED_RELATIONSHIP_EVENT (21, "ReIdentifiedRelationshipEvent",
                                          "The guid of an existing relationship has changed."),

    /**
     * An existing relationship has had its type changed.
     */
    RETYPED_RELATIONSHIP_EVENT       (22, "ReTypedRelationshipEvent",
                                          "An existing relationship has had its type changed."),

    /**
     * An existing relationship has changed home repository.
     */
    RE_HOMED_RELATIONSHIP_EVENT      (23, "ReHomedRelationshipEvent",
                                          "An existing relationship has changed home repository."),

    /**
     * A repository has requested the home repository of a relationship send details of the relationship so
     * the local repository can create a reference copy of the instance.
     */
    REFRESH_RELATIONSHIP_REQUEST     (24, "RefreshRelationshipRequestEvent",
                                          "A repository has requested the home repository of a relationship send " +
                                                  "details of the relationship so " +
                                                  "the local repository can create a reference copy of the instance."),

    /**
     * The local repository is refreshing the information about a relationship for the other repositories in the cohort.
     */
    REFRESHED_RELATIONSHIP_EVENT     (25, "RefreshedRelationshipEvent",
                                          "The local repository is refreshing the information about a relationship for the " +
                                           "other repositories in the cohort."),

    /**
     * The local repository is passing a batch of instances to the other repositories in the cohort.  They may be new, or updated and include
     * instances originating from different repositories.
     */
    BATCH_INSTANCES_EVENT            (26, "BatchInstancesEvent",
                                          "The local repository is passing a batch of instances to the other " +
                                                 "repositories in the cohort.  They may be new, or updated and include " +
                                                 "instances originating from different repositories."),

    /**
     * An existing active entity has been permanently removed from the repository. This request can not be undone.
     */
    DELETE_PURGED_ENTITY_EVENT       (27, "DeletePurgedEntityEvent",
                                          "An existing active entity has been permanently removed from the repository. " +
                                             "This request can not be undone."),

    /**
     * An existing active relationship has been permanently removed from the repository. This request can not be undone.
     */
    DELETE_PURGED_RELATIONSHIP_EVENT (28, "DeletePurgedEntityEvent",
                                          "An existing active relationship has been permanently removed from the repository. " +
                                            "This request can not be undone."),

    /**
     * An error has been detected in the exchange of instances between members of the cohort.
     */
    INSTANCE_ERROR_EVENT             (99, "InstanceErrorEvent",
                                          "An error has been detected in the exchange of instances between members of the cohort.")
    ;


    private static final long serialVersionUID = 1L;

    private final int    ordinal;
    private final String name;
    private final String description;


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
