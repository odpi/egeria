/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.collectionmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.viewservices.collectionmanager.server.CollectionManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The CollectionManagerResource provides the Spring API endpoints of the Collection Manager Open Metadata View Service (OMVS).
 = */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/collection-manager")

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
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the collections hang off of
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
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

    public CollectionsResponse getAttachedCollections(@PathVariable String            serverName,
                                                      @PathVariable String            parentGUID,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      int    startFrom,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      int    pageSize,
                                                      @RequestBody(required = false)
                                                      FilterRequestBody requestBody)
    {
        return restAPI.getAttachedCollections(serverName, parentGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of collections matching the search string.
     *
     * @param serverName name of the service to route the request to
     * @param classificationName option name of a collection classification
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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

    public CollectionsResponse findCollections(@PathVariable String            serverName,
                                               @RequestParam(required = false)
                                               String classificationName,
                                               @RequestParam (required = false, defaultValue = "false")
                                               boolean           startsWith,
                                               @RequestParam (required = false, defaultValue = "false")
                                               boolean           endsWith,
                                               @RequestParam (required = false, defaultValue = "false")
                                               boolean           ignoreCase,
                                               @RequestParam (required = false, defaultValue = "0")
                                               int               startFrom,
                                               @RequestParam (required = false, defaultValue = "0")
                                               int               pageSize,
                                               @RequestBody  (required = false)
                                               FilterRequestBody requestBody)
    {
        return restAPI.findCollections(serverName, classificationName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param serverName    name of called server
     * @param classificationName option name of a collection classification
     * @param requestBody      name of the collections to return - match is full text match in qualifiedName or name
     * @param startFrom index of the list to start from (0 for start)
     * @param pageSize  maximum number of elements to return
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

    public CollectionsResponse getCollectionsByName(@PathVariable String            serverName,
                                                    @RequestParam(required = false)
                                                    String classificationName,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int    startFrom,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int    pageSize,
                                                    @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getCollectionsByName(serverName, classificationName, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param serverName         name of called server
     * @param classificationName option name of a collection classification
     * @param requestBody the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-collection-type")
    @Operation(summary="getCollectionsByType",
            description="Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public CollectionsResponse getCollectionsByType(@PathVariable String            serverName,
                                                    @RequestParam(required = false)
                                                    String            classificationName,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int               startFrom,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int               pageSize,
                                                    @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getCollectionsByType(serverName, classificationName, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param serverName         name of called server
     * @param collectionGUID unique identifier of the required collection
     *
     * @return collection properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/collections/{collectionGUID}")
    @Operation(summary="getCollection",
            description="Return the properties of a specific collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public CollectionResponse getCollection(@PathVariable String serverName,
                                            @PathVariable String collectionGUID)
    {
        return restAPI.getCollection(serverName, collectionGUID, null);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param serverName         name of called server
     * @param collectionGUID unique identifier of the required collection
     *
     * @return collection properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}")
    @Operation(summary="getCollection",
            description="Return the properties of a specific collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public CollectionResponse getCollection(@PathVariable String serverName,
                                            @PathVariable String collectionGUID,
                                            @RequestBody  AnyTimeRequestBody requestBody)
    {
        return restAPI.getCollection(serverName, collectionGUID, requestBody);
    }


    /**
     * Create a new generic collection.
     *
     * @param serverName                 name of called server.
     * @param classificationName name of collection classification
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
                                         @RequestParam(required = false)
                                         String                   classificationName,
                                         @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, classificationName, requestBody);
    }


    /**
     * Create a new collection with the RootCollection classification.  Used to identify the top of a
     * collection hierarchy.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/root-collection")
    @Operation(summary="createRootCollection",
            description="Create a new collection with the RootCollection classification.  Used to identify the top of a collection hierarchy.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createRootCollection(@PathVariable String                   serverName,
                                             @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the DataSpec classification.  Used to identify a collection of data structures and
     * data fields used to define data requirements for a project or initiative.
     *
     * @param serverName              name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/data-spec-collection")
    @Operation(summary="createDataSpecCollection",
            description="Create a new collection with the DataSpec classification.  Used to identify a collection of data structures and " +
                    "data fields used to define data requirements for a project or initiative.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public GUIDResponse createDataSpecCollection(@PathVariable String                   serverName,
                                                 @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.DATA_SPEC_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the Data Dictionary classification.  Used to identify a collection of
     * data fields that represent a data store of collection of common data types.
     *
     * @param serverName              name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/data-dictionary-collection")
    @Operation(summary="createDataDictionaryCollection",
            description="Create a new collection with the Data Dictionary classification.  Used to identify a collection of " +
                    "data fields that represent a data store of collection of common data types.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-dictionary"))

    public GUIDResponse createDataDictionaryCollection(@PathVariable String                   serverName,
                                                       @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.DATA_DICTIONARY_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the Folder classification.  This is used to identify the organizing collections
     * in a collection hierarchy.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/folder")
    @Operation(summary="createFolderCollection",
            description="Create a new collection with the Folder classification.  This is used to identify the organizing collections in a collection hierarchy.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createFolderCollection(@PathVariable String                   serverName,
                                               @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the ContextEventCollection classification.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/context-event-collection")
    @Operation(summary="createContextEventCollection",
            description="Create a new collection with the ContextEventCollection classification.  This is used to group context events together." +
                    " For example, the collection may describe a series of events that affect a set of resources.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createContextEventCollection(@PathVariable String                   serverName,
                                                     @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.CONTEXT_EVENT_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the Namespace classification.  This is used to group elements that belong to the same namespace.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/namespace-collection")
    @Operation(summary="createNamespaceCollection",
            description="Create a new collection with the Namespace classification.  This is used to group elements that belong to the same namespace.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createNamespaceCollection(@PathVariable String                   serverName,
                                                  @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.NAMESPACE_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }



    /**
     * Create a new agreement with the DataSharingAgreement classification.  This is used to identify an agreement as being related to the sharing of data between two parties.     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/data-sharing-agreement")
    @Operation(summary="createDataSharingAgreement",
            description="Create a new agreement with the DataSharingAgreement classification.  This is used to identify an agreement as being related to the sharing of data between two parties.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createDataSharingAgreement(@PathVariable String                   serverName,
                                                   @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the EventSet classification.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/event-set-collection")
    @Operation(summary="createContextEventCollection",
            description="Create a new collection with the EventSet classification.  This is used to group event schemas together." +
                    " For example, the collection may describe a set of events emitted by a specific system or to disseminate information about a certain situation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createEventSetCollection(@PathVariable String                   serverName,
                                                 @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.EVENT_SET_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new collection with the NamingStandardRuleSet classification.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/naming-standard-rule-set-collection")
    @Operation(summary="createNamingStandardRuleSetCollection",
            description="Create a new collection with the NamingStandardRuleSet classification.  This is used to group event schemas together." +
                    " For example, the collection may describe a set of naming standard rule definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public GUIDResponse createNamingStandardRuleSetCollection(@PathVariable String                   serverName,
                                                              @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createCollection(serverName, OpenMetadataType.NAMING_STANDARD_RULE_SET_COLLECTION_CLASSIFICATION.typeName, requestBody);
    }


    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param serverName             calling user
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
                                                     @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createCollectionFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                         @PathVariable String                  collectionGUID,
                                         @RequestParam boolean                 replaceAllProperties,
                                         @RequestBody  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateCollection(serverName, collectionGUID, replaceAllProperties, requestBody);
    }



    /**
     * Update the status of a digital product.
     *
     * @param serverName         name of called server.
     * @param digitalProductGUID unique identifier of the digital product (returned from createCollection)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/digital-products/{digitalProductGUID}/update-status")
    @Operation(summary="updateDigitalProductStatus",
            description="Update the status of a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse updateDigitalProductStatus(@PathVariable String serverName,
                                                   @PathVariable String digitalProductGUID,
                                                   @RequestBody (required = false)
                                                   DigitalProductStatusRequestBody requestBody)
    {
        return restAPI.updateDigitalProductStatus(serverName, digitalProductGUID, requestBody);
    }


    /**
     * Update the status of an agreement.
     *
     * @param serverName         name of called server.
     * @param agreementGUID unique identifier of the agreement (returned from createCollection)
     * @param requestBody     properties for the new status.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/agreements/{agreementGUID}/update-status")
    @Operation(summary="updateAgreementStatus",
            description="Update the status of an agreement.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/agreement"))

    public VoidResponse updateAgreementStatus(@PathVariable
                                              String                                  serverName,
                                              @PathVariable
                                              String agreementGUID,
                                              @RequestBody (required = false)
                                              AgreementStatusRequestBody requestBody)
    {
        return restAPI.updateAgreementStatus(serverName, agreementGUID, requestBody);
    }





    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param serverName         name of called server
     * @param collectionGUID unique identifier of the collection
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody  description of how the collection will be used.
     * @param makeAnchor     like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
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
                                         @PathVariable String                  collectionGUID,
                                         @PathVariable String                  parentGUID,
                                         @RequestParam boolean                 makeAnchor,
                                         @RequestBody(required = false)
                                         RelationshipRequestBody requestBody)
    {
        return restAPI.attachCollection(serverName, collectionGUID, parentGUID, makeAnchor, requestBody);
    }


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param serverName         name of called server.
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
                                         @PathVariable String                    collectionGUID,
                                         @PathVariable String                    parentGUID,
                                         @RequestBody(required = false)
                                         MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachCollection(serverName, collectionGUID, parentGUID, requestBody);
    }



    /**
     * Link two dependent products.
     *
     * @param serverName         name of called server
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
            description="Link two dependent products.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkDigitalProductDependency(@PathVariable
                                                     String                                serverName,
                                                     @PathVariable
                                                     String consumerDigitalProductGUID,
                                                     @PathVariable
                                                     String consumedDigitalProductGUID,
                                                     @RequestBody (required = false)
                                                     RelationshipRequestBody requestBody)
    {
        return restAPI.linkDigitalProductDependency(serverName, consumerDigitalProductGUID, consumedDigitalProductGUID, requestBody);
    }


    /**
     * Unlink dependent products.
     *
     * @param serverName         name of called server
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
            description="Unlink dependent products.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachDigitalProductDependency(@PathVariable
                                                       String                    serverName,
                                                       @PathVariable
                                                       String consumerDigitalProductGUID,
                                                       @PathVariable
                                                       String consumedDigitalProductGUID,
                                                       @RequestBody (required = false)
                                                       MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachDigitalProductDependency(serverName, consumerDigitalProductGUID, consumedDigitalProductGUID, requestBody);
    }


    /**
     * Attach a subscriber to a subscription.
     *
     * @param serverName         name of called server
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
                                       @PathVariable
                                       String digitalSubscriberGUID,
                                       @PathVariable
                                       String digitalSubscriptionGUID,
                                       @RequestBody (required = false)
                                       RelationshipRequestBody requestBody)
    {
        return restAPI.linkSubscriber(serverName, digitalSubscriberGUID, digitalSubscriptionGUID, requestBody);
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param serverName         name of called server
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
                                         @PathVariable
                                         String digitalSubscriberGUID,
                                         @PathVariable
                                         String digitalSubscriptionGUID,
                                         @RequestBody (required = false)
                                         MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSubscriber(serverName, digitalSubscriberGUID, digitalSubscriptionGUID, requestBody);
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/digital-products/{digitalProductGUID}/product-managers/{digitalProductManagerGUID}/attach")
    @Operation(summary="linkProductManager",
            description="Attach a product manager to a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkProductManager(@PathVariable
                                           String                                serverName,
                                           @PathVariable
                                           String digitalProductGUID,
                                           @PathVariable
                                           String digitalProductManagerGUID,
                                           @RequestBody (required = false)
                                           RelationshipRequestBody requestBody)
    {
        return restAPI.linkProductManager(serverName, digitalProductGUID, digitalProductManagerGUID, requestBody);
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collection/digital-products/{digitalProductGUID}/product-managers/{digitalProductManagerGUID}/detach")
    @Operation(summary="detachProductManager",
            description="Detach a product manager from a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachProductManager(@PathVariable
                                             String                    serverName,
                                             @PathVariable
                                             String digitalProductGUID,
                                             @PathVariable
                                             String digitalProductManagerGUID,
                                             @RequestBody (required = false)
                                             MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachProductManager(serverName, digitalProductGUID, digitalProductManagerGUID, requestBody);
    }



    /**
     * Attach an actor to an agreement.
     *
     * @param serverName         name of called server
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
                                           @PathVariable
                                           String agreementGUID,
                                           @PathVariable
                                           String actorGUID,
                                           @RequestBody (required = false)
                                           RelationshipRequestBody requestBody)
    {
        return restAPI.linkAgreementActor(serverName, agreementGUID, actorGUID, requestBody);
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param serverName         name of called server
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
                                             @PathVariable
                                             String agreementActorRelationshipGUID,
                                             @RequestBody (required = false)
                                             MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachAgreementActor(serverName, agreementActorRelationshipGUID, requestBody);
    }


    /**
     * Attach an agreement to an element involved in its definition.
     *
     * @param serverName         name of called server
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
                                          @PathVariable
                                          String agreementGUID,
                                          @PathVariable
                                          String agreementItemGUID,
                                          @RequestBody (required = false)
                                          RelationshipRequestBody requestBody)
    {
        return restAPI.linkAgreementItem(serverName, agreementGUID, agreementItemGUID, requestBody);
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param serverName         name of called server
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
                                            @PathVariable
                                            String agreementGUID,
                                            @PathVariable
                                            String agreementItemGUID,
                                            @RequestBody (required = false)
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachAgreementItem(serverName, agreementGUID, agreementItemGUID, requestBody);
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param serverName         name of called server
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
                                     @PathVariable
                                     String agreementGUID,
                                     @PathVariable
                                     String externalReferenceGUID,
                                     @RequestBody (required = false)
                                     RelationshipRequestBody requestBody)
    {
        return restAPI.linkContract(serverName, agreementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param serverName         name of called server
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
                                       @PathVariable
                                       String agreementGUID,
                                       @PathVariable
                                       String externalReferenceGUID,
                                       @RequestBody (required = false)
                                       MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachContract(serverName, agreementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Delete a collection.  It is detected from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param cascadedDelete should any nested collections be deleted? If false, the delete fails if there are nested
     *                       collections.  If true, nested collections are delete - but not member elements
     *                       unless they are anchored to the collection
     *
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
                                         @PathVariable String          collectionGUID,
                                         @RequestParam(required = false, defaultValue = "false") boolean cascadedDelete,
                                         @RequestBody(required = false)
                                         MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteCollection(serverName, collectionGUID, cascadedDelete, requestBody);
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of collection details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/collections/{collectionGUID}/members")
    @Operation(summary="getCollectionMembers",
            description="Return a list of elements that are a member of a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public CollectionMembersResponse getCollectionMembers(@PathVariable String serverName,
                                                          @PathVariable String collectionGUID,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                          int    startFrom,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                          int    pageSize)
    {
        return restAPI.getCollectionMembers(serverName, collectionGUID, startFrom, pageSize, null);
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
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

    public CollectionMembersResponse getCollectionMembers(@PathVariable String serverName,
                                                          @PathVariable String collectionGUID,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                          int    startFrom,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                          int    pageSize,
                                                          @RequestBody(required = false)
                                                          ResultsRequestBody requestBody)
    {
        return restAPI.getCollectionMembers(serverName, collectionGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Return a graph of elements that are the nested members of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param serverName         name of called server
     * @param collectionGUID unique identifier of the collection
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     * @param requestBody additional properties for the search
     *
     * @return graph of collection details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/{collectionGUID}/graph")
    @Operation(summary="getCollectionGraph",
            description="Return a graph of elements that are the nested members of a collection along with elements immediately connected to the starting collection.  The result includes a mermaid graph of the returned elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public CollectionGraphResponse getCollectionGraph(@PathVariable String serverName,
                                                      @PathVariable String collectionGUID,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      int    startFrom,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      int    pageSize,
                                                      @RequestBody(required = false)
                                                      ResultsRequestBody requestBody)
    {
        return restAPI.getCollectionGraph(serverName, collectionGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Add an element to a collection.
     *
     * @param serverName               name of called server.
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
                                        @PathVariable String                  collectionGUID,
                                        @PathVariable String                  elementGUID,
                                        @RequestBody(required = false)
                                        RelationshipRequestBody requestBody)
    {
        return restAPI.addToCollection(serverName, collectionGUID, elementGUID, requestBody);
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param serverName               name of called server.
     * @param collectionGUID       unique identifier of the collection.
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
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
                                                   @PathVariable String                  collectionGUID,
                                                   @PathVariable String                  elementGUID,
                                                   @RequestParam boolean                 replaceAllProperties,
                                                   @RequestBody(required = false)
                                                   RelationshipRequestBody requestBody)
    {
        return restAPI.updateCollectionMembership(serverName, collectionGUID, elementGUID, replaceAllProperties, requestBody);
    }


    /**
     * Remove an element from a collection.
     *
     * @param serverName         name of called server.
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
                                             @PathVariable String                    collectionGUID,
                                             @PathVariable String                    elementGUID,
                                             @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeFromCollection(serverName, collectionGUID, elementGUID, requestBody);
    }
}

