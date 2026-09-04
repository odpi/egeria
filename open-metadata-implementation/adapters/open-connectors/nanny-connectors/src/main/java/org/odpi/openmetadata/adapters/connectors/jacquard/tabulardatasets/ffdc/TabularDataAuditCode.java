/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The TabularDataAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum TabularDataAuditCode implements AuditLogMessageSet
{
    /**
     * The {0} Tabular Metadata Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("TABULAR-METADATA-CONNECTORS-0001",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The {0} Tabular Metadata Connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot connector the the OMAG Infrastructure.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/tabular-data-set-connector/"),

    /**
     * TABULAR-METADATA-CONNECTORS-0002 - The {0} connector found {1} tabular data set(s) in digital product family {2} ({3})
     */
    FAMILY_MEMBERS_LOADED("TABULAR-METADATA-CONNECTORS-0002",
                          AuditLogRecordSeverityLevel.INFO,
                          "The {0} connector found {1} tabular data set(s) in digital product family {2} ({3})",
                          "The connector walked the family's members and found this many products with a readable tabular data set.  These are the tables it presents.",
                          "No action is required.  If a product in the family is missing from the count, check that it has an asset with a connection to a readable tabular data source.",
                          "https://egeria-project.org/concepts/tabular-data-set-connector/"),

    /**
     * TABULAR-METADATA-CONNECTORS-0003 - The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector {4} is not a readable tabular data source
     */
    FAMILY_MEMBER_NOT_TABULAR("TABULAR-METADATA-CONNECTORS-0003",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector {4} is not a readable tabular data source",
                              "The product is left out of the collection.  The other products in the family are still presented.",
                              "No action is required unless the product's data should be delivered with the family, in which case give its asset a connection to a connector that implements ReadableTabularDataSource.",
                              "https://egeria-project.org/concepts/tabular-data-set-connector/"),

    /**
     * TABULAR-METADATA-CONNECTORS-0004 - The {0} connector found that asset {1} of product {2} in digital product family {3} is a copy of asset {4} - both carry qualified name {5}; the copies have been linked as peer duplicates for the duplicate manager and only the first is presented
     */
    FAMILY_MEMBER_DUPLICATE_TABLE("TABULAR-METADATA-CONNECTORS-0004",
                                  AuditLogRecordSeverityLevel.ERROR,
                                  "The {0} connector found that asset {1} of product {2} in digital product family {3} is a copy of asset {4} - both carry qualified name {5}; the copies have been linked as peer duplicates for the duplicate manager and only the first is presented",
                                  "Two writers created the same product at the same time.  The connector links the two assets with a PeerDuplicateLink in DISCOVERED status, which is what the Mendel Automated Duplicate Manager works from to confirm and consolidate duplicates, and presents the first copy so that the family's data is still delivered.",
                                  "No action is required if the duplicate manager is deployed.  If it is not, a steward should review the peer duplicate link and remove or consolidate the copies.",
                                  "https://egeria-project.org/concepts/tabular-data-set-connector/"),

    /**
     * TABULAR-METADATA-CONNECTORS-0006 - The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its table name {4} is already used by asset {5} of a different product
     */
    FAMILY_MEMBER_TABLE_NAME_CLASH("TABULAR-METADATA-CONNECTORS-0006",
                                   AuditLogRecordSeverityLevel.ERROR,
                                   "The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its table name {4} is already used by asset {5} of a different product",
                                   "Two distinct products in the family give their data sets the same table name, and two tables of the same name cannot both be delivered into one destination.  The product found second is left out.",
                                   "Give the products in the family distinct table names in their data specifications.",
                                   "https://egeria-project.org/concepts/tabular-data-set-connector/"),

    /**
     * TABULAR-METADATA-CONNECTORS-0005 - The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector could not be built: {4} exception with message {5}
     */
    FAMILY_MEMBER_UNREADABLE("TABULAR-METADATA-CONNECTORS-0005",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                             "The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector could not be built: {4} exception with message {5}",
                             "The product is left out of the collection.  The other products in the family are still presented.",
                             "Use the details from the error message to correct the product asset's connection, then refresh the connector.",
                             "https://egeria-project.org/concepts/tabular-data-set-connector/"),

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
    TabularDataAuditCode(String                      messageId,
                         AuditLogRecordSeverityLevel severity,
                         String                      message,
                         String                      systemAction,
                         String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for TabularDataAuditCode expects to be passed one of the enumeration rows defined in
     * TabularDataAuditCode above.   For example:
     * <br>
     *     TabularDataAuditCode   auditCode = TabularDataAuditCode.UNEXPECTED_EXCEPTION;
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
    TabularDataAuditCode(String                      messageId,
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
        return "TabularDataAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
