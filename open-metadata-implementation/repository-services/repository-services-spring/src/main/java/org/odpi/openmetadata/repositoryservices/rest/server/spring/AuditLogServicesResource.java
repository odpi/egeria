/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSAuditLogRESTServices;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSMetadataHighwayRESTServices;
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
public class AuditLogServicesResource
{
    private OMRSAuditLogRESTServices restAPI = new OMRSAuditLogRESTServices();

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

    public AuditLogReportResponse getAuditLogReport(@PathVariable String   serverName,
                                                    @PathVariable String   userId)
    {
        return restAPI.getAuditLogReport(serverName, userId);
    }
}
