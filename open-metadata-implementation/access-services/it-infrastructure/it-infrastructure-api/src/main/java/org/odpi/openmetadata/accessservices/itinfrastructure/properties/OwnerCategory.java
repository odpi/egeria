/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OwnerCategory defines the identifier used in an Asset's owner property.
 * <ul>
 *     <li>User Id - The owner's user id is stored in the owner property.</li>
 *     <li>Profile - The owner's profile unique identifier (guid) is stored in the owner property.</li>
 *     <li>Other - A different identifier for the owner outside of the scope of open metadata has been used.</li>
 * </ul>
 * Being able to use a profile guid in this field allows for Asset's to be owned by Teams and Engines as well
 * as people.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OwnerCategory implements Serializable
{
    USER_ID     (0,  "UserId",    "The owner's user id is stored in the owner property."),
    PROFILE_ID  (1,  "ProfileId", "The owner's profile unique identifier (guid) is stored in the owner property."),
    OTHER       (99, "Other",     "A different identifier for the owner outside of the scope of open metadata has been used.");

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
    OwnerCategory(int  ordinal, String name, String description)
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
        return "OwnerCategory : " + name;
    }
}
