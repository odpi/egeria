/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;

import org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint.ProductRoleDefinition;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ValidValueSetListProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The ProductDefinition describes the products (or templates for products) found in the open metadata product catalog.
 */
public enum ProductDefinitionEnum implements ProductDefinition
{
    /**
     * Valid Value Set List
     */
    VALID_VALUE_SET_LIST("Valid Value Set List",
                         "OPEN-METADATA-" + OpenMetadataType.VALID_VALUE_DEFINITION.typeName + "-with-members",
                         ProductFolderDefinition.VALID_VALUE_SETS,
                         "Valid Value Set List",
                         "A tabular data set where each record describes an open metadata valid value set.",
                         ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                         ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                         ProductCommunityDefinition.REFERENCE_DATA_SIG,
                         new ProductSubscriptionDefinition[]{
                                 ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                 ProductSubscriptionDefinition.ONGOING_UPDATE},
                         "Valid Value Set List",
                         new ProductDataFieldDefinition[]{
                                 ProductDataFieldDefinition.GUID},
                         new ProductDataFieldDefinition[]{
                                 ProductDataFieldDefinition.QUALIFIED_NAME,
                                 ProductDataFieldDefinition.DISPLAY_NAME,
                                 ProductDataFieldDefinition.DESCRIPTION,
                                 ProductDataFieldDefinition.CATEGORY,
                                 ProductDataFieldDefinition.NAMESPACE,
                                 ProductDataFieldDefinition.PREFERRED_VALUE,
                                 ProductDataFieldDefinition.IS_CASE_SENSITIVE,
                                 ProductDataFieldDefinition.DATA_TYPE,
                                 ProductDataFieldDefinition.SCOPE,
                                 ProductDataFieldDefinition.USAGE},
                         new ValidValueSetListProvider(),
                         "ValidValueSetList"),

