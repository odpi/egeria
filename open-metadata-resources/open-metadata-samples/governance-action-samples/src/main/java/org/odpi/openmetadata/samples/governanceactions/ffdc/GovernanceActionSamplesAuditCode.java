/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceactions.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The GovernanceActionSamplesAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum GovernanceActionSamplesAuditCode implements AuditLogMessageSet
{
    /**
     * GOVERNANCE-ACTION-SAMPLES-0001 - The {0} governance action service has verified that hospital {1} ({2}) is certified to supply data for project {3} ({4})
     */
    CERTIFIED_HOSPITAL("GOVERNANCE-ACTION-SAMPLES-0001",
                       AuditLogRecordSeverityLevel.DECISION,
                       "The {0} governance action service has verified that hospital {1} ({2}) is certified to supply data for project {3} ({4})",
                       "The service sets up the onboarding pipeline for this hospital.",
                       "No specific action is required.  This message is to log that a verification check has taken place."),


    /**
     * GOVERNANCE-ACTION-SAMPLES-0002 - The {0} governance action service was passed a null value for {1}
     */
    MISSING_VALUE("GOVERNANCE-ACTION-SAMPLES-0002",
               AuditLogRecordSeverityLevel.ERROR,
               "The {0} governance action service was passed a null value for {1}",
               "The governance action service returns an INVALID completion status.",
               "This is an error in the way that the governance action service has been called, " +
                                        "which could be a direct invocation through the initiateEngineAction() method, initiateGovernanceActionType() method" +
                                        "or as part of a governance action process.  Identify which approach was used and add the required information as an action target to the invocation code.  Then rerun the request."),

    /**
     * GOVERNANCE-ACTION-SAMPLES-0003 - The {0} governance action service has not been passed a Unity Catalog (UC) catalog for the data lake
     */
    MISSING_CATALOG("GOVERNANCE-ACTION-SAMPLES-0003",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} governance action service has not been passed a Unity Catalog (UC) catalog for the data lake",
                    "The governance action service is not able to set up the data lake for the clinical trial.",
                    "Retry the request, but ensure that the action target for the catalog is supplied."),


    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for GovernanceActionSamplesAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceActionSamplesAuditCode above.   For example:
     *     GovernanceActionSamplesAuditCode   auditCode = GovernanceActionSamplesAuditCode.SERVER_NOT_AVAILABLE
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GovernanceActionSamplesAuditCode(String                      messageId,
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
        return "GovernanceActionSamplesAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
