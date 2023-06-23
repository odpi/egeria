/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConfidenceLevel identifies the level of confidence to place in the accuracy of a data item.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ConfidenceLevel
{
    /**
     * There is no assessment of the confidence level for this data.
     */
    UNCLASSIFIED  (0, 0, "Unclassified",
                               "There is no assessment of the confidence level for this data."),

    /**
     * The data comes from an ad hoc process.
     */
    AD_HOC        (1, 1, "Ad Hoc",
                               "The data comes from an ad hoc process."),

    /**
     * The data comes from a transactional system so it may have a narrow scope.
     */
    TRANSACTIONAL (2, 2, "Transactional",
                               "The data comes from a transactional system so it may have a narrow scope."),

    /**
     * The data comes from an authoritative source. This is the best set of values.
     */
    AUTHORITATIVE (3, 3, "Authoritative",
                               "The data comes from an authoritative source. This is the best set of values."),

    /**
     * The data is derived from other data through an analytical process.
     */
    DERIVED       (4, 4, "Derived",
                               "The data is derived from other data through an analytical process."),

    /**
     * The data comes from an obsolete source and must no longer be used.
     */
    OBSOLETE      (5, 5, "Obsolete",
                               "The data comes from an obsolete source and must no longer be used."),

    /**
     * Another classification assignment status.
     */
    OTHER         (99, 99, "Other",
                               "Another classification assignment status.");

    private static final String ENUM_TYPE_GUID  = "ae846797-d88a-4421-ad9a-318bf7c1fe6f";
    private static final String ENUM_TYPE_NAME  = "ConfidenceLevel";

    private final int    openTypeOrdinal;

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    ConfidenceLevel(int    ordinal,
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
        return "ConfidenceLevel{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
