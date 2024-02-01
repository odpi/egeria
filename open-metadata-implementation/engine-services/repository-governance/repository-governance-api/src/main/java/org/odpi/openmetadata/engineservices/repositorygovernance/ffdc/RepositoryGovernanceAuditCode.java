/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;



/**
 * The RepositoryGovernanceAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum RepositoryGovernanceAuditCode implements AuditLogMessageSet
{
    /**
     * OMES-REPOSITORY-GOVERNANCE-0001 - The Repository Governance engine services are initializing in server {0}
     */
    ENGINE_SERVICE_INITIALIZING("OMES-REPOSITORY-GOVERNANCE-0001",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "The Repository Governance engine services are initializing in server {0}; they will call the Open Metadata Repository Services (OMRS) on server {1} at {2}",
                                "A new OMAG server has been started that is configured to run the Repository Governance OMES.  " +
                                 "Within this engine service are one or more repository governance engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "repository governance engines is retrieved from the metadata server and the repository governance engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured repository governance engines."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0012 - The Repository Governance OMES is unable to initialize a new instance of itself in server {0}; error message is {1}
     */
    SERVICE_INSTANCE_FAILURE("OMES-REPOSITORY-GOVERNANCE-0012",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Repository Governance OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its repository governance services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0014 - The Repository Governance OMES in server {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("OMES-REPOSITORY-GOVERNANCE-0014",
                    AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The Repository Governance OMES in server {0} is shutting down",
                    "The local administrator has requested shut down of this engine service.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0015 - The Repository Governance OMES in server {0} has completed shutdown
     */
    SERVER_SHUTDOWN("OMES-REPOSITORY-GOVERNANCE-0015",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Repository Governance OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured repository governance engines shut down successfully."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0016 - The repository governance service {0} is starting with repository governance request type {1} in repository governance engine {2} (guid={3})
     */
    REPOSITORY_GOVERNANCE_SERVICE_STARTING("OMES-REPOSITORY-GOVERNANCE-0016",
                                           AuditLogRecordSeverityLevel.STARTUP,
                                           "The repository governance service {0} is starting with repository governance request type {1} in repository governance engine {2} (guid={3})",
                                           "A new repository governance request is being processed.",
                                           "Verify that the repository governance service ran to completion."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0017 - The repository governance service {0} for request type {1} is continuing to run in a background thread
     */
   REPOSITORY_GOVERNANCE_SERVICE_RETURNED("OMES-REPOSITORY-GOVERNANCE-0017",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "The repository governance service {0} for request type {1} is continuing to run in a background thread",
                                          "An repository governance service has returned from the start() method and without setting up the completion status prior to returning.  ",
                                          "Validate that this governance action service should still be running.  Typically you would expect an repository governance service to" +
                                               "still be running at this stage because it will have registered a listener."),


    /**
     * OMES-REPOSITORY-GOVERNANCE-0018 - The repository governance service {0} threw a {1} exception during repository governance request 
     * type {2} in repository governance engine {3} (guid={4}). The error message was {5}
     */
    REPOSITORY_GOVERNANCE_SERVICE_FAILED("OMES-REPOSITORY-GOVERNANCE-0018",
                                         AuditLogRecordSeverityLevel.EXCEPTION,
                                         "The repository governance service {0} threw a {1} exception during repository governance request type {2} in repository governance engine {3} (guid={4}). The error message was {5}",
                                         "A repository governance service failed to complete the maintenance of an repository governance.",
                                         "Review the exception to determine the cause of the error."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0019 - The repository governance service {0} has completed repository governance request type {1} in {2} milliseconds
     */
    REPOSITORY_GOVERNANCE_SERVICE_COMPLETE("OMES-REPOSITORY-GOVERNANCE-0019",
                                           AuditLogRecordSeverityLevel.SHUTDOWN,
                                           "The repository governance service {0} has completed repository governance request type {1} in {2} milliseconds",
                                           "A repository governance request has completed.",
                                           "It is possible to query the result of the repository governance request through the Governance Engine OMAS's REST API."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0020 - Repository Governance OMES in server {0} is unable to start any repository governance engines
     */
    NO_REPOSITORY_GOVERNANCE_ENGINES_STARTED("OMES-REPOSITORY-GOVERNANCE-0020",
                                             AuditLogRecordSeverityLevel.ERROR,
                                             "Repository Governance OMES in server {0} is unable to start any repository governance engines",
                                             "The engine service is not able to run any repository governance requests.  It fails to start.",
                                             "Add the configuration for at least one repository governance engine to this engine service."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0021 - RepositoryGovernance engine {0} is unable to update the status for repository governance service {1}.  
     * The exception was {2} with error message {3}
     */
    EXC_ON_ERROR_STATUS_UPDATE("OMES-REPOSITORY-GOVERNANCE-0021",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "RepositoryGovernance engine {0} is unable to update the status for repository governance service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a repository governance request. The repository governance report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the repository governance request."),

    /**
     * OMES-REPOSITORY-GOVERNANCE-0029 - The repository governance service {0} linked to repository governance request type {1} can not be started.
     * The {2} exception was returned with message {3}
     */
    INVALID_REPOSITORY_GOVERNANCE_SERVICE("OMES-REPOSITORY-GOVERNANCE-0029",
                                          AuditLogRecordSeverityLevel.EXCEPTION,
                                          "The repository governance service {0} linked to repository governance request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                                          "The repository governance request is not run and an error is returned to the caller.",
                                          "This may be an error in the repository governance service's logic or the repository governance service may not be properly deployed or " +
                                      "there is a configuration error related to the repository governance engine.  " +
                                      "The configuration that defines the repository governance request type in the repository governance engine and links " +
                                      "it to the repository governance service is maintained in the metadata server by the RepositoryGovernance " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "repository governance service's implementation has been deployed so the Repository Governance OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the repository governance service in which case, " +
                                      "raise an issue with the author of the repository governance service to get it fixed.  Once the cause is resolved, " +
                                      "retry the repository governance request."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for RepositoryGovernanceAuditCode expects to be passed one of the enumeration rows defined in
     * RepositoryGovernanceAuditCode above.   For example:
     *     RepositoryGovernanceAuditCode   auditCode = RepositoryGovernanceAuditCode.SERVER_SHUTDOWN;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    RepositoryGovernanceAuditCode(String                     messageId,
                                  AuditLogRecordSeverityLevel severity,
                                  String                     message,
                                  String                     systemAction,
                                  String                     userAction)
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
