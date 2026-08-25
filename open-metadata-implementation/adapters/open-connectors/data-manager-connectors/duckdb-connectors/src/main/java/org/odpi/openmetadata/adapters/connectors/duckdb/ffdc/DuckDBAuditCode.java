/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.duckdb.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The DuckDBAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DuckDBAuditCode implements AuditLogMessageSet
{
    /**
     * DUCKDB-CONNECTOR-0001 - The DuckDB connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("DUCKDB-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The DuckDB connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0002 - The {0} integration connector has catalogued DuckDB Database {1} ({2})
     */
    CATALOGED_DATABASE( "DUCKDB-CONNECTOR-0002",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has catalogued DuckDB Database {1} ({2})",
                  "The integration connector continues to catalog the contents of the DuckDB database.",
                  "This is an information message showing that the integration connector has found a new DuckDB database.",
                  "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0003 - The {0} integration connector is skipping DuckDB Database {1} ({2}) because it is already catalogued
     */
    SKIPPING_DATABASE( "DUCKDB-CONNECTOR-0003",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector is skipping DuckDB Database {1} ({2}) because it is already catalogued",
                        "The integration connector continues, using the existing catalogued database.",
                        "This is an information message showing that the integration connector is working, but does not need to create a new asset for this database.",
                        "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0004 - The {0} DuckDB Connector has been supplied with a friendship connector with GUID {1}
     */
    FRIENDSHIP_GUID("DUCKDB-CONNECTOR-0004",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} DuckDB Connector has been supplied with a friendship connector with GUID {1}",
                    "The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the DuckDB database with the open metadata ecosystem.",
                    "No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the DuckDB database.",
                    "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0005 - The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to DuckDB Database Asset {3} for Database {4}
     */
    NEW_CATALOG_TARGET("DUCKDB-CONNECTOR-0005",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to DuckDB Database Asset {3} for Database {4}",
                       "The connector has requested that its friendship connector starts to catalog the schemas, tables and columns of the DuckDB Database.",
                       "Verify that the cataloguing starts the next time that the friendship connector refreshes.",
                       "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0006 - The {0} connector discovered that DuckDB Database {1} has an attached source called {2} of type {3}
     */
    ATTACHED_SOURCE_FOUND("DUCKDB-CONNECTOR-0006",
                          AuditLogRecordSeverityLevel.INFO,
                          "The {0} connector discovered that DuckDB Database {1} has an attached source called {2} of type {3}",
                          "The connector records this as a federation relationship for the DuckDB database.",
                          "No specific action is required.  This is an information message describing a database that has been ATTACH-ed to the surveyed/catalogued DuckDB database.",
                          "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0007 - The {0} connector discovered that DuckDB Database {1} has a view called {2} that scans an external {3} resource at {4}
     */
    EXTERNAL_FILE_SOURCE_FOUND("DUCKDB-CONNECTOR-0007",
                               AuditLogRecordSeverityLevel.INFO,
                               "The {0} connector discovered that DuckDB Database {1} has a view called {2} that scans an external {3} resource at {4}",
                               "The connector records this as a federation relationship for the DuckDB database.",
                               "No specific action is required.  This is an information message describing an external file, or object-store, resource that is scanned by a view in the surveyed/catalogued DuckDB database.",
                               "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0008 - The {0} connector was unable to query DuckDB's {1} federation metadata for database {2}; the error message was: {3}
     */
    FEDERATION_QUERY_FAILED("DUCKDB-CONNECTOR-0008",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} connector was unable to query DuckDB's {1} federation metadata for database {2}; the error message was: {3}",
                            "The connector skips federation discovery processing for this database and continues with the rest of its processing.",
                            "This may not be an error - it is expected for a version of DuckDB that does not support this table function, or for a database that does not use DuckDB's federation capabilities.  If federation metadata is expected, verify that the DuckDB version in use supports it.",
                            "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0009 - The {0} connector was unable to catalog the federation relationship for {1} discovered in DuckDB Database {2}; the error message was: {3}
     */
    FEDERATION_LINK_FAILED("DUCKDB-CONNECTOR-0009",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} connector was unable to catalog the federation relationship for {1} discovered in DuckDB Database {2}; the error message was: {3}",
                           "The connector skips this particular federation relationship and continues cataloguing the remaining federation relationships and the rest of the DuckDB database.",
                           "Review the error message to determine whether any configuration change is needed to catalog this federation relationship on a subsequent refresh.",
                           "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0010 - The {0} connector was unable to run ATTACH_STATEMENTS entry "{1}" for DuckDB Database {2}; the error message was: {3}
     */
    ATTACH_STATEMENT_FAILED("DUCKDB-CONNECTOR-0010",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The {0} connector was unable to run attachStatements entry \"{1}\" for DuckDB Database {2}; the error message was: {3}",
                            "The connector skips this attach statement and continues to run any remaining attach statements, then continues with the rest of its processing.  Since this statement did not run, any federation relationship it was meant to (re)establish will not be found by federation discovery on this connection.",
                            "Review the error message - typically this means the statement's syntax is invalid, a required DuckDB extension has not been installed, or the credentials/network address in the statement are no longer correct.  Correct the attachStatements configuration property and try again.",
                            "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0011 - The {0} connector has catalogued the schema for external file source {1} ({2} columns)
     */
    FILE_SCHEMA_CATALOGED("DUCKDB-CONNECTOR-0011",
                          AuditLogRecordSeverityLevel.INFO,
                          "The {0} connector has catalogued the schema for external file source {1} ({2} columns)",
                          "The connector records the real column structure of the external file as a TabularSchemaType attached to the file asset, so that lineage can be traced back to it from the DuckDB view that scans it.",
                          "No specific action is required.  This is an information message confirming that the schema of an externally-scanned file has been catalogued.",
                          "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0012 - The {0} connector could not find a DuckDB-side RelationalTable for view {1} (tried qualified names {2} and {3}); classification and lineage linking for this view will be attempted again on a subsequent refresh
     */
    DUCKDB_TABLE_NOT_YET_CATALOGUED("DUCKDB-CONNECTOR-0012",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "The {0} connector could not find a DuckDB-side RelationalTable for view {1} (tried qualified names {2} and {3}); classification and lineage linking for this view will be attempted again on a subsequent refresh",
                                    "The connector skips the CalculatedValue classification and DerivedSchemaTypeQueryTarget lineage linking for this view on this refresh, but the underlying file asset and its schema have still been catalogued.",
                                    "This is expected on an early refresh, before the friendship connector (the generic JDBC integration connector) has had a chance to catalog this DuckDB database's own tables and views.  No action is required unless this message persists across multiple refreshes.",
                                    "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0013 - The {0} connector has catalogued attached table {1} from data source {2} as a TabularDataSet ({3} columns)
     */
    ATTACHED_TABLE_CATALOGED("DUCKDB-CONNECTOR-0013",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} connector has catalogued attached table {1} from data source {2} as a TabularDataSet ({3} columns)",
                             "The connector records the table's structure as a TabularDataSet asset, linked to the target RelationalDatabase asset via DataSetContent, so that the table is separately queryable/governable from open metadata even though it is not catalogued by the friendship connector.",
                             "No specific action is required.  This is an information message describing a table made available through a DuckDB ATTACH-ed network-backed data source.",
                             "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

    /**
     * DUCKDB-CONNECTOR-0014 - The {0} connector found {1} elements with qualified name {2} when only one was expected; the element is skipped for this refresh
     */
    AMBIGUOUS_ELEMENT_FOUND("DUCKDB-CONNECTOR-0014",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The {0} connector found {1} elements with qualified name {2} when only one was expected; the element is skipped for this refresh",
                            "The connector cannot safely determine which of the matching elements to use, so it skips creating or updating this element for the current refresh.",
                            "This should not normally happen - qualified names are expected to be unique.  Investigate how the duplicate elements were created and consider removing the redundant one(s).",
                            "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/"),

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
    DuckDBAuditCode(String                      messageId,
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
    DuckDBAuditCode(String                      messageId,
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
        return "DuckDBAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
