/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The EngineHostServicesErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Engine Host Services.  It is used in conjunction with all exceptions,
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
public enum EngineHostServicesErrorCode implements ExceptionMessageSet
{
    /**
     * ENGINE-HOST-SERVICES-400-001 - Engine host {0} has been passed a null configuration document section for the engine host services
     */
    NO_CONFIG_DOC(400,"ENGINE-HOST-SERVICES-400-001",
                  "Engine host {0} has been passed a null configuration document section for the engine host services",
                  "The engine host services can not retrieve its configuration values.  " +
                          "The hosting engine host server fails to start.",
                  "This is an internal logic error since the admin services should not have initialized the engine host services " +
                          "without this section of the configuration document filled in.  Raise an issue to get this fixed."),

    /**
     * ENGINE-HOST-SERVICES-400-002 - Engine host {0} is not configured with any engine services
     */
    NO_ENGINE_SERVICES_CONFIGURED(400,"ENGINE-HOST-SERVICES-400-002",
                                       "Engine host {0} is not configured with any engine services",
                                       "The engine host, fails to start because it would be bored with nothing to do.",
                                       "Add the configuration for at least one engine service to the engine services' section " +
                                               "of this engine host's configuration document and then restart the engine host server."),

    /**
     * ENGINE-HOST-SERVICES-400-003 - The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2}
     */
    SERVICE_INSTANCE_FAILURE(400, "ENGINE-HOST-SERVICES-400-003",
                             "The engine host services are unable to initialize a new instance of engine host {0}; " +
                                     "exception {1} with message {2}",
                             "The engine host services detected an error during the start up of a specific engine host instance.  " +
                                     "No engine services are running in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the engine host."),

    /**
     * ENGINE-HOST-SERVICES-400-004 - Engine service with URL marker {0} is not registered in the engine host {1}
     */
    UNKNOWN_ENGINE_SERVICE(400, "ENGINE-HOST-SERVICES-400-004",
                           "Engine service with URL marker {0} is not registered in the engine host {1}",
                           "The engine service specified on a request is not known to the engine host.",
                           "This may be a configuration error in the engine host or an error in the caller.  " +
                                   "The supported engine services are listed in the engine host's configuration.  " +
                                   "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                   "engine services are started.  Look for other error messages that indicate that an error occurred during " +
                                   "start up.  If the engine host is running the correct engine services then validate that " +
                                   "the caller has passed the correct URL marker of the engine service to the engine host." +
                                   "If all of this is correct then it may be a code error in the engine host services and you need to " +
                                   "raise an issue to get it fixed.  Once the cause is resolved, retry the request."),

    /**
     * ENGINE-HOST-SERVICES-400-005 - Governance engine named {0} is not running in the engine host {1}
     */
    UNKNOWN_ENGINE_NAME(400, "ENGINE-HOST-SERVICES-400-005",
                        "Governance engine named {0} is not running in the engine host {1}",
                        "The governance engine specified on a request is not known to the engine service.",
                        "This may be a configuration error in the engine host or an error in the caller.  " +
                                "The supported integration connectors are listed in the engine service's configuration.  " +
                                "Check the configuration document for the daemon and then its start up messages to ensure the correct " +
                                "engine services and connectors are started successfully.  " +
                                "Look for other error messages that indicate that an error occurred during " +
                                "start up.  If the engine host is running the correct engine services then validate that " +
                                "the caller has passed matching connector name and URL marker of the engine service to the " +
                                "engine host." +
                                "If all of this is correct then it may be a code error in the engine host services and you need to " +
                                "raise an issue to get it fixed.  Once the cause is resolved, retry the request."),

    /**
     * ENGINE-HOST-SERVICES-400-006 - No governance engines are running in the engine host {0}
     */
    NO_GOVERNANCE_ENGINES(400, "ENGINE-HOST-SERVICES-400-006",
                          "No governance engines are running in the engine host {0}",
                          "The call to the engine host fails and an exception is returned to the caller.",
                          "This is either a configuration error or a logic error.  If this is a configuration error, the" +
                                  "engine host will have logged detailed messages to the audit log to describe what is wrong " +
                                  "and how to fix it.  " +
                                  "If there are no errors in the configuration, raise an issue to get help to fix this."),

    /**
     * ENGINE-HOST-SERVICES-400-007 - No governance engines are running in the engine service {0} on engine host {1}
     */
    NO_ENGINES_FOR_SERVICE(400, "ENGINE-HOST-SERVICES-400-007",
                          "No governance engines are running in the engine service {0} on engine host {1}",
                          "The call to the engine service fails and an exception is returned to the caller.",
                          "This is either a configuration error or a logic error.  If this is a configuration error, the" +
                                  "engine host will have logged detailed messages to the audit log when it was initializing the engine service " +
                                  "to describe what is wrong and how to fix it.  " +
                                  "If there are no errors in the configuration, raise an issue to get help to fix this."),

