/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The DcatIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DcatIntegrationConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * DCAT-INTEGRATION-CONNECTOR-0001 - The {0} integration connector is monitoring topic {1} with connection: {2} for DCAT documents
     */
    TOPIC_RECEIVER_CONFIGURATION("DCAT-INTEGRATION-CONNECTOR-0001",
                                 AuditLogRecordSeverityLevel.INFO,
                                 "The {0} integration connector is monitoring topic {1} with connection: {2} for DCAT documents",
                                 "The connector publishes each event received from the topic as a DCAT document to the listeners registered in the same integration daemon.",
                                 "No specific action is required.  This message is to confirm the configuration for the DCAT event receiver integration connector.  It is output for each topic catalog target.",
                                 "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0002 - The {0} integration connector is monitoring directory {1} (from {2}) for DCAT documents
     */
    DIRECTORY_MONITORED("DCAT-INTEGRATION-CONNECTOR-0002",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector is monitoring directory {1} (from {2}) for DCAT documents",
                        "On each refresh the connector scans the directory tree for JSON-LD files that describe DCAT catalogs, datasets, dataset series or data services and publishes the new and changed documents to the listeners registered in the same integration daemon.",
                        "No specific action is required.  This message is to confirm the configuration for the DCAT files receiver integration connector.  It is output for the connection's endpoint and for each file folder catalog target.",
                        "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0003 - The {0} integration connector published the DCAT document from file {1}
     */
    DOCUMENT_PUBLISHED("DCAT-INTEGRATION-CONNECTOR-0003",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector published the DCAT document from file {1}",
                       "The file is new, or has changed since it was last published, so its content has been passed to the DCAT listeners registered in the same integration daemon.",
                       "No specific action is required.  This message records which files have been processed.",
                       "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0004 - The {0} integration connector is unable to use directory {1} (from {2}): {3}
     */
    DIRECTORY_NOT_ACCESSIBLE("DCAT-INTEGRATION-CONNECTOR-0004",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The {0} integration connector is unable to use directory {1} (from {2}): {3}",
                             "The connector skips the directory on this refresh and tries again on the next refresh.",
                             "Check that the directory exists and that the integration daemon's process has the access it needs.  Correct the catalog target or connection endpoint if the path is wrong.",
                             "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0005 - The {0} integration connector {1} element {2} ({3}) for DCAT {4} {5}
     */
    RESOURCE_CATALOGUED("DCAT-INTEGRATION-CONNECTOR-0005",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector {1} element {2} ({3}) for DCAT {4} {5}",
                        "A resource described in a DCAT document has been catalogued in open metadata.  A dataset is catalogued as a data set asset with an asset for each distribution, a data service as a deployed API and a catalog as a collection folder.",
                        "No specific action is required.  Use the element's unique identifier to retrieve it from open metadata.  Add it to the DCAT Publishing Catalog collection if it is to be published by this deployment.",
                        "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0006 - The {0} integration connector was not able to catalog everything in a DCAT document: {1}
     */
    DOCUMENT_MAPPING_WARNING("DCAT-INTEGRATION-CONNECTOR-0006",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The {0} integration connector was not able to catalog everything in a DCAT document: {1}",
                             "The connector has catalogued the rest of the document.",
                             "Use the message to locate the part of the document that needs to be corrected, then send the document again.  The elements that were catalogued are updated rather than duplicated.",
                             "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0007 - The {0} integration connector is unable to find a collection called {1}, which is named in its {2} configuration property
     */
    PUBLISHING_CATALOG_NOT_FOUND("DCAT-INTEGRATION-CONNECTOR-0007",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "The {0} integration connector is unable to find a collection called {1}, which is named in its {2} configuration property",
                                 "No DCAT descriptions are published.  The connector looks for the collection again on its next refresh.",
                                 "Either load the content pack that defines the collection (the default collection is supplied by the Core Content Pack), or set the configuration property to the qualified name or display name of the collection whose member assets are to be published.",
                                 "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0008 - The {0} integration connector has no directory to publish DCAT descriptions to
     */
    NO_DESTINATION("DCAT-INTEGRATION-CONNECTOR-0008",
                   AuditLogRecordSeverityLevel.ERROR,
                   "The {0} integration connector has no directory to publish DCAT descriptions to",
                   "No DCAT descriptions are published.  The connector looks for its destination directories again on its next refresh.",
                   "Either set the network address of the endpoint in the connector's connection to the name of the destination directory, or attach one or more file folders to the connector as catalog targets.",
                   "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0009 - The {0} integration connector published the DCAT description of {1} {2} ({3}) to file {4}
     */
    RESOURCE_STORED("DCAT-INTEGRATION-CONNECTOR-0009",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector published the DCAT description of {1} {2} ({3}) to file {4}",
                    "The asset is a member of the publishing catalog and its DCAT description is new, or has changed since it was last published, so the file has been written.",
                    "No specific action is required.  This message records what has been published and where.",
                    "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0010 - The {0} integration connector published the DCAT catalog for collection {1} to file {2}; it describes {3} dataset(s) and {4} data service(s)
     */
    CATALOG_STORED("DCAT-INTEGRATION-CONNECTOR-0010",
                   AuditLogRecordSeverityLevel.INFO,
                   "The {0} integration connector published the DCAT catalog for collection {1} to file {2}; it describes {3} dataset(s) and {4} data service(s)",
                   "The catalog file describes the collection as a DCAT catalog, along with every asset that is a member of the collection or of the collection folders nested in it.  It has been written because its content has changed.",
                   "No specific action is required.  The catalog file is the one to give to a portal that harvests DCAT catalogs.",
                   "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0011 - The {0} integration connector removed file {1} because the asset it describes is no longer in the publishing catalog
     */
    RESOURCE_WITHDRAWN("DCAT-INTEGRATION-CONNECTOR-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector removed file {1} because the asset it describes is no longer in the publishing catalog",
                       "The file was written by an earlier refresh for an asset that has since been removed from the publishing catalog, or deleted, or renamed.",
                       "No specific action is required.  Add the asset to the publishing catalog again if it was removed by mistake.",
                       "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0012 - The {0} integration connector was not able to generate the DCAT description of element {1}; the {2} exception had message: {3}
     */
    RESOURCE_NOT_PUBLISHED("DCAT-INTEGRATION-CONNECTOR-0012",
                           AuditLogRecordSeverityLevel.ERROR,
                           "The {0} integration connector was not able to generate the DCAT description of element {1}; the {2} exception had message: {3}",
                           "The element is left out of the published catalog.  The connector carries on with the other members of the publishing catalog and tries this one again on its next refresh.",
                           "Use the message from the exception to determine what is wrong with the element.  A common cause is that the connector's userId is not permitted to read the element or something attached to it.",
                           "https://egeria-project.org/concepts/asset/"),

    /**
     * DCAT-INTEGRATION-CONNECTOR-0013 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("DCAT-INTEGRATION-CONNECTOR-0013",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to complete the request it was processing.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/asset/"),

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
    DcatIntegrationConnectorAuditCode(String                      messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                      message,
                                       String                      systemAction,
                                       String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for DcatIntegrationConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * DcatIntegrationConnectorAuditCode above.   For example:
     * <br><br>
     *     DcatIntegrationConnectorAuditCode   auditCode = DcatIntegrationConnectorAuditCode.DOCUMENT_PUBLISHED;
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
    DcatIntegrationConnectorAuditCode(String                      messageId,
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
        return "DcatIntegrationConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
