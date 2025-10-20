/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externallinks.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.externallinks.server.ExternalLinksRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ExternalLinksResource provides part of the server-side implementation of the External Links OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: External Links OMVS", description="External references identify web pages where the information about useful resources such as downloadable data sources and models, or key documentation are located. External identifiers capture the identifiers for data items residing outside of the open metadata ecosystem whose contents have been synchronised into the open metadata system.  They are linked to their open metadata equivalent elements.  The External Links OMVS provides APIs for supporting the creation and editing of external references, and external identifiers along with queries to locate them.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/external-links/overview/"))

public class ExternalLinksResource
{
    private final ExternalLinksRESTServices restAPI = new ExternalLinksRESTServices();

    /**
     * Default constructor
     */
    public ExternalLinksResource()
    {
    }


    /**
     * Create an external reference.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the external reference.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/external-references")

    @Operation(summary="createExternalReference",
            description="Create an external reference.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public GUIDResponse createExternalReference(@PathVariable String                               serverName,
                                                @PathVariable String             urlMarker,
                                                @RequestBody (required = false)
                                                NewElementRequestBody requestBody)
    {
        return restAPI.createExternalReference(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent an external reference using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
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
    @PostMapping(path = "/external-references/from-template")
    @Operation(summary="createExternalReferenceFromTemplate",
            description="Create a new metadata element to represent an external reference using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public GUIDResponse createExternalReferenceFromTemplate(@PathVariable
                                                            String              serverName,
                                                            @PathVariable String             urlMarker,
                                                            @RequestBody (required = false)
                                                            TemplateRequestBody requestBody)
    {
        return restAPI.createExternalReferenceFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of an external reference.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID unique identifier of the external reference (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/update")
    @Operation(summary="updateExternalReference",
            description="Update the properties of an external reference.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse updateExternalReference(@PathVariable
                                                String                                  serverName,
                                                @PathVariable String             urlMarker,
                                                @PathVariable
                                                String                                  externalReferenceGUID,
                                                @RequestBody (required = false)
                                                UpdateElementRequestBody requestBody)
    {
        return restAPI.updateExternalReference(serverName, urlMarker, externalReferenceGUID, requestBody);
    }



    /**
     * Attach an external reference to an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID       unique identifier of the element
     * @param externalReferenceGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/external-references/{externalReferenceGUID}/attach")
    @Operation(summary="linkExternalReference",
            description="Attach an external reference to an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse linkExternalReference(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String elementGUID,
                                              @PathVariable
                                              String externalReferenceGUID,
                                              @RequestBody (required = false)
                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkExternalReference(serverName, urlMarker, elementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Detach an external reference from an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID       unique identifier of the element
     * @param externalReferenceGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/external-references/{externalReferenceGUID}/detach")
    @Operation(summary="detachExternalReference",
            description="Detach an element from an IT profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse detachExternalReference(@PathVariable
                                                String                    serverName,
                                                @PathVariable String             urlMarker,
                                                @PathVariable
                                                String elementGUID,
                                                @PathVariable
                                                String externalReferenceGUID,
                                                @RequestBody (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachExternalReference(serverName, urlMarker, elementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Attach an external media reference to an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID       unique identifier of the element
     * @param externalReferenceGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/media-references/{externalReferenceGUID}/attach")
    @Operation(summary="linkMediaReference",
            description="Attach an external media reference to an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse linkMediaReference(@PathVariable
                                           String                     serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String elementGUID,
                                           @PathVariable
                                           String externalReferenceGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkMediaReference(serverName, urlMarker, elementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Detach an external media reference from an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID       unique identifier of the element
     * @param externalReferenceGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/media-references/{externalReferenceGUID}/detach")
    @Operation(summary="detachMediaReference",
            description="Detach an external media reference from an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse detachMediaReference(@PathVariable
                                             String                    serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable
                                             String elementGUID,
                                             @PathVariable
                                             String externalReferenceGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachMediaReference(serverName, urlMarker, elementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Attach an element to its external document reference.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID       unique identifier of the element
     * @param externalReferenceGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/cited-document-references/{externalReferenceGUID}/attach")
    @Operation(summary="linkCitedDocumentReference",
            description="Attach an element to its external document reference.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse linkCitedDocumentReference(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String elementGUID,
                                                   @PathVariable
                                                   String externalReferenceGUID,
                                                   @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkCitedDocumentReference(serverName, urlMarker, elementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Detach an element from its external document reference.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID       unique identifier of the element
     * @param externalReferenceGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/cited-document-references/{externalReferenceGUID}/detach")
    @Operation(summary="detachCitedDocumentReference",
            description="Detach an element from its external document reference.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse detachCitedDocumentReference(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable
                                                     String elementGUID,
                                                     @PathVariable
                                                     String externalReferenceGUID,
                                                     @RequestBody (required = false)
                                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachCitedDocumentReference(serverName, urlMarker, elementGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Delete an external reference.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/delete")
    @Operation(summary="deleteExternalReference",
            description="Delete an external reference.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public VoidResponse deleteExternalReference(@PathVariable
                                                String                    serverName,
                                                @PathVariable String             urlMarker,
                                                @PathVariable
                                                String                    externalReferenceGUID,
                                                @RequestBody (required = false)
                                                DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteExternalReference(serverName, urlMarker, externalReferenceGUID, requestBody);
    }


    /**
     * Returns the list of external references with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-references/by-name")
    @Operation(summary="getExternalReferencesByName",
            description="Returns the list of external references with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public OpenMetadataRootElementsResponse getExternalReferencesByName(@PathVariable
                                                                        String            serverName,
                                                                        @PathVariable String             urlMarker,
                                                                        @RequestBody (required = false)
                                                                        FilterRequestBody requestBody)
    {
        return restAPI.getExternalReferencesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of external reference metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-references/by-search-string")
    @Operation(summary="findExternalReferences",
            description="Retrieve the list of external reference metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public OpenMetadataRootElementsResponse findExternalReferences(@PathVariable
                                                                   String                  serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestBody (required = false)
                                                                   SearchStringRequestBody requestBody)
    {
        return restAPI.findExternalReferences(serverName, urlMarker,  requestBody);
    }


    /**
     * Return the properties of a specific external reference.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/retrieve")
    @Operation(summary="getExternalReferenceByGUID",
            description="Return the properties of a specific external reference.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-reference"))

    public OpenMetadataRootElementResponse getExternalReferenceByGUID(@PathVariable
                                                                      String             serverName,
                                                                      @PathVariable String             urlMarker,
                                                                      @PathVariable
                                                                      String             externalReferenceGUID,
                                                                      @RequestBody (required = false)
                                                                      GetRequestBody requestBody)
    {
        return restAPI.getExternalReferenceByGUID(serverName, urlMarker, externalReferenceGUID, requestBody);
    }


    /* =====================================================================================================================
     * Work with External Identifiers
     */


