/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.auditlog;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The CognosAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum CognosAuditCode implements AuditLogMessageSet {

    SERVICE_INITIALIZING("OMAS-COGNOS-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Cognos Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Cognos OMAS.",
            "No action is required.  This is part of the normal operation of the server."),

//    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-COGNOS-0002",
//            OMRSAuditLogRecordSeverity.INFO,
//            "The Cognos Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
//            "The Cognos OMAS is registering to receive events from the connected open metadata repositories.",
//            "No action is required.  This is part of the normal operation of the server."),
//
//    SERVICE_REGISTERED_WITH_IV_IN_TOPIC("OMAS-COGNOS-0003",
//            OMRSAuditLogRecordSeverity.INFO,
//            "The Cognos Open Metadata Access Service (OMAS) is registering a listener with the Cognos In topic {0}",
//            "The Cognos OMAS is registering to receive incoming events from external tools and applications.",
//            "No action is required.  This is part of the normal operation of the server."),
//
    SERVICE_REGISTERED_WITH_COGNOS_OUT_TOPIC("OMAS-COGNOS-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is registering a publisher with the Cognos Out topic {0}",
            "The Cognos OMAS is registering to publish events to Cognos Out topic.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-COGNOS-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Cognos OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-COGNOS-0006",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Cognos OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),

    ERROR_INITIALIZING_CONNECTION("OMAS-COGNOS-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Cognos Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SERVICE_INSTANCE_TERMINATION_FAILURE("OMAS-COGNOS-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Cognos Open Metadata Access Service (OMAS) is unable to terminate an instance {0}",
            "The access service detected an error during the shut down of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, try to shut " +
                                                 "down the server."), 
    FIND_ENTITIES("OMAS-COGNOS-0009",
    		OMRSAuditLogRecordSeverity.INFO,
    		"Retrieving entities of type {0} with properties {1}.",
            "The local server has requested find entities operation.",
            "No action is required.  This is part of the normal operation of the server."),

//    SUPPORTED_ZONES("OMAS-COGNOS-0009",
//            OMRSAuditLogRecordSeverity.INFO,
//            "The Cognos Open Metadata Access Service (OMAS) is supporting the following governance zones {0}",
//            "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
//                    "This means it is only providing access to the Assets from these zone(s) and the new Assets will be visible only for these zone(s)",
//            "No action is required.  This is part of the normal operation of the service."),
//    BAD_CONFIG("OMAS-COGNOS-0010",
//            OMRSAuditLogRecordSeverity.ERROR,
//            "The Cognos Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property",
//            "The access service has not been passed valid configuration.",
//            "Correct the configuration and restart the service."),
//    ALL_ZONES("OMAS-COGNOS-0011",
//            OMRSAuditLogRecordSeverity.INFO,
//            "The Cognos Open Metadata Access Service (OMAS) is supporting all governance zones",
//            "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
//                    "This means it is providing access to all Assets irrespective of the zone(s) they are located in and the created Assets can be accessed from any zone",
//            "No action is required.  This is part of the normal operation of the service."),
//    NULL_OMRS_EVENT_RECEIVED("OMAS-COGNOS-0012",
//            OMRSAuditLogRecordSeverity.EXCEPTION,
//            "Unable to process a received event from topic {0} because its content is null",
//            "The system is unable to process an incoming event.",
//            "This may be caused by an internal logic error or the receipt of an incompatible OMRSEvent, " +
//                    "possibly from a later version of the OMRS protocol"),
    ;


	private AuditLogMessageDefinition messageDefinition;


    CognosAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
        messageDefinition = new AuditLogMessageDefinition(logMessageId, severity,logMessage, systemAction, userAction);
    }

	/**
	 * Retrieve a message definition object for an exception.  
	 * This method is used when there are no message inserts.
	 *
	 * @return message definition object.
	 */
    @Override
	public AuditLogMessageDefinition getMessageDefinition() {
		return messageDefinition;
	}


	/**
	 * Retrieve a message definition object for an exception.
	 * This method is used when there are values to be inserted into the message.
	 *
	 * @param params array of parameters (all strings).They are inserted into the message according to the numbering in the message text.
	 * @return message definition object.
	 */
	@Override
	public AuditLogMessageDefinition getMessageDefinition(String... params) {
		messageDefinition.setMessageParameters(params);
		return messageDefinition;
	}
	
	/**
	 * Build Audit log message from message definition of error code.
	 * @param msg message definition of the error code source.
	 * @param params array of parameters (all strings) inserted into the message according to the numbering in the message text.
	 * @return message that can be logged always is EXCEPTION severity.
	 * Note: it is often operation to log an exception - make it as AuditLogMessageDefinition interface.
	 */
	static public AuditLogMessageDefinition getAuditLogMessageDefinition(MessageDefinition msg, String... params) {
		AuditLogMessageDefinition message = new AuditLogMessageDefinition(
				msg.getMessageId(),	// add suffix like "-EX" to distinct from audit message with same id
				OMRSAuditLogRecordSeverity.EXCEPTION,
				msg.getMessageTemplate(),
				msg.getSystemAction(),
				msg.getUserAction()
			);
		message.setMessageParameters(params);
		return message;
	}
}
