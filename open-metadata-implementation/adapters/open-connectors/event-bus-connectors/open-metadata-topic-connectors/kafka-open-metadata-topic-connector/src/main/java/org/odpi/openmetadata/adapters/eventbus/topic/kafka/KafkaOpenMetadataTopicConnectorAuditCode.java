/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The KafkaOpenMetadataTopicConnectorAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum KafkaOpenMetadataTopicConnectorAuditCode
{
    SERVICE_INITIALIZING("OCF-KAFKA-TOPIC-CONNECTOR-0001",
              OMRSAuditLogRecordSeverity.STARTUP,
              "Connecting to Apache Kafka Topic {0} with a server identifier of {1}",
              "The local server has started up the Apache Kafka connector.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_PRODUCER_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0002",
              OMRSAuditLogRecordSeverity.STARTUP,
              "{0} properties passed to the Apache Kafka Producer for topic {1}",
              "The server is registering to receive events from Apache Kafka using the properties associated with this log record.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_CONSUMER_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0003",
              OMRSAuditLogRecordSeverity.STARTUP,
              "{0} properties passed to the Apache Kafka Consumer for topic {1}",
              "The server is registering to receive events from Apache Kafka using the properties associated with this log record.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OCF-KAFKA-TOPIC-CONNECTOR-0004",
              OMRSAuditLogRecordSeverity.SHUTDOWN,
              "The Apache Kafka connector for topic {0} is shutting down",
              "The local server has requested shut down of the Apache Kafka connector.",
              "No action is required.  This is part of the normal operation of the server."),

    NULL_ADDITIONAL_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0005",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Apache Kafka connector for topic {0} has been set up with no additional properties",
             "Without these properties, the server is not able to send and receive events on the topic.",
             "This problem must be fixed before the server can exchange metadata.  The properties are supplied on the event bus admin command."),

    UNABLE_TO_PARSE_CONFIG_PROPERTIES("OCF-KAFKA-TOPIC-CONNECTOR-0006",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Apache Kafka connector for topic {0} has been set up with configuration properties that produced the {1} exception when read.  This is the error message: {2}",
             "An exception occurred reading the configuration properties.  This means that the server is not able to send and receive events on the topic.",
             "This problem must be fixed before the server can exchange metadata.  The properties are supplied on the event bus admin command."),

    NO_TOPIC_NAME("OCF-KAFKA-TOPIC-CONNECTOR-0007",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Apache Kafka connector has been set up with no topic name",
             "Without the name of the topic, the server is not able to send and receive events.",
             "This problem must be fixed before the server can exchange metadata.  The topic name is supplied in the endpoint object of the connector's connection."),

    EXCEPTION_RECEIVING_EVENT("OCF-KAFKA-TOPIC-CONNECTOR-0008",
             OMRSAuditLogRecordSeverity.ERROR,
             "The connector listening on topic {0} received an unexpected exception {1} from Apache Kafka.  The message in the exception was {2}",
             "A call to receive events from Apache Kafka failed with an exception.",
             "Use the information in the event and the exception message, along with other messages to determine the source of the error."),

    EXCEPTION_DISTRIBUTING_EVENT("OCF-KAFKA-TOPIC-CONNECTOR-0009",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Apache Kafka connector listening on topic {0} received an unexpected exception {1} distributing an event to components within the server.  The event was {2} and the message in the exception was {3}",
             "An incoming event could not be processed by one or more components in the server.",
             "Use the information in the event and the exception message, along with other messages to determine the source of the error."),

    KAFKA_PRODUCER_START("OCF-KAFKA-TOPIC-CONNECTOR-0010",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Apache Kafka producer for topic {0} is starting up with {1} buffered messages",
             "The local server has started the Apache Kafka connector.",
             "No action is required.  This is part of the normal operation of the server."),

    KAFKA_PRODUCER_SHUTDOWN("OCF-KAFKA-TOPIC-CONNECTOR-0011",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Apache Kafka producer for topic {0} is shutting down after sending {2} messages and with {1} unsent messages",
             "The local server has requested shut down of the Apache Kafka connector.",
             "No action is required.  This is part of the normal operation of the server."),

    EVENT_SEND_IN_ERROR_LOOP("OCF-KAFKA-TOPIC-CONNECTOR-0012",
             OMRSAuditLogRecordSeverity.ERROR,
             "Unable to send event on topic {0}.  {1} events successfully sent; {2} events buffered. Latest error message is {3}",
             "There is a reoccurring error being returned by the Apache Kafka event bus.  Outbound events are being buffered.",
             "Review the operational status of Apache Kafka to ensure it is running and the topic is defined.  " +
                                     "If no events have been send, then it may be a configuration error, either in this " +
                                     "server or in the event bus itself. Once the error is corrected, " +
                                     "the server will send the buffered events.  "),

    MISSING_PROPERTY( "OCF-KAFKA-TOPIC-CONNECTOR-0013 ",
             OMRSAuditLogRecordSeverity.ERROR,
             "Property {0} is missing from the Kafka Event Bus configuration",
             "The system is unable to connect to the event bus.",
             "Add the missing property to the event bus properties in the server configuration.")

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(KafkaOpenMetadataTopicConnectorAuditCode.class);


    /**
     * The constructor for AssetConsumerAuditCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerAuditCode above.   For example:
     *
     *     KafkaOpenMetadataTopicConnectorAuditCode   auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_INITIALIZING;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    KafkaOpenMetadataTopicConnectorAuditCode(String                     messageId,
                                             OMRSAuditLogRecordSeverity severity,
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
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== Kafka Connector Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> Kafka Connector Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
