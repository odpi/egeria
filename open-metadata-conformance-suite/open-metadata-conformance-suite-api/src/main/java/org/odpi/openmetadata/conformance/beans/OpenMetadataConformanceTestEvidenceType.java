/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;


import java.io.Serializable;

/**
 * OpenMetadataConformanceTestEvidenceType describes the type of evidence that is stored in an
 * OpenMetadataConformanceTestEvidence object.
 */
public enum OpenMetadataConformanceTestEvidenceType implements Serializable
{
    /**
     * Unknown - No data available at this time.
     */
    NO_DATA_AVAILABLE      (0, "Unknown",
                            "No data available at this time."),

    /**
     * The test case condition is true.
     */
    SUCCESSFUL_ASSERTION   (0, "Successful assertion",
                            "The test case condition is true."),

    /**
     * The test case condition is false.
     */
    UNSUCCESSFUL_ASSERTION (1, "Unsuccessful assertion",
                            "The test case condition is false."),

    /**
     * The test case has discovered a property.
     */
    DISCOVERED_PROPERTY    (2, "Discovered property",
                            "The test case has discovered a property."),

    /**
     * The test case reports a correct response for a non-supported function.
     */
    NOT_SUPPORTED_FUNCTION (3, "Not supported function",
                            "The test case reports a correct response for a non-supported function."),

    /**
     * An exception occurred where it should not.
     */
    UNEXPECTED_EXCEPTION   (4, "Unexpected exception",
                            "An exception occurred where it should not.");

    private static final long serialVersionUID = 1L;

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is not natural
     *                             language resource bundle available.
     */
    OpenMetadataConformanceTestEvidenceType(int                ordinal,
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
        return "OpenMetadataConformanceTestEvidenceType{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
