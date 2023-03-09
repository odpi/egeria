/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.platform;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfilePriority;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PlatformConformanceProfile defines the list of functional profiles for an open metadata and governance
 * (OMAG) server platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum PlatformConformanceProfile implements Serializable
{
    PLATFORM_ORIGIN       (0,  "Platform origin",
                               "The technology under test is able to report on its origin.",
                               "https://egeria-project.org/guides/cts/platform-workbench/profiles/platform-origin",
                               OpenMetadataConformanceProfilePriority.MANDATORY_PROFILE);

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
    PlatformConformanceProfile(int                                    profileId,
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
        return "RepositoryConformanceProfile{" +
                "profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", profileDocumentationURL='" + profileDocumentationURL + '\'' +
                ", profilePriority=" + profilePriority +
                '}';
    }
}
