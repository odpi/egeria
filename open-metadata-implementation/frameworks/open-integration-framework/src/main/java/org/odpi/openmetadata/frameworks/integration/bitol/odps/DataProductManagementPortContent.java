/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

/**
 * DataProductManagementPortContent lists the types of content available through a management port of an ODPS data product.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataProductManagementPortContent
{
    /**
     * Information that helps consumers find and understand the data product.
     */
    DISCOVERABILITY("discoverability", "Information that helps consumers find and understand the data product."),

    /**
     * Operational metrics and health information about the data product.
     */
    OBSERVABILITY  ("observability", "Operational metrics and health information about the data product."),

    /**
     * Operations that control the data product.
     */
    CONTROL        ("control", "Operations that control the data product."),

    /**
     * The data dictionary of the data product.
     */
    DICTIONARY     ("dictionary", "The data dictionary of the data product.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataProductManagementPortContent(String value,
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
    public static DataProductManagementPortContent fromValue(String value)
    {
        if (value != null)
        {
            for (DataProductManagementPortContent candidate : DataProductManagementPortContent.values())
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
        return "DataProductManagementPortContent{value='" + value + "'}";
    }
}
