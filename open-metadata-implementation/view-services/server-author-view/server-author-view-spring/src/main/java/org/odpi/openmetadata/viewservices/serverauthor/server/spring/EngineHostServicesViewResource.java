/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.server.spring;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * EngineHostServicesViewResource provides the configuration for setting up an Engine Host.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}/servers/{serverToBeConfiguredName}")

@Tag(name="View Server: Server Author OMVS", description="The Server Author OMVS is for user interfaces supporting the creating and editing of OMAG Server Configuration Documents.",
     externalDocs=@ExternalDocumentation(description="Further information",
                                         url="https://egeria-project.org/services/omvs/server-author/overview"))

public class EngineHostServicesViewResource
{
    private final ServerAuthorViewRESTServices adminAPI = new ServerAuthorViewRESTServices();




    /**
     * Enable a single engine service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param requestBody  Engine host service request body
     * @param serviceURLMarker string indicating which engine service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/engine-services/{serviceURLMarker}")
    public ServerAuthorConfigurationResponse configureEngineService(@PathVariable String            userId,
                                                                    @PathVariable                   String              serverName,
                                                                    @PathVariable                   String serverToBeConfiguredName,
                                                                    @PathVariable                   String              serviceURLMarker,
                                                                    @RequestBody                    EngineServiceRequestBody requestBody)



    {
        return adminAPI.configureEngineService(userId, serverName, serverToBeConfiguredName, serviceURLMarker, requestBody);
    }


    /**
     * Disable a single engine service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param serviceURLMarker string indicating which engine service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/engine-services/{serviceURLMarker}")
    public ServerAuthorConfigurationResponse disableEngineService(  @PathVariable                   String              userId,
                                                                    @PathVariable                   String              serverName,
                                                                    @PathVariable                   String serverToBeConfiguredName,
                                                                    @PathVariable                   String              serviceURLMarker)
    {
        return adminAPI.disableEngineService(userId, serverName, serverToBeConfiguredName, serviceURLMarker);
    }
}
