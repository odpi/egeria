/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityAuditCode;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.metadatasecurity.properties.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.List;

/**
 * OpenMetadataServerSecurityConnector provides the base class for an Open Metadata Security Connector for
 * a server.  This connector is configured in an OMAG Configuration Document.  Its default behavior
 * is to reject every request.  It generates well-defined exceptions and audit log
 * messages.
 *
 * Override these to define the required access for the deployment environment.  The methods
 * in this base class can be called if access is to be denied as a way of making use of the message
 * logging and exceptions.
 */
public class OpenMetadataServerSecurityConnector extends ConnectorBase implements AuditLoggingComponent

{
    protected AuditLog auditLog          = null;
    protected String   serverName        = null;
    protected String   localServerUserId = null;
    protected String   connectorName     = null;

    protected final String unknownTypeName = "<Unknown>";


    /**
     * Write an audit log message to say that the connector is initializing.
     */
    protected void logConnectorStarting()
    {
        if (auditLog != null)
        {
            final String actionDescription = "start";

            auditLog.logMessage(actionDescription,
                                OpenMetadataSecurityAuditCode.SERVICE_INITIALIZING.getMessageDefinition(connectorName, serverName));
        }
    }


    /**
     * Write an audit log message to say that the connector is stopping.
     */
    protected void logConnectorDisconnecting()
    {
        if (auditLog != null)
        {
            final String actionDescription = "disconnect";

            auditLog.logMessage(actionDescription,
                                OpenMetadataSecurityAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(connectorName, serverName));
        }
    }


    /**
     * Return a string representing the unique identifier for the asset.
     * If the asset is null then the guid is "null", if the guid is null then
     * the result is "null-guid".
     *
     * @param asset asset to test
     * @return string identifier for messages
     */
    protected  String  getAssetGUID(Asset  asset)
    {
        if (asset == null)
        {
            return "<null>";
        }

        if (asset.getGUID() == null)
        {
            return "<null-guid>";
        }

        return asset.getGUID();
    }



    /**
     * Return a string representing the list of zones.
     *
     * @param zones zones to output
     * @return string for messages
     */
    protected  String  printZoneList(List<String>   zones)
    {
        if (zones == null)
        {
            return "<null>";
        }

        if (zones.isEmpty())
        {
            return "[]";
        }

        return zones.toString();
    }


    /**
     * Return a string representing the unique identifier for the connection.
     * If the connection is null then the guid is "null", if the guid is null then
     * the result is "null-name".
     *
     * @param connection connection to test
     * @return string identifier for messages
     */
    protected  String  getConnectionQualifiedName(Connection  connection)
    {
        if (connection == null)
        {
            return "<null>";
        }

        if (connection.getQualifiedName() == null)
        {
            return "<null-name>";
        }

        return connection.getQualifiedName();
    }



