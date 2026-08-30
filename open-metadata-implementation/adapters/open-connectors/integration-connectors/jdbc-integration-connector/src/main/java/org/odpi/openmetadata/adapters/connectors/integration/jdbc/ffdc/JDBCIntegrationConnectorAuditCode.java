/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The JDBCIntegrationConnectorAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum JDBCIntegrationConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * JDBC-INTEGRATION-CONNECTOR-0001 - Connector {0} is preparing to extract metadata from database {1}
     */
    STARTING_METADATA_TRANSFER("JDBC-INTEGRATION-CONNECTOR-0001",
                               AuditLogRecordSeverityLevel.INFO,
                               "Connector {0} is preparing to extract metadata from database {1}",
                               "The connector is about to connect to the named database to extract details of its schemas tables and columns.",
                               "Check that this is an appropriate database for the connector to be accessing.",
                               "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * JDBC-INTEGRATION-CONNECTOR-0002 - Connector {0} cannot extract metadata for database {1}
     */
    CONNECTION_FAILED("JDBC-INTEGRATION-CONNECTOR-0002",
                               AuditLogRecordSeverityLevel.ERROR,
                               "Connector {0} cannot connect to database {1}; the {2} exception returned a message of {3}",
                               "The connector requested a connection to the database and the exception occurred.",
                               "Check the set up of the open metadata connection attached to the database asset, or directly to this connector.  Are the userId and password correct?  Is the jdbc connection string specified in the endpoint's address correct?  Is the database server set up correctly to receive the connection request?",
                               "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * JDBC-INTEGRATION-CONNECTOR-0003 - Connector {0} has successfully extracted metadata from database {1}
     */
    EXITING_ON_COMPLETE("JDBC-INTEGRATION-CONNECTOR-0003",
                        AuditLogRecordSeverityLevel.INFO,
                        "Connector {0} has successfully extracted metadata from database {1}",
                        "The connector has completed its refresh of this database.",
                        "No user actions are necessary.  The connector will connect again with this database after the next refresh interval.",
                        "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * JDBC-INTEGRATION-CONNECTOR-0016 - The JDBC Integration Connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("JDBC-INTEGRATION-CONNECTOR-0016",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The JDBC Integration Connector {0} received an unexpected {1} exception during method {2} while working with database {3}; the error message was: {4}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/integration-connector/"),

    EXCEPTION_READING_JDBC("JDBC-INTEGRATION-CONNECTOR-0005",
                           AuditLogRecordSeverityLevel.EXCEPTION,
                           "An {0} exception while connecting to database {1}. Exception message is: {2}",
                           "The connector issued a SQL call to the database and the exception occurred.",
                           "Use the message from the exception to guide you in locating the error. ",
                           "https://egeria-project.org/concepts/integration-connector/"),

    EXCEPTION_WRITING_OMAS("JDBC-INTEGRATION-CONNECTOR-0006",
                           AuditLogRecordSeverityLevel.EXCEPTION,
                           "An {0} exception was received by method {1}. Exception message is: {2}",
                           "Upserting an entity into the Metadata Access Server failed.",
                           "Investigate OMAS availability. If it is available then contact the Egeria team for support",
                           "https://egeria-project.org/concepts/integration-connector/"),
    EXITING_ON_DATABASE_TRANSFER_FAIL("JDBC-INTEGRATION-CONNECTOR-0007",
                                      AuditLogRecordSeverityLevel.ERROR,
                                      "Exiting from method {0} as a result of a failed database transfer",
                                      "Stopping execution",
                                      "Consult the logs for the database transfer failure that caused the connector to exit, correct it, and refresh the connector.",
                                      "https://egeria-project.org/concepts/integration-connector/"),
    EXCEPTION_READING_OMAS("JDBC-INTEGRATION-CONNECTOR-0008",
                           AuditLogRecordSeverityLevel.EXCEPTION,
                           "Error reading data from Metadata Access Server in method {0}. Possible message is {1}",
                           "Reading open metadata from the repository.",
                           "Consult the logs for the error returned by the Metadata Access Server, and check that it is running and reachable.",
                           "https://egeria-project.org/concepts/integration-connector/"),

    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS("JDBC-INTEGRATION-CONNECTOR-0010",
                                            AuditLogRecordSeverityLevel.EXCEPTION,
                                            "Unknown error when removing element from Metadata Access Server with guid {0} and qualified name {1}.",
                                            "Removing element in OMAS",
                                            "Consult the logs for the error returned while removing the element, and check whether it is still present in the Metadata Access Server.",
                                            "https://egeria-project.org/concepts/integration-connector/"),

    TRANSFER_COMPLETE_FOR_DB_OBJECT("JDBC-INTEGRATION-CONNECTOR-0012",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "Transfer complete for {0}",
                                    "Continue execution",
                                    "None",
                                    "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * JDBC-INTEGRATION-CONNECTOR-0015 - Connector {0} found {1} elements in the metadata access server with a qualified name of {2}; expecting to find at most one
     */
    MULTIPLE_ELEMENTS_FOUND("JDBC-INTEGRATION-CONNECTOR-0015",
                            AuditLogRecordSeverityLevel.ERROR,
                            "Connector {0} found {1} elements in the metadata access server with a qualified name of {2}; expecting to find at most one",
                            "The connector is unable to determine whether this element already exists, so it is skipping it for this refresh rather than risk creating a duplicate.",
                            "Investigate why more than one element has this qualified name and remove the duplicates.",
                            "https://egeria-project.org/concepts/integration-connector/"),

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
     * @param messageId unique id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    JDBCIntegrationConnectorAuditCode(String                      messageId,
                                      AuditLogRecordSeverityLevel severity,
                                      String                      message,
                                      String                      systemAction,
                                      String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for JDBCIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined above.
     * Example:
     * JDBCIntegrationConnectorAuditCode auditCode = JDBCIntegrationConnectorAuditCode.EXCEPTION_COMMITTING_OFFSETS;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId unique id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    JDBCIntegrationConnectorAuditCode(String                      messageId,
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
    public AuditLogMessageDefinition getMessageDefinition(String... params)
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
