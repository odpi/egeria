/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.dino.server;


//import org.odpi.openmetadata.viewservices.tex.api.rest.TexTypesRequestBody;
//import org.odpi.openmetadata.viewservices.tex.api.rest.TypeExplorerResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoPlatformOverviewResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoPlatformRequestBody;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoPlatformServiceListRequestBody;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoResourceEndpointListResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerAuditLogResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerConfigResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerDoubleConfigResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerListResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerOverviewResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerRequestBody;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerTypeResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServiceListResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoStringResponse;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * The DinoViewRESTResource provides the Spring API endpoints of the Dino Open Metadata View Service (OMVS).
 * This interface provides an interfaces for Egeria operators.
 */

@RestController
@RequestMapping("/servers/{viewServerName}/open-metadata/view-services/dino/users/{userId}")

@Tag(name="Dino OMVS", description="Explore topology information in an Egeria deployment of platforms, servers, services and cohorts for graph visualization.", externalDocs=@ExternalDocumentation(description="Dino View Service (OMVS)",url="https://egeria.odpi.org/open-metadata-implementation/view-services/dino-view/"))

public class DinoViewRESTResource {

    private DinoViewRESTServices restAPI = new DinoViewRESTServices();


    /**
     * Default constructor
     */
    public DinoViewRESTResource() {
    }

    /* TODO - javadoc update
     * Get the configured resource endpoints
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of resource endpoints or exception information
     */

    @GetMapping("/resource-endpoints")
    public DinoResourceEndpointListResponse getResourceEndpoints(@PathVariable String        viewServerName,
                                                                 @PathVariable String        userId) {
        return restAPI.getResourceEndpointList(viewServerName, userId);

    }

    /* TODO - javadoc update
     * Get the platform overview
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the overview of the platform or exception information
     */

    @PostMapping("/platform/{platformName}")
    public DinoPlatformOverviewResponse getPlatformOverview(@PathVariable String                           viewServerName,
                                                            @PathVariable String                           userId,
                                                            @PathVariable String                           platformName,
                                                            @RequestBody DinoPlatformRequestBody           requestBody  ) {
        return restAPI.platformGetOverview(viewServerName, userId, requestBody);

    }



    /* TODO - javadoc update
     * Get the platform origin
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the platform's origin string or exception information
     */

    @PostMapping("/platform/{platformName}/origin")
    public DinoStringResponse getPlatformOrigin(@PathVariable String                           viewServerName,
                                                @PathVariable String                           userId,
                                                @PathVariable String                           platformName,
                                                @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetOrigin(viewServerName, userId, requestBody);

    }


    /* TODO - javadoc update
     * Get the active servers on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of names of active servers or exception information
     */

    @PostMapping("/platform/{platformName}/servers/active")
    public DinoServerListResponse getActiveServers(@PathVariable String                           viewServerName,
                                                   @PathVariable String                           userId,
                                                   @PathVariable String                           platformName,
                                                   @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetActiveServers(viewServerName, userId, requestBody);

    }



    /* TODO - javadoc update
     * Get the known servers on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of names of all known servers or exception information
     */

    @PostMapping("/platform/{platformName}/servers")
    public DinoServerListResponse getKnownServers(@PathVariable String                           viewServerName,
                                                   @PathVariable String                           userId,
                                                   @PathVariable String                           platformName,
                                                   @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetKnownServers(viewServerName, userId, requestBody);

    }

    /* TODO - javadoc update
     * Get the access services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/access-services")
    public DinoServiceListResponse getAccessServices(@PathVariable String                           viewServerName,
                                                     @PathVariable String                           userId,
                                                     @PathVariable String                           platformName,
                                                     @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetAccessServices(viewServerName, userId, requestBody);

    }

    /* TODO - javadoc update
     * Get the view services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/view-services")
    public DinoServiceListResponse getViewServices(@PathVariable String                           viewServerName,
                                                   @PathVariable String                           userId,
                                                   @PathVariable String                           platformName,
                                                   @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetViewServices(viewServerName, userId, requestBody);

    }
    /* TODO - javadoc update
     * Get the governance services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/governance-services")
    public DinoServiceListResponse getGovernanceServices(@PathVariable String                           viewServerName,
                                                         @PathVariable String                           userId,
                                                         @PathVariable String                           platformName,
                                                         @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetGovernanceServices(viewServerName, userId, requestBody);

    }
    /* TODO - javadoc update
     * Get the common services on a platform
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the list of service objects or exception information
     */

