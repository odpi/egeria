/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.feedbackmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.rest.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.properties.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.server.FeedbackManagerRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The FeedbackManagerResource provides the Spring API endpoints of the Feedback Manager Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/feedback-manager")

@Tag(name="API: Feedback Manager OMVS",
     description="Explore the contents of a glossary, such as its top-level glossary element, glossary categories and glossary terms, along with the elements that are linked to the terms, such assets.  Each operation includes optional forLineage and forDuplicateProcessing request parameters and an optional request body that includes an effective time field.  These affect the elements that are returned on the query.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/feedback-manager/overview/"))

public class FeedbackManagerResource
{

    private final FeedbackManagerRESTServices restAPI = new FeedbackManagerRESTServices();


    /**
     * Default constructor
     */
    public FeedbackManagerResource()
    {
    }

    
    /**
     * Adds a reply to a comment.
     *
     * @param serverName    name of the server instances for this request.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                        @RequestParam (required = false)
                                            String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                            String                         accessServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "false")
                                            boolean                        forLineage,
                                        @RequestParam (required = false, defaultValue = "false")
                                            boolean                        forDuplicateProcessing,
                                        @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.addCommentReply(serverName, commentGUID, isPublic, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Creates a comment and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param elementGUID        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                            @RequestParam (required = false)
                                                String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                String                         accessServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                        forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                        forDuplicateProcessing,
                                            @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.addCommentToElement(serverName, elementGUID, isPublic, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an element.
     *
     * @param serverName  name of the server instances for this request.
     * @param elementGUID   String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                         @RequestParam (required = false)
                                             String                         viewServiceURLMarker,
                                         @RequestParam (required = false, defaultValue = "asset-manager")
                                             String                         accessServiceURLMarker,
                                         @RequestBody (required = false)
                                         NullRequestBody requestBody)
    {
        return restAPI.addLikeToElement(serverName, elementGUID, isPublic, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the element.
     *
     * @param serverName  name of the server instances for this request.
     * @param elementGUID String - unique id for the element.
     * @param isPublic    is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                           @RequestParam (required = false)
                                               String                         viewServiceURLMarker,
                                           @RequestParam (required = false, defaultValue = "asset-manager")
                                               String                         accessServiceURLMarker,
                                           @RequestBody  RatingProperties requestBody)
    {
        return restAPI.addRatingToElement(serverName, elementGUID, isPublic, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Adds an informal tag (either private of public) to an element.
     *
     * @param serverName       name of the server instances for this request.
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                        @RequestParam (required = false)
                                            String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                            String                         accessServiceURLMarker,
                                        @RequestBody FeedbackProperties requestBody)
    {
        return restAPI.addTagToElement(serverName, elementGUID, tagGUID, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                          @RequestParam (required = false)
                                          String                         viewServiceURLMarker,
                                          @RequestParam (required = false, defaultValue = "asset-manager")
                                              String                         accessServiceURLMarker,
                                          @RequestBody TagProperties requestBody)
    {
        return restAPI.createInformalTag(serverName, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID      String - unique id for the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                    @RequestParam (required = false)
                                        String                         viewServiceURLMarker,
                                    @RequestParam (required = false, defaultValue = "asset-manager")
                                        String                         accessServiceURLMarker,
                                    @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, tagGUID, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.
     *
     * @param serverName name of the server instances for this request
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     *
     * @return element stubs list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/elements/by-tag/{tagGUID}")

    @Operation(summary="getElementsByTag",
               description="Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.",
               externalDocs=@ExternalDocumentation(description="Element classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public RelatedElementsResponse getElementsByTag(@PathVariable String serverName,
                                             @PathVariable String tagGUID,
                                             @RequestParam int    startFrom,
                                             @RequestParam int    pageSize,
                                             @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                             @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker)

    {
        return restAPI.getElementsByTag(serverName, tagGUID, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker);
    }


    /**
     * Return the informal tag for the supplied unique identifier (tagGUID).
     *
     * @param serverName name of the server instances for this request.
     * @param tagGUID unique identifier of the meaning.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                      @PathVariable String   tagGUID,
                                      @RequestParam (required = false)
                                          String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                          String                         accessServiceURLMarker)
    {
        return restAPI.getTag(serverName, tagGUID, viewServiceURLMarker, accessServiceURLMarker);
    }


    /**
     * Return the tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                              @RequestParam (required = false)
                                                  String                         viewServiceURLMarker,
                                              @RequestParam (required = false, defaultValue = "asset-manager")
                                                  String                         accessServiceURLMarker,
                                              @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getTagsByName(serverName, requestBody, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker);
    }


    /**
     * Return the list of the calling user's private informal tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                @RequestParam (required = false)
                                                    String                         viewServiceURLMarker,
                                                @RequestParam (required = false, defaultValue = "asset-manager")
                                                    String                         accessServiceURLMarker,
                                                @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMyTagsByName(serverName, requestBody, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker);
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                @RequestParam (required = false)
                                                    String                         viewServiceURLMarker,
                                                @RequestParam (required = false, defaultValue = "asset-manager")
                                                    String                         accessServiceURLMarker,
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
                                    viewServiceURLMarker,
                                    accessServiceURLMarker,
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                         @RequestParam (required = false)
                                             String                         viewServiceURLMarker,
                                         @RequestParam (required = false, defaultValue = "asset-manager")
                                             String                         accessServiceURLMarker,
                                         @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findTags(serverName,
                                startFrom,
                                pageSize,
                                startsWith,
                                endsWith,
                                ignoreCase,
                                viewServiceURLMarker,
                                accessServiceURLMarker,
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                           @RequestParam (required = false)
                                               String                         viewServiceURLMarker,
                                           @RequestParam (required = false, defaultValue = "asset-manager")
                                               String                         accessServiceURLMarker,
                                           @RequestBody  SearchStringRequestBody  requestBody)
    {
        return restAPI.findMyTags(serverName,
                                  startFrom,
                                  pageSize,
                                  startsWith,
                                  endsWith,
                                  ignoreCase,
                                  viewServiceURLMarker,
                                  accessServiceURLMarker,
                                  requestBody);
    }


    /**
     * Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID  String - unique id for the element object
     * @param commentGUID  String - unique id for the comment object
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                 @RequestParam (required = false)
                                                     String                         viewServiceURLMarker,
                                                 @RequestParam (required = false, defaultValue = "asset-manager")
                                                     String                         accessServiceURLMarker,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                 boolean                        forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                  boolean                        forDuplicateProcessing,
                                                 @RequestBody(required = false)
                                                                ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeCommentFromElement(serverName, elementGUID, commentGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param commentGUID  unique identifier for the comment object.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties controlling the request
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/comments/{commentGUID}")

    public CommentResponse getComment(@PathVariable String                        serverName,
                                      @PathVariable String                        commentGUID,
                                      @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forLineage,
                                      @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forDuplicateProcessing,
                                      @RequestBody(required = false)
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getCommentByGUID(serverName, commentGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                       @RequestParam (required = false)
                                                           String                         viewServiceURLMarker,
                                                       @RequestParam (required = false, defaultValue = "asset-manager")
                                                           String                         accessServiceURLMarker,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean                       forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean                       forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAttachedComments(serverName, elementGUID, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param elementGUID    unique identifier for the element where the like is attached.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                @RequestParam (required = false)
                                                    String                         viewServiceURLMarker,
                                                @RequestParam (required = false, defaultValue = "asset-manager")
                                                    String                         accessServiceURLMarker,
                                                @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeLikeFromElement(serverName, elementGUID, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Removes of a star rating/review that was added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param elementGUID         unique identifier for the element where the rating is attached.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                  @RequestParam (required = false)
                                                      String                         viewServiceURLMarker,
                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                      String                         accessServiceURLMarker,
                                                  @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeRatingFromElement(serverName, elementGUID, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Removes a link between a tag and an element that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                             @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                             @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker,
                                             @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeTagFromElement(serverName, elementGUID, tagGUID, viewServiceURLMarker, accessServiceURLMarker, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param commentGUID  unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic      is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                        @RequestParam (required = false)
                                            String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                            String                         accessServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        forLineage,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                        forDuplicateProcessing,
                                        @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateComment(serverName, commentGUID, isMergeUpdate, isPublic, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                            @RequestParam (required = false)
                                                String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                String                         accessServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                 forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                 forDuplicateProcessing,
                                            @RequestBody RelationshipRequestBody requestBody)
    {
        return restAPI.setupAcceptedAnswer(serverName,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           viewServiceURLMarker,
                                           accessServiceURLMarker,
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                            @RequestParam (required = false)
                                                          String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                          String                         accessServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forDuplicateProcessing,
                                            @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearAcceptedAnswer(serverName,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           viewServiceURLMarker,
                                           accessServiceURLMarker,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID      unique id for the tag
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                               @RequestParam (required = false)
                                                   String                         viewServiceURLMarker,
                                               @RequestParam (required = false, defaultValue = "asset-manager")
                                                   String                         accessServiceURLMarker,
                                               @RequestBody  InformalTagUpdateRequestBody requestBody)
    {
        return restAPI.updateTagDescription(serverName, tagGUID, viewServiceURLMarker, accessServiceURLMarker, requestBody);
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NoteLogsResponse findNoteLogs(@PathVariable String                  serverName,
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
                                         @RequestParam (required = false)
                                                    String                         viewServiceURLMarker,
                                         @RequestParam (required = false, defaultValue = "asset-manager")
                                                    String                         accessServiceURLMarker,
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
                                    viewServiceURLMarker,
                                    accessServiceURLMarker,
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NoteLogsResponse getNoteLogsByName(@PathVariable String          serverName,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize,
                                              @RequestParam (required = false)
                                                         String                         viewServiceURLMarker,
                                              @RequestParam (required = false, defaultValue = "asset-manager")
                                                         String                         accessServiceURLMarker,
                                              @RequestParam (required = false, defaultValue = "false")
                                                     boolean         forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                     boolean         forDuplicateProcessing,
                                              @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getNoteLogsByName(serverName, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param elementGUID element to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NoteLogsResponse getNoteLogsForElement(@PathVariable String          serverName,
                                                  @PathVariable String          elementGUID,
                                                  @RequestParam int             startFrom,
                                                  @RequestParam int             pageSize,
                                                  @RequestParam (required = false)
                                                             String                         viewServiceURLMarker,
                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                             String                         accessServiceURLMarker,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                         boolean         forLineage,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                         boolean         forDuplicateProcessing,
                                                  @RequestBody (required = false)
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteLogsForElement(serverName, elementGUID, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NoteLogResponse getNoteLogByGUID(@PathVariable String                        serverName,
                                            @PathVariable String                        noteLogGUID,
                                            @RequestParam (required = false)
                                                       String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                       String                         accessServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteLogByGUID(serverName, noteLogGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NotesResponse findNotes(@PathVariable String                  serverName,
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
                                   @RequestParam (required = false)
                                              String                         viewServiceURLMarker,
                                   @RequestParam (required = false, defaultValue = "asset-manager")
                                              String                         accessServiceURLMarker,
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
                                 viewServiceURLMarker,
                                 accessServiceURLMarker,
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NotesResponse getNotesForNoteLog(@PathVariable String                        serverName,
                                            @PathVariable String                        noteLogGUID,
                                            @RequestParam int                           startFrom,
                                            @RequestParam int                           pageSize,
                                            @RequestParam (required = false)
                                                       String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                       String                         accessServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                   boolean                       forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNotesForNoteLog(serverName, noteLogGUID, startFrom, pageSize, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param noteGUID unique identifier of the requested metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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

    public NoteResponse getNoteByGUID(@PathVariable String                        serverName,
                                      @PathVariable String                        noteGUID,
                                      @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forLineage,
                                      @RequestParam (required = false, defaultValue = "false")
                                             boolean                       forDuplicateProcessing,
                                      @RequestBody(required = false)
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteByGUID(serverName, noteGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }
}

