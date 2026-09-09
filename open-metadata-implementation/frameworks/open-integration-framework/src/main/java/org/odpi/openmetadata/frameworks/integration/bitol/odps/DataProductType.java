/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

/**
 * DataProductType lists the common architectural types of an ODPS data product (v1.1.0, RFC 0029); organizations may define their own.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataProductType
{
    /**
     * A data product that exposes the data of a single source system in the form the source produces it.
     */
    SOURCE_ALIGNED  ("sourceAligned", "A data product that exposes the data of a single source system in the form the source produces it."),

    /**
     * A data product that combines data from several other data products.
     */
    AGGREGATE       ("aggregate", "A data product that combines data from several other data products."),

    /**
     * A data product shaped for a particular consumer or use case.
     */
    CONSUMER_ALIGNED("consumerAligned", "A data product shaped for a particular consumer or use case.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataProductType(String value,
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
    public static DataProductType fromValue(String value)
    {
        if (value != null)
        {
            for (DataProductType candidate : DataProductType.values())
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
        return "DataProductType{value='" + value + "'}";
    }
}
