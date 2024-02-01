/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.database.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The DatabaseIntegratorAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum DatabaseIntegratorAuditCode implements AuditLogMessageSet
{
    /**
     * OMIS-DATABASE-INTEGRATOR-0001 - The database integrator context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OMIS-DATABASE-INTEGRATOR-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The database integrator context manager is being initialized for calls to server {0} on platform {1}",
                         "The Database Integrator OMIS is initializing its context manager.",
                         "Verify that the start up sequence goes on to initialize the context for each connector configured for this service."),

    /**
     * OMIS-DATABASE-INTEGRATOR-0002 -  Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}
     */
    CONNECTOR_CONTEXT_INITIALIZING("OMIS-DATABASE-INTEGRATOR-0002",
                                   AuditLogRecordSeverityLevel.STARTUP,
                                   "Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}",
                                   "A new context is created for an integration connector.  This acts as a client to the open metadata repositories " +
                                           "enabling the integration connector to synchronize open metadata with the third party technology's metadata",
                                   "Verify that this connector is being started with the correct configuration."),


    /**
     * OMIS-DATABASE-INTEGRATOR-0003 - Integration connector {0} has a null context
     */
    NULL_CONTEXT("OMIS-DATABASE-INTEGRATOR-0003",
                 AuditLogRecordSeverityLevel.ERROR,
                 "Integration connector {0} has a null context",
                 "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
                 "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                         "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for DatabaseIntegratorAuditCode expects to be passed one of the enumeration rows defined in
     * DatabaseIntegratorAuditCode above.   For example:
     * <br><br>
     *     DatabaseIntegratorAuditCode   auditCode = DatabaseIntegratorAuditCode.SERVER_SHUTDOWN;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DatabaseIntegratorAuditCode(String                      messageId,
                                AuditLogRecordSeverityLevel severity,
                                String                      message,
                                String                      systemAction,
                                String                      userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return new AuditLogMessageDefinition(logMessageId,
                                             severity,
                                             logMessage,
                                             systemAction,
                                             userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params)
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
                                                                                    systemAction,
                                                                                    userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
