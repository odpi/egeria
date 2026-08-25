/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.babbage.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The BabbageAuditCode is used to define the message content for the Audit Log.
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
public enum BabbageAuditCode implements AuditLogMessageSet
{
    /**
     * BABBAGE-ANALYTICAL-ENGINE-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("BABBAGE-ANALYTICAL-ENGINE-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot catalog one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/egeria-solutions/organization-insight/overview/"),

    /**
     * BABBAGE-ANALYTICAL-ENGINE-0009 - The {0} integration connector has stopped its monitoring of engine actions from server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("BABBAGE-ANALYTICAL-ENGINE-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring of engine actions from server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down.",
                       "https://egeria-project.org/egeria-solutions/organization-insight/overview/"),


    /**
     * BABBAGE-ANALYTICAL-ENGINE-0011 - The {0} integration connector is starting its monitoring for analytical work from server {1} on platform {2}
     */
    STARTING_CONNECTOR("BABBAGE-ANALYTICAL-ENGINE-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting its monitoring for analytical work from server {1} on platform {2}",
                       "The connector is initializing engine actions from the Governance Action Types connected as Catalog Targets.",
                       "Monitor the creation of the engine actions and ensure they are executing successfully.",
                       "https://egeria-project.org/egeria-solutions/organization-insight/overview/"),

    /**
     * BABBAGE-ANALYTICAL-ENGINE-0012 - The {0} integration connector has created a new engine action {1} for Governance Action Type {2} ({3})
     */
    NEW_ENGINE_ACTION("BABBAGE-ANALYTICAL-ENGINE-0012",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector has created a new engine action {1} for Governance Action Type {2} ({3})",
                      "The connector is initiating analytical work.",
                      "No action is required.  This message is for monitoring the set up of the engine actions.",
                      "https://egeria-project.org/egeria-solutions/organization-insight/overview/"),



    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    BabbageAuditCode(String                      messageId,
                     AuditLogRecordSeverityLevel severity,
                     String                      message,
                     String                      systemAction,
                     String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


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
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    BabbageAuditCode(String                      messageId,
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
        return "BabbageAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
