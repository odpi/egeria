/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

/**
 * BitolStatus lists the standard lifecycle status values of a Bitol data contract (ODCS) or data product (ODPS).
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum BitolStatus
{
    /**
     * The document has been proposed but not yet worked on.
     */
    PROPOSED  ("proposed", "The document has been proposed but not yet worked on."),

    /**
     * The document is being drafted and is not yet ready for use.
     */
    DRAFT     ("draft", "The document is being drafted and is not yet ready for use."),

    /**
     * The document is in force and the data is available for use.
     */
    ACTIVE    ("active", "The document is in force and the data is available for use."),

    /**
     * The document is still in force but consumers should migrate away from it.
     */
    DEPRECATED("deprecated", "The document is still in force but consumers should migrate away from it."),

    /**
     * The document is no longer in force and the data is no longer available.
     */
    RETIRED   ("retired", "The document is no longer in force and the data is no longer available.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    BitolStatus(String value,
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
    public static BitolStatus fromValue(String value)
    {
        if (value != null)
        {
            for (BitolStatus candidate : BitolStatus.values())
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
        return "BitolStatus{value='" + value + "'}";
    }
}
