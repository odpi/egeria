/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogReportResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogSeveritiesResponse;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSAuditLogRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AuditLogServicesResource provides the server-side support for the OMRS Repository REST Services API
 * that provide information about the local server's audit log.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/users/{userId}/audit-log")

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
     * @param userId calling user
     * @return variety of properties
     */
    @GetMapping(path = "/severity-definitions")

    @Operation(summary="getSeverityList",
               description="Return the details of the severities that this server supports.",
               externalDocs=@ExternalDocumentation(description="Audit Log",
                                                   url="https://egeria-project.org/concepts/audit-log/"))

    public AuditLogSeveritiesResponse getSeverityList(@PathVariable String   serverName,
                                                      @PathVariable String   userId)
    {
        return restAPI.getSeverityList(serverName, userId);
    }


    /**
     * Return the report from the local server's audit log.
     *
     * @param serverName server to query
     * @param userId calling user
     * @return registration properties for server
     */
    @GetMapping(path = "/report")

    @Operation(summary="getAuditLogReport",
               description="Return the report from the local server's audit log.",
               externalDocs=@ExternalDocumentation(description="Audit Log",
                                                   url="https://egeria-project.org/concepts/audit-log/"))

    public AuditLogReportResponse getAuditLogReport(@PathVariable String   serverName,
                                                    @PathVariable String   userId)
    {
        return restAPI.getAuditLogReport(serverName, userId);
    }
}
