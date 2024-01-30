/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OMAGAdminErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMAG Server
 * It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>501 - not implemented </li>
 *         <li>503 - service not available</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - unauthorized</li>
 *         <li>404 - not found</li>
 *         <li>405 - method not allowed</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum OMAGAdminErrorCode implements ExceptionMessageSet
{
    /**
     * OMAG-ADMIN-400-001 - OMAG server has been called with a null local server name
     */
    NULL_LOCAL_SERVER_NAME(400, "OMAG-ADMIN-400-001",
            "OMAG server has been called with a null local server name",
            "The system is unable to configure the local server.",
            "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMAG-ADMIN-400-002 - OMAG Server Platform was requested to start OMAG Server {0} but the configuration document retrieved for it
     * has the server name set to {1}
     */
    INCOMPATIBLE_SERVER_NAMES(400, "OMAG-ADMIN-400-002",
            "OMAG Server Platform was requested to start OMAG Server {0} but the configuration document retrieved for it " +
                    "has the server name set to {1}",
            "The system is unable to start the OMAG server because it can not retrieve the correct configuration document.",
            "The configuration is retrieved from the configuration document store connector.  " +
                                      "This connector is set up for the OMAG Server Platform.  " +
                                      "It is either not configured correctly, or there is an error in its " +
                                      "implementation because it is not retrieving the correct configuration document for" +
                                      "the requested server."),

    /**
     * OMAG-ADMIN-400-003 - OMAG server {0} has been configured with a null local server user identifier (userId)
     */
    NULL_LOCAL_SERVER_USERID(400, "OMAG-ADMIN-400-003",
                   "OMAG server {0} has been configured with a null local server user identifier (userId)",
                   "The system fails to start the server because this user identifier is needed for processing events " +
                                     "from external topics.  If the server continues to operate, it will not be able to function correctly.",
                   "The local server's user identifier is supplied in the configuration document for the OMAG server.  " +
                                     "This configuration needs to be corrected before the server can operate correctly.  " +
                                     "Once the configuration document has been corrected, restart the server."),

    /**
     * OMAG-ADMIN-400-004 - A REST API call to OMAG server {0} has been made with a null user identifier (userId)
     */
    NULL_USER_NAME(400, "OMAG-ADMIN-400-004",
                   "A REST API call to OMAG server {0} has been made with a null user identifier (userId)",
                   "The server rejects the request.",
                   "The user name is supplied in a parameter (typically called userID) in the call to the OMAG server. " +
                           "This parameter needs to be changes to a valid user identifier before the request can operate correctly."),

    /**
     * OMAG-ADMIN-400-005 - Unable to configure server {0} since access service {1} is not registered in this OMAG Server Platform
     */
    ACCESS_SERVICE_NOT_RECOGNIZED(400, "OMAG-ADMIN-400-005",
            "Unable to configure server {0} since access service {1} is not registered in this OMAG Server Platform",
            "The system is unable to add this access service to the server's configuration document.",
            "Check that the name of the access service is correctly specified in the configuration request.  " +
                                          "If you are not sure, issue the call to list the registered access services and verify the " +
                                          "values you are using.  If the name is right but the access service should be registered," +
                                          "then the developer of the access service needs to add this registration to the code of the access " +
                                          "service. An access service is registered in the " +
                                          "OMAG Server Platform by adding a description of the access service to the " +
                                          "access service registration (look for OMAGAccessServiceRegistration.registerAccessService() in " +
                                          "existing access service modules to see this code pattern). Once the access service being " +
                                          "requested is registered, retry the configuration request."),

    /**
     * OMAG-ADMIN-400-006 - Unable to configure server {0} since access service {1} is not enabled in this OMAG Server Platform
     */
    ACCESS_SERVICE_NOT_ENABLED(400, "OMAG-ADMIN-400-006",
            "Unable to configure server {0} since access service {1} is not enabled in this OMAG Server Platform",
            "The system is unable to configure the local server with this access service.",
            "Choose a different access service or enable the access service in this platform."),

    /**
     * OMAG-ADMIN-400-007 - OMAG server {0} has been configured with a null cohort name
     */
    NULL_COHORT_NAME(400, "OMAG-ADMIN-400-007",
            "OMAG server {0} has been configured with a null cohort name",
            "The system is unable to configure the local server with access to this cohort.",
            "The cohort name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can be configured to " +
                             "register with the cohort."),

    /**
     * OMAG-ADMIN-400-008 - The local repository mode has not been set for OMAG server {0}
     */
    LOCAL_REPOSITORY_MODE_NOT_SET(400, "OMAG-ADMIN-400-008",
            "The local repository mode has not been set for OMAG server {0}",
            "The local repository mode must be enabled before the event mapper connection, local metadata collection id or local metadata collection name is set.",
            "Set up a local repository for this server, then rerun the failing request."),

    /**
     * OMAG-ADMIN-400-009 - The OMAG server {0} has been passed null configuration
     */
    NULL_SERVER_CONFIG(400, "OMAG-ADMIN-400-009",
            "The OMAG server {0} has been passed null configuration",
            "The system is unable to initialize the local server instance without any configuration.",
            "Retry the request with server configuration."),

    /**
     * OMAG-ADMIN-400-010 - The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration
     */
    NULL_REPOSITORY_CONFIG(400, "OMAG-ADMIN-400-010",
            "The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration",
            "The system is unable to initialize the local server instance because all servers need at least an audit log which is supported by the " +
                                   "repository services.",
            "Use the administration services to add the repository services configuration."),


    /**
     * OMAG-ADMIN-400-011 No configuration document was found for OMAG server {0}
     */
    NO_CONFIG_DOCUMENT(400, "OMAG-ADMIN-400-011",
                           "No configuration document was found for OMAG server {0}",
                           "The system is unable to initialize the local server instance without a configuration document.",
                           "Use the administration services to build up the definition of the server into a configuration document."),

    /**
     * OMAG-ADMIN-400-012 Unable to parse configuration document for OMAG server {0} due to exception {1} with message {2}
     */
    CONFIG_DOCUMENT_PARSE_ERROR(400, "OMAG-ADMIN-400-012",
                       "Unable to parse configuration document for OMAG server {0} due to exception {1} with message {2}",
                       "The system is unable to process a configuration document.",
                       "Review the error message to understand why the parsing error occurred."),


    /**
     * OMAG-ADMIN-400-013 - The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2} which included a message {3}
     */
    BAD_CONFIG_FILE(400, "OMAG-ADMIN-400-013",
            "The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2} which included a message {3}",
            "The system is unable to initialize the server.",
            "Review the error message to determine the cause of the problem."),

    /**
     * OMAG-ADMIN-400-014 - The OMAG server {0} has been passed an invalid maximum page size of {1}
     */
    BAD_MAX_PAGE_SIZE(400, "OMAG-ADMIN-400-014",
            "The OMAG server {0} has been passed an invalid maximum page size of {1}",
            "The server failed to start.",
            "The maximum page size sets an upper limit on how many results a caller can request on a paged REST API call.  " +
                              "If it is set to zero then it means there is no limit, a positive number is the maximum paging size allowed.  " +
                              "Set the maximum page size in the configuration document to an appropriate value and restart the server."),


    /**
     * OMAG-ADMIN-400-016 - The OMAG server {0} is unable to set up new event bus configuration because other services are already configured
     */
    TOO_LATE_TO_SET_EVENT_BUS(400, "OMAG-ADMIN-400-016",
            "The OMAG server {0} is unable to set up new event bus configuration because other services are already configured",
            "It is not possible to change the event bus configuration for this server while there are other open metadata services configured.",
            "Remove any configuration for this server's cohorts, local repository and access services, " +
                                      "and retry the request to add the event bus configuration.  " +
                                      "Then it is possible to add the configuration for the other services back " +
                                      "into the configuration document."),

    /**
     * OMAG-ADMIN-400-017 - The OMAG server {0} is unable to add open metadata services until the event bus is configured
     */
    NO_EVENT_BUS_SET(400, "OMAG-ADMIN-400-017",
            "The OMAG server {0} is unable to add open metadata services until the event bus is configured",
            "No change has occurred in this server's configuration document.",
            "Add the event bus configuration using the administration services and retry the request."),

    /**
     * OMAG-ADMIN-400-018 - OMAG server {0} has been called with a null metadata collection name
     */
    NULL_METADATA_COLLECTION_NAME(400, "OMAG-ADMIN-400-018",
            "OMAG server {0} has been called with a null metadata collection name",
            "The system is unable to add this metadata collection name to the configuration document for the local server.",
            "The metadata collection name is optional.  If it is not set up then the local server name is used instead."),

    /**
     * OMAG-ADMIN-400-019 - OMAG server {0} has been called with a configuration document that has no services configured
     */
    EMPTY_CONFIGURATION(400, "OMAG-ADMIN-400-019",
            "OMAG server {0} has been called with a configuration document that has no services configured",
            "The requested server provides no function.",
            "Use the administration services to add configuration for OMAG services to the server's configuration document."),

    /**
     * OMAG-ADMIN-400-020 - The {0} service of OMAG server {1} has been configured with a null root URL for the remote {2} access service
     */
    NULL_ACCESS_SERVICE_ROOT_URL(400, "OMAG-ADMIN-400-020",
            "The {0} service of OMAG server {1} has been configured with a null root URL for the remote {2} access service",
            "The system is unable to accept this value in the configuration document because it needs this value to be able to call the correct " +
                                         "server platform where the access service is running.",
            "The root URL is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMAG-ADMIN-400-021 - The {0} service of OMAG server {1} has been configured with a null server name for the remote {2} access service
     */
    NULL_ACCESS_SERVICE_SERVER_NAME(400, "OMAG-ADMIN-400-021",
            "The {0} service of OMAG server {1} has been configured with a null server name for the remote {2} access service",
            "The system is unable to accept this value in the configuration document because it needs this value to be able to call the correct " +
                                            "server where the access service is running.",
            "The server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMAG-ADMIN-400-022 - OMAG server {0} has been configured with a null file name for an Open Metadata Archive
     */
    NULL_FILE_NAME(400, "OMAG-ADMIN-400-022",
            "OMAG server {0} has been configured with a null file name for an Open Metadata Archive",
            "The system is unable to configure the local server to load this Open Metadata Archive file.",
            "The file name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can load the open metadata archive."),

    /**
     * OMAG-ADMIN-400-023 - The configuration document for OMAG server {0} is at version {1} which is not compatible with this OMAG Server Platform which supports versions {2}
     */
    INCOMPATIBLE_CONFIG_FILE(400, "OMAG-ADMIN-400-023",
            "The configuration document for OMAG server {0} is at version {1} which is not compatible with this OMAG Server Platform which supports versions {2}",
            "The system is unable to configure the local server because it can not read the configuration document.",
            "Migrate the configuration document to a compatible version (or delete and recreate it).  See https://egeria-project.org/guides/migration/migrating-configuration-documents/"),

    /**
     * OMAG-ADMIN-400-024 - The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property.  The resulting {3} exception included the following message: {4}
     */
    BAD_CONFIG_PROPERTIES(400, "OMAG-ADMIN-400-024",
            "The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property.  The resulting {3} exception included the following message: {4}",
            "The access service has not been passed valid configuration .",
            "Correct the value of the failing configuration property and restart the server."),

    /**
     * OMAG-ADMIN-400-025 - The {0} Open Metadata Access Service (OMAS) has been passed a null enterprise OMRS topic for server {1}
     */
    NO_ENTERPRISE_TOPIC(400, "OMAG-ADMIN-400-025",
            "The {0} Open Metadata Access Service (OMAS) has been passed a null enterprise OMRS topic for server {1}",
            "The access service has not been passed valid configuration for its enterprise repository services.   It needs this value to retrieve " +
                                "metadata from the open metadata repositories.",
            "Correct the configuration for the enterprise repository services and restart the server."),

    /**
     * OMAG-ADMIN-400-026 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error
     * message was {3}
     */
    BAD_TOPIC_CONNECTOR(400, "OMAG-ADMIN-400-026",
            "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error " +
                    "message was {3}",
            "The access service has not been passed valid configuration.  It needs the topic connector to send and receive events.",
            "Correct the configuration for the topic connector and restart the server."),

    /**
     * OMAG-ADMIN-400-027 - OMAG server {0} has been called with a null connection for method {1}
     */
    NULL_CONNECTION(400, "OMAG-ADMIN-400-027",
            "OMAG server {0} has been called with a null connection for method {1}",
            "The system is unable to add this connection to the server's configuration document.",
            "Change the call to pass a valid connection for the server.  If you want to clear the connection use the clear version of the method."),

    /**
     * OMAG-ADMIN-400-028 - The OMAG Server Platform has been called with a null connection for method {0}
     */
    NULL_PLATFORM_CONNECTION(400, "OMAG-ADMIN-400-028",
            "The OMAG Server Platform has been called with a null connection for method {0}",
            "The admin services is unable to add this connection to the platform runtime.",
            "Change the call to pass a valid connection for the platform.  If you want to clear the connection use the clear version of the method."),


    /**
     * OMAG-ADMIN-400-031 - The configuration document for server {0} includes configuration for a {1} but also has configuration for the {2}
     * subsystem which is not a compatible combination
     */
    INCOMPATIBLE_SUBSYSTEMS(400, "OMAG-ADMIN-400-031",
            "The configuration document for server {0} includes configuration for a {1} but also has configuration for the {2} subsystem which " +
                    "is not a compatible combination",
            "The server fails to initialize and an exception is returned to the caller.",
            "Reconfigure the server to include a compatible combination of subsystems."),

    /**
     * OMAG-ADMIN-400-032 - The supplied configuration for server {0} was not accepted because there is no value provided for property {1}
     */
    MISSING_CONFIGURATION_PROPERTY(400, "OMAG-ADMIN-400-032",
            "The supplied configuration for server {0} was not accepted because there is no value provided for property {1}",
            "The system returns an exception and does not update the configuration document for the server.",
            "Retry the configuration request with the property value set up correctly."),

    /**
     * OMAG-ADMIN-400-033 - The OMAG server {0} is unable to override the cohort topic until the {1} cohort is set up
     */
    COHORT_NOT_KNOWN(400, "OMAG-ADMIN-400-033",
            "The OMAG server {0} is unable to override the cohort topic until the {1} cohort is set up",
            "No change has occurred in this server's configuration document because the admin services .",
            "Add the cohort configuration using the administration services and retry the request."),

    /**
     * OMAG-ADMIN-400-034 - The OMAG server {0} is unable to override the cohort topic for the {1} cohort because the contents of the topic
     * connection do not follow the expected pattern
     */
    COHORT_TOPIC_STRANGE(400, "OMAG-ADMIN-400-034",
            "The OMAG server {0} is unable to override the cohort topic for the {1} cohort because the contents of the topic connection do not " +
                    "follow the expected pattern",
            "No change has occurred in this server's configuration document because the topic connection in the cohort configuration does not " +
                                 "follow the same structure as Egeria expects and so any update may have unexpected consequences.",
            "Use the setCohortConfig() method to manually update the cohort topic in the cohort configuration."),

    /**
     * OMAG-ADMIN-400-035 - Unable to classify the type of server for OMAG server {0} from its configuration document
     */
    UNCLASSIFIABLE_SERVER(400, "OMAG-ADMIN-400-035",
            "Unable to classify the type of server for OMAG server {0} from its configuration document",
            "The system is unable to initialize the local server instance.",
            "Analyse the server's configuration document to determine why the type of server it requests " +
                                  "is not identified.  Update the server's configuration document to provide " +
                                  "a valid server configuration."),

    /**
     * OMAG-ADMIN-400-036 - Unable to configure server {0} since view service {1} is not registered in this OMAG Server Platform
     */
    VIEW_SERVICE_NOT_RECOGNIZED(400, "OMAG-ADMIN-400-036",
                                "Unable to configure server {0} since view service {1} is not registered in this OMAG Server Platform",
                                "The system is unable to add this view service to the server's configuration document.",
                                "Check that the name of the view service is correctly specified in the configuration request.  " +
                                        "If you are not sure, issue the call to list the registered view services and verify the " +
                                        "values you are using.  If the name is right, but the view service should be registered," +
                                        "then the developer of the view service needs to add this registration to the code of the view " +
                                        "service. A view service is registered in the " +
                                        "OMAG Server Platform by adding a description of the view service to the " +
                                        "view service registration. Once the view service being " +
                                        "requested is registered, retry the configuration request."),

    /**
     * OMAG-ADMIN-400-037 - Unable to configure server {0} since view service {1} is not enabled in this OMAG Server Platform
     */
    VIEW_SERVICE_NOT_ENABLED(400, "OMAG-ADMIN-400-037",
                             "Unable to configure server {0} since view service {1} is not enabled in this OMAG Server Platform",
                             "The system is unable to configure the local server with this view service.",
                             "Validate and correct the name of the view service URL marker or enable the view service in this platform."),

    /**
     * OMAG-ADMIN-400-038 - OMAG server {0} has been called by {1} with a null client config
     */
    NULL_CLIENT_CONFIG(400, "OMAG-ADMIN-400-038",
                           "OMAG server {0} has been called by {1} with a null client config",
                           "The system is unable to configure the local server with the governance service because it needs to be able to call a " +
                               "metadata access point or metadata server.",
                           "The client config is supplied by the caller to the OMAG server. This call needs to be supplied, including the name and URL of the OMAG server, before the server can operate correctly."),

    /**
     * OMAG-ADMIN-400-039 - The {0} service of OMAG server {1} has been configured with a null root URL for its remote OMAG Server
     */
    NULL_OMAG_SERVER_ROOT_URL(400, "OMAG-ADMIN-400-039",
                                 "The {0} service of OMAG server {1} has been configured with a null root URL for its remote OMAG Server",
                                 "The system is unable to accept this value in the configuration document because the server would not be able to " +
                                      "operate correctly.",
                                 "The root URL is supplied by the caller to the OMAG server. This URL value needs to be corrected before the server" +
                                      " can operate correctly."),

    /**
     * OMAG-ADMIN-400-040 - The {0} service of server {1} has been configured with a null name for the remote server
     */
    NULL_OMAG_SERVER_NAME(400, "OMAG-ADMIN-400-040",
                            "The {0} service of server {1} has been configured with a null name for the remote server",
                            "The system is unable to accept a null value for this property in the configuration document because the server would not" +
                                  " be able to operate properly.",
                            "The OMAG Server name is supplied by the caller to the OMAG server. This remote server name needs to be corrected before " +
                                  "the server can operate correctly."),

    /**
     * OMAG-ADMIN-400-041 - The connection passed to the {0} method does not describe a valid connector.  Connection object is: {1}.
     * The resulting exception {2} had message of {3}, system action of {4} and user action of {5}
     */
    BAD_CONNECTION(400, "OMAG-ADMIN-400-041",
                          "The connection passed to the {0} method does not describe a valid connector.  Connection object is: {1}.  " +
                                  "The resulting exception {2} had message of {3}, system action of {4} and user action of {5}",
                          "The connection was tested by the Open Connector Framework (OCF) Connector Broker and it was unable to create a" +
                           "connector for this connection and returned a detailed exception. Because of this exception, the system is " +
                           "unable to accept an invalid connection object and so the request is rejected.  No change is made to the configuration.",
                          "Use the detail messages from the connector broker to work out what is wrong with the connection object.  " +
                           "Once the connection object is corrected, retry the request."),

    /**
     * OMAG-ADMIN-400-042 - The {0} Open Metadata View Service (OMVS) has been passed an invalid configuration of {1} in the {2} property
     */
    VIEW_SERVICE_CONFIG(400, "OMAG-ADMIN-400-042",
                          "The {0} Open Metadata View Service (OMVS) has been passed an invalid configuration of {1} in the {2} property",
                          "The view service has not been passed valid configuration.",
                          "Check whether the view service expects SolutionViewServiceConfiguration or IntegrationViewServiceConfiguration, correct the configuration and restart the server."),

    /**
     * OMAG-ADMIN-400-043 - A retrieve all configurations has been attempted, but operation is not supported by the configuration store connector
     */
    RETRIEVE_ALL_CONFIGS_NOT_SUPPORTED(400, "OMAG-ADMIN-400-043",
                        "A retrieve all configurations has been attempted, but operation is not supported by the configuration store connector",
                        "The retrieve all server configurations operation is rejected.",
                        "Check whether OMAG Server configuration connector supports retrieve all configurations."),

    /**
     * OMAG-ADMIN-400-044 - User {0} has attempted to obtain a server config store to be able to retrieve the OMAG server stored configurations but an error occurred
     */
    UNABLE_TO_OBTAIN_SERVER_CONFIG_STORE(400, "OMAG-ADMIN-400-044",
                                         "User {0} has attempted to obtain a server config store to be able to retrieve the OMAG server stored configurations but an error occurred",
                                         "The retrieve all server configurations operation is rejected, as the OMAG Server Configuration store could not be obtained.",
                                         "Check that the OMAG Server configuration connector has been specified correctly."),

    /**
     * OMAG-ADMIN-400-045 - The {0} service of server {1} has been configured with a null service URL marker
     */
    NULL_SERVICE_URL_MARKER(400, "OMAG-ADMIN-400-045",
                            "The {0} service of server {1} has been configured with a null service URL marker",
                            "The system is unable to accept a null value for this property because the admin services " +
                                    "is not able determine which service to configure.",
                            "The service URL marker is passed as a parameter of the request.  The valid URL markers can be " +
                                    "retrieved by the platform services getRegisteredServices call.  Once you have the correct " +
                                    "value for your service, ensure it is passed on the configuration request."),

    /**
     * OMAG-ADMIN-400-046 - Unable to configure server {0} since engine service {1} is not registered in this OMAG Server Platform
     */
    ENGINE_SERVICE_NOT_RECOGNIZED(400, "OMAG-ADMIN-400-046",
                                "Unable to configure server {0} since engine service {1} is not registered in this OMAG Server Platform",
                                "The system is unable to add this engine service to the server's configuration document.",
                                "Check that the name of the engine service is correctly specified in the configuration request.  " +
                                        "If you are not sure, issue the call to list the registered engine services and verify the " +
                                        "values you are using.  If the name is right, but the engine service should be registered," +
                                        "then the developer of the engine service needs to add this registration to the code of the engine " +
                                        "service. An engine service is registered in the " +
                                        "OMAG Server Platform by adding a description of the engine service to the " +
                                        "engine service registration. Once the engine service being " +
                                        "requested is registered, retry the configuration request."),

    /**
     * OMAG-ADMIN-400-047 - Unable to configure server {0} since engine service {1} is not enabled in this OMAG Server Platform
     */
    ENGINE_SERVICE_NOT_ENABLED(400, "OMAG-ADMIN-400-047",
                             "Unable to configure server {0} since engine service {1} is not enabled in this OMAG Server Platform",
                             "The system is unable to configure the local server with this engine service.",
                             "Validate and correct the name of the engine service URL marker or enable the engine service in this platform."),


    /**
     * OMAG-ADMIN-400-051 - The {0} Open Metadata View Service (OMVS) for server {1} requires a max page size of at least {2}, but was configured with {3}
     */
    VIEW_SERVICE_MAX_PAGE_SIZE_TOO_LOW(400, "OMAG-ADMIN-400-051",
                                       "The {0} Open Metadata View Service (OMVS) for server {1} requires a max page size of at least {2}, but was configured with {3}",
                                       "The view service fails to start as it does not have a sufficiently large maxPageSize .",
                                       "Reconfigure the View service to have a maxPageSize that is sufficient."),

    /**
     * OMAG-ADMIN-400-052 - Unable to configure an event mapper for OMAG server {0} because its local repository mode is set to {1}
     */
    LOCAL_REPOSITORY_MODE_NOT_PROXY(400, "OMAG-ADMIN-400-052",
                                  "Unable to configure an event mapper for OMAG server {0} because its local repository mode is set to {1}",
                                  "The local repository mode must be set to repository proxy before the event mapper connection is set.  The system is unable to configure the local server.",
                                  "The local repository mode is supplied by the caller to the OMAG server when the repository connection is set up.  " +
                                          "This call to enable the repository connection needs to be made before the call to set the event mapper connection."),

    /**
     * OMAG-ADMIN-400-105 - The {0} property in the configuration for server {1} is null
     */
    NULL_PROPERTY_NAME(400, "OMAG-ADMIN-400-105",
               "The {0} property in the configuration for server {1} is null",
               "The server fails to start because this value is needed to operate successfully.",
               "Add a value for this property to the configuration document and restart the server."),

    /**
     * OMAG-ADMIN-404-100 - The {0} audit log destination connection name does not exist, so the requested {1} operation cannot proceed
     */
    AUDIT_LOG_DESTINATION_NOT_FOUND(404, "OMAG-ADMIN-404-100",
                       "The {0} audit log destination connection name does not exist, so the requested {1} operation cannot proceed",
                       "The audit log destination is not changed.",
                       "Amend the request so it refers to an audit destination log connection name that exists."),

    /**
     * OMAG-ADMIN-500-001 - Method {1} for OMAG server {0} returned an unexpected {2} exception with message {3}
     */
    UNEXPECTED_EXCEPTION(500, "OMAG-ADMIN-500-001",
            "Method {1} for OMAG server {0} returned an unexpected exception of {2} with message {3}",
            "The function requested failed.",
            "This is likely to be either a configuration, operational or logic error. Validate the request. Look at the user action for the embedded exception since this will provide the most specific information."),

    /**
     * OMAG-ADMIN-500-002 - Method {0} returned an unexpected {1} exception with message {2}
     */
    UNEXPECTED_PLATFORM_EXCEPTION(500, "OMAG-ADMIN-500-002",
            "Method {0} returned an unexpected {1} exception with message {2}",
            "The system is unable to configure the OMAG server.  The exception message gives more detail on the route cause of the problem.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request parameters.  If you are stuck," +
                                          " raise an issue on Egeria's GitHub."),

    /**
     * OMAG-ADMIN-500-003 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open
     * metadata topic connection because the connector provider is incorrect.  The error message was {3}
     */
    BAD_TOPIC_CONNECTOR_PROVIDER(500, "OMAG-ADMIN-500-003",
            "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
            "This is an internal error.  The access service is not using a valid connector provider.",
            "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve."),

    /**
     * OMAG-ADMIN-500-004 - The {0} service detected an unexpected {1} exception with message {2} during initialization
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION(500, "OMAG-ADMIN-500-004",
            "The {0} service detected an unexpected {1} exception with message {2} during initialization",
            "The system is unable to start the service in the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors in the audit log.  Validate the request.  " +
                                                "If you are stuck, raise an issue."),

    /**
     * OMAG-ADMIN-503-001 - A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "OMAG-ADMIN-503-001",
            "A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2}",
            "The server has issued a call to the open metadata admin service REST API in a remote server and has received an exception from the " +
                                       "local client libraries.",
            "Look for errors in the local client's console to understand and correct the source of the error.")

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
    OMAGAdminErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
