/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


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
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The engine host services are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as an engine host.  " +
                                 "Within the engine host are one or more Open Metadata Engine Services (OMESs) that host " +
                                "governance services (connectors) to actively govern open metadata and the digital landscape it represents.",
                        "Verify that the start up sequence goes on to initialize the configured engine services and engines.",
                        "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0002 - The engine host {0} has initialized
     */
    SERVER_INITIALIZED("ENGINE-HOST-SERVICES-0002",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The engine host {0} has initialized",
                       "The engine host services has completed initialization.",
                       "Verify that all the configured engine services have successfully started, the configuration for their " +
                               "assigned governance engines has been retrieved from the Governance Engine OMAS by the engine host services " +
                               "and the engine services are able to connect to their partner OMAS.",
                               "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0003 - The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2}
     */
    SERVICE_INSTANCE_FAILURE("ENGINE-HOST-SERVICES-0003",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                             "The engine host services are unable to initialize a new instance of engine host {0}; " +
                                     "exception {1} with message {2}",
                             "The engine host services detected an error during the start up of a specific engine host " +
                                     "instance.  Its integration services are not available.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the engine host.",
                                     "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0010 - The engine service {0} has been configured with a null admin class in engine host {1}
     */
    NULL_ENGINE_SERVICE_ADMIN_CLASS("ENGINE-HOST-SERVICES-0010",
                                    AuditLogRecordSeverityLevel.ERROR,
                         "The engine service {0} has been configured with a null admin class in engine host {1}",
                         "The engine service fails to start because the engine host can not initialize it.",
                         "Each engine service registers itself using a static method call with the engine host as" +
                                 "their classes are loaded into " +
                                 "the platform.  This is driven by the component scan for REST APIs implemented by the spring modules by " +
                                 "the platform-chassis-spring module.  " +
                                 "Ensure the engine service registers itself with the engine-host-services module and " +
                                 "the platform-chassis-spring module has access to the engine service's spring module.",
                                 "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0011 - The engine service {0} has been configured with an admin class of {1} which can not be
     * used by the class loader.  The {2} exception was returned with message {3}
     */
    BAD_ENGINE_SERVICE_ADMIN_CLASS("ENGINE-HOST-SERVICES-0011",
                                   AuditLogRecordSeverityLevel.EXCEPTION,
                            "The engine service {0} has been configured with an admin class of {1} which can not be " +
                                    "used by the class loader.  The {2} exception was returned with message {3}",
                            "The engine service fails to start.  Its governance engines, if any, are not activated.",
                            "Check that the jar containing the engine service's admin class is visible to the OMAG Server Platform through " +
                                    "the class path - and that the class name specified includes the full, correct package name and class name.  " +
                                    "Once the class is correctly set up, restart the engine host.  It will be necessary to restart the " +
                                    "OMAG Server Platform if the class path needed adjustment. ",
                                    "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0012 - The Open Metadata Engine Services (OMESs) are initializing in server {0}
     */
    STARTING_ENGINE_SERVICES("ENGINE-HOST-SERVICES-0012",
                             AuditLogRecordSeverityLevel.STARTUP,
                        "The Open Metadata Engine Services (OMESs) are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as an engine host.  " +
                                "Within the engine host are one or more Open Metadata Engine Services (OMESs) that host " +
                                "governance services (connectors) to actively govern open metadata and the digital landscape it represents.",
                        "Verify that the start up sequence goes on to initialize the configured engine services and engines.",
                        "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0014 - {0} out of {1} Open Metadata Engine Services (OMESs) in engine host server {2} have initialized
     */
    ALL_ENGINE_SERVICES_STARTED("ENGINE-HOST-SERVICES-0014",
                                AuditLogRecordSeverityLevel.STARTUP,
                                "{0} out of {1} Open Metadata Engine Services (OMESs) in engine host server {2} have initialized",
                                "The governance engine has completed initialization and is ready to receive governance requests.",
                                "Verify that the governance engine has been initialized wit the correct list of governance request types.",
                                "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0015 - The governance engine {0} in server {1} is shutting down
     */
    ENGINE_SHUTDOWN("ENGINE-HOST-SERVICES-0015",
                    AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The governance engine {0} in server {1} is shutting down",
                    "The local administrator has requested shut down of this governance engine.  No more governance requests will be processed by this engine.",
                    "Verify that this shutdown is intended and the governance engine is no longer needed.",
                    "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0016 - The {0} engine service is disabled and will not be started
     */
    SKIPPING_ENGINE_SERVICE("ENGINE-HOST-SERVICES-0016",
                            AuditLogRecordSeverityLevel.STARTUP,
                      "The {0} engine service is disabled and will not be started",
                      "Although the engine service is not started, the initialization of the server continues.",
                      "Engine services are typically disabled because the code is either incomplete or not working. " +
                              "It is necessary to connect with the Egeria community to find out when the service will be enabled.",
                              "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0017 - The engine host {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("ENGINE-HOST-SERVICES-0017",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The engine host {0} is shutting down",
                    "The local administrator has requested shut down of this engine host server.",
                    "Verify that this server is no longer needed and the shutdown is expected.",
                    "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0018 - The engine host {0} has completed shut down
     */
    SERVER_SHUTDOWN("ENGINE-HOST-SERVICES-0018",
                    AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The engine host {0} has completed shut down",
                         "The local administrator has requested shut down of this engine host server and the operation has completed.",
                         "Verify that all integration connectors that support the metadata exchange have shut down successfully.",
                         "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0019 - Engine host server {0} is not authorized to call the Governance Engine
     * OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}
     */
    SERVER_NOT_AUTHORIZED("ENGINE-HOST-SERVICES-0019",
                          AuditLogRecordSeverityLevel.SECURITY,
                          "Engine host server {0} is not authorized to call the Governance Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                          "Some, or all the metadata definitions needed for the governance engines are not accessible from the open" +
                                  "metadata ecosystem.",
                          "The userId comes from the engine host's configuration document.  It is stored as the localServerUserId.  " +
                                  "The authorization failure may be limited to a single operation, or extend to all requests to the Governance " +
                                  "Engine OMAS, specific metadata elements or the entire remote server.  Diagnose the " +
                                  "extent of the authorization failure.  Then ensure the engine host's userId has sufficient access.",
                                  "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0020 - Engine host server {0} failed to start.  The exception was {1} with message: {2}
     */
    ENGINE_SERVICE_INSTANCE_FAILURE("ENGINE-HOST-SERVICES-0020",
                                    AuditLogRecordSeverityLevel.EXCEPTION,
                          "Engine host server {0} failed to start.  The exception was {1} with message: {2}",
                          "The server encountered a problem and has halted initialization of its services.",
                          "Diagnose why the service cannot start using the messages logged to the audit log.",
                          "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0021 - Governance engine {0} in engine host server {1} is configured to process governance requests of type {2}
     */
    SUPPORTED_REQUEST_TYPE("ENGINE-HOST-SERVICES-0021",
                           AuditLogRecordSeverityLevel.INFO,
                             "Governance engine {0} in engine host server {1} is configured to process governance requests of type {2}",
                             "The governance engine has successfully retrieved the configuration to run requests for the named governance " +
                                     "request type.  It is ready to run governance requests of this type",
                             "Verify that this is an appropriate governance request type for the governance engine.",
                             "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0026 - The engine host services are unable to retrieve the connection for the configuration listener for server {0}
     * from metadata server {1}. Exception returned was {2} with error message {3}
     */
    NO_CONFIGURATION_LISTENER("ENGINE-HOST-SERVICES-0026",
                              AuditLogRecordSeverityLevel.EXCEPTION,
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
                                      "refresh-config command or wait for the engine host services to retry the configuration request.",
                                      "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0027 - The engine host services has registered the configuration listener for server {0} and governance engine {1}.
     * It will receive configuration updates from metadata access server {2}
     */
    CONFIGURATION_LISTENER_REGISTERED("ENGINE-HOST-SERVICES-0027",
                                      AuditLogRecordSeverityLevel.STARTUP,
                                      "The engine host services has registered the configuration " +
                                              "listener for server {0} and governance engine {1}.  It will receive configuration updates from metadata access server {2}",
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
                                              "refresh-config command or wait for the engine host services to retry the configuration request.",
                                              "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0028 - All governance service configuration is being refreshed for governance engine {0}
     */
    CLEARING_ALL_GOVERNANCE_SERVICE_CONFIG("ENGINE-HOST-SERVICES-0028",
                                           AuditLogRecordSeverityLevel.INFO,
                                          "All governance service configuration is being refreshed for governance engine {0}",
                                          "The engine host services will call the Governance Engine OMAS in the metadata server to " +
                                                  "retrieve details of all the governance services configured for this engine." +
                                                  "During this process, some governance request may fail if the associated governance" +
                                                  "service is only partially configured.",
                                          "Monitor the engine host services to ensure all the governance services are retrieved. " +
                                                  "Then it is ready to process new governance requests.",
                                                  "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0029 - Refreshing all governance service configuration has being completed for governance engine {0}
     */
    FINISHED_ALL_GOVERNANCE_SERVICE_CONFIG("ENGINE-HOST-SERVICES-0029",
                                           AuditLogRecordSeverityLevel.INFO,
                                          "All governance service configuration has been refreshed in governance engine {0}",
                                          "The governance engine is ready to receive governance requests for all successfully loaded " +
                                                  "governance services.",
                                          "No action is required as long as all the expected governance services are loaded." +
                                                  "If there are any governance services missing then validate the configuration of" +
                                                  "the governance engine in the metadata access server.",
                                                  "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0030 - Failed to refresh configuration for governance service registered as {0} for governance request types {1}.
     * The exception was {2} with error message {3}
     */
    GOVERNANCE_SERVICE_NO_CONFIG("ENGINE-HOST-SERVICES-0030",
                                 AuditLogRecordSeverityLevel.INFO,
                                "Failed to refresh configuration for the governance service {1} registered with governance engine {0}, registered with the " +
                                        " properties {2}.  The exception was {3} with error message {4}",
                                "The governance engine cannot process governance request types for the failed governance service.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Then, either wait for the engine host services to refresh the configuration, or issue the refreshConfig " +
                                        "call to request that the governance engine calls the Governance Engine OMAS to refresh the configuration for " +
                                        "the governance service.",
                                        "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0031 - Failed to refresh configuration for governance engine {0}.  The exception was {1} with error message {2}
     */
    GOVERNANCE_ENGINE_NO_CONFIG("ENGINE-HOST-SERVICES-0031",
                                AuditLogRecordSeverityLevel.ERROR,
                               "Failed to refresh configuration for governance engine {0}.  The exception was {1} with error message {2}",
                               "The governance engine cannot process any governance requests until its configuration can be retrieved.",
                               "Review the error messages and resolve the cause of the problem.  " +
                                       "Either wait for the engine host services to refresh the configuration, or issue the refreshConfig " +
                                       "call to request that the governance engine calls the Governance Engine OMAS to refresh the configuration for " +
                                       "the governance service.",
                                       "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0033 - Engine action {0} running governance service {1} for governance engine {2} with request type {3}
     * has recorded completion status of {4} and output guards of {5}.  Next governance action is given request parameters
     * called {6} and action targets of {7}.  The completion message was {8}
     */
    ENGINE_ACTION_RECORD_COMPLETION("ENGINE-HOST-SERVICES-0033",
                                    AuditLogRecordSeverityLevel.SHUTDOWN,
                                    "Engine action {0} running governance service {1} for governance engine {2} with request type {3} has recorded completion status of {4} and output guards of {5}.  Next engine action is given request parameters called {6} and action targets of {7}.  The completion message was {8}",
                                    "The governance engine shuts down this request to the governance service.",
                                    "Validate that the processing of this request is correct.",
                                    "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0034 - Failed to execute engine action for governance engine {0}.  The exception was {1} with error message {2}
     */
    ENGINE_ACTION_FAILED("ENGINE-HOST-SERVICES-0034",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "Failed to execute engine action for governance engine {0}.  The exception was {1} with error message {2}",
                         "The governance engine cannot process the engine action request.  The exception explains the " +
                                     "reason. The engine action has been marked as FAILED.",
                         "Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to " +
                                     "retry the governance action by updating its status back to REQUESTED status.",
                                     "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0036 - Governance engine {0} is cancelling running governance service for engine action {1}; thread name is {2}
     */
    ENGINE_ACTION_CANCELLED("ENGINE-HOST-SERVICES-0036",
                         AuditLogRecordSeverityLevel.INFO,
                         "Governance engine {0} is cancelling running governance service for engine action {1}; thread name is {2}",
                         "The governance engine has been requested to stop the execution of a governance service by a cancel request issued by an external user.",
                         "Validate that this request should have been cancelled.  Check it shutdown correctly.  Rerun the request if necessary.",
                         "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0037 - The Governance Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}
     */
    UNEXPECTED_EXCEPTION("ENGINE-HOST-SERVICES-0037",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} governance engine handler for {1} has received an unexpected {2} exception during method {3}; the error message was: {4}",
                         "The service cannot process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0153 - Refreshing governance engine {0}
     */
    CLEARING_ALL_GOVERNANCE_ENGINE_CONFIG("ENGINE-HOST-SERVICES-0153",
                                           AuditLogRecordSeverityLevel.INFO,
                                           "Refreshing governance engine {0}",
                                           "The engine host services will call the Governance Engine OMAS in the metadata server to " +
                                                   "retrieve details of this governance engine.",
                                           "Monitor the engine host services for errors.",
                                           "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-0154 - Refreshing of governance engine {0} is complete
     */
    FINISHED_ALL_GOVERNANCE_ENGINE_CONFIG("ENGINE-HOST-SERVICES-0154",
                                           AuditLogRecordSeverityLevel.INFO,
                                           "Refreshing of governance engine {0} is complete",
                                           "This governance engine is ready to receive governance requests for all successfully loaded " +
                                                   "governance services.",
                                           "No action is required as long as there are no errors reported.",
                                           "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-2000 - {0} caught an exception {1} while processing governance action {2}; the error message was {3}
     */
    ACTION_PROCESSING_ERROR( "ENGINE-HOST-SERVICES-2000",
                             AuditLogRecordSeverityLevel.ERROR,
                            "{0} caught an exception {1} while processing engine action {2}; the error message was {3}",
                            "The server is not able to start or complete the requested processing related to the governance service for this engine action.",
                            "Follow the instructions for the message associated with the exception.",
                            "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-2002 - {0} caught an exception {1} while restarting incomplete engine actions; the error message was {2}
     */
    UNEXPECTED_EXCEPTION_DURING_RESTART( "ENGINE-HOST-SERVICES-2002",
                             AuditLogRecordSeverityLevel.ERROR,
                             "{0} caught an exception {1} while restarting incomplete engine actions; the error message was {2}",
                             "The server is not able to complete the restart processing.",
                             "Follow the instructions for the message associated with the exception to resolve the error.  You may need to restart the engine host.",
                             "https://egeria-project.org/services/engine-host-services/"),

    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;
    


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    EngineHostServicesAuditCode(String                      messageId,
                                AuditLogRecordSeverityLevel severity,
                                String                      message,
                                String                      systemAction,
                                String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for EngineHostServicesAuditCode expects to be passed one of the enumeration rows defined in
     * EngineHostServicesAuditCode above.   For example:
     * <br><br>
     *     EngineHostServicesAuditCode   auditCode = EngineHostServicesAuditCode.SERVER_SHUTDOWN;
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
    EngineHostServicesAuditCode(String                      messageId,
                                AuditLogRecordSeverityLevel severity,
                                String                      message,
                                String                      systemAction,
                                String                      userAction,
                                String                      url)
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
    public AuditLogMessageDefinition getMessageDefinition(String... params)
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


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
