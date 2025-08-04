/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The GovernanceActionAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum GovernanceActionAuditCode implements AuditLogMessageSet
{
    /**
     * OMES-GOVERNANCE-ACTION-0001 - The Governance Action engine services are initializing in server {0}
     */
    ENGINE_SERVICE_INITIALIZING("OMES-GOVERNANCE-ACTION-0001",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "The Governance Action engine services are initializing in server {0}; they will call Governance Engine OMAS on server {1} at {2}",
                                "A new OMAG server has been started that is configured to run the Governance Action OMES.  " +
                                 "Within this engine service are one or more governance action engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "governance action engines is retrieved from the metadata server and the governance action engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured governance action engines."),

    /**
     * OMES-GOVERNANCE-ACTION-0002 - The Governance Action OMES is unable to initialize a new instance of itself in server {0}; error message is {1}
     */
    SERVICE_INSTANCE_FAILURE("OMES-GOVERNANCE-ACTION-0002",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Governance Action OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its governance action services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMES-GOVERNANCE-ACTION-0014 - The Governance Action OMES in server {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("OMES-GOVERNANCE-ACTION-0014",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The Governance Action OMES in server {0} is shutting down",
                    "The local administrator has requested shut down of this engine service.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * OMES-GOVERNANCE-ACTION-0016 - The Governance Action OMES in server {0} has completed shutdown
     */
   SERVER_SHUTDOWN("OMES-GOVERNANCE-ACTION-0016",
                   AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Governance Action OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured governance action engines shut down successfully."),

    /**
     * OMES-GOVERNANCE-ACTION-0017 - The {0} governance action service {1} is starting with request type {2} in governance action engine {3} (guid={4})
     */
    GOVERNANCE_ACTION_SERVICE_STARTING("OMES-GOVERNANCE-ACTION-0017",
                                       AuditLogRecordSeverityLevel.STARTUP,
                    "The {0} governance action service {1} is starting with request type {2} in governance action engine {3} (guid={4})",
                    "A new governance action service is starting to process a new request.",
                    "Verify that the governance action service is correctly configured, this action is intended. Verify that this " +
                            "service runs successfully."),

    /**
     * OMES-GOVERNANCE-ACTION-0018 - The {0} governance action service {1} threw a {2} exception in governance action engine {3} (guid={4}). The error message was {5}
     */
    GOVERNANCE_ACTION_SERVICE_FAILED("OMES-GOVERNANCE-ACTION-0018",
                                     AuditLogRecordSeverityLevel.EXCEPTION,
                    "The {0} governance action service {1} threw a {2} exception in governance action engine {3} (guid={4}). The error message was {5}",
                    "A governance action service produced an un expected exception.",
                    "Review the exception to determine the cause of the error.  It may be a coding error or configuration error."),

    /**
     * OMES-GOVERNANCE-ACTION-0019 - The {0} governance action service {1} for request type {2} has completed with status {3} in {4} milliseconds
     */
    GOVERNANCE_ACTION_SERVICE_COMPLETE("OMES-GOVERNANCE-ACTION-0019",
                                       AuditLogRecordSeverityLevel.SHUTDOWN,
                               "The {0} governance action service {1} for request type {2} has completed with status {3} in {4} milliseconds",
                               "A governance action service has returned from the start() method and set up the completion status prior to returning.  " +
                                       "The Governance Action OMES will call disconnect() on the governance action service since it is complete.  " +
                                       "The Governance Action entity in the metadata store will be updated to reflect the completion status",
                               "It is possible to query the result of the governance action through the Governance Engine OMAS REST API."),

    /**
     * OMES-GOVERNANCE-ACTION-0020 - The {0} governance action service {1} for request type {2} is continuing to run in a background thread
     */
    GOVERNANCE_ACTION_SERVICE_RETURNED("OMES-GOVERNANCE-ACTION-0020",
                                       AuditLogRecordSeverityLevel.INFO,
                                       "The {0} governance action service {1} for request type {2} is continuing to run in a background thread",
                                       "A governance action service has returned from the start() method without setting up the completion status prior to returning.",
                                       "Validate that this governance action service should still be running.  Typically you would expect a GovernanceActionService using Watchdog events to" +
                                               "still be running at this stage because it will have registered a listener. The other types of governance action services should have completed during " +
                                               "start() unless they are managing their own thread(s)."),

    /**
     * OMES-GOVERNANCE-ACTION-0027 - Governance Action OMES in server {0} is unable to start any governance action engines
     */
    NO_GOVERNANCE_ACTION_ENGINES_STARTED("OMES-GOVERNANCE-ACTION-0027",
                                         AuditLogRecordSeverityLevel.ERROR,
                         "Governance Action OMES in server {0} is unable to start any governance action engines",
                         "The engine service is not able to run any governance action requests.  It fails to start.",
                         "Add the configuration for at least one governance action engine to this engine service."),

    /**
     * OMES-GOVERNANCE-ACTION-0028 - Governance Action engine {0} is unable to update the completion status for governance action service {1}.  
     * The exception was {2} with error message {3}
     */
    EXC_ON_ERROR_STATUS_UPDATE("OMES-GOVERNANCE-ACTION-0028",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                                 "Governance Action engine {0} is unable to update the completion status for governance action service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a governance action request. The governance action report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the governance action request."),

    /**
     * The governance action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3}
     */
    INVALID_GOVERNANCE_ACTION_SERVICE( "OMES-GOVERNANCE-ACTION-0029",
                                       AuditLogRecordSeverityLevel.EXCEPTION,
                               "The governance action service {0} linked to request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                              "The governance action request is not run and an error is returned to the caller.",
                              "This may be an error in the governance action service's logic or the governance action service may not be properly deployed or " +
                                      "there is a configuration error related to the governance action engine.  " +
                                      "The configuration that defines the governance action request type in the governance action engine and links " +
                                      "it to the governance action service is maintained in the metadata server by the Governance Action " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "governance action service's implementation has been deployed so the Governance Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the governance action service in which case, " +
                                      "raise an issue with the author of the governance action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the governance action request."),

    /**
     * OMES-GOVERNANCE-ACTION-0030 - The governance action service {0} linked to request type {1} can not be started because the Governance 
     * Action OMES does not support the {2} type of governance action service.
     */
    UNKNOWN_GOVERNANCE_ACTION_SERVICE( "OMES-GOVERNANCE-ACTION-0030",
                                       AuditLogRecordSeverityLevel.ERROR,
                                       "The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of " +
                                               "governance action service.",
                                       "The governance action request is not run and an error is returned to the caller.  Subsequent requests to this governance action service will also fail.",
                                       "This version of the Governance Engine OMES does not support this type of governance action service.  " +
                                               "It is likely that you need a future version of Egeria or another platform that supports this type of governance action service."),

    /**
     * OMES-GOVERNANCE-ACTION-0031 - The governance action service {0} linked to request type {1} can not be started because it is not a
     * governance action service.  Its class is {2} rather than a subclass of {3}
     */
    NOT_GOVERNANCE_ACTION_SERVICE( "OMES-GOVERNANCE-ACTION-0031",
                                   AuditLogRecordSeverityLevel.ERROR,
                                       "The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  " +
                                               "Its class is {2} rather than a subclass of {3}",
                                       "The governance action request is not run and an error is returned to the caller.  Subsequent calls to this service will fail in the same way",
                                       "Correct the configuration for the Governance Action OMES to only include valid governance action service implementations."),

    /**
     * OMES-GOVERNANCE-ACTION-0032 - {0} governance service ({1}) with request type {2} has initialized in governance engine {3}
     */
    GOVERNANCE_ACTION_INITIALIZED("OMES-GOVERNANCE-ACTION-0032",
                                  AuditLogRecordSeverityLevel.STARTUP,
                                  "{0} governance service ({1}) with request type {2} has initialized in governance engine {3}",
                                  "The governance engine is starting a governance action request.",
                                  "Validate that the governance action ran to successful completion."),

    /**
     * OMES-GOVERNANCE-ACTION-0033 - The Governance Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}
     */
    UNEXPECTED_EXCEPTION("OMES-GOVERNANCE-ACTION-0033",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Governance Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}",
                         "The service is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),


    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for GovernanceActionAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceActionAuditCode above.   For example:
     *     GovernanceActionAuditCode   auditCode = GovernanceActionAuditCode.SERVER_SHUTDOWN;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GovernanceActionAuditCode(String                      messageId,
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GovernanceActionAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", messageDefinition=" + getMessageDefinition() +
                       '}';
    }
}
