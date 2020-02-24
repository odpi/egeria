/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OMAGAdminAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OMAGAdminAuditCode
{
    MAX_PAGE_SIZE("OMAG-ADMIN-0001",
                      OMRSAuditLogRecordSeverity.STARTUP,
                      "The {0} server is configured with a max page size of {1}",
                      "The server has been configured with a maximum page size.  This is a recommended approach.  The maximum " +
                          "page size value sets an upper limit on the number of results that a caller can request on any paging " +
                          "REST API to this server.  Setting maximum page size helps to prevent a denial of service attack that uses very " +
                          "large requests to overwhelm the server.",
                      "Validate that the setting of this value is adequate for the users of this server.  If the number is too small, " +
                          "callers will receive invalid parameter exceptions if they specify a maximum page size that is larger than this " +
                          "configured value."),

    UNLIMITED_MAX_PAGE_SIZE("OMAG-ADMIN-0002",
                            OMRSAuditLogRecordSeverity.STARTUP,
                            "The {0} server is configured with an unlimited maximum page size",
                            "The server has been configured with a maximum page size of zero.  This means a requester can use any paging " +
                                    "size that they need on a REST API call.  The down-side of this approach is that a server does not have" +
                                    "any defense against a denial of service attack that uses large requests to overwhelm the server.  It is not " +
                                    "recommended for a production environment.",
                            "It is recommended that this parameter is set to a positive integer that is large enough to satisfy legitimate " +
                                    "callers to the server.  The parameter is set in the server's configuration document."),

    INVALID_MAX_PAGE_SIZE("OMAG-ADMIN-0003",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The {0} server is configured with an invalid max page size of {1}",
                          "The server has been configured with a negative maximum page size.  " +
                                  "The maximum page size value sets an upper limit on the number of results that a caller can request on a" +
                                  "REST API call to this server.  Limiting this value to a negative number does not make any sense.",
                          "Update this parameter in the configuration document for this server.  " +
                                  "It should be set to a positive integer that is large enough to satisfy legitimate callers to the server."),

    SERVER_STARTUP_SUCCESS("OMAG-ADMIN-0004",
                          OMRSAuditLogRecordSeverity.STARTUP,
                          "The {0} server has successfully completed start up.  The following services are running: {1}",
                          "The request to start the server returns with a list of the services that were started.",
                          "Review the start up messages to ensure that all of the correct services have been started and the " +
                                   "are operating without errors."),

    SERVER_SHUTDOWN_STARTED("OMAG-ADMIN-0004",
                           OMRSAuditLogRecordSeverity.SHUTDOWN,
                           "The {0} server has begun the shutdown process",
                           "The request to stop the server has been issued, either through an explicit command, or because the" +
                                    "OMAG Server Platform is shutting down.  The operational admin services will sequentially shutdown " +
                                    "each of the server's running subsystems.",
                           "Review the shutdown messages to ensure that all of the services are shutting down without errors."),

    SERVER_SHUTDOWN_SUCCESS("OMAG-ADMIN-0004",
                           OMRSAuditLogRecordSeverity.SHUTDOWN,
                           "The {0} server has completed shutdown",
                           "The request to shutdown the server has completed.  No REST API calls nor events will be " +
                                    "processed by this server until it is restarted.",
                           "Review the shutdown messages to ensure that all of the subsystems have successfully released the" +
                                    "resources that they were using."),

    SERVER_SHUTDOWN_ERROR("OMAG-ADMIN-0005",
                            OMRSAuditLogRecordSeverity.SHUTDOWN,
                            "The {0} server has detected an {1} exception during server shutdown.  The error message was {2}",
                            "The request to shutdown the server has failed with an exception.  The server is in an " +
                                  "undetermined state.",
                            "Review the shutdown messages to ensure that all of the subsystems have successfully released the" +
                                    "resources that they were using.  Restart the server whenever its services are needed again."),

    STARTING_ACCESS_SERVICES("OMAG-ADMIN-0010",
        OMRSAuditLogRecordSeverity.STARTUP,
        "The Open Metadata Access Services (OMASs) are starting",
        "The operational admin services are initializing the access service subsystems in a metadata server instance.  " +
                "These provide specialist APIs for accessing open metadata.  Many of the access services support " +
                "both a REST API and event-based interaction through a topic.  They also support options that " +
                "control their behavior and the scope of the metadata that they work with.  The access service " +
                "subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.",
        "The server's configuration document lists the access services that should be started in this server.  " +
                "Verify that the expected access services are started and that they each report that their components are " +
                "working correctly."),

    SKIPPING_ACCESS_SERVICE("OMAG-ADMIN-0011",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The {0} is marked as DISABLED and so will not be started in the {1} server",
            "The operational admin services will skip the initialization of the access service subsystem " +
                    "in this metadata server because it is marked as disabled in the configuration document.",
            "The server's configuration document lists the access services that should be started in this server.  " +
                    "Verify that this access service should be disabled. If it should be enabled then change the definition" +
                    "of the access service in the configuration document to be enabled and restart the server."),

    ALL_ACCESS_SERVICES_STARTED("OMAG-ADMIN-0012",
            OMRSAuditLogRecordSeverity.STARTUP,
            "{0} out of {1} configured Open Metadata Access Services (OMASs) have started.",
            "The operational admin services have completed the initialization of all of the access service subsystems " +
                    "enabled in the metadata server.  They are ready for use.  An access services is configured by adding " +
                    "its configuration to the server's configuration document.  By default a newly configured access " +
                    "service is also ENABLED. A configured access service may be temporarily disabled in the configuration" +
                    "document.  In which case the start up sequence skips it and the number of started access services" +
                    "is less than the number of configured access services.",
            "Review the start up messages to ensure that all of the correct access services have been started " +
                    "and they are operating without errors."),

    STOPPING_ACCESS_SERVICES("OMAG-ADMIN-0013",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Open Metadata Access Services (OMASs) in server {0} are shutting down",
            "The server is in the process of The access services will be shut down one at a time.  When an access service competes" +
                    " its shutdown, " +
                    "it will no longer process events, nor REST API calls to this server.",
            "If the intention was to shutdown the access services, monitor the shutdown process to ensure that " +
                    "there are no errors reported.  "),

    ALL_ACCESS_SERVICES_STOPPED("OMAG-ADMIN-0014",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Open Metadata Access Services (OMASs) in server {0} have shutdown",
            "The access services are no longer available for the server.",
            "Validate that all resources used by the access services have been released."),

    ACCESS_SERVICE_INSTANCE_FAILURE("OMAG-ADMIN-0015",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The {0} access service is unable to initialize a new instance; error message is {1}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    BAD_ACCESS_SERVICE_ADMIN_CLASS("OMAG-ADMIN-0016",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The admin services are not able to start the {0} access service because the  {0} whilst initializing the {1} Open Metadata Access " +
                    "Service " +
                    "(OMAS); error message is {2}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                    "server."),

    ACCESS_SERVICE_FAILURE("OMAG-ADMIN-0017",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The admin services caught an unexpected {0} exception whilst initializing the {1} service. " +
                    " The error message is {2}",
            "The admin services detected an error during the start up of a specific access service subsystem.  " +
                    "Its services are not available for the server and so the server failed to start.  Details of the error " +
                    "that the access service detected are logged and a detailed exception is returned to the caller. ",
            "Review the error message and the other failures reported by the access service to determine the cause of the problem.  It is " +
                    "typically either " +
                    "a configuration error, or one of the external resources it needs, such as a topic on the event bus, is not  " +
                    "available. Access services may be developed by the Egeria community or a third party.  If the error messages " +
                    "are not sufficient to resolve the problem, raise an issue with the author of the access service to get this " +
                    "improved.  Once the root cause of the error is resolved, restart the server."),

    NULL_ACCESS_SERVICE_ADMIN_CLASS("OMAG-ADMIN-400-011 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system is unable to initialize this access service. The server failed to start.",
            "If the access service should be initialized then set up the appropriate admin services class name " +
                    "in the access service's configuration and restart the server instance. Otherwise, " +
                    "remove the configuration for this access service and restart the server."),

    STARTING_VIEW_SERVICES("OMAG-ADMIN-0020",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Open Metadata View Services (OMVSs) are starting",
            "The operational admin services are initializing the access service subsystems in a metadata server instance.  " +
                    "These provide specialist task orientated APIs for viewing open metadata.  The view services support " +
                    "a REST API. The view service subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.",
            "The server's configuration document lists the access services that should be started in this server.  " +
                    "Verify that the expected access services are started and that they each report that their components are " +
                    "working correctly."),

    SKIPPING_VIEW_SERVICE("OMAG-ADMIN-0021",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The {0} is marked as DISABLED and so will not be started in the {1} server",
            "The operational admin services will skip the initialization of the view service subsystem " +
                    "in this metadata server because it is marked as disabled in the configuration document.",
            "The server's configuration document lists the view services that should be started in this server.  " +
                    "Verify that this view service should be disabled. If it should be enabled then change the definition" +
                    "of the view service in the configuration document to be enabled and restart the server."),

    ALL_VIEW_SERVICES_STARTED("OMAG-ADMIN-0022",
            OMRSAuditLogRecordSeverity.STARTUP,
            "{0} out of {1} configured Open Metadata View Services (OMVSs) have started.",
            "The operational admin services have completed the initialization of all of the view service subsystems " +
                    "enabled in the metadata server.  They are ready for use.  An view services is configured by adding " +
                    "its configuration to the server's configuration document.  By default a newly configured view " +
                    "service is also ENABLED. A configured view service may be temporarily disabled in the configuration" +
                    "document.  In which case the start up sequence skips it and the number of started view services" +
                    "is less than the number of configured view services.",
            "Review the start up messages to ensure that all of the correct view services have been started " +
                    "and they are operating without errors."),

    STOPPING_VIEW_SERVICES("OMAG-ADMIN-0023",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Open Metadata View Services (OMVSs) in server {0} are shutting down",
            "The server is in the process of The view services will be shut down one at a time.  When an view service competes" +
                    " its shutdown, " +
                    "it will no longer process REST API calls to this server.",
            "If the intention was to shutdown the view services, monitor the shutdown process to ensure that " +
                    "there are no errors reported.  "),

    ALL_VIEW_SERVICES_STOPPED("OMAG-ADMIN-0024",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Open Metadata View Services (OMVSs) in server {0} have shutdown",
            "The view services are no longer available for the server.",
            "Validate that all resources used by the view services have been released."),

    VIEW_SERVICE_INSTANCE_FAILURE("OMAG-ADMIN-0025",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The {0} view service is unable to initialize a new instance; error message is {1}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    BAD_VIEW_SERVICE_ADMIN_CLASS("OMAG-ADMIN-0026",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The admin services are not able to start the {0} view service because the  {0} whilst initializing the {1} Open Metadata View " +
                    "Service " +
                    "(OMVS); error message is {2}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                    "server."),

    VIEW_SERVICE_FAILURE("OMAG-ADMIN-0027",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The admin services caught an unexpected {0} exception whilst initializing the {1} service. " +
                    " The error message is {2}",
            "The admin services detected an error during the start up of a specific view service subsystem.  " +
                    "Its services are not available for the server and so the server failed to start.  Details of the error " +
                    "that the view service detected are logged and a detailed exception is returned to the caller. ",
            "Review the error message and the other failures reported by the view service to determine the cause of the problem.  It is " +
                    "typically either " +
                    "a configuration error, or one of the external resources it needs, such as a topic on the event bus, is not  " +
                    "available. View services may be developed by the Egeria community or a third party.  If the error messages " +
                    "are not sufficient to resolve the problem, raise an issue with the author of the view service to get this " +
                    "improved.  Once the root cause of the error is resolved, restart the server."),

    NULL_VIEW_SERVICE_ADMIN_CLASS("OMAG-ADMIN-400-028 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The OMAG server {0} has been passed a null admin services class name for view service {1}",
            "The system is unable to initialize this view service. The server failed to start.",
            "If the view service should be initialized then set up the appropriate admin services class name " +
                    "in the view service's configuration and restart the server instance. Otherwise, " +
                    "remove the configuration for this view service and restart the server."),


    STARTING_GOVERNANCE_SERVICES("OMAG-ADMIN-0100",
                                 OMRSAuditLogRecordSeverity.STARTUP,
                                 "The governance services subsystem for the {0} called {1} is about to start",
                                 "The admin services are about to start the governance services subsystem.  It will begin to initialize, " +
                                         "logging start up messages to confirm that its internal components have successfully initialized.",
                                 "Review the start up messages as they occur to ensure the correct capability has been initialized."),

    GOVERNANCE_SERVICES_STARTED("OMAG-ADMIN-0101",
                                OMRSAuditLogRecordSeverity.STARTUP,
                                "The governance services subsystem for the {0} called {1} has completed start up",
                                "The governance services subsystem has completed its start up and reported no fatal errors.  " +
                                        "Its capability is operational.",
                                "Review the start up messages from the governance services to ensure all expected components have started and " +
                                        "are reporting no problems.  If no start up messages are produced by the governance services, it could be " +
                                        "that the governance services failed silently.  Try calling the external services to see if it is " +
                                        "operating.  Whether it is running successfully or failed silently, raise an issue with the Egeria " +
                                        "community to get the start up messages improved."),

    GOVERNANCE_SERVICE_FAILURE("OMAG-ADMIN-0102",
                           OMRSAuditLogRecordSeverity.EXCEPTION,
                           "The admin services caught an {0} exception whilst initializing the governance services subsystem for the " +
                                       "{1} called {2}; error message is {3}",
                           "The governance services subsystem detected an error during the start up of a specific server instance. " +
                                       "It has logged information about the type of error.  Its services are not " +
                                       "available and since these services are fundamental to the operation of the server, the " +
                                       "server fails to start.  An exception is returned to the external caller of this request to start the server.",
                           "Review the error message and the other reported failures from the governance services to determine the cause of the " +
                                       "problem.  Typically you are looking for either incorrect configuration or one of the resources it was" +
                                       "expecting is not available.  If there are no additional error messages then raise an issue with the Egeria " +
                                       "community to get this improved.  Once the root cause of the problem is resolved, restart the server."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(OMAGAdminAuditCode.class);


    /**
     * The constructor for OMAGAdminAuditCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerAuditCode above.   For example:
     *
     *     OMAGAdminAuditCode   auditCode = OMAGAdminAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OMAGAdminAuditCode(String                     messageId,
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
            log.debug(String.format("<== OMAG Admin Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> OMAG Admin Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
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
