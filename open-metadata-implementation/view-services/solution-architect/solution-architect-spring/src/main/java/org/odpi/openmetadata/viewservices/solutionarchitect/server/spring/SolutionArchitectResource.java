/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.solutionarchitect.server.SolutionArchitectRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SolutionArchitectResource provides part of the server-side implementation of the Solution Architect OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/solution-architect")

@Tag(name="API: Solution Architect OMVS", description="The Solution Architect OMVS is a REST API designed to support user interfaces (UIs) relating to the definition and display of solution blueprints and their supporting solution components along with the relevant information supply chains.",
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
                                                     NewInformationSupplyChainRequestBody requestBody)
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                                 replaceAllProperties,
                                                     @RequestBody (required = false)
                                                     UpdateInformationSupplyChainRequestBody requestBody)
    {
        return restAPI.updateInformationSupplyChain(serverName, informationSupplyChainGUID, replaceAllProperties, requestBody);
    }


    /**
     * Create an information supply chain segment and link it to its owning information supply chain.
     *
     * @param serverName                 name of called server.
     * @param informationSupplyChainGUID unique identifier of optional parent information supply chain
     * @param requestBody             properties for the information supply chain.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/{informationSupplyChainGUID}/segments")
    @Operation(summary="createInformationSupplyChainSegment",
            description="Create an information supply chain segment and link it to its owning information supply chain.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public GUIDResponse createInformationSupplyChainSegment(@PathVariable
                                                            String                                   serverName,
                                                            @PathVariable
                                                            String                                   informationSupplyChainGUID,
                                                            @RequestBody (required = false)
                                                            InformationSupplyChainSegmentRequestBody requestBody)
    {
        return restAPI.createInformationSupplyChainSegment(serverName, informationSupplyChainGUID, requestBody);
    }


    /**
     * Update the properties of an information supply chain segment.
     *
     * @param serverName         name of called server.
     * @param segmentGUID unique identifier of the information supply chain segment (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/segments/{segmentGUID}/update")
    @Operation(summary="updateInformationSupplyChainSegment",
            description="Update the properties of an information supply chain segment.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse   updateInformationSupplyChainSegment(@PathVariable
                                                              String                                   serverName,
                                                              @PathVariable
                                                              String                                   segmentGUID,
                                                              @RequestParam (required = false, defaultValue = "false")
                                                              boolean                                  replaceAllProperties,
                                                              @RequestBody (required = false)
                                                              InformationSupplyChainSegmentRequestBody requestBody)
    {
        return restAPI.updateInformationSupplyChainSegment(serverName, segmentGUID, replaceAllProperties, requestBody);
    }


    /**
     * Connect two information supply chain segments.
     *
     * @param serverName         name of called server
     * @param segment1GUID  unique identifier of the first segment
     * @param segment2GUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/segments/{segment1GUID}/link-to/{segment2GUID}/attach")
    @Operation(summary="linkSegments",
            description="Connect two information supply chain segments.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse linkSegments(@PathVariable
                                     String                                serverName,
                                     @PathVariable
                                     String                                segment1GUID,
                                     @PathVariable
                                     String                                segment2GUID,
                                     @RequestBody (required = false)
                                     InformationSupplyChainLinkRequestBody requestBody)
    {
        return restAPI.linkSegments(serverName, segment1GUID, segment2GUID, requestBody);
    }


    /**
     * Detach two information supply chain segments from one another.
     *
     * @param serverName         name of called server
     * @param segment1GUID  unique identifier of the first segment
     * @param segment2GUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/segments/{segment1GUID}/link-to/{segment2GUID}/detach")
    @Operation(summary="detachSegments",
            description="Detach two information supply chain segments from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse detachSegments(@PathVariable
                                       String                    serverName,
                                       @PathVariable
                                       String                    segment1GUID,
                                       @PathVariable
                                       String                    segment2GUID,
                                       @RequestBody (required = false)
                                       MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSegments(serverName, segment1GUID, segment2GUID, requestBody);
    }


    /**
     * Delete an information supply chain segment.
     *
     * @param serverName         name of called server
     * @param segmentGUID  unique identifier of the  segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/information-supply-chains/segments/{segmentGUID}/delete")
    @Operation(summary="deleteInformationSupplyChainSegment",
            description="Delete an information supply chain segment.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public VoidResponse deleteInformationSupplyChainSegment(@PathVariable
                                                            String                    serverName,
                                                            @PathVariable
                                                            String                    segmentGUID,
                                                            @RequestBody (required = false)
                                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteInformationSupplyChainSegment(serverName, segmentGUID, requestBody);
    }


    /**
     * Delete an information supply chain.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the element to delete
     * @param cascadedDelete can information supply chains be deleted if segments are attached?
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
                                                     @RequestParam(required = false, defaultValue = "false")
                                                     boolean                   cascadedDelete,
                                                     @RequestBody (required = false)
                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteInformationSupplyChain(serverName, informationSupplyChainGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of information supply chains with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int                     startFrom,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int                     pageSize,
                                                                            @RequestBody (required = false)
                                                                            FilterRequestBody requestBody)
    {
        return restAPI.getInformationSupplyChainsByName(serverName, addImplementation, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
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
    @PostMapping(path = "/information-supply-chains/by-search-string")
    @Operation(summary="findInformationSupplyChains",
            description="Retrieve the list of information supply chain metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public InformationSupplyChainsResponse findInformationSupplyChains(@PathVariable
                                                                       String                  serverName,
                                                                       @RequestParam (required = false, defaultValue = "true")
                                                                       boolean                 addImplementation,
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
        return restAPI.findInformationSupplyChains(serverName, addImplementation, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
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
                                                                          AnyTimeRequestBody requestBody)
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
                                                NewSolutionBlueprintRequestBody requestBody)
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                                @RequestParam (required = false, defaultValue = "false")
                                                boolean                                 replaceAllProperties,
                                                @RequestBody (required = false)
                                                UpdateSolutionBlueprintRequestBody requestBody)
    {
        return restAPI.updateSolutionBlueprint(serverName, solutionBlueprintGUID, replaceAllProperties, requestBody);
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
                                                         SolutionBlueprintCompositionRequestBody requestBody)
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
                                                             MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSolutionComponentFromBlueprint(serverName, parentSolutionBlueprintGUID, solutionComponentGUID, requestBody);
    }


    /**
     * Delete a solution blueprint.
     *
     * @param serverName         name of called server
     * @param solutionBlueprintGUID  unique identifier of the element to delete
     * @param cascadedDelete can solution blueprints be deleted if solution components are attached?
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
                                                @RequestParam(required = false, defaultValue = "false")
                                                boolean                   cascadedDelete,
                                                @RequestBody (required = false)
                                                MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteSolutionBlueprint(serverName, solutionBlueprintGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of solution blueprints with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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

    public SolutionBlueprintsResponse getSolutionBlueprintsByName(@PathVariable
                                                                  String            serverName,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                  int                     startFrom,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                  int                     pageSize,
                                                                  @RequestBody (required = false)
                                                                  FilterRequestBody requestBody)
    {
        return restAPI.getSolutionBlueprintsByName(serverName, startFrom, pageSize, requestBody);
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

    public SolutionBlueprintResponse getSolutionBlueprintByGUID(@PathVariable
                                                                String             serverName,
                                                                @PathVariable
                                                                String             solutionBlueprintGUID,
                                                                @RequestBody (required = false)
                                                                AnyTimeRequestBody requestBody)
    {
        return restAPI.getSolutionBlueprintByGUID(serverName, solutionBlueprintGUID, requestBody);
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.
     *
     * @param serverName name of the service to route the request to
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
    @PostMapping(path = "/solution-blueprints/by-search-string")
    @Operation(summary="findSolutionBlueprints",
            description="Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public SolutionBlueprintsResponse findSolutionBlueprints(@PathVariable String                  serverName,
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
                                                             @RequestBody  (required = false)
                                                             FilterRequestBody requestBody)
    {
        return restAPI.findSolutionBlueprints(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }



    /**
     * Create a solution role.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the solution role.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles")

    @Operation(summary="createSolutionRole",
            description="Create a solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public GUIDResponse createSolutionRole(@PathVariable
                                           String                  serverName,
                                           @RequestBody (required = false)
                                           NewActorRoleRequestBody requestBody)
    {
        return restAPI.createSolutionRole(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a solution role using an existing metadata element as a template.
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
    @PostMapping(path = "/solution-roles/from-template")
    @Operation(summary="createSolutionRoleFromTemplate",
            description="Create a new metadata element to represent a solution role using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public GUIDResponse createSolutionRoleFromTemplate(@PathVariable
                                                       String              serverName,
                                                       @RequestBody (required = false)
                                                       TemplateRequestBody requestBody)
    {
        return restAPI.createSolutionRoleFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a solution role.
     *
     * @param serverName         name of called server.
     * @param solutionRoleGUID unique identifier of the solution role (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/update")
    @Operation(summary="updateSolutionRole",
            description="Update the properties of a solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public VoidResponse updateSolutionRole(@PathVariable
                                           String                                  serverName,
                                           @PathVariable
                                           String                                  solutionRoleGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                           boolean                                 replaceAllProperties,
                                           @RequestBody (required = false)
                                           UpdateActorRoleRequestBody requestBody)
    {
        return restAPI.updateSolutionRole(serverName, solutionRoleGUID, replaceAllProperties, requestBody);
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
                                                   SolutionComponentActorRequestBody requestBody)
    {
        return restAPI.linkSolutionComponentActor(serverName, solutionRoleGUID, dataFieldGUID, requestBody);
    }


    /**
     * Detach a solution component from a solution role.
     *
     * @param serverName         name of called server
     * @param parentSolutionRoleGUID  unique identifier of the first solution role
     * @param memberDataFieldGUID      unique identifier of the second solution role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{parentSolutionRoleGUID}/solution-component-actors/{memberDataFieldGUID}/detach")
    @Operation(summary="detachSolutionComponentActor",
            description="Detach a solution component from a solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public VoidResponse detachSolutionComponentActor(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable
                                                     String parentSolutionRoleGUID,
                                                     @PathVariable
                                                     String memberDataFieldGUID,
                                                     @RequestBody (required = false)
                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSolutionComponentActor(serverName, parentSolutionRoleGUID, memberDataFieldGUID, requestBody);
    }


    /**
     * Delete a solution role.
     *
     * @param serverName         name of called server
     * @param solutionRoleGUID  unique identifier of the element to delete
     * @param cascadedDelete can solution roles be deleted if solution components are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/delete")
    @Operation(summary="deleteSolutionRole",
            description="Delete a solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public VoidResponse deleteSolutionRole(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String                    solutionRoleGUID,
                                           @RequestParam(required = false, defaultValue = "false")
                                           boolean                   cascadedDelete,
                                           @RequestBody (required = false)
                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteSolutionRole(serverName, solutionRoleGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of solution roles with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-roles/by-name")
    @Operation(summary="getSolutionRolesByName",
            description="Returns the list of solution roles with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public SolutionRolesResponse getSolutionRolesByName(@PathVariable
                                                        String            serverName,
                                                        @RequestParam (required = false, defaultValue = "0")
                                                        int                     startFrom,
                                                        @RequestParam (required = false, defaultValue = "0")
                                                        int                     pageSize,
                                                        @RequestBody (required = false)
                                                        FilterRequestBody requestBody)
    {
        return restAPI.getSolutionRolesByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     *
     * @param serverName name of the service to route the request to
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
    @PostMapping(path = "/solution-roles/by-search-string")
    @Operation(summary="findSolutionRoles",
            description="Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor"))

    public SolutionRolesResponse findSolutionRoles(@PathVariable String                  serverName,
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
                                                   @RequestBody  (required = false)
                                                   FilterRequestBody requestBody)
    {
        return restAPI.findSolutionRoles(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific solution role.
     *
     * @param serverName name of the service to route the request to
     * @param solutionRoleGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-roles/{solutionRoleGUID}/retrieve")
    @Operation(summary="getSolutionRoleByGUID",
            description="Return the properties of a specific solution role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-role"))

    public SolutionRoleResponse getSolutionRoleByGUID(@PathVariable
                                                      String             serverName,
                                                      @PathVariable
                                                      String             solutionRoleGUID,
                                                      @RequestBody (required = false)
                                                      AnyTimeRequestBody requestBody)
    {
        return restAPI.getSolutionRoleByGUID(serverName, solutionRoleGUID, requestBody);
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
                                                NewSolutionComponentRequestBody requestBody)
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                                String                                  serverName,
                                                @PathVariable
                                                String                                  solutionComponentGUID,
                                                @RequestParam (required = false, defaultValue = "false")
                                                boolean                                 replaceAllProperties,
                                                @RequestBody (required = false)
                                                UpdateSolutionComponentRequestBody requestBody)
    {
        return restAPI.updateSolutionComponent(serverName, solutionComponentGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a solution component to a solution component.
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
            description="Attach a solution component to a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse linkSubcomponent(@PathVariable
                                         String                     serverName,
                                         @PathVariable
                                         String                     solutionComponentGUID,
                                         @PathVariable
                                         String                     subcomponentGUID,
                                         @RequestBody (required = false)
                                         RelationshipRequestBody requestBody)
    {
        return restAPI.linkSubcomponent(serverName, solutionComponentGUID, subcomponentGUID, requestBody);
    }


    /**
     * Detach a solution component from a solution component.
     *
     * @param serverName         name of called server
     * @param parentSolutionComponentGUID  unique identifier of the first solution component
     * @param memberDataFieldGUID      unique identifier of the second solution component
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/solution-components/{parentSolutionComponentGUID}/subcomponents/{memberDataFieldGUID}/detach")
    @Operation(summary="detachSubcomponent",
            description="Detach a solution component from a solution component.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public VoidResponse detachSubcomponent(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String parentSolutionComponentGUID,
                                           @PathVariable
                                           String memberDataFieldGUID,
                                           @RequestBody (required = false)
                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSubcomponent(serverName, parentSolutionComponentGUID, memberDataFieldGUID, requestBody);
    }


    /**
     * Delete a solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentGUID  unique identifier of the element to delete
     * @param cascadedDelete can solution components be deleted if solution components are attached?
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
                                                @RequestParam(required = false, defaultValue = "false")
                                                boolean                   cascadedDelete,
                                                @RequestBody (required = false)
                                                MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteSolutionComponent(serverName, solutionComponentGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of solution components with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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

    public SolutionComponentsResponse getSolutionComponentsByName(@PathVariable
                                                                  String            serverName,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                  int                     startFrom,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                  int                     pageSize,
                                                                  @RequestBody (required = false)
                                                                  FilterRequestBody requestBody)
    {
        return restAPI.getSolutionComponentsByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     *
     * @param serverName name of the service to route the request to
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
    @PostMapping(path = "/solution-components/by-search-string")
    @Operation(summary="findSolutionComponents",
            description="Retrieve the list of solution metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public SolutionComponentsResponse findSolutionComponents(@PathVariable String                  serverName,
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
        return restAPI.findSolutionComponents(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
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

    public SolutionComponentResponse getSolutionComponentByGUID(@PathVariable
                                                                String             serverName,
                                                                @PathVariable
                                                                String             solutionComponentGUID,
                                                                @RequestBody (required = false)
                                                                AnyTimeRequestBody requestBody)
    {
        return restAPI.getSolutionComponentByGUID(serverName, solutionComponentGUID, requestBody);
    }



    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                                               @RequestParam (required = false, defaultValue = "0")
                                                                               int                     startFrom,
                                                                               @RequestParam (required = false, defaultValue = "0")
                                                                               int                     pageSize,
                                                                               @RequestBody(required = false)
                                                                               ResultsRequestBody requestBody)
    {
        return restAPI.getSolutionComponentImplementations(serverName, solutionComponentGUID, startFrom, pageSize, requestBody);
    }
}
