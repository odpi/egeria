/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.OpenMetadataRootHierarchyMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.VisualStyle;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The CollectionsClient supports requests related to collections.
 */
public class CollectionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName    name of this server (view server)
     * @param auditLog           logging destination
     * @param localServiceName   local service name
     * @param openMetadataClient access to open metadata
     */
    public CollectionHandler(String localServerName,
                             AuditLog auditLog,
                             String localServiceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, localServiceName, openMetadataClient, OpenMetadataType.COLLECTION.typeName);
    }


    /* =====================================================================================================================
     * Collections
     */

    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId       userId of user making request
     * @param parentGUID   unique identifier of referenceable object (typically a personal profile, project or
     *                     community) that the collections hang off of
     * @param queryOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAttachedCollections(String userId,
                                                                String parentGUID,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName              = "getLinkedCollections";
        final String parentGUIDParameterName = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        return this.getRelatedRootElements(userId,
                                           parentGUID,
                                           parentGUIDParameterName,
                                           1,
                                           OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param userId        userId of user making request
     * @param searchString  string to search for
     * @param searchOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> findCollections(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "findCollections";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param userId       userId of user making request
     * @param name         name of the collections to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionsByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getCollectionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of collections with a particular category.  This is an optional text field in the collection element.
     *
     * @param userId       userId of user making request
     * @param category     the collection type value to match on.  If it is null, all collections with a null category are returned
     * @param queryOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionsByCategory(String       userId,
                                                                  String       category,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName                  = "getCollectionsByCategory";
        final String collectionTypeParameterName = "category";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(category, collectionTypeParameterName, methodName);

        List<String> propertyNames = List.of(OpenMetadataProperty.CATEGORY.name);

        return super.getRootElementsByName(userId,
                                           category,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param userId         userId of user making request
     * @param collectionGUID unique identifier of the required collection
     * @param getOptions     multiple options to control the query
     * @return collection properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getCollectionByGUID(String     userId,
                                                       String     collectionGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getCollectionByGUID";

        return super.getRootElementByGUID(userId, collectionGUID, getOptions, methodName);
    }


    /**
     * Create a new generic collection.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the collection.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created Collection
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createCollection(String                                userId,
                                   NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   CollectionProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createCollection";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param userId                       calling user
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createCollectionFromTemplate(String userId,
                                               TemplateOptions templateOptions,
                                               String templateGUID,
                                               ElementProperties replacementProperties,
                                               Map<String, String> placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param updateOptions  provides a structure for the additional options when updating an element.
     * @param properties     properties for the collection.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCollection(String userId,
                                 String collectionGUID,
                                 UpdateOptions updateOptions,
                                 CollectionProperties properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName        = "updateCollection";
        final String guidParameterName = "collectionGUID";

        super.updateElement(userId,
                            collectionGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param userId            userId of user making request
     * @param collectionGUID    unique identifier of the collection
     * @param parentGUID        unique identifier of referenceable object that the collection should be attached to
     * @param makeAnchorOptions options to control access to open metadata
     * @param properties        description of how the collection will be used.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachCollection(String userId,
                                 String collectionGUID,
                                 String parentGUID,
                                 MakeAnchorOptions makeAnchorOptions,
                                 ResourceListProperties properties) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName                  = "attachCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName     = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        collectionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCollection(String userId,
                                 String collectionGUID,
                                 String parentGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName     = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        collectionGUID,
                                                        deleteOptions);
    }


    /**
     * Link two dependent products.
     *
     * @param userId                     userId of user making request
     * @param consumerDigitalProductGUID unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID unique identifier of the digital product that it is using.
     * @param metadataSourceOptions      options to control access to open metadata
     * @param relationshipProperties     description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDigitalProductDependency(String userId,
                                             String consumerDigitalProductGUID,
                                             String consumedDigitalProductGUID,
                                             MetadataSourceOptions metadataSourceOptions,
                                             DigitalProductDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkDigitalProductDependency";
        final String end1GUIDParameterName = "consumerDigitalProductGUID";
        final String end2GUIDParameterName = "consumedDigitalProductGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(consumerDigitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(consumedDigitalProductGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                        consumerDigitalProductGUID,
                                                        consumedDigitalProductGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Unlink dependent products.
     *
     * @param userId                     userId of user making request.
     * @param consumerDigitalProductGUID unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID unique identifier of the digital product that it is using.
     * @param deleteOptions              options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDigitalProductDependency(String userId,
                                               String consumerDigitalProductGUID,
                                               String consumedDigitalProductGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachDigitalProductDependency";

        final String end1GUIDParameterName = "consumerDigitalProductGUID";
        final String end2GUIDParameterName = "consumedDigitalProductGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(consumerDigitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(consumedDigitalProductGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                        consumerDigitalProductGUID,
                                                        consumedDigitalProductGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a subscriber to a subscription.
     *
     * @param userId                  userId of user making request
     * @param digitalSubscriberGUID   unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param metadataSourceOptions   options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubscriber(String userId,
                               String digitalSubscriberGUID,
                               String digitalSubscriptionGUID,
                               MetadataSourceOptions metadataSourceOptions,
                               DigitalSubscriberProperties relationshipProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName            = "linkSubscriber";
        final String end1GUIDParameterName = "digitalSubscriberGUID";
        final String end2GUIDParameterName = "digitalSubscriptionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalSubscriberGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalSubscriptionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                        digitalSubscriberGUID,
                                                        digitalSubscriptionGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param userId                  userId of user making request.
     * @param digitalSubscriberGUID   unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param deleteOptions           options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubscriber(String userId,
                                 String digitalSubscriberGUID,
                                 String digitalSubscriptionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachSubscriber";

        final String end1GUIDParameterName = "digitalSubscriberGUID";
        final String end2GUIDParameterName = "digitalSubscriptionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalSubscriberGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalSubscriptionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                        digitalSubscriberGUID,
                                                        digitalSubscriptionGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param userId                        userId of user making request
     * @param digitalProductGUID            unique identifier of the digital product
     * @param digitalProductManagerRoleGUID unique identifier of the product manager role
     * @param metadataSourceOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProductManager(String                    userId,
                                   String                    digitalProductGUID,
                                   String                    digitalProductManagerRoleGUID,
                                   MetadataSourceOptions     metadataSourceOptions,
                                   AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "linkProductManager";
        final String end1GUIDParameterName = "digitalProductGUID";
        final String end2GUIDParameterName = "digitalProductManagerRoleGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalProductManagerRoleGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        digitalProductManagerRoleGUID,
                                                        digitalProductGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param userId                    userId of user making request.
     * @param digitalProductGUID        unique identifier of the digital product
     * @param digitalProductManagerGUID unique identifier of the product manager role
     * @param deleteOptions             options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProductManager(String        userId,
                                     String        digitalProductGUID,
                                     String        digitalProductManagerGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachProductManager";

        final String end1GUIDParameterName = "digitalProductGUID";
        final String end2GUIDParameterName = "digitalProductManagerGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalProductManagerGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        digitalProductManagerGUID,
                                                        digitalProductGUID,
                                                        deleteOptions);
    }

    /**
     * Attach an actor to an agreement.
     *
     * @param userId                 userId of user making request
     * @param agreementGUID          unique identifier of the agreement
     * @param actorGUID              unique identifier of the actor
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkAgreementActor(String                   userId,
                                     String                   agreementGUID,
                                     String                   actorGUID,
                                     MetadataSourceOptions    metadataSourceOptions,
                                     AgreementActorProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkAgreementActor";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, end2GUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName,
                                                               agreementGUID,
                                                               actorGUID,
                                                               metadataSourceOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param userId                         userId of user making request.
     * @param agreementActorRelationshipGUID unique identifier of the element being described
     * @param deleteOptions                  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementActor(String userId,
                                     String agreementActorRelationshipGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachAgreementActor";

        final String end1GUIDParameterName = "agreementActorRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementActorRelationshipGUID, end1GUIDParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId,
                                                     agreementActorRelationshipGUID,
                                                     deleteOptions);
    }


    /**
     * Attach an agreement to an element involved in its definition.
     *
     * @param userId                 userId of user making request
     * @param agreementGUID          unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAgreementItem(String userId,
                                  String agreementGUID,
                                  String agreementItemGUID,
                                  MetadataSourceOptions metadataSourceOptions,
                                  AgreementItemProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkAgreementItem";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "agreementItemGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(agreementItemGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        agreementItemGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param userId            userId of user making request.
     * @param agreementGUID     unique identifier of the agreement
     * @param agreementItemGUID unique identifier of the agreement item
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementItem(String userId,
                                    String agreementGUID,
                                    String agreementItemGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachAgreementItem";

        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "agreementItemGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(agreementItemGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        agreementItemGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param userId                 userId of user making request
     * @param agreementGUID          unique identifier of the agreement
     * @param externalReferenceGUID  unique identifier of the external reference describing the location of the contract
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContract(String userId,
                             String agreementGUID,
                             String externalReferenceGUID,
                             MetadataSourceOptions metadataSourceOptions,
                             ContractLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "linkContract";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        externalReferenceGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param userId                userId of user making request.
     * @param agreementGUID         unique identifier of the agreement
     * @param externalReferenceGUID unique identifier of the external reference describing the location of the contract
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContract(String userId,
                               String agreementGUID,
                               String externalReferenceGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachContract";

        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        externalReferenceGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection
     * @param deleteOptions  options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteCollection(String userId,
                                 String collectionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName                  = "deleteCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, collectionGUID, deleteOptions);
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param queryOptions   options for query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionMembers(String userId,
                                                              String collectionGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName                  = "getCollectionMembers";
        final String collectionGUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        return super.getRelatedRootElements(userId,
                                            collectionGUID,
                                            collectionGUIDParameterName,
                                            1,
                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Return a nested hierarchy of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param queryOptions   multiple options to control the query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootHierarchy getCollectionHierarchy(String       userId,
                                                            String       collectionGUID,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName                  = "getCollectionHierarchy";
        final String collectionGUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        OpenMetadataRootHierarchy collectionHierarchy = this.convertToCollectionGraph(userId,
                                                                                      this.getCollectionByGUID(userId, collectionGUID, queryOptions),
                                                                                      queryOptions,
                                                                                      0);

        if (collectionHierarchy != null)
        {
            OpenMetadataRootHierarchyMermaidGraphBuilder graphBuilder = new OpenMetadataRootHierarchyMermaidGraphBuilder(collectionHierarchy,
                                                                                                                         "Collection Membership",
                                                                                                                         VisualStyle.COLLECTION);

            collectionHierarchy.setMermaidGraph(graphBuilder.getMermaidGraph());

            return collectionHierarchy;
        }


        return null;
    }


    /**
     * Convert the members of this collection to a nested hierarchy of collections.
     *
     * @param userId calling user
     * @param collectionElement starting collection
     * @param queryOptions options to control the query
     * @param currentDepth current depth of this query
     * @return nested hierarchy of collections
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private OpenMetadataRootHierarchy convertToCollectionGraph(String                  userId,
                                                               OpenMetadataRootElement collectionElement,
                                                               QueryOptions            queryOptions,
                                                               int                     currentDepth) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        if (collectionElement != null)
        {
            OpenMetadataRootHierarchy collectionHierarchy = new OpenMetadataRootHierarchy(collectionElement);

            if ((queryOptions == null) || (queryOptions.getGraphQueryDepth() > currentDepth))
            {
                /*
                 * OK to keep digging
                 */
                if (collectionElement.getCollectionMembers() != null)
                {
                    List<OpenMetadataRootHierarchy>     collectionMembers = new ArrayList<>();
                    List<RelatedMetadataElementSummary> otherMembers      = new ArrayList<>();

                    for (RelatedMetadataElementSummary collectionMember : collectionElement.getCollectionMembers())
                    {
                        if (collectionMember != null)
                        {
                            if (propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.COLLECTION.typeName))
                            {
                                OpenMetadataRootElement nestedCollection = this.getCollectionByGUID(userId,
                                                                                                    collectionMember.getRelatedElement().getElementHeader().getGUID(),
                                                                                                    queryOptions);

                                collectionMembers.add(this.convertToCollectionGraph(userId, nestedCollection, queryOptions, currentDepth + 1));
                            }
                            else
                            {
                                otherMembers.add(collectionMember);
                            }
                        }
                    }

                    if (!otherMembers.isEmpty())
                    {
                        collectionHierarchy.setCollectionMembers(otherMembers);
                    }
                    else
                    {
                        collectionHierarchy.setCollectionMembers(null);
                    }

                    if (!collectionMembers.isEmpty())
                    {
                        collectionHierarchy.setOpenMetadataRootHierarchies(collectionMembers);
                    }
                }
            }

            return collectionHierarchy;
        }

        return null;
    }


    /**
     * @param userId         calling user
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the collection member
     * @param queryOptions   multiple options to control the query
     * @return unique identifier for the relationship between the two elements
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getMembershipRelationshipGUID(String userId,
                                                 String collectionGUID,
                                                 String elementGUID,
                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        OpenMetadataRelationshipList linkedResources = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                          collectionGUID,
                                                                                                          elementGUID,
                                                                                                          OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                          queryOptions);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            for (OpenMetadataRelationship relatedMetadataElement : linkedResources.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    return relatedMetadataElement.getRelationshipGUID();
                }
            }
        }

        return null;
    }


    /**
     * Add an element to a collection.
     *
     * @param userId                userId of user making request.
     * @param collectionGUID        unique identifier of the collection.
     * @param metadataSourceOptions options to control access to open metadata
     * @param membershipProperties  properties describing the membership characteristics.
     * @param elementGUID           unique identifier of the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addToCollection(String userId,
                                String collectionGUID,
                                String elementGUID,
                                MetadataSourceOptions metadataSourceOptions,
                                CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName                  = "addToCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName    = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID, new QueryOptions(metadataSourceOptions));

        if (relationshipGUID == null)
        {
            openMetadataClient.createRelatedElementsInStore(userId,
                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                            collectionGUID,
                                                            elementGUID,
                                                            metadataSourceOptions,
                                                            relationshipBuilder.getNewElementProperties(membershipProperties));
        }
        else
        {
            this.updateCollectionMembership(userId,
                                            relationshipGUID,
                                            new UpdateOptions(metadataSourceOptions),
                                            membershipProperties);
        }
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param userId               userId of user making request.
     * @param collectionGUID       unique identifier of the collection.
     * @param elementGUID          unique identifier of the element.
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param membershipProperties properties describing the membership characteristics.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCollectionMembership(String userId,
                                           String collectionGUID,
                                           String elementGUID,
                                           UpdateOptions updateOptions,
                                           CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateCollectionMembership";

        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName    = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID, new QueryOptions(updateOptions));

        if (relationshipGUID != null)
        {
            this.updateCollectionMembership(userId, relationshipGUID, updateOptions, membershipProperties);
        }
        else
        {
            this.addToCollection(userId, collectionGUID, elementGUID, new NewElementOptions(updateOptions), membershipProperties);
        }
    }


    /**
     * Update the properties of a collection membership relationship.
     *
     * @param userId               calling user
     * @param relationshipGUID     unique identifier of the collection
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param membershipProperties properties describing the membership characteristics.
     */
    private void updateCollectionMembership(String userId,
                                            String relationshipGUID,
                                            UpdateOptions updateOptions,
                                            CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     relationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(membershipProperties));
    }


    /**
     * Remove an element from a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeFromCollection(String userId,
                                     String collectionGUID,
                                     String elementGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "removeFromCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName    = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                        collectionGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }
}