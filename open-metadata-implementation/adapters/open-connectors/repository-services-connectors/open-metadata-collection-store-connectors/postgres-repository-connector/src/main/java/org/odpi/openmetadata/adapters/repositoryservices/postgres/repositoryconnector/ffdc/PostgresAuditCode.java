/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The PostgresAuditCode is used to define the message content for the Audit Log.
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
public enum PostgresAuditCode implements AuditLogMessageSet 
{
    /**
     * POSTGRES-REPOSITORY-CONNECTOR-0001 - The PostgreSQL repository connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("POSTGRES-REPOSITORY-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The PostgreSQL repository connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/repository-connector/"),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-0002 - The PostgreSQL repository connector {0} is connecting to database {1}
     */
    STARTING_REPOSITORY("POSTGRES-REPOSITORY-CONNECTOR-0002",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The PostgreSQL repository connector {0} is connecting to database {1}",
                         "The connector is testing its connection to the database.",
                         "Check for errors following this message.",
                         "https://egeria-project.org/concepts/repository-connector/"),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-0003 - The PostgreSQL repository connector {0} is validating the schema definitions for schema {1}
     */
    CONFIRMING_REPOSITORY_SCHEMA("POSTGRES-REPOSITORY-CONNECTOR-0003",
                                 AuditLogRecordSeverityLevel.STARTUP,
                                 "The PostgreSQL repository connector {0} is validating the schema definitions for schema {1}",
                                 "The connector is testing the table and column definitions for the database schema.  If they are missing, they are created automatically.",
                                 "Check for errors in configuring the schema.",
                                 "https://egeria-project.org/concepts/repository-connector/"),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-0007 - The PostgreSQL repository connector {0} has is using a default asOfTime for queries of: {1}
     */
    DEFAULT_AS_OF_TIME("POSTGRES-REPOSITORY-CONNECTOR-0007",
                      AuditLogRecordSeverityLevel.STARTUP,
                      "The PostgreSQL repository connector {0} has is using a default asOfTime for queries of: {1}",
                      "All queries that do not explicitly specify an asOfTime will use this value.  A value of null means it will use the current time.  This value is changed using the 'defaultAsOfTime' configuration property.",
                      "Check that this is the intended value.  Typically it is only changed from its default value of null for audits that are focused on a particular moment in time.",
                      "https://egeria-project.org/concepts/repository-connector/"),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-0008 - The PostgreSQL repository connector {0} is using a repository mode of: {1}
     */
    REPOSITORY_MODE("POSTGRES-REPOSITORY-CONNECTOR-0008",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The PostgreSQL repository connector {0} is using a repository mode of: {1}",
                       "The repository mode is used to switch the repository into a read-only mode.  The default mode is read-write.  This value is changed using the 'repositoryMode' configuration property.  If it is set to 'readOnly' then repositoryMode=read-only; if it is set to anything else (or not set) then repositoryMode=read-write.",
                       "Check that this is the intended value.  Typically it is only changed from its default value of read-write for situations where you do not want any changes to be made to the metadata in the repository.",
                       "https://egeria-project.org/concepts/repository-connector/"),

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
     * @param messageId    - unique id for the message
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    PostgresAuditCode(String                      messageId,
                      AuditLogRecordSeverityLevel severity,
                      String                      message,
                      String                      systemAction,
                      String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for POSTGRESAuditCode expects to be passed one of the enumeration rows defined in
     * POSTGRESAuditCode above.   For example:
     * <p>
     * POSTGRESAuditCode   auditCode = POSTGRESAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    PostgresAuditCode(String                      messageId,
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
