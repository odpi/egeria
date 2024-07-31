/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * GovernanceEngineOMASResource supports the REST APIs for running Governance Action Service
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")

@Tag(name="Metadata Access Server: Governance Engine OMAS",
     description="The Governance Engine Open Metadata Access Service (OMAS) provides support for governance engines, services and actions.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-engine/overview/"))

public class GovernanceEngineOMASResource
{
    private final GovernanceEngineRESTServices restAPI = new GovernanceEngineRESTServices();


    /**
     * Return the connection object for the Governance Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the topic connection.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    @Operation(summary="getOutTopicConnection",
               description="Return the connection object for the Governance Engine OMAS's out topic.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/out-topic/"))

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Log an audit message about an asset.
     *
     * @param serverName            name of server instance to route request to
     * @param userId                userId of user making request.
     * @param assetGUID             unique identifier for asset.
     * @param governanceService     unique name for governance service.
     * @param message               message to log
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records/{governanceService}")

    @Operation(summary="logAssetAuditMessage",
               description="Log an audit message about an asset.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log/"))

    public VoidResponse logAssetAuditMessage(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String assetGUID,
                                             @PathVariable String governanceService,
                                             @RequestBody  String message)
    {
        return restAPI.logAssetAuditMessage(serverName, userId, assetGUID, governanceService, message);
    }
}
