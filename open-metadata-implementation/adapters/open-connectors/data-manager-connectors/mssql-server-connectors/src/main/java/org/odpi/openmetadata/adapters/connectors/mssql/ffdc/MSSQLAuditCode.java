/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mssql.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The MSSQLAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum MSSQLAuditCode implements AuditLogMessageSet
{
    /**
     * MSSQL-CONNECTOR-0001 - The Microsoft SQL Server connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("MSSQL-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Microsoft SQL Server connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/"),

    /**
     * MSSQL-CONNECTOR-0002 - The {0} survey action service cannot retrieve details of any databases for Microsoft SQL Server {1}
     */
    NO_DATABASES( "MSSQL-CONNECTOR-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} survey action service cannot retrieve details of any databases for Microsoft SQL Server {1} ({2})",
                       "The survey terminates.",
                       "This may not be an error if there are no user database on the database server.  If there are, check the permissions associated with the database userId.",
                       "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/"),

    /**
     * MSSQL-CONNECTOR-0003 - The {0} integration connector has catalogued Microsoft SQL Server Database {1} ({2})
     */
    CATALOGED_DATABASE( "MSSQL-CONNECTOR-0003",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has catalogued Microsoft SQL Server Database {1} ({2})",
                  "The integration connector looks for another database.",
                  "This is an information message showing that the integration connector has found a new database.",
                  "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/"),


    /**
     * MSSQL-CONNECTOR-0004 - The {0} integration connector is skipping Microsoft SQL Server Database {1} ({2}) because it is already catalogued
     */
    SKIPPING_DATABASE( "MSSQL-CONNECTOR-0004",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector is skipping Microsoft SQL Server Database {1} ({2}) because it is already catalogued",
                        "The integration connector continues, looking for another database.",
                        "This is an information message showing that the integration connector is working, but does not need to do any processing on this database.",
                        "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/"),


    /**
     * MSSQL-CONNECTOR-0007 - The {0} Microsoft SQL Server Connector has been supplied with a friendship connector with GUID {1}
     */
    FRIENDSHIP_GUID("MSSQL-CONNECTOR-0007",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} Microsoft SQL Server Connector has been supplied with a friendship connector with GUID {1}",
                    "The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the Microsoft SQL Server with the open metadata ecosystem.",
                    "No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the Microsoft SQL Server.",
                    "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/"),

    /**
     * MSSQL-CONNECTOR-0009 - The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Microsoft SQL Server Database Asset {3} for Database {4}
     */
    NEW_CATALOG_TARGET("MSSQL-CONNECTOR-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Microsoft SQL Server Database Asset {3} for Database {4}",
                       "The connector has requested that its friendship connector starts to catalog a new Microsoft SQL Server Database.",
                       "Verify that the cataloguing starts the next time that the friendship connector refreshes.",
                       "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/"),

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
    MSSQLAuditCode(String                      messageId,
                   AuditLogRecordSeverityLevel severity,
                   String                      message,
                   String                      systemAction,
                   String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * Constructor
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    MSSQLAuditCode(String                      messageId,
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
        return "MSSQLAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