    /**
     * ENGINE-HOST-SERVICES-400-008 - Engine host {0} is not configured with the platform URL root for the {1}
     */
    NO_CONFIG_OMAS_SERVER_URL(400,"ENGINE-HOST-SERVICES-400-008",
                       "Engine host {0} is not configured with the platform URL root for the {1}",
                       "The engine service is not able to locate the server where its partner OMAS is running in order " +
                               "to exchange metadata.  The engine host server fails to start.",
                       "To be successful each engine service needs both the platform URL root and the name of the " +
                               "server there the OMAS is running as well as the list of connections for the connectors it is to manage. Add this " +
                               "configuration to the engine host's configuration document and check that the " +
                               "other required configuration properties are in place. Then restart the engine host server."),

    /**
     * ENGINE-HOST-SERVICES-400-009 - Engine host {0} is not configured with the name for the server running the {1}
     */
    NO_CONFIG_OMAS_SERVER_NAME(400, "ENGINE-HOST-SERVICES-400-009",
                        "Engine host {0} is not configured with the name for the server running the {1}",
                        "The engine service is not able to locate the metadata server where its partner OMAS is running in order " +
                                "to exchange metadata.  The engine host fails to start.",
                        "Add the configuration for the server name for this engine service to the engine host's " +
                                "configuration document.  " +
                                "Ensure that the platform URL root points to the platform where the metadata server is running and that" +
                                "there is at least one connection for an integration connector listed.  Once the configuration document is set up " +
                                "correctly, restart the engine host."),

    /**
     * ENGINE-HOST-SERVICES-400-010 - Engine service {0} running in engine host {1} is not configured with the platform URL root for the {2}
     */
    NO_PARTNER_OMAS_SERVER_URL(400,"ENGINE-HOST-SERVICES-400-010",
                       "Engine service {0} running in engine host {1} is not configured with the platform URL root for the {2}",
                       "The engine service is not able to locate the server where its partner OMAS is running in order " +
                               "to exchange metadata.  The engine host server fails to start.",
                       "To be successful each engine service needs both the platform URL root and the name of the " +
                               "server there the OMAS is running as well as the list of connections for the connectors it is to manage. Add this " +
                               "configuration to the engine host's configuration document and check that the " +
                               "other required configuration properties are in place. Then restart the engine host server."),

    /**
     * ENGINE-HOST-SERVICES-400-011 - Engine service {0} running in engine host {1} is not configured with the name for the server running the {2}
     */
    NO_PARTNER_OMAS_SERVER_NAME(400, "ENGINE-HOST-SERVICES-400-011",
                        "Engine service {0} running in engine host {1} is not configured with the name for the server running the {2}",
                        "The engine service is not able to locate the metadata server where its partner OMAS is running in order " +
                                "to exchange metadata.  The engine host fails to start.",
                        "Add the configuration for the server name for this engine service to the engine host's " +
                                "configuration document.  " +
                                "Ensure that the platform URL root points to the platform where the metadata server is running and that" +
                                "there is at least one connection for an integration connector listed.  Once the configuration document is set up " +
                                "correctly, restart the engine host."),

    /**
     * ENGINE-HOST-SERVICES-400-012 - {0} in engine host {1} is configured with a null engine name
     */
    NULL_ENGINE_NAME(400, "ENGINE-HOST-SERVICES-400-012",
                     "{0} in engine host {1} is configured with a null engine name",
                     "The start up of the engine host server fails with an exception.",
                     "Correct the qualified name for the governance engine configured for the engine service" +
                             " in the engine host's configuration document."),

    /**
     * ENGINE-HOST-SERVICES-400-013 - The engine service {0} has been configured with a null admin class in engine host {1}
     */
    NULL_ENGINE_SERVICE_ADMIN_CLASS(400, "ENGINE-HOST-SERVICES-400-013",
                                    "The engine service {0} has been configured with a null admin class in engine host {1}",
                                    "The engine service fails to start because the engine host can not initialize it.",
                                    "Each engine service registers itself using a static method call with the engine host as" +
                                            "their classes are loaded into " +
                                            "the platform.  This is driven by the component scan for REST APIs implemented by the spring modules by " +
                                            "the platform-chassis-spring module.  " +
                                            "Ensure the engine service registers itself with the engine-host-services module and " +
                                            "the platform-chassis-spring module has access to the engine service's spring module."),

