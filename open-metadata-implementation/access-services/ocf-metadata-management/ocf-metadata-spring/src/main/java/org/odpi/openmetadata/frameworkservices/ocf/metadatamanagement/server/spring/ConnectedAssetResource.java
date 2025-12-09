/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OCFConnectionResponse;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server.OCFRESTServices;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

/**
 * The ConnectedAssetResource is the server-side implementation of the REST services needed to
 * populate the Open Connector Framework (OCF) Connected Asset Properties.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/connected-asset/users/{userId}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Metadata Access Services: Connected Asset Services",
        description="Provides common services for Open Metadata Access Services (OMASs) that managed connections, create connectors and retrieve information related to the asset connected to the connection.",
        externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/ocf-metadata-management/"))

public class ConnectedAssetResource
{
    private final OCFRESTServices restAPI = new OCFRESTServices();

    /**
     * Default constructor
     */
    public ConnectedAssetResource()
    {
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/connections/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConnectionByGUID",
            description=" Returns the connection object corresponding to the supplied connection GUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public OCFConnectionResponse getConnectionByGUID(@PathVariable String     serverName,
                                                     @PathVariable String     userId,
                                                     @PathVariable String     guid)
    {
        return restAPI.getConnectionByGUID(serverName, userId, guid);
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of user making request.
     * @param name  the unique name for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/connections/by-name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConnectionByName",
            description="Returns the connection object corresponding to the supplied connection name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public OCFConnectionResponse getConnectionByName(@PathVariable String     serverName,
                                                     @PathVariable String     userId,
                                                     @PathVariable String     name)
    {
        return restAPI.getConnectionByName(serverName, userId, name);
    }


    /**
     * Save the connection optionally linked to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
     * @param userId      userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository. This optional.
     *                    However, if specified then the new connection is attached to the asset
     * @param connection connection to save
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="saveConnectionForAsset",
            description="Save the connection optionally linked to the supplied asset GUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public GUIDResponse saveConnectionForAsset(@PathVariable String     serverName,
                                               @PathVariable String     userId,
                                               @RequestParam(required = false)
                                               String     assetGUID,
                                               @RequestBody  Connection connection)
    {
        return restAPI.saveConnectionForAsset(serverName, userId, assetGUID, connection);
    }


    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
     * @param userId      userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConnectionForAsset",
            description="Returns the connection corresponding to the supplied asset GUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public OCFConnectionResponse getConnectionForAsset(@PathVariable String   serverName,
                                                       @PathVariable String   userId,
                                                       @PathVariable String   assetGUID)
    {
        return restAPI.getConnectionForAsset(serverName, userId, assetGUID);
    }
}
