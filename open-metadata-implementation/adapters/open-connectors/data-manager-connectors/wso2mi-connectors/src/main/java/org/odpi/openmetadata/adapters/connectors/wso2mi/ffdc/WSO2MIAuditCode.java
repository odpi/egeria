/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The WSO2MIAuditCode is used to define the message content for the OMRS Audit Log.
 * The 6 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 *     <li>URL - link to a page describing the concept behind the message</li>
 * </ul>
 */
public enum WSO2MIAuditCode implements AuditLogMessageSet
{
    /**
     * WSO2MI-CONNECTOR-0001 - The WSO2 Micro Integrator connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("WSO2MI-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The WSO2 Micro Integrator connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/connectors/"),

    /**
     * WSO2MI-CONNECTOR-0002 - The {0} integration connector has catalogued WSO2 Micro Integrator API {1} ({2})
     */
    CATALOGED_API("WSO2MI-CONNECTOR-0002",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} integration connector has catalogued WSO2 Micro Integrator API {1} ({2})",
                  "The integration connector looks for another deployed API.",
                  "This is an information message showing that the integration connector has found a new deployed API.",
                  "https://egeria-project.org/connectors/"),

    /**
     * WSO2MI-CONNECTOR-0003 - The {0} integration connector is skipping WSO2 Micro Integrator API {1} ({2}) because it is already catalogued
     */
    SKIPPING_API("WSO2MI-CONNECTOR-0003",
                 AuditLogRecordSeverityLevel.INFO,
                 "The {0} integration connector is skipping WSO2 Micro Integrator API {1} ({2}) because it is already catalogued",
                 "The integration connector continues, looking for another deployed API.",
                 "This is an information message showing that the integration connector is working, but does not need to do any processing on this API.",
                 "https://egeria-project.org/connectors/"),

    /**
     * WSO2MI-CONNECTOR-0004 - The {0} connector has authenticated with the WSO2 Micro Integrator Management API at {1}
     */
    AUTHENTICATED("WSO2MI-CONNECTOR-0004",
                  AuditLogRecordSeverityLevel.INFO,
                  "The {0} connector has authenticated with the WSO2 Micro Integrator Management API at {1}",
                  "The connector exchanges the supplied user name and password for a short-lived bearer token and uses it for subsequent calls.",
                  "No action is required.  This message confirms that the Management API endpoint is reachable and the credentials are valid.",
                  "https://egeria-project.org/connectors/"),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;
    private final String                     url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId    unique id for the message
     * @param severity     severity of the message
     * @param message      text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction   instructions for resolving the situation, if any
     */
    WSO2MIAuditCode(String messageId, AuditLogRecordSeverityLevel severity, String message, String systemAction, String userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * Constructor.
     *
     * @param messageId    unique id for the message
     * @param severity     severity of the message
     * @param message      text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction   instructions for resolving the situation, if any
     * @param url          link to a page that describes the component or concept behind this message
     */
    WSO2MIAuditCode(String                      messageId,
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
        return new AuditLogMessageDefinition(logMessageId, severity, logMessage, systemAction, userAction, url);
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
     * JSON-style toString.
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "WSO2MIAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
