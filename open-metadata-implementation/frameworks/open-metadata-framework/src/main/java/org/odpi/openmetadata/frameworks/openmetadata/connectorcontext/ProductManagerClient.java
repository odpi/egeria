/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ProductManagerHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;

import java.util.List;
import java.util.Map;

/**
 * ProductManagerClient provides the compound operations of a product manager: creating a digital product together
 * with the elements that surround it, and adding subscription types to a product.  A subscription type is the
 * notification type that drives the subscribers' notifications, registered with the subscription manager, plus
 * the governance action process that a subscriber runs to take out a subscription of that type.
 * The individual relationships of a digital product are maintained through the CollectionClient.
 */
public class ProductManagerClient extends ConnectorContextClientBase
{
    private final ProductManagerHandler productManagerHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext      connector's context
     * @param localServerName    local server where this client is running - called the local server
     * @param localServiceName   name of this service
     * @param connectorUserId    userId to use when issuing open metadata requests
     * @param connectorGUID      unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog           logging destination
     * @param maxPageSize        max number of elements that can be returned on a query
     */
    public ProductManagerClient(ConnectorContextBase parentContext,
                                String               localServerName,
                                String               localServiceName,
                                String               connectorUserId,
                                String               connectorGUID,
                                String               externalSourceGUID,
                                String               externalSourceName,
                                OpenMetadataClient   openMetadataClient,
                                AuditLog             auditLog,
                                int                  maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.productManagerHandler = new ProductManagerHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new digital product and link it to the elements that surround it.  The product itself is created as
     * a collection, in the same way as createCollection; the remaining parameters name existing elements that are
     * then linked to the new product.  Each of them is optional.
     *
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
    public String createDigitalProduct(NewElementOptions                     newElementOptions,
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
        String elementGUID = productManagerHandler.createDigitalProduct(connectorUserId,
                                                                        newElementOptions,
                                                                        initialClassifications,
                                                                        properties,
                                                                        parentRelationshipProperties,
                                                                        productManagerGUID,
                                                                        productCommunityGUID,
                                                                        collectionGUIDs,
                                                                        questionGUIDs,
                                                                        productAssetGUID,
                                                                        governanceDefinitionGUIDs,
                                                                        dataSpecGUID);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Add a one-time subscription type to a product.  A subscriber to this type receives a single notification, and
     * so a single delivery of the product's data.  It is typically used to evaluate a product.
     *
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
    public String createOneTimeSubscription(String                digitalProductGUID,
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
        String elementGUID = productManagerHandler.createOneTimeSubscription(connectorUserId,
                                                                             digitalProductGUID,
                                                                             metadataSourceOptions,
                                                                             subscriptionManagerGUID,
                                                                             identifier,
                                                                             displayName,
                                                                             description,
                                                                             licenseTypeGUID,
                                                                             serviceLevelObjectiveGUID);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Add a periodic subscription type to a product.  A subscriber to this type receives a notification, and so a
     * delivery of the product's data, at regular intervals.
     *
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
    public String createPeriodicSubscription(String                digitalProductGUID,
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
        String elementGUID = productManagerHandler.createPeriodicSubscription(connectorUserId,
                                                                              digitalProductGUID,
                                                                              metadataSourceOptions,
                                                                              subscriptionManagerGUID,
                                                                              identifier,
                                                                              displayName,
                                                                              description,
                                                                              licenseTypeGUID,
                                                                              serviceLevelObjectiveGUID,
                                                                              notificationInterval);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Add an ongoing update subscription type to a product.  A subscriber to this type receives a notification, and
     * so a delivery of the product's data, whenever one of the monitored resources changes - but no more often than
     * the minimum notification interval.
     *
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
    public String createOngoingUpdateSubscription(String                digitalProductGUID,
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
        String elementGUID = productManagerHandler.createOngoingUpdateSubscription(connectorUserId,
                                                                                   digitalProductGUID,
                                                                                   metadataSourceOptions,
                                                                                   subscriptionManagerGUID,
                                                                                   identifier,
                                                                                   displayName,
                                                                                   description,
                                                                                   licenseTypeGUID,
                                                                                   serviceLevelObjectiveGUID,
                                                                                   monitoredResourceGUIDs,
                                                                                   minimumNotificationInterval);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }
}
