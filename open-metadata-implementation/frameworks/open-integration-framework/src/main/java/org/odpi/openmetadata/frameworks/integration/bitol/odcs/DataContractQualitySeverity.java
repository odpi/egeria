/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractQualitySeverity lists the standard severities of a failed data quality check in an ODCS data contract.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractQualitySeverity
{
    /**
     * A failure is informational and does not affect the use of the data.
     */
    INFO   ("info", "A failure is informational and does not affect the use of the data."),

    /**
     * A failure should be reviewed but the data remains usable.
     */
    WARNING("warning", "A failure should be reviewed but the data remains usable."),

    /**
     * A failure means the data does not meet the contract and should not be used.
     */
    ERROR  ("error", "A failure means the data does not meet the contract and should not be used.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractQualitySeverity(String value,
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
    public static DataContractQualitySeverity fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractQualitySeverity candidate : DataContractQualitySeverity.values())
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
        return "DataContractQualitySeverity{value='" + value + "'}";
    }
}
