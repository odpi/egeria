/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.feedbackmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.rest.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.server.FeedbackManagerRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The FeedbackManagerResource provides the Spring API endpoints of the Feedback Manager Open Metadata View Service (OMVS).
= */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Feedback Manager OMVS",
     description="Add comments, reviews, tags and notes to elements of interest.",
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
     * @param urlMarker  view service URL marker
     * @param elementGUID        String - unique id for the anchor element.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody   containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments/{commentGUID}/replies")

    @Operation(summary="addCommentReply",
               description="Adds a reply to a comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentReply(@PathVariable String                         serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String                         elementGUID,
                                        @PathVariable String                         commentGUID,
                                        @RequestParam (required = false, defaultValue = "false")
                                            boolean                        isPublic,
                                        @RequestParam (required = false)
                                            String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                            String                         accessServiceURLMarker,
                                        @RequestBody ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.addCommentReply(serverName, elementGUID, commentGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Creates a comment and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                         elementGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                boolean                        isPublic,
                                            @RequestParam (required = false)
                                                String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                String                         accessServiceURLMarker,
                                            @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.addCommentToElement(serverName, elementGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an element.
     *
     * @param serverName  name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID   String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
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
                                         @PathVariable String                        urlMarker,
                                         @PathVariable String         elementGUID,
                                         @RequestParam (required = false, defaultValue = "false")
                                             boolean                        isPublic,
                                         @RequestParam (required = false)
                                             String                         viewServiceURLMarker,
                                         @RequestParam (required = false, defaultValue = "asset-manager")
                                             String                         accessServiceURLMarker,
                                         @RequestBody (required = false)
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.addLikeToElement(serverName, elementGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the element.
     *
     * @param serverName  name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
               description="Adds, or updates, a star rating and optional review text to the element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse addRatingToElement(@PathVariable String           serverName,
                                           @PathVariable String                        urlMarker,
                                           @PathVariable String           elementGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                               boolean                        isPublic,
                                           @RequestParam (required = false)
                                               String                         viewServiceURLMarker,
                                           @RequestParam (required = false, defaultValue = "asset-manager")
                                               String                         accessServiceURLMarker,
                                           @RequestBody  RatingProperties requestBody)
    {
        return restAPI.addRatingToElement(serverName, elementGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Adds an informal tag (either private of public) to an element.
     *
     * @param serverName       name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody      optional effective time
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
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String             elementGUID,
                                        @PathVariable String             tagGUID,
                                        @RequestParam (required = false, defaultValue = "false")
                                            boolean                        isPublic,
                                        @RequestParam (required = false)
                                            String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                            String                         accessServiceURLMarker,
                                        @RequestBody (required = false)
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.addTagToElement(serverName, elementGUID, tagGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                          @PathVariable String                        urlMarker,
                                          @RequestParam (required = false)
                                          String                         viewServiceURLMarker,
                                          @RequestParam (required = false, defaultValue = "asset-manager")
                                              String                         accessServiceURLMarker,
                                          @RequestBody TagProperties requestBody)
    {
        return restAPI.createInformalTag(serverName, urlMarker, requestBody);
    }


    /**
     * Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
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
                                    @PathVariable String                        urlMarker,
                                    @PathVariable                   String          tagGUID,
                                    @RequestParam (required = false)
                                        String                         viewServiceURLMarker,
                                    @RequestParam (required = false, defaultValue = "asset-manager")
                                        String                         accessServiceURLMarker,
                                    @RequestBody(required = false)
                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, tagGUID, urlMarker, requestBody);
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return element stubs list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/by-tag/{tagGUID}/retrieve")

    @Operation(summary="getElementsByTag",
               description="Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.",
               externalDocs=@ExternalDocumentation(description="Element classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public RelatedElementsResponse getElementsByTag(@PathVariable String serverName,
                                                    @PathVariable String                        urlMarker,
                                                    @PathVariable String tagGUID,
                                                    @RequestParam int    startFrom,
                                                    @RequestParam int    pageSize,
                                                    @RequestParam (required = false)
                                                    String                         viewServiceURLMarker,
                                                    @RequestParam (required = false, defaultValue = "asset-manager")
                                                    String                         accessServiceURLMarker,
                                                    @RequestBody(required = false)
                                                        EffectiveTimeQueryRequestBody requestBody)

    {
        return restAPI.getElementsByTag(serverName, tagGUID, startFrom, pageSize, urlMarker, requestBody);
    }


    /**
     * Return the informal tag for the supplied unique identifier (tagGUID).
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param tagGUID unique identifier of the meaning.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return tag object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/retrieve")

    @Operation(summary="getTag",
               description="Return the informal tag for the supplied unique identifier (tagGUID).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagResponse getTag(@PathVariable String   serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String   tagGUID,
                                      @RequestParam (required = false)
                                          String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                          String                         accessServiceURLMarker,
                                      @RequestBody(required = false)
                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getTag(serverName, tagGUID, urlMarker, requestBody);
    }


    /**
     * Return the tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                              @PathVariable String                        urlMarker,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize,
                                              @RequestParam (required = false)
                                                  String                         viewServiceURLMarker,
                                              @RequestParam (required = false, defaultValue = "asset-manager")
                                                  String                         accessServiceURLMarker,
                                              @RequestBody FilterRequestBody requestBody)
    {
        return restAPI.getTagsByName(serverName, startFrom, pageSize, urlMarker, requestBody);
    }


    /**
     * Return the list of comments containing the supplied string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                @PathVariable String                        urlMarker,
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
                                                @RequestBody  (required = false)
                                                    FilterRequestBody              requestBody)
    {
        return restAPI.findComments(serverName,
                                    startFrom,
                                    pageSize,
                                    startsWith,
                                    endsWith,
                                    ignoreCase,
                                    urlMarker,
                                    requestBody);
    }


    /**
     * Return the list of tags containing the supplied string in the text. The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                         @PathVariable String                        urlMarker,
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
                                         @RequestBody  (required = false)
                                             FilterRequestBody              requestBody)
    {
        return restAPI.findTags(serverName,
                                startFrom,
                                pageSize,
                                startsWith,
                                endsWith,
                                ignoreCase,
                                urlMarker,
                                requestBody);
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.  The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                           @PathVariable String                        urlMarker,
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
                                           @RequestBody  (required = false)
                                               FilterRequestBody              requestBody)
    {
        return restAPI.findMyTags(serverName,
                                  startFrom,
                                  pageSize,
                                  startsWith,
                                  endsWith,
                                  ignoreCase,
                                  urlMarker,
                                  requestBody);
    }


    /**
     * Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param commentGUID  String - unique id for the comment object
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/remove")

    @Operation(summary="removeCommentFromElement",
               description="Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse removeCommentFromElement(@PathVariable String                         serverName,
                                                 @PathVariable String                        urlMarker,
                                                 @PathVariable String                         commentGUID,
                                                 @RequestParam (required = false)
                                                     String                         viewServiceURLMarker,
                                                 @RequestParam (required = false, defaultValue = "asset-manager")
                                                     String                         accessServiceURLMarker,
                                                 @RequestBody(required = false)
                                                                EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.removeCommentFromElement(serverName,  commentGUID, urlMarker, requestBody);
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param commentGUID  unique identifier for the comment object.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/retrieve")

    @Operation(summary="getComment",
            description="Return the requested comment.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public CommentResponse getComment(@PathVariable String                        serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String                        commentGUID,
                                      @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker,
                                      @RequestBody(required = false)
                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getCommentByGUID(serverName, commentGUID, urlMarker, requestBody);
    }


    /**
     * Return the ratings attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return list of ratings or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/ratings/retrieve")

    @Operation(summary="getAttachedComments",
            description="Return the ratings attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public RatingElementsResponse getAttachedRatings(@PathVariable String                        serverName,
                                                     @PathVariable String                        urlMarker,
                                                     @PathVariable String                        elementGUID,
                                                     @RequestParam int                           startFrom,
                                                     @RequestParam int                           pageSize,
                                                     @RequestParam (required = false)
                                                           String                         viewServiceURLMarker,
                                                     @RequestParam (required = false, defaultValue = "asset-manager")
                                                           String                         accessServiceURLMarker,
                                                     @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAttachedRatings(serverName, elementGUID, startFrom, pageSize, urlMarker, requestBody);
    }



    /**
     * Return the likes attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return list of likes or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/likes/retrieve")

    @Operation(summary="getAttachedLikes",
            description="Return the likes attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public LikeElementsResponse getAttachedLikes(@PathVariable String                        serverName,
                                                 @PathVariable String                        urlMarker,
                                                 @PathVariable String                        elementGUID,
                                                 @RequestParam int                           startFrom,
                                                 @RequestParam int                           pageSize,
                                                 @RequestParam (required = false)
                                                     String                         viewServiceURLMarker,
                                                 @RequestParam (required = false, defaultValue = "asset-manager")
                                                       String                         accessServiceURLMarker,
                                                 @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAttachedLikes(serverName, elementGUID, startFrom, pageSize, urlMarker, requestBody);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments/retrieve")

    @Operation(summary="getAttachedComments",
            description="Return the comments attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public CommentElementsResponse getAttachedComments(@PathVariable String                        serverName,
                                                       @PathVariable String                        urlMarker,
                                                       @PathVariable String                        elementGUID,
                                                       @RequestParam int                           startFrom,
                                                       @RequestParam int                           pageSize,
                                                       @RequestParam (required = false)
                                                       String                         viewServiceURLMarker,
                                                       @RequestParam (required = false, defaultValue = "asset-manager")
                                                       String                         accessServiceURLMarker,
                                                       @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAttachedComments(serverName, elementGUID, startFrom, pageSize, urlMarker, requestBody);
    }



    /**
     * Return the informal tags attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return list of informal tags or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/tags/retrieve")

    @Operation(summary="getAttachedTags",
            description="Return the informal tags attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public InformalTagsResponse getAttachedTags(@PathVariable String                        serverName,
                                                @PathVariable String                        urlMarker,
                                                @PathVariable String                        elementGUID,
                                                @RequestParam int                           startFrom,
                                                       @RequestParam int                           pageSize,
                                                       @RequestParam (required = false)
                                                       String                         viewServiceURLMarker,
                                                       @RequestParam (required = false, defaultValue = "asset-manager")
                                                       String                         accessServiceURLMarker,
                                                       @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAttachedTags(serverName, elementGUID, startFrom, pageSize, urlMarker, requestBody);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                                @PathVariable String                        urlMarker,
                                                @PathVariable                  String          elementGUID,
                                                @RequestParam (required = false)
                                                    String                         viewServiceURLMarker,
                                                @RequestParam (required = false, defaultValue = "asset-manager")
                                                    String                         accessServiceURLMarker,
                                                @RequestBody(required = false)
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.removeLikeFromElement(serverName, elementGUID, urlMarker, requestBody);
    }


    /**
     * Removes of a star rating/review that was added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                                  @PathVariable String                        urlMarker,
                                                  @PathVariable                  String          elementGUID,
                                                  @RequestParam (required = false)
                                                      String                         viewServiceURLMarker,
                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                      String                         accessServiceURLMarker,
                                                  @RequestBody(required = false)
                                                      EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.removeRatingFromElement(serverName, elementGUID, urlMarker, requestBody);
    }


    /**
     * Removes a link between a tag and an element that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
                                             @PathVariable String                        urlMarker,
                                             @PathVariable                  String          elementGUID,
                                             @PathVariable                  String          tagGUID,
                                             @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                             @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker,
                                             @RequestBody(required = false)
                                                 EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.removeTagFromElement(serverName, elementGUID, tagGUID, urlMarker, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param commentGUID  unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String                         commentGUID,
                                        @RequestParam (required = false, defaultValue = "false")
                                                      boolean                        isMergeUpdate,
                                        @RequestParam (required = false)
                                                      String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                                      String                         accessServiceURLMarker,
                                        @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateComment(serverName, commentGUID, isMergeUpdate, urlMarker, requestBody);
    }


    /**
     * Update an existing comment's visibility.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param commentGUID  unique identifier for the comment to change.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/parents/{parentGUID}/comments/{commentGUID}/update-visibility")

    @Operation(summary="updateCommentVisibility",
            description="Update an existing comment's visibility.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse   updateCommentVisibility(@PathVariable String               serverName,
                                                  @PathVariable String                        urlMarker,
                                                  @PathVariable String               parentGUID,
                                                  @PathVariable String               commentGUID,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                      boolean                        isPublic,
                                                  @RequestParam (required = false)
                                                      String                         viewServiceURLMarker,
                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                      String                         accessServiceURLMarker,
                                                  @RequestBody  (required = false)
                                                      EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.updateCommentVisibility(serverName, parentGUID, commentGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/comments/questions/{questionCommentGUID}/answers/{answerCommentGUID}")

    @Operation(summary="setupAcceptedAnswer",
            description="Link a comment that contains the best answer to a question posed in another comment.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse setupAcceptedAnswer(@PathVariable String                  serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                  questionCommentGUID,
                                            @PathVariable String                  answerCommentGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                boolean                        isPublic,
                                            @RequestParam (required = false)
                                                String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                String                         accessServiceURLMarker,
                                            @RequestBody (required = false)
                                                EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.setupAcceptedAnswer(serverName,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           isPublic,
                                           urlMarker,
                                           requestBody);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/comments/questions/{questionCommentGUID}/answers/{answerCommentGUID}/remove")

    @Operation(summary="clearAcceptedAnswer",
            description="Unlink a comment that contains an answer to a question posed in another comment.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse clearAcceptedAnswer(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                        questionCommentGUID,
                                            @PathVariable String                        answerCommentGUID,
                                            @RequestParam (required = false)
                                                          String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                          String                         accessServiceURLMarker,
                                            @RequestBody  (required = false)
                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearAcceptedAnswer(serverName,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           urlMarker,
                                           requestBody);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
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
                                               @PathVariable String                        urlMarker,
                                               @PathVariable String                       tagGUID,
                                               @RequestParam (required = false)
                                                   String                         viewServiceURLMarker,
                                               @RequestParam (required = false, defaultValue = "asset-manager")
                                                   String                         accessServiceURLMarker,
                                               @RequestBody  InformalTagUpdateRequestBody requestBody)
    {
        return restAPI.updateTagDescription(serverName, tagGUID, urlMarker, requestBody);
    }



    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
     * broadcast information.  Notelogs are typically maintained by the owners/stewards of an element.
     */


    /**
     * Creates a new noteLog and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element where the note log is located
     * @param isPublic                 is this element visible to other people.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/note-logs")

    @Operation(summary="createNoteLog",
            description="Creates a new noteLog and returns the unique identifier for it.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public GUIDResponse createNoteLog(@PathVariable String            serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String            elementGUID,
                                      @RequestParam (required = false, defaultValue = "false")
                                          boolean                        isPublic,
                                      @RequestParam (required = false)
                                          String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                          String            accessServiceURLMarker,
                                      @RequestBody NoteLogProperties requestBody)
    {
        return restAPI.createNoteLog(serverName, elementGUID, isPublic, urlMarker, requestBody);
    }


    /**
     * Update an existing note log.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param noteLogGUID  unique identifier for the note log to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/note-logs/{noteLogGUID}")

    @Operation(summary="updateNoteLog",
            description="Update an existing note log.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))


    public VoidResponse   updateNoteLog(@PathVariable String                         serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String                         noteLogGUID,
                                        @RequestParam boolean                        isMergeUpdate,
                                        @RequestParam (required = false)
                                        String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                        String                         accessServiceURLMarker,
                                        @RequestBody ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateNoteLog(serverName, noteLogGUID, isMergeUpdate, urlMarker, requestBody);
    }


    /**
     * Removes a note log from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID   unique id for the note log.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    @PostMapping(path = "/note-logs/{noteLogGUID}/remove")

    @Operation(summary="removeNoteLog",
            description="Removes a note log from the repository.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public VoidResponse   removeNoteLog(@PathVariable String          serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String          noteLogGUID,
                                        @RequestParam (required = false)
                                        String                         viewServiceURLMarker,
                                        @RequestParam (required = false, defaultValue = "asset-manager")
                                        String          accessServiceURLMarker,
                                        @RequestBody (required = false)
                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.deleteNoteLog(serverName, noteLogGUID, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody search string and effective time.
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/by-search-string")

    @Operation(summary="findNoteLogs",
            description="Retrieve the list of note log metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NoteLogsResponse findNoteLogs(@PathVariable String                  serverName,
                                         @PathVariable String                        urlMarker,
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
                                         @RequestBody  (required = false)
                                             FilterRequestBody              requestBody)
    {
        return restAPI.findNoteLogs(serverName,
                                    startFrom,
                                    pageSize,
                                    startsWith,
                                    endsWith,
                                    ignoreCase,
                                    urlMarker,
                                    requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/by-name")

    @Operation(summary="getNoteLogsByName",
            description="Retrieve the list of note log metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NoteLogsResponse getNoteLogsByName(@PathVariable String          serverName,
                                              @PathVariable String                        urlMarker,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize,
                                              @RequestParam (required = false)
                                                         String                         viewServiceURLMarker,
                                              @RequestParam (required = false, defaultValue = "asset-manager")
                                                         String                         accessServiceURLMarker,
                                              @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getNoteLogsByName(serverName, startFrom, pageSize, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID element to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/elements/{elementGUID}/note-logs/retrieve")

    @Operation(summary="getNoteLogsForElement",
            description="Retrieve the list of note log metadata elements attached to the element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NoteLogsResponse getNoteLogsForElement(@PathVariable String          serverName,
                                                  @PathVariable String                        urlMarker,
                                                  @PathVariable String          elementGUID,
                                                  @RequestParam int             startFrom,
                                                  @RequestParam int             pageSize,
                                                  @RequestParam (required = false)
                                                             String                         viewServiceURLMarker,
                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                             String                         accessServiceURLMarker,
                                                  @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteLogsForElement(serverName, elementGUID, startFrom, pageSize, urlMarker,requestBody);
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/{noteLogGUID}/retrieve")

    @Operation(summary="getNoteLogByGUID",
            description="Retrieve the note log metadata element with the supplied unique identifier.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NoteLogResponse getNoteLogByGUID(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                        noteLogGUID,
                                            @RequestParam (required = false)
                                                          String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                          String                         accessServiceURLMarker,
                                            @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteLogByGUID(serverName, noteLogGUID, urlMarker, requestBody);
    }


    /* ===============================================================================
     * A note log typically contains many notes, linked with relationships.
     */


    /**
     * Creates a new note for a note log and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID unique identifier of the  note log
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/note-logs/{noteLogGUID}/notes")

    @Operation(summary="createNote",
            description="Creates a new note in the note log and returns the unique identifier for it.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public GUIDResponse createNote(@PathVariable String         serverName,
                                   @PathVariable String                        urlMarker,
                                   @PathVariable String         noteLogGUID,
                                   @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                   @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String         accessServiceURLMarker,
                                   @RequestBody NoteProperties requestBody)
    {
        return restAPI.createNote(serverName, noteLogGUID, urlMarker, requestBody);
    }


    /**
     * Update an existing note.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param noteGUID  unique identifier for the note to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/notes/{noteGUID}")

    @Operation(summary="updateNote",
            description="Update an existing note.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public VoidResponse   updateNote(@PathVariable String                         serverName,
                                     @PathVariable String                        urlMarker,
                                     @PathVariable String                         noteGUID,
                                     @RequestParam boolean                        isMergeUpdate,
                                     @RequestParam (required = false)
                                                   String                         viewServiceURLMarker,
                                     @RequestParam (required = false, defaultValue = "asset-manager")
                                                   String                         accessServiceURLMarker,
                                     @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateNote(serverName, noteGUID, isMergeUpdate, urlMarker, requestBody);
    }


    /**
     * Removes a note from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteGUID   unique id for the note .
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/notes/{noteGUID}/remove")

    @Operation(summary="removeNote",
            description="Remove an existing note.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public VoidResponse   removeNote(@PathVariable String          serverName,
                                     @PathVariable String                        urlMarker,
                                     @PathVariable String          noteGUID,
                                     @RequestParam (required = false)
                                     String                         viewServiceURLMarker,
                                     @RequestParam (required = false, defaultValue = "asset-manager")
                                     String          accessServiceURLMarker,
                                     @RequestBody(required = false)
                                         EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.deleteNote(serverName, noteGUID, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody search string and effective time.
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/notes/by-search-string")

    @Operation(summary="findNotes",
            description="Retrieve the list of note metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NotesResponse findNotes(@PathVariable String                  serverName,
                                   @PathVariable String                        urlMarker,
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
                                   @RequestBody  (required = false)
                                       FilterRequestBody              requestBody)
    {
        return restAPI.findNotes(serverName,
                                 startFrom,
                                 pageSize,
                                 startsWith,
                                 endsWith,
                                 ignoreCase,
                                 urlMarker,
                                 requestBody);
    }


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/{noteLogGUID}/notes/retrieve")

    @Operation(summary="getNotesForNoteLog",
            description="Retrieve the list of notes associated with a note log.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NotesResponse getNotesForNoteLog(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                        noteLogGUID,
                                            @RequestParam int                           startFrom,
                                            @RequestParam int                           pageSize,
                                            @RequestParam (required = false)
                                                       String                         viewServiceURLMarker,
                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                       String                         accessServiceURLMarker,
                                            @RequestBody(required = false)
                                                EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNotesForNoteLog(serverName, noteLogGUID, startFrom, pageSize, urlMarker, requestBody);
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteGUID unique identifier of the requested metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/notes/{noteGUID}/retrieve")

    @Operation(summary="getNoteByGUID",
            description=" Retrieve the note metadata element with the supplied unique identifier.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public NoteResponse getNoteByGUID(@PathVariable String                        serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String                        noteGUID,
                                      @RequestParam (required = false)
                                                 String                         viewServiceURLMarker,
                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                                 String                         accessServiceURLMarker,
                                      @RequestBody(required = false) EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNoteByGUID(serverName, noteGUID, urlMarker, requestBody);
    }
}

