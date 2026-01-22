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
                          "configured value."),

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
                                    "callers to the server.  The parameter is set in the server's configuration document."),

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
                                  "It should be set to a positive integer that is large enough to satisfy legitimate callers to the server."),

    /**
     * SERVER-OPS-0004 - The {0} server has successfully completed start up.  The following services are running: {1}
     */
    SERVER_STARTUP_SUCCESS("SERVER-OPS-0004",
                          AuditLogRecordSeverityLevel.STARTUP,
                          "The {0} server has successfully completed start up.  The following services are running: {1}",
                          "The request to start the server returns with a list of the services that were started.",
                          "Review the start up messages to ensure that all the correct services have been started and the " +
                                   "are operating without errors."),

    /**
     * SERVER-OPS-0005 - The {0} server has begun the shutdown process
     */
    SERVER_SHUTDOWN_STARTED("SERVER-OPS-0005",
                           AuditLogRecordSeverityLevel.SHUTDOWN,
                           "The {0} server has begun the shutdown process",
                           "The request to stop the server has been issued, either through an explicit command, or because the" +
                                    "OMAG Server Platform is shutting down.  The operational admin services will sequentially shutdown " +
                                    "each of the server's running subsystems.",
                           "Review the shutdown messages to ensure that all the services are shutting down without errors."),

    /**
     * SERVER-OPS-0006 - The {0} server has completed shutdown
     */
    SERVER_SHUTDOWN_SUCCESS("SERVER-OPS-0006",
                           AuditLogRecordSeverityLevel.SHUTDOWN,
                           "The {0} server has completed shutdown",
                           "The request to shutdown the server has completed.  No REST API calls nor events will be " +
                                    "processed by this server until it is restarted.",
                           "Review the shutdown messages to ensure that all the subsystems have successfully released the" +
                                    "resources that they were using."),

    /**
     * SERVER-OPS-0007 - The {0} server has detected an {1} exception during server shutdown.  The error message was {2}
     */
    SERVER_SHUTDOWN_ERROR("SERVER-OPS-0007",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "The {0} server has detected an {1} exception during server shutdown.  The error message was {2}",
                            "The request to shutdown the server has failed with an exception.  The server is in an " +
                                  "undetermined state.",
                            "Review the shutdown messages to ensure that all the subsystems have successfully released the" +
                                    "resources that they were using.  Restart the server whenever its services are needed again."),

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
                "working correctly."),

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
                    "of the access service in the configuration document to be enabled and restart the server."),

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
                    "and they are operating without errors."),

    /**
     * SERVER-OPS-0013 - The Open Metadata Access Services (OMASs) in server {0} are shutting down
     */
    STOPPING_ACCESS_SERVICES("SERVER-OPS-0013",
            AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Open Metadata Access Services (OMASs) in server {0} are shutting down",
            "The server is in the process of terminating. The access services will be shut down one at a time.  When an access service completes" +
                    " its shutdown, " +
                    "it will no longer process events, nor REST API calls to this server.",
            "If the intention was to shutdown the access services, monitor the shutdown process to ensure that " +
                    "there are no errors reported.  "),

    /**
     * SERVER-OPS-0014 - The Open Metadata Access Services (OMASs) in server {0} have shutdown
     */
    ALL_ACCESS_SERVICES_STOPPED("SERVER-OPS-0014",
            AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Open Metadata Access Services (OMASs) in server {0} have shutdown",
            "The access services are no longer available for the server.",
            "Validate that all resources used by the access services have been released."),

    /**
     * SERVER-OPS-0015 - The {0} access service cannot initialize a new instance; error message is {1}
     */
    ACCESS_SERVICE_INSTANCE_FAILURE("SERVER-OPS-0015",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The {0} access service cannot initialize a new instance; error message is {1}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * SERVER-OPS-0016 - The admin services are not able to start the {0} access service because the admin service class {1} is invalid; error message is {2}
     */
    BAD_ACCESS_SERVICE_ADMIN_CLASS("SERVER-OPS-0016",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The admin services are not able to start the {0} access service because the admin service class {1} is invalid; error message is {2}",
            "The admin services was unable to create an instance of the admin service class for the access service during the start up of a " +
                                           "specific server instance.  The server fails to start.",
            "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                    "server."),

    /**
     * SERVER-OPS-0017 - The admin services caught an unexpected {0} exception whilst initializing the {1} service.
     * The error message is {2}
     */
    ACCESS_SERVICE_FAILURE("SERVER-OPS-0017",
            AuditLogRecordSeverityLevel.EXCEPTION,
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

    /**
     * SERVER-OPS-0018 - The OMAG server {0} has been passed a null admin services class name for access service {1}
     */
    NULL_ACCESS_SERVICE_ADMIN_CLASS("SERVER-OPS-0018",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system cannot initialize this access service. The server failed to start.",
            "If the access service should be initialized then set up the appropriate admin services class name " +
                    "in the access service's configuration and restart the server instance. Otherwise, " +
                    "remove the configuration for this access service and restart the server."),

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
                    "working correctly."),

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
                    "of the view service in the configuration document to be enabled and restart the server."),

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
                                                 "and they are operating without errors."),

    /**
     * SERVER-OPS-0023 - The Open Metadata View Services (OMVSs) in server {0} are shutting down
     */
    STOPPING_VIEW_SERVICES("SERVER-OPS-0023",
            AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Open Metadata View Services (OMVSs) in server {0} are shutting down",
            "The server is in the process of terminating. The view services will be shut down one at a time.  When an view service completes" +
                    " its shutdown, " +
                    "it will no longer process REST API calls to this server.",
            "If the intention was to shutdown the view services, monitor the shutdown process to ensure that " +
                    "there are no errors reported."),

    /**
     * SERVER-OPS-0024 - The Open Metadata View Services (OMVSs) in server {0} have shutdown
     */
    ALL_VIEW_SERVICES_STOPPED("SERVER-OPS-0024",
            AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Open Metadata View Services (OMVSs) in server {0} have shutdown",
            "The view services are no longer available for the server.",
            "Validate that all resources used by the view services have been released."),

    /**
     * SERVER-OPS-0025 - The {0} view service cannot initialize a new instance; error message is {1}
     */
    VIEW_SERVICE_INSTANCE_FAILURE("SERVER-OPS-0025",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The {0} view service cannot initialize a new instance; error message is {1}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem with the view service.  Once this is " +
                                          "resolved, restart the view server."),

    /**
     * SERVER-OPS-0026 - The admin services are not able to start the {0} view service because the admin service class {1} is invalid; error message is {2}
     */
    BAD_VIEW_SERVICE_ADMIN_CLASS("SERVER-OPS-0026",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The admin services are not able to start the {0} view service because the admin service class {1} is invalid; error message is {2}",
            "The admin services are unable to create an instance of the view service's admin class during the start up of a specific server " +
                                         "instance.  The server fails to start.",
            "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                    "view server."),

    /**
     * SERVER-OPS-0027 - The admin services caught an unexpected {0} exception whilst initializing the {1} service for a view server.
     * The error message is {2}
     */
    VIEW_SERVICE_FAILURE("SERVER-OPS-0027",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The admin services caught an unexpected {0} exception whilst initializing the {1} service for a view server. " +
                    " The error message is {2}",
            "The admin services detected an error during the start up of a specific view service subsystem.  " +
                    "Its services are not available for the server and so the server failed to start.  Details of the error " +
                    "that the view service detected are logged and a detailed exception is returned to the caller. ",
            "Review the error message and the other failures reported by the view service to determine the cause of the problem.  It is " +
                    "typically either " +
                    "a configuration error, or one of the external resources it needs, such as a topic on the event bus, is not  " +
                    "available. View services may be developed by the Egeria community or a third party.  If the error messages " +
                    "are not sufficient to resolve the problem, raise an issue with the author of the view service to get this " +
                    "improved.  Once the root cause of the error is resolved, restart the view server."),

    /**
     * SERVER-OPS-0028 - The OMAG server {0} has been passed a null admin services class name for view service {1}
     */
    NULL_VIEW_SERVICE_ADMIN_CLASS("SERVER-OPS-0028",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The OMAG server {0} has been passed a null admin services class name for view service {1}",
            "The system cannot initialize this view service. The server failed to start.",
            "If the view service should be initialized then set up the appropriate admin services class name " +
                    "in the view service's configuration and restart the server instance. Otherwise, " +
                    "remove the configuration for this view service and restart the view server."),

    /**
     * SERVER-OPS-0029 - The OMAG server {0} is activating generic view services that are not configured; these view services can only be called using a urlMarker of a configured service
     */
    ACTIVATING_UNCONFIGURED_GENERIC_VIEW_SERVICES("SERVER-OPS-0029",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The OMAG server {0} is activating generic view services that are not configured; these view services can only be called using a urlMarker of a configured service",
                                  "The system is initializing any of the generic view services that are not configured.  These services can only be used if called using a urlMarker from one of hte configured services.",
                                  "Check whether these view services should be properly configured."),

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
                                      "and they are operating without errors."),
    /**
     * SERVER-OPS-0040 - The Open Metadata Engine Services (OMESs) are starting
     */
    STARTING_ENGINE_SERVICES("SERVER-OPS-0040",
                           AuditLogRecordSeverityLevel.STARTUP,
                           "The Open Metadata Engine Services (OMESs) are starting",
                           "The operational admin services are initializing the engine service subsystems in a metadata server instance.  " +
                                   "These provide support for specialist governance engines.  The engine services support " +
                                   "a REST API. The engine service subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.",
                           "The server's configuration document lists the engine services that should be started in this server.  " +
                                   "Verify that the expected engine services are started and that they each report that their components are " +
                                   "working correctly."),

    /**
     * SERVER-OPS-0041- The {0} engine service is marked as DISABLED and so will not be started in the {1} engine host server
     */
    SKIPPING_ENGINE_SERVICE("SERVER-OPS-0041",
                          AuditLogRecordSeverityLevel.STARTUP,
                          "The {0} engine service is marked as DISABLED and so will not be started in the {1} engine host server",
                          "The operational admin services will skip the initialization of the engine service subsystem " +
                                  "in this OMAG server because it is marked as disabled in the configuration document.",
                          "The server's configuration document lists the engine services that should be started in this server.  " +
                                  "Verify that this engine service should be disabled. If it should be enabled then change the definition" +
                                  "of the engine service in the configuration document to be enabled and restart the server."),

    /**
     * SERVER-OPS-0042 - {0} out of {1} configured Open Metadata Engine Services (OMESs) have started
     */
    ALL_ENGINE_SERVICES_STARTED("SERVER-OPS-0042",
                              AuditLogRecordSeverityLevel.STARTUP,
                              "{0} out of {1} configured Open Metadata Engine Services (OMESs) have started",
                              "The operational admin services have completed the initialization of all the engine service subsystems " +
                                      "enabled in the engine host server.  They are ready for use.  An engine service is configured by adding " +
                                      "its configuration to the server's configuration document.  By default a newly configured engine " +
                                      "service is also ENABLED. A configured engine service may be temporarily disabled in the configuration" +
                                      "document.  In which case the start up sequence skips it and the number of started engine services" +
                                      "is less than the number of configured engine services.",
                              "Review the start up messages to ensure that all the correct engine services have been started " +
                                      "and they are operating without errors."),

    /**
     * SERVER-OPS-0043 - The Open Metadata Engine Services (OMESs) in server {0} are shutting down
     */
    STOPPING_ENGINE_SERVICES("SERVER-OPS-0043",
                           AuditLogRecordSeverityLevel.SHUTDOWN,
                           "The Open Metadata Engine Services (OMESs) in server {0} are shutting down",
                           "The server is in the process of terminating. The engine services will be shut down one at a time.  " +
                                   "When an engine service completes its shutdown, " +
                                   "it will no longer process events nor REST API calls in this server.",
                           "If the intention was to shutdown the engine services, monitor the shutdown process to ensure that " +
                                   "there are no errors reported."),

    /**
     * SERVER-OPS-0044 - The Open Metadata Engine Services (OMESs) in server {0} have shutdown
     */
    ALL_ENGINE_SERVICES_STOPPED("SERVER-OPS-0044",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                              "The Open Metadata Engine Services (OMESs) in server {0} have shutdown",
                              "The engine services are no longer available for the server.",
                              "Validate that all resources used by the engine services have been released."),

    /**
     * SERVER-OPS-0045 - The {0} engine service cannot initialize a new instance; error message is {1}
     */
    ENGINE_SERVICE_INSTANCE_FAILURE("SERVER-OPS-0045",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                                  "The {0} engine service cannot initialize a new instance; error message is {1}",
                                  "The engine service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                                  "Review the error message and any other reported failures to determine the cause of the problem with the engine service.  Once this is " +
                                          "resolved, restart the engine host server."),

    /**
     * SERVER-OPS-0046 - The admin services are not able to start the {0} engine service because the admin service class {1} is invalid; error message is {2}
     */
    BAD_ENGINE_SERVICE_ADMIN_CLASS("SERVER-OPS-0046",
                                 AuditLogRecordSeverityLevel.EXCEPTION,
                                 "The admin services are not able to start the {0} engine service because the admin service class {1} is invalid; error message is {2}",
                                 "The admin services are unable to create an instance of the engine service's admin class during the start up of a specific server " +
                                         "instance.  The server fails to start.",
                                 "Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the " +
                                         "engine host server."),

    /**
     * SERVER-OPS-0047 - The admin services caught an unexpected {0} exception whilst initializing the {1} service for an engine host server.
     * The error message is {2}
     */
    ENGINE_SERVICE_FAILURE("SERVER-OPS-0047",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The admin services caught an unexpected {0} exception whilst initializing the {1} service for an engine host server. " +
                                 " The error message is {2}",
                         "The admin services detected an error during the start up of a specific engine service subsystem.  " +
                                 "Its services are not available for the server and so the server failed to start.  Details of the error " +
                                 "that the engine service detected are logged and a detailed exception is returned to the caller. ",
                         "Review the error message and the other failures reported by the engine service to determine the cause of the problem.  It is " +
                                 "typically either " +
                                 "a configuration error, or one of the external resources it needs, such as a topic on the event bus, is not  " +
                                 "available. Engine services may be developed by the Egeria community or a third party.  If the error messages " +
                                 "are not sufficient to resolve the problem, raise an issue with the author of the engine service to get this " +
                                 "improved.  Once the root cause of the error is resolved, restart the engine host server."),

    /**
     * SERVER-OPS-0048 - The OMAG server {0} has been passed a null admin services class name for engine service {1}
     */
    NULL_ENGINE_SERVICE_ADMIN_CLASS("SERVER-OPS-0048",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                                  "The OMAG server {0} has been passed a null admin services class name for engine service {1}",
                                  "The system cannot initialize this engine service. The server failed to start.",
                                  "If the engine service should be initialized then set up the appropriate admin services class name " +
                                          "in the engine service's configuration and restart the server instance. Otherwise, " +
                                          "remove the configuration for this engine service and restart the engine host OMAG server."),

    /**
     * SERVER-OPS-0100 - The governance services subsystem for the {0} called {1} is about to start
     */
    STARTING_GOVERNANCE_SERVICES("SERVER-OPS-0100",
                                 AuditLogRecordSeverityLevel.STARTUP,
                                 "The governance services subsystem for the {0} called {1} is about to start",
                                 "The admin services are about to start the governance services subsystem.  It will begin to initialize, " +
                                         "logging start up messages to confirm that its internal components have successfully initialized.",
                                 "Review the start up messages as they occur to ensure the correct capability has been initialized in the " +
                                         "governance server."),

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
                                        "community to get the start up messages improved."),

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
                                       "community to get this improved.  Once the root cause of the problem is resolved, restart the server."),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


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
     */
    ServerOpsAuditCode(String                     messageId,
                       AuditLogRecordSeverityLevel severity,
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
}
