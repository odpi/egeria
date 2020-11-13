/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.rest.DiscoveryEngineStatusResponse;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.server.DiscoveryServerRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * DiscoveryServerResource provides the server-side catcher for REST calls using Spring.
 * The discovery server routes these requests to the names discovery engine.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/discovery-server/users/{userId}")

@Tag(name="Discovery Engine Services", description="The discovery engine services provide the core subsystem for a discovery server. A discovery server is an OMAG Server that hosts automated metadata discovery.", externalDocs=@ExternalDocumentation(description="Discovery Engine Services",url="https://egeria.odpi.org/open-metadata-implementation/governance-servers/discovery-engine-services/"))

public class DiscoveryServerResource
{
    private DiscoveryServerRESTServices restAPI = new DiscoveryServerRESTServices();


    /**
     * Return a summary of each of the discovery engines' status.
     *
     * @param serverName discovery server name
     * @param userId calling user
     * @return list of statuses - on for each assigned discovery engines or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/discovery-engines/status")

    public DiscoveryEngineStatusResponse getDiscoveryEngineStatuses(@PathVariable String   serverName,
                                                                    @PathVariable String   userId)
    {
        return restAPI.getDiscoveryEngineStatuses(serverName, userId);
    }


    /**
     * Request that the discovery engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * discovery server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId identifier of calling user
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "/discovery-engines/{discoveryEngineName}/refresh-config")

    public  VoidResponse refreshConfig(@PathVariable String                       serverName,
                                       @PathVariable String                       discoveryEngineName,
                                       @PathVariable String                       userId)
    {
        return restAPI.refreshConfig(serverName, discoveryEngineName, userId);
    }
}
