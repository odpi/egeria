/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OMFAuditCode is used to define the message content for the Audit Log.
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
public enum OMFAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-METADATA-0001 - The {0} connector is initiating the monitoring of file directory {1}
     */
    DIRECTORY_MONITORING_STARTING("OPEN-METADATA-0001",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} connector is initiating the monitoring of file directory {1}",
                                  "The connector is calling the Java file system monitoring library. " +
                                          "This will start a background thread to monitor the file directory.  Any changes to the files in the " +
                                          "directory will be reported to this connector.",
                                  "No action is required unless there are errors that follow indicating that the monitoring of the directory failed to start."),


    /**
     * OPEN-METADATA-0002 - The {0} connector is initiating the monitoring of file directory {1} and any subdirectories
     */
    DIRECTORY_TREE_MONITORING_STARTING("OPEN-METADATA-0002",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} connector is initiating the monitoring of file directory {1} and any subdirectories",
                                  "The connector is calling the Java file system monitoring library. " +
                                          "This will start a background thread to monitor the file directory and any subdirectories.  Any changes to the files in these " +
                                          "directories will be reported to this connector.",
                                  "No action is required unless there are errors that follow indicating that the monitoring of any of the directories failed to start."),


    /**
     * OPEN-METADATA-0003 - The {0} connector is stopping the monitoring of directory {1}
     */
    DIRECTORY_MONITORING_STOPPING("OPEN-METADATA-0003",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} connector is stopping the monitoring of directory {1}",
                                  "The connector is unregistering the file system watch services for the directory. " +
                                          "This will terminate the background thread set up to monitor the directory.  Any further changes to the files in this directory will no longer be notified to this connector.",
                                  "No action is required unless there are errors that follow indicating that the monitoring of the files failed to stop."),

    /**
     * OPEN-METADATA-0004 - An unexpected {0} exception was returned to the {1} connector by the Java file monitoring service.  The error message was {2}
     */
    UNEXPECTED_FILE_MONITORING_EXCEPTION("OPEN-METADATA-0004",
                                         AuditLogRecordSeverityLevel.ERROR,
                                         "An unexpected {0} exception was returned to the {1} connector by the by the Java file monitoring service.  The error message was {2}",
                                         "The exception is logged and the connector continues to synchronize metadata " +
                                             "through the refresh process.",
                                         "Use the message in the unexpected exception to determine the root cause of the error. Once this is " +
                                             "resolved, follow the instructions in the messages produced by the server to restart the connector. " +
                                             "Then validate that the monitoring starts successfully."),

    /**
     * OPEN-METADATA-0005 - The {0} connector has stopped all of its file system monitoring and is shutting down
     */
    FILE_SYSTEM_MONITORING_STOPPING("OPEN-METADATA-0005",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "The {0} connector has stopped all of its file system monitoring and is shutting down",
                                    "The file system monitor connector is disconnecting.",
                                    "No action is required unless there are errors that follow indicating that there were problems shutting down the connector."),


    /**
     * OPEN-METADATA-0006 - The {0} connector has been disconnected - either due to its own actions or a cancel request
     */
    DISCONNECT_DETECTED("OPEN-METADATA-0006",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} connector has been disconnected - either due to its own actions or a cancel request",
                        "Egeria will attempt to stop the work of the connector",
                        "Monitor the shutdown of the connector."),

    /**
     * OPEN-METADATA-0007 - The {0} connector can not retrieve the correlation information for {1} open metadata element {2} linked via metadata collection {3} to external element {4}
     */
    MISSING_CORRELATION("OPEN-METADATA-0007",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} connector can not retrieve the correlation information for {1} open metadata element {2} linked via metadata collection {3} to external element {4}",
                        "The correlation information that should be associated with the open metadata element is missing and the connector is not able to confidently synchronize it with the element from the external system.",
                        "Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to add the correlation information to the open metadata entity to allow the synchronization to continue."),

    /**
     * OPEN-METADATA-0008 - The valid metadata value {0} for property {1} has been created/updated in metadata element {2}
     */
    VALID_METADATA_UPDATE("OPEN-METADATA-0008",
                        AuditLogRecordSeverityLevel.INFO,
                        "The valid metadata value {0} for property {1} has been created/updated in metadata element {2}",
                        "The new or updated element is stored in the metadata repository.",
                        "This is an informational message to provide an audit log of changes to the open metadata valid values. If this is of interest then these messages can be captured and retained."),

    /**
     * OPEN-METADATA-0009 - The valid metadata value {0} for property {1} stored in metadata element {2} has been deleted
     */
    VALID_METADATA_DELETE("OPEN-METADATA-0009",
                          AuditLogRecordSeverityLevel.INFO,
                          "The valid metadata value {0} for property {1} stored in metadata element {2} has been deleted",
                          "The metadata element has been deleted from the repository.  There are no changed to other metadata elements using the obsolete value.",
                          "Use an API like Classification Explorer OMVS to find any metadata elements that is using this obsolete value and update them to use one of the remaining valid values."),

    /**
     * OPEN-METADATA-0010 - The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}
     */
    IGNORED_EGERIA_ELEMENT("OPEN-METADATA-0010",
                           AuditLogRecordSeverityLevel.ACTION,
                           "The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}",
                           "The element is ignored.",
                           "Determine why this element is in the metadata collection and determine if it should be synchronized with the catalog target.  If it should, then set up the permitted synchronization direction to allow it."),

    /**
     * OPEN-METADATA-0011 - The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}
     */
    UNKNOWN_ACTION ("OPEN-METADATA-0011",
                    AuditLogRecordSeverityLevel.ACTION,
                    "The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}",
                    "The connector stops processing.",
                    "Using information from the element, the set up of the connector, and the connector's logic to determine why this 'should not occur' case has happened."),

    /**
     * OPEN-METADATA-0012 - The {0} connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}
     */
    DISCONNECT_EXCEPTION("OPEN-METADATA-0012",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}",
                         "The connector is is unable to disconnect a connector to a catalog target.  Although it continues to run, it may have leaked a resource in the remote target.",
                         "Use the details from the error message to determine the cause of the error.  Check the remote target for errors and correct as needed."),

    /**
     * OPEN-METADATA-0013 - The {0} connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("OPEN-METADATA-0013",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector records the error anf tries to continue; subsequent errors may occur as a result of this initial failure",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * OPEN-METADATA-0014 - The {0} connector has stopped its monitoring and is shutting down
     */
    CONNECTOR_STOPPING("OPEN-METADATA-0014",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} connector has stopped its monitoring and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    /**
     * OPEN-METADATA-0015 - The valid metadata value {0} for property {1} is not found
     */
    VALID_METADATA_MISSING("OPEN-METADATA-0015",
                          AuditLogRecordSeverityLevel.ERROR,
                          "The valid metadata value {0} for property {1} is not found",
                          "The metadata element for this valid metadata value is not stored in the repository.",
                          "Check the parameter of the call to make sure there name and value have been properly defined."),

    /**
     * OPEN-METADATA-0016 - Thread interrupt for connector {0} with message {1}
     */
    THREAD_INTERRUPT("OPEN-METADATA-0016",
                           AuditLogRecordSeverityLevel.INFO,
                           "Thread interrupt for connector {0} with message {1}",
                           "The thread performing processing for the named connector has been interrupted.  This may indicate that shutdown is in progress.",
                           "Thread interrupts are part of normal processing.  Look for other errors."),

    /**
     * OPEN-METADATA-0017 - File change notification for connector {0} of kind {1} for path {2}
     */
    FILE_CHANGE_EVENT("OPEN-METADATA-0017",
                     AuditLogRecordSeverityLevel.INFO,
                     "File change notification for connector {0} of kind {1} for path {2}",
                     "A file change event has been received. If appropriate, it wil lbe passed to the connector.",
                     "Check that the connector reacts correctly to this event."),

    /**
     * OPEN-METADATA-0025 - The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1}.  The message was: {2}
     */
    UNEXPECTED_CONVERTER_EXCEPTION("OPEN-METADATA-0025",
                                   AuditLogRecordSeverityLevel.EXCEPTION,
                                   "The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3}",
                                   "The request returns an exception detailing the cause of the error.",
                                   "Review the stack trace to identify where the error occurred and work to resolve the cause."),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for OMFAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OMFAuditCode(String                      messageId,
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
        return "OMFAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
