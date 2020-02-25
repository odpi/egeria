/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OMAGCommonErrorCode is used to define first failure data capture (FFDC) for common errors.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA.   Typically the numbers used are:</li>
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
public enum OMAGCommonErrorCode
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAG-COMMON-400-001 ",
                             "The OMAG Server Platform URL is null",
                             "The system is unable to connect to the OMAG Server to fulfill the request.",
                             "Create a new client and pass the correct URL for the server on the constructor."),

    SERVER_URL_MALFORMED(400, "OMAG-COMMON-400-002 ",
                         "The OMAS Server URL {0} is not in a recognized format",
                         "The system is unable to connect to the OMAG Server to fulfill.",
                         "Create a new client and pass the correct URL for the server on the constructor."),

    SERVER_NAME_NOT_SPECIFIED(400, "OMAG-COMMON-400-003 ",
                              "The OMAG Server name is null",
                              "The system is unable to connect to the OMAG Server to fulfill the request.",
                              "Create a new client and pass the correct URL for the server on the constructor."),

    NULL_USER_ID(400, "OMAG-COMMON-400-004 ",
                 "The user identifier (user id) passed on the {0} operation is null",
                 "The system is unable to process the request without a user id.",
                 "Correct the code in the caller to provide the user id."),

    NULL_GUID(400, "OMAG-COMMON-400-005 ",
              "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a guid.",
              "Correct the code in the caller to provide the guid."),

    NULL_NAME(400, "OMAG-COMMON-400-006 ",
              "The name passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a name.",
              "Correct the code in the caller to provide the name."),

    NULL_ARRAY_PARAMETER(400, "OMAG-COMMON-400-007 ",
                         "The array value passed on the {0} parameter of the {1} operation is null or empty",
                         "The system is unable to process the request without this value.",
                         "Correct the code in the caller to provide the name."),

    NEGATIVE_START_FROM(400, "OMAG-COMMON-400-008 ",
            "The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative",
            "The system is unable to process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list",
            "Correct the code in the caller to provide a non-negative value."),

    NEGATIVE_PAGE_SIZE(400, "OMAG-COMMON-400-009 ",
                        "The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative",
                        "The system is unable to process the request with this invalid value.  It should be zero to return all of the result, or greater than zero to set a maximum",
                        "Correct the code in the caller to provide a non-negative value."),

    MAX_PAGE_SIZE(400, "OMAG-COMMON-400-010 ",
            "The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}",
            "The system is unable to process the request with this page size value.",
            "Correct the code in the caller to provide a smaller page size ."),

    NULL_CONNECTION_PARAMETER(400, "OMAG-COMMON-400-011 ",
                              "The connection value passed on the {0} parameter of the {1} operation is null",
                              "The system is unable to process the request without this value.",
                              "Correct the code in the caller to provide the connection."),

    NULL_ENUM(400, "OMAG-COMMON-400-012 ",
              "The enumeration value passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without this enumeration value.",
              "Correct the code in the caller to provide the name."),

    NULL_TEXT(400, "OMAG-COMMON-400-013 ",
              "The text field value passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without this text field value.",
              "Correct the code in the caller to provide the name."),

    NULL_LOCAL_SERVER_NAME(400, "OMAG-COMMON-400-014 ",
                           "OMAG server has been called with a null local server name",
                           "The system is unable to configure the local server.",
                           "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_OBJECT(400, "OMAG-COMMON-400-015 ",
                "The object passed on the {0} parameter of the {1} operation is null",
                "The system is unable to process the request without this object.",
                "Correct the code in the caller to provide the object."),

    UNEXPECTED_EXCEPTION(400, "OMAG-COMMON-400-016 ",
              "An unexpected {0} exception was caught by {1}; error message was {2}",
              "The system is unable to process the request.",
              "Review the error message and other diagnostics created at the same time."),

    NO_REQUEST_BODY(400, "OMAG-COMMON-400-017 ",
                    "An request by user {0} to method {1} on server {2} had no request body",
                    "The system is unable to process the request.",
                    "Update the caller to provide the request body."),

    UNRECOGNIZED_TYPE_NAME(400, "OMAG-COMMON-400-018 ",
                    "The type name {0} passed on method {1} of service {2} is not recognized",
                    "The system is unable to process the request.",
                    "Update the caller to provide a correct type name."),

    BAD_SUB_TYPE_NAME(400, "OMAG-COMMON-400-019 ",
                    "The type name {0} passed on method {1} of service {2} is not a sub-type of {3}",
                    "The system is unable to process the request.",
                    "Update the caller to provide a correct type name."),

    NOT_IN_THE_ZONE(400, "OMAG-COMMON-400-020 ",
                      "Asset {0} is not recognized by the {1} service",
                      "The system is unable to process the request.",
                      "Update the caller to provide a correct asset identifier."),

    UNKNOWN_ELEMENT(400, "OMAG-COMMON-400-021 ",
                    "The identifier {0} of the {1} passed by {2} to method {3} of service {4} is not recognized by the {5} server",
                    "The system is unable to process the request.",
                    "Update the caller to provide a correct asset identifier."),

    NULL_SEARCH_STRING(400, "OMAG-COMMON-400-022 ",
              "The search string passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a search string.",
              "Correct the code in the caller to provide the search string."),

    CANNOT_DELETE_ELEMENT_IN_USE(400, "OMAG-COMMON-400-023 ",
                       "Method {0} of service {1} is unable to delete {2} identified by {3} because it is still in use.",
                       "The system is unable to process the request.",
                       "Ensure the element is no longer in use before retrying the operation."),

    NULL_CONNECTOR_TYPE_PARAMETER(400, "OMAG-COMMON-400-024 ",
             "The connection value passed on the {0} parameter of the {1} operation has a null connector type",
             "The system is unable to process the request without this value.",
             "Correct the code in the caller to provide the connector type."),

    INSTANCE_WRONG_TYPE_FOR_GUID(404, "OMAG-COMMON-404-001 ",
                                 "The {0} method has retrieved an instance for unique identifier (guid) {1} which is of type {2} rather than type {3}",
                                 "The request fails.",
                                 "Check that the unique identifier is correct and the property server(s) is/are running."),

    METHOD_NOT_IMPLEMENTED(500, "OMAG-COMMON-500-001 ",
                           "Method {0} called by user {1} to OMAG Server {2} is not implemented in service {3}",
                           "The user has issued a valid call to an open metadata REST API that is currently not yet implemented.",
                           "Look to become a contributor or advocate for the ODPi Egeria community to help get this method implemented as soon as possible."),

    CLIENT_SIDE_REST_API_ERROR(503, "OMAG-COMMON-503-001 ",
                               "A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}",
                               "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                               "Look for errors in the local server's console to understand and correct the source of the error.")
    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(OMAGCommonErrorCode.class);


    /**
     * The constructor for OMAGCommonErrorCode expects to be passed one of the enumeration rows defined in
     * OMAGCommonErrorCode above.   For example:
     *
     *     OMAGCommonErrorCode   errorCode = OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    OMAGCommonErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== OMAGCommonErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OMAGCommonErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "OMAGCommonErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
