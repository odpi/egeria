/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The IntegrationDaemonServicesAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum IntegrationDaemonServicesAuditCode implements AuditLogMessageSet
{
    SERVER_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0001",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The integration daemon services are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as an integration daemon.  " +
                                 "Within the integration daemon are one or more Open Metadata Integration Services (OMISs) that host " +
                                "integration connectors to exchange metadata with third party technologies.",
                        "Verify that the start up sequence goes on to initialize the configured integration services."),

    INTEGRATION_SERVICE_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0002",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The {0} is initializing in server {1}",
                        "A new Open Metadata Integration Service (OMIS) is starting up in the integration daemon.  " +
                                "It will begin to initialize its context manager and the integration connectors that will exchange metadata " +
                                             "between the open metadata ecosystem and a third party technology.",
                        "Verify that the start up sequence goes on to initialize the configured integration connectors."),

    NO_OMAS_SERVER_URL("INTEGRATION-DAEMON-SERVICES-0003",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Integration service {0} is not configured with the platform URL root for its partner OMAS {1}",
                         "The service is not able to connect to the open metadata ecosystem.  It fails to start.",
                         "Add the platform URL root of the OMAG server where the partner OMAS is running " +
                               "to this integration service's configuration."),

    NO_OMAS_SERVER_NAME("INTEGRATION-DAEMON-SERVICES-0004",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Integration service {0} is not configured with the name for the server running its partner OMAS {1}",
                      "The service is not able to connect to the open metadata ecosystem.  It fails to start.",
                     "Add the server name of the OMAG server where the partner OMAS is running " +
                                "to this integration service's configuration."),

    NULL_CONTEXT_MANAGER("INTEGRATION-DAEMON-SERVICES-0005",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "The integration service {0} has been configured with a null context manager class in integration daemon {1}",
                         "The integration service fails to start because it is not able to initialize any integration " +
                                 "connectors.",
                         "The standard integration services are registered in a static method by the IntegrationDaemonHandler.  " +
                                 "If this integration service is one of these services, correct the logic to include the " +
                                 "context manager name.  If this integration service comes from a third party, make sure the class name " +
                                 "is specified when the third party integration service is configured."),

    INVALID_CONTEXT_MANAGER("INTEGRATION-DAEMON-SERVICES-0006",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
                            "The integration service {0} has been configured with a context manager class of {1} which can not be " +
                                    "used by the class loader.  The {2} exception was returned with message {3}",
                            "The integration service fails to start.  Its connectors, if any, are not activated.",
                            "Check that the jar for the context manager's class is visible to the OMAG Server Platform through " +
                                    "the class path - and that the class name specified includes the full, correct package name and class name.  " +
                                    "Once the class is correctly set up, restart the integration daemon.  It will be necessary to restart the " +
                                    "OMAG Server Platform if the class path needed adjustment. "),

    NO_INTEGRATION_CONNECTORS("INTEGRATION-DAEMON-SERVICES-0007",
                      OMRSAuditLogRecordSeverity.ERROR,
                      "The {0} integration service is configured without any integration connectors",
                      "The integration service completes initialization with no errors.",
                      "Add the connection for at least one integration connector to the integration service's section " +
                                      "of this integration daemon's configuration document and then restart the integration daemon."),

    INTEGRATION_CONNECTOR_INITIALIZING("INTEGRATION-DAEMON-SERVICES-0008",
                      OMRSAuditLogRecordSeverity.STARTUP,
                      "A new integration connector named {0} is initializing in integration service {1} running in integration daemon {2}, permitted synchronization is: {3}",
                      "The integration service is initializing an integration connector using the information in the configured " +
                                               "connection.",
                      "Verify that this connector is successfully initialized."),

    BAD_INTEGRATION_CONNECTION("INTEGRATION-DAEMON-SERVICES-0009",
                      OMRSAuditLogRecordSeverity.STARTUP,
                      "A new integration connector named {0} failed to initialize in integration service {1}.  " +
                                       "The exception returned was {2} with a message of {3}",
                      "The integration service fails to initialize.  This, in turn causes the integration daemon to fail to start.",
                      "Correct the connection for this integration connector in the integration service's section " +
                                       "of this integration daemon's configuration document and then " +
                                       "restart the integration daemon."),

    NOT_INTEGRATION_CONNECTOR("INTEGRATION-DAEMON-SERVICES-0010",
                     OMRSAuditLogRecordSeverity.STARTUP,
                     "The connection for integration connector named {0} created a connector of class {1} which does not implement the " +
                                      "correct {2} interface",
                     "The integration service, and hence the hosting integration daemon, fails to start.",
                              "Change the connection in the integration service's section " +
                                      "of this integration daemon's configuration document to a valid integration connector and then " +
                                      "restart the integration daemon."),

    INTEGRATION_SERVICE_INITIALIZED("INTEGRATION-DAEMON-SERVICES-0011",
                     OMRSAuditLogRecordSeverity.STARTUP,
                     "The {0} integration service has completed its initialization in integration daemon {1}.  It is running {2} integration " +
                                            "connector(s)",
                     "The integration service hands over its integration connectors to the integration daemon to manage.",
                     "Verify that there were no errors starting the integration connectors for this service."),

    SERVICE_INSTANCE_FAILURE("INTEGRATION-DAEMON-SERVICES-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The integration daemon services are unable to initialize a new instance of integration daemon {0}; " +
                                     "error message is {1}",
                             "The integration daemon services detected an error during the start up of a specific integration daemon " +
                                     "instance.  Its integration services are not available.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the integration daemon."),

    SERVER_INITIALIZED("INTEGRATION-DAEMON-SERVICES-0013",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The integration daemon {0} has initialized",
                       "The integration daemon services has completed initialization.",
                       "Verify that all the configured integration services, and their connectors within have successfully started and" +
                               "are able to connect both to their third party technology and their partner OMAS."),

    NO_PERMITTED_SYNCHRONIZATION("INTEGRATION-DAEMON-SERVICES-0014",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The integration service {0} does not have a default permitted synchronization value set.",
                       "The integration daemon is not able to initialize one of the configured integration because its defaultPermittedSynchronization value is null.  " +
                               "The integration daemon shuts down and this error is reported to the caller as a configuration exception.",
                       "Update the configuration for the integration service to include a value for the default permitted synchronization."),

    DAEMON_CONNECTOR_CONFIG_PROPS_UPDATE("INTEGRATION-DAEMON-SERVICES-0015",
                                         OMRSAuditLogRecordSeverity.INFO,
                                         "User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3}",
                                         "The connector will be restarted once the new properties are in place.",
                                         "Ensure that the connector does not report any errors during the restart processing as it operated on its new properties."),

    DAEMON_CONNECTOR_CONFIG_PROPS_CLEARED("INTEGRATION-DAEMON-SERVICES-0016",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2}",
                                          "The connector will be restarted once the properties are cleared.",
                                          "Ensure that the connector does not report any errors during the restart processing as it operated on its default properties."),

    SERVER_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0020",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The integration daemon {0} is shutting down",
                    "The local administrator has requested shut down of this integration daemon server.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    SERVICE_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0021",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                          "The integration service {0} in integration daemon {1} is shutting down",
                          "The local administrator has requested shut down of this integration service.  Once shutdown" +
                                  "is complete, no more metadata exchange will be processed by the integration connectors in this service.",
                          "Verify that there are no errors reported by the integration connectors as they shutdown."),

    CONNECTOR_SHUTTING_DOWN("INTEGRATION-DAEMON-SERVICES-0022",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The integration connector {0} is shutting down",
                         "The local administrator has requested shut down of the hosting integration daemon server.",
                         "Verify that this connector is no longer needed and the shutdown is expected."),

    CONNECTOR_SHUTDOWN_FAILURE("INTEGRATION-DAEMON-SERVICES-0023",
                            OMRSAuditLogRecordSeverity.SHUTDOWN,
                            "The integration connector {0} reported an error on disconnect.  The exception was {1} with message {2}",
                            "The integration service moves on to shut down its other connectors.",
                            "Review the error message and other diagnostics produced by the connector to determine the source of the " +
                                       "error.  Release any locked resources.  If a particular connector is repeatedly problematic and the " +
                                       "code can not be improved, then it may need to be isolated to its own integration daemon running on " +
                                       "its own OMAG Server Platform to allow the platform to be restarted to allow the operating system or " +
                                       "container to clear resources held by the connector."),

    INTEGRATION_CONNECTOR_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0024",
                                 OMRSAuditLogRecordSeverity.SHUTDOWN,
                                 "The integration connector {0} in integration service {1} has shutdown.  Statistics recorded were: {2}",
                                 "The local administrator has requested shut down of the hosting integration service.  " +
                                           "Once the connector is disconnected, no more metadata exchange will happen between this connector's " +
                                           "third party technology and the open metadata ecosystem.",
                                 "Verify that this shutdown is intended and this integration connector is no longer needed."),

    INTEGRATION_SERVICE_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0025",
                                 OMRSAuditLogRecordSeverity.SHUTDOWN,
                                 "The integration service {0} in integration daemon {1} has completed shutdown.",
                                 "The integration service has disconnected all of its connectors.",
                                 "Verify that there are no errors reported by the integration connectors as they shutdown."),

    SERVER_SHUTDOWN("INTEGRATION-DAEMON-SERVICES-0026",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The integration daemon {0} has completed shutdown",
                         "The local administrator has requested shut down of this integration daemon server and the operation has completed.",
                         "Verify that all integration connectors that support the metadata exchange have shut down successfully."),


    CONNECTOR_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0030",
                    OMRSAuditLogRecordSeverity.STARTUP,
                    "The dedicated thread for integration connector {0} has started in integration daemon {1}",
                    "The server will call the integration connector's engage() method to indicate that it can issue blocking calls.",
                    "Ensure that the connector is running successfully."),


    CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0031",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                     "The integration connector {0} method {1} has returned with a {2} exception containing message {3}",
                              "The server will change the integration connector's status to failed.",
                              "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then restart the connector."),

    CONNECTOR_FAILED("INTEGRATION-DAEMON-SERVICES-0032",
                    OMRSAuditLogRecordSeverity.ERROR,
                    "The integration connector {0} method {1} has not been called because it previously returned with a {2} exception " +
                             "containing message {4}",
                    "The server skips all calls to this connector until it is restarted.",
                    "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then, if appropriate, restart the connector."),


    ENGAGE_IMPLEMENTATION_MISSING("INTEGRATION-DAEMON-SERVICES-0033",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The integration connector {0} has been configured to have its own thread to issue blocking calls but has not " +
                                          "implemented the engage() method",
                    "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                          "It called the engage() method on this thread.  However, the default implementation of the " +
                                          "engage() method has been invoked suggesting that either the dedicated thread is not needed or " +
                                          "there is an error in the implementation of the connector.  The integration daemon " +
                                          "will terminate the thread once the engage() method returns.",
                    "If the connector does not need to issue blocking calls update the configuration to remove the need for the " +
                                          "dedicated thread.  Otherwise update the integration connector's implementation to override " +
                                          "the default engage() method implementation."),

    ENGAGE_RETURNED("INTEGRATION-DAEMON-SERVICES-0034",
                    OMRSAuditLogRecordSeverity.INFO,
                    "The integration connector {0} has returned from the engage() method in integration daemon {1}",
                    "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                          "It called the engage() method on this thread.  The engage() method has returned which means the " +
                                         "connector has finished its processing of a single blocking call.  " +
                                         "The integration daemon will wait one minute and then call engage() again unless the server is " +
                                         "shutting down.",
                    "Verify that the connector is not reporting errors which have caused it to terminate prematurely."),

    CONNECTOR_THREAD_TERMINATING("INTEGRATION-DAEMON-SERVICES-0035",
                                 OMRSAuditLogRecordSeverity.SHUTDOWN,
                                 "The dedicated thread for integration connector {0} is terminating in integration daemon {1}",
                                 "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                         "The integration daemon is shutting down and has requests that the dedicated thread for this " +
                                         "connector terminates.",
                                 "Verify that there are no errors as the thread terminates.  In particular, if the thread detects" +
                                         "shutdown after the integration daemon has completed, there should still be an orderly shutdown of " +
                                         "the connector."),

    INITIALIZE_ERROR("INTEGRATION-DAEMON-SERVICES-0036",
                    OMRSAuditLogRecordSeverity.ERROR,
                    "The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to connect to the associated metadata server",
                    "The server will change the integration connector's status to Initialize Failed.  It will retry the call to the metadata server during each refresh() call until the metadata server is contacted.",
                    "Check the status of the associated metadata server - it may need restarting.  Alternatively, the integration " +
                            "connector may be configured with the wrong metadata server, in which case the integration connector's " +
                            "configuration needs updating and the integration daemon will need restarting.  " +
                            "If neither of these are the cause of the problem, use the message from the exception and knowledge of the open metadata landscape to " +
                            "track down and resolve the cause of the error and then restart the connector."),

    CONFIG_ERROR("INTEGRATION-DAEMON-SERVICES-0037",
                     OMRSAuditLogRecordSeverity.ERROR,
                     "The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector",
                     "The server will change the integration connector's status to Configuration Failed.  It will ignore the connector during each refresh() call until the connector is restarted with workable configuration.",
                     "Check the configuration of the connector."),

    DAEMON_THREAD_STARTING("INTEGRATION-DAEMON-SERVICES-0040",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The integration daemon thread for integration daemon {0} has started",
                              "The thread will periodically call refresh() on the integration connectors hosted in this daemon.  " +
                                   "The time between each refresh is set up in the configuration for the integration connector.",
                              "Ensure that the connector is running successfully."),

    DAEMON_CONNECTOR_FIRST_REFRESH("INTEGRATION-DAEMON-SERVICES-0041",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The integration daemon thread is refreshing integration connector {0} for the first time in " +
                                           "the {1} integration daemon instance",
                           "The thread is about to call refresh() on the integration connectors hosted in this daemon.",
                           "Ensure that the connector does not report any errors during the refresh processing."),

    DAEMON_CONNECTOR_REFRESH("INTEGRATION-DAEMON-SERVICES-0042",
                             OMRSAuditLogRecordSeverity.INFO,
                             "The integration daemon thread is refreshing integration connector {0} in integration daemon {1}",
                             "The thread is about to call refresh() on the integration connectors hosted in this daemon.",
                             "Ensure that the connector does not report any errors during the refresh processing."),

    DAEMON_CONNECTOR_REFRESH_COMPLETE("INTEGRATION-DAEMON-SERVICES-0043",
                             OMRSAuditLogRecordSeverity.INFO,
                             "The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond",
                             "The to call refresh() has returned.",
                             "Verify that the time between refresh calls is appropriate for the connector."),

    DAEMON_THREAD_TERMINATING("INTEGRATION-DAEMON-SERVICES-0044",
                           OMRSAuditLogRecordSeverity.SHUTDOWN,
                           "The integration daemon thread for integration daemon {0} is shutting down",
                           "The thread will stop calling refresh() on the integration connectors hosted in this daemon and stop running.",
                           "Ensure that the thread terminates without errors."),

    DAEMON_THREAD_CONNECTOR_ERROR("INTEGRATION-DAEMON-SERVICES-0045",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    "The integration daemon thread for integration daemon {0} caught a {1} exception from an integration connector containing message {2}",
                    "The integration daemon thread will move to the next connector and revisit this connector at the next refresh time.",
                    "Use the message from the exception and knowledge of the integration connector's behavior to " +
                            "track down and resolve the cause of the error and then restart the connector.  The integration daemon thread will then continue to call the connector."),

    SERVER_NOT_AUTHORIZED("INTEGRATION-DAEMON-SERVICES-0050",
                          OMRSAuditLogRecordSeverity.SECURITY,
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


    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for IntegrationDaemonServicesAuditCode expects to be passed one of the enumeration rows defined in
     * IntegrationDaemonServicesAuditCode above.   For example:
     *
     *     IntegrationDaemonServicesAuditCode   auditCode = IntegrationDaemonServicesAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    IntegrationDaemonServicesAuditCode(String                     messageId,
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
        return "IntegrationDaemonServicesAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
