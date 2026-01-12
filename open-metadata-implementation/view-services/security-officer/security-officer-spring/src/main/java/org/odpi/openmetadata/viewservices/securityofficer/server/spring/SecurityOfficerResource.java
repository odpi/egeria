/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.securityofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.securityofficer.server.SecurityOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SecurityOfficerResource provides part of the server-side implementation of the Security Officer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/security-officer")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Security Officer", description="Supports a Security Officer as they lead the security governance program.  This builds on the capabilities of the Governance Officer OMVS.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/security-officer/overview/"))

public class SecurityOfficerResource
{
    private final SecurityOfficerRESTServices restAPI = new SecurityOfficerRESTServices();

    /**
     * Default constructor
     */
    public SecurityOfficerResource()
    {
    }



    /**
     * Attach a nested governance zone to a broader governance zone definition.
     *
     * @param serverName         name of called server
     * @param governanceZoneGUID  unique identifier of the first governance zone definition
     * @param nestedGovernanceZoneGUID      unique identifier of the second governance zone definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-zones/{governanceZoneGUID}/governance-zone-hierarchies/{nestedGovernanceZoneGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkGovernanceZones",
            description="Attach a nested governance zone to a broader governance zone definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-zone"))

    public VoidResponse linkGovernanceZones(@PathVariable
                                         String                  serverName,
                                         @PathVariable
                                         String                  governanceZoneGUID,
                                         @PathVariable
                                         String                  nestedGovernanceZoneGUID,
                                         @RequestBody (required = false)
                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkGovernanceZones(serverName, governanceZoneGUID, nestedGovernanceZoneGUID, requestBody);
    }


    /**
     * Detach a governance zone definition from a hierarchical relationship.
     *
     * @param serverName         name of called server
     * @param parentGovernanceZoneGUID  unique identifier of the first governance zone definition
     * @param memberDataFieldGUID      unique identifier of the second governance zone definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-zones/{parentGovernanceZoneGUID}/governance-zone-hierarchies/{memberDataFieldGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachGovernanceZones",
            description="Detach a nested governance zone from a broader governance zone definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-zone"))

    public VoidResponse detachGovernanceZones(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String parentGovernanceZoneGUID,
                                           @PathVariable
                                           String memberDataFieldGUID,
                                           @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachGovernanceZones(serverName, parentGovernanceZoneGUID, memberDataFieldGUID, requestBody);
    }


}
