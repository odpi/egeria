/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.platform;

import java.io.Serializable;

/**
 * PlatformConformanceProfileRequirement documents the different requirements for each of the platform workbench's profiles.
 * The Open Metadata Platform Tests report their findings for each requirement.
 */
public enum PlatformConformanceProfileRequirement implements Serializable
{
    ORIGIN_IDENTIFIER           (0, "Origin identifier",
                                   "The technology under test is able to return the identifier of the platform.",
                                   "https://egeria-project.org/guides/cts/platform-workbench/profiles/platform-origin/origin-identifier",
                                   PlatformConformanceProfile.PLATFORM_ORIGIN);

    private static final long serialVersionUID = 1L;

    private int                        ordinal;
    private String                     name;
    private String                     description;
    private String                     documentationURL;
    private PlatformConformanceProfile profile;



    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is not natural
     *                             language resource bundle available.
     * @param documentationURL link to more information
     * @param profile parent profile.
     */
    PlatformConformanceProfileRequirement(int                           ordinal,
                                          String                        name,
                                          String                        description,
                                          String                        documentationURL,
                                          PlatformConformanceProfile    profile)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.documentationURL = documentationURL;
        this.profile = profile;
    }


    /**
     * Return the identifier for the enum, used for indexing arrays etc with the enum.
     *
     * @return int identifier
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the name for the enum, used for message content.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the enum, used when there is not natural
     * language resource bundle available.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL to link to more documentation about this profile.
     *
     * @return url
     */
    public String getDocumentationURL()
    {
        return documentationURL;
    }


    /**
     * Return the profile that this requirement belongs to.
     *
     * @return priority enum
     */
    public PlatformConformanceProfile getProfile()
    {
        return profile;
    }


    /**
     * Return the profile identifier that this requirement belongs to.
     *
     * @return profile id
     */
    public Integer getProfileId()
    {
        return profile.getProfileId();
    }


    /**
     * Return the requirement identifier as an Integer.
     *
     * @return requirement id
     */
    public Integer getRequirementId()
    {
        return ordinal;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "PlatformConformanceProfileRequirement{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", documentationURL='" + documentationURL + '\'' +
                ", profile=" + profile +
                ", profileId=" + getProfileId() +
                ", requirementId=" + getRequirementId() +
                '}';
    }
}
