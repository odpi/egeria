/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * CollectionType lists suggested collection type values.  These are in addition to the classifications associated with a collection.
 */
public enum CollectionType
{
    /**
     * List of governance domain identifiers.
     */
    GOVERNANCE_DOMAIN_SET("Governance Domain Set",
                          "List of governance domain identifiers."),

    /**
     * A set of guided tasks to enable a new data-oriented use case.
     */
    DATA_JOURNEY("Data Journey",
                 "A set of guided tasks to enable a new data-oriented use case."),

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
    DIGITAL_PRODUCT_CONTENT("Digital Product Content",
        "The assets and related material that make up a digital product offering."),

    /**
     * The root of a folder hierarchy that describes a product catalog.
     */
    DIGITAL_PRODUCT_MARKETPLACE("Digital Product Marketplace",
                    "The root of a folder hierarchy that organizes a product catalog."),

    /**
     * A folder in the digital product catalog used to group similar products together.
     */
    DIGITAL_PRODUCT_CATEGORY("Digital Product Category",
                             "A folder in the digital product catalog used to group similar products together."),

    /**
     * The root of a folder hierarchy that describes an asset catalog.
     */
    ASSET_CATALOG_ROOT("Asset Catalog Root",
                         "The root of a folder hierarchy that organizes an asset catalog."),

    /**
     * A folder in the digital product catalog used to group similar products together.
     */
    ASSET_CATALOG_CATEGORY("Asset Catalog Category",
                             "A folder in the asset catalog used to group similar assets together."),


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
                                                OpenMetadataProperty.CATEGORY.name,
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
        return constructValidValueNamespace(null,
                                            OpenMetadataProperty.CATEGORY.name,
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
        return "CollectionType{" + name + '}';
    }
}
