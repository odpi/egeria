/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The AssetManagerAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum AssetManagerAuditCode implements AuditLogMessageSet
{

    SERVICE_INITIALIZING("OMAS-ASSET-MANAGER-0001",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The Asset Manager Open Metadata Access Service (OMAS) is initializing a new server instance",
                         "The local server has started up a new instance of the Asset Manager OMAS.  This service " +
                                 "supports the exchange of metadata between asset managers.",
                         "No action is required if the Asset Manager OMAS service is expected to be started in this server.  If " +
                                 "this service is not required then it can be removed from the access service " +
                                 "list in the configuration document for this server.  It will then not be started " +
                                 "the next time the server starts up."),

    SERVICE_PUBLISHING("OMAS-ASSET-MANAGER-0002",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Asset Manager Open Metadata Access Service (OMAS) is ready to publish asset manager notifications to topic {0}",
                       "The local server has started up the event publisher for the Asset Manager OMAS.  " +
                               "It will begin publishing asset manager metadata changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),

    SERVICE_INITIALIZED("OMAS-ASSET-MANAGER-0005",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Asset Manager Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Asset Manager OMAS has completed initialization of a new instance.",
            "Verify that this service has initialized successfully with both an in and out topic and it has " +
                                "the correct set of supported zones, publish zones " +
                                "and default zones defined.  Investigate any reported errors.  Also ensure that the" +
                                "enterprise repository services and the OCF metadata management services are initialized."),

    SERVICE_SHUTDOWN("OMAS-ASSET-MANAGER-0006",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Asset Manager Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Asset Manager OMAS server instance.",
            "No action is required if this shutdown was intended."),

    PUBLISHING_SHUTDOWN("OMAS-ASSET-MANAGER-0007",
                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                        "The Asset Manager Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an Asset Manager OMAS instance.  " +
                                "No more events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    PUBLISHING_SHUTDOWN_ERROR("OMAS-ASSET-MANAGER-0008",
                              OMRSAuditLogRecordSeverity.SHUTDOWN,
                              "The Asset Manager Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an Asset Manager OMAS instance.  " +
                                      "No more events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    SERVICE_INSTANCE_FAILURE("OMAS-ASSET-MANAGER-0011",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Asset Manager Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    
    SERVICE_INSTANCE_TERMINATION_FAILURE("OMAS-ASSET-MANAGER-0012",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Asset Manager Open Metadata Access Service (OMAS) is unable to terminate a new instance; error message is {0}",
            "The access service detected an error during the shut down of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, try to shut " +
                                                 "down the server."),

    OUT_TOPIC_EVENT("OMAS-ASSET-MANAGER-0013",
                    OMRSAuditLogRecordSeverity.EVENT,
                    "The Asset Manager Open Metadata Access Service (OMAS) has sent event of type: {0}",
                    "The access service sends out notifications about changes to assets located on " +
                            "asset managers.  This message is to create a record of the events that are being published.",
                    "This event indicates that one of the assets within an asset manager has changed."),

    PROCESS_EVENT_EXCEPTION("OMAS-ASSET-MANAGER-0014",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
                            "Event {0} could not be published due to {1} exception with message: {2}",
                            "The system is unable to publish the event to the Asset Manager OMAS's OutTopic.",
                            "Verify the topic configuration and that the event broker is running."),

    PARSE_EVENT_EXCEPTION("OMAS-ASSET-MANAGER-0015",
          OMRSAuditLogRecordSeverity.EXCEPTION,
          "Inbound event {0} could not be parsed. IOException included the following message: {1}",
          "The system is unable to process the event received on the Asset Manager OMAS in topic.",
          "Verify the content and structure of the in topic event.  Check that it includes the correct class name key"),


    ;

    private static final long    serialVersionUID = 1L;

    private final AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for AssetManagerAuditCode expects to be passed one of the enumeration rows defined in
     * AssetManagerAuditCode above.   For example:
     *
     *     AssetManagerAuditCode   auditCode = AssetManagerAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    AssetManagerAuditCode(String                     messageId,
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
        return "AssetManagerAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
