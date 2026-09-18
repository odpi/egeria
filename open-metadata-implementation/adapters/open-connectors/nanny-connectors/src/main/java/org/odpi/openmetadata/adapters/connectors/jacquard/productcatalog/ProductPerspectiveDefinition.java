/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductPerspectiveDefinition is used to populate the perspectives associated with the digital products.
 */
public enum ProductPerspectiveDefinition
{
    SECURITY("ef656a56-849f-4546-beb2-69564d3a61f4",
             "Security",
             "SECURITY-PERSPECTIVE",
             "A focus on the security of the organization's resources.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/ivor-padlock/"),

    GOVERNANCE("73ce1583-612c-4d70-9287-5bf34bb1af85",
               "Governance",
             "GOVERNANCE-PERSPECTIVE",
             "A focus on the executive and policy-led oversight of the organization's operations.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/jules-keeper/"),

    FINANCIAL("009bc3f4-45cf-4f6f-894c-113cde6f35d6",
              "Financial",
              "FINANCIAL-PERSPECTIVE",
              "A focus on the cost and effective financial management of the organization's operations.",
              "https://egeria-project.org/practices/coco-pharmaceuticals/personas/reggie-mint/"),

    STEWARD("f5f5251a-da44-43ea-92cc-afe726b2798b",
            "Steward",
             "STEWARD-PERSPECTIVE",
             "A focus on protecting the quality and trustworthiness of specific resources of the organization.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/tanya-tidie/"),

    OWNER("8e7e8e7b-1a40-48d8-b687-6b96303d53c8",
          "Owner",
             "OWNER-PERSPECTIVE",
             "A focus on the the existence, protection and investment, of specific resources of the organization.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/tessa-tube/"),

    CONSUMER("c996ada0-8c32-406d-90dc-c89cddc7b6c2",
             "Consumer",
             "CONSUMER-PERSPECTIVE",
             "A focus on the selection and use of appropriate resources of the organization.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/callie-quartile/"),

    APP_AI_BUILDER("7a9b5c5e-0eff-4e60-933b-8eb0737c2eeb",
                   "App/AI Builder",
                   "APP-AI-PERSPECTIVE",
                   "A focus on selecting resources to support the development of applications and AI models.",
                   "https://egeria-project.org/practices/coco-pharmaceuticals/personas/bob-nitter/"),

    PRIVACY("4beb3e56-887d-4442-97c1-ce788b4d70ab",
            "Privacy",
             "PRIVACY-PERSPECTIVE",
             "A focus on protecting the privacy of individuals and their data.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/faith-broker/"),

    COMMUNITY("9107a800-e354-4fea-82a8-e71e502e302c",
             "Community",
             "COMMUNITY-PERSPECTIVE",
             "A focus on the health of the community around a resource — contributor activity, responsiveness to issues, adoption — relevant mainly to community-driven resources like open-source repos organization's resources.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/tom-tally/"),

    DATA_EXPERT("82376297-93f9-4546-a034-968fc7cf7207",
             "Data Expert",
             "DATA-EXPERT-PERSPECTIVE",
             "A focus on developing valuable data resource for the organization. This involves understanding various data resource's quality, relevance, and utility.",
             "https://egeria-project.org/practices/coco-pharmaceuticals/personas/peter-profile/"),

    ARCHITECTURE("8bc76e59-d626-48a9-add6-92d8bb39ff55",
                 "Architecture",
                 "ARCHITECTURE-PERSPECTIVE",
                 "A focus on developing a coherent architecture for the resources of the organization. This involves understanding various data resource's quality, relevance, and utility.",
                 "https://egeria-project.org/practices/coco-pharmaceuticals/personas/erin-overview/"),

    ADMINISTRATION("cab3343b-8731-4b83-86ff-f988bf2143a5",
                   "Administration",
                   "ADMINISTRATOR-PERSPECTIVE",
                   "A focus on the operational and administrative aspects of the organization's resources. This involves the day-to-day upkeep, access, and reliability of these resources.",
                   "https://egeria-project.org/practices/coco-pharmaceuticals/personas/gary-geeke/"),

    ;

    private final String guid;
    private final String displayName;
    private final String identifier;
    private final String description;
    private final String url;


    /**
     * The constructor creates an instance of the enum
     *
     * @param guid  unique identifier for the enum
     * @param displayName  name for the enum
     * @param identifier      identifier for the enum
     * @param description  description of the use of this value
     * @param url          optional url for the term
     */
    ProductPerspectiveDefinition(String guid,
                                 String displayName,
                                 String identifier,
                                 String description,
                                 String url)
    {
        this.guid        = guid;
        this.displayName = displayName;
        this.identifier  = identifier;
        this.description = description;
        this.url         = url;
    }


    /**
     * Return the unique identifier for the enum.
     *
     * @return string guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the qualified name to use for the perspective.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return OpenMetadataType.PERSPECTIVE.typeName + "::" + displayName;
    }


    /**
     * Return the display name for this term.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the summary for this term.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the description for this term.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL for this term.
     *
     * @return string
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Return the folder for this term.
     *
     * @return ProductFolderDefinition
     */
    public ProductFolderDefinition getFolder()
    {
        return ProductFolderDefinition.PERSPECTIVES;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductPerspectiveDefinition{" + identifier + '}';
    }
}
