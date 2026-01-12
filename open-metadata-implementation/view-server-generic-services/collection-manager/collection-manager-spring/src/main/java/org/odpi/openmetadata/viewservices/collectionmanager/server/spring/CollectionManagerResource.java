/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.collectionmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Collection Manager",
        description="Maintain and explore the contents of collections. Collections are used to group related elements together.  The contents of a collection can also be organized into a hierarchy of folders. This useful function is used widely in open metadata.  For example, there are specialized collections for glossaries, solution blueprints, information supply chains, digital product catalogs, digital products themselves, agreements and event sets.  They may be also used to organize resources for a particular project, team or individual.",
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/collections")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * Returns the list of digital products matching the search string and optional deployment status.
     * (Use findAuthoredElements() in Classification Explorer for retrieving by content status.)
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of digital products
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDigitalProducts",
            description="Returns the list of digital products matching the search string and optional deployment status. (Use findAuthoredElements() in Classification Explorer for retrieving by content status.)",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public OpenMetadataRootElementsResponse findDigitalProducts(@PathVariable String            serverName,
                                                                @PathVariable String             urlMarker,
                                                                @RequestBody  (required = false)
                                                                DeploymentStatusSearchString requestBody)
    {
        return restAPI.findDigitalProducts(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of digital products matching the category and optional deployment status.
     * (Use getAuthoredElementsByCategory() in Classification Explorer for retrieving by content status.)
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of digital products
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDigitalProductByCategory",
            description="Returns the list of digital products matching the search string and optional deployment status. (Use findAuthoredElements() in Classification Explorer for retrieving by content status.)",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public OpenMetadataRootElementsResponse getDigitalProductByCategory(@PathVariable String            serverName,
                                                                        @PathVariable String             urlMarker,
                                                                        @RequestBody  (required = false)
                                                                            DeploymentStatusFilterRequestBody requestBody)
    {
        return restAPI.getDigitalProductByCategory(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCollectionsByName",
            description="Returns the list of collections with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-collection-category")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections")
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateCollection",
            description="Update the properties of a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public BooleanResponse updateCollection(@PathVariable String                  serverName,
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/collections/{collectionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="attachCollection",
            description="Connect an existing collection to an element using the ResourceList relationship (0019).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse attachCollection(@PathVariable String                  serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable String                  parentGUID,
                                         @PathVariable String                  collectionGUID,
                                         @RequestBody(required = false)
                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.attachCollection(serverName, urlMarker, parentGUID, collectionGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/collections/{collectionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachCollection",
            description="Detach an existing collection from an element connected via the ResourceList relationship (0019).  If the collection is anchored to the element, it is deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse detachCollection(@PathVariable String                    serverName,
                                         @PathVariable String                    urlMarker,
                                         @PathVariable String                    parentGUID,
                                         @PathVariable String                    collectionGUID,
                                         @RequestBody(required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachCollection(serverName, urlMarker, parentGUID, collectionGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/data-descriptions/{collectionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="attachDataDescription",
            description="Connect an existing data describing collection to an element using the DataDescription relationship (0580).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse attachDataDescription(@PathVariable String                  serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable String                  parentGUID,
                                              @PathVariable String                  collectionGUID,
                                              @RequestBody(required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.attachDataDescription(serverName, urlMarker, parentGUID, collectionGUID, requestBody);
    }


    /**
     * Detach an existing data describing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/data-descriptions/{collectionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataDescription",
            description="Detach an existing data describing collection from an element connected via the DataDescription relationship (0580).  If the collection is anchored to the element, it is deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse detachDataDescription(@PathVariable String                    serverName,
                                              @PathVariable String                    urlMarker,
                                              @PathVariable String                    parentGUID,
                                              @PathVariable String                    collectionGUID,
                                              @RequestBody(required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataDescription(serverName, urlMarker, parentGUID, collectionGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/subscribers/{digitalSubscriberGUID}/subscriptions/{digitalSubscriptionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subscribers/{digitalSubscriberGUID}/subscriptions/{digitalSubscriptionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSubscriber(serverName, urlMarker, digitalSubscriberGUID, digitalSubscriptionGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/agreement-actors/{actorGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collection/agreements/agreement-actors/{agreementActorRelationshipGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                 DeleteRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/agreement-items/{agreementItemGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/agreement-items/{agreementItemGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                DeleteRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/contract-links/{externalReferenceGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/contract-links/{externalReferenceGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachContract(serverName, urlMarker, agreementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Classify the collection to indicate that it is an editing collection - this means it is
     * a collection of element updates that will be merged into its source collection.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the metadata element to classify
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/{collectionGUID}/is-editing-collection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setEditingCollection",
            description="Classify the collection to indicate that it is an editing collection - this means it is " +
                    "a collection of element updates that will be merged into its source collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse setEditingCollection(@PathVariable String                       serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable String                       collectionGUID,
                                             @RequestBody  NewClassificationRequestBody requestBody)
    {
        return restAPI.setEditingCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Remove the editing collection designation from the collection.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the metadata element to declassify
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/{collectionGUID}/is-editing-collection/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearEditingCollection",
            description="Remove the editing collection designation from the collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse clearEditingCollection(@PathVariable String                    serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable String collectionGUID,
                                               @RequestBody(required = false)
                                               MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearEditingCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Classify the collection to indicate that it is a scoping collection - this means it is
     * a collection of elements that are being considered for inclusion in a project or activity.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the metadata element to classify
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/{collectionGUID}/is-scoping-collection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setScopingCollection",
            description="Classify the collection to indicate that it is a scoping collection - this means it is" +
                    " a collection of elements that are being considered for inclusion in a project or activity",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse setScopingCollection(@PathVariable String                       serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable String                       collectionGUID,
                                             @RequestBody  NewClassificationRequestBody requestBody)
    {
        return restAPI.setScopingCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Remove the scoping collection designation from the collection.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the metadata element to declassify
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/{collectionGUID}/is-scoping-collection/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearScopingCollection",
            description="Remove the scoping collection designation from the collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse clearScopingCollection(@PathVariable String                    serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable String collectionGUID,
                                               @RequestBody(required = false)
                                               MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearScopingCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Classify the collection to indicate that it is a staging collection - this means it is
     * a collection of element updates that will be transferred into another collection
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the metadata element to classify
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/{collectionGUID}/is-staging-collection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setStagingCollection",
            description="Classify the collection to indicate that it is a staging collection - this means it is" +
                    " a collection of element updates that will be transferred into another collection",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse setStagingCollection(@PathVariable String                       serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable String                       collectionGUID,
                                             @RequestBody  NewClassificationRequestBody requestBody)
    {
        return restAPI.setStagingCollection(serverName, urlMarker, collectionGUID, requestBody);
    }


    /**
     * Remove the staging collection designation from the collection.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the metadata element to declassify
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/collections/{collectionGUID}/is-staging-collection/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearStagingCollection",
            description="Remove the staging collection designation from the collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse clearStagingCollection(@PathVariable String                    serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable String collectionGUID,
                                               @RequestBody(required = false)
                                               MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearStagingCollection(serverName, urlMarker, collectionGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                             DeleteElementRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/members")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/hierarchy")
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeFromCollection",
            description="Remove an element from a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse removeFromCollection(@PathVariable String                    serverName,
                                             @PathVariable String                    urlMarker,
                                             @PathVariable String                    collectionGUID,
                                             @PathVariable String                    elementGUID,
                                             @RequestBody(required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeFromCollection(serverName, urlMarker, collectionGUID, elementGUID, requestBody);
    }
}

