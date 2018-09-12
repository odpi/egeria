/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.admin.ConnectedAssetAdmin;
import org.odpi.openmetadata.accessservices.connectedasset.rest.*;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
public class ConnectedAssetRESTServices
{
    static private String                  accessServiceName   = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName  name of this access service
     * @param repositoryConnector OMRS Repository Connector to the property server.
     */
    static public void setRepositoryConnector(String                  accessServiceName,
                                              OMRSRepositoryConnector repositoryConnector)
    {
        ConnectedAssetRESTServices.accessServiceName = accessServiceName;
        ConnectedAssetRESTServices.repositoryConnector = repositoryConnector;
    }

    /**
     * Default constructor
     */
    public ConnectedAssetRESTServices()
    {
        AccessServiceDescription myDescription = AccessServiceDescription.CONNECTED_ASSET_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                                                                                 myDescription.getAccessServiceName(),
                                                                                 myDescription.getAccessServiceDescription(),
                                                                                 myDescription.getAccessServiceWiki(),
                                                                                 AccessServiceOperationalStatus.ENABLED,
                                                                                 ConnectedAssetAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
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
    public AssetResponse getAssetSummary(String   userId,
                                         String   assetGUID)
    {
        return null;
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
    public AnnotationsResponse getAnnotations(String  userId,
                                              String  assetGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        return null;
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
    public CountResponse countAnnotations(String  userId,
                                          String  assetGUID)
    {
        return null;
    }


    /**
     * Returns the list of certifications for the asset.
     *
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
    public CertificationsResponse getCertifications(String  userId,
                                                    String  assetGUID,
                                                    int     elementStart,
                                                    int     maxElements)
    {
        return null;
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
    public CountResponse countCertifications(String  userId,
                                             String  assetGUID)
    {
        return null;
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
    public CommentsResponse getComments(String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
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
    public CountResponse countComments(String  userId,
                                       String  assetGUID)
    {
        return null;
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
    public ConnectionsResponse getConnections(String  userId,
                                              String  assetGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        return null;
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
    public CountResponse countConnections(String  userId,
                                          String  assetGUID)
    {
        return null;
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
    public ExternalIdentifiersResponse getExternalIdentifiers(String  userId,
                                                              String  assetGUID,
                                                              int     elementStart,
                                                              int     maxElements)
    {
        return null;
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
    public CountResponse countExternalIdentifiers(String  userId,
                                                  String  assetGUID)
    {
        return null;
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
    public ExternalReferencesResponse getExternalReferences(String  userId,
                                                            String  assetGUID,
                                                            int     elementStart,
                                                            int     maxElements)
    {
        return null;
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
    public CountResponse countExternalReferences(String  userId,
                                                 String  assetGUID)
    {
        return null;
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
    public InformalTagsResponse getInformalTags(String  userId,
                                                String  assetGUID,
                                                int     elementStart,
                                                int     maxElements)
    {
        return null;
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
    public CountResponse countInformalTags(String  userId,
                                           String  assetGUID)
    {
        return null;
    }


    /**
     * Returns the list of licenses for the asset.
     *
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
    public LicensesResponse getLicenses(String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
    }


    /**
     * Returns the count of licenses for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of license or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CountResponse countLicenses(String  userId,
                                           String  assetGUID)
    {
        return null;
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
    public LikesResponse getLikes(String  userId,
                                  String  assetGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        return null;
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
    public CountResponse countLikes(String  userId,
                                    String  assetGUID)
    {
        return null;
    }


    /**
     * Returns the list of known locations for the asset.
     *
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
    public LocationsResponse getKnownLocations(String  userId,
                                               String  assetGUID,
                                               int     elementStart,
                                               int     maxElements)
    {
        return null;
    }


    /**
     * Returns the count of known locations for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     *
     * @return a count of known locations or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CountResponse countKnownLocations(String  userId,
                                             String  assetGUID)
    {
        return null;
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
    public MeaningsResponse getMeanings(String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
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
    public CountResponse countMeanings(String  userId,
                                       String  assetGUID)
    {
        return null;
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
    public NoteLogsResponse getNoteLogs(String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        return null;
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
    public CountResponse countNoteLogs(String  userId,
                                       String  assetGUID)
    {
        return null;
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
    public NotesResponse getNotes(String  userId,
                                  String  assetGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        return null;
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
    public CountResponse countNotes(String  userId,
                                    String  assetGUID)
    {
        return null;
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
    public RatingsResponse getRatings(String  userId,
                                      String  assetGUID,
                                      int     elementStart,
                                      int     maxElements)
    {
        return null;
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
    public CountResponse countRatings(String  userId,
                                      String  assetGUID)
    {
        return null;
    }


    /**
     * Returns the list of related assets for the asset.
     *
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
    public RelatedAssetsResponse getRelatedAssets(String  userId,
                                                  String  assetGUID,
                                                  int     elementStart,
                                                  int     maxElements)
    {
        return null;
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
    public CountResponse countRelatedAssets(String  userId,
                                            String  assetGUID)
    {
        return null;
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
    public RelatedMediaReferencesResponse getRelatedMediaReferences(String  userId,
                                                                    String  assetGUID,
                                                                    int     elementStart,
                                                                    int     maxElements)
    {
        return null;
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
    public CountResponse countRelatedMediaReferences(String  userId,
                                                     String  assetGUID)
    {
        return null;
    }


    /**
     * Returns the schema for the asset.
     *
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
    public SchemaResponse getSchema(String  userId,
                                    String  assetGUID,
                                    int     elementStart,
                                    int     maxElements)
    {
        return null;
    }

}
