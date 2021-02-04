/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The GovernanceEngineAuditLog is used to define the message content for the OMRS Audit Log.
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
public enum GovernanceEngineAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-GOVERNANCE-ENGINE-0001",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The Governance Engine Open Metadata Access Service (OMAS) is initializing a new server instance",
                         "The local server has started up a new instance of the Governance Engine OMAS.  It will initialize " +
                                 "the interfaces for governance engine configuration, governance action process configuration, " +
                                 "and management of governance actions.  It will also begin publishing governance engine " +
                                 "configuration changes to its out topic.",
                         "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional."),

    SERVICE_REGISTERED_WITH_TOPIC("OMAS-GOVERNANCE-ENGINE-0002",
                                  OMRSAuditLogRecordSeverity.STARTUP,
                                  "The Governance Engine Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server instance {0}",
                                  "The Governance Engine OMAS is registering the server instance to receive events from the connected open metadata repositories.",
                                  "No action is required.  This is part of the normal operation of the service that is used to monitor " +
                                          "for changing metadata."),

    SERVICE_PUBLISHING("OMAS-GOVERNANCE-ENGINE-0004",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Governance Engine Open Metadata Access Service (OMAS) is ready to publish configuration refresh notifications to topic {0}",
                       "The local server has started up the event publisher for the Governance Engine OMAS.  " +
                               "It will begin publishing governance engine configuration changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),

    SERVICE_INITIALIZED("OMAS-GOVERNANCE-ENGINE-0005",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The Governance Engine Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
                        "The Governance Engine OMAS has completed initialization of a new server instance.",
                        "Verify that there are no error messages logged by the service.  If there are none it means that " +
                                "all parts of the service initialized successfully."),

    SERVICE_INSTANCE_FAILURE("OMAS-GOVERNANCE-ENGINE-0006",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The Governance Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
                             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    EVENT_PROCESSING_ERROR("OMAS-GOVERNANCE-ENGINE-0007",
                           OMRSAuditLogRecordSeverity.EXCEPTION,
                           "The Governance Engine Open Metadata Access Service (OMAS) encounter an exception while processing event from {0} of type {1} for instance {2}",
                           "The event could not be processed",
                           "Review the exception to determine the source of the error and correct it if necessary."),


    UNEXPECTED_INITIALIZATION_EXCEPTION("OMAS-GOVERNANCE-ENGINE-0008",
                                        OMRSAuditLogRecordSeverity.EXCEPTION,
                                        "The Governance Engine Open Metadata Access Service (OMAS) detected an unexpected {0} exception during the " +
                                                "initialization of its services; error message is {1}",
                                        "The access service detected an error during the start up of a specific server instance.  Its services are not available " +
                                                "for the server and an error is returned to the caller.",
                                        "Review the error message and any other reported failures to determine the cause of the problem.  In particular consider the" +
                                                " state of the Event Bus.  Once this is resolved, restart the server."),

    SERVICE_TERMINATING("OMAS-GOVERNANCE-ENGINE-0009",
                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                        "The Governance Engine Open Metadata Access Service (OMAS) is shutting down server instance {0}",
                        "The local handlers has requested shut down of the Governance Engine OMAS.",
                        "No action is required.  This is part of the normal operation of the service."),

    PUBLISHING_SHUTDOWN("OMAS-GOVERNANCE-ENGINE-0010",
                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                        "The Governance Engine Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an Governance Engine OMAS instance.  " +
                                "No more configuration events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    PUBLISHING_SHUTDOWN_ERROR("OMAS-GOVERNANCE-ENGINE-0011",
                              OMRSAuditLogRecordSeverity.SHUTDOWN,
                              "The Governance Engine Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an Governance Engine OMAS instance.  " +
                                      "No more configuration events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    SERVICE_SHUTDOWN("OMAS-GOVERNANCE-ENGINE-0012",
                     OMRSAuditLogRecordSeverity.SHUTDOWN,
                     "The Governance Engine Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
                     "The local administrator has requested shut down of an Governance Engine OMAS instance.  " +
                             "The governance engine interfaces are no longer available and no configuration events will " +
                             "be published to the out topic",
                     "This is part of the normal shutdown of the service.  Verify that all resources have been released."),

    REFRESH_GOVERNANCE_ENGINE("OMAS-GOVERNANCE-ENGINE-0013",
                             OMRSAuditLogRecordSeverity.INFO,
                             "The Governance Engine Open Metadata Access Service (OMAS) sent notification that the configuration for governance engine {0} " +
                                     "({1}) has changed",
                             "The access service sends out configuration notifications to ensure connected governance servers have the most up to-date " +
                                     "configuration about the governance engine.",
                             "This is part of the normal operation of the service.  No action is required if this change is expected beyond verifying that" +
                                     " the affected governance engines are updated in the governance servers.  If the change is " +
                                     "unexpected, use the " +
                                     "Governance Engine OMAS configuration interface to query the status of the governance engine properties.  " +
                                     "It should be possible to trace the source of the update to correct it."),

    REFRESH_GOVERNANCE_SERVICE("OMAS-GOVERNANCE-ENGINE-0014",
                               OMRSAuditLogRecordSeverity.INFO,
                               "The Governance Engine Open Metadata Access Service (OMAS) sent notification that governance engine {0} ({1}) had a " +
                                       "configuration change for governance request type {2} mapped to registered governance service {3}",
                               "The access service sends out configuration notifications to ensure connected governance engine servers have the most up to-date " +
                                       "configuration about the governance service.",
                               "This is part of the normal operation of the service.  No action is required if this change is expected beyond verifying that" +
                                       " the affected governance engines are updated with the latest governance services information.  If the change " +
                                       "is unexpected, use the " +
                                       "Governance Engine OMAS configuration interface to query the status of the registered governance services for " +
                                       "the affected governance engine.  It should be possible to trace the source of the update to correct it."),

    NEW_GOVERNANCE_ACTION("OMAS-GOVERNANCE-ENGINE-0015",
                               OMRSAuditLogRecordSeverity.INFO,
                               "The Governance Engine Open Metadata Access Service (OMAS) sent notification of a new governance action {0} for governance engine {1} ({2})",
                               "The access service sends out notifications about new governance actions to governance engines so " +
                                       "they can claim it and execute the requested governance service.",
                               "This is part of the normal operation of the service.  No action is required if this action is expected " +
                                       "beyond verifying that the requested action is picked up and executed."),

    BAD_GOVERNANCE_ACTION("OMAS-GOVERNANCE-ENGINE-0016",
                          OMRSAuditLogRecordSeverity.ERROR,
                          "The Governance Engine Open Metadata Access Service (OMAS) sent notification of a new governance action with no governance engine executor: {0}",
                          "The access service sends out notifications about new governance actions to governance engines so " +
                                  "they can claim it and execute the requested governance service.  However, this governance action does not have information " +
                                  "about the governance engine.",
                          "Review the governance action in the metadata repository.  Try to work out how it was set up.  Is it linked to a " +
                                  "governance engine? If not, why not? If it is then why was the governance action not populated with the governance engine name?"),

    WATCHDOG_EVENT("OMAS-GOVERNANCE-ENGINE-0017",
                          OMRSAuditLogRecordSeverity.INFO,
                          "The Governance Engine Open Metadata Access Service (OMAS) sent a metadata change event to listening Open Watchdog Governance Action Services: {0}",
                          "The access service sends out metadata change events when metadata instances change (with the exception of metadata " +
                                  "associated with processing governance services).  These events are passed to listening Open Watchdog Governance Action Services " +
                                  "as long as the event matches the criteria that where specified when the listener was registered.",
                          "This is part of the normal operation of the service.  Verify that these events are being received by the " +
                                  "watchdog governance action services."),

    OUT_TOPIC_FAILURE("OMAS-GOVERNANCE-ENGINE-0018",
                      OMRSAuditLogRecordSeverity.EXCEPTION,
                      "The Governance Engine Open Metadata Access Service (OMAS) is unable to send an event on its out topic {0}; exception {1} returned " +
                              "error message: {2}",
                      "The access service detected an error during the start up of the out topic.  Its services are not available for the server.",
                      "Review the error message and any other reported failures to determine the cause of the problem.  Check the status of the event " +
                              "bus.  Once this is resolved, restart the server."),

    OUT_TOPIC_EVENT("OMAS-GOVERNANCE-ENGINE-0019",
                    OMRSAuditLogRecordSeverity.EVENT,
                    "The Governance Engine Open Metadata Access Service (OMAS) has sent event of type {0} ",
                    "The access service sends out configuration notifications to ensure connected governance servers have the most up to-date " +
                            "configuration.  This message is to create a record of the events that are being published.",
                    "This event indicates that the configuration for a governance engine, or governance service has changed.  " +
                            "Check that each connected governance server receives this event and updates its configuration if " +
                            "the change affects their operation."),

    ASSET_AUDIT_LOG("OMAS-GOVERNANCE-ENGINE-0020",
                    OMRSAuditLogRecordSeverity.INFO,
                    "Log message for asset {0} from governance service {1}: {2}",
                    "A governance service has logged a message about an asset.",
                    "Review the message to ensure no action is required."),
    ;


    private AuditLogMessageDefinition messageDefinition;

    /**
     * The constructor for GovernanceEngineAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceEngineAuditCode above.   For example:
     * <p>
     * GovernanceEngineAuditCode   auditCode = GovernanceEngineAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    GovernanceEngineAuditCode(String                     messageId,
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
    public AuditLogMessageDefinition getMessageDefinition(String... params)
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
        return "GovernanceEngineAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
