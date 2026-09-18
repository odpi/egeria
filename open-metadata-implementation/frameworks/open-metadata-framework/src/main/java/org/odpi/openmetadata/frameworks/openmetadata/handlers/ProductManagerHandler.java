/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFAuditCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryPropertiesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.MonitoredResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceDomain;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductManagerHandler builds digital products and their subscription types.  It extends the CollectionHandler
 * (a digital product is a collection) with the compound operations that a product manager performs:
 * <ul>
 *     <li>createDigitalProduct creates the product collection and links it to its product manager, community,
 *     owning collections, guiding questions, product asset, governance definitions and data specification in one
 *     call.</li>
 *     <li>createOneTimeSubscription, createPeriodicSubscription and createOngoingUpdateSubscription each add a
 *     subscription type to a product: the notification type that drives the subscribers' notifications, registered
 *     with the subscription manager, and the governance action process that a subscriber runs to take out a
 *     subscription of that type.</li>
 * </ul>
 * The subscription types follow the pattern established by the Jacquard Digital Product Loom, so that products
 * built through this handler and products harvested by Jacquard are indistinguishable to the subscription
 * manager (Baudot) and the create/provision/cancel subscription governance services.
 */
public class ProductManagerHandler extends CollectionHandler
{
    /**
     * Unique identifier of the Baudot Digital Product Subscription Manager integration connector as defined in the
     * digital products content pack (IntegrationConnectorDefinition.BAUDOT_SUBSCRIPTION_MANAGER).  This is the
     * subscription manager that a product's notification types are registered with when the caller does not name one.
     */
    public static final String DEFAULT_SUBSCRIPTION_MANAGER_GUID = "fed3e17d-6aa0-4959-8af4-a2cbfde1717b";

    /**
     * Name of the catalog target under which a subscription manager receives a notification type
     * (BaudotCatalogTarget.NOTIFICATION_TYPE).
     */
    public static final String SUBSCRIPTION_MANAGER_CATALOG_TARGET_NAME = "notificationType";

    /**
     * Unique identifier of the governance action type that creates a digital subscription
     * (GovernanceActionTypeDefinition.CREATE_SUBSCRIPTION in the digital products content pack).
     */
    public static final String CREATE_SUBSCRIPTION_ACTION_TYPE_GUID = "369e63b9-56b6-4f31-96a2-3dcf26a21ca8";

    /**
     * Unique identifier of the governance action type that provisions a digital subscription
     * (GovernanceActionTypeDefinition.PROVISION_SUBSCRIPTION in the digital products content pack).
     */
    public static final String PROVISION_SUBSCRIPTION_ACTION_TYPE_GUID = "685d48bf-bebc-46da-b10f-ebbdaf79450b";

    /**
     * Unique identifier of the governance action type that cancels a digital subscription
     * (GovernanceActionTypeDefinition.CANCEL_SUBSCRIPTION in the digital products content pack).
     */
    public static final String CANCEL_SUBSCRIPTION_ACTION_TYPE_GUID = "88d9516b-6134-4cfd-bfcc-0e2fcd8dab7f";

    /**
     * Default identifier for a one-time subscription type.
     */
    public static final String ONE_TIME_SUBSCRIPTION_IDENTIFIER = "ONE-TIME-SUBSCRIPTION";

    /**
     * Default identifier for a periodic subscription type.
     */
    public static final String PERIODIC_SUBSCRIPTION_IDENTIFIER = "PERIODIC-SUBSCRIPTION";

    /**
     * Default identifier for an ongoing update subscription type.
     */
    public static final String ONGOING_UPDATE_SUBSCRIPTION_IDENTIFIER = "ONGOING-UPDATE-SUBSCRIPTION";

    /*
     * The request parameters and action targets of the create subscription governance service
     * (ManageDigitalSubscriptionRequestParameter and ManageDigitalSubscriptionActionTarget in the nanny connectors).
     * They are part of that service's contract, and the values here must match the names it looks for.
     */
    private static final String SUBSCRIPTION_NAME_REQUEST_PARAMETER        = "subscriptionType";
    private static final String SUBSCRIPTION_IDENTIFIER_REQUEST_PARAMETER  = "subscriptionTypeIdentifier";
    private static final String SUBSCRIPTION_DESCRIPTION_REQUEST_PARAMETER = "subscriptionDescription";

    private static final String DIGITAL_SUBSCRIPTION_ITEM_ACTION_TARGET   = "digitalSubscriptionItem";
    private static final String DIGITAL_SUBSCRIPTION_SOURCE_ACTION_TARGET = "sourceDataSet";
    private static final String LICENSE_TYPE_ACTION_TARGET                = "licenseType";
    private static final String NOTIFICATION_TYPE_ACTION_TARGET           = "subscriptionManagerNotificationType";
    private static final String DIGITAL_PRODUCT_OWNER_ACTION_TARGET       = "digitalProductOwner";
    private static final String SERVICE_LEVEL_OBJECTIVE_ACTION_TARGET     = "serviceLevelObjective";
    private static final String PROVISIONING_ACTION_TYPE_ACTION_TARGET    = "provisioningGovernanceActionType";
    private static final String CANCELLING_ACTION_TYPE_ACTION_TARGET      = "cancellingGovernanceActionType";

    private final GovernanceDefinitionHandler notificationTypeHandler;


    /**
     * Create a new handler.
     *
     * @param localServerName    name of this server (view server)
     * @param auditLog           logging destination
     * @param localServiceName   local service name
     * @param openMetadataClient access to open metadata
     */
    public ProductManagerHandler(String             localServerName,
                                 AuditLog           auditLog,
                                 String             localServiceName,
                                 OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, localServiceName, openMetadataClient, OpenMetadataType.DIGITAL_PRODUCT.typeName);

