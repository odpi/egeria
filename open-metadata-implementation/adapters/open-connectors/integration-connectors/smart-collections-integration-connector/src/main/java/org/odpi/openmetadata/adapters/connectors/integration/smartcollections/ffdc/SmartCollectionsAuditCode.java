/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.smartcollections.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The SmartCollectionsAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum SmartCollectionsAuditCode implements AuditLogMessageSet
{
    /**
     * SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0001 - Connector {0} is refreshing the membership of results set {1}
     */
    REFRESHING_RESULTS_SET("SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0001",
                           AuditLogRecordSeverityLevel.INFO,
                           "Connector {0} is refreshing the membership of results set {1}",
                           "The connector is about to run the saved query attached to the results set and update its membership to match the results.",
                           "No user action is required.",
                           "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0002 - Connector {0} found {1} saved queries attached to results set {2}; expecting to find at most one
     */
    WRONG_NUMBER_OF_SAVED_QUERIES("SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0002",
                                  AuditLogRecordSeverityLevel.ERROR,
                                  "Connector {0} found {1} saved queries attached to results set {2}; expecting to find exactly one",
                                  "The connector is unable to determine which saved query to run, so it is leaving the membership of the results set unchanged.",
                                  "Ensure that exactly one SavedQuery entity is linked to the results set via the SmartQuery relationship.",
                                  "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0003 - Connector {0} completed the membership refresh of results set {1}: {2} members added, {3} members removed
     */
    RESULTS_SET_REFRESHED("SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0003",
                          AuditLogRecordSeverityLevel.INFO,
                          "Connector {0} completed the membership refresh of results set {1}: {2} members added, {3} members removed",
                          "The connector has finished comparing the results of the saved query with the current membership of the results set.",
                          "No user action is required.",
                          "https://egeria-project.org/concepts/integration-connector/"),

    /**
     * SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0004 - The Smart Collections Integration Connector {0} received an unexpected {1} exception during method {2} while refreshing results set {3}; the error message was: {4}
     */
    UNEXPECTED_EXCEPTION("SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0004",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Smart Collections Integration Connector {0} received an unexpected {1} exception during method {2} while refreshing results set {3}; the error message was: {4}",
                         "The connector was unable to complete the membership refresh for this results set.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/concepts/integration-connector/"),

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
     * @param messageId unique id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    SmartCollectionsAuditCode(String                      messageId,
                              AuditLogRecordSeverityLevel severity,
                              String                      message,
                              String                      systemAction,
                              String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for SmartCollectionsAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId unique id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    SmartCollectionsAuditCode(String                      messageId,
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
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
