/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.ConnectorConfigPropertiesRequestBody;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.*;
import org.odpi.openmetadata.viewservices.runtimemanager.server.RuntimeManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The RuntimeManagerResource provides part of the server-side implementation of the Runtime Manager OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/runtime-manager")

@Tag(name="API: Runtime Manager OMVS", description="The Runtime Manager OMVS provides APIs for retrieving and updating code values and reference data.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/runtime-manager/overview/"))

public class RuntimeManagerResource
{
    private final RuntimeManagerRESTServices restAPI = new RuntimeManagerRESTServices();

    /**
     * Default constructor
     */
    public RuntimeManagerResource()
    {
    }


    /**
     * Returns the list of platforms with a particular name.
     *
     * @param serverName  name of called server
     * @param startFrom   index of the list to start from (0 for start)
     * @param pageSize    maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping("/platforms/by-name")

    @Operation(summary = "getPlatformsByName",
            description = "Returns the list of platforms with a particular name.  The name is specified in the filter.",
            externalDocs = @ExternalDocumentation(description = "Software Server Platforms",
                    url = "https://egeria-project.org/types/0/0037-Software-Server-Platforms/"))

    public SoftwareServerPlatformsResponse getPlatformsByName(@PathVariable String serverName,
                                                              @RequestParam int startFrom,
                                                              @RequestParam int pageSize,
                                                              @RequestBody (required = false)
                                                                     FilterRequestBody requestBody)
    {
        return restAPI.getPlatformsByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of platforms with a particular deployed implementation type.
     *
     * @param serverName  name of called server
     * @param requestBody name of the deployed implementation type - if null, all projects are returned
     * @param startFrom   index of the list to start from (0 for start)
     * @param pageSize    maximum number of elements to return
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping("/platforms/by-deployed-implementation-type")

    @Operation(summary = "getPlatformsByDeployedImplementationType",
            description = "Returns the list of platforms with a particular deployed implementation type.  The value is specified in the filter.  If it is null, or no request body is supplied, all platforms are returned.",
            externalDocs = @ExternalDocumentation(description = "Software Server Platforms",
                    url = "https://egeria-project.org/types/0/0037-Software-Server-Platforms/"))

    public SoftwareServerPlatformsResponse getPlatformsByDeployedImplType(@PathVariable String serverName,
                                                                          @RequestParam int startFrom,
                                                                          @RequestParam int pageSize,
                                                                          @RequestParam (required = false, defaultValue = "false") boolean getTemplates,
                                                                          @RequestBody (required = false)
                                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getPlatformsByDeployedImplType(serverName, startFrom, pageSize, getTemplates, requestBody);
    }


    /**
     * Returns details about the platform's catalog entry (asset).
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody effective time
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping("/platforms/{platformGUID}")

    @Operation(summary = "getPlatformByGUID",
            description = "Returns details about the platform's catalog entry (asset).",
            externalDocs = @ExternalDocumentation(description = "Software Server Platforms",
                    url = "https://egeria-project.org/types/0/0037-Software-Server-Platforms/"))

    public SoftwareServerPlatformResponse getPlatformByGUID(@PathVariable String serverName,
                                                            @PathVariable String platformGUID,
                                                            @RequestBody(required = false)
                                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getPlatformByGUID(serverName, platformGUID, requestBody);
    }


    /**
     * Returns details about the running platform.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping("/platforms/{platformGUID}/report")

    @Operation(summary = "getPlatformReport",
            description = "Returns details about the running OMAG Server Platform.",
            externalDocs = @ExternalDocumentation(description = "Software Server Platforms",
                    url = "https://egeria-project.org/types/0/0037-Software-Server-Platforms/"))

    public PlatformReportResponse getPlatformReport(@PathVariable String serverName,
                                                    @PathVariable String platformGUID)
    {
        return restAPI.getPlatformReport(serverName, platformGUID);
    }


    /**
     * Returns the list of servers with a particular name.
     *
     * @param serverName  name of called server
     * @param requestBody qualified name or display name of the server
     * @param startFrom   index of the list to start from (0 for start)
     * @param pageSize    maximum number of elements to return
     * @return a list of servers
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping("/software-servers/by-name")

    @Operation(summary = "getServersByName",
            description = "Returns the list of servers with a particular name.  The name is specified in the filter.",
            externalDocs = @ExternalDocumentation(description = "Software Servers",
                    url = "https://egeria-project.org/types/0/0040-Software-Servers/"))

    public SoftwareServersResponse getServersByName(@PathVariable String serverName,
                                                    @RequestParam int startFrom,
                                                    @RequestParam int pageSize,
                                                    @RequestBody(required = false)
                                                           FilterRequestBody requestBody)
    {
        return restAPI.getServersByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of servers with a particular deployed implementation type.
     *
     * @param serverName  name of called server
     * @param requestBody name of the deployed impl type - if null, all servers are returned
     * @param startFrom   index of the list to start from (0 for start)
     * @param pageSize    maximum number of elements to return
     * @param getTemplates boolean indicating whether templates or non-template servers should be returned.
     * @return a list of servers
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping("/software-servers/by-deployed-implementation-type")

    @Operation(summary = "getServersByDeployedImplementationType",
            description = "Returns the list of servers with a particular deployed implementation type.   The value is specified in the filter.  If it is null, or no request body is supplied, all servers are returned.",
            externalDocs = @ExternalDocumentation(description = "Software Servers",
                    url = "https://egeria-project.org/types/0/0040-Software-Servers/"))

    public SoftwareServersResponse getServersByDeployedImplType(@PathVariable String serverName,
                                                                @RequestParam int startFrom,
                                                                @RequestParam int pageSize,
                                                                @RequestParam (required = false, defaultValue = "false") boolean getTemplates,
                                                                @RequestBody(required = false) FilterRequestBody requestBody)
    {
        return restAPI.getServersByDeployedImplType(serverName, startFrom, pageSize, getTemplates, requestBody);
    }



    /**
     * Returns details about the server's catalog entry (asset).
     *
     * @param serverName  name of called server
     * @param serverGUID unique identifier of the platform
     * @param requestBody effective time
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping("/software-servers/{serverGUID}")

    @Operation(summary = "getServerByGUID",
            description = "Returns details about the server's catalog entry (asset).",
            externalDocs = @ExternalDocumentation(description = "Software Servers",
                    url = "https://egeria-project.org/types/0/0040-Software-Servers/"))

    public SoftwareServerResponse getServerByGUID(@PathVariable String serverName,
                                                  @PathVariable String serverGUID,
                                                  @RequestBody(required = false)
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getServerByGUID(serverName, serverGUID, requestBody);
    }


    /**
     * Returns details about the running server.
     *
     * @param serverName  name of called server
     * @param serverGUID unique identifier of the server to call
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping("/omag-servers/{serverGUID}/instance/report")

    @Operation(summary = "getOMAGServerReport",
            description = "Returns details about the running OMAG Server.",
            externalDocs = @ExternalDocumentation(description = "OMAG Servers",
                    url = "https://egeria-project.org/concepts/omag-server/"))

    public ServerReportResponse getServerReport(@PathVariable String serverName,
                                                @PathVariable String serverGUID)
    {
        return restAPI.getServerReport(serverName, serverGUID);
    }


    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param serverName  local server name
     * @param serverGUID unique identifier of the server to call
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping(path = "/omag-servers/{serverGUID}/instance")

    @Operation(summary="activateWithStoredConfig",
            description="Activate the named OMAG server using the appropriate configuration document found in the configuration store.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public SuccessMessageResponse activateWithStoredConfig(@PathVariable String serverName,
                                                           @PathVariable String serverGUID)
    {
        return restAPI.activateWithStoredConfig(serverName, serverGUID);
    }


    /**
     * Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.
     *
     * @param serverName  local server name
     * @param serverGUID unique identifier of the server to call
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/omag-servers/{serverGUID}/instance")

    @Operation(summary="shutdownServer",
            description="Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.")

    public VoidResponse shutdownServer(@PathVariable String serverName,
                                       @PathVariable String serverGUID)
    {
        return restAPI.shutdownServer(serverName, serverGUID);
    }


    /**
     * Permanently deactivate any active servers and unregister from
     * any cohorts.
     *
     * @param serverName  local server name
     * @param serverGUID unique identifier of the server to call
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/omag-servers/{serverGUID}")

    @Operation(summary="shutdownAndUnregisterServer",
            description="Shutdown the named OMAG server.  The server will also be removed " +
                    "from any open metadata repository cohorts it has registered with.")

    public VoidResponse shutdownAndUnregisterServer(@PathVariable String serverName,
                                                    @PathVariable String serverGUID)
    {
        return restAPI.shutdownAndUnregisterServer(serverName, serverGUID);
    }


    /*
     * =============================================================
     * Open Metadata Archives
     */


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param serverGUID unique identifier of the server to call
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    @PostMapping(path = { "/omag-servers/{serverGUID}/instance/load/open-metadata-archives/file",
            "/metadata-access-stores/{serverGUID}/instance/load/open-metadata-archives/file"})

