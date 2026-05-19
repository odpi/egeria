/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OpenMetadataSecurityAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OpenMetadataSecurityAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-METADATA-SECURITY-0001 - The Open Metadata Security Service {0} for OMAG Server Platform {1} is initializing
     */
    PLATFORM_INITIALIZING("OPEN-METADATA-SECURITY-0001",
                          AuditLogRecordSeverityLevel.STARTUP,
                          "The Open Metadata Security Service {0} for OMAG Server Platform {1} is initializing",
                          "The local server has started up a new instance of the Open Metadata Platform Security Service Connector.",
                          "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-SECURITY-0002 - The Open Metadata Security Service {0} for OMAG Server Platform {1} is shutting down
     */
    PLATFORM_SHUTDOWN("OPEN-METADATA-SECURITY-0002",
                      AuditLogRecordSeverityLevel.SHUTDOWN,
                      "The Open Metadata Security Service {0} for OMAG Server Platform {1} is shutting down",
                      "The local administrator has requested shut down of the Open Metadata Platform Security Service Connector.",
                      "No action is required.  This is part of the normal operation of the service."),


    /**
     * OPEN-METADATA-SECURITY-0003 - The Open Metadata Security Service {0} for server {1} is initializing
     */
    SERVICE_INITIALIZING("OPEN-METADATA-SECURITY-0003",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Open Metadata Security Service {0} for server {1} is initializing",
                         "The local server has started up a new instance of the Open Metadata Server Security Service Connector.",
                         "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-SECURITY-0004 - The Open Metadata Security Service {0} for server {1} is shutting down
     */
    SERVICE_SHUTDOWN("OPEN-METADATA-SECURITY-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Open Metadata Security Service {0} for server {1} is shutting down",
                     "The local administrator has requested shut down of the Open Metadata Server Security Service Connector.",
                     "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-SECURITY-0005 - User {0} is not authorized to issue a request to this OMAG Server Platform
     */
    UNAUTHORIZED_PLATFORM_ACCESS("OPEN-METADATA-SECURITY-0005",
                                 AuditLogRecordSeverityLevel.SECURITY,
                                 "User {0} is not authorized to issue a {1} request to OMAG Server Platform {2}",
                                 "The security service detected an unauthorized access to an OMAG Server Platform.",
                                 "Review the security policies and settings to determine if this access should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0006 - User {0} is not authorized to issue a request to server {1}
     */
    UNAUTHORIZED_SERVER_ACCESS("OPEN-METADATA-SECURITY-0006",
                               AuditLogRecordSeverityLevel.SECURITY,
                               "User {0} is not authorized to issue a request to server {1}",
                               "The security service detected an unauthorized access to a service.",
                               "Review the security policies and settings to determine if this access should be allowed or not." +
                                       "  Take action to either change the security sessions or determine the reason for the unauthorized request."),


    /**
     * OPEN-METADATA-SECURITY-0007 - User {0} is not authorized to issue {1} requests for service {2} on server {3}
     */
    UNAUTHORIZED_SERVICE_ACCESS("OPEN-METADATA-SECURITY-0007",
                                AuditLogRecordSeverityLevel.SECURITY,
                                "User {0} is not authorized to issue {1} requests for service {2} on server {3}",
                                "The security service detected an unauthorized access to a service.",
                                "Review the security policies and settings to determine if this access should be allowed or not." +
                                        "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0008 - User {0} is not authorized to attach feedback to element {1}
     */
    UNAUTHORIZED_ADD_FEEDBACK("OPEN-METADATA-SECURITY-0008",
                              AuditLogRecordSeverityLevel.SECURITY,
                              "User {0} is not authorized to attach feedback to element {1}",
                              "The security service detected an unauthorized change to an element.",
                              "Review the security policies and settings to determine if this access should be allowed or not." +
                                      "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0009 - User {0} is not authorized to change the zone membership of element {1} from {2} to {3}
     */
    UNAUTHORIZED_ZONE_CHANGE("OPEN-METADATA-SECURITY-0009",
                             AuditLogRecordSeverityLevel.SECURITY,
                             "User {0} is not authorized to change the zone membership of element {1} from {2} to {3}",
                             "The security service detected an unauthorized change to an element.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0010 - User {0} is not authorized to use connection {1}
     */
    UNAUTHORIZED_CONNECTION_ACCESS("OPEN-METADATA-SECURITY-0010",
                                   AuditLogRecordSeverityLevel.SECURITY,
                                   "User {0} is not authorized to use connection {1}",
                                   "The security service detected an unauthorized access to an asset's connection.",
                                   "Review the security policies and settings to determine if this access should be allowed or not." +
                                           "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0011 - User {0} is not authorized to issue operation {1} on {2} anchor element {3}
     */
    UNAUTHORIZED_ANCHOR_ACCESS("OPEN-METADATA-SECURITY-0011",
                               AuditLogRecordSeverityLevel.SECURITY,
                               "User {0} is not authorized to issue operation {1} on {2} anchor element {3}",
                               "The security service detected an unauthorized access to a member of the anchor element.",
                               "Review the security policies and settings to determine if this access should be allowed or not." +
                                       "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0015 - User {0} is not authorized to access open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_ACCESS("OPEN-METADATA-SECURITY-0015",
                             AuditLogRecordSeverityLevel.SECURITY,
                             "User {0} is not authorized to access open metadata type {1} ({2}) on server {3}",
                             "The security service detected an unauthorized access of an open metadata type.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0016 - User {0} is not authorized to change open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_CHANGE("OPEN-METADATA-SECURITY-0016",
                             AuditLogRecordSeverityLevel.SECURITY,
                             "User {0} is not authorized to change open metadata type {1} ({2}) on server {3}",
                             "The security service detected an unauthorized change of an open metadata type.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0017 - User {0} is not authorized to create an open metadata instance of type {1} on server {2}
     */
    UNAUTHORIZED_INSTANCE_CREATE("OPEN-METADATA-SECURITY-0017",
                                 AuditLogRecordSeverityLevel.SECURITY,
                                 "User {0} is not authorized to create an open metadata instance of type {1} on server {2}",
                                 "The security service detected an unauthorized access of an open metadata type.",
                                 "Review the security policies and settings to determine if this access should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0018 - User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}
     */
    UNAUTHORIZED_INSTANCE_ACCESS("OPEN-METADATA-SECURITY-0018",
                                 AuditLogRecordSeverityLevel.SECURITY,
                                 "User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}",
                                 "The security service detected an unauthorized access of an open metadata instance.",
                                 "Review the security policies and settings to determine if this access should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0019 - User {0} is not authorized to change open metadata type {1} of type {2} on server {3} using method {4}
     */
    UNAUTHORIZED_INSTANCE_CHANGE("OPEN-METADATA-SECURITY-0019",
                                 AuditLogRecordSeverityLevel.SECURITY,
                                 "User {0} is not authorized to change open metadata type {1} of type {2} on server {3} using method {4}",
                                 "The security service detected an unauthorized change of an open metadata instance.",
                                 "Review the security policies and settings to determine if this access should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0020 - User {0} is not authorized to issue operation {1} on {2} element {3}
     */
    UNAUTHORIZED_ELEMENT_ACCESS("OPEN-METADATA-SECURITY-0020",
                                AuditLogRecordSeverityLevel.SECURITY,
                                "User {0} is not authorized to issue operation {1} on {2} element {3}",
                                "The security service detected an unauthorized access to an element.",
                                "Review the security policies and settings to determine if this access to the element should be allowed or not." +
                                        "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0021 - Element {1} is not visible to user {0}; it has been filtered from the search results
     */
    FILTERED_ELEMENT("OPEN-METADATA-SECURITY-0021",
                     AuditLogRecordSeverityLevel.INFO,
                     "Element {0} is not visible to user {1}; it has been filtered from the search results",
                     "The system has filtered an element from the results because the user does not have the necessary permissions to access it.",
                     "The element is filtered from the results."),

    /**
     * OPEN-METADATA-SECURITY-0022 - User {0} is not recognized
     */
    UNKNOWN_USER("OPEN-METADATA-SECURITY-0022",
                 AuditLogRecordSeverityLevel.SECURITY,
                 "User {0} is not recognized",
                 "The security service has received a request from an unknown user.",
                 "Track down the source of the request and either add the user to the user directory or prevent the user from accessing again."),

    /**
     * OPEN-METADATA-SECURITY-0023 - Exception {0} occurred when retrieving user {1}; the exception message was {2}
     */
    FAILED_TO_RETRIEVE_USER("OPEN-METADATA-SECURITY-0023",
                            AuditLogRecordSeverityLevel.SECURITY,
                            "Exception {0} occurred when retrieving user {1}; the exception message was {2}",
                            "An exception occurred when the security service tried to retrieve a user account.",
                            "Use the information in the exception to determine the cause of this error.  The user will not be granted access to the open metadata ecosystem."),

    /**
     * OPEN-METADATA-SECURITY-0024 - User {0} has expired credentials
     */
    EXPIRED_USER("OPEN-METADATA-SECURITY-0024",
                 AuditLogRecordSeverityLevel.SECURITY,
                 "User {0} has expired credentials",
                 "The security service has received a request from a user whose credentials have expired.",
                 "Track down the source of the request and encourage the used to rest their credentials (probably password)."),

    /**
     * OPEN-METADATA-SECURITY-0025 - Security access control {0} is not recognized
     */
    UNKNOWN_CONTROL("OPEN-METADATA-SECURITY-0025",
                    AuditLogRecordSeverityLevel.SECURITY,
                    "Security access control {0} is not recognized",
                    "The security service has received a request for an unknown control.",
                    "Track down the source of the request and correct the name of the control - or add the missing control to the secrets store."),

    /**
     * OPEN-METADATA-SECURITY-0026 - Exception {0} occurred when retrieving security access control {1}; the exception message was {2}
     */
    FAILED_TO_RETRIEVE_CONTROL("OPEN-METADATA-SECURITY-0026",
                               AuditLogRecordSeverityLevel.SECURITY,
                               "Exception {0} occurred when retrieving security access control {1}; the exception message was {2}",
                               "An exception occurred when the security service tried to retrieve a security access control.",
                               "Use the information in the exception to determine the cause of this error.  The control will not be returned to the calling user."),

    /**
     * OPEN-METADATA-SECURITY-0027 - Adding user {0} to the platform user directory
     */
    ADDING_USER("OPEN-METADATA-SECURITY-0027",
                AuditLogRecordSeverityLevel.SECURITY,
                "Adding user {0} to the platform user directory",
                "A user account is added or updated in the platform's user directory",
                "Make sure this user is valid and has the correct permissions."),

    /**
     * OPEN-METADATA-SECURITY-0028 - Removing user {0} from the platform user directory
     */
    REMOVING_USER("OPEN-METADATA-SECURITY-0028",
                AuditLogRecordSeverityLevel.SECURITY,
                "Removing user {0} from the platform user directory",
                "A user account is removed from the platform's user directory",
                "Make sure this user is no longer needed."),


    /**
     * OPEN-METADATA-SECURITY-0029 - Adding security access control {0} to the platform user directory
     */
    ADDING_CONTROL("OPEN-METADATA-SECURITY-0029",
                AuditLogRecordSeverityLevel.SECURITY,
                "Adding security access control {0} to the platform user directory",
                "A security access control is added or updated in the platform's user directory",
                "Make sure this control is valid and has the correct permissions."),

    /**
     * OPEN-METADATA-SECURITY-0030 - Removing security access control {0} from the platform user directory
     */
    REMOVING_CONTROL("OPEN-METADATA-SECURITY-0030",
                  AuditLogRecordSeverityLevel.SECURITY,
                  "Removing security access control {0} from the platform user directory",
                  "A security access control is removed from the platform's user directory.",
                  "Make sure this control is no longer needed."),

    /**
     * OPEN-METADATA-SECURITY-0099 - The security connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("OPEN-METADATA-SECURITY-0099",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The security connector received an unexpected {0} exception during method {1}; the error message was: {2}",
                         "The connector cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for OpenMetadataSecurityAuditCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataSecurityAuditCode above.   For example:
     * <br>
     * OpenMetadataSecurityAuditCode   auditCode = OpenMetadataSecurityAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique identifier for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    OpenMetadataSecurityAuditCode(String messageId,
                                  AuditLogRecordSeverityLevel severity,
                                  String message,
                                  String systemAction,
                                  String userAction)
    {
        this.logMessageId = messageId;
        this.severity     = severity;
        this.logMessage   = message;
        this.systemAction = systemAction;
        this.userAction   = userAction;
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
        return "OpenMetadataObservabilityAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
