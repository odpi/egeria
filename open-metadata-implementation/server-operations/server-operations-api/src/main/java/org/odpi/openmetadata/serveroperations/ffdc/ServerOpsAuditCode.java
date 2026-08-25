/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The ServerOpsAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum ServerOpsAuditCode implements AuditLogMessageSet
{
    /**
     * SERVER-OPS-0001 - The {0} server is configured with a max page size of {1}
     */
    MAX_PAGE_SIZE("SERVER-OPS-0001",
                  AuditLogRecordSeverityLevel.STARTUP,
                  "The {0} server is configured with a max page size of {1}",
                  "The server has been configured with a maximum page size.  This is a recommended approach.  The maximum " +
                          "page size value sets an upper limit on the number of results that a caller can request on any paging " +
                          "REST API to this server.  Setting maximum page size helps to prevent a denial of service attack that uses very " +
                          "large requests to overwhelm the server.",
                  "Validate that the setting of this value is adequate for the users of this server.  If the number is too small, " +
                          "callers will receive invalid parameter exceptions if they specify a maximum page size that is larger than this " +
                          "configured value.",
                          "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0002 - The {0} server is configured with an unlimited maximum page size
     */
    UNLIMITED_MAX_PAGE_SIZE("SERVER-OPS-0002",
                            AuditLogRecordSeverityLevel.STARTUP,
                            "The {0} server is configured with an unlimited maximum page size",
                            "The server has been configured with a maximum page size of zero.  This means a requester can use any paging " +
                                    "size that they need on a REST API call.  The down-side of this approach is that a server does not have" +
                                    "any defense against a denial of service attack that uses large requests to overwhelm the server.  It is not " +
                                    "recommended for a production environment.",
                            "It is recommended that this parameter is set to a positive integer that is large enough to satisfy legitimate " +
                                    "callers to the server.  The parameter is set in the server's configuration document.",
                                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0003 - The {0} server is configured with an invalid max page size of {1}
     */
    INVALID_MAX_PAGE_SIZE("SERVER-OPS-0003",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "The {0} server is configured with an invalid max page size of {1}",
                          "The server has been configured with a negative maximum page size.  " +
                                  "The maximum page size value sets an upper limit on the number of results that a caller can request on a" +
                                  "REST API call to this server.  Limiting this value to a negative number does not make any sense.",
                          "Update this parameter in the configuration document for this server.  " +
                                  "It should be set to a positive integer that is large enough to satisfy legitimate callers to the server.",
                                  "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0004 - The {0} server has successfully completed start up.  The following services are running: {1}
     */
    SERVER_STARTUP_SUCCESS("SERVER-OPS-0004",
                          AuditLogRecordSeverityLevel.STARTUP,
                          "The {0} server has successfully completed start up.  The following services are running: {1}",
                          "The request to start the server returns with a list of the services that were started.",
                          "Review the start up messages to ensure that all the correct services have been started and the " +
                                   "are operating without errors.",
                                   "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0005 - The {0} server has begun the shutdown process
     */
    SERVER_SHUTDOWN_STARTED("SERVER-OPS-0005",
                           AuditLogRecordSeverityLevel.SHUTDOWN,
                           "The {0} server has begun the shutdown process",
                           "The request to stop the server has been issued, either through an explicit command, or because the" +
                                    "OMAG Server Platform is shutting down.  The operational admin services will sequentially shutdown " +
                                    "each of the server's running subsystems.",
                           "Review the shutdown messages to ensure that all the services are shutting down without errors.",
                           "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0006 - The {0} server has completed shutdown
     */
    SERVER_SHUTDOWN_SUCCESS("SERVER-OPS-0006",
                           AuditLogRecordSeverityLevel.SHUTDOWN,
                           "The {0} server has completed shutdown",
                           "The request to shutdown the server has completed.  No REST API calls nor events will be " +
                                    "processed by this server until it is restarted.",
                           "Review the shutdown messages to ensure that all the subsystems have successfully released the" +
                                    "resources that they were using.",
                                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0007 - The {0} server has detected an {1} exception during server shutdown.  The error message was {2}
     */
    SERVER_SHUTDOWN_ERROR("SERVER-OPS-0007",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "The {0} server has detected an {1} exception during server shutdown.  The error message was {2}",
                            "The request to shutdown the server has failed with an exception.  The server is in an " +
                                  "undetermined state.",
                            "Review the shutdown messages to ensure that all the subsystems have successfully released the" +
                                    "resources that they were using.  Restart the server whenever its services are needed again.",
                                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0010 - The Open Metadata Access Services (OMASs) are starting
     */
    STARTING_ACCESS_SERVICES("SERVER-OPS-0010",
        AuditLogRecordSeverityLevel.STARTUP,
        "The Open Metadata Access Services (OMASs) are starting",
        "The operational admin services are initializing the access service subsystems in a metadata server instance.  " +
                "These provide specialist APIs for accessing open metadata.  Many of the access services support " +
                "both a REST API and event-based interaction through a topic.  They also support options that " +
                "control their behavior and the scope of the metadata that they work with.  The access service " +
                "subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.",
        "The server's configuration document lists the access services that should be started in this server.  " +
                "Verify that the expected access services are started and that they each report that their components are " +
                "working correctly.",
                "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0011 - The {0} is marked as DISABLED and so will not be started in the {1} server
     */
    SKIPPING_ACCESS_SERVICE("SERVER-OPS-0011",
            AuditLogRecordSeverityLevel.STARTUP,
            "The {0} is marked as DISABLED and so will not be started in the {1} server",
            "The operational admin services will skip the initialization of the access service subsystem " +
                    "in this metadata server because it is marked as disabled in the configuration document.",
            "The server's configuration document lists the access services that should be started in this server.  " +
                    "Verify that this access service should be disabled. If it should be enabled then change the definition" +
                    "of the access service in the configuration document to be enabled and restart the server.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0012 - {0} out of {1} configured Open Metadata Access Services (OMASs) have started
     */
    ALL_ACCESS_SERVICES_STARTED("SERVER-OPS-0012",
            AuditLogRecordSeverityLevel.STARTUP,
            "{0} out of {1} configured Open Metadata Access Services (OMASs) have started",
            "The operational admin services have completed the initialization of all the access service subsystems " +
                    "enabled in the metadata server.  They are ready for use.  An access services is configured by adding " +
                    "its configuration to the server's configuration document.  By default a newly configured access " +
                    "service is also ENABLED. A configured access service may be temporarily disabled in the configuration" +
                    "document.  In which case the start up sequence skips it and the number of started access services" +
                    "is less than the number of configured access services.",
            "Review the start up messages to ensure that all the correct access services have been started " +
                    "and they are operating without errors.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0015 - The {0} access service cannot initialize a new instance; error message is {1}
     */
    ACCESS_SERVICE_INSTANCE_FAILURE("SERVER-OPS-0015",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The {0} access service cannot initialize a new instance; error message is {1}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.",
            "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0016 - The admin services are not able to start the {0} access service because the admin service class {1} is invalid; error message is {2}
     */
    BAD_ACCESS_SERVICE_ADMIN_CLASS("SERVER-OPS-0016",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The admin services are not able to start the {0} access service because the admin service class {1} is invalid; error message is {2}",
            "The admin services was unable to create an instance of the admin service class for the access service during the start up of a " +
                                           "specific server instance.  The server fails to start.",
            "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                    "server.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0018 - The OMAG server {0} has been passed a null admin services class name for access service {1}
     */
    NULL_ACCESS_SERVICE_ADMIN_CLASS("SERVER-OPS-0018",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system cannot initialize this access service. The server failed to start.",
            "If the access service should be initialized then set up the appropriate admin services class name " +
                    "in the access service's configuration and restart the server instance. Otherwise, " +
                    "remove the configuration for this access service and restart the server.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0020 - The Open Metadata View Services (OMVSs) are starting
     */
    STARTING_VIEW_SERVICES("SERVER-OPS-0020",
            AuditLogRecordSeverityLevel.STARTUP,
            "The Open Metadata View Services (OMVSs) are starting",
            "The operational admin services are initializing the view service subsystems in a metadata server instance.  " +
                    "These provide specialist task orientated APIs for viewing open metadata.  The view services support " +
                    "a REST API. The view service subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.",
            "The server's configuration document lists the view services that should be started in this server.  " +
                    "Verify that the expected view services are started and that they each report that their components are " +
                    "working correctly.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0021 - The {0} is marked as DISABLED and so will not be started in the {1} view server
     */
    SKIPPING_VIEW_SERVICE("SERVER-OPS-0021",
            AuditLogRecordSeverityLevel.STARTUP,
            "The {0} is marked as DISABLED and so will not be started in the {1} view server",
            "The operational admin services will skip the initialization of the view service subsystem " +
                    "in this view server because it is marked as disabled in the configuration document.",
            "The server's configuration document lists the view services that should be started in this server.  " +
                    "Verify that this view service should be disabled. If it should be enabled then change the definition" +
                    "of the view service in the configuration document to be enabled and restart the server.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0022 - {0} out of {1} configured Open Metadata View Services (OMVSs) have started
     */
    ALL_CONFIGURED_VIEW_SERVICES_STARTED("SERVER-OPS-0022",
                                         AuditLogRecordSeverityLevel.STARTUP,
                                         "{0} out of {1} configured Open Metadata View Services (OMVSs) have started; the active urlMarkers are: {2}",
                                         "The operational admin services have completed the initialization of all the configured view service subsystems " +
                                                 "enabled in the view server.  They are ready for use.  An view service is configured by adding " +
                                                 "its configuration to the server's configuration document.  By default a newly configured view " +
                                                 "service is also ENABLED. A configured view service may be temporarily disabled in the configuration" +
                                                 "document.  In which case the start up sequence skips it and the number of started view services" +
                                                 "is less than the number of configured view services.",
                                         "Review the start up messages to ensure that all the correct view services have been started " +
                                                 "and they are operating without errors.",
                                                 "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0025 - The {0} view service cannot initialize a new instance; error message is {1}
     */
    VIEW_SERVICE_INSTANCE_FAILURE("SERVER-OPS-0025",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The {0} view service cannot initialize a new instance; error message is {1}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem with the view service.  Once this is " +
                                          "resolved, restart the view server.",
                                          "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0026 - The admin services are not able to start the {0} view service because the admin service class {1} is invalid; error message is {2}
     */
    BAD_VIEW_SERVICE_ADMIN_CLASS("SERVER-OPS-0026",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The admin services are not able to start the {0} view service because the admin service class {1} is invalid; error message is {2}",
            "The admin services are unable to create an instance of the view service's admin class during the start up of a specific server " +
                                         "instance.  The server fails to start.",
            "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                    "view server.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0028 - The OMAG server {0} has been passed a null admin services class name for view service {1}
     */
    NULL_VIEW_SERVICE_ADMIN_CLASS("SERVER-OPS-0028",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The OMAG server {0} has been passed a null admin services class name for view service {1}",
            "The system cannot initialize this view service. The server failed to start.",
            "If the view service should be initialized then set up the appropriate admin services class name " +
                    "in the view service's configuration and restart the server instance. Otherwise, " +
                    "remove the configuration for this view service and restart the view server.",
                    "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0029 - The OMAG server {0} is activating generic view services that are not configured; these view services can only be called using a urlMarker of a configured service
     */
    ACTIVATING_UNCONFIGURED_GENERIC_VIEW_SERVICES("SERVER-OPS-0029",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The OMAG server {0} is activating generic view services that are not configured; these view services can only be called using a urlMarker of a configured service",
                                  "The system is initializing any of the generic view services that are not configured.  These services can only be used if called using a urlMarker from one of the configured services.",
                                  "Check whether these view services should be properly configured.",
                                  "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0022 - {0} unconfigured Open Metadata View Services (OMVSs) have started
     */
    ALL_UNCONFIGURED_GENERIC_VIEW_SERVICES_STARTED("SERVER-OPS-0030",
                              AuditLogRecordSeverityLevel.STARTUP,
                              "{0} unconfigured generic Open Metadata View Services (OMVSs) have started",
                              "The operational admin services have completed the initialization of all the unconfigured generic view service subsystems " +
                                      "enabled in the view server.  They are ready for use, but they may only be called using a urlMarker of a configured view service.  An view service is configured by adding " +
                                      "its configuration to the server's configuration document.",
                              "Review the start up messages to ensure that all the view services have been started " +
                                      "and they are operating without errors.",
                                      "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0100 - The governance services subsystem for the {0} called {1} is about to start
     */
    STARTING_GOVERNANCE_SERVICES("SERVER-OPS-0100",
                                 AuditLogRecordSeverityLevel.STARTUP,
                                 "The governance services subsystem for the {0} called {1} is about to start",
                                 "The admin services are about to start the governance services subsystem.  It will begin to initialize, " +
                                         "logging start up messages to confirm that its internal components have successfully initialized.",
                                 "Review the start up messages as they occur to ensure the correct capability has been initialized in the " +
                                         "governance server.",
                                         "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0101 - The governance services subsystem for the {0} called {1} has completed start up
     */
    GOVERNANCE_SERVICES_STARTED("SERVER-OPS-0101",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "The governance services subsystem for the {0} called {1} has completed start up",
                                "The governance services subsystem has completed its start up and reported no fatal errors.  " +
                                        "Its capability is operational.",
                                "Review the start up messages from the governance services to ensure all expected components have started and " +
                                        "are reporting no problems.  If no start up messages are produced by the governance services, it could be " +
                                        "that the governance services failed silently.  Try calling the external services to see if it is " +
                                        "operating.  Whether it is running successfully or failed silently, raise an issue with the Egeria " +
                                        "community to get the start up messages improved.",
                                        "https://egeria-project.org/services/server-operations/"),

    /**
     * SERVER-OPS-0102 - The admin services caught an {0} exception whilst initializing the governance services subsystem for the
     * {1} called {2}; error message is {3}
     */
    GOVERNANCE_SERVICE_FAILURE("SERVER-OPS-0102",
                           AuditLogRecordSeverityLevel.EXCEPTION,
                           "The admin services caught an {0} exception whilst initializing the governance services subsystem for the " +
                                       "{1} called {2}; error message is {3}",
                           "The governance services subsystem detected an error during the start up of a specific server instance. " +
                                       "It has logged information about the type of error.  Its services are not " +
                                       "available and since these services are fundamental to the operation of the server, the " +
                                       "server fails to start.  An exception is returned to the external caller of this request to start the server.",
                           "Review the error message and the other reported failures from the governance services to determine the cause of the " +
                                       "problem.  Typically you are looking for either incorrect configuration or one of the resources it was" +
                                       "expecting is not available.  If there are no additional error messages then raise an issue with the Egeria " +
                                       "community to get this improved.  Once the root cause of the problem is resolved, restart the server.",
                                       "https://egeria-project.org/services/server-operations/"),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;
    private final String                     url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    ServerOpsAuditCode(String                     messageId,
                       AuditLogRecordSeverityLevel severity,
                       String                     message,
                       String                     systemAction,
                       String                     userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for ServerOpsAuditCode expects to be passed one of the enumeration rows defined in
     * ServerOpsAuditCode above.   For example:
     * <br><br>
     *     ServerOpsAuditCode   auditCode = ServerOpsAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    ServerOpsAuditCode(String                     messageId,
                       AuditLogRecordSeverityLevel severity,
                       String                     message,
                       String                     systemAction,
                       String                     userAction,
                       String                     url)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                             userAction,
                                             url);
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
                                                                                    userAction,
                                                                                    url);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }
}
