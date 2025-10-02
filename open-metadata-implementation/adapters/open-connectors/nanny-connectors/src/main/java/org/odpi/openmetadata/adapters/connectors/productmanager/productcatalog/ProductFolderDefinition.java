/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductFolderDefinition describes the different folders in the Open Metadata Product Catalog.
 * This includes the top-level folder for the whole catalog.
 */
public enum ProductFolderDefinition
{
    /**
     * Open Metadata Digital Product Catalog
     */
    TOP_LEVEL(OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName,
              OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName,
              null,
              "Open Metadata Digital Product Catalog",
              "Extracts of open metadata organized into useful data sets.  These digital products support a variety of subscription choices.  Data can be delivered either as a CSV file, or as a PostGreSQL table.  Updates to the subscriber's copy typically occur within 1 hour of receiving the metadata update.",
              null),

    /**
     * Open Metadata Digital Product Glossary
     */
    GLOSSARY(OpenMetadataType.GLOSSARY.typeName,
             OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName,
             TOP_LEVEL,
             "Open Metadata Digital Product Glossary",
             "Terminology used in the open metadata digital product catalog.",
             null),

    /**
     * Basic terminology relating to digital products.
     */
    GLOSSARY_BASICS(OpenMetadataType.COLLECTION.typeName,
                    OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                    GLOSSARY,
                    "Digital Product Basics",
                    "Basic terminology relating to digital products in general, and the open metadata digital products in particular.",
                    null),

    /**
     * Terminology relating to digital subscriptions.
     */
    GLOSSARY_SUBSCRIPTIONS(OpenMetadataType.COLLECTION.typeName,
                           OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                           GLOSSARY,
                           "Digital Subscriptions",
                           "Terminology relating to digital subscriptions.",
                           null),

    /**
     * Terminology relating to data items found in the digital products.
     */
    GLOSSARY_DATA_ITEMS(OpenMetadataType.COLLECTION.typeName,
                        OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                        GLOSSARY,
                        "Data Item Semantics",
                        "Descriptions of the data found in data items of the digital products.",
                        null),

    /**
     * Open Metadata Digital Product Data Dictionary
     */
    DATA_DICTIONARY(OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName,
                    OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName,
                    TOP_LEVEL,
                    "Open Metadata Digital Product Data Dictionary",
                    "Details of the types of data fields used in the open metadata digital products.",
                    null),

    /**
     * Open Metadata Digital Product Catalog
     */
    PRODUCTS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
             OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
             TOP_LEVEL,
             "Open Metadata Digital Products",
             "Extracts of open metadata organized into useful data sets.  These digital products support a variety of subscription choices.  Data can be delivered either as a CSV file, or as a PostGreSQL table.  Updates to the subscriber's copy typically occur within 1 hour of receiving the metadata update.",
             null),

    /**
     * Valid Value Sets
     */
    VALID_VALUE_SETS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                     OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                     PRODUCTS,
                     "Valid Value Sets",
                     "Each product in this folder is an extract of the valid values associated with a valid value set.  The valid values are organized into a tabular data set, where each row is a specific valid value.  These products can be used as standard reference values when building other digital products to help consumers join data from multiple products together.",
                     "Reference Data"),

    /**
     * Party, Places and Products
     */
    PARTY_PLACES_PRODUCTS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                          OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                          PRODUCTS,
                          "Party, Places and Products",
                          "Each product in this folder consolidates information held in the open metadata about people, organizations, users, teams, locations and digital products.  This type of data is called master data because it describes the key entities that the organization operates around.  As such, some form of this data appears in most data sets.  Each product in this folder is organized into a tabular data set, where each row is a specific (master data) entity.   These are designed to be used as standard values that can be used for validation or to ensure that data in digital products is consistent making it easier to join data from multiple products.",
                          "Master Data"),


    /**
     * Open Metadata Types
     */
    OPEN_METADATA_TYPES(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                        OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                        PRODUCTS,
                        "Open Metadata Types",
                        "Each product in this folder provides a perspective on the open metadata types.",
                        "Metadata"),



    /**
     * Organization Observability
     */
    ORGANIZATION_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                               OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                               PRODUCTS,
                               "Organization Observability",
                               "Each product in this folder publishes insights about the activity of the organization observed through the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                               "Insight Notification"),


    /**
     * Governance Observability
     */
    GOVERNANCE_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                             OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                             PRODUCTS,
                             "Governance Observability",
                             "Each product in this folder publishes insights about the governance activity observed through the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                             "Insight Notification"),


    /**
     * IT Operations Observability
     */
    IT_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                     OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                     PRODUCTS,
                     "IT Operations Observability",
                     "Each product in this folder publishes insights about the operational health of the IT infrastructure supporting the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                     "Insight Notification"),


    ;


    private final String                  typeName;
    private final String                  classificationName;
    private final ProductFolderDefinition parent;
    private final String                  displayName;
    private final String                  description;
    private final String                  category;


    /**
     * Constructor for enum value.
     *
     * @param typeName type of folder
     * @param classificationName optional classification
     * @param parent optional parent folder
     * @param displayName display name
     * @param description description
     * @param category category
     */
    ProductFolderDefinition(String                  typeName,
                            String                  classificationName,
                            ProductFolderDefinition parent,
                            String                  displayName,
                            String                  description,
                            String                  category)
    {
        this.typeName           = typeName;
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
        return "OpenMetadataProductCatalog::" + classificationName + "::" + displayName;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Return the entry of the parent folder - null for top level.
     *
     * @return enum
     */
    public ProductFolderDefinition getParent()
    {
        return parent;
    }


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns an optional category for the collection.
     *
     * @return string
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
                "typeName='" + typeName + '\'' +
                ", classificationName='" + classificationName + '\'' +
                ", parent=" + parent +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                "} " + super.toString();
    }
}
