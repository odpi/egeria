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
            "OMAG server has been called with server name {0} and a configuration document where the server name is {1}",
            "The system is unable to start the local server.",
            "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_USER_NAME(400, "OMAG-ADMIN-400-003 ",
            "OMAG server {0} has been called with a null user name (userId)",
            "The system is unable to configure the local server.",
            "The user name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    ACCESS_SERVICE_NOT_RECOGNIZED(400, "OMAG-ADMIN-400-004 ",
            "Unable to configure server {0} since access service {1} is not registered in this OMAG Server Platform",
            "The system is unable to configure the local server.",
            "Validate and correct the name of the access service URL marker."),

    ACCESS_SERVICE_NOT_ENABLED(400, "OMAG-ADMIN-400-004 ",
            "Unable to configure server {0} since access service {1} is not enabled in this OMAG Server Platform",
            "The system is unable to configure the local server.",
            "Validate and correct the name of the access service URL marker or enable the access service in this platform."),

    NULL_COHORT_NAME(400, "OMAG-ADMIN-400-006 ",
            "OMAG server {0} has been configured with a null cohort name",
            "The system is unable to configure the local server.",
            "The cohort name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    LOCAL_REPOSITORY_MODE_NOT_SET(400, "OMAG-ADMIN-400-007 ",
            "The local repository mode has not been set for OMAG server {0}",
            "The local repository mode must be enabled before the event mapper connection or repository proxy connection is set.  The system is unable to configure the local server.",
            "The local repository mode is supplied by the caller to the OMAG server. This call to enable the local repository needs to be made before the call to set the event mapper connection or repository proxy connection."),

    NULL_SERVER_CONFIG(400, "OMAG-ADMIN-400-008 ",
            "The OMAG server {0} has been passed null configuration.",
            "The system is unable to initialize the local server instance.",
            "Retry the request with server configuration."),

    NULL_REPOSITORY_CONFIG(400, "OMAG-ADMIN-400-009 ",
            "The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration",
            "The system is unable to initialize the local server instance.",
            "Use the administration services to add the repository services configuration."),

    NULL_ACCESS_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-010 ",
            "The OMAG server {0} has been passed a null admin services class name for access service {1}",
            "The system is unable to initialize this access service.",
            "if the access service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    BAD_ACCESS_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-011 ",
            "The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2}",
            "The system is unable to initialize this access service.",
            "If the access service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    BAD_CONFIG_FILE(400, "OMAG-ADMIN-400-012 ",
            "The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2}",
            "The system is unable to initialize the server.",
            "Review the error message to determine the cause of the problem."),

    BAD_MAX_PAGE_SIZE(400, "OMAG-ADMIN-400-013 ",
            "The OMAG server {0} has been passed an invalid maximum page size of {1}",
            "The system has ignored this value.",
            "The maximum page size must be a number greater than zero.  Retry the request with a valid value."),

    ENTERPRISE_TOPIC_START_FAILED(400, "OMAG-ADMIN-400-014 ",
            "The OMAG server {0} is unable to start the enterprise OMRS topic connector, error message was {1}",
            "The open metadata access services will not be able to receive events from the connected repositories.",
            "Review the error messages and once the source of the problem is resolved, restart the server and retry the request."),

    TOO_LATE_TO_SET_EVENT_BUS(400, "OMAG-ADMIN-400-015 ",
            "The OMAG server {0} is unable to set up new event bus configuration because other services are already configured",
            "It is not possible to change the event bus configuration for this server while there are other open metadata services configured.",
            "Remove any configuration for this server's cohorts, local repository and access services, " +
                                      "and retry the request to add the event bus configuration.  " +
                                      "Then it is possible to add the configuration for the other services back " +
                                      "into the configuration document."),

    NO_EVENT_BUS_SET(400, "OMAG-ADMIN-400-016 ",
            "The OMAG server {0} is unable to add open metadata services until the event bus is configured",
            "No change has occurred in this server's configuration document.",
            "Add the event bus configuration using the administration services and retry the request."),

    NULL_METADATA_COLLECTION_NAME(400, "OMAG-ADMIN-400-017 ",
            "OMAG server {0} has been called with a null metadata collection name",
            "The system is unable to add this metadata collection name to the configuration document for the local server.",
            "The metadata collection name is optional.  If it is not set up then the local server name is used instead."),

    EMPTY_CONFIGURATION(400, "OMAG-ADMIN-400-018 ",
            "OMAG server {0} has been called with a configuration document that has no services configured",
            "The requested server provides no function.",
            "Use the administration services to add configuration for OMAG services to the server's configuration document."),

    NULL_ACCESS_SERVICE_ROOT_URL(400, "OMAG-ADMIN-400-019 ",
            "The {0} service of OMAG server {1} has been configured with a null root URL for the {2} access service",
            "The system is unable to accept this value in the configuration properties.",
            "The root URL is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_ACCESS_SERVICE_SERVER_NAME(400, "OMAG-ADMIN-400-020 ",
            "OMAG server {0} has been configured with a null cohort name",
            "The system is unable to accept this value in the configuration properties.",
            "The server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_FILE_NAME(400, "OMAG-ADMIN-400-021 ",
            "OMAG server {0} has been configured with a null file name for an Open Metadata Archive",
            "The system is unable to configure the local server to load this Open Metadata Archive file.",
            "The file name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can load the open metadata archive."),

    INCOMPATIBLE_CONFIG_FILE(400, "OMAG-ADMIN-400-022 ",
            "The configuration document for OMAG server {0} is at version {1} which is not compatible with this OMAG Server Platform which supports versions {2}",
            "The system is unable to configure the local server because it can not read the configuration document.",
            "Migrate the configuration document to a compatible version (or delete and recreate it).  See https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/migrating-configuration-documents.html"),

    BAD_CONFIG_PROPERTIES(400, "OMAG-ADMIN-400-023 ",
            "The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property.  The resulting exception of {3} included the following message: {4}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),

    NO_ENTERPRISE_TOPIC(400, "OMAG-ADMIN-400-024 ",
            "The {0} Open Metadata Access Service (OMAS) has been passed a null enterprise OMRS topic for server {1}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),

    BAD_TOPIC_CONNECTOR(400, "OMAG-ADMIN-400-025 ",
            "Method {0} called on behalf of the {1} Open Metadata Access Service (OMAS) detected a {2} exception when creating an open metadata topic connector.  The error message was {3}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),

    NULL_CONNECTION(400, "OMAG-ADMIN-400-026 ",
            "OMAG server {0} has been called with a null connection for method {1}",
            "The system is unable to add this connection to the configuration.",
            "Change the call to pass a valid connection.  If you want to clear the connection use the clear version of the method."),

    NULL_PLATFORM_CONNECTION(400, "OMAG-ADMIN-400-027 ",
            "The OMAG server platform has been called with a null connection for method {0}",
            "The system is unable to add this connection to the configuration.",
            "Change the call to pass a valid connection.  If you want to clear the connection use the clear version of the method."),

    NULL_VIEW_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-028 ",
            "The UI server {0} has been passed a null admin services class name for view service {1}",
            "The system is unable to initialize this view service.",
            "if the view service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    BAD_VIEW_SERVICE_ADMIN_CLASS(400, "OMAG-ADMIN-400-029 ",
            "The UI server {0} has been passed an invalid admin services class name {1} for view service {2}",
            "The system is unable to initialize this view service.",
            "If the view service should be initialized then set up the appropriate admin services class name and restart the server instance."),

    MISSING_CONFIGURATION_PROPERTY(400, "OMAG-ADMIN-400-030 ",
            "The OMAG server {0} could not be started because no value for property {1} was set in the provided server configuration.",
            "The system is unable to initialize the server.",
            "Include the property in the provided server configuration."),

    UNEXPECTED_EXCEPTION(500, "OMAG-ADMIN-500-001 ",
            "Method {1} for OMAG server {0} returned an unexpected exception of {2} with message {3}",
            "The system is unable to configure the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue."),

    UNEXPECTED_PLATFORM_EXCEPTION(500, "OMAG-ADMIN-500-002 ",
            "Method {1} returned an unexpected exception of {1} with message {2}",
            "The system is unable to configure the OMAG server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue.")
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
