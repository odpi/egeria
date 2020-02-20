/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OMAGAdminErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMAG Server
 * It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>501 - not implemented </li>
 *         <li>503 - Service not available</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - unauthorized</li>
 *         <li>404 - not found</li>
 *         <li>405 - method not allowed</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum OMAGAdminErrorCode
{
    NULL_LOCAL_SERVER_NAME(400, "OMAG-ADMIN-400-001 ",
            "OMAG server has been called with a null local server name",
            "The system is unable to configure the local server.",
            "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    INCOMPATIBLE_SERVER_NAMES(400, "OMAG-ADMIN-400-002 ",
            "OMAG Server Platform was requested to start OMAG Server {0} but the configuration document retrieved for it " +
                    "has the server name set to {1}",
            "The system is unable to start the OMAG server because it can not retrieve the correct configuration document.",
            "The configuration is retrieved from the configuration document store connector.  " +
                                      "This connector is set up for the OMAG Server Platform.  " +
                                      "It is either not configured correctly, or there is an error in its " +
                                      "implementation because it is not retrieving the correct configuration document for" +
                                      "the requested server."),

    NULL_LOCAL_SERVER_USERID(400, "OMAG-ADMIN-400-003 ",
                   "OMAG server {0} has been configured with a null local server user identifier (userId)",
                   "The system fails to start the server because this user identifier is needed for processing events " +
                                     "from external topics.  If the server continues to operate, it will not be able to function correctly.",
                   "The local server's user identifier is supplied in the configuration document for the OMAG server.  " +
                                     "This configuration needs to be corrected before the server can operate correctly.  " +
                                     "Once the configuration document has been corrected, restart the server."),

    NULL_USER_NAME(400, "OMAG-ADMIN-400-004 ",
                   "An REST API call to OMAG server {0} has been made with a null user identifier (userId)",
                   "The server rejects the request.",
                   "The user name is supplied in a parameter (typically called userID) in the call to the OMAG server. " +
                           "This parameter needs to be changes to a valid user identifier before the request can operate correctly."),

    ACCESS_SERVICE_NOT_RECOGNIZED(400, "OMAG-ADMIN-400-005 ",
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

    ACCESS_SERVICE_NOT_ENABLED(400, "OMAG-ADMIN-400-006 ",
            "Unable to configure server {0} since access service {1} is not enabled in this OMAG Server Platform",
            "The system is unable to configure the local server.",
            "Validate and correct the name of the access service URL marker or enable the access service in this platform."),

    NULL_COHORT_NAME(400, "OMAG-ADMIN-400-007 ",
            "OMAG server {0} has been configured with a null cohort name",
            "The system is unable to configure the local server.",
            "The cohort name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    LOCAL_REPOSITORY_MODE_NOT_SET(400, "OMAG-ADMIN-400-008 ",
            "The local repository mode has not been set for OMAG server {0}",
            "The local repository mode must be enabled before the event mapper connection or repository proxy connection is set.  The system is unable to configure the local server.",
            "The local repository mode is supplied by the caller to the OMAG server. This call to enable the local repository needs to be made before the call to set the event mapper connection or repository proxy connection."),

    NULL_SERVER_CONFIG(400, "OMAG-ADMIN-400-009 ",
            "The OMAG server {0} has been passed null configuration.",
            "The system is unable to initialize the local server instance.",
            "Retry the request with server configuration."),

    NULL_REPOSITORY_CONFIG(400, "OMAG-ADMIN-400-010 ",
            "The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration",
            "The system is unable to initialize the local server instance.",
            "Use the administration services to add the repository services configuration."),

    NULL_ACCESS_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-011 ",
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system is unable to initialize this access service. The server failed to start.",
            "If the access service should be initialized then set up the appropriate admin services class name " +
                                            "in the access service's configuration and restart the server instance. Otherwise, " +
                                            "remove the configuration for this access service and restart the server."),

    BAD_ACCESS_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-012 ",
            "The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2}",
            "The system is unable to initialize this access service and the server failed to start.",
            "The configuration document for the serve needs to be fixed before the server will restart.  " +
                                           "If the access service should be initialized then update its configuration and" +
                                           "ensure ist admin class name is set to the name of a Java Class that implements AccessServiceAdmin. " +
                                           "Otherwise delete the configuration for this access service.  " +
                                           "Once the configuration document is updated, restart the server."),

    BAD_CONFIG_FILE(400, "OMAG-ADMIN-400-013 ",
            "The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2} which included a message {3}",
            "The system is unable to initialize the server.",
            "Review the error message to determine the cause of the problem."),

    BAD_MAX_PAGE_SIZE(400, "OMAG-ADMIN-400-014 ",
            "The OMAG server {0} has been passed an invalid maximum page size of {1}",
            "The server failed to start.",
            "The maximum page size sets an upper limit on how many results a caller can request on a paged REST API call.  " +
                              "If it is set to zero then it means there is no limit, a positive number is the maximum paging size allowed.  " +
                              "Set the maximum page size in the configuration document to an appropriate value and restart the server."),

    ENTERPRISE_TOPIC_START_FAILED(400, "OMAG-ADMIN-400-015 ",
            "The OMAG server {0} is unable to start the enterprise OMRS topic connector, error message was {1}",
            "The open metadata access services will not be able to receive events from the connected repositories.",
            "Review the error messages and once the source of the problem is resolved, restart the server and retry the request."),

    TOO_LATE_TO_SET_EVENT_BUS(400, "OMAG-ADMIN-400-016 ",
            "The OMAG server {0} is unable to set up new event bus configuration because other services are already configured",
            "It is not possible to change the event bus configuration for this server while there are other open metadata services configured.",
            "Remove any configuration for this server's cohorts, local repository and access services, " +
                                      "and retry the request to add the event bus configuration.  " +
                                      "Then it is possible to add the configuration for the other services back " +
                                      "into the configuration document."),

    NO_EVENT_BUS_SET(400, "OMAG-ADMIN-400-017 ",
            "The OMAG server {0} is unable to add open metadata services until the event bus is configured",
            "No change has occurred in this server's configuration document.",
            "Add the event bus configuration using the administration services and retry the request."),

    NULL_METADATA_COLLECTION_NAME(400, "OMAG-ADMIN-400-018 ",
            "OMAG server {0} has been called with a null metadata collection name",
            "The system is unable to add this metadata collection name to the configuration document for the local server.",
            "The metadata collection name is optional.  If it is not set up then the local server name is used instead."),

    EMPTY_CONFIGURATION(400, "OMAG-ADMIN-400-019 ",
            "OMAG server {0} has been called with a configuration document that has no services configured",
            "The requested server provides no function.",
            "Use the administration services to add configuration for OMAG services to the server's configuration document."),

    NULL_ACCESS_SERVICE_ROOT_URL(400, "OMAG-ADMIN-400-020 ",
            "The {0} service of OMAG server {1} has been configured with a null root URL for the {2} access service",
            "The system is unable to accept this value in the configuration properties.",
            "The root URL is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_ACCESS_SERVICE_SERVER_NAME(400, "OMAG-ADMIN-400-021 ",
            "OMAG server {0} has been configured with a null cohort name",
            "The system is unable to accept this value in the configuration properties.",
            "The server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_FILE_NAME(400, "OMAG-ADMIN-400-022 ",
            "OMAG server {0} has been configured with a null file name for an Open Metadata Archive",
            "The system is unable to configure the local server to load this Open Metadata Archive file.",
            "The file name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can load the open metadata archive."),

    INCOMPATIBLE_CONFIG_FILE(400, "OMAG-ADMIN-400-023 ",
            "The configuration document for OMAG server {0} is at version {1} which is not compatible with this OMAG Server Platform which supports versions {2}",
            "The system is unable to configure the local server because it can not read the configuration document.",
            "Migrate the configuration document to a compatible version (or delete and recreate it).  See https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/migrating-configuration-documents.html"),

    BAD_CONFIG_PROPERTIES(400, "OMAG-ADMIN-400-024 ",
            "The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property.  The resulting exception of {3} included the following message: {4}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),

    NO_ENTERPRISE_TOPIC(400, "OMAG-ADMIN-400-025 ",
            "The {0} Open Metadata Access Service (OMAS) has been passed a null enterprise OMRS topic for server {1}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),

    BAD_TOPIC_CONNECTOR(400, "OMAG-ADMIN-400-026 ",
            "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error " +
                    "message was {3}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),

    NULL_CONNECTION(400, "OMAG-ADMIN-400-027 ",
            "OMAG server {0} has been called with a null connection for method {1}",
            "The system is unable to add this connection to the configuration.",
            "Change the call to pass a valid connection.  If you want to clear the connection use the clear version of the method."),

    NULL_PLATFORM_CONNECTION(400, "OMAG-ADMIN-400-028 ",
            "The OMAG server platform has been called with a null connection for method {0}",
            "The system is unable to add this connection to the configuration.",
            "Change the call to pass a valid connection.  If you want to clear the connection use the clear version of the method."),

    NULL_VIEW_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-029 ",
            "The UI server {0} has been passed a null admin services class name for view service {1}",
            "The system is unable to initialize this view service.",
            "if the view service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    BAD_VIEW_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-030 ",
            "The UI server {0} has been passed an invalid admin services class name {1} for view service {2}",
            "The system is unable to initialize this view service.",
            "If the view service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    INCOMPATIBLE_SUBSYSTEMS(400, "OMAG-ADMIN-400-031 ",
            "The configuration document for server {0} includes configuration for a {1} but also has configuration for the {2} subsystem which " +
                    "is not a compatible combination",
            "The server fails to initialize and an exception is returned to the caller.",
            "Reconfigure the server to include a compatible combination of subsystems."),

    MISSING_CONFIGURATION_PROPERTY(400, "OMAG-ADMIN-400-032 ",
            "The supplied configuration for server {0} was not accepted because there is no value provided for property {1}",
            "The system returns an exception and does not update the configuration document for the server.",
            "Retry the configuration request with the property value set up correctly."),

    COHORT_NOT_KNOWN(400, "OMAG-ADMIN-400-033 ",
            "The OMAG server {0} is unable to override the cohort topic until the {1} cohort is set up",
            "No change has occurred in this server's configuration document.",
            "Add the cohort configuration using the administration services and retry the request."),

    COHORT_TOPIC_STRANGE(400, "OMAG-ADMIN-400-034 ",
            "The OMAG server {0} is unable to override the cohort topic for the {1} cohort because the contents of the topic connection do not " +
                    "follow the expected pattern",
            "No change has occurred in this server's configuration document because the topic connection in the cohort configuration does not " +
                                 "follow the same structure as Egeria expects and so any update may have unexpected consequences.",
            "Use the setCohortConfig() method to manually update the cohort topic in the cohort configuration."),

    UNCLASSIFIABLE_SERVER(400, "OMAG-ADMIN-400-035 ",
            "Unable to classify the type of server for OMAG server {0} from its configuration document.",
            "The system is unable to initialize the local server instance.",
            "Analyse the server's configuration document to determine why the type of server it requests " +
                                  "is not identified.  Update the server's configuration document to provide " +
                                  "a valid server configuration."),

    VIEW_SERVICE_NOT_RECOGNIZED(400, "OMAG-ADMIN-400-036 ",
                                "Unable to configure server {0} since view service {1} is not registered in this OMAG Server Platform",
                                "The system is unable to add this view service to the server's configuration document.",
                                "Check that the name of the view service is correctly specified in the configuration request.  " +
                                        "If you are not sure, issue the call to list the registered view services and verify the " +
                                        "values you are using.  If the name is right, but the view service should be registered," +
                                        "then the developer of the view service needs to add this registration to the code of the view " +
                                        "service. An view service is registered in the " +
                                        "OMAG Server Platform by adding a description of the view service to the " +
                                        "view service registration. Once the view service being " +
                                        "requested is registered, retry the configuration request."),

    VIEW_SERVICE_NOT_ENABLED(400, "OMAG-ADMIN-400-036 ",
                             "Unable to configure server {0} since view service {1} is not enabled in this OMAG Server Platform",
                             "The system is unable to configure the local server.",
                             "Validate and correct the name of the view service URL marker or enable the view service in this platform."),

    NULL_CLIENT_CONFIG(400, "OMAG-ADMIN-400-037 ",
                           "OMAG server {0} has been called by {1} with a null client config",
                           "The system is unable to configure the local server.",
                           "The client config is supplied by the caller to the OMAG server. This call needs to be supplied, including the name and URL of the OMAG server, before the server can operate correctly."),

    NULL_OMAG_SERVER_ROOT_URL(400, "OMAG-ADMIN-400-038 ",
                                 "The {0} service of OMAG server {1} has been configured with a null root URL for the OMAG Server",
                                 "The system is unable to accept this value in the configuration properties.",
                                 "The root URL is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_OMAG_SERVER_NAME(400, "OMAG-ADMIN-400-039 ",
                          "The {0} service of server {1} has been configured with a null OMAG Server name",
                          "The system is unable to accept this value in the configuration properties.",
                          "The OMAG Server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    UNEXPECTED_EXCEPTION(500, "OMAG-ADMIN-500-001 ",
            "Method {1} for OMAG server {0} returned an unexpected exception of {2} with message {3}",
            "The system is unable to configure the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue."),

    UNEXPECTED_PLATFORM_EXCEPTION(500, "OMAG-ADMIN-500-002 ",
            "Method {0} returned an unexpected exception of {1} with message {2}",
            "The system is unable to configure the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an " +
                                          "issue."),
    BAD_TOPIC_CONNECTOR_PROVIDER(500, "OMAG-ADMIN-500-003 ",
            "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
            "This is an internal error.  The access service is not using a valid connector provider.",
            "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve."),

    UNEXPECTED_INITIALIZATION_EXCEPTION(500, "OMAG-ADMIN-500-004 ",
            "The {0} service detected an unexpected {1} exception with message {2} during initialization",
            "The system is unable to start the service in the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  " +
                                                "If you are stuck, raise an issue."),

    CLIENT_SIDE_REST_API_ERROR(503, "OMAG-ADMIN-503-001 ",
            "A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2}",
            "The server has issued a call to the open metadata admin service REST API in a remote server and has received an exception from the " +
                                       "local client libraries.",
            "Look for errors in the local client's console to understand and correct the source of the error.")

    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(OMAGAdminErrorCode.class);


    /**
     * The constructor for OMRSErrorCode expects to be passed one of the enumeration rows defined in
     * OMRSErrorCode above.   For example:
     *
     *     OMRSErrorCode   errorCode = OMRSErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode error code to use over REST calls
     * @param newErrorMessageId unique Id for the message
     * @param newErrorMessage text for the message
     * @param newSystemAction description of the action taken by the system when the error condition happened
     * @param newUserAction instructions for resolving the error
     */
    OMAGAdminErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
    {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== OMAGAdminErrorCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> OMAGAdminErrorCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }
}
