/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;


/**
 * The ProductGlossaryTermDefinition is used to populate the sustainability glossary.
 */
public enum ProductGlossaryTermDefinition
{
    /**
     * A means of operating that makes effective use of replaceable resources.
     */
    DIGITAL_PRODUCT("Digital Product",
                    "A digital product describes a collection of digital resources that are produced for a specific type of consumer or purpose. They conform to a specification and offer guarantees of service. They may also have terms and conditions associated with their use.",
                    "There are different ways to implement a digital product. For example:\n" +
                            "\n" +
                            "*    A data product is an example of a digital product that delivers a collection of data. Each consumer of the product gets the same data.\n" +
                            "*    An insight service is an example of a digital product that provides information based on query values from the consumer. Different consumers may receive different results.\n" +
                            "*    An embedded feature, such as a smartphone app, is an example of a digital product that is running in the consumer's environment/hardware.\n" +
                            "\n" +
                            "Digital products are designed to be shared and may be advertised in a marketplace. They are typically owned and consumed by teams responsible for business capabilities. There is typical a person, or team responsible for development of a digital product.\n",
                    "",
                    "https://egeria-project.org/types/7/0710-Digital-Products/",
                    ProductGlossaryCategoryDefinition.BASICS),

        ;

    private final String                            name;
    private final String                            summary;
    private final String                            description;
    private final String                            abbreviation;
    private final String                            url;
    private final ProductGlossaryCategoryDefinition category;


    /**
     * The constructor creates an instance of the enum
     *
     * @param name   unique id for the enum
     * @param summary   name for the enum
     * @param description   description of the use of this value
     * @param abbreviation optional abbreviation
     * @param url optional url for the term
     * @param category optional category for the term
     */
    ProductGlossaryTermDefinition(String                     name,
                                  String                     summary,
                                  String                     description,
                                  String                     abbreviation,
                                  String                     url,
                                  ProductGlossaryCategoryDefinition category)
    {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.abbreviation = abbreviation;
        this.url = url;
        this.category = category;
    }

    public String getQualifiedName()
    {
        return "GlossaryTerm::" + name;
    }


    public String getName()
    {
        return name;
    }


    public String getSummary()
    {
        return summary;
    }


    public String getDescription()
    {
        return description;
    }


    public String getAbbreviation()
    {
        return abbreviation;
    }


    public String getUrl() { return url; }


    public ProductGlossaryCategoryDefinition getCategory() { return category; }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductGlossaryTermDefinition{" + summary + '}';
    }
}
