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
public enum ProductDefinition
{
    /**
     * Valid Value Set List
     */
    VALID_VALUE_SET_LIST("Valid Value Sets List",
                         "OPEN-METADATA-" + OpenMetadataType.VALID_VALUE_DEFINITION.typeName + "-with-members",
                         ProductFolderDefinition.VALID_VALUE_SETS,
                         "Valid Value Sets List",
                         "A tabular data set where each record describes an open metadata valid value set.",
                         ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                         ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                         ProductCommunityDefinition.REFERENCE_DATA_SIG,
                         new ProductSubscriptionDefinition[]{
                                 ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                 ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                 ProductSubscriptionDefinition.ONGOING_UPDATE},
                         new ProductDataFieldDefinition[]{
                                 ProductDataFieldDefinition.GUID,
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
                         "ValidValueSetsList"),


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
    private final ProductDataFieldDefinition[] dataSpec;
    private final ConnectorProvider            connectorProvider;
    private final String                       catalogTargetName;



    ProductDefinition(String                          productName,
                      String                          identifier,
                      ProductFolderDefinition         parent,
                      String                          displayName,
                      String                          description,
                      String                          category,
                      ProductGovernanceDefinition     license,
                      ProductCommunityDefinition      community,
                      ProductSubscriptionDefinition[] subscriptionTypes,
                      ProductDataFieldDefinition[]    dataSpec,
                      ConnectorProvider               connectorProvider,
                      String                          catalogTargetName)
    {
        this.productName       = productName;
        this.identifier        = identifier;
        this.parent            = parent;
        this.displayName       = displayName;
        this.description       = description;
        this.category          = category;
        this.license           = license;
        this.community         = community;
        this.productManager    = ProductRoleDefinition.PRODUCT_MANAGER;
        this.subscriptionTypes = subscriptionTypes;
        this.dataSpec          = dataSpec;
        this.connectorProvider = connectorProvider;
        this.catalogTargetName = catalogTargetName;
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
     * Return the list of data fields to add to the data spec.
     *
     * @return list
     */
    public List<ProductDataFieldDefinition> getDataSpec()
    {
        if (dataSpec != null)
        {
            return new ArrayList<>(Arrays.asList(dataSpec));
        }

        return null;
    }


    /**
     * Return the class name for the connector provider that supports this product.
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