    /**
     * Add the description of a specific external identifier and link it to the associated metadata element.  Note, the external identifier is anchored to the scope.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/elements/{elementGUID}/external-identifiers/add")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "addExternalIdentifier",
            description = "Add the description of a specific external identifier and link it to the associated metadata element.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse addExternalIdentifier(@PathVariable String                               serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable String        elementGUID,
                                              @RequestBody NewExternalIdRequestBody requestBody)
    {
        return restAPI.addExternalIdentifier(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Update the properties of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalIdGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-identifiers/{externalIdGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "updateExternalIdentifier",
            description = "Update the properties of a specific external identifier.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse updateExternalIdentifier(@PathVariable String                   serverName,
                                                 @PathVariable String                   urlMarker,
                                                 @PathVariable String                   externalIdGUID,
                                                 @RequestBody  (required = false)
                                                 UpdateElementRequestBody requestBody)
    {
        return restAPI.updateExternalIdentifier(serverName, urlMarker, externalIdGUID, requestBody);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalIdGUID unique identifier (GUID) of the external id
     * @param requestBody  additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-identifiers/{externalIdGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "deleteExternalIdentifier",
            description = "Remove an external identifier from an existing external identifier.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse deleteExternalIdentifier(@PathVariable String serverName,
                                                 @PathVariable String urlMarker,
                                                 @PathVariable String externalIdGUID,
                                                 @RequestBody(required = false)
                                                 DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteExternalIdentifier(serverName, urlMarker, externalIdGUID, requestBody);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalIdLinkGUID unique identifier (GUID) of the external Id Link relationship
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-identifiers/links/{externalIdLinkGUID}/synchronized")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "confirmSynchronization",
            description = "Confirm that the values of a particular metadata element have been synchronized.  This is important from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse confirmSynchronization(@PathVariable String serverName,
                                               @PathVariable String urlMarker,
                                               @PathVariable String externalIdLinkGUID,
                                               @RequestBody (required = false)
                                               UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.confirmSynchronization(serverName, urlMarker, externalIdLinkGUID, requestBody);
    }


    /**
     * Returns the list of external ids with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-identifiers/by-name")
    @Operation(summary="getExternalIdsByName",
            description="Returns the list of external identifiers with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-identifier"))

    public OpenMetadataRootElementsResponse getExternalIdsByName(@PathVariable
                                                                 String            serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getExternalIdsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of external ids metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-identifiers/by-search-string")
    @Operation(summary="findExternalIds",
            description="Retrieve the list of external identifiers metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-identifier"))

    public OpenMetadataRootElementsResponse findExternalIds(@PathVariable
                                                            String                  serverName,
                                                            @PathVariable String             urlMarker,
                                                            @RequestBody (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findExternalIds(serverName, urlMarker,  requestBody);
    }


    /**
     * Return the properties of a specific external id.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param externalIdGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-identifiers/{externalIdGUID}/retrieve")
    @Operation(summary="getExternalIdByGUID",
            description="Return the properties of a specific external identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/external-identifier"))

    public OpenMetadataRootElementResponse getExternalIdByGUID(@PathVariable
                                                               String             serverName,
                                                               @PathVariable String             urlMarker,
                                                               @PathVariable
                                                               String externalIdGUID,
                                                               @RequestBody (required = false)
                                                               GetRequestBody requestBody)
    {
        return restAPI.getExternalIdByGUID(serverName, urlMarker, externalIdGUID, requestBody);
    }
}
