/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.collectionmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
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
     * CollectionsInterface methods
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
     * Returns the list of collections with a particular classification.
     *
     * @param serverName         name of called server
     * @param requestBody        name of the classification - if null, all collections are returned
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/collections/by-classifications")
    @Operation(summary="getClassifiedCollections",
            description="Returns the list of collections with a particular classification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public CollectionsResponse getClassifiedCollections(@PathVariable String            serverName,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                                         int    startFrom,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                                         int    pageSize,
                                                        @RequestBody(required = false)
                                                            FilterRequestBody requestBody)
    {
        return restAPI.getClassifiedCollections(serverName, startFrom, pageSize, requestBody);
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
                                         @RequestBody  NewCollectionRequestBody requestBody)
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
                                             @RequestBody  NewCollectionRequestBody requestBody)
    {
        return restAPI.createRootCollection(serverName, requestBody);
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
                                                 @RequestBody  NewCollectionRequestBody requestBody)
    {
        return restAPI.createDataSpecCollection(serverName, requestBody);
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
                                                       @RequestBody  NewCollectionRequestBody requestBody)
    {
        return restAPI.createDataDictionaryCollection(serverName, requestBody);
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
                                               @RequestBody  NewCollectionRequestBody requestBody)
    {
        return restAPI.createFolderCollection(serverName, requestBody);
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
     * Create a new collection that represents a digital product.
     *
     * @param serverName   name of called server.
     * @param requestBody properties for the collection and attached DigitalProduct classification
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products")
    @Operation(summary="createDigitalProduct",
            description="Create a new collection that represents a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public GUIDResponse createDigitalProduct(@PathVariable String                       serverName,
                                             @RequestBody  NewDigitalProductRequestBody requestBody)
    {
        return restAPI.createDigitalProduct(serverName, requestBody);
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

    public VoidResponse updateCollection(@PathVariable String               serverName,
                                         @PathVariable String               collectionGUID,
                                         @RequestParam boolean              replaceAllProperties,
                                         @RequestBody CollectionProperties requestBody)
    {
        return restAPI.updateCollection(serverName, collectionGUID, replaceAllProperties, requestBody);
    }


    /**
     * Update the properties of the DigitalProduct classification attached to a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the DigitalProduct classification.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/{collectionGUID}/update")
    @Operation(summary="updateDigitalProduct",
            description="Update the properties of the DigitalProduct classification attached to a collection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse   updateDigitalProduct(@PathVariable String                   serverName,
                                               @PathVariable String                   collectionGUID,
                                               @RequestParam boolean                  replaceAllProperties,
                                               @RequestBody  DigitalProductProperties requestBody)
    {
        return restAPI.updateDigitalProduct(serverName, collectionGUID, replaceAllProperties, requestBody);
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

    public VoidResponse attachCollection(@PathVariable String                 serverName,
                                         @PathVariable String                 collectionGUID,
                                         @PathVariable String                 parentGUID,
                                         @RequestParam boolean                makeAnchor,
                                         @RequestBody  ResourceListProperties requestBody)
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
            description="Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/collection"))

    public VoidResponse detachCollection(@PathVariable String          serverName,
                                         @PathVariable String          collectionGUID,
                                         @PathVariable String          parentGUID,
                                         @RequestBody(required = false)
                                                       NullRequestBody requestBody)
    {
        return restAPI.detachCollection(serverName, collectionGUID, parentGUID, requestBody);
    }


    /**
     * Delete a collection.  It is detected from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param cascadedDelete should nested collections be deleted? If false, the delete fails if there are nested
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
                                                       NullRequestBody requestBody)
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
     * @return list of asset details
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
        return restAPI.getCollectionMembers(serverName, collectionGUID, startFrom, pageSize);
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

    public VoidResponse addToCollection(@PathVariable String                         serverName,
                                        @PathVariable String                         collectionGUID,
                                        @PathVariable String                         elementGUID,
                                        @RequestBody(required = false)
                                                      CollectionMembershipProperties requestBody)
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

    public VoidResponse updateCollectionMembership(@PathVariable String                         serverName,
                                                   @PathVariable String                         collectionGUID,
                                                   @PathVariable String                         elementGUID,
                                                   @RequestParam boolean                        replaceAllProperties,
                                                   @RequestBody  CollectionMembershipProperties requestBody)
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

    public VoidResponse removeFromCollection(@PathVariable String          serverName,
                                             @PathVariable String          collectionGUID,
                                             @PathVariable String          elementGUID,
                                             @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeFromCollection(serverName, collectionGUID, elementGUID, requestBody);
    }
}

