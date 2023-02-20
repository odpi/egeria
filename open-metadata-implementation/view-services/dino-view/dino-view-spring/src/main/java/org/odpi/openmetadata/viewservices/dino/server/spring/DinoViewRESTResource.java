/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.dino.server.spring;



import org.odpi.openmetadata.viewservices.dino.api.rest.*;
import org.odpi.openmetadata.viewservices.dino.server.DinoViewRESTServices;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * The DinoViewRESTResource provides the Spring API endpoints of the Dino Open Metadata View Service (OMVS).
 * This interface provides an interfaces for Egeria operators.
 */

@RestController
@RequestMapping("/servers/{viewServerName}/open-metadata/view-services/dino/users/{userId}")

@Tag(name="Dino OMVS", description="Explore topology information in an Egeria deployment of platforms, servers, services and cohorts for graph visualization.", externalDocs=@ExternalDocumentation(description="Dino View Service (OMVS)",url="https://egeria-project.org/services/omvs/dino/overview/"))

public class DinoViewRESTResource {

    private final DinoViewRESTServices restAPI = new DinoViewRESTServices();


    /**
     * Default constructor
     */
    public DinoViewRESTResource() {
    }

    /**
     * Get the configured resource endpoints
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @return response object containing the list of resource endpoints or exception information
     */

    @GetMapping("/resource-endpoints")
    public DinoResourceEndpointListResponse getResourceEndpoints(@PathVariable String        viewServerName,
                                                                 @PathVariable String        userId) {
        return restAPI.getResourceEndpointList(viewServerName, userId);

    }

    /**
     * Get the platform overview
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the overview of the platform or exception information
     */

    @PostMapping("/platform/{platformName}")
    public DinoPlatformOverviewResponse getPlatformOverview(@PathVariable String                           viewServerName,
                                                            @PathVariable String                           userId,
                                                            @PathVariable String                           platformName,
                                                            @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetOverview(viewServerName, userId, requestBody);

    }



    /**
     * Get the platform origin
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the platform's origin string or exception information
     */

    @PostMapping("/platform/{platformName}/origin")
    public DinoStringResponse getPlatformOrigin(@PathVariable String                           viewServerName,
                                                @PathVariable String                           userId,
                                                @PathVariable String                           platformName,
                                                @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetOrigin(viewServerName, userId, requestBody);

    }


    /**
     * Get the active servers on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of names of active servers or exception information
     */

    @PostMapping("/platform/{platformName}/servers/active")
    public DinoServerListResponse getActiveServers(@PathVariable String                           viewServerName,
                                                   @PathVariable String                           userId,
                                                   @PathVariable String                           platformName,
                                                   @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetActiveServers(viewServerName, userId, requestBody);

    }



    /**
     * Get the known servers on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of names of all known servers or exception information
     */

    @PostMapping("/platform/{platformName}/servers")
    public DinoServerListResponse getKnownServers(@PathVariable String                           viewServerName,
                                                   @PathVariable String                          userId,
                                                   @PathVariable String                          platformName,
                                                   @RequestBody DinoPlatformRequestBody          requestBody  ) {
        return restAPI.platformGetKnownServers(viewServerName, userId, requestBody);

    }

    /**
     * Get the access services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/access-services")
    public DinoServiceListResponse getAccessServices(@PathVariable String                           viewServerName,
                                                     @PathVariable String                           userId,
                                                     @PathVariable String                           platformName,
                                                     @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetAccessServices(viewServerName, userId, requestBody);

    }

    /**
     * Get the view services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/view-services")
    public DinoServiceListResponse getViewServices(@PathVariable String                           viewServerName,
                                                   @PathVariable String                           userId,
                                                   @PathVariable String                           platformName,
                                                   @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetViewServices(viewServerName, userId, requestBody);

    }
    /**
     * Get the governance services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/governance-services")
    public DinoServiceListResponse getGovernanceServices(@PathVariable String                           viewServerName,
                                                         @PathVariable String                           userId,
                                                         @PathVariable String                           platformName,
                                                         @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetGovernanceServices(viewServerName, userId, requestBody);

    }


    /**
     * Get the common services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param platformName     name of the platform
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/common-services")
    public DinoServiceListResponse getCommonServices(@PathVariable String                           viewServerName,
                                                     @PathVariable String                           userId,
                                                     @PathVariable String                           platformName,
                                                     @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetCommonServices(viewServerName, userId, requestBody);

    }


    /**
     * Get the server overview
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the overview of the server or exception information
     */

