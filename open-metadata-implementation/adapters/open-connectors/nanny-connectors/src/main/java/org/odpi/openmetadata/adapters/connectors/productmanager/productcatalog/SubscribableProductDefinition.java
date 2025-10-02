/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;

import java.util.List;

/**
 * The ProductDefinition describes the products (or templates for products) found in the open metadata product catalog.
 */
public interface SubscribableProductDefinition
{
    /**
     * Returns the unique name for the product entity.
     *
     * @return qualified name
     */
    String getQualifiedName();


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    String getProductName();


    /**
     * Return the list of subscription types supported by this product.
     *
     * @return list
     */
    List<ProductSubscriptionDefinition> getSubscriptionTypes();
}
