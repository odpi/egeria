/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;

import org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint.ProductRoleDefinition;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The ProductDefinition describes the products (or templates for products) found in the open metadata product catalog.
 */
public interface ProductDefinition
{
    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    String getQualifiedName();


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    String getTypeName();


    /**
     * Return the list of product groups (if any) that this product belongs to.
     *
     * @return list
     */
    List<ProductDefinition> getProductFamilies();


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    String getProductName();


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    String getIdentifier();


    /**
     * Return the entry of the parent folder - null for top level and products that are part of groups.
     *
     * @return enum
     */
    ProductFolderDefinition getFolder();


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    String getDisplayName();


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    String getDescription();


    /**
     * Returns an optional category for the collection.
     *
     * @return string
     */
    String getCategory();


    /**
     * Return the license that wil lbe granted to data provided through a subscription mechanism.
     *
     * @return license definition
     */
    ProductGovernanceDefinition getLicense();


    /**
     * Return the community that provides the forum to discuss this product.
     *
     * @return community definition
     */
    ProductCommunityDefinition getCommunity();


    /**
     * Return the list of subscription types supported by this product.
     *
     * @return list
     */
    List<ProductSubscriptionDefinition> getSubscriptionTypes();



    /**
     * Return the name pattern for the table name to use for the tabular data set.
     *
     * @return Capitalized, space separated name
     */
    String getDataSpecTableName();


    /**
     * Return the list of identifying data fields to add to the data spec.
     *
     * @return list
     */
    List<ProductDataFieldDefinition> getDataSpecIdentifiers();


    /**
     * Return the list of non-identifying data fields to add to the data spec.
     *
     * @return list
     */
    List<ProductDataFieldDefinition> getDataSpecFields();


    /**
     * Return the type name to use for the product's asset.
     *
     * @return string
     */
    String getAssetTypeName();


    /**
     * Return the identifier to use for the asset.
     *
     * @return string
     */
    String getAssetIdentifier();


    /**
     * Return the class for the connector provider that supports this product.
     *
     * @return string
     */
    ConnectorProvider getConnectorProvider();


    /**
     * Return the catalog target name to use when registering the product asset with the harvester.
     *
     * @return string
     */
    String getCatalogTargetName();


    /**
     * Return the configuration properties - may be null.
     *
     * @return map
     */
    Map<String, Object> getConfigurationProperties();


    /**
     * Return the version identifier.
     *
     * @return string
     */
    String getVersionIdentifier();
}
