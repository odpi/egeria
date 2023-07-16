/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.elasticsearch.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The ElasticsearchIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.
 * <p>
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
public enum ElasticsearchIntegrationConnectorAuditCode implements AuditLogMessageSet {


    CONNECTOR_STOPPING("ELASTICSEARCH-INTEGRATION-CONNECTOR-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The {0} integration connector has stopped its monitoring and is shutting down",
            "The connector is disconnecting.",
            "No action is required unless there are errors that follow indicating that there were problems shutting down."),


    IO_EXCEPTION("ELASTICSEARCH-INTEGRATION-CONNECTOR-0002",
            OMRSAuditLogRecordSeverity.ERROR,
            "The {0} integration connector could not save data to Elasticsearch",
            "The connector is disconnecting.",
            "Verify the integrity of the ElasticSearch cluster and the client connection.");
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;


    /**
     * The constructor for ElasticsearchIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * ElasticsearchIntegrationConnectorAuditCode above.   For example:
     * <p>
     * ElasticsearchIntegrationConnectorAuditCode   auditCode = ElasticsearchIntegrationConnectorAuditCode.CONNECTOR_STOPPING;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique identifier for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    ElasticsearchIntegrationConnectorAuditCode(String messageId,
                                               OMRSAuditLogRecordSeverity severity,
                                               String message,
                                               String systemAction,
                                               String userAction) {
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
    public AuditLogMessageDefinition getMessageDefinition() {
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
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
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
    public String toString() {
        return "ElasticsearchIntegrationConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
