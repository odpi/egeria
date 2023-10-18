/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionOrder specifies the sequencing to use in a specific collection.
 * <ul>
 *     <li>QUALIFIED_NAME - Order the collection by the qualified name of the members in the collection.</li>
 *     <li>NAME - Order the collection by the name of the members in the collection.</li>
 *     <li>OWNER - Order the collection by the owner of the members in the collection.</li>
 *     <li>DATE_ADDED - Order the collection by the date that the members were added to the collection.</li>
 *     <li>DATE_UPDATED - Order the collection by the date that the members were updated in the collection.</li>
 *     <li>DATE_CREATED - Order the collection by the date that the members were created in the collection.</li>
 *     <li>OTHER - Order the collection by another value.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CollectionOrder implements Serializable
{
    /**
     * Order the collection by the names of the members in the collection.
     */
    NAME           (0,  0,  "Name",           "Order the collection by the names of the members in the collection."),

    /**
     * Order the collection by the owners of the members in the collection.
     */
    OWNER          (1,  1,  "Owner",          "Order the collection by the owners of the members in the collection."),

    /**
     * Order the collection by the date that the members were added to the collection.
     */
    DATE_ADDED     (2,  2,  "Date Added",     "Order the collection by the date that the members were added to the collection."),

    /**
     * Order the collection by the date that the members were updated in the collection.
     */
    DATE_UPDATED   (3,  3,  "Date Updated",   "Order the collection by the date that the members were updated in the collection."),

    /**
     * Order the collection by the date that the members were created in the collection.
     */
    DATE_CREATED   (4,  4,  "Date Created",   "Order the collection by the date that the members were created in the collection."),

    /**
     * Order the collection by another value.
     */
    OTHER          (99, 99, "Other",          "Order the collection by another value.");

    private static final long serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "1d412439-4272-4a7e-a940-1065f889fc56";
    private static final String ENUM_TYPE_NAME  = "OrderBy";

    private final int    openTypeOrdinal;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    CollectionOrder(int    ordinal,
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
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "CollectionOrder : " + name;
    }
}
