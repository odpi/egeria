/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.subjectarea.server.SubjectAreaRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaResource provides part of the server-side implementation of the Subject Area OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/subject-area")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Subject Area", description="Valuable expertise about your organization's data and operations is available across your organization.  This expertise will not be there indefinitely as individuals move on to new roles or leave the organization.  Subject areas define the topic areas where the gathering of subject matter expertise is needed to enhance the organization's capability. Subject Area provides APIs for defining the subject areas you need focus on and organizing the resulting knowledge.",
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
    @SecurityRequirement(name = "BearerAuthorization")

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
                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSubjectAreas(serverName, subjectAreaGUID, nestedSubjectAreaGUID, requestBody);
    }


    /**
     * Detach a subject area definition from a hierarchical relationship.
     *
     * @param serverName         name of called server
     * @param parentSubjectAreaGUID  unique identifier of the first subject area definition
     * @param nestedSubjectAreaGUID      unique identifier of the second subject area definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/subject-areas/{parentSubjectAreaGUID}/subject-area-hierarchies/{nestedSubjectAreaGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSubjectAreas",
            description="Detach a nested subject area from a broader subject area definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/subject-area"))

    public VoidResponse detachSubjectAreas(@PathVariable
                                           String                    serverName,
                                           @PathVariable
                                           String parentSubjectAreaGUID,
                                           @PathVariable
                                           String nestedSubjectAreaGUID,
                                           @RequestBody (required = false)
                                               DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSubjectAreas(serverName, parentSubjectAreaGUID, nestedSubjectAreaGUID, requestBody);
    }
}
