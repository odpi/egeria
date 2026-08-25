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
                          "without this section of the configuration document filled in.  Raise an issue to get this fixed.",
                          "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-002 - Engine host {0} is not configured with any engine services
     */
    NO_ENGINE_SERVICES_CONFIGURED(400,"ENGINE-HOST-SERVICES-400-002",
                                       "Engine host {0} is not configured with any engine services",
                                       "The engine host, fails to start because it would be bored with nothing to do.",
                                       "Add the configuration for at least one engine service to the engine services' section " +
                                               "of this engine host's configuration document and then restart the engine host server.",
                                               "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-003 - The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2}
     */
    SERVICE_INSTANCE_FAILURE(400, "ENGINE-HOST-SERVICES-400-003",
                             "The engine host services are unable to initialize a new instance of engine host {0}; " +
                                     "exception {1} with message {2}",
                             "The engine host services detected an error during the start up of a specific engine host instance.  " +
                                     "No engine services are running in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the engine host.",
                                     "https://egeria-project.org/services/engine-host-services/"),

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
                                   "raise an issue to get it fixed.  Once the cause is resolved, retry the request.",
                                   "https://egeria-project.org/services/engine-host-services/"),

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
                                "raise an issue to get it fixed.  Once the cause is resolved, retry the request.",
                                "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-006 - No governance engines are running in the engine host {0}
     */
    NO_GOVERNANCE_ENGINES(400, "ENGINE-HOST-SERVICES-400-006",
                          "No governance engines are running in the engine host {0}",
                          "The call to the engine host fails and an exception is returned to the caller.",
                          "This is either a configuration error or a logic error.  If this is a configuration error, the" +
                                  "engine host will have logged detailed messages to the audit log to describe what is wrong " +
                                  "and how to fix it.  " +
                                  "If there are no errors in the configuration, raise an issue to get help to fix this.",
                                  "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-007 - No governance engines are running in the engine service {0} on engine host {1}
     */
    NO_ENGINES_FOR_SERVICE(400, "ENGINE-HOST-SERVICES-400-007",
                          "No governance engines are running in the engine service {0} on engine host {1}",
                          "The call to the engine service fails and an exception is returned to the caller.",
                          "This is either a configuration error or a logic error.  If this is a configuration error, the" +
                                  "engine host will have logged detailed messages to the audit log when it was initializing the engine service " +
                                  "to describe what is wrong and how to fix it.  " +
                                  "If there are no errors in the configuration, raise an issue to get help to fix this.",
                                  "https://egeria-project.org/services/engine-host-services/"),

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
                                            "the platform-chassis-spring module has access to the engine service's spring module.",
                                            "https://egeria-project.org/services/engine-host-services/"),

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
                                           "OMAG Server Platform if the class path needed adjustment. ",
                                           "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-016 - Method {0} can not execute in the governance engine {1} hosted by engine host server {2} because the associated governance service properties are invalid: {3}
     */
    NULL_GOVERNANCE_SERVICE(400, "ENGINE-HOST-SERVICES-400-016",
                            "Method {0} can not execute in the governance engine {1} hosted by engine host server {2} because the associated " +
                                    "governance service properties are invalid: {3}",
                            "The governance request is not run and an error is returned to the caller.",
                            "This may be an error in the governance engine's logic or the Governance Engine OMAS may have returned " +
                                    "invalid configuration.  Raise an issue to get help to fix it",
                                    "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-017 - Engine host server {0} cannot pass a governance request to governance engine {1} because this
     * governance engine has not retrieved its configuration from the metadata access server
     */
    GOVERNANCE_ENGINE_NOT_INITIALIZED(400,"ENGINE-HOST-SERVICES-400-017",
                                      "Engine host server {0} cannot pass a governance request to governance engine {1} because this governance engine has not " +
                                              "retrieved its configuration from the metadata access server",
                                      "The governance engine is not able to run any governance requests until it is able to retrieve its configuration.",
                                      "Use the configuration interface of the Governance Engine OMAS to create a definition of at least one governance" +
                                              " engine.",
                                              "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-019 - The configuration document for engine {0} configuration property for engine service {1} in engine host {2} is null
     */
    NULL_SERVICE_CONFIG_VALUE(400, "ENGINE-HOST-SERVICES-400-019",
                              "The configuration document for engine {0} configuration property for engine service {1} in engine host {2} is null",
                              "The engine service fails to start and this causes the hosting engine host to fail.",
                              "Add a suitable value for this configuration property in the engine service configuration.",
                              "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-020 - The engine host services in engine host server {0} are unable to initialize a new instance of engine service {1}; exception {2} with message {3}
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION(400, "ENGINE-HOST-SERVICES-400-020",
                                        "The engine host services in engine host server {0} are unable to initialize a new instance of engine service {1}; exception {2} with message {3}",
                                        "The engine service detected an error during the start up of a specific governance engine instance.  " +
                                                "Its governance services are not available.",
                                        "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                                "Once this is resolved, restart the server.",
                                                "https://egeria-project.org/services/engine-host-services/"),

   /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the server has retrieved the
     * configuration, the governance engines will operate successfully.
     */

    /**
     * ENGINE-HOST-SERVICES-400-023 - Properties for governance engine called {0} have not been returned by open metadata server {1} to engine host services in server {2}
     */
    UNKNOWN_GOVERNANCE_ENGINE_CONFIG(400, "ENGINE-HOST-SERVICES-400-023",
                                    "Properties for governance engine called {0} have not been returned by open metadata server {1} to engine host services in server {2}",
                                    "The engine host server is still not able to initialize the governance engine and so it will not be able to support governance " +
                                            "requests targeted to this governance engine.",
                                    "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                            "configuration of the engine host.  Once the cause is resolved, restart the server.",
                                            "https://egeria-project.org/services/engine-host-services/"),

    /**
     * ENGINE-HOST-SERVICES-400-024 - Governance engine {0} defined in open metadata server {1} is of type {2} rather than {3}; engine host server {4} is not able to run requests for this governance engine
     */
    WRONG_TYPE_OF_GOVERNANCE_ENGINE(400, "ENGINE-HOST-SERVICES-400-024",
                                     "Governance engine {0} defined in open metadata server {1} is of type {2} rather than {3}; engine host server {4} is not able to run requests for this governance engine",
                                     "The governance engine has been associated with the wrong type of Open Metadata Engine Services (OMES) and so it will not de able to support governance " +
                                             "requests targeted to this governance engine.",
                                     "This is a configuration error.  Update the configuration for the engine host service to ensure governance engines are correctly " +
                                             "matched to the engine services.  Once the cause is resolved, restart the server.",
                                             "https://egeria-project.org/services/engine-host-services/"),

    ;


    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;
    private final String url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    EngineHostServicesErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    EngineHostServicesErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                              userAction,
                                              url);
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
                                                                                      userAction,
                                                                                      url);

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
                       ", url='" + url + '\'' +
                       '}';
    }
}
