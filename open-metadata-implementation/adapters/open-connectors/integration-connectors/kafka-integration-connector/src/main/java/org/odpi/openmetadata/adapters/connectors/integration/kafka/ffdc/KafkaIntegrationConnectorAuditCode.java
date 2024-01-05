/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.kafka.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The KafkaIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum KafkaIntegrationConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * KAFKA-INTEGRATION-CONNECTOR-0001 - The {0} integration connector has been initialized to monitor event broker at URL {1} with templateQualifiedName={2}
     */
    CONNECTOR_CONFIGURATION("KAFKA-INTEGRATION-CONNECTOR-0001",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector has been initialized to monitor event broker at URL {1} with templateQualifiedName={2}",
                            "The connector is designed to monitor changes to the topics managed by the event broker.  " +
                                    "If the templateQualifiedName is set, it identifies a template entity to use.",
                            "No specific action is required.  This message is to confirm the configuration for the integration connector."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0002 - The {0} integration connector encountered an {1} exception when opening event broker {2} during 
     * the {3} method.  The exception message included was {4}
     */
    BAD_CONFIGURATION("KAFKA-INTEGRATION-CONNECTOR-0002",
                      AuditLogRecordSeverityLevel.EXCEPTION,
                      "The {0} integration connector encountered an {1} exception when opening event broker {2} during the {3} method.  The exception message included was {4}",
                      "The exception is passed back to the Topic Integrator OMIS in the integration daemon that is hosting " +
                              "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                              "error handling that was performed.  These can help to determine how to recover from this error",
                      "This message contains the exception that was the original cause of the problem. Use the information from the " +
                              "exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  " +
                              "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                              "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0004 - The {0} integration connector received an unexpected {2} exception when retrieving topics from 
     * event broker at {1}.  The error message was {3}
     */
    UNABLE_TO_RETRIEVE_TOPICS("KAFKA-INTEGRATION-CONNECTOR-0004",
                              AuditLogRecordSeverityLevel.EXCEPTION,
                              "The {0} integration connector received an unexpected {2} exception when retrieving topics from event broker at {1}.  The error message was {3}",
                              "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
                              "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                      "resolved, follow the instructions in the messages produced by the integration daemon to restart this connector."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0005 - The {0} integration connector has retrieved {2} topics from {1}
     */
    RETRIEVED_TOPICS("KAFKA-INTEGRATION-CONNECTOR-0005",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector has retrieved {2} topics from {1}",
                     "The connector will maintain these topics as assets.",
                     "No action is required unless there are errors that follow indicating that the topics can not be maintained."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0009 - The {0} integration connector has stopped its topic monitoring and is shutting down
     */
    CONNECTOR_STOPPING("KAFKA-INTEGRATION-CONNECTOR-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its topic monitoring and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0014 - An unexpected {0} exception was returned to the {1} integration connector when it tried to update the
     * Topic in the metadata repositories for topic {2}.  The error message was {3}
     */
    UNEXPECTED_EXC_TOPIC_UPDATE("KAFKA-INTEGRATION-CONNECTOR-0014",
                                AuditLogRecordSeverityLevel.EXCEPTION,
                                "An unexpected {0} exception was returned to the {1} integration connector when it tried to update the " +
                                        "Topic in the metadata repositories for topic {2}.  The error message was {3}",
                                "The exception is logged and the integration connector continues to synchronize metadata.  " +
                                        "This topic is not catalogued at this time but may succeed later.",
                                "Use the message in the unexpected exception to determine the root cause of the error and fix it."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0015 - The {0} integration connector is unable to retrieve the Topic template with qualified name: {1}
     */
    MISSING_TEMPLATE("KAFKA-INTEGRATION-CONNECTOR-0015",
                     AuditLogRecordSeverityLevel.ERROR,
                     "The {0} integration connector is unable to retrieve the Topic template with qualified name: {1}",
                     "The metadata element for the template is not found in the open metadata repositories.  " +
                             "The template name was configured for the connector.  This means that topics should be catalogued " +
                             "using the template.  Since the template is missing, topics are not being catalogued.",
                     "Create the template in the metadata repository.  The connector will catalog the topics during " +
                             "its next periodic refresh or you can force it to refresh immediately by calling the refresh" +
                             "operation on the integration daemon."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0016 - The {0} integration connector created the Topic {1} ({2}) for a new real-world topic
     */
    TOPIC_CREATED("KAFKA-INTEGRATION-CONNECTOR-0016",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector created the Topic {1} ({2}) for a new real-world topic",
                  "The connector created the Topic as part of its monitoring of the topics in the event broker.",
                  "No action is required.  This message is to record the reason why the Topic was created."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0017 - The {0} integration connector created the Topic {1} ({2}) for a new real-world topic using template {3} ({4})
     */
    TOPIC_CREATED_FROM_TEMPLATE("KAFKA-INTEGRATION-CONNECTOR-0017",
                                AuditLogRecordSeverityLevel.INFO,
                                "The {0} integration connector created the Topic {1} ({2}) for a new real-world topic using template {3} ({4})",
                                "The connector created the Topic as part of its monitoring of the topics in the event broker.  " +
                                        "The template provides details of additional metadata that should also be attached to the new Topic element.  " +
                                        "It was specified in the templateQualifiedName configuration property of the connector.",
                                "No action is required.  This message is to record the reason why the Topic was created with the template."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0018 - The {0} integration connector has updated the Topic {1} ({2}) because the real-world topic changed
     */
    TOPIC_UPDATED("KAFKA-INTEGRATION-CONNECTOR-0018",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has updated the Topic {1} ({2}) because the real-world topic changed",
                  "The connector updated the Topic as part of its monitoring of the topics in the event broker.",
                  "No action is required.  This message is to record the reason why the Topic was updated."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0019 - The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is 
     * no longer defined in the event broker
     */
    TOPIC_DELETED("KAFKA-INTEGRATION-CONNECTOR-0019",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is no longer defined in the event broker",
                  "The connector removed the Topic as part of its monitoring of the topics in the event broker.",
                  "No action is required.  This message is to record the reason why the Topic was removed."),

    /**
     * KAFKA-INTEGRATION-CONNECTOR-0020 - The {0} integration connector has archived the Topic {1} ({2}) because the real-world topic
     * is no longer stored in the event broker
     */
    TOPIC_ARCHIVED("KAFKA-INTEGRATION-CONNECTOR-0020",
                   AuditLogRecordSeverityLevel.INFO,
                   "The {0} integration connector has archived the Topic {1} ({2}) because the real-world topic is no longer stored in the event broker",
                   "The connector updated the Topic to reflect that is is now just a placeholder for an asset that no longer exists.  " +
                           "Its presence is still needed in the metadata repository for lineage reporting.",
                   "No action is required.  This message is to record the reason why the Topic was archived."),


    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for KafkaIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * KafkaIntegrationConnectorAuditCode above.   For example:
     *     KafkaIntegrationConnectorAuditCode   auditCode = KafkaIntegrationConnectorAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    KafkaIntegrationConnectorAuditCode(String                      messageId,
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
        return "KafkaIntegrationConnectorAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
