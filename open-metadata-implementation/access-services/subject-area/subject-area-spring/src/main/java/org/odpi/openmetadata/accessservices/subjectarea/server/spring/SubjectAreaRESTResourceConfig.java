/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaConfigRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides term authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs = @ExternalDocumentation(description = "Subject Area Open Metadata Access Service (OMAS)", url = "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))
public class SubjectAreaRESTResourceConfig {
    private final SubjectAreaConfigRESTServices restAPI = new SubjectAreaConfigRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaRESTResourceConfig() {

    }

    /**
     * Get the config
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @return response which when successful contains the configuration
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/configs/current")
    public SubjectAreaOMASAPIResponse<Config> getConfig(@PathVariable String serverName,
                                                        @PathVariable String userId) {
        return restAPI.getConfig(serverName, userId);
    }


}
