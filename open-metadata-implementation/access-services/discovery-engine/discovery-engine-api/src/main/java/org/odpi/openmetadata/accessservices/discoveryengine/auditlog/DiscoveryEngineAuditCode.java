/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DiscoveryEngineAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DiscoveryEngineAuditCode
{
    SERVICE_INITIALIZING("OMAS-DISCOVERY-ENGINE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Discovery Engine Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Discovery Engine OMAS.  It will initialize " +
                                 "Open Discovery Framework (ODF) interfaces for discovery engine configuration, asset catalog search, " +
                                 "asset properties and the annotation store.  It will also begin publishing discovery engine " +
                                 "configuration changes to its out topic.",
             "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional."),

    SERVICE_PUBLISHING("OMAS-DISCOVERY-ENGINE-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Discovery Engine Open Metadata Access Service (OMAS) is ready to publish configuration refresh notifications to topic {0}",
            "The local server has started up the event publisher for the Discovery Engine OMAS.  " +
                               "It will begin publishing discovery engine configuration changes to its out topic.",
            "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional."),

    SERVICE_INITIALIZED("OMAS-DISCOVERY-ENGINE-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Discovery Engine Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The Discovery Engine OMAS has completed initialization of a new server instance.",
             "Verify that there are no error messages logged by the service.  If there are none it means that " +
                                "all parts of the service initialized successfully."),

    SERVICE_INSTANCE_FAILURE("OMAS-DISCOVERY-ENGINE-0004",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Discovery Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    UNEXPECTED_INITIALIZATION_EXCEPTION("OMAS-DISCOVERY-ENGINE-0005",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Discovery Engine Open Metadata Access Service (OMAS) detected an unexpected {0} exception during the " +
                                                "initialization of its services; error message is {1}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available " +
                                                "for the server and an error is returned to the caller.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    PUBLISHING_SHUTDOWN("OMAS-DISCOVERY-ENGINE-0006",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Discovery Engine Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
             "The local administrator has requested shut down of an Discovery Engine OMAS instance.  " +
                             "No more configuration events will be published to the named topic.",
             "This is part of the normal shutdown of the service.   No action is required if this is service" +
                             "shutdown was intentional."),

    PUBLISHING_SHUTDOWN_ERROR("OMAS-DISCOVERY-ENGINE-0007",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Discovery Engine Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
             "The local administrator has requested shut down of an Discovery Engine OMAS instance.  " +
                                "No more configuration events will be published to the named topic.",
             "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    SERVICE_SHUTDOWN("OMAS-DISCOVERY-ENGINE-0008",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Discovery Engine Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
            "The local administrator has requested shut down of an Discovery Engine OMAS instance.  " +
                             "The Open Discovery Framework (ODF) interfaces are no longer available and no configuration events will " +
                             "be published to the out topic",
            "This is part of the normal shutdown of the service.   No action is required if this is service" +
                             "shutdown was intentional."),

    REFRESH_DISCOVERY_ENGINE("OMAS-DISCOVERY-ENGINE-0009",
            OMRSAuditLogRecordSeverity.INFO,
            "The Discovery Engine Open Metadata Access Service (OMAS) sent notification that the configuration for discovery engine {0} " +
                                     "({1}) has changed",
            "The access service sends out configuration notifications to ensure connected discovery servers have the most up to-date " +
                                     "configuration.",
            "This is part of the normal operation of the service.  No action is required if this change is expected beyond verifying that" +
                                     " the affected discovery engines are updated in the discovery servers.  If the change is " +
                                     "unexpected, use the" +
                                     "Discovery Engine OMAS configuration interface to query the status of the discovery engine properties.  " +
                                     "It should be possible to trace the source of the update to correct it."),

    REFRESH_DISCOVERY_SERVICE("OMAS-DISCOVERY-ENGINE-0010",
             OMRSAuditLogRecordSeverity.INFO,
             "The Discovery Engine Open Metadata Access Service (OMAS) sent notification that discovery engine {0} ({1}) had a " +
                                      "configuration change for discovery request types {2} that map to registered discovery service {3}",
             "The access service sends out configuration notifications to ensure connected discovery servers have the most up to-date " +
                                      "configuration.",
              "This is part of the normal operation of the service.  No action is required if this change is expected beyond verifying that" +
                                      " the affected discovery engines are updated with the latest discovery services information.  If the change " +
                                      "is unexpected, use the" +
                                      "Discovery Engine OMAS configuration interface to query the status of the registered discovery services for " +
                                      "the affected discovery engine.  It should be possible to trace the source of the update to correct it."),


    OUT_TOPIC_FAILURE("OMAS-DISCOVERY-ENGINE-0011",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Discovery Engine Open Metadata Access Service (OMAS) is unable to send an event on its out topic {0}; exception {1} returned " +
                              "error message: {2}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    OUT_TOPIC_EVENT("OMAS-DISCOVERY-ENGINE-0012",
             OMRSAuditLogRecordSeverity.EVENT,
             "The Discovery Engine Open Metadata Access Service (OMAS) has sent event: {0}",
             "The access service sends out configuration notifications to ensure connected discovery servers have the most up to-date " +
                            "configuration.",
             "This event indicates that the configuration for a discovery engine, or discovery service has changed.  " +
                            "Check that each connected discovery server receives this event and updates its configuration if " +
                            "the change affects their operation."),

    ASSET_AUDIT_LOG("OMAS-DISCOVERY-SERVER-0013",
             OMRSAuditLogRecordSeverity.INFO,
            "Log message for asset {0} from discovery service {1}: {2}",
            "A discovery service has logged a message about an asset.",
            "Review the message to ensure no action is required.")
    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineAuditCode.class);


    /**
     * The constructor for DiscoveryEngineAuditCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineAuditCode above.   For example:
     *
     *     DiscoveryEngineAuditCode   auditCode = DiscoveryEngineAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DiscoveryEngineAuditCode(String                     messageId,
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
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== DiscoveryEngine Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> DiscoveryEngine Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
