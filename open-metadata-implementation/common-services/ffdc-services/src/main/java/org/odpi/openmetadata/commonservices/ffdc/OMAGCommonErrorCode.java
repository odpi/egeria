/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


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
public enum OMAGCommonErrorCode implements ExceptionMessageSet
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAG-COMMON-400-001",
                             "The OMAG Server Platform URL is null",
                             "The system is unable to identify the OMAG Server Platform.",
                             "Create a new client and pass the URL for the server on the constructor."),

    SERVER_URL_MALFORMED(400, "OMAG-COMMON-400-002",
                         "The OMAS Server URL {0} is not in a recognized format",
                         "The system is unable to connect to the OMAG Server Platform to fulfill any requests.",
                         "Create a new client and pass the correct URL for the server on the constructor."),

    SERVER_NAME_NOT_SPECIFIED(400, "OMAG-COMMON-400-003",
                              "The OMAG Server name is null",
                              "The system is unable to locate to the OMAG Server to fulfill any request.",
                              "Create a new client and pass the correct name for the server on the constructor."),

    NULL_USER_ID(400, "OMAG-COMMON-400-004",
                 "The user identifier (user id) passed on the {0} operation is null",
                 "The system is unable to process the request without a user id.",
                 "Correct the code in the caller to provide the user id."),

    NULL_GUID(400, "OMAG-COMMON-400-005",
              "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a guid.",
              "Correct the code in the caller to provide the guid."),

    NULL_NAME(400, "OMAG-COMMON-400-006",
              "The name passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a name.",
              "Correct the code in the caller to provide the name on the parameter."),

    NULL_ARRAY_PARAMETER(400, "OMAG-COMMON-400-007",
                         "The array value passed on the {0} parameter of the {1} operation is null or empty",
                         "The system is unable to process the request without this value.",
                         "Correct the code in the caller to provide the array."),

    NEGATIVE_START_FROM(400, "OMAG-COMMON-400-008",
            "The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative",
            "The system is unable to process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list",
            "Correct the code in the caller to provide a non-negative value for the starting point."),

    NEGATIVE_PAGE_SIZE(400, "OMAG-COMMON-400-009",
                        "The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative",
                        "The system is unable to process the request with this invalid value.  It should be zero to return all of the result, or greater than zero to set a maximum",
                        "Correct the code in the caller to provide a non-negative value for the page size."),

    MAX_PAGE_SIZE(400, "OMAG-COMMON-400-010",
            "The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}",
            "The system is unable to process the request with this page size value.",
            "Correct the code in the caller to provide a smaller page size ."),

    NULL_CONNECTION_PARAMETER(400, "OMAG-COMMON-400-011",
                              "The connection object passed on the {0} parameter of the {1} operation is null",
                              "The system is unable to process the request without this connection.",
                              "Correct the code in the caller to provide the connection."),

    NULL_ENUM(400, "OMAG-COMMON-400-012",
              "The enumeration value passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without this enumeration value.",
              "Correct the code in the caller to provide the enumeration value."),

    NULL_TEXT(400, "OMAG-COMMON-400-013",
              "The text field value passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without this text field value.",
              "Correct the code in the caller to provide a value in the text field."),

    NULL_LOCAL_SERVER_NAME(400, "OMAG-COMMON-400-014",
                           "OMAG server has been called with a null local server name",
                           "The system is unable to configure the local server without knowing what it is called.",
                           "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    NULL_OBJECT(400, "OMAG-COMMON-400-015",
                "The object passed on the {0} parameter of the {1} operation is null",
                "The system is unable to process the request without this object.",
                "Correct the code in the caller to provide the object."),

    UNEXPECTED_EXCEPTION(400, "OMAG-COMMON-400-016",
              "An unexpected {0} exception was caught by {1}; error message was {2}",
              "The system is unable to process the request.",
              "Review the error message and other diagnostics created at the same time."),

    NO_REQUEST_BODY(400, "OMAG-COMMON-400-017",
                    "An request by user {0} to method {1} on server {2} had no request body",
                    "The system is unable to process the request without the request body since it contains key information.",
                    "Update the caller to provide the request body."),

    UNRECOGNIZED_TYPE_NAME(400, "OMAG-COMMON-400-018",
                    "The type name {0} passed on method {1} of service {2} is not recognized",
                    "The system is unable to process the request because it does not understand the type.",
                    "Update the caller to provide a correct type name."),

    BAD_SUB_TYPE_NAME(400, "OMAG-COMMON-400-019",
                    "The type name {0} passed on method {1} of service {2} is not a sub-type of {3}",
                    "The system is unable to process the request because one of the parameters is not of the right type.",
                    "Update the caller to provide a valid type name for this request."),

    NOT_IN_THE_ZONE(400, "OMAG-COMMON-400-020",
                      "Asset {0} is not recognized by the {1} service",
                      "The system is unable to process the request because it can not retrieve the asset.",
                      "Update the caller to provide a recognized asset identifier."),

    UNKNOWN_ELEMENT(400, "OMAG-COMMON-400-021",
                    "The identifier {0} of the {1} passed by {2} to method {3} of service {4} is not recognized by the {5} server",
                    "The system is unable to process the request because the identifier is invalid.",
                    "Update the caller to provide a correct asset identifier."),

    NULL_SEARCH_STRING(400, "OMAG-COMMON-400-022",
              "The search string passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a search string.",
              "Correct the code in the caller to provide the search string."),

    CANNOT_DELETE_ELEMENT_IN_USE(400, "OMAG-COMMON-400-023",
                       "Method {0} of service {1} is unable to delete {2} identified by {3} because it is still in use.",
                       "The system is unable to process the request because it may cause other processing to fail.",
                       "Ensure the element is no longer in use before retrying the operation."),

    NULL_CONNECTOR_TYPE_PARAMETER(400, "OMAG-COMMON-400-024",
             "The connection object passed on the {0} parameter of the {1} operation has a null connector type",
             "The system is unable to process the request without knowing the type of the connector that the connection object is requesting.",
             "Correct the code in the caller to provide the connector type embedded in the connection."),

    INSTANCE_WRONG_TYPE_FOR_GUID(404, "OMAG-COMMON-404-001",
                                 "The {0} method has retrieved an instance for unique identifier (guid) {1} which is of type {2} rather than type {3}",
                                 "The request fails because the requested object is not of the right type.",
                                 "Retry the request with the correct unique identifier."),

    METHOD_NOT_IMPLEMENTED(500, "OMAG-COMMON-500-001",
                           "Method {0} called by user {1} to OMAG Server {2} is not implemented in service {3}",
                           "The user has issued a valid call to an open metadata REST API that is currently not yet implemented.",
                           "Look to become a contributor or advocate for the ODPi Egeria community to help get this method implemented as soon as possible."),

    CLIENT_SIDE_REST_API_ERROR(503, "OMAG-COMMON-503-001",
                               "A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}",
                               "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                               "Look for errors in the local server's console to understand and correct the source of the error.")
    ;

    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for OMAGCommonErrorCode expects to be passed one of the enumeration rows defined in
     * OMAGCommonErrorCode above.   For example:
     *
     *     OMAGCommonErrorCode   errorCode = OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OMAGCommonErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
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
        return "OMAGCommonErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
