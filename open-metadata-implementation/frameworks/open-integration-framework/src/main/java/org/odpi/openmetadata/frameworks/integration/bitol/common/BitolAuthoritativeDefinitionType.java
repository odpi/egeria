/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

/**
 * BitolAuthoritativeDefinitionType lists the standard types of link to an authoritative source of information about an element.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum BitolAuthoritativeDefinitionType
{
    /**
     * A link to the business definition of the element.
     */
    BUSINESS_DEFINITION          ("businessDefinition", "A link to the business definition of the element."),

    /**
     * A link to the implementation of the transformation that produces the element.
     */
    TRANSFORMATION_IMPLEMENTATION("transformationImplementation", "A link to the implementation of the transformation that produces the element."),

    /**
     * A link to a video tutorial.
     */
    VIDEO_TUTORIAL               ("videoTutorial", "A link to a video tutorial."),

    /**
     * A link to a tutorial.
     */
    TUTORIAL                     ("tutorial", "A link to a tutorial."),

    /**
     * A link to the implementation of the element, for example a JDBC URL.
     */
    IMPLEMENTATION               ("implementation", "A link to the implementation of the element, for example a JDBC URL."),

    /**
     * The canonical URL of the element.
     */
    CANONICAL_URL                ("canonicalUrl", "The canonical URL of the element."),

    /**
     * A link to the glossary entry that defines the element.
     */
    GLOSSARY                     ("glossary", "A link to the glossary entry that defines the element."),

    /**
     * A link to the ontology concept that defines the element.
     */
    ONTOLOGY                     ("ontology", "A link to the ontology concept that defines the element."),

    /**
     * A link to the taxonomy node that classifies the element.
     */
    TAXONOMY                     ("taxonomy", "A link to the taxonomy node that classifies the element.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    BitolAuthoritativeDefinitionType(String value,
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
    public static BitolAuthoritativeDefinitionType fromValue(String value)
    {
        if (value != null)
        {
            for (BitolAuthoritativeDefinitionType candidate : BitolAuthoritativeDefinitionType.values())
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
        return "BitolAuthoritativeDefinitionType{value='" + value + "'}";
    }
}
