/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSMetadataHighwayRESTServices;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * AnonRepositoryServicesResource provides the server-side support for the OMRS Repository REST Services API
 * that do not include a userId.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/users/{userId}/metadata-highway")
public class MetadataHighwayServicesResource
{
    private OMRSMetadataHighwayRESTServices restAPI = new OMRSMetadataHighwayRESTServices();

    /**
     * Default constructor
     */
    public MetadataHighwayServicesResource()
    {
    }


    /**
     * Return the details of the cohorts that this server is participating in.
     *
     * @param serverName name of server
     * @param userId calling user
     * @return variety of properties
     */
    @GetMapping( path = "/cohort-descriptions")

    public CohortListResponse getCohorts(@PathVariable String   serverName,
                                         @PathVariable String   userId)
    {
        return restAPI.getCohortList(serverName, userId);
    }


    /**
     * Return the local registration information used by this server to register with open metadata repository cohorts.
     *
     * @param serverName server to query
     * @param userId calling user
     * @return registration properties for server
     */
    @GetMapping( path = "/local-registration")

    public CohortMembershipResponse getLocalRegistration(@PathVariable String   serverName,
                                                         @PathVariable String   userId)
    {
        return restAPI.getLocalRegistration(serverName, userId);
    }


    @GetMapping( path = "/cohorts/{cohortName}/remote-members")

    public CohortMembershipListResponse getRemoteRegistrations(@PathVariable String   serverName,
                                                               @PathVariable String   userId,
                                                               @PathVariable String   cohortName)
    {
        return restAPI.getRemoteRegistrations(serverName, userId, cohortName);
    }

}
