/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.IntegrationGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.GovernanceConfigRESTServices;

import org.springframework.web.bind.annotation.*;

/**
 * EngineConfigurationResource provides the Spring wrapper for the Governance Engine Configuration Services
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/{serviceURLMarker}/governance-configuration-service/users/{userId}")

@Tag(name="Metadata Access Services: Governance Configuration Service", description="The Governance Configuration Service provides support for maintaining the metadata elements that control the operation of the governance servers.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/gaf-metadata-management/"))

public class GovernanceConfigurationResource
{
    private final GovernanceConfigRESTServices restAPI = new GovernanceConfigRESTServices();


    /**
     * Return the properties from the named governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-engines/by-name")

    @Operation(summary="getGovernanceEngineByName",
               description="Return the properties from the named governance engine definition.  " +
                                   "The requested name is either the qualified name or display name (if unique).",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public  GovernanceEngineElementResponse getGovernanceEngineByName(@PathVariable String          serverName,
                                                                      @PathVariable String          serviceURLMarker,
                                                                      @PathVariable String          userId,
                                                                      @RequestBody  NameRequestBody name)
    {
        return restAPI.getGovernanceEngineByName(serverName, serviceURLMarker, userId, name);
    }


    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/governance-services/{governanceServiceGUID}")

    @Operation(summary="getRegisteredGovernanceService",
               description="Retrieve a specific governance service registered with a governance engine.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public RegisteredGovernanceServiceResponse getRegisteredGovernanceService(@PathVariable String serverName,
                                                                              @PathVariable String serviceURLMarker,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String governanceEngineGUID,
                                                                              @PathVariable String governanceServiceGUID)
    {
        return restAPI.getRegisteredGovernanceService(serverName, serviceURLMarker, userId, governanceEngineGUID, governanceServiceGUID);
    }


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/governance-services")

    @Operation(summary="getRegisteredGovernanceServices",
               description="Retrieve the identifiers of the governance services registered with a governance engine.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public RegisteredGovernanceServicesResponse getRegisteredGovernanceServices(@PathVariable String serverName,
                                                                                @PathVariable String serviceURLMarker,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String governanceEngineGUID,
                                                                                @RequestParam int    startingFrom,
                                                                                @RequestParam int    maximumResults)
    {
        return restAPI.getRegisteredGovernanceServices(serverName, serviceURLMarker, userId, governanceEngineGUID, startingFrom, maximumResults);
    }


    /*
     * Integration connectors
     */


    /**
     * Return the properties from an integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups/by-name/{name}")

    @Operation(summary="getIntegrationGroupByName",
            description="Return the properties from an integration group definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public IntegrationGroupElementResponse getIntegrationGroupByName(@PathVariable String serverName,
                                                                     @PathVariable String serviceURLMarker,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String name)
    {
        return restAPI.getIntegrationGroupByName(serverName, serviceURLMarker, userId, name);
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-connectors/{guid}/registrations")

    @Operation(summary="getIntegrationConnectorRegistrations",
            description="Return the list of integration groups that a specific integration connector is registered with.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public GUIDListResponse getIntegrationConnectorRegistrations(@PathVariable String serverName,
                                                                 @PathVariable String serviceURLMarker,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String guid)
    {
        return restAPI.getIntegrationConnectorRegistrations(serverName, serviceURLMarker, userId, guid);
    }


    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @return details of the integration connector and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups/{integrationGroupGUID}/integration-connectors/{integrationConnectorGUID}")

    @Operation(summary="getRegisteredIntegrationConnector",
            description="Retrieve a specific integration connector registered with an integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public RegisteredIntegrationConnectorResponse getRegisteredIntegrationConnector(@PathVariable String serverName,
                                                                                    @PathVariable String serviceURLMarker,
                                                                                    @PathVariable String userId,
                                                                                    @PathVariable String integrationGroupGUID,
                                                                                    @PathVariable String integrationConnectorGUID)
    {
        return restAPI.getRegisteredIntegrationConnector(serverName, serviceURLMarker, userId, integrationGroupGUID, integrationConnectorGUID);
    }


    /**
     * Retrieve the details of the integration connectors registered with an integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups/{integrationGroupGUID}/integration-connectors")

    @Operation(summary="getRegisteredIntegrationConnectors",
            description="Retrieve the details of the integration connectors registered with an integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public RegisteredIntegrationConnectorsResponse getRegisteredIntegrationConnectors(@PathVariable String serverName,
                                                                                      @PathVariable String serviceURLMarker,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String integrationGroupGUID,
                                                                                      @RequestParam int    startingFrom,
                                                                                      @RequestParam int    maximumResults)
    {
        return restAPI.getRegisteredIntegrationConnectors(serverName, serviceURLMarker, userId, integrationGroupGUID, startingFrom, maximumResults);
    }
}
