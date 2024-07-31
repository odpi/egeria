/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceDomainRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * GovernanceDomainsResource sets up the governance domains that are part of an organization governance.
 * Each governance domain describes a focus for governance.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Metadata Access Server: Governance Program OMAS",
     description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceDomainsResource
{
    private final GovernanceDomainRESTServices restAPI = new GovernanceDomainRESTServices();

    /**
     * Default constructor
     */
    public GovernanceDomainsResource()
    {
    }


    /* =====================================================================================================================
     * The GovernanceDomainSet entity is the top level element in a collection of related governance domains.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Domain Set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets")

    public GUIDResponse createGovernanceDomainSet(@PathVariable String                   serverName,
                                                  @PathVariable String                   userId,
                                                  @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createGovernanceDomainSet(serverName, userId, requestBody);
    }


    /**
     * Update the metadata element representing a Governance Domain Set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param requestBody new properties for this element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/{governanceDomainSetGUID}/update")

    public VoidResponse updateGovernanceDomainSet(@PathVariable String                        serverName,
                                                  @PathVariable String                        userId,
                                                  @PathVariable String                        governanceDomainSetGUID,
                                                  @RequestBody  ReferenceableRequestBody      requestBody)
    {
        return restAPI.updateGovernanceDomainSet(serverName, userId, governanceDomainSetGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governanceDomainSet.  The governance domains are not deleted.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/{governanceDomainSetGUID}/delete")

    public VoidResponse removeGovernanceDomainSet(@PathVariable String          serverName,
                                                  @PathVariable String          userId,
                                                  @PathVariable String          governanceDomainSetGUID,
                                                  @RequestBody(required = false)
                                                                ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeGovernanceDomainSet(serverName, userId, governanceDomainSetGUID, requestBody);
    }


    /**
     * Retrieve the list of governanceDomainSet metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/by-search-string")

    public GovernanceDomainSetsResponse findGovernanceDomainSets(@PathVariable String                  serverName,
                                                                 @PathVariable String                  userId,
                                                                 @RequestParam int                     startFrom,
                                                                 @RequestParam int                     pageSize,
                                                                 @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceDomainSets(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governanceDomainSet metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/by-name")

    public GovernanceDomainSetsResponse getGovernanceDomainSetsByName(@PathVariable String          serverName,
                                                                      @PathVariable String          userId,
                                                                      @RequestParam int             startFrom,
                                                                      @RequestParam int             pageSize,
                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceDomainSetsByName(serverName, userId, startFrom, pageSize, requestBody);
    }




    /**
     * Retrieve the governanceDomainSet metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-domain-sets/{governanceDomainSetGUID}")

    public GovernanceDomainSetResponse getGovernanceDomainSetByGUID(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @PathVariable String governanceDomainSetGUID)
    {
        return restAPI.getGovernanceDomainSetByGUID(serverName, userId, governanceDomainSetGUID);
    }


    /* =====================================================================================================================
     * A Governance Domain describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties about the Governance Domain to store
     *
     * @return unique identifier of the new Governance Domain or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/governance-domains")

    public GUIDResponse createGovernanceDomain(@PathVariable String                     serverName,
                                               @PathVariable String                     userId,
                                               @RequestBody  ReferenceableRequestBody   requestBody)
    {
        return restAPI.createGovernanceDomain(serverName, userId, requestBody);
    }


    /**
     * Update the metadata element representing a Governance Domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domains/{governanceDomainGUID}/update")

    public VoidResponse updateGovernanceDomain(@PathVariable String                     serverName,
                                               @PathVariable String                     userId,
                                               @PathVariable String                     governanceDomainGUID,
                                               @RequestBody  ReferenceableRequestBody   requestBody)
    {
        return restAPI.updateGovernanceDomain(serverName, userId, governanceDomainGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a Governance Domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domains/{governanceDomainGUID}/delete")

    public VoidResponse deleteGovernanceDomain(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          governanceDomainGUID,
                                               @RequestBody(required = false)
                                                       ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteGovernanceDomain(serverName, userId, governanceDomainGUID, requestBody);
    }



    /**
     * Create a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     * @param requestBody relationship request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/{governanceDomainSetGUID}/governance-domains/{governanceDomainGUID}")

    public VoidResponse addDomainToSet(@PathVariable String          serverName,
                                       @PathVariable String          userId,
                                       @PathVariable String          governanceDomainSetGUID,
                                       @PathVariable String          governanceDomainGUID,
                                       @RequestBody(required = false)
                                               RelationshipRequestBody requestBody)
    {
        return restAPI.addDomainToSet(serverName, userId, governanceDomainSetGUID, governanceDomainGUID, requestBody);
    }



    /**
     * Remove a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     * @param requestBody relationship request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain-sets/{governanceDomainSetGUID}/governance-domains/{governanceDomainGUID}/delete")

    public VoidResponse removeDomainFromSet(@PathVariable String          serverName,
                                            @PathVariable String          userId,
                                            @PathVariable String          governanceDomainSetGUID,
                                            @PathVariable String          governanceDomainGUID,
                                            @RequestBody(required = false)
                                                    RelationshipRequestBody requestBody)
    {
        return restAPI.removeDomainFromSet(serverName, userId, governanceDomainSetGUID, governanceDomainGUID, requestBody);

    }


    /**
     * Retrieve the list of Governance Domain metadata elements defined for the governance program.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-domains")

    public GovernanceDomainsResponse getGovernanceDomains(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceDomains(serverName, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the list of Governance Domain metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain/by-search-string")

    public GovernanceDomainsResponse findGovernanceDomains(@PathVariable String                  serverName,
                                                           @PathVariable String                  userId,
                                                           @RequestParam int                     startFrom,
                                                           @RequestParam int                     pageSize,
                                                           @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceDomains(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Return the list of governance domain sets that a governance domain belong.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the governance domain to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the sets associated with the requested governanceDomainSet or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-domain-sets/by-governance-domains/{governanceDomainGUID}")

    public GovernanceDomainSetsResponse getSetsForGovernanceDomain(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String governanceDomainGUID,
                                                                   @RequestParam int    startFrom,
                                                                   @RequestParam int    pageSize)
    {
        return restAPI.getSetsForGovernanceDomain(serverName, userId, governanceDomainGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of Governance Domain metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-domain/by-name")

    public GovernanceDomainsResponse getGovernanceDomainsByName(@PathVariable String          serverName,
                                                                @PathVariable String          userId,
                                                                @RequestParam int             startFrom,
                                                                @RequestParam int             pageSize,
                                                                @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceDomainsByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied unique identifier assigned when the domain description was stored in
     * the metadata repository.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-domains/{governanceDomainGUID}")

    public GovernanceDomainResponse getGovernanceDomainByGUID(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String governanceDomainGUID)
    {
        return restAPI.getGovernanceDomainByGUID(serverName, userId, governanceDomainGUID);
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied domain identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier used to identify the domain
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-domains/by-identifier/{domainIdentifier}")

    public GovernanceDomainResponse getGovernanceDomainByIdentifier(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @PathVariable int    domainIdentifier)
    {
        return restAPI.getGovernanceDomainByIdentifier(serverName, userId, domainIdentifier);
    }
}
