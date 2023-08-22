/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


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
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The JDBC resource connector for database {0} received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * JDBC-RESOURCE-CONNECTOR-0002 - The JDBC resource connector has connected to database {0}
     */
    CONNECTOR_CONNECTED_TO_DATABASE("JDBC-RESOURCE-CONNECTOR-0002",
                                    OMRSAuditLogRecordSeverity.INFO,
                                    "The JDBC resource connector has connected to database {0}",
                                    "The connector is designed provide a standard interface to a relational database that supports Java Database Connectivity (JDBC).  This message confirms that the connector has successfully connected to the database.  The number of times that this message is emitted by a connector indicates how many database connections it is using.",
                                    "No specific action is required.  This message is to confirm that the configuration of the connector is sufficient to connect to the database."),

    /**
     * JDBC-RESOURCE-CONNECTOR-0002 - The JDBC resource connector has connected to database {0}
     */
    UNEXPECTED_ROW_COUNT_FROM_DATABASE("JDBC-RESOURCE-CONNECTOR-0003",
                                    OMRSAuditLogRecordSeverity.INFO,
                                    "The JDBC resource connector for database {0} has received {1} results from query {2}",
                                    "The connector is designed provide a standard interface to a relational database that supports Java Database Connectivity (JDBC).  This message confirms that the connector has successfully connected to the database.  The number of times that this message is emitted by a connector indicates how many database connections it is using.",
                                    "No specific action is required.  This message is to confirm that the configuration of the connector is sufficient to connect to the database."),

    /**
     * JDBC-RESOURCE-CONNECTOR-0009 - JDBC resource connector is closing all {0} connection(s) to database {1} and is shutting down
     */
    CONNECTOR_STOPPING("JDBC-RESOURCE-CONNECTOR-0009",
                       OMRSAuditLogRecordSeverity.INFO,
                       "The JDBC resource connector for database {0} is closing all {1} connection(s) to database and is shutting down",
                       "The connector has been requested to disconnect from the database and is ensuring all connections are closed.  This message is output by each data source that was created by the connector.  Therefore the number of times that this message is emitted indicates the number of data sources were created by the connector.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),



    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for JDBCAuditCode expects to be passed one of the enumeration rows defined in
     * JDBCAuditCode above.   For example:
     * <br>
     *     JDBCAuditCode   auditCode = JDBCAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    JDBCAuditCode(String                     messageId,
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
        return "JDBCAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
