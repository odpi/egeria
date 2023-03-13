/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataConformanceProfilePriority indicates whether a conformance profile is mandatory or optional.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataConformanceProfilePriority implements Serializable
{
    MANDATORY_PROFILE   (0, "Mandatory Profile",
                            "This profile must be supported for the technology under test to be compliant."),
    OPTIONAL_PROFILE    (1, "Optional Profile",
                            "This profile must either be supported, or a conformant response is required.");

    private static final long serialVersionUID = 1L;

    private int                ordinal;
    private String             name;
    private String             description;



    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is not natural
     *                             language resource bundle available.
     */
    OpenMetadataConformanceProfilePriority(int                ordinal,
                                           String             name,
                                           String             description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceProfilePriority{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
