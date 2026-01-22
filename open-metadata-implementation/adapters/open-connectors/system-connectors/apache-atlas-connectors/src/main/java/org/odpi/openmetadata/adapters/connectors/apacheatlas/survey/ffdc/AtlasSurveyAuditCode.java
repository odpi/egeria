/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The AtlasDiscoveryAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum AtlasSurveyAuditCode implements AuditLogMessageSet
{
    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0001 - The {0} Apache Atlas Discovery Connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} Apache Atlas Survey Action Service received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot continue to profile Apache Atlas.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),


    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0002 - The {0} Apache Atlas Survey Action Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_REST_CONNECTOR("APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0002",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The {0} Apache Atlas Survey Action Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                         "The connector cannot continue to profile Apache Atlas because it can not call its REST API.",
                         "Use the details from the error message to determine the class of the connector.  " +
                                 "Update the connector type associated with Apache Atlas's Connection in the metadata store."),


    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0003 - The {0} Apache Atlas Discovery Connector has been supplied with a resource connector of class {1} rather than class {2} for asset {3}
     */
    WRONG_ROOT_SCHEMA_TYPE("APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0003",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4}",
                         "The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot understand the existing root schema type.",
                         "Use the details from the error message to determine the origin and reason for the existing schema type.  If it is correct then disable the schema analysis of this survey action service.  It the existing root schema type should not be present, then delete it, and re-run the failed survey action service."),

    /**
     * APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0004 - The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis
     */
    MISSING_ASSET_UNIVERSE("APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0004",
                           AuditLogRecordSeverityLevel.ERROR,
                           "The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis",
                           "The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot access the existing root schema type from the asset universe because it is null.",
                           "Use the details from the error message to determine the asset universe being null.  Correct the error and re-run the failed survey action service."),


    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for AtlasDiscoveryAuditCode expects to be passed one of the enumeration rows defined in
     * AtlasDiscoveryAuditCode above.   For example:
     * <br><br>
     *     AtlasDiscoveryAuditCode   auditCode = AtlasDiscoveryAuditCode.UNEXPECTED_EXCEPTION;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    AtlasSurveyAuditCode(String                      messageId,
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
        return "AtlasDiscoveryAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
