/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;

/**
 * The ProductFolderDefinition describes the different folders in the Open Metadata Product Catalog.
 * This includes the top-level folder for the whole catalog.
 */
public enum ProductFolderDefinition
{
    /**
     * Top level collection for
     */
    TOP_LEVEL(   "Organization",
                    null,
                    "Organization",
                    "Information relating to an organization.",
                    "Reference Data"),


    ;


    private final String                  classificationName;
    private final ProductFolderDefinition parent;
    private final String                    displayName;
    private final String description;
    private final String category;



    ProductFolderDefinition(String                  classificationName,
                            ProductFolderDefinition parent,
                            String                  displayName,
                            String                  description,
                            String                  category)
    {
        this.classificationName = classificationName;
        this.parent             = parent;
        this.displayName        = displayName;
        this.description        = description;
        this.category           = category;
    }



    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "ProductCatalog::" + classificationName + "::" + displayName;
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return unique name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Return the name of the parent subject area - null for top level.
     *
     * @return subject area name.
     */
    public ProductFolderDefinition getParent()
    {
        return parent;
    }


    /**
     * Returns a descriptive name of the folder.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the products under the folder.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns a description of the organizational scope for the use of this subject area.
     *
     * @return scope
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "ProductFolderDefinition{" +
                "classificationName='" + classificationName + '\'' +
                ", parent=" + parent +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                "} " + super.toString();
    }
}
