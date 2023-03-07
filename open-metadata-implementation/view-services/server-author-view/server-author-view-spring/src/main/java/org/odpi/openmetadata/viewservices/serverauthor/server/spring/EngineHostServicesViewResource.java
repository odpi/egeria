/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.server.spring;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigengineServicesResource provides the configuration for setting up the Open Metadata View
 * Services (OMVSs).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}/servers/{serverToBeConfiguredName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class EngineHostServicesViewResource
{
    private ServerAuthorViewRESTServices adminAPI = new ServerAuthorViewRESTServices();




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
