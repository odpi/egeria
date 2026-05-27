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
import org.odpi.openmetadata.commonservices.ffdc.rest.NameListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UserAccountRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UserAccountResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SecurityAccessControlRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SecurityAccessControlResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
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
     * Set up or update a user account in the platform metadata security connector.  The user requires operator permission for the platform unless it is their own user account they are retrieving.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/platforms/{platformGUID}/user-accounts")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setUserAccount",
            description="Set up or update a user account in the platform metadata security connector.  The user requires operator permission for the platform unless it is their own user account they are retrieving.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview"))

    public VoidResponse setUserAccount(@PathVariable String serverName,
                                       @PathVariable String platformGUID,
                                       @RequestBody(required = false) UserAccountRequestBody requestBody)
    {
        return restAPI.setUserAccount(serverName, platformGUID, requestBody);
    }


    /**
     * Return the user account object for the requested user from the platform metadata security connector.  Null is returned if no platform security or user account has been set up.
     * The user requires operator permission for the platform unless it is their own user account they are retrieving.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param accountUserId    user id of the account
     * @return user account response
     */
    @GetMapping(path = "/platforms/{platformGUID}/user-accounts/{accountUserId}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getUserAccount",
            description="Return the user account object for the requested user from the platform metadata security connector.  Null is returned if no platform security or user account has been set up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/metadata-security/overview/"))


    public UserAccountResponse getUserAccount(@PathVariable String serverName,
                                              @PathVariable String platformGUID,
                                              @PathVariable String accountUserId)
    {
        return restAPI.getUserAccount(serverName, platformGUID, accountUserId);
    }


    /**
     * Clear the account for a user with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param accountUserId    user id of the account
     * @return void response
     */
    @DeleteMapping(path = "/platforms/{platformGUID}/user-accounts/{accountUserId}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteUserAccount",
            description="Clear the account for a user with the platform security connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview/"))

    public  VoidResponse deleteUserAccount(@PathVariable String serverName,
                                           @PathVariable String platformGUID,
                                           @PathVariable String accountUserId)
    {
        return restAPI.deleteUserAccount(serverName, platformGUID, accountUserId);
    }



    /**
     * Set up or update a security access control in the platform metadata security connector.
     * The user requires operator permission for the platform.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/platforms/{platformGUID}/security-access-control")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setSecurityAccessControl",
            description="Set up or update a security access control in the platform metadata security connector to control access to open metadata.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview"))

    public VoidResponse setSecurityAccessControl(@PathVariable String serverName,
                                                 @PathVariable String platformGUID,
                                                 @RequestBody(required = false) SecurityAccessControlRequestBody requestBody)
    {
        return restAPI.setSecurityAccessControl(serverName, platformGUID, requestBody);
    }


    /**
     * Return the security access control object from the platform metadata security connector.  Null is returned if no control has been set up.
     * The user requires operator permission for the platform.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param controlName    name of the control
     * @return user account response
     */
    @GetMapping(path = "/platforms/{platformGUID}/security-access-control/{controlName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSecurityAccessControl",
            description="Return the security access control object from the platform metadata security connector.  Null is returned if no platform security or control has been set up.",
            externalDocs=@ExternalDocumentation(description="Metadata Security",
                    url="https://egeria-project.org/features/metadata-security/overview/"))


    public SecurityAccessControlResponse getSecurityAccessControl(@PathVariable String serverName,
                                                                  @PathVariable String platformGUID,
                                                                  @PathVariable String controlName)
    {
        return restAPI.getSecurityAccessControl(serverName, platformGUID, controlName);
    }


    /**
     * Clear the named security access control with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param controlName    name of the control
     * @return void response
     */
    @DeleteMapping(path = "/platforms/{platformGUID}/security-access-control/{controlName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSecurityAccessControl",
            description="Clear the security access control with the platform security connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview/"))

    public  VoidResponse deleteSecurityAccessControl(@PathVariable String serverName,
                                                     @PathVariable String platformGUID,
                                                     @PathVariable String controlName)
    {
        return restAPI.deleteSecurityAccessControl(serverName, platformGUID, controlName);
    }


    /**
     * Return the list of users registered with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param status status of the user - or null for any status
     * @param type   type of user - or null for any type
     * @return list of matching userIds in the user directory
     */
    @GetMapping(path = "/platforms/{platformGUID}/user-accounts")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getUserList",
            description="Return the list of users registered with the platform security connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview/"))

    public NameListResponse getUserList(@PathVariable String serverName,
                                        @PathVariable String platformGUID,
                                        @RequestParam(required = false) UserAccountStatus status,
                                        @RequestParam(required = false) UserAccountType type)
    {
        return restAPI.getUserList(serverName, platformGUID, status, type);
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
     * @param governanceZoneGUID  unique identifier of the first governance zone definition
     * @param nestedGovernanceZoneGUID      unique identifier of the second governance zone definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-zones/{governanceZoneGUID}/governance-zone-hierarchies/{nestedGovernanceZoneGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachGovernanceZones",
            description="Detach a nested governance zone from a broader governance zone definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-zone"))

    public VoidResponse detachGovernanceZones(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String governanceZoneGUID,
                                           @PathVariable
                                           String nestedGovernanceZoneGUID,
                                           @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachGovernanceZones(serverName, governanceZoneGUID, nestedGovernanceZoneGUID, requestBody);
    }


}
