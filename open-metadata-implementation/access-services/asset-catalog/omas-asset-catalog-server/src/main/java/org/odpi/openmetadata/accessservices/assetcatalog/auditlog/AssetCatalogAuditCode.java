/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The AssetCatalogAuditCode is used to define the message content for the OMRS Audit Log.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>Log Message Id - to uniquely identify the message</li>
 * <li>Severity - is this an event, decision, action, error or exception</li>
 * <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 * <li>SystemAction - describes the result of the situation</li>
 * <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum AssetCatalogAuditCode implements AuditLogMessageSet
{
    /**
     * OMAS-ASSET-CATALOG-0001 The Asset Catalog OMAS has completed initialization of a new instance.
     */
    SERVICE_INITIALIZED("OMAS-ASSET-CATALOG-0001",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Asset Catalog Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
                        "The Asset Catalog OMAS has completed initialization of a new instance.",
                        "No action is required.  This is part of the normal operation of the service."),
    /**
     * OMAS-ASSET-CATALOG-0002 The local server has started up a new instance of the Asset Catalog OMAS.
     */
    SERVICE_INITIALIZING("OMAS-ASSET-CATALOG-0002",
                         AuditLogRecordSeverityLevel.STARTUP,
            "The Asset Catalog Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Asset Catalog OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    /**
     * OMAS-ASSET-CATALOG-0003 The access service detected an error during the start-up of a specific server instance.
     * Its services are not available for the specified server.
     */
    SERVICE_INSTANCE_FAILURE("OMAS-ASSET-CATALOG-0003",
                             AuditLogRecordSeverityLevel.ERROR,
            "The Asset Catalog Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance. Its services are not available for server {1}.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMAS-ASSET-CATALOG-0004 The local server has requested shut down of an Asset Catalog OMAS instance.
     */
    SERVICE_SHUTDOWN("OMAS-ASSET-CATALOG-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Asset Catalog Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
            "The local server has requested shut down of an Asset Catalog OMAS instance.",
            "No action is required. This is part of the normal operation of the service."),

    /**
     * OMAS-ASSET-CATALOG-0005 The event could not be processed.
     */
    EVENT_PROCESSING_EXCEPTION("OMAS-ASSET-CATALOG-0005",
                               AuditLogRecordSeverityLevel.EXCEPTION,
            "An exception with message {0} occurred while processing incoming event {1}",
            "The event could not be processed.",
            "Review the exception to determine the source of the error and correct it."),

    /**
     * OMAS-ASSET-CATALOG-0006 The event will not be processed.
     */
    EVENT_NOT_PROCESSING("OMAS-ASSET-CATALOG-0006",
                         AuditLogRecordSeverityLevel.TRACE,
            "An event is ignored {0}",
            "The event will not be processed.",
            "No action is required. This is part of the normal operation of the service."),;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    
    
    /**
     * The constructor for AssetCatalogAuditCode expects to be passed one of the enumeration rows defined in
     * AssetCatalogAuditCode above.   For example:
     * <p>
     * AssetCatalogAuditCode   auditCode = AssetCatalogAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique ID for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    AssetCatalogAuditCode(String                      messageId,
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
    public AuditLogMessageDefinition getMessageDefinition(String... params)
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
        return "AssetCatalogAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}