    @PostMapping("/server/{serverName}")
    public DinoServerOverviewResponse getServerOverview(@PathVariable String                           viewServerName,
                                                        @PathVariable String                           userId,
                                                        @PathVariable String                           serverName,
                                                        @RequestBody DinoServerRequestBody             requestBody  ) {
        return restAPI.serverGetOverview(viewServerName, userId, requestBody);

    }


    /**
     * Get the server origin
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the server origin or exception information
     */

    @PostMapping("/server/{serverName}/origin")
    public DinoStringResponse getServerOrigin(@PathVariable String                          viewServerName,
                                                @PathVariable String                        userId,
                                                @PathVariable String                        serverName,
                                                @RequestBody DinoServerRequestBody          requestBody  ) {
        return restAPI.serverGetOrigin(viewServerName, userId, requestBody);

    }

    /**
     * Get the server type classification
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the server type information or exception information
     */

    @PostMapping("/server/{serverName}/server-type-classification")
    public DinoServerTypeResponse getServerTypeClassification(@PathVariable String                           viewServerName,
                                                              @PathVariable String                           userId,
                                                              @PathVariable String                           serverName,
                                                              @RequestBody DinoServerRequestBody             requestBody  ) {
        return restAPI.serverGetTypeClassification(viewServerName, userId, requestBody);

    }

    /**
     * Get the server's stored configuration
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the server configuration or exception information
     */

    @PostMapping("/server/{serverName}/configuration")
    public DinoServerConfigResponse getServerStoredConfiguration(@PathVariable String                   viewServerName,
                                                                 @PathVariable String                   userId,
                                                                 @PathVariable String                   serverName,
                                                                 @RequestBody DinoServerRequestBody     requestBody  ) {
        return restAPI.serverGetStoredConfiguration(viewServerName, userId, requestBody);

    }


    /**
     * Get the server's active (running instance) configuration
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the server configuration or exception information
     */

    @PostMapping("/server/{serverName}/instance/configuration")
    public DinoServerConfigResponse getServerInstanceConfiguration(@PathVariable String                   viewServerName,
                                                                   @PathVariable String                   userId,
                                                                   @PathVariable String                   serverName,
                                                                   @RequestBody DinoServerRequestBody     requestBody  ) {
        return restAPI.serverGetInstanceConfiguration(viewServerName, userId, requestBody);

    }

    /**
     * Get the server's stored and active (running instance) configurations in a duplexed response
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the server configuration or exception information
     */

    @PostMapping("/server/{serverName}/stored-and-active-configuration")
    public DinoServerDoubleConfigResponse getServerStoredAndActiveConfiguration(@PathVariable String                   viewServerName,
                                                                                @PathVariable String                   userId,
                                                                                @PathVariable String                   serverName,
                                                                                @RequestBody DinoServerRequestBody     requestBody  ) {
        return restAPI.serverGetStoredAndActiveConfiguration(viewServerName, userId, requestBody);

    }

    /**
     * Get the server's audit log
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the server's audit log or exception information
     */

    @PostMapping("/server/{serverName}/audit-log")
    public DinoServerAuditLogResponse getServerAuditLog(@PathVariable String                           viewServerName,
                                                        @PathVariable String                           userId,
                                                        @PathVariable String                           serverName,
                                                        @RequestBody DinoServerRequestBody             requestBody  ) {
        return restAPI.serverGetAuditLog(viewServerName, userId, requestBody);

    }

    /**
     * Get a list of the integration services on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of integration services or exception information
     */

