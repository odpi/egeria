/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.collectionmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.collectionmanager.server.CollectionManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The CollectionManagerResource provides the Spring API endpoints of the Collection Manager Open Metadata View Service (OMVS).
 = */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Collection Manager OMVS",
        description="Maintain and explore the contents of nested collections. These collections can be used to represent digital products, or collections of resources for a particular project or team. They can be used to organize assets and other resources into logical groups.",
        externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/collection-manager/overview/"))

public class CollectionManagerResource
{

    private final CollectionManagerRESTServices restAPI = new CollectionManagerRESTServices();


    /**
     * Default constructor
     */
    public CollectionManagerResource()
    {
    }

    /* =====================================================================================================================
     * Collections interface methods
     */

    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param serverName     name of called server
     * @param urlMarker  view service URL marker
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the collections hang off of
     * @param requestBody filter response by collection type - if null, any value will do
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/collections")
    @Operation(summary="getAttachedCollections",
            description="Returns the list of collections that are linked off of the supplied element using the ResourceList relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementsResponse getAttachedCollections(@PathVariable String            serverName,
                                                                   @PathVariable String            urlMarker,
                                                                   @PathVariable String            parentGUID,
                                                                   @RequestBody(required = false)
                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getAttachedCollections(serverName, urlMarker, parentGUID, requestBody);
    }


