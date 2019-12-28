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
    @RequestMapping(method = RequestMethod.GET, path = "/cohort-descriptions")

    public CohortListResponse getCohorts(@PathVariable String   serverName,
                                         @PathVariable String   userId)
    {
        return restAPI.getCohortList(serverName, userId);
    }


    /**
     * Return the local registration information used by this server to register with open metadata repository cohorts.
     * No registration time is provided.  Use the cohort specific version to retrieve the registration time.
     *
     * @param serverName server to query
     * @param userId calling user
     * @return registration properties for server
     */
    @RequestMapping(method = RequestMethod.GET, path = "/local-registration")

    public CohortMembershipResponse getLocalRegistration(@PathVariable String   serverName,
                                                         @PathVariable String   userId)
    {
        return restAPI.getLocalRegistration(serverName, userId);
    }


    /**
     * Return the local registration information used by this server to register with the requested
     * open metadata repository cohort.
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of the specific cohort to query for the registration information
     * @return local registration properties for server
     */
    @RequestMapping(method = RequestMethod.GET, path = "/cohorts/{cohortName}/local-registration")

    public CohortMembershipResponse getLocalRegistration(@PathVariable String   serverName,
                                                         @PathVariable String   userId,
                                                         @PathVariable String   cohortName)
    {
        return restAPI.getLocalRegistration(serverName, userId, cohortName);
    }


    /**
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of the specific cohort
     * @return list of registration information for remote members
     */
    @RequestMapping(method = RequestMethod.GET, path = "/cohorts/{cohortName}/remote-members")

    public CohortMembershipListResponse getRemoteRegistrations(@PathVariable String   serverName,
                                                               @PathVariable String   userId,
                                                               @PathVariable String   cohortName)
    {
        return restAPI.getRemoteRegistrations(serverName, userId, cohortName);
    }

}
