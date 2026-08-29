/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProductFamilySubscriptionFVT covers the other thing a consumer can subscribe to: a whole product family.
 * <br>
 * A family is a collection of related products, and subscribing to one is a single act that covers every
 * product in it - which is the reason for offering families at all.  What that means in the repository is a
 * subscription to the family with a <em>nested</em> subscription beneath it for each member product, so that
 * the delivery of each product can be managed, and cancelled, in its own right while the consumer only ever
 * took out one subscription.
 * <br>
 * The destination is correspondingly different.  A single product delivers into one table - a tabular data
 * set - but a family delivers a table per product, so its destination is a schema: a tabular data set
 * collection.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProductFamilySubscriptionFVT
{
    private static OpenMetadataStore openMetadataStore;


    /**
     * Build the catalogue once for this class, and create the client the tests read it back through.
     *
     * @throws Exception the catalogue could not be built
     */
    @BeforeAll
    static void buildCatalogue() throws Exception
    {
        SubscriptionFvtTestSupport.ensureCatalogueBuilt();

        openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
    }


    /**
     * Subscribing to a family, in each of the types it offers, produces a subscription that covers every
     * product in the family.
     * <br>
     * The nested subscriptions are the whole point.  A family subscription that produced nothing beneath it
     * would be a subscription to a collection with no way to deliver any of its contents - so the assertion
     * is not merely that a subscription exists, but that one exists for each member product.
     *
     * @param subscriptionType type being subscribed to
     * @throws Exception problem taking out the subscription or reading it back
     */
    @ParameterizedTest(name = "{0}")
    @EnumSource(ProductSubscriptionDefinition.class)
    @DisplayName("Subscribing to a product family covers every product in the family")
    void familySubscriptionCoversEveryProduct(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        ProductDefinition family = ProductCatalogFVT.aProductFamilyWithMembers();

        assertTrue((family.getSubscriptionTypes() != null) && (family.getSubscriptionTypes().contains(subscriptionType)),
                   "Product family " + family.getProductName() + " does not offer a " + subscriptionType.getIdentifier()
                           + " subscription, so this type cannot be tested against it.");

        String destinationGUID  = SubscriptionDriver.catalogueFamilyDestination();
        String subscriptionGUID = SubscriptionDriver.takeOutSubscription(family, subscriptionType, destinationGUID);

        OpenMetadataElement subscription = openMetadataStore.getMetadataElementByGUID(subscriptionGUID);

        assertNotNull(subscription, "The family subscription the process reported creating (" + subscriptionGUID
                              + ") is not in the repository");
        assertEquals(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName,
                     subscription.getType().getTypeName(),
                     "Subscribing to family " + family.getProductName() + " produced a "
                             + subscription.getType().getTypeName() + " rather than a digital subscription");

        /*
         * Each nested subscription is an agreement item in its own right, so what it covers is read the same
         * way a single product subscription's item is.
         */
        List<String> coveredProductQualifiedNames = new ArrayList<>();

        for (RelatedMetadataElement nested : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                            subscriptionGUID,
                                                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                            1))
        {
            for (RelatedMetadataElement item : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                              nested.getElement().getElementGUID(),
                                                                                              OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                                              1))
            {
                coveredProductQualifiedNames.add(SubscriptionFvtTestSupport.getStringProperty(item.getElement(),
                                                                                               OpenMetadataProperty.QUALIFIED_NAME.name));
            }
        }

        List<String> uncoveredProducts = new ArrayList<>();

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            if ((productDefinition.getProductFamilies() != null)
                        && (productDefinition.getProductFamilies().contains(family))
                        && (! coveredProductQualifiedNames.contains(productDefinition.getQualifiedName())))
            {
                uncoveredProducts.add(productDefinition.getQualifiedName());
            }
        }

        assertTrue(uncoveredProducts.isEmpty(),
                   "The " + subscriptionType.getIdentifier() + " subscription to family " + family.getProductName()
                           + " does not cover " + uncoveredProducts.size() + " of its products: " + uncoveredProducts
                           + ".  It covers " + coveredProductQualifiedNames + ".");
    }
}
