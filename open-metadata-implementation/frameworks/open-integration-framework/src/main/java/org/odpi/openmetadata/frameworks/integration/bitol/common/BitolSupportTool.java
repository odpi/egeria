/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

/**
 * BitolSupportTool lists the standard tools that host a support or communication channel.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum BitolSupportTool
{
    /**
     * An email address or distribution list.
     */
    EMAIL      ("email", "An email address or distribution list."),

    /**
     * A Slack channel.
     */
    SLACK      ("slack", "A Slack channel."),

    /**
     * A Microsoft Teams channel.
     */
    TEAMS      ("teams", "A Microsoft Teams channel."),

    /**
     * A Discord channel.
     */
    DISCORD    ("discord", "A Discord channel."),

    /**
     * A ticketing (issue tracking) system.
     */
    TICKET     ("ticket", "A ticketing (issue tracking) system."),

    /**
     * A Google Chat space.
     */
    GOOGLE_CHAT("googlechat", "A Google Chat space."),

    /**
     * A tool not covered by the other values.
     */
    OTHER      ("other", "A tool not covered by the other values.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    BitolSupportTool(String value,
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
    public static BitolSupportTool fromValue(String value)
    {
        if (value != null)
        {
            for (BitolSupportTool candidate : BitolSupportTool.values())
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
        return "BitolSupportTool{value='" + value + "'}";
    }
}
