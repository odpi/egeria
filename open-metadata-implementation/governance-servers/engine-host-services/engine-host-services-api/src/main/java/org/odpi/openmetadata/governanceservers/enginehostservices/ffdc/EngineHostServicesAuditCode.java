/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The EngineHostServicesAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum EngineHostServicesAuditCode implements AuditLogMessageSet
{
    /**
     * ENGINE-HOST-SERVICES-0001 - The engine host services are initializing in server {0}
     */
    SERVER_INITIALIZING("ENGINE-HOST-SERVICES-0001",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The engine host services are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as an engine host.  " +
                                 "Within the engine host are one or more Open Metadata Engine Services (OMESs) that host " +
                                "governance services (connectors) to actively govern open metadata and the digital landscape it represents.",
                        "Verify that the start up sequence goes on to initialize the configured engine services and engines."),

    /**
     * ENGINE-HOST-SERVICES-0002 - The engine host {0} has initialized
     */
    SERVER_INITIALIZED("ENGINE-HOST-SERVICES-0002",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The engine host {0} has initialized",
                       "The engine host services has completed initialization.",
                       "Verify that all the configured engine services have successfully started, the configuration for their " +
                               "assigned governance engines has been retrieved from the Governance Engine OMAS by the engine host services " +
                               "and the engine services are able to connect to their partner OMAS."),

    /**
     * ENGINE-HOST-SERVICES-0003 - The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2}
     */
    SERVICE_INSTANCE_FAILURE("ENGINE-HOST-SERVICES-0003",
                             OMRSAuditLogRecordSeverity.EXCEPTION,
                             "The engine host services are unable to initialize a new instance of engine host {0}; " +
                                     "exception {1} with message {2}",
                             "The engine host services detected an error during the start up of a specific engine host " +
                                     "instance.  Its integration services are not available.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the engine host."),

    /**
     * ENGINE-HOST-SERVICES-0004 - Engine host {0} is not configured with the platform URL root for its configuration OMAS {1}
     */
    NO_CONFIG_OMAS_SERVER_URL("ENGINE-HOST-SERVICES-0004",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Engine host {0} is not configured with the platform URL root for its configuration OMAS {1}",
                         "The server is not able to connect to the open metadata ecosystem without the name of the platform.  It fails to start.",
                         "Add the platform URL root of the OMAG server where the Governance Engine OMAS is running " +
                               "to this engine service's configuration."),

    /**
     * ENGINE-HOST-SERVICES-0005 - Engine host {0} is not configured with the name for the server running its configuration OMAS {1}
     */
    NO_CONFIG_OMAS_SERVER_NAME("ENGINE-HOST-SERVICES-0005",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Engine host {0} is not configured with the name for the server running its configuration OMAS {1}",
                      "The server is not able to connect to the open metadata repositories.  It fails to start.",
                     "Add the server name of the OMAG server where the Governance Engine OMAS is running " +
                                "to this integration service's configuration."),


    /**
     * ENGINE-HOST-SERVICES-0006 - Engine service {0} in engine host {1} is not configured with the platform URL root for its partner OMAS {2}
     */
    NO_PARTNER_OMAS_SERVER_URL("ENGINE-HOST-SERVICES-0006",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "Engine service {0} in engine host {1} is not configured with the platform URL root for its partner OMAS {2}",
                              "The service is not able to connect to the open metadata ecosystem without the name of the platform.  It fails to start.",
                              "Add the platform URL root of the OMAG server where the partner OMAS is running " +
                                      "to this engine service's configuration."),

    /**
     * ENGINE-HOST-SERVICES-0007 - Engine service {0} in engine host {1} is not configured with the name for the server running its partner OMAS {2}
     */
    NO_PARTNER_OMAS_SERVER_NAME("ENGINE-HOST-SERVICES-0007",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Engine service {0} in engine host {1} is not configured with the name for the server running its partner OMAS {2}",
                               "The service is not able to connect to the open metadata repositories.  It fails to start.",
                               "Add the server name of the OMAG server where the partner OMAS is running " +
                                       "to this integration service's configuration."),


    /**
     * ENGINE-HOST-SERVICES-0008 - No governance engines are configured in the engine service {0} on engine host {1}
     */
    NO_ENGINES_FOR_SERVICE( "ENGINE-HOST-SERVICES-0008",
                            OMRSAuditLogRecordSeverity.ERROR,
                            "No governance engines are configured in the engine service {0} on engine host {1}",
                           "The call to the engine service fails and an exception is returned to the caller.",
                           "This is either a configuration error or a logic error.  If this is a configuration error, the" +
                                   "engine host will have logged detailed messages to the audit log when it was initializing the engine service " +
                                   "to describe what is wrong and how to fix it.  " +
                                   "If there are no errors in the configuration, raise an issue to get help to fix this."),

    /**
     * ENGINE-HOST-SERVICES-0009 - {0} in engine host {1} is configured with a null engine name
     */
    NULL_ENGINE_NAME("ENGINE-HOST-SERVICES-0009",
                                OMRSAuditLogRecordSeverity.ERROR,
                                "{0} in engine host {1} is configured with a null engine name",
                                "The start up of the engine host server fails with an exception.",
                                "Correct the qualified name for the governance engine configured for the engine service" +
                                        " in the engine host's configuration document."),

    /**
     * ENGINE-HOST-SERVICES-0010 - The engine service {0} has been configured with a null admin class in engine host {1}
     */
    NULL_ENGINE_SERVICE_ADMIN_CLASS("ENGINE-HOST-SERVICES-0010",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "The engine service {0} has been configured with a null admin class in engine host {1}",
                         "The engine service fails to start because the engine host can not initialize it.",
                         "Each engine service registers itself using a static method call with the engine host as" +
                                 "their classes are loaded into " +
                                 "the platform.  This is driven by the component scan for REST APIs implemented by the spring modules by " +
                                 "the platform-chassis-spring module.  " +
                                 "Ensure the engine service registers itself with the engine-host-services module and " +
                                 "the platform-chassis-spring module has access to the engine service's spring module."),

    /**
     * ENGINE-HOST-SERVICES-0011 - The engine service {0} has been configured with an admin class of {1} which can not be
     * used by the class loader.  The {2} exception was returned with message {3}
     */
    BAD_ENGINE_SERVICE_ADMIN_CLASS("ENGINE-HOST-SERVICES-0011",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
                            "The engine service {0} has been configured with an admin class of {1} which can not be " +
                                    "used by the class loader.  The {2} exception was returned with message {3}",
                            "The engine service fails to start.  Its governance engines, if any, are not activated.",
                            "Check that the jar containing the engine service's admin class is visible to the OMAG Server Platform through " +
                                    "the class path - and that the class name specified includes the full, correct package name and class name.  " +
                                    "Once the class is correctly set up, restart the engine host.  It will be necessary to restart the " +
                                    "OMAG Server Platform if the class path needed adjustment. "),

    /**
     * ENGINE-HOST-SERVICES-0012 - The Open Metadata Engine Services (OMESs) are initializing in server {0}
     */
    STARTING_ENGINE_SERVICES("ENGINE-HOST-SERVICES-0012",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The Open Metadata Engine Services (OMESs) are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as an engine host.  " +
                                "Within the engine host are one or more Open Metadata Engine Services (OMESs) that host " +
                                "governance services (connectors) to actively govern open metadata and the digital landscape it represents.",
                        "Verify that the start up sequence goes on to initialize the configured engine services and engines."),

    /**
     * ENGINE-HOST-SERVICES-0013 - Engine service {0} in engine host {1} is unable to start any governance engines
     */
    ENGINE_SERVICE_NULL_HANDLERS("ENGINE-HOST-SERVICES-0013",
                                 OMRSAuditLogRecordSeverity.ERROR,
                                 "Engine service {0} in engine host {1} is unable to start any governance engines",
                                 "The server is not able to run any governance requests.  It fails to start.",
                                 "Correct the configuration for the engine service to ensure it has at least one valid governance engine."),

    /**
     * ENGINE-HOST-SERVICES-0014 - {0} out of {1} Open Metadata Engine Services (OMESs) in engine host server {2} have initialized
     */
    ALL_ENGINE_SERVICES_STARTED("ENGINE-HOST-SERVICES-0014",
                                OMRSAuditLogRecordSeverity.STARTUP,
                                "{0} out of {1} Open Metadata Engine Services (OMESs) in engine host server {2} have initialized",
                                "The governance engine has completed initialization and is ready to receive governance requests.",
                                "Verify that the governance engine has been initialized wit the correct list of governance request types."),

    /**
     * ENGINE-HOST-SERVICES-0015 - The governance engine {0} in server {1} is shutting down
     */
    ENGINE_SHUTDOWN("ENGINE-HOST-SERVICES-0015",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The governance engine {0} in server {1} is shutting down",
                    "The local administrator has requested shut down of this governance engine.  No more governance requests will be processed by this engine.",
                    "Verify that this shutdown is intended and the governance engine is no longer needed."),


    /**
     * ENGINE-HOST-SERVICES-0016 - The {0} engine service is disabled and will not be started
     */
    SKIPPING_ENGINE_SERVICE("ENGINE-HOST-SERVICES-0016",
                      OMRSAuditLogRecordSeverity.STARTUP,
                      "The {0} engine service is disabled and will not be started",
                      "Although the engine service is not started, the initialization of the server continues.",
                      "Engine services are typically disabled because the code is either incomplete or not working. " +
                              "It is necessary to connect with the Egeria community to find out when the service will be enabled."),

    /**
     * ENGINE-HOST-SERVICES-0017 - The engine host {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("ENGINE-HOST-SERVICES-0017",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The engine host {0} is shutting down",
                    "The local administrator has requested shut down of this engine host server.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * ENGINE-HOST-SERVICES-0018 - The engine host {0} has completed shut down
     */
    SERVER_SHUTDOWN("ENGINE-HOST-SERVICES-0018",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The engine host {0} has completed shut down",
                         "The local administrator has requested shut down of this engine host server and the operation has completed.",
                         "Verify that all integration connectors that support the metadata exchange have shut down successfully."),

    /**
     * ENGINE-HOST-SERVICES-0019 - Engine host server {0} is not authorized to call the Governance Engine
     * OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}
     */
    SERVER_NOT_AUTHORIZED("ENGINE-HOST-SERVICES-0019",
                          OMRSAuditLogRecordSeverity.SECURITY,
                          "Engine host server {0} is not authorized to call the Governance Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                          "Some, or all the metadata definitions needed for the governance engines are not accessible from the open" +
                                  "metadata ecosystem.",
                          "The userId comes from the engine host's configuration document.  It is stored as the localServerUserId.  " +
                                  "The authorization failure may be limited to a single operation, or extend to all requests to the Governance " +
                                  "Engine OMAS, specific metadata elements or the entire remote server.  Diagnose the " +
                                  "extent of the authorization failure.  Then ensure the engine host's userId has sufficient access."),

    /**
     * ENGINE-HOST-SERVICES-0020 - Engine host server {0} failed to start.  The exception was {1} with message: {2}
     */
    ENGINE_SERVICE_INSTANCE_FAILURE("ENGINE-HOST-SERVICES-0020",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "Engine host server {0} failed to start.  The exception was {1} with message: {2}",
                          "The server encountered a problem and has halted initialization of its services.",
                          "Diagnose why the service is unable to start using the messages logged to the audit log."),

    /**
     * ENGINE-HOST-SERVICES-0021 - Governance engine {0} in engine host server {1} is configured to process governance requests of type {2}
     */
    SUPPORTED_REQUEST_TYPE("ENGINE-HOST-SERVICES-0021",
                             OMRSAuditLogRecordSeverity.INFO,
                             "Governance engine {0} in engine host server {1} is configured to process governance requests of type {2}",
                             "The governance engine has successfully retrieved the configuration to run requests for the named governance " +
                                     "request type.  It is ready to run governance requests of this type",
                             "Verify that this is an appropriate governance request type for the governance engine."),

    /**
     * ENGINE-HOST-SERVICES-0022 - Governance engine {0} in engine host server {1} is not configured to support any type of governance requests
     */
    NO_SUPPORTED_REQUEST_TYPES("ENGINE-HOST-SERVICES-0022",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Governance engine {0} in engine host server {1} is not configured to support any type of governance requests",
                               "The governance engine has no configuration that links it to a governance request type and a corresponding governance " +
                                       "governance service.  It is not able to process any governance requests because it would not know what to run.",
                               "Add the configuration for at least one registered governance service (and corresponding governance request type to " +
                                       "this governance engine."),

    /**
     * ENGINE-HOST-SERVICES-0023 - Governance engine {0} in engine host server {1} is not able to retrieve its configured governance request types from the
     * Governance Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}
     */
    REQUEST_TYPE_CONFIG_ERROR("ENGINE-HOST-SERVICES-0023",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "Governance engine {0} in engine host server {1} is not able to retrieve its configured governance request types from the " +
                                      "Governance Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                              "The governance engine has not been able to retrieve its configuration.  It is not able to process any governance " +
                                      "requests until this configuration is available.",
                              "Diagnose why the calls to Governance Engine OMAS are not working.  " +
                                      "The error message should help to narrow down the cause of the error.  " +
                                      "Once the problem has been resolved, restart the governance engine."),


    /**
     * ENGINE-HOST-SERVICES-0024 - Governance engine {0} is unable to update the status for governance service {1}.  The exception was {2} with error
     * message {3}
     */
    EXC_ON_ERROR_STATUS_UPDATE("ENGINE-HOST-SERVICES-0024",
                               OMRSAuditLogRecordSeverity.EXCEPTION,
                               "Governance engine {0} is unable to update the status for governance service {1}.  The exception was {2} with error " +
                                       "message {3}",
                               "The server is not able to record the failed result for a governance request. The governance report status is not updated.",
                               "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the governance request."),

    /**
     * ENGINE-HOST-SERVICES-0025 - Governance engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} returned to server {4}
     */
    UNKNOWN_GOVERNANCE_ENGINE_NAME( "ENGINE-HOST-SERVICES-0025",
                                   OMRSAuditLogRecordSeverity.STARTUP,
                                   "Governance engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} " +
                                           "returned to server {4}",
                                   "The engine host services in server is not able to initialize the governance engine and so it will not de able to support " +
                                           "governance requests targeted to this governance engine until this configuration is available.",
                                   "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                           "configuration of the server.  Once the cause is resolved, restart the server."),

    /**
     * ENGINE-HOST-SERVICES-0026 - The engine host services are unable to retrieve the connection for the configuration listener for server {0}
     * from metadata server {1}. Exception returned was {2} with error message {3}
     */
    NO_CONFIGURATION_LISTENER("ENGINE-HOST-SERVICES-0026",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "The engine host services are unable to retrieve the connection for the configuration " +
                                      "listener for server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The server continues to run.  The engine host services will start up the " +
                                      "governance engines and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the engine host services will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the engine host services will not be notified of changes to the governance " +
                                      "engines' configuration",
                              "This problem may be caused because the engine host services has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the Governance Engine OMAS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the engine host services to retry the configuration request."),

    /**
     * ENGINE-HOST-SERVICES-0027 - The engine host services has registered the configuration listener for server {0}.
     * It will receive configuration updates from metadata server {1}
     */
    CONFIGURATION_LISTENER_REGISTERED("ENGINE-HOST-SERVICES-0027",
                                      OMRSAuditLogRecordSeverity.STARTUP,
                                      "The engine host services has registered the configuration " +
                                              "listener for server {0}.  It will receive configuration updates from metadata server {1}",
                                      "The engine host services continues to run.  The engine host services will start up the " +
                                              "governance engines and they will operate with whatever configuration that they can retrieve.  " +
                                              "Periodically the engine host services will" +
                                              "retry the request to retrieve the connection information.  " +
                                              "Without the connection, the engine host services will not be notified of changes to the governance " +
                                              "engines' configuration",
                                      "This problem may be caused because the engine host services has been configured with the wrong location for the " +
                                              "metadata server, or the metadata server is not running the Governance Engine OMAS service or " +
                                              "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                              "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                              "refresh-config command or wait for the engine host services to retry the configuration request."),

    /**
     * ENGINE-HOST-SERVICES-0028 - All governance service configuration is being refreshed for governance engine {0}
     */
    CLEARING_ALL_GOVERNANCE_SERVICE_CONFIG("ENGINE-HOST-SERVICES-0028",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "All governance service configuration is being refreshed for governance engine {0}",
                                          "The engine host services will call the Governance Engine OMAS in the metadata server to " +
                                                  "retrieve details of all the governance services configured for this engine." +
                                                  "During this process, some governance request may fail if the associated governance" +
                                                  "service is only partially configured.",
                                          "Monitor the engine host services to ensure all the governance services are retrieved. " +
                                                  "Then it is ready to process new governance requests."),

    /**
     * ENGINE-HOST-SERVICES-0029 - Refreshing all governance service configuration has being completed for governance engine {0}
     */
    FINISHED_ALL_GOVERNANCE_SERVICE_CONFIG("ENGINE-HOST-SERVICES-0029",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Refreshing all governance service configuration has being completed for governance engine {0}",
                                          "The governance engine is ready to receive governance requests for all successfully loaded " +
                                                  "governance services.",
                                          "No action is required as long as all the expected governance services are loaded." +
                                                  "If there are any governance services missing then validate the configuration of" +
                                                  "the governance engine in the metadata server."),

    /**
     * ENGINE-HOST-SERVICES-0030 - Failed to refresh configuration for governance service registered as {0} for governance request types {1}.
     * The exception was {2} with error message {3}
     */
    GOVERNANCE_SERVICE_NO_CONFIG("ENGINE-HOST-SERVICES-0030",
                                OMRSAuditLogRecordSeverity.INFO,
                                "Failed to refresh configuration for governance service registered as " +
                                        "{0} for governance request types {1}.  The exception was {2} with error message {3}",
                                "The governance engine is unable to process governance request types for the failed governance service.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Then, either wait for the engine host services to refresh the configuration, or issue the refreshConfig " +
                                        "call to request that the governance engine calls the Governance Engine OMAS to refresh the configuration for " +
                                        "the governance service."),

    /**
     * ENGINE-HOST-SERVICES-0031 - Failed to refresh configuration for governance engine {0}.  The exception was {1} with error message {2}
     */
    GOVERNANCE_ENGINE_NO_CONFIG("ENGINE-HOST-SERVICES-0031",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Failed to refresh configuration for governance engine {0}.  The exception was {1} with error message {2}",
                               "The governance engine is unable to process any governance requests until its configuration can be retrieved.",
                               "Review the error messages and resolve the cause of the problem.  " +
                                       "Either wait for the engine host services to refresh the configuration, or issue the refreshConfig " +
                                       "call to request that the governance engine calls the Governance Engine OMAS to refresh the configuration for " +
                                       "the governance service."),

    /**
     * ENGINE-HOST-SERVICES-0032 - Governance action {0} running governance service {1} for governance engine {2} with request type {3} has
     * recorded completion on action target {4} with status {5} (start time {6}; completion time {7}) and completion message of {8}
     */
    GOVERNANCE_ACTION_TARGET_COMPLETION("ENGINE-HOST-SERVICES-0032",
                              OMRSAuditLogRecordSeverity.INFO,
                              "Governance action {0} running governance service {1} for governance engine {2} with request type {3} has recorded completion on action target {4} with status {5} (start time {6}; completion time {7}) and completion message of {8}",
                              "The governance action request has completed processing on one of its action targets.",
                              "Validate that the processing of this action target is correct."),

    /**
     * ENGINE-HOST-SERVICES-0033 - Governance action {0} running governance service {1} for governance engine {2} with request type {3}
     * has recorded completion status of {4} and output guards of {5}.  Next governance action is given request parameters
     * called {6} and action targets of {7}.  The completion message was {8}
     */
    GOVERNANCE_ACTION_RECORD_COMPLETION("ENGINE-HOST-SERVICES-0033",
                                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                                        "Governance action {0} running governance service {1} for governance engine {2} with request type {3} has recorded completion status of {4} and output guards of {5}.  Next governance action is given request parameters called {6} and action targets of {7}.  The completion message was {8}",
                                        "The governance engine is unable to process the governance action request.  The exception explains the " +
                                                "reason. The governance action has been marked as FAILED.",
                                        "Validate that the processing of this governance action request is correct."),

    /**
     * ENGINE-HOST-SERVICES-0034 - Failed to execute governance action for governance engine {0}.  The exception was {1} with error message {2}
     */
    GOVERNANCE_ACTION_FAILED("ENGINE-HOST-SERVICES-0034",
                             OMRSAuditLogRecordSeverity.EXCEPTION,
                             "Failed to execute governance action for governance engine {0}.  The exception was {1} with error message {2}",
                             "The governance engine is unable to process the governance action request.  The exception explains the " +
                                     "reason. The governance action has been marked as FAILED.",
                             "Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to " +
                                     "retry the governance action by updating its status back to REQUESTED status."),

    /**
     * ENGINE-HOST-SERVICES-0150 - {0} in server {1} is not configured with the platform URL root for the {2}
     */
    NO_OMAS_SERVER_URL("ENGINE-HOST-SERVICES-0150",
                       OMRSAuditLogRecordSeverity.ERROR,
                       "{0} in server {1} is not configured with the platform URL root for the {2}",
                       "The server is not able to retrieve its configuration from the metadata server because it does not " +
                               "know which platform it is running on.  The engine service fails to start.",
                       "Add the platform URL root of the metadata server where the partner OMAS is running " +
                               "to this engine service's configuration document."),

    /**
     * ENGINE-HOST-SERVICES-0151 - {0} in server {1} is not configured with the name for the server running the {2}
     */
    NO_OMAS_SERVER_NAME("ENGINE-HOST-SERVICES-0151",
                        OMRSAuditLogRecordSeverity.ERROR,
                        "{0} in server {1} is not configured with the name for the server running the {2}",
                        "The server is not able to retrieve its configuration because it does not know which metadata server to use.  " +
                                "It fails to start.",
                        "Add the server name of the metadata server where the partner OMAS is running " +
                                "to this engine service's configuration document."),

    /**
     * ENGINE-HOST-SERVICES-0152 - The {0} in server {1} is not configured with any engines
     */
    NO_ENGINES("ENGINE-HOST-SERVICES-0152",
               OMRSAuditLogRecordSeverity.ERROR,
               "The {0} in server {1} is not configured with any engines",
               "The server is not able to run any requests since this has not engines.  It fails to start.",
               "Add the qualified name for at least one engine to the engine service in this server's configuration document."),

    /**
     * ENGINE-HOST-SERVICES-2000 - {0} caught an exception {1} while processing governance action {2}; the error message was {3}
     */
    ACTION_PROCESSING_ERROR( "ENGINE-HOST-SERVICES-2000",
                            OMRSAuditLogRecordSeverity.ERROR,
                            "{0} caught an exception {1} while processing governance action {2}; the error message was {3}",
                            "The server is not able to run any services in this engine service.  The engine service fails to start which causes " +
                                    "the server to fail too.",
                            "Add the qualified name for at least one engine to the engine service in this server's configuration document " +
                                    "and then restart the server."),
    ;


    private final AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for EngineHostServicesAuditCode expects to be passed one of the enumeration rows defined in
     * EngineHostServicesAuditCode above.   For example:
     * <br><br>
     *     EngineHostServicesAuditCode   auditCode = EngineHostServicesAuditCode.SERVER_SHUTDOWN;
     * <br><br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    EngineHostServicesAuditCode(String                     messageId,
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "EngineHostServicesAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
