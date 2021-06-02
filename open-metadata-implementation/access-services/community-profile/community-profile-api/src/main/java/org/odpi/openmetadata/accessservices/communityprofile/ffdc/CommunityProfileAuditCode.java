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

    SERVICE_PUBLISHING("OMAS-COMMUNITY-PROFILE-0002",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Community Profile Open Metadata Access Service (OMAS) is ready to publish data manager notifications to topic {0}",
                       "The local server has started up the event publisher for the Community Profile OMAS.  " +
                               "It will begin publishing data manager metadata changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),

    SERVICE_INSTANCE_FAILURE("OMAS-COMMUNITY-PROFILE-0003",
                             OMRSAuditLogRecordSeverity.EXCEPTION,
                             "The Community Profile Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
                             "The Community Profile OMAS detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVICE_INITIALIZED("OMAS-COMMUNITY-PROFILE-0004",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Community Profile Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "Verify that he service started successfully.  You should see the start up of both the in topic and out topic " +
                                "as well as information about the karma point threshold that is in operation."),

    SERVICE_SHUTDOWN("OMAS-COMMUNITY-PROFILE-0005",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Community Profile Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local server has requested shut down of an Community Profile OMAS instance.",
             "No action is required if shutdown was requested."),

    PUBLISHING_SHUTDOWN("OMAS-COMMUNITY-PROFILE-0006",
                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                        "The Community Profile Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an Community Profile OMAS instance.  " +
                                "No more events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    PUBLISHING_SHUTDOWN_ERROR("OMAS-COMMUNITY-PROFILE-0007",
                              OMRSAuditLogRecordSeverity.SHUTDOWN,
                              "The Community Profile Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an Community Profile OMAS instance.  " +
                                      "No more events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    OUTBOUND_ENTITY_EVENT("OMAS-COMMUNITY-PROFILE-0010",
                          OMRSAuditLogRecordSeverity.EVENT,
                          "The Community Profile Open Metadata Access Service (OMAS) has sent an entity element event of type {0} on its out topic.  {1} event subject is {2}",
                          "The Community Profile OMAS has detected a situation that results in an outbound entity element event.",
                          "This message is for capturing a record of all of the events send on the out topic.  If a permanent record is needed " +
                                  "of these entity element events, then ensure there is an audit log destination that sends log records to permanent storage."),

    OUTBOUND_RELATIONSHIP_EVENT("OMAS-COMMUNITY-PROFILE-0011",
                          OMRSAuditLogRecordSeverity.EVENT,
                          "The Community Profile Open Metadata Access Service (OMAS) has sent a relationship event of type {0} on its out topic.  {1} relationship subject is {2} and is connecting {3} {4} to {5} {6}",
                          "The Community Profile OMAS has detected a situation that results in an outbound event about an entity element.",
                          "This message is for capturing a record of all of the entity events send on the out topic.  If a permanent record is needed " +
                                  "of these events, then ensure there is an audit log destination that sends log records to permanent storage."),

    OUTBOUND_CLASSIFICATION_EVENT("OMAS-COMMUNITY-PROFILE-0012",
                                OMRSAuditLogRecordSeverity.EVENT,
                                "The Community Profile Open Metadata Access Service (OMAS) has sent an event of type {0} on its out topic.  {1} event subject is {2} and the classification changed was {3}",
                                "The Community Profile OMAS has detected a situation that results in an outbound event about a change to the classifications of an entity element.",
                                "This message is for capturing a record of all of the classification events send on the out topic.  If a permanent record is needed " +
                                        "of these events, then ensure there is an audit log destination that sends log records to permanent storage."),

    OUTBOUND_EVENT_EXCEPTION("OMAS-COMMUNITY-PROFILE-0013",
                             OMRSAuditLogRecordSeverity.EXCEPTION,
                             "Unable to send an outbound event of type {0} for instance with unique identifier of {1} and type name {2} due to exception {3}.  The error message from the exception was {4}",
                             "The system detected an exception whilst attempting to send an event to the out topic.  No event is sent.",
                             "Investigate and correct the source of the error.  Once fixed, events will be sent."),

    KARMA_PLATEAU_AWARD("OMAS-COMMUNITY-PROFILE-0100",
             OMRSAuditLogRecordSeverity.INFO,
            "{0} has reached a new karma point plateau of {1} with {2} karma points",
            "The Community Profile OMAS has detected a contribution by an individual that takes them to the next plateau.",
            "This information is also sent on the out topic and could result in additional recognition for the individual."),

    KARMA_POINT_EXCEPTION("OMAS-COMMUNITY-PROFILE-0101",
             OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to award karma points to {0} due to exception {1}.  The error message from the exception was {2}",
            "The system detected an exception whilst attempting to award karma points.  No karma points were awarded.",
            "Investigate and correct the source of the error.  Once fixed, karma points will be awarded for new activity."),


    OUT_TOPIC_EVENT("OMAS-COMMUNITY-PROFILE-0200",
                    OMRSAuditLogRecordSeverity.EVENT,
                    "The Community Profile Open Metadata Access Service (OMAS) has sent event: {0}",
                            "The access service sends out notifications about changes to relevant elements.  " +
                            "This message is to create a record of the events that are being published.",
                            "This event indicates that one of the elements has changed."),
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
