/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarybrowser.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FeedbackProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TagProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ElementStubsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermRelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.RelatedElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryNameRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.HistoryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.InformalTagUpdateRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.RelationshipRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.server.GlossaryBrowserRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



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
                                                   url="https://egeria-project.org/services/omvs/glossary-workflow/overview/#controlled-glossary-terms"))


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
               description="Return the list of glossary term relationship status enum values.  These values are stored in a term-to-term, or term-to-category, relationship and are used to indicate how much the relationship should be trusted",
               externalDocs=@ExternalDocumentation(description="Relationship statuses",
                                                   url="https://egeria-project.org/services/omvs/glossary-workflow/overview/#relationship-statuses"))

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
     * Adds a reply to a comment.
     *
     * @param serverName    name of the server instances for this request.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody   containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/replies")

    @Operation(summary="addCommentReply",
               description="Adds a reply to a comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentReply(@PathVariable String                         serverName,
                                        @PathVariable String                         commentGUID,
                                        @RequestParam boolean                        isPublic,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        forLineage,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        forDuplicateProcessing,
                                        @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.addCommentReply(serverName, commentGUID, isPublic, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Creates a comment and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param elementGUID        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments")

    @Operation(summary="addCommentToElement",
               description="Creates a comment and attaches it to an element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentToElement(@PathVariable String                         serverName,
                                            @PathVariable String                         elementGUID,
                                            @RequestParam boolean                        isPublic,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                        forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                        forDuplicateProcessing,
                                            @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.addCommentToElement(serverName, elementGUID, isPublic, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an element.
     *
     * @param serverName  name of the server instances for this request.
     * @param elementGUID   String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param requestBody feedback request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/likes")

    @Operation(summary="addLikeToElement",
               description="Creates a <i>like</i> object and attaches it to an element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse addLikeToElement(@PathVariable String         serverName,
                                         @PathVariable String         elementGUID,
                                         @RequestParam boolean        isPublic,
                                         @RequestBody (required = false)
                                         NullRequestBody requestBody)
    {
        return restAPI.addLikeToElement(serverName, elementGUID, isPublic, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the element.
     *
     * @param serverName  name of the server instances for this request.
     * @param elementGUID String - unique id for the element.
     * @param isPublic    is this visible to other people
     * @param requestBody containing the StarRating and user review of element.
     *
     * @return elementGUID for new review object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/ratings")

    @Operation(summary="addRatingToElement",
               description="Adds a star rating and optional review text to the element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse addRatingToElement(@PathVariable String           serverName,
                                           @PathVariable String           elementGUID,
                                           @RequestParam boolean          isPublic,
                                           @RequestBody RatingProperties requestBody)
    {
        return restAPI.addRatingToElement(serverName, elementGUID, isPublic, requestBody);
    }


    /**
     * Adds an informal tag (either private of public) to an element.
     *
     * @param serverName       name of the server instances for this request.
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param requestBody      null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/tags/{tagGUID}")

    @Operation(summary="addTagToElement",
               description="Adds an informal tag (either private of public) to an element.",
               externalDocs=@ExternalDocumentation(description="Element Classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public VoidResponse addTagToElement(@PathVariable String             serverName,
                                        @PathVariable String             elementGUID,
                                        @PathVariable String             tagGUID,
                                        @RequestBody FeedbackProperties requestBody)
    {
        return restAPI.addTagToElement(serverName, elementGUID, tagGUID, requestBody);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param requestBody  public/private flag, name of the tag and (optional) description of the tag.
     *
     * @return new elementGUID or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags")

    @Operation(summary="createInformalTag",
               description="Creates a new informal tag and returns the unique identifier for it.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public GUIDResponse createInformalTag(@PathVariable String        serverName,
                                          @RequestBody TagProperties requestBody)
    {
        return restAPI.createInformalTag(serverName, requestBody);
    }


    /**
     * Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID      String - unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/remove")

    @Operation(summary="deleteTag",
               description="Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.  A private tag can be deleted by its creator and all the references are lost; a public tag can be deleted by anyone, but only if it is not attached to any referenceable.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public VoidResponse   deleteTag(@PathVariable                   String          serverName,
                                    @PathVariable                   String          tagGUID,
                                    @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, tagGUID, requestBody);
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.
     *
     * @param serverName name of the server instances for this request
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return element guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/elements/by-tag/{tagGUID}")

    @Operation(summary="getElementsByTag",
               description="Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.",
               externalDocs=@ExternalDocumentation(description="Element classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public GUIDListResponse getElementsByTag(@PathVariable String serverName,
                                             @PathVariable String tagGUID,
                                             @RequestParam int    startFrom,
                                             @RequestParam int    pageSize)

    {
        return restAPI.getElementsByTag(serverName, tagGUID, startFrom, pageSize);
    }


    /**
     * Return the informal tag for the supplied unique identifier (tagGUID).
     *
     * @param serverName name of the server instances for this request.
     * @param tagGUID unique identifier of the meaning.
     *
     * @return tag object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/tags/{tagGUID}")

    @Operation(summary="getTag",
               description="Return the informal tag for the supplied unique identifier (tagGUID).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagResponse getTag(@PathVariable String   serverName,
                                      @PathVariable String   tagGUID)
    {
        return restAPI.getTag(serverName, tagGUID);
    }


    /**
     * Return the tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-name")

    @Operation(summary="getTagsByName",
               description="Return the tags exactly matching the supplied name.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagsResponse getTagsByName(@PathVariable String          serverName,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize,
                                              @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getTagsByName(serverName, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of the calling user's private informal tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/private/by-name")

    @Operation(summary="getMyTagsByName",
               description="Return the list of the calling user's private informal tags exactly matching the supplied name.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagsResponse getMyTagsByName(@PathVariable String          serverName,
                                                @RequestParam int             startFrom,
                                                @RequestParam int             pageSize,
                                                @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMyTagsByName(serverName, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of comments containing the supplied string.
     *
     * @param serverName name of the server instances for this request.
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search string and effective time.
     *
     * @return list of comment objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/comments/by-search-string")

    @Operation(summary="findComment",
               description="Return the list of comments containing the supplied string. The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of an effective time to restrict the search to element that are/were effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Comment",
                                                   url="https://egeria-project.org/concepts/comment/"))

    public CommentElementsResponse findComments(@PathVariable String                  serverName,
                                                @RequestParam (required = false, defaultValue = "0")
                                                              int                     startFrom,
                                                @RequestParam (required = false, defaultValue = "0")
                                                              int                     pageSize,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                 startsWith,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                 endsWith,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                  ignoreCase,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                  forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                  forDuplicateProcessing,
                                                @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findComments(serverName,
                                    startFrom,
                                    pageSize,
                                    startsWith,
                                    endsWith,
                                    ignoreCase,
                                    forLineage,
                                    forDuplicateProcessing,
                                    requestBody);
    }


    /**
     * Return the list of comments containing the supplied string in the text. The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param requestBody search string and effective time.
     *
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-search-string")

    @Operation(summary="findTags",
               description="Return the list of informal tags containing the supplied string in their name or description. The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of an effective time to restrict the search to element that are/were effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagsResponse findTags(@PathVariable String                  serverName,
                                         @RequestParam (required = false, defaultValue = "0")
                                             int                     startFrom,
                                         @RequestParam (required = false, defaultValue = "0")
                                             int                     pageSize,
                                         @RequestParam (required = false, defaultValue = "false")
                                             boolean                 startsWith,
                                         @RequestParam (required = false, defaultValue = "false")
                                             boolean                 endsWith,
                                         @RequestParam (required = false, defaultValue = "false")
                                             boolean                  ignoreCase,
                                         @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findTags(serverName,
                                startFrom,
                                pageSize,
                                startsWith,
                                endsWith,
                                ignoreCase,
                                requestBody);
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.  The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param requestBody search string and effective time.
     *
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/private/by-search-string")

    @Operation(summary="findMyTags",
               description="Return the list of the calling user's private tags containing the supplied string in either the name or description.  The search string is a regular expression (RegEx).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagsResponse findMyTags(@PathVariable String                  serverName,
                                           @RequestParam (required = false, defaultValue = "0")
                                               int                     startFrom,
                                           @RequestParam (required = false, defaultValue = "0")
                                               int                     pageSize,
                                           @RequestParam (required = false, defaultValue = "false")
                                               boolean                 startsWith,
                                           @RequestParam (required = false, defaultValue = "false")
                                               boolean                 endsWith,
                                           @RequestParam (required = false, defaultValue = "false")
                                               boolean                  ignoreCase,
                                           @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findMyTags(serverName,
                                  startFrom,
                                  pageSize,
                                  startsWith,
                                  endsWith,
                                  ignoreCase,
                                  requestBody);
    }


    /**
     * Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID  String - unique id for the element object
     * @param commentGUID  String - unique id for the comment object
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments/{commentGUID}/remove")

    @Operation(summary="removeCommentFromElement",
               description="Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse removeCommentFromElement(@PathVariable String                         serverName,
                                                 @PathVariable String                         elementGUID,
                                                 @PathVariable String                         commentGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                 boolean                        forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                  boolean                        forDuplicateProcessing,
                                                 @RequestBody(required = false)
                                                                ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeCommentFromElement(serverName, elementGUID, commentGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param commentGUID  unique identifier for the comment object.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties controlling the request
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/comments/{commentGUID}")

    public CommentElementResponse getComment(@PathVariable String                        serverName,
                                             @PathVariable String                        commentGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forDuplicateProcessing,
                                             @RequestBody(required = false)
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getCommentByGUID(serverName, commentGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties controlling the request
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments/retrieve")

    public CommentElementsResponse getAttachedComments(@PathVariable String                        serverName,
                                                       @PathVariable String                        elementGUID,
                                                       @RequestParam int                           startFrom,
                                                       @RequestParam int                           pageSize,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean                       forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean                       forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAttachedComments(serverName, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param elementGUID    unique identifier for the element where the like is attached.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/likes/remove")

    @Operation(summary="removeLikeFromElement",
               description="Removes a <i>Like</i> added to the element by this user.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#element-feedback"))

    public VoidResponse   removeLikeFromElement(@PathVariable                  String          serverName,
                                                @PathVariable                  String          elementGUID,
                                                @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeLikeFromElement(serverName, elementGUID, requestBody);
    }


    /**
     * Removes of a star rating/review that was added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param elementGUID         unique identifier for the element where the rating is attached.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/ratings/remove")

    @Operation(summary="removeRatingFromElement",
               description="Removes of a star rating/review that was added to the element by this user.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse   removeRatingFromElement(@PathVariable                  String          serverName,
                                                  @PathVariable                  String          elementGUID,
                                                  @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeRatingFromElement(serverName, elementGUID, requestBody);
    }


    /**
     * Removes a link between a tag and an element that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/tags/{tagGUID}/remove")

    @Operation(summary="removeTagFromElement",
               description="Removes a link between a tag and an element that was added by this user.",
               externalDocs=@ExternalDocumentation(description="Element classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public VoidResponse removeTagFromElement(@PathVariable                  String          serverName,
                                             @PathVariable                  String          elementGUID,
                                             @PathVariable                  String          tagGUID,
                                             @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeTagFromElement(serverName, elementGUID, tagGUID, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param commentGUID  unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic      is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/update")

    @Operation(summary="updateComment",
               description="Update an existing comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse   updateComment(@PathVariable String                         serverName,
                                        @PathVariable String                         commentGUID,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        isMergeUpdate,
                                        @RequestParam boolean                        isPublic,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        forLineage,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        forDuplicateProcessing,
                                        @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateComment(serverName, commentGUID, isMergeUpdate, isPublic, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/comments/questions/{questionCommentGUID}/answers/{answerCommentGUID}")

    public VoidResponse setupAcceptedAnswer(@PathVariable String                  serverName,
                                            @PathVariable String                  questionCommentGUID,
                                            @PathVariable String                  answerCommentGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                 forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                 forDuplicateProcessing,
                                            @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupAcceptedAnswer(serverName,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/comments/questions/{questionCommentGUID}/answers/{answerCommentGUID}/remove")

    public VoidResponse clearAcceptedAnswer(@PathVariable String                        serverName,
                                            @PathVariable String                        questionCommentGUID,
                                            @PathVariable String                        answerCommentGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                       forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                       forDuplicateProcessing,
                                            @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearAcceptedAnswer(serverName,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID      unique id for the tag
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/update")

    @Operation(summary="updateTagDescription",
               description="Updates the description of an existing tag (either private or public).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public VoidResponse   updateTagDescription(@PathVariable String                       serverName,
                                               @PathVariable String                       tagGUID,
                                               @RequestBody  InformalTagUpdateRequestBody requestBody)
    {
        return restAPI.updateTagDescription(serverName, tagGUID, requestBody);
    }



    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
     * broadcast information.  Notelogs are typically maintained by the owners/stewards of an element.
     */


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server instances for this request.
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search string and effective time.
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/by-search-string")

    public NoteLogElementsResponse findNoteLogs(@PathVariable String                  serverName,
                                                @RequestParam (required = false, defaultValue = "0")
                                                    int                     startFrom,
                                                @RequestParam (required = false, defaultValue = "0")
                                                    int                     pageSize,
                                                @RequestParam (required = false, defaultValue = "false")
                                                    boolean                 startsWith,
                                                @RequestParam (required = false, defaultValue = "false")
                                                    boolean                 endsWith,
                                                @RequestParam (required = false, defaultValue = "false")
                                                    boolean                  ignoreCase,
                                                @RequestParam (required = false, defaultValue = "false")
                                                    boolean                  forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                    boolean                  forDuplicateProcessing,
                                                @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findNoteLogs(serverName,
                                    startFrom,
                                    pageSize,
                                    startsWith,
                                    endsWith,
                                    ignoreCase,
                                    forLineage,
                                    forDuplicateProcessing,
                                    requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/by-name")

    public NoteLogElementsResponse getNoteLogsByName(@PathVariable String          serverName,
                                                     @RequestParam int             startFrom,
                                                     @RequestParam int             pageSize,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean         forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean         forDuplicateProcessing,
                                                     @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getNoteLogsByName(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param elementGUID element to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/elements/{elementGUID}/note-logs/retrieve")

    public NoteLogElementsResponse getNoteLogsForElement(@PathVariable String          serverName,
                                                         @PathVariable String          elementGUID,
                                                         @RequestParam int             startFrom,
                                                         @RequestParam int             pageSize,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                         boolean         forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                         boolean         forDuplicateProcessing,
                                                         @RequestBody (required = false)
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteLogsForElement(serverName, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/{noteLogGUID}/retrieve")

    public NoteLogElementResponse getNoteLogByGUID(@PathVariable String                        serverName,
                                                   @PathVariable String                        noteLogGUID,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forDuplicateProcessing,
                                                   @RequestBody(required = false)
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteLogByGUID(serverName, noteLogGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /* ===============================================================================
     * A note log typically contains many notes, linked with relationships.
     */


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server instances for this request.
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search string and effective time.
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/notes/by-search-string")

    public NoteElementsResponse findNotes(@PathVariable String                  serverName,
                                          @RequestParam (required = false, defaultValue = "0")
                                              int                     startFrom,
                                          @RequestParam (required = false, defaultValue = "0")
                                              int                     pageSize,
                                          @RequestParam (required = false, defaultValue = "false")
                                              boolean                 startsWith,
                                          @RequestParam (required = false, defaultValue = "false")
                                              boolean                 endsWith,
                                          @RequestParam (required = false, defaultValue = "false")
                                              boolean                  ignoreCase,
                                          @RequestParam (required = false, defaultValue = "false")
                                              boolean                  forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                              boolean                  forDuplicateProcessing,
                                          @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findNotes(serverName,
                                 startFrom,
                                 pageSize,
                                 startsWith,
                                 endsWith,
                                 ignoreCase,
                                 forLineage,
                                 forDuplicateProcessing,
                                 requestBody);
    }


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/{noteLogGUID}/notes/retrieve")

    public NoteElementsResponse getNotesForNoteLog(@PathVariable String                        serverName,
                                                   @PathVariable String                        noteLogGUID,
                                                   @RequestParam int                           startFrom,
                                                   @RequestParam int                           pageSize,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forDuplicateProcessing,
                                                   @RequestBody(required = false)
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNotesForNoteLog(serverName, noteLogGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param noteGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/notes/{noteGUID}/retrieve")

    public NoteElementResponse getNoteByGUID(@PathVariable String                        serverName,
                                             @PathVariable String                        noteGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forDuplicateProcessing,
                                             @RequestBody(required = false)
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteByGUID(serverName, noteGUID, forLineage, forDuplicateProcessing, requestBody);
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

