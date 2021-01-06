/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The CommunityProfileAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum CommunityProfileAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-COMMUNITY-PROFILE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Community Profile Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Community Profile OMAS.  This service " +
                                 "supports the personal profiles used in the user interface.  It also manages organization," +
                                 "departmental and community " +
                                 "information and will reward individual contribution to open metadata as karma points.  " +
                                 "It can receive a feed of the latest user, organizational and profile information through " +
                                 "a feed from another system and will publish significant events such as whenever an individual " +
                                 "reaches a karma point plateau.",
             "No action is required if this service is required."),

    SERVICE_INITIALIZED("OMAS-COMMUNITY-PROFILE-0002",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Community Profile Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "Verify that he service started successfully.  You should see the start up of both the in topic and out topic " +
                                "as well as information about the karma point threshold that is in operation."),

    SERVICE_SHUTDOWN("OMAS-COMMUNITY-PROFILE-0003",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Community Profile Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local server has requested shut down of an Community Profile OMAS instance.",
             "No action is required if shutdown was requested."),

    SERVICE_INSTANCE_FAILURE("OMAS-COMMUNITY-PROFILE-0005",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Community Profile Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The Community Profile OMAS detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    OUTBOUND_EVENT("OMAS-COMMUNITY-PROFILE-0006",
             OMRSAuditLogRecordSeverity.EVENT,
             "The Community Profile Open Metadata Access Service (OMAS) has sent an event of type {0} on its out topic.  Event subject is {1}",
             "The Community Profile OMAS has detected a situation that results in an outbound event.",
             "This message is for capturing a record of all of the events send on the out topic.  If a permanent record is needed " +
                           "of these events, then ensure there is an audit log destination that sends log records to permanent storage."),

    INBOUND_EVENT("OMAS-COMMUNITY-PROFILE-0007",
             OMRSAuditLogRecordSeverity.EVENT,
             "The Community Profile Open Metadata Access Service (OMAS) has received an event of type {0} on its in topic.  Event subject is {1}",
             "The Community Profile OMAS has detected an incoming event.",
             "This message is for capturing a record of all of the events received on the in topic.  If a permanent record is needed " +
                          "of these events, then ensure there is an audit log destination that sends log records to permanent storage."),

    KARMA_PLATEAU_AWARD("OMAS-COMMUNITY-PROFILE-0008",
             OMRSAuditLogRecordSeverity.INFO,
            "{0} has reached a new karma point plateau of {1} with {2} karma points",
            "The Community Profile OMAS has detected a contribution by an individual that takes them to the next plateau.",
            "This information is also sent on the out topic and could result in additional recognition for the individual."),

    KARMA_POINT_EXCEPTION("OMAS-COMMUNITY-PROFILE-0009",
             OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to award karma points to {0} due to exception {1}.  The error message from the exception was {2}",
            "The system detected an exception whilst attempting to award karma points.  No karma points were awarded.",
            "Investigate and correct the source of the error.  Once fixed, karma points will be awarded for new activity."),

    OUTBOUND_EVENT_EXCEPTION("OMAS-COMMUNITY-PROFILE-0010",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to send an outbound event for instance with unique identifier of {0} and type name {1} due to exception {2}.  The error message from the exception was {3}",
            "The system detected an exception whilst attempting to send an event to the out topic.  No event is sent.",
            "Investigate and correct the source of the error.  Once fixed, events will be sent.")
    ;

    private AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for CommunityProfileAuditCode expects to be passed one of the enumeration rows defined in
     * CommunityProfileAuditCode above.   For example:
     *
     *     CommunityProfileAuditCode   auditCode = CommunityProfileAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    CommunityProfileAuditCode(String                     messageId,
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
        return "CommunityProfileAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
