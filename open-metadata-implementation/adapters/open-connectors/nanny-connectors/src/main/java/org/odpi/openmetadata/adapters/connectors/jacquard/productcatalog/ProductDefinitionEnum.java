/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;

import org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint.ProductRoleDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataTypesDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetListProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueSetListProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The ProductDefinition describes the fixed products and product groups found in the open metadata product catalog.
 */
public enum ProductDefinitionEnum implements ProductDefinition
{
    /*
     * =============================================================================================
     */

    /**
     * Valid Metadata Value Sets - the product definition for each valid metadata value set is dynamically created
     * when it is retrieved for the first time.
     */
    VALID_METADATA_VALUE_SETS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                              null,
                              "Valid Metadata Value Sets",
                              "OPEN-METADATA-VALID-VALUES-FAMILY",
                              ProductFolderDefinition.PRODUCTS,
                              "Valid Metadata Value Sets",
                              "Each product in this folder is an extract of the valid metadata values.  The valid metadata values are organized into a tabular data set, where each row is a specific valid value.  These products can be used as standard reference values when building other digital products to help consumers join data from multiple products together.",
                              ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                              ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                              ProductCommunityDefinition.REFERENCE_DATA_SIG,
                              new ProductSubscriptionDefinition[]{
                             ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                             ProductSubscriptionDefinition.ONGOING_UPDATE},
                              null,
                              null,
                              null,
                              OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                              "Data sets",
                              null,
                              null),

    /**
     * Valid Metadata Value Set List - Jacquard dynamically generates a digital product for each valid metadata value set.
     */
    VALID_METADATA_VALUE_SET_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                  new ProductDefinition[]{ProductDefinitionEnum.VALID_METADATA_VALUE_SETS},
                                  "Valid Metadata Value Set List",
                                  "OPEN-METADATA-" + OpenMetadataType.VALID_METADATA_VALUE.typeName + "-with-members",
                                  null,
                                  "Valid Metadata Value Set List",
                                  "A tabular data set where each record describes an open metadata property that has a valid metadata value set defined.  There is a digital product for each open metadata property in this list.",
                                  ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                                  ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                  ProductCommunityDefinition.REFERENCE_DATA_SIG,
                                  new ProductSubscriptionDefinition[]{
                                          ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                          ProductSubscriptionDefinition.ONGOING_UPDATE},
                                  "Valid Metadata Value Set List",
                                  new ProductDataFieldDefinition[]{
                                          ProductDataFieldDefinition.PROPERTY_NAME},
                                  new ProductDataFieldDefinition[]{
                                          ProductDataFieldDefinition.DESCRIPTION,
                                          ProductDataFieldDefinition.CREATE_TIME,
                                          ProductDataFieldDefinition.UPDATE_TIME,
                                          ProductDataFieldDefinition.DATA_TYPE},
                                  OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                  "Open Metadata Property List",
                                  new ValidMetadataValueSetListProvider(),
                                  "ValidMetadataValueSetList"),

    /*
     * =============================================================================================
     */

    /**
     * Reference Data Sets - the product definition for each reference data set is dynamically created
     * when it is retrieved for the first time.
     */
    REFERENCE_DATA_SETS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                              null,
                              "Reference Data Sets",
                              "OPEN-METADATA-VALID-VALUES-FAMILY",
                              ProductFolderDefinition.PRODUCTS,
                              "Reference Data Sets",
                              "Each product in this folder is an extract of the reference data values managed by open metadata.  The reference data values are organized into a tabular data set, where each row is a specific valid value.  These products can be used as standard reference values when building other digital products to help consumers join data from multiple products together.",
                              ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                              ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                              ProductCommunityDefinition.REFERENCE_DATA_SIG,
                              new ProductSubscriptionDefinition[]{
                                      ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.ONGOING_UPDATE},
                              null,
                              null,
                              null,
                              OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                              "Data sets",
                              null,
                              null),

    /**
     * Reference Data Set List - Jacquard dynamically generates a digital product for each reference data set in this list.
     */
    REFERENCE_DATA_SET_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                  new ProductDefinition[]{ProductDefinitionEnum.REFERENCE_DATA_SETS},
                                  "Reference Data Set List",
                                  "OPEN-METADATA-" + OpenMetadataType.REFERENCE_DATA_SET.typeName + "-with-members",
                                  null,
                                  "Reference Data Set List",
                                  "A tabular data set where each record describes a reference data set stored in open metadata.  There is a digital product for each reference data set in this list.",
                                  ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                                  ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                  ProductCommunityDefinition.REFERENCE_DATA_SIG,
                                  new ProductSubscriptionDefinition[]{
                                          ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                          ProductSubscriptionDefinition.ONGOING_UPDATE},
                                  "Reference Data Set List",
                                  new ProductDataFieldDefinition[]{
                                          ProductDataFieldDefinition.GUID},
                                  new ProductDataFieldDefinition[]{
                                          ProductDataFieldDefinition.IDENTIFIER,
                                          ProductDataFieldDefinition.DESCRIPTION,
                                          ProductDataFieldDefinition.CREATE_TIME,
                                          ProductDataFieldDefinition.UPDATE_TIME,
                                          ProductDataFieldDefinition.DATA_TYPE},
                                  OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                  "Data set",
                                  new ReferenceDataSetListProvider(),
                                  "ReferenceDataSetList"),

    /*
     * =============================================================================================
     */

    /**
     * Open Metadata Types - the type definitions returned by OMF have been enhanced beyond OMRS to
     * directly support this product family.
     */
    OPEN_METADATA_TYPES(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                        null,
                        "Open Metadata Types",
                        "OPEN-METADATA-TYPES-FAMILY",
                        ProductFolderDefinition.PRODUCTS,
                        "Open Metadata Types",
                        "Each product in this folder provides a perspective on the open metadata types.",
                        ProductCategoryDefinition.OPEN_METADATA_TYPES.getPreferredValue(),
                        ProductGovernanceDefinition.CC_BY_40,
                        ProductCommunityDefinition.REFERENCE_DATA_SIG,
                        new ProductSubscriptionDefinition[]{
                                ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                ProductSubscriptionDefinition.ONGOING_UPDATE},
                        null,
                        null,
                        null,
                        OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                        "Data sets",
                        null,
                        null),

    /**
     * Open Metadata Data Types List
     */
    DATA_TYPES_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                    new ProductDefinition[]{ProductDefinitionEnum.OPEN_METADATA_TYPES},
                    "Open Metadata Data Types List",
                    "OPEN_METADATA-DATA-TYPES",
                    null,
                    "Open Metadata Data Types List",
                    "A tabular data set where each record describes a type of property defined in the open metadata types.  These types map easily to standard programming languages and are also useful when cataloguing various technologies.",
                    ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.REFERENCE_DATA_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata Data Types List",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.DATA_TYPE},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.GUID,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.CATEGORY},
                    OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                    "Data set",
                    null,
                    "Open Metadata Data Types"),

    /**
     * Attribute List
     */
    ATTRIBUTES_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                    new ProductDefinition[]{ProductDefinitionEnum.OPEN_METADATA_TYPES},
                    "Open Metadata Attributes List",
                    "OPEN-METADATA-ATTRIBUTES-LIST",
                    null,
                    "Open Metadata Attributes List",
                    "A tabular data set where each record describes a type of attribute defined in the open metadata types.",
                    ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.REFERENCE_DATA_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata Attributes",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                            ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_NAME},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.CATEGORY,
                            ProductDataFieldDefinition.DATA_TYPE},
                    OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                    "Data set",
                    null,
                    "Open Metadata Attributes"),

    /**
     * Open Metadata Types List
     */
    TYPES_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
               new ProductDefinition[]{ProductDefinitionEnum.OPEN_METADATA_TYPES},
               "Open Metadata Types List",
               "OPEN-METADATA-TYPES-LIST",
               null,
               "Open Metadata Types List",
               "A tabular data set where each record describes an open metadata type.",
               ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
               ProductGovernanceDefinition.CC_BY_40,
               ProductCommunityDefinition.REFERENCE_DATA_SIG,
               new ProductSubscriptionDefinition[]{
                       ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                       ProductSubscriptionDefinition.ONGOING_UPDATE},
               "Open Metadata Types",
               new ProductDataFieldDefinition[]{
                       ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME},
               new ProductDataFieldDefinition[]{
                       ProductDataFieldDefinition.GUID,
                       ProductDataFieldDefinition.DESCRIPTION,
                       ProductDataFieldDefinition.CATEGORY,
                       ProductDataFieldDefinition.URL,
                       ProductDataFieldDefinition.BEAN_CLASS_NAME,
                       ProductDataFieldDefinition.OPEN_METADATA_SUBTYPES,
                       ProductDataFieldDefinition.OPEN_METADATA_SUPER_TYPES,
                       ProductDataFieldDefinition.CREATE_TIME,
                       ProductDataFieldDefinition.UPDATE_TIME,
                       ProductDataFieldDefinition.OPEN_METADATA_TYPE_STATUS},
               OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
               "Data set",
               new OpenMetadataTypesDataSetProvider(),
               "Open Metadata Types"),

    /**
     * Open Metadata Types List
     */
    ATTRIBUTES_FOR_TYPES_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                              new ProductDefinition[]{ProductDefinitionEnum.OPEN_METADATA_TYPES},
                              "Open Metadata Attributes For Types List",
                              "ALL-ATTRIBUTES-FOR-OPEN-METADATA-TYPES",
                              null,
                              "Open Metadata Attributes For Types List",
                              "A tabular data set where each record describes an attribute for an open metadata type. There is one row for each defined attribute for each type. This includes attributes inherited from its super type(s).",
                              ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                              ProductGovernanceDefinition.CC_BY_40,
                              ProductCommunityDefinition.REFERENCE_DATA_SIG,
                              new ProductSubscriptionDefinition[]{
                                      ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.ONGOING_UPDATE},
                              "Open Metadata Attributes For Types",
                              new ProductDataFieldDefinition[]{
                                      ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                                      ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_NAME},
                              new ProductDataFieldDefinition[]{
                                      ProductDataFieldDefinition.IS_NULLABLE,
                                      ProductDataFieldDefinition.DATA_TYPE,
                                      ProductDataFieldDefinition.DESCRIPTION,
                                      ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_STATUS},
                              OpenMetadataType.TABULAR_DATA_SET.typeName,
                              "Data set",
                              null,
                              "Open Metadata Attributes for Types"),

    /*
     * =============================================================================================
     */

    /**
     * Actor, Places, and Products
     */
    ACTOR_PLACES_PRODUCTS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                          null,
                          "Actor, Places and Products",
                          "MASTER-DATA-FAMILY",
                          ProductFolderDefinition.PRODUCTS,
                          "Actor, Places and Product Master Data",
                          "Each product in this folder lists information held in the open metadata about people, organizations, users, teams, locations and digital products.  This type of data is called master data because it describes the key entities that the organization operates around.  As such, some form of this data appears in most data sets.  Each product in this folder is organized into a tabular data set, where each row is a specific (master data) entity.   These are designed to be used as standard values that can be used for validation or to ensure that data in digital products is consistent making it easier to join data from multiple products.",
                          ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                          null,
                          ProductCommunityDefinition.MASTER_DATA_SIG,
                          new ProductSubscriptionDefinition[]{
                                  ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                  ProductSubscriptionDefinition.ONGOING_UPDATE},
                          null,
                          null,
                          null,
                          OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                          "Data sets",
                          null,
                          null),

    /**
     * Organizations List
     */
    ORGANIZATIONS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                  new ProductDefinition[]{ProductDefinitionEnum.ACTOR_PLACES_PRODUCTS},
                  "Organization List",
                  "Organizations",
                  null,
                  "Organization List",
                  "A tabular data set where each record describes an organization interacting with open metadata.",
                  ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                  ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                  ProductCommunityDefinition.MASTER_DATA_SIG,
                  new ProductSubscriptionDefinition[]{
                          ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                          ProductSubscriptionDefinition.ONGOING_UPDATE},
                  "Organizations",
                  new ProductDataFieldDefinition[]{
                          ProductDataFieldDefinition.GUID},
                  new ProductDataFieldDefinition[]{
                          ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                          ProductDataFieldDefinition.IDENTIFIER,
                          ProductDataFieldDefinition.DISPLAY_NAME,
                          ProductDataFieldDefinition.CATEGORY,
                          ProductDataFieldDefinition.DESCRIPTION},
                  OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                  "Data set",
                  null,
                  "Organizations"),

    /**
     * List of People
     */
    PEOPLE(OpenMetadataType.DIGITAL_PRODUCT.typeName,
           new ProductDefinition[]{ProductDefinitionEnum.ACTOR_PLACES_PRODUCTS},
           "List of People",
           "People List",
           null,
           "People List",
           "A tabular data set where each record describes a person interacting with open metadata.",
           ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
           ProductGovernanceDefinition.PERSONAL_DATA,
           ProductCommunityDefinition.MASTER_DATA_SIG,
           new ProductSubscriptionDefinition[]{
                   ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                   ProductSubscriptionDefinition.ONGOING_UPDATE},
           "People",
           new ProductDataFieldDefinition[]{
                   ProductDataFieldDefinition.GUID},
           new ProductDataFieldDefinition[]{
                   ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                   ProductDataFieldDefinition.IDENTIFIER,
                   ProductDataFieldDefinition.DISPLAY_NAME,
                   ProductDataFieldDefinition.DESCRIPTION},
           OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
           "Data set",
           null,
           "People"),

    /**
     * List of Digital Products
     */
    DIGITAL_PRODUCTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                     new ProductDefinition[]{ProductDefinitionEnum.ACTOR_PLACES_PRODUCTS},
                     "Digital Product Inventory",
                     "DIGITAL-PRODUCTS-INVENTORY",
                     null,
                     "Digital Product Inventory",
                     "A tabular data set where each record describes a digital product.",
                     ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                     ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                     ProductCommunityDefinition.MASTER_DATA_SIG,
                     new ProductSubscriptionDefinition[]{
                             ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                             ProductSubscriptionDefinition.ONGOING_UPDATE},
                     "Digital Products",
                     new ProductDataFieldDefinition[]{
                             ProductDataFieldDefinition.GUID},
                     new ProductDataFieldDefinition[]{
                             ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                             ProductDataFieldDefinition.IDENTIFIER,
                             ProductDataFieldDefinition.DISPLAY_NAME,
                             ProductDataFieldDefinition.DESCRIPTION,
                             ProductDataFieldDefinition.ELEMENT_STATUS},
                     OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                     "Data set",
                     null,
                     "DigitalProductsInventory"),

    /*
     * =============================================================================================
     */

    /**
     * List of Locations
     */
    LOCATIONS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
              new ProductDefinition[]{ProductDefinitionEnum.ACTOR_PLACES_PRODUCTS},
              "Location List",
              "LOCATIONS-LIST",
              null,
              "List of Locations",
              "A tabular data set where each record describes a location.  This could be a site, or a facility within a site.",
              ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
              ProductGovernanceDefinition.INTERNAL_USE_ONLY,
              ProductCommunityDefinition.MASTER_DATA_SIG,
              new ProductSubscriptionDefinition[]{
                      ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                      ProductSubscriptionDefinition.ONGOING_UPDATE},
              "Locations",
              new ProductDataFieldDefinition[]{
                      ProductDataFieldDefinition.LOCATION_GUID},
              new ProductDataFieldDefinition[]{
                      ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                      ProductDataFieldDefinition.LOCATION_ROLE,
                      ProductDataFieldDefinition.CATEGORY,
                      ProductDataFieldDefinition.IDENTIFIER,
                      ProductDataFieldDefinition.DISPLAY_NAME,
                      ProductDataFieldDefinition.DESCRIPTION,
                      ProductDataFieldDefinition.LOCATION_COORDINATES,
                      ProductDataFieldDefinition.LOCATION_COORDINATES,
                      ProductDataFieldDefinition.LOCATION_MAP_PROJECTION,
                      ProductDataFieldDefinition.LOCATION_POSTAL_ADDRESS,
                      ProductDataFieldDefinition.NETWORK_ADDRESS},
              OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
              "Data set",
              null,
              "Locations"),

    /*
     * =============================================================================================
     */

    /**
     * Organization Observability
     */
    ORGANIZATION_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                               null,
                               "Organization Observability",
                               "ORGANIZATION-OBSERVABILITY",
                               ProductFolderDefinition.PRODUCTS,
                               "Organization Observability",
                               "Each product in this folder publishes insights about the activity of the organization observed through the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                               ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                               ProductGovernanceDefinition.PERSONAL_DATA,
                               ProductCommunityDefinition.OBSERVABILITY_SIG,
                               new ProductSubscriptionDefinition[]{ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION},
                               null,
                               null,
                               null,
                               OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                               "Data sets",
                               null,
                               null),

    /*
     * =============================================================================================
     */

    /**
     * Governance Observability
     */
    GOVERNANCE_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                             null,
                             "Governance Observability",
                             "GOVERNANCE-OBSERVABILITY-FAMILY",
                             ProductFolderDefinition.PRODUCTS,
                             "Governance Observability",
                             "Each product in this folder publishes insights about the governance activity observed through the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                             ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                             ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                             ProductCommunityDefinition.OBSERVABILITY_SIG,
                             new ProductSubscriptionDefinition[]{ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION},
                             null,
                             null,
                             null,
                             OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                             "Data sets",
                             null,
                             null),

    /*
     * =============================================================================================
     */

    /**
     * IT Operations Observability
     */
    IT_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                     null,
                     "IT Operations Observability",
                     "IT-OBSERVABILITY-FAMILY",
                     ProductFolderDefinition.PRODUCTS,
                     "IT Operations Observability",
                     "Each product in this group publishes insights about the operational health of the IT infrastructure supporting the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                     ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                     ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                     ProductCommunityDefinition.OBSERVABILITY_SIG,
                     new ProductSubscriptionDefinition[]{ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION},
                     null,
                     null,
                     null,
                     OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                     "Data sets",
                     null,
                     null),

    ;


    private final String                          typeName;
    private final ProductDefinition[]             productFamilies;
    private final String                          productName;
    private final String                          identifier;
    private final ProductFolderDefinition         parent;
    private final String                          displayName;
    private final String                          description;
    private final String                          category;
    private final ProductGovernanceDefinition     license;
    private final ProductCommunityDefinition      community;
    private final ProductRoleDefinition           productManager;
    private final ProductRoleDefinition           productDeveloper;
    private final ProductRoleDefinition           productSupport;
    private final ProductSubscriptionDefinition[] subscriptionTypes;
    private final String                          dataSpecTableName;
    private final ProductDataFieldDefinition[]    dataSpecIdentifiers;
    private final ProductDataFieldDefinition[]    dataSpecFields;
    private final String                          assetTypeName;
    private final String                          assetIdentifier;
    private final ConnectorProvider               connectorProvider;
    private final String                          catalogTargetName;



    ProductDefinitionEnum(String                          typeName,
                          ProductDefinition[]             productFamilies,
                          String                          productName,
                          String                          identifier,
                          ProductFolderDefinition         parent,
                          String                          displayName,
                          String                          description,
                          String                          category,
                          ProductGovernanceDefinition     license,
                          ProductCommunityDefinition      community,
                          ProductSubscriptionDefinition[] subscriptionTypes,
                          String                          dataSpecTableName,
                          ProductDataFieldDefinition[]    dataSpecIdentifiers,
                          ProductDataFieldDefinition[]    dataSpecFields,
                          String                          assetTypeName,
                          String                          assetIdentifier,
                          ConnectorProvider               connectorProvider,
                          String                          catalogTargetName)
    {
        this.typeName        = typeName;
        this.productFamilies = productFamilies;
        this.productName     = productName;
        this.identifier          = identifier;
        this.parent              = parent;
        this.displayName         = displayName;
        this.description         = description;
        this.category            = category;
        this.license             = license;
        this.community           = community;
        this.productManager      = ProductRoleDefinition.PRODUCT_MANAGER;
        this.productDeveloper    = ProductRoleDefinition.PRODUCT_DEVELOPER;
        this.productSupport      = ProductRoleDefinition.JACQUARD_SUPPORT;
        this.subscriptionTypes   = subscriptionTypes;
        this.dataSpecTableName   = dataSpecTableName;
        this.dataSpecIdentifiers = dataSpecIdentifiers;
        this.dataSpecFields      = dataSpecFields;
        this.assetTypeName       = assetTypeName;
        this.assetIdentifier     = assetIdentifier;
        this.connectorProvider   = connectorProvider;
        this.catalogTargetName   = catalogTargetName;
    }



    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    @Override
    public String getQualifiedName()
    {
        return typeName + "::OpenMetadataDigitalProduct::" + identifier + "::" + displayName;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    @Override
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the list of product groups (if any) that this product belongs to.
     *
     * @return list
     */
    @Override
    public List<ProductDefinition> getProductFamilies()
    {
        if (productFamilies != null)
        {
            return new ArrayList<>(Arrays.asList(productFamilies));
        }

        return null;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    @Override
    public String getProductName()
    {
        return productName;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the entry of the parent folder - null for top level.
     *
     * @return enum
     */
    @Override
    public ProductFolderDefinition getFolder()
    {
        return parent;
    }


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns an optional category for the collection.
     *
     * @return string
     */
    @Override
    public String getCategory()
    {
        return category;
    }


    /**
     * Return the licence that will be granted to data provided through a subscription mechanism.
     *
     * @return license definition
     */
    @Override
    public ProductGovernanceDefinition getLicense()
    {
        return license;
    }


    /**
     * Return details of the community that provides the forum to discuss this product.
     *
     * @return community definition
     */
    @Override
    public ProductCommunityDefinition getCommunity()
    {
        return community;
    }


    /**
     * Return the project manager for the product.
     *
     * @return description of the product manager
     */
    @Override
    public ProductRoleDefinition getProductManager()
    {
        return productManager;
    }


    /**
     * Return the project developer for the product.
     *
     * @return description of the product developer
     */
    @Override
    public ProductRoleDefinition getProductDeveloper()
    {
        return productDeveloper;
    }


    /**
     * Return the project support for the product.
     *
     * @return description of the product support
     */
    @Override
    public ProductRoleDefinition getProductSupport()
    {
        return productSupport;
    }


    /**
     * Return the list of subscription types supported by this product.
     *
     * @return list
     */
    @Override
    public List<ProductSubscriptionDefinition> getSubscriptionTypes()
    {
        if (subscriptionTypes != null)
        {
            return new ArrayList<>(Arrays.asList(subscriptionTypes));
        }
        return null;
    }


    /**
     * Return the name pattern for the table name to use for the tabular data set.
     *
     * @return Capitalized, space separated name
     */
    @Override
    public String getDataSpecTableName()
    {
        return dataSpecTableName;
    }


    /**
     * Return the list of identifying data fields to add to the data spec.
     *
     * @return list
     */
    @Override
    public List<ProductDataFieldDefinition> getDataSpecIdentifiers()
    {
        if (dataSpecIdentifiers != null)
        {
            return new ArrayList<>(Arrays.asList(dataSpecIdentifiers));
        }

        return null;
    }


    /**
     * Return the list of non-identifying data fields to add to the data spec.
     *
     * @return list
     */
    @Override
    public List<ProductDataFieldDefinition> getDataSpecFields()
    {
        if (dataSpecFields != null)
        {
            return new ArrayList<>(Arrays.asList(dataSpecFields));
        }

        return null;
    }


    /**
     * Return the type name to use for the product's asset.
     *
     * @return string
     */
    @Override
    public String getAssetTypeName()
    {
        return assetTypeName;
    }


    /**
     * Return the identifier to use for the asset.
     *
     * @return string
     */
    @Override
    public String getAssetIdentifier()
    {
        return assetIdentifier;
    }


    /**
     * Return the connector provider class that supports this product.
     *
     * @return string
     */
    @Override
    public ConnectorProvider getConnectorProvider()
    {
        return connectorProvider;
    }


    /**
     * Return the catalog target name to use when registering the product asset with the harvester.
     *
     * @return string
     */
    @Override
    public String getCatalogTargetName()
    {
        return catalogTargetName;
    }


    /**
     * Return the configuration properties - may be null.
     *
     * @return map
     */
    @Override
    public Map<String, Object> getConfigurationProperties()
    {
        return null;
    }


    /**
     * Return the version identifier.
     *
     * @return string
     */
    @Override
    public String getVersionIdentifier()
    {
        return "6.0-SNAPSHOT";
    }


    /**
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "ProductDefinition{" +
                "productName='" + productName + '\'' +
                ", identifier='" + identifier + '\'' +
                "} " + super.toString();
    }
}
