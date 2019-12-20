/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The UIAdminErrorCode is used to define first failure data capture (FFDC) for errors that occur within the UI Server
 * It is used in conjunction with UI Exceptions, both Checked and Runtime (unchecked).
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
public enum UIAdminErrorCode
{
    NULL_LOCAL_SERVER_NAME(400, "UI-ADMIN-400-001 ",
            "UI Server has been called with a null local server name",
            "The system is unable to configure the local server.",
            "The local server name is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    INCOMPATIBLE_SERVER_NAMES(400, "UI-ADMIN-400-002 ",
            "UI Server has been called with server name {0} and a configuration document where the server name is {1}",
            "The system is unable to start the local server.",
            "The local server name is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    NULL_USER_NAME(400, "UI-ADMIN-400-003 ",
            "UI Server {0} has been called with a null user name (userId)",
            "The system is unable to configure the local server.",
            "The user name is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),
    NULL_GOVERNANCE_SERVICE_NAME(400, "UI-ADMIN-400-004 ",
            "UI Server {0} has been called by {1} with a null governance service name",
            "The system is unable to configure the local server.",
            "The governance service name is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),
    
    INVALID_GOVERNANCE_SERVICE_NAME(400, "UI-ADMIN-400-005 ",
            "UI Server {0} has been called by {1} with an invalid governance Server Name {2}. The valid governance server names are {3}.",
            "The system is unable to configure the local server.",
            "The governance server name that is supplied by the caller to the UI Server is not a valid value. This call needs to be corrected before the server can operate correctly."),
    NULL_METADATA_SERVER_NAME(400, "UI-ADMIN-400-006 ",
            "UI Server {0} has been called by {1} with a null Metadata server name",
            "The system is unable to configure the local server.",
            "The metadata server name is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    INVALID_METADATA_SERVER_URL(400, "UI-ADMIN-400-007 ",
            "UI Server {0} has been called by {1} with an invalid governance Server URL {2}",
            "The system is unable to configure the local server.",
            "The governance server name that is supplied by the caller to the UI Server is not a valid value. This call needs to be corrected before the server can operate correctly. "),
    NULL_METADATA_SERVER_URL(400, "UI-ADMIN-400-008 ",
            "UI Server {0} has been called by {1} with a null metadata server URL",
            "The system is unable to configure the local server.",
            "The metadata server URL is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    NULL_OPEN_LINEAGE_SERVER_URL(400, "UI-ADMIN-400-009 ",
            "UI Server {0} has been called by {1} with a null governance server URL",
            "The system is unable to configure the local server.",
            "The governance server URL is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    INVALID_OPEN_LINEAGE_SERVER_URL(400, "UI-ADMIN-400-010 ",
            "UI Server {0} has been called by {1} with an invalid governance Server URL {2}",
            "The system is unable to configure the local server.",
            "The governance server URL that is supplied by the caller to the UI Server is not a valid value. This call needs to be corrected before the server can operate correctly. The valid governance server names are {2}"),

    NULL_SERVER_CONFIG(400, "UI-ADMIN-400-011 ",
            "The UI Server {0} has been passed null configuration.",
            "The system is unable to initialize the local server instance.",
            "Retry the request with server configuration."),

    NULL_USER_STORE_CONFIG(400, "UI-ADMIN-400-012 ",
            "The UI Server {0} has been passed a configuration document with no open metadata user store configuration",
            "The system is unable to initialize the local server instance.",
            "Use the administration services to add the user store configuration."),

    BAD_CONFIG_FILE(400, "UI-ADMIN-400-013 ",
            "The UI Server {0} is not able to open its configuration file {1} due to the following error: {2}",
            "The system is unable to initialize the server.",
            "Review the error message to determine the cause of the problem."),

    BAD_MAX_PAGE_SIZE(400, "UI-ADMIN-400-014 ",
            "The UI Server {0} has been passed an invalid maximum page size of {1}",
            "The system has ignored this value.",
            "The maximum page size must be a number greater than zero.  Retry the request with a valid value."),

    NULL_OPEN_LINEAGE_SERVER_NAME(400, "UI-ADMIN-400-018 ",
            "UI Server {0} has been called by {1} with a null governance server name",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The governance server name is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    EMPTY_CONFIGURATION(400, "UI-ADMIN-400-019 ",
            "UI Server {0} has been called with a configuration document that has no services configured",
            "The requested server provides no function.",
            "Use the administration services to add configuration for OMAG services to the server's configuration document."),

    INCOMPATIBLE_CONFIG_FILE(400, "UI-ADMIN-400-022 ",
            "The configuration document for UI Server {0} is at version {1} which is not compatible with this UI Server Platform which supports versions {2}",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "Migrate the configuration document to a compatible version (or delete and recreate it).  See https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/migrating-configuration-documents.html"),

    INVALID_SOURCE_AUTHENTICATION(400, "UI-ADMIN-400-023 ",
            "The configuration document for UI Server {0} with an invalid sourceAuthentication {1}",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The source authentication is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    INCORRECT_USER_STORE_FOR_LDAP(400, "UI-ADMIN-400-024 ",
            "The configuration document for UI Server {0} has been called by {1} with an incorrect user configStore. User configStore class {2} was passed, but {3} was expected",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The user configStore configuration is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    NULL_URL_FOR_LDAP_USER_STORE(400, "UI-ADMIN-400-025 ",
            "The configuration document for UI Server {0} has been called by {1} with a null URL for the LDAP user configStore.",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The user configStore configuration is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    INVALID_URL_FOR_LDAP_USER_STORE(400, "UI-ADMIN-400-026 ",
            "The configuration document for UI Server {0} has been called by {1} with invalid URL {2} for the LDAP user configStore.",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The user configStore configuration is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    H2_WITH_USER_STORE(400, "UI-ADMIN-400-027 ",
            "The configuration document for UI Server {0} has been called by {1} with a user configStore config, which is not required for db source authentication",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The user configStore configuration is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    NULL_USER_STORE_FOR_LDAP(400, "UI-ADMIN-400-029 ",
        "The configuration document for UI Server {0} has been called by {1} with specifying LDAP source authentication with a null user configStore.",
        "The system is unable to add this governance server name to the configuration document for the local server.",
        "The user configStore configuration is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    NULL_CONNECTION(400, "UI-ADMIN-400-030 ",
            "UI Server {0} has been called with a null connection for method {1}",
            "The system is unable to add this connection to the configuration.",
            "Change the call to pass a valid connection.  If you want to clear the connection use the clear version of the method."),

    NULL_PLATFORM_CONNECTION(400, "UI-ADMIN-400-031 ",
            "The UI Server platform has been called with a null connection for method {0}",
            "The system is unable to add this connection to the configuration.",
            "Change the call to pass a valid connection.  If you want to clear the connection use the clear version of the method."),

    NULL_SOURCE_AUTHENTICATION(400, "UI-ADMIN-400-032 ",
            "The configuration document for UI Server {0} has been called by {1} without a source authentication.",
            "The system is unable to add this governance server name to the configuration document for the local server.",
            "The user configStore configuration is supplied by the caller to the UI Server. This call needs to be corrected before the server can operate correctly."),

    UNEXPECTED_EXCEPTION(500, "UI-ADMIN-500-001 ",
            "Method {1} for UI Server {0} returned an unexpected exception of {2} with message {3}",
            "The system is unable to configure the UI Server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue."),

    UNEXPECTED_PLATFORM_EXCEPTION(500, "UI-ADMIN-500-002 ",
            "Method {1} returned an unexpected exception of {1} with message {2}",
            "The system is unable to configure the UI Server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue."),
    SERVICE_NOT_INITIALIZED(500, "UI-ADMIN-500-003 ",
            "UI Service not initializes ",
            "The system is unable to start the UI Server.",
            "This is likely to be either an operational or logic error. Look for other errors.  Validate the request.  If you are stuck, raise an issue.")



            ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(UIAdminErrorCode.class);


    /**
     * The constructor for UIAdminErrorCode expects to be passed one of the enumeration rows defined in
     * UIAdminErrorCode above.   For example:
     *
     *     UIAdminErrorCode   errorCode = UIAdminErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode error code to use over REST calls
     * @param newErrorMessageId unique Id for the message
     * @param newErrorMessage text for the message
     * @param newSystemAction description of the action taken by the system when the error condition happened
     * @param newUserAction instructions for resolving the error
     */
    UIAdminErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
            log.debug(String.format("<== UIAdminErrorCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> UIAdminErrorCode.getMessage(%s): %s", Arrays.toString(params), result));
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
