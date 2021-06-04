/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.auditlog;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

public enum AssetLineageAuditCode implements AuditLogMessageSet {

    SERVICE_INITIALIZING("OMAS-ASSET-LINEAGE-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Asset Lineage Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Asset Lineage OMAS.",
            "No action is required.  This is part of the normal operation of the server."),


    SERVICE_INITIALIZED("OMAS-ASSET-LINEAGE-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Asset Lineage Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Asset Lineage OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-ASSET-LINEAGE-0003",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Asset Lineage Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Asset Lineage OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),


    SERVICE_INSTANCE_FAILURE("OMAS-ASSET-LINEAGE-0004",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Asset Lineage Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    EVENT_PROCESSING_EXCEPTION("OMAS-ASSET-LINEAGE-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "An exception occurred while processing incoming event {0}",
            "The event could not be processed",
            "Review the exception to determine the source of the error and correct it."),

    PUBLISH_PROCESS_INFO("OMAS-ASSET-LINEAGE-0006",
            OMRSAuditLogRecordSeverity.INFO,
            "{0} Processing sequence entity type {1} items {2}",
            "The Asset Lineage OMAS records information about processing sequence.",
            "No action is required."),

    ENTITY_INFO("OMAS-ASSET-LINEAGE-0007",
            OMRSAuditLogRecordSeverity.INFO,
            "{0} Entity type {1} guid {2}",
            "The Asset Lineage OMAS records information about entity.",
            "No action is required."),

    ENTITY_ERROR("OMAS-ASSET-LINEAGE-0008",
            OMRSAuditLogRecordSeverity.ERROR,
            "Error processing entity type {0} guid {1}",
            "The Asset Lineage OMAS records error while processing entity.",
            "Review the error message and check the entity in question to determine the cause of the problem."),

    PUBLISH_EVENT_ERROR("OMAS-ASSET-LINEAGE-0009",
                                  OMRSAuditLogRecordSeverity.ERROR,
            "Error while trying to use AssetLineagePublisher instance.",
                                          "The Asset Lineage OMAS will not publish entity.",
                                          "Possible configuration error; Contact the server administrator to " +
                                                  "check accessServiceOutTopic connection configuration before tying again."),

    ASSET_CONTEXT_INFO("OMAS-ASSET-LINEAGE-0010",
            OMRSAuditLogRecordSeverity.INFO,
            "The asset context for the entity with guid {0} is requested through REST endpoint and will be " +
                    "retrieved on the out topic.",
            "The Asset Lineage OMAS retrieves entity's asset context.",
            "No action is required.");

    private AuditLogMessageDefinition messageDefinition;

    /**
     * The constructor for AssetLineageAuditCode expects to be passed one of the enumeration rows defined in
     * AssetLineageAuditCode above.   For example:
     * <p>
     * AssetLineageAuditCode   auditCode = AssetLineageAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    AssetLineageAuditCode(String messageId,
                          OMRSAuditLogRecordSeverity severity,
                          String message,
                          String systemAction,
                          String userAction) {
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
    public AuditLogMessageDefinition getMessageDefinition() {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString() {
        return "AssetLineageAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
