/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OMAGServerInstanceErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with OMAG Server instances within the OMAG Server Platform
 * It is used in conjunction with all multi-admin exceptions, both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA   Typically the numbers used are:</li>
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
public enum OMAGServerInstanceErrorCode
{
    BAD_SERVER_SECURITY_CONNECTION(400, "OMAG-PLATFORM-400-001 ",
            "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
            "The system is unable to validate the users issuing requests to this server.",
            "Review the error message to determine the cause of the problem."),

    SERVICES_NOT_SHUTDOWN(400, "OMAG-PLATFORM-400-002 ",
            "The OMAG server {0} has been requested to shutdown but the following services are still running: {1}",
            "The system is unable to shutdown the server correctly.",
            "Review other error messages to determine the cause of the problem.  This is likely to be a logic error in the services listed in the message"),

    SERVER_NOT_AVAILABLE(404, "OMAG-PLATFORM-404-001 ",
            "The OMAG Server {0} is not available to service a request from user {1}",
            "The system is unable to process the request.",
            "Retry the request when the OMAG Server is available."),

    SERVICE_NOT_AVAILABLE(404, "OMAG-PLATFORM-404-002 ",
            "The {0} service is not available on OMAG Server {1} to handle a request from user {2}",
            "The system is unable to process the request.",
            "Verify that the correct server is being called on the correct platform and that the requested service is configured to run there.  " +
                                  "Once the correct environment is in place, retry the request."),

    SERVER_NAME_NOT_AVAILABLE(404, "OMAG-PLATFORM-404-003 ",
            "The server name is not available for the {0} operation",
            "The system is unable to connect to the open metadata repository.",
            "Check that the server where the access service is running initialized correctly.  " +
                            "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_INITIALIZED(404, "OMAG-PLATFORM-404-004 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the access service is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_AVAILABLE(404, "OMAG-PLATFORM-404-005 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository.",
            "Check that the server where the access service is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(OMAGServerInstanceErrorCode.class);


    /**
     * The constructor for OMAGServerInstanceErrorCode expects to be passed one of the enumeration rows defined in
     * OMAGServerInstanceErrorCode above.   For example:
     *
     *     OMAGServerInstanceErrorCode   errorCode = OMAGServerInstanceErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    OMAGServerInstanceErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== OMAGServerInstanceErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OMAGServerInstanceErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "OMAGServerInstanceErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
