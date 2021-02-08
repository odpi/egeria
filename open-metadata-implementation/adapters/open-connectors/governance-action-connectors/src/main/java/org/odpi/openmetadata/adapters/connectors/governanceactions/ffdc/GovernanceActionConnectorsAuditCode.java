/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The GovernanceActionConnectorsAuditCode is used to define the message content for the OMRS Audit Log.
 *
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
public enum GovernanceActionConnectorsAuditCode implements AuditLogMessageSet
{
    COPY_FILE("GOVERNANCE-ACTION-CONNECTORS-0001",
                          OMRSAuditLogRecordSeverity.INFO,
                          "The {0} governance action service is copying source file {1} to destination file {2}",
                          "The provisioning governance action service connector is designed to deploy files on request.  " +
                                  "This message confirms that a file has been copied.",
                          "No specific action is required.  This message is to log that a copy provisioning action has taken place."),

    MOVE_FILE("GOVERNANCE-ACTION-CONNECTORS-0002",
              OMRSAuditLogRecordSeverity.INFO,
              "The {0} governance action service is moving source file {1} to destination file {2}",
              "The provisioning governance action service connector is designed to deploy files on request.  " +
                      "This message confirms that a file has been moved.",
              "No specific action is required.  This message is to log that a move provisioning action has taken place."),

    PROVISIONING_FAILURE("GOVERNANCE-ACTION-CONNECTORS-0003",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The {0} governance action service is unable to provision file {1} to {2} destination folder using {3} file pattern.",
                              "This message is logged and the governance action is marked as failed",
                              "Since no exception occurred it means that there are currently files already occupying all of the possible file names allowed by the file pattern.  " +
                                      "Files in the destination folder need to be deleted or this connector needs to be reconfigured with a new destination folder or file pattern."),


    PROVISIONING_EXCEPTION("GOVERNANCE-ACTION-CONNECTORS-0004",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} governance action service encountered an {1} exception when provisioning file {2} to {3} destination folder using {4} file pattern.  The exception message included was {5}",
                          "The exception is logged.  More messages may follow if follow on attempts are made to provision the file.  These can help to determine how to recover from this error",
                          "This message contains the exception that was the original cause of the problem. Use the information from the " +
                                  "exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  " +
                                  "Use the messages that where subsequently logged during the error handling to discover how to restart the " +
                                  "connector in the integration daemon once the original cause of the error has been corrected."),



    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;


    /**
     * The constructor for GovernanceActionConnectorsAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceActionConnectorsAuditCode above.   For example:
     *
     *     GovernanceActionConnectorsAuditCode   auditCode = GovernanceActionConnectorsAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GovernanceActionConnectorsAuditCode(String                     messageId,
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
        return "GovernanceActionConnectorsAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
