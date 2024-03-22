/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates;


/**
 * The GlossaryCategoryDefinition is used to populate the teddy bear drop foot glossary.
 */
public enum GlossaryCategoryDefinition
{
    /**
     * These terms describe the types of data collected during the Teddy Bear Drop Foot study.
     */
    DATA_FIELDS("Teddy Bear Drop Foot Data Fields",
                "These terms describe the types of data collected during the Teddy Bear Drop Foot study."),


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
        return "GlossaryCategory:TeddyBearDropFootTerminology:" + name;
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
