/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The KafkaOpenMetadataTopicConnectorAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum KafkaOpenMetadataTopicConnectorAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OCF-KAFKA-TOPIC-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "Connecting to Apache Kafka Topic {0} with a server identifier of {1}",
                         "The local server has started up the Apache Kafka connector.",
                         "No action is required.  This is part of the normal operation of the server."),

    SERVICE_PRODUCER_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0002",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "{0} properties passed to the Apache Kafka Producer for topic {1}",
                                "The server is registering to receive events from Apache Kafka using the properties associated with this log record.",
                                "No action is required.  This is part of the normal operation of the server."),

    SERVICE_CONSUMER_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0003",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "{0} properties passed to the Apache Kafka Consumer for topic {1}",
                                "The server is registering to receive events from Apache Kafka using the properties associated with this log record.",
                                "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OCF-KAFKA-TOPIC-CONNECTOR-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Apache Kafka connector for topic {0} is shutting down",
                     "The local server has requested shut down of the Apache Kafka connector.",
                     "No action is required.  This is part of the normal operation of the server."),

    NULL_ADDITIONAL_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0005",
                               AuditLogRecordSeverityLevel.ERROR,
                               "The Apache Kafka connector for topic {0} has been set up with no additional properties",
                               "Without these properties, the server is not able to send and receive events on the topic.",
                               "This problem must be fixed before the server can exchange metadata.  The properties are supplied on the event bus admin command."),

    UNABLE_TO_PARSE_CONFIG_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0006",
                                      AuditLogRecordSeverityLevel.ERROR,
                                      "The Apache Kafka connector for topic {0} has been set up with configuration properties that produced the {1} exception when read.  This is the error message: {2}",
                                      "An exception occurred reading the configuration properties.  This means that the server is not able to send and receive events on the topic.",
                                      "This problem must be fixed before the server can exchange metadata.  The properties are supplied on the event bus admin command."),

    NO_TOPIC_NAME("OCF-KAFKA-TOPIC-CONNECTOR-0007",
                  AuditLogRecordSeverityLevel.ERROR,
                  "The Apache Kafka connector has been set up with no topic name",
                  "Without the name of the topic, the server is not able to send and receive events.",
                  "This problem must be fixed before the server can exchange metadata.  The topic name is supplied in the endpoint object of the connector's connection."),

    EXCEPTION_RECEIVING_EVENT("OCF-KAFKA-TOPIC-CONNECTOR-0008",
                              AuditLogRecordSeverityLevel.ERROR,
                              "The connector listening on topic {0} received an unexpected exception {1} from Apache Kafka.  The message in the exception was {2}",
                              "A call to receive events from Apache Kafka failed with an exception.",
                              "Use the information in the event and the exception message, along with other messages to determine the source of the error."),

    EXCEPTION_DISTRIBUTING_EVENT("OCF-KAFKA-TOPIC-CONNECTOR-0009",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "The Apache Kafka connector listening on topic {0} received an unexpected exception {1} distributing an event to components within the server.  The event was {2} and the message in the exception was {3}",
                                 "An incoming event could not be processed by one or more components in the server.",
                                 "Use the information in the event and the exception message, along with other messages to determine the source of the error."),

    KAFKA_PRODUCER_START("OCF-KAFKA-TOPIC-CONNECTOR-0010",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Apache Kafka producer for topic {0} is starting up with {1} buffered messages",
                         "The local server has started the Apache Kafka connector.",
                         "No action is required.  This is part of the normal operation of the server."),

    KAFKA_PRODUCER_SHUTDOWN("OCF-KAFKA-TOPIC-CONNECTOR-0011",
                            AuditLogRecordSeverityLevel.SHUTDOWN,
                            "The Apache Kafka producer for topic {0} is shutting down after sending {2} messages and with {1} unsent messages",
                            "The local server has requested shut down of the Apache Kafka connector.",
                            "No action is required.  This is part of the normal operation of the server."),

    EVENT_SEND_IN_ERROR_LOOP("OCF-KAFKA-TOPIC-CONNECTOR-0012",
                             AuditLogRecordSeverityLevel.ERROR,
                             "Unable to send event on topic {0}.  {1} events successfully sent; {2} events buffered. Latest error message is {3}",
                             "There is a reoccurring error being returned by the Apache Kafka event bus.  Outbound events are being buffered.",
                             "Review the operational status of Apache Kafka to ensure it is running and the topic is defined.  " +
                                     "If no events have been send, then it may be a configuration error, either in this " +
                                     "server or in the event bus itself. Once the error is corrected, " +
                                     "the server will send the buffered events.  "),

    MISSING_PROPERTY( "OCF-KAFKA-TOPIC-CONNECTOR-0013 ",
                      AuditLogRecordSeverityLevel.ERROR,
                      "Property {0} is missing from the Kafka Event Bus configuration",
                      "The system is unable to connect to the event bus.",
                      "Add the missing property to the event bus properties in the server configuration."),

    SERVICE_FAILED_INITIALIZING( "OCF-KAFKA-TOPIC-CONNECTOR-0014 ",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "Connecting to bootstrap Apache Kafka Broker {0}",
                                 "The local server has failed to started up the Apache Kafka connector, Kafka Broker is unavailable",
                                 "Ensure Kafka is running and restart the local Egeria Server"),

    KAFKA_CONNECTION_RETRY( "OCF-KAFKA-TOPIC-CONNECTOR-0015",
                            AuditLogRecordSeverityLevel.STARTUP,
                            "The local server is attempting to connect to Kafka brokers at {0} [ attempt {1} of {2} ]",
                            "The system retries the connection after a short wait.",
                            "Ensure the Kafka Cluster has started"),
    UNEXPECTED_SHUTDOWN_EXCEPTION( "OCF-KAFKA-TOPIC-CONNECTOR-0016",
                                   AuditLogRecordSeverityLevel.SHUTDOWN,
                                   "An unexpected error {0} was encountered while closing the kafka topic connector for {1}: action {2} and error message {3}",
                                   "The connector continues to shutdown.  Some resources may not be released properly.",
                                   "Check the OMAG Server's audit log and Kafka error logs for related messages that may indicate " +
                                           "if there are any unreleased resources."),
    EXCEPTION_COMMITTING_OFFSETS("OCF-KAFKA-TOPIC-CONNECTOR-0017",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "An unexpected error {0} was encountered while committing consumed event offsets to topic {1}: error message is {2}",
                                 "Depending on the nature of the error, events may no longer be exchanged with the topic.",
                                 "Check the OMAG Server's audit log and Kafka error logs for related messages that " +
                                         "indicate the cause of this error.  Work to clear the underlying error.  " +
                                         "Once fixed, it may be necessary to restart the server to cause a reconnect to Kafka."),
    FAILED_TO_COMMIT_CONSUMED_EVENTS("OCF-KAFKA-TOPIC-CONNECTOR-0018",
                                     AuditLogRecordSeverityLevel.INFO,
                                     "The Egeria client was rebalanced by Kafka and failed to commit already consumed events",
                                     "If this was experienced in a production environment check the kafka heartbeat and batch processing settings.",
                                     "Check the OMAG Server's audit log and Kafka error logs for related messages that " +
                                             "indicate the cause of this error.  Work to clear the underlying error.  " +
                                             "Once fixed, it may be necessary to restart the server to cause a reconnect to Kafka."),

    ERROR_CONNECTING_KAFKA_PRODUCER("OCF-KAFKA-TOPIC-CONNECTOR-0019",
                                    AuditLogRecordSeverityLevel.EXCEPTION,
                                    "Egeria encountered an exception when attempting to connect the message producer to the kafka topic {0}",
                                    "Resolve the kafka service is available and that the kafka producer connection properties are correct",
                                    "Check the  Kafka error logs for related messages that could " +
                                            "indicate the cause of this error.  Work to clear the underlying error.  " +
                                            "Once fixed, it may be necessary to restart the server to cause a reconnect to Kafka."),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for KafkaOpenMetadataTopicConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * KafkaOpenMetadataTopicConnectorAuditCode above.   For example:
     *     KafkaOpenMetadataTopicConnectorAuditCode   auditCode = KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_COMMITTING_OFFSETS;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId unique identifier for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    KafkaOpenMetadataTopicConnectorAuditCode(String                      messageId,
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
