/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataItemSortOrder is used for schema attributes that may have multiple instances.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataItemSortOrder implements Serializable
{
    UNKNOWN    (0, 99, "<Unknown>",  "The sort order is not specified."),
    ASCENDING  (1, 0, "Ascending",  "The attribute instances are organized so that the " +
            "smallest/lowest value is first and the rest of the instances follow in " +
            "ascending order."),
    DESCENDING (2, 1, "Descending", "The attribute instances are organized so that the " +
            "largest/highest value is first and the rest of the instances follow in " +
            "descending order."),
    UNSORTED   (3, 99, "Unsorted",   "The instances of the schema attribute may appear in any order.");


    public static final String ENUM_TYPE_GUID  = "aaa4df8f-1aca-4de8-9abd-1ef2aadba300";
    public static final String ENUM_TYPE_NAME  = "DataItemSortOrder";

    private int    openTypeOrdinal;

    private int    ordinal;
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
    DataItemSortOrder(int    ordinal,
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
        return "DataItemSortOrder{" +
                "codeValue=" + ordinal +
                ", codeName='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
