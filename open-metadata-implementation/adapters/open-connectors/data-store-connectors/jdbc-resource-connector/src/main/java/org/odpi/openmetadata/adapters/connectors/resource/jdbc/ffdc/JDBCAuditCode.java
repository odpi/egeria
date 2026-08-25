/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The JDBCAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum JDBCAuditCode implements AuditLogMessageSet
{
    /**
     * JDBC-RESOURCE-CONNECTOR-0001 - The JDBC resource connector received an unexpected exception {0} during method {1}; the error message was: {2}
     */
    UNEXPECTED_EXCEPTION("JDBC-RESOURCE-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The JDBC resource connector for database {0} received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * JDBC-RESOURCE-CONNECTOR-0003 - The JDBC resource connector for database {0} has received {1} results from query {2}
     */
    UNEXPECTED_ROW_COUNT_FROM_DATABASE("JDBC-RESOURCE-CONNECTOR-0003",
                                       AuditLogRecordSeverityLevel.INFO,
                                       "The JDBC resource connector for database {0} has received {1} results from query {2}",
                                    "The connector has detected that the row count on the SQL requests is incorrect.",
                                    "Check the code where this error occurred to determine if the connector code is wrong - or the caller.  Correct whichever has the problem.",
                                    "https://egeria-project.org/concepts/digital-resource-connector/"),

    ROllBACK_AFTER_EXCEPTION("JDBC-RESOURCE-CONNECTOR-0004",
                           AuditLogRecordSeverityLevel.INFO,
                           "The JDBC resource connector for database {0} has issued a rollback after receiving {1} error with message {2}",
                           "The connector is attempting to clean up the connection after an error.",
                           "Diagnose and fix the cause of the original exception.  Check that subsequent requests execute successfully."),

    /**
     * JDBC-RESOURCE-CONNECTOR-0009 - JDBC resource connector is closing all {0} connection(s) to database {1} and is shutting down
     */
    CONNECTOR_STOPPING("JDBC-RESOURCE-CONNECTOR-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The JDBC resource connector for database {0} is closing all {1} connection(s) to database and is shutting down",
                       "The connector has been requested to disconnect from the database and is ensuring all connections are closed.  This message is output by each data source that was created by the connector.  Therefore the number of times that this message is emitted indicates the number of data sources were created by the connector.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down.",
                       "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * JDBC-RESOURCE-CONNECTOR-0010 - The JDBC resource connector for database {0} has enabled TCP keepalive using driver property {1}
     */
    CONNECTION_KEEPALIVE_ENABLED("JDBC-RESOURCE-CONNECTOR-0010",
                                 AuditLogRecordSeverityLevel.INFO,
                                 "The JDBC resource connector for database {0} has enabled TCP keepalive using driver property {1}",
                                 "The connector has switched on socket level keepalive for the connections in its pool.  This stops the pool from filling up with connections whose network peer has disappeared silently, which would otherwise drain the pool to zero without it recovering.",
                                 "No action is required.  If the database is reached through a firewall or load balancer that drops idle connections, check that its idle timeout is longer than the keepalive interval configured in the operating system.",
                                 "https://egeria-project.org/concepts/digital-resource-connector/"),

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
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    JDBCAuditCode(String                      messageId,
                  AuditLogRecordSeverityLevel severity,
                  String                      message,
                  String                      systemAction,
                  String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for JDBCAuditCode expects to be passed one of the enumeration rows defined in
     * JDBCAuditCode above.   For example:
     * <br>
     *     JDBCAuditCode   auditCode = JDBCAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    JDBCAuditCode(String                      messageId,
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
        return "JDBCAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
