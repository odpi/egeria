/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;

import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataTypesDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetListProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueSetListProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceZoneName;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The ProductDefinition describes the fixed products and product groups found in the Open Metadata Digital Product Catalog.
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
                                      ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
                                          ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                          ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
                        "REFERENCE-DATA-SETS-FAMILY",
                        ProductFolderDefinition.PRODUCTS,
                        "Reference Data Sets",
                        "Each product in this folder is an extract of the reference data values managed by open metadata.  The reference data values are organized into a tabular data set, where each row is a specific valid value.  These products can be used as standard reference values when building other digital products to help consumers join data from multiple products together.",
                        ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                        ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                        ProductCommunityDefinition.REFERENCE_DATA_SIG,
                        new ProductSubscriptionDefinition[]{
                                ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
                                    ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                    ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
                                ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
                    "OPEN-METADATA-DATA-TYPES",
                    null,
                    "Open Metadata Data Types List",
                    "A tabular data set where each record describes a type of property defined in the open metadata types.  These types map easily to standard programming languages and are also useful when cataloguing various technologies.",
                    ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.REFERENCE_DATA_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata Data Types List",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.DATA_TYPE},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.GUID,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.CATEGORY,
                            ProductDataFieldDefinition.CREATE_TIME,
                            ProductDataFieldDefinition.UPDATE_TIME},
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
                            ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata Attributes",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                            ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_NAME},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.CATEGORY,
                            ProductDataFieldDefinition.DATA_TYPE,
                            ProductDataFieldDefinition.CREATE_TIME,
                            ProductDataFieldDefinition.UPDATE_TIME},
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
                       ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                       ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
     * Open Metadata Attributes For Types List
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
                                      ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.ONGOING_UPDATE},
                              "Open Metadata Attributes For Types",
                              new ProductDataFieldDefinition[]{
                                      ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                                      ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_NAME},
                              new ProductDataFieldDefinition[]{
                                      ProductDataFieldDefinition.IS_NULLABLE,
                                      ProductDataFieldDefinition.DATA_TYPE,
                                      ProductDataFieldDefinition.DESCRIPTION,
                                      ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_STATUS,
                                      ProductDataFieldDefinition.CREATE_TIME,
                                      ProductDataFieldDefinition.UPDATE_TIME},
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
                                  ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                  ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
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
                          ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                          ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                          ProductSubscriptionDefinition.ONGOING_UPDATE},
                  "Organizations",
                  new ProductDataFieldDefinition[]{
                          ProductDataFieldDefinition.GUID},
                  new ProductDataFieldDefinition[]{
                          ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                          ProductDataFieldDefinition.IDENTIFIER,
                          ProductDataFieldDefinition.DISPLAY_NAME,
                          ProductDataFieldDefinition.CATEGORY,
                          ProductDataFieldDefinition.DESCRIPTION,
                          ProductDataFieldDefinition.CREATE_TIME,
                          ProductDataFieldDefinition.UPDATE_TIME},
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
                   ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                   ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                   ProductSubscriptionDefinition.ONGOING_UPDATE},
           "People",
           new ProductDataFieldDefinition[]{
                   ProductDataFieldDefinition.GUID},
           new ProductDataFieldDefinition[]{
                   ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                   ProductDataFieldDefinition.IDENTIFIER,
                   ProductDataFieldDefinition.DISPLAY_NAME,
                   ProductDataFieldDefinition.DESCRIPTION,
                   ProductDataFieldDefinition.CREATE_TIME,
                   ProductDataFieldDefinition.UPDATE_TIME},
           OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
           "Data set",
           null,
           "People"),

    /**
     * List of Digital Products
     */
    DIGITAL_PRODUCTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                     new ProductDefinition[]{ProductDefinitionEnum.ACTOR_PLACES_PRODUCTS},
                     "Digital Product List",
                     "DIGITAL-PRODUCTS-LIST",
                     null,
                     "Digital Product List",
                     "A tabular data set where each record describes a digital product.",
                     ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                     ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                     ProductCommunityDefinition.MASTER_DATA_SIG,
                     new ProductSubscriptionDefinition[]{
                             ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                             ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                             ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                             ProductSubscriptionDefinition.ONGOING_UPDATE},
                     "Digital Products",
                     new ProductDataFieldDefinition[]{
                             ProductDataFieldDefinition.GUID},
                     new ProductDataFieldDefinition[]{
                             ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                             ProductDataFieldDefinition.IDENTIFIER,
                             ProductDataFieldDefinition.DISPLAY_NAME,
                             ProductDataFieldDefinition.DESCRIPTION,
                             ProductDataFieldDefinition.ELEMENT_STATUS,
                             ProductDataFieldDefinition.CREATE_TIME,
                             ProductDataFieldDefinition.UPDATE_TIME},
                     OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                     "Data set",
                     null,
                     "Digital Product List"),

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
                      ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                      ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                      ProductSubscriptionDefinition.ONGOING_UPDATE},
              "Locations",
              new ProductDataFieldDefinition[]{
                      ProductDataFieldDefinition.LOCATION_GUID},
              new ProductDataFieldDefinition[]{
                      ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                      ProductDataFieldDefinition.LOCATION_KIND,
                      ProductDataFieldDefinition.CATEGORY,
                      ProductDataFieldDefinition.IDENTIFIER,
                      ProductDataFieldDefinition.DISPLAY_NAME,
                      ProductDataFieldDefinition.DESCRIPTION,
                      ProductDataFieldDefinition.LOCATION_COORDINATES,
                      ProductDataFieldDefinition.LOCATION_COORDINATES,
                      ProductDataFieldDefinition.LOCATION_MAP_PROJECTION,
                      ProductDataFieldDefinition.LOCATION_POSTAL_ADDRESS,
                      ProductDataFieldDefinition.NETWORK_ADDRESS,
                      ProductDataFieldDefinition.CREATE_TIME,
                      ProductDataFieldDefinition.UPDATE_TIME},
              OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
              "Data set",
              null,
              "Locations"),



    /*
     * =============================================================================================
     */

    /**
     * Survey Reports
     */
    SURVEY_REPORTS(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                   null,
                   "Survey Reports",
                   "SURVEY-REPORTS",
                   ProductFolderDefinition.PRODUCTS,
                   "Survey Reports",
                   "Each product in this folder publishes insights from the surveys published through the open survey framework.  These surveys may be run as engine actions in the Engine Host, or run in an external surveying process that published results through the Data Discovery API.",
                   ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                   ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                   ProductCommunityDefinition.OBSERVABILITY_SIG,
                   new ProductSubscriptionDefinition[]{
                           ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                           ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                           ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                           ProductSubscriptionDefinition.ONGOING_UPDATE
                   },
                   null,
                   null,
                   null,
                   OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                   "Data sets",
                   null,
                   null),

    /**
     * List of Survey Reports
     */
    SURVEY_REPORT_LIST(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                       new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                       "Survey Report List",
                       "SURVEY-REPORT-LIST",
                       null,
                       "List of Survey Reports",
                       "A tabular data set where each record describes a survey report.  These surveys may be run as engine actions in the Engine Host, or run in an external surveying process that published results through the Data Discovery API.",
                       ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                       ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                       ProductCommunityDefinition.OBSERVABILITY_SIG,
                       new ProductSubscriptionDefinition[]{
                               ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                               ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                               ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                               ProductSubscriptionDefinition.ONGOING_UPDATE},
                       "Survey Report List",
                       new ProductDataFieldDefinition[]{
                               ProductDataFieldDefinition.SURVEY_REPORT_GUID},
                       new ProductDataFieldDefinition[]{
                               ProductDataFieldDefinition.QUALIFIED_NAME,
                               ProductDataFieldDefinition.DISPLAY_NAME,
                               ProductDataFieldDefinition.CATEGORY,
                               ProductDataFieldDefinition.IDENTIFIER,
                               ProductDataFieldDefinition.DISPLAY_NAME,
                               ProductDataFieldDefinition.DESCRIPTION,
                               ProductDataFieldDefinition.PURPOSE,
                               ProductDataFieldDefinition.START_TIMESTAMP,
                               ProductDataFieldDefinition.END_TIMESTAMP,
                               ProductDataFieldDefinition.ASSET_GUID,
                               ProductDataFieldDefinition.ASSET_TYPE_NAME,
                               ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                               ProductDataFieldDefinition.ENGINE_ACTION_GUID,
                               ProductDataFieldDefinition.INITIATOR_USER_ID,
                               ProductDataFieldDefinition.REQUEST_TYPE,
                               ProductDataFieldDefinition.GOVERNANCE_ENGINE_NAME,
                               ProductDataFieldDefinition.ENGINE_HOST_USER_ID,
                               ProductDataFieldDefinition.CREATE_TIME,
                               ProductDataFieldDefinition.UPDATE_TIME},
                       OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                       "Data set",
                       null,
                       "Survey Report List"),

    /**
     * List of Annotations
     */
    ANNOTATIONS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                "Annotations List",
                "ANNOTATIONS-LIST",
                null,
                "List of Annotations from all Survey Reports",
                "A tabular data set where each record describes an annotation from a survey report.",
                ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                ProductCommunityDefinition.OBSERVABILITY_SIG,
                new ProductSubscriptionDefinition[]{
                        ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                        ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                        ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                        ProductSubscriptionDefinition.ONGOING_UPDATE},
                "Annotation List",
                new ProductDataFieldDefinition[]{
                        ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                        ProductDataFieldDefinition.ANNOTATION_GUID,
                        ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                },
                new ProductDataFieldDefinition[]{
                        ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                        ProductDataFieldDefinition.QUALIFIED_NAME,
                        ProductDataFieldDefinition.DISPLAY_NAME,
                        ProductDataFieldDefinition.CATEGORY,
                        ProductDataFieldDefinition.IDENTIFIER,
                        ProductDataFieldDefinition.DISPLAY_NAME,
                        ProductDataFieldDefinition.DESCRIPTION,
                        ProductDataFieldDefinition.PURPOSE,
                        ProductDataFieldDefinition.START_TIMESTAMP,
                        ProductDataFieldDefinition.END_TIMESTAMP,
                        ProductDataFieldDefinition.ASSET_GUID,
                        ProductDataFieldDefinition.ASSET_TYPE_NAME,
                        ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                        ProductDataFieldDefinition.ENGINE_ACTION_GUID,
                        ProductDataFieldDefinition.INITIATOR_USER_ID,
                        ProductDataFieldDefinition.REQUEST_TYPE,
                        ProductDataFieldDefinition.GOVERNANCE_ENGINE_NAME,
                        ProductDataFieldDefinition.ENGINE_HOST_USER_ID,
                        ProductDataFieldDefinition.CREATE_TIME,
                        ProductDataFieldDefinition.UPDATE_TIME},
                OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                "Data set",
                null,
                "Annotation List"),

    /**
     * Request For Action Annotation List
     */
    REQUEST_FOR_ACTION(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                       new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                       "Request For Action Annotations List",
                       "REQUEST-FOR-ACTION-ANNOTATIONS-LIST",
                       null,
                       "List of Request For Action Annotations from all Survey Reports",
                       "A tabular data set where each record describes a request for action annotation from a survey report.  This digital product supplements the main annotation list product (Annotation List).",
                       ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                       ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                       ProductCommunityDefinition.OBSERVABILITY_SIG,
                       new ProductSubscriptionDefinition[]{
                               ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                               ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                               ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                               ProductSubscriptionDefinition.ONGOING_UPDATE},
                       "Request For Action Annotation List",
                       new ProductDataFieldDefinition[]{
                               ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                               ProductDataFieldDefinition.ANNOTATION_GUID,
                               ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                       },
                       new ProductDataFieldDefinition[]{
                               ProductDataFieldDefinition.ACTION_REQUEST_NAME,
                               ProductDataFieldDefinition.CREATE_TIME,
                               ProductDataFieldDefinition.UPDATE_TIME},
                       OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                       "Data set",
                       null,
                       "Request For Action Annotation List"),

    /**
     * Request For Action Annotation List
     */
    REQUEST_FOR_ACTION_TARGET(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                              new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                              "Request For Action Target List",
                              "REQUEST-FOR-ACTION-TARGET-LIST",
                              null,
                              "List of Request For Action Annotations from all Survey Reports",
                              "A tabular data set where each record describes a request for action annotation from a survey report.  This digital product supplements the main annotation list product (Annotation List).",
                              ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                              ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                              ProductCommunityDefinition.OBSERVABILITY_SIG,
                              new ProductSubscriptionDefinition[]{
                                      ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                      ProductSubscriptionDefinition.ONGOING_UPDATE},
                              "Request For Action Target List",
                              new ProductDataFieldDefinition[]{
                                      ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                      ProductDataFieldDefinition.ANNOTATION_GUID,
                                      ProductDataFieldDefinition.ACTION_TARGET_RELATIONSHIP_GUID,
                                      ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                              },
                              new ProductDataFieldDefinition[]{
                                      ProductDataFieldDefinition.ACTION_REQUEST_NAME,
                                      ProductDataFieldDefinition.ACTION_TARGET_GUID,
                                      ProductDataFieldDefinition.ACTION_TARGET_NAME,
                                      ProductDataFieldDefinition.ACTION_TARGET_TYPE_NAME,
                                      ProductDataFieldDefinition.CREATE_TIME,
                                      ProductDataFieldDefinition.UPDATE_TIME},
                              OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                              "Data set",
                              null,
                              "Request For Action Target List"),


    /**
     * Relational Data Manager Measurements
     */
    RELATIONAL_DATA_MANAGER_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                         new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                                         "Relational Data Manager Measurements",
                                         "RELATIONAL-DATA-MANAGER-MEASUREMENTS",
                                         null,
                                         "Resource measurement annotations for surveyed Relational Data Managers",
                                         "Details of the measurements collected by a survey of a relational data manager.  This digital product supplements the main annotation list product (Annotation List).",
                                         ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                                         ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                         ProductCommunityDefinition.OBSERVABILITY_SIG,
                                         new ProductSubscriptionDefinition[]{
                                                 ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                                 ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                                 ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                                 ProductSubscriptionDefinition.ONGOING_UPDATE},
                                         "Relational Data Manager Measurements",
                                         new ProductDataFieldDefinition[]{
                                                 ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                                 ProductDataFieldDefinition.ANNOTATION_GUID,
                                                 ProductDataFieldDefinition.RESOURCE_NAME,
                                                 ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                                         },
                                         new ProductDataFieldDefinition[]{
                                                 ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                                 ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                                 ProductDataFieldDefinition.SCHEMA_COUNT,
                                                 ProductDataFieldDefinition.TABLE_COUNT,
                                                 ProductDataFieldDefinition.VIEW_COUNT,
                                                 ProductDataFieldDefinition.MAT_VIEW_COUNT,
                                                 ProductDataFieldDefinition.COLUMN_COUNT,
                                                 ProductDataFieldDefinition.DATA_SIZE,
                                                 ProductDataFieldDefinition.ROWS_FETCHED,
                                                 ProductDataFieldDefinition.ROWS_UPDATED,
                                                 ProductDataFieldDefinition.ROWS_DELETED,
                                                 ProductDataFieldDefinition.SESSION_TIME,
                                                 ProductDataFieldDefinition.ACTIVE_TIME,
                                                 ProductDataFieldDefinition.CREATE_TIME,
                                                 ProductDataFieldDefinition.UPDATE_TIME},
                                         OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                         "Data set",
                                         null,
                                         "Relational Data Manager Measurements"),

    /**
     * Relational Schema Measurements
     */
    RELATIONAL_SCHEMA_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                   new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                                   "Relational Schema Measurements",
                                   "RELATIONAL-SCHEMA-MEASUREMENTS",
                                   null,
                                   "Resource measurement annotations for surveyed Relational Schemas",
                                   "Details of the measurements collected by a survey of relational schemas from multiple databases.  This digital product supplements the main annotation list product (Annotation List).",
                                   ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                                   ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                   ProductCommunityDefinition.OBSERVABILITY_SIG,
                                   new ProductSubscriptionDefinition[]{
                                           ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                           ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                           ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                           ProductSubscriptionDefinition.ONGOING_UPDATE},
                                   "Relational Schema Measurements",
                                   new ProductDataFieldDefinition[]{
                                           ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                           ProductDataFieldDefinition.ANNOTATION_GUID,
                                           ProductDataFieldDefinition.RESOURCE_NAME,
                                           ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                                   },
                                   new ProductDataFieldDefinition[]{
                                           ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                           ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                           ProductDataFieldDefinition.DISPLAY_NAME,
                                           ProductDataFieldDefinition.TABLE_COUNT,
                                           ProductDataFieldDefinition.VIEW_COUNT,
                                           ProductDataFieldDefinition.MAT_VIEW_COUNT,
                                           ProductDataFieldDefinition.COLUMN_COUNT,
                                           ProductDataFieldDefinition.DATA_SIZE,
                                           ProductDataFieldDefinition.CREATE_TIME,
                                           ProductDataFieldDefinition.UPDATE_TIME},
                                   OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                   "Data set",
                                   null,
                                   "Relational Schema Measurements"),


    /**
     * Relational Table Measurements
     */
    RELATIONAL_TABLE_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                  new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                                  "Relational Table Measurements",
                                  "RELATIONAL-TABLE-MEASUREMENTS",
                                  null,
                                  "Resource measurement annotations for surveyed Relational Tables",
                                  "Details of the measurements collected by a survey of relational tables from multiple databases.  This digital product supplements the main annotation list product (Annotation List).",
                                  ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                                  ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                  ProductCommunityDefinition.OBSERVABILITY_SIG,
                                  new ProductSubscriptionDefinition[]{
                                          ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                          ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                          ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                          ProductSubscriptionDefinition.ONGOING_UPDATE},
                                  "Relational Table Measurements",
                                  new ProductDataFieldDefinition[]{
                                          ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                          ProductDataFieldDefinition.ANNOTATION_GUID,
                                          ProductDataFieldDefinition.RESOURCE_NAME,
                                          ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                                  },
                                  new ProductDataFieldDefinition[]{
                                          ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                          ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                          ProductDataFieldDefinition.DISPLAY_NAME,
                                          ProductDataFieldDefinition.TABLE_COUNT,
                                          ProductDataFieldDefinition.VIEW_COUNT,
                                          ProductDataFieldDefinition.MAT_VIEW_COUNT,
                                          ProductDataFieldDefinition.COLUMN_COUNT,
                                          ProductDataFieldDefinition.DATA_SIZE,
                                          ProductDataFieldDefinition.CREATE_TIME,
                                          ProductDataFieldDefinition.UPDATE_TIME},
                                  OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                  "Data set",
                                  null,
                                  "Relational Table Measurements"),

    /**
     * Relational Column Measurements
     */
    RELATIONAL_COLUMN_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                   new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                                   "Relational Column Measurements",
                                   "RELATIONAL-COLUMN-MEASUREMENTS",
                                   null,
                                   "Resource measurement annotations for surveyed Relational Columns",
                                   "Details of the measurements collected by a survey of relational columns from multiple databases.  This digital product supplements the main annotation list product (Annotation List).",
                                   ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                                   ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                   ProductCommunityDefinition.OBSERVABILITY_SIG,
                                   new ProductSubscriptionDefinition[]{
                                           ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                           ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                           ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                           ProductSubscriptionDefinition.ONGOING_UPDATE},
                                   "Relational Column Measurements",
                                   new ProductDataFieldDefinition[]{
                                           ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                           ProductDataFieldDefinition.ANNOTATION_GUID,
                                           ProductDataFieldDefinition.RESOURCE_NAME,
                                           ProductDataFieldDefinition.SURVEY_SUBJECT_GUID
                                   },
                                   new ProductDataFieldDefinition[]{
                                           ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                           ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                           ProductDataFieldDefinition.DISPLAY_NAME,
                                           ProductDataFieldDefinition.DATA_TYPE,
                                           ProductDataFieldDefinition.DATA_SIZE,
                                           ProductDataFieldDefinition.DATA_SIZE,
                                           ProductDataFieldDefinition.AVERAGE_WIDTH,
                                           ProductDataFieldDefinition.NUMBER_OF_DISTINCT_VALUES,
                                           ProductDataFieldDefinition.MOST_COMMON_VALUES,
                                           ProductDataFieldDefinition.MOST_COMMON_VALUES_FREQUENCY,
                                           ProductDataFieldDefinition.CREATE_TIME,
                                           ProductDataFieldDefinition.UPDATE_TIME},
                                   OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                   "Data set",
                                   null,
                                   "Relational Column Measurements"),

    /**
     * File Measurements
     */
    FILE_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                      new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                      "File Measurements",
                      "FILE-MEASUREMENTS",
                      null,
                      "Resource measurement annotations for surveyed Files",
                      "Details of the measurements collected by a survey of files from multiple directories (folders).  This digital product supplements the main annotation list product (Annotation List).",
                      ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                      ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                      ProductCommunityDefinition.OBSERVABILITY_SIG,
                      new ProductSubscriptionDefinition[]{
                              ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                              ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                              ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                              ProductSubscriptionDefinition.ONGOING_UPDATE},
                      "File Measurements",
                      new ProductDataFieldDefinition[]{
                              ProductDataFieldDefinition.ANNOTATION_GUID,
                              ProductDataFieldDefinition.PATHNAME
                      },
                      new ProductDataFieldDefinition[]{
                              ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                              ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                              ProductDataFieldDefinition.SURVEY_SUBJECT_GUID,
                              ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                              ProductDataFieldDefinition.FILE_NAME,
                              ProductDataFieldDefinition.FILE_EXTENSION,
                              ProductDataFieldDefinition.FILE_TYPE,
                              ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE,
                              ProductDataFieldDefinition.ENCODING,
                              ProductDataFieldDefinition.ASSET_TYPE_NAME,
                              ProductDataFieldDefinition.CAN_READ,
                              ProductDataFieldDefinition.CAN_WRITE,
                              ProductDataFieldDefinition.CAN_EXECUTE,
                              ProductDataFieldDefinition.IS_SYM_LINK,
                              ProductDataFieldDefinition.IS_HIDDEN,
                              ProductDataFieldDefinition.FILE_CREATION_TIME,
                              ProductDataFieldDefinition.LAST_FILE_MODIFICATION_TIME,
                              ProductDataFieldDefinition.LAST_ACCESSED_TIME,
                              ProductDataFieldDefinition.FILE_SIZE,
                              ProductDataFieldDefinition.RECORD_COUNT,
                              ProductDataFieldDefinition.CREATE_TIME,
                              ProductDataFieldDefinition.UPDATE_TIME},
                      OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                      "Data set",
                      null,
                      "File Measurements"),

    /**
     * File Directory (Folder) Measurements
     */
    FOLDER_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                        new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                        "File Directory (Folder) Measurements",
                        "FILE-DIRECTORY-MEASUREMENTS",
                        null,
                        "Resource measurement annotations for surveyed directories (folders)",
                        "Details of files found in a directory (and subdirectories).  This digital product supplements the main annotation list product (Annotation List).",
                        ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                        ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                        ProductCommunityDefinition.OBSERVABILITY_SIG,
                        new ProductSubscriptionDefinition[]{
                                ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.ONGOING_UPDATE},
                        "File Directory Measurements",
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.ANNOTATION_GUID,
                                ProductDataFieldDefinition.PATHNAME
                        },
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                ProductDataFieldDefinition.SURVEY_SUBJECT_GUID,
                                ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                ProductDataFieldDefinition.FILE_COUNT,
                                ProductDataFieldDefinition.TOTAL_FILE_SIZE,
                                ProductDataFieldDefinition.SUB_DIRECTORY_COUNT,
                                ProductDataFieldDefinition.READABLE_FILE_COUNT,
                                ProductDataFieldDefinition.WRITEABLE_FILE_COUNT,
                                ProductDataFieldDefinition.EXECUTABLE_FILE_COUNT,
                                ProductDataFieldDefinition.SYM_LINK_FILE_COUNT,
                                ProductDataFieldDefinition.HIDDEN_FILE_COUNT,
                                ProductDataFieldDefinition.FILE_NAME_COUNT,
                                ProductDataFieldDefinition.FILE_EXTENSION_COUNT,
                                ProductDataFieldDefinition.FILE_TYPE_COUNT,
                                ProductDataFieldDefinition.ASSET_TYPE_COUNT,
                                ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE_COUNT,
                                ProductDataFieldDefinition.UNCLASSIFIED_FILE_COUNT,
                                ProductDataFieldDefinition.INACCESSIBLE_FILE_COUNT,
                                ProductDataFieldDefinition.LAST_FILE_CREATION_TIME,
                                ProductDataFieldDefinition.LAST_FILE_MODIFICATION_TIME,
                                ProductDataFieldDefinition.LAST_FILE_ACCESSED_TIME,
                                ProductDataFieldDefinition.CREATE_TIME,
                                ProductDataFieldDefinition.UPDATE_TIME},
                        OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                        "Data set",
                        null,
                        "File Directory Measurements"),


    /**
     * Resource Measurements
     */
    RESOURCE_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                          new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                          "Resource Measurements",
                          "RESOURCE-MEASUREMENTS",
                          null,
                          "Resource measurement annotations for surveyed resources",
                          "Details of individual resources surveyed.  This digital product supplements the main annotation list product (Annotation List).",
                          ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                          ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                          ProductCommunityDefinition.OBSERVABILITY_SIG,
                          new ProductSubscriptionDefinition[]{
                                  ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                  ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                  ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                  ProductSubscriptionDefinition.ONGOING_UPDATE},
                          "Resource Measurements",
                          new ProductDataFieldDefinition[]{
                                  ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                  ProductDataFieldDefinition.ANNOTATION_GUID,
                                  ProductDataFieldDefinition.SURVEY_SUBJECT_GUID,
                                  ProductDataFieldDefinition.MEASUREMENT_CATEGORY
                          },
                          new ProductDataFieldDefinition[]{
                                  ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                  ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                  ProductDataFieldDefinition.MEASUREMENT_NAME,
                                  ProductDataFieldDefinition.MEASUREMENT_VALUE,
                                  ProductDataFieldDefinition.MEASUREMENT_DISPLAY_NAME,
                                  ProductDataFieldDefinition.MEASUREMENT_NUMERIC_VALUE,
                                  ProductDataFieldDefinition.RESOURCE_CREATION_TIME,
                                  ProductDataFieldDefinition.LAST_MODIFIED_TIME,
                                  ProductDataFieldDefinition.LAST_ACCESSED_TIME,
                                  ProductDataFieldDefinition.RESOURCE_SIZE,
                                  ProductDataFieldDefinition.ENCODING,
                                  ProductDataFieldDefinition.CREATE_TIME,
                                  ProductDataFieldDefinition.UPDATE_TIME},
                          OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                          "Data set",
                          null,
                          "Resource Measurements"),


    /**
     * Profile Measurements
     */
    PROFILE_MEASUREMENTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                         new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                         "Profile Measurements",
                         "PROFILE-MEASUREMENTS",
                         null,
                         "Profile measurement annotations for surveyed resources",
                         "Details of individual profile measurements.  This digital product supplements the main annotation list product (Annotation List).",
                         ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                         ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                         ProductCommunityDefinition.OBSERVABILITY_SIG,
                         new ProductSubscriptionDefinition[]{
                                 ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                 ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                 ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                 ProductSubscriptionDefinition.ONGOING_UPDATE},
                         "Profile Measurements",
                         new ProductDataFieldDefinition[]{
                                 ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                 ProductDataFieldDefinition.ANNOTATION_GUID,
                                 ProductDataFieldDefinition.SURVEY_SUBJECT_GUID,
                                 ProductDataFieldDefinition.MEASUREMENT_CATEGORY
                         },
                         new ProductDataFieldDefinition[]{
                                 ProductDataFieldDefinition.METADATA_COLLECTION_ID,
                                 ProductDataFieldDefinition.SURVEY_SUBJECT_TYPE_NAME,
                                 ProductDataFieldDefinition.MEASUREMENT_NAME,
                                 ProductDataFieldDefinition.MEASUREMENT_VALUE,
                                 ProductDataFieldDefinition.MEASUREMENT_NUMERIC_VALUE,
                                 ProductDataFieldDefinition.CREATE_TIME,
                                 ProductDataFieldDefinition.UPDATE_TIME},
                         OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                         "Data set",
                         null,
                         "Profile Measurements"),


    /**
     * Missing File Classifiers
     */
    MISSING_FILE_CLASSIFIERS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                             new ProductDefinition[]{ProductDefinitionEnum.SURVEY_REPORTS},
                             "Missing File Classifiers",
                             "MISSING-FILE-CLASSIFIERS",
                             null,
                             "Missing File Classifiers",
                             "List of files that could not be classified using the file reference data supplied by Core Content Pack.",
                             ProductCategoryDefinition.SURVEY_REPORTS.getPreferredValue(),
                             ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                             ProductCommunityDefinition.OBSERVABILITY_SIG,
                             new ProductSubscriptionDefinition[]{
                                     ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                     ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                     ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                     ProductSubscriptionDefinition.ONGOING_UPDATE},
                             "Missing File Classifiers",
                             new ProductDataFieldDefinition[]{
                                     ProductDataFieldDefinition.SYNC_TIME,
                                     ProductDataFieldDefinition.PATHNAME
                             },
                             new ProductDataFieldDefinition[]{
                                     ProductDataFieldDefinition.SURVEY_REPORT_GUID,
                                     ProductDataFieldDefinition.FILE_NAME,
                                     ProductDataFieldDefinition.FILE_EXTENSION,
                                     ProductDataFieldDefinition.FILE_TYPE,
                                     ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE,
                                     ProductDataFieldDefinition.ENCODING,
                                     ProductDataFieldDefinition.ASSET_TYPE_NAME,
                                     ProductDataFieldDefinition.CREATE_TIME,
                                     ProductDataFieldDefinition.UPDATE_TIME},
                             OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                             "Data set",
                             null,
                             "Missing File Classifiers"),



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
                               "Each product in this folder publishes insights about the activity of the organization observed through the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each one as a trigger to perform specific processing.",
                               ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                               ProductGovernanceDefinition.PERSONAL_DATA,
                               ProductCommunityDefinition.OBSERVABILITY_SIG,
                               new ProductSubscriptionDefinition[]{
                                       ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                       ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                       ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                       ProductSubscriptionDefinition.ONGOING_UPDATE
                               },
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
                             new ProductSubscriptionDefinition[]{
                                     ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                     ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                     ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                     ProductSubscriptionDefinition.ONGOING_UPDATE
                             },
                             null,
                             null,
                             null,
                             OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                             "Data sets",
                             null,
                             null),

    /**
     * Details of the governance controls known by open metadata.
     */
    GOVERNANCE_CONTROLS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                   new ProductDefinition[]{ProductDefinitionEnum.GOVERNANCE_OBSERVABILITY},
                   "Governance Controls",
                   "GOVERNANCE-CONTROLS",
                   null,
                   "Governance Controls",
                   "A tabular data set where each record describes a governance control from open metadata.",
                   ProductCategoryDefinition.GOVERNANCE.getPreferredValue(),
                   ProductGovernanceDefinition.CC_BY_40,
                   ProductCommunityDefinition.GOVERNANCE_SIG,
                   new ProductSubscriptionDefinition[]{
                           ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                           ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                           ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                           ProductSubscriptionDefinition.ONGOING_UPDATE},
                   "Governance Controls",
                   new ProductDataFieldDefinition[]{
                           ProductDataFieldDefinition.GUID},
                   new ProductDataFieldDefinition[]{
                           ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                           ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                           ProductDataFieldDefinition.IDENTIFIER,
                           ProductDataFieldDefinition.URL,
                           ProductDataFieldDefinition.QUALIFIED_NAME,
                           ProductDataFieldDefinition.DESCRIPTION,
                           ProductDataFieldDefinition.DISPLAY_NAME,
                           ProductDataFieldDefinition.SCOPE,
                           ProductDataFieldDefinition.CREATE_TIME,
                           ProductDataFieldDefinition.UPDATE_TIME},
                   OpenMetadataType.TABULAR_DATA_SET.typeName,
                   "Data set",
                   null,
                   "Governance Controls"),

    /**
     * Details of the exceptions known by open metadata.
     */
    EXCEPTIONS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
               new ProductDefinition[]{ProductDefinitionEnum.GOVERNANCE_OBSERVABILITY},
                        "Exceptions",
                        "EXCEPTIONS",
                        null,
                        "Exceptions",
                        "A tabular data set where each record describes an exception from open metadata.",
                        ProductCategoryDefinition.GOVERNANCE.getPreferredValue(),
                        ProductGovernanceDefinition.CC_BY_40,
                        ProductCommunityDefinition.GOVERNANCE_SIG,
                        new ProductSubscriptionDefinition[]{
                                ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.ONGOING_UPDATE},
                        "Exceptions",
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.EXCEPTION_GUID},
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                                ProductDataFieldDefinition.EXCEPTION_TYPE_GUID,
                                ProductDataFieldDefinition.ELEMENT_GUID,
                                ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                                ProductDataFieldDefinition.LABEL,
                                ProductDataFieldDefinition.DESCRIPTION,
                                ProductDataFieldDefinition.LAST_REVIEW_TIME,
                                ProductDataFieldDefinition.REVIEW_DATE,
                                ProductDataFieldDefinition.STEWARD,
                                ProductDataFieldDefinition.STEWARD_TYPE_NAME,
                                ProductDataFieldDefinition.STEWARD_PROPERTY_NAME,
                                ProductDataFieldDefinition.NOTES,
                                ProductDataFieldDefinition.CREATE_TIME,
                                ProductDataFieldDefinition.UPDATE_TIME},
                        OpenMetadataType.TABULAR_DATA_SET.typeName,
                        "Data set",
                        null,
                        "Exceptions"),

    /**
     * Details of the certifications known by open metadata.
     */
    CERTIFICATIONS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
               new ProductDefinition[]{ProductDefinitionEnum.GOVERNANCE_OBSERVABILITY},
               "Certifications",
               "CERTIFICATIONS",
               null,
               "Certifications",
               "A tabular data set where each record describes a certification known by open metadata.",
               ProductCategoryDefinition.GOVERNANCE.getPreferredValue(),
               ProductGovernanceDefinition.CC_BY_40,
               ProductCommunityDefinition.GOVERNANCE_SIG,
               new ProductSubscriptionDefinition[]{
                       ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                       ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                       ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                       ProductSubscriptionDefinition.ONGOING_UPDATE},
               "Certifications",
               new ProductDataFieldDefinition[]{
                       ProductDataFieldDefinition.EXCEPTION_GUID},
               new ProductDataFieldDefinition[]{
                       ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                       ProductDataFieldDefinition.EXCEPTION_TYPE_GUID,
                       ProductDataFieldDefinition.ELEMENT_GUID,
                       ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                       ProductDataFieldDefinition.COVERAGE_START,
                       ProductDataFieldDefinition.COVERAGE_END,
                       ProductDataFieldDefinition.CERTIFIED_BY,
                       ProductDataFieldDefinition.CERTIFIED_BY_TYPE_NAME,
                       ProductDataFieldDefinition.CERTIFIED_BY_PROPERTY_NAME,
                       ProductDataFieldDefinition.CUSTODIAN,
                       ProductDataFieldDefinition.CUSTODIAN_TYPE_NAME,
                       ProductDataFieldDefinition.CUSTODIAN_PROPERTY_NAME,
                       ProductDataFieldDefinition.RECIPIENT,
                       ProductDataFieldDefinition.RECIPIENT_TYPE_NAME,
                       ProductDataFieldDefinition.RECIPIENT_PROPERTY_NAME,
                       ProductDataFieldDefinition.NOTES,
                       ProductDataFieldDefinition.CREATE_TIME,
                       ProductDataFieldDefinition.UPDATE_TIME},
               OpenMetadataType.TABULAR_DATA_SET.typeName,
               "Data set",
               null,
               "Certifications"),

    /**
     * Details of the licenses known by open metadata.
     */
    LICENSES(OpenMetadataType.DIGITAL_PRODUCT.typeName,
               new ProductDefinition[]{ProductDefinitionEnum.GOVERNANCE_OBSERVABILITY},
               "Licenses",
               "LICENSES",
               null,
               "Licenses",
               "A tabular data set where each record describes a license known by the open metadata.",
               ProductCategoryDefinition.GOVERNANCE.getPreferredValue(),
               ProductGovernanceDefinition.CC_BY_40,
               ProductCommunityDefinition.GOVERNANCE_SIG,
               new ProductSubscriptionDefinition[]{
                       ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                       ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                       ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                       ProductSubscriptionDefinition.ONGOING_UPDATE},
               "Licenses",
               new ProductDataFieldDefinition[]{
                       ProductDataFieldDefinition.LICENSE_GUID},
               new ProductDataFieldDefinition[]{
                       ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                       ProductDataFieldDefinition.LICENSE_TYPE_GUID,
                       ProductDataFieldDefinition.ELEMENT_GUID,
                       ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                       ProductDataFieldDefinition.COVERAGE_START,
                       ProductDataFieldDefinition.COVERAGE_END,
                       ProductDataFieldDefinition.LICENSED_BY,
                       ProductDataFieldDefinition.LICENSED_BY_TYPE_NAME,
                       ProductDataFieldDefinition.LICENSED_BY_PROPERTY_NAME,
                       ProductDataFieldDefinition.CUSTODIAN,
                       ProductDataFieldDefinition.CUSTODIAN_TYPE_NAME,
                       ProductDataFieldDefinition.CUSTODIAN_PROPERTY_NAME,
                       ProductDataFieldDefinition.LICENSEE,
                       ProductDataFieldDefinition.LICENSEE_TYPE_NAME,
                       ProductDataFieldDefinition.LICENSEE_PROPERTY_NAME,
                       ProductDataFieldDefinition.NOTES,
                       ProductDataFieldDefinition.CREATE_TIME,
                       ProductDataFieldDefinition.UPDATE_TIME},
               OpenMetadataType.TABULAR_DATA_SET.typeName,
               "Data set",
               null,
               "Licenses"),

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
                     new ProductSubscriptionDefinition[]{
                             ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                             ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                             ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                             ProductSubscriptionDefinition.ONGOING_UPDATE
                     },
                     null,
                     null,
                     null,
                     OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                     "Data sets",
                     null,
                     null),

    /**
     * Details of the servers known by open metadata.
     */
    SERVERS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                        new ProductDefinition[]{ProductDefinitionEnum.IT_OBSERVABILITY},
                        "Servers",
                        "SERVERS",
                        null,
                        "Servers",
                        "A tabular data set where each record describes a server catalogued in open metadata.",
                        ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                        ProductGovernanceDefinition.CC_BY_40,
                        ProductCommunityDefinition.OBSERVABILITY_SIG,
                        new ProductSubscriptionDefinition[]{
                                ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.ONGOING_UPDATE},
                        "Governance Controls",
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.GUID},
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                                ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                                ProductDataFieldDefinition.URL,
                                ProductDataFieldDefinition.QUALIFIED_NAME,
                                ProductDataFieldDefinition.DESCRIPTION,
                                ProductDataFieldDefinition.DISPLAY_NAME,
                                ProductDataFieldDefinition.SCOPE,
                                ProductDataFieldDefinition.CREATE_TIME,
                                ProductDataFieldDefinition.UPDATE_TIME},
                        OpenMetadataType.TABULAR_DATA_SET.typeName,
                        "Data set",
                        null,
                        "Governance Controls"),

    /*
     * =============================================================================================
     */

    /**
     * Security Observability
     */
    SECURITY_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                           null,
                           "Security Observability",
                           "SECURITY-OBSERVABILITY-FAMILY",
                           ProductFolderDefinition.PRODUCTS,
                           "Security Observability",
                           "Each product in this group publishes insights about the security settings supporting the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                           ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                           ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                           ProductCommunityDefinition.SECURITY_SIG,
                           new ProductSubscriptionDefinition[]{
                                   ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.ONGOING_UPDATE
                           },
                           null,
                           null,
                           null,
                           OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                           "Data sets",
                           null,
                           null,
                           new String[]{GovernanceZoneName.SECURITY.getZoneName()}),

    /**
     * Secrets Observability
     */
    SECRETS_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                       new ProductDefinition[]{SECURITY_OBSERVABILITY},
                       "Secrets Observability",
                       "SECRETS-OBSERVABILITY-FAMILY",
                       null,
                       "Secrets Observability",
                       "Each product in this group publishes insights about the secrets stores and collections known to the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                       ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                       ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                       ProductCommunityDefinition.SECURITY_SIG,
                       new ProductSubscriptionDefinition[]{
                               ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                               ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                               ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                               ProductSubscriptionDefinition.ONGOING_UPDATE
                       },
                       null,
                       null,
                       null,
                       OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                       "Data sets",
                       null,
                       null,
                       new String[]{GovernanceZoneName.SECURITY.getZoneName()}),

    /**
     * Details of the secrets store known by open metadata.
     */
    SECRETS_STORES(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                    new ProductDefinition[]{ProductDefinitionEnum.SECRETS_OBSERVABILITY},
                    "Open Metadata Secrets Stores",
                    "OPEN-METADATA-SECRETS-STORES",
                    null,
                    "Open Metadata Secrets Stores",
                    "A tabular data set where each record describes a secrets store known to open metadata.",
                    ProductCategoryDefinition.SECURITY.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.SECURITY_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata Secrets Stores",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.ASSET_GUID},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                            ProductDataFieldDefinition.RESOURCE_NAME,
                            ProductDataFieldDefinition.URL,
                            ProductDataFieldDefinition.QUALIFIED_NAME,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.DISPLAY_NAME,
                            ProductDataFieldDefinition.NETWORK_ADDRESS,
                            ProductDataFieldDefinition.CREATE_TIME,
                            ProductDataFieldDefinition.UPDATE_TIME},
                    OpenMetadataType.TABULAR_DATA_SET.typeName,
                    "Data set",
                    null,
                    "Open Metadata Secrets Stores"),

    /**
     * Details of the secrets collections known by open metadata.
     */
    SECRETS_COLLECTIONS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                    new ProductDefinition[]{ProductDefinitionEnum.SECRETS_OBSERVABILITY},
                    "Open Metadata Secrets Collections",
                    "OPEN-METADATA-SECRETS-COLLECTIONS",
                    null,
                    "Open Metadata Secrets Collections",
                    "A tabular data set where each record describes a user identity and its associated actor profile.",
                    ProductCategoryDefinition.SECURITY.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.SECURITY_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata Secrets Collections",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.SECRETS_COLLECTION_GUID},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.SECRETS_STORE_GUID,
                            ProductDataFieldDefinition.SECRETS_COLLECTION_NAME,
                            ProductDataFieldDefinition.QUALIFIED_NAME,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.DISPLAY_NAME,
                            ProductDataFieldDefinition.REFRESH_TIME_INTERVAL,
                            ProductDataFieldDefinition.USER_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.EMPLOYEE_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.CONTRACTOR_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.EXTERNAL_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.DIGITAL_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.ACTIVE_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.EXPIRED_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.LOCKED_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.DISABLED_ACCOUNT_COUNT,
                            ProductDataFieldDefinition.CREATE_TIME,
                            ProductDataFieldDefinition.UPDATE_TIME},
                    OpenMetadataType.TABULAR_DATA_SET.typeName,
                    "Data set",
                    null,
                    "Open Metadata Secrets Collections"),


    /**
     * User Observability
     */
    USER_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                       new ProductDefinition[]{SECURITY_OBSERVABILITY},
                           "User Observability",
                           "USER-OBSERVABILITY-FAMILY",
                           null,
                           "User Observability",
                           "Each product in this group publishes insights about the security settings of the users of the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                           ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                           ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                           ProductCommunityDefinition.SECURITY_SIG,
                           new ProductSubscriptionDefinition[]{
                                   ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.ONGOING_UPDATE
                           },
                           null,
                           null,
                           null,
                           OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                           "Data sets",
                           null,
                           null,
                           new String[]{GovernanceZoneName.SECURITY.getZoneName()}),

    /**
     * Details of the user identities defined for open metadata.
     */
    USER_IDENTITIES(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                    new ProductDefinition[]{ProductDefinitionEnum.USER_OBSERVABILITY},
                    "Open Metadata User Identities",
                    "OPEN-METADATA-USER-IDENTITIES",
                    null,
                    "Open Metadata User Identities",
                    "A tabular data set where each record describes a user identity and its associated actor profile.",
                    ProductCategoryDefinition.SECURITY.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.SECURITY_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata User Identities",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.USER_ID},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.PROFILE_GUID,
                            ProductDataFieldDefinition.PROFILE_NAME,
                            ProductDataFieldDefinition.DISTINGUISHED_NAME,
                            ProductDataFieldDefinition.QUALIFIED_NAME,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.DISPLAY_NAME,
                            ProductDataFieldDefinition.SECURITY_GROUPS,
                            ProductDataFieldDefinition.SECURITY_ROLES,
                            ProductDataFieldDefinition.SECRETS_COLLECTION_NAME,
                            ProductDataFieldDefinition.CREATE_TIME,
                            ProductDataFieldDefinition.UPDATE_TIME},
                    OpenMetadataType.TABULAR_DATA_SET.typeName,
                    "Data set",
                    null,
                    "Open Metadata User Identities"),

    /**
     * Details of the user accounts defined in a secrets collection.
     */
    USER_ACCOUNTS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                    new ProductDefinition[]{ProductDefinitionEnum.USER_OBSERVABILITY},
                    "Open Metadata User Accounts",
                    "OPEN-METADATA-USER-ACCOUNTS",
                    null,
                    "Open Metadata User Accounts",
                    "A tabular data set where each record describes a user account defined in a secrets store.",
                    ProductCategoryDefinition.SECURITY.getPreferredValue(),
                    ProductGovernanceDefinition.CC_BY_40,
                    ProductCommunityDefinition.SECURITY_SIG,
                    new ProductSubscriptionDefinition[]{
                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                            ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                    "Open Metadata User Accounts",
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.USER_ID},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.SECRETS_COLLECTION_NAME,
                            ProductDataFieldDefinition.NETWORK_ADDRESS,
                            ProductDataFieldDefinition.DISTINGUISHED_NAME,
                            ProductDataFieldDefinition.QUALIFIED_NAME,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.DISPLAY_NAME,
                            ProductDataFieldDefinition.USER_ACCOUNT_TYPE,
                            ProductDataFieldDefinition.USER_ACCOUNT_STATUS,
                            ProductDataFieldDefinition.SECURITY_GROUPS,
                            ProductDataFieldDefinition.SECURITY_ROLES,
                            ProductDataFieldDefinition.CREATE_TIME,
                            ProductDataFieldDefinition.UPDATE_TIME},
                    OpenMetadataType.TABULAR_DATA_SET.typeName,
                    "Data set",
                    null,
                    "Open Metadata User Accounts"),

    /**
     * Governance Zone Observability
     */
    ZONE_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                       new ProductDefinition[]{SECURITY_OBSERVABILITY},
                           "Governance Zone Observability",
                           "ZONE-OBSERVABILITY-FAMILY",
                           null,
                           "Governance Zone Observability",
                           "Each product in this group publishes insights about the governance zones controlling visibility in the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                           ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                           ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                           ProductCommunityDefinition.SECURITY_SIG,
                           new ProductSubscriptionDefinition[]{
                                   ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.ONGOING_UPDATE
                           },
                           null,
                           null,
                           null,
                           OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                           "Data sets",
                           null,
                           null,
                           new String[]{GovernanceZoneName.SECURITY.getZoneName()}),

    /**
     * Details of a governance zone and the total count of members and related elements.
     */
    ZONE_MEMBERSHIP_TOTALS(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                        new ProductDefinition[]{ProductDefinitionEnum.SECURITY_OBSERVABILITY},
                        "Zone Membership Totals",
                        "ZONE-MEMBERSHIP-TOTALS",
                        null,
                        "Zone Membership Totals",
                        "A tabular data set where each record describes a governance zone and the total count of members and related elements.",
                        ProductCategoryDefinition.GOVERNANCE.getPreferredValue(),
                        ProductGovernanceDefinition.CC_BY_40,
                        ProductCommunityDefinition.GOVERNANCE_SIG,
                        new ProductSubscriptionDefinition[]{
                                ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                ProductSubscriptionDefinition.ONGOING_UPDATE},
                        "Zone Membership Totals",
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.GUID,
                                ProductDataFieldDefinition.UPDATE_TIME},
                        new ProductDataFieldDefinition[]{
                                ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                                ProductDataFieldDefinition.IDENTIFIER,
                                ProductDataFieldDefinition.CRITERIA,
                                ProductDataFieldDefinition.TOTAL_ELEMENT_MEMBERSHIP,
                                ProductDataFieldDefinition.ANCHORED_ELEMENT_MEMBERSHIP,
                                ProductDataFieldDefinition.ALL_ELEMENT_MEMBERSHIP,
                                ProductDataFieldDefinition.CREATE_TIME},
                        OpenMetadataType.TABULAR_DATA_SET.typeName,
                        "Data set",
                        null,
                        "Zone Membership Totals"),

    /**
     * Details of the total count of members and related elements of a specific type in the governance zone.
     */
    ZONE_MEMBERSHIP_PROFILE(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                            new ProductDefinition[]{ProductDefinitionEnum.SECURITY_OBSERVABILITY},
                            "Zone Membership Profile",
                            "ZONE-MEMBERSHIP-PROFILE",
                            null,
                            "Zone Membership Profile",
                            "A tabular data set where each record describes the total count of members and related elements of a specific type in the governance zone.",
                            ProductCategoryDefinition.GOVERNANCE.getPreferredValue(),
                            ProductGovernanceDefinition.CC_BY_40,
                            ProductCommunityDefinition.GOVERNANCE_SIG,
                            new ProductSubscriptionDefinition[]{
                                    ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                    ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                    ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                    ProductSubscriptionDefinition.ONGOING_UPDATE},
                            "Zone Membership Profile",
                            new ProductDataFieldDefinition[]{
                                    ProductDataFieldDefinition.GUID,
                                    ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                                    ProductDataFieldDefinition.UPDATE_TIME},
                            new ProductDataFieldDefinition[]{
                                    ProductDataFieldDefinition.DOMAIN_IDENTIFIER,
                                    ProductDataFieldDefinition.IDENTIFIER,
                                    ProductDataFieldDefinition.CRITERIA,
                                    ProductDataFieldDefinition.TOTAL_TYPE_MEMBERSHIP,
                                    ProductDataFieldDefinition.ANCHORED_TYPE_MEMBERSHIP,
                                    ProductDataFieldDefinition.ALL_TYPE_MEMBERSHIP,
                                    ProductDataFieldDefinition.CREATE_TIME},
                            OpenMetadataType.TABULAR_DATA_SET.typeName,
                            "Data set",
                            null,
                            "Zone Membership Profile"),

    /**
     * Service Observability
     */
    SERVICE_OBSERVABILITY(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                           new ProductDefinition[]{SECURITY_OBSERVABILITY},
                           "Service Observability",
                           "SERVICE-OBSERVABILITY-FAMILY",
                           null,
                           "Security Observability",
                           "Each product in this group publishes insights about the servers, services and operations supporting the open metadata ecosystem.  The latest insight is published to subscribers on a regular basis.  Subscribers can maintain a history of the insight publications, or treat each on as a trigger to perform specific processing.",
                           ProductCategoryDefinition.INSIGHT_NOTIFICATIONS.getPreferredValue(),
                           ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                           ProductCommunityDefinition.SECURITY_SIG,
                           new ProductSubscriptionDefinition[]{
                                   ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                   ProductSubscriptionDefinition.ONGOING_UPDATE
                           },
                           null,
                           null,
                           null,
                           OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                           "Data sets",
                           null,
                           null,
                           new String[]{GovernanceZoneName.SECURITY.getZoneName()}),


    ;


    private final String                          typeName;
    private final ProductDefinition[]             productFamilies;
    private final String                          productName;
    private final String                          identifier;
    private final ProductFolderDefinition         parentFolder;
    private final String                          displayName;
    private final String                          description;
    private final String                          category;
    private final ProductGovernanceDefinition     license;
    private final ProductCommunityDefinition      community;
    private final ProductSubscriptionDefinition[] subscriptionTypes;
    private final String                          dataSpecTableName;
    private final ProductDataFieldDefinition[]    dataSpecIdentifiers;
    private final ProductDataFieldDefinition[]    dataSpecFields;
    private final String                          assetTypeName;
    private final String                          assetIdentifier;
    private final ConnectorProvider               connectorProvider;
    private final String                          catalogTargetName;
    private final String[]                        zoneMembership;



    ProductDefinitionEnum(String                          typeName,
                          ProductDefinition[]             productFamilies,
                          String                          productName,
                          String                          identifier,
                          ProductFolderDefinition         parentFolder,
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
                          String                          catalogTargetName,
                          String[]                        zoneMembership)
    {
        this.typeName            = typeName;
        this.productFamilies     = productFamilies;
        this.productName         = productName;
        this.identifier          = identifier;
        this.parentFolder        = parentFolder;
        this.displayName         = displayName;
        this.description         = description;
        this.category            = category;
        this.license             = license;
        this.community           = community;
        this.subscriptionTypes   = subscriptionTypes;
        this.dataSpecTableName   = dataSpecTableName;
        this.dataSpecIdentifiers = dataSpecIdentifiers;
        this.dataSpecFields      = dataSpecFields;
        this.assetTypeName       = assetTypeName;
        this.assetIdentifier     = assetIdentifier;
        this.connectorProvider   = connectorProvider;
        this.catalogTargetName   = catalogTargetName;
        this.zoneMembership      = zoneMembership;
    }


    ProductDefinitionEnum(String                          typeName,
                          ProductDefinition[]             productFamilies,
                          String                          productName,
                          String                          identifier,
                          ProductFolderDefinition parentFolder,
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
        this.typeName            = typeName;
        this.productFamilies     = productFamilies;
        this.productName         = productName;
        this.identifier          = identifier;
        this.parentFolder        = parentFolder;
        this.displayName         = displayName;
        this.description         = description;
        this.category            = category;
        this.license             = license;
        this.community           = community;
        this.subscriptionTypes   = subscriptionTypes;
        this.dataSpecTableName   = dataSpecTableName;
        this.dataSpecIdentifiers = dataSpecIdentifiers;
        this.dataSpecFields      = dataSpecFields;
        this.assetTypeName       = assetTypeName;
        this.assetIdentifier     = assetIdentifier;
        this.connectorProvider   = connectorProvider;
        this.catalogTargetName   = catalogTargetName;
        this.zoneMembership      = null;
    }



    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    @Override
    public String getQualifiedName()
    {
        return typeName + "::Jacquard::" + identifier + "::" + displayName;
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
        return parentFolder;
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
        return "6.1-SNAPSHOT";
    }


    /**
     * Return the zone membership for the product.
     *
     * @return list of zone names (default = ["digital-products"])
     */
    @Override
    public List<String> zoneMembership()
    {
        if (zoneMembership != null)
        {
            return new ArrayList<>(Arrays.asList(zoneMembership));
        }

        return List.of(GovernanceZoneName.DIGITAL_PRODUCTS.getZoneName());
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
