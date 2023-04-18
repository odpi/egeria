/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The RelatedMediaUsage defines how a related media reference can be used in conjunction with the asset properties.
 * These usage options are not mutually exclusive.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RelatedMediaUsage implements Serializable
{
    ICON           (0, 0, "Icon", "Provides a small image to represent the asset in tree views and graphs."),
    THUMBNAIL      (1, 1, "Thumbnail", "Provides a small image about the asset that can be used in lists."),
    ILLUSTRATION   (2, 2, "Illustration", "Illustrates how the asset works or what it contains. It is complementary to the asset's description."),
    USAGE_GUIDANCE (3, 3, "Usage Guidance", "Provides guidance to a person on how to use the asset."),
    OTHER          (99, 99, "Other", "Another usage.");

    private static final String ENUM_TYPE_GUID  = "c6861a72-7485-48c9-8040-876f6c342b61";
    private static final String ENUM_TYPE_NAME  = "MediaUsage";

    private final int    openTypeOrdinal;

    private final int    ordinal;
    private final String name;
    private final String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    RelatedMediaUsage(int    ordinal,
                      int    openTypeOrdinal,
                      String name,
                      String description)
    {
        this.ordinal         = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name            = name;
        this.description     = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int media usage code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the media usage pattern for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
    }


    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedMediaUsage{" +
                "mediaUsageCode=" + ordinal +
                ", mediaUsageName='" + name + '\'' +
                ", mediaUsageDescription='" + description + '\'' +
                '}';
    }
}