    @Operation(summary="addOpenMetadataArchiveFile",
            description="An open metadata archive contains metadata types and instances.  This operation loads an open metadata " +
                    "archive that is readable through the connector identified by the connection.  " +
                    "It can be used with OMAG servers that are of type Open Metadata Store.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchiveFile(@PathVariable String serverName,
                                                   @PathVariable String serverGUID,
                                                   @RequestBody  String fileName)
    {
        return restAPI.addOpenMetadataArchiveFile(serverName, serverGUID, fileName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param serverGUID unique identifier of the server to call
     * @param openMetadataArchive openMetadataArchive for the open metadata archive.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or openMetadataArchive parameter.
     */
    @PostMapping(path = {"/omag-servers/{serverGUID}/instance/load/open-metadata-archives/archive-content",
                         "/metadata-access-stores/{serverGUID}/instance/load/open-metadata-archives/archive-content"})

    @Operation(summary="addOpenMetadataArchiveContent",
            description="An open metadata archive contains metadata types and instances.  This operation loads the supplied open metadata " +
                    "archive into the local repository.  It can be used with OMAG servers that are of type Open Metadata Store.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchive(@PathVariable String              serverName,
                                               @PathVariable String              serverGUID,
                                               @RequestBody  OpenMetadataArchive openMetadataArchive)
    {
        return restAPI.addOpenMetadataArchiveContent(serverName, serverGUID, openMetadataArchive);
    }


    /*
     * =============================================================
     * Engine Host
     */


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param serverGUID unique identifier of the server to call
     * @param governanceEngineName unique name of the governance engine
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    @GetMapping(path = "/engine-hosts/{serverGUID}/governance-engines/{governanceEngineName}/refresh-config")

    @Operation(summary="refreshConfig",
            description="Request that the governance engine refresh its configuration by calling the metadata server. " +
                    "This request is useful if the metadata server has an outage, particularly while the " +
                    "governance server is initializing.  This request just ensures that the latest configuration " +
                    "is in use.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine-definition/"))

    public  VoidResponse refreshConfig(@PathVariable String                       serverName,
                                       @PathVariable String                       serverGUID,
                                       @PathVariable String                       governanceEngineName)
    {
        return restAPI.refreshConfig(serverName, serverGUID, governanceEngineName);
    }



    /**
     * Request that all governance engines refresh their configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param serverGUID unique identifier of the server to call
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    @GetMapping(path = "/engine-hosts/{serverGUID}/governance-engines/refresh-config")

    @Operation(summary="refreshConfig",
            description="Request that all governance engines refresh their configuration by calling the metadata server. " +
                    "This request is useful if the metadata server has an outage, particularly while the " +
                    "governance server is initializing.  This request just ensures that the latest configuration " +
                    "is in use.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine-definition/"))

    public  VoidResponse refreshConfig(@PathVariable String serverName,
                                       @PathVariable String serverGUID)
    {
        return restAPI.refreshConfig(serverName, serverGUID);
    }


    /*
     * =============================================================
     * Integration Daemon
     */


    /**
     * Retrieve the configuration properties of the named integration connector running in the integration daemon.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param connectorName name of a specific connector
     *
     * @return properties map or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @GetMapping(path = "/integration-daemons/{serverGUID}/integration-connectors/{connectorName}/configuration-properties")

    @Operation(summary="getConfigurationProperties",
            description="Retrieve the configuration properties of the named integration connector running in the integration daemon.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public PropertiesResponse getConfigurationProperties(@PathVariable String serverName,
                                                         @PathVariable String serverGUID,
                                                         @PathVariable String connectorName)
    {
        return restAPI.getConfigurationProperties(serverName, serverGUID, connectorName);
    }


    /**
     * Update the configuration properties of the integration connectors, or specific integration connector if a
     * connector name is supplied.  This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/integration-connectors/configuration-properties")

    @Operation(summary="updateConfigurationProperties",
            description="Update the configuration properties of the integration connectors, or specific integration connector if a connector name is supplied.  This update is in memory and will not persist over a server restart.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  VoidResponse updateConfigurationProperties(@PathVariable String                               serverName,
                                                       @PathVariable String                               serverGUID,
                                                       @RequestBody  ConnectorConfigPropertiesRequestBody requestBody)
    {
        return restAPI.updateConfigurationProperties(serverName, serverGUID, requestBody);
    }


    /**
     * Update the endpoint network address for a specific integration connector.
     * This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param connectorName name of a specific connector
     * @param requestBody new endpoint address
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/integration-connectors/{connectorName}/endpoint-network-address")

    @Operation(summary="updateEndpointNetworkAddress",
            description="Update the endpoint network address for a specific integration connector.  This update is in memory and will not persist over a server restart.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  VoidResponse updateEndpointNetworkAddress(@PathVariable String            serverName,
                                                      @PathVariable String            serverGUID,
                                                      @PathVariable String            connectorName,
                                                      @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.updateEndpointNetworkAddress(serverName, serverGUID, connectorName, requestBody);
    }


    /**
     * Update the connection for a specific integration connector.
     * This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param connectorName name of a specific connector
     * @param requestBody new connection object
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/integration-connectors/{connectorName}/connection")

    @Operation(summary="updateConnectorConnection",
            description="Update the connection for a specific integration connector.  This update is in memory and will not persist over a server restart.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  VoidResponse updateConnectorConnection(@PathVariable String     serverName,
                                                   @PathVariable String     serverGUID,
                                                   @PathVariable String     connectorName,
                                                   @RequestBody  Connection requestBody)
    {
        return restAPI.updateConnectorConnection(serverName, serverGUID, connectorName,requestBody);
    }


    /**
     * Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/integration-connectors/refresh")

    @Operation(summary="refreshConnectors",
            description="Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse refreshConnectors(@PathVariable                  String          serverName,
                                          @PathVariable                  String          serverGUID,
                                          @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.refreshConnectors(serverName, serverGUID, requestBody);
    }


    /**
     * Restart all connectors running in the integration daemon, or restart a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/integration-connectors/restart")

    @Operation(summary="restartConnectors",
            description="Restart all connectors running in the integration daemon, or restart a specific connector if the connector name is specified.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse restartConnectors(@PathVariable                  String          serverName,
                                          @PathVariable                  String          serverGUID,
                                          @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.restartConnectors(serverName, serverGUID, requestBody);
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata access server.
     * Changes to the connector configuration will result in the affected connectors being restarted.
     * This request is useful if the metadata access server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param serverGUID unique identifier of the server to call
     * @param integrationGroupName unique name of the integration group
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  IntegrationGroupException there was a problem detected by the integration group.
     */
    @GetMapping(path = "/integration-daemons/{serverGUID}/integration-groups/{integrationGroupName}/refresh-config")

    @Operation(summary="refreshIntegrationGroupConfig",
            description="Request that the integration group refresh its configuration by calling the metadata access server. " +
                    "Changes to the connector configuration will result in the affected connectors being restarted. " +
                    "This request is useful if the metadata access server has an outage, particularly while the " +
                    "integration daemon is initializing.  This request just ensures that the latest configuration " +
                    "is in use.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public  VoidResponse refreshIntegrationGroupConfig(@PathVariable String serverName,
                                                       @PathVariable String serverGUID,
                                                       @PathVariable String integrationGroupName)
    {
        return restAPI.refreshIntegrationGroupConfig(serverName, serverGUID, integrationGroupName);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param event open lineage event to publish.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/open-lineage-events/publish")

    @Operation(summary="publishOpenLineageEvent",
            description="Send an Open Lineage event to the integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for open lineage events.",
            externalDocs=@ExternalDocumentation(description="Open Lineage Standard",
                    url="https://egeria-project.org/features/lineage-management/overview/#the-openlineage-standard"))

    public VoidResponse publishOpenLineageEvent(@PathVariable String serverName,
                                                @PathVariable String serverGUID,
                                                @RequestBody  String event)
    {
        return restAPI.publishOpenLineageEvent(serverName, serverGUID, event);
    }


    /*
     * =============================================================
     * Metadata Access Store
     */


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @GetMapping(path = "/cohort-members/{serverGUID}/cohorts/{cohortName}/connect")

    @Operation(summary = "connectToCohort",
            description = "A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the" +
                    " open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration" +
                    " event has been sent." +
                    " If the server has already registered in the past, it sends a reregistration request.",
            externalDocs = @ExternalDocumentation(description = "Cohort Member",
                    url = "https://egeria-project.org/concepts/cohort-member/"))

    public BooleanResponse connectToCohortGet(@PathVariable String serverName,
                                              @PathVariable String serverGUID,
                                              @PathVariable String cohortName)
    {
        return restAPI.connectToCohort(serverName, serverGUID, cohortName, null);
    }




    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @param requestBody null request body
     */
    @PostMapping(path = "/cohort-members/{serverGUID}/cohorts/{cohortName}/connect")

    @Operation(summary="connectToCohort",
            description="A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the" +
                    " open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration" +
                    " event has been sent." +
                    " If the server has already registered in the past, it sends a reregistration request.",
            externalDocs=@ExternalDocumentation(description="Cohort Member",
                    url="https://egeria-project.org/concepts/cohort-member/"))

    public BooleanResponse connectToCohortPost(@PathVariable String          serverName,
                                               @PathVariable String          serverGUID,
                                               @PathVariable String          cohortName,
                                               @RequestBody (required = false)
                                                             NullRequestBody requestBody)
    {
        return restAPI.connectToCohort(serverName, serverGUID, cohortName, requestBody);
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @GetMapping(path = "/cohort-members/{serverGUID}/cohorts/{cohortName}/disconnect")

    @Operation(summary="disconnectFromCohort",
            description="Disconnect communications from a specific cohort.",
            externalDocs=@ExternalDocumentation(description="Cohort Member",
                    url="https://egeria-project.org/concepts/cohort-member/"))

    public BooleanResponse disconnectFromCohortGet(@PathVariable String serverName,
                                                   @PathVariable String serverGUID,
                                                   @PathVariable String cohortName)
    {
        return restAPI.disconnectFromCohort(serverName, serverGUID, cohortName, null);
    }



    /**
     * Disconnect communications from a specific cohort.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @param requestBody null request body
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @PostMapping(path = "/cohort-members/{serverGUID}/cohorts/{cohortName}/disconnect")

    @Operation(summary="disconnectFromCohort",
            description="Disconnect communications from a specific cohort.",
            externalDocs=@ExternalDocumentation(description="Cohort Member",
                    url="https://egeria-project.org/concepts/cohort-member/"))

    public BooleanResponse disconnectFromCohortPost(@PathVariable String          serverName,
                                                    @PathVariable String          serverGUID,
                                                    @PathVariable String          cohortName,
                                                    @RequestBody (required = false)
                                                                  NullRequestBody requestBody)
    {
        return restAPI.disconnectFromCohort(serverName, serverGUID, cohortName, requestBody);
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @GetMapping(path = "/cohort-members/{serverGUID}/cohorts/{cohortName}/unregister")

    @Operation(summary="unregisterFromCohort",
            description="Unregister from a specific cohort and disconnect from cohort communications.",
            externalDocs=@ExternalDocumentation(description="Cohort Member",
                    url="https://egeria-project.org/concepts/cohort-member/"))

    public BooleanResponse unregisterFromCohortGet(@PathVariable String serverName,
                                                   @PathVariable String serverGUID,
                                                   @PathVariable String cohortName)
    {
        return restAPI.unregisterFromCohort(serverName, serverGUID, cohortName, null);
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @param requestBody null request body
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @PostMapping(path = "/cohort-members/{serverGUID}/cohorts/{cohortName}/unregister")

    @Operation(summary="unregisterFromCohort",
            description="Unregister from a specific cohort and disconnect from cohort communications.",
            externalDocs=@ExternalDocumentation(description="Cohort Member",
                    url="https://egeria-project.org/concepts/cohort-member/"))

    public BooleanResponse unregisterFromCohortPost(@PathVariable String          serverName,
                                                    @PathVariable String          serverGUID,
                                                    @PathVariable String          cohortName,
                                                    @RequestBody (required = false)
                                                                  NullRequestBody requestBody)
    {
        return restAPI.unregisterFromCohort(serverName, serverGUID, cohortName, requestBody);
    }
}