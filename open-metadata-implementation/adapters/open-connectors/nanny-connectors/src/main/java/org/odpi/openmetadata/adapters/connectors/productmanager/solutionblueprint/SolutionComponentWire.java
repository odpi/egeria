/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint;

import java.util.List;

/**
 * Define the linkage between solution components defined for Coco Pharmaceuticals.
 * Still experimenting on the usage of the
 */
public enum SolutionComponentWire
{

    MAINTAIN_PRODUCT(ProductSolutionComponent.OPEN_METADATA_HARVESTER,
                     ProductSolutionComponent.OPEN_METADATA_PRODUCT,
                     "maintain catalog entry",
                     "The harvester is responsible for the description of each product in the product catalog."),

    MONITOR_METADATA(ProductSolutionComponent.OPEN_METADATA_HARVESTER,
                     ProductSolutionComponent.METADATA_ACCESS_STORE,
                     "monitor for changes",
                     "The harvester monitors the content of the open metadata repositories looking for possible new products and updates to existing products."),

    QUERY_METADATA(ProductSolutionComponent.OPEN_METADATA_PRODUCT,
                   ProductSolutionComponent.METADATA_ACCESS_STORE,
                   "query metadata",
                   "The open metadata product is implemented as an open metadata query."),

    MONITOR_PRODUCT_CHANGES(ProductSolutionComponent.OPEN_METADATA_WATCHDOG,
                            ProductSolutionComponent.OPEN_METADATA_PRODUCT,
                            "monitor product changes",
                            "Each product has a watchdog to manage the distribution of notifications to subscribers when the product description changes.  These subscribers may be individuals, communities or provisioning pipelines servicing different product consumers."),

    NOTIFY_COMMUNITY(ProductSolutionComponent.OPEN_METADATA_WATCHDOG,
                     ProductSolutionComponent.PRODUCT_COMMUNITY_COMPONENT,
                     "notify when product changes",
                     "Notify the subscribing community when a product changes."),

    INITIATE_PIPELINE(ProductSolutionComponent.OPEN_METADATA_WATCHDOG,
                      ProductSolutionComponent.PROVISIONING_PIPELINE,
                      "initiate when product changes",
                      "Initiate the subscribing pipelines when a product changes.  They push the data changes to downstream product consumers."),

    MAINTAIN_PRODUCT_DELIVERY(ProductSolutionComponent.PROVISIONING_PIPELINE,
                              ProductSolutionComponent.PRODUCT_DELIVERY_LOCATION,
                              "maintain data",
                              "Update the subscriber's copy of the data when a product changes."),

    PRODUCT_CATALOG_SEARCH(ProductSolutionComponent.PRODUCT_CATALOG,
                           ProductSolutionComponent.OPEN_METADATA_PRODUCT,
                           "search for type of data",
                           "The product catalog uses the content of the digital product descriptions to provide candidate products to consumer search requests."),

    READ_SUBSCRIPTION_TYPE(ProductSolutionComponent.NEW_PRODUCT_SUBSCRIPTION,
                           ProductSolutionComponent.OPEN_METADATA_PRODUCT,
                           "read subscription requirements",
                           "When a new subscription is set up, the requirements and details of the pipeline to configure are retrieved from the product description."),

    CONFIGURE_SUBSCRIPTION(ProductSolutionComponent.NEW_PRODUCT_SUBSCRIPTION,
                           ProductSolutionComponent.OPEN_METADATA_WATCHDOG,
                           "configure subscription",
                           "The product's watchdog is configured with details of the new pipeline that supports the subscription."),

    REMOVE_SUBSCRIPTION(ProductSolutionComponent.CANCEL_PRODUCT_SUBSCRIPTION,
                           ProductSolutionComponent.OPEN_METADATA_WATCHDOG,
                           "remove subscription",
                           "The pipeline subscription is removed from the product's watchdog so it is no longer called when the product changes."),

    REMOVE_COPY(ProductSolutionComponent.CANCEL_PRODUCT_SUBSCRIPTION,
                        ProductSolutionComponent.PRODUCT_DELIVERY_LOCATION,
                        "(maybe) remove copy",
                        "Depending on the type of subscription, the subscriber's copy of the product may be removed/archived."),

    ;

    final ProductSolutionComponent component1;
    final ProductSolutionComponent component2;
    final String                   label;
    final String                   description;

    SolutionComponentWire(ProductSolutionComponent component1,
                          ProductSolutionComponent component2,
                          String                   label,
                          String                   description)
    {
        this.component1              = component1;
        this.component2              = component2;
        this.label                   = label;
        this.description             = description;
    }


    public ProductSolutionComponent getComponent1()
    {
        return component1;
    }

    public ProductSolutionComponent getComponent2()
    {
        return component2;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }

    public List<String> getISCQualifiedNames()
    {
        return null;
    }
}
