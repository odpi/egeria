/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The ApacheAtlasAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum ApacheAtlasAuditCode implements AuditLogMessageSet
{
    CONNECTOR_CONFIGURATION_ALL("APACHE-ATLAS-INTEGRATION-CONNECTOR-0001",
                                OMRSAuditLogRecordSeverity.INFO,
                                "The {0} integration connector has been initialized to publish all glossary terms to the Apache Atlas server at URL {1}",
                                "The connector is designed to publish changes to all active glossary terms to Apache Atlas glossaries.",
                                "No specific action is required.  This message is to confirm the configuration for the integration connector allows all glossaries to be published."),

    CONNECTOR_CONFIGURATION_SPECIFIC("APACHE-ATLAS-INTEGRATION-CONNECTOR-0002",
                                OMRSAuditLogRecordSeverity.INFO,
                                "The {0} integration connector has been initialized to publish glossary terms from glossary {2} to the Apache Atlas server at URL {1}",
                                "The connector is designed to publish changes to all active glossary terms from the name glossary to an equivalent glossary on Apache Atlas.",
                                "No specific action is required.  This message is to confirm the configuration for the integration connector limits which glossaries are to be published."),

    BAD_CONFIGURATION("APACHE-ATLAS-INTEGRATION-CONNECTOR-0003",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} integration connector encountered an {1} exception when opening event broker {2} during the {3} method.  The exception message included was {4}",
                          "The exception is passed back to the Topic Integrator OMIS in the integration daemon that is hosting " +
                                  "this connector to enable it to perform error handling.  More messages are likely to follow describing the " +
                                  "error handling that was performed.  These can help to determine how to recover from this error",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),

    UNABLE_TO_RETRIEVE_TOPICS("APACHE-ATLAS-INTEGRATION-CONNECTOR-0004",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
                            "The {0} integration connector received an unexpected {2} exception when retrieving topics from event broker at {1}.  The error message was {3}",
                                     "The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.",
                                     "Use the message in the nested exception to determine the root cause of the error. Once this is " +
                                             "resolved, follow the instructions in the messages produced by the integration daemon to restart this connector."),

    RETRIEVED_TOPICS("APACHE-ATLAS-INTEGRATION-CONNECTOR-0005",
                              OMRSAuditLogRecordSeverity.INFO,
                              "The {0} integration connector has retrieved {2} topics from {1}",
                              "The connector will maintain these topics as assets.",
                              "No action is required unless there are errors that follow indicating that the topics can not be maintained."),

    CONNECTOR_STOPPING("APACHE-ATLAS-INTEGRATION-CONNECTOR-0009",
                                  OMRSAuditLogRecordSeverity.INFO,
                                  "The {0} integration connector has stopped its topic monitoring and is shutting down",
                                  "The connector is disconnecting.",
                                  "No action is required unless there are errors that follow indicating that there were problems shutting down."),

    UNEXPECTED_EXC_TOPIC_UPDATE("APACHE-ATLAS-INTEGRATION-CONNECTOR-0014",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "An unexpected {0} exception was returned to the {1} integration connector when it tried to update the " +
                                         "Topic in the metadata repositories for topic {2}.  The error message was {3}",
                                 "The exception is logged and the integration connector continues to synchronize metadata.  " +
                                         "This topic is not catalogued at this time but may succeed later.",
                                 "Use the message in the unexpected exception to determine the root cause of the error and fix it."),

    MISSING_TEMPLATE("APACHE-ATLAS-INTEGRATION-CONNECTOR-0015",
                     OMRSAuditLogRecordSeverity.ERROR,
                     "The {0} integration connector is unable to retrieve the Topic template with qualified name: {1}",
                     "The metadata element for the template is not found in the open metadata repositories.  " +
                             "The template name was configured for the connector.  This means that topics should be catalogued " +
                             "using the template.  Since the template is missing, topics are not being catalogued.",
                     "Create the template in the metadata repository.  The connector will catalog the topics during " +
                             "its next periodic refresh or you can force it to refresh immediately by calling the refresh" +
                             "operation on the integration daemon."),

    TOPIC_CREATED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0016",
                      OMRSAuditLogRecordSeverity.INFO,
                     "The {0} integration connector created the Topic {1} ({2}) for a new real-world topic",
                     "The connector created the Topic as part of its monitoring of the topics in the event broker.",
                     "No action is required.  This message is to record the reason why the Topic was created."),

    TOPIC_CREATED_FROM_TEMPLATE("APACHE-ATLAS-INTEGRATION-CONNECTOR-0017",
                      OMRSAuditLogRecordSeverity.INFO,
                      "The {0} integration connector created the Topic {1} ({2}) for a new real-world topic using template {3} ({4})",
                      "The connector created the Topic as part of its monitoring of the topics in the event broker.  " +
                              "The template provides details of additional metadata that should also be attached to the new Topic element.  " +
                              "It was specified in the templateQualifiedName configuration property of the connector.",
                      "No action is required.  This message is to record the reason why the Topic was created with the template."),

    TOPIC_UPDATED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0018",
                      OMRSAuditLogRecordSeverity.INFO,
                      "The {0} integration connector has updated the Topic {1} ({2}) because the real-world topic changed",
                      "The connector updated the Topic as part of its monitoring of the topics in the event broker.",
                      "No action is required.  This message is to record the reason why the Topic was updated."),

    TOPIC_DELETED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0019",
                      OMRSAuditLogRecordSeverity.INFO,
                      "The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is no longer defined in the event broker",
                      "The connector removed the Topic as part of its monitoring of the topics in the event broker.",
                      "No action is required.  This message is to record the reason why the Topic was removed."),

    TOPIC_ARCHIVED("APACHE-ATLAS-INTEGRATION-CONNECTOR-0020",
                      OMRSAuditLogRecordSeverity.INFO,
                      "The {0} integration connector has archived the Topic {1} ({2}) because the real-world topic is no longer stored in the event broker",
                      "The connector updated the Topic to reflect that is is now just a placeholder for an asset that no longer exists.  " +
                              "Its presence is still needed in the metadata repository for lineage reporting.",
                      "No action is required.  This message is to record the reason why the Topic was archived."),


    CLIENT_SIDE_REST_API_ERROR( "APACHE-ATLAS-INTEGRATION-CONNECTOR-0030",
                                OMRSAuditLogRecordSeverity.EXCEPTION,
                                "A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}",
                                "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                                "Look for errors in the local server's console to understand and correct the source of the error.")


    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for ApacheAtlasAuditCode expects to be passed one of the enumeration rows defined in
     * ApacheAtlasAuditCode above.   For example:
     * <br>
     *     ApacheAtlasAuditCode   auditCode = ApacheAtlasAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    ApacheAtlasAuditCode(String                     messageId,
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
        return "ApacheAtlasAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
