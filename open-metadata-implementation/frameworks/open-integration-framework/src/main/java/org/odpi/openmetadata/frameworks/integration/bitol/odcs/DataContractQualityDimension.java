/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractQualityDimension lists the data quality dimensions used in an ODCS data contract.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractQualityDimension
{
    /**
     * The data correctly describes the real world.
     */
    ACCURACY    ("accuracy", "The data correctly describes the real world."),

    /**
     * All required data is present.
     */
    COMPLETENESS("completeness", "All required data is present."),

    /**
     * The data follows the required formats and standards.
     */
    CONFORMITY  ("conformity", "The data follows the required formats and standards."),

    /**
     * The data does not contradict itself or related data.
     */
    CONSISTENCY ("consistency", "The data does not contradict itself or related data."),

    /**
     * The data covers the required population.
     */
    COVERAGE    ("coverage", "The data covers the required population."),

    /**
     * The data is available when it is needed.
     */
    TIMELINESS  ("timeliness", "The data is available when it is needed."),

    /**
     * There are no unwanted duplicates.
     */
    UNIQUENESS  ("uniqueness", "There are no unwanted duplicates.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractQualityDimension(String value,
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
    public static DataContractQualityDimension fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractQualityDimension candidate : DataContractQualityDimension.values())
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
        return "DataContractQualityDimension{value='" + value + "'}";
    }
}
