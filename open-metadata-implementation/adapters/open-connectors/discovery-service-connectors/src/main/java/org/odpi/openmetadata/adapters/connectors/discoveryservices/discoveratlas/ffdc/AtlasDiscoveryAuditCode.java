/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The AtlasDiscoveryAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum AtlasDiscoveryAuditCode implements AuditLogMessageSet
{
    /**
     * APACHE-ATLAS-DISCOVERY-CONNECTOR-0001 - The {0} Apache Atlas Discovery Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("APACHE-ATLAS-DISCOVERY-CONNECTOR-0001",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The {0} Apache Atlas Discovery Connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to continue to profile Apache Atlas.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),


    /**
     * APACHE-ATLAS-DISCOVERY-CONNECTOR-0002 - The {0} Apache Atlas Discovery Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_REST_CONNECTOR("APACHE-ATLAS-DISCOVERY-CONNECTOR-0002",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "The {0} Apache Atlas Discovery Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                         "The connector is unable to continue to profile Apache Atlas because it can not call its REST API.",
                         "Use the details from the error message to determine the class of the connector.  " +
                                 "Update the connector type associated with Apache Atlas's Connection in the metadata store."),

    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(AtlasDiscoveryAuditCode.class);


    /**
     * The constructor for AtlasDiscoveryAuditCode expects to be passed one of the enumeration rows defined in
     * AtlasDiscoveryAuditCode above.   For example:
     * <br><br>
     *     AtlasDiscoveryAuditCode   auditCode = AtlasDiscoveryAuditCode.UNEXPECTED_EXCEPTION;
     * <br><br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    AtlasDiscoveryAuditCode(String                     messageId,
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
        return "AtlasDiscoveryAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
