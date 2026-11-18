/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SubscriptionCancellationFVT covers the end of a subscription: the consumer cancelling it.
 * <br>
 * The create-subscription service builds a cancellation process for every subscription it creates and lists
 * it among the subscription's resources, so cancelling is a matter of running the process the subscription
 * itself offers.  The cancel-subscription service behind it withdraws the license that was granted to the
 * subscriber's destination and deletes the subscription - and everything anchored to the subscription goes
 * with it: the provisioning pipeline, the cancellation process, and the engine action that is running the
 * cancellation.
 * <br>
 * That last one is what makes cancellation different from every other governance action in this suite.  The
 * service deletes the engine action it is running under, so it has to say how it finished <em>before</em> it
 * deletes the subscription; afterwards there is nothing left to record a completion status on.  It also means
 * the action cannot be followed in the usual way, because once it has done its job it can no longer be read.
 * So this class waits for the subscription to go, and then - as its last act - restores it, to see what state
 * the engine action was in when it went.
 * <br>
 * The whole journey - subscribe, wait for the delivery, cancel, wait for the subscription to go - runs once,
 * before the tests, and each test then checks one consequence of it.  The subscription is to a product the
 * other classes do not subscribe to and delivers into a destination of its own, so cancelling it takes
 * nothing away from the subscriptions they share.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionCancellationFVT
{
    /**
     * The subscription type used here.  An evaluation subscription delivers once, as soon as it is taken out,
     * which gives the cancellation something real to stop and gives this class a point at which the delivery
     * is known to be over.
     */
    private static final ProductSubscriptionDefinition SUBSCRIPTION_TYPE = ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION;

    /**
     * The guard the cancel-subscription service returns when it has cancelled a subscription
     * (ManageAssetGuard.DELETE_COMPLETE).  It is repeated here because the governance action connectors are
     * only on this suite's runtime classpath - the engine host loads them by class name.
     */
    private static final String CANCELLED_GUARD = "delete-complete";

    private static OpenMetadataStore openMetadataStore;
    private static ProductDefinition productDefinition;

    private static String       productGUID;
    private static String       destinationGUID;
    private static String       subscriptionGUID;
    private static String       pipelineGUID;
    private static String       cancellationProcessGUID;
    private static String       cancellationProcessTypeName;
    private static String       cancellationProcessQualifiedName;
    private static List<String> licensesBeforeCancellation;
    private static long         rowsBeforeCancellation;


    /**
     * Take out a subscription, let it deliver, and cancel it.
     *
     * @throws Exception the subscription could not be taken out or cancelled
     */
    @BeforeAll
    static void subscribeThenCancel() throws Exception
    {
        SubscriptionFvtTestSupport.ensureCatalogueBuilt();

        openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        productDefinition = aProductNobodyElseSubscribesTo();

        OpenMetadataElement product = openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(product, "Product " + productDefinition.getQualifiedName() + " is not in the catalogue");

        productGUID = product.getElementGUID();

        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.CANCELLATION_DESTINATION_PURPOSE);
        String tableName  = SubscriptionFvtTestSupport.destinationTableName();

        destinationGUID  = SubscriptionDriver.catalogueTabularDataSet(SubscriptionFvtTestSupport.CANCELLATION_DESTINATION_PURPOSE, tableName);
        subscriptionGUID = SubscriptionDriver.takeOutSubscription(productDefinition, SUBSCRIPTION_TYPE, destinationGUID);

        for (RelatedMetadataElement implementation : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                   subscriptionGUID,
                                                                                                   OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                                                   1))
        {
            pipelineGUID = implementation.getElement().getElementGUID();
        }

        assertNotNull(pipelineGUID,
                      "The " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to " + productDefinition.getProductName()
                              + " has no provisioning pipeline, so there is nothing for a cancellation to stop.");

        /*
         * The delivery is allowed to finish before the subscription is cancelled.  A delivery that is still
         * running when its subscription is deleted loses the engine action it is running under, and that is a
         * different situation from the one tested here.  The lineage is the last thing a delivery records, so
         * it is what says the delivery is over.
         */
        try (Connection connection = SubscriptionFvtTestSupport.getServerUnderTestConnection(SubscriptionFvtTestSupport.getDatabaseName()))
        {
            SubscriptionFvtTestSupport.waitFor("the " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to "
                                                       + productDefinition.getProductName() + " to deliver to " + schemaName
                                                       + "." + tableName + " and record its lineage, before it is cancelled",
                                               "subscription.fvt.provisioning.timeout.seconds",
                                               300,
                                               SubscriptionCancellationFVT::deliveryLineageRecorded);

            rowsBeforeCancellation = SubscriptionFvtTestSupport.getRowCount(connection, schemaName, tableName);
        }

        licensesBeforeCancellation = licensesForSubscription();

        OpenMetadataElement cancellationProcess = SubscriptionDriver.findCancellationProcess(subscriptionGUID);

        if (cancellationProcess != null)
        {
            cancellationProcessGUID     = cancellationProcess.getElementGUID();
            cancellationProcessTypeName = cancellationProcess.getType().getTypeName();
        }

        cancellationProcessQualifiedName = SubscriptionDriver.cancelSubscription(productDefinition, SUBSCRIPTION_TYPE, subscriptionGUID);

        SubscriptionFvtTestSupport.waitFor("the " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to "
                                                   + productDefinition.getProductName() + " (" + subscriptionGUID
                                                   + ") to be removed by its cancellation process "
                                                   + cancellationProcessQualifiedName,
                                           "subscription.fvt.engine.action.timeout.seconds",
                                           300,
                                           () -> ! SubscriptionFvtTestSupport.elementExists(openMetadataStore, subscriptionGUID));
    }


    /**
     * The subscription says how it can be cancelled, and the answer is a process that deletes.
     */
    @Test
    @DisplayName("A subscription offers the process that cancels it")
    void subscriptionOffersItsCancellationProcess()
    {
        assertNotNull(cancellationProcessGUID, "The subscription offered no cancellation process.");

        assertEquals(OpenMetadataType.DELETING_ACTION_PROCESS.typeName,
                     cancellationProcessTypeName,
                     "The subscription's cancellation process is a " + cancellationProcessTypeName
                             + " rather than a deleting action process.");

        assertTrue(cancellationProcessQualifiedName.contains(subscriptionGUID),
                   "The cancellation process " + cancellationProcessQualifiedName + " is not named after the subscription"
                           + " it cancels (" + subscriptionGUID + "), so one subscription's process could be mistaken for"
                           + " another's.");
    }


    /**
     * Cancelling removes the subscription: it can no longer be read, and the product no longer lists it among
     * the agreements it is an item of.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("Cancelling a subscription removes it")
    void cancelledSubscriptionIsRemoved() throws Exception
    {
        assertFalse(SubscriptionFvtTestSupport.elementExists(openMetadataStore, subscriptionGUID),
                    "The cancelled subscription " + subscriptionGUID + " can still be read.");

        assertNull(SubscriptionDriver.findSubscription(productDefinition, SUBSCRIPTION_TYPE),
                   "Product " + productDefinition.getProductName() + " still has a " + SUBSCRIPTION_TYPE.getIdentifier()
                           + " subscription after the only one taken out was cancelled.");
    }


    /**
     * The cancellation says how it finished before it removes the engine action it is running under.
     * <br>
     * The engine action is anchored to the subscription, so deleting the subscription deletes it too.  A
     * service that deleted first and reported afterwards would have nothing to report on: the action would be
     * left, deleted, at whatever status it had while it was running, and nobody looking back at it could tell
     * a cancellation that worked from one that died half-way.
     * <br>
     * Looking back at it takes some doing.  A deleted element whose anchor has also been deleted is not
     * returned by any read - not by a search that asks for deleted elements, and not by a request for its
     * history - because a reader's right to see an element is decided by its anchor, and the anchor cannot be
     * read either.  So the cancellation is reversed: restoring the subscription puts back everything the
     * delete removed, in the state it was in when it went, and the engine action can then be read like any
     * other.  It has to show a finished piece of work.
     * <br>
     * This test runs last, because while it runs the subscription exists again - which is exactly what the
     * other tests in this class say it does not.  It removes the subscription again before it finishes.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @Order(Integer.MAX_VALUE)
    @DisplayName("The cancellation records its completion status before its engine action is deleted")
    void cancellationCompletesBeforeItsEngineActionIsDeleted() throws Exception
    {
        assertTrue(cancellationEngineActions().isEmpty(),
                   "The engine action that ran cancellation process " + cancellationProcessQualifiedName + " can still be"
                           + " found after the cancellation, so it was not deleted with the subscription it is anchored to.");

        try
        {
            openMetadataStore.restoreMetadataElementInStore(subscriptionGUID, new MetadataSourceOptions());

            List<OpenMetadataElement> engineActions = cancellationEngineActions();

            assertEquals(1, engineActions.size(),
                         "One cancellation was asked for, so restoring the subscription should bring back the one engine"
                                 + " action that ran cancellation process " + cancellationProcessQualifiedName + "; found "
                                 + engineActions.size() + ".");

            OpenMetadataElement engineAction   = engineActions.get(0);
            PropertyHelper      propertyHelper = new PropertyHelper();

            String activityStatus = propertyHelper.getEnumPropertySymbolicName("subscription-fvt",
                                                                               OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                               engineAction.getElementProperties(),
                                                                               "cancellationCompletesBeforeItsEngineActionIsDeleted");

            assertTrue(ActivityStatus.COMPLETED.name().equalsIgnoreCase(activityStatus),
                       "The engine action that ran the cancellation (" + engineAction.getElementGUID() + ") was deleted with"
                               + " the subscription while its status was " + activityStatus + " rather than "
                               + ActivityStatus.COMPLETED.name() + ".  The cancel-subscription service has to record its"
                               + " completion status before it deletes the subscription, because the engine action goes with it.");

            List<String> completionGuards = propertyHelper.getStringArrayProperty("subscription-fvt",
                                                                                  OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                                                  engineAction.getElementProperties(),
                                                                                  "cancellationCompletesBeforeItsEngineActionIsDeleted");

            assertTrue((completionGuards != null) && (completionGuards.contains(CANCELLED_GUARD)),
                       "The engine action that ran the cancellation finished with guards " + completionGuards
                               + " rather than '" + CANCELLED_GUARD + "', so it did not report that the subscription was cancelled.");
        }
        finally
        {
            SubscriptionFvtTestSupport.purgeElement(openMetadataStore, subscriptionGUID);
        }
    }


    /**
     * The license granted to the subscriber's destination for this subscription is withdrawn.  A subscriber
     * who has cancelled no longer has the product owner's permission to hold the data, and the record of that
     * permission should not outlive the agreement it came from.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("Cancelling a subscription withdraws the license it granted")
    void cancellationWithdrawsTheLicense() throws Exception
    {
        assertFalse(licensesBeforeCancellation.isEmpty(),
                    "The destination was not licensed for the " + SUBSCRIPTION_TYPE.getIdentifier() + " subscription to "
                            + productDefinition.getProductName() + " before it was cancelled, so there was no license for"
                            + " the cancellation to withdraw.");

        List<String> licensesAfterCancellation = licensesForSubscription();

        assertTrue(licensesAfterCancellation.isEmpty(),
                   "The destination is still licensed for the cancelled subscription, under " + licensesAfterCancellation + ".");
    }


    /**
     * Everything that existed only to serve the subscription goes with it: the pipeline that delivered its
     * data, and the process that cancelled it.  A pipeline left behind would still be a subscriber of the
     * product's notification type, and the subscription manager would go on delivering to somebody who had
     * cancelled.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("Cancelling a subscription removes its provisioning pipeline and its cancellation process")
    void cancellationRemovesTheSubscriptionsProcesses() throws Exception
    {
        assertFalse(SubscriptionFvtTestSupport.elementExists(openMetadataStore, pipelineGUID),
                    "The provisioning pipeline " + pipelineGUID + " outlived the subscription it delivered for, so the"
                            + " subscription manager can still reach it.");

        assertNotNull(cancellationProcessGUID, "The subscription offered no cancellation process.");

        assertFalse(SubscriptionFvtTestSupport.elementExists(openMetadataStore, cancellationProcessGUID),
                    "The cancellation process " + cancellationProcessGUID + " outlived the subscription it cancelled.");
    }


    /**
     * Cancelling takes away the agreement and nothing else.  The product is still in the catalogue for the
     * next consumer, the subscriber's destination is still catalogued, and the data already delivered is
     * still in the subscriber's table - it is theirs to remove.
     *
     * @throws Exception problem reading the repository or the destination
     */
    @Test
    @DisplayName("Cancelling a subscription leaves the product, the destination and the delivered data alone")
    void cancellationLeavesTheProductAndTheDestination() throws Exception
    {
        assertTrue(SubscriptionFvtTestSupport.elementExists(openMetadataStore, productGUID),
                   "Product " + productDefinition.getProductName() + " was removed when a subscription to it was cancelled.");

        assertTrue(SubscriptionFvtTestSupport.elementExists(openMetadataStore, destinationGUID),
                   "The subscriber's destination asset was removed when the subscription delivering to it was cancelled.");

        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.CANCELLATION_DESTINATION_PURPOSE);
        String tableName  = SubscriptionFvtTestSupport.destinationTableName();

        assertTrue(rowsBeforeCancellation > 0,
                   "Nothing had been delivered to " + schemaName + "." + tableName + " when the subscription was cancelled,"
                           + " so there is no delivered data for the cancellation to have left alone.");

        try (Connection connection = SubscriptionFvtTestSupport.getServerUnderTestConnection(SubscriptionFvtTestSupport.getDatabaseName()))
        {
            assertEquals(rowsBeforeCancellation,
                         SubscriptionFvtTestSupport.getRowCount(connection, schemaName, tableName),
                         "The rows already delivered to " + schemaName + "." + tableName + " changed when the subscription"
                                 + " was cancelled.");
        }
    }


    /**
     * Return a product that the other classes in this suite do not subscribe to, and that always has data to
     * deliver.  The list of open metadata data types is both: the others all subscribe to the product
     * {@link ProductCatalogFVT#aProductWithSubscriptions()} hands them, and the data types are defined by
     * Egeria itself, so the list is never empty - unlike, say, the list of reference data sets, which is
     * empty in a repository that holds none.
     *
     * @return product definition
     */
    private static ProductDefinition aProductNobodyElseSubscribesTo()
    {
        ProductDefinition candidate = ProductDefinitionEnum.DATA_TYPES_LIST;

        if (candidate == ProductCatalogFVT.aProductWithSubscriptions())
        {
            throw new AssertionError(candidate.getProductName() + " is now the product the other classes in this suite"
                                             + " subscribe to, so cancelling a subscription to it here would take it away"
                                             + " from them.  Choose a different product for this class.");
        }

        if ((candidate.getSubscriptionTypes() == null) || (! candidate.getSubscriptionTypes().contains(SUBSCRIPTION_TYPE)))
        {
            throw new AssertionError(candidate.getProductName() + " does not offer a " + SUBSCRIPTION_TYPE.getIdentifier()
                                             + " subscription, so this class cannot subscribe to it.");
        }

        return candidate;
    }


    /**
     * Has the delivery recorded its lineage?  The data flows from the product's asset into the pipeline and
     * from the pipeline into the destination; the second of those is written last.
     *
     * @return true once the pipeline has a data flow into the destination
     * @throws Exception problem reading the repository
     */
    private static boolean deliveryLineageRecorded() throws Exception
    {
        for (RelatedMetadataElement dataFlow : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                              pipelineGUID,
                                                                                              OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                              1))
        {
            if (destinationGUID.equals(dataFlow.getElement().getElementGUID()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Return the licenses the destination holds for this subscription.  A license is a relationship from the
     * licensed element to the license type, and the create-subscription service records the subscription it
     * was granted for as the license's identifier - which is how the cancellation knows which one to withdraw.
     *
     * @return qualified names of the license types the destination is licensed under for this subscription
     * @throws Exception problem reading the repository
     */
    private static List<String> licensesForSubscription() throws Exception
    {
        List<String> licenseTypeNames = new ArrayList<>();

        for (RelatedMetadataElement license : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                             destinationGUID,
                                                                                             OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                                                             1))
        {
            if (subscriptionGUID.equals(SubscriptionFvtTestSupport.getRelationshipStringProperty(license,
                                                                                                  OpenMetadataProperty.LICENSE_ID.name)))
            {
                licenseTypeNames.add(SubscriptionFvtTestSupport.getStringProperty(license.getElement(),
                                                                                  OpenMetadataProperty.QUALIFIED_NAME.name));
            }
        }

        return licenseTypeNames;
    }


    /**
     * Return the engine actions that ran the cancellation process, whether or not they have been deleted.
     * <br>
     * They are found by name rather than by following the process, because by the time anybody looks the
     * process has been deleted and so have its relationships.  An engine action is named after the process
     * step it runs, and the cancellation process is named after the subscription, so the process's qualified
     * name picks out exactly the actions that cancelled this subscription.
     *
     * @return engine actions, never null
     * @throws Exception problem reading the repository
     */
    private static List<OpenMetadataElement> cancellationEngineActions() throws Exception
    {
        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(new PropertyHelper().addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              cancellationProcessQualifiedName,
                                                                              PropertyComparisonOperator.LIKE));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.ENGINE_ACTION.typeName);
        queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
        queryOptions.setPageSize(SubscriptionFvtTestSupport.MAX_PAGE_SIZE);
        queryOptions.setForLineage(true);

        List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

        return (found == null) ? new ArrayList<>() : found;
    }
}
