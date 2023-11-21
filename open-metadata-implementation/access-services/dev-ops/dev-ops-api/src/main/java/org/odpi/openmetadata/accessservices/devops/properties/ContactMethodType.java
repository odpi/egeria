/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.devops.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContactMethodType specifies the contact mechanism to use to contact an individual.
 * <ul>
 *     <li>EMAIL</li>
 *     <li>PHONE</li>
 *     <li>CHAT</li>
 *     <li>PROFILE</li>
 *     <li>ACCOUNT</li>
 *     <li>OTHER</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ContactMethodType
{
    /**
     * Send email.
     */
    EMAIL     (0, 0,  "Email",    "Send email."),

    /**
     * Call by phone.
     */
    PHONE     (1, 1,  "Phone",    "Call by phone."),

    /**
     * Send chat message.
     */
    CHAT      (2, 2,  "Chat",     "Send chat message."),

    /**
     * Send comment to personal profile.
     */
    PROFILE   (3, 3,  "Profile",  "Send comment to personal profile."),

    /**
     * Send comment to a social media account.
     */
    ACCOUNT   (4, 4,  "Account",  "Send comment to a social media account."),

    /**
     * Another contact mechanism.
     */
    OTHER     (99,99, "Other",    "Another contact mechanism.");


    private static final String ENUM_TYPE_GUID  = "30e7d8cd-df01-46e8-9247-a24c5650910d";
    private static final String ENUM_TYPE_NAME  = "ContactMethodType";

    private final int    openTypeOrdinal;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    ContactMethodType(int    ordinal,
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
        return "ContactMethodType : " + name;
    }
}
