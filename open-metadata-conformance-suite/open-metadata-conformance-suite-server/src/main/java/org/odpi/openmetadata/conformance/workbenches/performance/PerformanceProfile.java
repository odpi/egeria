/* SPDX-License-Identifier: Apache 2.0 */
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
    ENTITY_CREATION      (1, "Entity creation",
            "Performance tests for the technology under test's ability to create entities.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/entity-creation",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    ENTITY_SEARCH        (2, "Entity search",
            "Performance tests for the technology under test's ability to search entities.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/entity-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    RELATIONSHIP_CREATION(3, "Relationship creation",
            "Performance tests for the technology under test's ability to create relationships.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/relationship-creation",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    RELATIONSHIP_SEARCH  (4, "Relationship search",
            "Performance tests for the technology under test's ability to search relationships.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/relationship-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    ENTITY_CLASSIFICATION(5, "Entity classification",
            "Performance tests for the technology under test's ability to classify entities.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/entity-classification",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    CLASSIFICATION_SEARCH(6, "Classification search",
            "Performance tests for the technology under test's ability to search entities based on classification.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/classification-search",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE),
    ENVIRONMENT          (999, "Environment",
            "Information about the environment in which the performance tests were executed.",
            "https://egeria.odpi.org/open-metadata-conformance-suite/docs/performance-workbench/profiles/environment",
            OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE);

    private static final long serialVersionUID = 1L;

    private int                                    profileId;
    private String                                 profileName;
    private String                                 profileDescription;
    private String                                 profileDocumentationURL;
    private OpenMetadataConformanceProfilePriority profilePriority;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param profileId int identifier for the enum, used for indexing arrays etc with the enum.
     * @param profileName String name for the enum, used for message content.
     * @param profileDescription String default description for the enum, used when there is not natural
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
     * Return the identifier for the enum, used for indexing arrays etc with the enum.
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
     * Return the default description for the enum, used when there is not natural
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
