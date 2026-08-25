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
     * The monitoring of OMAG Server Platform '{0}' has started
     */
    EGERIA_TARGET_START("OMAG-CONNECTORS-0010",
                           AuditLogRecordSeverityLevel.INFO,
                           "The monitoring of OMAG Server Platform {0} has started",
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
