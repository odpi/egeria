/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.solutionarchitect.server.SolutionArchitectRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SolutionArchitectResource provides part of the server-side implementation of the Solution Architect OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/solution-architect")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Solution Architect", description="During the planning phase of a project, architects typically use drawing tools to sketch out the new components that are to be developed and how they relate to existing components.  Solution blueprints are the open metadata equivalent of the sketch and they show the solution components and actors involved and how they collaborate.  The advantage of creating a solution blueprint over a sketch diagram is that it is easy to visualize different levels of detail and, as the project rolls out, the implementation of the components can be linked into the blueprint, providing traceability from project intent to actual operation.  In a similar way, information supply chains allow the modelling of key data flows needed by your organization.   These can then be linked to metadata about the systems and pipelines that implement them, providing a means to summarize statistics from lineage about the operation of the data flows.  The Solution Architect OMVS supports the definition and display of solution blueprints and their supporting solution components along with the relevant information supply chains.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/solution-architect/overview/"))

public class SolutionArchitectResource
{
    private final SolutionArchitectRESTServices restAPI = new SolutionArchitectRESTServices();

    /**
     * Default constructor
     */
    public SolutionArchitectResource()
    {
    }


    /**
     * Create an information supply chain.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the information supply chain.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createInformationSupplyChain",
            description="Create an information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public GUIDResponse createInformationSupplyChain(@PathVariable
                                                     String                               serverName,
                                                     @RequestBody (required = false)
                                                     NewElementRequestBody requestBody)
    {
        return restAPI.createInformationSupplyChain(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent an information supply chain using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createInformationSupplyChainFromTemplate",
            description="Create a new metadata element to represent an information supply chain using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public GUIDResponse createInformationSupplyChainFromTemplate(@PathVariable
                                                                 String              serverName,
                                                                 @RequestBody (required = false)
                                                                 TemplateRequestBody requestBody)
    {
        return restAPI.createInformationSupplyChainFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of an information supply chain.
     *
     * @param serverName         name of called server.
     * @param informationSupplyChainGUID unique identifier of the information supply chain (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateInformationSupplyChain",
            description="Update the properties of an information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public BooleanResponse updateInformationSupplyChain(@PathVariable String                                  serverName,
                                                        @PathVariable String                                  informationSupplyChainGUID,
                                                        @RequestBody (required = false)
                                                        UpdateElementRequestBody requestBody)
    {
        return restAPI.updateInformationSupplyChain(serverName, informationSupplyChainGUID, requestBody);
    }


    /**
     * Connect two peers in an information supply chains.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param serverName         name of called server
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{peerOneGUID}/peer-links/{peerTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkPeersInInformationSupplyChain",
            description="Connect two peer information supply chains.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse linkPeersInInformationSupplyChain(@PathVariable
                                                          String                                serverName,
                                                          @PathVariable
                                                          String peerOneGUID,
                                                          @PathVariable
                                                          String peerTwoGUID,
                                                          @RequestBody (required = false)
                                                          NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkPeersInInformationSupplyChain(serverName, peerOneGUID, peerTwoGUID, requestBody);
    }


    /**
     * Detach two peers in an information supply chain from one another.    The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param serverName         name of called server
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{peerOneGUID}/peer-links/{peerTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="unlinkPeerInformationSupplyChains",
            description="Detach two peers in an information supply chain from one another.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse unlinkPeerInformationSupplyChains(@PathVariable
                                                          String                    serverName,
                                                          @PathVariable
                                                          String peerOneGUID,
                                                          @PathVariable
                                                          String peerTwoGUID,
                                                          @RequestBody (required = false)
                                                          DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unlinkPeersInInformationSupplyChain(serverName, peerOneGUID, peerTwoGUID, requestBody);
    }


    /**
     * Delete an information supply chain.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteInformationSupplyChain",
            description="Delete an information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse deleteInformationSupplyChain(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable
                                                     String                    informationSupplyChainGUID,
                                                     @RequestBody (required = false)
                                                     DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteInformationSupplyChain(serverName, informationSupplyChainGUID, requestBody);
    }


    /**
     * Returns the list of information supply chains with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getInformationSupplyChainsByName",
            description="Returns the list of information supply chains with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public OpenMetadataRootElementsResponse getInformationSupplyChainsByName(@PathVariable
                                                                             String            serverName,
                                                                             @RequestParam (required = false, defaultValue = "true")
                                                                             boolean           addImplementation,
                                                                             @RequestBody (required = false)
                                                                             FilterRequestBody requestBody)
    {
        return restAPI.getInformationSupplyChainsByName(serverName, addImplementation,  requestBody);
    }


    /**
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findInformationSupplyChains",
            description="Retrieve the list of information supply chain metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public OpenMetadataRootElementsResponse findInformationSupplyChains(@PathVariable
                                                                        String                  serverName,
                                                                        @RequestParam (required = false, defaultValue = "true")
                                                                        boolean                 addImplementation,
                                                                        @RequestBody (required = false)
                                                                        SearchStringRequestBody requestBody)
    {
        return restAPI.findInformationSupplyChains(serverName, addImplementation, requestBody);
    }



    /**
     * Return the properties of a specific information supply chain.
     *
     * @param serverName name of the service to route the request to
     * @param informationSupplyChainGUID    unique identifier of the required element
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getInformationSupplyChainByGUID",
            description="Return the properties of a specific information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public OpenMetadataRootElementResponse getInformationSupplyChainByGUID(@PathVariable
                                                                           String             serverName,
                                                                           @PathVariable
                                                                           String             informationSupplyChainGUID,
                                                                           @RequestParam (required = false, defaultValue = "true")
                                                                           boolean            addImplementation,
                                                                           @RequestBody (required = false)
                                                                           GetRequestBody requestBody)
    {
        return restAPI.getInformationSupplyChainByGUID(serverName, informationSupplyChainGUID, addImplementation, requestBody);
    }


    /**
     * Create a solution blueprint.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the solution blueprint.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createSolutionBlueprint",
            description="Create a solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public GUIDResponse createSolutionBlueprint(@PathVariable
                                                String                               serverName,
                                                @RequestBody (required = false)
                                                NewElementRequestBody requestBody)
    {
        return restAPI.createSolutionBlueprint(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a solution blueprint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createSolutionBlueprintFromTemplate",
            description="Create a new metadata element to represent a solution blueprint using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public GUIDResponse createSolutionBlueprintFromTemplate(@PathVariable
                                                            String              serverName,
                                                            @RequestBody (required = false)
                                                            TemplateRequestBody requestBody)
    {
        return restAPI.createSolutionBlueprintFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a solution blueprint.
     *
     * @param serverName         name of called server.
     * @param solutionBlueprintGUID unique identifier of the solution blueprint (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints/{solutionBlueprintGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSolutionBlueprint",
            description="Update the properties of a solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public BooleanResponse updateSolutionBlueprint(@PathVariable
                                                   String                                  serverName,
                                                   @PathVariable
                                                   String                                  solutionBlueprintGUID,
                                                   @RequestBody (required = false)
                                                   UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSolutionBlueprint(serverName, solutionBlueprintGUID, requestBody);
    }


    /**
     * Attach a solution blueprint to the element that ir describes.
     *
     * @param serverName         name of called server
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{parentGUID}/solution-designs/{solutionBlueprintGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSolutionDesign",
            description="Attach a solution blueprint to the element that it describes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public VoidResponse linkSolutionDesign(@PathVariable
                                           String                                serverName,
                                           @PathVariable
                                           String parentGUID,
                                           @PathVariable
                                           String solutionBlueprintGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSolutionDesign(serverName, parentGUID, solutionBlueprintGUID, requestBody);
    }


    /**
     * Detach a solution blueprint from the element it describes.
     *
     * @param serverName         name of called server
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{parentGUID}/solution-designs/{solutionBlueprintGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSolutionDesign",
            description="Detach a solution blueprint from the element it describes..",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public VoidResponse detachSolutionDesign(@PathVariable
                                             String                    serverName,
                                             @PathVariable
                                             String parentGUID,
                                             @PathVariable
                                             String solutionBlueprintGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionDesign(serverName, parentGUID, solutionBlueprintGUID, requestBody);
    }


    /**
     * Delete a solution blueprint.
     *
     * @param serverName         name of called server
     * @param solutionBlueprintGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints/{solutionBlueprintGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSolutionBlueprint",
            description="Delete a solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public VoidResponse deleteSolutionBlueprint(@PathVariable
                                                String                    serverName,
                                                @PathVariable
                                                String                    solutionBlueprintGUID,
                                                @RequestBody (required = false)
                                                DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteSolutionBlueprint(serverName, solutionBlueprintGUID, requestBody);
    }


    /**
     * Returns the list of solution blueprints with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSolutionBlueprintsByName",
            description="Returns the list of solution blueprints with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public OpenMetadataRootElementsResponse getSolutionBlueprintsByName(@PathVariable String            serverName,
                                                                        @RequestBody (required = false) FilterRequestBody requestBody)
    {
        return restAPI.getSolutionBlueprintsByName(serverName, requestBody);
    }


    /**
     * Return the properties of a specific solution blueprint.
     *
     * @param serverName name of the service to route the request to
     * @param solutionBlueprintGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/{solutionBlueprintGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSolutionBlueprintByGUID",
            description="Return the properties of a specific solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public OpenMetadataRootElementResponse getSolutionBlueprintByGUID(@PathVariable
                                                                      String             serverName,
                                                                      @PathVariable
                                                                      String             solutionBlueprintGUID,
                                                                      @RequestBody (required = false)
                                                                      GetRequestBody requestBody)
    {
        return restAPI.getSolutionBlueprintByGUID(serverName, solutionBlueprintGUID, requestBody);
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findSolutionBlueprints",
            description="Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public OpenMetadataRootElementsResponse findSolutionBlueprints(@PathVariable String                  serverName,
                                                                   @RequestBody  (required = false)
                                                                   SearchStringRequestBody requestBody)
    {
        return restAPI.findSolutionBlueprints(serverName, requestBody);
    }


    /**
     * Attach a solution component to a solution role.
     *
     * @param serverName         name of called server
     * @param solutionRoleGUID  unique identifier of the first solution role
     * @param dataFieldGUID      unique identifier of the second solution role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/solution-component-actors/{dataFieldGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSolutionComponentActor",
            description="Attach a solution component to a solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public VoidResponse linkSolutionComponentActor(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable
                                                   String                     solutionRoleGUID,
                                                   @PathVariable
                                                   String                     dataFieldGUID,
                                                   @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSolutionComponentActor(serverName, solutionRoleGUID, dataFieldGUID, requestBody);
    }


    /**
     * Detach a solution component from a solution role.
     *
     * @param serverName         name of called server
     * @param solutionRoleGUID  unique identifier of the first solution role
     * @param solutionComponentGUID      unique identifier of the second solution role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/solution-component-actors/{solutionComponentGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSolutionComponentActor",
            description="Detach a solution component from a solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public VoidResponse detachSolutionComponentActor(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable
                                                     String solutionRoleGUID,
                                                     @PathVariable
                                                     String solutionComponentGUID,
                                                     @RequestBody (required = false)
                                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionComponentActor(serverName, solutionRoleGUID, solutionComponentGUID, requestBody);
    }


    /**
     * Create a solution component.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the solution component.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createSolutionComponent",
            description="Create a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public GUIDResponse createSolutionComponent(@PathVariable
                                                String                               serverName,
                                                @RequestBody (required = false)
                                                NewElementRequestBody requestBody)
    {
        return restAPI.createSolutionComponent(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a solution component using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createSolutionComponentFromTemplate",
            description="Create a new metadata element to represent a solution component using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public GUIDResponse createSolutionComponentFromTemplate(@PathVariable
                                                            String              serverName,
                                                            @RequestBody (required = false)
                                                            TemplateRequestBody requestBody)
    {
        return restAPI.createSolutionComponentFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a solution component.
     *
     * @param serverName         name of called server.
     * @param solutionComponentGUID unique identifier of the solution component (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSolutionComponent",
            description="Update the properties of a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public BooleanResponse updateSolutionComponent(@PathVariable
                                                   String                   serverName,
                                                   @PathVariable
                                                   String                   solutionComponentGUID,
                                                   @RequestBody (required = false)
                                                   UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSolutionComponent(serverName, solutionComponentGUID, requestBody);
    }


    /**
     * Attach a solution component to a nested solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentGUID  unique identifier of the first solution component
     * @param subcomponentGUID      unique identifier of the second solution component
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/subcomponents/{subcomponentGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSubcomponent",
            description="Attach a solution component to a nested solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse linkSubcomponent(@PathVariable
                                         String                     serverName,
                                         @PathVariable
                                         String                     solutionComponentGUID,
                                         @PathVariable
                                         String                     subcomponentGUID,
                                         @RequestBody (required = false)
                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSubcomponent(serverName, solutionComponentGUID, subcomponentGUID, requestBody);
    }


    /**
     * Detach a solution component from a solution component.
     *
     * @param serverName         name of called server
     * @param parentSolutionComponentGUID  unique identifier of the first solution component
     * @param subcomponentGUID      unique identifier of the second solution component
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{parentSolutionComponentGUID}/subcomponents/{subcomponentGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSubcomponent",
            description="Detach a solution component from a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachSubcomponent(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String parentSolutionComponentGUID,
                                           @PathVariable
                                           String subcomponentGUID,
                                           @RequestBody (required = false)
                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSubcomponent(serverName, parentSolutionComponentGUID, subcomponentGUID, requestBody);
    }


    /**
     * Attach a solution component to a solution component as a peer in a solution.
     *
     * @param serverName         name of called server
     * @param solutionComponentOneGUID  unique identifier of the first solution component
     * @param solutionComponentTwoGUID      unique identifier of the second solution component
     * @param requestBody  description of the relationship.
     *
     * @return relationship GUID or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentOneGUID}/wired-to/{solutionComponentTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSolutionLinkingWire",
            description="Attach a solution component to a solution component as a peer in a solution.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public GUIDResponse linkSolutionLinkingWire(@PathVariable
                                                String                     serverName,
                                                @PathVariable
                                                String solutionComponentOneGUID,
                                                @PathVariable
                                                String solutionComponentTwoGUID,
                                                @RequestBody (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSolutionLinkingWire(serverName, solutionComponentOneGUID, solutionComponentTwoGUID, requestBody);
    }


    /**
     * Update the solution linking wire relationship.
     *
     * @param serverName name of the server instance to connect to
     * @param relationshipGUID unique identifier for the relationship
     * @param requestBody the properties of the relationship
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/relationships/{relationshipGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSolutionLinkingWire",
            description="Update the solution linking wire relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse updateSolutionLinkingWire(@PathVariable String                        serverName,
                                                  @PathVariable String                        relationshipGUID,
                                                  @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateSolutionLinkingWire(serverName, relationshipGUID, requestBody);
    }


    /**
     * Delete all solution linking wire relationships between two metadata elements.
     *
     * @param serverName         name of called server
     * @param solutionComponentOneGUID  unique identifier of the first solution component
     * @param solutionComponentTwoGUID      unique identifier of the second solution component
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentOneGUID}/wired-to/{solutionComponentTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachAllSolutionLinkingWires",
            description="Detach a solution component from a peer solution component.  All solution linking wires are removed.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachAllSolutionLinkingWires(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable
                                                  String solutionComponentOneGUID,
                                                  @PathVariable
                                                  String solutionComponentTwoGUID,
                                                  @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAllSolutionLinkingWire(serverName, solutionComponentOneGUID, solutionComponentTwoGUID, requestBody);
    }


    /**
     * Detach a solution component from a peer solution component.
     *
     * @param serverName         name of called server
     * @param relationshipGUID  unique identifier of the first solution component
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/wires/{relationshipGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSolutionLinkingWire",
            description="Detach a solution component from a peer solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachSolutionLinkingWire(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable
                                                  String relationshipGUID,
                                                  @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionLinkingWire(serverName, relationshipGUID, requestBody);
    }


    /**
     * Delete a solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSolutionComponent",
            description="Delete a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse deleteSolutionComponent(@PathVariable
                                                String                    serverName,
                                                @PathVariable
                                                String                    solutionComponentGUID,
                                                @RequestBody (required = false)
                                                DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteSolutionComponent(serverName, solutionComponentGUID, requestBody);
    }


    /**
     * Returns the list of solution components with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSolutionComponentsByName",
            description="Returns the list of solution components with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public OpenMetadataRootElementsResponse getSolutionComponentsByName(@PathVariable
                                                                        String            serverName,
                                                                        @RequestBody (required = false)
                                                                        FilterRequestBody requestBody)
    {
        return restAPI.getSolutionComponentsByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findSolutionComponents",
            description="Retrieve the list of solution metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public OpenMetadataRootElementsResponse findSolutionComponents(@PathVariable String                  serverName,
                                                                   @RequestBody (required = false)
                                                                   SearchStringRequestBody requestBody)
    {
        return restAPI.findSolutionComponents(serverName,  requestBody);
    }



    /**
     * Return the properties of a specific solution component.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSolutionComponentByGUID",
            description="Return the properties of a specific solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public OpenMetadataRootElementResponse getSolutionComponentByGUID(@PathVariable
                                                                      String             serverName,
                                                                      @PathVariable
                                                                      String             solutionComponentGUID,
                                                                      @RequestBody (required = false)
                                                                      GetRequestBody requestBody)
    {
        return restAPI.getSolutionComponentByGUID(serverName, solutionComponentGUID, requestBody);
    }



    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/implementations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSolutionComponentImplementations",
            description="Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public RelatedMetadataElementsResponse getSolutionComponentImplementations(@PathVariable String                  serverName,
                                                                               @PathVariable String                  solutionComponentGUID,
                                                                               @RequestBody(required = false)
                                                                               ResultsRequestBody requestBody)
    {
        return restAPI.getSolutionComponentImplementations(serverName, solutionComponentGUID, requestBody);
    }


    /**
     * Create a new design pattern.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody details of the design pattern
     *
     * @return unique identifier of the design pattern or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDesignPattern",
            description="Create a new design pattern.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public GUIDResponse createDesignPattern(@PathVariable
                                            String                serverName,
                                            @RequestBody (required = false)
                                            NewElementRequestBody requestBody)
    {
        return restAPI.createDesignPattern(serverName, requestBody);
    }


    /**
     * Create a new design pattern using a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody details of the template and overrides
     *
     * @return unique identifier of the design pattern or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDesignPatternFromTemplate",
            description="Create a new design pattern using a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public GUIDResponse createDesignPatternFromTemplate(@PathVariable
                                                        String              serverName,
                                                        @RequestBody (required = false)
                                                        TemplateRequestBody requestBody)
    {
        return restAPI.createDesignPatternFromTemplate(serverName, requestBody);
    }


    /**
     * Update an existing design pattern.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternGUID unique identifier of the design pattern to update
     * @param requestBody details of the design pattern
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{designPatternGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDesignPattern",
            description="Update an existing design pattern.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public BooleanResponse updateDesignPattern(@PathVariable
                                               String                   serverName,
                                               @PathVariable
                                               String                   designPatternGUID,
                                               @RequestBody (required = false)
                                               UpdateElementRequestBody requestBody)
    {
        return restAPI.updateDesignPattern(serverName, designPatternGUID, requestBody);
    }


    /**
     * Link two design patterns together as parent and child.
     *
     * @param serverName name of the service to route the request to
     * @param parentDesignPatternGUID unique identifier of the parent design pattern
     * @param nestedDesignPatternGUID unique identifier of the child design pattern
     * @param requestBody properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{parentDesignPatternGUID}/nested-design-patterns/{nestedDesignPatternGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkNestedDesignPatterns",
            description="Link two design patterns together as parent and child.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkNestedDesignPatterns(@PathVariable
                                                 String                   serverName,
                                                 @PathVariable
                                                 String                   parentDesignPatternGUID,
                                                 @PathVariable
                                                 String                   nestedDesignPatternGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNestedDesignPatterns(serverName, parentDesignPatternGUID, nestedDesignPatternGUID, requestBody);
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param serverName name of the service to route the request to
     * @param parentDesignPatternGUID unique identifier of the parent design pattern
     * @param nestedDesignPatternGUID unique identifier of the child design pattern
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{parentDesignPatternGUID}/nested-design-patterns/{nestedDesignPatternGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachNestedDesignPatterns",
            description="Remove the link between two design patterns.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachNestedDesignPatterns(@PathVariable
                                                   String                      serverName,
                                                   @PathVariable
                                                   String                      parentDesignPatternGUID,
                                                   @PathVariable
                                                   String                      nestedDesignPatternGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNestedDesignPatterns(serverName, parentDesignPatternGUID, nestedDesignPatternGUID, requestBody);
    }


    /**
     * Link two design patterns together as general and specialized.
     *
     * @param serverName name of the service to route the request to
     * @param generalizedDesignPatternGUID unique identifier of the generalized design pattern
     * @param specializedDesignPatternGUID unique identifier of the specialized design pattern
     * @param requestBody properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{generalizedDesignPatternGUID}/specialized-design-patterns/{specializedDesignPatternGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSpecializedDesignPatterns",
            description="Link two design patterns together as general and specialized.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkSpecializedDesignPatterns(@PathVariable
                                                      String                   serverName,
                                                      @PathVariable
                                                      String                   generalizedDesignPatternGUID,
                                                      @PathVariable
                                                      String                   specializedDesignPatternGUID,
                                                      @RequestBody (required = false)
                                                      NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSpecializedDesignPatterns(serverName, generalizedDesignPatternGUID, specializedDesignPatternGUID, requestBody);
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param serverName name of the service to route the request to
     * @param generalizedDesignPatternGUID unique identifier of the generalized design pattern
     * @param specializedDesignPatternGUID unique identifier of the specialized design pattern
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{generalizedDesignPatternGUID}/specialized-design-patterns/{specializedDesignPatternGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSpecializedDesignPatterns",
            description="Remove the link between two design patterns.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachSpecializedDesignPatterns(@PathVariable
                                                        String                      serverName,
                                                        @PathVariable
                                                        String                      generalizedDesignPatternGUID,
                                                        @PathVariable
                                                        String                      specializedDesignPatternGUID,
                                                        @RequestBody (required = false)
                                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSpecializedDesignPatterns(serverName, generalizedDesignPatternGUID, specializedDesignPatternGUID, requestBody);
    }


    /**
     * Link two design patterns together as related.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternOneGUID unique identifier of the first design pattern
     * @param designPatternTwoGUID unique identifier of the second design pattern
     * @param requestBody properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{designPatternOneGUID}/related-design-patterns/{designPatternTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkRelatedDesignPatterns",
            description="Link two design patterns together as related.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkRelatedDesignPatterns(@PathVariable
                                                  String                   serverName,
                                                  @PathVariable
                                                  String                   designPatternOneGUID,
                                                  @PathVariable
                                                  String                   designPatternTwoGUID,
                                                  @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkRelatedDesignPatterns(serverName, designPatternOneGUID, designPatternTwoGUID, requestBody);
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternOneGUID unique identifier of the first design pattern
     * @param designPatternTwoGUID unique identifier of the second design pattern
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{designPatternOneGUID}/related-design-patterns/{designPatternTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachRelatedDesignPatterns",
            description="Remove the link between two design patterns.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachRelatedDesignPatterns(@PathVariable
                                                    String                      serverName,
                                                    @PathVariable
                                                    String                      designPatternOneGUID,
                                                    @PathVariable
                                                    String                      designPatternTwoGUID,
                                                    @RequestBody (required = false)
                                                    DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachRelatedDesignPatterns(serverName, designPatternOneGUID, designPatternTwoGUID, requestBody);
    }


    /**
     * Delete an existing design pattern.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternGUID unique identifier of the design pattern to delete
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{designPatternGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteDesignPattern",
            description="Delete an existing design pattern.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse deleteDesignPattern(@PathVariable
                                            String                  serverName,
                                            @PathVariable
                                            String                  designPatternGUID,
                                            @RequestBody (required = false)
                                            DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteDesignPattern(serverName, designPatternGUID, requestBody);
    }


    /**
     * Retrieve the list of design patterns with a matching name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody search string and paging options
     *
     * @return list of design patterns or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDesignPatternsByName",
            description="Retrieve the list of design patterns with a matching name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public OpenMetadataRootElementsResponse getDesignPatternsByName(@PathVariable
                                                                    String            serverName,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getDesignPatternsByName(serverName, requestBody);
    }


    /**
     * Retrieve the design pattern with the matching unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternGUID unique identifier of the design pattern to retrieve
     * @param requestBody options for the get
     *
     * @return design pattern or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/{designPatternGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDesignPatternByGUID",
            description="Retrieve the design pattern with the matching unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public OpenMetadataRootElementResponse getDesignPatternByGUID(@PathVariable
                                                                  String         serverName,
                                                                  @PathVariable
                                                                  String         designPatternGUID,
                                                                  @RequestBody (required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getDesignPatternByGUID(serverName, designPatternGUID, requestBody);
    }


    /**
     * Retrieve the list of design patterns that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody search string and paging options
     *
     * @return list of design patterns or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/design-patterns/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDesignPatterns",
            description="Retrieve the list of design patterns that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public OpenMetadataRootElementsResponse findDesignPatterns(@PathVariable
                                                               String                  serverName,
                                                               @RequestBody (required = false)
                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findDesignPatterns(serverName, requestBody);
    }

    /**
     * Create a concept model element.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createConceptModelElement",
            description="Create a concept model element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public GUIDResponse createConceptModelElement(@PathVariable String serverName,
                                                  @RequestBody (required = false)
                                                  NewElementRequestBody requestBody)
    {
        return restAPI.createConceptModelElement(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a concept model element using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createConceptModelElementFromTemplate",
            description="Create a new metadata element to represent a concept model element using an existing metadata element as a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public GUIDResponse createConceptModelElementFromTemplate(@PathVariable String serverName,
                                                              @RequestBody (required = false)
                                                              TemplateRequestBody requestBody)
    {
        return restAPI.createConceptModelElementFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a concept model element.
     *
     * @param serverName name of the server to route the request to
     * @param conceptModelElementGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements/{conceptModelElementGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateConceptModelElement",
            description="Update the properties of a concept model element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public BooleanResponse updateConceptModelElement(@PathVariable String serverName,
                                                     @PathVariable String conceptModelElementGUID,
                                                     @RequestBody (required = false)
                                                     UpdateElementRequestBody requestBody)
    {
        return restAPI.updateConceptModelElement(serverName, conceptModelElementGUID, requestBody);
    }


    /**
     * Delete a concept model element.
     *
     * @param serverName name of the server to route the request to
     * @param conceptModelElementGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements/{conceptModelElementGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteConceptModelElement",
            description="Delete a concept model element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse deleteConceptModelElement(@PathVariable String serverName,
                                                  @PathVariable String conceptModelElementGUID,
                                                  @RequestBody (required = false)
                                                  DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteConceptModelElement(serverName, conceptModelElementGUID, requestBody);
    }


    /**
     * Returns the list of concept model elements with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConceptModelElementsByName",
            description="Returns the list of concept model elements with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public OpenMetadataRootElementsResponse getConceptModelElementsByName(@PathVariable String serverName,
                                                                          @RequestBody (required = false)
                                                                          FilterRequestBody requestBody)
    {
        return restAPI.getConceptModelElementsByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of concept model element metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findConceptModelElements",
            description="Retrieve the list of concept model element metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public OpenMetadataRootElementsResponse findConceptModelElements(@PathVariable String serverName,
                                                                     @RequestBody (required = false)
                                                                     SearchStringRequestBody requestBody)
    {
        return restAPI.findConceptModelElements(serverName, requestBody);
    }


    /**
     * Return the properties of a specific concept model element.
     *
     * @param serverName name of the server to route the request to
     * @param conceptModelElementGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-model-elements/{conceptModelElementGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConceptModelElementByGUID",
            description="Return the properties of a specific concept model element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public OpenMetadataRootElementResponse getConceptModelElementByGUID(@PathVariable String serverName,
                                                                        @PathVariable String conceptModelElementGUID,
                                                                        @RequestBody (required = false)
                                                                        GetRequestBody requestBody)
    {
        return restAPI.getConceptModelElementByGUID(serverName, conceptModelElementGUID, requestBody);
    }


    /**
     * Attach a concept model to the element whose concepts it describes.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier
     * @param conceptModelGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/concept-designs/{conceptModelGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkConceptDesign",
            description="Attach a concept model to the element whose concepts it describes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkConceptDesign(@PathVariable String serverName,
                                          @PathVariable String elementGUID,
                                          @PathVariable String conceptModelGUID,
                                          @RequestBody (required = false)
                                          NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConceptDesign(serverName, elementGUID, conceptModelGUID, requestBody);
    }


    /**
     * Detach a concept model from the element whose concepts it described.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier
     * @param conceptModelGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/concept-designs/{conceptModelGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachConceptDesign",
            description="Detach a concept model from the element whose concepts it described.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachConceptDesign(@PathVariable String serverName,
                                            @PathVariable String elementGUID,
                                            @PathVariable String conceptModelGUID,
                                            @RequestBody (required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachConceptDesign(serverName, elementGUID, conceptModelGUID, requestBody);
    }


    /**
     * Attach a concept bead to one of the ends of a concept bead relationship.
     *
     * @param serverName name of the server to route the request to
     * @param conceptBeadRelationshipGUID unique identifier
     * @param conceptBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-bead-relationships/{conceptBeadRelationshipGUID}/relationship-ends/{conceptBeadGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkConceptBeadRelationshipEnd",
            description="Attach a concept bead to one of the ends of a concept bead relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkConceptBeadRelationshipEnd(@PathVariable String serverName,
                                                       @PathVariable String conceptBeadRelationshipGUID,
                                                       @PathVariable String conceptBeadGUID,
                                                       @RequestBody (required = false)
                                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConceptBeadRelationshipEnd(serverName, conceptBeadRelationshipGUID, conceptBeadGUID, requestBody);
    }


    /**
     * Detach a concept bead from one of the ends of a concept bead relationship.
     *
     * @param serverName name of the server to route the request to
     * @param conceptBeadRelationshipGUID unique identifier
     * @param conceptBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-bead-relationships/{conceptBeadRelationshipGUID}/relationship-ends/{conceptBeadGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachConceptBeadRelationshipEnd",
            description="Detach a concept bead from one of the ends of a concept bead relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachConceptBeadRelationshipEnd(@PathVariable String serverName,
                                                         @PathVariable String conceptBeadRelationshipGUID,
                                                         @PathVariable String conceptBeadGUID,
                                                         @RequestBody (required = false)
                                                         DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachConceptBeadRelationshipEnd(serverName, conceptBeadRelationshipGUID, conceptBeadGUID, requestBody);
    }


    /**
     * Attach a concept bead attribute to the concept bead that acts as its type.
     *
     * @param serverName name of the server to route the request to
     * @param conceptBeadAttributeGUID unique identifier
     * @param conceptBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-bead-attributes/{conceptBeadAttributeGUID}/typed-by/{conceptBeadGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkTypedByConceptBead",
            description="Attach a concept bead attribute to the concept bead that acts as its type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkTypedByConceptBead(@PathVariable String serverName,
                                               @PathVariable String conceptBeadAttributeGUID,
                                               @PathVariable String conceptBeadGUID,
                                               @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkTypedByConceptBead(serverName, conceptBeadAttributeGUID, conceptBeadGUID, requestBody);
    }


    /**
     * Detach a concept bead attribute from the concept bead that acted as its type.
     *
     * @param serverName name of the server to route the request to
     * @param conceptBeadAttributeGUID unique identifier
     * @param conceptBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-bead-attributes/{conceptBeadAttributeGUID}/typed-by/{conceptBeadGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachTypedByConceptBead",
            description="Detach a concept bead attribute from the concept bead that acted as its type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachTypedByConceptBead(@PathVariable String serverName,
                                                 @PathVariable String conceptBeadAttributeGUID,
                                                 @PathVariable String conceptBeadGUID,
                                                 @RequestBody (required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachTypedByConceptBead(serverName, conceptBeadAttributeGUID, conceptBeadGUID, requestBody);
    }


    /**
     * Attach a concept bead to the concept bead that it inherits from.
     *
     * @param serverName name of the server to route the request to
     * @param inheritingBeadGUID unique identifier
     * @param inheritedBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-beads/{inheritingBeadGUID}/is-a/{inheritedBeadGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkIsAConceptBead",
            description="Attach a concept bead to the concept bead that it inherits from.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkIsAConceptBead(@PathVariable String serverName,
                                           @PathVariable String inheritingBeadGUID,
                                           @PathVariable String inheritedBeadGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkIsAConceptBead(serverName, inheritingBeadGUID, inheritedBeadGUID, requestBody);
    }


    /**
     * Detach a concept bead from the concept bead that it inherited from.
     *
     * @param serverName name of the server to route the request to
     * @param inheritingBeadGUID unique identifier
     * @param inheritedBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-beads/{inheritingBeadGUID}/is-a/{inheritedBeadGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachIsAConceptBead",
            description="Detach a concept bead from the concept bead that it inherited from.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachIsAConceptBead(@PathVariable String serverName,
                                             @PathVariable String inheritingBeadGUID,
                                             @PathVariable String inheritedBeadGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachIsAConceptBead(serverName, inheritingBeadGUID, inheritedBeadGUID, requestBody);
    }


    /**
     * Attach a concept bead attribute to its parent concept bead.
     *
     * @param serverName name of the server to route the request to
     * @param conceptBeadGUID unique identifier
     * @param conceptBeadAttributeGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-beads/{conceptBeadGUID}/attribute-links/{conceptBeadAttributeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkConceptBeadAttributeLink",
            description="Attach a concept bead attribute to its parent concept bead.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkConceptBeadAttributeLink(@PathVariable String serverName,
                                                     @PathVariable String conceptBeadGUID,
                                                     @PathVariable String conceptBeadAttributeGUID,
                                                     @RequestBody (required = false)
                                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConceptBeadAttributeLink(serverName, conceptBeadGUID, conceptBeadAttributeGUID, requestBody);
    }


    /**
     * Detach a concept bead attribute from its parent concept bead.
     *
     * @param serverName name of the server to route the request to
     * @param conceptBeadGUID unique identifier
     * @param conceptBeadAttributeGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-beads/{conceptBeadGUID}/attribute-links/{conceptBeadAttributeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachConceptBeadAttributeLink",
            description="Detach a concept bead attribute from its parent concept bead.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachConceptBeadAttributeLink(@PathVariable String serverName,
                                                       @PathVariable String conceptBeadGUID,
                                                       @PathVariable String conceptBeadAttributeGUID,
                                                       @RequestBody (required = false)
                                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachConceptBeadAttributeLink(serverName, conceptBeadGUID, conceptBeadAttributeGUID, requestBody);
    }


    /**
     * Attach a concept bead to a concept bead that extends it.
     *
     * @param serverName name of the server to route the request to
     * @param extendedBeadGUID unique identifier
     * @param extensionBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-beads/{extendedBeadGUID}/extensions/{extensionBeadGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkConceptBeadExtension",
            description="Attach a concept bead to a concept bead that extends it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse linkConceptBeadExtension(@PathVariable String serverName,
                                                 @PathVariable String extendedBeadGUID,
                                                 @PathVariable String extensionBeadGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConceptBeadExtension(serverName, extendedBeadGUID, extensionBeadGUID, requestBody);
    }


    /**
     * Detach a concept bead from a concept bead that extended it.
     *
     * @param serverName name of the server to route the request to
     * @param extendedBeadGUID unique identifier
     * @param extensionBeadGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/concept-beads/{extendedBeadGUID}/extensions/{extensionBeadGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachConceptBeadExtension",
            description="Detach a concept bead from a concept bead that extended it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/"))

    public VoidResponse detachConceptBeadExtension(@PathVariable String serverName,
                                                   @PathVariable String extendedBeadGUID,
                                                   @PathVariable String extensionBeadGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachConceptBeadExtension(serverName, extendedBeadGUID, extensionBeadGUID, requestBody);
    }

    /**
     * Attach a solution port to the solution component that exposes it.
     *
     * @param serverName name of the server to route the request to
     * @param solutionComponentGUID unique identifier of the solution component
     * @param solutionPortGUID unique identifier of the solution port
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/solution-ports/{solutionPortGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSolutionComponentPort",
            description="Attach a solution port to the solution component that exposes it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse linkSolutionComponentPort(@PathVariable String serverName,
                                                  @PathVariable String solutionComponentGUID,
                                                  @PathVariable String solutionPortGUID,
                                                  @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSolutionComponentPort(serverName, solutionComponentGUID, solutionPortGUID, requestBody);
    }


    /**
     * Detach a solution port from the solution component that exposed it.
     *
     * @param serverName name of the server to route the request to
     * @param solutionComponentGUID unique identifier of the solution component
     * @param solutionPortGUID unique identifier of the solution port
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/solution-ports/{solutionPortGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSolutionComponentPort",
            description="Detach a solution port from the solution component that exposed it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachSolutionComponentPort(@PathVariable String serverName,
                                                    @PathVariable String solutionComponentGUID,
                                                    @PathVariable String solutionPortGUID,
                                                    @RequestBody (required = false)
                                                    DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionComponentPort(serverName, solutionComponentGUID, solutionPortGUID, requestBody);
    }


    /**
     * Attach a solution port to the solution port that it delegates to.
     *
     * @param serverName name of the server to route the request to
     * @param alignsToPortGUID unique identifier of the solution port that is aligned to
     * @param delegationPortGUID unique identifier of the solution port that delegates
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-ports/{alignsToPortGUID}/port-delegations/{delegationPortGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSolutionPortDelegation",
            description="Attach a solution port to the solution port that it delegates to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse linkSolutionPortDelegation(@PathVariable String serverName,
                                                   @PathVariable String alignsToPortGUID,
                                                   @PathVariable String delegationPortGUID,
                                                   @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSolutionPortDelegation(serverName, alignsToPortGUID, delegationPortGUID, requestBody);
    }


    /**
     * Detach a solution port from the solution port that it delegated to.
     *
     * @param serverName name of the server to route the request to
     * @param alignsToPortGUID unique identifier of the solution port that is aligned to
     * @param delegationPortGUID unique identifier of the solution port that delegates
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-ports/{alignsToPortGUID}/port-delegations/{delegationPortGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSolutionPortDelegation",
            description="Detach a solution port from the solution port that it delegated to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachSolutionPortDelegation(@PathVariable String serverName,
                                                     @PathVariable String alignsToPortGUID,
                                                     @PathVariable String delegationPortGUID,
                                                     @RequestBody (required = false)
                                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionPortDelegation(serverName, alignsToPortGUID, delegationPortGUID, requestBody);
    }
}
