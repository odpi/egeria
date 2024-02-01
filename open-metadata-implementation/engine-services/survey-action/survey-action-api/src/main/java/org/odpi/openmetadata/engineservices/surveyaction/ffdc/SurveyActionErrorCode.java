/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The SurveyActionErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Discovery Engine Services.  It is used in conjunction with all exceptions,
 * both Checked and Runtime (unchecked).
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
public enum SurveyActionErrorCode implements ExceptionMessageSet
{
    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    /**
     *
     */
    SURVEY_ACTION_ENGINE_INSTANCE_FAILURE(400, "OMES-SURVEY-ACTION-400-007",
                                          "The Survey Action OMES are unable to initialize a new instance of survey action engine {0}; error message is {1}",
                                          "The Survey Action OMES detected an error during the start up of a specific survey action engine instance.  " +
                                              "Its survey action services are not available in the Survey Action OMES.",
                                          "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "OMES-SURVEY-ACTION-400-008",
                             "The Survey Action OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Survey Action OMES detected an error during the start up of a specific server instance.  " +
                                     "No survey action services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the survey action engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "OMES-SURVEY-ACTION-400-010",
             "The Survey Action OMES are unable to retrieve the connection for the configuration " +
                                  "listener for server {0} from metadata server {1}. " +
                                  "Exception returned was {2} with error message {3}",
             "The server continues to run.  The Survey Action OMES will start up the " +
                                  "survey action engines and they will operate with whatever configuration that they can retrieve.  " +
                                  "Periodically the Survey Action OMES will" +
                                  "retry the request to retrieve the connection information.  " +
                                  "Without the connection, the Survey Action OMES will not be notified of changes to the discovery " +
                                                    "engines' configuration",
              "This problem may be caused because the Survey Action OMES has been configured with the wrong location for the " +
                                  "metadata server, or the metadata server is not running the Discovery Engine OMAS service or " +
                                  "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                  "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                  "refresh-config command or wait for the Survey Action OMES to retry the configuration request."),

    UNKNOWN_ENGINE_CONFIG_AT_STARTUP(400, "OMES-SURVEY-ACTION-400-011",
                                     "Properties for survey action engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to server {4}",
                                     "The Survey Action OMES is not able to initialize the survey action engine and so it will not de able to support discovery " +
                                 "requests targeted to this survey action engine.  ",
                                     "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the Survey Action OMES.  Once the cause is resolved, restart the server."),

    NO_SURVEY_ENGINES_STARTED(400, "OMES-SURVEY-ACTION-400-012",
                              "Survey Action OMES in server {0} is unable to start any survey action engines",
                              "The server is not able to run any discovery requests.  It fails to start.",
                              "Add the configuration for at least one survey action engine to this Survey Action OMES."),

    NO_STEWARDSHIP_ACTION_CLIENT(400, "OMES-SURVEY-ACTION-400-013",
                                 "Survey Action OMES in server {0} is unable to start a client to the Discovery Engine OMAS for survey action engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any discovery requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_DISCOVERY_ENGINE_CONFIG(400, "OMES-SURVEY-ACTION-400-014",
             "Properties for survey action engine called {0} have not been returned by open metadata server {1} to Survey Action OMES in server {2}",
             "The Survey Action OMES is not able to initialize the survey action engine and so it will not de able to support discovery " +
                                            "requests targeted to this survey action engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the Survey Action OMES.  Once the cause is resolved, restart the server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_SURVEY_ACTION_ENGINE(400, "OMES-SURVEY-ACTION-400-020",
                                 "Discovery engine {0} is not running in the Survey Action OMES in server {1}",
                                 "The survey action engine requested on a request is not known to the Survey Action OMES.",
                                 "This may be a configuration error in the Survey Action OMES or an error in the caller.  " +
                                     "The supported survey action engines are listed in the Survey Action OMES's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "survey action engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the Survey Action OMES is running the correct survey action engines then validate that " +
                                     "the caller has passed the correct name of the survey action engine to the Survey Action OMES.  If all of this is " +
                                     "correct then it may be a code error in the Survey Action OMES and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_REQUEST_TYPE(400, "OMES-SURVEY-ACTION-400-021",
             "The discovery request type {0} is not recognized by survey action engine {1} hosted by server {2}",
             "The discovery request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the survey action engine or the metadata server" +
                                           "used by the engine host server may be down.  " +
                                           "The configuration that defines the discovery request type in the survey action engine and links " +
                                           "it to the survey action service that should run is maintained in the metadata server by the Discovery " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the discovery " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the Survey Action OMES in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the discovery request."),

    INVALID_DISCOVERY_SERVICE(400, "OMES-SURVEY-ACTION-400-022",
             "The survey action service {0} linked to discovery request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
             "The discovery request is not run and an error is returned to the caller.",
             "This may be an error in the survey action service's logic or the survey action service may not be properly deployed or " +
                                      "there is a configuration error related to the survey action engine.  " +
                                      "The configuration that defines the discovery request type in the survey action engine and links " +
                                      "it to the survey action service is maintained in the metadata server by the Discovery " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "survey action service's implementation has been deployed so the Survey Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the survey action service in which case, " +
                                      "raise an issue with the author of the survey action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the discovery request."),

    NULL_DISCOVERY_SERVICE(400, "OMES-SURVEY-ACTION-400-023",
                              "Method {0} can not execute in the survey action engine {1} hosted by Survey Action OMES in server {2} because the associated " +
                                      "survey action service properties are null",
                              "The discovery request is not run and an error is returned to the caller.",
                              "This may be an error in the survey action engine's logic or the Discovery Engine OMAS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    DISCOVERY_ENGINE_NOT_INITIALIZED(400,"OMES-SURVEY-ACTION-400-024",
             "Survey Action OMES in server {0} is unable to pass a discovery request to survey action engine {1} because this survey action engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The survey action engine is not able to run any discovery requests until it is able to retrieve its configuration.",
                                     "Use the configuration interface of the Discovery Engine OMAS to create a definition of at least one discovery" +
                                             " engine."),
    ;


    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    SurveyActionErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode,
                                              errorMessageId,
                                              errorMessage,
                                              systemAction,
                                              userAction);
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
        ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                                      errorMessageId,
                                                                                      errorMessage,
                                                                                      systemAction,
                                                                                      userAction);

        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
