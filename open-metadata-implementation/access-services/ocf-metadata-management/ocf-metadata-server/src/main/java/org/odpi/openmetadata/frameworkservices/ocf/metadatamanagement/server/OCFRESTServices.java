/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OCFConnectionResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The OCFRESTServices is the server-side implementation of the Connected Asset REST interface used by connectors.
 */
public class OCFRESTServices
{
    private static final OCFServicesInstanceHandler instanceHandler = new OCFServicesInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OCFRESTServices.class),
                                                                                        instanceHandler.getServiceName());
    private   final  RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public OCFRESTServices()
    {
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OCFConnectionResponse getConnectionByGUID(String     serverName,
                                                     String     userId,
                                                     String     guid)
    {
        final String guidParameterName = "guid";
        final String methodName = "getConnectionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse response = new OCFConnectionResponse();
        AuditLog              auditLog = null;

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
                                                                           new Date(),
                                                                           methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request
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
    public OCFConnectionResponse getConnectionByName(String   serverName,
                                                     String   userId,
                                                     String   name)
    {
        final String nameParameterName = "name";
        final String methodName = "getConnectionByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse response = new OCFConnectionResponse();
        AuditLog              auditLog = null;

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
                                                                         null,
                                                                         methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
     * @param userId      userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OCFConnectionResponse getConnectionForAsset(String   serverName,
                                                       String   userId,
                                                       String   assetGUID)
    {
        final String assetGUIDParameterName = "assetGUID";
        final String methodName = "getConnectionForAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse  response = new OCFConnectionResponse();
        AuditLog               auditLog = null;

        try
        {
            ConnectionHandler<Connection> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(handler.getConnectionForAsset(userId,
                                                                 assetGUID,
                                                                 assetGUIDParameterName,
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Save the connection optionally linked to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
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
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
            }
            else
            {
                EntityDetail asset = connectionHandler.getEntityFromRepository(userId,
                                                                               assetGUID,
                                                                               assetGUIDParameterName,
                                                                               OpenMetadataType.ASSET.typeName,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               new Date(),
                                                                               methodName);

                response.setGUID(connectionHandler.saveConnection(userId,
                                                                  null,
                                                                  null,
                                                                  assetGUID,
                                                                  assetGUID,
                                                                  assetGUIDParameterName,
                                                                  asset.getType().getTypeDefName(),
                                                                  connectionHandler.getRepositoryHelper().getStringProperty(serverName,
                                                                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                            asset.getProperties(),
                                                                                                                            methodName),
                                                                  connection,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
