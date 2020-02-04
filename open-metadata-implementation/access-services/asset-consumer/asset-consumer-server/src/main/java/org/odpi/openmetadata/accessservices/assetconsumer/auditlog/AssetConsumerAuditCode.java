/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.auditlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The AssetConsumerAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum AssetConsumerAuditCode
{
    SERVICE_INITIALIZING("OMAS-ASSET-CONSUMER-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Asset Consumer Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Asset Consumer OMAS.  " +
                                 "This service enables tools to locate assets in the catalog, retrieve information about them" +
                                 "and create open connectors to access the content of the assets.  It also sends " +
                                 "events on its out topic each time a new asset is created or updated.",
             "No action is required as long as this service is expected to be started for this server.  " +
                                 "If it is not required, then remove the configuration for this service " +
                                 "from the access service list in this server's configuration document."),

    SERVICE_INITIALIZED("OMAS-ASSET-CONSUMER-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Asset Consumer Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "Verify that this service has initialized successfully and has the correct set of supported zones " +
                                "defined.  Investigate any reported errors.  Also ensure that the enterprise repository " +
                                "services and the OCF metadata management services are initialized."),

    SERVICE_SHUTDOWN("OMAS-ASSET-CONSUMER-0004",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Asset Consumer Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Asset Consumer OMAS instance.",
             "No action is required if the server shutdown was intentional."),

    SERVICE_INSTANCE_FAILURE("OMAS-ASSET-CONSUMER-0005",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Asset Consumer Open Metadata Access Service (OMAS) is unable to initialize a new instance in server {0}; the {1} exception " +
                                     "occurred with error message: {2}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    ASSET_AUDIT_LOG("OMAS-ASSET-CONSUMER-0008",
               OMRSAuditLogRecordSeverity.ASSET,
               "Audit message for asset {0}: {1}",
               "An asset consumer has logged a message for an asset.",
               "Review the message to ensure no action is required.")

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(AssetConsumerAuditCode.class);


    /**
     * The constructor for AssetConsumerAuditCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerAuditCode above.   For example:
     *
     *     AssetConsumerAuditCode   auditCode = AssetConsumerAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    AssetConsumerAuditCode(String                     messageId,
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
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== AssetConsumer Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> AssetConsumer Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
