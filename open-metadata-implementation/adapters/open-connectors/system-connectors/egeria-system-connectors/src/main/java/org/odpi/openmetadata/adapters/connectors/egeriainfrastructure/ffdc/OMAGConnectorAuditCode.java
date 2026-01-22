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
 *     <li>Severity - is this an event, decision, action, error or exception</li>
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
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * The {0} integration connector has been started and will call the platforms with userId {1}.  The monitored platforms are: {2}
     */
    EGERIA_CONNECTOR_START("OMAG-CONNECTORS-0002",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} Egeria Connector has been started.  The monitored platforms are: {1}",
                           "The connector is designed to catalog details of Software Server Platforms that have the deployedImplementationType property set to 'OMAG Server Platform'.",
                           "No specific action is required.  This message is to confirm the start of the integration connector."),

    /**
     * The {0} integration connector is not able to retrieve platform {1} ({2}) from the catalog
     */
    UNKNOWN_PLATFORM("OMAG-CONNECTORS-0003",
                     AuditLogRecordSeverityLevel.ERROR,
                     "The {0} integration connector is not able to retrieve platform {1} ({2}) from the catalog",
                     "The connector continues to catalog platforms.",
                     "Determine why this platform is not catalogued."),

    /**
     * The {0} integration connector has stopped its platform monitoring and is shutting down
     */
    CONNECTOR_STOPPING("OMAG-CONNECTORS-0004",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its platform monitoring and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),


    /**
     * The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}
     */
    NEW_SERVER("OMAG-CONNECTORS-0005",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}",
                       "The connector is has catalogued a new server.",
                       "No action is required unless there are errors that follow indicating that there were problems with the new definition."),

    /**
     * The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id
     */
    NULL_METADATA_COLLECTION_ID("OMAG-CONNECTORS-0006",
               AuditLogRecordSeverityLevel.INFO,
               "The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id",
               "No metadata collection asset nor inventory catalog software capability is connected to the server.",
               "This is only ok if the server is a metadata access point."),

    /**
     * The {0} integration connector is refreshing the lineage of {1} server {2}
     */
    REFRESH_SERVER_LINEAGE("OMAG-CONNECTORS-0007",
                                AuditLogRecordSeverityLevel.INFO,
                                "The {0} integration connector is refreshing the lineage of {1} server {2}",
                                "The connector has retrieved the connector for this server and is comparing the catalogued lineage relationships with the server's configuration",
                                "Validate that there are no errors reported while these relationships are reviewed and updated."),

    /**
     * The {0} integration connector cannot refresh the lineage of {1} server {2}. Exception was of type {3} with message {4}
     */
    REFRESH_SERVER_LINEAGE_FAILED("OMAG-CONNECTORS-0008",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} integration connector cannot refresh the lineage of {1} server {2}. Exception was of type {3} with message {4}",
                           "The connector hit a problem refreshing lineage relationships for the server. The exception describes the nature of the issue.",
                           "Review the exception to determine the cause of the problem.  Correct it and the lineage will be updated on the next refresh."),


    /**
     * The {0} integration connector cannot refresh the lineage of {1} server {2} because the connector is of type {3} rather than type {4}
     */
    REFRESH_SERVER_LINEAGE_BAD_CONNECTOR("OMAG-CONNECTORS-0008",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector cannot refresh the lineage of {1} server {2} because the connector is of type {3} rather than type {4}",
                                  "The connector hit a problem refreshing lineage relationships for the server because the resource connector associated with the server is no of the expected type.",
                                  "Correct the connector type linked to the server's connection and the lineage will be updated on the next refresh."),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


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
     */
    OMAGConnectorAuditCode(String                      messageId,
                           AuditLogRecordSeverityLevel severity,
                           String                      message,
                           String                      systemAction,
                           String                      userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
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
                                             userAction);
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
                                                                                    userAction);
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
                '}';
    }
}
