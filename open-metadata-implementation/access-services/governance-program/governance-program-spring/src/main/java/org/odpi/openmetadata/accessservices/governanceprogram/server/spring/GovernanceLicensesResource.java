/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.server.LicenseRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * GovernanceLicensesResource sets up the license types that are part of an organization governance.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Metadata Access Server: Governance Program OMAS",
     description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceLicensesResource
{
    private final LicenseRESTServices restAPI = new LicenseRESTServices();

    /**
     * Default constructor
     */
    public GovernanceLicensesResource()
    {
    }



    /* ========================================
     * License Types
     */

    /**
     * Create a description of the license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody license properties and initial status
     *
     * @return unique identifier of new definition or
     *  InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/license-types")

    public GUIDResponse createLicenseType(@PathVariable String                          serverName,
                                          @PathVariable String                          userId,
                                          @RequestBody NewGovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.createLicenseType(serverName, userId, requestBody);
    }


    /**
     * Update the properties of the license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody license properties
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/license-types/{licenseTypeGUID}/update")

    public VoidResponse updateLicenseType(@PathVariable String                          serverName,
                                          @PathVariable String                          userId,
                                          @PathVariable String                          licenseTypeGUID,
                                          @RequestParam boolean                         isMergeUpdate,
                                          @RequestBody NewGovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.updateLicenseType(serverName, userId, licenseTypeGUID, isMergeUpdate, requestBody);
    }


    /**
     * Delete the properties of the license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to delete
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/license-types/{licenseTypeGUID}/delete")

    public VoidResponse deleteLicenseType(@PathVariable String                    serverName,
                                          @PathVariable String                    userId,
                                          @PathVariable String                    licenseTypeGUID,
                                          @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteLicenseType(serverName, userId, licenseTypeGUID, requestBody);
    }


    /**
     * Retrieve the license type by the unique identifier assigned by this service when it was created.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to retrieve
     *
     * @return properties of the license type or
     *  InvalidParameterException guid or userId is null; guid is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/license-types/{licenseTypeGUID}")

    public LicenseTypeResponse getLicenseTypeByGUID(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String licenseTypeGUID)
    {
        return restAPI.getLicenseTypeByGUID(serverName, userId, licenseTypeGUID);
    }


    /**
     * Retrieve the license type by its assigned unique document identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching license type or
     *  InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/license-types/by-document-id/{documentIdentifier}")

    public LicenseTypeResponse getLicenseTypeByDocId(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String documentIdentifier)
    {
        return restAPI.getLicenseTypeByDocId(serverName, userId, documentIdentifier);
    }


    /**
     * Retrieve all the license types for a particular title.  The title can include regEx wildcards.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody short description of the license
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching license types (null if no matching elements) or
     *  InvalidParameterException title or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/license-types/by-title")

    public LicenseTypesResponse getLicenseTypesByTitle(@PathVariable String                  serverName,
                                                       @PathVariable String                  userId,
                                                       @RequestParam int                     startFrom,
                                                       @RequestParam int                     pageSize,
                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.getLicenseTypesByTitle(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve all the license type definitions for a specific governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching license type definitions or
     *  InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/license-types/by-domain/{domainIdentifier}")

    public LicenseTypesResponse getLicenseTypeByDomainId(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable int    domainIdentifier,
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
    {
        return restAPI.getLicenseTypeByDomainId(serverName, userId, domainIdentifier, startFrom, pageSize);
    }


    /* =======================================
     * Licenses
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element being licensed
     * @param licenseTypeGUID unique identifier for the license type
     * @param requestBody the properties of the license
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/{elementGUID}/license-types/{licenseTypeGUID}/license")

    public GUIDResponse licenseElement(@PathVariable String                  serverName,
                                       @PathVariable String                  userId,
                                       @PathVariable String                  elementGUID,
                                       @PathVariable String                  licenseTypeGUID,
                                       @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.licenseElement(serverName, userId, elementGUID, licenseTypeGUID, requestBody);
    }


    /**
     * Update the properties of a license.  Remember to include the licenseId in the properties if the element has multiple
     * licenses for the same license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param requestBody the properties of the license
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/licenses/{licenseGUID}/update")

    public VoidResponse updateLicense(@PathVariable String                  serverName,
                                      @PathVariable String                  userId,
                                      @PathVariable String                  licenseGUID,
                                      @RequestParam boolean                 isMergeUpdate,
                                      @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.updateLicense(serverName, userId, licenseGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the license for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/licenses/{licenseGUID}/delete")

    public VoidResponse unlicenseElement(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  licenseGUID,
                                         @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.unlicenseElement(serverName, userId, licenseGUID, requestBody);
    }



    /**
     * Return information about the elements linked to a license.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseGUID unique identifier for the license
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/elements/licenses/{licenseGUID}")

    public RelatedElementsResponse  getLicensedElements(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String licenseGUID,
                                                        @RequestParam int    startFrom,
                                                        @RequestParam int    pageSize)
    {
        return restAPI.getLicensedElements(serverName, userId, licenseGUID, startFrom, pageSize);
    }


    /**
     * Return information about the licenses linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier for the license
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/elements/{elementGUID}/licenses")

    public RelatedElementsResponse getLicenses(@PathVariable String serverName,
                                               @PathVariable String userId,
                                               @PathVariable String elementGUID,
                                               @RequestParam int    startFrom,
                                               @RequestParam int    pageSize)
    {
        return restAPI.getLicenses(serverName, userId, elementGUID, startFrom, pageSize);
    }
}
