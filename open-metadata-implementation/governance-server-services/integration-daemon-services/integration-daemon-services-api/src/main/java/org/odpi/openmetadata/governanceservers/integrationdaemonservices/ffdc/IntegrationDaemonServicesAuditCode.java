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
                                 "Within the integration daemon are one or more Open Metadata Integration Services (OMISs) that host " +
                                "integration connectors to exchange metadata with third party technologies.",
                        "Verify that the start up sequence goes on to initialize the configured integration services."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0002 - The {0} is initializing in server {1}
     */
    INTEGRATION_SERVICE_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0002",
                                     AuditLogRecordSeverityLevel.STARTUP,
                        "The {0} is initializing in server {1}",
                        "A new Open Metadata Integration Service (OMIS) is starting up in the integration daemon.  " +
                                "It will begin to initialize its context manager and the integration connectors that will exchange metadata " +
                                             "between the open metadata ecosystem and a third party technology.",
                        "Verify that the start up sequence goes on to initialize the configured integration connectors."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0003 - Integration service {0} is not configured with the platform URL root for its partner OMAS {1}
     */
    NO_OMAS_SERVER_URL("INTEGRATION-DAEMON-SERVICES-0003",
                       AuditLogRecordSeverityLevel.ERROR,
                         "Integration service {0} is not configured with the platform URL root for its partner OMAS {1}",
                         "The service is not able to connect to the open metadata ecosystem.  It fails to start.",
                         "Add the platform URL root of the OMAG server where the partner OMAS is running " +
                               "to this integration service's configuration."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0004 - Integration service {0} is not configured with the name for the server running its partner OMAS {1}
     */
    NO_OMAS_SERVER_NAME("INTEGRATION-DAEMON-SERVICES-0004",
                        AuditLogRecordSeverityLevel.ERROR,
                       "Integration service {0} is not configured with the name for the server running its partner OMAS {1}",
                      "The service is not able to connect to the open metadata ecosystem.  It fails to start.",
                     "Add the server name of the OMAG server where the partner OMAS is running " +
                                "to this integration service's configuration."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0005 - The integration service {0} has been configured with a null context manager class in integration daemon {1}
     */
    NULL_CONTEXT_MANAGER("INTEGRATION-DAEMON-SERVICES-0005",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The integration service {0} has been configured with a null context manager class in integration daemon {1}",
                         "The integration service fails to start because it is not able to initialize any integration " +
                                 "connectors.",
                         "The standard integration services are registered in a static method by the IntegrationDaemonHandler.  " +
                                 "If this integration service is one of these services, correct the logic to include the " +
                                 "context manager name.  If this integration service comes from a third party, make sure the class name " +
                                 "is specified when the third party integration service is configured."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0006 - The integration service {0} has been configured with a context manager class of {1}
     * which can not be used by the class loader.  The {2} exception was returned with message {3}
     */
    INVALID_CONTEXT_MANAGER("INTEGRATION-DAEMON-SERVICES-0006",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "The integration service {0} has been configured with a context manager class of {1} which can not be " +
                                    "used by the class loader.  The {2} exception was returned with message {3}",
                            "The integration service fails to start.  Its connectors, if any, are not activated.",
                            "Check that the jar for the context manager's class is visible to the OMAG Server Platform through " +
                                    "the class path - and that the class name specified includes the full, correct package name and class name.  " +
                                    "Once the class is correctly set up, restart the integration daemon.  It will be necessary to restart the " +
                                    "OMAG Server Platform if the class path needed adjustment. "),

    /**
     * INTEGRATION-DAEMON-SERVICES-0007 - The {0} integration service is configured without any integration connectors
     */
    NO_INTEGRATION_CONNECTORS("INTEGRATION-DAEMON-SERVICES-0007",
                              AuditLogRecordSeverityLevel.ERROR,
                      "The {0} integration service is configured without any integration connectors",
                      "The integration service completes initialization with no errors.",
                      "Add the connection for at least one integration connector to the integration service's section " +
                                      "of this integration daemon's configuration document and then restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0008 - A new integration connector named {0} is initializing in integration service {1} running in integration daemon {2}, permitted synchronization is: {3}
     */
    INTEGRATION_CONNECTOR_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0008",
                                       AuditLogRecordSeverityLevel.STARTUP,
                      "A new integration connector named {0} is initializing in integration service {1} running in integration daemon {2}, permitted synchronization is: {3}",
                      "The integration service is initializing an integration connector using the information in the configured " +
                                               "connection.",
                      "Verify that this connector is successfully initialized."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0009 - A new integration connector named {0} failed to initialize in integration service {1}.
     * The exception returned was {2} with a message of {3}
     */
    BAD_INTEGRATION_CONNECTION("INTEGRATION-DAEMON-SERVICES-0009",
                               AuditLogRecordSeverityLevel.STARTUP,
                      "A new integration connector named {0} failed to initialize in integration service {1}.  " +
                                       "The exception returned was {2} with a message of {3}",
                      "The integration service fails to initialize.  This, in turn causes the integration daemon to fail to start.",
                      "Correct the connection for this integration connector in the integration service's section " +
                                       "of this integration daemon's configuration document and then " +
                                       "restart the integration daemon."),

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
                                      "restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0011 - The {0} integration service has completed its initialization in integration daemon {1}.
     * It is running {2} integration connector(s)
     */
    INTEGRATION_SERVICE_INITIALIZED("INTEGRATION-DAEMON-SERVICES-0011",
                                    AuditLogRecordSeverityLevel.STARTUP,
                     "The {0} integration service has completed its initialization in integration daemon {1}.  It is running {2} integration " +
                                            "connector(s)",
                     "The integration service hands over its integration connectors to the integration daemon to manage.",
                     "Verify that there were no errors starting the integration connectors for this service."),

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
                                     "Once this is resolved, restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0013 - The integration daemon {0} has initialized
     */
    SERVER_INITIALIZED("INTEGRATION-DAEMON-SERVICES-0013",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The integration daemon {0} has initialized",
                       "The integration daemon services has completed initialization.",
                       "Verify that all the configured integration services, and their connectors within have successfully started and" +
                               "are able to connect both to their third party technology and their partner OMAS."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0014 - The integration service {0} does not have a default permitted synchronization value set.
     */
    NO_PERMITTED_SYNCHRONIZATION("INTEGRATION-DAEMON-SERVICES-0014",
                                 AuditLogRecordSeverityLevel.STARTUP,
                       "The integration service {0} does not have a default permitted synchronization value set.",
                       "The integration daemon is not able to initialize one of the configured integration because its defaultPermittedSynchronization value is null.  " +
                               "The integration daemon shuts down and this error is reported to the caller as a configuration exception.",
                       "Update the configuration for the integration service to include a value for the default permitted synchronization."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0015 - User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3}
     */
    DAEMON_CONNECTOR_CONFIG_PROPS_UPDATE("INTEGRATION-DAEMON-SERVICES-0015",
                                         AuditLogRecordSeverityLevel.INFO,
                                         "User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3}",
                                         "The connector will be restarted once the new properties are in place.",
                                         "Ensure that the connector does not report any errors during the restart processing as it operates using its new properties."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0016 - User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2}
     */
    DAEMON_CONNECTOR_CONFIG_PROPS_CLEARED("INTEGRATION-DAEMON-SERVICES-0016",
                                          AuditLogRecordSeverityLevel.INFO,
                                          "User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2}",
                                          "The connector will be restarted once the properties are cleared.",
                                          "Ensure that the connector does not report any errors during the restart processing as it operated on its default properties."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0017 - Integration daemon {0} is not configured with any integration services
     */
    NO_INTEGRATION_SERVICES_CONFIGURED("INTEGRATION-DAEMON-SERVICES-0017",
                                       AuditLogRecordSeverityLevel.INFO,
                                       "Integration daemon {0} is not configured with any integration services",
                                       "The integration daemon continues start up, looking for integration groups.",
                                       "No change is required if the integration daemon requires no statically configured integration connectors. " +
                                               "If statically configured integration connectors are required, add the configuration for at least " +
                                               "one integration service, with the associated integration connector configurations," +
                                               " to this integration daemon's configuration document and then restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0018 - Integration daemon {0} is not configured with any integration groups
     */
    NO_INTEGRATION_GROUPS_CONFIGURED("INTEGRATION-DAEMON-SERVICES-0018",
                                     AuditLogRecordSeverityLevel.INFO,
                                       "Integration daemon {0} is not configured with any integration groups",
                                       "The integration daemon continues start up.",
                                       "No change is required if the integration daemon requires no dynamically configured integration connectors. " +
                                               "If dynamically configured integration connectors are required, add the configuration for at least " +
                                               "one integration group to this integration daemon's configuration document and then restart the integration daemon."),

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
                                      "Ensure the configuration for the integration connectors is attached to the integration group(s) configured for this integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0020 - The integration daemon {0} is shutting down
     */
    SERVER_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0020",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                    "The integration daemon {0} is shutting down",
                    "The local administrator has requested shut down of this integration daemon server.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0021 - The integration service {0} in integration daemon {1} is shutting down
     */
    SERVICE_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0021",
                          AuditLogRecordSeverityLevel.SHUTDOWN,
                          "The integration service {0} in integration daemon {1} is shutting down",
                          "The local administrator has requested shut down of this integration service.  Once shutdown" +
                                  "is complete, no more metadata exchange will be processed by the integration connectors in this service.",
                          "Verify that there are no errors reported by the integration connectors as they shutdown."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0022 - The integration connector {0} is shutting down
     */
    CONNECTOR_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0022",
                            AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The integration connector {0} is shutting down",
                         "The local administrator has requested shut down of the hosting integration daemon server.",
                         "Verify that this connector is no longer needed and the shutdown is expected."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0023 - The integration connector {0} reported an error on disconnect.  The exception was {1} with message {2}
     */
    CONNECTOR_SHUTDOWN_FAILURE("INTEGRATION-DAEMON-SERVICES-0023",
                               AuditLogRecordSeverityLevel.SHUTDOWN,
                            "The integration connector {0} reported an error on disconnect.  The exception was {1} with message {2}",
                            "The integration service moves on to shut down its other connectors.",
                            "Review the error message and other diagnostics produced by the connector to determine the source of the " +
                                       "error.  Release any locked resources.  If a particular connector is repeatedly problematic and the " +
                                       "code can not be improved, then it may need to be isolated to its own integration daemon running on " +
                                       "its own OMAG Server Platform to allow the platform to be restarted to allow the operating system or " +
                                       "container to clear resources held by the connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0024 - The integration connector {0} in integration service {1} has shutdown.  Statistics recorded were: {2}
     */
    INTEGRATION_CONNECTOR_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0024",
                                   AuditLogRecordSeverityLevel.SHUTDOWN,
                                 "The integration connector {0} in integration service {1} has shutdown.  Statistics recorded were: {2}",
                                 "The local administrator has requested shut down of the hosting integration service.  " +
                                           "Once the connector is disconnected, no more metadata exchange will happen between this connector's " +
                                           "third party technology and the open metadata ecosystem.",
                                 "Verify that this shutdown is intended and this integration connector is no longer needed."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0025 - The integration service {0} in integration daemon {1} has completed shutdown
     */
    INTEGRATION_SERVICE_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0025",
                                 AuditLogRecordSeverityLevel.SHUTDOWN,
                                 "The integration service {0} in integration daemon {1} has completed shutdown",
                                 "The integration service has disconnected all of its connectors.",
                                 "Verify that there are no errors reported by the integration connectors as they shutdown."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0026 - The integration daemon {0} has completed shutdown
     */
    SERVER_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0026",
                    AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The integration daemon {0} has completed shutdown",
                         "The local administrator has requested shut down of this integration daemon server and the operation has completed.",
                         "Verify that all integration connectors that support the metadata exchange have shut down successfully."),


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
                                      "refresh-config command or wait for the engine host services to retry the configuration request."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0028 - Failed to refresh configuration for integration group {0}.  The exception was {1} with error message {2}
     */
    INTEGRATION_GROUP_NO_CONFIG("INTEGRATION-DAEMON-SERVICES-0028",
                                AuditLogRecordSeverityLevel.INFO,
                                "Failed to refresh configuration for integration group {0}.  The exception was {1} with error message {2}",
                                "The integration group is unable to process any integration connector requests until its configuration can be retrieved.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the integration daemon services to refresh the configuration, or issue the refreshConfig " +
                                        "call to request that the integration group calls the Governance Engine OMAS to refresh the configuration for " +
                                        "the integration group."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0030 - The dedicated thread for integration connector {0} has started in integration daemon {1}
     */
    CONNECTOR_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0030",
                              AuditLogRecordSeverityLevel.STARTUP,
                    "The dedicated thread for integration connector {0} has started in integration daemon {1}",
                    "The server will call the integration connector's engage() method to indicate that it can issue blocking calls.",
                    "Ensure that the connector is running successfully."),


    /**
     * INTEGRATION-DAEMON-SERVICES-0031 - The integration connector {0} method {1} has returned with a {2} exception containing message {3}
     */
    CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0031",
                    AuditLogRecordSeverityLevel.EXCEPTION,
                     "The integration connector {0} method {1} has returned with a {2} exception containing message {3}",
                              "The server will change the integration connector's status to failed.",
                              "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then restart the connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0032 - The integration connector {0} method {1} has not been called because it previously
     * returned with a {2} exception containing message {4}
     */
    CONNECTOR_FAILED("INTEGRATION-DAEMON-SERVICES-0032",
                     AuditLogRecordSeverityLevel.ERROR,
                    "The integration connector {0} method {1} has not been called because it previously returned with a {2} exception " +
                             "containing message {4}",
                    "The server skips all calls to this connector until it is restarted.",
                    "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then, if appropriate, restart the connector."),

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
                    "Verify that the connector is not reporting errors which have caused it to terminate prematurely."),

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
                                         "the connector."),

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
                            "track down and resolve the cause of the error and then restart the connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0037 - The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector
     */
    CONFIG_ERROR("INTEGRATION-DAEMON-SERVICES-0037",
                 AuditLogRecordSeverityLevel.ERROR,
                     "The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector",
                     "The server will change the integration connector's status to Configuration Failed.  It will ignore the connector during each refresh() call until the connector is restarted with workable configuration.",
                     "Check the configuration of the connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0040 - The integration daemon thread for integration daemon {0} has started
     */
    DAEMON_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0040",
                           AuditLogRecordSeverityLevel.STARTUP,
                              "The integration daemon thread for integration daemon {0} has started",
                              "The thread will periodically call refresh() on the integration connectors hosted in this daemon.  " +
                                   "The time between each refresh is set up in the configuration for the integration connector.",
                              "Ensure that the connector is running successfully."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0041 - Integration connector {0} is refreshing for the first time in the {1} integration daemon
     */
    DAEMON_CONNECTOR_FIRST_REFRESH("INTEGRATION-DAEMON-SERVICES-0041",
                                   AuditLogRecordSeverityLevel.INFO,
                           "Integration connector {0} is refreshing for the first time in the {1} integration daemon",
                           "The thread is about to call refresh() on the integration connector hosted in this daemon for the first time.",
                           "Ensure that the connector does not report any errors during the refresh processing."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0042 - Integration connector {0} is refreshing again in {1} integration daemon
     */
    DAEMON_CONNECTOR_REFRESH("INTEGRATION-DAEMON-SERVICES-0042",
                             AuditLogRecordSeverityLevel.INFO,
                             "Integration connector {0} is refreshing again in {1} integration daemon",
                             "The thread is about to call refresh() on the integration connector hosted in this daemon.",
                             "Ensure that the connector does not report any errors during the refresh processing."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0043 - The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond(s)
     */
    DAEMON_CONNECTOR_REFRESH_COMPLETE("INTEGRATION-DAEMON-SERVICES-0043",
                                      AuditLogRecordSeverityLevel.INFO,
                             "The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond(s)",
                             "The to call refresh() has returned.",
                             "Verify that the time between refresh calls is appropriate for the connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0044 - The integration daemon thread for integration daemon {0} is shutting down
     */
    DAEMON_THREAD_TERMINATING("INTEGRATION-DAEMON-SERVICES-0044",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                           "The integration daemon thread for integration daemon {0} is shutting down",
                           "The thread will stop calling refresh() on the integration connectors hosted in this daemon and stop running.",
                           "Ensure that the thread terminates without errors."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0045 - The integration daemon thread for integration daemon {0} caught a {1} exception from an integration connector containing message {2}
     */
    DAEMON_THREAD_CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0045",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                    "The integration daemon thread for integration daemon {0} caught a {1} exception from an integration connector containing message {2}",
                    "The integration daemon thread will move to the next connector and revisit this connector at the next refresh time.",
                    "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then restart the connector.  The integration daemon thread will then continue to call the connector."),

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
                                  "leaving the rest of the integration connectors working with the current userId."),

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
                                                   "Then it is ready to process new refresh requests."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0052 - The integration group {0} in server {1} is shutting down
     */
    GROUP_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0052",
                   AuditLogRecordSeverityLevel.SHUTDOWN,
                   "The integration group {0} in server {1} is shutting down",
                   "The local administrator has requested shut down of this integration group.  No more governance requests will be processed by this engine.",
                   "Verify that this shutdown is intended and the integration group is no longer needed."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0053 - Failed to process a change to integration group {0}.  The exception was {1} with error message {2}
     */
    GROUP_CHANGE_FAILED("INTEGRATION-DAEMON-SERVICES-0053",
                        AuditLogRecordSeverityLevel.EXCEPTION,
                        "Failed to process a change to integration group {0}.  The exception was {1} with error message {2}",
                        "The integration daemon is unable to process the change to a governance group.  The exception explains the reason.",
                        "Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to " +
                                     "refresh the configuration of the integration group by calling the integration daemon's refreshConfig service."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0054 - Failed to process a change to integration connector {0}.  The exception was {1} with error message {2}
     */
    CONNECTOR_CHANGE_FAILED("INTEGRATION-DAEMON-SERVICES-0054",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                        "Failed to process a change to integration connector {0}.  The exception was {1} with error message {2}",
                        "The integration daemon is unable to process the change to a integration connector.  The exception explains the reason.",
                        "Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to " +
                                "refresh the configuration of the integration group by calling the integration daemon's refreshConfig service."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0055 - Failed to start up integration connector {0} because its interface does not match to an integration
     * service connector interface that is supported by this server
     */
    UNKNOWN_CONNECTOR_INTERFACE("INTEGRATION-DAEMON-SERVICES-0055",
                                AuditLogRecordSeverityLevel.ERROR,
                            "Failed to start up integration connector {0} because its interface does not match to an integration service connector " +
                                        "interface that is supported by this server",
                            "The integration connector is ignored.",
                            "Review the implementation of the integration connector and ensure it is implementing an appropriate interface.  Once " +
                                        "resolved, the integration daemon's platform needs to be restarted to load the new implementation for the " +
                                        "integration connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0056 - Refresh of all integration connector configuration has being completed for integration group {0}
     */
    FINISHED_ALL_INTEGRATION_CONNECTOR_CONFIG("INTEGRATION-DAEMON-SERVICES-0056",
                                              AuditLogRecordSeverityLevel.INFO,
                                              "Refresh of all integration connector configuration has being completed for integration group {0}",
                                              "The integration connectors for this integration group are running with the latest configuration.",
                                              "No action is required as long as all the expected integration connectors are started." +
                                                   "If there are any errors reported by the integration connectors then validate the configuration " +
                                                   "of the integration connector and its associated integration group in the metadata server."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0057 - User {0} has updated the endpoint network address for the integration connector {1} in integration daemon {2} to: {3}
     */
    DAEMON_CONNECTOR_ENDPOINT_UPDATE("INTEGRATION-DAEMON-SERVICES-0057",
                                     AuditLogRecordSeverityLevel.INFO,
                                         "User {0} has updated the endpoint network address for the integration connector {1} in integration daemon {2} to: {3}",
                                         "The connector will be restarted once the new endpoint network address is in place.",
                                         "Ensure that the connector does not report any errors during the restart processing as it connects to the new endpoint."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0058 - User {0} has attempted to update the endpoint network address for the integration connector {1} in integration daemon {2} to {3} but this connector does not have an endpoint defined
     */
    DAEMON_CONNECTOR_NO_ENDPOINT_TO_UPDATE("INTEGRATION-DAEMON-SERVICES-0058",
                                           AuditLogRecordSeverityLevel.ERROR,
                                     "User {0} has attempted to update the endpoint network address for the integration connector {1} in integration daemon {2} to {3} but this connector does not have an endpoint defined",
                                     "The connector continues to operate as before.",
                                     "If the connector should have an endpoint then update the whole connection for the connector."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0059 - User {0} has updated the connection for the integration connector {1} in integration daemon {2}
     */
    DAEMON_CONNECTOR_CONNECTION_UPDATE("INTEGRATION-DAEMON-SERVICES-0059",
                                       AuditLogRecordSeverityLevel.INFO,
                                     "User {0} has updated the connection for the integration connector {1} in integration daemon {2}",
                                     "The connector will be restarted once the new connection is in place.",
                                     "Ensure that the connector does not report any errors during the restart processing as it operates with this new connection information."),


    /**
     * INTEGRATION-DAEMON-SERVICES-0060 - The integration connector refresh thread for integration connector {0} has started
     */
    REFRESH_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0060",
                           AuditLogRecordSeverityLevel.STARTUP,
                           "The integration connector refresh thread for integration connector {0} has started",
                           "The thread will periodically call refresh() on the integration connector.  " +
                                   "The time between each refresh is set up in the configuration for the integration connector.",
                           "Ensure that the integration connector is running successfully."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0064 - The integration connector refresh thread for integration connector {0} is shutting down
     */
    REFRESH_THREAD_TERMINATING("INTEGRATION-DAEMON-SERVICES-0064",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                              "The integration connector refresh thread for integration connector {0} is shutting down",
                              "The thread will stop calling refresh() on the integration connectors hosted in this daemon and stop running.",
                              "Ensure that the thread terminates without errors."),

    /**
     * INTEGRATION-DAEMON-SERVICES-0065 - The integration connector refresh thread for integration connector {0} caught a {1} exception  containing message {2}
     */
    REFRESH_THREAD_CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0065",
                                  AuditLogRecordSeverityLevel.EXCEPTION,
                                  "The integration connector refresh thread for integration connector {0} caught a {1} exception containing message {2}",
                                  "The integration connector thread will revisit this connector at the next refresh time.",
                                  "Use the message from the exception and knowledge of the integration connector's behavior to " +
                                          "track down and resolve the cause of the error and then restart the connector.  " +
                                           "The integration connector refresh thread will then continue to call the connector."),

    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


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
     */
    IntegrationDaemonServicesAuditCode(String                      messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                      message,
                                       String                      systemAction,
                                       String                      userAction)
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
    public AuditLogMessageDefinition getMessageDefinition(String... params)
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
                                                                                    systemAction,
                                                                                    userAction);
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
                       '}';
    }
}
