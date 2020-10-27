/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.auditlog;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The AnalyticsModelingAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum AnalyticsModelingAuditCode implements AuditLogMessageSet {

    SERVICE_INITIALIZING("OMAS-ANALYTICS-MODELING-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Analytics Modeling Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Analytics Modeling OMAS.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_OUT_TOPIC("OMAS-ANALYTICS-MODELING-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Analytics Modeling Open Metadata Access Service (OMAS) is registering a publisher with the Analytics Modeling Out topic {0}",
            "The Analytics Modeling OMAS is registering to publish events to Analytics Modeling Out topic.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-ANALYTICS-MODELING-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Analytics Modeling Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Analytics Modeling OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-ANALYTICS-MODELING-0006",
            OMRSAuditLogRecordSeverity.INFO,
            "The Analytics Modeling Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Analytics Modeling OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),

    ERROR_INITIALIZING_CONNECTION("OMAS-ANALYTICS-MODELING-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Analytics Modeling Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SERVICE_INSTANCE_TERMINATION_FAILURE("OMAS-ANALYTICS-MODELING-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Analytics Modeling Open Metadata Access Service (OMAS) is unable to terminate an instance {0}",
            "The access service detected an error during the shut down of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, try to shut " +
                                                 "down the server."), 
    FIND_ENTITIES("OMAS-ANALYTICS-MODELING-0009",
    		OMRSAuditLogRecordSeverity.INFO,
    		"Retrieving entities of type {0} with properties {1}.",
            "The local server has requested find entities operation.",
            "No action is required.  This is part of the normal operation of the server."),

    ;


	private final AuditLogMessageDefinition messageDefinition;


    AnalyticsModelingAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
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
