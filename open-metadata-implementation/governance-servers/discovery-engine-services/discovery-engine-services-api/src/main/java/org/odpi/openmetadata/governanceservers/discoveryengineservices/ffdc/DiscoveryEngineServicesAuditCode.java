/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;



/**
 * The DiscoveryEngineServicesAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DiscoveryEngineServicesAuditCode implements AuditLogMessageSet
{
    SERVER_INITIALIZING("DISCOVERY-ENGINE-SERVICES-0001",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The Discovery engine services are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as a discovery server.  " +
                                 "Within the discovery server are one or more discovery engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "discovery engines is retrieved from the metadata server and the discovery engines are initialized.",
                        "Verify that the start up sequence goes on to initialize the configured discovery engines."),

    NO_OMAS_SERVER_URL("DISCOVERY-ENGINE-SERVICES-0002",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Discovery server {0} is not configured with the platform URL root for the Discovery Engine OMAS",
                         "The server is not able to retrieve its configuration.  It fails to start.",
                         "Add the platform URL root of the open metadata server where the Discovery Engine OMAS is running " +
                               "to this discovery server's configuration document."),

    NO_OMAS_SERVER_NAME("DISCOVERY-ENGINE-SERVICES-0003",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Discovery server {0} is not configured with the name for the server running the Discovery Engine OMAS",
                      "The server is not able to retrieve its configuration.  It fails to start.",
                     "Add the server name of the open metadata server where the Discovery Engine OMAS is running " +
                                "to this discovery server's configuration document."),

    NO_DISCOVERY_ENGINES("DISCOVERY-ENGINE-SERVICES-0004",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Discovery server {0} is configured with no discovery engines",
                       "The server is not able to run any discovery requests.  It fails to start.",
                       "Add the qualified name for at least one discovery engine to this discovery server's configuration document."),

    ENGINE_INITIALIZING("DISCOVERY-ENGINE-SERVICES-0005",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "A new discovery engine instance {0} is initializing in discovery server {1}",
                        "The discovery server is initializing a discovery engine to analyze the " +
                                "content of assets on demand and create annotation metadata.  The configuration for this discovery engine is " +
                                "retrieved from the Discovery Engine OMAS running in the metadata server",
                        "Verify that this discovery engine successfully retrieves its configuration from the metadata server."),
    SERVER_NOT_AUTHORIZED("DISCOVERY-ENGINE-SERVICES-0007",
                     OMRSAuditLogRecordSeverity.EXCEPTION,
                     "Discovery server {0} is not authorized to retrieve any its configuration from the Discovery Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                     "The discovery engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any discovery requests.",
                     "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                             "It could be because because this discovery server is configured with the wrong userId, it is calling the wrong server, or the " +
                                  "remote server is correct but it needs updating to allow this userId.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the discovery engine"),

    SUPPORTED_DISCOVERY_TYPE("DISCOVERY-ENGINE-SERVICES-0008",
                             OMRSAuditLogRecordSeverity.INFO,
                             "Discovery engine {0} in discovery server {1} is configured to process discovery requests of type {2}",
                             "The discovery engine has successfully retrieved the configuration to run analysis requests for the named discovery " +
                                     "request type.  It is ready to run discovery requests of this type",
                             "Verify that this is an appropriate discovery request type for the discovery engine."),

    NO_SUPPORTED_REQUEST_TYPES("DISCOVERY-ENGINE-SERVICES-0009",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Discovery engine {0} in discovery server {1} is not configured to support any type of discovery requests",
                               "The discovery engine has no configuration that links it to a discovery request type and a corresponding discovery " +
                                       "discovery service.  It is not able to process any discovery requests because it would not know what to run.",
                               "Add the configuration for at least one registered discovery service (and corresponding discovery request type to " +
                                       "this discovery engine."),

    REQUEST_TYPE_CONFIG_ERROR("DISCOVERY-ENGINE-SERVICES-0010",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "Discovery engine {0} in discovery server {1} is not able to retrieve its configured discovery request types from the " +
                                    "Discovery Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                              "The discovery engine has not been able to retrieve its configuration.  It is not able to process any discovery " +
                                    "requests until this configuration is available.",
                              "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                                    "The error message should help to narrow down the cause of the error.  " +
                                    "Once the problem has been resolved, restart the discovery engine."),

    ENGINE_INITIALIZED("DISCOVERY-ENGINE-SERVICES-0011",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The discovery engine {0} in discovery server {1} has initialized",
                        "The discovery engine has completed initialization and is ready to receive discovery requests.",
                        "Verify that the discovery engine has been initialized wit the correct list of discovery request types."),

    SERVICE_INSTANCE_FAILURE("DISCOVERY-ENGINE-SERVICES-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The discovery engine services are unable to initialize a new instance of discovery server {0}; error message is {1}",
                             "The discovery engine services detected an error during the start up of a specific discovery server instance.  Its discovery services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVER_INITIALIZED("DISCOVERY-ENGINE-SERVICES-0013",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The discovery server {0} has initialized",
                       "The discovery server has completed initialization.",
                       "Verify that all of the configured discovery engines have successfully started and retrieved their configuration."),

    SERVER_SHUTTING_DOWN("DISCOVERY-ENGINE-SERVICES-0014",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The discovery server {0} is shutting down",
                    "The local administrator has requested shut down of this discovery server.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    ENGINE_SHUTDOWN("DISCOVERY-ENGINE-SERVICES-0015",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The discovery engine {0} in discovery server {1} is shutting down",
                    "The local administrator has requested shut down of this discovery engine.  No more discovery requests will be processed by this engine.",
                    "Verify that this shutdown is intended and the discovery engine is no longer needed."),

    SERVER_SHUTDOWN("DISCOVERY-ENGINE-SERVICES-0016",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The discovery server {0} has completed shutdown",
                         "The local administrator has requested shut down of this discovery server and the operation has completed.",
                         "Verify that all configured discovery engines shut down successfully."),

    DISCOVERY_SERVICE_STARTING("DISCOVERY-ENGINE-SERVICES-0017",
                    OMRSAuditLogRecordSeverity.STARTUP,
                    "The discovery service {0} is starting to analyze asset {1} with discovery request type {2} in discovery engine {3} (guid={4});" +
                                       " the results will be stored in discovery analysis report {5}",
                    "A new discovery request is being processed.",
                    "Verify that the discovery service ran to completion."),

    DISCOVERY_SERVICE_FAILED("DISCOVERY-ENGINE-SERVICES-0018",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    "The discovery service {0} threw an exception of type {1} during the generation of discovery analysis report {2} for asset {3} " +
                                     "during discovery request type {4} in discovery engine {5} (guid={6}). The error message was {7}",
                    "A discovery service failed to complete the analysis of an asset.",
                    "Review the exception to determine the cause of the error."),

    DISCOVERY_SERVICE_COMPLETE("DISCOVERY-ENGINE-SERVICES-0019",
                               OMRSAuditLogRecordSeverity.SHUTDOWN,
                               "The discovery service {0} has completed the analysis of asset {1} with discovery request type {2} in {3} " +
                                       "milliseconds; the results are stored in discovery analysis report {4}",
                               "A discovery request has completed.",
                               "It is possible to query the result of the discovery request through the discovery server's discovery engine " +
                                       "services interface."),

    NO_DISCOVERY_ENGINES_STARTED("DISCOVERY-ENGINE-SERVICES-0020",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Discovery server {0} is unable to start any discovery engines",
                         "The server is not able to run any discovery requests.  It fails to start.",
                         "Add the configuration for at least one discovery engine to this discovery server."),

    EXC_ON_ERROR_STATUS_UPDATE("DISCOVERY-ENGINE-SERVICES-0021",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "Discovery engine {0} is unable to update the status for discovery service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a discovery request. The discovery report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_ENGINE_NAME( "DISCOVERY-ENGINE-SERVICES-0022",
                                   OMRSAuditLogRecordSeverity.STARTUP,
                                  "Discovery engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} " +
                                           "returned to discovery server {4}",
                                  "The discovery server is not able to initialize the discovery engine and so it will not de able to support " +
                                           "discovery requests targeted to this discovery engine until this configuration is available.",
                                  "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the discovery server.  Once the cause is resolved, restart the discovery server."),

    NO_CONFIGURATION_LISTENER("DISCOVERY-ENGINE-SERVICES-0023",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The discovery engine services are unable to retrieve the connection for the configuration " +
                                      "listener for discovery server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The discovery server continues to run.  The discovery engine services will start up the " +
                                                    "discovery engines and they will operate with whatever configuration that they can retrieve.  " +
                                                    "Periodically the discovery engine services will" +
                                                    "retry the request to retrieve the connection information.  " +
                                                    "Without the connection, the discovery server will not be notified of changes to the discovery " +
                                                    "engines' configuration",
                              "This problem may be caused because the discovery server has been configured with the wrong location for the " +
                                                    "metadata server, or the metadata server is not running the Discovery Engine OMAS service or " +
                                                    "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                                    "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                                    "refresh-config command or wait for the discovery server to retry the configuration request."),

    CONFIGURATION_LISTENER_REGISTERED("DISCOVERY-ENGINE-SERVICES-0024",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The discovery engine services has registered the configuration " +
                                      "listener for discovery server {0}.  It will receive configuration updates from metadata server {1}",
                              "The discovery server continues to run.  The discovery engine services will start up the " +
                                      "discovery engines and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the discovery engine services will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the discovery server will not be notified of changes to the discovery " +
                                      "engines' configuration",
                              "This problem may be caused because the discovery server has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the Discovery Engine OMAS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the discovery server to retry the configuration request."),

    CLEARING_ALL_DISCOVERY_SERVICE_CONFIG("DISCOVERY-ENGINE-SERVICES-0025",
                                 OMRSAuditLogRecordSeverity.INFO,
                                 "All discovery service configuration is being refreshed for discovery engine {0}",
                                 "The discovery server will call the Discovery Engine OMAS in the metadata server to " +
                                                  "retrieve details of all of the discovery services configured for this engine." +
                                                  "During this process, some discovery request may fail if the associated discovery" +
                                                  "service is only partially configured.",
                                 "Monitor the discovery server to ensure all of the discovery services are retrieved." +
                                                  "Then it is ready to process new discovery requests."),

    FINISHED_ALL_DISCOVERY_SERVICE_CONFIG("DISCOVERY-ENGINE-SERVICES-0026",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Refreshing all discovery service configuration has being completed for discovery engine {0}",
                                          "The discovery engine is ready to receive discovery requests for all successfully loaded " +
                                                  "discovery services.",
                                          "No action is required as long as all of the expected discovery services are loaded." +
                                                  "If there are any discovery services missing then validate the configuration of" +
                                                  "the discovery engine in the metadata server."),

    DISCOVERY_SERVICE_NO_CONFIG("DISCOVERY-ENGINE-SERVICES-0027",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Failed to refresh configuration for discovery service registered as " +
                                        "{0} for discovery request types {1}.  The exception was {2} with error message {3}",
                                          "The discovery engine is unable to process discovery request types for the failed discovery service.",
                                          "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the discovery server to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the discovery engine calls the Discovery Engine OMAS to refresh the configuration for" +
                                        " the discovery service."),

    DISCOVERY_ENGINE_NO_CONFIG("DISCOVERY-ENGINE-SERVICES-0028",
                                OMRSAuditLogRecordSeverity.ERROR,
                                "Failed to refresh configuration for discovery engine {0}.  The exception was {1} with error message {2}",
                                "The discovery engine is unable to process any discovery requests until its configuration can be retrieved.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the discovery server to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the discovery engine calls the Discovery Engine OMAS to refresh the configuration for" +
                                        " the discovery service."),



    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for DiscoveryEngineServicesAuditCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineServicesAuditCode above.   For example:
     *
     *     DiscoveryEngineServicesAuditCode   auditCode = DiscoveryEngineServicesAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DiscoveryEngineServicesAuditCode(String                     messageId,
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
        return "DiscoveryEngineServicesAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
