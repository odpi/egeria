/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The ArchiveManagerErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Archive Engine Services.  It is used in conjunction with all exceptions,
 * both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum ArchiveManagerErrorCode implements ExceptionMessageSet
{
    NULL_ARCHIVE_CONTEXT(400, "OMES-ARCHIVE-MANAGER-400-001",
                         "No archive context supplied to the archive service {0}",
                         "The archive service has no access to open metadata, the request type and request parameters.",
                         "This may be a configuration or, more likely a code error in the archive engine.  Look for other error messages and review the code of the archive service.  Once the cause is resolved, retry the archive request."),

    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    ARCHIVE_ENGINE_INSTANCE_FAILURE(400, "OMES-ARCHIVE-MANAGER-400-007 ",
                         "The Archive Manager OMES are unable to initialize a new instance of archive engine {0}; error message is {1}",
                         "The Archive Manager OMES detected an error during the start up of a specific archive engine instance.  " +
                                              "Its archive services are not available in the Archive Manager OMES.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "OMES-ARCHIVE-MANAGER-400-008 ",
                             "The Archive Manager OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Archive Manager OMES detected an error during the start up of a specific server instance.  " +
                                     "No archive services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the archive engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "OMES-ARCHIVE-MANAGER-400-010 ",
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

    UNKNOWN_ARCHIVE_ENGINE_CONFIG_AT_STARTUP(400, "OMES-ARCHIVE-MANAGER-400-011 ",
             "Properties for archive engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to server {4}",
             "The Archive Manager OMES is not able to initialize the archive engine and so it will not de able to support archive " +
                                 "requests targeted to this archive engine.  ",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the Archive Manager OMES.  Once the cause is resolved, restart the server."),

    NO_ARCHIVE_ENGINES_STARTED(400,"OMES-ARCHIVE-MANAGER-400-012 ",
             "Archive Manager OMES in server {0} is unable to start any archive engines",
             "The server is not able to run any archive requests.  It fails to start.",
             "Add the configuration for at least one archive engine to this Archive Manager OMES."),

    NO_ARCHIVE_ENGINE_CLIENT(400,"OMES-ARCHIVE-MANAGER-400-013 ",
                                 "Archive Manager OMES in server {0} is unable to start a client to the Archive Engine OMAS for archive engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any archive requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_ARCHIVE_ENGINE_CONFIG(400, "OMES-ARCHIVE-MANAGER-400-014 ",
             "Properties for archive engine called {0} have not been returned by open metadata server {1} to Archive Manager OMES in server {2}",
             "The Archive Manager OMES is not able to initialize the archive engine and so it will not de able to support archive " +
                                            "requests targeted to this archive engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the Archive Manager OMES.  Once the cause is resolved, restart the server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_ARCHIVE_ENGINE(400, "OMES-ARCHIVE-MANAGER-400-020 ",
                             "Archive engine {0} is not running in the Archive Manager OMES in server {1}",
                             "The archive engine requested on a request is not known to the Archive Manager OMES.",
                             "This may be a configuration error in the Archive Manager OMES or an error in the caller.  " +
                                     "The supported archive engines are listed in the Archive Manager OMES's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "archive engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the Archive Manager OMES is running the correct archive engines then validate that " +
                                     "the caller has passed the correct name of the archive engine to the Archive Manager OMES.  If all of this is " +
                                     "correct then it may be a code error in the Archive Manager OMES and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the archive request."),

    UNKNOWN_ARCHIVE_REQUEST_TYPE(400, "OMES-ARCHIVE-MANAGER-400-021 ",
             "The archive request type {0} is not recognized by archive engine {1} hosted by server {2}",
             "The archive request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the archive engine or the metadata server" +
                                           "used by the engine host server may be down.  " +
                                           "The configuration that defines the archive request type in the archive engine and links " +
                                           "it to the archive service that should run is maintained in the metadata server by the Archive " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the archive " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the Archive Manager OMES in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the archive request."),

    INVALID_ARCHIVE_SERVICE(400, "OMES-ARCHIVE-MANAGER-400-022 ",
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

    NULL_ARCHIVE_SERVICE(400, "OMES-ARCHIVE-MANAGER-400-023 ",
                              "Method {0} can not execute in the archive engine {1} hosted by Archive Manager OMES in server {2} because the associated " +
                                      "archive service properties are null",
                              "The archive request is not run and an error is returned to the caller.",
                              "This may be an error in the archive engine's logic or the Archive Engine OMAS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    ARCHIVE_ENGINE_NOT_INITIALIZED(400,"OMES-ARCHIVE-MANAGER-400-024 ",
             "Archive Manager OMES in server {0} is unable to pass a archive request to archive engine {1} because this archive engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The archive engine is not able to run any archive requests until it is able to retrieve its configuration.",
                                     "Use the configuration interface of the Archive Engine OMAS to create a definition of at least one archive" +
                                             " engine."),

    UNEXPECTED_EXCEPTION(500, "OMES-ARCHIVE-MANAGER-500-001",
                         "Unexpected {0} exception in archive service {1} of type {2} detected by method {3}.  The error message was {4}",
                         "The archive service failed during its operation.",
                         "This may be a configuration or a code error.  Look for other error messages and review the code of the archive service. " +
                                 "Once the cause is resolved, retry the archive request."),

    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for ArchiveManagerErrorCode expects to be passed one of the enumeration rows defined in
     * ArchiveManagerErrorCode above.   For example:
     *
     *     ArchiveManagerErrorCode   errorCode = ArchiveManagerErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ArchiveManagerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }
}
