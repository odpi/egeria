/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.repositorygovernance.server.RepositoryGovernanceRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * RepositoryGovernanceResource provides the server-side catcher for REST calls using Spring that validate RepositoryGovernance Service implementations
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/engine-services/repository-governance/users/{userId}")

@Tag(name="Repository Governance OMES", description="The Repository Governance OMES provide the core subsystem for driving requests to govern open metadata repositories.",
     externalDocs=@ExternalDocumentation(description="Repository Governance Open Metadata Engine Services (OMES)",
                                         url="https://egeria-project.org/services/omes/repository-governance/overview/"))

public class RepositoryGovernanceResource
{
    private RepositoryGovernanceRESTServices restAPI = new RepositoryGovernanceRESTServices();


    /**
     * Validate the connector and return its connector type.  The engine service does not need to
     * be running in the integration daemon in order for this call to be successful.  It only needs to be registered with the
     * integration daemon.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type or
     *
     *  InvalidParameterException the connector provider class name is not a valid connector fo this service
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException there was a problem detected by the integration service
     */
    @GetMapping(path = "/validate-connector/{connectorProviderClassName}")

    public ConnectorReportResponse validateConnector(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String connectorProviderClassName)
    {
        return restAPI.validateConnector(serverName, userId, connectorProviderClassName);
    }
}
