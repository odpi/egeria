/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.CertificationTypeListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.CertificationTypeResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.server.CertificationRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * GovernanceCertificationsResource sets up the certification types that are part of an organization governance.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Governance Program OMAS",
     description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Governance Program Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceCertificationsResource
{
    private final CertificationRESTServices restAPI = new CertificationRESTServices();

    /**
     * Default constructor
     */
    public GovernanceCertificationsResource()
    {
    }



    /* ========================================
     * Certification Types
     */

    /**
     * Create a description of the certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody certification properties and initial status
     *
     * @return unique identifier of new definition or
     *  InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certification-types")

    public GUIDResponse createCertificationType(@PathVariable String                          serverName,
                                                @PathVariable String                          userId,
                                                @RequestBody  GovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.createCertificationType(serverName, userId, requestBody);
    }


    /**
     * Update the properties of the certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody certification properties
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certification-types/{certificationTypeGUID}/update")

    public VoidResponse updateCertificationType(@PathVariable String                          serverName,
                                                @PathVariable String                          userId,
                                                @PathVariable String                          certificationTypeGUID,
                                                @RequestParam boolean                         isMergeUpdate,
                                                @RequestBody  GovernanceDefinitionRequestBody requestBody)
    {
        return restAPI.updateCertificationType(serverName, userId, certificationTypeGUID, isMergeUpdate, requestBody);
    }


    /**
     * Delete the properties of the certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to delete
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certification-types/{certificationTypeGUID}/delete")

    public VoidResponse deleteCertificationType(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @PathVariable String                    certificationTypeGUID,
                                                @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteCertificationType(serverName, userId, certificationTypeGUID, requestBody);
    }


    /**
     * Retrieve the certification type by the unique identifier assigned by this service when it was created.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to retrieve
     *
     * @return properties of the certification type or
     *  InvalidParameterException guid or userId is null; guid is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/certification-types/{certificationTypeGUID}")

    public CertificationTypeResponse getCertificationTypeByGUID(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable String certificationTypeGUID)
    {
        return restAPI.getCertificationTypeByGUID(serverName, userId, certificationTypeGUID);
    }


    /**
     * Retrieve the certification type by its assigned unique document identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching certification type or
     *  InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/certification-types/by-document-id/{documentIdentifier}")

    public CertificationTypeResponse getCertificationTypeByDocId(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String documentIdentifier)
    {
        return restAPI.getCertificationTypeByDocId(serverName, userId, documentIdentifier);
    }


    /**
     * Retrieve all the certification types for a particular title.  The title can include regEx wildcards.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody short description of the certification
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching certification types (null if no matching elements) or
     *  InvalidParameterException title or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certification-types/by-title")

    public CertificationTypeListResponse getCertificationTypesByTitle(@PathVariable String                  serverName,
                                                                      @PathVariable String                  userId,
                                                                      @RequestParam int                     startFrom,
                                                                      @RequestParam int                     pageSize,
                                                                      @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.getCertificationTypesByTitle(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve all the certification type definitions for a specific governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching certification type definitions or
     *  InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/certification-types/by-domain/{domainIdentifier}")

    public CertificationTypeListResponse getCertificationTypeByDomainId(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable int    domainIdentifier,
                                                                        @RequestParam int    startFrom,
                                                                        @RequestParam int    pageSize)
    {
        return restAPI.getCertificationTypeByDomainId(serverName, userId, domainIdentifier, startFrom, pageSize);
    }


    /* =======================================
     * Certifications
     */

    /**
     * Link an element to a certification type and include details of the certification in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param requestBody the properties of the certification
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/{elementGUID}/certification-types/{certificationTypeGUID}/certify")

    public GUIDResponse certifyElement(@PathVariable String                  serverName,
                                       @PathVariable String                  userId,
                                       @PathVariable String                  elementGUID,
                                       @PathVariable String                  certificationTypeGUID,
                                       @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.certifyElement(serverName, userId, elementGUID, certificationTypeGUID, requestBody);
    }


    /**
     * Update the properties of a certification.  Remember to include the certificationId in the properties if the element has multiple
     * certifications for the same certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param certificationGUID unique identifier for the certification relationship
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param requestBody the properties of the certification
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certifications/{certificationGUID}/update")

    public VoidResponse updateCertification(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  certificationGUID,
                                            @RequestParam boolean                 isMergeUpdate,
                                            @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.updateCertification(serverName, userId, certificationGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the certification for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param certificationGUID unique identifier for the certification relationship
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certifications/{certificationGUID}/delete")

    public VoidResponse decertifyElement(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  certificationGUID,
                                         @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.decertifyElement(serverName, userId, certificationGUID, requestBody);
    }



    /**
     * Return information about the elements linked to a certification.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param certificationGUID unique identifier for the certification
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the certification or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/elements/certifications/{certificationGUID}")

    public RelatedElementListResponse getCertifiedElements(@PathVariable String serverName,
                                                           @PathVariable String userId,
                                                           @PathVariable String certificationGUID,
                                                           @RequestParam int    startFrom,
                                                           @RequestParam int    pageSize)
    {
        return restAPI.getCertifiedElements(serverName, userId, certificationGUID, startFrom, pageSize);
    }


    /**
     * Return information about the certifications linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier for the certification
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the certification or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping (path = "/elements/{elementGUID}/certifications")

    public RelatedElementListResponse getCertifications(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String elementGUID,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.getCertifications(serverName, userId, elementGUID, startFrom, pageSize);
    }
}
