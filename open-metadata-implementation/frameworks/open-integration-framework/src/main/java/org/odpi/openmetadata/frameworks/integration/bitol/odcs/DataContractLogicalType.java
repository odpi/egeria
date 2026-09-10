/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractLogicalType lists the platform independent logical types of a schema property in an ODCS data contract.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractLogicalType
{
    /**
     * A sequence of characters.
     */
    STRING   ("string", "A sequence of characters."),

    /**
     * A calendar date.
     */
    DATE     ("date", "A calendar date."),

    /**
     * A date and time, optionally with a timezone.
     */
    TIMESTAMP("timestamp", "A date and time, optionally with a timezone."),

    /**
     * A time of day, optionally with a timezone.
     */
    TIME     ("time", "A time of day, optionally with a timezone."),

    /**
     * A number that may have a fractional part.
     */
    NUMBER   ("number", "A number that may have a fractional part."),

    /**
     * A whole number.
     */
    INTEGER  ("integer", "A whole number."),

    /**
     * A structure with nested properties.
     */
    OBJECT   ("object", "A structure with nested properties."),

    /**
     * A list of items of the type described by the items property.
     */
    ARRAY    ("array", "A list of items of the type described by the items property."),

    /**
     * A true or false value.
     */
    BOOLEAN  ("boolean", "A true or false value."),

    /**
     * A key/value (dictionary) structure whose key and value are described by the map object of the property.
     */
    MAP      ("map", "A key/value (dictionary) structure whose key and value are described by the map object of the property."),

    /**
     * A fixed-dimension dense numeric array for embeddings and similarity search.
     */
    VECTOR   ("vector", "A fixed-dimension dense numeric array for embeddings and similarity search.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractLogicalType(String value,
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
    public static DataContractLogicalType fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractLogicalType candidate : DataContractLogicalType.values())
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
        return "DataContractLogicalType{value='" + value + "'}";
    }
}
