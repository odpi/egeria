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
 * A GlossaryTermRelationshipStatus defines the status of a relationship with a glossary term.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GlossaryTermRelationshipStatus implements Serializable
{
    /**
     * Draft - The term relationship is in development.
     */
    DRAFT      (0,  0,  "Draft",      "The term relationship is in development."),

    /**
     * Active - The term relationship is approved and in use.
     */
    ACTIVE     (1,  1,  "Active",     "The term relationship is approved and in use."),

    /**
     * Deprecated - The term relationship should no longer be used.
     */
    DEPRECATED (2,  2,  "Deprecated", "The term relationship should no longer be used."),

    /**
     * Obsolete - The term relationship must no longer be used.
     */
    OBSOLETE   (3,  3,  "Obsolete",   "The term relationship must no longer be used."),

    /**
     * Other - Another term relationship status.
     */
    OTHER      (99, 99, "Other",      "Another term relationship status.");

    private static final String ENUM_TYPE_GUID  = "42282652-7d60-435e-ad3e-7cfe5291bcc7";
    private static final String ENUM_TYPE_NAME  = "TermRelationshipStatus";

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
    GlossaryTermRelationshipStatus(int    ordinal,
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
        return "GlossaryTermRelationshipStatus{" +
                       "openTypeOrdinal=" + openTypeOrdinal +
                       ", ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", openTypeGUID='" + getOpenTypeGUID() + '\'' +
                       ", openTypeName='" + getOpenTypeName() + '\'' +
                       '}';
    }
}