    /**
     * Returns the list of collections matching the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-search-string")
    @Operation(summary="findCollections",
            description="Returns the list of collections matching the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementsResponse findCollections(@PathVariable String            serverName,
                                                            @PathVariable String             urlMarker,
                                                            @RequestBody  (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findCollections(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param serverName    name of called server
     * @param urlMarker  view service URL marker
     * @param requestBody      name of the collections to return - match is full text match in qualifiedName or name

     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-name")
    @Operation(summary="getCollectionsByName",
            description="Returns the list of collections with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementsResponse getCollectionsByName(@PathVariable String            serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getCollectionsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param requestBody the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-collection-category")
    @Operation(summary="getCollectionsByCategory",
            description="Returns the list of collections with a particular category.  This is an optional text field in the collection element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementsResponse getCollectionsByType(@PathVariable String            serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getCollectionsByCategory(serverName, urlMarker, requestBody);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the required collection
     *
     * @return collection properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}")
    @Operation(summary="getCollectionByGUID",
            description="Return the properties of a specific collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementResponse getCollectionByGUID(@PathVariable String serverName,
                                                               @PathVariable String urlMarker,
                                                               @PathVariable String collectionGUID,
                                                               @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getCollectionByGUID(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Create a new generic collection.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections")
    @Operation(summary="createCollection",
            description="Create a new generic collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createCollection(@PathVariable String                   serverName,
                                         @PathVariable String             urlMarker,
                                         @RequestBody(required = false)  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/from-template")
    @Operation(summary="createCollectionFromTemplate",
            description="Create a new metadata element to represent a collection using an existing metadata element as a template." +
                    " The template defines additional classifications and relationships that should be added to the new collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createCollectionFromTemplate(@PathVariable String              serverName,
                                                     @PathVariable String             urlMarker,
                                                     @RequestBody(required = false)  TemplateRequestBody requestBody)
    {
        return restAPI.createCollectionFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a collection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param requestBody     properties for the collection.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/update")
    @Operation(summary="updateCollection",
            description="Update the properties of a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse updateCollection(@PathVariable String                  serverName,
                                         @PathVariable String                  urlMarker,
                                         @PathVariable String                  collectionGUID,
                                         @RequestBody(required = false)  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody  description of how the collection will be used.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/collections/{collectionGUID}/attach")
    @Operation(summary="attachCollection",
            description="Connect an existing collection to an element using the ResourceList relationship (0019).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse attachCollection(@PathVariable String                  serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable String                  collectionGUID,
                                         @PathVariable String                  parentGUID,
                                         @RequestBody(required = false)
                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.attachCollection(serverName, urlMarker, collectionGUID, parentGUID, requestBody);
    }


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/collections/{collectionGUID}/detach")
    @Operation(summary="detachCollection",
            description="Detach an existing collection from an element connected via the ResourceList relationship (0019).  If the collection is anchored to the element, it is deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse detachCollection(@PathVariable String                    serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable String                    collectionGUID,
                                         @PathVariable String                    parentGUID,
                                         @RequestBody(required = false)
                                             DeleteRequestBody requestBody)
    {
        return restAPI.detachCollection(serverName, urlMarker, collectionGUID, parentGUID, requestBody);
    }



    /**
     * Link two dependent digital products.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/digital-products/{consumerDigitalProductGUID}/product-dependencies/{consumedDigitalProductGUID}/attach")
    @Operation(summary="linkDigitalProductDependency",
            description="Link two dependent digital products.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkDigitalProductDependency(@PathVariable
                                                     String                                serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable
                                                     String consumerDigitalProductGUID,
                                                     @PathVariable
                                                     String consumedDigitalProductGUID,
                                                     @RequestBody (required = false)
                                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDigitalProductDependency(serverName, urlMarker, consumerDigitalProductGUID, consumedDigitalProductGUID, requestBody);
    }


    /**
     * Unlink dependent digital products.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/digital-products/{consumerDigitalProductGUID}/product-dependencies/{consumedDigitalProductGUID}/detach")
    @Operation(summary="detachDigitalProductDependency",
            description="Unlink dependent digital products.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachDigitalProductDependency(@PathVariable
                                                       String                    serverName,
                                                       @PathVariable String             urlMarker,
                                                       @PathVariable
                                                       String consumerDigitalProductGUID,
                                                       @PathVariable
                                                       String consumedDigitalProductGUID,
                                                       @RequestBody (required = false)
                                                           DeleteRequestBody requestBody)
    {
        return restAPI.detachDigitalProductDependency(serverName, urlMarker, consumerDigitalProductGUID, consumedDigitalProductGUID, requestBody);
    }


    /**
     * Attach a subscriber to a subscription.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/subscribers/{digitalSubscriberGUID}/subscriptions/{digitalSubscriptionGUID}/attach")
    @Operation(summary="linkSubscriber",
            description="Attach a subscriber to a subscription.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkSubscriber(@PathVariable
                                       String                                serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable
                                       String digitalSubscriberGUID,
                                       @PathVariable
                                       String digitalSubscriptionGUID,
                                       @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSubscriber(serverName, urlMarker, digitalSubscriberGUID, digitalSubscriptionGUID, requestBody);
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subscribers/{digitalSubscriberGUID}/subscriptions/{digitalSubscriptionGUID}/detach")
    @Operation(summary="detachSubscriber",
            description="Detach a subscriber from a subscription.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachSubscriber(@PathVariable
                                         String                    serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String digitalSubscriberGUID,
                                         @PathVariable
                                         String digitalSubscriptionGUID,
                                         @RequestBody (required = false)
                                             DeleteRequestBody requestBody)
    {
        return restAPI.detachSubscriber(serverName, urlMarker, digitalSubscriberGUID, digitalSubscriptionGUID, requestBody);
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/digital-products/{digitalProductGUID}/product-managers/{digitalProductManagerRoleGUID}/attach")
    @Operation(summary="linkProductManager",
            description="Attach a product manager to a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkProductManager(@PathVariable
                                           String                                serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String digitalProductGUID,
                                           @PathVariable
                                           String digitalProductManagerRoleGUID,
                                           @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkProductManager(serverName, urlMarker, digitalProductGUID, digitalProductManagerRoleGUID, requestBody);
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collection/digital-products/{digitalProductGUID}/product-managers/{digitalProductManagerRoleGUID}/detach")
    @Operation(summary="detachProductManager",
            description="Detach a product manager from a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachProductManager(@PathVariable
                                             String                    serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable
                                             String digitalProductGUID,
                                             @PathVariable
                                             String digitalProductManagerRoleGUID,
                                             @RequestBody (required = false)
                                                 DeleteRequestBody requestBody)
    {
        return restAPI.detachProductManager(serverName, urlMarker, digitalProductGUID, digitalProductManagerRoleGUID, requestBody);
    }



    /**
     * Attach an actor to an agreement.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param actorGUID      unique identifier of the actor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/agreement-actors/{actorGUID}/attach")
    @Operation(summary="linkAgreementActor",
            description="Attach an actor to an agreement.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/agreement"))

    public GUIDResponse linkAgreementActor(@PathVariable
                                           String                                serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String agreementGUID,
                                           @PathVariable
                                           String actorGUID,
                                           @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAgreementActor(serverName, urlMarker, agreementGUID, actorGUID, requestBody);
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementActorRelationshipGUID  unique identifier of the element being described
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collection/agreements/agreement-actors/{agreementActorRelationshipGUID}/detach")
    @Operation(summary="detachAgreementActor",
            description="Detach an actor from an agreement.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/agreement"))

    public VoidResponse detachAgreementActor(@PathVariable
                                             String                    serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable
                                             String agreementActorRelationshipGUID,
                                             @RequestBody (required = false)
                                                 DeleteRequestBody requestBody)
    {
        return restAPI.detachAgreementActor(serverName, urlMarker, agreementActorRelationshipGUID, requestBody);
    }


    /**
     * Attach an agreement to an element referenced in its definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/agreement-items/{agreementItemGUID}/attach")
    @Operation(summary="linkAgreementItem",
            description="Attach an agreement to an element involved in its definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/agreement"))

    public VoidResponse linkAgreementItem(@PathVariable
                                          String                                serverName,
                                          @PathVariable String             urlMarker,
                                          @PathVariable
                                          String agreementGUID,
                                          @PathVariable
                                          String agreementItemGUID,
                                          @RequestBody (required = false)
                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAgreementItem(serverName, urlMarker, agreementGUID, agreementItemGUID, requestBody);
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/agreement-items/{agreementItemGUID}/detach")
    @Operation(summary="detachAgreementItem",
            description="Detach an agreement from an element involved in its definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/agreement"))

    public VoidResponse detachAgreementItem(@PathVariable
                                            String                    serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String agreementGUID,
                                            @PathVariable
                                            String agreementItemGUID,
                                            @RequestBody (required = false)
                                                DeleteRequestBody requestBody)
    {
        return restAPI.detachAgreementItem(serverName, urlMarker, agreementGUID, agreementItemGUID, requestBody);
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/contract-links/{externalReferenceGUID}/attach")
    @Operation(summary="linkContract",
            description="Attach an agreement to an external reference element that describes the location of the contract documents.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/agreement"))

    public VoidResponse linkContract(@PathVariable
                                     String                                serverName,
                                     @PathVariable String             urlMarker,
                                     @PathVariable
                                     String agreementGUID,
                                     @PathVariable
                                     String externalReferenceGUID,
                                     @RequestBody (required = false)
                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkContract(serverName, urlMarker, agreementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collection/agreements/{agreementGUID}/contract-links/{externalReferenceGUID}/detach")
    @Operation(summary="detachContract",
            description="Detach an agreement from an external reference describing the location of the contract documents.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachContract(@PathVariable
                                       String                    serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable
                                       String agreementGUID,
                                       @PathVariable
                                       String externalReferenceGUID,
                                       @RequestBody (required = false)
                                           DeleteRequestBody requestBody)
    {
        return restAPI.detachContract(serverName, urlMarker, agreementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Delete a collection.  It is detected from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     * @param requestBody delete options
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/delete")
    @Operation(summary="deleteCollection",
            description="Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection " +
                    "then they are also deleted; otherwise they are just detached. The option cascadedDelete (default value=false) " +
                    "controls how nested collections are handled.  If it is false then the delete fails if there are nested collections. " +
                    "If true then nested collections are deleted, irrespective of their anchor.  However, any elements anchored to these " +
                    "nested collections are deleted as well. (Use this option wisely :)",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse deleteCollection(@PathVariable String          serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable String          collectionGUID,
                                         @RequestBody(required = false)
                                             DeleteRequestBody requestBody)
    {
        return restAPI.deleteCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     *
     * @return list of collection details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/members")
    @Operation(summary="getCollectionMembers",
            description="Return a list of elements that are a member of a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementsResponse getCollectionMembers(@PathVariable String serverName,
                                                                 @PathVariable String urlMarker,
                                                                 @PathVariable String collectionGUID,
                                                                 @RequestBody(required = false)
                                                                     ResultsRequestBody requestBody)
    {
        return restAPI.getCollectionMembers(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Return a hierarchy of nested collections.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection
     * @param requestBody additional properties for the search
     *
     * @return graph of collection details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/hierarchy")
    @Operation(summary="getCollectionHierarchy",
            description="Return a graph of elements that are the nested members of a collection along with elements immediately connected to the starting collection.  The result includes a mermaid graph of the returned elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementResponse getCollectionHierarchy(@PathVariable String serverName,
                                                                  @PathVariable String urlMarker,
                                                                  @PathVariable String collectionGUID,
                                                                  @RequestBody(required = false)
                                                                      ResultsRequestBody requestBody)
    {
        return restAPI.getCollectionHierarchy(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Add an element to a collection.
     *
     * @param serverName               name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID       unique identifier of the collection.
     * @param requestBody properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/members/{elementGUID}/attach")
    @Operation(summary="addToCollection",
            description="Add an element to a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse addToCollection(@PathVariable String                  serverName,
                                        @PathVariable String                  urlMarker,
                                        @PathVariable String                  collectionGUID,
                                        @PathVariable String                  elementGUID,
                                        @RequestBody(required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.addToCollection(serverName, urlMarker, collectionGUID, elementGUID, requestBody);
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param serverName               name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID       unique identifier of the collection.
     * @param requestBody properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/members/{elementGUID}/update")
    @Operation(summary="updateCollectionMembership",
            description="Update an element's membership to a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse updateCollectionMembership(@PathVariable String                  serverName,
                                                   @PathVariable String                  urlMarker,
                                                   @PathVariable String                  collectionGUID,
                                                   @PathVariable String                  elementGUID,
                                                   @RequestBody(required = false)
                                                   UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateCollectionMembership(serverName, urlMarker, collectionGUID, elementGUID, requestBody);
    }


    /**
     * Remove an element from a collection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     * @param requestBody  null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/members/{elementGUID}/detach")
    @Operation(summary="removeFromCollection",
            description="Remove an element from a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse removeFromCollection(@PathVariable String                    serverName,
                                             @PathVariable String                    urlMarker,
                                             @PathVariable String                    collectionGUID,
                                             @PathVariable String                    elementGUID,
                                             @RequestBody DeleteRequestBody requestBody)
    {
        return restAPI.removeFromCollection(serverName, urlMarker, collectionGUID, elementGUID, requestBody);
    }
}

