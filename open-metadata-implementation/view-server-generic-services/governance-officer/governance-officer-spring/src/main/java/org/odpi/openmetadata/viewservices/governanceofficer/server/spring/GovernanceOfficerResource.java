/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.viewservices.governanceofficer.server.GovernanceOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The GovernanceOfficerResource provides part of the server-side implementation of the Governance Officer OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Governance Officer OMVS", description="The Governance Officer OMVS provides APIs for supporting the creation and editing of a new governance domain.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/governance-officer/overview/"))

public class GovernanceOfficerResource
{
    private final GovernanceOfficerRESTServices restAPI = new GovernanceOfficerRESTServices();

    /**
     * Default constructor
     */
    public GovernanceOfficerResource()
    {
    }



    /**
     * Create a governance definition.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the governance definition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions")

    @Operation(summary="createGovernanceDefinition",
            description="Create a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GUIDResponse createGovernanceDefinition(@PathVariable String                               serverName,
                                                   @PathVariable String             urlMarker,
                                                   @RequestBody (required = false)
                                                   NewGovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.createGovernanceDefinition(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a governance definition using an existing metadata element as a template.
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
    @PostMapping(path = "/governance-definitions/from-template")
    @Operation(summary="createGovernanceDefinitionFromTemplate",
            description="Create a new metadata element to represent a governance definition using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GUIDResponse createGovernanceDefinitionFromTemplate(@PathVariable
                                                               String              serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody (required = false)
                                                               TemplateRequestBody requestBody)
    {
        return restAPI.createGovernanceDefinitionFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a governance definition.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/update")
    @Operation(summary="updateGovernanceDefinition",
            description="Update the properties of a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse updateGovernanceDefinition(@PathVariable
                                                   String                                  serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                                  governanceDefinitionGUID,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                                 replaceAllProperties,
                                                   @RequestBody (required = false)
                                                   UpdateGovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.updateGovernanceDefinition(serverName, urlMarker, governanceDefinitionGUID, replaceAllProperties, requestBody);
    }


    /**
     * Update the status of a governance definition.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/update-status")
    @Operation(summary="updateGovernanceDefinitionStatus",
            description="Update the status of a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse updateGovernanceDefinitionStatus(@PathVariable
                                                         String                                  serverName,
                                                         @PathVariable String             urlMarker,
                                                         @PathVariable
                                                         String                                  governanceDefinitionGUID,
                                                         @RequestBody (required = false)
                                                         GovernanceDefinitionStatusRequestBody requestBody)
    {
        return restAPI.updateGovernanceDefinitionStatus(serverName, urlMarker, governanceDefinitionGUID, requestBody);
    }


    /**
     * Attach two peer governance definitions.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/peer-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/attach")
    @Operation(summary="linkPeerDefinitions",
            description="Attach two peer governance definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse linkPeerDefinitions(@PathVariable
                                            String                     serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String                     governanceDefinitionOneGUID,
                                            @PathVariable
                                            String                     relationshipTypeName,
                                            @PathVariable
                                            String                     governanceDefinitionTwoGUID,
                                            @RequestBody (required = false)
                                            RelationshipRequestBody requestBody)
    {
        return restAPI.linkPeerDefinitions(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Detach a governance definition from one of its peers.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/peer-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/detach")
    @Operation(summary="detachPeerDefinitions",
            description="Detach a data field from a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse detachPeerDefinitions(@PathVariable
                                              String                    serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                     governanceDefinitionOneGUID,
                                              @PathVariable
                                              String                     relationshipTypeName,
                                              @PathVariable
                                              String                     governanceDefinitionTwoGUID,
                                              @RequestBody (required = false)
                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachPeerDefinitions(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Attach a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/supporting-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/attach")
    @Operation(summary="attachSupportingDefinition",
            description="Attach a supporting governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse attachSupportingDefinition(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                     governanceDefinitionOneGUID,
                                                   @PathVariable
                                                   String                     relationshipTypeName,
                                                   @PathVariable
                                                   String                     governanceDefinitionTwoGUID,
                                                   @RequestBody (required = false)
                                                   RelationshipRequestBody requestBody)
    {
        return restAPI.attachSupportingDefinition(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Detach a governance definition from a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/supporting-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/detach")
    @Operation(summary="detachSupportingDefinition",
            description="Detach a governance definition from a supporting governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse detachSupportingDefinition(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                     governanceDefinitionOneGUID,
                                                   @PathVariable
                                                   String                     relationshipTypeName,
                                                   @PathVariable
                                                   String                     governanceDefinitionTwoGUID,
                                                   @RequestBody (required = false)
                                                   MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSupportingDefinition(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Delete a governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID  unique identifier of the element to delete
     * @param cascadedDelete can governance definitions be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/delete")
    @Operation(summary="deleteGovernanceDefinition",
            description="Delete a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse deleteGovernanceDefinition(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                    governanceDefinitionGUID,
                                                   @RequestParam(required = false, defaultValue = "false")
                                                   boolean                   cascadedDelete,
                                                   @RequestBody (required = false)
                                                   MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteGovernanceDefinition(serverName, urlMarker, governanceDefinitionGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of governance definitions with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/by-name")
    @Operation(summary="getGovernanceDefinitionsByName",
            description="Returns the list of governance definitions with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GovernanceDefinitionsResponse getGovernanceDefinitionsByName(@PathVariable
                                                                        String            serverName,
                                                                        @PathVariable String             urlMarker,
                                                                        @RequestParam (required = false, defaultValue = "0")
                                                                        int                     startFrom,
                                                                        @RequestParam (required = false, defaultValue = "0")
                                                                        int                     pageSize,
                                                                        @RequestBody (required = false)
                                                                        FilterRequestBody requestBody)
    {
        return restAPI.getGovernanceDefinitionsByName(serverName, urlMarker, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance definition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/by-search-string")
    @Operation(summary="findGovernanceDefinitions",
            description="Retrieve the list of governance definition metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GovernanceDefinitionsResponse findGovernanceDefinitions(@PathVariable
                                                                   String                  serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                   int                     startFrom,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                   int                     pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                 startsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                 endsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                 ignoreCase,
                                                                   @RequestBody (required = false)
                                                                   FilterRequestBody requestBody)
    {
        return restAPI.findGovernanceDefinitions(serverName, urlMarker, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/retrieve")
    @Operation(summary="getGovernanceDefinitionByGUID",
            description="Return the properties of a specific governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GovernanceDefinitionResponse getGovernanceDefinitionByGUID(@PathVariable
                                                                      String             serverName,
                                                                      @PathVariable String             urlMarker,
                                                                      @PathVariable
                                                                      String             governanceDefinitionGUID,
                                                                      @RequestBody (required = false)
                                                                      AnyTimeRequestBody requestBody)
    {
        return restAPI.getGovernanceDefinitionByGUID(serverName, urlMarker, governanceDefinitionGUID, requestBody);
    }


    /**
     * Return the governance definition associated with a unique identifier and the other governance definitions linked to it.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param startFrom paging start
     * @param pageSize max elements that can be returned
     * @param requestBody additional query parameters
     *
     * @return governance definition and its linked elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/in-context")

    public GovernanceDefinitionGraphResponse getGovernanceDefinitionInContext(@PathVariable String             serverName,
                                                                              @PathVariable String             urlMarker,
                                                                              @PathVariable String             governanceDefinitionGUID,
                                                                              @RequestParam(required = false, defaultValue = "0") int startFrom,
                                                                              @RequestParam(required = false, defaultValue = "0") int pageSize,
                                                                              @RequestBody (required = false)  ResultsRequestBody requestBody)
    {
        return restAPI.getGovernanceDefinitionInContext(serverName, urlMarker, governanceDefinitionGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementations/{implementationGUID}/attach")
    @Operation(summary="linkDesignToImplementation",
            description="Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse linkDesignToImplementation(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String designGUID,
                                                   @PathVariable
                                                   String implementationGUID,
                                                   @RequestBody (required = false)
                                                   RelationshipRequestBody requestBody)
    {
        return restAPI.linkDesignToImplementation(serverName, urlMarker, designGUID, implementationGUID, requestBody);
    }


    /**
     * Detach a design object such as a solution component or governance definition from its implementation. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementations/{implementationGUID}/detach")
    @Operation(summary="detachDesignFromImplementation",
            description="Detach a design object such as a solution component or governance definition from its implementation. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse detachDesignFromImplementation(@PathVariable
                                                       String                    serverName,
                                                       @PathVariable String             urlMarker,
                                                       @PathVariable
                                                       String                     designGUID,
                                                       @PathVariable
                                                       String                     implementationGUID,
                                                       @RequestBody (required = false)
                                                       MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachDesignFromImplementation(serverName, urlMarker, designGUID, implementationGUID, requestBody);
    }


    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID unique identifier of the design object
     * @param implementationResourceGUID unique identifier of the implementation resource
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementation-resources/{implementationResourceGUID}/attach")
    @Operation(summary="linkImplementationResource",
            description="Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse linkImplementationResource(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String designGUID,
                                                   @PathVariable
                                                   String implementationResourceGUID,
                                                   @RequestBody (required = false)
                                                   RelationshipRequestBody requestBody)
    {
        return restAPI.linkImplementationResource(serverName, urlMarker, designGUID, implementationResourceGUID, requestBody);
    }


    /**
     * Detach a design object such as a solution component or governance definition from one of its implementation resources. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID unique identifier of the design object
     * @param implementationResourceGUID unique identifier of the implementation resource
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementation-resources/{implementationResourceGUID}/detach")
    @Operation(summary="detachImplementationResource",
            description="Detach a design object such as a solution component or governance definition from one of its implementation resources. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse detachImplementationResource(@PathVariable String                    serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable String                     designGUID,
                                                     @PathVariable String implementationResourceGUID,
                                                     @RequestBody (required = false) MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachImplementationResource(serverName, urlMarker, designGUID, implementationResourceGUID, requestBody);
    }
}
