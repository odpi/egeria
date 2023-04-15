/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarybrowser.server.spring;

import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.viewservices.glossarybrowser.server.GlossaryBrowserRESTServices;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * The GlossaryBrowserResource provides the Spring API endpoints of the Glossary Browser Open Metadata View Service (OMVS).
 * This interface provides an interfaces for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-browser/users/{userId}")

@Tag(name="Glossary Browser OMVS", description="Explore the contents of a glossary.", externalDocs=@ExternalDocumentation(description="Glossary Browser View Service (OMVS)",url="https://egeria-project.org/services/omvs/glossary-browser/overview/"))

public class GlossaryBrowserResource
{

    private final GlossaryBrowserRESTServices restAPI = new GlossaryBrowserRESTServices();


    /**
     * Default constructor
     */
    public GlossaryBrowserResource()
    {
    }


    /**
     * Return the list of glossary term status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/status-list")

    public GlossaryTermStatusListResponse getGlossaryTermStatuses(@PathVariable String serverName,
                                                                  @PathVariable String userId)
    {
        return restAPI.getGlossaryTermStatuses(serverName, userId);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/relationships/status-list")

    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(@PathVariable String serverName,
                                                                                          @PathVariable String userId)
    {
        return restAPI.getGlossaryTermRelationshipStatuses(serverName, userId);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/activity-types")

    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(@PathVariable String serverName,
                                                                             @PathVariable String userId)
    {
        return restAPI.getGlossaryTermActivityTypes(serverName, userId);
    }

    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-search-string")

    public GlossaryElementsResponse findGlossaries(@PathVariable String                          serverName,
                                                   @PathVariable String                          userId,
                                                   @RequestParam int                             startFrom,
                                                   @RequestParam int                             pageSize,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                        startsWith,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                        endsWith,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                        ignoreCase,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                        forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                        forDuplicateProcessing,
                                                   @RequestBody  SearchStringRequestBody        requestBody)
    {
        return restAPI.findGlossaries(serverName, userId, startFrom, pageSize, startsWith, endsWith, ignoreCase, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-name")

    public GlossaryElementsResponse   getGlossariesByName(@PathVariable String                  serverName,
                                                          @PathVariable String                  userId,
                                                          @RequestParam int                     startFrom,
                                                          @RequestParam int                     pageSize,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 forLineage,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 forDuplicateProcessing,
                                                          @RequestBody  NameRequestBody         requestBody)
    {
        return restAPI.getGlossariesByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/retrieve")

    public GlossaryElementResponse getGlossaryByGUID(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @PathVariable String                        glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                       forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                       forDuplicateProcessing,
                                                     @RequestBody(required = false)
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryByGUID(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }




    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties and correlators
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/by-search-string")

    public GlossaryCategoryElementsResponse findGlossaryCategories(@PathVariable String                          serverName,
                                                                   @PathVariable String                          userId,
                                                                   @RequestParam int                             startFrom,
                                                                   @RequestParam int                             pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                        startsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                        endsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                        ignoreCase,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                               forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                               forDuplicateProcessing,
                                                                   @RequestBody  GlossarySearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryCategories(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, startsWith, endsWith, ignoreCase, requestBody);
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of metadata elements describing the categories associated with the requested glossary or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/categories/retrieve")

    public GlossaryCategoryElementsResponse getCategoriesForGlossary(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryGUID,
                                                                     @RequestParam int                                startFrom,
                                                                     @RequestParam int                                pageSize,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getCategoriesForGlossary(serverName, userId, glossaryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody name to search for and correlators
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/by-name")

    public GlossaryCategoryElementsResponse   getGlossaryCategoriesByName(@PathVariable String                  serverName,
                                                                          @PathVariable String                  userId,
                                                                          @RequestParam int                     startFrom,
                                                                          @RequestParam int                     pageSize,
                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                                        boolean                       forLineage,
                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                                        boolean                       forDuplicateProcessing,
                                                                          @RequestBody  GlossaryNameRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoriesByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/retrieve")

    public GlossaryCategoryElementResponse getGlossaryCategoryByGUID(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoryByGUID(serverName, userId, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return parent glossary category element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/parent/retrieve")

    public GlossaryCategoryElementResponse getGlossaryCategoryParent(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoryParent(serverName, userId, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of glossary category elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/subcategories/retrieve")

    public GlossaryCategoryElementsResponse getGlossarySubCategories(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestParam int                                startFrom,
                                                                     @RequestParam int                                pageSize,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossarySubCategories(serverName, userId, glossaryCategoryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers and search string
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-search-string")

    public GlossaryTermElementsResponse findGlossaryTerms(@PathVariable String                          serverName,
                                                          @PathVariable String                          userId,
                                                          @RequestParam int                             startFrom,
                                                          @RequestParam int                             pageSize,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                        startsWith,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                        endsWith,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                        ignoreCase,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         forLineage,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         forDuplicateProcessing,
                                                          @RequestBody  GlossarySearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryTerms(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, startsWith, endsWith, ignoreCase, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms/retrieve")

    public GlossaryTermElementsResponse getTermsForGlossary(@PathVariable String                        serverName,
                                                            @PathVariable String                        userId,
                                                            @PathVariable String                        glossaryGUID,
                                                            @RequestParam int                           startFrom,
                                                            @RequestParam int                           pageSize,
                                                            @RequestParam (required = false, defaultValue = "false")
                                                                          boolean                       forLineage,
                                                            @RequestParam (required = false, defaultValue = "false")
                                                                          boolean                       forDuplicateProcessing,
                                                            @RequestBody(required = false)
                                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getTermsForGlossary(serverName, userId, glossaryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/terms/retrieve")

    public GlossaryTermElementsResponse getTermsForGlossaryCategory(@PathVariable String                             serverName,
                                                                    @PathVariable String                             userId,
                                                                    @PathVariable String                             glossaryCategoryGUID,
                                                                    @RequestParam int                                startFrom,
                                                                    @RequestParam int                                pageSize,
                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                  boolean                      forLineage,
                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                  boolean                      forDuplicateProcessing,
                                                                    @RequestBody(required = false)
                                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getTermsForGlossaryCategory(serverName, userId, glossaryCategoryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-name")

    public GlossaryTermElementsResponse   getGlossaryTermsByName(@PathVariable String                  serverName,
                                                                 @PathVariable String                  userId,
                                                                 @RequestParam int                     startFrom,
                                                                 @RequestParam int                     pageSize,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                 forLineage,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                 forDuplicateProcessing,
                                                                 @RequestBody  GlossaryNameRequestBody requestBody)
    {
        return restAPI.getGlossaryTermsByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/retrieve")

    public GlossaryTermElementResponse getGlossaryTermByGUID(@PathVariable String                             serverName,
                                                             @PathVariable String                             userId,
                                                             @PathVariable String                             glossaryTermGUID,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean                      forLineage,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean                      forDuplicateProcessing,
                                                             @RequestBody(required = false)
                                                                           EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryTermByGUID(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve all the versions of a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of object to retrieve
     * @param startFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/history")

    public GlossaryTermElementsResponse getGlossaryTermHistory(@PathVariable String                 serverName,
                                                               @PathVariable String                 userId,
                                                               @PathVariable String                 glossaryTermGUID,
                                                               @RequestParam int                    startFrom,
                                                               @RequestParam int                    pageSize,
                                                               @RequestParam (required = false, defaultValue = "false")
                                                                             boolean                oldestFirst,
                                                               @RequestParam (required = false, defaultValue = "false")
                                                                             boolean                forLineage,
                                                               @RequestParam (required = false, defaultValue = "false")
                                                                             boolean                forDuplicateProcessing,
                                                               @RequestBody(required = false)
                                                                             HistoryRequestBody     requestBody)
    {
        return restAPI.getGlossaryTermHistory(serverName, userId, glossaryTermGUID, startFrom, pageSize, oldestFirst, forLineage, forDuplicateProcessing, requestBody);
    }

}