        this.notificationTypeHandler = new GovernanceDefinitionHandler(localServerName,
                                                                       auditLog,
                                                                       localServiceName,
                                                                       openMetadataClient,
                                                                       OpenMetadataType.NOTIFICATION_TYPE.typeName);
    }


    /* =====================================================================================================================
     * Digital products
     */

    /**
     * Create a new digital product and link it to the elements that surround it.  The product itself is created as
     * a collection, in the same way as createCollection; the remaining parameters name existing elements that are
     * then linked to the new product.  Each of them is optional.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new product.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param productManagerGUID           unique identifier of the actor role that manages the product; linked as the
     *                                     product's owner (AssignmentScope) and, if a community is supplied, as that
     *                                     community's discussion leader
     * @param productCommunityGUID         unique identifier of the community that discusses the product; the community
     *                                     is scoped by the product (ScopedBy) and its note log receives the product's
     *                                     subscription notifications
     * @param collectionGUIDs              unique identifiers of the collections that the product is a member of -
     *                                     typically the product catalog folders and digital product families it belongs to
     * @param questionGUIDs                unique identifiers of the glossary terms that describe the questions the
     *                                     product answers (SupplementaryProperties)
     * @param productAssetGUID             unique identifier of the asset that holds the product's data; it becomes a
     *                                     member of the product collection and is the source for its subscriptions
     * @param governanceDefinitionGUIDs    unique identifiers of the governance definitions that the product is governed
     *                                     by, such as the license type granted to subscribers
     * @param dataSpecGUID                 unique identifier of the data specification collection that describes the
     *                                     product's data (DataDescription)
     * @return unique identifier of the newly created product
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDigitalProduct(String                                userId,
                                       NewElementOptions                     newElementOptions,
                                       Map<String, ClassificationProperties> initialClassifications,
                                       DigitalProductProperties              properties,
                                       RelationshipProperties                parentRelationshipProperties,
                                       String                                productManagerGUID,
                                       String                                productCommunityGUID,
                                       List<String>                          collectionGUIDs,
                                       List<String>                          questionGUIDs,
                                       String                                productAssetGUID,
                                       List<String>                          governanceDefinitionGUIDs,
                                       String                                dataSpecGUID) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createDigitalProduct";

        String productGUID = super.createNewElement(userId,
                                                    newElementOptions,
                                                    initialClassifications,
                                                    properties,
                                                    parentRelationshipProperties,
                                                    methodName);

        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(newElementOptions);

        if (productManagerGUID != null)
        {
            AssignmentScopeProperties assignmentScopeProperties = new AssignmentScopeProperties();

            assignmentScopeProperties.setAssignmentType(AssignmentType.OWNER.getDisplayName());
            assignmentScopeProperties.setDescription(AssignmentType.OWNER.getDescription());

            super.linkProductManager(userId, productGUID, productManagerGUID, makeAnchorOptions, assignmentScopeProperties);

            if (productCommunityGUID != null)
            {
                /*
                 * The product manager leads the discussion in the product's community.
                 */
                assignmentScopeProperties.setAssignmentType(AssignmentType.DISCUSSION_LEADER.getDisplayName());
                assignmentScopeProperties.setDescription(AssignmentType.DISCUSSION_LEADER.getDescription());

                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                productManagerGUID,
                                                                productCommunityGUID,
                                                                makeAnchorOptions,
                                                                relationshipBuilder.getNewElementProperties(assignmentScopeProperties));
            }
        }

        if (productCommunityGUID != null)
        {
            openMetadataClient.createRelatedElementsInStore(userId,
                                                            OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                            productCommunityGUID,
                                                            productGUID,
                                                            makeAnchorOptions,
                                                            null);
        }

        if (collectionGUIDs != null)
        {
            for (String collectionGUID : collectionGUIDs)
            {
                if (collectionGUID != null)
                {
                    super.addToCollection(userId, collectionGUID, productGUID, makeAnchorOptions, null);
                }
            }
        }

        if (questionGUIDs != null)
        {
            SupplementaryPropertiesProperties supplementaryPropertiesProperties = new SupplementaryPropertiesProperties();

            supplementaryPropertiesProperties.setLabel("Guiding question");
            supplementaryPropertiesProperties.setDescription("This is the type of question that " + this.getProductName(properties) + " is designed to answer.");

            for (String questionGUID : questionGUIDs)
            {
                if (questionGUID != null)
                {
                    openMetadataClient.createRelatedElementsInStore(userId,
                                                                    OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName,
                                                                    productGUID,
                                                                    questionGUID,
                                                                    makeAnchorOptions,
                                                                    relationshipBuilder.getNewElementProperties(supplementaryPropertiesProperties));
                }
            }
        }

        if (productAssetGUID != null)
        {
            CollectionMembershipProperties collectionMembershipProperties = new CollectionMembershipProperties();

            collectionMembershipProperties.setMembershipType("product data set");

            super.addToCollection(userId, productGUID, productAssetGUID, makeAnchorOptions, collectionMembershipProperties);
        }

        if (governanceDefinitionGUIDs != null)
        {
            GetOptions getOptions = new GetOptions(newElementOptions);

            for (String governanceDefinitionGUID : governanceDefinitionGUIDs)
            {
                if (governanceDefinitionGUID != null)
                {
                    /*
                     * A license type is what a subscriber's asset is granted, and the link says so; any other
                     * governance definition is linked without comment.
                     */
                    GovernedByProperties governedByProperties = null;

                    OpenMetadataElement governanceDefinition = openMetadataClient.getMetadataElementByGUID(userId, governanceDefinitionGUID, getOptions);

                    if ((governanceDefinition != null) && (propertyHelper.isTypeOf(governanceDefinition, OpenMetadataType.LICENSE_TYPE.typeName)))
                    {
                        governedByProperties = new GovernedByProperties();

                        governedByProperties.setLabel("subscriber's license");
                        governedByProperties.setDescription("This is the license that a subscriber's asset will be given to access the product data.");
                    }

                    openMetadataClient.createRelatedElementsInStore(userId,
                                                                    OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                    productGUID,
                                                                    governanceDefinitionGUID,
                                                                    makeAnchorOptions,
                                                                    relationshipBuilder.getNewElementProperties(governedByProperties));
                }
            }
        }

        if (dataSpecGUID != null)
        {
            DataDescriptionProperties dataDescriptionProperties = new DataDescriptionProperties();

            dataDescriptionProperties.setLabel("data-specification");
            dataDescriptionProperties.setDescription("Description of the data structure(s) used in this product.  Each data structure is a member of the data specification.");

            super.attachDataDescription(userId, productGUID, dataSpecGUID, makeAnchorOptions, dataDescriptionProperties);
        }

        return productGUID;
    }


    /* =====================================================================================================================
     * Subscription types
     */

    /**
     * Add a one-time subscription type to a product.  A subscriber to this type receives a single notification, and
     * so a single delivery of the product's data.  It is typically used to evaluate a product.
     *
     * @param userId                    userId of user making request
     * @param digitalProductGUID        unique identifier of the product
     * @param metadataSourceOptions     options to control access to open metadata
     * @param subscriptionManagerGUID   unique identifier of the integration connector that notifies the subscribers;
     *                                  null means the Baudot Digital Product Subscription Manager from the digital
     *                                  products content pack
     * @param identifier                identifier of the subscription type; null means ONE-TIME-SUBSCRIPTION
     * @param displayName               display name of the subscription type; null means a default name
     * @param description               description of the subscription type; null means a default description
     * @param licenseTypeGUID           unique identifier of the license type granted to the subscriber's asset;
     *                                  null means the first license type that the product is governed by
     * @param serviceLevelObjectiveGUID unique identifier of the service level objective offered by the subscription;
     *                                  null means the first service level objective that the product is governed by
     * @return unique identifier of the governance action process that creates a subscription of this type
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createOneTimeSubscription(String                userId,
                                            String                digitalProductGUID,
                                            MetadataSourceOptions metadataSourceOptions,
                                            String                subscriptionManagerGUID,
                                            String                identifier,
                                            String                displayName,
                                            String                description,
                                            String                licenseTypeGUID,
                                            String                serviceLevelObjectiveGUID) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "createOneTimeSubscription";

        return this.createSubscriptionType(userId,
                                           digitalProductGUID,
                                           metadataSourceOptions,
                                           subscriptionManagerGUID,
                                           this.defaultValue(identifier, ONE_TIME_SUBSCRIPTION_IDENTIFIER),
                                           this.defaultValue(displayName, "One-time subscription"),
                                           this.defaultValue(description, "This subscription delivers the data to the target destination just once to allow an evaluation of the product data."),
                                           licenseTypeGUID,
                                           serviceLevelObjectiveGUID,
                                           false,
                                           0,
                                           null,
                                           methodName);
    }


    /**
     * Add a periodic subscription type to a product.  A subscriber to this type receives a notification, and so a
     * delivery of the product's data, at regular intervals.
     *
     * @param userId                    userId of user making request
     * @param digitalProductGUID        unique identifier of the product
     * @param metadataSourceOptions     options to control access to open metadata
     * @param subscriptionManagerGUID   unique identifier of the integration connector that notifies the subscribers;
     *                                  null means the Baudot Digital Product Subscription Manager from the digital
     *                                  products content pack
     * @param identifier                identifier of the subscription type; null means PERIODIC-SUBSCRIPTION.  A product
     *                                  that offers more than one periodic subscription type needs a distinct identifier
     *                                  for each
     * @param displayName               display name of the subscription type; null means a default name
     * @param description               description of the subscription type; null means a default description
     * @param licenseTypeGUID           unique identifier of the license type granted to the subscriber's asset;
     *                                  null means the first license type that the product is governed by
     * @param serviceLevelObjectiveGUID unique identifier of the service level objective offered by the subscription;
     *                                  null means the first service level objective that the product is governed by
     * @param notificationInterval      time between notifications in minutes
     * @return unique identifier of the governance action process that creates a subscription of this type
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createPeriodicSubscription(String                userId,
                                             String                digitalProductGUID,
                                             MetadataSourceOptions metadataSourceOptions,
                                             String                subscriptionManagerGUID,
                                             String                identifier,
                                             String                displayName,
                                             String                description,
                                             String                licenseTypeGUID,
                                             String                serviceLevelObjectiveGUID,
                                             long                  notificationInterval) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "createPeriodicSubscription";

        return this.createSubscriptionType(userId,
                                           digitalProductGUID,
                                           metadataSourceOptions,
                                           subscriptionManagerGUID,
                                           this.defaultValue(identifier, PERIODIC_SUBSCRIPTION_IDENTIFIER),
                                           this.defaultValue(displayName, "Periodic subscription"),
                                           this.defaultValue(description, "This subscription delivers the data to the target destination every " + notificationInterval + " minutes."),
                                           licenseTypeGUID,
                                           serviceLevelObjectiveGUID,
                                           true,
                                           notificationInterval,
                                           null,
                                           methodName);
    }


    /**
     * Add an ongoing update subscription type to a product.  A subscriber to this type receives a notification, and
     * so a delivery of the product's data, whenever one of the monitored resources changes - but no more often than
     * the minimum notification interval.
     *
     * @param userId                      userId of user making request
     * @param digitalProductGUID          unique identifier of the product
     * @param metadataSourceOptions       options to control access to open metadata
     * @param subscriptionManagerGUID     unique identifier of the integration connector that notifies the subscribers;
     *                                    null means the Baudot Digital Product Subscription Manager from the digital
     *                                    products content pack
     * @param identifier                  identifier of the subscription type; null means ONGOING-UPDATE-SUBSCRIPTION
     * @param displayName                 display name of the subscription type; null means a default name
     * @param description                 description of the subscription type; null means a default description
     * @param licenseTypeGUID             unique identifier of the license type granted to the subscriber's asset;
     *                                    null means the first license type that the product is governed by
     * @param serviceLevelObjectiveGUID   unique identifier of the service level objective offered by the subscription;
     *                                    null means the first service level objective that the product is governed by
     * @param monitoredResourceGUIDs      unique identifiers of the metadata elements whose changes trigger a notification -
     *                                    typically the product's asset
     * @param minimumNotificationInterval minimum time between notifications in minutes; 0 means a notification for
     *                                    every change
     * @return unique identifier of the governance action process that creates a subscription of this type
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createOngoingUpdateSubscription(String                userId,
                                                  String                digitalProductGUID,
                                                  MetadataSourceOptions metadataSourceOptions,
                                                  String                subscriptionManagerGUID,
                                                  String                identifier,
                                                  String                displayName,
                                                  String                description,
                                                  String                licenseTypeGUID,
                                                  String                serviceLevelObjectiveGUID,
                                                  List<String>          monitoredResourceGUIDs,
                                                  long                  minimumNotificationInterval) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName                       = "createOngoingUpdateSubscription";
        final String monitoredResourcesParameterName  = "monitoredResourceGUIDs";

        propertyHelper.validateObject(monitoredResourceGUIDs, monitoredResourcesParameterName, methodName);

        return this.createSubscriptionType(userId,
                                           digitalProductGUID,
                                           metadataSourceOptions,
                                           subscriptionManagerGUID,
                                           this.defaultValue(identifier, ONGOING_UPDATE_SUBSCRIPTION_IDENTIFIER),
                                           this.defaultValue(displayName, "Ongoing update subscription"),
                                           this.defaultValue(description, "This subscription delivers data updates to the target destination whenever the product's data changes."),
                                           licenseTypeGUID,
                                           serviceLevelObjectiveGUID,
                                           true,
                                           minimumNotificationInterval,
                                           monitoredResourceGUIDs,
                                           methodName);
    }


    /**
     * Return the subscription manager to register the notification type with.  A subscription manager named by
     * the caller is used as given - an unknown one fails the request.  When none is named, the default is the
     * Baudot Subscription Manager from the digital products content pack, and that content pack may not be
     * loaded: rather than fail the whole subscription type for a manager the caller never asked for, the
     * default is checked and null is returned if it is not defined, so that the notification type is still
     * created and the missing manager is reported.
     *
     * @param userId calling user
     * @param suppliedSubscriptionManagerGUID subscription manager named by the caller (may be null)
     * @param metadataSourceOptions options to control access to open metadata
     * @param methodName calling method
     * @return unique identifier of the subscription manager, or null if the default is not defined
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getSubscriptionManagerGUID(String                userId,
                                              String                suppliedSubscriptionManagerGUID,
                                              MetadataSourceOptions metadataSourceOptions,
                                              String                methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        if (suppliedSubscriptionManagerGUID != null)
        {
            return suppliedSubscriptionManagerGUID;
        }

        try
        {
            OpenMetadataElement subscriptionManager = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                                  DEFAULT_SUBSCRIPTION_MANAGER_GUID,
                                                                                                  new GetOptions(metadataSourceOptions));

            if (subscriptionManager != null)
            {
                return DEFAULT_SUBSCRIPTION_MANAGER_GUID;
            }
        }
        catch (InvalidParameterException notFound)
        {
            /*
             * The default subscription manager is not defined - reported below, once the notification type exists.
             */
        }

        return null;
    }


    /**
     * Return the supplied value, or the default if the supplied value is null.
     *
     * @param suppliedValue value from the caller
     * @param defaultValue value to use when the caller supplied none
     * @return value
     */
    private String defaultValue(String suppliedValue,
                                String defaultValue)
    {
        if (suppliedValue == null)
        {
            return defaultValue;
        }

        return suppliedValue;
    }


    /**
     * Return the name of a product from its properties.  The product name is preferred, then the display name,
     * then the qualified name.
     *
     * @param productProperties properties of the product
     * @return name
     */
    private String getProductName(DigitalProductProperties productProperties)
    {
        if (productProperties.getProductName() != null)
        {
            return productProperties.getProductName();
        }
        else if (productProperties.getDisplayName() != null)
        {
            return productProperties.getDisplayName();
        }

        return productProperties.getQualifiedName();
    }


    /**
     * Return the unique identifier of the first related element of the requested type, or null if there is none.
     *
     * @param relatedElements related elements of the product
     * @param typeName the type that the element must be (or be a subtype of)
     * @return unique identifier or null
     */
    private String getFirstRelatedElementGUID(List<RelatedMetadataElementSummary> relatedElements,
                                              String                              typeName)
    {
        if (relatedElements != null)
        {
            for (RelatedMetadataElementSummary relatedElement : relatedElements)
            {
                if ((relatedElement != null) &&
                        (relatedElement.getRelatedElement() != null) &&
                        (propertyHelper.isTypeOf(relatedElement.getRelatedElement().getElementHeader(), typeName)))
                {
                    return relatedElement.getRelatedElement().getElementHeader().getGUID();
                }
            }
        }

        return null;
    }


    /**
     * The elements that surround a product and are needed to build one of its subscription types.
     *
     * @param productName name of the product
     * @param productIdentifier identifier of the product
     * @param productAssetGUID unique identifier of the asset that holds the product's data (may be null)
     * @param licenseTypeGUID unique identifier of the license granted to subscribers (may be null)
     * @param serviceLevelObjectiveGUID unique identifier of the service level objective (may be null)
     * @param productManagerGUID unique identifier of the product manager's role (may be null)
     * @param communityNoteLogGUID unique identifier of the product community's note log (may be null)
     */
    private record ProductContext(String productName,
                                  String productIdentifier,
                                  String productAssetGUID,
                                  String licenseTypeGUID,
                                  String serviceLevelObjectiveGUID,
                                  String productManagerGUID,
                                  String communityNoteLogGUID)
    {
    }


    /**
     * Retrieve the product and the elements around it that the subscription type is built from.
     *
     * @param userId calling user
     * @param digitalProductGUID unique identifier of the product
     * @param metadataSourceOptions options to control access to open metadata
     * @param suppliedLicenseTypeGUID license type named by the caller (may be null)
     * @param suppliedServiceLevelObjectiveGUID service level objective named by the caller (may be null)
     * @param methodName calling method
     * @return product context
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private ProductContext getProductContext(String                userId,
                                             String                digitalProductGUID,
                                             MetadataSourceOptions metadataSourceOptions,
                                             String                suppliedLicenseTypeGUID,
                                             String                suppliedServiceLevelObjectiveGUID,
                                             String                methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String productPropertiesParameterName = "digitalProduct.properties";

        /*
         * Only the relationships that the subscription type is built from are retrieved.  A product's full graph
         * includes every notification type and governance action process already attached to it.
         */
        GetOptions getOptions = new GetOptions(metadataSourceOptions);

        getOptions.setGraphQueryDepth(1);
        getOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                       OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                       OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                       OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName));

        OpenMetadataRootElement product = super.getRootElementByGUID(userId, digitalProductGUID, getOptions, methodName);

        if (! (product.getProperties() instanceof DigitalProductProperties productProperties))
        {
            throw new InvalidParameterException(OMFErrorCode.WRONG_TYPE_FOR_ELEMENT.getMessageDefinition(digitalProductGUID,
                                                                                               product.getElementHeader().getType().getTypeName(),
                                                                                               OpenMetadataType.DIGITAL_PRODUCT.typeName),
                                                this.getClass().getName(),
                                                methodName,
                                                productPropertiesParameterName);
        }

        String productName       = this.getProductName(productProperties);
        String productIdentifier = this.defaultValue(productProperties.getIdentifier(), productName);

        String licenseTypeGUID = this.defaultValue(suppliedLicenseTypeGUID,
                                                   this.getFirstRelatedElementGUID(product.getGovernedBy(), OpenMetadataType.LICENSE_TYPE.typeName));

        String serviceLevelObjectiveGUID = this.defaultValue(suppliedServiceLevelObjectiveGUID,
                                                             this.getFirstRelatedElementGUID(product.getGovernedBy(), OpenMetadataType.SERVICE_LEVEL_OBJECTIVE.typeName));

        String productAssetGUID   = this.getFirstRelatedElementGUID(product.getCollectionMembers(), OpenMetadataType.ASSET.typeName);
        String productManagerGUID = this.getFirstRelatedElementGUID(product.getAssignedActors(), OpenMetadataType.ACTOR.typeName);

        /*
         * The community's note log receives the notifications so that the community can see the activity around
         * the product.
         */
        String communityNoteLogGUID = null;
        String communityGUID        = this.getFirstRelatedElementGUID(product.getScopedElements(), OpenMetadataType.COMMUNITY.typeName);

        if (communityGUID != null)
        {
            RelatedMetadataElementList noteLogs = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                communityGUID,
                                                                                                1,
                                                                                                OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP.typeName,
                                                                                                new QueryOptions(metadataSourceOptions));

            if ((noteLogs != null) && (noteLogs.getElementList() != null))
            {
                for (RelatedMetadataElement noteLog : noteLogs.getElementList())
                {
                    if ((noteLog != null) && (noteLog.getElement() != null))
                    {
                        communityNoteLogGUID = noteLog.getElement().getElementGUID();
                        break;
                    }
                }
            }
        }

        return new ProductContext(productName,
                                  productIdentifier,
                                  productAssetGUID,
                                  licenseTypeGUID,
                                  serviceLevelObjectiveGUID,
                                  productManagerGUID,
                                  communityNoteLogGUID);
    }


    /**
     * Set up a subscription type for a product.  This is a notification type, registered with the subscription
     * manager, that drives the notifications to the subscribers; and a governance action process, configured from
     * the create subscription governance action type, that a subscriber runs to take out a subscription of this type.
     * Both are anchored to the product.  Either may already exist, in which case it is reused.
     *
     * @param userId calling user
     * @param digitalProductGUID unique identifier of the product
     * @param metadataSourceOptions options to control access to open metadata
     * @param suppliedSubscriptionManagerGUID subscription manager named by the caller (may be null)
     * @param identifier identifier of the subscription type
     * @param displayName display name of the subscription type
     * @param description description of the subscription type
     * @param suppliedLicenseTypeGUID license type named by the caller (may be null)
     * @param suppliedServiceLevelObjectiveGUID service level objective named by the caller (may be null)
     * @param multipleNotificationsPermitted false for a single notification per subscriber
     * @param minimumNotificationInterval minimum minutes between notifications
     * @param monitoredResourceGUIDs elements whose changes trigger a notification (may be null)
     * @param methodName calling method
     * @return unique identifier of the governance action process
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String createSubscriptionType(String                userId,
                                          String                digitalProductGUID,
                                          MetadataSourceOptions metadataSourceOptions,
                                          String                suppliedSubscriptionManagerGUID,
                                          String                identifier,
                                          String                displayName,
                                          String                description,
                                          String                suppliedLicenseTypeGUID,
                                          String                suppliedServiceLevelObjectiveGUID,
                                          boolean               multipleNotificationsPermitted,
                                          long                  minimumNotificationInterval,
                                          List<String>          monitoredResourceGUIDs,
                                          String                methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String guidParameterName       = "digitalProductGUID";
        final String identifierParameterName = "identifier";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalProductGUID, guidParameterName, methodName);
        propertyHelper.validateMandatoryName(identifier, identifierParameterName, methodName);

        if (metadataSourceOptions == null)
        {
            metadataSourceOptions = new MetadataSourceOptions();
        }

        String subscriptionManagerGUID = this.getSubscriptionManagerGUID(userId, suppliedSubscriptionManagerGUID, metadataSourceOptions, methodName);

        ProductContext productContext = this.getProductContext(userId,
                                                               digitalProductGUID,
                                                               metadataSourceOptions,
                                                               suppliedLicenseTypeGUID,
                                                               suppliedServiceLevelObjectiveGUID,
                                                               methodName);

        String notificationTypeGUID = this.addNotificationType(userId,
                                                               digitalProductGUID,
                                                               metadataSourceOptions,
                                                               productContext,
                                                               identifier,
                                                               displayName,
                                                               description,
                                                               multipleNotificationsPermitted,
                                                               minimumNotificationInterval,
                                                               monitoredResourceGUIDs,
                                                               subscriptionManagerGUID,
                                                               methodName);

        return this.addSubscriptionGovernanceActionProcess(userId,
                                                           digitalProductGUID,
                                                           metadataSourceOptions,
                                                           productContext,
                                                           identifier,
                                                           displayName,
                                                           description,
                                                           notificationTypeGUID,
                                                           methodName);
    }


    /**
     * Set up the notification type for a subscription type.  The notification type is anchored to the product.
     * If it already exists, its notification pattern is brought into line with the request and any monitored
     * resources not yet linked are added.  Either way it is registered with the subscription manager.
     *
     * @param userId calling user
     * @param digitalProductGUID unique identifier of the product
     * @param metadataSourceOptions options to control access to open metadata
     * @param productContext the elements around the product
     * @param identifier identifier of the subscription type
     * @param displayName display name of the subscription type
     * @param description description of the subscription type
     * @param multipleNotificationsPermitted false for a single notification per subscriber
     * @param minimumNotificationInterval minimum minutes between notifications
     * @param monitoredResourceGUIDs elements whose changes trigger a notification (may be null)
     * @param subscriptionManagerGUID integration connector that notifies the subscribers
     * @param methodName calling method
     * @return unique identifier of the notification type
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String addNotificationType(String                userId,
                                       String                digitalProductGUID,
                                       MetadataSourceOptions metadataSourceOptions,
                                       ProductContext        productContext,
                                       String                identifier,
                                       String                displayName,
                                       String                description,
                                       boolean               multipleNotificationsPermitted,
                                       long                  minimumNotificationInterval,
                                       List<String>          monitoredResourceGUIDs,
                                       String                subscriptionManagerGUID,
                                       String                methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(metadataSourceOptions);

        NotificationTypeProperties notificationTypeProperties = new NotificationTypeProperties();

        notificationTypeProperties.setQualifiedName(OpenMetadataType.NOTIFICATION_TYPE.typeName + "::" + digitalProductGUID + "::" + productContext.productName() + "::" + identifier);
        notificationTypeProperties.setIdentifier(identifier);
        notificationTypeProperties.setDisplayName("Notification type for " + displayName + " for product " + productContext.productName());
        notificationTypeProperties.setDescription(description);
        notificationTypeProperties.setDomainIdentifier(GovernanceDomain.DATA_SHARING.getOrdinal());
        notificationTypeProperties.setPlannedStartDate(new Date());
        notificationTypeProperties.setMultipleNotificationsPermitted(multipleNotificationsPermitted);
        notificationTypeProperties.setMinimumNotificationInterval(minimumNotificationInterval);

        /*
         * A notification type only notifies while it is ACTIVE: the subscription manager sends nothing for a
         * notification type in any other content status.
         */
        notificationTypeProperties.setContentStatus(ContentStatus.ACTIVE);

        GetOptions getOptions = new GetOptions(metadataSourceOptions);

        getOptions.setGraphQueryDepth(0);

        OpenMetadataRootElement notificationTypeElement = notificationTypeHandler.getRootElementByUniqueName(userId,
                                                                                                             notificationTypeProperties.getQualifiedName(),
                                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                             getOptions,
                                                                                                             methodName);

        String notificationTypeGUID;

        if (notificationTypeElement == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

            newElementOptions.setAnchorGUID(digitalProductGUID);
            newElementOptions.setAnchorScopeGUIDs(Collections.singletonList(digitalProductGUID));
            newElementOptions.setIsOwnAnchor(false);

            notificationTypeGUID = notificationTypeHandler.createGovernanceDefinition(userId,
                                                                                      newElementOptions,
                                                                                      null,
                                                                                      notificationTypeProperties,
                                                                                      null);

            NotificationSubscriberProperties notificationSubscriberProperties = new NotificationSubscriberProperties();

            notificationSubscriberProperties.setActivityStatus(ActivityStatus.IN_PROGRESS);

            if (productContext.communityNoteLogGUID() != null)
            {
                notificationSubscriberProperties.setLabel("community notifications");
                notificationSubscriberProperties.setDescription("A note log collects the notifications from the subscription manager based on activity around notification type: " + notificationTypeGUID);

                notificationTypeHandler.linkNotificationSubscriber(userId, notificationTypeGUID, productContext.communityNoteLogGUID(), makeAnchorOptions, notificationSubscriberProperties);
            }

            /*
             * The product manager receives notifications to enable monitoring of product activity.
             */
            if (productContext.productManagerGUID() != null)
            {
                notificationSubscriberProperties.setLabel("product manager notifications");
                notificationSubscriberProperties.setDescription("Notifications from the subscription manager related to notification type: " + notificationTypeGUID + " are sent to the product manager.");

                notificationTypeHandler.linkNotificationSubscriber(userId, notificationTypeGUID, productContext.productManagerGUID(), makeAnchorOptions, notificationSubscriberProperties);
            }
        }
        else
        {
            notificationTypeGUID = notificationTypeElement.getElementHeader().getGUID();

            /*
             * An existing notification type is brought up to date with a merge, so that nothing else about it is
             * touched.  A notification type that is not ACTIVE sends nothing, and the notification pattern comes
             * from this request.
             */
            if ((notificationTypeElement.getProperties() instanceof NotificationTypeProperties existingProperties) &&
                    ((existingProperties.getContentStatus() != ContentStatus.ACTIVE) ||
                     (existingProperties.getMultipleNotificationsPermitted() != multipleNotificationsPermitted) ||
                     (existingProperties.getMinimumNotificationInterval() != minimumNotificationInterval)))
            {
                NotificationTypeProperties updatedProperties = new NotificationTypeProperties();

                updatedProperties.setContentStatus(ContentStatus.ACTIVE);
                updatedProperties.setMultipleNotificationsPermitted(multipleNotificationsPermitted);
                updatedProperties.setMinimumNotificationInterval(minimumNotificationInterval);

                notificationTypeHandler.updateGovernanceDefinition(userId,
                                                                   notificationTypeGUID,
                                                                   new UpdateOptions(metadataSourceOptions),
                                                                   updatedProperties);
            }
        }

        this.linkMonitoredResources(userId, notificationTypeGUID, metadataSourceOptions, productContext, monitoredResourceGUIDs, notificationTypeElement != null);

        if (subscriptionManagerGUID != null)
        {
            this.registerWithSubscriptionManager(userId, notificationTypeGUID, subscriptionManagerGUID, metadataSourceOptions, notificationTypeElement != null);
        }
        else if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OMFAuditCode.DEFAULT_SUBSCRIPTION_MANAGER_NOT_FOUND.getMessageDefinition(localServiceName,
                                                                                                        notificationTypeGUID,
                                                                                                        DEFAULT_SUBSCRIPTION_MANAGER_GUID));
        }

        return notificationTypeGUID;
    }


    /**
     * Link the monitored resources to the notification type.  When the notification type already existed, the
     * resources it already monitors are skipped.
     *
     * @param userId calling user
     * @param notificationTypeGUID unique identifier of the notification type
     * @param metadataSourceOptions options to control access to open metadata
     * @param productContext the elements around the product
     * @param monitoredResourceGUIDs elements whose changes trigger a notification (may be null)
     * @param notificationTypeExisted was the notification type already catalogued?
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkMonitoredResources(String                userId,
                                        String                notificationTypeGUID,
                                        MetadataSourceOptions metadataSourceOptions,
                                        ProductContext        productContext,
                                        List<String>          monitoredResourceGUIDs,
                                        boolean               notificationTypeExisted) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        if (monitoredResourceGUIDs == null)
        {
            return;
        }

        List<String> existingMonitoredResources = new ArrayList<>();

        if (notificationTypeExisted)
        {
            RelatedMetadataElementList monitoredResources = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                          notificationTypeGUID,
                                                                                                          1,
                                                                                                          OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName,
                                                                                                          new QueryOptions(metadataSourceOptions));

            if ((monitoredResources != null) && (monitoredResources.getElementList() != null))
            {
                for (RelatedMetadataElement monitoredResource : monitoredResources.getElementList())
                {
                    if ((monitoredResource != null) && (monitoredResource.getElement() != null))
                    {
                        existingMonitoredResources.add(monitoredResource.getElement().getElementGUID());
                    }
                }
            }
        }

        MakeAnchorOptions           makeAnchorOptions           = new MakeAnchorOptions(metadataSourceOptions);
        MonitoredResourceProperties monitoredResourceProperties = new MonitoredResourceProperties();

        monitoredResourceProperties.setLabel("monitored resource");
        monitoredResourceProperties.setDescription("Changes to this element trigger a notification to the subscribers of the " + productContext.productName() + " product.");

        for (String monitoredResourceGUID : monitoredResourceGUIDs)
        {
            if ((monitoredResourceGUID != null) && (! existingMonitoredResources.contains(monitoredResourceGUID)))
            {
                notificationTypeHandler.linkMonitoredResource(userId, notificationTypeGUID, monitoredResourceGUID, makeAnchorOptions, monitoredResourceProperties);
            }
        }
    }


    /**
     * Hand the notification type to the subscription manager as a catalog target, so that the manager notifies
     * its subscribers.  The manager notices new catalog targets on its next refresh.  A notification type that
     * already existed may already be one of the manager's catalog targets, so its catalog target relationships are
     * checked first.
     *
     * @param userId calling user
     * @param notificationTypeGUID unique identifier of the notification type
     * @param subscriptionManagerGUID integration connector that notifies the subscribers
     * @param metadataSourceOptions options to control access to open metadata
     * @param notificationTypeExisted was the notification type already catalogued?
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void registerWithSubscriptionManager(String                userId,
                                                 String                notificationTypeGUID,
                                                 String                subscriptionManagerGUID,
                                                 MetadataSourceOptions metadataSourceOptions,
                                                 boolean               notificationTypeExisted) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        if (notificationTypeExisted)
        {
            RelatedMetadataElementList catalogTargetRelationships = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  notificationTypeGUID,
                                                                                                                  2,
                                                                                                                  OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                                                                                  new QueryOptions(metadataSourceOptions));

            if ((catalogTargetRelationships != null) && (catalogTargetRelationships.getElementList() != null))
            {
                for (RelatedMetadataElement catalogTargetRelationship : catalogTargetRelationships.getElementList())
                {
                    if ((catalogTargetRelationship != null) &&
                            (catalogTargetRelationship.getElement() != null) &&
                            (subscriptionManagerGUID.equals(catalogTargetRelationship.getElement().getElementGUID())))
                    {
                        return;
                    }
                }
            }
        }

        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

        catalogTargetProperties.setCatalogTargetName(SUBSCRIPTION_MANAGER_CATALOG_TARGET_NAME);
        catalogTargetProperties.setPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                        subscriptionManagerGUID,
                                                        notificationTypeGUID,
                                                        new MakeAnchorOptions(metadataSourceOptions),
                                                        relationshipBuilder.getNewElementProperties(catalogTargetProperties));
    }


    /**
     * Set up the governance action process that a subscriber runs to take out a subscription of this type.  It is
     * configured from the create subscription governance action type, with the elements of the product supplied as
     * action targets so that the subscriber only needs to name themselves and their destination.  The process is
     * linked to the product as a resource for creating subscriptions.
     *
     * @param userId calling user
     * @param digitalProductGUID unique identifier of the product
     * @param metadataSourceOptions options to control access to open metadata
     * @param productContext the elements around the product
     * @param identifier identifier of the subscription type
     * @param displayName display name of the subscription type
     * @param description description of the subscription type
     * @param notificationTypeGUID unique identifier of the notification type driving the subscription
     * @param methodName calling method
     * @return unique identifier of the governance action process
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String addSubscriptionGovernanceActionProcess(String                userId,
                                                          String                digitalProductGUID,
                                                          MetadataSourceOptions metadataSourceOptions,
                                                          ProductContext        productContext,
                                                          String                identifier,
                                                          String                displayName,
                                                          String                description,
                                                          String                notificationTypeGUID,
                                                          String                methodName) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        /*
         * The qualified name follows the pattern used by the Jacquard Digital Product Loom, so that a product
         * catalogued by Jacquard and then maintained through this handler shares its processes.
         */
        String processQualifiedName = OpenMetadataType.PROVISIONING_ACTION_PROCESS.typeName + "::" + productContext.productName() + "::" + ResourceUse.CREATE_SUBSCRIPTION.getResourceUse() + "::" + identifier;

        /*
         * The process is looked up through the client rather than this handler's own root element lookup,
         * because that lookup only returns elements of the handler's type - digital products - and a process
         * is not one.
         */
        GetOptions getOptions = new GetOptions(metadataSourceOptions);

        getOptions.setMetadataElementTypeName(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName);

        OpenMetadataElement existingProcess = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                processQualifiedName,
                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                getOptions);

        if (existingProcess != null)
        {
            return existingProcess.getElementGUID();
        }

        String              subscriptionName            = displayName + " for " + productContext.productName();
        Map<String, String> additionalRequestParameters = new HashMap<>();

        additionalRequestParameters.put(SUBSCRIPTION_NAME_REQUEST_PARAMETER, subscriptionName);
        additionalRequestParameters.put(SUBSCRIPTION_IDENTIFIER_REQUEST_PARAMETER, identifier + "-" + productContext.productIdentifier());
        additionalRequestParameters.put(SUBSCRIPTION_DESCRIPTION_REQUEST_PARAMETER, description);

        String governanceActionProcessGUID = this.createProcessFromGovernanceActionType(userId,
                                                                                        metadataSourceOptions,
                                                                                        OpenMetadataType.SUBSCRIBING_ACTION_PROCESS.typeName,
                                                                                        processQualifiedName,
                                                                                        "Create " + subscriptionName,
                                                                                        description + "  Supply the requester (actor entity) as an action target called digitalSubscriptionRequester and the asset where the data is to be sent to as action target named destinationDataSet.",
                                                                                        GovernanceDomain.DATA_SHARING.getOrdinal(),
                                                                                        CREATE_SUBSCRIPTION_ACTION_TYPE_GUID,
                                                                                        additionalRequestParameters,
                                                                                        digitalProductGUID,
                                                                                        Collections.singletonList(digitalProductGUID));

        /*
         * The action targets supplied here are the ones that are fixed for the subscription type.  Their
         * specification properties are removed from the process so that the remaining specification properties
         * cover the ones that the subscriber needs to supply.
         */
        List<String> actionTargetNames = new ArrayList<>();

        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, digitalProductGUID, DIGITAL_SUBSCRIPTION_ITEM_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, productContext.productAssetGUID(), DIGITAL_SUBSCRIPTION_SOURCE_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, productContext.licenseTypeGUID(), LICENSE_TYPE_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, notificationTypeGUID, NOTIFICATION_TYPE_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, productContext.productManagerGUID(), DIGITAL_PRODUCT_OWNER_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, productContext.serviceLevelObjectiveGUID(), SERVICE_LEVEL_OBJECTIVE_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, PROVISION_SUBSCRIPTION_ACTION_TYPE_GUID, PROVISIONING_ACTION_TYPE_ACTION_TARGET, actionTargetNames);
        this.addActionTarget(userId, metadataSourceOptions, governanceActionProcessGUID, CANCEL_SUBSCRIPTION_ACTION_TYPE_GUID, CANCELLING_ACTION_TYPE_ACTION_TARGET, actionTargetNames);

        /*
         * The license type and notification type are optional to the governance service, so their specification
         * properties are removed whether or not one was supplied.
         */
        if (! actionTargetNames.contains(LICENSE_TYPE_ACTION_TARGET))
        {
            actionTargetNames.add(LICENSE_TYPE_ACTION_TARGET);
        }
        if (! actionTargetNames.contains(NOTIFICATION_TYPE_ACTION_TARGET))
        {
            actionTargetNames.add(NOTIFICATION_TYPE_ACTION_TARGET);
        }

        this.removeSatisfiedSpecificationProperties(userId, metadataSourceOptions, governanceActionProcessGUID, actionTargetNames, additionalRequestParameters, methodName);

        /*
         * Link the new governance action process to the product.
         */
        ResourceListProperties resourceListProperties = new ResourceListProperties();

        resourceListProperties.setResourceUse(ResourceUse.CREATE_SUBSCRIPTION.getResourceUse());
        resourceListProperties.setDescription(ResourceUse.CREATE_SUBSCRIPTION.getDescription());

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        digitalProductGUID,
                                                        governanceActionProcessGUID,
                                                        new MakeAnchorOptions(metadataSourceOptions),
                                                        relationshipBuilder.getNewElementProperties(resourceListProperties));

        return governanceActionProcessGUID;
    }


    /**
     * Add an action target to a governance action process, if there is an element to add.
     *
     * @param userId calling user
     * @param metadataSourceOptions options to control access to open metadata
     * @param governanceActionProcessGUID unique identifier of the process
     * @param actionTargetGUID unique identifier of the element to add (null means nothing is added)
     * @param actionTargetName name of the action target
     * @param actionTargetNames list of the action target names supplied so far - added to if the element is added
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addActionTarget(String                userId,
                                 MetadataSourceOptions metadataSourceOptions,
                                 String                governanceActionProcessGUID,
                                 String                actionTargetGUID,
                                 String                actionTargetName,
                                 List<String>          actionTargetNames) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        if (actionTargetGUID != null)
        {
            openMetadataClient.createRelatedElementsInStore(userId,
                                                            OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                            governanceActionProcessGUID,
                                                            actionTargetGUID,
                                                            new MakeAnchorOptions(metadataSourceOptions),
                                                            new NewElementProperties(propertyHelper.addStringProperty(null,
                                                                                                                      OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                                      actionTargetName)));
            actionTargetNames.add(actionTargetName);
        }
    }


    /**
     * Remove the specification properties from the process for the action targets and request parameters that have
     * already been supplied.  This leaves the specification properties describing what the caller of the process
     * needs to supply.
     *
     * @param userId calling user
     * @param metadataSourceOptions options to control access to open metadata
     * @param governanceActionProcessGUID unique identifier of the process
     * @param actionTargetNames the action targets already supplied
     * @param requestParameters the request parameters already supplied
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void removeSatisfiedSpecificationProperties(String                userId,
                                                        MetadataSourceOptions metadataSourceOptions,
                                                        String                governanceActionProcessGUID,
                                                        List<String>          actionTargetNames,
                                                        Map<String, String>   requestParameters,
                                                        String                methodName) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        RelatedMetadataElementList specificationProperties = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           governanceActionProcessGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                           new QueryOptions(metadataSourceOptions));

        if ((specificationProperties == null) || (specificationProperties.getElementList() == null))
        {
            return;
        }

        DeleteOptions deleteOptions = new DeleteOptions(metadataSourceOptions);

        for (RelatedMetadataElement specificationProperty : specificationProperties.getElementList())
        {
            if ((specificationProperty != null) && (specificationProperty.getElement() != null))
            {
                String propertyType = propertyHelper.getStringProperty(localServiceName,
                                                                       OpenMetadataProperty.PROPERTY_NAME.name,
                                                                       specificationProperty.getRelationshipProperties(),
                                                                       methodName);
                String preferredValue = propertyHelper.getStringProperty(localServiceName,
                                                                         OpenMetadataProperty.PREFERRED_VALUE.name,
                                                                         specificationProperty.getElement().getElementProperties(),
                                                                         methodName);

                if (SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType().equals(propertyType))
                {
                    if (actionTargetNames.contains(preferredValue))
                    {
                        openMetadataClient.deleteRelationshipInStore(userId, specificationProperty.getRelationshipGUID(), deleteOptions);
                    }
                }
                else if (SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getPropertyType().equals(propertyType))
                {
                    if (requestParameters.containsKey(preferredValue))
                    {
                        openMetadataClient.deleteRelationshipInStore(userId, specificationProperty.getRelationshipGUID(), deleteOptions);
                    }
                }
            }
        }
    }


    /**
     * Create a specific governance action process from a generic governance action type.  The process has a single
     * step that runs the governance action type's governance service, with the additional request parameters
     * supplied on the flow into that step.  The action targets and specification properties of the governance
     * action type are copied to the new process.
     *
     * @param userId calling user
     * @param metadataSourceOptions options to control access to open metadata
     * @param processType type of the process
     * @param processQualifiedName new qualified name for the process
     * @param processName new name for the process
     * @param processDescription new description for the process
     * @param domainIdentifier governance domain
     * @param governanceActionTypeGUID the unique identifier of the governance action type
     * @param additionalRequestParameters the additional, predefined request parameters to add to the
     *                                   GovernanceActionProcessFlow relationship
     * @param anchorGUID unique identifier of the anchor
     * @param anchorScopeGUIDs unique identifiers of the search scope
     * @return unique identifier of new governance action process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createProcessFromGovernanceActionType(String                userId,
                                                         MetadataSourceOptions metadataSourceOptions,
                                                         String                processType,
                                                         String                processQualifiedName,
                                                         String                processName,
                                                         String                processDescription,
                                                         int                   domainIdentifier,
                                                         String                governanceActionTypeGUID,
                                                         Map<String, String>   additionalRequestParameters,
                                                         String                anchorGUID,
                                                         List<String>          anchorScopeGUIDs) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        ElementProperties processProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               processQualifiedName);

        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             processName);

        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             processDescription);

        NewElementOptions processOptions = new NewElementOptions(metadataSourceOptions);

        processOptions.setAnchorGUID(anchorGUID);
        processOptions.setIsOwnAnchor(anchorGUID == null);
        processOptions.setAnchorScopeGUIDs(anchorScopeGUIDs);

        String processGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                             processType,
                                                                             processOptions,
                                                                             null,
                                                                             new NewElementProperties(processProperties),
                                                                             null);

        GetOptions        getOptions        = new GetOptions(metadataSourceOptions);
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(metadataSourceOptions);

        OpenMetadataElement governanceActionType = openMetadataClient.getMetadataElementByGUID(userId, governanceActionTypeGUID, getOptions);

        if (governanceActionType != null)
        {
            RelatedMetadataElement governanceActionExecutorRelationship = openMetadataClient.getRelatedMetadataElement(userId,
                                                                                                                       governanceActionTypeGUID,
                                                                                                                       1,
                                                                                                                       OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                                                                                       getOptions);

            if ((governanceActionExecutorRelationship != null) && (governanceActionExecutorRelationship.getElement() != null))
            {
                String governanceEngineGUID = governanceActionExecutorRelationship.getElement().getElementGUID();

                ElementProperties processStepProperties = propertyHelper.addStringProperty(governanceActionType.getElementProperties(),
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                           processQualifiedName + ":processStep1");

                processStepProperties = propertyHelper.addIntProperty(processStepProperties,
                                                                      OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                                      domainIdentifier);

                ElementProperties processFlowProperties = propertyHelper.addStringMapProperty(null,
                                                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                              additionalRequestParameters);

                NewElementOptions processStepOptions = new NewElementOptions(metadataSourceOptions);

                processStepOptions.setAnchorGUID(processGUID);
                processStepOptions.setIsOwnAnchor(false);
                processStepOptions.setAnchorScopeGUIDs(anchorScopeGUIDs);
                processStepOptions.setParentGUID(processGUID);
                processStepOptions.setParentRelationshipTypeName(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName);
                processStepOptions.setParentAtEnd1(true);

                String processStep1GUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                                          processStepOptions,
                                                                                          null,
                                                                                          new NewElementProperties(processStepProperties),
                                                                                          new NewElementProperties(processFlowProperties));

                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                                processStep1GUID,
                                                                governanceEngineGUID,
                                                                makeAnchorOptions,
                                                                new NewElementProperties(governanceActionExecutorRelationship.getRelationshipProperties()));

                /*
                 * Copy the pre-populated governance action targets to the new process.
                 */
                this.copyRelationships(userId,
                                       governanceActionTypeGUID,
                                       processGUID,
                                       OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                       metadataSourceOptions);

                /*
                 * Copy the specification properties to the new process.
                 */
                this.copyRelationships(userId,
                                       governanceActionTypeGUID,
                                       processGUID,
                                       OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                       metadataSourceOptions);
            }
        }

        return processGUID;
    }


    /**
     * Copy the relationships of one type that start at the source element to the destination element.
     *
     * @param userId calling user
     * @param sourceGUID element whose relationships are copied
     * @param destinationGUID element that receives the copies
     * @param relationshipTypeName type of relationship to copy
     * @param metadataSourceOptions options to control access to open metadata
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void copyRelationships(String                userId,
                                   String                sourceGUID,
                                   String                destinationGUID,
                                   String                relationshipTypeName,
                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        RelatedMetadataElementList relationships = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                 sourceGUID,
                                                                                                 1,
                                                                                                 relationshipTypeName,
                                                                                                 new QueryOptions(metadataSourceOptions));

        if ((relationships != null) && (relationships.getElementList() != null))
        {
            MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(metadataSourceOptions);

            for (RelatedMetadataElement relationship : relationships.getElementList())
            {
                if ((relationship != null) && (relationship.getElement() != null))
                {
                    openMetadataClient.createRelatedElementsInStore(userId,
                                                                    relationshipTypeName,
                                                                    destinationGUID,
                                                                    relationship.getElement().getElementGUID(),
                                                                    makeAnchorOptions,
                                                                    new NewElementProperties(relationship.getRelationshipProperties()));
                }
            }
        }
    }
}
