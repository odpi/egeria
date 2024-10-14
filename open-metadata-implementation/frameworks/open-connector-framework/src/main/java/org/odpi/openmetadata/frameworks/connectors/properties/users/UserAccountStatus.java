/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The UserAccountStatus enum defines the status of a user account
 * <ul>
 *     <li>Disabled: The user account is not in use.</li>
 *     <li>Locked: The user account is in use but is temporarily locked.</li>
 *     <li>CredentialsExpired: The user account is in use and unlocked, but the credentials have expired.</li>
 *     <li>Available: The user account is enabled and unlocked, with active credentials.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum UserAccountStatus
{
    /**
     * The user account is not in use.
     */
    DISABLED                 (0,    "Disabled",     "The user account is not in use."),

    /**
     * The user account is in use but is temporarily locked.
     */
    LOCKED                   (1,    "Locked",      "The user account is in use but is temporarily locked."),

    /**
     * The user account is in use and unlocked, but the credentials have expired.
     */
    CREDENTIALS_EXPIRED      (2,   "Prepared",     "The user account is in use and unlocked, but the credentials have expired."),

    /**
     * The user account is enabled and unlocked, with active credentials.
     */
    AVAILABLE                (3,   "Available",     "The user account is enabled and unlocked, with active credentials.");


    private  final int    ordinal;
    private  final String name;
    private  final String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name     String name
     * @param description String description
     */
    UserAccountStatus(int     ordinal,
                      String  name,
                      String  description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the descriptive name for the enum.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the description for the enum.
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
        return "UserAccountStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
