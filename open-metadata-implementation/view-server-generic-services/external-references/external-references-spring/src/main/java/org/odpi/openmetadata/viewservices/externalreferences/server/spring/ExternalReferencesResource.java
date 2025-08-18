/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externalreferences.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.externalreferences.server.ExternalReferencesRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ExternalReferencesResource provides part of the server-side implementation of the External References OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: External References OMVS", description="The External References OMVS provides APIs for supporting the creation and editing of external references, schema attributes and user identities.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/external-references/overview/"))

public class ExternalReferencesResource
{
    private final ExternalReferencesRESTServices restAPI = new ExternalReferencesRESTServices();

    /**
     * Default constructor
     */
    public ExternalReferencesResource()
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
                                                DeleteRequestBody requestBody)
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
                                             DeleteRequestBody requestBody)
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
                                                     DeleteRequestBody requestBody)
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
                                                DeleteRequestBody requestBody)
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
}
