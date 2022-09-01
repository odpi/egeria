/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;



/**
 * The AssetAnalysisAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum AssetAnalysisAuditCode implements AuditLogMessageSet
{
    ENGINE_SERVICE_INITIALIZING("OMES-ASSET-ANALYSIS-0001",
                                OMRSAuditLogRecordSeverity.STARTUP,
                                "The Asset Analysis engine services are initializing in server {0}",
                                "A new OMAG server has been started that is configured to run the Asset Analysis OMES.  " +
                                 "Within this engine service are one or more discovery engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "discovery engines is retrieved from the metadata server and the discovery engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured discovery engines."),

    ENGINE_INITIALIZING("OMES-ASSET-ANALYSIS-0005",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "A new discovery engine instance {0} is initializing in server {1}",
                        "The Asset Analysis OMES is initializing a discovery engine to analyze the " +
                                "content of assets on demand and create annotation metadata.  The configuration for this discovery engine is " +
                                "retrieved from the Discovery Engine OMAS running in the metadata server",
                        "Verify that this discovery engine successfully retrieves its configuration from the metadata server."),

    SERVER_NOT_AUTHORIZED("OMES-ASSET-ANALYSIS-0007",
                     OMRSAuditLogRecordSeverity.EXCEPTION,
                     "Asset Analysis OMES in engine host server {0} is not authorized to retrieve any its configuration from the Discovery Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                     "The discovery engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any discovery requests.",
                     "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                             "It could be because because this server is configured with the wrong userId, it is calling the wrong server, or the " +
                                  "remote server is correct but it needs updating to allow this userId.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the engine host server."),

    SUPPORTED_DISCOVERY_TYPE("OMES-ASSET-ANALYSIS-0008",
                             OMRSAuditLogRecordSeverity.INFO,
                             "Discovery engine {0} in engine host server {1} is configured to process discovery requests of type {2}",
                             "The discovery engine has successfully retrieved the configuration to run analysis requests for the named discovery " +
                                     "request type.  It is ready to run discovery requests of this type",
                             "Verify that this is an appropriate discovery request type for the discovery engine."),

    NO_SUPPORTED_REQUEST_TYPES("OMES-ASSET-ANALYSIS-0009",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Discovery engine {0} in engine host server {1} is not configured to support any type of discovery requests",
                               "The discovery engine has no configuration that links it to a discovery request type and a corresponding discovery " +
                                       "discovery service.  It is not able to process any discovery requests because it would not know what to run.",
                               "Add the configuration for at least one registered discovery service (and corresponding discovery request type to " +
                                       "this discovery engine."),

    REQUEST_TYPE_CONFIG_ERROR("OMES-ASSET-ANALYSIS-0010",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "Discovery engine {0} in engine host server {1} is not able to retrieve its configured discovery request types from the " +
                                    "Discovery Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                              "The discovery engine has not been able to retrieve its configuration.  It is not able to process any discovery " +
                                    "requests until this configuration is available.",
                              "Diagnose why the calls to Discovery Engine OMAS are not working.  " +
                                    "The error message should help to narrow down the cause of the error.  " +
                                    "Once the problem has been resolved, restart the discovery engine."),

    ENGINE_INITIALIZED("OMES-ASSET-ANALYSIS-0011",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The discovery engine {0} in engine host server {1} has initialized",
                        "The discovery engine has completed initialization and is ready to receive discovery requests.",
                        "Verify that the discovery engine has been initialized wit the correct list of discovery request types."),

    SERVICE_INSTANCE_FAILURE("OMES-ASSET-ANALYSIS-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The Asset Analysis OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its discovery services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVER_INITIALIZED("OMES-ASSET-ANALYSIS-0013",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Asset Analysis OMES in server {0} has initialized",
                       "The engine service has completed initialization.",
                       "Verify that all the configured discovery engines have successfully started and retrieved their configuration."),

    SERVER_SHUTTING_DOWN("OMES-ASSET-ANALYSIS-0014",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The Asset Analysis OMES in server {0} is shutting down",
                    "The local administrator has requested shut down of this engine service.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

   SERVER_SHUTDOWN("OMES-ASSET-ANALYSIS-0016",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The Asset Analysis OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured discovery engines shut down successfully."),

    DISCOVERY_SERVICE_STARTING("OMES-ASSET-ANALYSIS-0017",
                    OMRSAuditLogRecordSeverity.STARTUP,
                    "The discovery service {0} is starting to analyze asset {1} with discovery request type {2} in discovery engine {3} (guid={4});" +
                                       " the results will be stored in discovery analysis report {5}",
                    "A new discovery request is being processed.",
                    "Verify that the discovery service ran to completion."),

    DISCOVERY_SERVICE_FAILED("OMES-ASSET-ANALYSIS-0018",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    "The discovery service {0} threw an exception of type {1} during the generation of discovery analysis report {2} for asset {3} " +
                                     "during discovery request type {4} in discovery engine {5} (guid={6}). The error message was {7}",
                    "A discovery service failed to complete the analysis of an asset.",
                    "Review the exception to determine the cause of the error."),

    DISCOVERY_SERVICE_COMPLETE("OMES-ASSET-ANALYSIS-0019",
                               OMRSAuditLogRecordSeverity.SHUTDOWN,
                               "The discovery service {0} has completed the analysis of asset {1} with discovery request type {2} in {3} " +
                                       "milliseconds; the results are stored in discovery analysis report {4}",
                               "A discovery request has completed.",
                               "It is possible to query the result of the discovery request through the engine service's REST API."),

    NO_DISCOVERY_ENGINES_STARTED("OMES-ASSET-ANALYSIS-0020",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Asset Analysis OMES in server {0} is unable to start any discovery engines",
                         "The engine service is not able to run any discovery requests.  It fails to start.",
                         "Add the configuration for at least one discovery engine to this engine service."),

    EXC_ON_ERROR_STATUS_UPDATE("OMES-ASSET-ANALYSIS-0021",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "Discovery engine {0} is unable to update the status for discovery service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a discovery request. The discovery report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_ENGINE_NAME( "OMES-ASSET-ANALYSIS-0022",
                                   OMRSAuditLogRecordSeverity.STARTUP,
                                  "Discovery engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} " +
                                           "returned to server {4}",
                                  "The Asset Analysis OMES in server is not able to initialize the discovery engine and so it will not de able to support " +
                                           "discovery requests targeted to this discovery engine until this configuration is available.",
                                  "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the server.  Once the cause is resolved, restart the server."),

    NO_CONFIGURATION_LISTENER("OMES-ASSET-ANALYSIS-0023",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The Asset Analysis OMES are unable to retrieve the connection for the configuration " +
                                      "listener for server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The server continues to run.  The Asset Analysis OMES will start up the " +
                                                    "discovery engines and they will operate with whatever configuration that they can retrieve.  " +
                                                    "Periodically the Asset Analysis OMES will" +
                                                    "retry the request to retrieve the connection information.  " +
                                                    "Without the connection, the Asset Analysis OMES will not be notified of changes to the discovery " +
                                                    "engines' configuration",
                              "This problem may be caused because the Asset Analysis OMES has been configured with the wrong location for the " +
                                                    "metadata server, or the metadata server is not running the Discovery Engine OMAS service or " +
                                                    "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                                    "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                                    "refresh-config command or wait for the Asset Analysis OMES to retry the configuration request."),

    CONFIGURATION_LISTENER_REGISTERED("OMES-ASSET-ANALYSIS-0024",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The Asset Analysis OMES has registered the configuration " +
                                      "listener for server {0}.  It will receive configuration updates from metadata server {1}",
                              "The Asset Analysis OMES continues to run.  The Asset Analysis OMES will start up the " +
                                      "discovery engines and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the Asset Analysis OMES will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the Asset Analysis OMES will not be notified of changes to the discovery " +
                                      "engines' configuration",
                              "This problem may be caused because the Asset Analysis OMES has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the Discovery Engine OMAS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the Asset Analysis OMES to retry the configuration request."),

    CLEARING_ALL_DISCOVERY_SERVICE_CONFIG("OMES-ASSET-ANALYSIS-0025",
                                 OMRSAuditLogRecordSeverity.INFO,
                                 "All discovery service configuration is being refreshed for discovery engine {0}",
                                 "The Asset Analysis OMES will call the Discovery Engine OMAS in the metadata server to " +
                                                  "retrieve details of all the discovery services configured for this engine." +
                                                  "During this process, some discovery request may fail if the associated discovery" +
                                                  "service is only partially configured.",
                                 "Monitor the Asset Analysis OMES to ensure all the discovery services are retrieved." +
                                                  "Then it is ready to process new discovery requests."),

    FINISHED_ALL_DISCOVERY_SERVICE_CONFIG("OMES-ASSET-ANALYSIS-0026",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Refreshing all discovery service configuration has being completed for discovery engine {0}",
                                          "The discovery engine is ready to receive discovery requests for all successfully loaded " +
                                                  "discovery services.",
                                          "No action is required as long as all the expected discovery services are loaded." +
                                                  "If there are any discovery services missing then validate the configuration of" +
                                                  "the discovery engine in the metadata server."),

    DISCOVERY_SERVICE_NO_CONFIG("OMES-ASSET-ANALYSIS-0027",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Failed to refresh configuration for discovery service registered as " +
                                        "{0} for discovery request types {1}.  The exception was {2} with error message {3}",
                                          "The discovery engine is unable to process discovery request types for the failed discovery service.",
                                          "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the Asset Analysis OMES to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the discovery engine calls the Discovery Engine OMAS to refresh the configuration for" +
                                        " the discovery service."),

    DISCOVERY_ENGINE_NO_CONFIG("OMES-ASSET-ANALYSIS-0028",
                                OMRSAuditLogRecordSeverity.ERROR,
                                "Failed to refresh configuration for discovery engine {0}.  The exception was {1} with error message {2}",
                                "The discovery engine is unable to process any discovery requests until its configuration can be retrieved.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the Asset Analysis OMES to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the discovery engine calls the Discovery Engine OMAS to refresh the configuration for" +
                                        " the discovery service."),


    INVALID_DISCOVERY_SERVICE( "OMES-ASSET-ANALYSIS-0029",
                               OMRSAuditLogRecordSeverity.EXCEPTION,
                               "The discovery service {0} linked to discovery request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                              "The discovery request is not run and an error is returned to the caller.",
                              "This may be an error in the discovery services's logic or the discovery service may not be properly deployed or " +
                                      "there is a configuration error related to the discovery engine.  " +
                                      "The configuration that defines the discovery request type in the discovery engine and links " +
                                      "it to the discovery service is maintained in the metadata server by the Discovery " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "discovery service's implementation has been deployed so the Asset Analysis OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the discovery service in which case, " +
                                      "raise an issue with the author of the discovery service to get it fixed.  Once the cause is resolved, " +
                                      "retry the discovery request."),
    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for AssetAnalysisAuditCode expects to be passed one of the enumeration rows defined in
     * AssetAnalysisAuditCode above.   For example:
     *
     *     AssetAnalysisAuditCode   auditCode = AssetAnalysisAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    AssetAnalysisAuditCode(String                     messageId,
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
        return "AssetAnalysisAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