    @PostMapping("/platform/{platformName}/registered-services/common-services")
    public DinoServiceListResponse getCommonServices(@PathVariable String                           viewServerName,
                                                     @PathVariable String                           userId,
                                                     @PathVariable String                           platformName,
                                                     @RequestBody DinoPlatformRequestBody requestBody  ) {
        return restAPI.platformGetCommonServices(viewServerName, userId, requestBody);

    }


    /* TODO - javadoc update
     * Get the server overview
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the overview of the server or exception information
     */

    @PostMapping("/server/{serverName}")
    public DinoServerOverviewResponse getServerOverview(@PathVariable String                           viewServerName,
                                                        @PathVariable String                           userId,
                                                        @PathVariable String                           serverName,
                                                        @RequestBody DinoServerRequestBody           requestBody  ) {
        return restAPI.serverGetOverview(viewServerName, userId, requestBody);

    }


    /* TODO - javadoc update
     * Get the server origin
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */

    @PostMapping("/server/{serverName}/origin")
    public DinoStringResponse getServerOrigin(@PathVariable String                           viewServerName,
                                                @PathVariable String                           userId,
                                                @PathVariable String                           serverName,
                                                @RequestBody DinoServerRequestBody requestBody  ) {
        return restAPI.serverGetOrigin(viewServerName, userId, requestBody);

    }

    /* TODO - javadoc update
     * Get the server type classification
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the server type information (as a String) or exception information
     */

    @PostMapping("/server/{serverName}/server-type-classification")
    public DinoServerTypeResponse getServerTypeClassification(@PathVariable String                           viewServerName,
                                                              @PathVariable String                           userId,
                                                              @PathVariable String                           serverName,
                                                              @RequestBody DinoServerRequestBody requestBody  ) {
        return restAPI.serverGetTypeClassification(viewServerName, userId, requestBody);

    }

    /* TODO - javadoc update
     * Get the server's stored configuration
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */

    @PostMapping("/server/{serverName}/configuration")
    public DinoServerConfigResponse getServerStoredConfiguration(@PathVariable String                           viewServerName,
                                                           @PathVariable String                           userId,
                                                           @PathVariable String                           serverName,
                                                           @RequestBody DinoServerRequestBody requestBody  ) {
        return restAPI.serverGetStoredConfiguration(viewServerName, userId, requestBody);

    }


    /* TODO - javadoc update
     * Get the server's active (running instance) configuration
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */

    @PostMapping("/server/{serverName}/instance/configuration")
    public DinoServerConfigResponse getServerInstanceConfiguration(@PathVariable String                           viewServerName,
                                                           @PathVariable String                           userId,
                                                           @PathVariable String                           serverName,
                                                           @RequestBody DinoServerRequestBody requestBody  ) {
        return restAPI.serverGetInstanceConfiguration(viewServerName, userId, requestBody);

    }

    /* TODO - javadoc update
     * Get the server's stored and active (running instance) configurations in a duplexed response
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */

    @PostMapping("/server/{serverName}/stored-and-active-configuration")
    public DinoServerDoubleConfigResponse getServerStoredAndActiveConfiguration(@PathVariable String                           viewServerName,
                                                                                @PathVariable String                           userId,
                                                                                @PathVariable String                           serverName,
                                                                                @RequestBody DinoServerRequestBody requestBody  ) {
        return restAPI.serverGetStoredAndActiveConfiguration(viewServerName, userId, requestBody);

    }

    /* TODO - javadoc update
     * Get the server's audit log
     *
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */

    @PostMapping("/server/{serverName}/audit-log")
    public DinoServerAuditLogResponse getServerAuditLog(@PathVariable String                           viewServerName,
                                                        @PathVariable String                           userId,
                                                        @PathVariable String                           serverName,
                                                        @RequestBody DinoServerRequestBody requestBody  ) {
        return restAPI.serverGetAuditLog(viewServerName, userId, requestBody);

    }
}
