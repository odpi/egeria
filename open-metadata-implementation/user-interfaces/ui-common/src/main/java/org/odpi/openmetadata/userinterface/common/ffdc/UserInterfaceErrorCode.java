/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.common.ffdc;


import java.text.MessageFormat;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The UserInterfaceErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
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
public enum UserInterfaceErrorCode
{
    MALFORMED_INPUT_EXCEPTION(500, "USER-INTERFACE-500-001",
            "The response received from service does not have the expected format.",
            "Check the configuration and the response from the service.",
            "Please check the request."),
    INVALID_REQUEST_FOR_ASSET_CATALOG(500, "USER-INTERFACE-500-002",
            "The request for asset catalog is invalid",
            "The system is unable to handle request.",
            "Check that the configuration for Asset Catalog is correct." ),
    RESOURCE_NOT_FOUND(503, "USER-INTERFACE-503-003",
            "The resource cannot be accessed",
            "Check services are up.",
            "Please try again later."),
    USER_NOT_AUTHORIZED(401, "USER-INTERFACE-401-004",
            "User is not authorized",
            "The system is unable to authorize the user.",
            "Check your credentials."),
    SERVICE_NOT_INITIALIZED(503, "USER-INTERFACE-401-005 ",
            "The view service {0} has not been initialized and can not support REST API call {1}",
            "The server has received a call to one of its open metadata view services but is unable to process it because the view  service is not active.",
            "If the server is supposed to have this view service activated, correct the server configuration and restart the server."),
    DEPENDANT_SERVER_NOT_AVAILABLE(503, "USER-INTERFACE-401-006 ",
            "The dependant server {0} with URL {1} is not available.",
            "The server has received a call to one of its open metadata view services but is unable to process it because a dependant server id not available.",
            "Ensure the server is correctly specified in the UI configuration and is started."),
    INVALID_SERVERNAME(400, "USER-INTERFACE-400-007 ",
            "A request has been issued by the UI server to invalid server {0}. ",
            "The system is unable to process the request with the supplied server name, as there is no server to send the request to.",
            "Ensure there is a server defined in the configuration with name {0)} ."),
    INVALID_PARAMETER(400, "USER-INTERFACE-400-008 ",
        "A request has been issued by the UI server and received an error. ",
        "The system is unable to process the request.",
        "Raise a github request that there is an unexpected invalid parameter exception."),
    SERVICE_NOT_AVAILABLE(400, "USER-INTERFACE-400-009 ",
            "A request has been issued by the UI server for service {0} but it is not available. ",
            "The system is unable to process the request for an unavailable service.",
            "Ensure the service is correctly specified in the UI configuration and is started."),
            ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(UserInterfaceErrorCode.class);


    /**
     * The constructor for UserInterfaceErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
     *
     *     UserInterfaceErrorCode   errorCode = UserInterfaceErrorCode.SERVER_URL_NOT_SPECIFIED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    UserInterfaceErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== UserInterfaceErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> UserInterfaceErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "UserInterfaceErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
