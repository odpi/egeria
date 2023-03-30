/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SecurityTagsProperties;
import org.odpi.openmetadata.accessservices.assetmanager.server.StewardshipExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The StewardshipExchangeResource provides part of the server-side implementation of the Asset Manager Open Metadata
 * Assess Service (OMAS).  This interface provides the ability to add governance related classifications and relationship to elements.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/asset-manager/overview/"))

public class StewardshipExchangeResource
{
    private final StewardshipExchangeRESTServices restAPI = new StewardshipExchangeRESTServices();

    /**
     * Default constructor
     */
    public StewardshipExchangeResource()
    {
    }
    

    /**
     * Add or replace the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags")

    public VoidResponse addSecurityTags(@PathVariable String                 serverName,
                                        @PathVariable String                 userId,
                                        @PathVariable String                 elementGUID,
                                        @RequestBody  SecurityTagsProperties requestBody)
    {
        return restAPI.addSecurityTags(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags/remove")

    public VoidResponse removeSecurityTags(@PathVariable String          serverName,
                                           @PathVariable String          userId,
                                           @PathVariable String          elementGUID,
                                           @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeSecurityTags(serverName, userId, elementGUID, requestBody);
    }
}
