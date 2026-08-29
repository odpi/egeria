/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductSubscriptionDefinition;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.subscriptions.ManageDigitalSubscriptionActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SubscriptionDriver does the things a consumer's tooling would do on their behalf: catalogue somewhere for
 * the data to be delivered, say who is asking, and run the subscription option the product publishes.
 * <br>
 * The destination is a real PostgreSQL object catalogued from the PostgreSQL content pack's own template
 * rather than an element invented here, because that is what a subscription delivers into and a stub would
 * not prove that the subscription could be pointed at anything usable.  The two shapes are not
 * interchangeable: a single product delivers into a <b>tabular data set</b> - one table - and a product
 * family delivers into a <b>tabular data set collection</b> - a schema, with room for one table per product
 * in the family.
 * <br>
 * Everything else the create-subscription service needs is already on the subscription option: Jacquard put
 * the product, its data, its license, its notification type, its owner and its service level objective there
 * when it built the catalogue.  The two action targets supplied here are precisely the two that depend on who
 * is asking, which is why they are the two the consumer has to provide.
 */
class SubscriptionDriver
{
    /**
     * The subscriptions taken out during this run, keyed by product and type - see
     * {@link #takeOutSubscription}.
     */
    private static final java.util.Map<String, String> subscriptionsTakenOut = new java.util.HashMap<>();


    /**
     * Create the destination a single product's subscription delivers into: one table on the PostgreSQL
     * server under test, catalogued as a tabular data set.
     *
     * @param subscriptionType subscription type this destination belongs to - each type gets its own
     * @return unique identifier of the destination asset
     * @throws Exception the destination could not be catalogued
     */
    static String catalogueProductDestination(ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.destinationPurpose(subscriptionType));
        String tableName  = SubscriptionFvtTestSupport.destinationTableName();

