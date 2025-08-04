/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OIFAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OIFAuditCode implements AuditLogMessageSet
{
    /**
     * OIF-CONNECTOR-0001 - The integration connector context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OIF-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The integration connector context manager is being initialized for calls to server {0} on platform {1}",
                         "The integration daemon is initializing its context manager.",
                         "Verify that the start up sequence goes on to initialize the context for each connector configured for this service."),

    /**
     * OIF-CONNECTOR-0002 - Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}
     */
    CONNECTOR_CONTEXT_INITIALIZING("OIF-CONNECTOR-0002",
                                   AuditLogRecordSeverityLevel.STARTUP,
                                   "Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3}",
                                   "A new context is created for an integration connector.  This acts as a client to the open metadata repositories " +
                                           "enabling the integration connector to synchronize open metadata with the third party technology's metadata",
                                   "Verify that this connector is being started with the correct configuration."),

    /**
     * OIF-CONNECTOR-0003 - The context for connector {0} has its permitted synchronization set to {1}
     */
    PERMITTED_SYNCHRONIZATION("OIF-CONNECTOR-0003",
                              AuditLogRecordSeverityLevel.STARTUP,
                              "The context for connector {0} has its permitted synchronization set to {1}",
                              "The context is set up to ensure that the connector can only issue requests that support the permitted synchronization.  " +
                                      "If the connector issues requests that are not permitted it is returned UserNotAuthorizedExceptions.",
                              "Check that this permitted synchronized value is as expected.  If it is not," +
                                      "change the configuration for this connector and restart the integration daemon."),

    /**
     * OIF-CONNECTOR-0004 - Integration connector {0} has a null context
     */
    NULL_CONTEXT("OIF-CONNECTOR-0004",
                 AuditLogRecordSeverityLevel.ERROR,
                 "Integration connector {0} has a null context",
                 "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
                 "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                         "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),

    /**
     * OIF-CONNECTOR-0005 - A {0} exception with message {1} occurred when parsing open lineage event: {2}
     */
    OPEN_LINEAGE_FORMAT_ERROR("OIF-CONNECTOR-0005",
                              AuditLogRecordSeverityLevel.ERROR,
                              "A {0} exception with message {1} occurred when parsing open lineage event: {2}",
                              "The integration daemon is unable to parse an incoming open lineage event into Egeria's OpenLineageRunEvent bean.  " +
                                      "This may be due to either (1) an invalid open lineage event, or (2) Egeria's OpenLineageRunEvent not supporting an advancement in the open lineage standard.  " +
                                      "The raw event is passed to the listening connectors with a null OpenLineageRunEvent bean.  The connector can use the open lineage standard server to process the event facet by facet.",
                              "Verify the format of the open lineage event.  If incorrect, seek the source of the event.  If correct, look to enhance Egeria's OpenLineageRunEvent."),

    /**
     * OIF-CONNECTOR-0006 - A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event
     */
    OPEN_LINEAGE_PUBLISH_ERROR("OIF-CONNECTOR-0006",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                               "A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event",
                               "The integration daemon has caught the exception and will continue to pass the event to the remaining listening integration connectors.",
                               "Look at the resulting stack trace to understand what went wrong in the called integration connector."),



    /**
     * OIF-CONNECTOR-0007 - No action targets are defined for the {0} integration connector
     */
    NO_CATALOG_TARGETS("OIF-CONNECTOR-0007",
                                 AuditLogRecordSeverityLevel.INFO,
                                 "No catalog targets are defined for the {0} integration connector",
                                 "The integration connector waits for the next refresh.",
                                 "Add one or more action targets to the integration connector to provide it with work to do."),

    /**
     * OIF-CONNECTOR-0008 - The {0} integration connector is refreshing action target {1}
     */
    REFRESHING_CATALOG_TARGET("OIF-CONNECTOR-0008",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is refreshing action target {1}",
                       "The integration connector performs the requested metadata synchronization.",
                       "Check for reported errors.   Otherwise, the connector is working as configured."),


    /**
     * OIF-CONNECTOR-0009 - The {0} integration connector has refreshed {1} action target(s)
     */
    REFRESHED_CATALOG_TARGETS("OIF-CONNECTOR-0009",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector has refreshed {1} action target(s)",
                              "The integration connector has completed refresh processing of the catalog targets.",
                              "Check that the correct action targets have been processes, and adjust them if necessary before the next refresh."),

    /**
     * OIF-CONNECTOR-0010 - The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}
     */
    IGNORED_EGERIA_ELEMENT("OIF-CONNECTOR-0010",
                           AuditLogRecordSeverityLevel.ACTION,
                           "The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}",
                           "The element is ignored.",
                           "Determine why this element is in the metadata collection and determine if it should be synchronized with the catalog target.  If it should, then set up the permitted synchronization direction to allow it."),

    /**
     * OIF-CONNECTOR-0011 - The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}
     */
    UNKNOWN_ACTION ("OIF-CONNECTOR-0011",
                    AuditLogRecordSeverityLevel.ACTION,
                    "The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}",
                    "The connector stops processing.",
                    "Using information from the element, the set up of the connector, and the connector's logic to determine why this 'should not occur' case has happened."),

    /**
     * OIF-CONNECTOR-0012 - The {0} integration connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}
     */
    DISCONNECT_EXCEPTION("OIF-CONNECTOR-0012",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}",
                         "The connector is is unable to disconnect a connector to a catalog target.  Although it continues to run, it may have leaked a resource in the remote target.",
                         "Use the details from the error message to determine the cause of the error.  Check the remote target for errors and correct as needed."),

    /**
     * OIF-CONNECTOR-0013 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("OIF-CONNECTOR-0013",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector records the error anf tries to continue; subsequent errors may occur as a result of this initial failure",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * OIF-CONNECTOR-0014 - The {0} integration connector has stopped its monitoring and is shutting down
     */
    CONNECTOR_STOPPING("OIF-CONNECTOR-0014",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    /**
     * OIF-CONNECTOR-0015 - An unexpected {0} exception was returned to the {1} integration connector while retrieving the action targets.  The error message was {2}
     */
    GET_CATALOG_TARGET_EXCEPTION("OIF-CONNECTOR-0015",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "An unexpected {0} exception was returned to the {1} integration connector while retrieving the catalog targets.  The error message was {2}",
                                 "The exception is logged and the integration connector waits for the next refresh.",
                                 "Use the message in the unexpected exception to determine the root cause of the error. Once this is " +
                                         "resolved, follow the instructions to prepare the integration connector for the next refresh."),


    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for OIFAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OIFAuditCode(String                      messageId,
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
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
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
        return "OIFAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