    /**
     * Return a string representing the unique identifier for a repository instance.
     * If the instance is null then the guid is "null", if the guid is null then
     * the result is "null-guid".
     *
     * @param instance instance to test
     * @return string identifier for messages
     */
    protected  String  getInstanceGUID(InstanceHeader instance)
    {
        if (instance == null)
        {
            return "<null>";
        }

        if (instance.getGUID() == null)
        {
            return "<null-guid>";
        }

        return instance.getGUID();
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedServerAccess(String   userId,
                                                 String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName, OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVER_ACCESS.getMessageDefinition(userId, serverName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_SERVER_ACCESS.getMessageDefinition(userId, serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param serviceName name of service
     * @param serviceOperationName name of operation
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedServiceAccess(String   userId,
                                                  String   serviceName,
                                                  String   serviceOperationName,
                                                  String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVICE_ACCESS.getMessageDefinition(userId,
                                                                                                               serviceOperationName,
                                                                                                               serviceName,
                                                                                                               serverName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_SERVICE_ACCESS.getMessageDefinition(userId, serviceOperationName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param typeGUID uniqueId of type
     * @param typeName name of type
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedTypeAccess(String   userId,
                                               String   typeGUID,
                                               String   typeName,
                                               String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_TYPE_ACCESS.getMessageDefinition(userId, typeName, typeGUID, serverName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_TYPE_ACCESS.getMessageDefinition(userId,
                                                                                                                         typeName,
                                                                                                                         typeGUID,
                                                                                                                         serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param typeGUID uniqueId of type
     * @param typeName name of type
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedTypeChange(String   userId,
                                               String   typeGUID,
                                               String   typeName,
                                               String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_TYPE_CHANGE.getMessageDefinition(userId, typeName, typeGUID, serverName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_TYPE_CHANGE.getMessageDefinition(userId,
                                                                                                                         typeName,
                                                                                                                         typeGUID,
                                                                                                                         serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an unauthorized access.
     *
     * @param userId calling user
     * @param typeGUID uniqueId of type
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedInstanceCreate(String   userId,
                                                   String   typeGUID,
                                                   String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_CREATE.getMessageDefinition(userId, typeGUID, serverName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_TYPE_CHANGE.getMessageDefinition(userId, typeGUID, serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param instanceGUID uniqueId of type
     * @param typeName name of type
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedInstanceAccess(String   userId,
                                                   String   instanceGUID,
                                                   String   typeName,
                                                   String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_ACCESS.getMessageDefinition(userId,
                                                                                                                instanceGUID,
                                                                                                                typeName,
                                                                                                                serverName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_INSTANCE_ACCESS.getMessageDefinition(userId,
                                                                                                                             instanceGUID,
                                                                                                                             typeName,
                                                                                                                             serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param instanceGUID uniqueId of type
     * @param typeName name of type
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedInstanceChange(String   userId,
                                                   String   instanceGUID,
                                                   String   typeName,
                                                   String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_CHANGE.getMessageDefinition(userId,
                                                                                                                instanceGUID,
                                                                                                                typeName,
                                                                                                                serverName,
                                                                                                                methodName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_INSTANCE_CHANGE.getMessageDefinition(userId,
                                                                                                                             instanceGUID,
                                                                                                                             typeName,
                                                                                                                             serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param asset asset being accessed
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedAssetAccess(String   userId,
                                                Asset    asset,
                                                String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_ASSET_ACCESS.getMessageDefinition(userId, this.getAssetGUID(asset)));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ASSET_ACCESS.getMessageDefinition(userId,
                                                                                                                          this.getAssetGUID(asset)),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param asset asset being accessed
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedAssetChange(String   userId,
                                                Asset    asset,
                                                String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_ASSET_CHANGE.getMessageDefinition(userId, this.getAssetGUID(asset)));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ASSET_CHANGE.getMessageDefinition(userId,
                                                                                                                          this.getAssetGUID(asset)),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param asset asset being accessed
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedAssetCreate(String   userId,
                                                Asset    asset,
                                                String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_ASSET_CREATE.getMessageDefinition(userId, asset.getTypeName()));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ASSET_CREATE.getMessageDefinition(userId,
                                                                                                                          asset.getTypeName()),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param asset asset being accessed
     * @param propertyName name of property that is missing
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwIncompleteAsset(String   userId,
                                        Asset    asset,
                                        String   propertyName,
                                        String   methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.INCOMPLETE_ASSET.getMessageDefinition(userId, this.getAssetGUID(asset), propertyName));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.INCOMPLETE_ASSET.getMessageDefinition(userId,
                                                                                                                 this.getAssetGUID(asset),
                                                                                                                 propertyName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param asset asset being accessed
     * @param originalZones previous value of the zone membership for the asset being accessed
     * @param newZones new value of the zone membership for the asset being accessed
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedZoneChange(String        userId,
                                               Asset         asset,
                                               List<String>  originalZones,
                                               List<String>  newZones,
                                               String        methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_ZONE_CHANGE.getMessageDefinition(userId,
                                                                                                            this.getAssetGUID(asset),
                                                                                                            this.printZoneList(originalZones),
                                                                                                            this.printZoneList(newZones)));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ZONE_CHANGE.getMessageDefinition(userId,
                                                                                                                         this.getAssetGUID(asset),
                                                                                                                         this.printZoneList(originalZones),
                                                                                                                         this.printZoneList(newZones)),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param asset asset in error
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    protected void throwUnauthorizedAssetFeedback(String       userId,
                                                  Asset        asset,
                                                  String       methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_ASSET_FEEDBACK.getMessageDefinition(userId,
                                                                                                               this.getAssetGUID(asset)));
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ASSET_FEEDBACK.getMessageDefinition(userId,
                                                                                                                            this.getAssetGUID(asset)),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param connection connection to validate
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedConnectionAccess(String       userId,
                                                     Connection   connection,
                                                     String       methodName) throws UserNotAuthorizedException
    {
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVICE_ACCESS.getMessageDefinition(userId,
                                                                                                               this.getConnectionQualifiedName(connection)),
                                connection.toString());
        }

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_SERVICE_ACCESS.getMessageDefinition(userId,
                                                                                                                            this.getConnectionQualifiedName(connection)),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set the name of the server that this connector is supporting.
     *
     * @param serverName name of server
     */
    public void  setServerName(String   serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Provide the local server's userId.  This is used for requests that originate from within the local
     * server.
     *
     * @param userId local server's userId
     */
    public void setLocalServerUserId(String    userId)
    {
        this.localServerUserId = userId;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        connectorName = this.getClass().getName();
        logConnectorStarting();
    }


    /**
     * Determine the appropriate setting for the supported zones depending on the user and the
     * default supported zones set up for the service.  This is called whenever an asset is accessed.
     *
     * @param supportedZones default setting of the supported zones for the service
     * @param serviceName name of the called service
     * @param user name of the user
     *
     * @return list of supported zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    protected List<String> setSupportedZonesForUser(List<String>  supportedZones,
                                                    String        serviceName,
                                                    String        user) throws InvalidParameterException,
                                                                               PropertyServerException
    {
        return supportedZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * default zones.  This is called whenever a new asset is created.
     *
     * The default behavior is to use the default values, unless the zones have been explicitly set up,
     * in which case, they are left unchanged.
     *
     * @param defaultZones setting of the default zones for the service
     * @param asset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    protected List<String> setAssetZonesToDefault(List<String>  defaultZones,
                                                  Asset         asset) throws InvalidParameterException,
                                                                              PropertyServerException
    {
        List<String>  resultingZones = null;

        if (asset != null)
        {
            if ((asset.getZoneMembership() == null) || (asset.getZoneMembership().isEmpty()))
            {
                resultingZones = defaultZones;
            }
            else
            {
                resultingZones = asset.getZoneMembership();
            }
        }

        return resultingZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * The default behavior is to keep the updated zones as they are.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param publishZones  setting of the publishZones for the service
     * @param originalAsset original values for the asset
     * @param updatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    protected List<String> verifyAssetZones(List<String>  defaultZones,
                                            List<String>  supportedZones,
                                            List<String>  publishZones,
                                            Asset         originalAsset,
                                            Asset         updatedAsset) throws InvalidParameterException,
                                                                               PropertyServerException
    {
        List<String>  resultingZones = null;

        if (updatedAsset != null)
        {
            resultingZones = updatedAsset.getZoneMembership();
        }

        return resultingZones;
    }


    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    protected void validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForServer";

        throwUnauthorizedServerAccess(userId, methodName);
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    protected void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserAsServerAdmin";
        final String  serviceName = "Administration Services";
        final String  serviceOperationName = "configuration";

        throwUnauthorizedServiceAccess(userId, serviceName, serviceOperationName, methodName);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    protected void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserAsServerOperator";
        final String  serviceName = "Administration Services";
        final String  serviceOperationName = "operations";

        throwUnauthorizedServiceAccess(userId, serviceName, serviceOperationName, methodName);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    protected void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserAsServerInvestigator";
        final String  serviceName = "Administration Services";
        final String  serviceOperationName = "query";

        throwUnauthorizedServiceAccess(userId, serviceName, serviceOperationName, methodName);
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    protected void  validateUserForService(String   userId,
                                           String   serviceName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForService";
        final String  serviceOperationName = "any";

        throwUnauthorizedServiceAccess(userId, serviceName, serviceOperationName, methodName);
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param serviceOperationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    protected void  validateUserForServiceOperation(String   userId,
                                                    String   serviceName,
                                                    String   serviceOperationName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForServiceOperation";

        throwUnauthorizedServiceAccess(userId, serviceName, serviceOperationName, methodName);
    }

    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    protected void  validateUserForConnection(String     userId,
                                              Connection connection) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForConnection";

        throwUnauthorizedConnectionAccess(userId, connection, methodName);
    }


    /**
     * Select a connection from the list of connections attached to an asset.
     *
     * @param userId calling user
     * @param asset asset requested by caller
     * @param connections list of attached connections
     * @return selected connection or null (pretend there are no connections attached to the asset) or
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    protected Connection validateUserForAssetConnectionList(String           userId,
                                                            Asset            asset,
                                                            List<Connection> connections) throws UserNotAuthorizedException
    {
        UserNotAuthorizedException caughtException = null;

        if ((connections != null) && (! connections.isEmpty()))
        {
            for (Connection connection : connections)
            {
                if (connection != null)
                {
                    try
                    {
                        validateUserForConnection(userId, connection);
                        return connection;
                    }
                    catch (UserNotAuthorizedException error)
                    {
                        caughtException = error;
                    }
                }
            }
        }

        if (caughtException != null)
        {
            throw caughtException;
        }

        return null;
    }


    /**
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    protected void  validateUserForAssetCreate(String     userId,
                                               Asset      asset) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForAssetCreate";

        throwUnauthorizedAssetCreate(userId, asset, methodName);
    }


    /**
     * Tests for whether a specific user should have read access to a specific asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset to test
     * @throws UserNotAuthorizedException the user is not authorized to access this asset
     */
    protected void  validateUserForAssetRead(String     userId,
                                             Asset      asset) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForAssetRead";

        throwUnauthorizedAssetAccess(userId, asset, methodName);
    }


    /**
     * Tests for whether a specific user should have the right to update an asset.
     * This is used for a general asset update, which may include changes to the
     * zones and the ownership.
     *
     * @param userId identifier of user
     * @param originalAsset original asset details
     * @param originalAssetAuditHeader details of the asset's audit header
     * @param newAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    protected void  validateUserForAssetDetailUpdate(String           userId,
                                                     Asset            originalAsset,
                                                     AssetAuditHeader originalAssetAuditHeader,
                                                     Asset            newAsset) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForAssetDetailUpdate";

        throwUnauthorizedAssetChange(userId, originalAsset, methodName);
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an asset such as schema and connections.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    protected void validateUserForAssetAttachmentUpdate(String     userId,
                                                        Asset      asset) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForAssetAttachmentUpdate";

        throwUnauthorizedAssetChange(userId, asset, methodName);
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the asset.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    protected void  validateUserForAssetFeedback(String     userId,
                                                 Asset      asset) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForAssetFeedback";

        throwUnauthorizedAssetFeedback(userId, asset, methodName);
    }


    /**
     * Tests for whether a specific user should have the right to delete an asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    protected void  validateUserForAssetDelete(String     userId,
                                               Asset      asset) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForAssetDelete";

        throwUnauthorizedAssetChange(userId, asset, methodName);
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeCreate(String     userId,
                                              String     metadataCollectionName,
                                              TypeDef    typeDef) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeCreate";

        if (typeDef != null)
        {
            throwUnauthorizedTypeChange(userId, typeDef.getGUID(), typeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeCreate(String              userId,
                                              String              metadataCollectionName,
                                              AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeCreate";

        if (attributeTypeDef != null)
        {
            throwUnauthorizedTypeChange(userId, attributeTypeDef.getGUID(), attributeTypeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef  type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    protected void  validateUserForTypeRead(String       userId,
                                            String       metadataCollectionName,
                                            TypeDef      typeDef) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeRead";

        if (typeDef != null)
        {
            throwUnauthorizedTypeAccess(userId, typeDef.getGUID(), typeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef  type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    protected void  validateUserForTypeRead(String                userId,
                                            String                metadataCollectionName,
                                            AttributeTypeDef      attributeTypeDef) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeRead";

        if (attributeTypeDef != null)
        {
            throwUnauthorizedTypeAccess(userId, attributeTypeDef.getGUID(), attributeTypeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef current typeDef details
     * @param patch proposed changes to type
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeUpdate(String       userId,
                                              String       metadataCollectionName,
                                              TypeDef      typeDef,
                                              TypeDefPatch patch) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeUpdate";

        if (typeDef != null)
        {
            throwUnauthorizedTypeChange(userId, typeDef.getGUID(), typeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeDelete(String     userId,
                                              String     metadataCollectionName,
                                              TypeDef    typeDef) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeDelete";

        if (typeDef != null)
        {
            throwUnauthorizedTypeChange(userId, typeDef.getGUID(), typeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeDelete(String           userId,
                                              String           metadataCollectionName,
                                              AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeDelete";

        if (attributeTypeDef != null)
        {
            throwUnauthorizedTypeChange(userId, attributeTypeDef.getGUID(), attributeTypeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeReIdentify(String  userId,
                                                  String  metadataCollectionName,
                                                  TypeDef originalTypeDef,
                                                  String  newTypeDefGUID,
                                                  String  newTypeDefName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeReIdentify";

        if (originalTypeDef != null)
        {
            throwUnauthorizedTypeChange(userId, originalTypeDef.getGUID(), originalTypeDef.getName(), methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    protected void  validateUserForTypeReIdentify(String           userId,
                                                  String           metadataCollectionName,
                                                  AttributeTypeDef originalAttributeTypeDef,
                                                  String           newTypeDefGUID,
                                                  String           newTypeDefName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForTypeReIdentify";

        if (originalAttributeTypeDef != null)
        {
            throwUnauthorizedTypeChange(userId,
                                        originalAttributeTypeDef.getGUID(),
                                        originalAttributeTypeDef.getName(),
                                        methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityCreate(String                     userId,
                                                String                     metadataCollectionName,
                                                String                     entityTypeGUID,
                                                InstanceProperties         initialProperties,
                                                List<Classification>       initialClassifications,
                                                InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityCreate";

        throwUnauthorizedInstanceCreate(userId, entityTypeGUID, methodName);
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (maybe altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    protected EntityDetail  validateUserForEntityRead(String          userId,
                                                      String          metadataCollectionName,
                                                      EntityDetail    instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityRead";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }

            throwUnauthorizedInstanceAccess(userId, instance.getGUID(), typeName, methodName);
        }

        return null;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    protected void  validateUserForEntitySummaryRead(String        userId,
                                                     String        metadataCollectionName,
                                                     EntitySummary instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntitySummaryRead";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceAccess(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    protected void  validateUserForEntityProxyRead(String      userId,
                                                   String      metadataCollectionName,
                                                   EntityProxy instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityProxyRead";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }

            throwUnauthorizedInstanceAccess(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityUpdate(String          userId,
                                                String          metadataCollectionName,
                                                EntityDetail    instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityUpdate";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to add a classification to an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityClassificationAdd(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityClassificationAdd";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityClassificationUpdate(String               userId,
                                                              String               metadataCollectionName,
                                                              EntitySummary        instance,
                                                              String               classificationName,
                                                              InstanceProperties   properties) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityClassificationUpdate";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityClassificationDelete(String               userId,
                                                              String               metadataCollectionName,
                                                              EntitySummary        instance,
                                                              String               classificationName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityClassificationDelete";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityDelete(String       userId,
                                                String       metadataCollectionName,
                                                EntityDetail instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityDelete";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to restore a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityRestore(String       userId,
                                                 String       metadataCollectionName,
                                                 String       deletedEntityGUID) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityRestore";

        throwUnauthorizedInstanceChange(userId, deletedEntityGUID, "<Unknown>", methodName);
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityReIdentification(String       userId,
                                                          String       metadataCollectionName,
                                                          EntityDetail instance,
                                                          String       newGUID) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityReIdentification";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), type.getTypeDefName(), methodName);
            }
            else
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), unknownTypeName, methodName);
            }
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the type of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityReTyping(String         userId,
                                                  String         metadataCollectionName,
                                                  EntityDetail   instance,
                                                  TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityReTyping";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), type.getTypeDefName(), methodName);
            }
            else
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), unknownTypeName, methodName);
            }
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForEntityReHoming(String         userId,
                                                  String         metadataCollectionName,
                                                  EntityDetail   instance,
                                                  String         newHomeMetadataCollectionId,
                                                  String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForEntityReHoming";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), type.getTypeDefName(), methodName);
            }
            else
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), unknownTypeName, methodName);
            }
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneSummary the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoSummary the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForRelationshipCreate(String               userId,
                                                      String               metadataCollectionName,
                                                      String               relationshipTypeGUID,
                                                      InstanceProperties   initialProperties,
                                                      EntitySummary        entityOneSummary,
                                                      EntitySummary        entityTwoSummary,
                                                      InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipCreate";

        throwUnauthorizedInstanceCreate(userId, relationshipTypeGUID, methodName);
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return relationship
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    protected Relationship validateUserForRelationshipRead(String       userId,
                                                           String       metadataCollectionName,
                                                           Relationship instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipRead";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceAccess(userId, instance.getGUID(), typeName, methodName);
        }

        return null;
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void validateUserForRelationshipUpdate(String        userId,
                                                     String        metadataCollectionName,
                                                     Relationship  instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipUpdate";

        if (instance != null)
        {
            String typeName = null;

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForRelationshipDelete(String       userId,
                                                      String       metadataCollectionName,
                                                      Relationship instance) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipDelete";

        if (instance != null)
        {
            String typeName = "<Unknown>";

            if (instance.getType() != null)
            {
                typeName = instance.getType().getTypeDefName();
            }
            throwUnauthorizedInstanceChange(userId, instance.getGUID(), typeName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to restore a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForRelationshipRestore(String       userId,
                                                       String       metadataCollectionName,
                                                       String       deletedRelationshipGUID) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipRestore";

        throwUnauthorizedInstanceChange(userId, deletedRelationshipGUID, "<Unknown>", methodName);
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForRelationshipReIdentification(String       userId,
                                                                String       metadataCollectionName,
                                                                Relationship instance,
                                                                String       newGUID) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipReIdentification";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), type.getTypeDefName(), methodName);
            }
            else
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), unknownTypeName, methodName);
            }
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the type of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForRelationshipReTyping(String         userId,
                                                        String         metadataCollectionName,
                                                        Relationship   instance,
                                                        TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipReTyping";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), type.getTypeDefName(), methodName);
            }
            else
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), unknownTypeName, methodName);
            }
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    protected void  validateUserForRelationshipReHoming(String         userId,
                                                        String         metadataCollectionName,
                                                        Relationship   instance,
                                                        String         newHomeMetadataCollectionId,
                                                        String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        final String  methodName = "validateUserForRelationshipReHoming";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), type.getTypeDefName(), methodName);
            }
            else
            {
                throwUnauthorizedInstanceChange(userId, instance.getGUID(), unknownTypeName, methodName);
            }
        }
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        logConnectorDisconnecting();
    }
}
