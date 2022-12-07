/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewConfigRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The GlossaryAuthorRESTServicesInstance provides the org.odpi.openmetadata.viewservices.glossaryauthor.server  implementation of the Glossary Author Open Metadata
 * View Service (OMVS) for glossaries.  This interface provides glossary authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/configs")

@Tag(name="Glossary Author OMVS",
     description="Develop a definition of a subject area by authoring glossaries, including terms for use by a user interface.",
     externalDocs=@ExternalDocumentation(description="Glossary Author View Services (OMVS)",
                                                                                                                                                                                                  url="https://egeria-project.org/services/omvs/glossary-author/overview"))

public class GlossaryAuthorViewGonfigRESTResource {

    private final GlossaryAuthorViewConfigRESTServices restAPI = new GlossaryAuthorViewConfigRESTServices();


    /**
     * Default constructor
     */
    public GlossaryAuthorViewGonfigRESTResource() {
    }


    /**
     * Get a config.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       identifier of the configuration to get
     * @return response which when successful contains the config
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata repository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Config> getGlossary(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String guid) {
        return restAPI.getConfig(serverName, userId, guid);

    }
}
