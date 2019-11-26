/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The ViewServiceErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Open Metadata View Services (OMVSs) Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum ViewServiceErrorCode
{
    USER_NOT_AUTHORIZED(400, "VIEW-SERVICE-400-001 ",
            "User {0} is not authorized to issue the {1} request for open metadata view service {3} on server {4}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    PROPERTY_SERVER_ERROR(400, "VIEW-SERVICE-400-002 ",
            "An unexpected error was returned by the property server during {1} request for open metadata view service {2} on server {3}; message was {0}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    SERVER_NOT_AVAILABLE(404, "VIEW-SERVICE-404-001 ",
            "The Metadata Server {0} is not available",
            "The system is unable to connect to the Metadata Server.",
            "Check that the Metadata Server URL is correct in the configuration and the open metadata and governance (OMAG) server is running.  Retry the request when the OMAG Server is available."),
    CLIENT_SIDE_REST_API_ERROR(503, "VIEW-SERVICE-503-001 ",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has issued a call to the open metadata view service REST API in a remote server and has received an exception from the local client libraries.",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(503, "VIEW-SERVICE-503-002 ",
            "The view service {0} has not been initialized and can not support REST API call {1}",
            "The server has received a call to one of its open metadata view services but is unable to process it because the view  service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server.")
    ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(ViewServiceErrorCode.class);


    /**
     * The constructor for ViewServiceErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
     *
     *     ViewServiceErrorCode   errorCode = ViewServiceErrorCode.SERVER_URL_NOT_SPECIFIED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    ViewServiceErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== ViewServiceErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> ViewServiceErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ViewServiceErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