    /**
     * The engine service {0} has been configured with an admin class of {1} which can not be used by the class loader.  The {2} exception was returned with message {3}
     */
    BAD_ENGINE_SERVICE_ADMIN_CLASS(400, "ENGINE-HOST-SERVICES-400-014",
                                   "The engine service {0} has been configured with an admin class of {1} which can not be " +
                                           "used by the class loader.  The {2} exception was returned with message {3}",
                                   "The engine service fails to start.  Its governance engines, if any, are not activated.",
                                   "Check that the jar containing the engine service's admin class is visible to the OMAG Server Platform through " +
                                           "the class path - and that the class name specified includes the full, correct package name and class name.  " +
                                           "Once the class is correctly set up, restart the engine host.  It will be necessary to restart the " +
                                           "OMAG Server Platform if the class path needed adjustment. "),


    /**
     * ENGINE-HOST-SERVICES-400-015 - Engine service {0} in engine host {1} is unable to start any governance engines
     */
    ENGINE_SERVICE_NULL_HANDLERS(400,"ENGINE-HOST-SERVICES-400-015",
                                 "Engine service {0} in engine host {1} is unable to start any governance engines",
                                 "The server is not able to run any governance requests.  It fails to start.",
                                 "Correct the configuration for the engine service to ensure it has at least one valid governance engine."),


    /**
     * ENGINE-HOST-SERVICES-400-016 - Method {0} can not execute in the governance engine {1} hosted by engine host server {2} because the associated governance service properties are invalid: {3}
     */
    NULL_GOVERNANCE_SERVICE(400, "ENGINE-HOST-SERVICES-400-016",
                            "Method {0} can not execute in the governance engine {1} hosted by engine host server {2} because the associated " +
                                    "governance service properties are invalid: {3}",
                            "The governance request is not run and an error is returned to the caller.",
                            "This may be an error in the governance engine's logic or the Governance Engine OMAS may have returned " +
                                    "invalid configuration.  Raise an issue to get help to fix it"),

    /**
     * ENGINE-HOST-SERVICES-400-017 - Engine host server {0} is unable to pass a governance request to governance engine {1} because this
     * governance engine has not retrieved its configuration from the metadata access server
     */
    GOVERNANCE_ENGINE_NOT_INITIALIZED(400,"ENGINE-HOST-SERVICES-400-017",
                                      "Engine host server {0} is unable to pass a governance request to governance engine {1} because this governance engine has not " +
                                              "retrieved its configuration from the metadata access server",
                                      "The governance engine is not able to run any governance requests until it is able to retrieve its configuration.",
                                      "Use the configuration interface of the Governance Engine OMAS to create a definition of at least one governance" +
                                              " engine."),

    /**
     * ENGINE-HOST-SERVICES-400-018 - The engine service URL marker {0} is not recognized.  Valid service URL markers are: {1}
     */
    UNRECOGNIZED_SERVICE(400, "ENGINE-HOST-SERVICES-400-018",
                         "The engine service URL marker {0} is not recognized.  Valid service URL markers are: {1}",
                         "The request fails and returns this exception.  No action is taken by the engine host.",
                         "Correct the supplied URL marker to one that is valid.  The admin services has a command to list the " +
                                 "engine services configured for this engine host."),

    /**
     * ENGINE-HOST-SERVICES-400-019 - The configuration document for engine {0} configuration property for engine service {1} in engine host {2} is null
     */
    NULL_SERVICE_CONFIG_VALUE(400, "ENGINE-HOST-SERVICES-400-019",
                              "The configuration document for engine {0} configuration property for engine service {1} in engine host {2} is null",
                              "The engine service fails to start and this causes the hosting engine host to fail.",
                              "Add a suitable value for this configuration property in the engine service configuration."),

    /**
     * ENGINE-HOST-SERVICES-400-020 - The engine host services in engine host server {0} are unable to initialize a new instance of engine service {1}; exception {2} with message {3}
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION(400, "ENGINE-HOST-SERVICES-400-020",
                                        "The engine host services in engine host server {0} are unable to initialize a new instance of engine service {1}; exception {2} with message {3}",
                                        "The engine service detected an error during the start up of a specific governance engine instance.  " +
                                                "Its governance services are not available.",
                                        "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                                "Once this is resolved, restart the server."),

    

   /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the governance engines will operate successfully.
     */

