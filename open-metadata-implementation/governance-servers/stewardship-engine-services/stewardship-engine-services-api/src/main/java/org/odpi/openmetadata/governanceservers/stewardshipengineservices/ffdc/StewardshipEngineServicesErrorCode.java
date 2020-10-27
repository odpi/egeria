/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The StewardshipEngineServicesErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Stewardship Engine Services.  It is used in conjunction with all exceptions,
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
public enum StewardshipEngineServicesErrorCode implements ExceptionMessageSet
{
    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */
    NO_CONFIG_DOC(400,"STEWARDSHIP-ENGINE-SERVICES-400-001 ",
                  "Stewardship server {0} has been passed a null configuration document section for the stewardship engine services",
                  "The stewardship engine services can not retrieve its configuration values.  " +
                          "The hosting stewardship server fails to start.",
                  "This is an internal logic error since the admin services should not have initialized the stewardship engine services" +
                          "without this section of the configuration document filled in.  Raise an issue to get this fixed."),

    NO_OMAS_SERVER_URL(400,"STEWARDSHIP-ENGINE-SERVICES-400-002 ",
                       "Stewardship server {0} is not configured with the platform URL root for the Stewardship Engine OMAS",
                       "The stewardship engine services is not able to locate the metadata server to retrieve the configuration for " +
                               "the stewardship engines.  The stewardship server fails to start.",
                       "To be successful the stewardship engine services needs both the platform URL root and the name of the metadata " +
                               "server as well as the list of stewardship engines it is to host. Add the " +
                               "configuration for the platform URL root to this stewardship server's configuration document and check that the " +
                               "other required configuration properties are in place. Then restart the stewardship server."),

    NO_OMAS_SERVER_NAME(400, "STEWARDSHIP-ENGINE-SERVICES-400-003 ",
                        "Stewardship server {0} is not configured with the name for the server running the Stewardship Engine OMAS",
                        "The server is not able to retrieve its configuration from the metadata server.  It fails to start.",
                        "Add the configuration for the server name to this stewardship server's configuration document.  " +
                                "Ensure that the platform URL root points to the platform where the metadata server is running and that" +
                                "there is at least one stewardship engine listed.  Once the configuration document is set up correctly,  " +
                                "restart the stewardship server."),

    NO_STEWARDSHIP_ENGINES(400,"STEWARDSHIP-ENGINE-SERVICES-400-004 ",
                         "Stewardship server {0} is not configured with any stewardship engines",
                         "The server is not able to run any stewardship requests.  It fails to start.",
                         "Add the qualified name for at least one stewardship engine to the stewardship engine services section" +
                                 "of this stewardship server's configuration document " +
                                 "and then restart the stewardship server."),

