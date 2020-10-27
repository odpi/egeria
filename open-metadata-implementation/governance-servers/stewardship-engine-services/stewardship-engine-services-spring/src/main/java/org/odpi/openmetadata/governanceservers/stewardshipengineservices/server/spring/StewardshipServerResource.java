/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.rest.StewardshipEngineStatusResponse;
import org.odpi.openmetadata.governanceservers.stewardshipservices.server.StewardshipServerRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * StewardshipServerResource provides the server-side catcher for REST calls using Spring.
 * The stewardship server routes these requests to the names stewardship engine.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/stewardship-server/users/{userId}")

@Tag(name="Stewardship Engine Services", description="The stewardship engine services provide the core subsystem for a stewardship server. A stewardship server is an OMAG Server that hosts automated metadata stewardship.", externalDocs=@ExternalDocumentation(description="Stewardship Engine Services",url="https://egeria.odpi.org/open-metadata-implementation/governance-servers/stewardship-engine-services/"))

public class StewardshipServerResource
{
    private StewardshipServerRESTServices restAPI = new StewardshipServerRESTServices();


    /**
     * Return a summary of each of the stewardship engines' status.
     *
     * @param serverName stewardship server name
     * @param userId calling user
     * @return list of statuses - on for each assigned stewardship engines or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/stewardship-engines/status")

    public StewardshipEngineStatusResponse getStewardshipEngineStatuses(@PathVariable String   serverName,
                                                                        @PathVariable String   userId)
    {
        return restAPI.getStewardshipEngineStatuses(serverName, userId);
    }


    /**
     * Request that the stewardship engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * stewardship server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the stewardship server.
     * @param stewardshipEngineName unique name of the stewardship engine.
     * @param userId identifier of calling user
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  StewardshipEngineException there was a problem detected by the stewardship engine.
     */
    @GetMapping(path = "/stewardship-engines/{stewardshipEngineName}/refresh-config")

    public  VoidResponse refreshConfig(@PathVariable String                       serverName,
                                       @PathVariable String                       stewardshipEngineName,
                                       @PathVariable String                       userId)
    {
        return restAPI.refreshConfig(serverName, stewardshipEngineName, userId);
    }
}
