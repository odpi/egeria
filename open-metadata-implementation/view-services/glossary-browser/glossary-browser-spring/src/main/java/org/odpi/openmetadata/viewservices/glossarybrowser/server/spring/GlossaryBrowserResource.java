/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarybrowser.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryNameRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.HistoryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.*;
import org.odpi.openmetadata.viewservices.glossarybrowser.server.GlossaryBrowserRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The GlossaryBrowserResource provides the Spring API endpoints of the Glossary Browser Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/glossary-browser")

@Tag(name="API: Glossary Browser OMVS",
     description="Explore the contents of a glossary, such as its top-level glossary element, glossary categories and glossary terms, along with the elements that are linked to the terms, such assets.  Each operation includes optional forLineage and forDuplicateProcessing request parameters and an optional request body that includes an effective time field.  These affect the elements that are returned on the query.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/glossary-browser/overview/"))

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
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/status-list")

    @Operation(summary="getGlossaryTermStatuses",
               description="Return the list of glossary term status enum values.  These values are used in a glossary workflow to describe the state of the content of the term.",
               externalDocs=@ExternalDocumentation(description="Controlled glossary terms",
                                                   url="https://egeria-project.org/services/omvs/glossary-manager/overview/#controlled-glossary-terms"))


    public GlossaryTermStatusListResponse getGlossaryTermStatuses(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermStatuses(serverName);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/relationships/status-list")

    @Operation(summary="getGlossaryTermRelationshipStatuses",
               description="Return the list of glossary term relationship status enum values.  These values are stored in a term-to-term, or term-to-category, relationship and are used to indicate how much the relationship should be trusted.",
               externalDocs=@ExternalDocumentation(description="Relationship statuses",
                                                   url="https://egeria-project.org/services/omvs/glossary-manager/overview/#relationship-statuses"))

    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermRelationshipStatuses(serverName);
    }


    /**
     * Return the list of glossary term activity type enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/activity-types")

    @Operation(summary="getGlossaryTermActivityTypes",
               description="Return the list of glossary term activity type enum values.  These values are used in the ActivityDescription classification that is attached to a glossary term that represents some type of activity.",
               externalDocs=@ExternalDocumentation(description="Activity description",
                                                   url="https://egeria-project.org/types/3/0340-Dictionary/#activitydescription"))

    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermActivityTypes(serverName);
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="findGlossaries",
               description="Retrieve the list of glossary metadata elements that contain the search string.  The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.",
               externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                                                   url="https://egeria-project.org/types/3/0310-Glossary/"))

    public GlossaryElementsResponse findGlossaries(@PathVariable String                          serverName,
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
        return restAPI.findGlossaries(serverName, startFrom, pageSize, startsWith, endsWith, ignoreCase, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getGlossariesByName",
               description="Retrieve the list of glossary metadata elements with an exactly matching qualified or display name. There are no wildcards supported on this request.",
               externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                                                   url="https://egeria-project.org/types/3/0310-Glossary/"))
    
    public GlossaryElementsResponse   getGlossariesByName(@PathVariable String                  serverName,
                                                          @RequestParam int                     startFrom,
                                                          @RequestParam int                     pageSize,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 forLineage,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 forDuplicateProcessing,
                                                          @RequestBody  NameRequestBody         requestBody)
    {
        return restAPI.getGlossariesByName(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getGlossaryByGUID",
               description="Retrieve the glossary metadata element with the supplied unique identifier.  The optional request body allows you to specify that the glossary element should only be returned if it was effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                                                   url="https://egeria-project.org/types/3/0310-Glossary/"))

    public GlossaryElementResponse getGlossaryByGUID(@PathVariable String                        serverName,
                                                     @PathVariable String                        glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                       forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                       forDuplicateProcessing,
                                                     @RequestBody(required = false)
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryByGUID(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary metadata element for the requested category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/for-category/{glossaryCategoryGUID}/retrieve")

    @Operation(summary="getGlossaryForCategory",
               description="Retrieve the glossary metadata element for the requested category.  The optional request body allows you to specify that the glossary element should only be returned if it was effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                                                   url="https://egeria-project.org/types/3/0310-Glossary/"))

    public GlossaryElementResponse getGlossaryForCategory(@PathVariable String                        serverName,
                                                          @PathVariable String                        glossaryCategoryGUID,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                       forLineage,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                       forDuplicateProcessing,
                                                          @RequestBody(required = false)
                                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryForCategory(serverName, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary metadata element for the requested term.
     *
     * @param serverName name of the server to route the request to
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
    @PostMapping(path = "/glossaries/for-term/{glossaryTermGUID}/retrieve")

    @Operation(summary="getGlossaryForTerm",
               description="Retrieve the glossary metadata element for the requested term.  The optional request body allows you to specify that the glossary element should only be returned if it was effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                                                   url="https://egeria-project.org/types/3/0310-Glossary/"))

    public GlossaryElementResponse getGlossaryForTerm(@PathVariable String                        serverName,
                                                      @PathVariable String                        glossaryTermGUID,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                                    boolean                       forLineage,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                                    boolean                       forDuplicateProcessing,
                                                      @RequestBody(required = false)
                                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryForTerm(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }

    
    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="findGlossaryCategories",
               description="Retrieve the list of glossary category metadata elements that contain the search string.  The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of a glossaryGUID to restrict the search to within a single glossary.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementsResponse findGlossaryCategories(@PathVariable String                          serverName,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                 int                             startFrom,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                 int                             pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                         startsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                         endsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                         ignoreCase,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                         forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                         forDuplicateProcessing,
                                                                   @RequestBody  GlossarySearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryCategories(serverName, startFrom, pageSize, startsWith, endsWith, ignoreCase, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getCategoriesForGlossary",
               description="Return the list of categories associated with a glossary.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementsResponse getCategoriesForGlossary(@PathVariable String                             serverName,
                                                                     @PathVariable String                             glossaryGUID,
                                                                     @RequestParam (required = false, defaultValue = "0")
                                                                                   int                                startFrom,
                                                                     @RequestParam (required = false, defaultValue = "0")
                                                                                   int                                pageSize,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                            forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                            forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody      requestBody)
    {
        return restAPI.getCategoriesForGlossary(serverName, glossaryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return the list of categories associated with a term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary to query
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
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/categories/retrieve")

    @Operation(summary="getCategoriesForTerm",
               description="Return the list of categories associated with a term.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementsResponse getCategoriesForTerm(@PathVariable String                             serverName,
                                                                 @PathVariable String                             glossaryTermGUID,
                                                                 @RequestParam (required = false, defaultValue = "0")
                                                                               int                                startFrom,
                                                                 @RequestParam (required = false, defaultValue = "0")
                                                                               int                                pageSize,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                            forLineage,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                            forDuplicateProcessing,
                                                                 @RequestBody(required = false)
                                                                               EffectiveTimeQueryRequestBody      requestBody)
    {
        return restAPI.getCategoriesForTerm(serverName, glossaryTermGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getGlossaryCategoriesByName",
               description="Retrieve the list of glossary category metadata elements that either have the requested qualified name or display name.  The name to search for is located in the request body and is interpreted as a plain string.  The request body also supports the specification of a glossaryGUID to restrict the search to within a single glossary.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementsResponse   getGlossaryCategoriesByName(@PathVariable String                  serverName,
                                                                          @RequestParam int                     startFrom,
                                                                          @RequestParam int                     pageSize,
                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                                        boolean                 forLineage,
                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                                        boolean                 forDuplicateProcessing,
                                                                          @RequestBody  GlossaryNameRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoriesByName(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getGlossaryCategoryByGUID",
               description="Retrieve the requested glossary category metadata element.  The optional request body contain an effective time for the query.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementResponse getGlossaryCategoryByGUID(@PathVariable String                             serverName,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                      forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoryByGUID(serverName, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the parent glossary category metadata element for the glossary category with the supplied unique identifier.  If the
     * requested category does not have a parent category, null is returned.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getGlossaryCategoryParent",
               description="Glossary categories can be organized in a hierarchy. Retrieve the parent glossary category metadata element for the glossary category with the supplied unique identifier.  If the requested category does not have a parent category, null is returned.  The optional request body contain an effective time for the query.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementResponse getGlossaryCategoryParent(@PathVariable String                        serverName,
                                                                     @PathVariable String                        glossaryCategoryGUID,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                       forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                   boolean                       forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoryParent(serverName, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the subcategories for the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getGlossarySubCategories",
               description="Glossary categories can be organized in a hierarchy. Retrieve the subcategories for the glossary category metadata element with the supplied unique identifier.  If the requested category does not have any subcategories, null is returned.  The optional request body contain an effective time for the query.",
               externalDocs=@ExternalDocumentation(description="Glossary category metadata element",
                                                   url="https://egeria-project.org/types/3/0320-Category-Hierarchy/"))

    public GlossaryCategoryElementsResponse getGlossarySubCategories(@PathVariable String                             serverName,
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
        return restAPI.getGlossarySubCategories(serverName, glossaryCategoryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="findGlossaryTerms",
               description="Retrieve the list of glossary term metadata elements that contain the search string.  The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of a glossaryGUID to restrict the search to within a single glossary.",
               externalDocs=@ExternalDocumentation(description="Glossary term metadata element",
                                                   url="https://egeria-project.org/types/3/0330-Terms/"))

    public GlossaryTermElementsResponse findGlossaryTerms(@PathVariable String                          serverName,
                                                          @RequestParam int                             startFrom,
                                                          @RequestParam int                             pageSize,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         startsWith,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         endsWith,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         ignoreCase,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         forLineage,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                         forDuplicateProcessing,
                                                          @RequestBody  GlossarySearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryTerms(serverName, startFrom, pageSize, startsWith, endsWith, ignoreCase, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param serverName name of the server to route the request to
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

    @Operation(summary="getTermsForGlossary",
               description="Retrieve the list of glossary terms associated with a glossary.  The request body also supports the specification of an effective time for the query.",
               externalDocs=@ExternalDocumentation(description="Glossary term metadata element",
                                                   url="https://egeria-project.org/types/3/0330-Terms/"))

    public GlossaryTermElementsResponse getTermsForGlossary(@PathVariable String                        serverName,
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
        return restAPI.getTermsForGlossary(serverName, glossaryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param serverName name of the server to route the request to
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

    public GlossaryTermElementsResponse getTermsForGlossaryCategory(@PathVariable String                              serverName,
                                                                    @PathVariable String                              glossaryCategoryGUID,
                                                                    @RequestParam int                                 startFrom,
                                                                    @RequestParam int                                 pageSize,
                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                  boolean                             forLineage,
                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                  boolean                             forDuplicateProcessing,
                                                                    @RequestBody(required = false)
                                                                                  GlossaryTermRelationshipRequestBody requestBody)
    {
        return restAPI.getTermsForGlossaryCategory(serverName, glossaryCategoryGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with the requested glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary of interest
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
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/related-terms")

    public GlossaryTermElementsResponse getRelatedTerms(@PathVariable String                              serverName,
                                                        @PathVariable String                              glossaryTermGUID,
                                                        @RequestParam int                                 startFrom,
                                                        @RequestParam int                                 pageSize,
                                                        @RequestParam (required = false, defaultValue = "false")
                                                                      boolean                             forLineage,
                                                        @RequestParam (required = false, defaultValue = "false")
                                                                      boolean                             forDuplicateProcessing,
                                                        @RequestBody(required = false)
                                                                      GlossaryTermRelationshipRequestBody requestBody)
    {
        return restAPI.getRelatedTerms(serverName, glossaryTermGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
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
                                                                 @RequestParam int                     startFrom,
                                                                 @RequestParam int                     pageSize,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                 forLineage,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                 forDuplicateProcessing,
                                                                 @RequestBody  GlossaryNameRequestBody requestBody)
    {
        return restAPI.getGlossaryTermsByName(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
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
                                                             @PathVariable String                             glossaryTermGUID,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean                      forLineage,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean                      forDuplicateProcessing,
                                                             @RequestBody(required = false)
                                                                           EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGlossaryTermByGUID(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve all the versions of a glossary term.
     *
     * @param serverName name of the server to route the request to
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
        return restAPI.getGlossaryTermHistory(serverName, glossaryTermGUID, startFrom, pageSize, oldestFirst, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-data-field")

    public ElementStubsResponse getDataFieldClassifiedElements(@PathVariable String                      serverName,
                                                               @RequestParam(required = false, defaultValue = "0")
                                                                             int                         startFrom,
                                                               @RequestParam(required = false, defaultValue = "0")
                                                                             int                         pageSize,
                                                               @RequestParam(required = false, defaultValue = "false")
                                                                             boolean                     forLineage,
                                                               @RequestParam(required = false, defaultValue = "false")
                                                                             boolean                     forDuplicateProcessing,
                                                               @RequestBody(required = false)
                                                                             FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getDataFieldClassifiedElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }

    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-confidence")

    public ElementStubsResponse getConfidenceClassifiedElements(@PathVariable String                      serverName,
                                                                @RequestParam(required = false, defaultValue = "0")
                                                                int                         startFrom,
                                                                @RequestParam(required = false, defaultValue = "0")
                                                                int                         pageSize,
                                                                @RequestParam(required = false, defaultValue = "false")
                                                                boolean                     forLineage,
                                                                @RequestParam(required = false, defaultValue = "false")
                                                                boolean                     forDuplicateProcessing,
                                                                @RequestBody(required = false)
                                                                FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getConfidenceClassifiedElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-criticality")

    public ElementStubsResponse getCriticalityClassifiedElements(@PathVariable String                      serverName,
                                                                 @RequestParam(required = false, defaultValue = "0")
                                                                 int                         startFrom,
                                                                 @RequestParam(required = false, defaultValue = "0")
                                                                 int                         pageSize,
                                                                 @RequestParam(required = false, defaultValue = "false")
                                                                 boolean                     forLineage,
                                                                 @RequestParam(required = false, defaultValue = "false")
                                                                 boolean                     forDuplicateProcessing,
                                                                 @RequestBody(required = false)
                                                                 FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getCriticalityClassifiedElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-confidentiality")

    public ElementStubsResponse getConfidentialityClassifiedElements(@PathVariable String                      serverName,
                                                                     @RequestParam(required = false, defaultValue = "0")
                                                                     int                         startFrom,
                                                                     @RequestParam(required = false, defaultValue = "0")
                                                                     int                         pageSize,
                                                                     @RequestParam(required = false, defaultValue = "false")
                                                                     boolean                     forLineage,
                                                                     @RequestParam(required = false, defaultValue = "false")
                                                                     boolean                     forDuplicateProcessing,
                                                                     @RequestBody(required = false)
                                                                     FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getConfidentialityClassifiedElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-retention")

    public ElementStubsResponse getRetentionClassifiedElements(@PathVariable String                      serverName,
                                                               @RequestParam(required = false, defaultValue = "0")
                                                               int                         startFrom,
                                                               @RequestParam(required = false, defaultValue = "0")
                                                               int                         pageSize,
                                                               @RequestParam(required = false, defaultValue = "false")
                                                               boolean                     forLineage,
                                                               @RequestParam(required = false, defaultValue = "false")
                                                               boolean                     forDuplicateProcessing,
                                                               @RequestBody(required = false)
                                                               FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getRetentionClassifiedElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-security-tags")

    public ElementStubsResponse getSecurityTaggedElements(@PathVariable String                      serverName,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                          int                         startFrom,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                          int                         pageSize,
                                                          @RequestParam(required = false, defaultValue = "false")
                                                          boolean                     forLineage,
                                                          @RequestParam(required = false, defaultValue = "false")
                                                          boolean                     forDuplicateProcessing,
                                                          @RequestBody(required = false)
                                                          FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getSecurityTaggedElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-ownership")

    public ElementStubsResponse getOwnersElements(@PathVariable String                      serverName,
                                                  @RequestParam(required = false, defaultValue = "0")
                                                  int                         startFrom,
                                                  @RequestParam(required = false, defaultValue = "0")
                                                  int                         pageSize,
                                                  @RequestParam(required = false, defaultValue = "false")
                                                  boolean                     forLineage,
                                                  @RequestParam(required = false, defaultValue = "false")
                                                  boolean                     forDuplicateProcessing,
                                                  @RequestBody(required = false)
                                                  FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getOwnersElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-subject-area-membership")

    public ElementStubsResponse getMembersOfSubjectArea(@PathVariable String                      serverName,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         startFrom,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         pageSize,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forLineage,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forDuplicateProcessing,
                                                        @RequestBody(required = false)
                                                        FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getMembersOfSubjectArea(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */

    @PostMapping("/glossaries/terms/by-semantic-assignment/{elementGUID}")

    public GlossaryTermElementsResponse getMeanings(@PathVariable String                        serverName,
                                                    @PathVariable String                        elementGUID,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int                         startFrom,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int                         pageSize,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                    boolean                     forLineage,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                    boolean                     forDuplicateProcessing,
                                                    @RequestBody(required = false)
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getMeanings(serverName, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping("/elements/by-semantic-assignment/{glossaryTermGUID}")

    public RelatedElementsResponse getSemanticAssignees(@PathVariable String                        serverName,
                                                        @PathVariable String                        glossaryTermGUID,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         startFrom,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         pageSize,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forLineage,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forDuplicateProcessing,
                                                        @RequestBody(required = false)
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getSemanticAssignees(serverName, glossaryTermGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/governed-by/{governanceDefinitionGUID}")

    public RelatedElementsResponse getGovernedElements(@PathVariable String                        serverName,
                                                       @PathVariable String                        governanceDefinitionGUID,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                       int                         startFrom,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                       int                         pageSize,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                       boolean                     forLineage,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                       boolean                     forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGovernedElements(serverName, governanceDefinitionGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by")

    public GovernanceDefinitionsResponse getGovernedByDefinitions(@PathVariable String                        serverName,
                                                                  @PathVariable String                        elementGUID,
                                                                  @RequestParam(required = false, defaultValue = "0")
                                                                  int                         startFrom,
                                                                  @RequestParam(required = false, defaultValue = "0")
                                                                  int                         pageSize,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                  boolean                     forLineage,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                  boolean                     forDuplicateProcessing,
                                                                  @RequestBody(required = false)
                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGovernedByDefinitions(serverName, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/sourced-from/{elementGUID}")

    public RelatedElementsResponse getSourceElements(@PathVariable String                        serverName,
                                                     @PathVariable String elementGUID,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                                   int                         startFrom,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                                   int                         pageSize,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                     forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                     forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getSourceElements(serverName, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/sourced-from")

    public RelatedElementsResponse getElementsSourceFrom(@PathVariable String                        serverName,
                                                         @PathVariable String                        elementGUID,
                                                         @RequestParam (required = false, defaultValue = "0")
                                                                       int                           startFrom,
                                                         @RequestParam (required = false, defaultValue = "0")
                                                                       int                           pageSize,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                       forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                       forDuplicateProcessing,
                                                         @RequestBody  (required = false)
                                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getElementsSourceFrom(serverName, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }

}

