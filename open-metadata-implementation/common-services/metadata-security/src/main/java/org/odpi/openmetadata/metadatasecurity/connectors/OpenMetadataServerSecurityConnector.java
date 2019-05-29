/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataConnectionSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServerSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServiceSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataZoneSecurity;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;

/**
 * OpenMetadataServerSecurityConnector provides the base class for an Open Metadata Security Connector for
 * a server.  This connector is configured in an OMAG Configuration Document.
 */
public class OpenMetadataServerSecurityConnector extends ConnectorBase implements AuditableConnector,
                                                                                  OpenMetadataServerSecurity,
                                                                                  OpenMetadataServiceSecurity,
                                                                                  OpenMetadataConnectionSecurity,
                                                                                  OpenMetadataZoneSecurity
{
    protected  OMRSAuditLog  auditLog = null;
    protected  String        serverName = null;

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(OMRSAuditLog auditLog)
    {
        this.auditLog = auditLog;
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
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    public void  validateUserForService(String   userId,
                                        String   serviceName) throws UserNotAuthorizedException
    {
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param operationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   operationName) throws UserNotAuthorizedException
    {
    }

    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    public void  validateUserForConnection(String     userId,
                                           Connection connection) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have access to assets within a zone.
     *
     * @param userId identifier of user
     * @param zoneName name of the zones
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    public void  validateUserForZone(String     userId,
                                     String     zoneName) throws UserNotAuthorizedException
    {
    }
}
