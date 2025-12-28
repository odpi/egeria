/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogReportResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogSeveritiesResponse;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSAuditLogRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * AuditLogServicesResource provides the server-side support for the OMRS Repository REST Services API
 * that provide information about the local server's audit log.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/audit-log")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Repository Services - Audit Log", description="Details of the activity within an OMAG Server are written to " +
        "the server's configured audit log destinations.  This service retrieves information about the audit log and its " +
        "contents for a particular server.",
        externalDocs= @ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/services/omrs/"))


public class AuditLogServicesResource
{
    private final OMRSAuditLogRESTServices restAPI = new OMRSAuditLogRESTServices();

    /**
     * Default constructor
     */
    public AuditLogServicesResource()
    {
    }


    /**
     * Return the details of the severities that this server supports.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return variety of properties
     */
    @GetMapping(path = "/severity-definitions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSeverityList",
               description="Return the details of the severities that this server supports.",
               externalDocs=@ExternalDocumentation(description="Audit Log",
                                                   url="https://egeria-project.org/concepts/audit-log/"))

    public AuditLogSeveritiesResponse getSeverityList(@PathVariable String serverName,
                                                      @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.getSeverityList(serverName, delegatingUserId);
    }


    /**
     * Return the report from the local server's audit log.
     *
     * @param serverName server to query
     * @param delegatingUserId external userId making request
     * @return registration properties for server
     */
    @GetMapping(path = "/report")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAuditLogReport",
               description="Return the report from the local server's audit log.",
               externalDocs=@ExternalDocumentation(description="Audit Log",
                                                   url="https://egeria-project.org/concepts/audit-log/"))

    public AuditLogReportResponse getAuditLogReport(@PathVariable String   serverName,
                                                    @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.getAuditLogReport(serverName, delegatingUserId);
    }
}
