/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ZoneListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ZoneResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceZoneRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceLeadershipResource provides a Spring based server-side REST API
 * that supports the GovernanceLeadershipInterface.   It delegates each request to the
 * GovernanceZoneRESTServices.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}/governance-zone-manager")

@Tag(name="Governance Program OMAS", description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape." +
        "\n", externalDocs=@ExternalDocumentation(description="Governance Program Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/governance-program/"))

public class GovernanceZonesResource
{
    private GovernanceZoneRESTServices  restAPI = new GovernanceZoneRESTServices();

    /**
     * Default constructor
     */
    public GovernanceZonesResource()
    {
    }


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody other properties for a governance zone
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones")

    public VoidResponse  createGovernanceZone(@PathVariable String                   serverName,
                                              @PathVariable String                   userId,
                                              @RequestBody  GovernanceZoneProperties requestBody)
    {
        return restAPI.createGovernanceZone(serverName, userId, requestBody);
    }


    /**
     * Return information about all of the governance zones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     *
     * @return properties of the governance zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/governance-zones")

    public ZoneListResponse getGovernanceZones(@PathVariable String   serverName,
                                               @PathVariable String   userId,
                                               @RequestParam int      startingFrom,
                                               @RequestParam int      maximumResults)
    {
        return restAPI.getGovernanceZones(serverName, userId, startingFrom, maximumResults);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/governance-zones/name/{qualifiedName}")

    public ZoneResponse getGovernanceZone(@PathVariable String   serverName,
                                          @PathVariable String   userId,
                                          @PathVariable String   qualifiedName)
    {
        return restAPI.getGovernanceZone(serverName, userId, qualifiedName);
    }
}
