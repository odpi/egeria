/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.GovernanceConfigRESTServices;

import org.springframework.web.bind.annotation.*;

/**
 * EngineConfigurationResource provides the Spring wrapper for the Governance Engine Configuration Services
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/governance-engine-configuration-service/users/{userId}")

@Tag(name="Framework Services: Governance Engine Configuration", description="The Governance Engine Configuration Service provides support for creating governance engine definitions in open metadata.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/concepts/governance-engine-definition/"))

public class GovernanceEngineConfigurationResource
{
    private final GovernanceConfigRESTServices restAPI = new GovernanceConfigRESTServices();


    /**
     * Create a new governance engine definition.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @PostMapping(path = "/governance-engines/{typeName}")

    @Operation(summary="createGovernanceEngine",
               description="Create a new governance engine definition.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public GUIDResponse createGovernanceEngine(@PathVariable String                         serverName,
                                               @PathVariable String                         serviceURLMarker,
                                               @PathVariable String                         userId,
                                               @PathVariable String                         typeName,
                                               @RequestBody  NewGovernanceEngineRequestBody requestBody)
    {
        return restAPI.createGovernanceEngine(serverName, serviceURLMarker, userId, typeName, requestBody);
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance engine definition.
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-engines/{guid}")

    @Operation(summary="getGovernanceEngineByGUID",
               description="Return the properties from a governance engine definition.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public GovernanceEngineElementResponse getGovernanceEngineByGUID(@PathVariable String serverName,
                                                                     @PathVariable String serviceURLMarker,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid)
    {
        return restAPI.getGovernanceEngineByGUID(serverName, serviceURLMarker, userId, guid);
    }


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
     * Return the list of governance engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="getAllGovernanceEngines",
               description="Return the list of governance engine definitions that are stored.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public GovernanceEngineElementsResponse getAllGovernanceEngines(@PathVariable String serverName,
                                                                    @PathVariable String serviceURLMarker,
                                                                    @PathVariable String userId,
                                                                    @RequestParam int    startingFrom,
                                                                    @RequestParam int    maximumResults)
    {
        return restAPI.getAllGovernanceEngines(serverName, serviceURLMarker, userId, startingFrom, maximumResults);
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="updateGovernanceEngine",
               description="Update the properties of an existing governance engine definition.  Use the current value to" +
                                   " keep a property value the same, or use the new value.  Null means remove the property from" +
                                   " the definition.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public VoidResponse updateGovernanceEngine(@PathVariable String                            serverName,
                                               @PathVariable String                            serviceURLMarker,
                                               @PathVariable String                            userId,
                                               @PathVariable String                            guid,
                                               @RequestBody  UpdateGovernanceEngineRequestBody requestBody)
    {
        return restAPI.updateGovernanceEngine(serverName, serviceURLMarker, userId, guid, requestBody);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="deleteGovernanceEngine",
               description="Remove the properties of the governance engine.  Both the guid and the qualified name is supplied" +
                                   " to validate that the correct governance engine is being deleted.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public  VoidResponse   deleteGovernanceEngine(@PathVariable String            serverName,
                                                  @PathVariable String            serviceURLMarker,
                                                  @PathVariable String            userId,
                                                  @PathVariable String            guid,
                                                  @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteGovernanceEngine(serverName, serviceURLMarker, userId, guid, requestBody);
    }


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @PostMapping(path = "/governance-services/types/{typeName}")

    @Operation(summary="createGovernanceService",
               description="Create a governance service definition.  The same governance service can be associated with multiple" +
                                   " governance engines.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-service/"))

    public  GUIDResponse createGovernanceService(@PathVariable String                          serverName,
                                                 @PathVariable String                          serviceURLMarker,
                                                 @PathVariable String                          userId,
                                                 @PathVariable String                          typeName,
                                                 @RequestBody  NewGovernanceServiceRequestBody requestBody)
    {
        return restAPI.createGovernanceService(serverName, serviceURLMarker, userId, typeName, requestBody);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services/{guid}")

    @Operation(summary="getGovernanceServiceByGUID",
               description="Return the properties from a governance service definition.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-service/"))

    public GovernanceServiceElementResponse getGovernanceServiceByGUID(@PathVariable String serverName,
                                                                       @PathVariable String serviceURLMarker,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid)
    {
        return restAPI.getGovernanceServiceByGUID(serverName, serviceURLMarker, userId, guid);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param requestBody qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services/by-name")

    @Operation(summary="getGovernanceServiceByName",
               description="Return the properties from a named governance service definition. " +
                                   "The requested name is either the qualified name or display name (if unique).",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-service/"))

    public  GovernanceServiceElementResponse getGovernanceServiceByName(@PathVariable String          serverName,
                                                                        @PathVariable String          serviceURLMarker,
                                                                        @PathVariable String          userId,
                                                                        @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceServiceByName(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="getAllGovernanceServices",
               description="Return the list of governance services definitions that are stored.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-service/"))

    public GovernanceServiceElementsResponse getAllGovernanceServices(@PathVariable String serverName,
                                                                      @PathVariable String serviceURLMarker,
                                                                      @PathVariable String userId,
                                                                      @RequestParam int    startingFrom,
                                                                      @RequestParam int    maximumResults)
    {
        return restAPI.getAllGovernanceServices(serverName, serviceURLMarker, userId, startingFrom, maximumResults);
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @GetMapping(path = "/governance-services/{guid}/registrations")

    @Operation(summary="getGovernanceServiceRegistrations",
               description="Return the list of governance engines that a specific governance service is registered with.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public GUIDListResponse getGovernanceServiceRegistrations(@PathVariable String serverName,
                                                              @PathVariable String serviceURLMarker,
                                                              @PathVariable String userId,
                                                              @PathVariable String guid)
    {
        return restAPI.getGovernanceServiceRegistrations(serverName, serviceURLMarker, userId, guid);
    }


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="updateGovernanceService",
               description="Update the properties of an existing governance service definition.  Use the current value to" +
                                   " keep a property value the same, or use the new value.  Null means remove the property from" +
                                   " the definition.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-service/"))

    public VoidResponse updateGovernanceService(@PathVariable String                              serverName,
                                                @PathVariable String                              serviceURLMarker,
                                                @PathVariable String                              userId,
                                                @PathVariable String                              guid,
                                                @RequestBody  UpdateGovernanceServiceRequestBody  requestBody)
    {
        return restAPI.updateGovernanceService(serverName, serviceURLMarker, userId, guid, requestBody);
    }


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="deleteGovernanceService",
               description="Remove the properties of the governance service.  Both the guid and the qualified name is supplied" +
                                   " to validate that the correct governance service is being deleted.  The governance service is also" +
                                   " unregistered from its governance engines.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-service/"))

    public VoidResponse deleteGovernanceService(@PathVariable String            serverName,
                                                @PathVariable String            serviceURLMarker,
                                                @PathVariable String            userId,
                                                @PathVariable String            guid,
                                                @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteGovernanceService(serverName, serviceURLMarker, userId, guid, requestBody);
    }


    /**
     * Register a governance service with a specific governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="registerGovernanceServiceWithEngine",
               description="Register a governance service with a specific governance engine.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public VoidResponse registerGovernanceServiceWithEngine(@PathVariable String                                   serverName,
                                                            @PathVariable String                                   serviceURLMarker,
                                                            @PathVariable String                                   userId,
                                                            @PathVariable String                                   guid,
                                                            @RequestBody  GovernanceServiceRegistrationRequestBody requestBody)
    {
        return restAPI.registerGovernanceServiceWithEngine(serverName, serviceURLMarker, userId, guid, requestBody);
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


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="unregisterGovernanceServiceFromEngine",
               description="Unregister a governance service from the governance engine.  The governance engine and governance service are not " +
                                   "removed, only the relationship between them.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-engine/"))

    public VoidResponse unregisterGovernanceServiceFromEngine(@PathVariable                  String          serverName,
                                                              @PathVariable                  String          serviceURLMarker,
                                                              @PathVariable                  String          userId,
                                                              @PathVariable                  String          governanceEngineGUID,
                                                              @PathVariable                  String          governanceServiceGUID,
                                                              @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unregisterGovernanceServiceFromEngine(serverName, serviceURLMarker, userId, governanceEngineGUID, governanceServiceGUID, requestBody);
    }
}
