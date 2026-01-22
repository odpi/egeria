/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The JacquardAuditCode is used to define the message content for the Audit Log.
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
public enum JacquardAuditCode implements AuditLogMessageSet
{
    /**
     * JACQUARD-HARVESTER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("JACQUARD-HARVESTER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot catalog one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * JACQUARD-HARVESTER-0002 - Integration connector {0} cannot determine if tabular data source {1} has changed since it has no last update time column
     */
    NO_LAST_UPDATE_DATE( "JACQUARD-HARVESTER-0002",
                         AuditLogRecordSeverityLevel.ERROR,
                         "Integration connector {0} cannot determine if tabular data source {1} has changed since it has no last update time column",
                         "The integration connector skips this data source.",
                         "Update the data source to ensure it has a column called 'updateTime'."),

    /**
     * JACQUARD-HARVESTER-0003 - Integration connector {0} cannot determine if tabular data source {1} has changed since it has no createTime column
     */
    NO_CREATION_DATE( "JACQUARD-HARVESTER-0003",
                         AuditLogRecordSeverityLevel.ERROR,
                         "Integration connector {0} cannot determine if tabular data source {1} has changed since it has no createTime column",
                         "The integration connector skips this data source because of the missing create time column.",
                         "Update the data source to ensure it has a column called 'createTime'."),

    /**
     * JACQUARD-HARVESTER-0004 - Integration connector {0} detected that row {1} of dataset {2} has been updated on {3}
     */
    DATA_SET_UPDATE_DETECTED( "JACQUARD-HARVESTER-0004",
                              AuditLogRecordSeverityLevel.INFO,
                              "Integration connector {0} detected that row {1} of dataset {2} has been updated on {3}; row values {4}",
                              "The integration connector updates the last update data in the Governance Classification for this data source.",
                              "Validate that this assertion is reasonable."),

    /**
     * JACQUARD-HARVESTER-0005 - The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}
     */
    BAD_CONFIGURATION("JACQUARD-HARVESTER-0005",
                      AuditLogRecordSeverityLevel.EXCEPTION,
                      "The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}",
                      "The exception is passed back to the integration daemon that is hosting " +
                              "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                              "error handling that was performed.  These can help to determine how to recover from this error",
                      "This message contains the exception that was the original cause of the problem. Use the information from the " +
                              "exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  " +
                              "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                              "connector in the integration daemon once the original cause of the error has been corrected."),

    /**
     * JACQUARD-HARVESTER-0006 - The {0} integration connector has initiated the Badot Subscription Manager running as engine action {1} with {2} action targets
     */
    BARDOT_STARTED("JACQUARD-HARVESTER-0006",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has initiated the Badot Subscription Manager running as engine action {1} with {2} action targets",
                       "The connector has started the Badot Subscription Manager.",
                       "No action is required unless there are errors that follow indicating that there were problems with the subscription manager."),

    /**
     * JACQUARD-HARVESTER-0009 - The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("JACQUARD-HARVESTER-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    /**
     * JACQUARD-HARVESTER-0010 - The {0} integration connector has created a new {1} supporting definition with GUID {2}
     */
    CREATED_SUPPORTING_DEFINITION("JACQUARD-HARVESTER-0010",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector has created a new {1} supporting definition called {2} with GUID {3}",
                                  "The connector is creating the metadata elements that supports the definition of the Open Metadata Digital Product Catalog.",
                                  "No action is required.  This message is used to show the progress of the setup."),

    /**
     * JACQUARD-HARVESTER-0011 - The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products
     */
    STARTING_CONNECTOR("JACQUARD-HARVESTER-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products",
                       "The connector is initializing the definitions for the Open Metadata Digital Product Catalog.",
                       "Monitor the set up of the catalog and the switch over to monitoring."),

    /**
     * JACQUARD-HARVESTER-0012 - The {0} integration connector has created a new digital product {1} called {2}
     */
    NEW_OPEN_METADATA_PRODUCT("JACQUARD-HARVESTER-0012",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} integration connector has created a new digital product {1} called {2}",
                              "The connector is setting up the fixed open metadata digital products.",
                              "No action is required.  This message is for monitoring the set up of the fixed digital products."),


    /**
     * JACQUARD-HARVESTER-0014 - The {0} integration connector is linking {1} element {2} to {3} element {4} using relationship {5}
     */
    LINKING_ELEMENTS("JACQUARD-HARVESTER-0014",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector is linking {1} element {2} to {3} element {4} using relationship {5}",
                     "The connector is linking product catalog elements together.",
                     "No action is required.  This message is for monitoring the set up of the Open Metadata Digital Product Catalog."),

    /**
     * JACQUARD-HARVESTER-0015 - The {0} integration connector has retrieved a new {1} supporting definition with GUID {2}
     */
    RETRIEVING_SUPPORTING_DEFINITION("JACQUARD-HARVESTER-0015",
                                     AuditLogRecordSeverityLevel.INFO,
                                     "The {0} integration connector has retrieved a new {1} supporting definition called {2} with GUID {3}",
                                     "The connector is retrieving the metadata elements that supports the definition of the Open Metadata Digital Product Catalog.",
                                     "No action is required.  This message is used to show progress during the setup."),

    /**
     * JACQUARD-HARVESTER-0016 - The {0} integration connector has retrieved an existing digital product {1} called {2}
     */
    RETRIEVING_OPEN_METADATA_PRODUCT("JACQUARD-HARVESTER-0016",
                                     AuditLogRecordSeverityLevel.INFO,
                                     "The {0} integration connector has retrieved an existing digital product {1} called {2}",
                                     "The connector is retrieving the fixed open metadata digital products.",
                                     "No action is required.  This message is for monitoring the retrieval of the fixed digital products."),

    /**
     * JACQUARD-HARVESTER-0017 - The {0} integration connector has retrieved existing digital product {1} for valid value set {2}
     */
    RETRIEVED_VALID_VALUE_PRODUCT("JACQUARD-HARVESTER-0017",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} integration connector has retrieved existing digital product {1} for valid value set {2}",
                                  "The connector is retrieving valid value set digital products.",
                                  "No action is required.  This message is for monitoring the retrieval of the valid value set products."),


    /**
     * JACQUARD-HARVESTER-0032 - The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}
     */
    UNABLE_TO_REGISTER_LISTENER("JACQUARD-HARVESTER-0032",
                                AuditLogRecordSeverityLevel.EXCEPTION,
                                "The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}",
                                "The connector continues to scan and synchronize metadata as configured.  Without the listener, updates to open metadata elements with only be synchronized to Apache Atlas during a refresh scan.",
                                "The likely cause of this error is that the Asset Manager OMAS in the metadata access server used by the integration daemon is not configured to support topics.  This can be changed by reconfiguring the metadata access server to support topics.  A less likely cause is that the metadata access server has stopped running"),



    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


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
    JacquardAuditCode(String                      messageId,
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
        return "JacquardAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
