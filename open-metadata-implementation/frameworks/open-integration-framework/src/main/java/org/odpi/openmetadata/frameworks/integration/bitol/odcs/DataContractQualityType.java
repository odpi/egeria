/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractQualityType lists the types of data quality check in an ODCS data contract.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractQualityType
{
    /**
     * A check described in text only, intended for humans.
     */
    TEXT   ("text", "A check described in text only, intended for humans."),

    /**
     * A check that evaluates a standard metric (see DataContractQualityMetric) with a comparison operator.
     */
    LIBRARY("library", "A check that evaluates a standard metric (see DataContractQualityMetric) with a comparison operator."),

    /**
     * A check that evaluates a SQL query with a comparison operator.
     */
    SQL    ("sql", "A check that evaluates a SQL query with a comparison operator."),

    /**
     * A check implemented by a named data quality engine.
     */
    CUSTOM ("custom", "A check implemented by a named data quality engine.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractQualityType(String value,
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
    public static DataContractQualityType fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractQualityType candidate : DataContractQualityType.values())
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
        return "DataContractQualityType{value='" + value + "'}";
    }
}
