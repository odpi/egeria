/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.auditlog;

/**
 * ComponentDevelopmentStatus describes the development status of a component.
 */
public enum ComponentDevelopmentStatus
{
    /**
     * In Development - This component is still being built.  Some features may work, but it is still subject to change.
     */
    IN_DEVELOPMENT    (0,  "In Development",    "This component is still being built.  Some features may work, but it is still subject to change."),

    /**
     * Technical Preview - This component is complete and can be used.  However, some features may change based on the feedback from early adopters.
     */
    TECHNICAL_PREVIEW (1,  "Technical Preview", "This component is complete and can be used.  However, some features may change based on the feedback from early adopters."),

    /**
     * Stable - This component is complete and can be used.  Any updates will be added in a backward compatible manner.
     */
    STABLE            (2,  "Stable",            "This component is complete and can be used.  Any updates will be added in a backward compatible manner."),

    /**
     * Sample - This component is supplied as a sample.  It can be used 'as is' or may be modified as desired.
     */
    SAMPLE            (3,  "Sample",            "This component is supplied as a sample.  It can be used 'as is' or may be modified as desired."),

    /**
     * Deprecated - This component is deprecated and may be removed in a later release.
     */
    DEPRECATED        (99, "Deprecated",        "This component is deprecated and may be removed in a later release.");

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param name default name
     * @param description default description
     */
    ComponentDevelopmentStatus(int    ordinal,
                               String name,
                               String description)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.description     = description;
    }


    /**
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for this enum.
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
        return "ComponentDevelopmentStatus{" +
                       "ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }
}
