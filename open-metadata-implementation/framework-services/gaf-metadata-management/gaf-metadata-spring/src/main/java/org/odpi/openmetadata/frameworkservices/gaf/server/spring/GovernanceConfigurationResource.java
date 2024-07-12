/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationGroupProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.GovernanceConfigRESTServices;

import org.springframework.web.bind.annotation.*;

/**
 * EngineConfigurationResource provides the Spring wrapper for the Governance Engine Configuration Services
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/governance-configuration-service/users/{userId}")

@Tag(name="Framework Services: Governance Configuration Service", description="The Governance Configuration Service provides support for maintaining the metadata elements that control the operation of the governance servers.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/gaf-metadata-management/"))

public class GovernanceConfigurationResource
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


    /*
     * Integration connectors
     */


    /**
     * Create a new integration group definition.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param requestBody containing properties of integration group.
     *
     * @return unique identifier (guid) of the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-groups/new")

    @Operation(summary="createIntegrationGroup",
            description="Create a new integration group definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public GUIDResponse createIntegrationGroup(@PathVariable String                     serverName,
                                               @PathVariable String                     serviceURLMarker,
                                               @PathVariable String                     userId,
                                               @RequestBody IntegrationGroupProperties requestBody)
    {
        return restAPI.createIntegrationGroup(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration group definition.
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups/{guid}")

    @Operation(summary="getIntegrationGroupByGUID",
            description="Return the properties from an integration group definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public IntegrationGroupElementResponse getIntegrationGroupByGUID(@PathVariable String serverName,
                                                                     @PathVariable String serviceURLMarker,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid)
    {
        return restAPI.getIntegrationGroupByGUID(serverName, serviceURLMarker, userId, guid);
    }


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
     * Return the list of integration group definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration group definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups")

    @Operation(summary="getAllIntegrationGroups",
            description="Return the list of integration group definitions that are stored.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public IntegrationGroupElementsResponse getAllIntegrationGroups(@PathVariable String serverName,
                                                                    @PathVariable String serviceURLMarker,
                                                                    @PathVariable String userId,
                                                                    @RequestParam int    startingFrom,
                                                                    @RequestParam int    maximumResults)
    {
        return restAPI.getAllIntegrationGroups(serverName, serviceURLMarker, userId, startingFrom, maximumResults);
    }


    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param requestBody containing the new properties of the integration group.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-groups/{guid}/update")

    @Operation(summary="updateIntegrationGroup",
            description="Update the properties of an existing integration group definition.  Use the current value to" +
                    " keep a property value the same, or use the new value.  Null means remove the property from" +
                    " the definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse updateIntegrationGroup(@PathVariable String                     serverName,
                                               @PathVariable String                     serviceURLMarker,
                                               @PathVariable String                     userId,
                                               @PathVariable String                     guid,
                                               @RequestParam boolean                    isMergeUpdate,
                                               @RequestBody  IntegrationGroupProperties requestBody)
    {
        return restAPI.updateIntegrationGroup(serverName, serviceURLMarker, userId, guid, isMergeUpdate, requestBody);
    }


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param requestBody containing the unique name for the integration group.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-groups/{guid}/delete")

    @Operation(summary="deleteIntegrationGroup",
            description="Remove the properties of the integration group.  Both the guid and the qualified name is supplied" +
                    " to validate that the correct integration group is being deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public  VoidResponse   deleteIntegrationGroup(@PathVariable String            serverName,
                                                  @PathVariable String            serviceURLMarker,
                                                  @PathVariable String            userId,
                                                  @PathVariable String            guid,
                                                  @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteIntegrationGroup(serverName, serviceURLMarker, userId, guid, requestBody);
    }


    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param requestBody containing:
     *                    qualifiedName - unique name for the integration connector;
     *                    displayName -  display name for the integration connector;
     *                    description - description of the analysis provided by the integration connector;
     *                    connection -  connection to instantiate the integration connector implementation.
     *
     * @return unique identifier of the integration connector or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-connectors/new")

    @Operation(summary="createIntegrationConnector",
            description="Create an integration connector definition.  The same integration connector can be associated with multiple" +
                    " integration groups.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  GUIDResponse createIntegrationConnector(@PathVariable String                         serverName,
                                                    @PathVariable String                         serviceURLMarker,
                                                    @PathVariable String                         userId,
                                                    @RequestBody IntegrationConnectorProperties requestBody)
    {
        return restAPI.createIntegrationConnector(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-connectors/{guid}")

    @Operation(summary="getIntegrationConnectorByGUID",
            description="Return the properties from an integration connector definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public IntegrationConnectorElementResponse getIntegrationConnectorByGUID(@PathVariable String serverName,
                                                                             @PathVariable String serviceURLMarker,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String guid)
    {
        return restAPI.getIntegrationConnectorByGUID(serverName, serviceURLMarker, userId, guid);
    }


    /**
     * Return the properties from an integration connector definition.
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
    @GetMapping(path = "/integration-connectors/by-name/{name}")

    @Operation(summary="getIntegrationConnectorByName",
            description="Return the properties from an integration connector definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  IntegrationConnectorElementResponse getIntegrationConnectorByName(@PathVariable String serverName,
                                                                              @PathVariable String serviceURLMarker,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String name)
    {
        return restAPI.getIntegrationConnectorByName(serverName, serviceURLMarker, userId, name);
    }


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration connector definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-connectors")

    @Operation(summary="getAllIntegrationConnectors",
            description="Return the list of integration connectors definitions that are stored.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public IntegrationConnectorElementsResponse getAllIntegrationConnectors(@PathVariable String serverName,
                                                                            @PathVariable String serviceURLMarker,
                                                                            @PathVariable String userId,
                                                                            @RequestParam int    startingFrom,
                                                                            @RequestParam int    maximumResults)
    {
        return restAPI.getAllIntegrationConnectors(serverName, serviceURLMarker, userId, startingFrom, maximumResults);
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
     * Update the properties of an existing integration connector definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param requestBody containing the new parameters for the integration connector.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-connectors/{guid}/update")

    @Operation(summary="updateIntegrationConnector",
            description="Update the properties of an existing integration connector definition. The isMergeUpdate request parameter is " +
                    "used to indicate whether supplied null values mean keep existing value (true) or remove existing value (false).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse updateIntegrationConnector(@PathVariable String                         serverName,
                                                   @PathVariable String                         serviceURLMarker,
                                                   @PathVariable String                         userId,
                                                   @PathVariable String                         guid,
                                                   @RequestParam boolean                        isMergeUpdate,
                                                   @RequestBody  IntegrationConnectorProperties requestBody)
    {
        return restAPI.updateIntegrationConnector(serverName, serviceURLMarker, userId, guid, isMergeUpdate, requestBody);
    }


    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param requestBody containing the unique name for the integration connector.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-connectors/{guid}/delete")

    @Operation(summary="deleteIntegrationConnector",
            description="Remove the properties of the integration connector.  Both the guid and the qualified name is supplied" +
                    " to validate that the correct integration connector is being deleted.  The integration connector is also" +
                    " unregistered from its integration groups.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse deleteIntegrationConnector(@PathVariable String            serverName,
                                                   @PathVariable String            serviceURLMarker,
                                                   @PathVariable String            userId,
                                                   @PathVariable String            guid,
                                                   @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteIntegrationConnector(serverName, serviceURLMarker, userId, guid, requestBody);
    }


    /**
     * Register an integration connector with a specific integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param requestBody containing:
     *                    integrationGroupGUID - unique identifier of the integration connector;
     *                    governanceRequestTypes - list of asset governance types that this integration connector is able to process.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-groups/{integrationGroupGUID}/integration-connectors/{integrationConnectorGUID}")

    @Operation(summary="registerIntegrationConnectorWithGroup",
            description="Register an integration connector with a specific integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse registerIntegrationConnectorWithGroup(@PathVariable String                                   serverName,
                                                              @PathVariable String                                   serviceURLMarker,
                                                              @PathVariable String                                   userId,
                                                              @PathVariable String                                   integrationGroupGUID,
                                                              @PathVariable String                                   integrationConnectorGUID,
                                                              @RequestBody RegisteredIntegrationConnectorProperties requestBody)
    {
        return restAPI.registerIntegrationConnectorWithGroup(serverName, serviceURLMarker, userId, integrationGroupGUID, integrationConnectorGUID, requestBody);
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


    /**
     * Unregister an integration connector from the integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-groups/{integrationGroupGUID}/integration-connectors/{integrationConnectorGUID}/delete")

    @Operation(summary="unregisterIntegrationConnectorFromGroup",
            description="Unregister an integration connector from the integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse unregisterIntegrationConnectorFromGroup(@PathVariable                  String          serverName,
                                                                @PathVariable                  String          serviceURLMarker,
                                                                @PathVariable                  String          userId,
                                                                @PathVariable                  String          integrationGroupGUID,
                                                                @PathVariable                  String          integrationConnectorGUID,
                                                                @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unregisterIntegrationConnectorFromGroup(serverName, serviceURLMarker, userId, integrationGroupGUID, integrationConnectorGUID, requestBody);
    }


    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return guid or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets/{metadataElementGUID}")

    @Operation(summary="addCatalogTarget",
            description="Add a catalog target to an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public GUIDResponse addCatalogTarget(@PathVariable String                  serverName,
                                         @PathVariable String                  serviceURLMarker,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  integrationConnectorGUID,
                                         @PathVariable String                  metadataElementGUID,
                                         @RequestBody CatalogTargetProperties requestBody)
    {
        return restAPI.addCatalogTarget(serverName, serviceURLMarker, userId, integrationConnectorGUID, metadataElementGUID, requestBody);
    }



    /**
     * Update a catalog target for an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}/update")

    @Operation(summary="updateCatalogTarget",
            description="Update a catalog target for an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse updateCatalogTarget(@PathVariable String                  serverName,
                                            @PathVariable String                  serviceURLMarker,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  relationshipGUID,
                                            @RequestBody  CatalogTargetProperties requestBody)
    {
        return restAPI.updateCatalogTarget(serverName, serviceURLMarker, userId, relationshipGUID, requestBody);
    }



    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/catalog-targets/{relationshipGUID}")

    @Operation(summary="getCatalogTarget",
            description="Retrieve a specific catalog target associated with an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public CatalogTargetResponse getCatalogTarget(@PathVariable String serverName,
                                                  @PathVariable String serviceURLMarker,
                                                  @PathVariable String userId,
                                                  @PathVariable String relationshipGUID)
    {
        return restAPI.getCatalogTarget(serverName, serviceURLMarker, userId, relationshipGUID);
    }


    /**
     * Retrieve the details of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets")

    @Operation(summary="getCatalogTargets",
            description="Retrieve the details of the metadata elements identified as catalog targets with an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public CatalogTargetsResponse  getCatalogTargets(@PathVariable String  serverName,
                                                     @PathVariable String  serviceURLMarker,
                                                     @PathVariable String  userId,
                                                     @PathVariable String  integrationConnectorGUID,
                                                     @RequestParam int     startingFrom,
                                                     @RequestParam int     maximumResults)
    {
        return restAPI.getCatalogTargets(serverName, serviceURLMarker, userId, integrationConnectorGUID, startingFrom, maximumResults);
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}/delete")

    @Operation(summary="removeCatalogTarget",
            description="Unregister a catalog target from the integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse removeCatalogTarget(@PathVariable String          serverName,
                                            @PathVariable String          serviceURLMarker,
                                            @PathVariable String          userId,
                                            @PathVariable String          relationshipGUID,
                                            @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeCatalogTarget(serverName, serviceURLMarker, userId, relationshipGUID, requestBody);
    }
}
