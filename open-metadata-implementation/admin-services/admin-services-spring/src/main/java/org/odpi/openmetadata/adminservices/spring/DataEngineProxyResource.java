/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerDataEngineProxyService;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * DataEngineProxyResource provides the API for configuring a data engine proxy in an OMAG
 * server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/data-engine-proxy-service")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-an-omag-server.html"))

public class DataEngineProxyResource {

    private OMAGServerDataEngineProxyService adminAPI = new OMAGServerDataEngineProxyService();

    /**
     * Store the provided Data Engine Proxy configuration
     *
     * @param userId                user that is issuing the request
     * @param serverName            local server name
     * @param dataEngineProxyConfig configuration for the data engine proxy
     * @return void response or
     * OMAGNotAuthorizedException    the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/configuration")
    public VoidResponse setDataEngineProxyConfig(@PathVariable String                userId,
                                                 @PathVariable String                serverName,
                                                 @RequestBody  DataEngineProxyConfig dataEngineProxyConfig)
    {
        return adminAPI.setDataEngineProxyConfig(userId, serverName, dataEngineProxyConfig);
    }

    /**
     * Remove this Data Engine Proxy from the server configuration.
     *
     * @param userId     user that is issuing the request
     * @param serverName local server name
     * @return void response
     */
    @DeleteMapping(path = "")
    public VoidResponse deleteDataEngineProxy(@PathVariable String userId, 
                                              @PathVariable String serverName)
    {
        return adminAPI.deleteDataEngineProxy(userId, serverName);
    }

}
