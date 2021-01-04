/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SecurityOfficerOMASAPIResponse;
import org.odpi.openmetadata.accessservices.securityofficer.server.services.SecurityOfficerRESTService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/security-officer/users/{userId}")

@Tag(name="Security Officer OMAS", description="The Security Officer Open Metadata Access Service (OMAS) provides access to metadata for policy enforcement frameworks such as Apache Ranger.", externalDocs=@ExternalDocumentation(description="Security Officer Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/security-officer/"))

public class SecurityOfficerOMASResource {

    private SecurityOfficerRESTService service = new SecurityOfficerRESTService();

    /**
     * Returns the security tag for the given schema element
     *
     * @param serverName      name of the server instances for this request
     * @param userId          String - userId of user making request.
     * @param schemaElementId unique identifier of the schema element
     */
    @GetMapping( path = "/security-tag/element/{schemaElementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SecurityOfficerOMASAPIResponse getSecurityTagBySchemaElementIdentifier(@PathVariable String serverName, @PathVariable String userId, @PathVariable String schemaElementId) {
        return service.getSecurityTagBySchemaElementId(serverName, userId, schemaElementId);
    }

    /**
     * Save or update the security tag for the given schema element
     *
     * @param serverName                name of the server instances for this request
     * @param userId                    String - userId of user making request.
     * @param securityTagClassification security tag assigned to the schema element
     * @param schemaElementId           unique identifier of the schema element
     */
    @PostMapping( path = "/security-tag/element/{schemaElementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SecurityOfficerOMASAPIResponse getSecurityTagBySchemaElementIdentifier(@PathVariable String serverName, @PathVariable String userId,
                                                                                  @PathVariable String schemaElementId,
                                                                                  @RequestBody SecurityClassification securityTagClassification) {
        return service.updateSecurityTag(serverName, userId, schemaElementId, securityTagClassification);
    }

    /**
     * Delete the security tag for the given schema element
     *
     * @param serverName      name of the server instances for this request
     * @param userId          String - userId of user making request.
     * @param schemaElementId unique identifier of the schema element
     */
    @DeleteMapping( path = "/security-tag/element/{schemaElementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SecurityOfficerOMASAPIResponse deleteSecurityTagBySchemaElementIdentifier(@PathVariable String serverName, @PathVariable String userId,
                                                                                     @PathVariable String schemaElementId) {
        return service.deleteSecurityTag(serverName, userId, schemaElementId);
    }

    /**
     * Returns the security tags available
     *
     * @param serverName name of the server instances for this request
     * @param userId     String - userId of user making request.
     */
    @GetMapping( path = "/security-tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public SecurityOfficerOMASAPIResponse getSecurityTagBySchemaElementIdentifier(@PathVariable String serverName, @PathVariable String userId) {
        return service.getSecurityTags(serverName, userId);
    }
}
