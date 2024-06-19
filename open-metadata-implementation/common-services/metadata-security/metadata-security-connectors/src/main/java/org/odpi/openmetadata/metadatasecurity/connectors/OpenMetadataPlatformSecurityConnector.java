/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataPlatformSecurity;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityAuditCode;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;

/**
 * OpenMetadataPlatformSecurityConnector provides the base class for a connector that validates access to the
 * platform services that are not specific to an OMAG Server.  This optional connector can be set up once the
 * OMAGServerPlatform is running.
 * The default implementation does not allow any access.  It generates well-defined exceptions and console log
 * messages.  It is over-ridden to define the required access for the deployment environment.  The methods
 * in this base class can be called if access is to be denied as a way of reusing the message logging and exceptions.
 */
public class OpenMetadataPlatformSecurityConnector extends ConnectorBase implements OpenMetadataPlatformSecurity
{
    protected MessageFormatter messageFormatter = new MessageFormatter();
    protected String           connectorName    = null;
    protected String           serverRootURL    = null;


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the OMRS.
     *
     * @param severity is this an event, decision, error or exception?
     * @param logMessage description of the audit log record including specific resources involved
     */
    protected void logRecord(AuditLogRecordSeverity severity,
                             String                 logMessage)
    {
        System.out.println(severity.getName() + " " + logMessage);
    }



    /**
     * Write an audit log message to say that the connector is initializing.
     */
    protected void logConnectorStarting()
    {
        AuditLogMessageDefinition messageDefinition = OpenMetadataSecurityAuditCode.PLATFORM_INITIALIZING.getMessageDefinition(connectorName, serverRootURL);
        this.logRecord(messageDefinition.getSeverity(),
                       messageFormatter.getFormattedMessage(messageDefinition));
    }


    /**
     * Write an audit log message to say that the connector is stopping.
     */
    protected void logConnectorDisconnecting()
    {
        AuditLogMessageDefinition messageDefinition = OpenMetadataSecurityAuditCode.PLATFORM_SHUTDOWN.getMessageDefinition(connectorName, serverRootURL);
        this.logRecord(messageDefinition.getSeverity(),
                       messageFormatter.getFormattedMessage(messageDefinition));

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
     * @param methodName calling method
     * @throws UserNotAuthorizedException the authorization check failed
     */
    protected void throwUnauthorizedPlatformAccess(String   userId,
                                                   String   methodName) throws UserNotAuthorizedException
    {
        AuditLogMessageDefinition messageDefinition = OpenMetadataSecurityAuditCode.UNAUTHORIZED_PLATFORM_ACCESS.getMessageDefinition(userId, serverRootURL);

        this.logRecord(messageDefinition.getSeverity(),
                       messageFormatter.getFormattedMessage(messageDefinition));

        throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.UNAUTHORIZED_PLATFORM_ACCESS.getMessageDefinition(userId, serverRootURL),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
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
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    @Override
    public void  validateUserForNewServer(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserForNewServer";

        throwUnauthorizedPlatformAccess(userId, methodName);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    @Override
    public void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserAsOperatorForPlatform";

        throwUnauthorizedPlatformAccess(userId, methodName);
    }


    /**
     * Check that the calling user is authorized to issue diagnostic requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    @Override
    public void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserAsInvestigatorForPlatform";

        throwUnauthorizedPlatformAccess(userId, methodName);
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
