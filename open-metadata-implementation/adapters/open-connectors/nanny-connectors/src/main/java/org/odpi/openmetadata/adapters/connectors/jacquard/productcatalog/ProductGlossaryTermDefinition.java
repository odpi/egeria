/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductGlossaryTermDefinition is used to populate the sustainability glossary.
 */
public enum ProductGlossaryTermDefinition
{
    /**
     * A means of operating that makes effective use of replaceable resources.
     */
    DIGITAL_PRODUCT("Digital Product",
                    "A digital product describes a collection of digital resources that are produced for a specific type of consumer or purpose. They conform to a specification and offer guarantees of service. They may also have terms and conditions associated with their use.",
                    "There are different ways to implement a digital product. For example:\n" +
                            "\n" +
                            "*    A data product is an example of a digital product that delivers a collection of data. Each consumer of the product gets the same data.\n" +
                            "*    An insight service is an example of a digital product that provides information based on query values from the consumer. Different consumers may receive different results.\n" +
                            "*    An embedded feature, such as a smartphone app, is an example of a digital product that is running in the consumer's environment/hardware.\n" +
                            "\n" +
                            "Digital products are designed to be shared and may be advertised in a marketplace. They are typically owned and consumed by teams responsible for business capabilities. There is typical a person, or team responsible for development of a digital product.\n",
                    "",
                    "https://egeria-project.org/types/7/0710-Digital-Products/",
                    ProductFolderDefinition.GLOSSARY_BASICS),

    EVALUATION_SUBSCRIPTION("Evaluation Subscription",
                            "A subscription to a digital product that delivers the data to the target destination just once.",
                            "A subscription to a digital product that delivers the data to the target destination just once.  It is used when evaluating a product's data, and also when data delivery needs to be controlled by the subscriber - for example to ensure data values remain constant through a particular processing task (such as training an AI model).",
                            "",
                            "",
                            ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    ONGOING_UPDATE_SUBSCRIPTION("Ongoing Update Subscription",
                                "A subscription to a digital product that delivers the data to the target destination within an hour of it changing.",
                                "A subscription to a digital product that delivers the data to the target destination within an hour of it changing.  This type of subscription ensures the latest information is always available to the consumer.",
                                "",
                                "",
                                ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    DAILY_REFRESH_SUBSCRIPTION("Evaluation Subscription",
                               "A subscription to a digital product that delivers the data to the target destination once a day.",
                               "A subscription to a digital product that delivers the data to the target destination once a day.",
                               "",
                               "",
                               ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    WEEKLY_REFRESH_SUBSCRIPTION("Evaluation Subscription",
                                "A subscription to a digital product that delivers the data to the target destination once a week.",
                                "A subscription to a digital product that delivers the data to the target destination once a week.",
                                "",
                                "",
                                ProductFolderDefinition.GLOSSARY_SUBSCRIPTIONS),

    GUID("Unique Identifier",
         "A unique identifier for a product or service.",
         "A unique identifier for a product or service.  It is used to ensure that each product or service is uniquely identifiable within the system.",
         "",
         "",
         ProductFolderDefinition.GLOSSARY_DATA_ITEMS),

    OPEN_METADATA_TYPE_NAME("Open Metadata Type Name",
                            "The name of the open metadata type for a product or service.",
                            "The name of the open metadata type for a product or service.  It is used to identify the type of product or service within the system.",
                            "",
                            "",
                            ProductFolderDefinition.GLOSSARY_DATA_ITEMS)
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

    public String getQualifiedName()
    {
        return OpenMetadataType.GLOSSARY_TERM.typeName + "::Jacquard::" + displayName;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public String getSummary()
    {
        return summary;
    }


    public String getDescription()
    {
        return description;
    }


    public String getAbbreviation()
    {
        return abbreviation;
    }


    public String getUrl()
    {
        return url;
    }


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
