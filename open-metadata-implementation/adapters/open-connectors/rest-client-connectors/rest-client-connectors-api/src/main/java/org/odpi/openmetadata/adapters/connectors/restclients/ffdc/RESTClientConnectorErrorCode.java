/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The RESTClientConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the REST Client.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum RESTClientConnectorErrorCode
{
    NULL_RESPONSE_FROM_API(503, "CLIENT-SIDE-REST-API-CONNECTOR-503-001 ",
            "A null response was received from REST API call {0} to server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "CLIENT-SIDE-REST-API-CONNECTOR-503-002 ",
            "A client-side exception {0} was received by method {1} from API call {2} to server {3} on platform {4}.  The error message was {5}",
            "The client has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Review the error message to determine the cause of the error.  Check that the server is running an the URL is correct. Look for errors in the local server's console to understand and correct the cause of the error. Then rerun the request"),
    EXCEPTION_RESPONSE_FROM_API(503, "CLIENT-SIDE-REST-API-CONNECTOR-503-003 ",
            "A {0} exception was received from REST API call {1} to server {2}: error message was: {3}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received an exception response.",
            "The error message should indicate the cause of the error.  Otherwise look for errors in the remote server's audit log and console to understand and correct the source of the error.")
    ;


    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;

    private static final Logger log = LoggerFactory.getLogger(RESTClientConnectorErrorCode.class);


    /**
     * The constructor for CommunityProfileErrorCode expects to be passed one of the enumeration rows defined in
     * CommunityProfileErrorCode above.   For example:
     *
     *     CommunityProfileErrorCode   errorCode = CommunityProfileErrorCode.PROFILE_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    RESTClientConnectorErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== CommunityProfileErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> CommunityProfileErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "CommunityProfileErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
