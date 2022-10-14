/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;



/**
 * The RepositoryGovernanceAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum RepositoryGovernanceAuditCode implements AuditLogMessageSet
{
    ENGINE_SERVICE_INITIALIZING("OMES-REPOSITORY-GOVERNANCE-0001",
                                OMRSAuditLogRecordSeverity.STARTUP,
                                "The Repository Governance engine services are initializing in server {0}",
                                "A new OMAG server has been started that is configured to run the Repository Governance OMES.  " +
                                 "Within this engine service are one or more repository governance engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "repository governance engines is retrieved from the metadata server and the repository governance engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured repository governance engines."),

    ENGINE_INITIALIZING("OMES-REPOSITORY-GOVERNANCE-0005",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "A new repository governance engine instance {0} is initializing in server {1}",
                        "The Repository Governance OMES is initializing a repository governance engine to analyze the " +
                                "content of assets on demand and create annotation metadata.  The configuration for this repository governance engine is " +
                                "retrieved from the OMRS running in the metadata server",
                        "Verify that this repository governance engine successfully retrieves its configuration from the metadata server."),

    SERVER_NOT_AUTHORIZED("OMES-REPOSITORY-GOVERNANCE-0007",
                     OMRSAuditLogRecordSeverity.EXCEPTION,
                     "Repository Governance OMES in engine host server {0} is not authorized to retrieve any its configuration from the RepositoryGovernance Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                     "The repository governance engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any repository governance requests.",
                     "Diagnose why the calls to OMRS are not working.  " +
                             "It could be because because this server is configured with the wrong userId, it is calling the wrong server, or the " +
                                  "remote server is correct but it needs updating to allow this userId.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the engine host server."),

    SUPPORTED_REPOSITORY_GOVERNANCE_TYPE("OMES-REPOSITORY-GOVERNANCE-0008",
                             OMRSAuditLogRecordSeverity.INFO,
                             "RepositoryGovernance engine {0} in engine host server {1} is configured to process repository governance requests of type {2}",
                             "The repository governance engine has successfully retrieved the configuration to run analysis requests for the named repository governance " +
                                     "request type.  It is ready to run repository governance requests of this type",
                             "Verify that this is an appropriate repository governance request type for the repository governance engine."),

    NO_SUPPORTED_REQUEST_TYPES("OMES-REPOSITORY-GOVERNANCE-0009",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "RepositoryGovernance engine {0} in engine host server {1} is not configured to support any type of repository governance requests",
                               "The repository governance engine has no configuration that links it to a repository governance request type and a corresponding repository governance " +
                                       "repository governance service.  It is not able to process any repository governance requests because it would not know what to run.",
                               "Add the configuration for at least one registered repository governance service (and corresponding repository governance request type to " +
                                       "this repository governance engine."),

    REQUEST_TYPE_CONFIG_ERROR("OMES-REPOSITORY-GOVERNANCE-0010",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "RepositoryGovernance engine {0} in engine host server {1} is not able to retrieve its configured repository governance request types from the " +
                                    "OMRS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                              "The repository governance engine has not been able to retrieve its configuration.  It is not able to process any repository governance " +
                                    "requests until this configuration is available.",
                              "Diagnose why the calls to OMRS are not working.  " +
                                    "The error message should help to narrow down the cause of the error.  " +
                                    "Once the problem has been resolved, restart the repository governance engine."),

    ENGINE_INITIALIZED("OMES-REPOSITORY-GOVERNANCE-0011",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The repository governance engine {0} in engine host server {1} has initialized",
                        "The repository governance engine has completed initialization and is ready to receive repository governance requests.",
                        "Verify that the repository governance engine has been initialized wit the correct list of repository governance request types."),

    SERVICE_INSTANCE_FAILURE("OMES-REPOSITORY-GOVERNANCE-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The Repository Governance OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its repository governance services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVER_INITIALIZED("OMES-REPOSITORY-GOVERNANCE-0013",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Repository Governance OMES in server {0} has initialized",
                       "The engine service has completed initialization.",
                       "Verify that all the configured repository governance engines have successfully started and retrieved their configuration."),

    SERVER_SHUTTING_DOWN("OMES-REPOSITORY-GOVERNANCE-0014",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The Repository Governance OMES in server {0} is shutting down",
                    "The local administrator has requested shut down of this engine service.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

    SERVER_SHUTDOWN("OMES-REPOSITORY-GOVERNANCE-0015",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The Repository Governance OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured repository governance engines shut down successfully."),

    REPOSITORY_GOVERNANCE_SERVICE_STARTING("OMES-REPOSITORY-GOVERNANCE-0016",
                                           OMRSAuditLogRecordSeverity.STARTUP,
                                           "The repository governance service {0} is starting with repository governance request type {1} in repository governance engine {2} (guid={3})",
                                           "A new repository governance request is being processed.",
                                           "Verify that the repository governance service ran to completion."),

   REPOSITORY_GOVERNANCE_SERVICE_RETURNED("OMES-REPOSITORY-GOVERNANCE-0017",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "The repository governance service {0} for request type {1} is continuing to run in a background thread",
                                          "An repository governance service has returned from the start() method and without setting up the completion status prior to returning.  ",
                                          "Validate that this governance action service should still be running.  Typically you would expect an repository governance service to" +
                                               "still be running at this stage because it will have registered a listener."),


    REPOSITORY_GOVERNANCE_SERVICE_FAILED("OMES-REPOSITORY-GOVERNANCE-0018",
                                         OMRSAuditLogRecordSeverity.EXCEPTION,
                                         "The repository governance service {0} threw an exception of type {1} during repository governance request type {2} in repository governance engine {3} (guid={4}). The error message was {5}",
                                         "A repository governance service failed to complete the maintenance of an repository governance.",
                                         "Review the exception to determine the cause of the error."),

    REPOSITORY_GOVERNANCE_SERVICE_COMPLETE("OMES-REPOSITORY-GOVERNANCE-0019",
                                           OMRSAuditLogRecordSeverity.SHUTDOWN,
                                           "The repository governance service {0} has completed repository governance request type {1} in {2} milliseconds",
                                           "A repository governance request has completed.",
                                           "It is possible to query the result of the repository governance request through the Governance Engine OMAS's REST API."),

    NO_REPOSITORY_GOVERNANCE_ENGINES_STARTED("OMES-REPOSITORY-GOVERNANCE-0020",
                                             OMRSAuditLogRecordSeverity.ERROR,
                                             "Repository Governance OMES in server {0} is unable to start any repository governance engines",
                                             "The engine service is not able to run any repository governance requests.  It fails to start.",
                                             "Add the configuration for at least one repository governance engine to this engine service."),

    EXC_ON_ERROR_STATUS_UPDATE("OMES-REPOSITORY-GOVERNANCE-0021",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "RepositoryGovernance engine {0} is unable to update the status for repository governance service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a repository governance request. The repository governance report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the repository governance request."),

    UNKNOWN_REPOSITORY_GOVERNANCE_ENGINE_NAME("OMES-REPOSITORY-GOVERNANCE-0022",
                                              OMRSAuditLogRecordSeverity.STARTUP,
                                              "Repository governance engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} " +
                                           "returned to server {4}",
                                              "The Repository Governance OMES in server is not able to initialize the repository governance engine and so it will not de able to support " +
                                           "repository governance requests targeted to this repository governance engine until this configuration is available.",
                                              "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the server.  Once the cause is resolved, restart the server."),

    NO_CONFIGURATION_LISTENER("OMES-REPOSITORY-GOVERNANCE-0023",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The Repository Governance OMES are unable to retrieve the connection for the configuration " +
                                      "listener for server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The server continues to run.  The Repository Governance OMES will start up the " +
                                                    "repository governance engines and they will operate with whatever configuration that they can retrieve.  " +
                                                    "Periodically the Repository Governance OMES will" +
                                                    "retry the request to retrieve the connection information.  " +
                                                    "Without the connection, the Repository Governance OMES will not be notified of changes to the repository governance " +
                                                    "engines' configuration",
                              "This problem may be caused because the Repository Governance OMES has been configured with the wrong location for the " +
                                                    "metadata server, or the metadata server is not running the OMRS service or " +
                                                    "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                                    "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                                    "refresh-config command or wait for the Repository Governance OMES to retry the configuration request."),

    CONFIGURATION_LISTENER_REGISTERED("OMES-REPOSITORY-GOVERNANCE-0024",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The Repository Governance OMES has registered the configuration " +
                                      "listener for server {0}.  It will receive configuration updates from metadata server {1}",
                              "The Repository Governance OMES continues to run.  The Repository Governance OMES will start up the " +
                                      "repository governance engines and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the Repository Governance OMES will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the Repository Governance OMES will not be notified of changes to the repository governance " +
                                      "engines' configuration",
                              "This problem may be caused because the Repository Governance OMES has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the OMRS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the Repository Governance OMES to retry the configuration request."),

    CLEARING_ALL_REPOSITORY_GOVERNANCE_SERVICE_CONFIG("OMES-REPOSITORY-GOVERNANCE-0025",
                                                      OMRSAuditLogRecordSeverity.INFO,
                                                      "All repository governance service configuration is being refreshed for repository governance engine {0}",
                                                      "The Repository Governance OMES will call the OMRS in the metadata server to " +
                                                  "retrieve details of all the repository governance services configured for this engine." +
                                                  "During this process, some repository governance request may fail if the associated repository governance" +
                                                  "service is only partially configured.",
                                                      "Monitor the Repository Governance OMES to ensure all the repository governance services are retrieved." +
                                                  "Then it is ready to process new repository governance requests."),

    FINISHED_ALL_REPOSITORY_GOVERNANCE_SERVICE_CONFIG("OMES-REPOSITORY-GOVERNANCE-0026",
                                                      OMRSAuditLogRecordSeverity.INFO,
                                                      "Refreshing all repository governance service configuration has being completed for repository governance engine {0}",
                                                      "The repository governance engine is ready to receive repository governance requests for all successfully loaded " +
                                                  "repository governance services.",
                                                      "No action is required as long as all the expected repository governance services are loaded." +
                                                  "If there are any repository governance services missing then validate the configuration of" +
                                                  "the repository governance engine in the metadata server."),

    REPOSITORY_GOVERNANCE_SERVICE_NO_CONFIG("OMES-REPOSITORY-GOVERNANCE-0027",
                                            OMRSAuditLogRecordSeverity.INFO,
                                            "Failed to refresh configuration for repository governance service registered as " +
                                        "{0} for repository governance request types {1}.  The exception was {2} with error message {3}",
                                            "The repository governance engine is unable to process repository governance request types for the failed repository governance service.",
                                            "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the Repository Governance OMES to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the repository governance engine calls the OMRS to refresh the configuration for" +
                                        " the repository governance service."),

    REPOSITORY_GOVERNANCE_ENGINE_NO_CONFIG("OMES-REPOSITORY-GOVERNANCE-0028",
                                           OMRSAuditLogRecordSeverity.ERROR,
                                           "Failed to refresh configuration for repository governance engine {0}.  The exception was {1} with error message {2}",
                                           "The repository governance engine is unable to process any repository governance requests until its configuration can be retrieved.",
                                           "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the Repository Governance OMES to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the repository governance engine calls the OMRS to refresh the configuration for" +
                                        " the repository governance service."),


    INVALID_REPOSITORY_GOVERNANCE_SERVICE("OMES-REPOSITORY-GOVERNANCE-0029",
                                          OMRSAuditLogRecordSeverity.EXCEPTION,
                                          "The repository governance service {0} linked to repository governance request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                                          "The repository governance request is not run and an error is returned to the caller.",
                                          "This may be an error in the repository governance services's logic or the repository governance service may not be properly deployed or " +
                                      "there is a configuration error related to the repository governance engine.  " +
                                      "The configuration that defines the repository governance request type in the repository governance engine and links " +
                                      "it to the repository governance service is maintained in the metadata server by the RepositoryGovernance " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "repository governance service's implementation has been deployed so the Repository Governance OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the repository governance service in which case, " +
                                      "raise an issue with the author of the repository governance service to get it fixed.  Once the cause is resolved, " +
                                      "retry the repository governance request."),
    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for RepositoryGovernanceAuditCode expects to be passed one of the enumeration rows defined in
     * RepositoryGovernanceAuditCode above.   For example:
     *
     *     RepositoryGovernanceAuditCode   auditCode = RepositoryGovernanceAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    RepositoryGovernanceAuditCode(String                     messageId,
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
        return "RepositoryGovernanceAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
