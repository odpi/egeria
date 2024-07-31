/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceZoneRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceZonesResource provides a Spring based server-side REST API
 * that supports the GovernanceZonesInterface.   It delegates each request to the
 * GovernanceZoneRESTServices.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Metadata Access Server: Governance Program OMAS", description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape." +
        "\n",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceZonesResource
{
    private final GovernanceZoneRESTServices  restAPI = new GovernanceZoneRESTServices();

    /**
     * Default constructor
     */
    public GovernanceZonesResource()
    {
    }


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition, the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody other properties for a governance zone
     *
     * @return unique identifier of the new zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones")

    public GUIDResponse createGovernanceZone(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createGovernanceZone(serverName, userId, requestBody);
    }


    /**
     * Update the definition of a zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones/{zoneGUID}")

    public VoidResponse updateGovernanceZone(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   zoneGUID,
                                             @RequestParam boolean                  isMergeUpdate,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateGovernanceZone(serverName, userId, zoneGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the definition of a zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     * @param requestBody external source requestBody
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones/{zoneGUID}/delete")

    public VoidResponse deleteGovernanceZone(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String zoneGUID,
                                             @RequestBody(required = false)
                                                     ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteGovernanceZone(serverName, userId, zoneGUID, requestBody);
    }


    /**
     * Link two related governance zones together as part of a hierarchy.
     * A zone can only have one parent but many child zones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones/{parentZoneGUID}/nested-zone/{childZoneGUID}/link")

    public VoidResponse linkZonesInHierarchy(@PathVariable String          serverName,
                                             @PathVariable String          userId,
                                             @PathVariable String          parentZoneGUID,
                                             @PathVariable String          childZoneGUID,
                                             @RequestBody(required = false)
                                                     RelationshipRequestBody requestBody)
    {
        return restAPI.linkZonesInHierarchy(serverName, userId, parentZoneGUID, childZoneGUID, requestBody);
    }


    /**
     * Remove the link between two zones in the zone hierarchy.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones/{parentZoneGUID}/nested-zone/{childZoneGUID}/unlink")

    public VoidResponse unlinkZonesInHierarchy(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          parentZoneGUID,
                                               @PathVariable String          childZoneGUID,
                                               @RequestBody(required = false)
                                                             RelationshipRequestBody requestBody)
    {
        return restAPI.unlinkZonesInHierarchy(serverName, userId, parentZoneGUID, childZoneGUID, requestBody);
    }


    /**
     * Link a governance zone to a governance definition that controls how the assets in the zone should be governed.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones/{zoneGUID}/governed-by/{definitionGUID}/link")

    public VoidResponse linkZoneToGovernanceDefinition(@PathVariable String          serverName,
                                                       @PathVariable String          userId,
                                                       @PathVariable String          zoneGUID,
                                                       @PathVariable String          definitionGUID,
                                                       @RequestBody(required = false)
                                                                     RelationshipRequestBody requestBody)
    {
        return restAPI.linkZoneToGovernanceDefinition(serverName, userId, zoneGUID, definitionGUID, requestBody);
    }


    /**
     * Remove the link between a zone and a governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-zones/{zoneGUID}/governed-by/{definitionGUID}/unlink")

    public VoidResponse unlinkZoneFromGovernanceDefinition(@PathVariable String          serverName,
                                                           @PathVariable String          userId,
                                                           @PathVariable String          zoneGUID,
                                                           @PathVariable String          definitionGUID,
                                                           @RequestBody(required = false)
                                                                         RelationshipRequestBody requestBody)
    {
        return restAPI.unlinkZoneFromGovernanceDefinition(serverName, userId, zoneGUID, definitionGUID, requestBody);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone or
     *  InvalidParameterException zoneGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/governance-zones/{zoneGUID}")

    public GovernanceZoneResponse getGovernanceZoneByGUID(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String zoneGUID)
    {
        return restAPI.getGovernanceZoneByGUID(serverName, userId, zoneGUID);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/governance-zones/name/{qualifiedName}")

    public GovernanceZoneResponse getGovernanceZoneByName(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String qualifiedName)
    {
        return restAPI.getGovernanceZoneByName(serverName, userId, qualifiedName);
    }


    /**
     * Return information about the defined governance zones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the governance zone or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/governance-zones/for-domain")

    public GovernanceZonesResponse getGovernanceZonesForDomain(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @RequestParam int    domainIdentifier,
                                                               @RequestParam int    startFrom,
                                                               @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceZonesForDomain(serverName, userId, domainIdentifier, startFrom, pageSize);
    }


    /**
     * Return information about a specific governance zone and its linked governance definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone linked to the associated governance definitions or
     *  InvalidParameterException zoneGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/governance-zones/{zoneGUID}/with-definitions")

    public GovernanceZoneDefinitionResponse getGovernanceZoneDefinitionByGUID(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String zoneGUID)
    {
        return restAPI.getGovernanceZoneDefinitionByGUID(serverName, userId, zoneGUID);
    }
}
