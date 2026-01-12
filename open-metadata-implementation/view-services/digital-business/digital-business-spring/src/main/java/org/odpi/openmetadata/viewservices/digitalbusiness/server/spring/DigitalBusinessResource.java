/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.digitalbusiness.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.digitalbusiness.server.DigitalBusinessRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DigitalBusinessResource provides part of the server-side implementation of the Digital Business OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/digital-business")

@Tag(name="API: Digital Business", description="Manages definitions of business capabilities that show how the capabilities that support the organization's business are organized.  This is a key source of business context.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/digital-business/overview/"))

public class DigitalBusinessResource
{
    private final DigitalBusinessRESTServices restAPI = new DigitalBusinessRESTServices();

    /**
     * Default constructor
     */
    public DigitalBusinessResource()
    {
    }


    /**
     * Link dependent business capabilities.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID  unique identifier of the first business capability definition
     * @param supportingBusinessCapabilityGUID      unique identifier of the second business capability definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/business-capabilities/{businessCapabilityGUID}/dependencies/{supportingBusinessCapabilityGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkBusinessCapabilityDependency",
            description="Link dependent business capabilities.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/business-capability"))

    public VoidResponse linkBusinessCapabilityDependency(@PathVariable
                                                         String                  serverName,
                                                         @PathVariable
                                                         String                  businessCapabilityGUID,
                                                         @PathVariable
                                                         String supportingBusinessCapabilityGUID,
                                                         @RequestBody (required = false)
                                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkBusinessCapabilityDependency(serverName, businessCapabilityGUID, supportingBusinessCapabilityGUID, requestBody);
    }


    /**
     * Detach dependent business capabilities.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID  unique identifier of the first business capability definition
     * @param supportingBusinessCapabilityGUID      unique identifier of the second business capability definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/business-capabilities/{businessCapabilityGUID}/dependencies/{supportingBusinessCapabilityGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachBusinessCapabilityDependency",
            description="Detach dependent business capabilities.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/business-capability"))

    public VoidResponse detachBusinessCapabilityDependency(@PathVariable
                                                           String                    serverName,
                                                           @PathVariable
                                                           String businessCapabilityGUID,
                                                           @PathVariable
                                                           String supportingBusinessCapabilityGUID,
                                                           @RequestBody (required = false)
                                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachBusinessCapabilityDependency(serverName, businessCapabilityGUID, supportingBusinessCapabilityGUID, requestBody);
    }



    /**
     * Attach a business capability to an element that provides digital support.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/business-capabilities/{businessCapabilityGUID}/digital-support/{elementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDigitalSupport",
            description="Attach a business capability to an element that provides digital support.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/business-capability"))

    public VoidResponse linkDigitalSupport(@PathVariable
                                           String                  serverName,
                                           @PathVariable
                                           String                  businessCapabilityGUID,
                                           @PathVariable
                                           String elementGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDigitalSupport(serverName, businessCapabilityGUID, elementGUID, requestBody);
    }


    /**
     * Detach a business capability definition from a hierarchical relationship.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param requestBody  description of the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/business-capabilities/{businessCapabilityGUID}/digital-support/{elementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDigitalSupport",
            description="Detach a nested business capability from a broader business capability definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/business-capability"))

    public VoidResponse detachDigitalSupport(@PathVariable
                                             String                    serverName,
                                             @PathVariable
                                             String businessCapabilityGUID,
                                             @PathVariable
                                             String elementGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDigitalSupport(serverName, businessCapabilityGUID, elementGUID, requestBody);
    }




    /**
     * Classify an element to indicate that it is significant to a particular business capability.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/business-significant")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setBusinessSignificant",
            description="Classify an element to indicate that it is significant to a particular business capability.",
            externalDocs=@ExternalDocumentation(description="Subject Areas",
                    url="https://egeria-project.org/concepts/business-capability/"))

    public VoidResponse setBusinessSignificant(@PathVariable String                    serverName,
                                               @PathVariable String                    elementGUID,
                                               @RequestBody  (required = false)
                                               NewClassificationRequestBody requestBody)
    {
        return restAPI.setBusinessSignificant(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the business significant classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/business-significant/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearBusinessSignificance",
            description="Remove the business significant classification from the element.",
            externalDocs=@ExternalDocumentation(description="Subject Areas",
                    url="https://egeria-project.org/concepts/business-capability/"))

    public VoidResponse clearBusinessSignificance(@PathVariable String                    serverName,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false) DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearBusinessSignificance(serverName, elementGUID, requestBody);
    }
}
