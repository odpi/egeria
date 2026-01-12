/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductRoleDefinition is used to feed the definition of the actor roles for
 * Coco Pharmaceuticals' product catalog.
 */
public enum ProductRoleDefinition
{
    PRODUCT_MANAGER("eca8fc08-224b-462f-ba21-00cb73795c69",
                    OpenMetadataType.DIGITAL_PRODUCT_MANAGER.typeName,
                    "OpenMetadataProductManagerRole",
                    "Open Metadata Product Manager",
                    "The product manager role covering the digital products built around open metadata."),

    PRODUCT_DEVELOPER("14d45536-19a5-42a0-820d-8cb82cb9d162",
                      OpenMetadataType.PERSON_ROLE.typeName,
                      "OpenMetadataProductDeveloperRole",
                      "Open Metadata Product Developer",
                      "The product developer role covers the implementation of the digital products built around open metadata."),


    PRODUCT_SUPPORT("744d0310-2d88-4044-bfa3-775e6186576c",
                    OpenMetadataType.PERSON_ROLE.typeName,
                    "OpenMetadataProductSupportRole",
                    "Open Metadata Product Support Role",
                    "The product support role covering the support of the digital products built around open metadata.  This includes the contents and subscription issues."),


    PRODUCT_CONSUMER("f36d049c-5a49-4131-9615-afbfcbf0c08a",
                     OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "OpenMetadataProductConsumer",
                     "Open Metadata Product Subscriber",
                     "Subscriber to one of the open metadata data products from Egeria."),

    PRODUCT_COMMUNITY_MEMBER("25c06984-637b-4604-b19e-ee920358349d",
                     OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "OpenMetadataProductMember",
                     "Open Metadata Digital Product Community Member",
                     "Community to exchange information and collect feedback about the open metadata digital products."),

    EGERIA_COMMUNITY("c8df1738-674f-489b-afe2-aea1086881f7",
                             OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                             "EgeriaOpenSourceCommunity",
                             "Egeria Open Source Community",
                             "Open Source Community developing the Egeria Software and related resources."),

    ;

    private final String guid;
    private final String typeName;
    private final String identifier;
    private final String displayName;
    private final String description;


    /**
     * ProductRoleDefinition constructor creates an instance of the enum
     *
     * @param guid         unique identifier for the role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param description   description of the assets in the role
     */
    ProductRoleDefinition(String guid,
                          String typeName,
                          String identifier,
                          String displayName,
                          String description)
    {
        this.guid        = guid;
        this.typeName    = typeName;
        this.identifier  = identifier;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Return the name of the PersonRole type to use.
     *
     * @return type name
     */
    public String getGUID()
    {
        return guid;
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
        return typeName + "::" + guid + "::" + identifier;
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
