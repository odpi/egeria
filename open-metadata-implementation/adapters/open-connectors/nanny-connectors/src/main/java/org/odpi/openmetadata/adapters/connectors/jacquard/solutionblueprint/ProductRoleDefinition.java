/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;

/**
 * The ProductRoleDefinition is used to feed the definition of the actor roles for
 * Coco Pharmaceuticals' product catalog.
 */
public enum ProductRoleDefinition
{
    /**
     * The product manager role covering the digital products built around open metadata.  This includes the data contents and subscription issues.
     */
    PRODUCT_MANAGER(OpenMetadataType.DIGITAL_PRODUCT_MANAGER.typeName,
                    "OpenMetadataProductManagerRole",
                    "Open Metadata Product Manager",
                    "The product manager role covering the digital products built around open metadata.  This includes the data contents and subscription issues.",
                    new ProductSolutionBlueprint[] {
                            ProductSolutionBlueprint.ALL
                    }),

    /**
     * The product developer role covers the implementation of the digital products built around open metadata.
     */
    PRODUCT_DEVELOPER( OpenMetadataType.IT_PROFILE_ROLE.typeName,
                      "OpenMetadataProductDeveloperRole",
                      "Open Metadata Product Developer",
                      "The product developer role covers the implementation of the digital products built around open metadata.",
                       new ProductSolutionBlueprint[] {
                               ProductSolutionBlueprint.ALL
                       }),


    /**
     * The Jacquard support role covers the support of the Jacquard Digital Product Loom connector and subscription management.
     */
    JACQUARD_SUPPORT(OpenMetadataType.PERSON_ROLE.typeName,
                     "JacquardSupportRole",
                     "Open Metadata Digital Product Loom (Jacquard) Support Role",
                     "The Jacquard support role covers the support of the Jacquard Digital Product Loom connector and subscription management.",
                     new ProductSolutionBlueprint[] {
                             ProductSolutionBlueprint.ALL
                     }),


    /**
     * Subscriber to one of the open metadata data products from the Open Metadata Ecosystem.
     */
    PRODUCT_SUBSCRIBER(OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "OpenMetadataProductSubscriber",
                     "Open Metadata Product Subscriber",
                     "Subscriber to one of the open metadata data products from Open Metadata Ecosystem.",
                       new ProductSolutionBlueprint[] {
                               ProductSolutionBlueprint.ALL,
                               ProductSolutionBlueprint.CANCELLING_SUBSCRIPTION,
                               ProductSolutionBlueprint.CREATING_SUBSCRIPTION,
                               ProductSolutionBlueprint.JACQUARD,
                               ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER,
                               ProductSolutionBlueprint.SEARCHING_FOR_DATA
                       }),


    /**
     * Consumer of the data provisioned from one of the open metadata data products from the Open Metadata Ecosystem.
     */
    PRODUCT_CONSUMER(OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "OpenMetadataProductConsumer",
                     "Open Metadata Product Consumer",
                     "Consumer of the data provisioned from one of the open metadata data products from the Open Metadata Ecosystem.",
                     new ProductSolutionBlueprint[] {
                             ProductSolutionBlueprint.ALL,
                             ProductSolutionBlueprint.CANCELLING_SUBSCRIPTION,
                             ProductSolutionBlueprint.CREATING_SUBSCRIPTION,
                             ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER
                     }),


    /**
     * Community to exchange information and collect feedback about the open metadata digital products.
     */
    PRODUCT_COMMUNITY_MEMBER(OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "OpenMetadataProductMember",
                     "Open Metadata Digital Product Community Member",
                     "Community to exchange information and collect feedback about the open metadata digital products.",
                             new ProductSolutionBlueprint[] {
                                     ProductSolutionBlueprint.ALL,
                                     ProductSolutionBlueprint.PRODUCT_COMMUNITY
                             }),

    ;

    private final String typeName;
    private final String identifier;
    private final String displayName;
    private final String description;
    private final ProductSolutionBlueprint[] consumingBlueprints;


    /**
     * ProductRoleDefinition constructor creates an instance of the enum
     *
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param description   description of the assets in the role
     */
    ProductRoleDefinition(String typeName,
                          String identifier,
                          String displayName,
                          String description,
                          ProductSolutionBlueprint[] consumingBlueprints)
    {
        this.typeName    = typeName;
        this.identifier  = identifier;
        this.displayName = displayName;
        this.description = description;
        this.consumingBlueprints    = consumingBlueprints;
    }


    /**
     * Return the type name
     *
     * @return string
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Returns the unique name for the role entity.
     *
     * @return identifier
     */
    public String getQualifiedName()
    {
        return typeName + "::Jacquard::" + identifier;
    }


    /**
     * Returns the unique name for the role.
     *
     * @return identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Returns a descriptive name of the role.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the role.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the list of blueprints to connect this component to.
     *
     * @return list or null
     */
    public List<ProductSolutionBlueprint> getConsumingBlueprints()
    {
        if (consumingBlueprints == null)
        {
            return null;
        }

        return Arrays.asList(consumingBlueprints);
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductRoleDefinition{" + "identifier='" + identifier + '}';
    }
}
