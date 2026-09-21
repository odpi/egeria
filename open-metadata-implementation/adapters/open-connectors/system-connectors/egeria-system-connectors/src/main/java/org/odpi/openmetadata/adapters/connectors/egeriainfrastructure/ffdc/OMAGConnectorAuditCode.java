/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OMAGConnectorAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OMAGConnectorAuditCode implements AuditLogMessageSet
{
    /**
     * The {0} Egeria Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("OMAG-CONNECTORS-0001",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The {0} Egeria Connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot connector the the OMAG Infrastructure.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector has been started and will call the platforms with userId {1}.  The monitored platforms are: {2}
     */
    EGERIA_CONNECTOR_START("OMAG-CONNECTORS-0002",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} Egeria Connector has been started.  The monitored platforms are: {1}",
                           "The connector is designed to catalog details of Software Server Platforms that have the deployedImplementationType property set to 'OMAG Server Platform'.",
                           "No specific action is required.  This message is to confirm the start of the integration connector.",
                           "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}
     */
    NEW_SERVER("OMAG-CONNECTORS-0005",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}",
                       "The connector is has catalogued a new server.",
                       "No action is required unless there are errors that follow indicating that there were problems with the new definition.",
                       "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id
     */
    NULL_METADATA_COLLECTION_ID("OMAG-CONNECTORS-0006",
               AuditLogRecordSeverityLevel.INFO,
               "The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id",
               "No metadata collection asset nor inventory catalog software capability is connected to the server.",
               "This is only ok if the server is a metadata access point.",
               "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * Connector {0} has started monitoring OMAG Server Platform: {1}
     */
    EGERIA_TARGET_START("OMAG-CONNECTORS-0010",
                           AuditLogRecordSeverityLevel.INFO,
                           "Connector {0} has started monitoring OMAG Server Platform: {1}",
                           "The connector will synchronize the configuration of the platform and its servers with its open metadata description.",
                           "No specific action is required.  This message is to confirm the start of the target processor.",
                           "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * Connector {0} is synchronizing metadata for OMAG Server Platform: {1}
     */
    EGERIA_TARGET_REFRESH("OMAG-CONNECTORS-0011",
                        AuditLogRecordSeverityLevel.INFO,
                        "Connector {0} is synchronizing metadata for OMAG Server Platform: {1}",
                        "The connector is synchronizing the configuration of the platform and its servers with its open metadata description.",
                        "No specific action is required.  This message is to confirm the refreshing of the target processor.",
                        "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector was not able to catalog server {1} on platform {2}; the {3} exception returned message {4}
     */
    SERVER_CATALOG_FAILED("OMAG-CONNECTORS-0012",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "The {0} integration connector was not able to catalog server {1} on platform {2}; the {3} exception returned message {4}",
                          "The connector moves on to the platform's other servers.  This server's open metadata description is left as it was, which means it no longer reflects the running server.",
                          "Use the message to work out why this one server could not be catalogued.  A duplicate qualified name usually means the server, or something belonging to it, has already been catalogued under a name that this connector no longer computes - most often because the server has been renamed, moved to another platform, or catalogued by an earlier release.",
                          "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector has renamed platform element {1} from {2} to {3} so that it can be recognised again
     */
    PLATFORM_NAME_MIGRATED("OMAG-CONNECTORS-0013",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} integration connector has renamed platform element {1} from {2} to {3} so that it can be recognised again",
                           "The connector carries on using the element it renamed, so the platform's history, its servers and anything else attached to it are kept.",
                           "No action is required.  Earlier releases named a platform element after the platform's own name and organization, which is not unique to one running platform and is not the name this connector looks the element up by.  This message records the one-off correction; it should not appear again for the same platform.",
                           "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector is now monitoring the OMAG Server Platform at {1}, catalogued as {2}
     */
    PLATFORM_CATALOG_TARGET_ADDED("OMAG-CONNECTORS-0014",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector is now monitoring the OMAG Server Platform at {1}, catalogued as {2}",
                                  "The platform has been registered as one of this connector's catalog targets, so its configuration and servers are synchronized with open metadata on every refresh.",
                                  "No specific action is required.  This message records a platform being taken under management, which happens the first time the connector runs against it.",
                                  "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

    /**
     * The {0} integration connector could not register the OMAG Server Platform at {1} yet; the {2} exception returned message {3}
     */
    PLATFORM_REGISTRATION_DEFERRED("OMAG-CONNECTORS-0015",
                                   AuditLogRecordSeverityLevel.EXCEPTION,
                                   "The {0} integration connector could not register the OMAG Server Platform at {1} yet; the {2} exception returned message {3}",
                                   "The connector carries on with the catalog targets it already has, and tries this platform again on its next refresh.  The platform is not monitored in the meantime.",
                                   "This is usually temporary - most often the metadata access store was restarting, or had not finished loading its content packs, when the connector started.  If the message keeps appearing, use the exception to work out why the connector cannot write to the metadata access store.",
                                   "https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/"),

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
    OMAGConnectorAuditCode(String                      messageId,
                           AuditLogRecordSeverityLevel severity,
                           String                      message,
                           String                      systemAction,
                           String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for OMAGConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * OMAGConnectorAuditCode above.   For example:
     * <br>
     *     OMAGConnectorAuditCode   auditCode = OMAGConnectorAuditCode.SERVER_NOT_AVAILABLE;
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
    OMAGConnectorAuditCode(String                      messageId,
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
        return "OMAGConnectorAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
