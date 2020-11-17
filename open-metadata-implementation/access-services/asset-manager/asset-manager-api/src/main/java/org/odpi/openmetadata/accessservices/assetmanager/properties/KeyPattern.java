/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A KeyPattern defines the type of External Identifier in use for an asset, or the type of Primary Key used within an
 * asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum KeyPattern implements Serializable
{
    LOCAL_KEY     (0,  0, "Local Key", "Unique key allocated and used within the scope of a single system."),
    RECYCLED_KEY  (1,  1, "Recycled Key", "Key allocated and used within the scope of a single system that " +
            "is periodically reused for different records."),
    NATURAL_KEY   (2,  2, "Natural Key", "Key derived from an attribute of the entity, such as email address, passport number."),
    MIRROR_KEY    (3,  3, "Mirror Key", "Key value copied from another system."),
    AGGREGATE_KEY (4,  4, "Aggregate Key", "Key formed by combining keys from multiple systems."),
    CALLERS_KEY   (5,  5, "Caller's Key", "Key from another system can bey used if system name provided."),
    STABLE_KEY    (6,  6, "Stable Key", "Key value will remain active even if records are merged."),
    OTHER         (99, 99, "Other", "Another key pattern.");

    public static final String ENUM_TYPE_GUID  = "8904df8f-1aca-4de8-9abd-1ef2aadba300";
    public static final String ENUM_TYPE_NAME  = "KeyPattern";


    private int    ordinal;
    private int    openTypeOrdinal;
    private String name;
    private String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    KeyPattern(int    ordinal,
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
     * @return int key pattern code
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
     * Return the default description for the key pattern for this enum instance.
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
        return "KeyPattern{" +
                       "openTypeOrdinal=" + openTypeOrdinal +
                       ", ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", openTypeGUID='" + getOpenTypeGUID() + '\'' +
                       ", openTypeName='" + getOpenTypeName() + '\'' +
                       '}';
    }
}