/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

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
public enum AssetConsumerAuditCode implements AuditLogMessageSet
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
               "Review the message to ensure no action is required."),

    OUT_TOPIC_EVENT("OMAS-ASSET-CONSUMER-0012",
                    OMRSAuditLogRecordSeverity.EVENT,
             "The Asset Consumer Open Metadata Access Service (OMAS) has sent event: {0}",
                     "The access service sends out asset notifications to ensure connected tools have the most up to-date " +
                     "knowledge about assets.  This audit log message is to create a record of the events that are being published.",
                     "This event indicates that the metadata for an asset has changed.  This my or may not be significant to " +
                             "the receiving tools.") ;

    private static final long    serialVersionUID = 1L;

    private AuditLogMessageDefinition messageDefinition;


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
        messageDefinition = new AuditLogMessageDefinition(messageId,
                                                          severity,
                                                          message,
                                                          systemAction,
                                                          userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
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
        return "AssetConsumerAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
