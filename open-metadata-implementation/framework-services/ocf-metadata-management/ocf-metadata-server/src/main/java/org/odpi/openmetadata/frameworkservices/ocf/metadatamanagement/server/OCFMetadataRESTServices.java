/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc.OCFMetadataAuditCode;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The OCFMetadataRESTServices is the server-side implementation of the Connected Asset REST interface used by connectors.
 */
public class OCFMetadataRESTServices
{
    private static final  OCFMetadataInstanceHandler instanceHandler = new OCFMetadataInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OCFMetadataRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private   final  RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public OCFMetadataRESTServices()
    {
    }

    /**
     * Log an audit message about this asset.
     *
     * @param serverName     name of server instance to route request to
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     * @param governanceService name of governance service
     * @param message        message to log
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public VoidResponse logAssetAuditMessage(String serverName,
                                             String userId,
                                             String assetGUID,
                                             String governanceService,
                                             String message)
    {
        final String   methodName = "logAssetAuditMessage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            auditLog.logMessage(methodName, OCFMetadataAuditCode.ASSET_AUDIT_LOG.getMessageDefinition(assetGUID,
                                                                                                      governanceService,
                                                                                                      message));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
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
        final String guidParameterName = "guid";
        final String methodName = "getConnectionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            ConnectionHandler<Connection> connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(connectionHandler.getBeanFromRepository(userId,
                                                                           guid,
                                                                           guidParameterName,
                                                                           OpenMetadataType.CONNECTION.typeName,
                                                                           false,
                                                                           false,
                                                                           instanceHandler.getSupportedZones(userId,
                                                                                                             serverName,
                                                                                                             serviceURLName,
                                                                                                             methodName),
                                                                           new Date(),
                                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId userId of user making request.
     * @param name   this may be the qualifiedName or displayName of the connection.
     *
     * @return connection object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException - there is no connection defined for this name or
     * AmbiguousConnectionNameException - there is more than one connection defined for this name or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByName(String   serverName,
                                                  String   serviceURLName,
                                                  String   userId,
                                                  String   name)
    {
        final String nameParameterName = "name";
        final String methodName = "getConnectionByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            ConnectionHandler<Connection> connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(connectionHandler.getBeanByUniqueName(userId,
                                                                         name,
                                                                         nameParameterName,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         OpenMetadataType.CONNECTION.typeGUID,
                                                                         OpenMetadataType.CONNECTION.typeName,
                                                                         null,
                                                                         null,
                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         instanceHandler.getSupportedZones(userId,
                                                                                                           serverName,
                                                                                                           serviceURLName,
                                                                                                           methodName),
                                                                         null,
                                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
        final String connectionGUIDParameterName = "connectionGUID";
        final String methodName = "getAssetForConnectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;


        try
        {
            AssetHandler<Asset> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.getAssetForConnection(userId,
                                                           connectionGUID,
                                                           connectionGUIDParameterName,
                                                           instanceHandler.getSupportedZones(userId,
                                                                                             serverName,
                                                                                             serviceURLName,
                                                                                             methodName),
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String assetGUIDParameterName = "assetGUID";
        final String methodName = "getConnectionForAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse  response = new ConnectionResponse();
        AuditLog            auditLog = null;

        try
        {
            ConnectionHandler<Connection> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(handler.getConnectionForAsset(userId,
                                                                 assetGUID,
                                                                 assetGUIDParameterName,
                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                   serverName,
                                                                                                   serviceURLName,
                                                                                                   methodName),
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Save the connection optionally linked to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId      userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository. This optional.
     *                    However, if specified then the new connection is attached to the asset
     * @param connection connection to save
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse saveConnectionForAsset(String     serverName,
                                               String     serviceURLName,
                                               String     userId,
                                               String     assetGUID,
                                               Connection connection)
    {
        final String assetGUIDParameterName = "assetGUID";
        final String methodName = "saveConnectionForAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog            auditLog = null;

        try
        {
            ConnectionHandler<Connection> connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (assetGUID == null)
            {
                response.setGUID(connectionHandler.saveConnection(userId,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  connection,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  instanceHandler.getSupportedZones(userId,
                                                                                                    serverName,
                                                                                                    serviceURLName,
                                                                                                    methodName),
                                                                  new Date(),
                                                                  methodName));
            }
            else
            {
                AssetHandler<Asset> assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                Asset asset = assetHandler.getBeanFromRepository(userId,
                                                                 assetGUID,
                                                                 assetGUIDParameterName,
                                                                 OpenMetadataType.ASSET.typeName,
                                                                 false,
                                                                 false,
                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                   serverName,
                                                                                                   serviceURLName,
                                                                                                   methodName),
                                                                 new Date(),
                                                                 methodName);

                response.setGUID(connectionHandler.saveConnection(userId,
                                                                  null,
                                                                  null,
                                                                  assetGUID,
                                                                  assetGUID,
                                                                  assetGUIDParameterName,
                                                                  asset.getType().getTypeName(),
                                                                  asset.getQualifiedName(),
                                                                  connection,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  instanceHandler.getSupportedZones(userId,
                                                                                                    serverName,
                                                                                                    serviceURLName,
                                                                                                    methodName),
                                                                  new Date(),
                                                                  methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
                                           String   methodName)
    {
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        final String assetGUIDParameterName = "assetGUID";

        AssetResponse response = new AssetResponse();
        AuditLog      auditLog = null;

        try
        {
            List<String>  supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName);

            AssetHandler<Asset>                 assetHandler      = instanceHandler.getAssetHandler(userId, serverName, methodName);
            SchemaTypeHandler<SchemaType>       schemaTypeHandler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);
            OMRSRepositoryHelper                repositoryHelper  = instanceHandler.getRepositoryHelper(userId, serverName, methodName);


            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            Date effectiveTime = new Date();

            Asset asset = assetHandler.getBeanFromRepository(userId,
                                                             assetGUID,
                                                             assetGUIDParameterName,
                                                             OpenMetadataType.ASSET.typeName,
                                                             false,
                                                             false,
                                                             supportedZones,
                                                             effectiveTime,
                                                             methodName);
            if (asset != null)
            {
                EntityDetail glossaryEntity = assetHandler.getSupplementaryProperties(assetGUID,
                                                                                      assetGUIDParameterName,
                                                                                      OpenMetadataType.ASSET.typeName,
                                                                                      false,
                                                                                      false,
                                                                                      effectiveTime,
                                                                                      methodName);

                if ((glossaryEntity != null) && (glossaryEntity.getProperties() != null))
                {
                    asset.setDisplayName(repositoryHelper.getStringProperty(instanceHandler.getServiceName(serviceURLName),
                                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                                            glossaryEntity.getProperties(),
                                                                            methodName));

                    asset.setDisplaySummary(repositoryHelper.getStringProperty(instanceHandler.getServiceName(serviceURLName),
                                                                               OpenMetadataProperty.SUMMARY.name,
                                                                               glossaryEntity.getProperties(),
                                                                               methodName));
                    asset.setDisplayDescription(repositoryHelper.getStringProperty(instanceHandler.getServiceName(serviceURLName),
                                                                                   OpenMetadataProperty.DESCRIPTION.name,
                                                                                   glossaryEntity.getProperties(),
                                                                                   methodName));
                    asset.setAbbreviation(repositoryHelper.getStringProperty(instanceHandler.getServiceName(serviceURLName),
                                                                             OpenMetadataProperty.ABBREVIATION.name,
                                                                             glossaryEntity.getProperties(),
                                                                             methodName));
                    asset.setUsage(repositoryHelper.getStringProperty(instanceHandler.getServiceName(serviceURLName),
                                                                      OpenMetadataProperty.USAGE.name,
                                                                      glossaryEntity.getProperties(),
                                                                      methodName));
                }
                response.setAsset(asset);
                response.setSchemaType(schemaTypeHandler.getSchemaTypeForAsset(userId, assetGUID, assetGUIDParameterName,  false, false, effectiveTime, methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
    @SuppressWarnings(value = "unused")
    public AssetResponse getConnectedAssetSummary(String   serverName,
                                                  String   serviceURLName,
                                                  String   userId,
                                                  String   assetGUID,
                                                  String   connectionGUID)
    {
        final String methodName = "getConnectedAssetSummary";

        return this.getAssetResponse(serverName, serviceURLName, userId, assetGUID, methodName);
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

        return this.getAssetResponse(serverName, serviceURLName, userId, assetGUID, methodName);
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
        final String methodName = "getCertifications";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        final String  assetGUIDParameterName = "assetGUID";

        CertificationsResponse  response = new CertificationsResponse();
        AuditLog                auditLog = null;

        try
        {
            CertificationHandler<Certification> handler = instanceHandler.getCertificationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setList(handler.getCertifications(userId,
                                                       assetGUID,
                                                       assetGUIDParameterName,
                                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                       elementStart,
                                                       maxElements,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Returns the list of comments for the requested element.
     *
     * @param serverName   String   name of server instance to call
     * @param serviceURLName  String   name of the service that created the connector that issued this request
     * @param userId       String   userId of user making request
     * @param assetGUID    String   unique id for asset
     * @param assetGUIDParameterName String name of parameter supplying assetGUID
     * @param elementGUID    String   unique id for element object
     * @param elementGUIDParameterName String name of parameter supplying elementGUID
     * @param elementTypeName String type name of the requested element
     * @param elementStart int      starting position for fist returned element
     * @param maxElements  int      maximum number of elements to return on the call
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
                                                 String  assetGUIDParameterName,
                                                 String  elementGUID,
                                                 String  elementGUIDParameterName,
                                                 String  elementTypeName,
                                                 int     elementStart,
                                                 int     maxElements,
                                                 String  methodName)
    {
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommentsResponse  response = new CommentsResponse();
        AuditLog          auditLog = null;

        try
        {
            CommentHandler<Comment>  handler = instanceHandler.getCommentHandler(userId,serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<Comment>  attachedComments = handler.getComments(userId,
                                                                  assetGUID,
                                                                  assetGUIDParameterName,
                                                                  elementGUID,
                                                                  elementGUIDParameterName,
                                                                  elementTypeName,
                                                                  instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                  elementStart,
                                                                  maxElements,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);
            List<CommentResponse> results = new ArrayList<>();

            if (attachedComments != null)
            {
                for (Comment  comment : attachedComments)
                {
                    if (comment != null)
                    {
                        CommentResponse commentResponse = new CommentResponse();

                        commentResponse.setComment(comment);

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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String assetGUIDParameterName = "assetGUID";
        final String methodName = "getAssetComments";

        return getAttachedComments(serverName,
                                   serviceURLName,
                                   userId,
                                   assetGUID,
                                   assetGUIDParameterName,
                                   assetGUID,
                                   assetGUIDParameterName,
                                   OpenMetadataType.ASSET.typeName,
                                   elementStart,
                                   maxElements,
                                   methodName);
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
        final String assetGUIDParameterName = "assetGUID";
        final String commentGUIDParameterName = "commentGUID";
        final String methodName = "getAssetCommentReplies";

        return getAttachedComments(serverName,
                                   serviceURLName,
                                   userId,
                                   assetGUID,
                                   assetGUIDParameterName,
                                   commentGUID,
                                   commentGUIDParameterName,
                                   OpenMetadataType.COMMENT.typeName,
                                   elementStart,
                                   maxElements,
                                   methodName);
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
        final String methodName = "getConnections";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse  response = new ConnectionsResponse();
        AuditLog             auditLog = null;

        try
        {
            ConnectionHandler<Connection> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getConnectionsForAsset(userId,
                                                            assetGUID,
                                                            guidParameterName,
                                                            instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                            elementStart,
                                                            maxElements,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getExternalIdentifiersForElement";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalIdentifiersResponse  response = new ExternalIdentifiersResponse();
        AuditLog                     auditLog = null;

        try
        {
            ExternalIdentifierHandler<ExternalIdentifier, Object> handler = instanceHandler.getExternalIdentifierHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getExternalIdentifiersForElement(userId,
                                                                      assetGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataType.ASSET.typeName,
                                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                      elementStart,
                                                                      maxElements,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getExternalReferences";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferencesResponse  response = new ExternalReferencesResponse();
        AuditLog                    auditLog = null;

        try
        {
            ExternalReferenceLinkHandler<ExternalReference> handler = instanceHandler.getExternalReferenceHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getExternalReferences(userId,
                                                           assetGUID,
                                                           guidParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                           elementStart,
                                                           maxElements,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getInformalTags";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse  response = new InformalTagsResponse();
        AuditLog              auditLog = null;

        try
        {
            InformalTagHandler<InformalTag> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getAttachedTags(userId,
                                                     assetGUID,
                                                     guidParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                     elementStart,
                                                     maxElements,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getLicenses";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicensesResponse  response = new LicensesResponse();
        AuditLog          auditLog = null;

        try
        {
            LicenseHandler<License> handler = instanceHandler.getLicenseHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getLicenses(userId,
                                                 assetGUID,
                                                 guidParameterName,
                                                 OpenMetadataType.ASSET.typeName,
                                                 instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                 elementStart,
                                                 maxElements,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getLikes";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LikesResponse  response = new LikesResponse();
        AuditLog       auditLog = null;

        try
        {
            LikeHandler<Like> handler = instanceHandler.getLikeHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getLikes(userId,
                                              assetGUID,
                                              guidParameterName,
                                              OpenMetadataType.ASSET.typeName,
                                              instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                              elementStart,
                                              maxElements,
                                              false,
                                              false,
                                              new Date(),
                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getKnownLocations";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationsResponse  response = new LocationsResponse();
        AuditLog           auditLog = null;

        try
        {
            LocationHandler<Location> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getAssetLocations(userId,
                                                       assetGUID,
                                                       guidParameterName,
                                                       OpenMetadataType.ASSET.typeName,
                                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                       elementStart,
                                                       maxElements,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getNoteLogs";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogsResponse  response = new NoteLogsResponse();
        AuditLog          auditLog = null;

        try
        {
            NoteLogHandler<NoteLogHeader> handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<NoteLogHeader>          noteLogs = handler.getAttachedNoteLogs(userId,
                                                                                assetGUID,
                                                                                guidParameterName,
                                                                                OpenMetadataType.ASSET.typeName,
                                                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                                elementStart,
                                                                                maxElements,
                                                                                false,
                                                                                false,
                                                                                new Date(),
                                                                                methodName);
            List<NoteLogResponse>  results = new ArrayList<>();

            if (noteLogs != null)
            {
                for (NoteLogHeader noteLog : noteLogs)
                {
                    if (noteLog != null)
                    {
                        NoteLogResponse noteLogResponse = new NoteLogResponse();

                        noteLogResponse.setNoteLog(noteLog);

                        results.add(noteLogResponse);
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

            response.setList(results);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getNotes";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NotesResponse  response = new NotesResponse();
        AuditLog       auditLog = null;

        try
        {
            NoteHandler<Note> handler = instanceHandler.getNoteHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getNotes(userId,
                                              noteLogGUID,
                                              guidParameterName,
                                              OpenMetadataType.ASSET.typeName,
                                              instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                              elementStart,
                                              maxElements,
                                              false,
                                              false,
                                              new Date(),
                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getRatings";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RatingsResponse  response = new RatingsResponse();
        AuditLog         auditLog = null;

        try
        {
            RatingHandler<Rating> handler = instanceHandler.getRatingHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getRatings(userId,
                                                assetGUID,
                                                guidParameterName,
                                                OpenMetadataType.ASSET.typeName,
                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                elementStart,
                                                maxElements,
                                                false,
                                                false,
                                                new Date(),
                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getRelatedAssets";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedAssetsResponse  response = new RelatedAssetsResponse();
        AuditLog               auditLog = null;

        try
        {
            RelatedAssetHandler<RelatedAsset> handler = instanceHandler.getRelatedAssetHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getRelatedAssets(userId,
                                                      assetGUID,
                                                      guidParameterName,
                                                      OpenMetadataType.ASSET.typeName,
                                                      null,
                                                      null,
                                                      OpenMetadataType.ASSET.typeName,
                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                      0,
                                                      elementStart,
                                                      maxElements,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getMoreInformation";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        MoreInformationResponse  response = new MoreInformationResponse();
        AuditLog                 auditLog = null;

        try
        {
            ReferenceableHandler<Referenceable> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getMoreInformation(userId,
                                                        elementGUID,
                                                        guidParameterName,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                        elementStart,
                                                        maxElements,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName));
            response.setStartingFromElement(elementStart);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
        final String methodName = "getRelatedMediaReferences";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedMediaReferencesResponse  response = new RelatedMediaReferencesResponse();
        AuditLog                        auditLog = null;

        try
        {
            RelatedMediaHandler<RelatedMediaReference> handler = instanceHandler.getRelatedMediaHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setList(handler.getRelatedMedia(userId,
                                                     assetGUID,
                                                     guidParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                     elementStart,
                                                     maxElements,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Returns a list of schema attributes for a schema type.
     *
     * @param serverName     String   name of server instance to call.
     * @param serviceURLName String   name of the service that created the connector that issued this request.
     * @param userId         String   userId of user making request.
     * @param parentSchemaGUID String   unique id for containing schema element.
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
                                                       String  parentSchemaGUID,
                                                       int     elementStart,
                                                       int     maxElements)
   {
        final String methodName = "getSchemaAttributes";
        final String guidParameterName = "parentSchemaGUID";

       RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse  response = new SchemaAttributesResponse();
        AuditLog                  auditLog = null;

        try
        {
            SchemaAttributeHandler<SchemaAttribute, SchemaType> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<SchemaAttribute> schemaAttributes = handler.getAttachedSchemaAttributes(userId,
                                                                                         parentSchemaGUID,
                                                                                         guidParameterName,
                                                                                         OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                         instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                                         elementStart,
                                                                                         maxElements,
                                                                                         false,
                                                                                         false,
                                                                                         new Date(),
                                                                                         methodName);
            if (schemaAttributes != null)
            {
                final String                 schemaAttributeGUIDParameterName = "schemaAttribute.getGUID()";
                GlossaryTermHandler<Meaning> meaningHandler                   = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                for (SchemaAttribute schemaAttribute : schemaAttributes)
                {
                    schemaAttribute.setMeanings(meaningHandler.getAttachedMeanings(userId,
                                                                                   schemaAttribute.getGUID(),
                                                                                   schemaAttributeGUIDParameterName,
                                                                                   OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                   instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                                   0,
                                                                                   0,
                                                                                   false,
                                                                                   false,
                                                                                   new Date(),
                                                                                   methodName));
                }

                response.setList(schemaAttributes);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
   }


    /**
     * Returns a list of schema attributes for a schema type.
     *
     * @param serverName     String   name of server instance to call.
     * @param serviceURLName String   name of the service that created the connector that issued this request.
     * @param userId         String   userId of user making request.
     * @param parentSchemaTypeGUID String   unique id for containing schema type.
     * @param elementStart   int      starting position for fist returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     *
     * @return a schema attributes response or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public APIOperationsResponse getAPIOperations(String  serverName,
                                                  String  serviceURLName,
                                                  String  userId,
                                                  String  parentSchemaTypeGUID,
                                                  int     elementStart,
                                                  int     maxElements)
    {
        final String methodName = "getAPIOperations";
        final String guidParameterName = "parentSchemaTypeGUID";
        final String apiOpGUIDParameterName = "apiOperation.getGUID()";
        final String schemaTypeGUIDParameterName = "apiParameterList.getGUID()";

        Date effectiveTime = new Date();

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIOperationsResponse response = new APIOperationsResponse();
        AuditLog              auditLog = null;

        try
        {
            APIOperationHandler<APIOperation> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<APIOperation> apiOperations = handler.getAPIOperationsForAPISchemaType(userId,
                                                                                        parentSchemaTypeGUID,
                                                                                        guidParameterName,
                                                                                        instanceHandler.getSupportedZones(userId, serverName, serviceURLName, methodName),
                                                                                        elementStart,
                                                                                        maxElements,
                                                                                        false,
                                                                                        false,
                                                                                        effectiveTime,
                                                                                        methodName);

            if (apiOperations != null)
            {
                List<APIOperationResponse> resultsList = new ArrayList<>();

                for (APIOperation apiOperation : apiOperations)
                {
                    APIOperationResponse apiOperationResponse = new APIOperationResponse();

                    List<Relationship> relationships = handler.getAllAttachmentLinks(userId,
                                                                                     apiOperation.getGUID(),
                                                                                     apiOpGUIDParameterName,
                                                                                     OpenMetadataType.API_OPERATION_TYPE_NAME,
                                                                                     null,
                                                                                     null,
                                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                                     null,
                                                                                     false,
                                                                                     false,
                                                                                     effectiveTime,
                                                                                     methodName);

                    if (relationships != null)
                    {
                        OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);
                        SchemaTypeHandler<SchemaType> schemaTypeHandler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                        for (Relationship relationship : relationships)
                        {
                            if (repositoryHelper.isTypeOf(serviceURLName, relationship.getType().getTypeDefName(), OpenMetadataType.API_HEADER_RELATIONSHIP_TYPE_NAME))
                            {
                                SchemaType schemaType = schemaTypeHandler.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, false, false, effectiveTime, methodName);

                                apiOperation.setHeaderSchemaType(schemaType);

                                int attributeCount = schemaTypeHandler.countAttachments(userId,
                                                                                        schemaType.getGUID(),
                                                                                        OpenMetadataType.API_PARAMETER_LIST_TYPE_NAME,
                                                                                        OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                        OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                        2,
                                                                                        false,
                                                                                        false,
                                                                                        effectiveTime,
                                                                                        methodName);

                                apiOperationResponse.setHeaderAttributeCount(attributeCount);
                            }
                            else if (repositoryHelper.isTypeOf(serviceURLName, relationship.getType().getTypeDefName(), OpenMetadataType.API_REQUEST_RELATIONSHIP_TYPE_NAME))
                            {
                                SchemaType schemaType = schemaTypeHandler.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, false, false, effectiveTime, methodName);

                                apiOperation.setRequestSchemaType(schemaType);

                                int attributeCount = schemaTypeHandler.countAttachments(userId,
                                                                                        schemaType.getGUID(),
                                                                                        OpenMetadataType.API_PARAMETER_LIST_TYPE_NAME,
                                                                                        OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                        OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                        2,
                                                                                        false,
                                                                                        false,
                                                                                        effectiveTime,
                                                                                        methodName);

                                apiOperationResponse.setRequestAttributeCount(attributeCount);
                            }
                            else if (repositoryHelper.isTypeOf(serviceURLName, relationship.getType().getTypeDefName(), OpenMetadataType.API_RESPONSE_RELATIONSHIP_TYPE_NAME))
                            {
                                SchemaType schemaType = schemaTypeHandler.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, false, false, effectiveTime, methodName);

                                apiOperation.setResponseSchemaType(schemaType);

                                int attributeCount = schemaTypeHandler.countAttachments(userId,
                                                                                        schemaType.getGUID(),
                                                                                        OpenMetadataType.API_PARAMETER_LIST_TYPE_NAME,
                                                                                        OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                        OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                        2,
                                                                                        false,
                                                                                        false,
                                                                                        effectiveTime,
                                                                                        methodName);

                                apiOperationResponse.setResponseAttributeCount(attributeCount);
                            }
                        }
                    }

                    apiOperationResponse.setAPIOperation(apiOperation);

                    resultsList.add(apiOperationResponse);
                }

                response.setList(resultsList);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Returns the anchor asset.
     *
     * @param serverName String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId     String   userId of user making request.
     * @param guid  String   unique id for the metadata element.
     *
     * @return a bean with the basic properties about the anchor asset or
     * InvalidParameterException - the userId is null or invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetResponse getAnchorAssetFromGUID(String serverName,
                                                String serviceURLName,
                                                String userId,
                                                String guid)
    {
        final String methodName = "getAnchorAssetFromGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetResponse response = new AssetResponse();
        AuditLog      auditLog = null;

        Date effectiveTime = new Date();

        try 
        {
            ReferenceableHandler<Referenceable> referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);
            RepositoryHandler repositoryHandler = instanceHandler.getRepositoryHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                    guid,
                                                                    OpenMetadataProperty.GUID.name,
                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                    false,
                                                                    false,
                                                                    effectiveTime,
                                                                    methodName);

            if (entity != null)
            {
                if (repositoryHandler.isEntityATypeOf(userId,
                                                      guid,
                                                      OpenMetadataProperty.GUID.name,
                                                      OpenMetadataType.ASSET.typeName,
                                                      effectiveTime,
                                                      methodName))
                {
                    response = getAssetResponse(serverName, serviceURLName, userId, guid, methodName);
                }
                else
                {
                    EntityDetail anchorEntity = referenceableHandler.validateEntityAndAnchorForRead(userId,
                                                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                                                    entity,
                                                                                                    OpenMetadataProperty.GUID.name,
                                                                                                    true,
                                                                                                    false,
                                                                                                    false,
                                                                                                    false,
                                                                                                    null,
                                                                                                    effectiveTime,
                                                                                                    methodName);
                    if (anchorEntity != null && repositoryHandler.isEntityATypeOf(userId,
                                                                                  anchorEntity.getGUID(),
                                                                                  OpenMetadataProperty.GUID.name,
                                                                                  OpenMetadataType.ASSET.typeName,
                                                                                  effectiveTime,
                                                                                  methodName))
                    {
                        response = getAssetResponse(serverName, serviceURLName, userId, anchorEntity.getGUID(), methodName);
                    }
                }
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
