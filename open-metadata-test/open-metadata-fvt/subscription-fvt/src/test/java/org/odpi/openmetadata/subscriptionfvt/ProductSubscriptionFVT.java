/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.adapters.connectors.subscriptions.ManageDigitalSubscriptionActionTarget;
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
 * ProductSubscriptionFVT covers the last step of a consumer's journey: subscribing to a product.
 * <br>
 * It runs once for <b>each</b> of the four subscription types the catalogue offers.  They are not variations
 * on a theme: an evaluation subscription delivers once and is not repeated, the daily and weekly ones deliver
 * on a fixed interval, and the ongoing one delivers whenever the source data changes.  Those differences are
 * carried by the notification type behind each option, and a suite that subscribed to only one of them would
 * not notice if the other three had been wired to the wrong one.
 * <br>
 * Each type subscribes into its own destination table, because a subscription is named after the destination
 * it delivers to - sharing one would leave four subscriptions whose names differ only by a timestamp, and a
 * failure would not say which was which.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProductSubscriptionFVT
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
     * Take out one subscription of each type and check what arrived: a digital subscription of the right
     * type, identifying the subscription type it was taken out under, recorded as an agreement between the
     * subscriber and the product they subscribed to.
     *
     * @param subscriptionType type being subscribed to
     * @throws Exception problem taking out the subscription or reading it back
     */
    @ParameterizedTest(name = "{0}")
    @EnumSource(ProductSubscriptionDefinition.class)
    @DisplayName("A consumer can subscribe to a digital product")
    void consumerCanSubscribeToAProduct(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        ProductDefinition productDefinition = ProductCatalogFVT.aProductWithSubscriptions();

        assertTrue(productDefinition.getSubscriptionTypes().contains(subscriptionType),
                   "Product " + productDefinition.getProductName() + " does not offer a " + subscriptionType.getIdentifier()
                           + " subscription, so this type cannot be tested against it.");

        String destinationGUID = SubscriptionDriver.catalogueProductDestination(subscriptionType);
        String subscriptionGUID = SubscriptionDriver.takeOutSubscription(productDefinition, subscriptionType, destinationGUID);

        OpenMetadataElement subscription = openMetadataStore.getMetadataElementByGUID(subscriptionGUID);

        assertNotNull(subscription, "The subscription the process reported creating (" + subscriptionGUID + ") is not in the repository");

        assertEquals(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName,
                     subscription.getType().getTypeName(),
                     "Subscribing to " + productDefinition.getProductName() + " produced a "
                             + subscription.getType().getTypeName() + " rather than a digital subscription");

        /*
         * The identifier is what says which of the four types was taken out.  Jacquard builds it into the
         * subscription option as a request parameter, so an option wired to the wrong notification type would
         * show up here as a subscription carrying somebody else's identifier.
         */
        assertEquals(SubscriptionDriver.subscriptionIdentifier(productDefinition, subscriptionType),
                     SubscriptionFvtTestSupport.getStringProperty(subscription, OpenMetadataProperty.IDENTIFIER.name),
                     "The subscription taken out for " + subscriptionType.getIdentifier() + " does not identify itself as that type");
    }


    /**
     * The subscription records who it is for.  Without this the data would be delivered on nobody's behalf,
     * and there would be no way to find out what an individual has subscribed to.
     *
     * @param subscriptionType type being subscribed to
     * @throws Exception problem taking out the subscription or reading it back
     */
    @ParameterizedTest(name = "{0}")
    @EnumSource(ProductSubscriptionDefinition.class)
    @DisplayName("A subscription records the consumer it was taken out for")
    void subscriptionRecordsItsSubscriber(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        ProductDefinition productDefinition = ProductCatalogFVT.aProductWithSubscriptions();

        String destinationGUID  = SubscriptionDriver.catalogueProductDestination(subscriptionType);
        String subscriptionGUID = SubscriptionDriver.takeOutSubscription(productDefinition, subscriptionType, destinationGUID);
        String requesterGUID    = SubscriptionDriver.subscriptionRequester();

        List<String> actorGUIDs = new ArrayList<>();
        List<String> partyNames = new ArrayList<>();

        for (RelatedMetadataElement actor : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                           subscriptionGUID,
                                                                                           OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName,
                                                                                           1))
        {
            actorGUIDs.add(actor.getElement().getElementGUID());
            partyNames.add(SubscriptionFvtTestSupport.getRelationshipStringProperty(actor,
                                                                                     OpenMetadataProperty.AGREEMENT_PARTY_NAME.name));
        }

        assertTrue(actorGUIDs.contains(requesterGUID),
                   "The " + subscriptionType.getIdentifier() + " subscription to " + productDefinition.getProductName()
                           + " is not linked to the consumer who asked for it.  Its parties are " + partyNames + ".");

        assertTrue(partyNames.contains(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName()),
                   "No party on the " + subscriptionType.getIdentifier() + " subscription is recorded as the requester -"
                           + " the parties are " + partyNames + ", so there is nothing to say which of them is the subscriber.");
    }


    /**
     * The subscription records what it is a subscription to.  This is the link that makes a subscription
     * answerable in both directions: what has this consumer subscribed to, and who has subscribed to this
     * product.
     *
     * @param subscriptionType type being subscribed to
     * @throws Exception problem taking out the subscription or reading it back
     */
    @ParameterizedTest(name = "{0}")
    @EnumSource(ProductSubscriptionDefinition.class)
    @DisplayName("A subscription records the product it is for")
    void subscriptionRecordsItsProduct(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        ProductDefinition productDefinition = ProductCatalogFVT.aProductWithSubscriptions();

        String destinationGUID  = SubscriptionDriver.catalogueProductDestination(subscriptionType);
        String subscriptionGUID = SubscriptionDriver.takeOutSubscription(productDefinition, subscriptionType, destinationGUID);

        OpenMetadataElement product = openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(product, "Product " + productDefinition.getQualifiedName() + " is not in the catalogue");

        List<String> agreementItemGUIDs = new ArrayList<>();

        for (RelatedMetadataElement item : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                          subscriptionGUID,
                                                                                          OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                                          1))
        {
            agreementItemGUIDs.add(item.getElement().getElementGUID());
        }

        assertTrue(agreementItemGUIDs.contains(product.getElementGUID()),
                   "The " + subscriptionType.getIdentifier() + " subscription is not linked to "
                           + productDefinition.getProductName() + ", the product it was taken out for.");
    }
}
