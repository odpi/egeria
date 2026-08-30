/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.wedgwood.WedgwoodProvisionSubscriptionActionTarget;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
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
 * SubscriptionProvisioningFVT covers what a subscription is for: the data arriving.
 * <br>
 * Taking out a subscription builds a <b>provisioning pipeline</b> - a process created from the product's
 * provisioning governance action type, anchored to the subscription, holding the product's asset as its source
 * and the consumer's destination as its target.  That pipeline is registered as a subscriber of the product's
 * notification type, so the subscription manager reaches it when there is something to deliver.
 * <br>
 * The tests check the pipeline was built and wired up, and then check the only thing that ultimately matters:
 * that rows appear in the consumer's own PostgreSQL table.  Everything before that is machinery; a
 * subscription that is perfectly recorded and delivers nothing has not done its job.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class SubscriptionProvisioningFVT
{
    /**
     * The subscription type used here.  An evaluation subscription is the one that delivers once, as soon as
     * the subscription is taken out, which is what makes it testable: the daily and weekly types deliver on
     * their interval, and waiting a day for a test to pass is not an option.
     */
    private static final ProductSubscriptionDefinition SUBSCRIPTION_TYPE = ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION;

    private static OpenMetadataStore openMetadataStore;
    private static ProductDefinition productDefinition;


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
        productDefinition = ProductCatalogFVT.aProductWithSubscriptions();
    }


    /**
     * Taking out a subscription builds the pipeline that will deliver its data, pointed at the product's own
     * asset and at the destination the consumer asked for.
     *
     * @throws Exception problem taking out the subscription or reading it back
     */
    @Test
    @DisplayName("A subscription builds a provisioning pipeline from the product to the destination")
    void subscriptionBuildsAProvisioningPipeline() throws Exception
    {
        String subscriptionGUID = subscription();

        OpenMetadataElement pipeline = provisioningPipeline(subscriptionGUID);

        assertNotNull(pipeline,
                      "The " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to " + productDefinition.getProductName()
                              + " has no provisioning pipeline, so nothing would ever deliver its data.");

        assertEquals(OpenMetadataType.PROVISIONING_ACTION_PROCESS.typeName,
                     pipeline.getType().getTypeName(),
                     "The subscription's pipeline is a " + pipeline.getType().getTypeName()
                             + " rather than a provisioning action process");

        String sourceGUID      = pipelineActionTarget(pipeline, WedgwoodProvisionSubscriptionActionTarget.SOURCE_DATA_SET.getName());
        String destinationGUID = pipelineActionTarget(pipeline, WedgwoodProvisionSubscriptionActionTarget.DESTINATION_DATA_SET.getName());

        assertNotNull(sourceGUID, "The provisioning pipeline names no source, so it has nothing to deliver from.");
        assertNotNull(destinationGUID, "The provisioning pipeline names no destination, so it has nowhere to deliver to.");

        assertEquals(SubscriptionDriver.catalogueProductDestination(SUBSCRIPTION_TYPE),
                     destinationGUID,
                     "The provisioning pipeline is pointed at a different destination from the one the subscription"
                             + " was taken out for.");
    }


    /**
     * The pipeline is registered as a subscriber of the product's notification type.  This is the link the
     * subscription manager follows when there is something to deliver - without it the pipeline exists and is
     * never run.
     *
     * @throws Exception problem taking out the subscription or reading it back
     */
    @Test
    @DisplayName("The provisioning pipeline is a subscriber of the product's notification type")
    void provisioningPipelineSubscribesToTheNotificationType() throws Exception
    {
        String subscriptionGUID = subscription();

        OpenMetadataElement pipeline = provisioningPipeline(subscriptionGUID);

        assertNotNull(pipeline, "The subscription has no provisioning pipeline");

        List<String> notificationTypeNames = new ArrayList<>();

        for (RelatedMetadataElement notificationType : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                     pipeline.getElementGUID(),
                                                                                                     OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName,
                                                                                                     2))
        {
            notificationTypeNames.add(SubscriptionFvtTestSupport.getStringProperty(notificationType.getElement(),
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name));
        }

        assertTrue(! notificationTypeNames.isEmpty(),
                   "The provisioning pipeline for the " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to "
                           + productDefinition.getProductName() + " is not a subscriber of any notification type, so the"
                           + " subscription manager has no way to reach it when there is data to deliver.");
    }


    /**
     * The data arrives, without anybody asking for it a second time.
     * <br>
     * This is the assertion the whole suite is building towards, and it deliberately triggers nothing: taking
     * out the subscription is the only action.  The subscription manager notices the new subscriber, runs the
     * pipeline, and the rows appear.  The test reads the consumer's own PostgreSQL table directly rather than
     * asking Egeria what it thinks it delivered, because the question is whether the data is really there.
     *
     * @throws Exception problem taking out the subscription or reading the destination
     */
    @Test
    @DisplayName("The product's data is delivered to the subscriber's destination")
    void productDataIsDeliveredToTheDestination() throws Exception
    {
        subscription();

        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.destinationPurpose(SUBSCRIPTION_TYPE));
        String tableName  = SubscriptionFvtTestSupport.destinationTableName();

        long[] deliveredRows = new long[]{0};

        try (Connection connection = SubscriptionFvtTestSupport.getServerUnderTestConnection(SubscriptionFvtTestSupport.getDatabaseName()))
        {
            SubscriptionFvtTestSupport.waitFor("the " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to "
                                                       + productDefinition.getProductName() + " delivered its data to "
                                                       + schemaName + "." + tableName,
                                               "subscription.fvt.provisioning.timeout.seconds",
                                               300,
                                               () ->
                                               {
                                                   deliveredRows[0] = SubscriptionFvtTestSupport.getRowCount(connection, schemaName, tableName);

                                                   return deliveredRows[0] > 0;
                                               });
        }

        assertTrue(deliveredRows[0] > 0,
                   "Nothing arrived in " + schemaName + "." + tableName + " for the " + SUBSCRIPTION_TYPE.getIdentifier()
                           + " subscription to " + productDefinition.getProductName() + ".  The subscriber has an"
                           + " agreement and no data.");
    }


    /**
     * Take out - or reuse - the subscription these tests deliver into.
     *
     * @return unique identifier of the subscription
     * @throws Exception the subscription could not be taken out
     */
    private static String subscription() throws Exception
    {
        String destinationGUID = SubscriptionDriver.catalogueProductDestination(SUBSCRIPTION_TYPE);

        return SubscriptionDriver.takeOutSubscription(productDefinition, SUBSCRIPTION_TYPE, destinationGUID);
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
