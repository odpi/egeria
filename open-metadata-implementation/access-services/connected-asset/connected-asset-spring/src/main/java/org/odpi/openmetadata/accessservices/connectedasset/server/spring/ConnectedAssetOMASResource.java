/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server.spring;

import org.odpi.openmetadata.accessservices.connectedasset.rest.*;
import org.odpi.openmetadata.accessservices.connectedasset.server.ConnectedAssetRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/connected-asset/users/{userId}")
public class ConnectedAssetOMASResource
{
    private ConnectedAssetRESTServices restAPI = new ConnectedAssetRESTServices();

    /**
     * Default constructor
     */
    public ConnectedAssetOMASResource()
    {
    }


    /**
     * Returns the basic information about the asset.
     *
     * @param serverName String   name of server instance to call.
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     *
     * @return a bean with the basic properties about the asset or
     * InvalidParameterException - the userId is null or invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}")

    public AssetResponse getAssetSummary(@PathVariable String   serverName,
                                         @PathVariable String   userId,
                                         @PathVariable String   assetGUID)
    {
        return restAPI.getAssetSummary(serverName, userId, assetGUID);
    }


    /**
     * Returns the list of annotations for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of annotations  or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/annotations")

    public AnnotationsResponse getAnnotations(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  assetGUID,
                                              @RequestParam int     elementStart,
                                              @RequestParam int     maxElements)
    {
        return restAPI.getAnnotations(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of certifications for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of certifications or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/certifications")

    public CertificationsResponse getCertifications(@PathVariable String  serverName,
                                                    @PathVariable String  userId,
                                                    @PathVariable String  assetGUID,
                                                    @RequestParam int     elementStart,
                                                    @RequestParam int     maxElements)
    {
        return restAPI.getCertifications(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of comments for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/comments")

    public CommentsResponse getComments(@PathVariable String  serverName,
                                        @PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getComments(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of comments for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param commentGUID  String   unique id for the root comment.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/comments/{commentGUID}/replies")

    public CommentsResponse getCommentReplies(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  commentGUID,
                                              @RequestParam int     elementStart,
                                              @RequestParam int     maxElements)
    {
        return restAPI.getCommentReplies(serverName, userId, commentGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of connections for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of connections or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/connections")

    public ConnectionsResponse getConnections(@PathVariable String  serverName,
                                              @PathVariable String  userId,
                                              @PathVariable String  assetGUID,
                                              @RequestParam int     elementStart,
                                              @RequestParam int     maxElements)
    {
        return restAPI.getConnections(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of external identifiers for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of external identifiers or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/external-identifiers")

    public ExternalIdentifiersResponse getExternalIdentifiers(@PathVariable String  serverName,
                                                              @PathVariable String  userId,
                                                              @PathVariable String  assetGUID,
                                                              @RequestParam int     elementStart,
                                                              @RequestParam int     maxElements)
    {
        return restAPI.getExternalIdentifiers(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of external references for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of external references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/external-references")

    public ExternalReferencesResponse getExternalReferences(@PathVariable String  serverName,
                                                            @PathVariable String  userId,
                                                            @PathVariable String  assetGUID,
                                                            @RequestParam int     elementStart,
                                                            @RequestParam int     maxElements)
    {
        return restAPI.getExternalReferences(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of informal tags for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of informal tags or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/informal-tags")

    public InformalTagsResponse getInformalTags(@PathVariable String  serverName,
                                                @PathVariable String  userId,
                                                @PathVariable String  assetGUID,
                                                @RequestParam int     elementStart,
                                                @RequestParam int     maxElements)
    {
        return restAPI.getInformalTags(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of licenses for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of informal tags or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/licenses")

    public LicensesResponse getLicenses(@PathVariable String  serverName,
                                        @PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getLicenses(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of likes for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of likes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/likes")

    public LikesResponse getLikes(@PathVariable String  serverName,
                                  @PathVariable String  userId,
                                  @PathVariable String  assetGUID,
                                  @RequestParam int     elementStart,
                                  @RequestParam int     maxElements)
    {
        return restAPI.getLikes(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of known locations for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of locations or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/known-locations")

    public LocationsResponse getKnownLocations(@PathVariable String  serverName,
                                               @PathVariable String  userId,
                                               @PathVariable String  assetGUID,
                                               @RequestParam int     elementStart,
                                               @RequestParam int     maxElements)
    {
        return restAPI.getKnownLocations(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of note logs for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of note logs or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/note-logs")

    public NoteLogsResponse getNoteLogs(@PathVariable String  serverName,
                                        @PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getNoteLogs(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of notes for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param noteLogGUID  String   unique id for note log.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of notes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/note-log/{noteLogGUID}/notes")

    public NotesResponse getNotes(@PathVariable String  serverName,
                                  @PathVariable String  userId,
                                  @PathVariable String  noteLogGUID,
                                  @RequestParam int     elementStart,
                                  @RequestParam int     maxElements)
    {
        return restAPI.getNotes(serverName, userId, noteLogGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of ratings for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of ratings or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/ratings")

    public RatingsResponse getRatings(@PathVariable String  serverName,
                                      @PathVariable String  userId,
                                      @PathVariable String  assetGUID,
                                      @RequestParam int     elementStart,
                                      @RequestParam int     maxElements)
    {
        return restAPI.getRatings(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of related assets for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/related-assets")

    public RelatedAssetsResponse getRelatedAssets(@PathVariable String  serverName,
                                                  @PathVariable String  userId,
                                                  @PathVariable String  assetGUID,
                                                  @RequestParam int     elementStart,
                                                  @RequestParam int     maxElements)
    {
        return restAPI.getRelatedAssets(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of related media references for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related media references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/related-media-references")

    public RelatedMediaReferencesResponse getRelatedMediaReferences(@PathVariable String  serverName,
                                                                    @PathVariable String  userId,
                                                                    @PathVariable String  assetGUID,
                                                                    @RequestParam int     elementStart,
                                                                    @RequestParam int     maxElements)
    {
        return restAPI.getRelatedMediaReferences(serverName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns a list of schema attributes for a schema type.
     *
     * @param serverName     String   name of server instance to call.
     * @param userId         String   userId of user making request.
     * @param schemaTypeGUID String   unique id for containing schema type.
     * @param elementStart   int      starting position for fist returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     *
     * @return a schema attributes response or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{schemaTypeGUID}/schema-attributes")

    public SchemaAttributesResponse getSchemaAttributes(@PathVariable String  serverName,
                                                        @PathVariable String  userId,
                                                        @PathVariable String  schemaTypeGUID,
                                                        @RequestParam int     elementStart,
                                                        @RequestParam int     maxElements)
    {
        return restAPI.getSchemaAttributes(serverName, userId, schemaTypeGUID, elementStart, maxElements);
    }

}
