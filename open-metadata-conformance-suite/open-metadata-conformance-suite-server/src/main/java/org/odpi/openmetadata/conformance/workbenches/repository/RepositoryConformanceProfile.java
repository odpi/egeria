/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfilePriority;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RepositoryConformanceProfile defines the list of functional profiles for an open metadata repository.
 * An open metadata repository needs to support at least one profile to be conformant.  However,
 * in practice, metadata sharing is required in order to support any of the other profiles, so it is
 * effectively mandatory.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RepositoryConformanceProfile implements Serializable
{
    METADATA_SHARING               (0,  "Metadata sharing",
                                        "The technology under test is able to share metadata with other members of the cohort.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing",
                                        OpenMetadataConformanceProfilePriority.MANDATORY_PROFILE),
    REFERENCE_COPIES               (1,  "Reference copies",
                                        "The technology under test is able to save, lock and purge reference copies of metadata from other members of the cohort.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/reference-copies",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    METADATA_MAINTENANCE           (2,  "Metadata maintenance",
                                        "The technology under test supports requests to create, update and purge metadata instances.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/metadata-maintenance",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    DYNAMIC_TYPES                  (3,  "Dynamic types",
                                        "The technology under test supports changes to the list of its supported types while it is running.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/dynamic-types",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    GRAPH_QUERIES                  (4,  "Graph queries",
                                        "The technology under test supports graph-like queries that return collections of metadata instances.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/graph-queries",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    HISTORICAL_SEARCH              (5,  "Historical search",
                                        "The technology under test supports search for the state of the metadata instances at a specific time in the past.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/historical-search",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    ENTITY_PROXIES                 (6,  "Entity proxies",
                                        "The technology under test is able to store stubs for entities to use on relationships when the full entity is not available.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/entity-proxies",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    SOFT_DELETE_RESTORE            (7,  "Soft-delete and restore",
                                        "The technology under test allows an instance to be soft-deleted and restored.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/soft-delete-restore",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    UNDO_UPDATE                    (8,  "Undo an update",
                                        "The technology under test is able to restore an instance to its previous version (although the version number is updated).",
                                        "https://egeria-project.org/guides/cts/repository-profiles/undo-update",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    REIDENTIFY_INSTANCE            (9,  "Reidentify instance",
                                        "The technology under test supports the command to change the unique identifier (guid) of a metadata instance.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/reidentify-instance",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    RETYPE_INSTANCE                (10, "Retype instance",
                                        "The technology under test supports the command to change the type of a metadata instance to either its super type or a subtype.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/retype-instance",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    REHOME_INSTANCE                (11, "Rehome instance",
                                        "The technology under test supports the command to update the metadata collection id for a metadata instance.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/rehome-instance",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    ENTITY_SEARCH                   (12, "Entity search",
                                        "The technology under test supports the ability to search for entity instances.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/entity-search",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    RELATIONSHIP_SEARCH             (13, "Relationship search",
                                        "The technology under test supports the ability to search for relationship instances.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/relationship-search",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    ENTITY_ADVANCED_SEARCH          (14, "Entity advanced search",
                                        "The technology under test supports the use of regular expressions to search for metadata instances.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/entity-advanced-search",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    RELATIONSHIP_ADVANCED_SEARCH    (15, "Relationship advanced search",
                                         "The technology under test supports the use of regular expressions to search for relationship instances.",
                                         "https://egeria-project.org/guides/cts/repository-profiles/relationship-advanced-search",
                                         OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    INSTANCE_VERSIONS               (16, "List the versions of each instance",
                                         "The technology under test is able to return the list of versions of an instance that are stored.",
                                        "https://egeria-project.org/guides/cts/repository-profiles/instance-versions",
                                        OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    HOME_CLASSIFICATIONS            (17, "Store classifications independently of entity",
                                     "The technology under test is able to provide a home to a classification when the entity is homed in a different repository.",
                                     "https://egeria-project.org/guides/cts/repository-profiles/home-classifications",
                                     OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    EXTERNAL_INSTANCES             (18, "External instances",
                                     "The technology under test is able to store and maintain external entities.",
                                     "https://egeria-project.org/guides/cts/repository-profiles/external-instances",
                                     OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    RESTORE_FROM_BACKUP             (19, "Restore metadata from a backup",
                                     "The technology under test is able to process events that load metadata from its own backup.",
                                     "https://egeria-project.org/guides/cts/repository-profiles/restore-from-backup",
                                     OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE);


    private static final long serialVersionUID = 1L;

    private final int                                    profileId;
    private final String                                 profileName;
    private final String                                 profileDescription;
    private final String                                 profileDocumentationURL;
    private final OpenMetadataConformanceProfilePriority profilePriority;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param profileId int identifier for the enum, used for indexing arrays etc. with the enum.
     * @param profileName String name for the enum, used for message content.
     * @param profileDescription String default description for the enum, used when there is no natural
     *                             language resource bundle available.
     * @param profileDocumentationURL link to more documentation
     * @param profilePriority is the profile mandatory or not?
     */
    RepositoryConformanceProfile(int profileId,
                                 String                                 profileName,
                                 String                                 profileDescription,
                                 String                                 profileDocumentationURL,
                                 OpenMetadataConformanceProfilePriority profilePriority)
    {
        this.profileId = profileId;
        this.profileName = profileName;
        this.profileDescription = profileDescription;
        this.profileDocumentationURL = profileDocumentationURL;
        this.profilePriority = profilePriority;
    }


    /**
     * Return the identifier for the enum, used for indexing arrays etc. with the enum.
     *
     * @return int identifier
     */
    public Integer getProfileId()
    {
        return profileId;
    }


    /**
     * Return the name for the enum, used for message content.
     *
     * @return String name
     */
    public String getProfileName()
    {
        return profileName;
    }


    /**
     * Return the default description for the enum, used when there is no natural
     * language resource bundle available.
     *
     * @return String default description
     */
    public String getProfileDescription()
    {
        return profileDescription;
    }


    /**
     * Return the URL to link to more documentation about this profile.
     *
     * @return url
     */
    public String getProfileDocumentationURL()
    {
        return profileDocumentationURL;
    }


    /**
     * Return whether this profile is mandatory or optional.
     *
     * @return OpenMetadataConformanceProfilePriority enum
     */
    public OpenMetadataConformanceProfilePriority getProfilePriority()
    {
        return profilePriority;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "RepositoryConformanceProfile{" +
                "profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", profileDocumentationURL='" + profileDocumentationURL + '\'' +
                ", profilePriority=" + profilePriority +
                '}';
    }
}
