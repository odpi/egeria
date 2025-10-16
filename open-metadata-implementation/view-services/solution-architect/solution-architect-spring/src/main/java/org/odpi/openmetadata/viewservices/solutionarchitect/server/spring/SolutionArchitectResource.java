/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name="API: Solution Architect OMVS", description="During the planning phase of a project, architects typically use drawing tools to sketch out the new components that are to be developed and how they relate to existing components.  Solution blueprints are the open metadata equivalent of the sketch and they show the solution components and actors involved and how they collaborate.  The advantage of creating a solution blueprint over a sketch diagram is that it is easy to visualize different levels of detail and, as the project rolls out, the implementation of the components can be linked into the blueprint, providing traceability from project intent to actual operation.  In a similar way, information supply chains allow the modelling of key data flows needed by your organization.   These can then be linked to metadata about the systems and pipelines that implement them, providing a means to summarize statistics from lineage about the operation of the data flows.  The Solution Architect OMVS supports the definition and display of solution blueprints and their supporting solution components along with the relevant information supply chains.",
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains")

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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/from-template")
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
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/update")
    @Operation(summary="updateInformationSupplyChain",
            description="Update the properties of an information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse updateInformationSupplyChain(@PathVariable
                                                     String                                  serverName,
                                                     @PathVariable
                                                     String                                  informationSupplyChainGUID,
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{peerOneGUID}/peer-links/{peerTwoGUID}/attach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{peerOneGUID}/peer-links/{peerTwoGUID}/detach")
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
     * Connect a nested information supply chain to its parent.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the parent information supply chain
     * @param nestedInformationSupplyChainGUID      unique identifier of the child information supply chain
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/compositions/{nestedInformationSupplyChainGUID}/attach")
    @Operation(summary="composeInformationSupplyChains",
            description="Connect a nested information supply chain to its parent.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse composeInformationSupplyChains(@PathVariable
                                                        String                                serverName,
                                                        @PathVariable
                                                        String informationSupplyChainGUID,
                                                        @PathVariable
                                                        String nestedInformationSupplyChainGUID,
                                                        @RequestBody (required = false)
                                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.composeInformationSupplyChains(serverName, informationSupplyChainGUID, nestedInformationSupplyChainGUID, requestBody);
    }


    /**
     * Detach a nested information supply chain from its parent.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the parent information supply chain
     * @param nestedInformationSupplyChainGUID      unique identifier of the child information supply chain
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/compositions/{nestedInformationSupplyChainGUID}/detach")
    @Operation(summary="decomposeInformationSupplyChains",
            description="Detach a nested information supply chain from its parent.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse decomposeInformationSupplyChains(@PathVariable
                                                          String                    serverName,
                                                          @PathVariable
                                                          String informationSupplyChainGUID,
                                                          @PathVariable
                                                          String nestedInformationSupplyChainGUID,
                                                          @RequestBody (required = false)
                                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.decomposeInformationSupplyChains(serverName, informationSupplyChainGUID, nestedInformationSupplyChainGUID, requestBody);
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/delete")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/by-name")
    @Operation(summary="getInformationSupplyChainsByName",
            description="Returns the list of information supply chains with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public InformationSupplyChainsResponse getInformationSupplyChainsByName(@PathVariable
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/by-search-string")
    @Operation(summary="findInformationSupplyChains",
            description="Retrieve the list of information supply chain metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public InformationSupplyChainsResponse findInformationSupplyChains(@PathVariable
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/retrieve")
    @Operation(summary="getInformationSupplyChainByGUID",
            description="Return the properties of a specific information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public InformationSupplyChainResponse getInformationSupplyChainByGUID(@PathVariable
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints")

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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/from-template")
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
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints/{solutionBlueprintGUID}/update")
    @Operation(summary="updateSolutionBlueprint",
            description="Update the properties of a solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public VoidResponse updateSolutionBlueprint(@PathVariable
                                                String                                  serverName,
                                                @PathVariable
                                                String                                  solutionBlueprintGUID,
                                                @RequestBody (required = false)
                                                UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSolutionBlueprint(serverName, solutionBlueprintGUID, requestBody);
    }


    /**
     * Attach a solution component to a solution blueprint.
     *
     * @param serverName         name of called server
     * @param parentSolutionBlueprintGUID  unique identifier of the first solution blueprint
     * @param solutionComponentGUID      unique identifier of the second solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints/{parentSolutionBlueprintGUID}/solution-components/{solutionComponentGUID}/attach")
    @Operation(summary="linkSolutionComponentToBlueprint",
            description="Attach a solution component to a solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public VoidResponse linkSolutionComponentToBlueprint(@PathVariable
                                                         String                                serverName,
                                                         @PathVariable
                                                         String parentSolutionBlueprintGUID,
                                                         @PathVariable
                                                         String solutionComponentGUID,
                                                         @RequestBody (required = false)
                                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSolutionComponentToBlueprint(serverName, parentSolutionBlueprintGUID, solutionComponentGUID, requestBody);
    }


    /**
     * Detach a solution component from a solution blueprint.
     *
     * @param serverName         name of called server
     * @param parentSolutionBlueprintGUID  unique identifier of the first solution blueprint
     * @param solutionComponentGUID      unique identifier of the second solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints/{parentSolutionBlueprintGUID}/solution-components/{solutionComponentGUID}/detach")
    @Operation(summary="detachSolutionComponentFromBlueprint",
            description="Detach a solution component from a solution blueprint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public VoidResponse detachSolutionComponentFromBlueprint(@PathVariable
                                                             String                    serverName,
                                                             @PathVariable
                                                             String parentSolutionBlueprintGUID,
                                                             @PathVariable
                                                             String solutionComponentGUID,
                                                             @RequestBody (required = false)
                                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionComponentFromBlueprint(serverName, parentSolutionBlueprintGUID, solutionComponentGUID, requestBody);
    }




    /**
     * Attach a solution blueprint to the element that is describes.
     *
     * @param serverName         name of called server
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{parentGUID}/solution-designs/{solutionBlueprintGUID}/attach")
    @Operation(summary="linkSolutionDesign",
            description="Attach a solution blueprint to the element that is describes.",
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{parentGUID}/solution-designs/{solutionBlueprintGUID}/detach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-blueprints/{solutionBlueprintGUID}/delete")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/by-name")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/{solutionBlueprintGUID}/retrieve")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/by-search-string")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/solution-component-actors/{dataFieldGUID}/attach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/solution-component-actors/{solutionComponentGUID}/detach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components")

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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/from-template")
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
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/update")
    @Operation(summary="updateSolutionComponent",
            description="Update the properties of a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse updateSolutionComponent(@PathVariable
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/subcomponents/{subcomponentGUID}/attach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{parentSolutionComponentGUID}/subcomponents/{subcomponentGUID}/detach")
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
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentOneGUID}/wired-to/{solutionComponentTwoGUID}/attach")
    @Operation(summary="linkSolutionLinkingWire",
            description="Attach a solution component to a solution component as a peer in a solution.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse linkSolutionLinkingWire(@PathVariable
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
     * Detach a solution component from a peer solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentOneGUID  unique identifier of the first solution component
     * @param solutionComponentTwoGUID      unique identifier of the second solution component
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentOneGUID}/wired-to/{solutionComponentTwoGUID}/detach")
    @Operation(summary="detachSolutionLinkingWire",
            description="Detach a solution component from a peer solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachSolutionLinkingWire(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String solutionComponentOneGUID,
                                           @PathVariable
                                           String solutionComponentTwoGUID,
                                           @RequestBody (required = false)
                                                      DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSolutionLinkingWire(serverName, solutionComponentOneGUID, solutionComponentTwoGUID, requestBody);
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/delete")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/by-name")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/by-search-string")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/retrieve")
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/implementations")
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
}
