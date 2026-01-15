/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint;

/**
 * Define the relationship between the solution roles and the solution components
 */
public enum SolutionComponentActor
{
    PRODUCT_SUPPORT_TO_HARVESTER(ProductRoleDefinition.JACQUARD_SUPPORT,
                                 ProductSolutionComponent.OPEN_METADATA_HARVESTER,
                                 "Maintains",
                                 "Maintains the Jacquard Digital Product Loom."),

    PRODUCT_MANAGER_TO_PRODUCT(ProductRoleDefinition.PRODUCT_MANAGER,
                                 ProductSolutionComponent.OPEN_METADATA_PRODUCT,
                                 "Develops",
                                 "Develops the product description and tabular data sources from different open metadata collections."),

    PRODUCT_COMMUNITY_MEMBERSHIP(ProductRoleDefinition.PRODUCT_COMMUNITY_MEMBER,
                               ProductSolutionComponent.PRODUCT_COMMUNITY_COMPONENT,
                               "Member of",
                               "Individuals from different parts of the organization can belong to the community to receive information about the products, supply new requirements, provide feedback about the products and to discuss how different consumers are using the products."),

    SUPPORT_COMMUNITY_MEMBERSHIP(ProductRoleDefinition.JACQUARD_SUPPORT,
                                 ProductSolutionComponent.PRODUCT_COMMUNITY_COMPONENT,
                                 "Jacquard and subscription manager discussions",
                                 "Individuals appointed to the support team are responsible for the overall health of the Jacquard Digital Product Loom connector and so use the community to discuss new features and improvements."),

    MANAGER_COMMUNITY_MEMBERSHIP(ProductRoleDefinition.PRODUCT_MANAGER,
                                 ProductSolutionComponent.PRODUCT_COMMUNITY_COMPONENT,
                                 "Product discussions",
                                 "The various product managers are responsible for the content of the various digital products.  The community offers a useful venue for discussing new requirements and content improvements."),

    SHOP_FOR_DATA(ProductRoleDefinition.PRODUCT_SUBSCRIBER,
                                 ProductSolutionComponent.PRODUCT_CATALOG,
                                 "Shop for data",
                                 "Perform searches on the product catalog to locate possible products for their project."),

    SUBSCRIBE_TO_PRODUCT(ProductRoleDefinition.PRODUCT_SUBSCRIBER,
                         ProductSolutionComponent.NEW_PRODUCT_SUBSCRIPTION,
                         "Subscribe to product",
                         "Request that a new subscription is activated.  The selected subscription type determines the level of service and the type of technology to use when provisioning the product data."),


    CANCEL_SUBSCRIPTION(ProductRoleDefinition.PRODUCT_SUBSCRIBER,
                         ProductSolutionComponent.CANCEL_PRODUCT_SUBSCRIPTION,
                         "Cancel product subscription",
                         "Request that an existing subscription pipeline is shutdown.  The subscription type determines whether the data at the destination is removed or not."),

    CONSUME_DATA(ProductRoleDefinition.PRODUCT_CONSUMER,
                 ProductSolutionComponent.PRODUCT_DELIVERY_LOCATION,
                 "Consume data",
                 "Request that an existing subscription pipeline is shutdown.  The subscription type determines whether the data at the destination is removed or not."),


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


    /**
     * Return the solution role to link to.
     *
     * @return role definition
     */
    public ProductRoleDefinition getSolutionRole()
    {
        return solutionRole;
    }

    /**
     * Return the solution component to link to.
     *
     * @return component definition
     */
    public ProductSolutionComponent getSolutionComponent()
    {
        return productSolutionComponent;
    }


    /**
     * Return the role relationship label.
     *
     * @return string
     */
    public String getRole()
    {
        return role;
    }


    /**
     * Return the role relationship description.
     *
     * @return string
     */
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
