/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITInfrastructureRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The ITInfrastructureResource provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/it-infrastructure/users/{userId}")

@Tag(name="IT Infrastructure OMAS", description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets." +
        "\n", externalDocs=@ExternalDocumentation(description="IT Infrastructure Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/it-infrastructure/"))

public class ITInfrastructureResource
{
    private ITInfrastructureRESTServices restAPI = new ITInfrastructureRESTServices();

    /**
     * Default constructor
     */
    public ITInfrastructureResource()
    {
    }


    /* ============================================================================
     * A connection links to an endpoint  to define where the resource is located
     */

    /**
     * Create a new metadata element to represent a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints")

    public GUIDResponse createEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.createEndpoint(serverName, userId, null, requestBody);
    }



    /**
     * Create a new metadata element to represent a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/for-infrastructure/{infrastructureGUID}")

    public GUIDResponse createEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              infrastructureGUID,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.createEndpoint(serverName, userId, infrastructureGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a endpoint  using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/network-address/{networkAddress}/from-template/{templateGUID}")

    public GUIDResponse createEndpointFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              networkAddress,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createEndpointFromTemplate(serverName, userId, null, networkAddress, templateGUID, requestBody);
    }



    /**
     * Create a new metadata element to represent a endpoint  using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/for-infrastructure/{infrastructureGUID}/network-address/{networkAddress}/from-template/{templateGUID}")

    public GUIDResponse createEndpointFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              infrastructureGUID,
                                                   @PathVariable String              networkAddress,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createEndpointFromTemplate(serverName, userId, infrastructureGUID, networkAddress, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}")

    public VoidResponse updateEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              endpointGUID,
                                       @RequestParam boolean             isMergeUpdate,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.updateEndpoint(serverName, userId, endpointGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/delete")

    public VoidResponse removeEndpoint(@PathVariable String                    serverName,
                                       @PathVariable String                    userId,
                                       @PathVariable String                    endpointGUID,
                                       @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeEndpoint(serverName, userId, endpointGUID, requestBody);
    }


    /**
     * Retrieve the list of endpoint  metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/endpoints/by-search-string/{searchString}")

    public EndpointsResponse findEndpoints(@PathVariable String serverName,
                                           @PathVariable String userId,
                                           @PathVariable String searchString,
                                           @RequestParam int    startFrom,
                                           @RequestParam int    pageSize)
    {
        return restAPI.findEndpoints(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint  metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/endpoints/by-name/{name}")

    public EndpointsResponse getEndpointsByName(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String name,
                                                @RequestParam int    startFrom,
                                                @RequestParam int    pageSize)
    {
        return restAPI.getEndpointsByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint  metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/endpoints/{guid}")

    public EndpointResponse getEndpointByGUID(@PathVariable String serverName,
                                              @PathVariable String userId,
                                              @PathVariable String guid)
    {
        return restAPI.getEndpointByGUID(serverName, userId, guid);
    }

}
