/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OpenLineageIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OpenLineageIntegrationConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0001 - The {0} integration connector has been initialized to monitor Apache Kafka topic {1} with connection: {2}
     */
    KAFKA_RECEIVER_CONFIGURATION("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0001",
                                 AuditLogRecordSeverityLevel.INFO,
                                 "The {0} integration connector is monitoring Apache Kafka topic {1} with connection: {2}",
                                 "The connector is designed to monitor open lineage events published to an Apache Kafka topic.",
                                 "No specific action is required.  This message is to confirm the configuration for the Kafka Open Lineage Receiver integration connector.  It is output for each unique embedded connector and KafkaTopic catalog target"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0002 - The {0} integration connector encountered an {1} exception when opening connector to topic {2} during the {3} method.  The exception message included was {4} and the connection was {5}
     */
    BAD_KAFKA_RECEIVER_CONFIGURATION("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0002",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "The {0} integration connector encountered an {1} exception when opening connector to topic {2} during the {3} method.  The exception message included was {4} and the connection was {5}",
                          "The exception is passed back to the integration daemon that is hosting " +
                                  "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                                  "error handling that was performed.  These can help to determine how to recover from this error",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0003 - The {0} integration connector was passed no embedded open metadata topic connection in its connection properties: {1}
     */
    NO_KAFKA_CONNECTION("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0003",
                       AuditLogRecordSeverityLevel.ERROR,
                       "The {0} integration connector was passed no embedded open metadata topic connection in its connection properties: {1}",
                       "The configuration for this connector does not have the correct connection configuration that includes the connection for the kafka open metadata topic connector.",
                       "Review the documentation for this connector and correct its configuration to include the connection for the open " +
                               "metadata topic connection that should be embedded in the connection for the integration connector with the topic name set up in the network address of the endpoint."),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0004 - The {0} integration connector was passed a blank topic name in its connection properties: {1}
     */
    NO_KAFKA_TOPIC_NAME("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0004",
                            AuditLogRecordSeverityLevel.ERROR,
                        "The {0} integration connector was passed a blank topic name in its connection properties: {1}",
                        "The configuration for this connector does not include the name of the topic in the embedded connection for the kafka open metadata topic connector.",
                        "Review the documentation for this connector and correct its configuration to ensure the connection for the open " +
                                "metadata topic connection that should be embedded in the connection for the integration connector has the topic name set up in the network address of the endpoint."),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0005 - The {0} integration connector is listening for open lineage events on topic {1}
     */
    LISTENING_ON_TOPIC("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0005",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector is listening for open lineage events on topic {1}",
                              "The connector will pass any events received to the other integration connectors that have registered listeners.",
                              "No action is required unless there are errors that follow indicating that there are problems access events from the topic."),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-001 - The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION( "OPEN-LINEAGE-INTEGRATION-CONNECTOR-0010",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3}",
                         "The connector is unable to process one or more lineage events.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for OpenLineageIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * OpenLineageIntegrationConnectorAuditCode above.   For example:
     * <br><br>
     *     OpenLineageIntegrationConnectorAuditCode   auditCode = OpenLineageIntegrationConnectorAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OpenLineageIntegrationConnectorAuditCode(String                      messageId,
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
        return "OpenLineageIntegrationConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
