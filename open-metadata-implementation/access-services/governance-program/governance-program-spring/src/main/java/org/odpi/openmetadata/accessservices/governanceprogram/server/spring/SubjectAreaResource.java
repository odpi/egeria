/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.SubjectAreaDefinitionResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.SubjectAreaListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.SubjectAreaResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.server.SubjectAreaRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The SubjectAreasResource provides a Spring based server-side REST API
 * that supports the SubjectAreasInterface.   It delegates each request to the
 * SubjectAreaRESTServices.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Governance Program OMAS", description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape."
        , externalDocs=@ExternalDocumentation(description="Governance Program Open Metadata Access Service (OMAS)",
                                              url="https://odpi.github.io/egeria-docs/services/omas/governance-program/overview/"))

public class SubjectAreaResource
{
    private SubjectAreaRESTServices  restAPI = new SubjectAreaRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaResource()
    {
    }


    /**
     * Create a definition of a subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody other properties for a subject area
     *
     * @return unique identifier of the new subject area or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas")

    public GUIDResponse createSubjectArea(@PathVariable String                serverName,
                                          @PathVariable String                userId,
                                          @RequestBody  SubjectAreaProperties requestBody)
    {
        return restAPI.createSubjectArea(serverName, userId, requestBody);
    }


    /**
     * Update the definition of a subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}")

    public VoidResponse updateSubjectArea(@PathVariable String                serverName,
                                          @PathVariable String                userId,
                                          @PathVariable String                subjectAreaGUID,
                                          @RequestParam boolean               isMergeUpdate,
                                          @RequestBody  SubjectAreaProperties requestBody)
    {
        return restAPI.updateSubjectArea(serverName, userId, subjectAreaGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the definition of a subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/delete")

    public VoidResponse deleteSubjectArea(@PathVariable String          serverName,
                                          @PathVariable String          userId,
                                          @PathVariable String          subjectAreaGUID,
                                          @RequestBody(required = false)
                                                        NullRequestBody requestBody)
    {
        return restAPI.deleteSubjectArea(serverName, userId, subjectAreaGUID, requestBody);
    }


    /**
     * Link two related subject areas together as part of a hierarchy.
     * A subject area can only have one parent but many child subject areas.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas/{parentSubjectAreaGUID}/nested-subject area/{childSubjectAreaGUID}/link")

    public VoidResponse linkSubjectAreasInHierarchy(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @PathVariable String          parentSubjectAreaGUID,
                                                    @PathVariable String          childSubjectAreaGUID,
                                                    @RequestBody(required = false)
                                                                  NullRequestBody requestBody)
    {
        return restAPI.linkSubjectAreasInHierarchy(serverName, userId, parentSubjectAreaGUID, childSubjectAreaGUID, requestBody);
    }


    /**
     * Remove the link between two subject areas in the subject area hierarchy.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas/{parentSubjectAreaGUID}/nested-subject area/{childSubjectAreaGUID}/unlink")

    public VoidResponse unlinkSubjectAreasInHierarchy(@PathVariable String          serverName,
                                                      @PathVariable String          userId,
                                                      @PathVariable String          parentSubjectAreaGUID,
                                                      @PathVariable String          childSubjectAreaGUID,
                                                      @RequestBody(required = false)
                                                                    NullRequestBody requestBody)
    {
        return restAPI.unlinkSubjectAreasInHierarchy(serverName, userId, parentSubjectAreaGUID, childSubjectAreaGUID, requestBody);
    }


    /**
     * Link a subject area to a governance definition that controls how the assets in the subject area should be governed.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of the subject area
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/governed-by/{definitionGUID}/link")

    public VoidResponse linkSubjectAreaToGovernanceDefinition(@PathVariable String          serverName,
                                                              @PathVariable String          userId,
                                                              @PathVariable String          subjectAreaGUID,
                                                              @PathVariable String          definitionGUID,
                                                              @RequestBody(required = false)
                                                                            NullRequestBody requestBody)
    {
        return restAPI.linkSubjectAreaToGovernanceDefinition(serverName, userId, subjectAreaGUID, definitionGUID, requestBody);
    }


    /**
     * Remove the link between a subject area and a governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of the subject area
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/governed-by/{definitionGUID}/unlink")

    public VoidResponse unlinkSubjectAreaFromGovernanceDefinition(@PathVariable String          serverName,
                                                                  @PathVariable String          userId,
                                                                  @PathVariable String          subjectAreaGUID,
                                                                  @PathVariable String          definitionGUID,
                                                                  @RequestBody(required = false)
                                                                                NullRequestBody requestBody)
    {
        return restAPI.unlinkSubjectAreaFromGovernanceDefinition(serverName, userId, subjectAreaGUID, definitionGUID, requestBody);
    }


    /**
     * Return information about a specific subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area or
     *  InvalidParameterException subjectAreaGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/subject-areas/{subjectAreaGUID}")

    public SubjectAreaResponse getSubjectAreaByGUID(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String subjectAreaGUID)
    {
        return restAPI.getSubjectAreaByGUID(serverName, userId, subjectAreaGUID);
    }


    /**
     * Return information about a specific subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the subject area
     *
     * @return properties of the subject area or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/subject-areas/name/{qualifiedName}")

    public SubjectAreaResponse getSubjectAreaByName(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String qualifiedName)
    {
        return restAPI.getSubjectAreaByName(serverName, userId, qualifiedName);
    }


    /**
     * Return information about the defined subject areas.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/subject-areas/for-domain")

    public SubjectAreaListResponse getSubjectAreasForDomain(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @RequestParam int    domainIdentifier,
                                                            @RequestParam int    startFrom,
                                                            @RequestParam int    pageSize)
    {
        return restAPI.getSubjectAreasForDomain(serverName, userId, domainIdentifier, startFrom, pageSize);
    }


    /**
     * Return information about a specific subject area and its linked governance definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area linked to the associated governance definitions or
     *  InvalidParameterException subjectAreaGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/subject-areas/{subjectAreaGUID}/with-definitions")

    public SubjectAreaDefinitionResponse getSubjectAreaDefinitionByGUID(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String subjectAreaGUID)
    {
        return restAPI.getSubjectAreaDefinitionByGUID(serverName, userId, subjectAreaGUID);
    }
}
