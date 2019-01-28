/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.connectedasset.handlers.*;
import org.odpi.openmetadata.accessservices.connectedasset.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
public class ConnectedAssetRESTServices
{
    static private ConnectedAssetInstanceHandler   instanceHandler = new ConnectedAssetInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(ConnectedAssetRESTServices.class);

    /**
     * Default constructor
     */
    public ConnectedAssetRESTServices()
    {
    }


    /**
     * Returns the basic information about the asset.  The connection guid allows the short description for the
     * asset to be filled out.
     *
     * @param serverName  name of the server.
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     * @param connectionGUID  unique id for connection used to access asset.
     *
     * @return a bean with the basic properties about the asset or
     * InvalidParameterException - the asset GUID is null or invalid or
     * UnrecognizedAssetGUIDException - the asset GUID is not recognized by the property server or
     * UnrecognizedConnectionGUIDException - the connection GUID is not recognized by the property server or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetResponse getConnectedAssetSummary(String   serverName,
                                                  String   userId,
                                                  String   assetGUID,
                                                  String   connectionGUID)
    {
        final String methodName = "getConnectedAssetSummary";

        AssetResponse  response = new AssetResponse();

        try
        {
            AssetHandler assetHandler = new AssetHandler(instanceHandler.getAccessServiceName(),
                                                         serverName,
                                                         instanceHandler.getRepositoryConnector(serverName),
                                                         userId,
                                                         assetGUID,
                                                         connectionGUID);

            response.setAsset(assetHandler.getAsset());
            response.setCertificationCount(assetHandler.getCertificationCount());
            response.setCommentCount(assetHandler.getCommentCount());
            response.setConnectionCount(assetHandler.getConnectionCount());
            response.setExternalIdentifierCount(assetHandler.getExternalIdentifierCount());
            response.setExternalReferencesCount(assetHandler.getExternalReferencesCount());
            response.setInformalTagCount(assetHandler.getInformalTagCount());
            response.setLicenseCount(assetHandler.getLicenseCount());
            response.setLikeCount(assetHandler.getLikeCount());
            response.setKnownLocationsCount(assetHandler.getKnownLocationsCount());
            response.setNoteLogsCount(assetHandler.getNoteLogsCount());
            response.setRatingsCount(assetHandler.getRatingsCount());
            response.setRelatedAssetCount(assetHandler.getRelatedAssetCount());
            response.setRelatedMediaReferenceCount(assetHandler.getRelatedMediaReferenceCount());
            response.setSchemaType(assetHandler.getSchemaType());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (UnrecognizedAssetGUIDException error)
        {
            captureUnrecognizedAssetGUIDException(response, error);
        }
        catch (UnrecognizedConnectionGUIDException error)
        {
            captureUnrecognizedConnectionGUIDException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
    public AssetResponse getAssetSummary(String   serverName,
                                         String   userId,
                                         String   assetGUID)
    {
        final String        methodName = "getAssetSummary";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        AssetResponse  response = new AssetResponse();

        try
        {
            AssetHandler   assetHandler = new AssetHandler(instanceHandler.getAccessServiceName(),
                                                           serverName,
                                                           instanceHandler.getRepositoryConnector(serverName),
                                                           userId,
                                                           assetGUID);

            response.setAsset(assetHandler.getAsset());
            response.setCertificationCount(assetHandler.getCertificationCount());
            response.setCommentCount(assetHandler.getCommentCount());
            response.setConnectionCount(assetHandler.getConnectionCount());
            response.setExternalIdentifierCount(assetHandler.getExternalIdentifierCount());
            response.setExternalReferencesCount(assetHandler.getExternalReferencesCount());
            response.setInformalTagCount(assetHandler.getInformalTagCount());
            response.setLicenseCount(assetHandler.getLicenseCount());
            response.setLikeCount(assetHandler.getLikeCount());
            response.setKnownLocationsCount(assetHandler.getKnownLocationsCount());
            response.setNoteLogsCount(assetHandler.getNoteLogsCount());
            response.setRatingsCount(assetHandler.getRatingsCount());
            response.setRelatedAssetCount(assetHandler.getRelatedAssetCount());
            response.setRelatedMediaReferenceCount(assetHandler.getRelatedMediaReferenceCount());
            response.setSchemaType(assetHandler.getSchemaType());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (UnrecognizedAssetGUIDException error)
        {
            captureUnrecognizedAssetGUIDException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CertificationsResponse getCertifications(String  serverName,
                                                    String  userId,
                                                    String  assetGUID,
                                                    int     elementStart,
                                                    int     maxElements)
    {
        final String        methodName = "getCertifications";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        CertificationsResponse  response = new CertificationsResponse();

        try
        {
            CertificationsHandler handler = new CertificationsHandler(instanceHandler.getAccessServiceName(),
                                                                      serverName,
                                                                      instanceHandler.getRepositoryConnector(serverName),
                                                                      userId,
                                                                      assetGUID,
                                                                      elementStart,
                                                                      maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CommentsResponse getComments(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        final String        methodName = "getComments";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        CommentsResponse  response = new CommentsResponse();

        try
        {
            CommentsHandler handler = new CommentsHandler(instanceHandler.getAccessServiceName(),
                                                          serverName,
                                                          instanceHandler.getRepositoryConnector(serverName),
                                                          userId,
                                                          assetGUID,
                                                          elementStart,
                                                          maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CommentsResponse getCommentReplies(String  serverName,
                                              String  userId,
                                              String  commentGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        final String        methodName = "getCommentReplies";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        CommentsResponse  response = new CommentsResponse();

        try
        {
            CommentRepliesHandler handler = new CommentRepliesHandler(instanceHandler.getAccessServiceName(),
                                                                      serverName,
                                                                      instanceHandler.getRepositoryConnector(serverName),
                                                                      userId,
                                                                      commentGUID,
                                                                      elementStart,
                                                                      maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ConnectionsResponse getConnections(String  serverName,
                                              String  userId,
                                              String  assetGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        final String        methodName = "getConnections";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        ConnectionsResponse  response = new ConnectionsResponse();

        try
        {
            ConnectionsHandler handler = new ConnectionsHandler(instanceHandler.getAccessServiceName(),
                                                                serverName,
                                                                instanceHandler.getRepositoryConnector(serverName),
                                                                userId,
                                                                assetGUID,
                                                                elementStart,
                                                                maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ExternalIdentifiersResponse getExternalIdentifiers(String  serverName,
                                                              String  userId,
                                                              String  assetGUID,
                                                              int     elementStart,
                                                              int     maxElements)
    {
        final String        methodName = "getExternalIdentifiers";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        ExternalIdentifiersResponse  response = new ExternalIdentifiersResponse();

        try
        {
            ExternalIdentifiersHandler handler = new ExternalIdentifiersHandler(instanceHandler.getAccessServiceName(),
                                                                                serverName,
                                                                                instanceHandler.getRepositoryConnector(serverName),
                                                                                userId,
                                                                                assetGUID,
                                                                                elementStart,
                                                                                maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ExternalReferencesResponse getExternalReferences(String  serverName,
                                                            String  userId,
                                                            String  assetGUID,
                                                            int     elementStart,
                                                            int     maxElements)
    {
        final String        methodName = "getExternalReferences";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        ExternalReferencesResponse  response = new ExternalReferencesResponse();

        try
        {
            ExternalReferencesHandler handler = new ExternalReferencesHandler(instanceHandler.getAccessServiceName(),
                                                                              serverName,
                                                                              instanceHandler.getRepositoryConnector(serverName),
                                                                              userId,
                                                                              assetGUID,
                                                                              elementStart,
                                                                              maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getInformalTags(String  serverName,
                                                String  userId,
                                                String  assetGUID,
                                                int     elementStart,
                                                int     maxElements)
    {
        final String        methodName = "getInformalTags";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        InformalTagsResponse  response = new InformalTagsResponse();

        try
        {
            InformalTagsHandler handler = new InformalTagsHandler(instanceHandler.getAccessServiceName(),
                                                                  serverName,
                                                                  instanceHandler.getRepositoryConnector(serverName),
                                                                  userId,
                                                                  assetGUID,
                                                                  elementStart,
                                                                  maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public LicensesResponse getLicenses(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        final String        methodName = "getLicenses";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        LicensesResponse  response = new LicensesResponse();

        try
        {
            LicensesHandler handler = new LicensesHandler(instanceHandler.getAccessServiceName(),
                                                          serverName,
                                                          instanceHandler.getRepositoryConnector(serverName),
                                                          userId,
                                                          assetGUID,
                                                          elementStart,
                                                          maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public LikesResponse getLikes(String  serverName,
                                  String  userId,
                                  String  assetGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        final String        methodName = "getLikes";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        LikesResponse  response = new LikesResponse();

        try
        {
            LikesHandler handler = new LikesHandler(instanceHandler.getAccessServiceName(),
                                                    serverName,
                                                    instanceHandler.getRepositoryConnector(serverName),
                                                    userId,
                                                    assetGUID,
                                                    elementStart,
                                                    maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public LocationsResponse getKnownLocations(String  serverName,
                                               String  userId,
                                               String  assetGUID,
                                               int     elementStart,
                                               int     maxElements)
    {
        final String        methodName = "getKnownLocations";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        LocationsResponse  response = new LocationsResponse();

        try
        {
            LocationsHandler handler = new LocationsHandler(instanceHandler.getAccessServiceName(),
                                                            serverName,
                                                            instanceHandler.getRepositoryConnector(serverName),
                                                            userId,
                                                            assetGUID,
                                                            elementStart,
                                                            maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public NoteLogsResponse getNoteLogs(String  serverName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        final String        methodName = "getNoteLogs";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        NoteLogsResponse  response = new NoteLogsResponse();

        try
        {
            NoteLogsHandler handler = new NoteLogsHandler(instanceHandler.getAccessServiceName(),
                                                          serverName,
                                                          instanceHandler.getRepositoryConnector(serverName),
                                                          userId,
                                                          assetGUID,
                                                          elementStart,
                                                          maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public NotesResponse getNotes(String  serverName,
                                  String  userId,
                                  String  noteLogGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        final String        methodName = "getNotes";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        NotesResponse  response = new NotesResponse();

        try
        {
            NotesHandler handler = new NotesHandler(instanceHandler.getAccessServiceName(),
                                                    serverName,
                                                    instanceHandler.getRepositoryConnector(serverName),
                                                    userId,
                                                    noteLogGUID,
                                                    elementStart,
                                                    maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public RatingsResponse getRatings(String  serverName,
                                      String  userId,
                                      String  assetGUID,
                                      int     elementStart,
                                      int     maxElements)
    {
        final String        methodName = "getRatings";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        RatingsResponse  response = new RatingsResponse();

        try
        {
            RatingsHandler handler = new RatingsHandler(instanceHandler.getAccessServiceName(),
                                                        serverName,
                                                        instanceHandler.getRepositoryConnector(serverName),
                                                        userId,
                                                        assetGUID,
                                                        elementStart,
                                                        maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public RelatedAssetsResponse getRelatedAssets(String  serverName,
                                                  String  userId,
                                                  String  assetGUID,
                                                  int     elementStart,
                                                  int     maxElements)
    {
        final String        methodName = "getRelatedAssets";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        RelatedAssetsResponse  response = new RelatedAssetsResponse();

        try
        {
            RelatedAssetsHandler handler = new RelatedAssetsHandler(instanceHandler.getAccessServiceName(),
                                                                    serverName,
                                                                    instanceHandler.getRepositoryConnector(serverName),
                                                                    userId,
                                                                    assetGUID,
                                                                    elementStart,
                                                                    maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public RelatedMediaReferencesResponse getRelatedMediaReferences(String  serverName,
                                                                    String  userId,
                                                                    String  assetGUID,
                                                                    int     elementStart,
                                                                    int     maxElements)
    {
        final String        methodName = "getRelatedMediaReferences";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        RelatedMediaReferencesResponse  response = new RelatedMediaReferencesResponse();

        try
        {
            RelatedMediaReferencesHandler handler = new RelatedMediaReferencesHandler(instanceHandler.getAccessServiceName(),
                                                                                      serverName,
                                                                                      instanceHandler.getRepositoryConnector(serverName),
                                                                                      userId,
                                                                                      assetGUID,
                                                                                      elementStart,
                                                                                      maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
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
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
   public SchemaAttributesResponse getSchemaAttributes(String  serverName,
                                                       String  userId,
                                                       String  schemaTypeGUID,
                                                       int     elementStart,
                                                       int     maxElements)
    {
        final String        methodName = "getSchemaAttributes";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        SchemaAttributesResponse  response = new SchemaAttributesResponse();

        try
        {
            SchemaAttributesHandler handler = new SchemaAttributesHandler(instanceHandler.getAccessServiceName(),
                                                                          serverName,
                                                                          instanceHandler.getRepositoryConnector(serverName),
                                                                          userId,
                                                                          schemaTypeGUID,
                                                                          elementStart,
                                                                          maxElements);

            response.setList(handler.getList());
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(ConnectedAssetOMASAPIResponse      response,
                                         ConnectedAssetCheckedExceptionBase error,
                                         String                             exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(ConnectedAssetOMASAPIResponse      response,
                                         ConnectedAssetCheckedExceptionBase error,
                                         String                             exceptionClassName,
                                         Map<String, Object>                exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(ConnectedAssetOMASAPIResponse response,
                                                  InvalidParameterException error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUnrecognizedAssetGUIDException(ConnectedAssetOMASAPIResponse response,
                                                       UnrecognizedAssetGUIDException error)
    {
        String  assetGUID = error.getAssetGUID();

        if (assetGUID != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("assetGUID", assetGUID);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUnrecognizedConnectionGUIDException(ConnectedAssetOMASAPIResponse       response,
                                                            UnrecognizedConnectionGUIDException error)
    {
        String  connectionGUID = error.getConnectionGUID();

        if (connectionGUID != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("connectionGUID", connectionGUID);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void capturePropertyServerException(ConnectedAssetOMASAPIResponse     response,
                                                PropertyServerException error)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(PropertyServerException.class.getName());
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(ConnectedAssetOMASAPIResponse response,
                                                   UserNotAuthorizedException error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            response.setExceptionProperties(exceptionProperties);
        }

        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(UserNotAuthorizedException.class.getName());
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }
}
