/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractQualityMetric lists the standard metrics evaluated by a library data quality check in an ODCS data contract.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractQualityMetric
{
    /**
     * The number of null values.
     */
    NULL_VALUES     ("nullValues", "The number of null values."),

    /**
     * The number of missing values (nulls and values in the arguments' missing list).
     */
    MISSING_VALUES  ("missingValues", "The number of missing values (nulls and values in the arguments' missing list)."),

    /**
     * The number of values that fail the validity rules.
     */
    INVALID_VALUES  ("invalidValues", "The number of values that fail the validity rules."),

    /**
     * The number of duplicated values.
     */
    DUPLICATE_VALUES("duplicateValues", "The number of duplicated values."),

    /**
     * The number of rows.
     */
    ROW_COUNT       ("rowCount", "The number of rows.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractQualityMetric(String value,
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
    public static DataContractQualityMetric fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractQualityMetric candidate : DataContractQualityMetric.values())
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
        return "DataContractQualityMetric{value='" + value + "'}";
    }
}
