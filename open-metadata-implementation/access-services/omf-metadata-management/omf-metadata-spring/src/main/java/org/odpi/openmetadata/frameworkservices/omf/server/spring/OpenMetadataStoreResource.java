/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.translations.TranslationDetailProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.server.OpenMetadataStoreRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * OpenMetadataStoreResource supports the REST APIs for running Open Metadata Store Service
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/open-metadata-store/users/{userId}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Metadata Access Services: Open Metadata Store Services",
     description="Provides generic open metadata retrieval and management services for Open Metadata Access Services (OMASs).",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omf-metadata-management/"))


public class OpenMetadataStoreResource
{
    private final OpenMetadataStoreRESTServices restAPI = new OpenMetadataStoreRESTServices();
    

    /**
     * Return the connection object for the Open Metadata Store's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier for the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")
    @SecurityRequirement(name = "BearerAuthorization")

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for attributes in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllTypes",
            description="Return the list of types loaded into this server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefGalleryResponse getAllTypes(@PathVariable String   serverName,
                                              @PathVariable String   userId)
    {
        return restAPI.getAllTypes(serverName, userId);
    }


    /**
     * Returns all the entity type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/entity-defs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getEntityDefs",
            description="Returns all the entity type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getEntityDefs(@PathVariable String serverName,
                                             @PathVariable String userId)
    {
        return restAPI.findTypeDefsByCategory(serverName, userId, OpenMetadataTypeDefCategory.ENTITY_DEF);
    }


    /**
     * Returns all the relationship type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/relationship-defs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipDefs",
            description="Returns all the relationship type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getRelationshipDefs(@PathVariable String serverName,
                                                   @PathVariable String userId)
    {
        return restAPI.findTypeDefsByCategory(serverName, userId, OpenMetadataTypeDefCategory.RELATIONSHIP_DEF);
    }


    /**
     * Returns all the classification type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/classification-defs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getClassificationDefs",
            description="Returns all the classification type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getClassificationDefs(@PathVariable String serverName,
                                             @PathVariable String userId)
    {
        return restAPI.findTypeDefsByCategory(serverName, userId, OpenMetadataTypeDefCategory.CLASSIFICATION_DEF);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier identifier of the element in the standard null means any.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/external-id")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findTypesByExternalId",
            description="Return the types that are linked to the elements from the specified standard.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse findTypesByExternalId(@PathVariable                   String    serverName,
                                                     @PathVariable                   String    userId,
                                                     @RequestParam(required = false) String    standard,
                                                     @RequestParam(required = false) String    organization,
                                                     @RequestParam(required = false) String    identifier)
    {
        return restAPI.findTypesByExternalId(serverName, userId, standard, organization, identifier);
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param typeName name of type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/sub-types")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSubTypes",
            description="Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the type has no subtypes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getSubTypes(@PathVariable String serverName,
                                           @PathVariable String userId,
                                           @RequestParam String typeName)
    {
        return restAPI.getSubTypes(serverName, userId, typeName);
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException The requested TypeDef is not known in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/guid/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTypeDefByGUID",
            description="Return the TypeDef identified by the GUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefResponse getTypeDefByGUID(@PathVariable String    serverName,
                                            @PathVariable String    userId,
                                            @PathVariable String    guid)
    {
        return restAPI.getTypeDefByGUID(serverName, userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef
     * @return AttributeTypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the guid is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException The requested TypeDef is not known in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-attribute-types/guid/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAttributeTypeDefByGUID",
            description="Return the AttributeTypeDef identified by the GUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public AttributeTypeDefResponse getAttributeTypeDefByGUID(@PathVariable String    serverName,
                                                              @PathVariable String    userId,
                                                              @PathVariable String    guid)
    {
        return restAPI.getAttributeTypeDefByGUID(serverName, userId, guid);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTypeDefByName",
            description="Return the TypeDef identified by the unique name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefResponse getTypeDefByName(@PathVariable String    serverName,
                                            @PathVariable String    userId,
                                            @PathVariable String    name)
    {
        return restAPI.getTypeDefByName(serverName, userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return AttributeTypeDefResponse:
     * AttributeTypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-attribute-types/name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAttributeTypeDefByName",
            description="Return the AttributeTypeDef identified by the unique name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public  AttributeTypeDefResponse getAttributeTypeDefByName(@PathVariable String    serverName,
                                                               @PathVariable String    userId,
                                                               @PathVariable String    name)
    {
        return restAPI.getAttributeTypeDefByName(serverName, userId, name);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @GetMapping(path = "/metadata-elements/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  userId,
                                                                @PathVariable String  elementGUID,
                                                                @RequestParam (required = false, defaultValue = "false")
                                                                              boolean forLineage,
                                                                @RequestParam (required = false, defaultValue = "false")
                                                                              boolean forDuplicateProcessing,
                                                                @RequestParam (required = false, defaultValue = "0")
                                                                              long    effectiveTime)
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);

        if (effectiveTime != 0)
        {
            requestBody.setEffectiveTime(new Date(effectiveTime));
        }

        return restAPI.getMetadataElementByGUID(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  userId,
                                                                @PathVariable String  elementGUID,
                                                                @RequestBody (required = false)
                                                                    GetRequestBody requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                      @PathVariable String          userId,
                                                                      @RequestBody  UniqueNameRequestBody requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, userId, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/guid-by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String                serverName,
                                                           @PathVariable String                userId,
                                                           @RequestBody  UniqueNameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, userId, requestBody);
    }


    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param elementGUID unique identifier of object to retrieve
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementHistory",
            description="Retrieve all the versions of an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataElementsResponse getMetadataElementHistory(@PathVariable String                 serverName,
                                                                  @PathVariable String                 userId,
                                                                  @PathVariable String                 elementGUID,
                                                                  @RequestBody(required = false)
                                                                  HistoryRequestBody     requestBody)
    {
        return restAPI.getMetadataElementHistory(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Retrieve all the versions of an element's classification.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param elementGUID unique identifier of object to retrieve
     * @param classificationName name of classification to retrieve
     * @param requestBody the time window required
     * @return list of classifications or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/classifications/{classificationName}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getClassificationHistory",
            description="Retrieve all the versions of an element's classification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public AttachedClassificationsResponse getClassificationHistory(@PathVariable String                 serverName,
                                                                  @PathVariable String                 userId,
                                                                 @PathVariable String                 elementGUID,
                                                                 @PathVariable String                 classificationName,
                                                                  @RequestBody(required = false)
                                                                  HistoryRequestBody     requestBody)
    {
        return restAPI.getClassificationHistory(serverName, userId, elementGUID, classificationName, requestBody);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMetadataElementsWithString",
            description="Retrieve the metadata elements that contain the requested string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataElementsResponse findMetadataElementsWithString(@PathVariable String                  serverName,
                                                                       @PathVariable String                  userId,
                                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findMetadataElementsWithString(serverName, userId, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param anchorGUID unique identifier of anchor
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/metadata-elements/by-search-string/for-anchor/{anchorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsForAnchor",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied anchorGUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesResponse findElementsForAnchor(@PathVariable String                  serverName,
                                                             @PathVariable String                  userId,
                                                             @PathVariable String                  anchorGUID,
                                                             @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsForAnchor(serverName, userId, anchorGUID, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param anchorDomainName name of open metadata type for the domain
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/metadata-elements/by-search-string/in-anchor-domain/{anchorDomainName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsInAnchorDomain",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied domain name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesListResponse findElementsInAnchorDomain(@PathVariable String                  serverName,
                                                                      @PathVariable String                  userId,
                                                                      @PathVariable String                  anchorDomainName,
                                                                      @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsInAnchorDomain(serverName, userId, anchorDomainName, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */

