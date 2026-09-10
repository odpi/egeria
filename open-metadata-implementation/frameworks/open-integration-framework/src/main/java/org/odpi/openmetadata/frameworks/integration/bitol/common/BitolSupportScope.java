/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

/**
 * BitolSupportScope lists the standard scopes of a support or communication channel.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum BitolSupportScope
{
    /**
     * Interactive discussion with the team.
     */
    INTERACTIVE  ("interactive", "Interactive discussion with the team."),

    /**
     * Announcements from the team.
     */
    ANNOUNCEMENTS("announcements", "Announcements from the team."),

    /**
     * Reporting and tracking issues.
     */
    ISSUES       ("issues", "Reporting and tracking issues."),

    /**
     * Automated notifications.
     */
    NOTIFICATIONS("notifications", "Automated notifications.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    BitolSupportScope(String value,
                      String description)
    {
        this.value       = value;
        this.description = description;
    }


    /**
     * Return the string used in the document.
     *
     * @return string value
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Return the description of the meaning of the value.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the enumeration that matches the supplied document value (case-insensitive).
     *
     * @param value string from the document
     * @return matching enumeration or null if the value is not one of the standard values
     */
    public static BitolSupportScope fromValue(String value)
    {
        if (value != null)
        {
            for (BitolSupportScope candidate : BitolSupportScope.values())
            {
                if (candidate.value.equalsIgnoreCase(value))
                {
                    return candidate;
                }
            }
        }

        return null;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolSupportScope{value='" + value + "'}";
    }
}
