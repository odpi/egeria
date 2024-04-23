/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProcessContainmentType defines the ownership of a process withing a sub process. It is used in a
 * ProcessHierarchy relationship.
 * <ul>
 * <li>OWNED - The parent process owns the child process in the relationship, such that if the parent is removed the child should also be removed.
 * A child can have at most one such parent.</li>
 * <li>USED - The child process is simply used by the parent. A child process can have many such relationships to parents.</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ProcessContainmentType implements Serializable
{
    OWNED (0,  0,  "Owned",  "The parent process owns the child process in the relationship, such that if the parent is removed the child should also be removed. A child can have at most one such parent."),
    USED  (1,  1,  "Used",   "The child process is simply used by the parent. A child process can have many such relationships to parents."),
    OTHER (99, 99, "Other",  "None of the above.");

    private static final long serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "1bb4b908-7983-4802-a2b5-91b095552ee9";
    private static final String ENUM_TYPE_NAME  = "ProcessContainmentType";

    private final int    ordinal;
    private final int    openTypeOrdinal;
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
    ProcessContainmentType(int    ordinal,
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
        return "ProcessContainmentType{" +
                       "codeValue=" + ordinal +
                       ", codeName='" + name + '\'' +
                       ", description='" + description +
                       '}';
    }
}

