/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


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
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The Open Metadata Security Service {0} for OMAG Server Platform {1} is initializing",
                         "The local server has started up a new instance of the Open Metadata Platform Security Service Connector.",
                         "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-SECURITY-0002 - The Open Metadata Security Service {0} for OMAG Server Platform {1} is shutting down
     */
    PLATFORM_SHUTDOWN("OPEN-METADATA-SECURITY-0002",
                     OMRSAuditLogRecordSeverity.SHUTDOWN,
                     "The Open Metadata Security Service {0} for OMAG Server Platform {1} is shutting down",
                     "The local administrator has requested shut down of the Open Metadata Platform Security Service Connector.",
                     "No action is required.  This is part of the normal operation of the service."),


    /**
     * OPEN-METADATA-SECURITY-0003 - The Open Metadata Security Service {0} for server {1} is initializing
     */
    SERVICE_INITIALIZING("OPEN-METADATA-SECURITY-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Open Metadata Security Service {0} for server {1} is initializing",
             "The local server has started up a new instance of the Open Metadata Server Security Service Connector.",
             "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-SECURITY-0004 - The Open Metadata Security Service {0} for server {1} is shutting down
     */
    SERVICE_SHUTDOWN("OPEN-METADATA-SECURITY-0004",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Open Metadata Security Service {0} for server {1} is shutting down",
             "The local administrator has requested shut down of the Open Metadata Server Security Service Connector.",
             "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-SECURITY-0005 - User {0} is not authorized to issue a request to this OMAG Server Platform
     */
    UNAUTHORIZED_PLATFORM_ACCESS("OPEN-METADATA-SECURITY-0005",
             OMRSAuditLogRecordSeverity.SECURITY,
             "User {0} is not authorized to issue a request to this OMAG Server Platform",
             "The security service detected an unauthorized access to an OMAG Server Platform.",
             "Review the security policies and settings to determine if this access should be allowed or not." +
                                        "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0006 - User {0} is not authorized to issue a request to server {1}
     */
    UNAUTHORIZED_SERVER_ACCESS("OPEN-METADATA-SECURITY-0006",
                               OMRSAuditLogRecordSeverity.SECURITY,
                               "User {0} is not authorized to issue a request to server {1}",
                               "The security service detected an unauthorized access to a service.",
                               "Review the security policies and settings to determine if this access should be allowed or not." +
                                       "  Take action to either change the security sessions or determine the reason for the unauthorized request."),


    /**
     * OPEN-METADATA-SECURITY-0007 - User {0} is not authorized to issue {1} requests for service {2} on server {3}
     */
    UNAUTHORIZED_SERVICE_ACCESS("OPEN-METADATA-SECURITY-0007",
                                OMRSAuditLogRecordSeverity.SECURITY,
                                "User {0} is not authorized to issue {1} requests for service {2} on server {3}",
                                "The security service detected an unauthorized access to a service.",
                                "Review the security policies and settings to determine if this access should be allowed or not." +
                                        "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0008 - User {0} is not authorized to attach feedback to asset {1}
     */
    UNAUTHORIZED_ASSET_FEEDBACK("OPEN-METADATA-SECURITY-0008",
              OMRSAuditLogRecordSeverity.SECURITY,
             "User {0} is not authorized to attach feedback to asset {1}",
             "The security service detected an unauthorized change to an asset.",
             "Review the security policies and settings to determine if this access should be allowed or not." +
                                        "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0009 - User {0} is not authorized to change the zone membership of asset {1} from {2} to {3}
     */
    UNAUTHORIZED_ZONE_CHANGE("OPEN-METADATA-SECURITY-0009",
                             OMRSAuditLogRecordSeverity.SECURITY,
                             "User {0} is not authorized to change the zone membership of asset {1} from {2} to {3}",
                             "The security service detected an unauthorized change to an asset.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0010 - User {0} is not authorized to use connection {1}
     */
    UNAUTHORIZED_CONNECTION_ACCESS("OPEN-METADATA-SECURITY-0010",
             OMRSAuditLogRecordSeverity.SECURITY,
             "User {0} is not authorized to use connection {1}",
             "The security service detected an unauthorized access to an asset.",
             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0011 - User {0} is not authorized to access asset {1}
     */
    UNAUTHORIZED_ASSET_ACCESS("OPEN-METADATA-SECURITY-0011",
                             OMRSAuditLogRecordSeverity.SECURITY,
                             "User {0} is not authorized to access asset {1}",
                             "The security service detected an unauthorized access to an asset.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0012 - User {0} is not authorized to create an asset of type {1}
     */
    UNAUTHORIZED_ASSET_CREATE("OPEN-METADATA-SECURITY-0012",
                               OMRSAuditLogRecordSeverity.SECURITY,
                              "User {0} is not authorized to create an asset of type {1}",
                              "The security service detected an unauthorized create of an asset.",
                              "Review the security policies and settings to determine if this create should be allowed or not." +
                                      "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0013 - User {0} is not authorized to change asset {1}
     */
    UNAUTHORIZED_ASSET_CHANGE("OPEN-METADATA-SECURITY-0013",
                              OMRSAuditLogRecordSeverity.SECURITY,
                              "User {0} is not authorized to change asset {1}",
                              "The security service detected an unauthorized access to an asset.",
                              "Review the security policies and settings to determine if this access should be allowed or not." +
                                      "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0014 - User {0} is not authorized to change asset {1} because it has missing properties: {2}
     */
    INCOMPLETE_ASSET(         "OPEN-METADATA-SECURITY-0014",
                              OMRSAuditLogRecordSeverity.SECURITY,
                              "User {0} is not authorized to change asset {1} because it has missing properties: {2}",
                              "The system is unable to process a request from the user because the asset is not correctly or completely filled out.",
                              "The request fails with a UserNotAuthorizedException exception."),

    /**
     * OPEN-METADATA-SECURITY-0015 - User {0} is not authorized to access open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_ACCESS("OPEN-METADATA-SECURITY-0015",
                              OMRSAuditLogRecordSeverity.SECURITY,
                              "User {0} is not authorized to access open metadata type {1} ({2}) on server {3}",
                              "The security service detected an unauthorized access of an open metadata type.",
                              "Review the security policies and settings to determine if this access should be allowed or not." +
                                      "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0016 - User {0} is not authorized to change open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_CHANGE("OPEN-METADATA-SECURITY-0016",
                             OMRSAuditLogRecordSeverity.SECURITY,
                             "User {0} is not authorized to change open metadata type {1} ({2}) on server {3}",
                             "The security service detected an unauthorized change of an open metadata type.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0017 - User {0} is not authorized to create an open metadata instance of type {1} on server {2}
     */
    UNAUTHORIZED_INSTANCE_CREATE("OPEN-METADATA-SECURITY-0017",
                                 OMRSAuditLogRecordSeverity.SECURITY,
                                 "User {0} is not authorized to create an open metadata instance of type {1} on server {2}",
                                 "The security service detected an unauthorized access of an open metadata type.",
                                 "Review the security policies and settings to determine if this access should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0018 - User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}
     */
    UNAUTHORIZED_INSTANCE_ACCESS("OPEN-METADATA-SECURITY-0018",
                             OMRSAuditLogRecordSeverity.SECURITY,
                             "User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}",
                             "The security service detected an unauthorized access of an open metadata type.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0019 - User {0} is not authorized to change open metadata type {1} of type {2} on server {3} using method {4}
     */
    UNAUTHORIZED_INSTANCE_CHANGE("OPEN-METADATA-SECURITY-0019",
                             OMRSAuditLogRecordSeverity.SECURITY,
                             "User {0} is not authorized to change open metadata type {1} of type {2} on server {3} using method {4}",
                             "The security service detected an unauthorized change of an open metadata instance.",
                             "Review the security policies and settings to determine if this access should be allowed or not." +
                                     "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0020 - User {0} is not authorized to issue operation {1} on glossary {2}
     */
    UNAUTHORIZED_GLOSSARY_ACCESS("OPEN-METADATA-SECURITY-0020",
                              OMRSAuditLogRecordSeverity.SECURITY,
                              "User {0} is not authorized to issue operation {1} on glossary {2}",
                              "The security service detected an unauthorized access to a glossary.",
                              "Review the security policies and settings to determine if this access to a glossary should be allowed or not." +
                                      "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OPEN-METADATA-SECURITY-0021 - User {0} is not authorized to issue operation {1} because the glossary is null
     */
    NULL_GLOSSARY("OPEN-METADATA-SECURITY-0021",
                              OMRSAuditLogRecordSeverity.SECURITY,
                              "User {0} is not authorized to issue operation {1} because the glossary is null",
                              "The system is unable to process a request from the user because the glossary element is not correctly anchored on a glossary.",
                              "The request fails with a UserNotAuthorizedException exception. Add the anchor relationship of the glossary element to its glossary and corresponding Anchors classification.  When both are in place, re-run the request."),

    /**
     * OPEN-METADATA-SECURITY-0050 - User {0} retrieved {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_READ("OPEN-METADATA-SECURITY-0050",
                  OMRSAuditLogRecordSeverity.ACTIVITY,
                  "User {0} retrieved {1} asset {2} during operation {3} of service {4}",
                  "This message is used to capture user requests to receive an asset.",
                  "No action is required, but this message can be used to capture user activity information."),


    /**
     * OPEN-METADATA-SECURITY-0051 - User {0} retrieved an attachment to {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_READ_ATTACHMENT("OPEN-METADATA-SECURITY-0051",
                        OMRSAuditLogRecordSeverity.ACTIVITY,
                        "User {0} retrieved an attachment to {1} asset {2} during operation {3} of service {4}",
                        "This message is used to capture user requests to receive full details about an asset.",
                        "No action is required, but this message can be used to capture user activity information about the use of assets."),


    /**
     * OPEN-METADATA-SECURITY-0052 - User {0} updated an attachment to {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_UPDATE_ATTACHMENT("OPEN-METADATA-SECURITY-0052",
                                   OMRSAuditLogRecordSeverity.ACTIVITY,
                                   "User {0} updated an attachment to {1} asset {2} during operation {3} of service {4}",
                                   "This message is used to capture user requests to build out the knowledge for an asset.",
                                   "No action is required, but this message can be used to capture user activity information related to curation of an asset."),


    /**
     * OPEN-METADATA-SECURITY-0053 - User {0} updated feedback related to {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_UPDATE_FEEDBACK("OPEN-METADATA-SECURITY-0053",
                                   OMRSAuditLogRecordSeverity.ACTIVITY,
                                   "User {0} updated feedback related to {1} asset {2} during operation {3} of service {4}",
                                   "This message is used to capture user requests to maintain feedback on an asset.",
                                   "No action is required, but this message can be used to capture user activity information related to the maintenance of feedback attached to an asset."),

    /**
     * OPEN-METADATA-SECURITY-0054 - User {0} updated {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_UPDATE("OPEN-METADATA-SECURITY-0054",
                                   OMRSAuditLogRecordSeverity.ACTIVITY,
                                   "User {0} updated {1} asset {2} during operation {3} of service {4}",
                                   "This message is used to capture user requests to update an asset.",
                                   "No action is required, but this message can be used to capture user activity information related to asset updates."),

    /**
     * OPEN-METADATA-SECURITY-0055 - User {0} deleted {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_DELETE("OPEN-METADATA-SECURITY-0055",
                          OMRSAuditLogRecordSeverity.ACTIVITY,
                          "User {0} deleted {1} asset {2} during operation {3} of service {4}",
                          "This message is used to capture user requests to delete an asset.",
                          "No action is required, but this message can be used to capture user activity information related to asset deletion."),

    ;

    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for OpenMetadataSecurityAuditCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataSecurityAuditCode above.   For example:
     * <br>
     *     OpenMetadataSecurityAuditCode   auditCode = OpenMetadataSecurityAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OpenMetadataSecurityAuditCode(String                     messageId,
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
        return "OpenMetadataSecurityAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
