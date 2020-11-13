/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;



/**
 * The StewardshipEngineServicesAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum StewardshipEngineServicesAuditCode implements AuditLogMessageSet
{
    SERVER_INITIALIZING("STEWARDSHIP-ENGINE-SERVICES-0001",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The Stewardship engine services are initializing in server {0}",
                        "A new OMAG server has been started that is configured to run as a stewardship server.  " +
                                 "Within the stewardship server are one or more stewardship engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "stewardship engines is retrieved from the metadata server and the stewardship engines are initialized.",
                        "Verify that the start up sequence goes on to initialize the configured stewardship engines."),

    NO_OMAS_SERVER_URL("STEWARDSHIP-ENGINE-SERVICES-0002",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Stewardship server {0} is not configured with the platform URL root for the Stewardship Engine OMAS",
                         "The server is not able to retrieve its configuration.  It fails to start.",
                         "Add the platform URL root of the open metadata server where the Stewardship Engine OMAS is running " +
                               "to this stewardship server's configuration document."),

    NO_OMAS_SERVER_NAME("STEWARDSHIP-ENGINE-SERVICES-0003",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Stewardship server {0} is not configured with the name for the server running the Stewardship Engine OMAS",
                      "The server is not able to retrieve its configuration.  It fails to start.",
                     "Add the server name of the open metadata server where the Stewardship Engine OMAS is running " +
                                "to this stewardship server's configuration document."),

    NO_STEWARDSHIP_ENGINES("STEWARDSHIP-ENGINE-SERVICES-0004",
                        OMRSAuditLogRecordSeverity.ERROR,
                       "Stewardship server {0} is configured with no stewardship engines",
                       "The server is not able to run any stewardship requests.  It fails to start.",
                       "Add the qualified name for at least one stewardship engine to this stewardship server's configuration document."),

    ENGINE_INITIALIZING("STEWARDSHIP-ENGINE-SERVICES-0005",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "A new stewardship engine instance {0} is initializing in stewardship server {1}",
                        "The stewardship server is initializing a stewardship engine to analyze the " +
                                "content of assets on demand and create annotation metadata.  The configuration for this stewardship engine is " +
                                "retrieved from the Stewardship Engine OMAS running in the metadata server",
                        "Verify that this stewardship engine successfully retrieves its configuration from the metadata server."),
    SERVER_NOT_AUTHORIZED("STEWARDSHIP-ENGINE-SERVICES-0007",
                     OMRSAuditLogRecordSeverity.EXCEPTION,
                     "Stewardship server {0} is not authorized to retrieve any its configuration from the Stewardship Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                     "The stewardship engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any stewardship requests.",
                     "Diagnose why the calls to Stewardship Engine OMAS are not working.  " +
                             "It could be because because this stewardship server is configured with the wrong userId, it is calling the wrong server, or the " +
                                  "remote server is correct but it needs updating to allow this userId.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the stewardship engine"),

    SUPPORTED_STEWARDSHIP_TYPE("STEWARDSHIP-ENGINE-SERVICES-0008",
                             OMRSAuditLogRecordSeverity.INFO,
                             "Stewardship engine {0} in stewardship server {1} is configured to process stewardship requests of type {2}",
                             "The stewardship engine has successfully retrieved the configuration to run analysis requests for the named stewardship " +
                                     "request type.  It is ready to run stewardship requests of this type",
                             "Verify that this is an appropriate stewardship request type for the stewardship engine."),

    NO_SUPPORTED_REQUEST_TYPES("STEWARDSHIP-ENGINE-SERVICES-0009",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Stewardship engine {0} in stewardship server {1} is not configured to support any type of stewardship requests",
                               "The stewardship engine has no configuration that links it to a stewardship request type and a corresponding stewardship " +
                                       "stewardship service.  It is not able to process any stewardship requests because it would not know what to run.",
                               "Add the configuration for at least one registered stewardship service (and corresponding stewardship request type to " +
                                       "this stewardship engine."),

    REQUEST_TYPE_CONFIG_ERROR("STEWARDSHIP-ENGINE-SERVICES-0010",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "Stewardship engine {0} in stewardship server {1} is not able to retrieve its configured stewardship request types from the " +
                                    "Stewardship Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                              "The stewardship engine has not been able to retrieve its configuration.  It is not able to process any stewardship " +
                                    "requests until this configuration is available.",
                              "Diagnose why the calls to Stewardship Engine OMAS are not working.  " +
                                    "The error message should help to narrow down the cause of the error.  " +
                                    "Once the problem has been resolved, restart the stewardship engine."),

    ENGINE_INITIALIZED("STEWARDSHIP-ENGINE-SERVICES-0011",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The stewardship engine {0} in stewardship server {1} has initialized",
                        "The stewardship engine has completed initialization and is ready to receive stewardship requests.",
                        "Verify that the stewardship engine has been initialized wit the correct list of stewardship request types."),

    SERVICE_INSTANCE_FAILURE("STEWARDSHIP-ENGINE-SERVICES-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The stewardship engine services are unable to initialize a new instance of stewardship server {0}; error message is {1}",
                             "The stewardship engine services detected an error during the start up of a specific stewardship server instance.  Its stewardship services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVER_INITIALIZED("STEWARDSHIP-ENGINE-SERVICES-0013",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The stewardship server {0} has initialized",
                       "The stewardship server has completed initialization.",
                       "Verify that all of the configured stewardship engines have successfully started and retrieved their configuration."),

    SERVER_SHUTTING_DOWN("STEWARDSHIP-ENGINE-SERVICES-0014",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The stewardship server {0} is shutting down",
                    "The local administrator has requested shut down of this stewardship server.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    ENGINE_SHUTDOWN("STEWARDSHIP-ENGINE-SERVICES-0015",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The stewardship engine {0} in stewardship server {1} is shutting down",
                    "The local administrator has requested shut down of this stewardship engine.  No more stewardship requests will be processed by this engine.",
                    "Verify that this shutdown is intended and the stewardship engine is no longer needed."),

    SERVER_SHUTDOWN("STEWARDSHIP-ENGINE-SERVICES-0016",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The stewardship server {0} has completed shutdown",
                         "The local administrator has requested shut down of this stewardship server and the operation has completed.",
                         "Verify that all configured stewardship engines shut down successfully."),

    STEWARDSHIP_SERVICE_STARTING("STEWARDSHIP-ENGINE-SERVICES-0017",
                    OMRSAuditLogRecordSeverity.STARTUP,
                    "The stewardship service {0} is starting to analyze asset {1} with stewardship request type {2} in stewardship engine {3} (guid={4});" +
                                       " the results will be stored in stewardship analysis report {5}",
                    "A new stewardship request is being processed.",
                    "Verify that the stewardship service ran to completion."),

    STEWARDSHIP_SERVICE_FAILED("STEWARDSHIP-ENGINE-SERVICES-0018",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    "The stewardship service {0} threw an exception of type {1} during the generation of stewardship analysis report {2} for asset {3} " +
                                     "during stewardship request type {4} in stewardship engine {5} (guid={6}). The error message was {7}",
                    "A stewardship service failed to complete the analysis of an asset.",
                    "Review the exception to determine the cause of the error."),

    STEWARDSHIP_SERVICE_COMPLETE("STEWARDSHIP-ENGINE-SERVICES-0019",
                               OMRSAuditLogRecordSeverity.SHUTDOWN,
                               "The stewardship service {0} has completed the analysis of asset {1} with stewardship request type {2} in {3} " +
                                       "milliseconds; the results are stored in stewardship analysis report {4}",
                               "A stewardship request has completed.",
                               "It is possible to query the result of the stewardship request through the stewardship server's stewardship engine " +
                                       "services interface."),

    NO_STEWARDSHIP_ENGINES_STARTED("STEWARDSHIP-ENGINE-SERVICES-0020",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Stewardship server {0} is unable to start any stewardship engines",
                         "The server is not able to run any stewardship requests.  It fails to start.",
                         "Add the configuration for at least one stewardship engine to this stewardship server."),

    EXC_ON_ERROR_STATUS_UPDATE("STEWARDSHIP-ENGINE-SERVICES-0021",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "Stewardship engine {0} is unable to update the status for stewardship service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a stewardship request. The stewardship report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the stewardship request."),

    UNKNOWN_STEWARDSHIP_ENGINE_NAME( "STEWARDSHIP-ENGINE-SERVICES-0022",
                                   OMRSAuditLogRecordSeverity.STARTUP,
                                  "Stewardship engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} " +
                                           "returned to stewardship server {4}",
                                  "The stewardship server is not able to initialize the stewardship engine and so it will not de able to support " +
                                           "stewardship requests targeted to this stewardship engine until this configuration is available.",
                                  "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the stewardship server.  Once the cause is resolved, restart the stewardship server."),

    NO_CONFIGURATION_LISTENER("STEWARDSHIP-ENGINE-SERVICES-0023",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The stewardship engine services are unable to retrieve the connection for the configuration " +
                                      "listener for stewardship server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The stewardship server continues to run.  The stewardship engine services will start up the " +
                                                    "stewardship engines and they will operate with whatever configuration that they can retrieve.  " +
                                                    "Periodically the stewardship engine services will" +
                                                    "retry the request to retrieve the connection information.  " +
                                                    "Without the connection, the stewardship server will not be notified of changes to the stewardship " +
                                                    "engines' configuration",
                              "This problem may be caused because the stewardship server has been configured with the wrong location for the " +
                                                    "metadata server, or the metadata server is not running the Stewardship Engine OMAS service or " +
                                                    "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                                    "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                                    "refresh-config command or wait for the stewardship server to retry the configuration request."),

    CONFIGURATION_LISTENER_REGISTERED("STEWARDSHIP-ENGINE-SERVICES-0024",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The stewardship engine services has registered the configuration " +
                                      "listener for stewardship server {0}.  It will receive configuration updates from metadata server {1}",
                              "The stewardship server continues to run.  The stewardship engine services will start up the " +
                                      "stewardship engines and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the stewardship engine services will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the stewardship server will not be notified of changes to the stewardship " +
                                      "engines' configuration",
                              "This problem may be caused because the stewardship server has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the Stewardship Engine OMAS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the stewardship server to retry the configuration request."),

    CLEARING_ALL_STEWARDSHIP_SERVICE_CONFIG("STEWARDSHIP-ENGINE-SERVICES-0025",
                                 OMRSAuditLogRecordSeverity.INFO,
                                 "All stewardship service configuration is being refreshed for stewardship engine {0}",
                                 "The stewardship server will call the Stewardship Engine OMAS in the metadata server to " +
                                                  "retrieve details of all of the stewardship services configured for this engine." +
                                                  "During this process, some stewardship request may fail if the associated stewardship" +
                                                  "service is only partially configured.",
                                 "Monitor the stewardship server to ensure all of the stewardship services are retrieved." +
                                                  "Then it is ready to process new stewardship requests."),

    FINISHED_ALL_STEWARDSHIP_SERVICE_CONFIG("STEWARDSHIP-ENGINE-SERVICES-0026",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Refreshing all stewardship service configuration has being completed for stewardship engine {0}",
                                          "The stewardship engine is ready to receive stewardship requests for all successfully loaded " +
                                                  "stewardship services.",
                                          "No action is required as long as all of the expected stewardship services are loaded." +
                                                  "If there are any stewardship services missing then validate the configuration of" +
                                                  "the stewardship engine in the metadata server."),

    STEWARDSHIP_SERVICE_NO_CONFIG("STEWARDSHIP-ENGINE-SERVICES-0027",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Failed to refresh configuration for stewardship service registered as " +
                                        "{0} for stewardship request types {1}.  The exception was {2} with error message {3}",
                                          "The stewardship engine is unable to process stewardship request types for the failed stewardship service.",
                                          "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the stewardship server to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the stewardship engine calls the Stewardship Engine OMAS to refresh the configuration for" +
                                        " the stewardship service."),

    STEWARDSHIP_ENGINE_NO_CONFIG("STEWARDSHIP-ENGINE-SERVICES-0028",
                                OMRSAuditLogRecordSeverity.ERROR,
                                "Failed to refresh configuration for stewardship engine {0}.  The exception was {1} with error message {2}",
                                "The stewardship engine is unable to process any stewardship requests until its configuration can be retrieved.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the stewardship server to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the stewardship engine calls the Stewardship Engine OMAS to refresh the configuration for" +
                                        " the stewardship service."),



    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for StewardshipEngineServicesAuditCode expects to be passed one of the enumeration rows defined in
     * StewardshipEngineServicesAuditCode above.   For example:
     *
     *     StewardshipEngineServicesAuditCode   auditCode = StewardshipEngineServicesAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    StewardshipEngineServicesAuditCode(String                     messageId,
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
        return "StewardshipEngineServicesAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