    /**
     * Attributes List
     */
    ATTRIBUTES_LIST("Open Metadata Attributes List",
                    "OPEN-METADATA-ATTRIBUTES",
                    ProductFolderDefinition.OPEN_METADATA_TYPES,
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
                            ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_NAME},
                    new ProductDataFieldDefinition[]{
                            ProductDataFieldDefinition.GUID,
                            ProductDataFieldDefinition.DESCRIPTION,
                            ProductDataFieldDefinition.CATEGORY,
                            ProductDataFieldDefinition.DATA_TYPE},
                    null,
                    "OpenMetadataAttributes"),

    /**
     * Open Metadata Types List
     */
    TYPES_LIST("Open Metadata Types List",
               "OPEN-METADATA-TYPES",
               ProductFolderDefinition.OPEN_METADATA_TYPES,
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
                       ProductDataFieldDefinition.OPEN_METADATA_TYPE_GUID,
                       ProductDataFieldDefinition.DESCRIPTION,
                       ProductDataFieldDefinition.CATEGORY,
                       ProductDataFieldDefinition.WIKI_LINK,
                       ProductDataFieldDefinition.BEAN_CLASS_NAME,
                       ProductDataFieldDefinition.OPEN_METADATA_SUBTYPES,
                       ProductDataFieldDefinition.OPEN_METADATA_SUPER_TYPES},
               null,
               "OpenMetadataTypes"),

    /**
     * Open Metadata Types List
     */
    ATTRIBUTES_FOR_TYPES_LIST("Open Metadata Attributes For Types List",
                              "ALL-ATTRIBUTES-FOR-OPEN-METADATA-TYPES",
                              ProductFolderDefinition.OPEN_METADATA_TYPES,
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
                                      ProductDataFieldDefinition.DESCRIPTION},
                              null,
                              "OpenMetadataAttributesForTypes"),


    /**
     * Organizations List
     */
    ORGANIZATIONS("Organizations List",
                  "Organizations",
                  ProductFolderDefinition.PARTY_PLACES_PRODUCTS,
                  "Organizations List",
                  "A tabular data set where each record describes an organization interacting with open metadata.",
                  ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                  ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                  ProductCommunityDefinition.REFERENCE_DATA_SIG,
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
                  null,
                  "Organizations"),

    /**
     * List of People
     */
    PEOPLE("List of People",
           "People List",
           ProductFolderDefinition.PARTY_PLACES_PRODUCTS,
           "People List",
           "A tabular data set where each record describes a person interacting with open metadata.",
           ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
           ProductGovernanceDefinition.PERSONAL_DATA,
           ProductCommunityDefinition.REFERENCE_DATA_SIG,
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
           null,
           "People"),

    /**
     * List of Digital Products
     */
    DIGITAL_PRODUCTS("Digital Product Inventory",
                     "DIGITAL-PRODUCTS-INVENTORY",
                     ProductFolderDefinition.PARTY_PLACES_PRODUCTS,
                     "Digital Products Inventory",
                     "A tabular data set where each record describes a digital product.",
                     ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
                     ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                     ProductCommunityDefinition.REFERENCE_DATA_SIG,
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
                     null,
                     "DigitalProductsInventory"),

    /**
     * List of Locations
     */
    LOCATIONS("Location List",
              "LOCATIONS-LIST",
              ProductFolderDefinition.PARTY_PLACES_PRODUCTS,
              "List of Locations",
              "A tabular data set where each record describes a location.  This could be a site, or a facility within a site.",
              ProductCategoryDefinition.MASTER_DATA.getPreferredValue(),
              ProductGovernanceDefinition.INTERNAL_USE_ONLY,
              ProductCommunityDefinition.REFERENCE_DATA_SIG,
              new ProductSubscriptionDefinition[]{
                      ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                      ProductSubscriptionDefinition.ONGOING_UPDATE},
              "Locations",
              new ProductDataFieldDefinition[]{
                      ProductDataFieldDefinition.LOCATION_GUID},
              new ProductDataFieldDefinition[]{
                      ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME,
                      ProductDataFieldDefinition.LOCATION_CLASSIFICATION_NAME,
                      ProductDataFieldDefinition.CATEGORY,
                      ProductDataFieldDefinition.IDENTIFIER,
                      ProductDataFieldDefinition.DISPLAY_NAME,
                      ProductDataFieldDefinition.DESCRIPTION,
                      ProductDataFieldDefinition.LOCATION_COORDINATES,
                      ProductDataFieldDefinition.LOCATION_COORDINATES,
                      ProductDataFieldDefinition.LOCATION_MAP_PROJECTION,
                      ProductDataFieldDefinition.LOCATION_POSTAL_ADDRESS,
                      ProductDataFieldDefinition.NETWORK_ADDRESS},
              null,
              "Locations"),
    ;


    private final String                          productName;
    private final String                          identifier;
    private final ProductFolderDefinition         parent;
    private final String                          displayName;
    private final String                          description;
    private final String                          category;
    private final ProductGovernanceDefinition     license;
    private final ProductCommunityDefinition      community;
    private final ProductRoleDefinition           productManager;
    private final ProductSubscriptionDefinition[] subscriptionTypes;
    private final String                          dataSpecTableName;
    private final ProductDataFieldDefinition[]    dataSpecIdentifiers;
    private final ProductDataFieldDefinition[]    dataSpecFields;
    private final ConnectorProvider               connectorProvider;
    private final String                          catalogTargetName;



    ProductDefinitionEnum(String                          productName,
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
                          ConnectorProvider               connectorProvider,
                          String                          catalogTargetName)
    {
        this.productName         = productName;
        this.identifier          = identifier;
        this.parent              = parent;
        this.displayName         = displayName;
        this.description         = description;
        this.category            = category;
        this.license             = license;
        this.community           = community;
        this.productManager      = ProductRoleDefinition.PRODUCT_MANAGER;
        this.subscriptionTypes   = subscriptionTypes;
        this.dataSpecTableName   = dataSpecTableName;
        this.dataSpecIdentifiers = dataSpecIdentifiers;
        this.dataSpecFields      = dataSpecFields;
        this.connectorProvider   = connectorProvider;
        this.catalogTargetName   = catalogTargetName;
    }



    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "OpenMetadataProductCatalog::DigitalProduct::" + identifier + "::" + displayName;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    public String getProductName()
    {
        return productName;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    public String getIdentifier()
    {
        return identifier;
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
     * Return the license that wil lbe granted to data provided through a subscription mechanism.
     *
     * @return license definition
     */
    public ProductGovernanceDefinition getLicense()
    {
        return license;
    }


    /**
     * Return the community that provides the forum to discuss this product.
     *
     * @return community definition
     */
    public ProductCommunityDefinition getCommunity()
    {
        return community;
    }


    /**
     * Return the project manager for the product.
     *
     * @return description of the product manager
     */
    public ProductRoleDefinition getProductManager()
    {
        return productManager;
    }


    /**
     * Return the list of subscription types supported by this product.
     *
     * @return list
     */
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
    public String getDataSpecTableName()
    {
        return dataSpecTableName;
    }


    /**
     * Return the list of identifying data fields to add to the data spec.
     *
     * @return list
     */
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
    public List<ProductDataFieldDefinition> getDataSpecFields()
    {
        if (dataSpecFields != null)
        {
            return new ArrayList<>(Arrays.asList(dataSpecFields));
        }

        return null;
    }


    /**
     * Return the class for the connector provider that supports this product.
     *
     * @return string
     */
    public ConnectorProvider getConnectorProvider()
    {
        return connectorProvider;
    }


    /**
     * Return the catalog target name to use when registering the product asset with the harvester.
     *
     * @return string
     */
    public String getCatalogTargetName()
    {
        return catalogTargetName;
    }


    /**
     * Return the version identifier.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return "V1.0";
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
