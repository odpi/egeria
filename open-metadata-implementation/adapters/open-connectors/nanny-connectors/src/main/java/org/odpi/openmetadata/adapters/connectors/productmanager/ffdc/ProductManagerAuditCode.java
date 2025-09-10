/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The DistributeKafkaAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum ProductManagerAuditCode implements AuditLogMessageSet
{
    /**
     * HARVEST-DIGITAL-PRODUCTS-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("HARVEST-DIGITAL-PRODUCTS-0001",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to catalog one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * HARVEST-DIGITAL-PRODUCTS-0002 - Integration connector {0} is unable to determine if tabular data source {1} has changed since it has no last update time column
     */
    NO_LAST_UPDATE_DATE( "HARVEST-DIGITAL-PRODUCTS-0002",
                        OMRSAuditLogRecordSeverity.ERROR,
                        "Integration connector {0} is unable to determine if tabular data source {1} has changed since it has no last update time column",
                        "The integration connector skips this data source.",
                        "Update the data source to ensure it has a column called 'updateTime'."),


    /**
     * HARVEST-DIGITAL-PRODUCTS-0003 - Integration connector {0} detected that row {1} of dataset {2} has been updated on {3}
     */
    DATA_SET_UPDATE_DETECTED( "HARVEST-DIGITAL-PRODUCTS-0003",
                         OMRSAuditLogRecordSeverity.INFO,
                         "Integration connector {0} detected that row {1} of dataset {2} has been updated on {3}; row values {4}",
                         "The integration connector updates the last update data in the Governance Classification for this data source.",
                         "Validate that this assertion is reasonable."),

    /**
     * HARVEST-DIGITAL-PRODUCTS-0005 - The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}
     */
    BAD_CONFIGURATION("HARVEST-DIGITAL-PRODUCTS-0005",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}",
                          "The exception is passed back to the integration daemon that is hosting " +
                                  "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                                  "error handling that was performed.  These can help to determine how to recover from this error",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * HARVEST-DIGITAL-PRODUCTS-0009 - The {0} integration connector has stopped its monitoring of Apache Atlas at {1} and is shutting down
     */
    CONNECTOR_STOPPING("HARVEST-DIGITAL-PRODUCTS-0009",
                       OMRSAuditLogRecordSeverity.INFO,
                       "The {0} integration connector has stopped its monitoring of Apache Atlas at {1} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),


    /**
     * HARVEST-DIGITAL-PRODUCTS-0032 - The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}
     */
    UNABLE_TO_REGISTER_LISTENER("HARVEST-DIGITAL-PRODUCTS-0032",
                      OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}",
                                  "The connector continues to scan and synchronize metadata as configured.  Without the listener, updates to open metadata elements with only be synchronized to Apache Atlas during a refresh scan.",
                                  "The likely cause of this error is that the Asset Manager OMAS in the metadata access server used by the integration daemon is not configured to support topics.  This can be changed by reconfiguring the metadata access server to support topics.  A less likely cause is that the metadata access server has stopped running"),


    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for DistributeKafkaAuditCode expects to be passed one of the enumeration rows defined in
     * DistributeKafkaAuditCode above.   For example:
     * <br>
     *     DistributeKafkaAuditCode   auditCode = DistributeKafkaAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    ProductManagerAuditCode(String                     messageId,
                            OMRSAuditLogRecordSeverity severity,
                            String                     message,
                            String                     systemAction,
                            String                     userAction)
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
        return "DistributeKafkaAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
