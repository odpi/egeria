/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;


/**
 * The DigitalProductsDefinition defined the basic folder hierarchy for Coco Pharmaceuticals' digital
 * product catalog.  It makes use of the subject area definitions.
 */
public enum DigitalProductsDefinition
{

    DIGITAL_PRODUCT_MANAGEMENT ("Governance:DigitalProductManagement",
                                ProductSolutionBlueprint.COCO_COMBO_PRODUCTS,
                                "Digital Product Management",
                                "Information relating to the management of digital products."),
    ;


    private final String                   productIdentifier;
    private final ProductSolutionBlueprint solutionBlueprint;
    private final String                   displayName;
    private final String                                 description;


    DigitalProductsDefinition(String                   identifier,
                              ProductSolutionBlueprint solutionBlueprint,
                              String                   displayName,
                              String                   description)
    {
        this.productIdentifier = identifier;
        this.solutionBlueprint = solutionBlueprint;
        this.displayName       = displayName;
        this.description     = description;
    }



    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "DigitalProductCatalogFolder::" + productIdentifier;
    }


    /**
     * Returns the display name for the folder.
     *
     * @return unique name
     */
    public String getProductIdentifier()
    {
        return productIdentifier;
    }


    /**
     * Return the name of the parent subject area - null for top level.
     *
     * @return subject area name.
     */
    public ProductSolutionBlueprint getSolutionBlueprint()
    {
        return solutionBlueprint;
    }


    /**
     * Returns a descriptive name of the zone.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the assets within the zone.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "CocoSubjectAreaDefinition{" +
                       "subjectAreaName='" + productIdentifier + '\'' + '}';
    }
}
