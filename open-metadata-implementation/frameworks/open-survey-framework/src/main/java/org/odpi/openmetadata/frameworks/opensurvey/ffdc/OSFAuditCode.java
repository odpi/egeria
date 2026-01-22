/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OSFAuditCode is used to define the message content for the Audit Log.
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
public enum OSFAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-SURVEY-0001 - The {0} survey action service has been disconnected - either due to its own actions or a cancel request
     */
    DISCONNECT_DETECTED("OPEN-SURVEY-0001",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} survey action service has been disconnected - either due to its own actions or a cancel request",
                        "The survey action framework will attempt to stop the work of the survey action framework",
                        "Monitor the shutdown of the survey action service."),

    /**
     * OPEN-SURVEY-0002 - The {0} Survey Action Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_TYPE_OF_CONNECTOR("OPEN-SURVEY-0002",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The {0} Survey Action Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                            "The survey cannot continue since it cannot work with the supplied connector.",
                            "Use the details from the error message to determine the class of the connector.  " +
                                    "Update the connector type associated with its Connection in the metadata store."),

    /**
     * OPEN-SURVEY-0003 - The survey action service {0} is creating log file {1}
     */
    CREATING_LOG_FILE("OPEN-SURVEY-0003",
                      AuditLogRecordSeverityLevel.INFO,
                      "The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2}",
                      "This message tells the survey team that a particular survey log file is being created.",
                      "No specific action is required.  The results are added to the log file and the asset for this log file is catalogued as a CSV file."),


    /**
     * OPEN-SURVEY-0004 - The survey action service {0} is overriding log file {1}
     */
    REUSING_LOG_FILE("OPEN-SURVEY-0004",
                     AuditLogRecordSeverityLevel.INFO,
                     "The survey action service {0} is overriding log file {1}",
                     "This message warns the survey team that a particular survey log file is being reused.",
                     "No specific action is required.  The new results are appended to the existing results."),


    /**
     * OPEN-SURVEY-0005 - No information about the asset {0} has been returned from the asset store for survey action service {1}
     */
    NO_ASSET( "OPEN-SURVEY-0005",
              AuditLogRecordSeverityLevel.ERROR,
              "No information about the asset {0} has been returned from the asset store for survey action framework {1}",
             "The service terminates without running the requested function.",
             "This is an unexpected condition because the lack of an asset should have been caught before this point."),


    /**
     * OPEN-SURVEY-0006 - Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}
     */
    WRONG_TYPE_OF_ASSET( "OPEN-SURVEY-0006",
              AuditLogRecordSeverityLevel.ERROR,
                       "Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}",
                       "The survey action service terminates.",
                       "The caller has requested a governance request type that is incompatible with the type of the " +
                               "asset that has been supplied.  This problem could be resolved by issuing the survey request with " +
                               "a governance request type that is compatible with the asset, or changing the survey action service " +
                               "associated with the governance request type to one that supports this type of asset."),

    /**
     * OPEN-SURVEY-0007 - Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3}
     */
    INVALID_ROOT_SCHEMA_TYPE( "OPEN-SURVEY-0007",
                              AuditLogRecordSeverityLevel.ERROR,
        "Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3}",
                             "The survey action service terminates because it can not proceed.",
                             "The caller has requested a governance request type that cannot process a root schema for an asset because its type is unsupported." +
                                     "  This problem could be resolved by issuing the survey request with " +
                                     "a governance request type that is compatible with the asset's schema, or changing the survey action service " +
                                     "associated with the governance request type to one that supports this type of schema."),

    /**
     * OPEN-SURVEY-0008 - The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached
     */
    NO_SCHEMA("OPEN-SURVEY-0008",
              AuditLogRecordSeverityLevel.ERROR,
              "The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached",
              "The survey cannot continue since it cannot assess whether the data stored in the associated resource matches the desired schema.",
              "Update the asset to include the desired schema and re-run this survey.  If you want to discover the asset's schema then use a different survey service."),

    /**
     * OPEN-SURVEY-0009 - The {0} Survey Acton Service has been supplied with asset {1} which does not have any schema attributes attached
     */
    NO_SCHEMA_ATTRIBUTES("OPEN-SURVEY-0009",
              AuditLogRecordSeverityLevel.ERROR,
              "The {0} Survey Acton Service has been supplied with asset {1} which does not have any schema attributes attached",
              "The survey cannot continue since it cannot assess whether the data stored in the associated resource matches the desired schema because there are no schema attributes attached to the root schema.",
              "Update the asset to include the desired schema attributes and re-run this survey.  If you want to discover the asset's schema then use a different survey service."),


    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for OSFAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OSFAuditCode(String                      messageId,
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
        return "OSFAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
