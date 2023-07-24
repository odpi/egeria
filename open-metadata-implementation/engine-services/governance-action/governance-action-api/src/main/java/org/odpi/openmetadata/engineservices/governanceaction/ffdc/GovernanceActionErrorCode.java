/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The GovernanceActionErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Governance Action Engine Services.  It is used in conjunction with all exceptions,
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
public enum GovernanceActionErrorCode implements ExceptionMessageSet
{
    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    GOVERNANCE_ACTION_ENGINE_INSTANCE_FAILURE(400, "OMES-GOVERNANCE-ACTION-400-007",
                         "The Governance Action OMES are unable to initialize a new instance of governance action engine {0}; error message is {1}",
                         "The Governance Action OMES detected an error during the start up of a specific governance action engine instance.  " +
                                              "Its governance action services are not available in the Governance Action OMES.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "OMES-GOVERNANCE-ACTION-400-008",
                             "The Governance Action OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Governance Action OMES detected an error during the start up of a specific server instance.  " +
                                     "No governance action services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the governance action engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "OMES-GOVERNANCE-ACTION-400-010",
             "The Governance Action OMES are unable to retrieve the connection for the configuration " +
                                  "listener for server {0} from metadata server {1}. " +
                                  "Exception returned was {2} with error message {3}",
             "The server continues to run.  The Governance Action OMES will start up the " +
                                  "governance action engines and they will operate with whatever configuration that they can retrieve.  " +
                                  "Periodically the Governance Action OMES will" +
                                  "retry the request to retrieve the connection information.  " +
                                  "Without the connection, the Governance Action OMES will not be notified of changes to the governance action " +
                                                    "engines' configuration",
              "This problem may be caused because the Governance Action OMES has been configured with the wrong location for the " +
                                  "metadata server, or the metadata server is not running the Governance Action Engine OMAS service or " +
                                  "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                  "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                  "refresh-config command or wait for the Governance Action OMES to retry the configuration request."),

    UNKNOWN_GOVERNANCE_ACTION_ENGINE_CONFIG_AT_STARTUP(400, "OMES-GOVERNANCE-ACTION-400-011",
             "Properties for governance action engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to server {4}",
             "The Governance Action OMES is not able to initialize the governance action engine and so it will not de able to support governance action " +
                                 "requests targeted to this governance action engine.  ",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the Governance Action OMES.  Once the cause is resolved, restart the server."),

    NO_GOVERNANCE_ACTION_ENGINES_STARTED(400,"OMES-GOVERNANCE-ACTION-400-012",
             "Governance Action OMES in server {0} is unable to start any governance action engines",
             "The server is not able to run any governance action requests.  It fails to start.",
             "Add the configuration for at least one governance action engine to this Governance Action OMES."),

    NO_GOVERNANCE_ACTION_ENGINE_CLIENT(400,"OMES-GOVERNANCE-ACTION-400-013",
                                 "Governance Action OMES in server {0} is unable to start a client to the Governance Action Engine OMAS for governance action engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any governance action requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_GOVERNANCE_ACTION_ENGINE_CONFIG(400, "OMES-GOVERNANCE-ACTION-400-014",
             "Properties for governance action engine called {0} have not been returned by open metadata server {1} to Governance Action OMES in server {2}",
             "The Governance Action OMES is not able to initialize the governance action engine and so it will not de able to support governance action " +
                                            "requests targeted to this governance action engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the Governance Action OMES.  Once the cause is resolved, restart the server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_GOVERNANCE_ACTION_ENGINE(400, "OMES-GOVERNANCE-ACTION-400-020",
                             "Governance Action engine {0} is not running in the Governance Action OMES in server {1}",
                             "The governance action engine requested on a request is not known to the Governance Action OMES.",
                             "This may be a configuration error in the Governance Action OMES or an error in the caller.  " +
                                     "The supported governance action engines are listed in the Governance Action OMES's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "governance action engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the Governance Action OMES is running the correct governance action engines then validate that " +
                                     "the caller has passed the correct name of the governance action engine to the Governance Action OMES.  If all of this is " +
                                     "correct then it may be a code error in the Governance Action OMES and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the governance action request."),

    UNKNOWN_GOVERNANCE_ACTION_REQUEST_TYPE(400, "OMES-GOVERNANCE-ACTION-400-021",
             "The governance action request type {0} is not recognized by governance action engine {1} hosted by server {2}",
             "The governance action request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the governance action engine or the metadata server" +
                                           "used by the engine host server may be down.  " +
                                           "The configuration that defines the governance action request type in the governance action engine and links " +
                                           "it to the governance action service that should run is maintained in the metadata server by the Governance Action " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the governance action " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the Governance Action OMES in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the governance action request."),

    INVALID_GOVERNANCE_ACTION_SERVICE(400, "OMES-GOVERNANCE-ACTION-400-022",
             "The governance action service {0} linked to governance action request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
             "The governance action request is not run and an error is returned to the caller.",
             "This may be an error in the governance action services's logic or the governance action service may not be properly deployed or " +
                                      "there is a configuration error related to the governance action engine.  " +
                                      "The configuration that defines the governance action request type in the governance action engine and links " +
                                      "it to the governance action service is maintained in the metadata server by the Governance Action " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "governance action service's implementation has been deployed so the Governance Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the governance action service in which case, " +
                                      "raise an issue with the author of the governance action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the governance action request."),

    NULL_GOVERNANCE_ACTION_SERVICE(400, "OMES-GOVERNANCE-ACTION-400-023",
                              "Method {0} can not execute in the governance action engine {1} hosted by Governance Action OMES in server {2} because the associated " +
                                      "governance action service properties are null",
                              "The governance action request is not run and an error is returned to the caller.",
                              "This may be an error in the governance action engine's logic or the Governance Action Engine OMAS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    GOVERNANCE_ACTION_ENGINE_NOT_INITIALIZED(400,"OMES-GOVERNANCE-ACTION-400-024",
             "Governance Action OMES in server {0} is unable to pass a governance action request to governance action engine {1} because this governance action engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The governance action engine is not able to run any governance action requests until it is able to retrieve its configuration.",
                                     "Use the configuration interface of the Governance Action Engine OMAS to create a definition of at least one governance action" +
                                             " engine."),

    UNKNOWN_GOVERNANCE_ACTION_SERVICE( 400, "OMES-GOVERNANCE-ACTION-400-030",
                                       "The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of " +
                                               "governance action service.",
                                       "The governance action request is not run and an error is returned to the caller.  Subsequent requests to this governance action service will also fail.",
                                       "This version of the Governance Engine OMES does not support this type of governance action service.  " +
                                               "It is likely that you need a future version of Egeria or another platform that supports this type of governance action service."),

    NOT_GOVERNANCE_ACTION_SERVICE( 400, "OMES-GOVERNANCE-ACTION-400-031",
                                   "The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  " +
                                           "Its class is {2} rather than a subclass of {3}",
                                   "The governance action request is not run and an error is returned to the caller.  Subsequent calls to this service will fail in the same way",
                                   "Correct the configuration for the Governance Action OMES to only include valid governance action service implementations."),

    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for GovernanceActionErrorCode expects to be passed one of the enumeration rows defined in
     * GovernanceActionErrorCode above.   For example:
     *
     *     GovernanceActionErrorCode   errorCode = GovernanceActionErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GovernanceActionErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
