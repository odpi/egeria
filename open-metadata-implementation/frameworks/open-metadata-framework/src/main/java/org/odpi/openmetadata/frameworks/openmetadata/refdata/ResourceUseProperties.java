/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ResourceUseProperties lists common property values found in the resourceUseProperties attribute when a particular
 * resourceUse value is in use.
 */
public enum ResourceUseProperties
{
    /**
     * The open metadata type name of a relationship to connect the linked element with a new element generated from the linked to template.
     */
    PARENT_RELATIONSHIP_TYPE_NAME("parentRelationshipTypeName",
                    "The open metadata type name of a relationship to connect the linked element with a new element generated from the linked to template."),

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
    ResourceUseProperties(String name,
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
                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                null,
                                                name);
    }


    /**
     * Return the namespace for this value.
     *
     * @return string
     */
    public String getNamespace()
    {
        return constructValidValueNamespace(null,
                                            OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                            null);
    }




    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ResourceUseProperties{" + name + '}';
    }
}
