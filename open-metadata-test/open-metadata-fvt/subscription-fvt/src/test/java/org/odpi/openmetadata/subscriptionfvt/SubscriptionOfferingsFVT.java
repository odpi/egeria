/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SubscriptionOfferingsFVT covers the second step of a consumer's journey: having found a product, finding
 * out what subscriptions it offers.
 * <br>
 * A subscription option is published as a <em>subscribing action process</em> - a governance action process
 * that Jacquard has already loaded with everything about the product that does not depend on who is asking:
 * the product itself, its data, its license, its notification type, its owner and its service level
 * objective.  The process is attached to the product by a {@code ResourceList} relationship whose resource
 * use is {@code CreateSubscription}, and that relationship is the list a consumer reads.
 * <br>
 * The remaining specification properties on the process are the other half of the answer: they say what the
 * consumer still has to supply - who is subscribing, and where the data should be delivered.  A subscription
 * option that does not say that cannot be acted on by anybody who did not write it.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class SubscriptionOfferingsFVT
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
     * A product offers one subscription option for each subscription type its definition declares, and each
     * one is a subscribing action process named for the type it takes out.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A digital product offers a subscription option for each type it declares")
    void productOffersEachDeclaredSubscriptionType() throws Exception
    {
        ProductDefinition productDefinition = ProductCatalogFVT.aProductWithSubscriptions();

        List<String> offeredQualifiedNames = subscriptionOfferings(productDefinition);

        List<String> missingTypes = new ArrayList<>();

        for (ProductSubscriptionDefinition subscriptionType : productDefinition.getSubscriptionTypes())
        {
            String expectedQualifiedName = SubscriptionFvtTestSupport.subscriptionProcessQualifiedName(productDefinition,
                                                                                                        subscriptionType);

            if (! offeredQualifiedNames.contains(expectedQualifiedName))
            {
                missingTypes.add(subscriptionType.getIdentifier());
            }
        }

        assertTrue(missingTypes.isEmpty(),
                   "Product " + productDefinition.getProductName() + " declares " + productDefinition.getSubscriptionTypes().size()
                           + " subscription type(s) but offers no way to take out " + missingTypes + ".  Offered: "
                           + offeredQualifiedNames);
    }


    /**
     * Each subscription option is a process the consumer can actually run, of the type the model reserves for
     * subscribing, and carrying the identifier of the subscription type it takes out.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("Each subscription option is a subscribing action process for its type")
    void eachOfferingIsASubscribingProcess() throws Exception
    {
        ProductDefinition productDefinition = ProductCatalogFVT.aProductWithSubscriptions();

        for (ProductSubscriptionDefinition subscriptionType : productDefinition.getSubscriptionTypes())
        {
            String qualifiedName = SubscriptionFvtTestSupport.subscriptionProcessQualifiedName(productDefinition, subscriptionType);

            OpenMetadataElement process = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);

            assertNotNull(process, "No subscription option for " + subscriptionType.getIdentifier() + " on product "
                                  + productDefinition.getProductName() + " (" + qualifiedName + ")");

            assertEquals(OpenMetadataType.SUBSCRIBING_ACTION_PROCESS.typeName,
                         process.getType().getTypeName(),
                         "The " + subscriptionType.getIdentifier() + " option on " + productDefinition.getProductName()
                                 + " is a " + process.getType().getTypeName() + ".  A consumer looking for the ways to"
                                 + " subscribe searches for subscribing action processes and would not find it.");
        }
    }


    /**
     * A product family offers the subscription types its definition declares.
     * <br>
     * This is the family half of the catalogue, and it matters because a subscription to a family covers
     * every product in it - which is the reason to offer families at all.  The family's definition declares
     * subscription types exactly as a single product's does, and the create-subscription service knows what
     * to do with a family when it gets one: it creates a nested subscription for each member.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A product family offers a subscription option for each type it declares")
    void productFamilyOffersEachDeclaredSubscriptionType() throws Exception
    {
        ProductDefinition family = ProductCatalogFVT.aProductFamilyWithMembers();

        if ((family.getSubscriptionTypes() == null) || (family.getSubscriptionTypes().isEmpty()))
        {
            return;
        }

        List<String> offeredQualifiedNames = subscriptionOfferings(family);

        List<String> missingTypes = new ArrayList<>();

        for (ProductSubscriptionDefinition subscriptionType : family.getSubscriptionTypes())
        {
            if (! offeredQualifiedNames.contains(SubscriptionFvtTestSupport.subscriptionProcessQualifiedName(family, subscriptionType)))
            {
                missingTypes.add(subscriptionType.getIdentifier());
            }
        }

        assertTrue(missingTypes.isEmpty(),
                   "Product family " + family.getProductName() + " declares " + family.getSubscriptionTypes().size()
                           + " subscription type(s) but offers no way to take out " + missingTypes + ".  Offered: "
                           + offeredQualifiedNames);
    }


    /**
     * A product that has nothing to deliver offers no subscriptions.
     * <br>
     * A digital product's data is produced by a connector, and a definition that names no connector provider
     * gets no asset - so a subscription to it could create the agreement but never deliver anything.  Twenty
     * of the catalogue's products are in that state today: they describe the data they would carry and have no
     * connector written yet.  Offering a subscription to one of them would be a promise that cannot be kept,
     * which is why the rule is asserted here rather than left as a property of the code that happens to hold.
     * <br>
     * A product <em>family</em> also has no asset and does offer subscriptions - see
     * {@link #productFamilyOffersEachDeclaredSubscriptionType}.  The two look alike and are not: a family's
     * data is its members' data, and its subscription delivers by way of them.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A product with no data to deliver offers no subscriptions")
    void productWithNoAssetOffersNoSubscriptions() throws Exception
    {
        ProductDefinition productWithoutAsset = null;

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            if ((OpenMetadataType.DIGITAL_PRODUCT.typeName.equals(productDefinition.getTypeName()))
                        && (productDefinition.getConnectorProvider() == null)
                        && (productDefinition.getSubscriptionTypes() != null)
                        && (! productDefinition.getSubscriptionTypes().isEmpty()))
            {
                productWithoutAsset = productDefinition;
                break;
            }
        }

        if (productWithoutAsset == null)
        {
            /*
             * Every product now has a connector to produce its data, which is where the catalogue is heading.
             * There is nothing left for this test to check.
             */
            return;
        }

        List<String> offeredQualifiedNames = subscriptionOfferings(productWithoutAsset);

        assertTrue(offeredQualifiedNames.isEmpty(),
                   "Product " + productWithoutAsset.getProductName() + " has no connector to produce its data, so a"
                           + " subscription to it could never deliver anything - but it offers " + offeredQualifiedNames.size()
                           + " subscription option(s): " + offeredQualifiedNames);
    }


    /**
     * Return the qualified names of the subscription options attached to one product or family - the list a
     * consumer reads to find out how it can subscribe.
     *
     * @param productDefinition product or family to ask about
     * @return qualified names of the subscribing action processes offered
     * @throws Exception problem reading the repository
     */
    static List<String> subscriptionOfferings(ProductDefinition productDefinition) throws Exception
    {
        OpenMetadataStore store = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement product = store.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(product, "Product " + productDefinition.getQualifiedName() + " is not in the catalogue");

        List<String> offeredQualifiedNames = new ArrayList<>();

        for (RelatedMetadataElement resource : SubscriptionFvtTestSupport.getRelatedElements(store,
                                                                                             product.getElementGUID(),
                                                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                                             1))
        {
            String resourceUse = SubscriptionFvtTestSupport.getRelationshipStringProperty(resource,
                                                                                           OpenMetadataProperty.RESOURCE_USE.name);

            if (ResourceUse.CREATE_SUBSCRIPTION.getResourceUse().equals(resourceUse))
            {
                offeredQualifiedNames.add(SubscriptionFvtTestSupport.getStringProperty(resource.getElement(),
                                                                                        OpenMetadataProperty.QUALIFIED_NAME.name));
            }
        }

        return offeredQualifiedNames;
    }
}
