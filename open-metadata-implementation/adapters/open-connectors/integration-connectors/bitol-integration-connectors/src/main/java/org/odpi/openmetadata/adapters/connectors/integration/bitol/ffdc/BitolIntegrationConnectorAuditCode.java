/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The BitolIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum BitolIntegrationConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * BITOL-INTEGRATION-CONNECTOR-0001 - The {0} integration connector is monitoring topic {1} with connection: {2} for Bitol documents
     */
    TOPIC_RECEIVER_CONFIGURATION("BITOL-INTEGRATION-CONNECTOR-0001",
                                 AuditLogRecordSeverityLevel.INFO,
                                 "The {0} integration connector is monitoring topic {1} with connection: {2} for Bitol documents",
                                 "The connector publishes each event received from the topic as a Bitol document to the listeners registered in the same integration daemon.",
                                 "No specific action is required.  This message is to confirm the configuration for the Bitol event receiver integration connector.  It is output for each topic catalog target.",
                                 "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0002 - The {0} integration connector is monitoring directory {1} (from {2}) for Bitol documents
     */
    DIRECTORY_MONITORED("BITOL-INTEGRATION-CONNECTOR-0002",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector is monitoring directory {1} (from {2}) for Bitol documents",
                        "On each refresh the connector scans the directory tree for YAML and JSON files whose kind is DataContract or DataProduct and publishes the new and changed documents to the listeners registered in the same integration daemon.",
                        "No specific action is required.  This message is to confirm the configuration for the Bitol files receiver integration connector.  It is output for the connection's endpoint and for each file folder catalog target.",
                        "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0003 - The {0} integration connector published the {1} document from file {2}
     */
    DOCUMENT_PUBLISHED("BITOL-INTEGRATION-CONNECTOR-0003",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector published the {1} document from file {2}",
                       "The file is new, or has changed since it was last published, so its content has been passed to the Bitol listeners registered in the same integration daemon.",
                       "No specific action is required.  This message records which files have been processed.",
                       "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0004 - The {0} integration connector is unable to read directory {1} (from {2}): {3}
     */
    DIRECTORY_NOT_ACCESSIBLE("BITOL-INTEGRATION-CONNECTOR-0004",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The {0} integration connector is unable to read directory {1} (from {2}): {3}",
                             "The connector skips the directory on this refresh and tries again on the next refresh.",
                             "Check that the directory exists and is readable by the integration daemon's process.  Correct the catalog target or connection endpoint if the path is wrong.",
                             "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0005 - The {0} integration connector stored a {1} document as {2}
     */
    DOCUMENT_STORED("BITOL-INTEGRATION-CONNECTOR-0005",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector stored a {1} document as {2}",
                    "A Bitol document published in the integration daemon has been written to the store.",
                    "No specific action is required.  This message records where documents have been stored.",
                    "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0006 - The {0} integration connector {1} element {2} for {3} document {4} version {5}
     */
    DOCUMENT_CATALOGUED("BITOL-INTEGRATION-CONNECTOR-0006",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector {1} element {2} for {3} document {4} version {5}",
                        "A Bitol document published in the integration daemon has been catalogued in open metadata.  Each version of a document is a separate element, identified by a qualified name that includes the version.",
                        "No specific action is required.  This message records which documents have been catalogued.",
                        "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0007 - The {0} integration connector could not fully represent {1} document {2} version {3}: {4}
     */
    DOCUMENT_MAPPING_WARNING("BITOL-INTEGRATION-CONNECTOR-0007",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} integration connector could not fully represent {1} document {2} version {3}: {4}",
                             "Part of the document refers to something that is not yet known to open metadata, for example a team member without a user identity or a data contract that has not been catalogued.  The rest of the document has been catalogued and the missing detail is recorded in the element's additional properties.",
                             "Catalogue the missing element (for example publish the referenced data contract, or create the user identity and profile) and republish the document to complete the links.",
                             "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0008 - The {0} integration connector skipped a {1} document because it has no identifier; the document begins: {2}
     */
    DOCUMENT_WITHOUT_ID("BITOL-INTEGRATION-CONNECTOR-0008",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} integration connector skipped a {1} document because it has no identifier; the document begins: {2}",
                        "The Bitol standards require every document to carry an id.  Without it the connector can not build stable names for the elements and so the document is not catalogued.",
                        "Add an id (typically a UUID) to the document and publish it again.",
                        "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0009 - The {0} integration connector generated and published the {1} document {2} version {3} from element {4}
     */
    DOCUMENT_GENERATED("BITOL-INTEGRATION-CONNECTOR-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector generated and published the {1} document {2} version {3} from element {4}",
                       "A digital product or data sharing agreement in open metadata has been expressed as a Bitol document and passed to the Bitol listeners registered in the same integration daemon, for example to be stored as a file.",
                       "No specific action is required.  This message records which documents have been generated.",
                       "https://egeria-project.org/concepts/digital-product/"),

    /**
     * BITOL-INTEGRATION-CONNECTOR-0010 - The {0} integration connector received an unexpected {1} exception in method {2} when working with Bitol documents; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("BITOL-INTEGRATION-CONNECTOR-0010",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected {1} exception in method {2} when working with Bitol documents; the error message was: {3}",
                         "The connector cannot process one or more Bitol documents.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/digital-product/"),

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
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    BitolIntegrationConnectorAuditCode(String                      messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                      message,
                                       String                      systemAction,
                                       String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for BitolIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * BitolIntegrationConnectorAuditCode above.   For example:
     * <br><br>
     *     BitolIntegrationConnectorAuditCode   auditCode = BitolIntegrationConnectorAuditCode.DOCUMENT_PUBLISHED;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url - link to a page describing the component or concept
     */
    BitolIntegrationConnectorAuditCode(String                      messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                      message,
                                       String                      systemAction,
                                       String                      userAction,
                                       String                      url)
    {
        this.logMessageId = messageId;
        this.severity     = severity;
        this.logMessage   = message;
        this.systemAction = systemAction;
        this.userAction   = userAction;
        this.url          = url;
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
        return "BitolIntegrationConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
