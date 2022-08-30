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
 * RetentionBasis defines the retention requirements associated with a data item.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RetentionBasis implements Serializable
{
    UNCLASSIFIED       (0, 0, "Unclassified",
                               "There is no assessment of the retention requirements for this data."),
    TEMPORARY          (1, 1, "Temporary",
                               "This data is temporary. There are no formal retention requirements."),
    PROJECT_LIFETIME   (2, 2, "Project Lifetime",
                               "The data is needed for the lifetime of the referenced project."),
    TEAM_LIFETIME      (3, 3, "Team Lifetime",
                               "The data is needed for the lifetime of the referenced team."),
    CONTRACT_LIFETIME  (4, 4, "Contract Lifetime",
                               "The data is needed for the lifetime of the referenced contract."),
    REGULATED_LIFETIME (5, 5, "Regulated Lifetime",
                               "The retention period for the data is defined by the referenced regulation."),
    TIMEBOXED_LIFETIME (6, 6, "Time Boxed Lifetime",
                               "The data is needed for the specified time."),
    OTHER             (99, 99, "Other",
                               "Another basis for determining the retention requirement.");

    public static final String ENUM_TYPE_GUID  = "de79bf78-ecb0-4fd0-978f-ecc2cb4ff6c7";
    public static final String ENUM_TYPE_NAME  = "RetentionBasis";

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
    RetentionBasis(int    ordinal,
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
        return "RetentionBasis{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
