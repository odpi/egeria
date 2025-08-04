/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityAuditCode;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.List;

/**
 * OpenMetadataServerSecurityConnector provides the base class for an Open Metadata Security Connector for
 * both the platform and a server.  This connector is configured in an OMAG Configuration Document.
 * Its default behavior is to reject every request.  It generates well-defined exceptions and audit log
 * messages.
 * Override these to define the required access for the deployment environment.  The methods
 * in this base class can be called if access is to be denied as a way of making use of the message
 * logging and exceptions.
 */
public class OpenMetadataSecurityConnector extends ConnectorBase implements AuditLoggingComponent

{
    protected       String           serverRootURL     = null;
    protected       String           serverName        = "platform";
    protected       String           localServerUserId = null;
    protected       String           connectorName     = null;

    protected final String unknownTypeName = "<Unknown>";



    /**
     * Write a log message to say that the connector is initializing.
     *
     */
    protected void logConnectorStarting()
    {
        final String actionDescription = "Connector starting";

        if (auditLog != null)
        {
            logRecord(actionDescription,
                      OpenMetadataSecurityAuditCode.SERVICE_INITIALIZING.getMessageDefinition(connectorName, serverName));
        }
    }


    /**
     * Write an audit log message to say that the connector is stopping.
     */
    protected void logConnectorDisconnecting()
    {
        final String actionDescription = "disconnecting connector";

        if (auditLog != null)
        {
            logRecord(actionDescription,
                      OpenMetadataSecurityAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(connectorName, serverName));
        }
    }


