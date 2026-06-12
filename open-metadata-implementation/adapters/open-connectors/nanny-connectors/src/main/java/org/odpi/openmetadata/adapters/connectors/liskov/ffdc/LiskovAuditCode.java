/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The LiskovAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data for the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum LiskovAuditCode implements AuditLogMessageSet
{
    /**
     * LISKOV-DATA-HUB-MANAGER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("LISKOV-DATA-HUB-MANAGER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot catalog one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * LISKOV-DATA-HUB-MANAGER-0009 - The {0} integration connector has stopped its monitoring of data hubs from server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("LISKOV-DATA-HUB-MANAGER-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring of data hubs from server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),


    /**
     * LISKOV-DATA-HUB-MANAGER-0011 - The {0} integration connector is starting its monitoring of data hubs from server {1} on platform {2}
     */
    STARTING_CONNECTOR("LISKOV-DATA-HUB-MANAGER-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting its monitoring of data hubs from server {1} on platform {2}",
                       "The connector is initializing its monitoring of the data hubs connected as Catalog Targets.",
                       "Monitor the data dictionaries for these data hubs are being maintained successfully."),

    /**
     * LISKOV-DATA-HUB-MANAGER-0012 - The {0} integration connector has created a new catalog target for data hub {1} ({2})
     */
    NEW_DATA_HUB("LISKOV-DATA-HUB-MANAGER-0012",
                 AuditLogRecordSeverityLevel.INFO,
                 "The {0} integration connector has created a new catalog target for data hub {1} ({2})",
                 "The connector is initiating its management of a new data hub .",
                 "No action is required.  This message is for monitoring the set up of the data hub management."),


    /**
     * LISKOV-DATA-HUB-MANAGER-0013 - The {0} integration connector has created a new data dictionary for data hub {1} ({2})
     */
    NEW_DATA_DICTIONARY("LISKOV-DATA-HUB-MANAGER-0013",
                 AuditLogRecordSeverityLevel.INFO,
                 "The {0} integration connector has created a new data dictionary for data hub {1} ({2})",
                 "The connector has created a data dictionary for a new data hub.",
                 "No action is required.  This message is for monitoring the set up of the data hub data dictionary."),

    /**
     * LISKOV-DATA-HUB-MANAGER-0014 - The {0} integration connector has created a new data field for data hub {1} ({2})
     */
    NEW_DATA_FIELD("LISKOV-DATA-HUB-MANAGER-0014",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector has created a new data field for data hub {1} ({2})",
                        "The connector has created a data field for a data hub's data dictionary.",
                        "No action is required.  This message is for monitoring the set up of the data hub's data fields."),

    /**
     * LISKOV-DATA-HUB-MANAGER-0015 - The {0} integration connector is refreshing data dictionary for data hub {1} ({2})
     */
    REFRESHING_DATA_HUB("LISKOV-DATA-HUB-MANAGER-0015",
                 AuditLogRecordSeverityLevel.INFO,
                 "The {0} integration connector is refreshing data dictionary for data hub {1} ({2})",
                 "The connector is initiating its refreshing of a data hub .",
                 "No action is required.  This message is for monitoring the activity of the data hub management."),

    /**
     * LISKOV-DATA-HUB-MANAGER-0016 - The {0} integration connector is refreshing data fields from {1} data store {2} ({3}) for data hub {4} ({5})
     */
    REFRESHING_DATA_HUB_STORE("LISKOV-DATA-HUB-MANAGER-0016",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector is refreshing data fields from {1} data store {2} ({3}) for data hub {4} ({5})",
                              "The connector is initiating its refreshing of a data hub .",
                              "No action is required.  This message is for monitoring the activity of the data hub management."),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for DistributeKafkaAuditCode expects to be passed one of the enumeration rows defined in
     * DistributeKafkaAuditCode above.   For example:
     * <br>
     *     DistributeKafkaAuditCode   auditCode = DistributeKafkaAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    LiskovAuditCode(String                      messageId,
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
        return "LiskovAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
