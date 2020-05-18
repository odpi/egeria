/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapabilitiesProperties;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for data platform independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}")

@Tag(name="Data Platform OMAS",
        description="The Data Platform OMAS provides APIs for tools and applications wishing to manage metadata relating to data platforms.",
        externalDocs=@ExternalDocumentation(description="Data Platform Open Metadata Access Service (OMAS)",
                url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-platform/"))

public class DataPlatformOMASResource
{
    private DataPlatformRESTServices restAPI = new DataPlatformRESTServices();


    /**
     * Instantiates a new Data Platform OMAS resource.
     */
    public DataPlatformOMASResource()
    {
    }


    /**
     * Return the connection object for the Discovery Engine OMAS's out topic.
     *
     * @param serverName name of the server to route the request to.
     * @param userId identifier of calling user.
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection")

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId)
    {
        return restAPI.getOutTopicConnection(serverName, userId);
    }


    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data platform.  Typically this is Egeria's data platform proxy.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param integratorCapabilities description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/integrators")

    public GUIDResponse createDataPlatformIntegrator(@PathVariable String                               serverName,
                                                     @PathVariable String                               userId,
                                                     @RequestBody  SoftwareServerCapabilitiesProperties integratorCapabilities)
    {
        return restAPI.createDataPlatformIntegrator(serverName, userId, integratorCapabilities);
    }


    /**
     * Retrieve the unique identifier of the integration daemon.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @GetMapping(path = "integrators/by-name/{qualifiedName}")

    public GUIDResponse  getDataPlatformIntegratorGUID(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String qualifiedName)
    {
        return restAPI.getDataPlatformIntegratorGUID(serverName, userId, qualifiedName);
    }
}