    /**
     * Set up the URL Root for the platform where this is running.
     *
     * @param serverURLRoot url root
     */
    public void setServerPlatformURL(String    serverURLRoot)
    {
        this.serverRootURL = serverURLRoot;
    }


    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param requestType type of request
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedPlatformAccess(String   userId,
                                                   String   requestType,
                                                   String   methodName) throws UserNotAuthorizedException
    {
        AuditLogMessageDefinition messageDefinition = OpenMetadataSecurityAuditCode.UNAUTHORIZED_PLATFORM_ACCESS.getMessageDefinition(userId, requestType, serverRootURL);

        this.logRecord(methodName, messageDefinition);

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_PLATFORM_ACCESS.getMessageDefinition(userId, requestType, serverRootURL),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
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
        logRecord(methodName, OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVER_ACCESS.getMessageDefinition(userId, serverName));

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
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVICE_ACCESS.getMessageDefinition(userId,
                                                                                                 serviceOperationName,
                                                                                                 serviceName,
                                                                                                 serverName));

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
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_TYPE_ACCESS.getMessageDefinition(userId, typeName, typeGUID, serverName));

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
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_TYPE_CHANGE.getMessageDefinition(userId, typeName, typeGUID, serverName));

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
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_CREATE.getMessageDefinition(userId, typeGUID, serverName));

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
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_ACCESS.getMessageDefinition(userId,
                                                                                                  instanceGUID,
                                                                                                  typeName,
                                                                                                  serverName));

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
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_CHANGE.getMessageDefinition(userId,
                                                                                                  instanceGUID,
                                                                                                  typeName,
                                                                                                  serverName,
                                                                                                  methodName));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_INSTANCE_CHANGE.getMessageDefinition(userId,
                                                                                                                             instanceGUID,
                                                                                                                             typeName,
                                                                                                                             serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an unauthorized access.
     *
     * @param userId calling user
     * @param operationName name of operation
     * @param elementTypeName type of element
     * @param elementGUID element being accessed
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedAnchorAccess(String   userId,
                                                 String   operationName,
                                                 String   elementTypeName,
                                                 String   elementGUID,
                                                 String   methodName) throws UserNotAuthorizedException
    {
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_ANCHOR_ACCESS.getMessageDefinition(userId, operationName, elementTypeName, elementGUID));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ANCHOR_ACCESS.getMessageDefinition(userId, operationName, elementTypeName, elementGUID),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }



    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param underLyingException optional exception
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnknownUser(String    userId,
                                    Exception underLyingException,
                                    String    methodName) throws UserNotAuthorizedException
    {
        if (underLyingException == null)
        {
            logRecord(methodName, OpenMetadataSecurityAuditCode.UNKNOWN_USER.getMessageDefinition(userId));

            throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNKNOWN_USER.getMessageDefinition(userId),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
        else
        {
            logRecord(methodName,
                      OpenMetadataSecurityAuditCode.FAILED_TO_RETRIEVE_USER.getMessageDefinition(underLyingException.getClass().getName(),
                                                                                                 userId,
                                                                                                 underLyingException.getMessage()));

            throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.FAILED_TO_RETRIEVE_USER.getMessageDefinition(underLyingException.getClass().getName(),
                                                                                                                            userId,
                                                                                                                            underLyingException.getMessage()),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 underLyingException,
                                                 userId);
        }
    }



    /**
     * Write an audit log message and throw exception to record an
     * unauthorized access.
     *
     * @param userId calling user
     * @param elementGUID element being accessed
     * @param originalZones previous value of the zone membership for the element being accessed
     * @param newZones new value of the zone membership for the element being accessed
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedZoneChange(String        userId,
                                               String         elementGUID,
                                               List<String>  originalZones,
                                               List<String>  newZones,
                                               String        methodName) throws UserNotAuthorizedException
    {
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_ZONE_CHANGE.getMessageDefinition(userId,
                                                                                              elementGUID,
                                                                                              this.printZoneList(originalZones),
                                                                                              this.printZoneList(newZones)));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ZONE_CHANGE.getMessageDefinition(userId,
                                                                                                                         elementGUID,
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
     * @param elementGUID element in error
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    protected void throwUnauthorizedAddFeedback(String       userId,
                                                String       elementGUID,
                                                String       methodName) throws UserNotAuthorizedException
    {
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_ADD_FEEDBACK.getMessageDefinition(userId,
                                                                                               elementGUID));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ASSET_FEEDBACK.getMessageDefinition(userId,
                                                                                                                            elementGUID),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an unauthorized access.
     *
     * @param userId calling user
     * @param operation of requested operation
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwMissingAnchor(String       userId,
                                      String       operation,
                                      String       methodName) throws UserNotAuthorizedException
    {
        logRecord(methodName, OpenMetadataSecurityAuditCode.NULL_ANCHOR.getMessageDefinition(userId, operation));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.NULL_ANCHOR.getMessageDefinition(userId, operation),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Write an audit log message and throw exception to record an unauthorized access.
     *
     * @param userId calling user
     * @param operation of requested operation
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedElementAccess(String userId,
                                                  String operation,
                                                  String entityGUID,
                                                  String entityTypeName,
                                                  String methodName) throws UserNotAuthorizedException
    {
        logRecord(methodName,
                  OpenMetadataSecurityAuditCode.UNAUTHORIZED_ELEMENT_ACCESS.getMessageDefinition(userId, operation, entityTypeName, entityGUID));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_ELEMENT_ACCESS.getMessageDefinition(userId,
                                                                                                                            operation,
                                                                                                                            entityTypeName,
                                                                                                                            entityGUID),
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
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
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
     * @throws UserNotAuthorizedException unknown user
     */
    protected List<String> setSupportedZonesForUser(List<String>  supportedZones,
                                                    String        serviceName,
                                                    String        user) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        return supportedZones;
    }



    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public void  validateUserForNewServer(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserForNewServer";
        final String requestType = "NewServer";

        throwUnauthorizedPlatformAccess(userId, requestType, methodName);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    public void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserAsOperatorForPlatform";
        final String requestType = "Operator";

        throwUnauthorizedPlatformAccess(userId, requestType, methodName);
    }


    /**
     * Check that the calling user is authorized to issue diagnostic requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    public void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserAsInvestigatorForPlatform";
        final String requestType = "Investigator";

        throwUnauthorizedPlatformAccess(userId, requestType, methodName);
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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

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

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        if (originalAttributeTypeDef != null)
        {
            throwUnauthorizedTypeChange(userId,
                                        originalAttributeTypeDef.getGUID(),
                                        originalAttributeTypeDef.getName(),
                                        methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
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
     * Tests for whether a specific user should have the right to update an instance within a repository.
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
     * Tests for whether a specific user should have the right to delete an instance within a repository.
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
     * Tests for whether a specific user should have the right to restore an instance within a repository.
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
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
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
     * Tests for whether a specific user should have the right to change an instance's type within a repository.
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
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
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
     * Tests for whether a specific user should have the right to create an instance within a repository.
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
     * Tests for whether a specific user should have the right to update an instance within a repository.
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
     * Tests for whether a specific user should have the right to delete an instance within a repository.
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
     * Tests for whether a specific user should have the right to restore an instance within a repository.
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
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
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
     * Tests for whether a specific user should have the right to change an instance's type within a repository.
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
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
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
        logConnectorDisconnecting();

        super.disconnect();
    }
}