    /**
     * ENGINE-HOST-SERVICES-400-021 - The engine host services are unable to retrieve the connection for the configuration
     * listener for server {0} from metadata server {1}. Exception returned was {2} with error message {3}
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "ENGINE-HOST-SERVICES-400-021",
                                            "The engine host services are unable to retrieve the connection for the configuration " +
                                                    "listener for server {0} from metadata server {1}. " +
                                                    "Exception returned was {2} with error message {3}",
                                            "The server continues to run.  The engine host services will start up the " +
                                                    "governance engines and they will operate with whatever configuration that they can retrieve.  " +
                                                    "Periodically the engine host services will" +
                                                    "retry the request to retrieve the connection information.  " +
                                                    "Without the connection, the engine host services will not be notified of changes to the governance " +
                                                    "engines' configuration",
                                            "This problem may be caused because the engine host services has been configured with the wrong location for the " +
                                                    "metadata server, or the metadata server is not running the Governance Engine OMAS service or " +
                                                    "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                                    "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                                    "refresh-config command or wait for the engine host services to retry the configuration request."),

    /**
     * ENGINE-HOST-SERVICES-400-022 - Properties for governance engine called {0} have not been returned by open metadata server {1}.
     * Exception {2} with message {3} returned to server {4}
     */
    UNKNOWN_GOVERNANCE_ENGINE_CONFIG_AT_STARTUP(400, "ENGINE-HOST-SERVICES-400-022",
                                               "Properties for governance engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                                       "with message {3} returned to server {4}",
                                               "The engine host services is not able to initialize the governance engine and so it will not be able to support governance " +
                                                       "requests targeted to this governance engine.  ",
                                               "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                                       "configuration of the engine host services.  Once the cause is resolved, restart the server."),


    /**
     * ENGINE-HOST-SERVICES-400-023 - Properties for governance engine called {0} have not been returned by open metadata server {1} to engine host services in server {2}
     */
    UNKNOWN_GOVERNANCE_ENGINE_CONFIG(400, "ENGINE-HOST-SERVICES-400-023",
                                    "Properties for governance engine called {0} have not been returned by open metadata server {1} to engine host services in server {2}",
                                    "The engine host server is still not able to initialize the governance engine and so it will not be able to support governance " +
                                            "requests targeted to this governance engine.",
                                    "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                            "configuration of the engine host.  Once the cause is resolved, restart the server."),

    /**
     * ENGINE-HOST-SERVICES-400-024 - Governance engine {0} defined in open metadata server {1} is of type {2} rather than {3}; engine host server {4} is not able to run requests for this governance engine
     */
    WRONG_TYPE_OF_GOVERNANCE_ENGINE(400, "ENGINE-HOST-SERVICES-400-024",
                                     "Governance engine {0} defined in open metadata server {1} is of type {2} rather than {3}; engine host server {4} is not able to run requests for this governance engine",
                                     "The governance engine has been associated with the wrong type of Open Metadata Engine Services (OMES) and so it will not de able to support governance " +
                                             "requests targeted to this governance engine.",
                                     "This is a configuration error.  Update the configuration for the engine host service to ensure governance engines are correctly " +
                                             "matched to the engine services.  Once the cause is resolved, restart the server."),

    /**
     * ENGINE-HOST-SERVICES-400-102 - {0} in server {1} is not configured with the platform URL root for the {2}
     */
    NO_OMAS_SERVER_URL(400,"ENGINE-HOST-SERVICES-400-102",
                       "{0} in server {1} is not configured with the platform URL root for the {2}",
                       "The engine service is not able to locate the metadata server to retrieve the configuration for " +
                               "its governance engines.  The Engine Host server fails to start.",
                       "To be successful the engine service needs both the platform URL root and the name of the metadata " +
                               "server as well as the list of engines it is to host. Add the " +
                               "configuration for the platform URL root to this server's configuration document and check that the " +
                               "other required configuration properties are in place. Then restart this server."),

    /**
     * ENGINE-HOST-SERVICES-400-103 - {0} in server {1} is not configured with the name for the server running the {2}
     */
    NO_OMAS_SERVER_NAME(400, "ENGINE-HOST-SERVICES-400-103",
                        "{0} in server {1} is not configured with the name for the server running the {2}",
                        "The server is not able to retrieve its configuration from the metadata server.  It fails to start.",
                        "Add the configuration for the metadata server name to this server's configuration document.  " +
                                "Ensure that the platform URL root points to the platform where the metadata server is running and that" +
                                "there is at least one engine listed.  Once the configuration document is set up correctly,  " +
                                "restart this server."),

    /**
     * ENGINE-HOST-SERVICES-400-104 - {0} in server {1} is not configured with any engines
     */
    NO_ENGINES(400, "ENGINE-HOST-SERVICES-400-104",
               "{0} in server {1} is not configured with any engines",
               "The server is not able to run any services in this engine service.  The engine service fails to start which causes " +
                       "the server to fail too.",
               "Add the qualified name for at least one engine to the engine service in this server's configuration document " +
                       "and then restart the server."),
 ;


    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for EngineHostServicesErrorCode expects to be passed one of the enumeration rows defined in
     * EngineHostServicesErrorCode above.   For example:
     * <br><br>
     *     EngineHostServicesErrorCode   errorCode = EngineHostServicesErrorCode.UNKNOWN_ENDPOINT;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    EngineHostServicesErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "EngineHostServicesErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
