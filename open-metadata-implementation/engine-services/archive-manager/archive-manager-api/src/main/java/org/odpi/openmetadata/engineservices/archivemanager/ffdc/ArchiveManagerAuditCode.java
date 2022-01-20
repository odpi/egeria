/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;



/**
 * The ArchiveManagerAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum ArchiveManagerAuditCode implements AuditLogMessageSet
{
    ENGINE_SERVICE_INITIALIZING("OMES-ARCHIVE-MANAGER-0001",
                                OMRSAuditLogRecordSeverity.STARTUP,
                                "The Archive Manager engine services are initializing in server {0}",
                                "A new OMAG server has been started that is configured to run the Archive Manager OMES.  " +
                                 "Within this engine service are one or more archive engines that analyze the " +
                                 "content of assets on demand and create annotation metadata. The configuration for the " +
                                "archive engines is retrieved from the metadata server and the archive engines are initialized.",
                                "Verify that the start up sequence goes on to initialize the configured archive engines."),

    ENGINE_INITIALIZING("OMES-ARCHIVE-MANAGER-0005",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "A new archive engine instance {0} is initializing in server {1}",
                        "The Archive Manager OMES is initializing a archive engine to analyze the " +
                                "content of assets on demand and create annotation metadata.  The configuration for this archive engine is " +
                                "retrieved from the Archive Engine OMAS running in the metadata server",
                        "Verify that this archive engine successfully retrieves its configuration from the metadata server."),

    SERVER_NOT_AUTHORIZED("OMES-ARCHIVE-MANAGER-0007",
                     OMRSAuditLogRecordSeverity.EXCEPTION,
                     "Archive Manager OMES in engine host server {0} is not authorized to retrieve any its configuration from the Archive Engine " +
                                  "OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}",
                     "The archive engine is unable to retrieved its configuration.  " +
                             "It has failed to start and will not be able to process any archive requests.",
                     "Diagnose why the calls to Archive Engine OMAS are not working.  " +
                             "It could be because because this server is configured with the wrong userId, it is calling the wrong server, or the " +
                                  "remote server is correct but it needs updating to allow this userId.  The error message should help to narrow down the cause of the error.  " +
                             "Once the problem has been resolved, restart the engine host server."),

    SUPPORTED_ARCHIVE_TYPE("OMES-ARCHIVE-MANAGER-0008",
                             OMRSAuditLogRecordSeverity.INFO,
                             "Archive engine {0} in engine host server {1} is configured to process archive requests of type {2}",
                             "The archive engine has successfully retrieved the configuration to run analysis requests for the named archive " +
                                     "request type.  It is ready to run archive requests of this type",
                             "Verify that this is an appropriate archive request type for the archive engine."),

    NO_SUPPORTED_REQUEST_TYPES("OMES-ARCHIVE-MANAGER-0009",
                               OMRSAuditLogRecordSeverity.ERROR,
                               "Archive engine {0} in engine host server {1} is not configured to support any type of archive requests",
                               "The archive engine has no configuration that links it to a archive request type and a corresponding archive " +
                                       "archive service.  It is not able to process any archive requests because it would not know what to run.",
                               "Add the configuration for at least one registered archive service (and corresponding archive request type to " +
                                       "this archive engine."),

    REQUEST_TYPE_CONFIG_ERROR("OMES-ARCHIVE-MANAGER-0010",
                              OMRSAuditLogRecordSeverity.EXCEPTION,
                              "Archive engine {0} in engine host server {1} is not able to retrieve its configured archive request types from the " +
                                    "Archive Engine OMAS running in server {2} on OMAG Server Platform {3}.  The error message was {4}",
                              "The archive engine has not been able to retrieve its configuration.  It is not able to process any archive " +
                                    "requests until this configuration is available.",
                              "Diagnose why the calls to Archive Engine OMAS are not working.  " +
                                    "The error message should help to narrow down the cause of the error.  " +
                                    "Once the problem has been resolved, restart the archive engine."),

    ENGINE_INITIALIZED("OMES-ARCHIVE-MANAGER-0011",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The archive engine {0} in engine host server {1} has initialized",
                        "The archive engine has completed initialization and is ready to receive archive requests.",
                        "Verify that the archive engine has been initialized wit the correct list of archive request types."),

    SERVICE_INSTANCE_FAILURE("OMES-ARCHIVE-MANAGER-0012",
                             OMRSAuditLogRecordSeverity.ERROR,
                             "The Archive Manager OMES is unable to initialize a new instance of itself in server {0}; error message is {1}",
                             "The engine services detected an error during the start up of a specific engine host server instance.  Its archive services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVER_INITIALIZED("OMES-ARCHIVE-MANAGER-0013",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Archive Manager OMES in server {0} has initialized",
                       "The engine service has completed initialization.",
                       "Verify that all of the configured archive engines have successfully started and retrieved their configuration."),

    SERVER_SHUTTING_DOWN("OMES-ARCHIVE-MANAGER-0014",
                    OMRSAuditLogRecordSeverity.SHUTDOWN,
                    "The Archive Manager OMES in server {0} is shutting down",
                    "The local administrator has requested shut down of this engine service.",
                    "Verify that this server is no longer needed and the shutdown is expected."),

   SERVER_SHUTDOWN("OMES-ARCHIVE-MANAGER-0015",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The Archive Manager OMES in server {0} has completed shutdown",
                         "The local administrator has requested shut down of this engine service and the operation has completed.",
                         "Verify that all configured archive engines shut down successfully."),

    ARCHIVE_SERVICE_STARTING("OMES-ARCHIVE-MANAGER-0016",
                    OMRSAuditLogRecordSeverity.STARTUP,
                    "The archive service {0} is starting with archive request type {1} in archive engine {2} (guid={3})",
                    "A new archive request is being processed.",
                    "Verify that the archive service ran to completion."),

   ARCHIVE_SERVICE_RETURNED("OMES-ARCHIVE-MANAGER-0017",
                                       OMRSAuditLogRecordSeverity.INFO,
                                       "The archive service {0} for request type {1} is continuing to run in a background thread",
                                       "An archive service has returned from the start() method and without setting up the completion status prior to returning.  ",
                                       "Validate that this governance action service should still be running.  Typically you would expect an archive service to" +
                                               "still be running at this stage because it will have registered a listener."),


    ARCHIVE_SERVICE_FAILED("OMES-ARCHIVE-MANAGER-0018",
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    "The archive service {0} threw an exception of type {1} during archive request type {2} in archive engine {3} (guid={4}). The error message was {5}",
                    "A archive service failed to complete the maintenance of an archive.",
                    "Review the exception to determine the cause of the error."),

    ARCHIVE_SERVICE_COMPLETE("OMES-ARCHIVE-MANAGER-0019",
                               OMRSAuditLogRecordSeverity.SHUTDOWN,
                               "The archive service {0} has completed archive request type {1} in {2} milliseconds",
                               "A archive request has completed.",
                               "It is possible to query the result of the archive request through the Governance Engine OMAS's REST API."),

    NO_ARCHIVE_ENGINES_STARTED("OMES-ARCHIVE-MANAGER-0020",
                         OMRSAuditLogRecordSeverity.ERROR,
                         "Archive Manager OMES in server {0} is unable to start any archive engines",
                         "The engine service is not able to run any archive requests.  It fails to start.",
                         "Add the configuration for at least one archive engine to this engine service."),

    EXC_ON_ERROR_STATUS_UPDATE("OMES-ARCHIVE-MANAGER-0021",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "Archive engine {0} is unable to update the status for archive service {1}.  The exception was {2} with error " +
                                       "message {3}",
                                 "The server is not able to record the failed result for a archive request. The archive report status is not updated.",
                                 "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the archive request."),

    UNKNOWN_ARCHIVE_ENGINE_NAME( "OMES-ARCHIVE-MANAGER-0022",
                                   OMRSAuditLogRecordSeverity.STARTUP,
                                  "Archive engine called {0} is not known by metadata server {1}.  Exception {2} with message {3} " +
                                           "returned to server {4}",
                                  "The Archive Manager OMES in server is not able to initialize the archive engine and so it will not de able to support " +
                                           "archive requests targeted to this archive engine until this configuration is available.",
                                  "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the server.  Once the cause is resolved, restart the server."),

    NO_CONFIGURATION_LISTENER("OMES-ARCHIVE-MANAGER-0023",
                              OMRSAuditLogRecordSeverity.ERROR,
                              "The Archive Manager OMES are unable to retrieve the connection for the configuration " +
                                      "listener for server {0} from metadata server {1}. " +
                                      "Exception returned was {2} with error message {3}",
                              "The server continues to run.  The Archive Manager OMES will start up the " +
                                                    "archive engines and they will operate with whatever configuration that they can retrieve.  " +
                                                    "Periodically the Archive Manager OMES will" +
                                                    "retry the request to retrieve the connection information.  " +
                                                    "Without the connection, the Archive Manager OMES will not be notified of changes to the archive " +
                                                    "engines' configuration",
                              "This problem may be caused because the Archive Manager OMES has been configured with the wrong location for the " +
                                                    "metadata server, or the metadata server is not running the Archive Engine OMAS service or " +
                                                    "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                                    "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                                    "refresh-config command or wait for the Archive Manager OMES to retry the configuration request."),

    CONFIGURATION_LISTENER_REGISTERED("OMES-ARCHIVE-MANAGER-0024",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The Archive Manager OMES has registered the configuration " +
                                      "listener for server {0}.  It will receive configuration updates from metadata server {1}",
                              "The Archive Manager OMES continues to run.  The Archive Manager OMES will start up the " +
                                      "archive engines and they will operate with whatever configuration that they can retrieve.  " +
                                      "Periodically the Archive Manager OMES will" +
                                      "retry the request to retrieve the connection information.  " +
                                      "Without the connection, the Archive Manager OMES will not be notified of changes to the archive " +
                                      "engines' configuration",
                              "This problem may be caused because the Archive Manager OMES has been configured with the wrong location for the " +
                                      "metadata server, or the metadata server is not running the Archive Engine OMAS service or " +
                                      "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                      "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                      "refresh-config command or wait for the Archive Manager OMES to retry the configuration request."),

    CLEARING_ALL_ARCHIVE_SERVICE_CONFIG("OMES-ARCHIVE-MANAGER-0025",
                                 OMRSAuditLogRecordSeverity.INFO,
                                 "All archive service configuration is being refreshed for archive engine {0}",
                                 "The Archive Manager OMES will call the Archive Engine OMAS in the metadata server to " +
                                                  "retrieve details of all of the archive services configured for this engine." +
                                                  "During this process, some archive request may fail if the associated archive" +
                                                  "service is only partially configured.",
                                 "Monitor the Archive Manager OMES to ensure all of the archive services are retrieved." +
                                                  "Then it is ready to process new archive requests."),

    FINISHED_ALL_ARCHIVE_SERVICE_CONFIG("OMES-ARCHIVE-MANAGER-0026",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Refreshing all archive service configuration has being completed for archive engine {0}",
                                          "The archive engine is ready to receive archive requests for all successfully loaded " +
                                                  "archive services.",
                                          "No action is required as long as all of the expected archive services are loaded." +
                                                  "If there are any archive services missing then validate the configuration of" +
                                                  "the archive engine in the metadata server."),

    ARCHIVE_SERVICE_NO_CONFIG("OMES-ARCHIVE-MANAGER-0027",
                                          OMRSAuditLogRecordSeverity.INFO,
                                          "Failed to refresh configuration for archive service registered as " +
                                        "{0} for archive request types {1}.  The exception was {2} with error message {3}",
                                          "The archive engine is unable to process archive request types for the failed archive service.",
                                          "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the Archive Manager OMES to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the archive engine calls the Archive Engine OMAS to refresh the configuration for" +
                                        " the archive service."),

    ARCHIVE_ENGINE_NO_CONFIG("OMES-ARCHIVE-MANAGER-0028",
                                OMRSAuditLogRecordSeverity.ERROR,
                                "Failed to refresh configuration for archive engine {0}.  The exception was {1} with error message {2}",
                                "The archive engine is unable to process any archive requests until its configuration can be retrieved.",
                                "Review the error messages and resolve the cause of the problem.  " +
                                        "Either wait for the Archive Manager OMES to refresh the configuration, or issue the refreshConfig" +
                                        "call to request that the archive engine calls the Archive Engine OMAS to refresh the configuration for" +
                                        " the archive service."),


    INVALID_ARCHIVE_SERVICE( "OMES-ARCHIVE-MANAGER-0029",
                               OMRSAuditLogRecordSeverity.EXCEPTION,
                               "The archive service {0} linked to archive request type {1} can not be started.  " +
                                      "The {2} exception was returned with message {3}",
                              "The archive request is not run and an error is returned to the caller.",
                              "This may be an error in the archive services's logic or the archive service may not be properly deployed or " +
                                      "there is a configuration error related to the archive engine.  " +
                                      "The configuration that defines the archive request type in the archive engine and links " +
                                      "it to the archive service is maintained in the metadata server by the Archive " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "archive service's implementation has been deployed so the Archive Manager OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the archive service in which case, " +
                                      "raise an issue with the author of the archive service to get it fixed.  Once the cause is resolved, " +
                                      "retry the archive request."),
    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for ArchiveManagerAuditCode expects to be passed one of the enumeration rows defined in
     * ArchiveManagerAuditCode above.   For example:
     *
     *     ArchiveManagerAuditCode   auditCode = ArchiveManagerAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    ArchiveManagerAuditCode(String                     messageId,
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
        return "ArchiveManagerAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
