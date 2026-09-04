/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The IntegrationDaemonServicesAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum IntegrationDaemonServicesAuditCode implements AuditLogMessageSet
{
    /**
     * INTEGRATION-DAEMON-SERVICES-0001 - The integration daemon services are initializing in server {0}
     */
    SERVER_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0001",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The integration daemon services are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as an integration daemon.  " +
                                 "Within the integration daemon are one or more dynamic integration groups that host " +
                                "integration connectors to exchange metadata with third party technologies.",
                        "Verify that the start up sequence goes on to initialize the configured integration services.",
                        "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0003 - Integration service {0} is not configured with the platform URL root for its partner OMAS {1}
     */
    NO_OMAS_SERVER_URL("INTEGRATION-DAEMON-SERVICES-0003",
                       AuditLogRecordSeverityLevel.ERROR,
                         "Integration daemon {0} is not configured with the platform URL root for its partner OMAS {1}",
                         "The service is not able to connect to the open metadata ecosystem.  It fails to start.",
                         "Add the platform URL root of the OMAG server where the partner OMAS is running " +
                               "to this integration service's configuration.",
                               "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0004 - Integration service {0} is not configured with the name for the server running its partner OMAS {1}
     */
    NO_OMAS_SERVER_NAME("INTEGRATION-DAEMON-SERVICES-0004",
                        AuditLogRecordSeverityLevel.ERROR,
                       "Integration daemon {0} is not configured with the name for the server running its partner OMAS {1}",
                      "The service is not able to connect to the open metadata ecosystem.  It fails to start.",
                     "Add the server name of the OMAG server where the partner OMAS is running " +
                                "to this integration service's configuration.",
                                "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0008 - A new integration connector named {0} is initializing in integration service {1} running in integration daemon {2}, permitted synchronization is: {3}
     */
    INTEGRATION_CONNECTOR_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0008",
                                       AuditLogRecordSeverityLevel.STARTUP,
                      "A new integration connector named {0} is initializing in integration daemon {1}, permitted synchronization is: {2}",
                      "The integration daemon is initializing an integration connector using the information in the configured " +
                                               "connection.",
                      "Verify that this connector is successfully initialized.",
                      "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0009 - A new integration connector named {0} failed to initialize in integration service {1}.
     * The exception returned was {2} with a message of {3}
     */
    BAD_INTEGRATION_CONNECTION("INTEGRATION-DAEMON-SERVICES-0009",
                               AuditLogRecordSeverityLevel.STARTUP,
                      "A new integration connector named {0} failed to initialize in integration daemon {1}.  " +
                                       "The exception returned was {2} with a message of {3}",
                      "The integration service fails to initialize.  This, in turn causes the integration daemon to fail to start.",
                      "Correct the connection for this integration connector in the integration service's section " +
                                       "of this integration daemon's configuration document and then " +
                                       "restart the integration daemon.",
                                       "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0010 - The connection for integration connector named {0} created a connector of class {1}
     * which does not implement the correct {2} interface
     */
    NOT_INTEGRATION_CONNECTOR("INTEGRATION-DAEMON-SERVICES-0010",
                              AuditLogRecordSeverityLevel.STARTUP,
                     "The connection for integration connector named {0} created a connector of class {1} which does not implement the " +
                                      "correct {2} interface",
                     "The integration service, and hence the hosting integration daemon, fails to start.",
                              "Change the connection in the integration service's section " +
                                      "of this integration daemon's configuration document to a valid integration connector and then " +
                                      "restart the integration daemon.",
                                      "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0012 - The integration daemon services are unable to initialize a new instance of integration daemon {0};
     * error message is {1}
     */
    SERVICE_INSTANCE_FAILURE("INTEGRATION-DAEMON-SERVICES-0012",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The integration daemon services are unable to initialize a new instance of integration daemon {0}; " +
                                     "error message is {1}",
                             "The integration daemon services detected an error during the start up of a specific integration daemon " +
                                     "instance.  Its integration services are not available.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the integration daemon.",
                                     "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0013 - The integration daemon {0} has initialized
     */
    SERVER_INITIALIZED("INTEGRATION-DAEMON-SERVICES-0013",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The integration daemon {0} has initialized",
                       "The integration daemon services has completed initialization.",
                       "Verify that all the configured integration services, and their connectors within have successfully started and" +
                               "are able to connect both to their third party technology and their partner OMAS.",
                               "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0015 - User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3}
     */
    DAEMON_CONNECTOR_CONFIG_PROPS_UPDATE("INTEGRATION-DAEMON-SERVICES-0015",
                                         AuditLogRecordSeverityLevel.INFO,
                                         "User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3}",
                                         "The connector will be restarted once the new properties are in place.",
                                         "Ensure that the connector does not report any errors during the restart processing as it operates using its new properties.",
                                         "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0016 - User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2}
     */
    DAEMON_CONNECTOR_CONFIG_PROPS_CLEARED("INTEGRATION-DAEMON-SERVICES-0016",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2}",
                                          "The connector will be restarted once the properties are cleared.",
                                          "Ensure that the connector does not report any errors during the restart processing as it operated on its default properties.",
                                          "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0019 - The integration daemon services has registered the configuration listener for server {0}.  It will receive configuration updates from metadata server {1}
     */
    CONFIGURATION_LISTENER_REGISTERED("INTEGRATION-DAEMON-SERVICES-0019",
                                      AuditLogRecordSeverityLevel.STARTUP,
                                      "The integration daemon services has registered the configuration " +
                                              "listener for server {0}.  It will receive configuration updates from metadata server {1}",
                                      "The integration daemon continues to run.  The integration daemon services will start up the " +
                                              "integration groups and they will operate with whatever configuration that they can retrieve.  " +
                                              "Periodically the integration daemon services will" +
                                              "retry the request to retrieve the integration connector configuration associated with the group and activate/deactivate the requested integration connectors as requested.",
                                      "Ensure the configuration for the integration connectors is attached to the integration group(s) configured for this integration daemon.",
                                      "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0020 - The integration daemon {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0020",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The integration daemon {0} is shutting down",
                    "The local administrator has requested shut down of this integration daemon server.",
                    "Verify that this server is no longer needed and the shutdown is expected.",
                    "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0026 - The integration daemon {0} has completed shutdown
     */
    SERVER_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0026",
                    AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The integration daemon {0} has completed shutdown",
                         "The local administrator has requested shut down of this integration daemon server and the operation has completed.",
                         "Verify that all integration connectors that support the metadata exchange have shut down successfully.",
                         "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0027 - The integration daemon services are unable to retrieve the connection for the configuration
     * listener for server {0} from metadata server {1}. Exception returned was {2} with error message {3}
     */
    NO_CONFIGURATION_LISTENER("INTEGRATION-DAEMON-SERVICES-0027",
                              AuditLogRecordSeverityLevel.EXCEPTION,
                              "The integration daemon services are unable to retrieve the connection for the configuration " +
                                      "listener for server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The server continues to run.  The engine host services will start up the " +
                                      "integration services and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the integration daemon services will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the integration daemon services will not be notified of changes to the integration " +
                                      "groups' configuration",
                              "This problem may be caused because the integration daemon services has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the Governance Engine OMAS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the engine host services to retry the configuration request.",
                                      "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0028 - Unable to refresh configuration for integration group {0}.  The exception was {1} with an error message {2}
     */
    INTEGRATION_GROUP_NO_CONFIG("INTEGRATION-DAEMON-SERVICES-0028",
                                AuditLogRecordSeverityLevel.INFO,
                                "Unable to refresh configuration for integration group {0}.  The exception was {1} with an error message {2}",
                                "The integration group cannot process any integration connector requests until its configuration can be retrieved.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the integration daemon services to refresh the configuration, or issue the refreshConfig " +
                                        "call to request that the integration group calls the Governance Engine OMAS to refresh the configuration for " +
                                        "the integration group.",
                                        "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0030 - The dedicated thread for integration connector {0} has started in integration daemon {1}
     */
    CONNECTOR_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0030",
                              AuditLogRecordSeverityLevel.STARTUP,
                    "The dedicated thread for integration connector {0} has started in integration daemon {1}",
                    "The server will call the integration connector's engage() method to indicate that it can issue blocking calls.",
                    "Ensure that the connector is running successfully.",
                    "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0031 - The integration connector {0} method {1} has returned with a {2} exception containing message {3}
     */
    CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0031",
                    AuditLogRecordSeverityLevel.EXCEPTION,
                     "The integration connector {0} method {1} has returned with a {2} exception containing message {3}",
                              "The server will change the integration connector's status to failed.",
                              "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then restart the connector.",
                            "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0034 - The integration connector {0} has returned from the "engage()" method in integration daemon {1}
     */
    ENGAGE_RETURNED("INTEGRATION-DAEMON-SERVICES-0034",
                    AuditLogRecordSeverityLevel.INFO,
                    "The integration connector {0} has returned from the engage() method in integration daemon {1}",
                    "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                          "It called the engage() method on this thread.  The engage() method has returned which means the " +
                                         "connector has finished its processing of a single blocking call.  " +
                                         "The integration daemon will wait one minute and then call engage() again unless the server is " +
                                         "shutting down.",
                    "Verify that the connector is not reporting errors which have caused it to terminate prematurely.",
                    "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0035 - The dedicated thread for integration connector {0} is terminating in integration daemon {1}
     */
    CONNECTOR_THREAD_TERMINATING("INTEGRATION-DAEMON-SERVICES-0035",
                                 AuditLogRecordSeverityLevel.SHUTDOWN,
                                 "The dedicated thread for integration connector {0} is terminating in integration daemon {1}",
                                 "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                         "The integration daemon is shutting down and has requests that the dedicated thread for this " +
                                         "connector terminates.",
                                 "Verify that there are no errors as the thread terminates.  In particular, if the thread detects" +
                                         "shutdown after the integration daemon has completed, there should still be an orderly shutdown of " +
                                         "the connector.",
                                         "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0036 - The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to connect to the associated metadata access server
     */
    INITIALIZE_ERROR("INTEGRATION-DAEMON-SERVICES-0036",
                     AuditLogRecordSeverityLevel.ERROR,
                    "The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to connect to the associated metadata access server",
                    "The server will change the integration connector's status to Initialize Failed.  It will retry the call to the metadata server during each refresh() call until the metadata server is contacted.",
                    "Check the status of the associated metadata server - it may need restarting.  Alternatively, the integration " +
                            "connector may be configured with the wrong metadata server, in which case the integration connector's " +
                            "configuration needs updating and the integration daemon will need restarting.  " +
                            "If neither of these are the cause of the problem, use the message from the exception and knowledge of the open metadata landscape to " +
                            "track down and resolve the cause of the error and then restart the connector.",
                            "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0037 - The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector
     */
    CONFIG_ERROR("INTEGRATION-DAEMON-SERVICES-0037",
                 AuditLogRecordSeverityLevel.ERROR,
                     "The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector",
                     "The server will change the integration connector's status to Configuration Failed.  It will ignore the connector during each refresh() call until the connector is restarted with workable configuration.",
                     "Check the configuration of the connector.",
                     "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0041 - Integration connector {0} is refreshing for the first time in the {1} integration daemon
     */
    DAEMON_CONNECTOR_FIRST_REFRESH("INTEGRATION-DAEMON-SERVICES-0041",
                                   AuditLogRecordSeverityLevel.INFO,
                           "Integration connector {0} is refreshing for the first time in the {1} integration daemon",
                           "The thread is about to call refresh() on the integration connector hosted in this daemon for the first time.",
                           "Ensure that the connector does not report any errors during the refresh processing.",
                           "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0042 - Integration connector {0} is refreshing again in {1} integration daemon
     */
    DAEMON_CONNECTOR_REFRESH("INTEGRATION-DAEMON-SERVICES-0042",
                             AuditLogRecordSeverityLevel.INFO,
                             "Integration connector {0} is refreshing again in {1} integration daemon",
                             "The thread is about to call refresh() on the integration connector hosted in this daemon.",
                             "Ensure that the connector does not report any errors during the refresh processing.",
                             "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0043 - The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond(s)
     */
    DAEMON_CONNECTOR_REFRESH_COMPLETE("INTEGRATION-DAEMON-SERVICES-0043",
                                      AuditLogRecordSeverityLevel.INFO,
                             "The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond(s)",
                             "The to call refresh() has returned.",
                             "Verify that the time between refresh calls is appropriate for the connector.",
                             "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0050 - Integration service {0} is not authorized to call its partner OMAS running in integration daemon {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}
     */
    SERVER_NOT_AUTHORIZED("INTEGRATION-DAEMON-SERVICES-0050",
                          AuditLogRecordSeverityLevel.SECURITY,
                          "Integration service {0} is not authorized to call its partner " +
                                  "OMAS running in integration daemon {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                          "Some, or all the metadata from the connected third party technologies can not be exchanged with the open" +
                                  "metadata ecosystem.",
                          "The userId comes from the integration daemon's configuration document.  It is stored as the localServerUserId.  " +
                                  "The authorization failure may be limited to a single operation, or extend to all requests to a specific partner " +
                                  "OMAS, specific metadata elements or an entire metadata access point or metadata server.  Diagnose the " +
                                  "extent of the authorization failure.  Then either turn off the integration services that are not permitted or " +
                                  "ensure the integration's userId has sufficient access.  If one of the integration connectors needs unusually " +
                                  "permissive access, you could consider isolating it in its own integration daemon that has a more powerful userId, " +
                                  "leaving the rest of the integration connectors working with the current userId.",
                                  "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0051 - All integration connector configuration is being refreshed for integration group {0}
     */
    CLEARING_ALL_INTEGRATION_CONNECTOR_CONFIG("INTEGRATION-DAEMON-SERVICES-0051",
                                              AuditLogRecordSeverityLevel.INFO,
                                              "All integration connector configuration is being refreshed for integration group {0}",
                                              "The integration daemon services will call the Governance Engine OMAS in the metadata server to " +
                                                   "retrieve details of all the integration connectors configured for this integration group." +
                                                   "During this process, some refresh requests may fail if the associated integration" +
                                                   "connector is only partially configured.",
                                              "Monitor the integration daemon services to ensure all the integration connectors are retrieved. " +
                                                   "Then it is ready to process new refresh requests.",
                                                   "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0053 - Failed to process a change to integration group {0}.  The exception was {1} with error message {2}
     */
    GROUP_CHANGE_FAILED("INTEGRATION-DAEMON-SERVICES-0053",
                        AuditLogRecordSeverityLevel.EXCEPTION,
                        "Failed to process a change to integration group {0}.  The exception was {1} with error message {2}",
                        "The integration daemon cannot process the change to a governance group.  The exception explains the reason.",
                        "Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to " +
                                     "refresh the configuration of the integration group by calling the integration daemon's refreshConfig service.",
                                     "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0054 - Failed to process a change to integration connector {0}.  The exception was {1} with error message {2}
     */
    CONNECTOR_CHANGE_FAILED("INTEGRATION-DAEMON-SERVICES-0054",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                        "Failed to process a change to integration connector {0}.  The exception was {1} with error message {2}",
                        "The integration daemon cannot process the change to a integration connector.  The exception explains the reason.",
                        "Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to " +
                                "refresh the configuration of the integration group by calling the integration daemon's refreshConfig service.",
                                "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0056 - Refresh of all integration connector configuration has completed for integration group {0}
     */
    FINISHED_ALL_INTEGRATION_CONNECTOR_CONFIG("INTEGRATION-DAEMON-SERVICES-0056",
                                              AuditLogRecordSeverityLevel.INFO,
                                              "Refresh of all integration connector configuration has completed for integration group {0}",
                                              "The integration connectors for this integration group are running with the latest configuration.",
                                              "No action is required as long as all the expected integration connectors are started." +
                                                   "If there are any errors reported by the integration connectors then validate the configuration " +
                                                   "of the integration connector and its associated integration group in the metadata server.",
                                                   "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0057 - User {0} has updated the endpoint network address for the integration connector {1} in integration daemon {2} to: {3}
     */
    DAEMON_CONNECTOR_ENDPOINT_UPDATE("INTEGRATION-DAEMON-SERVICES-0057",
                                     AuditLogRecordSeverityLevel.INFO,
                                         "User {0} has updated the endpoint network address for the integration connector {1} in integration daemon {2} to: {3}",
                                         "The connector will be restarted once the new endpoint network address is in place.",
                                         "Ensure that the connector does not report any errors during the restart processing as it connects to the new endpoint.",
                                         "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0058 - User {0} has attempted to update the endpoint network address for the integration connector {1} in integration daemon {2} to {3} but this connector does not have an endpoint defined
     */
    DAEMON_CONNECTOR_NO_ENDPOINT_TO_UPDATE("INTEGRATION-DAEMON-SERVICES-0058",
                                           AuditLogRecordSeverityLevel.ERROR,
                                     "User {0} has attempted to update the endpoint network address for the integration connector {1} in integration daemon {2} to {3} but this connector does not have an endpoint defined",
                                     "The connector continues to operate as before.",
                                     "If the connector should have an endpoint then update the whole connection for the connector.",
                                     "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0060 - The integration connector refresh thread for integration connector {0} has started
     */
    REFRESH_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0060",
                           AuditLogRecordSeverityLevel.STARTUP,
                           "The integration connector refresh thread for integration connector {0} has started",
                           "The thread will periodically call refresh() on the integration connector.  " +
                                   "The time between each refresh is set up in the configuration for the integration connector.",
                           "Ensure that the integration connector is running successfully.",
                           "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0064 - The integration connector refresh thread for integration connector {0} is shutting down
     */
    REFRESH_THREAD_TERMINATING("INTEGRATION-DAEMON-SERVICES-0064",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                              "The integration connector refresh thread for integration connector {0} is shutting down",
                              "The thread will stop calling refresh() on the integration connectors hosted in this daemon and stop running.",
                              "Ensure that the thread terminates without errors.",
                              "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0066 - The registration of integration connector {0} in integration daemon {1} has changed ({2}); the connector is being reinitialized
     */
    CONNECTOR_REGISTRATION_CHANGED("INTEGRATION-DAEMON-SERVICES-0066",
                                   AuditLogRecordSeverityLevel.INFO,
                                   "The registration of integration connector {0} in integration daemon {1} has changed ({2}); the connector is being reinitialized",
                                   "The integration daemon re-read the connector's registration in response to a change to the connector or its group and found it different from the one the running connector was built from.  The running connector is disconnected and a new one built from the new registration.",
                                   "No action is required if the registration was changed deliberately.  If it was not, compare the two connections recorded with this message to see what differed.",
                                   "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0067 - The registration of integration connector {0} in integration daemon {1} has changed ({2}) while the connector is refreshing; it will be reinitialized when the refresh completes
     */
    CONNECTOR_REINITIALIZE_DEFERRED("INTEGRATION-DAEMON-SERVICES-0067",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "The registration of integration connector {0} in integration daemon {1} has changed ({2}) while the connector is refreshing; it will be reinitialized when the refresh completes",
                                    "Reinitializing a connector disconnects it and starts a new instance.  Doing that while the current instance is part way through a refresh would leave two instances of the connector working at once, so the change is held until the refresh returns.",
                                    "No action is required.  The connector is reinitialized from the new registration as soon as its current refresh completes.",
                                    "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * INTEGRATION-DAEMON-SERVICES-0065 - The integration connector refresh thread for integration connector {0} caught a {1} exception  containing message {2}
     */
    REFRESH_THREAD_CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0065",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                                  "The integration connector refresh thread for integration connector {0} caught a {1} exception containing message {2}",
                                  "The integration connector thread will revisit this connector at the next refresh time.",
                                  "Use the message from the exception and knowledge of the integration connector's behavior to " +
                                          "track down and resolve the cause of the error and then restart the connector.  " +
                                           "The integration connector refresh thread will then continue to call the connector.",
                                           "https://egeria-project.org/services/integration-daemon-services/"),

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
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    IntegrationDaemonServicesAuditCode(String                      messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                      message,
                                       String                      systemAction,
                                       String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for IntegrationDaemonServicesAuditCode expects to be passed one of the enumeration rows defined in
     * IntegrationDaemonServicesAuditCode above.   For example:
     * <br><br>
     *     IntegrationDaemonServicesAuditCode   auditCode = IntegrationDaemonServicesAuditCode.SERVER_SHUTDOWN;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    IntegrationDaemonServicesAuditCode(String                      messageId,
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
