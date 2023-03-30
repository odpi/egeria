/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FeedbackProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TagProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagUpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.server.CollaborationExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The CollaborationExchangeResource provides part of the server-side implementation of the Asset Manager Open Metadata
 * Assess Service (OMAS).  This interface provides collaboration elements support - such as comments, likes, ratings, informal tags.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/asset-manager/overview/"))

public class CollaborationExchangeResource
{
    private final CollaborationExchangeRESTServices restAPI = new CollaborationExchangeRESTServices();

    /**
     * Default constructor
     */
    public CollaborationExchangeResource()
    {
    }
    

    /**
     * Adds a reply to a comment.
     *
     * @param serverName    name of the server instances for this request.
     * @param userId        String - userId of user making request.
     * @param elementGUID     String - unique id of element that this chain of comments is linked.
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

    @Operation(summary="addCommentReply",
               description="Adds a reply to a comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentReply(@PathVariable String             serverName,
                                        @PathVariable String             userId,
                                        @PathVariable String             elementGUID,
                                        @PathVariable String             commentGUID,
                                        @RequestBody CommentProperties requestBody)
    {
        return restAPI.addCommentReply(serverName, userId, elementGUID, commentGUID, requestBody);
    }


    /**
     * Creates a comment and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param userId      String - userId of user making request.
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

    @Operation(summary="addCommentToElement",
               description="Creates a comment and attaches it to an element.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public GUIDResponse addCommentToElement(@PathVariable String             serverName,
                                          @PathVariable String             userId,
                                          @PathVariable String             elementGUID,
                                          @RequestBody CommentProperties requestBody)
    {
        return restAPI.addCommentToElement(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an element.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param elementGUID   String - unique id for the element.
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

    public VoidResponse addLikeToElement(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              elementGUID,
                                       @RequestBody FeedbackProperties requestBody)
    {
        return restAPI.addLikeToElement(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the element.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param elementGUID        String - unique id for the element.
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

    public VoidResponse addRatingToElement(@PathVariable String            serverName,
                                         @PathVariable String            userId,
                                         @PathVariable String            elementGUID,
                                         @RequestBody RatingProperties requestBody)
    {
        return restAPI.addRatingToElement(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Adds an informal tag (either private of public) to an element.
     *
     * @param serverName       name of the server instances for this request.
     * @param userId           userId of user making request.
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

    public VoidResponse addTagToElement(@PathVariable String              serverName,
                                      @PathVariable String              userId,
                                      @PathVariable String              elementGUID,
                                      @PathVariable String              tagGUID,
                                      @RequestBody FeedbackProperties requestBody)
    {
        return restAPI.addTagToElement(serverName, userId, elementGUID, tagGUID, requestBody);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param requestBody  public/private flag, name of the tag and (optional) description of the tag.
     *
     * @return new elementGUID or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags")

    @Operation(summary="createTag",
               description="Creates a new informal tag and returns the unique identifier for it.",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public GUIDResponse createTag(@PathVariable String         serverName,
                                  @PathVariable String         userId,
                                  @RequestBody TagProperties requestBody)
    {
        return restAPI.createTag(serverName, userId, requestBody);
    }


    /**
     * Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       String - userId of user making request.
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
                                    @PathVariable                   String          userId,
                                    @PathVariable                   String          tagGUID,
                                    @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, userId, tagGUID, requestBody);
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one of its schema elements.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
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
                                           @PathVariable String userId,
                                           @PathVariable String tagGUID,
                                           @RequestParam int    startFrom,
                                           @RequestParam int    pageSize)

    {
        return restAPI.getElementsByTag(serverName, userId, tagGUID, startFrom, pageSize);
    }


    /**
     * Return the informal tag for the supplied unique identifier (tagGUID).
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of the user making the request.
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
                                      @PathVariable String   userId,
                                      @PathVariable String   tagGUID)
    {
        return restAPI.getTag(serverName, userId, tagGUID);
    }


    /**
     * Return the tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
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
                                              @PathVariable String          userId,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize,
                                              @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getTagsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of the calling user's private informal tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
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
                                                @PathVariable String          userId,
                                                @RequestParam int             startFrom,
                                                @RequestParam int             pageSize,
                                                @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMyTagsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of informal tags containing the supplied string in either the name or description. The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param requestBody name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-search-string")

    @Operation(summary="findTags",
               description="Return the list of informal tags containing the supplied string in either the name or description. The search string is a regular expression (RegEx).",
               externalDocs=@ExternalDocumentation(description="Informal Tag",
                                                   url="https://egeria-project.org/concepts/informal-tag/"))

    public InformalTagsResponse findTags(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @RequestParam int                     startFrom,
                                         @RequestParam int                     pageSize,
                                         @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findTags(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.  The search string is a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param requestBody name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
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
                                           @PathVariable String                  userId,
                                           @RequestParam int                     startFrom,
                                           @RequestParam int                     pageSize,
                                           @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findMyTags(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Removes a comment added to the element by this user.  This deletes the link to the comment, the comment itself and any comment replies attached to it.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param elementGUID  String - unique id for the element object
     * @param commentGUID  String - unique id for the comment object
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

    public VoidResponse removeCommentFromElement(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          elementGUID,
                                               @PathVariable                  String          commentGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeCommentFromElement(serverName, userId, elementGUID, commentGUID, requestBody);
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @GetMapping(path = "/comments/{commentGUID}")

    public CommentElementResponse getComment(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String commentGUID)
    {
        return restAPI.getComment(serverName, userId, commentGUID);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @GetMapping(path = "/elements/{elementGUID}/comments")

    public CommentElementsResponse getAttachedComments(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String elementGUID,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.getAttachedComments(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
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
                                              @PathVariable                  String          userId,
                                              @PathVariable                  String          elementGUID,
                                              @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeLikeFromElement(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Removes of a star rating/review that was added to the element by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
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
                                                @PathVariable                  String          userId,
                                                @PathVariable                  String          elementGUID,
                                                @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeRatingFromElement(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Removes a link between a tag and an element that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
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

    public VoidResponse   removeTagFromElement(@PathVariable                  String          serverName,
                                             @PathVariable                  String          userId,
                                             @PathVariable                  String          elementGUID,
                                             @PathVariable                  String          tagGUID,
                                             @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeTagFromElement(serverName, userId, elementGUID, tagGUID, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comment is attached to (directly or indirectly).
     * @param commentGUID  unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "elements/{elementGUID}/comments/{commentGUID}/update")

    @Operation(summary="updateComment",
               description="Update an existing comment.",
               externalDocs=@ExternalDocumentation(description="Element Feedback",
                                                   url="https://egeria-project.org/patterns/metadata-manager/overview/#asset-feedback"))

    public VoidResponse   updateComment(@PathVariable String             serverName,
                                        @PathVariable String             userId,
                                        @PathVariable String             elementGUID,
                                        @PathVariable String             commentGUID,
                                        @RequestBody CommentProperties requestBody)
    {
        return restAPI.updateComment(serverName, userId, elementGUID, commentGUID, requestBody);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param tagGUID      unique id for the tag.
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

    public VoidResponse   updateTagDescription(@PathVariable String                serverName,
                                               @PathVariable String                userId,
                                               @PathVariable String                tagGUID,
                                               @RequestBody InformalTagUpdateRequestBody requestBody)
    {
        return restAPI.updateTagDescription(serverName, userId, tagGUID, requestBody);
    }
}
