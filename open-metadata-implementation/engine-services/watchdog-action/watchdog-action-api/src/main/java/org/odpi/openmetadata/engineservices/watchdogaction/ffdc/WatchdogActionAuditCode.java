/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;



/**
 * The WatchdogActionAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum WatchdogActionAuditCode implements AuditLogMessageSet
{
    /**
     * OMES-WATCHDOG-ACTION-0001 - The Watchdog Action engine services are initializing in server {0}
     */
    ENGINE_SERVICE_INITIALIZING("OMES-WATCHDOG-ACTION-0001",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "The Watchdog Action engine services are initializing in server {0}; they will call services on server {1} at {2}",
                                "A new OMAG server has been started that is configured to run the Watchdog Action OMES.  " +
                                 "Within this engine service are one or more watchdog action engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "watchdog action engines is retrieved from the metadata server and the watchdog action engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured watchdog action engines."),

    /**
     * OMES-WATCHDOG-ACTION-0012 - The Watchdog Action OMES is unable to initialize a new instance of itself in server {0}; error message is {1}
     */
    SERVICE_INSTANCE_FAILURE("OMES-WATCHDOG-ACTION-0012",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Watchdog Action OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its watchdog action services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMES-WATCHDOG-ACTION-0014 - The Watchdog Action OMES in server {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("OMES-WATCHDOG-ACTION-0014",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Watchdog Action OMES in server {0} is shutting down",
                         "The local administrator has requested shut down of this engine service.",
                         "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * OMES-WATCHDOG-ACTION-0016 - The Watchdog Action OMES in server {0} has completed shutdown
     */
   SERVER_SHUTDOWN("OMES-WATCHDOG-ACTION-0016",
                   AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Watchdog Action OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured watchdog action engines shut down successfully."),

    /**
     * OMES-WATCHDOG-ACTION-0017 - The watchdog action service {0} is starting to analyze asset {1} with request type {2}
     * in watchdog action engine {3} (guid={4}); the results will be result in notifications/actions to subscribers of the {1} notification type
     */
    WATCHDOG_ACTION_SERVICE_STARTING("OMES-WATCHDOG-ACTION-0017",
                                   AuditLogRecordSeverityLevel.STARTUP,
                                   "The watchdog action service {0} is starting to monitor notification type {1} with request type {2} in watchdog action engine {3} (guid={4});" +
                                       " this will result in notifications/actions to subscribers of the {1} notification type",
                                   "A new monitoring request is being processed.",
                                   "Verify that the watchdog action service ran to completion."),

    /**
     * OMES-WATCHDOG-ACTION-0018 - The watchdog action service {0} threw a {1} exception during the notification/actioning of subscriber
     * {2} for asset {3} during  request type {4} in watchdog action engine {5} (guid={6}). The error message was {7}
     */
    WATCHDOG_ACTION_SERVICE_FAILED("OMES-WATCHDOG-ACTION-0018",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "The watchdog action service {0} threw a {1} exception during the during the notification/actioning for notification {2} " +
                                     "during request type {3} in watchdog action engine {4} (guid={5}). The error message was {6}",
                                 "A watchdog action service failed to complete the notification/actioning of a subscriber.",
                                 "Review the exception to determine the cause of the error."),

    /**
     * OMES-WATCHDOG-ACTION-0019 - The watchdog action service {0} has completed monitoring for notification type {1} with request type {2} in {3}
     * milliseconds; the results are notified to subscribers {4}
     */
    WATCHDOG_ACTION_SERVICE_COMPLETE("OMES-WATCHDOG-ACTION-0019",
                                   AuditLogRecordSeverityLevel.SHUTDOWN,
                                   "The watchdog action service {0} has completed monitoring for notification type {1} with request type {2} in {3} milliseconds",
                                   "A monitoring request has completed.",
                                   "It is possible to query the result of the monitoring request through Egeria's Open Metadata REST APIs."),

    /**
     * OMES-WATCHDOG-ACTION-0020 - The {0} governance action service {1} for request type {2} is continuing to run in a background thread
     */
    WATCHDOG_ACTION_SERVICE_RETURNED("OMES-WATCHDOG-ACTION-0020",
                                       AuditLogRecordSeverityLevel.INFO,
                                       "The {0} watchdog action service {1} for request type {2} is continuing to run in a background thread",
                                       "A watchdog action service has returned from the start() method without setting up the completion status prior to returning.",
                                       "Validate that this watchdog action service should still be running.  Typically you would expect a Watchdog action service to" +
                                               "still be running at this stage because it will have registered a listener."),


    /**
     * OMES-WATCHDOG-ACTION-0021 - Watchdog Action OMES in server {0} is unable to start any watchdog action engines
     */
    NO_WATCHDOG_ACTION_ENGINES_STARTED("OMES-WATCHDOG-ACTION-0021",
                                     AuditLogRecordSeverityLevel.ERROR,
                                     "Watchdog Action OMES in server {0} is unable to start any watchdog action engines",
                                     "The engine service is not able to run any monitoring requests.  It fails to start.",
                                     "Add the configuration for at least one watchdog action engine to this engine service."),

    /**
     * OMES-WATCHDOG-ACTION-0022 - Watchdog action engine {0} is unable to update the status for watchdog action service {1}.
     * The exception was {2} with error message {3}
     */
    EXC_ON_ERROR_STATUS_UPDATE("OMES-WATCHDOG-ACTION-0022",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                                 "Watchdog action engine {0} is unable to update the status for watchdog action service {1}.  The exception was {2} with error message {3}",
                                 "The server is not able to record the failed result for a monitoring watchdog request. The subscribers are not notified.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the monitoring request."),

    /**
     * OMES-WATCHDOG-ACTION-0023 - Method {0} can not execute in the watchdog action engine {1} hosted by Watchdog Action OMES in server {2} because the associated " +
     *                                 "watchdog action service properties are null
     */
    NULL_WATCHDOG_SERVICE( "OMES-WATCHDOG-ACTION-0023",
                        AuditLogRecordSeverityLevel.ERROR,
                        "Method {0} can not execute in the watchdog action engine {1} hosted by Watchdog Action OMES in server {2} because the associated " +
                                "watchdog action service properties are null",
                        "The monitoring request is not run and an error is returned to the caller.",
                        "This may be an error in the watchdog action engine's logic or the Watchdog Action Framework (OWF) may have returned " +
                                "invalid configuration.  Raise an issue to get help to fix it"),


    /**
     * OMES-WATCHDOG-ACTION-0029 - The watchdog action service {0} linked to request type {1} can not be started.
     * The {2} exception was returned with message {3}
     */
    INVALID_WATCHDOG_ACTION_SERVICE("OMES-WATCHDOG-ACTION-0029",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                                  "The watchdog action service {0} linked to request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                                  "The monitoring watchdog request is not run and an error is returned to the caller.",
                                  "This may be an error in the watchdog action service's logic or the watchdog action service may not be properly deployed or " +
                                      "there is a configuration error related to the watchdog action engine.  " +
                                      "The configuration that defines the request type in the watchdog action engine and links " +
                                      "it to the watchdog action service is maintained in the metadata server by the Governance " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "watchdog action service's implementation has been deployed so the Watchdog Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the watchdog action service in which case, " +
                                      "raise an issue with the author of the watchdog action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the monitoring request."),

    /**
     * OMES-WATCHDOG-ACTION-0030 - The watchdog action service {0} linked to request type {1} is processing asset {2} and ignoring the following asset action targets: {3}
     */
    IGNORING_NOTIFICATION_TYPES("OMES-WATCHDOG-ACTION-0030",
                                AuditLogRecordSeverityLevel.INFO,
                                "The watchdog action service {0} linked to request type {1} for engine action {2} is processing asset {3} and ignoring the following asset action targets: {4}",
                                "There are multiple assets in the action targets.  The watchdog action service can only process one of them.  The other assets are ignored.",
                                "Create a new engine action for each of the ignored assets so that they each run in their own watchdog action service."),

    /**
     * OMES-GOVERNANCE-ACTION-0033 - The Governance Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}
     */
    UNEXPECTED_EXCEPTION("OMES-WATCHDOG-ACTION-0033",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Watchdog Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}",
                         "The service is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),


    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for WatchdogActionAuditCode expects to be passed one of the enumeration rows defined in
     * WatchdogActionAuditCode above.   For example:
     *     WatchdogActionAuditCode   auditCode = WatchdogActionAuditCode.SERVER_SHUTDOWN;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    WatchdogActionAuditCode(String                      messageId,
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
