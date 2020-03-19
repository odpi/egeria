/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
public class OCFMetadataRESTServices
{
    static private OCFMetadataInstanceHandler instanceHandler = new OCFMetadataInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(OCFMetadataRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public OCFMetadataRESTServices()
    {
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByGUID(String     serverName,
                                                  String     serviceURLName,
                                                  String     userId,
                                                  String     guid)
    {
        final String        methodName = "getConnectionByGUID";

        log.debug("Calling method: " + methodName + " from service " + serviceURLName + " for server " + serverName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            ConnectionHandler connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(connectionHandler.getConnection(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }



    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param serverName name of the server instances for this request
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException - there is no asset associated with this connection or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse getAssetForConnectionGUID(String   serverName,
                                                  String   serviceURLName,
                                                  String   userId,
                                                  String   connectionGUID)
    {
        final String        methodName = "getAssetForConnectionGUID";

        log.debug("Calling method: " + methodName + " from service " + serviceURLName + " for server " + serverName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;


        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.getAssetForConnection(userId, connectionGUID));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId      userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionForAsset(String   serverName,
                                                    String   serviceURLName,
                                                    String   userId,
                                                    String   assetGUID)
    {
        final String        methodName = "getConnectionForAsset";

        log.debug("Calling method: " + methodName + " from service " + serviceURLName + " for server " + serverName);

        ConnectionResponse  response = new ConnectionResponse();
        AuditLog            auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(handler.getConnectionForAsset(userId, assetGUID));
        }
        catch (InvalidParameterException  error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the basic information about the asset.  The connection guid allows the short description for the
     * asset to be filled out.
     *
     * @param serverName  name of the server.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     * @param connectionGUID  unique id for connection used to access asset.
     * @param methodName calling method
     *
     * @return a bean with the basic properties about the asset or
     * InvalidParameterException - the asset GUID is null or invalid or
     * UnrecognizedAssetGUIDException - the asset GUID is not recognized by the property server or
     * UnrecognizedConnectionGUIDException - the connection GUID is not recognized by the property server or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private AssetResponse getAssetResponse(String   serverName,
                                           String   serviceURLName,
                                           String   userId,
                                           String   assetGUID,
                                           String   connectionGUID,
                                           String   methodName)
    {
        log.debug("Calling method: " + methodName + " from service " + serviceURLName + " for server " + serverName);

        AssetResponse response = new AssetResponse();
        AuditLog      auditLog = null;

        try
        {
            List<String>  supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName);

            AssetHandler assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (connectionGUID != null)
            {
                response.setAsset(assetHandler.getAsset(userId,
                                                        supportedZones,
                                                        assetGUID,
                                                        connectionGUID,
                                                        instanceHandler.getServiceName(serviceURLName),
                                                        methodName));
            }
            else
            {
                response.setAsset(assetHandler.getAsset(userId,
                                                        supportedZones,
                                                        assetGUID,
                                                        instanceHandler.getServiceName(serviceURLName),
                                                        methodName));
            }
            response.setCertificationCount(assetHandler.getCertificationCount(userId, assetGUID, methodName));
            response.setCommentCount(assetHandler.getCommentCount(userId, assetGUID, methodName));
            response.setConnectionCount(assetHandler.getConnectionCount(userId, assetGUID, methodName));
            response.setExternalIdentifierCount(assetHandler.getExternalIdentifierCount(userId, assetGUID, methodName));
            response.setExternalReferencesCount(assetHandler.getExternalReferencesCount(userId, assetGUID, methodName));
            response.setInformalTagCount(assetHandler.getInformalTagCount(userId, assetGUID, methodName));
            response.setLicenseCount(assetHandler.getLicenseCount(userId, assetGUID, methodName));
            response.setLikeCount(assetHandler.getLikeCount(userId, assetGUID, methodName));
            response.setKnownLocationsCount(assetHandler.getKnownLocationsCount(userId, assetGUID, methodName));
            response.setNoteLogsCount(assetHandler.getNoteLogsCount(userId, assetGUID, methodName));
            response.setRatingsCount(assetHandler.getRatingsCount(userId, assetGUID, methodName));
            response.setRelatedAssetCount(assetHandler.getRelatedAssetCount(userId, assetGUID, methodName));
            response.setRelatedMediaReferenceCount(assetHandler.getRelatedMediaReferenceCount(userId, assetGUID, methodName));
            response.setSchemaType(assetHandler.getSchemaType(userId, assetGUID, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the basic information about the asset.  The connection guid allows the short description for the
     * asset to be filled out.
     *
     * @param serverName  name of the server.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                                  String   serviceURLName,
                                                  String   userId,
                                                  String   assetGUID,
                                                  String   connectionGUID)
    {
        final String methodName = "getConnectedAssetSummary";

        return this.getAssetResponse(serverName, serviceURLName, userId, assetGUID, connectionGUID, methodName);
    }


    /**
     * Returns the basic information about the asset.
     *
     * @param serverName String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                         String   serviceURLName,
                                         String   userId,
                                         String   assetGUID)
    {
        final String        methodName = "getAssetSummary";

        return this.getAssetResponse(serverName, serviceURLName, userId, assetGUID, null, methodName);
    }


    /**
     * Returns the list of certifications for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                                    String  serviceURLName,
                                                    String  userId,
                                                    String  assetGUID,
                                                    int     elementStart,
                                                    int     maxElements)
    {
        final String        methodName = "getCertifications";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        CertificationsResponse  response = new CertificationsResponse();
        AuditLog                auditLog = null;

        try
        {
            CertificationHandler handler = instanceHandler.getCertificationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setList(handler.getCertifications(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of comments for the requested anchor.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param anchorGUID    String   unique id for anchor object.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     * @param methodName  String name of calling method.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private CommentsResponse getAttachedComments(String  serverName,
                                                 String  serviceURLName,
                                                 String  userId,
                                                 String  assetGUID,
                                                 String  anchorGUID,
                                                 int     elementStart,
                                                 int     maxElements,
                                                 String  methodName)
    {
        log.debug("Calling method: " + methodName + " for server " + serverName);

        CommentsResponse  response = new CommentsResponse();
        AuditLog          auditLog = null;

        try
        {
            AssetHandler  handler = instanceHandler.getAssetHandler(userId,serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<Comment>  attachedComments = handler.getAssetComments(userId,
                                                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                       assetGUID,
                                                                       anchorGUID,
                                                                       elementStart,
                                                                       maxElements,
                                                                       methodName);
            List<CommentResponse> results          = new ArrayList<>();

            if (attachedComments != null)
            {
                for (Comment  comment : attachedComments)
                {
                    if (comment != null)
                    {
                        CommentResponse commentResponse = new CommentResponse();

                        commentResponse.setComment(comment);
                        commentResponse.setReplyCount(handler.getCommentCount(userId, comment.getGUID(), methodName));

                        results.add(commentResponse);
                    }
                }
            }

            if (results.isEmpty())
            {
                response.setList(null);
            }
            else
            {
                response.setList(results);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of comments for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
    public CommentsResponse getAssetComments(String  serverName,
                                             String  serviceURLName,
                                             String  userId,
                                             String  assetGUID,
                                             int     elementStart,
                                             int     maxElements)
    {
        final String methodName = "getAssetComments";

        return getAttachedComments(serverName, serviceURLName, userId, assetGUID, assetGUID, elementStart, maxElements, methodName);
    }


    /**
     * Returns the list of replies to a comment.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param commentGUID  String   unique id for root comment.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public CommentsResponse getAssetCommentReplies(String  serverName,
                                                   String  serviceURLName,
                                                   String  userId,
                                                   String  assetGUID,
                                                   String  commentGUID,
                                                   int     elementStart,
                                                   int     maxElements)
    {
        final String        methodName = "getAssetCommentReplies";

        return getAttachedComments(serverName, serviceURLName, userId, assetGUID, commentGUID, elementStart, maxElements, methodName);
    }


    /**
     * Returns the list of connections for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                              String  serviceURLName,
                                              String  userId,
                                              String  assetGUID,
                                              int     elementStart,
                                              int     maxElements)
    {
        final String        methodName = "getConnections";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        ConnectionsResponse  response = new ConnectionsResponse();
        AuditLog             auditLog = null;

        try
        {
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getConnections(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of external identifiers for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                                              String  serviceURLName,
                                                              String  userId,
                                                              String  assetGUID,
                                                              int     elementStart,
                                                              int     maxElements)
    {
        final String        methodName = "getExternalIdentifiers";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        ExternalIdentifiersResponse  response = new ExternalIdentifiersResponse();
        AuditLog                     auditLog = null;

        try
        {
            ExternalIdentifierHandler handler = instanceHandler.getExternalIdentifierHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getExternalIdentifiers(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of external references for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                                            String  serviceURLName,
                                                            String  userId,
                                                            String  assetGUID,
                                                            int     elementStart,
                                                            int     maxElements)
    {
        final String        methodName = "getExternalReferences";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        ExternalReferencesResponse  response = new ExternalReferencesResponse();
        AuditLog                    auditLog = null;

        try
        {
            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getExternalReferences(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of informal tags for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                                String  serviceURLName,
                                                String  userId,
                                                String  assetGUID,
                                                int     elementStart,
                                                int     maxElements)
    {
        final String        methodName = "getInformalTags";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        InformalTagsResponse  response = new InformalTagsResponse();
        AuditLog              auditLog = null;

        try
        {
            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getAttachedTags(userId,
                                                     assetGUID,
                                                     AssetMapper.ASSET_TYPE_NAME,
                                                     elementStart,
                                                     maxElements,
                                                     methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of licenses for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                        String  serviceURLName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        final String        methodName = "getLicenses";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        LicensesResponse  response = new LicensesResponse();
        AuditLog          auditLog = null;

        try
        {
            LicenseHandler handler = instanceHandler.getLicenseHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getLicenses(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of likes for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                  String  serviceURLName,
                                  String  userId,
                                  String  assetGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        final String        methodName = "getLikes";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        LikesResponse  response = new LikesResponse();
        AuditLog       auditLog = null;

        try
        {
            LikeHandler handler = instanceHandler.getLikeHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getLikes(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of known locations for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                               String  serviceURLName,
                                               String  userId,
                                               String  assetGUID,
                                               int     elementStart,
                                               int     maxElements)
    {
        final String        methodName = "getKnownLocations";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        LocationsResponse  response = new LocationsResponse();
        AuditLog           auditLog = null;

        try
        {
            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getLocations(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of note logs for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                        String  serviceURLName,
                                        String  userId,
                                        String  assetGUID,
                                        int     elementStart,
                                        int     maxElements)
    {
        final String        methodName = "getNoteLogs";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        NoteLogsResponse  response = new NoteLogsResponse();
        AuditLog          auditLog = null;

        try
        {
            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<NoteLog>          noteLogs = handler.getAttachedNoteLogs(userId, assetGUID, elementStart, maxElements, methodName);
            List<NoteLogResponse>  results = new ArrayList<>();

            for (NoteLog  noteLog : noteLogs)
            {
                if (noteLog != null)
                {
                    NoteLogResponse   noteLogResponse = new NoteLogResponse();

                    noteLogResponse.setNoteLog(noteLog);
                    noteLogResponse.setNoteCount(handler.countAttachedNoteLogs(userId, noteLog.getGUID(), methodName));

                    results.add(noteLogResponse);
                }
            }

            if (results.isEmpty())
            {
                response.setList(null);
            }
            else
            {
                response.setList(results);
            }

            response.setList(results);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of notes for a note log.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                  String  serviceURLName,
                                  String  userId,
                                  String  noteLogGUID,
                                  int     elementStart,
                                  int     maxElements)
    {
        final String        methodName = "getNotes";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        NotesResponse  response = new NotesResponse();
        AuditLog       auditLog = null;

        try
        {
            NoteHandler handler = instanceHandler.getNoteHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getNotes(userId, noteLogGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of ratings for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                      String  serviceURLName,
                                      String  userId,
                                      String  assetGUID,
                                      int     elementStart,
                                      int     maxElements)
    {
        final String        methodName = "getRatings";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        RatingsResponse  response = new RatingsResponse();
        AuditLog         auditLog = null;

        try
        {
            RatingHandler handler = instanceHandler.getRatingHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getRatings(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of related assets for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
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
                                                  String  serviceURLName,
                                                  String  userId,
                                                  String  assetGUID,
                                                  int     elementStart,
                                                  int     maxElements)
    {
        final String        methodName = "getRelatedAssets";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        RelatedAssetsResponse  response = new RelatedAssetsResponse();
        AuditLog               auditLog = null;

        try
        {
            List<String>  supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getRelatedAssets(userId,
                                                      supportedZones,
                                                      assetGUID,
                                                      elementStart,
                                                      maxElements,
                                                      serviceURLName,
                                                      methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of related Referenceables that provide more information for this asset, schema, ...
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param elementGUID    String   unique id for the element.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    public MoreInformationResponse getMoreInformation(String  serverName,
                                                      String  serviceURLName,
                                                      String  userId,
                                                      String  elementGUID,
                                                      int     elementStart,
                                                      int     maxElements)
    {
        final String        methodName = "getMoreInformation";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        MoreInformationResponse  response = new MoreInformationResponse();
        AuditLog                 auditLog = null;

        try
        {
            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getMoreInformation(userId,
                                                        elementGUID,
                                                        elementStart,
                                                        maxElements,
                                                        methodName));
            response.setStartingFromElement(elementStart);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the list of related media references for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId          String   userId of user making request.
     * @param assetGUID       String   unique id for asset.
     * @param elementStart   int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related media references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public RelatedMediaReferencesResponse getRelatedMediaReferences(String  serverName,
                                                                    String  serviceURLName,
                                                                    String  userId,
                                                                    String  assetGUID,
                                                                    int     elementStart,
                                                                    int     maxElements)
    {
        final String        methodName = "getRelatedMediaReferences";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        RelatedMediaReferencesResponse  response = new RelatedMediaReferencesResponse();
        AuditLog                        auditLog = null;

        try
        {
            RelatedMediaHandler handler = instanceHandler.getRelatedMediaHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getRelatedMedia(userId, assetGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns a list of schema attributes for a schema type.
     *
     * @param serverName     String   name of server instance to call.
     * @param serviceURLName String   name of the service that created the connector that issued this request.
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
                                                       String  serviceURLName,
                                                       String  userId,
                                                       String  schemaTypeGUID,
                                                       int     elementStart,
                                                       int     maxElements)
   {
        final String        methodName = "getSchemaAttributes";

        log.debug("Calling method: " + methodName + " for server " + serverName);

        SchemaAttributesResponse  response = new SchemaAttributesResponse();
        AuditLog                  auditLog = null;

        try
        {
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getSchemaAttributes(userId, schemaTypeGUID, elementStart, maxElements, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName  + " for server " + serverName + " with response: " + response.toString());

        return response;
   }
}