    @PostMapping("/server/{serverName}/integration-services")
    public DinoServiceListResponse getServerIntegrationServices(@PathVariable String                      viewServerName,
                                                                @PathVariable String                      userId,
                                                                @PathVariable String                      serverName,
                                                                @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetIntegrationServices(viewServerName, userId, requestBody);

    }

    /**
     * Get a list of the engine services on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of integration services or exception information
     */

    @PostMapping("/server/{serverName}/engine-services")
    public DinoServiceListResponse getServerEngineServices(@PathVariable String                      viewServerName,
                                                           @PathVariable String                      userId,
                                                           @PathVariable String                      serverName,
                                                           @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetEngineServices(viewServerName, userId, requestBody);

    }
    /**
     * Get a list of the access services on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of integration services or exception information
     */

    @PostMapping("/server/{serverName}/access-services")
    public DinoServiceListResponse getServerAccessServices(@PathVariable String                      viewServerName,
                                                           @PathVariable String                      userId,
                                                           @PathVariable String                      serverName,
                                                           @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetAccessServices(viewServerName, userId, requestBody);

    }

    /**
     * Get a list of the view services on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the list of integration services or exception information
     */

    @PostMapping("/server/{serverName}/view-services")
    public DinoServiceListResponse getServerViewServices(@PathVariable String                      viewServerName,
                                                         @PathVariable String                      userId,
                                                         @PathVariable String                      serverName,
                                                         @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetViewServices(viewServerName, userId, requestBody);

    }


    /**
     * Get the details of an integration service running on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the service's details or exception information
     */

    @PostMapping("/server/{serverName}/integration-service-details")
    public DinoServiceDetailsResponse getServerIntegrationServiceDetails(@PathVariable String                      viewServerName,
                                                                         @PathVariable String                      userId,
                                                                         @PathVariable String                      serverName,
                                                                         @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetIntegrationServiceDetails(viewServerName, userId, requestBody);

    }

    /**
     * Get the details of an engine service running on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the service's details or exception information
     */

    @PostMapping("/server/{serverName}/engine-service-details")
    public DinoServiceDetailsResponse getServerEngineServiceDetails(@PathVariable String                      viewServerName,
                                                                    @PathVariable String                      userId,
                                                                    @PathVariable String                      serverName,
                                                                    @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetEngineServiceDetails(viewServerName, userId, requestBody);

    }

    /**
     * Get the details of an access service running on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the service's details or exception information
     */

    @PostMapping("/server/{serverName}/access-service-details")
    public DinoServiceDetailsResponse getServerAccessServiceDetails(@PathVariable String                      viewServerName,
                                                                    @PathVariable String                      userId,
                                                                    @PathVariable String                      serverName,
                                                                    @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetAccessServiceDetails(viewServerName, userId, requestBody);

    }

    /**
     * Get the details of a view service running on the server
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the service's details or exception information
     */

    @PostMapping("/server/{serverName}/view-service-details")
    public DinoServiceDetailsResponse getServerViewServiceDetails(@PathVariable String                      viewServerName,
                                                                  @PathVariable String                      userId,
                                                                  @PathVariable String                      serverName,
                                                                  @RequestBody DinoServiceRequestBody       requestBody  ) {
        return restAPI.serverGetViewServiceDetails(viewServerName, userId, requestBody);

    }


    /**
     * Get the details of an engine running on the server. This wll return a list of the services registered to the engine
     * The request body contains the serverName, platformName and OMES service name, plus the engine name.
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId           user account under which to conduct operation.
     * @param serverName       name of the server
     * @param requestBody      request body containing parameters to formulate repository request
     * @return response object containing the service's details or exception information
     */

    @PostMapping("/server/{serverName}/engine-details")
    public DinoEngineDetailsResponse getServerEngineServiceDetails(@PathVariable String                      viewServerName,
                                                                    @PathVariable String                      userId,
                                                                    @PathVariable String                      serverName,
                                                                    @RequestBody DinoEngineRequestBody requestBody  ) {
        return restAPI.serverGetEngineDetails(viewServerName, userId, requestBody);

    }
}
