/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductGlossaryTermDefinition is used to populate the product catalog glossary.
 */
public enum ProductGlossaryTermDefinition
{
    /**
     * A digital product describes a collection of digital resources that are produced for a specific type of consumer or purpose.
     */
    DIGITAL_PRODUCT("Digital Product",
                    "A digital product describes a collection of digital resources that are produced for a specific type of consumer or purpose. They conform to a specification and offer guarantees of service. They may also have terms and conditions associated with their use.",
                    """
                            There are different ways to implement a digital product. For example:
                            
                            *    A data product is an example of a digital product that delivers a collection of data. Each consumer of the product gets the same data.
                            *    An insight service is an example of a digital product that provides information based on query values from the consumer. Different consumers may receive different results.
                            *    An embedded feature, such as a smartphone app, is an example of a digital product that is running in the consumer's environment/hardware.
                            
                            Digital products are designed to be shared and may be advertised in a marketplace. They are typically owned and consumed by teams responsible for business capabilities. There is typical a person, or team responsible for development of a digital product.
                            """,
                    "",
                    "https://egeria-project.org/types/7/0710-Digital-Products/",
                    ProductFolderDefinition.GLOSSARY_BASICS),

    EVALUATION_SUBSCRIPTION("Evaluation Subscription",
                            "A subscription to a digital product that delivers the data to the target destination just once.",
                            "A subscription to a digital product that delivers the data to the target destination just once.  It is used when evaluating a product's data, and also when data delivery needs to be controlled by the subscriber - for example to ensure data values remain constant through a particular processing task (such as training an AI model).",
                            null,
                            "https://egeria-project.org/concepts/digital-subscription/",
                            ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    ONGOING_UPDATE_SUBSCRIPTION("Ongoing Update Subscription",
                                "A subscription to a digital product that delivers the data to the target destination within an hour of it changing.",
                                "A subscription to a digital product that delivers the data to the target destination within an hour of it changing.  This type of subscription ensures the latest information is always available to the consumer.",
                                null,
                                "https://egeria-project.org/concepts/digital-subscription/",
                                ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    DAILY_REFRESH_SUBSCRIPTION("Evaluation Subscription",
                               "A subscription to a digital product that delivers the data to the target destination once a day.",
                               "A subscription to a digital product that delivers the data to the target destination once a day.",
                               null,
                               "https://egeria-project.org/concepts/digital-subscription/",
                               ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    WEEKLY_REFRESH_SUBSCRIPTION("Evaluation Subscription",
                                "A subscription to a digital product that delivers the data to the target destination once a week.",
                                "A subscription to a digital product that delivers the data to the target destination once a week.",
                                null,
                                "https://egeria-project.org/concepts/digital-subscription/",
                                ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    GUID("Unique Identifier",
         "A unique identifier for an open metadata element.",
         "A unique identifier for an open metadata element.  It is used to ensure that each element is uniquely identifiable within the open metadata ecosystem.  It can be used to retrieve the properties of the element.",
         "GUID",
         "https://egeria-project.org/concepts/guid/",
         ProductFolderDefinition.GLOSSARY_DATA_ITEMS),

    RELATED_GUID("Related Unique Identifier",
         "A unique identifier for a related open metadata element.",
         "A unique identifier for an open metadata element that is linked to another element using a relationship.",
         "GUID",
         "https://egeria-project.org/concepts/guid/",
         ProductFolderDefinition.GLOSSARY_DATA_ITEMS),

    OPEN_METADATA_TYPE_NAME("Open Metadata Type Name",
                            "The name of the open metadata type for an open metadata element.",
                            "The name of the open metadata type for an open metadata element.  It identifies a type definition that defines the attributes that can be stored for an element of this type, and the relationships that can be used to link this element to other elements, and the classifications that can be applied to this element.",
                            "typeName",
                            "https://egeria-project.org/concepts/open-metadata-type-definitions/",
                            ProductFolderDefinition.GLOSSARY_DATA_ITEMS),

    USER_ID("User Identifier",
            "The name of the user account that uniquely identifies the user (person or process) behind an action or update to the open metadata ecosystem.",
            "Before accessing the open metadata ecosystem, a user must authenticate to the open metadata ecosystem.  This requires a user account which includes a unique name (the user identifier) and a secret that only the user knows.  Once the user has proved their identity by supplying the secret for their user account on request, the user identifier is associated with every update they user makes.",
            "userId",
            "https://egeria-project.org/concepts/user-identity/",
            ProductFolderDefinition.GLOSSARY_DATA_ITEMS),
    ;

    private final String                  displayName;
    private final String                  summary;
    private final String                  description;
    private final String                  abbreviation;
    private final String                  url;
    private final ProductFolderDefinition folder;


    /**
     * The constructor creates an instance of the enum
     *
     * @param displayName  unique id for the enum
     * @param summary      name for the enum
     * @param description  description of the use of this value
     * @param abbreviation optional abbreviation
     * @param url          optional url for the term
     * @param folder       optional folder for the term
     */
    ProductGlossaryTermDefinition(String displayName,
                                  String summary,
                                  String description,
                                  String abbreviation,
                                  String url,
                                  ProductFolderDefinition folder)
    {
        this.displayName  = displayName;
        this.summary      = summary;
        this.description  = description;
        this.abbreviation = abbreviation;
        this.url          = url;
        this.folder       = folder;
    }

    /**
     * Return the qualified name to use for the glossary term.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return OpenMetadataType.GLOSSARY_TERM.typeName + "::Jacquard::" + displayName;
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
    public String getSummary()
    {
        return summary;
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
     * Return the abbreviation for this term.
     *
     * @return string
     */
    public String getAbbreviation()
    {
        return abbreviation;
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
        return folder;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductGlossaryTermDefinition{" + summary + '}';
    }
}
