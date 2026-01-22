/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The PostgresAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum PostgresAuditCode implements AuditLogMessageSet
{
    /**
     * POSTGRES-CONNECTOR-0001 - The PostgreSQL connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("POSTGRES-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The PostgreSQL connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * POSTGRES-CONNECTOR-0002 - The {0} survey action service cannot retrieve details of any databases for PostgreSQL Database Server {1}
     */
    NO_DATABASES( "POSTGRES-CONNECTOR-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} survey action service cannot retrieve details of any databases for PostgreSQL Database Server {1} ({2})",
                       "The survey terminates.",
                       "This may not be an error if there are no user database on the database server.  If there are, check the permissions associated with the database userId."),

    /**
     * POSTGRES-CONNECTOR-0003 - The {0} integration connector has catalogued PostgreSQL Database {1} ({2})
     */
    CATALOGED_DATABASE( "POSTGRES-CONNECTOR-0003",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has catalogued PostgreSQL Database {1} ({2})",
                  "The integration connector looks for another database.",
                  "This is an information message showing that the integration connector has found a new database."),


    /**
     * POSTGRES-CONNECTOR-0004 - he {0} integration connector is skipping PostgreSQL Database {1} ({2}) because it is already catalogued
     */
    SKIPPING_DATABASE( "POSTGRES-CONNECTOR-0004",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector is skipping PostgreSQL Database {1} ({2}) because it is already catalogued",
                        "The integration connector continues, looking for another database.",
                        "This is an information message showing that the integration connector is working, but does not need to do any processing on this database."),


    /**
     * POSTGRES-CONNECTOR-0007 - The {0} PostgreSQL Server Connector has been supplied with a friendship connector with GUID {1}
     */
    FRIENDSHIP_GUID("POSTGRES-CONNECTOR-0007",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} PostgreSQL Server Connector has been supplied with a friendship connector with GUID {1}",
                    "The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the PostgreSQL Server with the open metadata ecosystem.",
                    "No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the PostgreSQL Server."),

    /**
     * POSTGRES-CONNECTOR-0009 - The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Unity Catalog Server Asset {3} for Unity Catalog (UC) {4}
     */
    NEW_CATALOG_TARGET("POSTGRES-CONNECTOR-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to PostgreSQL Database Asset {3} for Database {4}",
                       "The connector has requested that its friendship connector starts to catalog a new PostgreSQL Database.",
                       "Verify that the cataloguing starts the next time that the friendship connector refreshes."),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * Constructor
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    PostgresAuditCode(String                      messageId,
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
        return "PostgresAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
