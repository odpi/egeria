/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.egeria.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The EgeriaInfrastructureConnectorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum EgeriaInfrastructureConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * The {0} integration connector has been started and will call the platforms with userId {1}.  The monitored platforms are: {2}
     */
    EGERIA_CONNECTOR_START("EGERIA-INFRASTRUCTURE-CONNECTORS-0001",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The {0} integration connector has been started and will call the platforms with userId {1}.  The monitored platforms are: {2}",
                           "The connector is designed to catalog details of Software Server Platforms that have the deployedImplementationType property set to 'OMAG Server Platform'.",
                           "No specific action is required.  This message is to confirm the start of the integration connector."),

    /**
     * The {0} integration connector has been initialized with the following platforms: {1}
     */
    KAFKA_CONNECTOR_START("EGERIA-INFRASTRUCTURE-CONNECTORS-0002",
                          OMRSAuditLogRecordSeverity.INFO,
                          "The {0} integration connector has been initialized with the following platforms: {1}",
                          "The connector is designed to monitor changes to the topics managed by the catalogued event brokers and validate that all topics are catalogued.",
                          "No specific action is required.  This message is to confirm the configuration for the integration connector."),

    /**
     * The {0} integration connector received an unexpected {2} exception when auditing topics.  The error message was {2}
     */
    UNABLE_TO_RETRIEVE_TOPICS("EGERIA-INFRASTRUCTURE-CONNECTORS-0003",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "The {0} integration connector received an unexpected {2} exception when auditing topics.  The error message was {2}",
                              "The exception is returned to the integration daemon that is hosting this integration connector to enable it to perform error handling.",
                              "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                      "resolved, follow the instructions in the messages produced by the integration daemon to restart this integration connector."),

    /**
     * The {0} integration connector has retrieved a topic called {1} from {2} that is not catalogued
     */
    UNKNOWN_TOPIC("EGERIA-INFRASTRUCTURE-CONNECTORS-0004",
                  OMRSAuditLogRecordSeverity.ACTION,
                  "The {0} integration connector has retrieved a topic called {1} from {2} that is not catalogued",
                  "The connector continues to validate topics.",
                  "Determine why this topic is not catalogued."),


    /**
     * The {0} integration connector has detected an Egeria OMAG Server Platform called '{1}' ({2}) with unique identifier {3}
     */
    PLATFORM_DETECTED("EGERIA-INFRASTRUCTURE-CONNECTORS-0005",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The {0} integration connector has detected an Egeria OMAG Server Platform called '{1}' ({2}) with unique identifier {3}",
                    "The connector will look up the endpoint for the platform.",
                    "No specific action is required.  This message is to confirm the detection of the platform."),


    /**
     * The {0} integration connector has started cataloguing Egeria OMAG Server Platform called '{1}' ({2}) with unique identifier {3} located at URL {4}
     */
    PLATFORM_CATALOGUING_STARTED("EGERIA-INFRASTRUCTURE-CONNECTORS-0006",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The {0} integration connector has started cataloguing Egeria OMAG Server Platform called '{1}' ({2}) with unique identifier {3} located at URL {4}",
                    "The connector is ready to catalog details of Software Server Platform.",
                    "No specific action is required.  This message is to confirm the start of the cataloguing process for the platform."),


    /**
     * The {0} integration connector received an unexpected {1} exception when retrieving details of a platform.  The error message was {2}
     */
    UNEXPECTED_EXCEPTION("EGERIA-INFRASTRUCTURE-CONNECTORS-0007",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The {0} integration connector received an unexpected {1} exception when retrieving details of a platform.  The error message was {2}",
                         "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
                         "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                             "resolved, follow the instructions in the messages produced by the integration daemon to restart this connector."),

    /**
     * The {0} integration connector is not able to retrieve platform {1} ({2}) from the catalog
     */
    UNKNOWN_PLATFORM("EGERIA-INFRASTRUCTURE-CONNECTORS-0008",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The {0} integration connector is not able to retrieve platform {1} ({2}) from the catalog",
                              "The connector continues to catalog platforms.",
                              "Determine why this platform is not catalogued."),

    /**
     * The {0} integration connector has stopped its platform monitoring and is shutting down
     */
    CONNECTOR_STOPPING("EGERIA-INFRASTRUCTURE-CONNECTORS-0009",
                                  OMRSAuditLogRecordSeverity.INFO,
                                  "The {0} integration connector has stopped its platform monitoring and is shutting down",
                                  "The connector is disconnecting.",
                                  "No action is required unless there are errors that follow indicating that there were problems shutting down."),

       ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for KafkaTopicsCaptureConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * KafkaTopicsCaptureConnectorAuditCode above.   For example:
     * <br><br>
     *     KafkaTopicsCaptureConnectorAuditCode   auditCode = KafkaTopicsCaptureConnectorAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    EgeriaInfrastructureConnectorAuditCode(String                     messageId,
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
        return "EgeriaInfrastructureConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
