/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc;

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
     * APACHE-KAFKA-INTEGRATION-CONNECTOR-0001 - The {0} integration connector has been initialized to monitor event broker at URL {1} with templateQualifiedName={2}
     */
    CONNECTOR_CONFIGURATION("APACHE-KAFKA-INTEGRATION-CONNECTOR-0001",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector is cataloguing event broker {1} at URL {2} with template={3}",
                            "The connector monitors changes to the topics managed by the event broker and catalogs them in open metadata.",
                            "No specific action is required.  This message is to confirm the configuration for a specific catalog target.",
                            "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/"),

    /**
     * APACHE-KAFKA-INTEGRATION-CONNECTOR-0004 - The {0} integration connector received an unexpected {2} exception when retrieving topics from
     * event broker at {1}.  The error message was {3}
     */
    UNABLE_TO_RETRIEVE_TOPICS("APACHE-KAFKA-INTEGRATION-CONNECTOR-0004",
                              AuditLogRecordSeverityLevel.EXCEPTION,
                              "The {0} integration connector received an unexpected {2} exception when retrieving topics from event broker at {1}.  The error message was {3}",
                              "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
                              "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                      "resolved, follow the instructions in the messages produced by the integration daemon to restart this connector.",
                                      "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/"),

    /**
     * APACHE-KAFKA-INTEGRATION-CONNECTOR-0005 - The {0} integration connector has retrieved {2} topics from {1}
     */
    RETRIEVED_TOPICS("APACHE-KAFKA-INTEGRATION-CONNECTOR-0005",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector has retrieved {2} topics from {1}",
                     "The connector will maintain these topics as assets.",
                     "No action is required unless there are errors that follow indicating that the topics can not be maintained.",
                     "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/"),

    /**
     * APACHE-KAFKA-INTEGRATION-CONNECTOR-0016 - The {0} integration connector created the Topic {1} ({2}) for a new real-world topic
     */
    TOPIC_CREATED("APACHE-KAFKA-INTEGRATION-CONNECTOR-0016",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector created the Topic {1} ({2}) for a new real-world topic",
                  "The connector created the Topic as part of its monitoring of the topics in the event broker.",
                  "No action is required.  This message is to record the reason why the Topic was created.",
                  "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/"),

    /**
     * APACHE-KAFKA-INTEGRATION-CONNECTOR-0019 - The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is
     * no longer defined in the event broker
     */
    TOPIC_DELETED("APACHE-KAFKA-INTEGRATION-CONNECTOR-0019",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is no longer defined in the event broker",
                  "The connector removed the Topic as part of its monitoring of the topics in the event broker.",
                  "No action is required.  This message is to record the reason why the Topic was removed.",
                  "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/"),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;
    private final String                     url;


    /**
     * Constructor for the message definitions that have no page to link to.
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
        this(messageId, severity, message, systemAction, userAction, null);
    }


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
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    KafkaIntegrationConnectorAuditCode(String                      messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                      message,
                                       String                      systemAction,
                                       String                      userAction,
                                       String                      url)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                             userAction,
                                             url);
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
                                                                                    userAction,
                                                                                    url);
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
                       ", url='" + url + '\'' +
                       '}';
    }
}
