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
 * GovernanceClassificationStatus identifies the status of one of the governance action classification.
 * It provides some provenance on the process that assigned the value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceClassificationStatus implements Serializable
{
    /**
     * The classification assignment was discovered by an automated process.
     */
    DISCOVERED (0, 0,"Discovered",
                               "The classification assignment was discovered by an automated process."),

    /**
     * The classification assignment was proposed by a subject matter expert.
     */
    PROPOSED   (1, 1,"Proposed",
                               "The classification assignment was proposed by a subject matter expert."),

    /**
     * The classification assignment was imported from another metadata system.
     */
    IMPORTED   (2, 2,"Imported",
                               "The classification assignment was imported from another metadata system."),

    /**
     * The classification assignment has been validated and approved by a subject matter expert.
     */
    VALIDATED  (3, 3,"Validated",
                               "The classification assignment has been validated and approved by a subject matter expert."),

    /**
     * The classification assignment should no longer be used.
     */
    DEPRECATED (4, 4,"Deprecated",
                               "The classification assignment should no longer be used."),

    /**
     * The classification assignment must no longer be used.
     */
    OBSOLETE   (5, 5,"Obsolete",
                               "The classification assignment must no longer be used."),

    /**
     * Another classification assignment status.
     */
    OTHER     (99, 99, "Other",
                               "Another classification assignment status.");

    private static final String ENUM_TYPE_GUID  = "cc540586-ac7c-41ba-8cc1-4da694a6a8e4";
    private static final String ENUM_TYPE_NAME  = "GovernanceClassificationStatus";

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
    GovernanceClassificationStatus(int    ordinal,
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
        return "GovernanceClassificationStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