    @PostMapping(path = "/metadata-elements/by-search-string/in-anchor-scope/{anchorScopeGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsInAnchorScope",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied scope guid.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesListResponse findElementsInAnchorScope(@PathVariable String                  serverName,
                                                                     @PathVariable String                  userId,
                                                                     @PathVariable String                  anchorScopeGUID,
                                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsInAnchorScope(serverName, userId, anchorScopeGUID, requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/any-type")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public RelatedMetadataElementListResponse getAllRelatedMetadataElements(@PathVariable String  serverName,
                                                                            @PathVariable String  userId,
                                                                            @PathVariable String  elementGUID,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int     startingAtEnd,
                                                                            @RequestBody (required = false)
                                                                            ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  userId,
                                                  elementGUID,
                                                  null,
                                                  startingAtEnd,
                                                  requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element via a specific relationship type.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/type/{relationshipTypeName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element via a specific relationship type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public RelatedMetadataElementListResponse getRelatedMetadataElements(@PathVariable String  serverName,
                                                                         @PathVariable String  userId,
                                                                         @PathVariable String  elementGUID,
                                                                         @PathVariable String  relationshipTypeName,
                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                         int     startingAtEnd,
                                                                         @RequestBody (required = false)
                                                                         ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  userId,
                                                  elementGUID,
                                                  relationshipTypeName,
                                                  startingAtEnd,
                                                  requestBody);
    }


    /**
     * Retrieve the relationships linking the supplied elements.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship

     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-any-type/to-elements/{metadataElementAtEnd2GUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataRelationshipListResponse getAllMetadataElementRelationships(@PathVariable String  serverName,
                                                                                   @PathVariable String  userId,
                                                                                   @PathVariable String  metadataElementAtEnd1GUID,
                                                                                   @PathVariable String  metadataElementAtEnd2GUID,
                                                                                   @RequestBody(required = false)
                                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       userId,
                                                       metadataElementAtEnd1GUID,
                                                       null,
                                                       metadataElementAtEnd2GUID,
                                                       requestBody);
    }


    /**
     * Retrieve the relationships linking the supplied elements via a specific type of relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-type/{relationshipTypeName}/to-elements/{metadataElementAtEnd2GUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements via a specific type of relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataRelationshipListResponse getMetadataElementRelationships(@PathVariable String  serverName,
                                                                                @PathVariable String  userId,
                                                                                @PathVariable String  metadataElementAtEnd1GUID,
                                                                                @PathVariable String  relationshipTypeName,
                                                                                @PathVariable String  metadataElementAtEnd2GUID,
                                                                                @RequestBody (required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       userId,
                                                       metadataElementAtEnd1GUID,
                                                       relationshipTypeName,
                                                       metadataElementAtEnd2GUID,
                                                       requestBody);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-conditions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMetadataElements",
            description="Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataElementsResponse findMetadataElements(@PathVariable String          serverName,
                                                             @PathVariable String          userId,
                                                             @RequestBody (required = false)
                                                                           FindRequestBody requestBody)
    {
        return restAPI.findMetadataElements(serverName, userId, requestBody);
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request
     * @param userId the userId of the requesting user
     * @param elementGUID  unique identifier for the element
     * @param requestBody effective time and asOfTime
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/with-anchored-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    public OpenMetadataGraphResponse getAnchoredElementsGraph(@PathVariable String          serverName,
                                                              @PathVariable String          userId,
                                                              @PathVariable String          elementGUID,
                                                              @RequestBody (required = false)
                                                              ResultsRequestBody requestBody)
    {
        return restAPI.getAnchoredElementsGraph(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-search-conditions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRelationshipsBetweenMetadataElements",
            description="Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataRelationshipListResponse findRelationshipsBetweenMetadataElements(@PathVariable String          serverName,
                                                                                         @PathVariable String          userId,
                                                                                         @RequestBody  FindRelationshipRequestBody requestBody)
    {
        return restAPI.findRelationshipsBetweenMetadataElements(serverName, userId, requestBody);
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param relationshipGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @GetMapping(path = "/relationships/by-guid/{relationshipGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipByGUID",
            description="Retrieve the relationship using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataRelationshipResponse getRelationshipByGUID(@PathVariable String  serverName,
                                                                  @PathVariable String  userId,
                                                                  @PathVariable String  relationshipGUID,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                               boolean forLineage,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                               boolean forDuplicateProcessing,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                               long    effectiveTime)
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);

        if (effectiveTime != 0)
        {
            requestBody.setEffectiveTime(new Date(effectiveTime));
        }

        return restAPI.getRelationshipByGUID(serverName, userId, relationshipGUID, requestBody);
    }




    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param relationshipGUID unique identifier for the metadata element
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-guid/{relationshipGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipByGUID",
            description="Retrieve the relationship using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    @Deprecated
    public OpenMetadataRelationshipResponse getRelationshipByGUID(@PathVariable String  serverName,
                                                                  @PathVariable String  userId,
                                                                  @PathVariable String  relationshipGUID,
                                                                  @RequestBody (required = false) GetRequestBody requestBody)
    {
        return restAPI.getRelationshipByGUID(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Retrieve all the versions of a relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipGUID unique identifier of object to retrieve
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/relationships/{relationshipGUID}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipHistory",
            description="Retrieve all the versions of a relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public OpenMetadataRelationshipListResponse getRelationshipHistory(@PathVariable String                 serverName,
                                                                       @PathVariable String                 userId,
                                                                       @PathVariable String                 relationshipGUID,
                                                                       @RequestBody(required = false)
                                                                      HistoryRequestBody     requestBody)
    {
        return restAPI.getRelationshipHistory(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createMetadataElementInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @RequestBody NewOpenMetadataElementRequestBody requestBody)
    {
        return restAPI.createMetadataElementInStore(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element in the metadata store using a template.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createMetadataElementFromTemplate(@PathVariable String                          serverName,
                                                          @PathVariable String                          userId,
                                                          @RequestBody  OpenMetadataTemplateRequestBody requestBody)
    {
        return restAPI.createMetadataElementFromTemplate(serverName, userId, requestBody);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new properties
     *
     * @return boolean or
     *  InvalidParameterException either the unique identifier or the properties are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public BooleanResponse updateMetadataElementInStore(@PathVariable String                      serverName,
                                                        @PathVariable String                      userId,
                                                        @PathVariable String                      metadataElementGUID,
                                                        @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Update the zone membership to increase its visibility.  The publishZones are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/publish")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse publishMetadataElement(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    metadataElementGUID,
                                               @RequestBody (required = false)
                                                   MetadataSourceRequestBody requestBody)
    {
        return restAPI.publishMetadataElement(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Update the zone membership to decrease its visibility.  The defaultZones are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/withdraw")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse withdrawMetadataElement(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @PathVariable String                    metadataElementGUID,
                                                @RequestBody (required = false)
                                                    MetadataSourceRequestBody requestBody)
    {
        return restAPI.withdrawMetadataElement(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-effectivity")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateMetadataElementEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            userId,
                                                                @PathVariable String                            metadataElementGUID,
                                                                @RequestBody UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementEffectivityInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody delete request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public  VoidResponse deleteMetadataElementInStore(@PathVariable String            serverName,
                                                      @PathVariable String            userId,
                                                      @PathVariable String            metadataElementGUID,
                                                      @RequestBody(required = false)
                                                          OpenMetadataDeleteRequestBody requestBody)
    {
        return restAPI.deleteMetadataElementInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Archive a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to archive this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/archive")
    @SecurityRequirement(name = "BearerAuthorization")

    public  VoidResponse archiveMetadataElementInStore(@PathVariable String            serverName,
                                                       @PathVariable String            userId,
                                                       @PathVariable String            metadataElementGUID,
                                                       @RequestBody(required = false)  DeleteRequestBody requestBody)
    {
        return restAPI.archiveMetadataElementInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param requestBody properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse classifyMetadataElementInStore(@PathVariable String                       serverName,
                                                       @PathVariable String                       userId,
                                                       @PathVariable String                       metadataElementGUID,
                                                       @PathVariable String                       classificationName,
                                                       @RequestBody NewOpenMetadataClassificationRequestBody requestBody)
    {
        return restAPI.classifyMetadataElementInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody new properties for the classification
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse reclassifyMetadataElementInStore(@PathVariable String                      serverName,
                                                         @PathVariable String                      userId,
                                                         @PathVariable String                      metadataElementGUID,
                                                         @PathVariable String                      classificationName,
                                                         @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.reclassifyMetadataElementInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-effectivity")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateClassificationEffectivityInStore(@PathVariable String                            serverName,
                                                               @PathVariable String                            userId,
                                                               @PathVariable String                            metadataElementGUID,
                                                               @PathVariable String                            classificationName,
                                                               @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateClassificationEffectivityInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to remove this classification
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse declassifyMetadataElementInStore(@PathVariable String            serverName,
                                                         @PathVariable String            userId,
                                                         @PathVariable String            metadataElementGUID,
                                                         @PathVariable String            classificationName,
                                                         @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.declassifyMetadataElementInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody the properties of the relationship
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createRelatedElementsInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @RequestBody  NewRelatedElementsRequestBody requestBody)
    {
        return restAPI.createRelatedElementsInStore(serverName, userId, requestBody);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody new properties for the relationship
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to update this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/update-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateRelatedElementsInStore(@PathVariable String                      serverName,
                                                     @PathVariable String                      userId,
                                                     @PathVariable String                      relationshipGUID,
                                                     @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsInStore(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/update-effectivity")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateRelatedElementsEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            userId,
                                                                @PathVariable String                            relationshipGUID,
                                                                @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsEffectivityInStore(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse deleteRelationshipInStore(@PathVariable String                        serverName,
                                                  @PathVariable String                        userId,
                                                  @PathVariable String                        relationshipGUID,
                                                  @RequestBody  OpenMetadataDeleteRequestBody requestBody)
    {
        return restAPI.deleteRelationshipInStore(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @return void or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/multi-language/set-translation/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse setTranslation(@PathVariable String            serverName,
                                       @PathVariable String            userId,
                                       @PathVariable String            elementGUID,
                                       @RequestBody TranslationDetailProperties translationDetail)
    {
        return restAPI.setTranslation(serverName, userId, elementGUID, translationDetail);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the language is null or not known or not unique (add locale)
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/multi-language/clear-translation/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse clearTranslation(@PathVariable String          serverName,
                                         @PathVariable String          userId,
                                         @PathVariable String          elementGUID,
                                         @RequestParam(required = false)
                                                       String language,
                                         @RequestParam(required = false)
                                                       String locale,
                                         @RequestBody (required=false)
                                                       NullRequestBody requestBody)
    {
        return restAPI.clearTranslation(serverName, userId, elementGUID, language, locale, requestBody);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/multi-language/get-translation/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    public TranslationDetailResponse getTranslation(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String elementGUID,
                                                    @RequestParam(required = false)
                                                                  String language,
                                                    @RequestParam(required = false)
                                                                  String locale)
    {
        return restAPI.getTranslation(serverName, userId, elementGUID, language, locale);
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/multi-language/get-translations/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    public TranslationListResponse getTranslations(@PathVariable String serverName,
                                                   @PathVariable String userId,
                                                   @PathVariable String elementGUID,
                                                   @RequestParam int    startFrom,
                                                   @RequestParam int    pageSize)
    {
        return restAPI.getTranslations(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Log an audit message about an asset.
     *
     * @param serverName            name of server instance to route request to
     * @param userId                userId of user making request.
     * @param assetGUID             unique identifier for asset.
     * @param callingService     unique name for governance service.
     * @param message               message to log
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records/{callingService}")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse logAssetAuditMessage(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String assetGUID,
                                             @PathVariable String callingService,
                                             @RequestBody  String message)
    {
        return restAPI.logAssetAuditMessage(serverName, userId, assetGUID, callingService, message);
    }
}
