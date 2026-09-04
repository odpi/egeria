/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.adapters.connectors.wedgwood.WedgwoodProvisionSubscriptionActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * ProductFamilySubscriptionFVT covers the other thing a consumer can subscribe to: a whole product family.
 * <br>
 * A family is a collection of related products, and subscribing to one is a single act that covers every
 * product in it - including products added to the family after the subscription was taken out.  The family
 * is subscribed to as one product: one subscription, one provisioning pipeline, whose source is the family's
 * own asset.  That asset is a tabular data set collection whose connector walks the family's members and
 * presents each product's data set as a table, so the pipeline delivers the whole family in one pass.
 * <br>
 * The destination is the other half of the same shape.  A single product delivers into a table - a tabular data
 * set - but a family delivers a table per product, so its destination is a schema: a tabular data set
 * collection.  The suite catalogues one on the PostgreSQL server under test and checks that a table per product
 * appears in it.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProductFamilySubscriptionFVT
{
    /**
     * The subscription type whose delivery is checked.  An evaluation subscription delivers once, as soon as
     * the subscription is taken out, which is what makes it testable within a test's patience.
     */
    private static final ProductSubscriptionDefinition DELIVERING_SUBSCRIPTION_TYPE = ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION;

    private static OpenMetadataStore openMetadataStore;
    private static ProductDefinition family;


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
        family            = ProductCatalogFVT.aProductFamilyWithMembers();
    }


    /**
     * Subscribing to a family, in each of the types it offers, produces exactly one subscription, and it is to
     * the family itself.
     * <br>
     * A family subscription used to be expanded at subscription time into a nested subscription per member, so
     * that a consumer could prune the ones they did not want.  That cost as much as subscribing to every product
     * separately, and a product added to the family later was never included.  So the assertion here is the
     * opposite of what it was: the subscription's agreement item is the family, and nothing is nested beneath it.
     *
     * @param subscriptionType type being subscribed to
     * @throws Exception problem taking out the subscription or reading it back
     */
    @ParameterizedTest(name = "{0}")
    @EnumSource(ProductSubscriptionDefinition.class)
    @DisplayName("Subscribing to a product family is a single subscription to the family")
    void familySubscriptionIsASingleSubscription(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        assertTrue((family.getSubscriptionTypes() != null) && (family.getSubscriptionTypes().contains(subscriptionType)),
                   "Product family " + family.getProductName() + " does not offer a " + subscriptionType.getIdentifier()
                           + " subscription, so this type cannot be tested against it.");

        String subscriptionGUID = subscription(subscriptionType);

        OpenMetadataElement subscription = openMetadataStore.getMetadataElementByGUID(subscriptionGUID);

        assertNotNull(subscription, "The family subscription the process reported creating (" + subscriptionGUID
                              + ") is not in the repository");
        assertEquals(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName,
                     subscription.getType().getTypeName(),
                     "Subscribing to family " + family.getProductName() + " produced a "
                             + subscription.getType().getTypeName() + " rather than a digital subscription");

        List<String> agreementItems = new ArrayList<>();

        for (RelatedMetadataElement item : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                          subscriptionGUID,
                                                                                          OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                                          1))
        {
            agreementItems.add(SubscriptionFvtTestSupport.getStringProperty(item.getElement(), OpenMetadataProperty.QUALIFIED_NAME.name));
        }

        assertEquals(List.of(family.getQualifiedName()),
                     agreementItems,
                     "The " + subscriptionType.getIdentifier() + " subscription to family " + family.getProductName()
                             + " should be an agreement covering the family and nothing else; its agreement items are "
                             + agreementItems);

        List<String> nestedSubscriptions = new ArrayList<>();

        for (RelatedMetadataElement member : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                            subscriptionGUID,
                                                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                            1))
        {
            if (OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName.equals(member.getElement().getType().getTypeName()))
            {
                nestedSubscriptions.add(member.getElement().getElementGUID());
            }
        }

        assertTrue(nestedSubscriptions.isEmpty(),
                   "The " + subscriptionType.getIdentifier() + " subscription to family " + family.getProductName()
                           + " has " + nestedSubscriptions.size() + " subscription(s) nested beneath it.  A family is"
                           + " subscribed to as one product; nothing should be expanded at subscription time.");
    }


    /**
     * The family subscription is provisioned from the family's own asset: the tabular data set collection over
     * its members' data sets.  This is the link that makes the single subscription deliverable - without it a
     * subscription to a collection would have no way to deliver any of its contents.
     *
     * @param subscriptionType type being subscribed to
     * @throws Exception problem taking out the subscription or reading it back
     */
    @ParameterizedTest(name = "{0}")
    @EnumSource(ProductSubscriptionDefinition.class)
    @DisplayName("A product family subscription is provisioned from the family's data set collection")
    void familySubscriptionIsProvisionedFromTheFamilyCollection(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        String subscriptionGUID = subscription(subscriptionType);

        OpenMetadataElement pipeline = provisioningPipeline(subscriptionGUID);

        assertNotNull(pipeline,
                      "The " + subscriptionType.getIdentifier() + " subscription to family " + family.getProductName()
                              + " has no provisioning pipeline, so nothing would ever deliver its data.");

        String sourceGUID = pipelineActionTarget(pipeline, WedgwoodProvisionSubscriptionActionTarget.SOURCE_DATA_SET.getName());

        assertNotNull(sourceGUID, "The family's provisioning pipeline names no source, so it has nothing to deliver from.");

        OpenMetadataElement source = openMetadataStore.getMetadataElementByGUID(sourceGUID);

        assertNotNull(source, "The family's provisioning pipeline names a source (" + sourceGUID + ") that is not in the repository.");
        assertEquals(OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                     source.getType().getTypeName(),
                     "The family's provisioning pipeline is fed from a " + source.getType().getTypeName()
                             + " rather than the tabular data set collection that presents the family's products.");

        assertTrue(familyAssetGUIDs().contains(sourceGUID),
                   "The family's provisioning pipeline is fed from " + sourceGUID + ", which is not an asset of family "
                           + family.getProductName() + ".  The family's assets are " + familyAssetGUIDs());
    }


    /**
     * The family's data arrives as one table per product, without anybody asking for it a second time.
     * <br>
     * Taking out the subscription is the only action.  The subscription manager notices the new subscriber, runs
     * the pipeline, and the pipeline walks the family's collection delivering each product's data set into the
     * destination schema.  The test reads the consumer's own PostgreSQL schema directly, because the question is
     * whether the data is really there - and it counts the products in the family from the repository rather
     * than from the catalogue definitions, because most of this family's products are harvested by Jacquard
     * rather than declared in advance.
     *
     * @throws Exception problem taking out the subscription or reading the destination
     */
    @Test
    @DisplayName("The family's products are delivered as one table each to the subscriber's schema")
    void familyProductsAreDeliveredAsATableEach() throws Exception
    {
        subscription(DELIVERING_SUBSCRIPTION_TYPE);

        int expectedTables = productsWithDataSets().size();

        assertTrue(expectedTables > 0, "Product family " + family.getProductName() + " has no products with a data set, so there is nothing to deliver.");

        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.FAMILY_DESTINATION_PURPOSE);

        List<String> deliveredTables = new ArrayList<>();

        try (Connection connection = SubscriptionFvtTestSupport.getServerUnderTestConnection(SubscriptionFvtTestSupport.getDatabaseName()))
        {
            SubscriptionFvtTestSupport.waitFor("the " + DELIVERING_SUBSCRIPTION_TYPE.getIdentifier() + " subscription to family "
                                                       + family.getProductName() + " delivered all " + expectedTables
                                                       + " of its products' tables to schema " + schemaName,
                                               "subscription.fvt.family.provisioning.timeout.seconds",
                                               1200,
                                               () ->
                                               {
                                                   deliveredTables.clear();
                                                   deliveredTables.addAll(SubscriptionFvtTestSupport.getTableNames(connection, schemaName));

                                                   return deliveredTables.size() >= expectedTables;
                                               });

            assertEquals(expectedTables,
                         deliveredTables.size(),
                         "Family " + family.getProductName() + " has " + expectedTables + " products with a data set but "
                                 + deliveredTables.size() + " tables arrived in " + schemaName + ": " + deliveredTables);

            long deliveredRows = 0;

            for (String tableName : deliveredTables)
            {
                deliveredRows = deliveredRows + SubscriptionFvtTestSupport.getRowCount(connection, schemaName, tableName);
            }

            assertTrue(deliveredRows > 0,
                       "All " + deliveredTables.size() + " tables of family " + family.getProductName() + " arrived in "
                               + schemaName + " but none of them holds a row.");
        }
    }


    /**
     * Take out - or reuse - the subscription to the family of one type, delivering into the family's schema.
     *
     * @param subscriptionType type to take out
     * @return unique identifier of the subscription
     * @throws Exception the subscription could not be taken out
     */
    private static String subscription(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        String destinationGUID = SubscriptionDriver.catalogueFamilyDestination();

        return SubscriptionDriver.takeOutSubscription(family, subscriptionType, destinationGUID);
    }


    /**
     * Return the family's own assets: the members of the family that are assets rather than products.
     *
     * @return unique identifiers of the family's assets
     * @throws Exception problem reading the repository
     */
    private static List<String> familyAssetGUIDs() throws Exception
    {
        List<String> assetGUIDs = new ArrayList<>();

        for (RelatedMetadataElement member : familyMembers())
        {
            if (! isProduct(member.getElement()))
            {
                assetGUIDs.add(member.getElement().getElementGUID());
            }
        }

        return assetGUIDs;
    }


    /**
     * Return the products in the family that have a data set to deliver: the member products that themselves
     * have an asset as a member.
     *
     * @return qualified names of the products
     * @throws Exception problem reading the repository
     */
    private static List<String> productsWithDataSets() throws Exception
    {
        List<String> products = new ArrayList<>();

        for (RelatedMetadataElement member : familyMembers())
        {
            if (isProduct(member.getElement()))
            {
                for (RelatedMetadataElement productMember : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                           member.getElement().getElementGUID(),
                                                                                                           OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                           1))
                {
                    if (! isProduct(productMember.getElement()))
                    {
                        products.add(SubscriptionFvtTestSupport.getStringProperty(member.getElement(), OpenMetadataProperty.QUALIFIED_NAME.name));
                        break;
                    }
                }
            }
        }

        return products;
    }


    /**
     * Return the members of the family.
     *
     * @return related elements
     * @throws Exception problem reading the repository
     */
    private static List<RelatedMetadataElement> familyMembers() throws Exception
    {
        OpenMetadataElement familyElement = openMetadataStore.getMetadataElementByUniqueName(family.getQualifiedName(),
                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(familyElement, "Product family " + family.getQualifiedName() + " is not in the catalogue");

        return SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                             familyElement.getElementGUID(),
                                                             OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                             1);
    }


    /**
     * Is this element a digital product (or product family)?  Everything else that is a member of a product or a
     * family is an asset.
     *
     * @param element element to test
     * @return boolean
     */
    private static boolean isProduct(OpenMetadataElement element)
    {
        if (element.getType().getSuperTypeNames() != null)
        {
            for (String superTypeName : element.getType().getSuperTypeNames())
            {
                if (OpenMetadataType.DIGITAL_PRODUCT.typeName.equals(superTypeName))
                {
                    return true;
                }
            }
        }

        return OpenMetadataType.DIGITAL_PRODUCT.typeName.equals(element.getType().getTypeName());
    }


    /**
     * Return the provisioning pipeline built for one subscription.  It is reached from the subscription by the
     * relationship that records what implements it.
     *
     * @param subscriptionGUID subscription to ask about
     * @return the pipeline, or null if the subscription has none
     * @throws Exception problem reading the repository
     */
    private static OpenMetadataElement provisioningPipeline(String subscriptionGUID) throws Exception
    {
        for (RelatedMetadataElement implementation : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                   subscriptionGUID,
                                                                                                   OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                                                   1))
        {
            return openMetadataStore.getMetadataElementByGUID(implementation.getElement().getElementGUID());
        }

        return null;
    }


    /**
     * Return the element one of the pipeline's action targets names.
     *
     * @param pipeline provisioning pipeline
     * @param actionTargetName name of the action target wanted
     * @return the element's unique identifier, or null if the pipeline has no target by that name
     * @throws Exception problem reading the repository
     */
    private static String pipelineActionTarget(OpenMetadataElement pipeline,
                                               String              actionTargetName) throws Exception
    {
        for (RelatedMetadataElement actionTarget : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                  pipeline.getElementGUID(),
                                                                                                  OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                                                  1))
        {
            if (actionTargetName.equals(SubscriptionFvtTestSupport.getRelationshipStringProperty(actionTarget,
                                                                                                  OpenMetadataProperty.ACTION_TARGET_NAME.name)))
            {
                return actionTarget.getElement().getElementGUID();
            }
        }

        return null;
    }
}
