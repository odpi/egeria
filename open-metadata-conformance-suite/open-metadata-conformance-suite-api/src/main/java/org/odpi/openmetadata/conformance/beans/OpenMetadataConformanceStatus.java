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
 * OpenMetadataConformanceStatus is used to document the level of conformance to requirements (and hence profiles)
 * detected by test cases.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataConformanceStatus implements Serializable
{
    UNKNOWN_STATUS               (0, "Unknown status",
                                     "There is not enough evidence to determine the conformance of the technology under test."),
    CONFORMANT_FULL_SUPPORT      (1, "Conformant with full support",
                                     "The technology provides correctly functioning support for all features in this profile."),
    CONFORMANT_PARTIAL_SUPPORT   (2, "Conformant with partial support",
                                     "The technology provides correctly functioning support for some of the features in this profile and responds appropriately for features it does not support."),
    CONFORMANT_NO_SUPPORT        (3, "Conformant with no support",
                                     "The technology provides correctly functioning responses that indicate it has no support for the features in this profile."),
    NOT_CONFORMANT               (4, "Not Conformant",
                                     "The technology is not functioning according to the open metadata specifications.");

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
    OpenMetadataConformanceStatus(int                ordinal,
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
