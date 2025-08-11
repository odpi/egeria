/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;


/**
 * The ProductGlossaryCategoryDefinition is used to populate the sustainability glossary.
 */
public enum ProductGlossaryCategoryDefinition
{
    /**
     * Basic terminology relating to digital products.
     */
    BASICS("The basics",
                  "Basic terminology relating to digital products."),

    /**
     * Terminology relating to digital subscriptions.
     */
    SUBSCRIPTIONS("Digital Subscriptions",
           "Terminology relating to digital subscriptions."),


    ;

    private final String name;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param name   unique id for the enum
     * @param description   description of the use of this value
     */
    ProductGlossaryCategoryDefinition(String name,
                                      String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getQualifiedName()
    {
        return "GlossaryCategory::" + name;
    }


    public String getName()
    {
        return name;
    }


    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductGlossaryCategoryDefinition{" + name + '}';
    }
}
