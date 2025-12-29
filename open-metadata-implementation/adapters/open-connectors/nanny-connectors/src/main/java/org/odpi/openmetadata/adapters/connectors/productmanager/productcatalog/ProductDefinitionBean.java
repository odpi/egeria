/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;

import org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint.ProductRoleDefinition;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The ProductDefinition describes the products (or templates for products) found in the open metadata product catalog.
 */
public class ProductDefinitionBean implements ProductDefinition
{
    private final String                          typeName;
    private final ProductDefinition[]             productGroups;
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
    private final String                          assetTypeName;
    private final String                          assetIdentifier;
    private final ConnectorProvider               connectorProvider;
    private final String                          catalogTargetName;
    private final Map<String, Object>             configurationProperties;


    /**
     * Construct a product definition.
     *
     * @param typeName name of the type to use - eg DigitalProduct or DigitalProductFamily
     * @param productGroups list of groups that this product belongs to (can be null)
     * @param productName name of the product
     * @param identifier product identifier
     * @param parent folder/product group
     * @param displayName display name
     * @param description description
     * @param category category of product
     * @param license license
     * @param community community
     * @param subscriptionTypes list of subscription types offered
     * @param dataSpecTableName logical name of the tabular data set
     * @param dataSpecIdentifiers list of data fields that form the unique identifier
     * @param dataSpecFields list of other data fields
     * @param assetTypeName type name for the associated product asset
     * @param assetIdentifier identifier for the appropriate asset
     * @param connectorProvider connector provider class (or null)
     * @param configurationProperties configuration properties for the asset's connection
     * @param catalogTargetName catalog target name for the refresh process
     */
    public ProductDefinitionBean(String                          typeName,
                                 ProductDefinition[]             productGroups,
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
                                 Map<String, Object>             configurationProperties,
                                 String                          catalogTargetName)
    {
        this.typeName                = typeName;
        this.productGroups           = productGroups;
        this.productName             = productName;
        this.identifier              = identifier;
        this.parent                  = parent;
        this.displayName             = displayName;
        this.description             = description;
        this.category                = category;
        this.license                 = license;
        this.community               = community;
        this.productManager          = ProductRoleDefinition.PRODUCT_MANAGER;
        this.subscriptionTypes       = subscriptionTypes;
        this.dataSpecTableName       = dataSpecTableName;
        this.dataSpecIdentifiers     = dataSpecIdentifiers;
        this.dataSpecFields          = dataSpecFields;
        this.assetTypeName           = assetTypeName;
        this.assetIdentifier         = assetIdentifier;
        this.connectorProvider       = connectorProvider;
        this.configurationProperties = configurationProperties;
        this.catalogTargetName       = catalogTargetName;
    }



    /**
     * Returns the unique name for the product entity.
     *
     * @return qualified name
     */
    @Override
    public String getQualifiedName()
    {
        return "OpenMetadataProductCatalog::DigitalProduct::" + identifier + "::" + displayName;
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
        if (productGroups != null)
        {
            return new ArrayList<>(Arrays.asList(productGroups));
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
     * Return the entry of the parent folder - null for top level and products that are part of groups.
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
     * Return the license that wil lbe granted to data provided through a subscription mechanism.
     *
     * @return license definition
     */
    @Override
    public ProductGovernanceDefinition getLicense()
    {
        return license;
    }


    /**
     * Return the community that provides the forum to discuss this product.
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
     * Return the class for the connector provider that supports this product.
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
        return configurationProperties;
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
