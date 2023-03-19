/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceengine.properties.CatalogTargetProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.IntegrationGroupProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.accessservices.governanceengine.server.IntegrationConfigRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * GroupConfigurationResource provides the Spring wrapper for the Integration Group Configuration Services
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")

@Tag(name="Governance Engine OMAS", description="The Governance Engine Open Metadata Access Service (OMAS) provides support for governance engines, services and actions.",
        externalDocs=@ExternalDocumentation(description="Governance Engine Open Metadata Access Service (OMAS)",
                url="https://egeria-project.org/services/omas/governance-engine/overview/"))

public class GroupConfigurationResource
{
    private final IntegrationConfigRESTServices restAPI = new IntegrationConfigRESTServices();


    /**
     * Create a new integration group definition.
     *
     * @param serverName name of the service to route the request to
     * @param userId identifier of calling user
     * @param requestBody containing properties of integration group.
     *
     * @return unique identifier (guid) of the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @PostMapping(path = "/integration-groups/new")

    public GUIDResponse createIntegrationGroup(@PathVariable String                     serverName,
                                               @PathVariable String                     userId,
                                               @RequestBody  IntegrationGroupProperties requestBody)
    {
        return restAPI.createIntegrationGroup(serverName, userId, requestBody);
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration group definition.
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups/{guid}")

    public IntegrationGroupElementResponse getIntegrationGroupByGUID(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid)
    {
        return restAPI.getIntegrationGroupByGUID(serverName, userId, guid);
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-groups/by-name/{name}")

    public  IntegrationGroupElementResponse getIntegrationGroupByName(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String name)
    {
        return restAPI.getIntegrationGroupByName(serverName, userId, name);
    }


    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
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

    public IntegrationGroupElementsResponse getAllIntegrationGroups(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @RequestParam int    startingFrom,
                                                                    @RequestParam int    maximumResults)
    {
        return restAPI.getAllIntegrationGroups(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
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

    public VoidResponse updateIntegrationGroup(@PathVariable String                     serverName,
                                               @PathVariable String                     userId,
                                               @PathVariable String                     guid,
                                               @RequestParam boolean                    isMergeUpdate,
                                               @RequestBody  IntegrationGroupProperties requestBody)
    {
        return restAPI.updateIntegrationGroup(serverName, userId, guid, isMergeUpdate, requestBody);
    }


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param serverName name of the service to route the request to.
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

    public  VoidResponse   deleteIntegrationGroup(@PathVariable String            serverName,
                                                  @PathVariable String            userId,
                                                  @PathVariable String            guid,
                                                  @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteIntegrationGroup(serverName, userId, guid, requestBody);
    }


    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param serverName name of the service to route the request to
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

    public  GUIDResponse createIntegrationConnector(@PathVariable String                         serverName,
                                                    @PathVariable String                         userId,
                                                    @RequestBody  IntegrationConnectorProperties requestBody)
    {
        return restAPI.createIntegrationConnector(serverName, userId, requestBody);
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-connectors/{guid}")

    public IntegrationConnectorElementResponse getIntegrationConnectorByGUID(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String guid)
    {
        return restAPI.getIntegrationConnectorByGUID(serverName, userId, guid);
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-connectors/by-name/{name}")

    public  IntegrationConnectorElementResponse getIntegrationConnectorByName(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String name)
    {
        return restAPI.getIntegrationConnectorByName(serverName, userId, name);
    }


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
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

    public IntegrationConnectorElementsResponse getAllIntegrationConnectors(@PathVariable String serverName,
                                                                            @PathVariable String userId,
                                                                            @RequestParam int    startingFrom,
                                                                            @RequestParam int    maximumResults)
    {
        return restAPI.getAllIntegrationConnectors(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @GetMapping(path = "/integration-connectors/{guid}/registrations")

    public GUIDListResponse getIntegrationConnectorRegistrations(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String guid)
    {
        return restAPI.getIntegrationConnectorRegistrations(serverName, userId, guid);
    }


    /**
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
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

    public VoidResponse updateIntegrationConnector(@PathVariable String                          serverName,
                                                   @PathVariable String                          userId,
                                                   @PathVariable String                          guid,
                                                   @RequestParam boolean                         isMergeUpdate,
                                                   @RequestBody  IntegrationConnectorProperties  requestBody)
    {
        return restAPI.updateIntegrationConnector(serverName, userId, guid, isMergeUpdate, requestBody);
    }


    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param serverName name of the service to route the request to.
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

    public VoidResponse deleteIntegrationConnector(@PathVariable String            serverName,
                                                   @PathVariable String            userId,
                                                   @PathVariable String            guid,
                                                   @RequestBody  DeleteRequestBody requestBody)
    {
        return restAPI.deleteIntegrationConnector(serverName, userId, guid, requestBody);
    }


    /**
     * Register an integration connector with a specific integration group.
     *
     * @param serverName name of the service to route the request to.
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

    public VoidResponse registerIntegrationConnectorWithGroup(@PathVariable String                                   serverName,
                                                              @PathVariable String                                   userId,
                                                              @PathVariable String                                   integrationGroupGUID,
                                                              @PathVariable String                                   integrationConnectorGUID,
                                                              @RequestBody  RegisteredIntegrationConnectorProperties requestBody)
    {
        return restAPI.registerIntegrationConnectorWithGroup(serverName, userId, integrationGroupGUID, integrationConnectorGUID, requestBody);
    }


    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param serverName name of the service to route the request to.
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

    public RegisteredIntegrationConnectorResponse getRegisteredIntegrationConnector(@PathVariable String serverName,
                                                                                    @PathVariable String userId,
                                                                                    @PathVariable String integrationGroupGUID,
                                                                                    @PathVariable String integrationConnectorGUID)
    {
        return restAPI.getRegisteredIntegrationConnector(serverName, userId, integrationGroupGUID, integrationConnectorGUID);
    }


    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param serverName name of the service to route the request to.
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

    public RegisteredIntegrationConnectorsResponse getRegisteredIntegrationConnectors(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String integrationGroupGUID,
                                                                                      @RequestParam int    startingFrom,
                                                                                      @RequestParam int    maximumResults)
    {
        return restAPI.getRegisteredIntegrationConnectors(serverName, userId, integrationGroupGUID, startingFrom, maximumResults);
    }


    /**
     * Unregister an integration connector from the integration group.
     *
     * @param serverName name of the service to route the request to.
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

    public VoidResponse unregisterIntegrationConnectorFromEngine(@PathVariable                  String          serverName,
                                                                 @PathVariable                  String          userId,
                                                                 @PathVariable                  String          integrationGroupGUID,
                                                                 @PathVariable                  String          integrationConnectorGUID,
                                                                 @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unregisterIntegrationConnectorFromEngine(serverName, userId, integrationGroupGUID, integrationConnectorGUID, requestBody);
    }


    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets/{metadataElementGUID}")

    public VoidResponse addCatalogTarget(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  integrationConnectorGUID,
                                         @PathVariable String                  metadataElementGUID,
                                         @RequestBody  CatalogTargetProperties requestBody)
    {
        return restAPI.addCatalogTarget(serverName, userId, integrationConnectorGUID, metadataElementGUID, requestBody);
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets/{metadataElementGUID}")

    public CatalogTargetResponse getCatalogTarget(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String integrationConnectorGUID,
                                                  @PathVariable String metadataElementGUID)
    {
         return restAPI.getCatalogTarget(serverName, userId, integrationConnectorGUID, metadataElementGUID);
    }


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
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

    public CatalogTargetsResponse  getCatalogTargets(@PathVariable String  serverName,
                                                     @PathVariable String  userId,
                                                     @PathVariable String  integrationConnectorGUID,
                                                     @RequestParam int     startingFrom,
                                                     @RequestParam int     maximumResults)
    {
        return restAPI.getCatalogTargets(serverName, userId, integrationConnectorGUID, startingFrom, maximumResults);
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param metadataElementGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets/{metadataElementGUID}/delete")

    public VoidResponse removeCatalogTarget(@PathVariable String          serverName,
                                            @PathVariable String          userId,
                                            @PathVariable String          integrationConnectorGUID,
                                            @PathVariable String          metadataElementGUID,
                                            @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeCatalogTarget(serverName, userId, integrationConnectorGUID, metadataElementGUID, requestBody);
    }
}
