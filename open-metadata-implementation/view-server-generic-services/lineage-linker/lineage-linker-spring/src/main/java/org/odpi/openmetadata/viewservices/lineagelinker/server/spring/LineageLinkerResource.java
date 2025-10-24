/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.lineagelinker.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.lineagelinker.server.LineageLinkerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The LineageLinkerResource provides part of the server-side implementation of the Lineage Linker OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Lineage Linker OMVS", description="The Lineage Linker OMVS provides APIs for supporting the creation and editing of schema types, schema attributes and user identities.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/lineage-linker/overview/"))

public class LineageLinkerResource
{
    private final LineageLinkerRESTServices restAPI = new LineageLinkerRESTServices();

    /**
     * Default constructor
     */
    public LineageLinkerResource()
    {
    }


    /**
     * Create a lineage relationship between two elements that indicates that data or control is flowing from one element to another.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementOneGUID unique identifier of the element at end one
     * @param elementTwoGUID unique identifier of the element at end two
     * @param relationshipTypeName lineage relationship name
     * @param requestBody the properties of the relationship
     *
     * @return relationship guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/from-elements/{elementOneGUID}/via/{relationshipTypeName}/to-elements/{elementTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkLineage",
            description="Create a lineage relationship between two elements that indicates that data or control is flowing from one element to another.",
            externalDocs=@ExternalDocumentation(description="Lineage Management",
                    url="https://egeria-project.org/features/lineage-management/overview/"))

    public GUIDResponse linkLineage(@PathVariable String                    serverName,
                                    @PathVariable String                    urlMarker,
                                    @PathVariable String                    elementOneGUID,
                                    @PathVariable String                    relationshipTypeName,
                                    @PathVariable String                    elementTwoGUID,
                                    @RequestBody(required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkLineage(serverName, urlMarker, elementOneGUID, relationshipTypeName, elementTwoGUID, requestBody);
    }


    /**
     * Update the lineage relationship.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param lineageRelationshipGUID unique identifier for the relationship
     * @param requestBody the properties of the relationship
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/relationships/{lineageRelationshipGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateLineage",
            description="Update the lineage relationship.",
            externalDocs=@ExternalDocumentation(description="Lineage Management",
                    url="https://egeria-project.org/features/lineage-management/overview/"))

    public VoidResponse updateLineage(@PathVariable String                        serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String                        lineageRelationshipGUID,
                                      @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateLineage(serverName, urlMarker, lineageRelationshipGUID, requestBody);
    }


    /**
     * Remove a lineage relationship between two elements.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param lineageRelationshipGUID unique identifier of the lineage relationship
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/relationships/{lineageRelationshipGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachLineage",
            description="Remove a lineage relationship between two elements..",
            externalDocs=@ExternalDocumentation(description="Lineage Management",
                    url="https://egeria-project.org/features/lineage-management/overview/"))

    public VoidResponse detachLineage(@PathVariable String           serverName,
                                      @PathVariable String           urlMarker,
                                      @PathVariable String           lineageRelationshipGUID,
                                      @RequestBody(required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachLineage(serverName, urlMarker, lineageRelationshipGUID, requestBody);
    }
}
