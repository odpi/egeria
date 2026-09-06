/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractSLADriver lists the standard reasons (drivers) behind a service level agreement property in an ODCS data contract.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractSLADriver
{
    /**
     * The SLA property is required by a regulation.
     */
    REGULATORY ("regulatory", "The SLA property is required by a regulation."),

    /**
     * The SLA property is required by analytical use of the data.
     */
    ANALYTICS  ("analytics", "The SLA property is required by analytical use of the data."),

    /**
     * The SLA property is required by operational use of the data.
     */
    OPERATIONAL("operational", "The SLA property is required by operational use of the data.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractSLADriver(String value,
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
    public static DataContractSLADriver fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractSLADriver candidate : DataContractSLADriver.values())
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
        return "DataContractSLADriver{value='" + value + "'}";
    }
}
