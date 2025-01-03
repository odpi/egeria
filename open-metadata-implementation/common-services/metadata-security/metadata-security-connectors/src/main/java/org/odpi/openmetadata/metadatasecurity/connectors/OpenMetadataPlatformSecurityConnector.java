/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataPlatformSecurity;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityAuditCode;

/**
 * OpenMetadataPlatformSecurityConnector provides the base class for a connector that validates access to the
 * platform services that are not specific to an OMAG Server.  This optional connector can be set up once the
 * OMAGServerPlatform is running.
 * The default implementation does not allow any access.  It generates well-defined exceptions and console log
 * messages.  It is over-ridden to define the required access for the deployment environment.  The methods
 * in this base class can be called if access is to be denied as a way of reusing the message logging and exceptions.
 */
public class OpenMetadataPlatformSecurityConnector extends OpenMetadataSecurityConnector implements OpenMetadataPlatformSecurity
{
    protected MessageFormatter messageFormatter = new MessageFormatter();



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

}
