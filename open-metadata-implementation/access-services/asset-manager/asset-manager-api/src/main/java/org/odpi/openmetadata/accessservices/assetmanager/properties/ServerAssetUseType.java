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
public enum ServerAssetUseType implements Serializable
{
    /**
     * Owns - The asset is managed and updated via this relationship.
     */
    OWNS      (0,  0,  "Owns",     "The asset is managed and updated via this relationship."),

    /**
     * Governs - The asset is governed through this relationship.
     */
    GOVERNS   (1,  1,  "Governs",  "The asset is governed through this relationship."),

    /**
     * Maintains - The asset is maintained through this relationship.
     */
    MAINTAINS (2,  2,  "Maintains","The asset is maintained through this relationship."),

    /**
     * Uses - The asset is used through this relationship.
     */
    USES      (3,  3,  "Uses",     "The asset is used through this relationship."),

    /**
     * Other - None of the above.
     */
    OTHER     (99, 99, "Other",    "None of the above.");

    private static final long serialVersionUID = 1L;

    public static final String ENUM_TYPE_GUID  = "09439481-9489-467c-9ae5-178a6e0b6b5a";
    public static final String ENUM_TYPE_NAME  = "ServerAssetUseType";

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
    ServerAssetUseType(int    ordinal,
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
        return "ServerAssetUseType{" +
                       "codeValue=" + ordinal +
                       ", codeName='" + name + '\'' +
                       ", description='" + description +
                       '}';
    }
}

