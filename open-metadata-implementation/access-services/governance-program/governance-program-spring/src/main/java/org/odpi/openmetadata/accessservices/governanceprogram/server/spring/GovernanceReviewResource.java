/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ElementStubListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionGraphResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricImplementationListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceReviewRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * GovernanceReviewResource reviews the status of the governance program.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Governance Program OMAS",
     description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Governance Program Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceReviewResource
{
    private final GovernanceReviewRESTServices restAPI = new GovernanceReviewRESTServices();

    /**
     * Default constructor
     */
    public GovernanceReviewResource()
    {
    }


    /**
     * Retrieve the certification type by the unique identifier assigned by this service when it was created.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to retrieve
     *
     * @return properties of the certification type or
     *  InvalidParameterException guid or userId is null; guid is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/review/governance-definitions/{definitionGUID}")

    public GovernanceDefinitionResponse getGovernanceDefinitionByGUID(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String definitionGUID)
    {
        return restAPI.getGovernanceDefinitionByGUID(serverName, userId, definitionGUID);
    }


    /**
     * Return the list of governance definitions associated with a particular governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param typeName option type name to restrict retrieval to a specific type
     * @param domainIdentifier identifier of the governance domain - 0 = all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(path = "/review/governance-definitions/{typeName}/for-domain")

    public GovernanceDefinitionListResponse getGovernanceDefinitionsForDomain(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String typeName,
                                                                              @RequestParam int    domainIdentifier,
                                                                              @RequestParam int    startFrom,
                                                                              @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceDefinitionsForDomain(serverName, userId, typeName, domainIdentifier, startFrom, pageSize);
    }


    /**
     * Return the list of governance definitions associated with a unique docId.  In an ideal world, there should be only one.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param typeName option types name to restrict retrieval to a specific type
     * @param docId unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(path = "/review/governance-definitions/{typeName}/for-document-id/{docId}")

    public GovernanceDefinitionListResponse getGovernanceDefinitionsForDocId(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String typeName,
                                                                             @PathVariable String docId,
                                                                             @RequestParam int    startFrom,
                                                                             @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceDefinitionsForDocId(serverName, userId, typeName, docId, startFrom, pageSize);
    }


    /**
     * Return the governance definition associated with a unique identifier and the other governance definitions linked to it.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition
     *
     * @return governance definition and its linked elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(path = "/review/governance-definitions/{governanceDefinitionGUID}/in-context")

    public GovernanceDefinitionGraphResponse getGovernanceDefinitionInContext(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String governanceDefinitionGUID)
    {
        return restAPI.getGovernanceDefinitionInContext(serverName, userId, governanceDefinitionGUID);
    }


    /**
     * Return the list of governance definitions that match the search string - this can be a regular expression.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param typeName option types name to restrict retrieval to a specific type
     * @param requestBody value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @PostMapping(path = "/review/governance-definitions/{typeName}/by-search-string")

    public GovernanceDefinitionListResponse findGovernanceDefinitions(@PathVariable String                  serverName,
                                                                      @PathVariable String                  userId,
                                                                      @PathVariable String                  typeName,
                                                                      @RequestParam int                     startFrom,
                                                                      @RequestParam int                     pageSize,
                                                                      @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceDefinitions(serverName, userId, typeName, startFrom, pageSize, requestBody);
    }


    /**
     * Return details of the metrics for a governance definition along with details of where the measurements are stored
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDefinitionGUID unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of associated metrics and links for retrieving the captured measurements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(path = "/review/governance-definitions/{governanceDefinitionGUID}/metrics-implementation")

    public GovernanceMetricImplementationListResponse getGovernanceDefinitionMetrics(@PathVariable String serverName,
                                                                                     @PathVariable String userId,
                                                                                     @PathVariable String governanceDefinitionGUID,
                                                                                     @RequestParam int    startFrom,
                                                                                     @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceDefinitionMetrics(serverName, userId, governanceDefinitionGUID, startFrom, pageSize);
    }


    /**
     * Return the list of assets that are members of a particular zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneName unique name of the zone to search for
     * @param subTypeName optional asset subtypeName to limit the results
     * @param startFrom where to start from in the list of assets
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for assets in the requested zone or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(path = "/review/governance-zones/{zoneName}/members/{subTypeName}")

    public ElementStubListResponse getGovernanceZoneMembers(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String zoneName,
                                                            @PathVariable String subTypeName,
                                                            @RequestParam int    startFrom,
                                                            @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceZoneMembers(serverName, userId, zoneName, subTypeName, startFrom, pageSize);
    }
}
