/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The RepositoryGovernanceErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the RepositoryGovernance Engine Services.  It is used in conjunction with all exceptions,
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
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum RepositoryGovernanceErrorCode implements ExceptionMessageSet
{
    NULL_REPOSITORY_GOVERNANCE_CONTEXT(400, "OMES-REPOSITORY-GOVERNANCE-400-001",
                         "No repository governance context supplied to the repository governance service {0}",
                         "The repository governance service has no access to open metadata, the request type and request parameters.",
                         "This may be a configuration or, more likely a code error in the repository governance engine.  Look for other error messages and review the code of the repository governance service.  Once the cause is resolved, retry the repository governance request."),

    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    REPOSITORY_GOVERNANCE_ENGINE_INSTANCE_FAILURE(400, "OMES-REPOSITORY-GOVERNANCE-400-007 ",
                         "The Repository Governance OMES are unable to initialize a new instance of repository governance engine {0}; error message is {1}",
                         "The Repository Governance OMES detected an error during the start up of a specific repository governance engine instance.  " +
                                              "Its repository governance services are not available in the Repository Governance OMES.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "OMES-REPOSITORY-GOVERNANCE-400-008 ",
                             "The Repository Governance OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Repository Governance OMES detected an error during the start up of a specific server instance.  " +
                                     "No repository governance services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the repository governance engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "OMES-REPOSITORY-GOVERNANCE-400-010 ",
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

    UNKNOWN_REPOSITORY_GOVERNANCE_ENGINE_CONFIG_AT_STARTUP(400, "OMES-REPOSITORY-GOVERNANCE-400-011 ",
             "Properties for repository governance engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to server {4}",
             "The Repository Governance OMES is not able to initialize the repository governance engine and so it will not de able to support repository governance " +
                                 "requests targeted to this repository governance engine.  ",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the Repository Governance OMES.  Once the cause is resolved, restart the server."),

    NO_REPOSITORY_GOVERNANCE_ENGINES_STARTED(400,"OMES-REPOSITORY-GOVERNANCE-400-012 ",
             "Repository Governance OMES in server {0} is unable to start any repository governance engines",
             "The server is not able to run any repository governance requests.  It fails to start.",
             "Add the configuration for at least one repository governance engine to this Repository Governance OMES."),

    NO_REPOSITORY_GOVERNANCE_ENGINE_CLIENT(400,"OMES-REPOSITORY-GOVERNANCE-400-013 ",
                                 "Repository Governance OMES in server {0} is unable to start a client to the OMRS for repository governance engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any repository governance requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_REPOSITORY_GOVERNANCE_ENGINE_CONFIG(400, "OMES-REPOSITORY-GOVERNANCE-400-014 ",
             "Properties for repository governance engine called {0} have not been returned by open metadata server {1} to Repository Governance OMES in server {2}",
             "The Repository Governance OMES is not able to initialize the repository governance engine and so it will not de able to support repository governance " +
                                            "requests targeted to this repository governance engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the Repository Governance OMES.  Once the cause is resolved, restart the server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_REPOSITORY_GOVERNANCE_ENGINE(400, "OMES-REPOSITORY-GOVERNANCE-400-020 ",
                             "RepositoryGovernance engine {0} is not running in the Repository Governance OMES in server {1}",
                             "The repository governance engine requested on a request is not known to the Repository Governance OMES.",
                             "This may be a configuration error in the Repository Governance OMES or an error in the caller.  " +
                                     "The supported repository governance engines are listed in the Repository Governance OMES's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "repository governance engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the Repository Governance OMES is running the correct repository governance engines then validate that " +
                                     "the caller has passed the correct name of the repository governance engine to the Repository Governance OMES.  If all of this is " +
                                     "correct then it may be a code error in the Repository Governance OMES and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the repository governance request."),

    UNKNOWN_REPOSITORY_GOVERNANCE_REQUEST_TYPE(400, "OMES-REPOSITORY-GOVERNANCE-400-021 ",
             "The repository governance request type {0} is not recognized by repository governance engine {1} hosted by server {2}",
             "The repository governance request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the repository governance engine or the metadata server" +
                                           "used by the engine host server may be down.  " +
                                           "The configuration that defines the repository governance request type in the repository governance engine and links " +
                                           "it to the repository governance service that should run is maintained in the metadata server by the RepositoryGovernance " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the repository governance " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the Repository Governance OMES in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the repository governance request."),

    INVALID_REPOSITORY_GOVERNANCE_SERVICE(400, "OMES-REPOSITORY-GOVERNANCE-400-022 ",
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

    NULL_REPOSITORY_GOVERNANCE_SERVICE(400, "OMES-REPOSITORY-GOVERNANCE-400-023 ",
                              "Method {0} can not execute in the repository governance engine {1} hosted by Repository Governance OMES in server {2} because the associated " +
                                      "repository governance service properties are null",
                              "The repository governance request is not run and an error is returned to the caller.",
                              "This may be an error in the repository governance engine's logic or the OMRS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    REPOSITORY_GOVERNANCE_ENGINE_NOT_INITIALIZED(400,"OMES-REPOSITORY-GOVERNANCE-400-024 ",
             "Repository Governance OMES in server {0} is unable to pass a repository governance request to repository governance engine {1} because this repository governance engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The repository governance engine is not able to run any repository governance requests until it is able to retrieve its configuration.",
                                     "Use the configuration interface of the OMRS to create a definition of at least one repository governance" +
                                             " engine."),

    UNEXPECTED_EXCEPTION(500, "OMES-REPOSITORY-GOVERNANCE-500-001",
                         "Unexpected {0} exception in repository governance service {1} of type {2} detected by method {3}.  The error message was {4}",
                         "The repository governance service failed during its operation.",
                         "This may be a configuration or a code error.  Look for other error messages and review the code of the repository governance service. " +
                                 "Once the cause is resolved, retry the repository governance request."),

    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for RepositoryGovernanceErrorCode expects to be passed one of the enumeration rows defined in
     * RepositoryGovernanceErrorCode above.   For example:
     *
     *     RepositoryGovernanceErrorCode   errorCode = RepositoryGovernanceErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    RepositoryGovernanceErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
