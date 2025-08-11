/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

import java.util.List;

/**
 * Define the linkage between solution components defined for Coco Pharmaceuticals.
 * Still experimenting on the usage of the
 */
public enum SolutionComponentWire
{

    MOVE_FILE_TO_SEEK_ORIGIN(ProductSolutionComponent.COCO_ARCHIVE,
                             ProductSolutionComponent.COCO_ARCHIVE,
                             "provisioning complete",
                             "The data is in position and ready for the origin to be validated."),

    ;

    final ProductSolutionComponent component1;
    final ProductSolutionComponent component2;
    final String                   label;
    final String                   description;

    SolutionComponentWire(ProductSolutionComponent component1,
                          ProductSolutionComponent component2,
                          String                   label,
                          String                   description)
    {
        this.component1              = component1;
        this.component2              = component2;
        this.label                   = label;
        this.description             = description;
    }


    public ProductSolutionComponent getComponent1()
    {
        return component1;
    }

    public ProductSolutionComponent getComponent2()
    {
        return component2;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }

    public List<String> getISCQualifiedNames()
    {
        return null;
    }
}
