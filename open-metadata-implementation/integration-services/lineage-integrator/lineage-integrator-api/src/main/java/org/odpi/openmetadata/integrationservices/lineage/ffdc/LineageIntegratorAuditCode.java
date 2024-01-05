/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The LineageIntegratorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum LineageIntegratorAuditCode implements AuditLogMessageSet
{
    /**
     * OMIS-LINEAGE-INTEGRATOR-0001 - The lineage integrator context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OMIS-LINEAGE-INTEGRATOR-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The lineage integrator context manager is being initialized for calls to server {0} on platform {1}",
                         "The Lineage Integrator OMIS is initializing its context manager.",
                         "Verify that the start up sequence goes on to initialize the context for each connector configured for this service."),

    /**
     * OMIS-LINEAGE-INTEGRATOR-0002 - Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}
     */
    CONNECTOR_CONTEXT_INITIALIZING("OMIS-LINEAGE-INTEGRATOR-0002",
                                   AuditLogRecordSeverityLevel.STARTUP,
                                   "Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}",
                                   "A new context is created for an integration connector.  This acts as a client to the open metadata repositories " +
                                           "enabling the integration connector to synchronize open metadata with the third party technology's metadata",
                                   "Verify that this connector is being started with the correct configuration."),

    /**
     * OMIS-LINEAGE-INTEGRATOR-0003 - The context for connector {0} has its permitted synchronization set to {1}
     */
    PERMITTED_SYNCHRONIZATION("OMIS-LINEAGE-INTEGRATOR-0003",
                              AuditLogRecordSeverityLevel.STARTUP,
                              "The context for connector {0} has its permitted synchronization set to {1}",
                              "The context is set up to ensure that the connector can only issue requests that support the permitted synchronization.  " +
                                      "If the connector issues requests that are not permitted it is returned UserNotAuthorizedExceptions.",
                              "Check that this permitted synchronized value is as expected.  If it is not," +
                                      "change the configuration for this connector and restart the integration daemon."),

    /**
     * OMIS-LINEAGE-INTEGRATOR-0004 - Integration connector {0} has a null context
     */
    NULL_CONTEXT("OMIS-LINEAGE-INTEGRATOR-0004",
                 AuditLogRecordSeverityLevel.ERROR,
                 "Integration connector {0} has a null context",
                 "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
                 "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                         "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),

    /**
     * OMIS-LINEAGE-INTEGRATOR-0005 - A {0} exception with message {1} occurred when parsing open lineage event: {2}
     */
    OPEN_LINEAGE_FORMAT_ERROR("OMIS-LINEAGE-INTEGRATOR-0005",
                              AuditLogRecordSeverityLevel.ERROR,
                              "A {0} exception with message {1} occurred when parsing open lineage event: {2}",
                              "The Lineage Integrator OMIS is unable to parse an incoming open lineage event into Egeria's OpenLineageRunEvent bean.  " +
                                      "This may be due to either (1) an invalid open lineage event, or (2) Egeria's OpenLineageRunEvent not supporting an advancement in the open lineage standard.  " +
                                      "The raw event is passed to the listening connectors with a null OpenLineageRunEvent bean.  The connector can use the open lineage standard server to process the event facet by facet.",
                              "Verify the format of the open lineage event.  If incorrect, seek the source of the event.  If correct, look to enhance Egeria's OpenLineageRunEvent."),

    /**
     * OMIS-LINEAGE-INTEGRATOR-0006 - A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event
     */
    OPEN_LINEAGE_PUBLISH_ERROR("OMIS-LINEAGE-INTEGRATOR-0006",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                               "A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event",
                               "The Lineage Integrator OMIS has caught the exception and will continue to pass the event to the remaining listening integration connectors.",
                               "Look at the resulting stack trace to understand what went wrong in the called integration connector."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for LineageIntegratorAuditCode expects to be passed one of the enumeration rows defined in
     * LineageIntegratorAuditCode above.   For example:
     * <br><br>
     *     LineageIntegratorAuditCode   auditCode = LineageIntegratorAuditCode.SERVER_SHUTDOWN;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    LineageIntegratorAuditCode(String                      messageId,
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
