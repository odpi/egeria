/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.viewservices.subjectarea.server.SubjectAreaRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaResource provides part of the server-side implementation of the Subject Area OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/subject-area")

@Tag(name="API: Subject Area OMVS", description="The Subject Area OMVS provides APIs for building schemas for new data assets.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/subject-area/overview/"))

public class SubjectAreaResource
{
    private final SubjectAreaRESTServices restAPI = new SubjectAreaRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaResource()
    {
    }


    /**
     * Create a subject area definition.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the subject area definition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subject-areas")

    @Operation(summary="createSubjectArea",
            description="Create a subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public GUIDResponse createSubjectArea(@PathVariable
                                            String                               serverName,
                                            @RequestBody (required = false)
                                            NewElementRequestBody requestBody)
    {
        return restAPI.createSubjectArea(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a subject area definition using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/subject-areas/from-template")
    @Operation(summary="createSubjectAreaFromTemplate",
            description="Create a new metadata element to represent a subject area definition using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public GUIDResponse createSubjectAreaFromTemplate(@PathVariable
                                                        String              serverName,
                                                        @RequestBody (required = false)
                                                        TemplateRequestBody requestBody)
    {
        return restAPI.createSubjectAreaFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a subject area definition.
     *
     * @param serverName         name of called server.
     * @param subjectAreaGUID unique identifier of the subject area definition (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/update")
    @Operation(summary="updateSubjectArea",
            description="Update the properties of a subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public VoidResponse updateSubjectArea(@PathVariable
                                            String                                  serverName,
                                            @PathVariable
                                            String                                  subjectAreaGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                                 replaceAllProperties,
                                            @RequestBody (required = false)
                                            UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSubjectArea(serverName, subjectAreaGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a nested subject area to a broader subject area definition.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID  unique identifier of the first subject area definition
     * @param nestedSubjectAreaGUID      unique identifier of the second subject area definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/subject-area-hierarchies/{nestedSubjectAreaGUID}/attach")
    @Operation(summary="linkSubjectAreas",
            description="Attach a nested subject area to a broader subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public VoidResponse linkSubjectAreas(@PathVariable
                                            String                  serverName,
                                            @PathVariable
                                            String                  subjectAreaGUID,
                                            @PathVariable
                                            String                  nestedSubjectAreaGUID,
                                            @RequestBody (required = false)
                                            RelationshipRequestBody requestBody)
    {
        return restAPI.linkSubjectAreas(serverName, subjectAreaGUID, nestedSubjectAreaGUID, requestBody);
    }


    /**
     * Detach a data field from a subject area definition.
     *
     * @param serverName         name of called server
     * @param parentSubjectAreaGUID  unique identifier of the first subject area definition
     * @param memberDataFieldGUID      unique identifier of the second subject area definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subject-areas/{parentSubjectAreaGUID}/subject-area-hierarchies/{memberDataFieldGUID}/detach")
    @Operation(summary="detachSubjectAreas",
            description="Detach a nested subject area from a broader subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public VoidResponse detachSubjectAreas(@PathVariable
                                              String                    serverName,
                                              @PathVariable
                                              String parentSubjectAreaGUID,
                                              @PathVariable
                                              String memberDataFieldGUID,
                                              @RequestBody (required = false)
                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSubjectAreas(serverName, parentSubjectAreaGUID, memberDataFieldGUID, requestBody);
    }


    /**
     * Delete a subject area definition.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID  unique identifier of the element to delete
     * @param cascadedDelete can subject area definitions be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/delete")
    @Operation(summary="deleteSubjectArea",
            description="Delete a subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public VoidResponse deleteSubjectArea(@PathVariable
                                            String                    serverName,
                                            @PathVariable
                                            String                    subjectAreaGUID,
                                            @RequestParam(required = false, defaultValue = "false")
                                            boolean                   cascadedDelete,
                                            @RequestBody (required = false)
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteSubjectArea(serverName, subjectAreaGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of subject area definitions with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/subject-areas/by-name")
    @Operation(summary="getSubjectAreasByName",
            description="Returns the list of subject area definitions with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public SubjectAreasResponse getSubjectAreasByName(@PathVariable
                                                          String            serverName,
                                                          @RequestParam (required = false, defaultValue = "0")
                                                          int                     startFrom,
                                                          @RequestParam (required = false, defaultValue = "0")
                                                          int                     pageSize,
                                                          @RequestBody (required = false)
                                                          FilterRequestBody requestBody)
    {
        return restAPI.getSubjectAreasByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of subject area definition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/subject-areas/by-search-string")
    @Operation(summary="findSubjectAreas",
            description="Retrieve the list of subject area definition metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public SubjectAreasResponse findSubjectAreas(@PathVariable
                                                     String                  serverName,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                     int                     startFrom,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                     int                     pageSize,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 startsWith,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 endsWith,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 ignoreCase,
                                                     @RequestBody (required = false)
                                                     FilterRequestBody requestBody)
    {
        return restAPI.findSubjectAreas(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific subject area definition.
     *
     * @param serverName name of the service to route the request to
     * @param subjectAreaGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/subject-areas/{subjectAreaGUID}/retrieve")
    @Operation(summary="getSubjectAreaByGUID",
            description="Return the properties of a specific subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public SubjectAreaResponse getSubjectAreaByGUID(@PathVariable
                                                        String             serverName,
                                                        @PathVariable
                                                        String             subjectAreaGUID,
                                                        @RequestBody (required = false)
                                                        AnyTimeRequestBody requestBody)
    {
        return restAPI.getSubjectAreaByGUID(serverName, subjectAreaGUID, requestBody);
    }
}
