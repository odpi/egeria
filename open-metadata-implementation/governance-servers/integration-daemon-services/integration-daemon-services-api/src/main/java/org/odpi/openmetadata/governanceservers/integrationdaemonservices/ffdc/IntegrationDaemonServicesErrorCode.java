/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The IntegrationDaemonServicesErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Integration Daemon Services.  It is used in conjunction with all exceptions,
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
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum IntegrationDaemonServicesErrorCode implements ExceptionMessageSet
{
    /**
     * INTEGRATION-DAEMON-SERVICES-400-001 - Integration daemon {0} has been passed a null configuration document section for the integration daemon services
     */
    NO_CONFIG_DOC(400,"INTEGRATION-DAEMON-SERVICES-400-001",
                  "Integration daemon {0} has been passed a null configuration document section for the integration daemon services",
                  "The integration daemon services can not retrieve any configuration values.  " +
                          "The integration daemon fails to start because it would be bored with nothing to do.",
                  "Add the configuration for at least one integration service or integration group to this integration daemon's configuration document and then restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-003 - Integration service {0} is not configured with the platform URL root for the {1}
     */
    NO_OMAS_SERVER_URL(400,"INTEGRATION-DAEMON-SERVICES-400-003",
                       "Integration service {0} is not configured with the platform URL root for the {1}",
                       "The integration service is not able to locate the server where its partner OMAS is running in order " +
                               "to exchange metadata.  The integration daemon server fails to start.",
                       "To be successful each integration service needs both the platform URL root and the name of the " +
                               "server there the OMAS is running as well as the list of connections for the connectors it is to manage. Add this " +
                               "configuration to the integration daemon's configuration document and check that the " +
                               "other required configuration properties are in place. Then restart the integration daemon server."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-004 - Integration service {0} is not configured with the name for the server running the {1}
     */
    NO_OMAS_SERVER_NAME(400, "INTEGRATION-DAEMON-SERVICES-400-004",
                        "Integration service {0} is not configured with the name for the server running the {1}",
                        "The integration service is not able to locate the metadata server where its partner OMAS is running in order " +
                                "to exchange metadata.  The integration daemon fails to start.",
                        "Add the configuration for the server name for this integration service to the integration daemon's " +
                                "configuration document.  " +
                                "Ensure that the platform URL root points to the platform where the metadata server is running and that" +
                                "there is at least one connection for an integration connector listed.  Once the configuration document is set up " +
                                "correctly, restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-006 - The integration service {0} has been configured with a context manager class of {1} which can not be
     * used by the class loader.  The {2} exception was returned with message {3}
     */
    INVALID_CONTEXT_MANAGER(400, "INTEGRATION-DAEMON-SERVICES-400-006",
                            "The integration service {0} has been configured with a context manager class of {1} which can not be " +
                                    "used by the class loader.  The {2} exception was returned with message {3}",
                            "The integration service fails to start.  Its connectors, if any, are not activated.",
                            "Check that the jar for the context manager's class is visible to the OMAG Server Platform through " +
                                    "the class path - and that the class name specified includes the full, correct package name and class name.  " +
                                    "Once the class is correctly set up, restart the integration daemon.  It will be necessary to restart the " +
                                    "OMAG Server Platform if the class path needed adjustment. "),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-007 - Integration daemon {0} is not configured with any integration connectors
     */
    NO_INTEGRATION_CONNECTORS_CONFIGURED(400,"INTEGRATION-DAEMON-SERVICES-400-007",
                                       "Integration daemon {0} is not configured with any integration connectors",
                                       "Since none of the configured integration services have any integration connectors " +
                                                 "configured, the integration daemon, fails to start because it would be bored " +
                                               "with nothing to do.",
                                       "Add the configuration for at least one integration connector to one of the integration services " +
                                                 "in this integration daemon's configuration document and then restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-008 - The integration daemon services are unable to initialize a new instance of integration daemon {0}; error message is {1}
     */
    INTEGRATION_DAEMON_INSTANCE_FAILURE(400, "INTEGRATION-DAEMON-SERVICES-400-008",
                         "The integration daemon services are unable to initialize a new instance of integration daemon {0}; error message is {1}",
                         "The integration daemon services detected an error during the start up of a specific integration daemon instance.  " +
                                              "Its integration services are not available.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-009 - The integration daemon services are unable to initialize a new instance of integration daemon {0};
     * error message is {1}
     */
    SERVICE_INSTANCE_FAILURE(400, "INTEGRATION-DAEMON-SERVICES-400-009",
                             "The integration daemon services are unable to initialize a new instance of integration daemon {0}; " +
                                     "error message is {1}",
                             "The integration daemon services detected an error during the start up of a specific integration daemon instance.  " +
                                     "No integration services are running in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-010 - The integration service URL marker {0} is not recognized.  Valid service URL markers are: {1}
     */
    UNRECOGNIZED_SERVICE(400, "INTEGRATION-DAEMON-SERVICES-400-010",
                             "The integration service URL marker {0} is not recognized.  Valid service URL markers are: {1}",
                             "The request fails and returns this exception.  No action is taken by the integration daemon.",
                             "Correct the supplied URL marker to one that is valid.  The admin services has a command to list the " +
                                 "integration services configured for this integration daemon."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-011 - The {0} configuration property for integration service {1} in integration daemon {2} is null
     */
    NULL_SERVICE_CONFIG_VALUE(400, "INTEGRATION-DAEMON-SERVICES-400-011",
                         "The {0} configuration property for integration service {1} in integration daemon {2} is null",
                         "The integration service fails to start and this causes the hosting integration daemon to fail.",
                         "Add a suitable value for this configuration property in the integration service configuration."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-012 - The integration service {0} in integration daemon {1} does not have a default permitted synchronization value set
     */
    NO_PERMITTED_SYNCHRONIZATION( 400,"INTEGRATION-DAEMON-SERVICES-400-012",
                                 "The integration service {0} in integration daemon {1} does not have a default permitted synchronization value set",
                                 "The integration daemon is not able to initialize one of the configured integration because its defaultPermittedSynchronization value is null.  " +
                                 "The integration daemon shuts down, this error is reported to the caller and a similar message is written to the audit log.",
                                  "Update the configuration for the integration service to include a value for the default permitted synchronization."),

    /*
     * Errors when running requests
     */

    /**
     * INTEGRATION-DAEMON-SERVICES-400-030 - Integration service with URL marker {0} is not registered in the integration daemon {1}
     */
    UNKNOWN_INTEGRATION_SERVICE(400, "INTEGRATION-DAEMON-SERVICES-400-030",
                             "Integration service with URL marker {0} is not registered in the integration daemon {1}",
                             "The integration service specified on a request is not known to the integration daemon.",
                             "This may be a configuration error in the integration daemon or an error in the caller.  " +
                                     "The supported integration services are listed in the integration daemon's configuration.  " +
                                     "Check the configuration document for the daemon and then its start up messages to ensure the correct " +
                                     "integration services are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the integration daemon is running the correct integration services then validate that " +
                                     "the caller has passed the correct URL marker of the integration service to the integration daemon." +
                                     "If all of this is correct then it may be a code error in the integration daemon services and you need to " +
                                     "raise an issue to get it fixed.  Once the cause is resolved, retry the request."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-031 - Integration connector named {0} is not running in the integration daemon {1}
     */
    UNKNOWN_CONNECTOR_NAME(400, "INTEGRATION-DAEMON-SERVICES-400-031",
                                "Integration connector named {0} is not running in the integration daemon {1}",
                                "The integration connector specified on a request is not known to the integration daemon.",
                                "This may be a configuration error in the integration daemon or an error in the caller.  " +
                                        "The supported integration connectors may be listed in the integration service's configuration" +
                                        "or be part of one of the configured integration groups.  " +
                                        "Check the configuration document for the daemon and then its start up messages to ensure the correct " +
                                        "integration services and connectors are started successfully.  " +
                                        "Look for other error messages that indicate that an error occurred during " +
                                        "start up.  If the integration daemon is running the correct integration services then validate that " +
                                        "the caller has passed matching connector name and URL marker of the integration service to the " +
                                        "integration daemon." +
                                        "If all of this is correct then it may be a code error in the integration daemon services and you need to " +
                                        "raise an issue to get it fixed.  Once the cause is resolved, retry the request."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-032 - Properties for integration group called {0} have not been returned by open metadata server {1}
     * to the integration daemon services in server {2}
     */
    UNKNOWN_INTEGRATION_GROUP_CONFIG(400, "INTEGRATION-DAEMON-SERVICES-400-032",
                                     "Properties for integration group called {0} have not been returned by open metadata server {1} to the integration" +
                                             " daemon services in server {2}",
                                     "The integration daemon is still not able to initialize the integration group and so it will not be " +
                                             "able to support any integration connectors for this group.",
                                     "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                             "configuration of the integration daemon.  If the name of the group needs to change, restart the " +
                                             "server."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-033 - Integration daemon {0} is unable to process connectors from integration group {1} because it is
     * unable to retrieve its configuration from the metadata access server
     */
    INTEGRATION_GROUP_NOT_INITIALIZED(400,"INTEGRATION-DAEMON-SERVICES-400-033",
                                      "Integration daemon {0} is unable to process connectors from integration group {1} because it is " +
                                              "unable to retrieve its configuration from the metadata access server",
                                      "The integration daemon is not able to run any integration connector from this group until it is able to " +
                                              "retrieve the group's configuration.",
                                      "Use the configuration interface of the Governance Engine OMAS to create a definition of the integration" +
                                              " group."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-034 - Integration group named {0} is not running in the integration daemon {1}
     */
    UNKNOWN_GROUP_NAME(400, "INTEGRATION-DAEMON-SERVICES-400-034",
                       "Integration group named {0} is not running in the integration daemon {1}",
                       "The integration group specified on a request is not known to the integration daemon.",
                       "This may be a configuration error in the integration daemon or an error in the caller.  " +
                                "The supported integration groups are listed in the integration daemon's configuration.  " +
                                "Check the configuration document for the daemon and then its start up messages to ensure the correct " +
                                "integration groups are started successfully.  " +
                                "Look for other error messages that indicate that an error occurred during " +
                                "start up.  If the integration daemon is running the correct integration groups then validate that " +
                                "the caller has passed the correct name." +
                                "If all of this is correct then it may be a code error in the integration daemon services and you need to " +
                                "raise an issue to get it fixed.  Once the cause is resolved, retry the request."),

    /**
     * INTEGRATION-DAEMON-SERVICES-400-035 - No integration groups are running in the integration daemon {0}
     */
    NO_INTEGRATION_GROUPS(400, "INTEGRATION-DAEMON-SERVICES-400-035",
                          "No integration groups are running in the integration daemon {0}",
                          "The call to the integration daemon fails and an exception is returned to the caller.",
                          "This is either a configuration error or a logic error.  If this is a configuration error, add the required integration " +
                                  "groups to the configuration document.  " +
                                  "If there are no errors in the configuration, raise an issue to get help to fix this."),


    /*
     * Internal logic errors
     */

    /**
     * INTEGRATION-DAEMON-SERVICES-500-001 - No integration services are running in the integration daemon
     */
    NO_INTEGRATION_SERVICES(500, "INTEGRATION-DAEMON-SERVICES-500-001",
                            "No integration services are running in the integration daemon",
                            "The call to the integration daemon fails and an exception is returned to the caller.",
                            "This is either a configuration error or a logic error.  If this is a configuration error, the" +
                                    "integration daemon will have logged detailed messages to the audit log to describe what is wrong " +
                                    "and how to fix it.  " +
                                    "If there are no errors in the configuration, raise an issue to get help to fix this."),

    /**
     * INTEGRATION-DAEMON-SERVICES-500-002 - The integration service {0} has been registered with this platform with a null context manager class
     */
    NULL_CONTEXT_MANAGER(500, "INTEGRATION-DAEMON-SERVICES-500-002",
                         "The integration service {0} has been registered with this platform with a null context manager class",
                         "The integration service is not able to initialize any integration connectors.",
                         "Integration services register themselves as they are loaded by Springs component scan mechanism.  " +
                                 "The implementation of the integration service needs to be updated to that it registers correctly with the " +
                                 "platform."),
    ;


    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for IntegrationDaemonServicesErrorCode expects to be passed one of the enumeration rows defined in
     * IntegrationDaemonServicesErrorCode above.   For example:
     * <br><br>
     *     IntegrationDaemonServicesErrorCode   errorCode = IntegrationDaemonServicesErrorCode.UNKNOWN_ENDPOINT;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    IntegrationDaemonServicesErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "IntegrationDaemonServicesErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
