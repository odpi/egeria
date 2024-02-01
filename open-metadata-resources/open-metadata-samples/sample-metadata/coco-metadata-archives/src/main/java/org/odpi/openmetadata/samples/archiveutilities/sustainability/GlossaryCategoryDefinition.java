/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


/**
 * The GlossaryCategoryDefinition is used to populate the sustainability glossary.
 */
public enum GlossaryCategoryDefinition
{
    /**
     * Types of chemicals that are significant in managing sustainability.
     */
    CHEMICALS("Chemicals",
                  "Types of chemicals that are significant in managing sustainability."),


    ;

    private final String name;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param name   unique id for the enum
     * @param description   description of the use of this value
     */
    GlossaryCategoryDefinition(String name,
                               String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getQualifiedName()
    {
        return "GlossaryCategory:" + name;
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
        return "GlossaryCategoryDefinition{" + name + '}';
    }
}
