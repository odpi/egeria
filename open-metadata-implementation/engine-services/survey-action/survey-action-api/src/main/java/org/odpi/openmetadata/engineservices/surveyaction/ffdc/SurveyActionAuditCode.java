/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;



/**
 * The SurveyActionAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum SurveyActionAuditCode implements AuditLogMessageSet
{
    /**
     * OMES-SURVEY-ACTION-0001 - The Survey Action engine services are initializing in server {0}
     */
    ENGINE_SERVICE_INITIALIZING("OMES-SURVEY-ACTION-0001",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "The Survey Action engine services are initializing in server {0}",
                                "A new OMAG server has been started that is configured to run the Survey Action OMES.  " +
                                 "Within this engine service are one or more survey action engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "survey action engines is retrieved from the metadata server and the survey action engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured survey action engines."),

    /**
     * OMES-SURVEY-ACTION-0012 - The Survey Action OMES cannot initialize a new instance of itself in server {0}; error message is {1}
     */
    SERVICE_INSTANCE_FAILURE("OMES-SURVEY-ACTION-0012",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Survey Action OMES cannot initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its survey action services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMES-SURVEY-ACTION-0014 - The Survey Action OMES in server {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("OMES-SURVEY-ACTION-0014",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Survey Action OMES in server {0} is shutting down",
                         "The local administrator has requested shut down of this engine service.",
                         "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * OMES-SURVEY-ACTION-0016 - The Survey Action OMES in server {0} has completed shutdown
     */
   SERVER_SHUTDOWN("OMES-SURVEY-ACTION-0016",
                   AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Survey Action OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured survey action engines shut down successfully."),

    /**
     * OMES-SURVEY-ACTION-0017 - The survey action service {0} is starting to analyze asset {1} with request type {2}
     * in survey action engine {3} (guid={4}); the results will be stored in survey report {5}
     */
    SURVEY_ACTION_SERVICE_STARTING("OMES-SURVEY-ACTION-0017",
                                   AuditLogRecordSeverityLevel.STARTUP,
                                   "The survey action service {0} is starting to analyze asset {1} with request type {2} in survey action engine {3} (guid={4});" +
                                       " the results will be stored in survey report {5}",
                                   "A new survey request is being processed.",
                                   "Verify that the survey action service ran to completion."),

    /**
     * OMES-SURVEY-ACTION-0018 - The survey action service {0} threw a {1} exception during the generation of survey report
     * {2} for asset {3} during request type {4} in survey action engine {5} (guid={6}). The error message was {7}
     */
    SURVEY_ACTION_SERVICE_FAILED("OMES-SURVEY-ACTION-0018",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "The survey action service {0} threw a {1} exception during the generation of survey report {2} for asset {3} " +
                                     "during request type {4} in survey action engine {5} (guid={6}). The error message was {7}",
                                 "A survey action service failed to complete the analysis of an asset.",
                                 "Review the exception to determine the cause of the error."),

    /**
     * OMES-SURVEY-ACTION-0019 - The survey action service {0} has completed the analysis of asset {1} with request type {2} in {3}
     * milliseconds; the results are stored in survey report {4}
     */
    SURVEY_ACTION_SERVICE_COMPLETE("OMES-SURVEY-ACTION-0019",
                                   AuditLogRecordSeverityLevel.SHUTDOWN,
                                   "The survey action service {0} has completed the analysis of asset {1} with request type {2} in {3} " +
                                       "milliseconds; the results are stored in survey report {4}",
                                   "A survey request has completed.",
                                   "It is possible to query the result of the survey request through Egeria's Open Metadata REST APIs."),

    /**
     * OMES-SURVEY-ACTION-0020 - Survey Action OMES in server {0} cannot start any survey action engines
     */
    NO_SURVEY_ACTION_ENGINES_STARTED("OMES-SURVEY-ACTION-0020",
                                     AuditLogRecordSeverityLevel.ERROR,
                                     "Survey Action OMES in server {0} cannot start any survey action engines",
                                     "The engine service is not able to run any survey requests.  It fails to start.",
                                     "Add the configuration for at least one survey action engine to this engine service."),

    /**
     * OMES-SURVEY-ACTION-0021 - Survey action engine {0} cannot update the status for survey action service {1}.
     * The exception was {2} with error message {3}
     */
    EXC_ON_ERROR_STATUS_UPDATE("OMES-SURVEY-ACTION-0021",
                               AuditLogRecordSeverityLevel.EXCEPTION,
                                 "Survey action engine {0} cannot update the status for survey action service {1}.  The exception was {2} with error message {3}",
                                 "The server is not able to record the failed result for a survey request. The survey report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the survey request."),

    /**
     * OMES-SURVEY-ACTION-0022 - Method {0} can not execute in the survey action engine {1} hosted by Survey Action OMES in server {2} because the associated " +
     *                                 "survey action service properties are null
     */
    NULL_SURVEY_SERVICE( "OMES-SURVEY-ACTION-0022",
                        AuditLogRecordSeverityLevel.ERROR,
                        "Method {0} can not execute in the survey action engine {1} hosted by Survey Action OMES in server {2} because the associated " +
                                "survey action service properties are null",
                        "The survey request is not run and an error is returned to the caller.",
                        "This may be an error in the survey action engine's logic or the Open Survey Framework (OSF) may have returned " +
                                "invalid configuration.  Raise an issue to get help to fix it"),


    /**
     * OMES-SURVEY-ACTION-0029 - The survey action service {0} linked to request type {1} can not be started.
     * The {2} exception was returned with message {3}
     */
    INVALID_SURVEY_ACTION_SERVICE("OMES-SURVEY-ACTION-0029",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                                  "The survey action service {0} linked to request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                                  "The survey request is not run and an error is returned to the caller.",
                                  "This may be an error in the survey action service's logic or the survey action service may not be properly deployed or " +
                                      "there is a configuration error related to the survey action engine.  " +
                                      "The configuration that defines the request type in the survey action engine and links " +
                                      "it to the survey action service is maintained in the metadata server by the Governance " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "survey action service's implementation has been deployed so the Survey Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the survey action service in which case, " +
                                      "raise an issue with the author of the survey action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the survey request."),

    /**
     * OMES-SURVEY-ACTION-0030 - The survey action service {0} linked to request type {1} is processing asset {2} and ignoring the following asset action targets: {3}
     */
    IGNORING_ASSETS("OMES-SURVEY-ACTION-0030",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The survey action service {0} linked to request type {1} for engine action {2} is processing asset {3} and ignoring the following asset action targets: {4}",
                                  "There are multiple assets in the action targets.  The survey action service can only process one of them.  The other assets are ignored.",
                                  "Create a new engine action for each of the ignored assets so that they each run in their own survey action service."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for SurveyActionAuditCode expects to be passed one of the enumeration rows defined in
     * SurveyActionAuditCode above.   For example:
     *     SurveyActionAuditCode   auditCode = SurveyActionAuditCode.SERVER_SHUTDOWN;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    SurveyActionAuditCode(String                      messageId,
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
