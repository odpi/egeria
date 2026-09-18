/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductCommunityDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductGovernanceDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductQuestionDefinition;
import org.odpi.openmetadata.adapters.connectors.subscriptions.ManageDigitalSubscriptionActionTarget;
import org.odpi.openmetadata.adapters.connectors.wedgwood.WedgwoodProvisionSubscriptionActionTarget;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ProductManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ProductManagerHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataSpecProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProductManagerFVT covers the product manager's side of the catalogue: building a digital product through the
 * product manager client rather than having Jacquard harvest it, and giving it the subscription types a consumer
 * can take out.
 * <br>
 * This is the same client the Product Manager OMVS delegates to, and the one Jacquard itself now builds its
 * products and subscription types with - so what is checked here is that a product built this way is, to the
 * rest of the catalogue, indistinguishable from a harvested one: it offers its subscription options the same
 * way, its notification types are handed to the same subscription manager, and a consumer can subscribe to it
 * and have the data arrive.
 * <br>
 * The product's surroundings are borrowed from the catalogue Jacquard builds - its community, license, service
 * level objective and question are Jacquard's - because those are the things a product manager would pick from
 * when defining a product, and creating parallel copies would test nothing extra.  Its data is a real PostgreSQL
 * table that this class fills before the product is created and catalogues from the PostgreSQL content pack's
 * tabular data set template: a subscription delivers, and a product with nothing to deliver would prove only that
 * the metadata was wired.  This is also the one place the suite reads through the PostgreSQL tabular data set
 * connector rather than writing through it.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProductManagerFVT
{
    private static final String PRODUCT_NAME           = SubscriptionFvtTestSupport.TEST_MARKER + " product";
    private static final String PRODUCT_IDENTIFIER     = "PRODUCT-MANAGER-FVT";
    private static final String PRODUCT_QUALIFIED_NAME = OpenMetadataType.DIGITAL_PRODUCT.typeName + "::" + SubscriptionFvtTestSupport.TEST_MARKER + "::product-manager";

    private static final String SOURCE_TABLE_NAME = "product_data";

    /**
     * The subscription types this class gives its product.  Their identifiers are the client's defaults; the
     * intervals are the ones Jacquard's daily and ongoing definitions use.
     */
    private static final long PERIODIC_INTERVAL_MINUTES = 24 * 60;
    private static final long ONGOING_INTERVAL_MINUTES  = 10;

    private static ConnectorContextBase connectorContext;
    private static OpenMetadataStore    openMetadataStore;

    private static String productGUID;
    private static String productManagerRoleGUID;
    private static String communityGUID;
    private static String folderGUID;
    private static String questionGUID;
    private static String sourceAssetGUID;
    private static String licenseTypeGUID;
    private static String serviceLevelObjectiveGUID;
    private static String dataSpecGUID;

    private static String oneTimeProcessGUID;
    private static String periodicProcessGUID;
    private static String ongoingUpdateProcessGUID;


    /**
     * Build the product once for this class.  Jacquard's catalogue is built first because the product borrows
     * from it, and because that is also what starts the Baudot subscription manager that the product's
     * notification types are handed to.
     *
     * @throws Exception the product could not be built
     */
    @BeforeAll
    static void buildProduct() throws Exception
    {
        SubscriptionFvtTestSupport.ensureCatalogueBuilt();

        connectorContext  = ConnectorContextFactory.newContext();
        openMetadataStore = connectorContext.getOpenMetadataStore();

        communityGUID             = jacquardElement(ProductCommunityDefinition.REFERENCE_DATA_SIG.getQualifiedName());
        questionGUID              = jacquardElement(ProductQuestionDefinition.USER_ACCOUNT_STATUS.getQualifiedName());
        licenseTypeGUID           = jacquardElement(ProductGovernanceDefinition.INTERNAL_USE_ONLY.getQualifiedName());
        serviceLevelObjectiveGUID = jacquardElement(ProductGovernanceDefinition.ONE_TIME_SLO.getQualifiedName());

        productManagerRoleGUID = createProductManagerRole();
        folderGUID             = createFolder();
        dataSpecGUID           = createDataSpec();
        sourceAssetGUID        = createSourceData();

        productGUID = createProduct();

        ProductManagerClient productManagerClient = connectorContext.getProductManagerClient();

        oneTimeProcessGUID = productManagerClient.createOneTimeSubscription(productGUID,
                                                                            productManagerClient.getMetadataSourceOptions(),
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            serviceLevelObjectiveGUID);

        periodicProcessGUID = productManagerClient.createPeriodicSubscription(productGUID,
                                                                              productManagerClient.getMetadataSourceOptions(),
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              serviceLevelObjectiveGUID,
                                                                              PERIODIC_INTERVAL_MINUTES);

        ongoingUpdateProcessGUID = productManagerClient.createOngoingUpdateSubscription(productGUID,
                                                                                        productManagerClient.getMetadataSourceOptions(),
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        serviceLevelObjectiveGUID,
                                                                                        List.of(sourceAssetGUID),
                                                                                        ONGOING_INTERVAL_MINUTES);
    }


    /**
     * The product is linked to everything it was created with.  Each link is the relationship the rest of the
     * catalogue expects - the same ones Jacquard creates - which is what lets the subscription types below find
     * the product's owner, license and data from the product alone.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A product created through the product manager client is linked to its surroundings")
    void productIsLinkedToItsSurroundings() throws Exception
    {
        OpenMetadataElement product = openMetadataStore.getMetadataElementByGUID(productGUID);

        assertNotNull(product, "The product the client reported creating (" + productGUID + ") is not in the repository");
        assertEquals(OpenMetadataType.DIGITAL_PRODUCT.typeName, product.getType().getTypeName(), "The product is not a digital product");

        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, 2).contains(productManagerRoleGUID),
                   "The product is not assigned to its product manager role - the subscription types would have no owner to notify.");
        assertTrue(relatedGUIDs(productManagerRoleGUID, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, 1).contains(communityGUID),
                   "The product manager is not the discussion leader of the product's community.");
        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName, 2).contains(communityGUID),
                   "The product's community is not scoped by the product - the community's note log would not receive its notifications.");
        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, 2).contains(folderGUID),
                   "The product is not a member of the folder it was created in.");
        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, 1).contains(sourceAssetGUID),
                   "The product does not hold its asset - a subscription would have no source to deliver from.");
        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName, 1).contains(questionGUID),
                   "The product is not linked to the question it answers.");
        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName, 1).contains(licenseTypeGUID),
                   "The product is not governed by the license its subscribers are granted.");
        assertEquals("subscriber's license",
                     governedByLabel(licenseTypeGUID),
                     "The link from the product to its license does not say that the license is the one a subscriber is granted.");
        assertTrue(relatedGUIDs(productGUID, OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName, 1).contains(dataSpecGUID),
                   "The product is not described by its data specification.");
    }


    /**
     * A subscription type is published the way Jacquard publishes one: a subscribing action process, offered
     * from the product as a resource for creating subscriptions, loaded with everything about the product that
     * does not depend on who is asking - and leaving the consumer to supply exactly the two things that do.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A subscription type is offered from the product as a subscribing action process")
    void subscriptionTypeIsOfferedFromTheProduct() throws Exception
    {
        OpenMetadataElement process = openMetadataStore.getMetadataElementByGUID(oneTimeProcessGUID);

        assertNotNull(process, "The subscription option the client reported creating (" + oneTimeProcessGUID + ") is not in the repository");
        assertEquals(OpenMetadataType.SUBSCRIBING_ACTION_PROCESS.typeName,
                     process.getType().getTypeName(),
                     "The subscription option is a " + process.getType().getTypeName() + " - a consumer looking for the ways to"
                             + " subscribe searches for subscribing action processes and would not find it.");

        assertTrue(subscriptionOfferings().contains(oneTimeProcessGUID),
                   "The one-time subscription option is not offered from the product as a resource for creating subscriptions.");

        Map<String, String> actionTargets = actionTargets(oneTimeProcessGUID);

        assertEquals(productGUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName()),
                     "The subscription option does not name the product as the item being subscribed to.");
        assertEquals(sourceAssetGUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName()),
                     "The subscription option does not name the product's asset as the source of the data.");
        assertEquals(licenseTypeGUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName()),
                     "The subscription option does not carry the license the product is governed by.");
        assertEquals(serviceLevelObjectiveGUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getName()),
                     "The subscription option does not carry the service level objective it was created with.");
        assertEquals(productManagerRoleGUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName()),
                     "The subscription option does not name the product manager as the product's owner.");
        assertEquals(ProductManagerHandler.PROVISION_SUBSCRIPTION_ACTION_TYPE_GUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName()),
                     "The subscription option does not say how a subscription is provisioned.");
        assertEquals(ProductManagerHandler.CANCEL_SUBSCRIPTION_ACTION_TYPE_GUID, actionTargets.get(ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName()),
                     "The subscription option does not say how a subscription is cancelled.");
        assertNotNull(actionTargets.get(ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getName()),
                      "The subscription option has no notification type, so nothing would ever notify its subscribers.");

        /*
         * What is left for the consumer to supply is exactly who they are and where the data should go.
         */
        List<String> remainingActionTargets = remainingSupportedActionTargets(oneTimeProcessGUID);

        assertEquals(List.of(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName(),
                             ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName()).stream().sorted().toList(),
                     remainingActionTargets.stream().sorted().toList(),
                     "The subscription option asks the consumer to supply " + remainingActionTargets + ".  It should ask for"
                             + " exactly the requester and the destination - everything else is known from the product.");
    }


    /**
     * A one-time subscription type notifies once.  Its notification type is handed to the subscription manager
     * and notifies the product manager, so that the owner sees the activity around the product.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A one-time subscription type notifies its subscribers once")
    void oneTimeSubscriptionTypeNotifiesOnce() throws Exception
    {
        String notificationTypeGUID = notificationType(oneTimeProcessGUID);

        NotificationTypeProperties notificationType = notificationTypeProperties(notificationTypeGUID);

        assertFalse(notificationType.getMultipleNotificationsPermitted(),
                    "The one-time subscription type's notification type permits repeated notifications, so an evaluation"
                            + " subscription would have its data delivered again on every refresh.");
        assertEquals(ContentStatus.ACTIVE, notificationType.getContentStatus(),
                     "The notification type is not active, so the subscription manager would send nothing for it.");

        assertTrue(relatedGUIDs(notificationTypeGUID, OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName, 2)
                           .contains(IntegrationConnectorDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGUID()),
                   "The notification type is not a catalog target of the Baudot subscription manager, so nothing is"
                           + " looking after it and its subscribers would never be notified.");

        assertTrue(relatedGUIDs(notificationTypeGUID, OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName, 1).contains(productManagerRoleGUID),
                   "The product manager is not a subscriber of the notification type, so the owner would not see the activity around the product.");

        assertTrue(relatedGUIDs(notificationTypeGUID, OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, 1).isEmpty(),
                   "The one-time subscription type's notification type monitors resources, which would say it notifies on change.");
    }


    /**
     * A periodic subscription type notifies on its interval and watches nothing.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A periodic subscription type notifies its subscribers on its interval")
    void periodicSubscriptionTypeNotifiesOnItsInterval() throws Exception
    {
        String notificationTypeGUID = notificationType(periodicProcessGUID);

        NotificationTypeProperties notificationType = notificationTypeProperties(notificationTypeGUID);

        assertTrue(notificationType.getMultipleNotificationsPermitted(),
                   "The periodic subscription type's notification type permits a single notification, so subscribers"
                           + " would receive the data once and never again.");
        assertEquals(PERIODIC_INTERVAL_MINUTES, notificationType.getMinimumNotificationInterval(),
                     "The periodic subscription type does not carry the interval it was created with.");

        assertTrue(relatedGUIDs(notificationTypeGUID, OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, 1).isEmpty(),
                   "The periodic subscription type's notification type monitors resources, so the subscription manager"
                           + " would treat it as change-driven rather than sending on the interval.");

        assertTrue(subscriptionOfferings().contains(periodicProcessGUID),
                   "The periodic subscription option is not offered from the product.");
    }


    /**
     * An ongoing update subscription type watches the product's data, and notifies no more often than its
     * minimum interval.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("An ongoing update subscription type watches the product's data")
    void ongoingUpdateSubscriptionTypeWatchesTheProductsData() throws Exception
    {
        String notificationTypeGUID = notificationType(ongoingUpdateProcessGUID);

        NotificationTypeProperties notificationType = notificationTypeProperties(notificationTypeGUID);

        assertTrue(notificationType.getMultipleNotificationsPermitted(),
                   "The ongoing update subscription type's notification type permits a single notification.");
        assertEquals(ONGOING_INTERVAL_MINUTES, notificationType.getMinimumNotificationInterval(),
                     "The ongoing update subscription type does not carry the minimum interval it was created with.");

        assertTrue(relatedGUIDs(notificationTypeGUID, OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, 1).contains(sourceAssetGUID),
                   "The ongoing update subscription type's notification type does not monitor the product's asset, so a"
                           + " change to the data would notify nobody.");

        assertTrue(subscriptionOfferings().contains(ongoingUpdateProcessGUID),
                   "The ongoing update subscription option is not offered from the product.");
    }


    /**
     * Asking for a subscription type the product already has reuses it.  Jacquard asks on every refresh, so a
     * client that built a second notification type and process each time would leave the product offering the
     * same subscription many times over, each with its own notification type.
     *
     * @throws Exception problem creating or reading the subscription type
     */
    @Test
    @DisplayName("Asking for a subscription type the product already has reuses it")
    void repeatedSubscriptionTypeIsReused() throws Exception
    {
        ProductManagerClient productManagerClient = connectorContext.getProductManagerClient();

        String notificationTypeBefore = notificationType(oneTimeProcessGUID);

        String processGUID = productManagerClient.createOneTimeSubscription(productGUID,
                                                                            productManagerClient.getMetadataSourceOptions(),
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            serviceLevelObjectiveGUID);

        assertEquals(oneTimeProcessGUID, processGUID,
                     "Asking for the one-time subscription type a second time created a second subscription option.");
        assertEquals(notificationTypeBefore, notificationType(processGUID),
                     "Asking for the one-time subscription type a second time gave it a different notification type.");

        List<String> offerings = subscriptionOfferings();

        assertEquals(3, offerings.size(),
                     "The product offers " + offerings.size() + " subscription options rather than the three subscription types it has: " + offerings);
    }


    /**
     * A consumer can subscribe to the product, the same way as to a harvested one, and the subscription records
     * what it is for.
     *
     * @throws Exception problem taking out the subscription or reading it back
     */
    @Test
    @DisplayName("A consumer can subscribe to a product created through the product manager client")
    void consumerCanSubscribeToTheProduct() throws Exception
    {
        String subscriptionGUID = subscription();

        OpenMetadataElement subscription = openMetadataStore.getMetadataElementByGUID(subscriptionGUID);

        assertNotNull(subscription, "The subscription the process reported creating (" + subscriptionGUID + ") is not in the repository");
        assertEquals(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName,
                     subscription.getType().getTypeName(),
                     "Subscribing to " + PRODUCT_NAME + " produced a " + subscription.getType().getTypeName() + " rather than a digital subscription");

        assertTrue(relatedGUIDs(subscriptionGUID, OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName, 1).contains(productGUID),
                   "The subscription is not linked to the product it was taken out for.");

        /*
         * The provisioning pipeline is what delivers the data, and it is pointed at the product's own asset -
         * the one the product was created with - and at the consumer's destination.
         */
        OpenMetadataElement pipeline = provisioningPipeline(subscriptionGUID);

        assertNotNull(pipeline, "The subscription has no provisioning pipeline, so nothing would ever deliver its data.");

        Map<String, String> pipelineActionTargets = actionTargets(pipeline.getElementGUID());

        assertEquals(sourceAssetGUID, pipelineActionTargets.get(WedgwoodProvisionSubscriptionActionTarget.SOURCE_DATA_SET.getName()),
                     "The provisioning pipeline is not pointed at the product's asset as its source.");
        assertEquals(destination(), pipelineActionTargets.get(WedgwoodProvisionSubscriptionActionTarget.DESTINATION_DATA_SET.getName()),
                     "The provisioning pipeline is not pointed at the destination the subscription was taken out for.");
    }


    /**
     * The product's data arrives at the subscriber's destination.  This is the check that the product built
     * through the client is a working product and not just a well-connected collection: the rows put in the
     * source table before the product was created are read through the product's asset and written to the
     * consumer's table by the pipeline the subscription built.
     *
     * @throws Exception problem taking out the subscription or reading the destination
     */
    @Test
    @DisplayName("The product's data is delivered to the subscriber's destination")
    void productDataIsDeliveredToTheDestination() throws Exception
    {
        subscription();

        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.PRODUCT_MANAGER_DESTINATION_PURPOSE);
        String tableName  = SubscriptionFvtTestSupport.destinationTableName();

        long[] deliveredRows = new long[]{0};

        try (Connection connection = SubscriptionFvtTestSupport.getServerUnderTestConnection(SubscriptionFvtTestSupport.getDatabaseName()))
        {
            SubscriptionFvtTestSupport.waitFor("the one-time subscription to " + PRODUCT_NAME + " delivered its data to "
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
                   "Nothing arrived in " + schemaName + "." + tableName + " for the one-time subscription to " + PRODUCT_NAME
                           + ".  The subscriber has an agreement and no data.");
    }


    /* =====================================================================================================================
     * Building the product
     */

    /**
     * Return one of the elements Jacquard built, by the qualified name its definition gives it.
     *
     * @param qualifiedName qualified name of the element
     * @return unique identifier
     * @throws Exception problem reading the repository
     */
    private static String jacquardElement(String qualifiedName) throws Exception
    {
        OpenMetadataElement element = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(element, "Jacquard has not built " + qualifiedName + ", which this product is defined in terms of.");

        return element.getElementGUID();
    }


    /**
     * Create the role that manages the product.  A product manager is created before the product here, as a
     * product manager choosing from existing roles would; Jacquard creates one per product afterwards.
     *
     * @return unique identifier of the role
     * @throws Exception problem creating the role
     */
    private static String createProductManagerRole() throws Exception
    {
        PersonRoleProperties properties = new PersonRoleProperties();

        properties.setQualifiedName(OpenMetadataType.PERSON_ROLE.typeName + "::" + SubscriptionFvtTestSupport.TEST_MARKER + "::product-manager");
        properties.setDisplayName("Product Manager for " + PRODUCT_NAME);
        properties.setDescription("The role that manages the product the subscription-fvt suite builds through the product manager client.");

        return connectorContext.getActorRoleClient().createActorRole(ownAnchorOptions(), null, properties, null);
    }


    /**
     * Create the folder the product is filed in.
     *
     * @return unique identifier of the folder
     * @throws Exception problem creating the folder
     */
    private static String createFolder() throws Exception
    {
        CollectionFolderProperties properties = new CollectionFolderProperties();

        properties.setQualifiedName(OpenMetadataType.COLLECTION_FOLDER.typeName + "::" + SubscriptionFvtTestSupport.TEST_MARKER + "::product-manager");
        properties.setDisplayName(SubscriptionFvtTestSupport.TEST_MARKER + " products");
        properties.setDescription("The folder holding the products the subscription-fvt suite builds through the product manager client.");

        return connectorContext.getCollectionClient(OpenMetadataType.COLLECTION_FOLDER.typeName).createCollection(ownAnchorOptions(), null, properties, null);
    }


    /**
     * Create the data specification that describes the product's data.
     *
     * @return unique identifier of the data specification
     * @throws Exception problem creating the data specification
     */
    private static String createDataSpec() throws Exception
    {
        DataSpecProperties properties = new DataSpecProperties();

        properties.setQualifiedName(OpenMetadataType.DATA_SPEC_COLLECTION.typeName + "::" + SubscriptionFvtTestSupport.TEST_MARKER + "::product-manager");
        properties.setDisplayName("Data specification for " + PRODUCT_NAME);
        properties.setDescription("The data specification of the product the subscription-fvt suite builds through the product manager client.");

        return connectorContext.getCollectionClient(OpenMetadataType.DATA_SPEC_COLLECTION.typeName).createCollection(ownAnchorOptions(), null, properties, null);
    }


    /**
     * Put some data where the product will deliver it from, and catalogue it as the product's asset.  The
     * schema was created empty when the run started; the table and its rows are this product's own.  The table
     * has a primary key so that the records have the stable order that reading by position relies on, and a
     * repeated delivery replaces rather than duplicates them at the destination.
     *
     * @return unique identifier of the asset
     * @throws Exception problem creating the table or cataloguing it
     */
    private static String createSourceData() throws Exception
    {
        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.PRODUCT_MANAGER_SOURCE_PURPOSE);

        try (Connection connection = SubscriptionFvtTestSupport.getServerUnderTestConnection(SubscriptionFvtTestSupport.getDatabaseName());
             Statement  statement  = connection.createStatement())
        {
            statement.execute("create table if not exists " + schemaName + "." + SOURCE_TABLE_NAME
                                      + " (product_key integer primary key, product_value varchar(80))");

            if (SubscriptionFvtTestSupport.getRowCount(connection, schemaName, SOURCE_TABLE_NAME) == 0)
            {
                statement.execute("insert into " + schemaName + "." + SOURCE_TABLE_NAME + " values (1, 'first'), (2, 'second'), (3, 'third')");
            }
        }

        return SubscriptionDriver.catalogueTabularDataSet(SubscriptionFvtTestSupport.PRODUCT_MANAGER_SOURCE_PURPOSE, SOURCE_TABLE_NAME);
    }


    /**
     * Create the product through the product manager client, linked to everything created and borrowed above.
     *
     * @return unique identifier of the product
     * @throws Exception problem creating the product
     */
    private static String createProduct() throws Exception
    {
        DigitalProductProperties properties = new DigitalProductProperties();

        properties.setQualifiedName(PRODUCT_QUALIFIED_NAME);
        properties.setDisplayName(PRODUCT_NAME);
        properties.setDescription("A product built through the product manager client by the subscription-fvt suite, to show that a product"
                                          + " built this way offers its subscriptions and delivers its data exactly as a harvested one does.");
        properties.setIdentifier(PRODUCT_IDENTIFIER);
        properties.setProductName(PRODUCT_NAME);
        properties.setCategory("Test data");
        properties.setIntroductionDate(new Date());
        properties.setContentStatus(ContentStatus.ACTIVE);
        properties.setDeploymentStatus(DeploymentStatus.ACTIVE);

        return connectorContext.getProductManagerClient().createDigitalProduct(ownAnchorOptions(),
                                                                               null,
                                                                               properties,
                                                                               null,
                                                                               productManagerRoleGUID,
                                                                               communityGUID,
                                                                               List.of(folderGUID),
                                                                               List.of(questionGUID),
                                                                               sourceAssetGUID,
                                                                               List.of(licenseTypeGUID),
                                                                               dataSpecGUID);
    }


    /**
     * Options for an element that is its own anchor.
     *
     * @return new element options
     */
    private static NewElementOptions ownAnchorOptions()
    {
        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        return newElementOptions;
    }


    /* =====================================================================================================================
     * Reading the product back
     */

    /**
     * Return the unique identifiers of the elements related to one element by one type of relationship.
     *
     * @param elementGUID element to start from
     * @param relationshipTypeName type of relationship to follow
     * @param startingAtEnd which end the starting element is at
     * @return unique identifiers, never null
     * @throws Exception problem reading the repository
     */
    private static List<String> relatedGUIDs(String elementGUID,
                                             String relationshipTypeName,
                                             int    startingAtEnd) throws Exception
    {
        List<String> relatedGUIDs = new ArrayList<>();

        for (RelatedMetadataElement relatedElement : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                    elementGUID,
                                                                                                    relationshipTypeName,
                                                                                                    startingAtEnd))
        {
            relatedGUIDs.add(relatedElement.getElement().getElementGUID());
        }

        return relatedGUIDs;
    }


    /**
     * Return the label on the product's link to one of the governance definitions it is governed by.
     *
     * @param governanceDefinitionGUID governance definition at the other end of the link
     * @return label, or null if the product is not governed by that definition or the link has no label
     * @throws Exception problem reading the repository
     */
    private static String governedByLabel(String governanceDefinitionGUID) throws Exception
    {
        for (RelatedMetadataElement governedBy : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                productGUID,
                                                                                                OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                                                1))
        {
            if (governanceDefinitionGUID.equals(governedBy.getElement().getElementGUID()))
            {
                return SubscriptionFvtTestSupport.getRelationshipStringProperty(governedBy, OpenMetadataProperty.LABEL.name);
            }
        }

        return null;
    }


    /**
     * Return the subscription options the product offers - the processes attached to it as resources for
     * creating subscriptions, which is the list a consumer reads.
     *
     * @return unique identifiers of the offered processes
     * @throws Exception problem reading the repository
     */
    private static List<String> subscriptionOfferings() throws Exception
    {
        List<String> offerings = new ArrayList<>();

        for (RelatedMetadataElement resource : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                             productGUID,
                                                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                                             1))
        {
            if (ResourceUse.CREATE_SUBSCRIPTION.getResourceUse().equals(SubscriptionFvtTestSupport.getRelationshipStringProperty(resource, OpenMetadataProperty.RESOURCE_USE.name)))
            {
                offerings.add(resource.getElement().getElementGUID());
            }
        }

        return offerings;
    }


    /**
     * Return the action targets of a process, keyed by action target name.
     *
     * @param processGUID process to ask about
     * @return map of action target name to element unique identifier
     * @throws Exception problem reading the repository
     */
    private static Map<String, String> actionTargets(String processGUID) throws Exception
    {
        Map<String, String> actionTargets = new HashMap<>();

        for (RelatedMetadataElement actionTarget : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                  processGUID,
                                                                                                  OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                                                  1))
        {
            actionTargets.put(SubscriptionFvtTestSupport.getRelationshipStringProperty(actionTarget, OpenMetadataProperty.ACTION_TARGET_NAME.name),
                              actionTarget.getElement().getElementGUID());
        }

        return actionTargets;
    }


    /**
     * Return the names of the action targets a process still asks its caller to supply.
     *
     * @param processGUID process to ask about
     * @return action target names
     * @throws Exception problem reading the repository
     */
    private static List<String> remainingSupportedActionTargets(String processGUID) throws Exception
    {
        List<String> actionTargetNames = new ArrayList<>();

        for (RelatedMetadataElement specificationProperty : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                                           processGUID,
                                                                                                           OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                           1))
        {
            if (SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType()
                                                                 .equals(SubscriptionFvtTestSupport.getRelationshipStringProperty(specificationProperty, OpenMetadataProperty.PROPERTY_NAME.name)))
            {
                actionTargetNames.add(SubscriptionFvtTestSupport.getStringProperty(specificationProperty.getElement(), OpenMetadataProperty.PREFERRED_VALUE.name));
            }
        }

        return actionTargetNames;
    }


    /**
     * Return the notification type behind a subscription option.
     *
     * @param processGUID subscription option
     * @return unique identifier of its notification type
     * @throws Exception problem reading the repository
     */
    private static String notificationType(String processGUID) throws Exception
    {
        String notificationTypeGUID = actionTargets(processGUID).get(ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getName());

        assertNotNull(notificationTypeGUID, "Subscription option " + processGUID + " has no notification type");

        return notificationTypeGUID;
    }


    /**
     * Return the properties of a notification type.
     *
     * @param notificationTypeGUID notification type to read
     * @return its properties
     * @throws Exception problem reading the repository
     */
    private static NotificationTypeProperties notificationTypeProperties(String notificationTypeGUID) throws Exception
    {
        GetOptions getOptions = connectorContext.getGovernanceDefinitionClient().getGetOptions();

        getOptions.setGraphQueryDepth(0);

        OpenMetadataRootElement notificationType = connectorContext.getGovernanceDefinitionClient(OpenMetadataType.NOTIFICATION_TYPE.typeName)
                                                                   .getGovernanceDefinitionByGUID(notificationTypeGUID, getOptions);

        assertNotNull(notificationType, "Notification type " + notificationTypeGUID + " is not in the repository");
        assertTrue(notificationType.getProperties() instanceof NotificationTypeProperties,
                   "Element " + notificationTypeGUID + " is a " + notificationType.getElementHeader().getType().getTypeName() + " rather than a notification type");

        return (NotificationTypeProperties) notificationType.getProperties();
    }


    /* =====================================================================================================================
     * Subscribing to the product
     */

    /**
     * Return the destination the one-time subscription delivers into, cataloguing it the first time.
     *
     * @return unique identifier of the destination asset
     * @throws Exception the destination could not be catalogued
     */
    private static String destination() throws Exception
    {
        return SubscriptionDriver.catalogueTabularDataSet(SubscriptionFvtTestSupport.PRODUCT_MANAGER_DESTINATION_PURPOSE,
                                                          SubscriptionFvtTestSupport.destinationTableName());
    }


    /**
     * Take out - or reuse - the one-time subscription these tests deliver into.  The subscription option is
     * found from the product, as a consumer would find it, and run by its qualified name.
     *
     * @return unique identifier of the subscription
     * @throws Exception the subscription could not be taken out
     */
    private static String subscription() throws Exception
    {
        OpenMetadataElement process = openMetadataStore.getMetadataElementByGUID(oneTimeProcessGUID);

        assertNotNull(process, "The one-time subscription option is not in the repository");

        return SubscriptionDriver.takeOutSubscription(productGUID,
                                                      PRODUCT_NAME,
                                                      SubscriptionFvtTestSupport.getStringProperty(process, OpenMetadataProperty.QUALIFIED_NAME.name),
                                                      ProductManagerHandler.ONE_TIME_SUBSCRIPTION_IDENTIFIER + "-" + PRODUCT_IDENTIFIER,
                                                      destination());
    }


    /**
     * Return the provisioning pipeline built for one subscription.
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
}
