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
                                 "No specific action is required.  This message is to confirm the configuration for the Kafka Open Lineage Receiver integration connector.  It is output for each unique embedded connector and KafkaTopic catalog target",
                                 "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-001 - The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION( "OPEN-LINEAGE-INTEGRATION-CONNECTOR-0010",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3}",
                         "The connector cannot process one or more lineage events.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0020 - The {0} integration connector has catalogued {1} {2} ({3}) from open lineage {4} {5}
     */
    ELEMENT_CATALOGUED("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0020",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has catalogued {1} {2} ({3}) from open lineage {4} {5}",
                       "The connector has created a new open metadata element to represent a job, run or dataset found in an open lineage event.",
                       "No specific action is required.  This message is to record the new element in the open metadata ecosystem.",
                       "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0021 - The {0} integration connector has added a {1} lineage relationship from {2} to {3}
     */
    LINEAGE_CATALOGUED("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0021",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has added a {1} lineage relationship from {2} to {3}",
                       "The connector has linked two elements together to represent a data flow, control flow, process hierarchy or column mapping found in an open lineage event.",
                       "No specific action is required.  This message is to record the new relationship in the open metadata ecosystem.",
                       "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0022 - The {0} integration connector is ignoring open lineage event {1} because {2}
     */
    EVENT_IGNORED("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0022",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector is ignoring open lineage event {1} because {2}",
                  "The connector cannot map the event to open metadata because it is missing a mandatory value, or its job or dataset matches multiple existing elements.",
                  "Review the event and, if it is expected to be catalogued, correct the producer or remove the duplicate elements from the open metadata repository.",
                  "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0024 - The {0} integration connector has renamed asset {1} from {2} to {3} following open lineage {4}
     */
    DATASET_RENAMED("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0024",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector has renamed asset {1} from {2} to {3} following open lineage {4}",
                    "An open lineage event reported a RENAME lifecycle state change for a dataset, so the names of the asset that represents it have been updated.  The previous names remain visible in the history of the element.",
                    "No specific action is required.  Use the element's history to see the names it had before the rename.",
                    "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0025 - The {0} integration connector has removed asset {1} ({2}) using delete method {3} following open lineage {4}
     */
    DATASET_DROPPED("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0025",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector has removed asset {1} ({2}) using delete method {3} following open lineage {4}",
                    "An open lineage event reported a DROP lifecycle state change for a dataset, so the asset that represents it has been archived or deleted according to the delete method configured for the connector.",
                    "No specific action is required.  Change the connector's delete method if archived (Memento) assets are wanted instead of deleted ones, or vice versa.",
                    "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

    /**
     * OPEN-LINEAGE-INTEGRATION-CONNECTOR-0023 - The {0} integration connector was unable to record {1} for open lineage event {2}; the {3} exception message was: {4}
     */
    OPTIONAL_METADATA_FAILED("OPEN-LINEAGE-INTEGRATION-CONNECTOR-0023",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The {0} integration connector was unable to record {1} for open lineage event {2}; the {3} exception message was: {4}",
                             "The lineage from the event has been catalogued, but the optional metadata (run details, schema, statistics, data quality or data scope) could not be recorded.",
                             "Use the details from the error message to determine the cause of the error.  The optional metadata will be captured from subsequent events once the problem is resolved.",
                             "https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/"),

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
    OpenLineageIntegrationConnectorAuditCode(String                      messageId,
                                             AuditLogRecordSeverityLevel severity,
                                             String                      message,
                                             String                      systemAction,
                                             String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


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
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OpenLineageIntegrationConnectorAuditCode(String                      messageId,
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
        return "OpenLineageIntegrationConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
