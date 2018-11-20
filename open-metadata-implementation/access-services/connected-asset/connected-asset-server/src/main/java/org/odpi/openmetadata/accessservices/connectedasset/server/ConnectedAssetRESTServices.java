/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.rest.*;


/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
public class ConnectedAssetRESTServices
{
    static private String                          accessServiceName   = null;
    static private ConnectedAssetInstanceHandler   instanceHandler = new ConnectedAssetInstanceHandler();

    /**
     * Default constructor
     */
    public ConnectedAssetRESTServices()
    {
    }


    /**
     * Returns the basic information about the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     *
     * @return a bean with the basic properties about the asset or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetResponse getAssetSummary(String   serverName,
                                         String   userId,
                                         String   assetGUID)
    {
        return null;
    }


    /**
     * Returns the basic information about the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId             userId of user making request.
     * @param connectionGUID     unique id for connection.
     *
     * @return a bean with the basic properties about the asset or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse getAssetIdForConnection(String   serverName,
                                                String   userId,
                                                String   connectionGUID)
    {
        return null;
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
    public AnnotationsResponse getAnnotations(String  serverName,
                                              String  userId,
                                              String  assetGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        return null;
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
     * @return a list of certifications  or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CertificationsResponse getCertifications(String  serverName,
                                                    String  userId,
                                                    String  assetGUID,
                                                    int     elementStart,
                                                    int     maxElements)
    {
        return null;
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
    public CommentsResponse getComments(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
    }


    /**
     * Returns the list of replies to a comment.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param commentGUID  String   unique id for root comment.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CommentsResponse getCommentReplies(String  serverName,
                                              String  userId,
                                              String  commentGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        return null;
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
    public ConnectionsResponse getConnections(String  serverName,
                                              String  userId,
                                              String  assetGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        return null;
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
    public ExternalIdentifiersResponse getExternalIdentifiers(String  serverName,
                                                              String  userId,
                                                              String  assetGUID,
                                                              int     elementStart,
                                                              int     maxElements)
    {
        return null;
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
    public ExternalReferencesResponse getExternalReferences(String  serverName,
                                                            String  userId,
                                                            String  assetGUID,
                                                            int     elementStart,
                                                            int     maxElements)
    {
        return null;
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
    public InformalTagsResponse getInformalTags(String  serverName,
                                                String  userId,
                                                String  assetGUID,
                                                int     elementStart,
                                                int     maxElements)
    {
        return null;
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
     * @return a list of licenses or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public LicensesResponse getLicenses(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
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
    public LikesResponse getLikes(String  serverName,
                                  String  userId,
                                  String  assetGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        return null;
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
     * @return a list of known locations or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public LocationsResponse getKnownLocations(String  serverName,
                                               String  userId,
                                               String  assetGUID,
                                               int     elementStart,
                                               int     maxElements)
    {
        return null;
    }


    /**
     * Returns the list of meanings for the asset.
     *
     * @param serverName   String   name of server instance to call.
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
    public MeaningsResponse getMeanings(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
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
    public NoteLogsResponse getNoteLogs(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
    }


    /**
     * Returns the list of notes for a note log.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param noteLogGUID  String   unique id for the note log.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of notes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public NotesResponse getNotes(String  serverName,
                                  String  userId,
                                  String  noteLogGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        return null;
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
    public RatingsResponse getRatings(String  serverName,
                                      String  userId,
                                      String  assetGUID,
                                      int     elementStart,
                                      int     maxElements)
    {
        return null;
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
     * @return a list of assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public RelatedAssetsResponse getRelatedAssets(String  serverName,
                                                  String  userId,
                                                  String  assetGUID,
                                                  int     elementStart,
                                                  int     maxElements)
    {
        return null;
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
    public RelatedMediaReferencesResponse getRelatedMediaReferences(String  serverName,
                                                                    String  userId,
                                                                    String  assetGUID,
                                                                    int     elementStart,
                                                                    int     maxElements)
    {
        return null;
    }


    /**
     * Returns the schema for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public SchemaResponse getSchema(String  serverName,
                                    String  userId,
                                    String  assetGUID,
                                    int     elementStart,
                                    int     maxElements)
    {
        return null;
    }

}
