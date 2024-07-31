/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.accessservices.stewardshipaction.server.StewardshipActionRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The StewardshipActionResource provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/stewardship-action/users/{userId}")

@Tag(name="Metadata Access Server: Stewardship Action OMAS", description="The Stewardship Action OMAS provides APIs and events for tools and applications focused on resolving issues detected in the data landscape.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/stewardship-action/overview/"))

public class StewardshipActionResource
{
    private final StewardshipActionRESTServices restAPI = new StewardshipActionRESTServices();

    /**
     * Default constructor
     */
    public StewardshipActionResource()
    {
    }


    /**
     * Return the connection object for the Stewardship Action OMAS's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /*
     * ==============================================
     * DuplicateManagementInterface
     * ==============================================
     */

    /**
     * Create a simple relationship between two elements.  These elements must be of the same type.  If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{element1GUID}/peer-duplicate-of/{element2GUID}")

    public VoidResponse linkElementsAsDuplicates(@PathVariable String                serverName,
                                                 @PathVariable String                userId,
                                                 @PathVariable String                element1GUID,
                                                 @PathVariable String                element2GUID,
                                                 @RequestBody  DuplicatesRequestBody requestBody)
    {
        return restAPI.linkElementsAsDuplicates(serverName, userId, element1GUID, element2GUID, requestBody);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{element1GUID}/peer-duplicate-of/{element2GUID}/delete")

    public VoidResponse unlinkElementsAsDuplicates(@PathVariable String          serverName,
                                                   @PathVariable String          userId,
                                                   @PathVariable String          element1GUID,
                                                   @PathVariable String          element2GUID,
                                                   @RequestBody(required = false)
                                                                 NullRequestBody requestBody)
    {
        return restAPI.unlinkElementsAsDuplicates(serverName, userId, element1GUID, element2GUID, requestBody);
    }


    /**
     * Classify an element as a known duplicate.  This will mean that it is included in duplicate processing during metadata retrieval requests.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/known-duplicate")

    public VoidResponse markElementAsKnownDuplicate(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @PathVariable String          elementGUID,
                                                    @RequestBody(required = false)
                                                                  NullRequestBody requestBody)
    {
        return restAPI.markElementAsKnownDuplicate(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Remove the classification that identifies this element as a known duplicate.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/known-duplicate/delete")

    public VoidResponse unmarkElementAsKnownDuplicate(@PathVariable String          serverName,
                                                      @PathVariable String          userId,
                                                      @PathVariable String          elementGUID,
                                                      @RequestBody(required = false)
                                                                    NullRequestBody requestBody)
    {
        return restAPI.unmarkElementAsKnownDuplicate(serverName, userId, elementGUID, requestBody);
    }


    /**
     * List the elements that are linked as peer duplicates to the requested element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of linked duplicates or
     * InvalidParameterException one of the parameters is null or invalid
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/elements/{elementGUID}/peer-duplicates")

    public DuplicatesResponse getPeerDuplicates(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String elementGUID,
                                                @RequestParam int    startFrom,
                                                @RequestParam int    pageSize)
    {
        return restAPI.getPeerDuplicates(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Mark an element as a consolidated duplicate (or update the properties if it is already marked as such).
     * This method assumes that a standard create method has been used to create the element first using the values from contributing elements.
     * It is just adding the ConsolidatedDuplicate classification to the element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of the element that contains the consolidated information from a collection of elements
     *                                  that are all duplicates of one another.
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{consolidatedDuplicateGUID}/consolidated-duplicate")

    public VoidResponse markAsConsolidatedDuplicate(@PathVariable String                serverName,
                                                    @PathVariable String                userId,
                                                    @PathVariable String                consolidatedDuplicateGUID,
                                                    @RequestBody  DuplicatesRequestBody requestBody)
    {
        return restAPI.markAsConsolidatedDuplicate(serverName, userId, consolidatedDuplicateGUID, requestBody);
    }


    /**
     * Create a ConsolidatedDuplicateLink relationship between the consolidated duplicate element and one of its contributing element.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/consolidated-duplicate/{consolidatedDuplicateGUID}/contributing-element/{contributingElementGUID}")

    public VoidResponse linkElementToConsolidatedDuplicate(@PathVariable String          serverName,
                                                           @PathVariable String          userId,
                                                           @PathVariable String          consolidatedDuplicateGUID,
                                                           @PathVariable String          contributingElementGUID,
                                                           @RequestBody (required = false)
                                                                         NullRequestBody requestBody)
    {
        return restAPI.linkElementToConsolidatedDuplicate(serverName, userId, consolidatedDuplicateGUID, contributingElementGUID, requestBody);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/consolidated-duplicate/{consolidatedDuplicateGUID}/contributing-element/{contributingElementGUID}/delete")

    public VoidResponse unlinkElementFromConsolidatedDuplicate(@PathVariable String          serverName,
                                                               @PathVariable String          userId,
                                                               @PathVariable String          consolidatedDuplicateGUID,
                                                               @PathVariable String          contributingElementGUID,
                                                               @RequestBody (required = false)
                                                                             NullRequestBody requestBody)
    {
        return restAPI.unlinkElementFromConsolidatedDuplicate(serverName, userId, consolidatedDuplicateGUID, contributingElementGUID, requestBody);
    }


    /**
     * List the elements that are contributing to a consolidating duplicate element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of contributing duplicates or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/elements/consolidated-duplicate/{consolidatedDuplicateGUID}/contributing-elements")

    public ElementStubsResponse getContributingDuplicates(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String consolidatedDuplicateGUID,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getContributingDuplicates(serverName, userId, consolidatedDuplicateGUID, startFrom, pageSize);
    }


    /**
     * Return details of the consolidated duplicate for a requested element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID element to query
     *
     * @return header of consolidated duplicated or null if none or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/elements/{elementGUID}/consolidated-duplicate")

    public ElementStubResponse getConsolidatedDuplicate(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String elementGUID)
    {
        return restAPI.getConsolidatedDuplicate(serverName, userId, elementGUID);
    }


    /**
     * Remove the consolidated duplicate element and the links to the elements that contributed to its values.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{consolidatedDuplicateGUID}/consolidated-duplicate/delete")

    public VoidResponse removeConsolidatedDuplicate(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @PathVariable String          consolidatedDuplicateGUID,
                                                    @RequestBody (required = false)
                                                                  NullRequestBody requestBody)
    {
        return restAPI.removeConsolidatedDuplicate(serverName, userId, consolidatedDuplicateGUID, requestBody);
    }
}
