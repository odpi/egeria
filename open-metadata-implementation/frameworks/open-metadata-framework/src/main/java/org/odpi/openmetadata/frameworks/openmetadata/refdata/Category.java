/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * Category lists suggested category values.  These are used in referenceables.
 */
public enum Category
{
    /**
     * List of values for an open metadata attribute.
     */
    VALID_METADATA_VALUES("Valid Metadata Values",
                          "List of values for a open metadata attribute."),

    /**
     * List of properties values to provide to a command, connector, template or service.
     */
    SPECIFICATION_PROPERTY("Specification Property",
                          "List of properties values to provide to a command, connector, template or service."),

    /**
     * List of open metadata type information.
     */
    OPEN_METADATA_TYPES("Open Metadata Types",
                           "List of open metadata type information."),

    /**
     * A set of guided tasks to enable a new data-oriented use case.
     */
    SUSTAINABILITY("Sustainability",
                 "Resources to support sustainability initiatives."),

    /**
     * A collection of guided activities either suggested, or taken, to complete a project.
     */
    CLINICAL_TRIALS("Clinical Trials",
                    "Resources supporting clinical trials."),

    /**
     * A set of guided tasks to enable a new data-oriented use case.
     */
    DATA_JOURNEY("Data Journey",
                         "A set of guided tasks to enable a new data-oriented use case."),
    ;

    /**
     * Property value.
     */
    private final String name;


    /**
     * Property value description.
     */
    private final String description;


    /**
     * Constructor for individual enum value.
     *
     * @param name the property value to use in project status
     * @param description description of the project status property value
     */
    Category(String name,
             String description)
    {
        this.name        = name;
        this.description = description;
    }


    /**
     * Return the name of the value.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }



    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.CATEGORY.name,
                                                null,
                                                name);
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CollectionType{" + name + '}';
    }
}
