/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractSemanticType lists the semantic roles a schema property can play in an ODCS data contract (v3.2.0, RFC 0034).
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractSemanticType
{
    /**
     * A physical column in the underlying data store; the default when no semantic type is given.
     */
    COLUMN   ("column", "A physical column in the underlying data store; the default when no semantic type is given."),

    /**
     * An aggregated value, such as SUM(revenue), whose aggregation expression is held in the transform logic.
     */
    MEASURE  ("measure", "An aggregated value, such as SUM(revenue), whose aggregation expression is held in the transform logic."),

    /**
     * A categorical attribute used for grouping and filtering.
     */
    DIMENSION("dimension", "A categorical attribute used for grouping and filtering.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractSemanticType(String value,
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
    public static DataContractSemanticType fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractSemanticType candidate : DataContractSemanticType.values())
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
        return "DataContractSemanticType{value='" + value + "'}";
    }
}
