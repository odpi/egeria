/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceConfigRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * EngineConfigurationResource provides the Spring wrapper for the Governance Engine Configuration Services
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")

@Tag(name="Governance Engine OMAS", description="The Governance Engine Open Metadata Access Service (OMAS) provides support for governance engines, services and actions.",
     externalDocs=@ExternalDocumentation(description="Governance Engine Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/governance-engine/overview/"))

public class EngineConfigurationResource
{
    private final GovernanceConfigRESTServices restAPI = new GovernanceConfigRESTServices();


    /**
     * Create a new governance engine definition.
     *
     * @param serverName name of the service to route the request to
     * @param userId identifier of calling user
     * @param typeName type of governance engine
     * @param requestBody containing:
     *                qualifiedName - unique name for the governance engine;
     *                displayName - display name for messages and user interfaces;
     *                description - description of the types of governance services that wil be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-engines/new/{typeName}")

    public GUIDResponse createGovernanceEngine(@PathVariable String                         serverName,
                                               @PathVariable String                         userId,
                                               @PathVariable String                         typeName,
                                               @RequestBody  NewGovernanceEngineRequestBody requestBody)
    {
        return restAPI.createGovernanceEngine(serverName, userId, typeName, requestBody);
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance engine definition.
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-engines/{guid}")

    public GovernanceEngineElementResponse getGovernanceEngineByGUID(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid)
    {
        return restAPI.getGovernanceEngineByGUID(serverName, userId, guid);
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-engines/by-name/{name}")

    public  GovernanceEngineElementResponse getGovernanceEngineByName(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String name)
    {
        return restAPI.getGovernanceEngineByName(serverName, userId, name);
    }


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance engine definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-engines")

    public GovernanceEngineElementsResponse getAllGovernanceEngines(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @RequestParam int    startingFrom,
                                                                    @RequestParam int    maximumResults)
    {
        return restAPI.getAllGovernanceEngines(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param requestBody containing the new properties of the governance engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-engines/{guid}/update")

    public VoidResponse updateGovernanceEngine(@PathVariable String                            serverName,
                                               @PathVariable String                            userId,
                                               @PathVariable String                            guid,
                                               @RequestBody  UpdateGovernanceEngineRequestBody requestBody)
    {
        return restAPI.updateGovernanceEngine(serverName, userId, guid, requestBody);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param requestBody containing the unique name for the governance engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-engines/{guid}/delete")

    public  VoidResponse   deleteGovernanceEngine(@PathVariable String            serverName,
                                                  @PathVariable String            userId,
                                                  @PathVariable String            guid,
                                                  @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteGovernanceEngine(serverName, userId, guid, requestBody);
    }


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param serverName name of the service to route the request to
     * @param userId identifier of calling user
     * @param typeName type of governance service
     * @param requestBody containing:
     *                    qualifiedName - unique name for the governance service;
     *                    displayName -  display name for the governance service;
     *                    description - description of the analysis provided by the governance service;
     *                    connection -  connection to instantiate the governance service implementation.
     *
     * @return unique identifier of the governance service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-services/new/{typeName}")

    public  GUIDResponse createGovernanceService(@PathVariable String                          serverName,
                                                 @PathVariable String                          userId,
                                                 @PathVariable String                          typeName,
                                                 @RequestBody  NewGovernanceServiceRequestBody requestBody)
    {
        return restAPI.createGovernanceService(serverName, userId, typeName, requestBody);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services/{guid}")

    public GovernanceServiceElementResponse getGovernanceServiceByGUID(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid)
    {
        return restAPI.getGovernanceServiceByGUID(serverName, userId, guid);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services/by-name/{name}")

    public  GovernanceServiceElementResponse getGovernanceServiceByName(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String name)
    {
        return restAPI.getGovernanceServiceByName(serverName, userId, name);
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services")

    public GovernanceServiceElementsResponse getAllGovernanceServices(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @RequestParam int    startingFrom,
                                                                      @RequestParam int    maximumResults)
    {
        return restAPI.getAllGovernanceServices(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services/{guid}/registrations")

    public GUIDListResponse getGovernanceServiceRegistrations(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String guid)
    {
        return restAPI.getGovernanceServiceRegistrations(serverName, userId, guid);
    }


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param requestBody containing the new parameters for the governance service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-services/{guid}/update")

    public VoidResponse updateGovernanceService(@PathVariable String                              serverName,
                                                @PathVariable String                              userId,
                                                @PathVariable String                              guid,
                                                @RequestBody  UpdateGovernanceServiceRequestBody  requestBody)
    {
        return restAPI.updateGovernanceService(serverName, userId, guid, requestBody);
    }


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param requestBody containing the unique name for the governance service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-services/{guid}/delete")

    public VoidResponse deleteGovernanceService(@PathVariable String            serverName,
                                                @PathVariable String            userId,
                                                @PathVariable String            guid,
                                                @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteGovernanceService(serverName, userId, guid, requestBody);
    }


    /**
     * Register a governance service with a specific governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance engine.
     * @param requestBody containing:
     *                    guid - unique identifier of the governance service;
     *                    governanceRequestTypes - list of asset governance types that this governance service is able to process.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-engines/{guid}/governance-services")

    public VoidResponse registerGovernanceServiceWithEngine(@PathVariable String                                   serverName,
                                                            @PathVariable String                                   userId,
                                                            @PathVariable String                                   guid,
                                                            @RequestBody  GovernanceServiceRegistrationRequestBody requestBody)
    {
        return restAPI.registerGovernanceServiceWithEngine(serverName, userId, guid, requestBody);
    }


    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param serverName name of the service to route the request to.
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

    public RegisteredGovernanceServiceResponse getRegisteredGovernanceService(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String governanceEngineGUID,
                                                                              @PathVariable String governanceServiceGUID)
    {
        return restAPI.getRegisteredGovernanceService(serverName, userId, governanceEngineGUID, governanceServiceGUID);
    }


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param serverName name of the service to route the request to.
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

    public RegisteredGovernanceServicesResponse getRegisteredGovernanceServices(@PathVariable String serverName,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String governanceEngineGUID,
                                                                                @RequestParam int    startingFrom,
                                                                                @RequestParam int    maximumResults)
    {
        return restAPI.getRegisteredGovernanceServices(serverName, userId, governanceEngineGUID, startingFrom, maximumResults);
    }


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @PostMapping(path = "/governance-engines/{governanceEngineGUID}/governance-services/{governanceServiceGUID}/delete")

    public VoidResponse unregisterGovernanceServiceFromEngine(@PathVariable                  String          serverName,
                                                              @PathVariable                  String          userId,
                                                              @PathVariable                  String          governanceEngineGUID,
                                                              @PathVariable                  String          governanceServiceGUID,
                                                              @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unregisterGovernanceServiceFromEngine(serverName, userId, governanceEngineGUID, governanceServiceGUID, requestBody);
    }
}
