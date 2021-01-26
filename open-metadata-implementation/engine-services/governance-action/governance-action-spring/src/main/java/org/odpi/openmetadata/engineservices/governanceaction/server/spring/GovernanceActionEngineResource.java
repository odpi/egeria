/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.engineservices.governanceaction.rest.EngineSummaryListResponse;
import org.odpi.openmetadata.engineservices.governanceaction.server.GovernanceActionRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * GovernanceActionEngineResource provides the server-side catcher for REST calls using Spring that target a specific
 * governance action engine hosted in a engine host server.
 * The engine host server routes these requests to the named governance action engine.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/engine-services/governance-action/users/{userId}")

@Tag(name="Governance Action OMES", description="The Governance Action OMES provide the core subsystem for driving requests for automated governance action services.", externalDocs=@ExternalDocumentation(description="Governance Action Open Metadata Engine Services (OMES)",url="https://egeria.odpi.org/open-metadata-implementation/engine-services/governance-action/"))

public class GovernanceActionEngineResource
{
    private GovernanceActionRESTServices restAPI = new GovernanceActionRESTServices();


    /**
     * Return the names of the the governance engines running in this Governance Action Open Metadata Engine Services (OMES).
     *
     * @param serverName name of the engine host server
     * @param userId identifier of calling user
     *
     * @return list of engine summaries or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceActionEngineException there was a problem detected by the governance action engine.
     */
    @GetMapping(path = "/governance-action-engines")

    public EngineSummaryListResponse getLocalEngines(@PathVariable String serverName,
                                                     @PathVariable String userId)
    {
        return restAPI.getLocalEngines(serverName, userId);
    }
}
