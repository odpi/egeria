/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.feedbackmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.server.FeedbackManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The FeedbackManagerResource provides the Spring API endpoints of the Feedback Manager Open Metadata View Service (OMVS).
= */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
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
     * @param requestBody   containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments/{commentGUID}/replies")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addCommentReply",
               description="Adds a reply to a comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentReply(@PathVariable String                         serverName,
                                        @PathVariable String                         urlMarker,
                                        @PathVariable String                         elementGUID,
                                        @PathVariable String                         commentGUID,
                                        @RequestBody(required = false) NewAttachmentRequestBody requestBody)
    {
        return restAPI.addCommentReply(serverName, urlMarker, elementGUID, commentGUID, requestBody);
    }


    /**
     * Creates a comment and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID        String - unique id for the element.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addCommentToElement",
               description="Creates a comment and attaches it to an element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentToElement(@PathVariable String                 serverName,
                                            @PathVariable String                 urlMarker,
                                            @PathVariable String                 elementGUID,
                                            @RequestBody(required = false)  NewAttachmentRequestBody requestBody)
    {
        return restAPI.addCommentToElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an element.
     *
     * @param serverName  name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID   String - unique id for the element.
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/likes")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addLikeToElement",
               description="Creates a <i>like</i> object and attaches it to an element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse addLikeToElement(@PathVariable String         serverName,
                                         @PathVariable String                        urlMarker,
                                         @PathVariable String         elementGUID,
                                         @RequestBody (required = false)
                                             UpdateElementRequestBody requestBody)
    {
        return restAPI.addLikeToElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the element.
     *
     * @param serverName  name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID String - unique id for the element.
     * @param requestBody containing the StarRating and user review of element.
     *
     * @return elementGUID for new review object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/ratings")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addRatingToElement",
               description="Adds, or updates, a star rating and optional review text to the element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse addRatingToElement(@PathVariable String           serverName,
                                           @PathVariable String           urlMarker,
                                           @PathVariable String           elementGUID,
                                           @RequestBody(required = false)  UpdateElementRequestBody requestBody)
    {
        return restAPI.addRatingToElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Adds an informal tag (either private of public) to an element.
     *
     * @param serverName       name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param requestBody      optional effective time
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/tags/{tagGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addTagToElement",
               description="Adds an informal tag (either private of public) to an element.",
               externalDocs=@ExternalDocumentation(description="Element Classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public VoidResponse addTagToElement(@PathVariable String             serverName,
                                        @PathVariable String             urlMarker,
                                        @PathVariable String             elementGUID,
                                        @PathVariable String             tagGUID,
                                        @RequestBody (required = false)
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.addTagToElement(serverName, urlMarker, elementGUID, tagGUID, requestBody);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody  public/private flag, name of the tag and (optional) description of the tag.
     *
     * @return new elementGUID or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createInformalTag",
               description="Creates a new informal tag and returns the unique identifier for it.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public GUIDResponse createInformalTag(@PathVariable String                serverName,
                                          @PathVariable String               urlMarker,
                                          @RequestBody(required = false) NewElementRequestBody requestBody)
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
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteTag",
               description="Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.  A private tag can be deleted by its creator and all the references are lost; a public tag can be deleted by anyone, but only if it is not attached to any referenceable.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public VoidResponse   deleteTag(@PathVariable                   String          serverName,
                                    @PathVariable String                        urlMarker,
                                    @PathVariable                   String          tagGUID,
                                    @RequestBody(required = false)
                                        DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, urlMarker, tagGUID, requestBody);
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param tagGUID unique identifier of tag.
     * @param requestBody optional effective time
     *
     * @return element stubs list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/by-tag/{tagGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsByTag",
               description="Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.",
               externalDocs=@ExternalDocumentation(description="Element classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public OpenMetadataRootElementsResponse getElementsByTag(@PathVariable String serverName,
                                                    @PathVariable String                        urlMarker,
                                                    @PathVariable String tagGUID,
                                                    @RequestBody(required = false)
                                                        ResultsRequestBody requestBody)

    {
        return restAPI.getElementsByTag(serverName, urlMarker, tagGUID, requestBody);
    }


    /**
     * Return the informal tag for the supplied unique identifier (tagGUID).
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param tagGUID unique identifier of the meaning.
     * @param requestBody optional effective time
     *
     * @return tag object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTag",
               description="Return the informal tag for the supplied unique identifier (tagGUID).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public OpenMetadataRootElementResponse getTag(@PathVariable String   serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String   tagGUID,
                                      @RequestBody(required = false)
                                          GetRequestBody requestBody)
    {
        return restAPI.getTag(serverName, urlMarker, tagGUID, requestBody);
    }


    /**
     * Return the tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody name of tag.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTagsByName",
               description="Return the tags exactly matching the supplied name.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public OpenMetadataRootElementsResponse getTagsByName(@PathVariable String          serverName,
                                              @PathVariable String                        urlMarker,
                                              @RequestBody(required = false) FilterRequestBody requestBody)
    {
        return restAPI.getTagsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Return the list of comments containing the supplied string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of comment objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/comments/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findComment",
               description="Return the list of comments containing the supplied string. The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of an effective time to restrict the search to element that are/were effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Comment",
                                                   url="https://egeria-project.org/concepts/comment/"))

    public OpenMetadataRootElementsResponse findComments(@PathVariable String                  serverName,
                                                @PathVariable String                        urlMarker,
                                                @RequestBody  (required = false)
                                                    SearchStringRequestBody              requestBody)
    {
        return restAPI.findComments(serverName, urlMarker, requestBody);
    }


    /**
     * Return the list of tags containing the supplied string in the text. The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findTags",
               description="Return the list of informal tags containing the supplied string in their name or description. The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of an effective time to restrict the search to element that are/were effective at a particular time.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public OpenMetadataRootElementsResponse findTags(@PathVariable String                  serverName,
                                         @PathVariable String                        urlMarker,
                                         @RequestBody  (required = false)
                                             SearchStringRequestBody              requestBody)
    {
        return restAPI.findTags(serverName, urlMarker, requestBody);
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.  The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/private/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMyTags",
               description="Return the list of the calling user's private tags containing the supplied string in either the name or description.  The search string is a regular expression (RegEx).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public OpenMetadataRootElementsResponse findMyTags(@PathVariable String                  serverName,
                                           @PathVariable String                        urlMarker,
                                           @RequestBody  (required = false)
                                               SearchStringRequestBody              requestBody)
    {
        return restAPI.findMyTags(serverName, urlMarker, requestBody);
    }


    /**
     * Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param commentGUID  String - unique id for the comment object
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeCommentFromElement",
               description="Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse removeCommentFromElement(@PathVariable String                         serverName,
                                                 @PathVariable String                        urlMarker,
                                                 @PathVariable String                         commentGUID,
                                                 @RequestBody(required = false)
                                                     DeleteElementRequestBody requestBody)
    {
        return restAPI.removeCommentFromElement(serverName, urlMarker,  commentGUID, requestBody);
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param commentGUID  unique identifier for the comment object.
     * @param requestBody optional effective time
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getComment",
            description="Return the requested comment.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementResponse getComment(@PathVariable String                        serverName,
                                                      @PathVariable String                        urlMarker,
                                                      @PathVariable String                        commentGUID,
                                                      @RequestBody(required = false)
                                                          GetRequestBody requestBody)
    {
        return restAPI.getCommentByGUID(serverName, urlMarker, commentGUID, requestBody);
    }


    /**
     * Return the ratings attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param requestBody optional effective time
     * @return list of ratings or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/ratings/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAttachedRatings",
            description="Return the ratings attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getAttachedRatings(@PathVariable String                        serverName,
                                                     @PathVariable String                        urlMarker,
                                                     @PathVariable String                        elementGUID,
                                                     @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getAttachedRatings(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Return the likes attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param requestBody optional effective time
     * @return list of likes or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/likes/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAttachedLikes",
            description="Return the likes attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getAttachedLikes(@PathVariable String                        serverName,
                                                 @PathVariable String                        urlMarker,
                                                 @PathVariable String                        elementGUID,
                                                 @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getAttachedLikes(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param requestBody optional effective time
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/comments/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAttachedComments",
            description="Return the comments attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getAttachedComments(@PathVariable String                        serverName,
                                                       @PathVariable String                        urlMarker,
                                                       @PathVariable String                        elementGUID,
                                                       @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getAttachedComments(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Return the informal tags attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param requestBody optional effective time
     * @return list of informal tags or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/tags/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAttachedTags",
            description="Return the informal tags attached to an element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getAttachedTags(@PathVariable String                        serverName,
                                                @PathVariable String                        urlMarker,
                                                @PathVariable String                        elementGUID,
                                                @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getAttachedTags(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element where the like is attached.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/likes/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeLikeFromElement",
               description="Removes a <i>Like</i> added to the element by this user.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#element-feedback"))

    public VoidResponse   removeLikeFromElement(@PathVariable                  String          serverName,
                                                @PathVariable String                        urlMarker,
                                                @PathVariable                  String          elementGUID,
                                                @RequestBody(required = false)
                                                    DeleteElementRequestBody requestBody)
    {
        return restAPI.removeLikeFromElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Removes of a star rating/review that was added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID         unique identifier for the element where the rating is attached.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/ratings/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeRatingFromElement",
               description="Removes of a star rating/review that was added to the element by this user.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse   removeRatingFromElement(@PathVariable                  String          serverName,
                                                  @PathVariable String                        urlMarker,
                                                  @PathVariable                  String          elementGUID,
                                                  @RequestBody(required = false)
                                                      DeleteElementRequestBody requestBody)
    {
        return restAPI.removeRatingFromElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Removes a link between a tag and an element that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeTagFromElement",
               description="Removes a link between a tag and an element that was added by this user.",
               externalDocs=@ExternalDocumentation(description="Element classifiers",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-classifiers"))

    public VoidResponse removeTagFromElement(@PathVariable                  String          serverName,
                                             @PathVariable String                        urlMarker,
                                             @PathVariable                  String          elementGUID,
                                             @PathVariable                  String          tagGUID,
                                             @RequestBody(required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeTagFromElement(serverName, urlMarker, elementGUID, tagGUID, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param commentGUID  unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/comments/{commentGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateComment",
               description="Update an existing comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse   updateComment(@PathVariable String                         serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String                         commentGUID,
                                        @RequestBody(required = false)  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateComment(serverName, urlMarker, commentGUID, requestBody);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/comments/questions/{questionCommentGUID}/answers/{answerCommentGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setupAcceptedAnswer",
            description="Link a comment that contains the best answer to a question posed in another comment.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse setupAcceptedAnswer(@PathVariable String                  serverName,
                                            @PathVariable String                  urlMarker,
                                            @PathVariable String                  questionCommentGUID,
                                            @PathVariable String                  answerCommentGUID,
                                            @RequestBody (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupAcceptedAnswer(serverName, urlMarker, questionCommentGUID, answerCommentGUID, requestBody);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/comments/questions/{questionCommentGUID}/answers/{answerCommentGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAcceptedAnswer",
            description="Unlink a comment that contains an answer to a question posed in another comment.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse clearAcceptedAnswer(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                        questionCommentGUID,
                                            @PathVariable String                        answerCommentGUID,
                                            @RequestBody  (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.clearAcceptedAnswer(serverName, urlMarker, questionCommentGUID, answerCommentGUID, requestBody);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param tagGUID      unique id for the tag
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateTagDescription",
               description="Updates the description of an existing tag (either private or public).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public VoidResponse   updateTagDescription(@PathVariable String                       serverName,
                                               @PathVariable String                        urlMarker,
                                               @PathVariable String                       tagGUID,
                                               @RequestBody  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateTagDescription(serverName, urlMarker, tagGUID, requestBody);
    }



    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
     * broadcast information.  Notelogs are typically maintained by the owners/stewards of an element.
     */


    /**
     * Creates a new noteLog for an element and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element where the note log is located
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/note-logs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createNoteLog",
            description="Creates a new noteLog for an element and returns the unique identifier for it.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public GUIDResponse createNoteLog(@PathVariable String            serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String            elementGUID,
                                      @RequestBody NewAttachmentRequestBody requestBody)
    {
        return restAPI.createNoteLog(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Creates a new freestanding noteLog and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/note-logs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createNoteLog",
            description="Creates a new freestanding noteLog and returns the unique identifier for it.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public GUIDResponse createNoteLog(@PathVariable String            serverName,
                                      @PathVariable String                        urlMarker,
                                      @RequestBody NewElementRequestBody requestBody)
    {
        return restAPI.createNoteLog(serverName, urlMarker, requestBody);
    }


    /**
     * Update an existing note log.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param noteLogGUID  unique identifier for the note log to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/note-logs/{noteLogGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateNoteLog",
            description="Update an existing note log.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))


    public VoidResponse   updateNoteLog(@PathVariable String                         serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String                         noteLogGUID,
                                        @RequestBody UpdateElementRequestBody requestBody)
    {
        return restAPI.updateNoteLog(serverName, urlMarker, noteLogGUID, requestBody);
    }


    /**
     * Removes a note log from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID   unique id for the note log.
     * @param requestBody  delete request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    @PostMapping(path = "/note-logs/{noteLogGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeNoteLog",
            description="Removes a note log from the repository.",
            externalDocs=@ExternalDocumentation(description="Note Logs",
                    url="https://egeria-project.org/concepts/note-log/"))

    public VoidResponse   removeNoteLog(@PathVariable String          serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String          noteLogGUID,
                                        @RequestBody (required = false)
                                            DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteNoteLog(serverName, urlMarker, noteLogGUID, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findNoteLogs",
            description="Retrieve the list of note log metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse findNoteLogs(@PathVariable String                  serverName,
                                                         @PathVariable String                        urlMarker,
                                                         @RequestBody  (required = false)
                                                             SearchStringRequestBody              requestBody)
    {
        return restAPI.findNoteLogs(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNoteLogsByName",
            description="Retrieve the list of note log metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getNoteLogsByName(@PathVariable String          serverName,
                                                              @PathVariable String            urlMarker,
                                                              @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getNoteLogsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID element to start from
     * @param requestBody optional effective time
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/elements/{elementGUID}/note-logs/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNoteLogsForElement",
            description="Retrieve the list of note log metadata elements attached to the element.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getNoteLogsForElement(@PathVariable String          serverName,
                                                                  @PathVariable String                        urlMarker,
                                                                  @PathVariable String          elementGUID,
                                                                  @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getNoteLogsForElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param requestBody optional effective time
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/{noteLogGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNoteLogByGUID",
            description="Retrieve the note log metadata element with the supplied unique identifier.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementResponse getNoteLogByGUID(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                        noteLogGUID,
                                            @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getNoteLogByGUID(serverName, urlMarker, noteLogGUID, requestBody);
    }


    /* ===============================================================================
     * A note log typically contains many notes, linked with relationships.
     */


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param noteLogGUID unique identifier of the note log of interest
     * @param requestBody optional effective time
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/note-logs/{noteLogGUID}/notes/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNotesForNoteLog",
            description="Retrieve the list of notes associated with a note log.",
            externalDocs=@ExternalDocumentation(description="Element Feedback",
                    url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public OpenMetadataRootElementsResponse getNotesForNoteLog(@PathVariable String                        serverName,
                                                               @PathVariable String                        urlMarker,
                                                               @PathVariable String                        noteLogGUID,
                                                               @RequestBody(required = false)
                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getNotesForNoteLog(serverName, urlMarker, noteLogGUID, requestBody);
    }
}

