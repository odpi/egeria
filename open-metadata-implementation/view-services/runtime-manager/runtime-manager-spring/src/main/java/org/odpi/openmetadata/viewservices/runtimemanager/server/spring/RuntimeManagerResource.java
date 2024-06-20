/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerPlatformListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerPlatformResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.PlatformReportResponse;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.ServerReportResponse;
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

    public SoftwareServerPlatformListResponse getPlatformsByName(@PathVariable String serverName,
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

    public SoftwareServerPlatformListResponse getPlatformsByDeployedImplType(@PathVariable String serverName,
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
            description = "Returns details about the running platform.",
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
    @PostMapping("/servers/by-name")

    @Operation(summary = "getServersByName",
            description = "Returns the list of servers with a particular name.  The name is specified in the filter.",
            externalDocs = @ExternalDocumentation(description = "Software Servers",
                    url = "https://egeria-project.org/types/0/0040-Software-Servers/"))

    public SoftwareServerListResponse getServersByName(@PathVariable String serverName,
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
    @PostMapping("/servers/by-deployed-implementation-type")

    @Operation(summary = "getServersByDeployedImplementationType",
            description = "Returns the list of servers with a particular deployed implementation type.   The value is specified in the filter.  If it is null, or no request body is supplied, all servers are returned.",
            externalDocs = @ExternalDocumentation(description = "Software Servers",
                    url = "https://egeria-project.org/types/0/0040-Software-Servers/"))

    public SoftwareServerListResponse getServersByDeployedImplType(@PathVariable String serverName,
                                                                   @RequestParam int startFrom,
                                                                   @RequestParam int pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false") boolean getTemplates,
                                                                   @RequestBody FilterRequestBody requestBody)
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
    @PostMapping("/servers/{serverGUID}")

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
     * @param serverGUID unique identifier of the platform
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping("/servers/{serverGUID}/report")

    @Operation(summary = "getServerReport",
            description = "Returns details about the running platform.",
            externalDocs = @ExternalDocumentation(description = "Software Servers",
                    url = "https://egeria-project.org/types/00040-Software-Servers/"))

    public ServerReportResponse getServerReport(@PathVariable String serverName,
                                                @PathVariable String serverGUID)
    {
        return restAPI.getServerReport(serverName, serverGUID);
    }

}