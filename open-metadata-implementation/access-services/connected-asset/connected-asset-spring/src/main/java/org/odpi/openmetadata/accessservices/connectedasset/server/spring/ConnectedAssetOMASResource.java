/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.server.spring;

import org.odpi.openmetadata.accessservices.connectedasset.rest.*;
import org.odpi.openmetadata.accessservices.connectedasset.server.ConnectedAssetRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
@RestController
@RequestMapping("/open-metadata/access-services/connected-asset/users/{userId}")
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
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     *
     * @return a bean with the basic properties about the asset or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}")

    public AssetResponse getAssetSummary(@PathVariable String   userId,
                                         @PathVariable String   assetGUID)
    {
        return restAPI.getAssetSummary(userId, assetGUID);
    }


    /**
     * Returns the list of annotations for the asset.
     *
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

    public AnnotationsResponse getAnnotations(@PathVariable String  userId,
                                              @PathVariable String  assetGUID,
                                              @RequestParam int     elementStart,
                                              @RequestParam int     maxElements)
    {
        return restAPI.getAnnotations(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of annotations for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of annotations  or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/annotations/count")

    public CountResponse countAnnotations(@PathVariable String  userId,
                                          @PathVariable String  assetGUID)
    {
        return restAPI.countAnnotations(userId, assetGUID);
    }


    /**
     * Returns the list of certifications for the asset.
     *
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

    public CertificationsResponse getCertifications(@PathVariable String  userId,
                                                    @PathVariable String  assetGUID,
                                                    @RequestParam int     elementStart,
                                                    @RequestParam int     maxElements)
    {
        return restAPI.getCertifications(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of certifications for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of certifications or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/certifications/count")

    public CountResponse countCertifications(@PathVariable String  userId,
                                             @PathVariable String  assetGUID)
    {
        return restAPI.countCertifications(userId, assetGUID);
    }


    /**
     * Returns the list of comments for the asset.
     *
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

    public CommentsResponse getComments(@PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getComments(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of comments for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/comments/count")

    public CountResponse countComments(@PathVariable String  userId,
                                       @PathVariable String  assetGUID)
    {
        return restAPI.countComments(userId, assetGUID);
    }


    /**
     * Returns the list of connections for the asset.
     *
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

    public ConnectionsResponse getConnections(@PathVariable String  userId,
                                              @PathVariable String  assetGUID,
                                              @RequestParam int     elementStart,
                                              @RequestParam int     maxElements)
    {
        return restAPI.getConnections(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of connections for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of connections or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/connections/count")

    public CountResponse countConnections(@PathVariable String  userId,
                                          @PathVariable String  assetGUID)
    {
        return restAPI.countConnections(userId, assetGUID);
    }


    /**
     * Returns the list of external identifiers for the asset.
     *
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

    public ExternalIdentifiersResponse getExternalIdentifiers(@PathVariable String  userId,
                                                              @PathVariable String  assetGUID,
                                                              @RequestParam int     elementStart,
                                                              @RequestParam int     maxElements)
    {
        return restAPI.getExternalIdentifiers(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of external identifiers for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of external identifiers or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/external-identifiers/count")

    public CountResponse countExternalIdentifiers(@PathVariable String  userId,
                                                 @PathVariable String  assetGUID)
    {
        return restAPI.countExternalIdentifiers(userId, assetGUID);
    }


    /**
     * Returns the list of external references for the asset.
     *
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

    public ExternalReferencesResponse getExternalReferences(@PathVariable String  userId,
                                                            @PathVariable String  assetGUID,
                                                            @RequestParam int     elementStart,
                                                            @RequestParam int     maxElements)
    {
        return restAPI.getExternalReferences(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of external references for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of external references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/external-references/count")

    public CountResponse countExternalReferences(@PathVariable String  userId,
                                                 @PathVariable String  assetGUID)
    {
        return restAPI.countExternalReferences(userId, assetGUID);
    }


    /**
     * Returns the list of informal tags for the asset.
     *
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

    public InformalTagsResponse getInformalTags(@PathVariable String  userId,
                                                @PathVariable String  assetGUID,
                                                @RequestParam int     elementStart,
                                                @RequestParam int     maxElements)
    {
        return restAPI.getInformalTags(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of informal tags for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of informal tags or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/informal-tags/count")

    public CountResponse countInformalTags(@PathVariable String  userId,
                                           @PathVariable String  assetGUID)
    {
        return restAPI.countInformalTags(userId, assetGUID);
    }


    /**
     * Returns the list of licenses for the asset.
     *
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

    public LicensesResponse getLicenses(@PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getLicenses(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of licenses for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of informal tags or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/licenses/count")

    public CountResponse getLicenses(@PathVariable String  userId,
                                     @PathVariable String  assetGUID)
    {
        return restAPI.countLicenses(userId, assetGUID);
    }


    /**
     * Returns the list of likes for the asset.
     *
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

    public LikesResponse getLikes(@PathVariable String  userId,
                                  @PathVariable String  assetGUID,
                                  @RequestParam int     elementStart,
                                  @RequestParam int     maxElements)
    {
        return restAPI.getLikes(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of likes for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of likes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/likes/count")

    public CountResponse countLikes(@PathVariable String  userId,
                                    @PathVariable String  assetGUID)
    {
        return restAPI.countLikes(userId, assetGUID);
    }


    /**
     * Returns the list of known locations for the asset.
     *
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

    public LocationsResponse getKnownLocations(@PathVariable String  userId,
                                               @PathVariable String  assetGUID,
                                               @RequestParam int     elementStart,
                                               @RequestParam int     maxElements)
    {
        return restAPI.getKnownLocations(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of known locations for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of locations or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/known-locations/count")

    public CountResponse countKnownLocations(@PathVariable String  userId,
                                             @PathVariable String  assetGUID)
    {
        return restAPI.countKnownLocations(userId, assetGUID);
    }


    /**
     * Returns the list of meanings for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of meanings or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/meanings")

    public MeaningsResponse getMeanings(@PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getMeanings(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of meanings for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of meanings or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/meanings/count")

    public CountResponse countMeanings(@PathVariable String  userId,
                                       @PathVariable String  assetGUID)
    {
        return restAPI.countMeanings(userId, assetGUID);
    }


    /**
     * Returns the list of note logs for the asset.
     *
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

    public NoteLogsResponse getNoteLogs(@PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getNoteLogs(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of note logs for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of note logs or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/note-logs/count")

    public CountResponse countNoteLogs(@PathVariable String  userId,
                                       @PathVariable String  assetGUID)
    {
        return restAPI.countNoteLogs(userId, assetGUID);
    }


    /**
     * Returns the list of notes for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of notes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/notes")

    public NotesResponse getNotes(@PathVariable String  userId,
                                  @PathVariable String  assetGUID,
                                  @RequestParam int     elementStart,
                                  @RequestParam int     maxElements)
    {
        return restAPI.getNotes(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of notes for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of notes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/notes/count")

    public CountResponse countNotes(@PathVariable String  userId,
                                    @PathVariable String  assetGUID)
    {
        return restAPI.countNotes(userId, assetGUID);
    }


    /**
     * Returns the list of ratings for the asset.
     *
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

    public RatingsResponse getRatings(@PathVariable String  userId,
                                      @PathVariable String  assetGUID,
                                      @RequestParam int     elementStart,
                                      @RequestParam int     maxElements)
    {
        return restAPI.getRatings(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of ratings for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of ratings or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/ratings/count")

    public CountResponse countRatings(@PathVariable String  userId,
                                      @PathVariable String  assetGUID)
    {
        return restAPI.countRatings(userId, assetGUID);
    }


    /**
     * Returns the list of related assets for the asset.
     *
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

    public RelatedAssetsResponse getRelatedAssets(@PathVariable String  userId,
                                                  @PathVariable String  assetGUID,
                                                  @RequestParam int     elementStart,
                                                  @RequestParam int     maxElements)
    {
        return restAPI.getRelatedAssets(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of related assets for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of related assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/related-assets/count")

    public CountResponse countRelatedAssets(@PathVariable String  userId,
                                            @PathVariable String  assetGUID)
    {
        return restAPI.countRelatedAssets(userId, assetGUID);
    }


    /**
     * Returns the list of related media references for the asset.
     *
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

    public RelatedMediaReferencesResponse getRelatedMediaReferences(@PathVariable String  userId,
                                                                    @PathVariable String  assetGUID,
                                                                    @RequestParam int     elementStart,
                                                                    @RequestParam int     maxElements)
    {
        return restAPI.getRelatedMediaReferences(userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the count of related media references for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of related media references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/related-media-references/count")

    public CountResponse countRelatedMediaReferences(@PathVariable String  userId,
                                                     @PathVariable String  assetGUID)
    {
        return restAPI.countRelatedMediaReferences(userId, assetGUID);
    }


    /**
     * Returns the schema for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a schema or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/schema")

    public SchemaResponse getSchema(@PathVariable String  userId,
                                    @PathVariable String  assetGUID,
                                    @RequestParam int     elementStart,
                                    @RequestParam int     maxElements)
    {
        return restAPI.getSchema(userId, assetGUID, elementStart, maxElements);
    }

}
