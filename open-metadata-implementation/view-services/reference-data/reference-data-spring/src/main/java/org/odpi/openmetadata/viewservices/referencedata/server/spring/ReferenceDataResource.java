/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.referencedata.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.referencedata.server.ReferenceDataRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ReferenceDataResource provides part of the server-side implementation of the Reference Data OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/reference-data")

@Tag(name="API: Reference Data OMVS", description="Reference data is used by systems as valid value lists.  Although the lists of reference data may be similar from system to system, they often vary in small details and use different identifiers.  Reference data management supports the capture, mapping and synchronization of the reference data across your systems, simplifying data pipeline processing and reducing data integration errors.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/reference-data/overview/"))

public class ReferenceDataResource
{
    private final ReferenceDataRESTServices restAPI = new ReferenceDataRESTServices();

    /**
     * Default constructor
     */
    public ReferenceDataResource()
    {
    }



    /**
     * Create a validValueDefinition.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the validValueDefinition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-value-definitions")

    @Operation(summary="createValidValueDefinition",
            description="Create a validValueDefinition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public GUIDResponse createValidValueDefinition(@PathVariable String serverName,
                                                   @RequestBody (required = false)
                                                   NewElementRequestBody requestBody)
    {
        return restAPI.createValidValueDefinition(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a validValueDefinition using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-value-definitions/from-template")
    @Operation(summary="createValidValueDefinitionFromTemplate",
            description="Create a new metadata element to represent a validValueDefinition using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public GUIDResponse createValidValueDefinitionFromTemplate(@PathVariable
                                                               String              serverName,
                                                               @RequestBody (required = false)
                                                               TemplateRequestBody requestBody)
    {
        return restAPI.createValidValueDefinitionFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a validValueDefinition.
     *
     * @param serverName         name of called server.
     * @param validValueDefinitionGUID unique identifier of the validValueDefinition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-value-definitions/{validValueDefinitionGUID}/update")
    @Operation(summary="updateValidValueDefinition",
            description="Update the properties of a validValueDefinition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse updateValidValueDefinition(@PathVariable
                                                   String                                  serverName,
                                                   @PathVariable
                                                   String                                  validValueDefinitionGUID,
                                                   @RequestBody (required = false)
                                                   UpdateElementRequestBody requestBody)
    {
        return restAPI.updateValidValueDefinition(serverName, validValueDefinitionGUID, requestBody);
    }


    /**
     * Attach a valid value to an implementation - probably a referenceable.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param elementGUID       unique identifier of the element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionGUID}/implementation/elements/{elementGUID}/attach")
    @Operation(summary="linkValidValueImplementation",
            description="Attach a valid value to an implementation - probably a referenceable.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkValidValueImplementation(@PathVariable String serverName,
                                                     @PathVariable String validValueDefinitionGUID,
                                                     @PathVariable String elementGUID,
                                                     @RequestBody (required = false)
                                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkValidValuesAssignment(serverName, validValueDefinitionGUID, elementGUID, requestBody);
    }


    /**
     * Detach a valid value from an implementation - probably a referenceable.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param elementGUID       unique identifier of the element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionGUID}/implementation/elements/{elementGUID}/detach")
    @Operation(summary="detachValidValueImplementation",
            description="Detach a valid value from an implementation - probably a referenceable.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachValidValueImplementation(@PathVariable String serverName,
                                                       @PathVariable String validValueDefinitionGUID,
                                                       @PathVariable String elementGUID,
                                                       @RequestBody (required = false)
                                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachValidValueImplementation(serverName, validValueDefinitionGUID, elementGUID, requestBody);
    }


    /**
     * Attach a valid value to a consumer - probably a schema element or data set.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/valid-values-assignment/{validValueDefinitionGUID}/attach")
    @Operation(summary="linkValidValuesAssignment",
            description="Attach a valid value to a consumer - probably a schema element or data set.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkValidValuesAssignment(@PathVariable
                                                  String                     serverName,
                                                  @PathVariable
                                                  String elementGUID,
                                                  @PathVariable
                                                  String validValueDefinitionGUID,
                                                  @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkValidValuesAssignment(serverName, elementGUID, validValueDefinitionGUID, requestBody);
    }


    /**
     * Detach a valid value from a consumer - probably a schema element or data set.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/valid-values-assignment/{validValueDefinitionGUID}/detach")
    @Operation(summary="detachValidValuesAssignment",
            description="Detach a valid value from a consumer - probably a schema element or data set.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachValidValuesAssignment(@PathVariable
                                                    String                    serverName,
                                                    @PathVariable
                                                    String elementGUID,
                                                    @PathVariable
                                                    String validValueDefinitionGUID,
                                                    @RequestBody (required = false)
                                                    DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachValidValuesAssignment(serverName, elementGUID, validValueDefinitionGUID, requestBody);
    }



    /**
     * Attach a valid value to a tagged element.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/reference-value-assignment/{validValueDefinitionGUID}/attach")
    @Operation(summary="linkReferenceValueAssignment",
            description="Attach a valid value to a tagged element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkReferenceValueAssignment(@PathVariable String serverName,
                                                     @PathVariable String elementGUID,
                                                     @PathVariable String validValueDefinitionGUID,
                                                     @RequestBody (required = false)
                                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkReferenceValueAssignment(serverName, elementGUID, validValueDefinitionGUID, requestBody);
    }


    /**
     * Detach a valid value from a tagged element
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param validValueDefinitionGUID unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/reference-value-assignment/{validValueDefinitionGUID}/detach")
    @Operation(summary="detachReferenceValueAssignment",
            description="Detach a valid value from a tagged element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachReferenceValueAssignment(@PathVariable String                    serverName,
                                                       @PathVariable String elementGUID,
                                                       @PathVariable
                                                           String validValueDefinitionGUID,
                                                       @RequestBody (required = false)
                                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachReferenceValueAssignment(serverName, elementGUID, validValueDefinitionGUID, requestBody);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionOneGUID}/associated-valid-values/{validValueDefinitionTwoGUID}/attach")
    @Operation(summary="linkAssociatedValidValues",
            description="Attach a valid value to one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkAssociatedValidValues(@PathVariable String serverName,
                                                  @PathVariable String validValueDefinitionOneGUID,
                                                  @PathVariable String validValueDefinitionTwoGUID,
                                                  @RequestBody (required = false)
                                                      NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAssociatedValidValues(serverName, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionOneGUID}/associated-valid-values/{validValueDefinitionTwoGUID}/detach")
    @Operation(summary="detachAssociatedValidValues",
            description="Detach a valid value from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachAssociatedValidValues(@PathVariable String serverName,
                                                    @PathVariable String validValueDefinitionOneGUID,
                                                    @PathVariable String validValueDefinitionTwoGUID,
                                                    @RequestBody (required = false)
                                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAssociatedValidValues(serverName, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
    }



    /**
     * Attach a valid value to one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionOneGUID}/consistent-valid-values/{validValueDefinitionTwoGUID}/attach")
    @Operation(summary="linkConsistentValidValues",
            description="Attach a valid value to one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkConsistentValidValues(@PathVariable String serverName,
                                                  @PathVariable String validValueDefinitionOneGUID,
                                                  @PathVariable String validValueDefinitionTwoGUID,
                                                  @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConsistentValidValues(serverName, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionOneGUID}/consistent-valid-values/{validValueDefinitionTwoGUID}/detach")
    @Operation(summary="detachConsistentValidValues",
            description="Detach a valid value from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachConsistentValidValues(@PathVariable String serverName,
                                                    @PathVariable String validValueDefinitionOneGUID,
                                                    @PathVariable String validValueDefinitionTwoGUID,
                                                    @RequestBody (required = false)
                                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachConsistentValidValues(serverName, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionOneGUID}/mapped-valid-values/{validValueDefinitionTwoGUID}/attach")
    @Operation(summary="linkMappedValidValues",
            description="Attach a valid value to one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkMappedValidValues(@PathVariable String serverName,
                                                  @PathVariable String validValueDefinitionOneGUID,
                                                  @PathVariable String validValueDefinitionTwoGUID,
                                                  @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkMappedValidValues(serverName, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionOneGUID}/mapped-valid-values/{validValueDefinitionTwoGUID}/detach")
    @Operation(summary="detachMappedValidValues",
            description="Detach a valid value from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachMappedValidValues(@PathVariable String serverName,
                                                @PathVariable String validValueDefinitionOneGUID,
                                                @PathVariable String validValueDefinitionTwoGUID,
                                                @RequestBody (required = false)
                                                    DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachMappedValidValues(serverName, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
    }


    /**
     * Attach a valid value to a valid value set.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID          unique identifier of the super validValueDefinition
     * @param nestedValidValueDefinitionGUID            unique identifier of the nested validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionGUID}/members/{nestedValidValueDefinitionGUID}/attach")
    @Operation(summary="linkValidValueMember",
            description="Attach a valid value to a valid value set.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse linkValidValueMember(@PathVariable String  serverName,
                                             @PathVariable String validValueDefinitionGUID,
                                             @PathVariable String nestedValidValueDefinitionGUID,
                                             @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkValidValueMember(serverName, validValueDefinitionGUID, nestedValidValueDefinitionGUID, requestBody);
    }


    /**
     * Detach a valid value from a valid value set.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID          unique identifier of the super validValueDefinition
     * @param nestedValidValueDefinitionGUID            unique identifier of the nested validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-values/{validValueDefinitionGUID}/members/{nestedValidValueDefinitionGUID}/detach")
    @Operation(summary="detachValidValueMember",
            description="Detach a valid value from a valid value set.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse detachValidValueMember(@PathVariable String                    serverName,
                                               @PathVariable String validValueDefinitionGUID,
                                               @PathVariable String nestedValidValueDefinitionGUID,
                                               @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachValidValueMember(serverName, validValueDefinitionGUID, nestedValidValueDefinitionGUID, requestBody);
    }



    /**
     * Delete a validValueDefinition.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/valid-value-definitions/{validValueDefinitionGUID}/delete")
    @Operation(summary="deleteValidValueDefinition",
            description="Delete a validValueDefinition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public VoidResponse deleteValidValueDefinition(@PathVariable
                                                   String            serverName,
                                                   @PathVariable
                                                   String            validValueDefinitionGUID,
                                                   @RequestBody (required = false)
                                                   DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteValidValueDefinition(serverName, validValueDefinitionGUID, requestBody);
    }


    /**
     * Returns the list of validValueDefinitions with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-value-definitions/by-name")
    @Operation(summary="getValidValueDefinitionsByName",
            description="Returns the list of validValueDefinitions with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public OpenMetadataRootElementsResponse getValidValueDefinitionsByName(@PathVariable
                                                                           String            serverName,
                                                                           @RequestBody (required = false)
                                                                           FilterRequestBody requestBody)
    {
        return restAPI.getValidValueDefinitionsByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of validValueDefinition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-value-definitions/by-search-string")
    @Operation(summary="findValidValueDefinitions",
            description="Retrieve the list of validValueDefinition metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public OpenMetadataRootElementsResponse findValidValueDefinitions(@PathVariable
                                                                      String                  serverName,
                                                                      @RequestBody (required = false)
                                                                      SearchStringRequestBody requestBody)
    {
        return restAPI.findValidValueDefinitions(serverName, requestBody);
    }


    /**
     * Return the properties of a specific validValueDefinition.
     *
     * @param serverName name of the service to route the request to
     * @param validValueDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-value-definitions/{validValueDefinitionGUID}/retrieve")
    @Operation(summary="getValidValueDefinitionByGUID",
            description="Return the properties of a specific validValueDefinition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/valid-value-definition"))

    public OpenMetadataRootElementResponse getValidValueDefinitionByGUID(@PathVariable
                                                                         String             serverName,
                                                                         @PathVariable
                                                                         String             validValueDefinitionGUID,
                                                                         @RequestBody (required = false)
                                                                         GetRequestBody requestBody)
    {
        return restAPI.getValidValueDefinitionByGUID(serverName, validValueDefinitionGUID, requestBody);
    }
}
