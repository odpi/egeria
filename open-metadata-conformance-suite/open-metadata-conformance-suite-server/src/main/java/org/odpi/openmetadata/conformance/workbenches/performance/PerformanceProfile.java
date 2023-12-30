/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.performance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfilePriority;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PerformanceProfile defines the list of performance profiles for an open metadata and governance
 * (OMAG) server platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum PerformanceProfile implements Serializable
{
    /**
     * Performance tests for the technology under test's ability to create entities.
     */
    ENTITY_CREATION      (1, "Entity creation",
            "Performance tests for the technology under test's ability to create entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-creation",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to search entities.
     */
    ENTITY_SEARCH        (2, "Entity search",
            "Performance tests for the technology under test's ability to search entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to create relationships.
     */
    RELATIONSHIP_CREATION(3, "Relationship creation",
            "Performance tests for the technology under test's ability to create relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-creation",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to search relationships.
     */
    RELATIONSHIP_SEARCH  (4, "Relationship search",
            "Performance tests for the technology under test's ability to search relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to classify entities.
     */
    ENTITY_CLASSIFICATION(5, "Entity classification",
            "Performance tests for the technology under test's ability to classify entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-classification",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to search entities based on classification.
     */
    CLASSIFICATION_SEARCH(6, "Classification search",
            "Performance tests for the technology under test's ability to search entities based on classification.",
            "https://egeria-project.org/guides/cts/performance-profiles/classification-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to update entities.
     */
    ENTITY_UPDATE        (7, "Entity update",
            "Performance tests for the technology under test's ability to update entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-update",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to update relationships.
     */
    RELATIONSHIP_UPDATE  (8, "Relationship update",
            "Performance tests for the technology under test's ability to update relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-update",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to update classifications.
     */
    CLASSIFICATION_UPDATE(9, "Classification update",
            "Performance tests for the technology under test's ability to update classifications.",
            "https://egeria-project.org/guides/cts/performance-profiles/classification-update",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to undo entity updates.
     */
    ENTITY_UNDO          (10, "Entity undo",
            "Performance tests for the technology under test's ability to undo entity updates.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-undo",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to undo relationship updates.
     */
    RELATIONSHIP_UNDO    (11, "Relationship undo",
            "Performance tests for the technology under test's ability to undo relationship updates.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-undo",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to retrieve entities by ID.
     */
    ENTITY_RETRIEVAL     (12, "Entity retrieval",
            "Performance tests for the technology under test's ability to retrieve entities by ID.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-retrieval",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to retrieve entities' history by ID.
     */
    ENTITY_HISTORY_RETRIEVAL(13, "Entity history retrieval",
            "Performance tests for the technology under test's ability to retrieve entities' history by ID.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-history-retrieval",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to retrieve relationships by ID.
     */
    RELATIONSHIP_RETRIEVAL(14, "Relationship retrieval",
            "Performance tests for the technology under test's ability to retrieve relationships by ID.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-retrieval",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to retrieve relationships' history by ID.
     */
    RELATIONSHIP_HISTORY_RETRIEVAL(15, "Relationship history retrieval",
            "Performance tests for the technology under test's ability to retrieve relationships' history by ID.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-history-retrieval",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to search entities' history.
     */
    ENTITY_HISTORY_SEARCH(16, "Entity history search",
            "Performance tests for the technology under test's ability to search entities' history.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-historical-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to search relationships' history.
     */
    RELATIONSHIP_HISTORY_SEARCH(17, "Relationship history search",
            "Performance tests for the technology under test's ability to search relationships' history.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-history-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to run graph queries.
     */
    GRAPH_QUERIES        (18, "Graph queries",
            "Performance tests for the technology under test's ability to run graph queries.",
            "https://egeria-project.org/guides/cts/performance-profiles/graph-perf-queries",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to run graph history queries.
     */
    GRAPH_HISTORY_QUERIES(19, "Graph history queries",
            "Performance tests for the technology under test's ability to run graph history queries.",
            "https://egeria-project.org/guides/cts/performance-profiles/graph-history-queries",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to re-home entities.
     */
    ENTITY_RE_HOME       (20, "Entity re-home",
            "Performance tests for the technology under test's ability to re-home entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-re-home",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to re-home relationships.
     */
    RELATIONSHIP_RE_HOME (21, "Relationship re-home",
            "Performance tests for the technology under test's ability to re-home relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-re-home",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to declassify entities.
     */
    ENTITY_DECLASSIFY    (22, "Entity declassify",
            "Performance tests for the technology under test's ability to declassify entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-declassify",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to retype entities.
     */
    ENTITY_RETYPE        (23, "Entity retype",
            "Performance tests for the technology under test's ability to retype entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-retype",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to retype relationships.
     */
    RELATIONSHIP_RETYPE  (24, "Relationship retype",
            "Performance tests for the technology under test's ability to retype relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-retype",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to re-identify entities.
     */
    ENTITY_RE_IDENTIFY   (25, "Entity re-identify",
            "Performance tests for the technology under test's ability to re-identify entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-re-identify",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to re-identify relationships.
     */
    RELATIONSHIP_RE_IDENTIFY(26, "Relationship re-identify",
            "Performance tests for the technology under test's ability to re-identify relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-re-identify",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to delete relationships.
     */
    RELATIONSHIP_DELETE  (27, "Relationship delete",
            "Performance tests for the technology under test's ability to delete relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-delete",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to delete entities.
     */
    ENTITY_DELETE        (28, "Entity delete",
            "Performance tests for the technology under test's ability to delete entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-delete",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to restore entities.
     */
    ENTITY_RESTORE       (29, "Entity restore",
            "Performance tests for the technology under test's ability to restore entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-restore",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to restore relationships.
     */
    RELATIONSHIP_RESTORE (30, "Relationship restore",
            "Performance tests for the technology under test's ability to restore relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-restore",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to purge relationships.
     */
    RELATIONSHIP_PURGE   (31, "Relationship purge",
            "Performance tests for the technology under test's ability to purge relationships.",
            "https://egeria-project.org/guides/cts/performance-profiles/relationship-purge",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Performance tests for the technology under test's ability to purge entities.
     */
    ENTITY_PURGE         (32, "Entity purge",
            "Performance tests for the technology under test's ability to purge entities.",
            "https://egeria-project.org/guides/cts/performance-profiles/entity-purge",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),

    /**
     * Information about the environment in which the performance tests were executed.
     */
    ENVIRONMENT          (999, "Environment",
            "Information about the environment in which the performance tests were executed.",
            "https://egeria-project.org/guides/cts/performance-profiles/environment",
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
     * @param profileId int identifier for the enum, used for indexing arrays etcetera with the enum.
     * @param profileName String name for the enum, used for message content.
     * @param profileDescription String default description for the enum, used when there is not a natural
     *                             language resource bundle available.
     * @param profileDocumentationURL link to more documentation
     * @param profilePriority is the profile mandatory or not?
     */
    PerformanceProfile(int                                    profileId,
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
     * Return the identifier for the enum, used for indexing arrays etcetera with the enum.
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
     * Return the default description for the enum, used when there is not a natural
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
        return "PerformanceProfile{" +
                "profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", profileDocumentationURL='" + profileDocumentationURL + '\'' +
                ", profilePriority=" + profilePriority +
                '}';
    }
}
