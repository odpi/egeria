/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AssetAnalysisErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Discovery Engine Services.  It is used in conjunction with all exceptions,
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
public enum AssetAnalysisErrorCode implements ExceptionMessageSet
{
    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */

    DISCOVERY_ENGINE_INSTANCE_FAILURE(400, "OMES-ASSET-ANALYSIS-400-007 ",
                         "The Asset Analysis OMES are unable to initialize a new instance of discovery engine {0}; error message is {1}",
                         "The Asset Analysis OMES detected an error during the start up of a specific discovery engine instance.  " +
                                              "Its discovery services are not available in the Asset Analysis OMES.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "OMES-ASSET-ANALYSIS-400-008 ",
                             "The Asset Analysis OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Asset Analysis OMES detected an error during the start up of a specific server instance.  " +
                                     "No discovery services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the discovery engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "OMES-ASSET-ANALYSIS-400-010 ",
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

    UNKNOWN_DISCOVERY_ENGINE_CONFIG_AT_STARTUP(400, "OMES-ASSET-ANALYSIS-400-011 ",
             "Properties for discovery engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to server {4}",
             "The Asset Analysis OMES is not able to initialize the discovery engine and so it will not de able to support discovery " +
                                 "requests targeted to this discovery engine.  ",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the Asset Analysis OMES.  Once the cause is resolved, restart the server."),

    NO_DISCOVERY_ENGINES_STARTED(400,"OMES-ASSET-ANALYSIS-400-012 ",
             "Asset Analysis OMES in server {0} is unable to start any discovery engines",
             "The server is not able to run any discovery requests.  It fails to start.",
             "Add the configuration for at least one discovery engine to this Asset Analysis OMES."),

    NO_DISCOVERY_ENGINE_CLIENT(400,"OMES-ASSET-ANALYSIS-400-013 ",
                                 "Asset Analysis OMES in server {0} is unable to start a client to the Discovery Engine OMAS for discovery engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any discovery requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_DISCOVERY_ENGINE_CONFIG(400, "OMES-ASSET-ANALYSIS-400-014 ",
             "Properties for discovery engine called {0} have not been returned by open metadata server {1} to Asset Analysis OMES in server {2}",
             "The Asset Analysis OMES is not able to initialize the discovery engine and so it will not de able to support discovery " +
                                            "requests targeted to this discovery engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the Asset Analysis OMES.  Once the cause is resolved, restart the server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_DISCOVERY_ENGINE(400, "OMES-ASSET-ANALYSIS-400-020 ",
                             "Discovery engine {0} is not running in the Asset Analysis OMES in server {1}",
                             "The discovery engine requested on a request is not known to the Asset Analysis OMES.",
                             "This may be a configuration error in the Asset Analysis OMES or an error in the caller.  " +
                                     "The supported discovery engines are listed in the Asset Analysis OMES's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "discovery engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the Asset Analysis OMES is running the correct discovery engines then validate that " +
                                     "the caller has passed the correct name of the discovery engine to the Asset Analysis OMES.  If all of this is " +
                                     "correct then it may be a code error in the Asset Analysis OMES and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_REQUEST_TYPE(400, "OMES-ASSET-ANALYSIS-400-021 ",
             "The discovery request type {0} is not recognized by discovery engine {1} hosted by server {2}",
             "The discovery request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the discovery engine or the metadata server" +
                                           "used by the engine host server may be down.  " +
                                           "The configuration that defines the discovery request type in the discovery engine and links " +
                                           "it to the discovery service that should run is maintained in the metadata server by the Discovery " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the discovery " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the Asset Analysis OMES in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the discovery request."),

    INVALID_DISCOVERY_SERVICE(400, "OMES-ASSET-ANALYSIS-400-022 ",
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

    NULL_DISCOVERY_SERVICE(400, "OMES-ASSET-ANALYSIS-400-023 ",
                              "Method {0} can not execute in the discovery engine {1} hosted by Asset Analysis OMES in server {2} because the associated " +
                                      "discovery service properties are null",
                              "The discovery request is not run and an error is returned to the caller.",
                              "This may be an error in the discovery engine's logic or the Discovery Engine OMAS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    DISCOVERY_ENGINE_NOT_INITIALIZED(400,"OMES-ASSET-ANALYSIS-400-024 ",
             "Asset Analysis OMES in server {0} is unable to pass a discovery request to discovery engine {1} because this discovery engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The discovery engine is not able to run any discovery requests until it is able to retrieve its configuration.",
                                     "Use the configuration interface of the Discovery Engine OMAS to create a definition of at least one discovery" +
                                             " engine."),
    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for AssetAnalysisErrorCode expects to be passed one of the enumeration rows defined in
     * AssetAnalysisErrorCode above.   For example:
     *
     *     AssetAnalysisErrorCode   errorCode = AssetAnalysisErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    AssetAnalysisErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