    STEWARDSHIP_ENGINE_INSTANCE_FAILURE(400, "STEWARDSHIP-ENGINE-SERVICES-400-007 ",
                         "The stewardship engine services are unable to initialize a new instance of stewardship engine {0}; error message is {1}",
                         "The stewardship engine services detected an error during the start up of a specific stewardship engine instance.  " +
                                              "Its stewardship services are not available in the stewardship server.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "STEWARDSHIP-ENGINE-SERVICES-400-008 ",
                             "The stewardship engine services are unable to initialize a new instance of stewardship server {0}; error message is {1}",
                             "The stewardship engine services detected an error during the start up of a specific stewardship server instance.  " +
                                     "No stewardship services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the stewardship server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the stewardship server has retrieved the
     * configuration, the stewardship engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "STEWARDSHIP-ENGINE-SERVICES-400-010 ",
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

    UNKNOWN_STEWARDSHIP_ENGINE_CONFIG_AT_STARTUP(400, "STEWARDSHIP-ENGINE-SERVICES-400-011 ",
             "Properties for stewardship engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to stewardship server {4}",
             "The stewardship server is not able to initialize the stewardship engine and so it will not de able to support stewardship " +
                                 "requests targeted to this stewardship engine.  ",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the stewardship server.  Once the cause is resolved, restart the stewardship server."),

    NO_STEWARDSHIP_ENGINES_STARTED(400,"STEWARDSHIP-ENGINE-SERVICES-400-012 ",
             "Stewardship server {0} is unable to start any stewardship engines",
             "The server is not able to run any stewardship requests.  It fails to start.",
             "Add the configuration for at least one stewardship engine to this stewardship server."),

    NO_STEWARDSHIP_ENGINE_CLIENT(400,"STEWARDSHIP-ENGINE-SERVICES-400-013 ",
                                 "Stewardship server {0} is unable to start a client to the Stewardship Engine OMAS for stewardship engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any stewardship requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_STEWARDSHIP_ENGINE_CONFIG(400, "STEWARDSHIP-ENGINE-SERVICES-400-014 ",
             "Properties for stewardship engine called {0} have not been returned by open metadata server {1} to stewardship server {2}",
             "The stewardship server is not able to initialize the stewardship engine and so it will not de able to support stewardship " +
                                            "requests targeted to this stewardship engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the stewardship server.  Once the cause is resolved, restart the stewardship server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_STEWARDSHIP_ENGINE(400, "STEWARDSHIP-ENGINE-SERVICES-400-020 ",
                             "Stewardship engine {0} is not running in the stewardship server {1}",
                             "The stewardship engine requested on a request is not known to the stewardship server.",
                             "This may be a configuration error in the stewardship server or an error in the caller.  " +
                                     "The supported stewardship engines are listed in the stewardship server's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "stewardship engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the stewardship server is running the correct stewardship engines then validate that " +
                                     "the caller has passed the correct name of the stewardship engine to the stewardship server.  If all of this is " +
                                     "correct then it may be a code error in the stewardship engine services and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the stewardship request."),

    UNKNOWN_STEWARDSHIP_REQUEST_TYPE(400, "STEWARDSHIP-ENGINE-SERVICES-400-021 ",
             "The stewardship request type {0} is not recognized by stewardship engine {1} hosted by stewardship server {2}",
             "The stewardship request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the stewardship engine or the metadata server" +
                                           "used by the stewardship server may be down.  " +
                                           "The configuration that defines the stewardship request type in the stewardship engine and links " +
                                           "it to the stewardship service that should run is maintained in the metadata server by the Stewardship " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the stewardship " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the stewardship server in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the stewardship request."),

    INVALID_STEWARDSHIP_SERVICE(400, "STEWARDSHIP-ENGINE-SERVICES-400-022 ",
             "The stewardship service {0} linked to stewardship request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
             "The stewardship request is not run and an error is returned to the caller.",
             "This may be an error in the stewardship services's logic or the stewardship service may not be properly deployed or " +
                                      "there is a configuration error related to the stewardship engine.  " +
                                      "The configuration that defines the stewardship request type in the stewardship engine and links " +
                                      "it to the stewardship service is maintained in the metadata server by the Stewardship " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "stewardship service's implementation has been deployed so the stewardship server can load it.  If all this is " +
                                      "true this it is likely to be a code error in the stewardship service in which case, " +
                                      "raise an issue with the author of the stewardship service to get it fixed.  Once the cause is resolved, " +
                                      "retry the stewardship request."),

    NULL_STEWARDSHIP_SERVICE(400, "STEWARDSHIP-ENGINE-SERVICES-400-023 ",
                              "Method {0} can not execute in the stewardship engine {1} hosted by stewardship server {2} because the associated " +
                                      "stewardship service properties are null",
                              "The stewardship request is not run and an error is returned to the caller.",
                              "This may be an error in the stewardship engine's logic or the Stewardship Engine OMAS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    STEWARDSHIP_ENGINE_NOT_INITIALIZED(400,"STEWARDSHIP-ENGINE-SERVICES-400-024 ",
             "Stewardship server {0} is unable to pass a stewardship request to stewardship engine {1} because this stewardship engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The stewardship engine is not able to run any stewardship requests until it is able to retrieve its configuration.",
                                     "Use the configuration interface of the Stewardship Engine OMAS to create a definition of at least one stewardship" +
                                             " engine."),
    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for StewardshipEngineServicesErrorCode expects to be passed one of the enumeration rows defined in
     * StewardshipEngineServicesErrorCode above.   For example:
     *
     *     StewardshipEngineServicesErrorCode   errorCode = StewardshipEngineServicesErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    StewardshipEngineServicesErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }
}
