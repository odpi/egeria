/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.refdata;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * CollectionType lists suggested collection type values.  These are in addition to the classifications associated with a collection.
 */
public enum CollectionType
{
    /**
     * A collection of guided activities either suggested, or taken, to complete a project.
     */
    ACTIVITY_FOLDER("Activity Folder",
                    "A collection of guided activities either suggested, or taken, to complete a project."),

    /**
     * A collection of data field descriptions that characterize a data asset needed by the project.
     */
    DATA_SPEC("Data Spec",
         "A collection of data field descriptions that characterize a data asset needed by the project."),

    /**
     * The assets and related material that make up a digital product offering.
     */
    PRODUCT_CONTENT("Product Content",
        "The assets and related material that make up a digital product offering."),

    /**
     * The root of a folder hierarchy that describes a product catalog.
     */
    PRODUCT_CATALOG_ROOT("Product Catalog Root",
                    "The root of a folder hierarchy that describes a product catalog."),

    /**
     * The root of a folder hierarchy that describes an asset catalog.
     */
    ASSET_CATALOG_ROOT("Asset Catalog Root",
                         "The root of a folder hierarchy that describes an asset catalog."),


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
    CollectionType(String name,
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
                                                OpenMetadataProperty.PROJECT_PHASE.name,
                                                null,
                                                name);
    }


    /**
     * Return the category for this value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueCategory(null,
                                           OpenMetadataProperty.PROJECT_PHASE.name,
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
        return "ProjectPhase{" + name + '}';
    }
}
