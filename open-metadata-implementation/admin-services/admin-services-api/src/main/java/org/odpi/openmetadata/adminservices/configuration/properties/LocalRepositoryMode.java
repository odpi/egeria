/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LocalRepositoryMode classifies the capability of the server's local repository.
 * This value will influence the server type classification.  It is also useful documentation
 * for an administrator to understand the expected capability that each repository is able to perform.
 * <ul>
 *     <li>
 *         UNCLASSIFIED means that the mode of the repository is not known.
 *     </li>
 *     <li>
 *         NO_REPOSITORY means that the server is running without a local repository.
 *     </li>
 *     <li>
 *         METADATA_CACHE means that it is able to act as a cache for metadata but not to master new values.
 *         It is typically used as a metadata access point where Open Metadata Archives are
 *         introduced onto a cohort.  These provide standard metadata definitions to the cohort(s).
 *     </li>
 *     <li>
 *         REPOSITORY_PROXY means this local repository is actually a proxy to third party metadata repository.
 *         It is restricted to the capability of the third party repository.
 *     </li>
 *     <li>
 *         PLUGIN_REPOSITORY means the repository is able to support the open metadata types and instances
 *         natively.  This type of repository has been built specifically for open metadata and as such is able to
 *         absorb new types of metadata.  At least one of these types of repository should be operating in a cohort
 *         if the access services are enabled.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum LocalRepositoryMode
{
    /**
     * The mode of the repository is unknown.
     */
    UNCLASSIFIED         (0,  "Unclassified",        "The mode of the repository is unknown."),

    /**
     * The server is running without a local repository.
     */
    NO_REPOSITORY        (1,  "No Repository",       "The server is running without a local repository."),

    /**
     * This local repository is read only and is able to act as a cache for metadata but not to master new values.
     */
    METADATA_CACHE       (2,  "Metadata Cache",      "This local repository is read only and is able to act as a cache " +
                                                             "for metadata but not to master new values."),

    /**
     * This local repository is actually a proxy to third party metadata repository. It is restricted to the capability of the third party repository.
     */
    REPOSITORY_PROXY     (3,  "Repository Proxy",    "This local repository is actually a proxy to third party " +
                                                             "metadata repository. It is restricted to the capability of the third party repository."),

    /**
     * This local repository is a plugin repository that is part of Egeria and has been built
     * specifically for open metadata and as such is able to dynamically absorb new types of metadata.
     */
    OPEN_METADATA_NATIVE (4,  "Open Metadata Native","This local repository is a plugin repository that is " +
                                                             "part of Egeria and has been built " +
                                                "specifically for open metadata and as such is able to dynamically absorb new types of metadata."),

    /**
     * This local repository has been built by a third party as a plugin repository.
     */
    PLUGIN_REPOSITORY    (5,  "Plugin Repository",   "This local repository has been built by a third party as a plugin repository.");

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Constructor for the metadata instance replication rule.
     *
     * @param ordinal the code number of this enum value.
     * @param name the name of this enum value.
     * @param description the description of this enum value.
     */
    LocalRepositoryMode(int ordinal, String name, String description)
    {
        this.ordinal     = ordinal;
        this.name        = name;
        this.description = description;
    }


    /**
     * Return the code number of this enum value.
     *
     * @return int  code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the name of this enum value.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of this enum value.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LocalRepositoryMode{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
