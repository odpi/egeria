/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.GlossaryExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import org.springframework.web.bind.annotation.*;


/**
 * GlossaryExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for glossaries.  It matches the GlossaryExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
        externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager/"))

public class GlossaryExchangeResource
{
    private GlossaryExchangeRESTServices restAPI = new GlossaryExchangeRESTServices();

    /**
     * Default constructor
     */
    public GlossaryExchangeResource()
    {
    }


    /* ========================================================
     * The glossary is the root object for a glossary.  All of the glossary's categories and terms are anchored
     * to it so that if the glossary is deleted, all of the categories and terms within it are also deleted.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries")

    public GUIDResponse createGlossary(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @RequestBody  GlossaryRequestBody requestBody)
    {
        return restAPI.createGlossary(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     *
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/from-template/{templateGUID}")

    public GUIDResponse createGlossaryFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}")

    public VoidResponse updateGlossary(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              glossaryGUID,
                                       @RequestBody  GlossaryRequestBody requestBody)
    {
        return restAPI.updateGlossary(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/remove")

    public VoidResponse removeGlossary(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              glossaryGUID,
                                       @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeGlossary(serverName, userId, glossaryGUID, requestBody);
    }
}
