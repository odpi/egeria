/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The DarwinAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data for the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum DarwinAuditCode implements AuditLogMessageSet
{
    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to complete the maintenance of the digital product dependencies.  The dependencies " +
                                 "are left as they were at the point of failure and are reconciled again on the next refresh.",
                         "Use the details from the error message to determine the cause of the error and correct it.",
                         "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0002 - The {0} integration connector is starting to maintain the digital product dependencies in
     * server {1} on platform {2}; lineage paths are followed for up to {3} steps
     */
    STARTING_CONNECTOR("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting to maintain the digital product dependencies in server {1} on platform {2}; " +
                               "lineage paths are followed for up to {3} steps",
                       "On each refresh, the connector works upwards from the finest-grained lineage: it derives data flows between " +
                               "data assets from the data mappings between their schema elements, data flows between software servers " +
                               "from the lineage between the data assets their capabilities own, and dependencies between digital " +
                               "products from the data lineage between the assets that are members of the products.  At each level it " +
                               "creates the relationships that are missing, removes the ones it created earlier that the finer-grained " +
                               "lineage no longer supports, and fills in the information supply chain on relationships asserted by " +
                               "external users.  It also records an exception against each product whose externally asserted " +
                               "dependencies are not proven by lineage.",
                       "No action is required.  This message is for monitoring the start up of the product dependency manager.",
                       "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0003 - The {0} integration connector has created the {1} exception type ({2}) to record the
     * digital products whose externally asserted dependencies are not proven by lineage
     */
    NEW_EXCEPTION_TYPE("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0003",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has created the {1} exception type ({2}) to record the digital products whose " +
                               "externally asserted dependencies are not proven by lineage",
                       "The exception type is created because there was no existing exception type with this name.  All of the " +
                               "exceptions raised by this connector are linked to it.",
                       "No action is required.  Link the exception type to the appropriate governance policy if the organization " +
                               "wants the exceptions it records to be reviewed as part of the governance program.",
                       "https://egeria-project.org/types/4/0455-Exception-Management/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0004 - The {0} integration connector has recorded that digital product {1} depends on digital
     * product {2} through information supply chain {3} (relationship {4})
     */
    DEPENDENCY_CREATED("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0004",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has recorded that digital product {1} depends on digital product {2} through " +
                               "information supply chain {3} (relationship {4})",
                       "The data lineage shows that data flows from an asset that is a member of the second product to an asset that " +
                               "is a member of the first, and there was no DigitalProductDependency relationship recording this for the " +
                               "information supply chain, so the connector has created one.",
                       "No action is required.  The relationship is removed automatically if the lineage stops supporting it.",
                       "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0005 - The {0} integration connector has removed the dependency ({1}) of digital product {2} on
     * digital product {3} through information supply chain {4} because the lineage no longer supports it
     */
    DEPENDENCY_REMOVED("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0005",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has removed the dependency ({1}) of digital product {2} on digital product {3} " +
                               "through information supply chain {4} because the lineage no longer supports it",
                       "The connector created this relationship on an earlier refresh from the data lineage between the products' " +
                               "assets.  That lineage is no longer there - or it is now recorded by a relationship asserted by an " +
                               "external user - so the connector's own relationship is removed.  Relationships asserted by external " +
                               "users are never removed.",
                       "No action is required.  If the dependency is real but the lineage is missing, correct the lineage and the " +
                               "relationship is recreated on the next refresh.",
                       "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0006 - The {0} integration connector has set the information supply chain of the dependency ({1})
     * of digital product {2} on digital product {3}, asserted by {4}, to {5}
     */
    SUPPLY_CHAIN_SET("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0006",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector has set the information supply chain of the dependency ({1}) of digital product {2} " +
                             "on digital product {3}, asserted by {4}, to {5}",
                     "A DigitalProductDependency relationship asserted by an external user did not name an information supply chain.  " +
                             "The data lineage between the products' assets proves the dependency, so the connector has filled in the " +
                             "information supply chain from the first lineage path it found.",
                     "No action is required.  If the dependency belongs to a different information supply chain, correct the " +
                             "relationship; the connector does not change an information supply chain once it is set.",
                     "https://egeria-project.org/concepts/information-supply-chain/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0007 - The {0} integration connector has recorded an exception ({1}) against digital product {2}
     * because {3} of its dependencies asserted by external users are not proven by lineage: {4}
     */
    UNPROVEN_DEPENDENCIES("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0007",
                          AuditLogRecordSeverityLevel.ACTION,
                          "The {0} integration connector has recorded an exception ({1}) against digital product {2} because {3} of its " +
                                  "dependencies asserted by external users are not proven by lineage: {4}",
                          "The listed DigitalProductDependency relationships were asserted by external users, but there is no data " +
                                  "lineage path between the assets of the two products for the information supply chain named in the " +
                                  "relationship.  The connector does not remove relationships it did not create, so it records the " +
                                  "discrepancy as an Exception relationship linking the dependent product to its exception type.  The " +
                                  "affectedRelationships property of the exception lists the unproven relationships.",
                          "Review the listed relationships.  Either add the missing lineage, correct the information supply chain " +
                                  "named on the relationship, or remove the relationship if the dependency does not exist.  The " +
                                  "exception is cleared automatically once none of the product's externally asserted dependencies " +
                                  "are unproven.",
                          "https://egeria-project.org/types/4/0455-Exception-Management/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0008 - The {0} integration connector has cleared the exception ({1}) against digital product {2}
     * because all of its dependencies asserted by external users are now proven by lineage
     */
    EXCEPTION_CLEARED("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0008",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector has cleared the exception ({1}) against digital product {2} because all of its " +
                              "dependencies asserted by external users are now proven by lineage",
                      "The unproven dependencies that the exception recorded have either been removed or are now supported by the " +
                              "data lineage between the products' assets, so the Exception relationship is deleted.",
                      "No action is required.  This message is for monitoring the resolution of exceptions.",
                      "https://egeria-project.org/types/4/0455-Exception-Management/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0009 - The {0} integration connector stopped following {1} lineage paths because they exceeded
     * the limit of {2} steps
     */
    LINEAGE_DEPTH_EXCEEDED("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0009",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} integration connector stopped following {1} lineage paths because they exceeded the limit of {2} steps",
                           "The connector follows the data lineage downstream from each asset that is a member of a digital product, " +
                                   "or is owned by a software server's capability, until it reaches an asset that belongs to another " +
                                   "product or server.  A path that has not reached one within the configured number of steps is " +
                                   "abandoned, so a relationship at the end of a very long path is not recorded.",
                           "If the lineage graph legitimately contains long paths between products or servers, increase the maxLineageDepth " +
                                   "configuration property of the connector.",
                           "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0010 - The {0} integration connector reviewed {1} digital products and derived {2} dependencies
     * from their lineage; it created {3} relationships, removed {4}, set the information supply chain on {5} and found {6} unproven; it
     * raised {7} exceptions, updated {8} and cleared {9}
     */
    REFRESH_COMPLETE("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0010",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector reviewed {1} digital products and derived {2} dependencies from their lineage; it " +
                             "created {3} relationships, removed {4}, set the information supply chain on {5} and found {6} unproven; " +
                             "it raised {7} exceptions, updated {8} and cleared {9}",
                     "The refresh has completed and the DigitalProductDependency relationships are consistent with the data lineage " +
                             "between the products' assets.",
                     "No action is required.  This message is for monitoring the operation of the product dependency manager.",
                     "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0011 - The {0} integration connector has stopped maintaining the digital product dependencies in
     * server {1} on platform {2}
     */
    CONNECTOR_STOPPING("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped maintaining the digital product dependencies in server {1} on platform {2}",
                       "The connector is shutting down.  The dependencies are left as they are.",
                       "No action is required.  This message is for monitoring the shutdown of the product dependency manager.",
                       "https://egeria-project.org/concepts/digital-product/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0012 - The {0} integration connector has recorded that data flows from {1} to {2} through
     * information supply chain {3} (relationship {4}) because {5}
     */
    LINEAGE_CREATED("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0012",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector has recorded that data flows from {1} to {2} through information supply chain {3} " +
                            "(relationship {4}) because {5}",
                    "Finer-grained lineage shows that data flows between the two elements, and there was no DataFlow relationship " +
                            "recording this for the information supply chain, so the connector has created one.  This is how lineage " +
                            "bubbles up from the detailed level to the coarse-grained: data mappings between schema elements imply a " +
                            "data flow between their assets, and lineage between the assets owned by software servers' capabilities " +
                            "implies a data flow between the servers.",
                    "No action is required.  The relationship is removed automatically if the finer-grained lineage stops supporting it.",
                    "https://egeria-project.org/features/lineage-management/overview/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0013 - The {0} integration connector has removed the data flow ({1}) from {2} to {3} through
     * information supply chain {4} because the finer-grained lineage no longer supports it
     */
    LINEAGE_REMOVED("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0013",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector has removed the data flow ({1}) from {2} to {3} through information supply chain {4} " +
                            "because the finer-grained lineage no longer supports it",
                    "The connector created this DataFlow relationship on an earlier refresh from the finer-grained lineage between the " +
                            "two elements.  That lineage is no longer there - or the data flow is now recorded by a relationship asserted " +
                            "by an external user - so the connector's own relationship is removed.  Relationships asserted by external " +
                            "users are never removed.",
                    "No action is required.  If the data flow is real but the finer-grained lineage is missing, correct the lineage " +
                            "and the relationship is recreated on the next refresh.",
                    "https://egeria-project.org/features/lineage-management/overview/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0014 - The {0} integration connector has set the information supply chain of the data flow ({1})
     * from {2} to {3}, asserted by {4}, to {5}
     */
    LINEAGE_SUPPLY_CHAIN_SET("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0014",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} integration connector has set the information supply chain of the data flow ({1}) from {2} to {3}, " +
                                     "asserted by {4}, to {5}",
                             "A DataFlow relationship asserted by an external user did not name an information supply chain.  The " +
                                     "finer-grained lineage between the two elements proves the data flow, so the connector has filled " +
                                     "in the information supply chain from the first lineage that does.",
                             "No action is required.  If the data flow belongs to a different information supply chain, correct the " +
                                     "relationship; the connector does not change an information supply chain once it is set.",
                             "https://egeria-project.org/concepts/information-supply-chain/"),

    /**
     * DARWIN-PRODUCT-DEPENDENCY-MANAGER-0015 - The {0} integration connector derived {1} data flows between data assets from the data
     * mappings between their schema elements, and {2} data flows between software servers from the lineage between the data assets
     * their capabilities own; it created {3} DataFlow relationships, removed {4} and set the information supply chain on {5}
     */
    LINEAGE_REFRESH_COMPLETE("DARWIN-PRODUCT-DEPENDENCY-MANAGER-0015",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} integration connector derived {1} data flows between data assets from the data mappings between their " +
                                     "schema elements, and {2} data flows between software servers from the lineage between the data assets " +
                                     "their capabilities own; it created {3} DataFlow relationships, removed {4} and set the information " +
                                     "supply chain on {5}",
                             "The coarse-grained lineage is consistent with the finer-grained lineage beneath it.  The digital product " +
                                     "dependencies are derived next, from the asset-level lineage that this step has just completed.",
                             "No action is required.  This message is for monitoring the derivation of the coarse-grained lineage.",
                             "https://egeria-project.org/features/lineage-management/overview/"),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * The constructor for DarwinAuditCode expects to be passed one of the enumeration rows defined in
     * DarwinAuditCode above.   For example:
     * <br>
     *     DarwinAuditCode   auditCode = DarwinAuditCode.STARTING_CONNECTOR;
     * <br>
     * This will expand out to the 6 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    DarwinAuditCode(String                      messageId,
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
        return "DarwinAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
