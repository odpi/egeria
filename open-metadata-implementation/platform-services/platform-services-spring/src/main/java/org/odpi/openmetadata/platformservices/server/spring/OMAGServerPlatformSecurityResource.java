/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformSecurityServices;
import org.springframework.web.bind.annotation.*;

/**
 * OMAGServerPlatformSecurityResource provides the API to configure the security connector that validates
 * platform requests that do not reference an OMAG server.  These requests are used by the
 * team that run the platform as a service.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/server-platform/security")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that the OMAG Server Platform is hosting.  " +
                                                   "It is also able to dynamically change the platform metadata security connector.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/platform-services/overview"))

public class OMAGServerPlatformSecurityResource
{
    private final OMAGServerPlatformSecurityServices adminSecurityAPI = new OMAGServerPlatformSecurityServices();


    /**
     * Set up a platform metadata security connector
     *
     * @param delegatingUserId external userId making request
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPlatformSecurityConnection",
               description="Set up a platform metadata security connector to control access to the platform and admin services.  This overrides the previously configured connection.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/platform-services/overview/"))

    public VoidResponse setPlatformSecurityConnection(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                      @RequestBody  PlatformSecurityRequestBody requestBody)
    {
        return adminSecurityAPI.setPlatformSecurityConnection(delegatingUserId, requestBody);
    }


    /**
     * Return the connection object for platform metadata security connector.  Null is returned if no platform security
     * has been set up.
     *
     * @param delegatingUserId external userId making request
     * @return connection response
     */
    @GetMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getPlatformSecurityConnection",
               description="Return the connection object for platform metadata security connector.  Null is returned if no connector has been set up.",
               externalDocs=@ExternalDocumentation(description="Metadata Security",
                                                   url="https://egeria-project.org/features/metadata-security/overview/"))


    public ConnectionResponse getPlatformSecurityConnection(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminSecurityAPI.getPlatformSecurityConnection(delegatingUserId);
    }


    /**
     * Clear the connection object for platform security.  This means there is no platform security set up.
     *
     * @param delegatingUserId external userId making request
     * @return void response
     */
    @DeleteMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearPlatformSecurityConnection",
               description="Clear the connection object for platform security.  This means there is no platform security set up. Note that this command must be issued by a user that has permission to operate the OMAG Server Platform.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/platform-services/overview/"))

    public  VoidResponse clearPlatformSecurityConnection(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminSecurityAPI.clearPlatformSecurityConnection(delegatingUserId);
    }


    /**
     * Return the list of users from the platform metadata security connector.  Null is returned if no platform security or user accounts have been set up.
     *
     * @param delegatingUserId external userId making request
     * @param userAccountStatus status of the user - or null for any status
     * @param userAccountType   type of user - or null for any type
     * @return user list response
     */
    @GetMapping(path = "/user-list")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getUserList",
            description="Return the list of users from the platform metadata security connector.  Null is returned if no platform security or user accounts have been set up.",
            externalDocs=@ExternalDocumentation(description="Metadata Security",
                    url="https://egeria-project.org/features/metadata-security/overview/"))


    public NameListResponse getUserList(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                        @Parameter(description="UserAccountStatus enum")  @RequestParam(required = false) UserAccountStatus userAccountStatus,
                                        @Parameter(description="UserAccountType enum")  @RequestParam(required = false) UserAccountType userAccountType)
    {
        return adminSecurityAPI.getUserList(delegatingUserId, userAccountStatus, userAccountType);
    }

    /**
     * Set up a user account in the platform metadata security connector
     *
     * @param delegatingUserId external userId making request
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/user-accounts")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setUserAccount",
            description="Set up a user in the platform metadata security connector to control access to open metadata.  This overrides the ",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview"))

    public VoidResponse setUserAccount(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                       @RequestBody UserAccountRequestBody requestBody)
    {
        return adminSecurityAPI.setUserAccount(delegatingUserId, requestBody);
    }


    /**
     * Return the user account object for the requested user from the platform metadata security connector.  Null is returned if no platform security or user account has been set up.
     *
     * @param accountUserId    user id of the account
     * @param delegatingUserId external userId making request
     * @return user account response
     */
    @GetMapping(path = "/user-accounts/{accountUserId}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getUserAccount",
            description="Return the user account object for the requested user from the platform metadata security connector.  Null is returned if no platform security or user account has been set up.",
            externalDocs=@ExternalDocumentation(description="Metadata Security",
                    url="https://egeria-project.org/features/metadata-security/overview/"))


    public UserAccountResponse getUserAccount(@Parameter(description="account user id")  @PathVariable String accountUserId,
                                              @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminSecurityAPI.getUserAccount(delegatingUserId, accountUserId);
    }


    /**
     * Clear the account for a user with the platform security connector.
     *
     * @param accountUserId    user id of the account
     * @param delegatingUserId external userId making request
     * @return void response
     */
    @DeleteMapping(path = "/user-accounts/{accountUserId}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteUserAccount",
            description="Clear the account for a user with the platform security connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview/"))

    public  VoidResponse deleteUserAccount(@Parameter(description="account user id")  @PathVariable String accountUserId,
                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminSecurityAPI.deleteUserAccount(delegatingUserId, accountUserId);
    }


    /**
     * Set up a security access control in the platform metadata security connector
     *
     * @param delegatingUserId external userId making request
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/security-access-controls")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setSecurityAccessControl",
            description="Set up a security access control in the platform metadata security connector to control access to open metadata.  This overrides the ",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview"))

    public VoidResponse setSecurityAccessControl(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                       @RequestBody SecurityAccessControlRequestBody requestBody)
    {
        return adminSecurityAPI.setSecurityAccessControl(delegatingUserId, requestBody);
    }


    /**
     * Return the requested security access control object from the platform metadata security connector.  Null is returned if no platform security or security access control has been set up.
     *
     * @param controlName    user id of the account
     * @param delegatingUserId external userId making request
     * @return security access control response
     */
    @GetMapping(path = "/security-access-controls/{controlName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSecurityAccessControl",
            description="Return the requested security access control object from the platform metadata security connector.  Null is returned if no platform security or security access control has been set up.",
            externalDocs=@ExternalDocumentation(description="Metadata Security",
                    url="https://egeria-project.org/features/metadata-security/overview/"))


    public SecurityAccessControlResponse getSecurityAccessControl(@Parameter(description="control name")  @PathVariable String controlName,
                                                                  @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminSecurityAPI.getSecurityAccessControl(delegatingUserId, controlName);
    }


    /**
     * Clear the security access control with the platform security connector.
     *
     * @param controlName    user id of the account
     * @param delegatingUserId external userId making request
     * @return void response
     */
    @DeleteMapping(path = "/security-access-controls/{controlName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSecurityAccessControl",
            description="Clear the security access control with the platform security connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/platform-services/overview/"))

    public  VoidResponse deleteSecurityAccessControl(@Parameter(description="control name")  @PathVariable String controlName,
                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminSecurityAPI.deleteSecurityAccessControl(delegatingUserId, controlName);
    }
}
