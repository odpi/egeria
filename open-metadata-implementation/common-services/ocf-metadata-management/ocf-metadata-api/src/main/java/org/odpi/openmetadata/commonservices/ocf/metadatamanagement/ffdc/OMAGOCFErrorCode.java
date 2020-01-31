/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OMAGOCFErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with OCF Beans.
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
public enum OMAGOCFErrorCode
{
    NULL_CONNECTION_PARAMETER(400, "OMAG-OCF-400-001 ",
                              "The connection value passed on the {0} parameter of the {1} operation is null",
                              "The system is unable to process the request without this value.",
                              "Correct the code in the caller to provide the name."),

    NO_ASSET_PROPERTIES(400, "OMAG-OCF-400-002 ",
                        "The request for the properties of asset {0} failed with the following message returned: {1}",
                        "The system is unable to process the request.",
                        "Use the information in the message to understand the nature of the problem and once it is resolved, retry the request."),

    NULL_CLASSIFICATION_NAME(400, "OMAG-OCF-400-003 ",
                             "Service {0} is unable to process one of the classifications supplied on the {1} call because the classification name is null",
                             "The system is unable to create a new instance.",
                             "Verify the classification parameters passed with this request."),

    BAD_CLASSIFICATION_PROPERTIES(400, "OMAG-OCF-400-004 ",
                                  "Service {0} is unable to process the properties supplied with classification {1}.  The associated error message was: {2}",
                                  "The system is unable to create a new instance.",
                                  "Verify the classification parameters passed with this request."),

    OMRS_NOT_INITIALIZED(404, "OMAG-OCF-404-001 ",
                         "The open metadata repository services are not initialized for the {0} operation",
                         "The system is unable to connect to an open metadata repository.",
                         "Check that the server where the Open Connector Framework metadata services are running is initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),

    NULL_CONNECTOR_RETURNED(500, "OMAG-OCF-500-001 ",
                            "The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service" +
                                    " running in OMAG Server {2} at {3}",
                            "The system is unable to create a connector which means some of its services will not work.",
                            "This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connection" +
                                    "and correct if necessary.  If the connection is correct, contact the Egeria community for help."),

    WRONG_TYPE_OF_CONNECTOR(500, "OMAG-OCF-500-002 ",
                            "The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is " +
                                    "not of the required type. It should be an instance of {4}",
                            "The system is unable to create the required connector which means some of its services will not work.",
                            "Verify that the OMAG server is running and the OMAS service is correctly configured."),

    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(OMAGOCFErrorCode.class);


    /**
     * The constructor for OMAGOCFErrorCode expects to be passed one of the enumeration rows defined in
     * OMAGOCFErrorCode above.   For example:
     *
     *     OMAGOCFErrorCode   errorCode = OMAGOCFErrorCode.NULL_CONNECTION_PARAMETER;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    OMAGOCFErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== OMAGOCFErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OMAGOCFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "OMAGOCFErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
