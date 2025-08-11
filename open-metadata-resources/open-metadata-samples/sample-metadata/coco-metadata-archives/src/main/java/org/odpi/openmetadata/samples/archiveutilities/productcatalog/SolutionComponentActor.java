/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

/**
 * Define the relationship between the solution roles and the solution components
 */
public enum SolutionComponentActor
{
    PRODUCT_MANAGER_TO_ARCHIVE(ProductRoleDefinition.PRODUCT_MANAGER,
                               ProductSolutionComponent.COCO_ARCHIVE,
                               "Sources content",
                               "Validates content, looking for suitable products"),

    ;

    final ProductRoleDefinition    solutionRole;
    final ProductSolutionComponent productSolutionComponent;
    final String                   role;
    final String                 description;

    SolutionComponentActor(ProductRoleDefinition solutionRole,
                           ProductSolutionComponent productSolutionComponent,
                           String                 role,
                           String                 description)
    {
        this.solutionRole             = solutionRole;
        this.productSolutionComponent = productSolutionComponent;
        this.role                     = role;
        this.description       = description;
    }


    public ProductRoleDefinition getSolutionRole()
    {
        return solutionRole;
    }

    public ProductSolutionComponent getSolutionComponent()
    {
        return productSolutionComponent;
    }

    public String getRole()
    {
        return role;
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
        return "SolutionComponentActor{" +
                "solutionRole=" + solutionRole +
                ", productSolutionComponent=" + productSolutionComponent +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                "}";
    }
}