        return catalogueDestination(PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_TEMPLATE.getTemplateGUID(),
                                    PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getAssociatedTypeName(),
                                    SubscriptionFvtTestSupport.dataSetTemplatePlaceholders(schemaName, tableName),
                                    SubscriptionFvtTestSupport.tabularDataSetQualifiedName(schemaName, tableName));
    }


    /**
     * Create the destination a product family's subscription delivers into: one schema on the PostgreSQL
     * server under test, catalogued as a tabular data set collection.
     *
     * @return unique identifier of the destination asset
     * @throws Exception the destination could not be catalogued
     */
    static String catalogueFamilyDestination() throws Exception
    {
        String schemaName = SubscriptionFvtTestSupport.destinationSchemaName(SubscriptionFvtTestSupport.FAMILY_DESTINATION_PURPOSE);

        return catalogueDestination(PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getTemplateGUID(),
                                    PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                    SubscriptionFvtTestSupport.dataSetCollectionTemplatePlaceholders(schemaName),
                                    SubscriptionFvtTestSupport.tabularDataSetCollectionQualifiedName(schemaName));
    }


    /**
     * Catalogue one destination from a template, or return the one an earlier test in this run already
     * catalogued.
     * <br>
     * Creating from a template rather than building the asset by hand matters: the template brings the
     * connection, the endpoint and the secrets store reference with it, so what the subscription is pointed
     * at is something the provisioning could actually write to.  The asset is also checked for placeholders
     * that were never substituted, because an unsubstituted placeholder does not fail - it just leaves
     * "~{schemaName}~" sitting where a schema name belongs.
     *
     * @param templateGUID template to create from
     * @param typeName type of asset the template creates
     * @param placeholderValues values for the template's placeholders
     * @param expectedQualifiedName the qualified name the template will build
     * @return unique identifier of the destination asset
     * @throws Exception the destination could not be catalogued
     */
    private static String catalogueDestination(String              templateGUID,
                                               String              typeName,
                                               java.util.Map<String, String> placeholderValues,
                                               String              expectedQualifiedName) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement existing = openMetadataStore.getMetadataElementByUniqueName(expectedQualifiedName,
                                                                                        OpenMetadataProperty.QUALIFIED_NAME.name);

        if (existing != null)
        {
            return existing.getElementGUID();
        }

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        String destinationGUID = openMetadataStore.createMetadataElementFromTemplate(typeName,
                                                                                     templateOptions,
                                                                                     templateGUID,
                                                                                     null,
                                                                                     null,
                                                                                     placeholderValues,
                                                                                     null);

        assertNotNull(destinationGUID, "The " + typeName + " destination was not created from template " + templateGUID);

        OpenMetadataElement destination = openMetadataStore.getMetadataElementByGUID(destinationGUID);

        List<String> survivingPlaceholders = SubscriptionFvtTestSupport.findPlaceholders(expectedQualifiedName,
                                                                                          destination.getElementProperties());

        if (! survivingPlaceholders.isEmpty())
        {
            throw new AssertionError("The destination catalogued from template " + templateGUID + " still carries "
                                             + "unsubstituted placeholder(s) " + survivingPlaceholders
                                             + " - the subscription would be pointed at an asset describing a variable name"
                                             + " rather than a real table.");
        }

        return destinationGUID;
    }


    /**
     * Return the actor this suite subscribes as, creating it the first time it is needed.
     * <br>
     * A subscription is an agreement between a product's owner and somebody, so there has to be a somebody.
     * The consumer here is a person rather than the suite's own userId, because that is what a subscription
     * records - who it is for, not which client made the call.
     *
     * @return unique identifier of the requester
     * @throws Exception the requester could not be created
     */
    static synchronized String subscriptionRequester() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        PropertyHelper    propertyHelper    = new PropertyHelper();

        String qualifiedName = OpenMetadataType.PERSON.typeName + "::" + SubscriptionFvtTestSupport.TEST_MARKER + "::subscriber";

        OpenMetadataElement existing = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                        OpenMetadataProperty.QUALIFIED_NAME.name);

        if (existing != null)
        {
            return existing.getElementGUID();
        }

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         qualifiedName);

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                       "subscription-fvt subscriber");
        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                       "The person the subscription-fvt suite takes out its subscriptions for.");

        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        return openMetadataStore.createMetadataElementInStore(OpenMetadataType.PERSON.typeName,
                                                              newElementOptions,
                                                              null,
                                                              new NewElementProperties(properties),
                                                              null);
    }


    /**
     * Take out one subscription, the way a consumer would: run the subscription option the product publishes,
     * supplying the two things it does not already know - who is subscribing and where the data goes - and
     * wait for the result.
     * <br>
     * The result is looked up in the repository rather than read off the engine action that produced it.  A
     * governance service records what it produced as an output action target, but those are handed to the
     * <em>next</em> step of the process, and taking out a subscription is a one-step process - so there is no
     * next step for them to appear on.  Finding the subscription by following the product's agreement items is
     * also the more honest check: it is how a consumer would find what they had just subscribed to.
     * <br>
     * One subscription is taken out per product and type for the whole run, and shared by every test that
     * needs it.  Subscribing again would work, but it would leave several subscriptions to the same product
     * whose names differed only by a timestamp, and an assertion that found the wrong one would be reporting
     * on a different test's work.
     *
     * @param productDefinition product or family being subscribed to
     * @param subscriptionType type of subscription being taken out
     * @param destinationGUID where the data is to be delivered
     * @return unique identifier of the new digital subscription
     * @throws Exception the subscription could not be taken out
     */
    static synchronized String takeOutSubscription(ProductDefinition             productDefinition,
                                                   ProductSubscriptionDefinition subscriptionType,
                                                   String                        destinationGUID) throws Exception
    {
        String subscriptionKey = productDefinition.getQualifiedName() + "::" + subscriptionType.getIdentifier();

        if (subscriptionsTakenOut.containsKey(subscriptionKey))
        {
            return subscriptionsTakenOut.get(subscriptionKey);
        }

        String processQualifiedName = SubscriptionFvtTestSupport.subscriptionProcessQualifiedName(productDefinition,
                                                                                                   subscriptionType);

        List<NewActionTarget> actionTargets = new ArrayList<>();

        actionTargets.add(newActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName(),
                                          subscriptionRequester()));
        actionTargets.add(newActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName(),
                                          destinationGUID));

        EgeriaOpenGovernanceClient openGovernanceClient = new EgeriaOpenGovernanceClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                                                         OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         SubscriptionFvtTestSupport.MAX_PAGE_SIZE,
                                                                                         null);

        String processInstanceGUID = openGovernanceClient.initiateGovernanceActionProcess(OMAGPlatformExtension.USER_ID,
                                                                                           processQualifiedName,
                                                                                           null,
                                                                                           null,
                                                                                           actionTargets,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           null);

        assertNotNull(processInstanceGUID,
                      "Asking to subscribe to " + productDefinition.getProductName() + " with a "
                              + subscriptionType.getIdentifier() + " started nothing (" + processQualifiedName + ")");

        new EngineActionWaiter().waitForProcess(processInstanceGUID,
                                                subscriptionType.getIdentifier() + " subscription to "
                                                        + productDefinition.getProductName());

        String subscriptionGUID = findSubscription(productDefinition, subscriptionType);

        assertNotNull(subscriptionGUID,
                      "The " + subscriptionType.getIdentifier() + " subscription process for "
                              + productDefinition.getProductName() + " finished, but no digital subscription to that product"
                              + " carrying identifier '" + subscriptionIdentifier(productDefinition, subscriptionType)
                              + "' is in the repository.");

        subscriptionsTakenOut.put(subscriptionKey, subscriptionGUID);

        return subscriptionGUID;
    }


    /**
     * Find the subscription to one product taken out under one subscription type, the way a consumer would:
     * by looking at what agreements the product is an item of.
     *
     * @param productDefinition product or family subscribed to
     * @param subscriptionType type taken out
     * @return unique identifier of the subscription, or null if there is not one
     * @throws Exception problem reading the repository
     */
    static String findSubscription(ProductDefinition             productDefinition,
                                   ProductSubscriptionDefinition subscriptionType) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement product = openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(product, "Product " + productDefinition.getQualifiedName() + " is not in the catalogue");

        String wantedIdentifier = subscriptionIdentifier(productDefinition, subscriptionType);

        for (RelatedMetadataElement agreement : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                               product.getElementGUID(),
                                                                                               OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                                               2))
        {
            if (wantedIdentifier.equals(SubscriptionFvtTestSupport.getStringProperty(agreement.getElement(),
                                                                                      OpenMetadataProperty.IDENTIFIER.name)))
            {
                return agreement.getElement().getElementGUID();
            }
        }

        return null;
    }


    /**
     * Return the identifier the create-subscription service gives a subscription: the subscription type and
     * the product it is for.  Jacquard builds this into the subscription option as a request parameter, so it
     * is what says which of the offered types was actually taken out.
     *
     * @param productDefinition product subscribed to
     * @param subscriptionType type taken out
     * @return subscription identifier
     */
    static String subscriptionIdentifier(ProductDefinition             productDefinition,
                                         ProductSubscriptionDefinition subscriptionType)
    {
        return subscriptionType.getIdentifier() + "-" + productDefinition.getIdentifier();
    }


    /**
     * Build one action target for an initiate request.
     *
     * @param actionTargetName name the governance service looks the target up by
     * @param actionTargetGUID element being supplied
     * @return action target
     */
    private static NewActionTarget newActionTarget(String actionTargetName,
                                                   String actionTargetGUID)
    {
        NewActionTarget actionTarget = new NewActionTarget();

        actionTarget.setActionTargetName(actionTargetName);
        actionTarget.setActionTargetGUID(actionTargetGUID);

        return actionTarget;
    }
}
