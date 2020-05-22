/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipResponse;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSMetadataHighwayRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * MetadataHighwayServicesResource provides the server-side support for the OMRS Repository REST Services API
 * that provide information about the local server's interaction with an open metadata repository cohort.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/users/{userId}/metadata-highway")

@Tag(name="Repository Services - Metadata Highway",
        description="The Metadata Highway services are part of the Open Metadata Repository Services (OMRS). " +
                "They provide information about the status and membership of Open Metadata Repository Cohorts" +
                "that the local server is connected to.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria.odpi.org/open-metadata-implementation/repository-services/"))


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
    @GetMapping(path = "/cohort-descriptions")

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
    @GetMapping(path = "/local-registration")

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
    @GetMapping(path = "/cohorts/{cohortName}/local-registration")

    public CohortMembershipResponse getLocalRegistration(@PathVariable String   serverName,
                                                         @PathVariable String   userId,
                                                         @PathVariable String   cohortName)
    {
        return restAPI.getLocalRegistration(serverName, userId, cohortName);
    }


    /**
     * Return the list of registrations received from remote members of the cohort.
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of the specific cohort
     * @return list of registration information for remote members
     */
    @GetMapping(path = "/cohorts/{cohortName}/remote-members")

    public CohortMembershipListResponse getRemoteRegistrations(@PathVariable String   serverName,
                                                               @PathVariable String   userId,
                                                               @PathVariable String   cohortName)
    {
        return restAPI.getRemoteRegistrations(serverName, userId, cohortName);
    }

}
