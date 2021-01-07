/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.accessservices.discoveryengine.server.DiscoveryConfigurationServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * DiscoveryConfigurationResource provides the Spring wrapper for the DiscoveryConfigurationServices
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/discovery-engine/users/{userId}")

@Tag(name="Discovery Engine OMAS", description="The Discovery Engine OMAS provides APIs and events for metadata discovery tools that are surveying the data landscape and recording information in metadata repositories.", externalDocs=@ExternalDocumentation(description="Discovery Engine Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/discovery-engine/"))

public class DiscoveryConfigurationResource
{
    private DiscoveryConfigurationServices  restAPI = new DiscoveryConfigurationServices();


    /**
     * Return the connection object for the Discovery Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Create a new discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                qualifiedName - unique name for the discovery engine;
     *                displayName - display name for messages and user interfaces;
     *                description - description of the types of discovery services that wil be associated with
     *                    this discovery engine.
     *
     * @return unique identifier (guid) of the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-engines")

    public GUIDResponse createDiscoveryEngine(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @RequestBody  NewDiscoveryEngineRequestBody requestBody)
    {
        return restAPI.createDiscoveryEngine(serverName, userId, requestBody);
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-engines/{guid}")

    public DiscoveryEnginePropertiesResponse getDiscoveryEngineByGUID(@PathVariable String    serverName,
                                                                      @PathVariable String    userId,
                                                                      @PathVariable String    guid)
    {
        return restAPI.getDiscoveryEngineByGUID(serverName, userId, guid);
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-engines/by-name/{name}")

    public  DiscoveryEnginePropertiesResponse getDiscoveryEngineByName(@PathVariable String    serverName,
                                                                       @PathVariable String    userId,
                                                                       @PathVariable String    name)
    {
        return restAPI.getDiscoveryEngineByName(serverName, userId, name);
    }


    /**
     * Return the list of discovery engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery engine definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-engines")

    public DiscoveryEngineListResponse getAllDiscoveryEngines(@PathVariable String  serverName,
                                                              @PathVariable String  userId,
                                                              @RequestParam int     startingFrom,
                                                              @RequestParam int     maximumResults)
    {
        return restAPI.getAllDiscoveryEngines(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Update the properties of an existing discovery engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param requestBody containing the new properties of the discovery engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-engines/{guid}")

    public VoidResponse updateDiscoveryEngine(@PathVariable String                           serverName,
                                              @PathVariable String                           userId,
                                              @PathVariable String                           guid,
                                              @RequestBody  UpdateDiscoveryEngineRequestBody requestBody)
    {
        return restAPI.updateDiscoveryEngine(serverName, userId, guid, requestBody);
    }


    /**
     * Remove the properties of the discovery engine.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery engine is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param requestBody containing the unique name for the discovery engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-engines/{guid}/delete")

    public  VoidResponse    deleteDiscoveryEngine(@PathVariable String             serverName,
                                                  @PathVariable String             userId,
                                                  @PathVariable String             guid,
                                                  @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteDiscoveryEngine(serverName, userId, guid, requestBody);
    }


    /**
     * Create a discovery service definition.  The same discovery service can be associated with multiple
     * discovery engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                    qualifiedName - unique name for the discovery service;
     *                    displayName -  display name for the discovery service;
     *                    description - description of the analysis provided by the discovery service;
     *                    connection -  connection to instantiate the discovery service implementation.
     *
     * @return unique identifier of the discovery service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-services")

    public  GUIDResponse  createDiscoveryService(@PathVariable String                         serverName,
                                                 @PathVariable String                         userId,
                                                 @RequestBody  NewDiscoveryServiceRequestBody requestBody)
    {
        return restAPI.createDiscoveryService(serverName, userId, requestBody);
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the discovery service definition.
     *
     * @return properties of the discovery service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-services/{guid}")

    public DiscoveryServicePropertiesResponse getDiscoveryServiceByGUID(@PathVariable String    serverName,
                                                                        @PathVariable String    userId,
                                                                        @PathVariable String    guid)
    {
        return restAPI.getDiscoveryServiceByGUID(serverName, userId, guid);
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-services/by-name/{name}")

    public  DiscoveryServicePropertiesResponse getDiscoveryServiceByName(@PathVariable String    serverName,
                                                                         @PathVariable String    userId,
                                                                         @PathVariable String    name)
    {
        return restAPI.getDiscoveryServiceByName(serverName, userId, name);
    }


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery service definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-services")

    public  DiscoveryServiceListResponse getAllDiscoveryServices(@PathVariable String  serverName,
                                                                 @PathVariable String  userId,
                                                                 @RequestParam int     startingFrom,
                                                                 @RequestParam int     maximumResults)
    {
        return restAPI.getAllDiscoveryServices(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Return the list of discovery engines that a specific discovery service is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid discovery service to search for.
     *
     * @return list of discovery engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-services/{guid}/registrations")

    public GUIDListResponse getDiscoveryServiceRegistrations(@PathVariable String   serverName,
                                                             @PathVariable String   userId,
                                                             @PathVariable String   guid)
    {
        return restAPI.getDiscoveryServiceRegistrations(serverName, userId, guid);
    }


    /**
     * Update the properties of an existing discovery service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param requestBody containing the new parameters for the discovery service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-services/{guid}")

    public  VoidResponse    updateDiscoveryService(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             guid,
                                                   @RequestBody  UpdateDiscoveryServiceRequestBody  requestBody)
    {
        return restAPI.updateDiscoveryService(serverName, userId, guid, requestBody);
    }


    /**
     * Remove the properties of the discovery service.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery service is being deleted.  The discovery service is also
     * unregistered from its discovery engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param requestBody containing the unique name for the discovery service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-services/{guid}/delete")

    public  VoidResponse    deleteDiscoveryService(@PathVariable String             serverName,
                                                   @PathVariable String             userId,
                                                   @PathVariable String             guid,
                                                   @RequestBody  DeleteRequestBody  requestBody)
    {
        return restAPI.deleteDiscoveryService(serverName, userId, guid, requestBody);
    }


    /**
     * Register a discovery service with a specific discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine.
     * @param requestBody containing:
     *                    guid - unique identifier of the discovery service;
     *                    discoveryRequestTypes - list of asset discovery types that this discovery service is able to process.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-engines/{guid}/discovery-services")

    public  VoidResponse  registerDiscoveryServiceWithEngine(@PathVariable String                                  serverName,
                                                             @PathVariable String                                  userId,
                                                             @PathVariable String                                  guid,
                                                             @RequestBody  DiscoveryServiceRegistrationRequestBody requestBody)
    {
        return restAPI.registerDiscoveryServiceWithEngine(serverName, userId, guid, requestBody);
    }


    /**
     * Retrieve a specific discovery service registered with a discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @return details of the discovery service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-engines/{discoveryEngineGUID}/discovery-services/{discoveryServiceGUID}")

    public RegisteredDiscoveryServiceResponse getRegisteredDiscoveryService(@PathVariable String  serverName,
                                                                            @PathVariable String  userId,
                                                                            @PathVariable String  discoveryEngineGUID,
                                                                            @PathVariable String  discoveryServiceGUID)
    {
        return restAPI.getRegisteredDiscoveryService(serverName, userId, discoveryEngineGUID, discoveryServiceGUID);
    }


    /**
     * Retrieve the identifiers of the discovery services registered with a discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/discovery-engines/{discoveryEngineGUID}/discovery-services")

    public  GUIDListResponse  getRegisteredDiscoveryServices(@PathVariable String  serverName,
                                                             @PathVariable String  userId,
                                                             @PathVariable String  discoveryEngineGUID,
                                                             @RequestParam int     startingFrom,
                                                             @RequestParam int     maximumResults)
    {
        return restAPI.getRegisteredDiscoveryServices(serverName, userId, discoveryEngineGUID, startingFrom, maximumResults);
    }


    /**
     * Unregister a discovery service from the discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @PostMapping(path = "/discovery-engines/{discoveryEngineGUID}/discovery-services/{discoveryServiceGUID}/delete")

    public VoidResponse unregisterDiscoveryServiceFromEngine(@PathVariable String           serverName,
                                                             @PathVariable String           userId,
                                                             @PathVariable String           discoveryEngineGUID,
                                                             @PathVariable String           discoveryServiceGUID,
                                                             @RequestBody  NullRequestBody  requestBody)
    {
        return restAPI.unregisterDiscoveryServiceFromEngine(serverName, userId, discoveryEngineGUID, discoveryServiceGUID, requestBody);
    }

}
