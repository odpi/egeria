/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.privacyofficer.server.spring;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.privacyofficer.server.PrivacyOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The PrivacyOfficerResource provides part of the server-side implementation of the Privacy Officer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/privacy-officer")

@Tag(name="API: Privacy Officer", description="Supports a Privacy Officer as they lead the data privacy governance program.  This builds on the capabilities of the Governance Officer API.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/privacy-officer/overview/"))

public class PrivacyOfficerResource
{
    private final PrivacyOfficerRESTServices restAPI = new PrivacyOfficerRESTServices();

    /**
     * Default constructor
     */
    public PrivacyOfficerResource()
    {
    }


    /**
     * Link a data processing purpose to a data processing description.
     *
     * @param serverName         name of called server
     * @param dataProcessingPurposeGUID          unique identifier of the data processing purpose
     * @param dataProcessingDescriptionGUID  unique identifier of the data processing description
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-processing-purposes/{dataProcessingPurposeGUID}/permitted-processing/{dataProcessingDescriptionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkPermittedProcessing",
            description="Link a data processing purpose to a data processing description.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-purpose"))

    public VoidResponse linkPermittedProcessing(@PathVariable
                                                String                  serverName,
                                                @PathVariable
                                                String                  dataProcessingPurposeGUID,
                                                @PathVariable
                                                String                  dataProcessingDescriptionGUID,
                                                @RequestBody (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkPermittedProcessing(serverName, dataProcessingPurposeGUID, dataProcessingDescriptionGUID, requestBody);
    }


    /**
     * Detach a data processing purpose from a data processing description.
     *
     * @param serverName         name of called server
     * @param dataProcessingPurposeGUID          unique identifier of the data processing purpose
     * @param dataProcessingDescriptionGUID  unique identifier of the data processing description
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-processing-purposes/{dataProcessingPurposeGUID}/permitted-processing/{dataProcessingDescriptionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachPermittedProcessing",
            description="Detach a data processing purpose from a data processing description.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-purpose"))

    public VoidResponse detachPermittedProcessing(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable
                                                  String                    dataProcessingPurposeGUID,
                                                  @PathVariable
                                                  String                    dataProcessingDescriptionGUID,
                                                  @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachPermittedProcessing(serverName, dataProcessingPurposeGUID, dataProcessingDescriptionGUID, requestBody);
    }


    /**
     * Link a data processing action to a target element.
     *
     * @param serverName         name of called server
     * @param dataProcessingActionGUID          unique identifier of the data processing action
     * @param targetGUID  unique identifier of the target element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-processing-actions/{dataProcessingActionGUID}/targets/{targetGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDataProcessingTarget",
            description="Link a data processing action to a target element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-action"))

    public VoidResponse linkDataProcessingTarget(@PathVariable
                                                 String                  serverName,
                                                 @PathVariable
                                                 String                  dataProcessingActionGUID,
                                                 @PathVariable
                                                 String                  targetGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataProcessingTarget(serverName, dataProcessingActionGUID, targetGUID, requestBody);
    }


    /**
     * Detach a data processing action from a target element.
     *
     * @param serverName         name of called server
     * @param dataProcessingActionGUID          unique identifier of the data processing action
     * @param targetGUID  unique identifier of the target element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-processing-actions/{dataProcessingActionGUID}/targets/{targetGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataProcessingTarget",
            description="Detach a data processing action from a target element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-action"))

    public VoidResponse detachDataProcessingTarget(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable
                                                   String                    dataProcessingActionGUID,
                                                   @PathVariable
                                                   String                    targetGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataProcessingTarget(serverName, dataProcessingActionGUID, targetGUID, requestBody);
    }

    /**
     * Attach a data processing description to the element that performs the processing.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of the element that performs the data processing
     * @param dataProcessingDescriptionGUID unique identifier of the data processing description
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/data-processing-specifications/{dataProcessingDescriptionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDataProcessingSpecification",
            description="Attach a data processing description to the element that performs the processing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-description"))

    public VoidResponse linkDataProcessingSpecification(@PathVariable String serverName,
                                                        @PathVariable String elementGUID,
                                                        @PathVariable String dataProcessingDescriptionGUID,
                                                        @RequestBody (required = false)
                                                        NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataProcessingSpecification(serverName, elementGUID, dataProcessingDescriptionGUID, requestBody);
    }


    /**
     * Detach a data processing description from the element that performed the processing.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of the element that performs the data processing
     * @param dataProcessingDescriptionGUID unique identifier of the data processing description
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/data-processing-specifications/{dataProcessingDescriptionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataProcessingSpecification",
            description="Detach a data processing description from the element that performed the processing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-description"))

    public VoidResponse detachDataProcessingSpecification(@PathVariable String serverName,
                                                          @PathVariable String elementGUID,
                                                          @PathVariable String dataProcessingDescriptionGUID,
                                                          @RequestBody (required = false)
                                                          DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataProcessingSpecification(serverName, elementGUID, dataProcessingDescriptionGUID, requestBody);
    }


    /**
     * Attach a child data processing action to its parent data processing action.
     *
     * @param serverName name of the server to route the request to
     * @param parentProcessingActionGUID unique identifier of the parent data processing action
     * @param childProcessingActionGUID unique identifier of the child data processing action
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-processing-actions/{parentProcessingActionGUID}/detailed-processing-actions/{childProcessingActionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDetailedProcessingAction",
            description="Attach a child data processing action to its parent data processing action.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-description"))

    public VoidResponse linkDetailedProcessingAction(@PathVariable String serverName,
                                                     @PathVariable String parentProcessingActionGUID,
                                                     @PathVariable String childProcessingActionGUID,
                                                     @RequestBody (required = false)
                                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDetailedProcessingAction(serverName, parentProcessingActionGUID, childProcessingActionGUID, requestBody);
    }


    /**
     * Detach a child data processing action from its parent data processing action.
     *
     * @param serverName name of the server to route the request to
     * @param parentProcessingActionGUID unique identifier of the parent data processing action
     * @param childProcessingActionGUID unique identifier of the child data processing action
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-processing-actions/{parentProcessingActionGUID}/detailed-processing-actions/{childProcessingActionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDetailedProcessingAction",
            description="Detach a child data processing action from its parent data processing action.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-processing-description"))

    public VoidResponse detachDetailedProcessingAction(@PathVariable String serverName,
                                                       @PathVariable String parentProcessingActionGUID,
                                                       @PathVariable String childProcessingActionGUID,
                                                       @RequestBody (required = false)
                                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDetailedProcessingAction(serverName, parentProcessingActionGUID, childProcessingActionGUID, requestBody);
    }
}
