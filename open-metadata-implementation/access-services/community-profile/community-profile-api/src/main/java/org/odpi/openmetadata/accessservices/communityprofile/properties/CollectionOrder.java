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
 *     <li>TYPE_NAME - Order the collection by type.</li>
 *     <li>QUALIFIED_NAME - Order the collection by the qualified name of the members in the collection.</li>
 *     <li>NAME - Order the collection by the name of the members in the collection.</li>
 *     <li>OWNER - Order the collection by the owner of the members in the collection.</li>
 *     <li>DATE_ADDED - Order the collection by the date that the members were added to the collection.</li>
 *     <li>DATE_UPDATED - Order the collection by the date that the members were updated in the collection.</li>
 *     <li>DATE_CREATED - Order the collection by the date that the members were created in the collection.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CollectionOrder implements Serializable
{
    TYPE_NAME      (0,  "Type Name",      "Order the collection by the type names of the members in the collection."),
    QUALIFIED_NAME (1,  "Qualified Name", "Order the collection by the qualified names of the members in the collection."),
    NAME           (2,  "Name",           "Order the collection by the names of the members in the collection."),
    OWNER          (3,  "Owner",          "Order the collection by the owners of the members in the collection (assets only)."),
    DATE_ADDED     (4,  "Date Added",     "Order the collection by the date that the members were added to the collection."),
    DATE_UPDATED   (5,  "Date Updated",   "Order the collection by the date that the members were updated in the collection."),
    DATE_CREATED   (6,  "Date Created",   "Order the collection by the date that the members were created in the collection.");

    private static final long serialVersionUID = 1L;

    private int            ordinal;
    private String         name;
    private String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    CollectionOrder(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
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
