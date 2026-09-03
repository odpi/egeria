/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OMAGCommonErrorCode is used to define first failure data capture (FFDC) for common errors.  It belongs to the FFDC Services module
 * and should not be used by other modules.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA.   Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>403 - forbidden</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OMAGCommonErrorCode implements ExceptionMessageSet
{
    /**
     * OMAGCommonAuditCode - The OMAG Server Platform URL is null
     */
    SERVER_URL_NOT_SPECIFIED(400, "OMAG-COMMON-400-001",
                             "The OMAG Server Platform URL is null or blank",
                             "The system cannot identify the OMAG Server Platform.",
                             "Create a new client and pass the URL for the server on the constructor.",
                             "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-003 - The OMAG Server name is null
     */
    SERVER_NAME_NOT_SPECIFIED(400, "OMAG-COMMON-400-003",
                              "The OMAG Server name is null or blank",
                              "The system cannot locate to the OMAG Server to fulfill any request.",
                              "Create a new client and pass the correct name for the server on the constructor.",
                              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-004 - The user identifier (user id) passed on the {0} operation is null
     */
    NULL_USER_ID(400, "OMAG-COMMON-400-004",
                 "The user identifier (user id) passed on the {0} operation is null",
                 "The system cannot process the request without a user id.",
                 "Correct the code in the caller to provide the user id.",
                 "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-005 - The unique identifier (guid) passed on the {0} parameter of the {1} operation is null
     */
    NULL_GUID(400, "OMAG-COMMON-400-005",
              "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
              "The system cannot process the request without a guid.",
              "Correct the code in the caller to provide the guid.",
              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-006 - The name passed on the {0} parameter of the {1} operation is null
     */
    NULL_NAME(400, "OMAG-COMMON-400-006",
              "The name passed on the {0} parameter of the {1} operation is null",
              "The system cannot process the request without a name.",
              "Correct the code in the caller to provide the name on the parameter.",
              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-007 - The array value passed on the {0} parameter of the {1} operation is null or empty
     */
    NULL_ARRAY_PARAMETER(400, "OMAG-COMMON-400-007",
                         "The array value passed on the {0} parameter of the {1} operation is null or empty",
                         "The system cannot process the request without this value.",
                         "Correct the code in the caller to provide the array.",
                         "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-008 - The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative
     */
    NEGATIVE_START_FROM(400, "OMAG-COMMON-400-008",
            "The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative",
            "The system cannot process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list",
            "Correct the code in the caller to provide a non-negative value for the starting point.",
            "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-009 - The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative
     */
    NEGATIVE_PAGE_SIZE(400, "OMAG-COMMON-400-009",
                        "The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative",
                        "The system cannot process the request with this invalid value.  It should be zero to return all the result, or greater than zero to set a maximum",
                        "Correct the code in the caller to provide a non-negative value for the page size.",
                        "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-010 - The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}
     */
    MAX_PAGE_SIZE(400, "OMAG-COMMON-400-010",
            "The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}",
            "The system cannot process the request with this page size value.",
            "Correct the code in the caller to provide a smaller page size .",
            "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-011 - The connection object passed on the {0} parameter of the {1} operation is null
     */
    NULL_CONNECTION_PARAMETER(400, "OMAG-COMMON-400-011",
                              "The connection object passed on the {0} parameter of the {1} operation is null",
                              "The system cannot process the request without this connection.",
                              "Correct the code in the caller to provide the connection.",
                              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-012 - The enumeration value passed on the {0} parameter of the {1} operation is null
     */
    NULL_ENUM(400, "OMAG-COMMON-400-012",
              "The enumeration value passed on the {0} parameter of the {1} operation is null",
              "The system cannot process the request without this enumeration value.",
              "Correct the code in the caller to provide the enumeration value.",
              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-013 - The text field value passed on the {0} parameter of the {1} operation is null
     */
    NULL_TEXT(400, "OMAG-COMMON-400-013",
              "The text field value passed on the {0} parameter of the {1} operation is null",
              "The system cannot process the request without this text field value.",
              "Correct the code in the caller to provide a value in the text field.",
              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-015 - The object passed on the {0} parameter of the {1} operation is null
     */
    NULL_OBJECT(400, "OMAG-COMMON-400-015",
                "The object passed on the {0} parameter of the {1} operation is null",
                "The system cannot process the request without this object.",
                "Correct the code in the caller to provide the object.",
                "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-016 - An unexpected {0} exception was caught by {1}; error message was {2}
     */
    UNEXPECTED_EXCEPTION(400, "OMAG-COMMON-400-016",
                         "An unexpected {0} exception was caught by {1}; error message was {2}",
                         "The system cannot process the request and has returned an exception to the caller.",
                         "Review the error message.  Also look up its full message definition which includes the system action " +
                                 "and user action.  This is most likely to describe the correct action to take to resolve the error.  " +
                                 "If that does not help, look for other diagnostics created at the same time.  Also validate that the " +
                                 "caller is a valid client of this server and is operating correctly.",
                                 "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-017 - An request by user {0} to method {1} on server {2} had no request body
     */
    NO_REQUEST_BODY(400, "OMAG-COMMON-400-017",
                    "An request by user {0} to method {1} on server {2} had no request body",
                    "The system cannot process the request without the request body since it contains key information.",
                    "Update the caller to provide the request body.",
                    "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-018 - The type name {0} passed on method {1} of service {2} is not recognized
     */
    UNRECOGNIZED_TYPE_NAME(400, "OMAG-COMMON-400-018",
                    "The type name {0} passed on method {1} of service {2} is not recognized",
                    "The system cannot process the request because it does not understand the type.",
                    "Update the caller to provide a correct type name.",
                    "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-019 - The type name {0} passed on method {1} of service {2} is not a sub-type of {3}
     */
    BAD_SUB_TYPE_NAME(400, "OMAG-COMMON-400-019",
                    "The type name {0} passed on method {1} of service {2} is not a sub-type of {3}",
                    "The system cannot process the request because one of the parameters is not of the right type.",
                    "Update the caller to provide a valid type name for this request.",
                    "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-021 - The identifier {0} of the {1} passed by {2} to method {3} of service {4} is not recognized by the {5} server
     */
    UNKNOWN_ELEMENT(400, "OMAG-COMMON-400-021",
                    "The identifier {0} of the {1} passed by {2} to method {3} of service {4} is not recognized by the {5} server",
                    "The system cannot process the request because the identifier is invalid.",
                    "Update the caller to provide a correct identifier.",
                    "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-022 - The search string passed on the {0} parameter of the {1} operation is null
     */
    NULL_SEARCH_STRING(400, "OMAG-COMMON-400-022",
              "The search string passed on the {0} parameter of the {1} operation is null",
              "The system cannot process the request without a search string.",
              "Correct the code in the caller to provide the search string.",
              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-023 - Method {0} of service {1} cannot delete {2} identified by {3} because it is still in use.
     */
    CANNOT_DELETE_ELEMENT_IN_USE(400, "OMAG-COMMON-400-023",
              "Method {0} of service {1} cannot delete {2} identified by {3} because it is still in use",
              "The system cannot process the request because it may cause other processing to fail.",
              "Ensure the element is no longer in use before retrying the operation.",
              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-024 - The connection object passed on the {0} parameter of the {1} operation has a null connector type
     */
    NULL_CONNECTOR_TYPE_PARAMETER(400, "OMAG-COMMON-400-024",
             "The connection object passed on the {0} parameter of the {1} operation has a null connector type",
             "The system cannot process the request without knowing the type of the connector that the connection object is requesting.",
             "Correct the code in the caller to provide the connector type embedded in the connection.",
             "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-026 - The {0} element {1} is expected to be anchored to {2} but is in fact anchored to {3}. Method {4} cannot proceed
     */
    WRONG_ANCHOR_GUID(400, "OMAG-COMMON-400-026",
               "The {0} element {1} is expected to be anchored to {2} but is in fact anchored to {3}. Method {4} cannot proceed",
               "The system cannot process the request because the requested object is not anchored to the expected element.",
               "Check the code in the caller to verify it is providing either the correct identifier of the object or the correct" +
                       "anchor identifier since this is the most likely cause of the error.  However, it is possible that there is an " +
                       "error in the way that the anchor GUID was set up in the element.  If this is the case, it is necessary to trace " +
                       "back to find how the element was created and then look at where the error was introduced.",
                       "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-028 - The search string passed on the {0} parameter of the {1} operation is invalid and results in a {2} exception when executed.  The error message is {3}
     */
    INVALID_SEARCH_STRING(400, "OMAG-COMMON-400-028",
                       "The search string passed on the {0} parameter of the {1} operation is invalid and results in a {2} exception when executed.  The error message is {3}",
                       "The system cannot process the request with this search string.",
                       "Correct the code in the caller to provide a valid regular expression search string.",
                       "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-029 - The properties object passed on the {0} operation is either null or not of the correct {1} class
     */
    INVALID_PROPERTIES_OBJECT(400, "OMAG-COMMON-400-029",
                          "The properties object passed on the {0} operation is either null or not of the correct {1} class",
                          "The system cannot proceed because it can not interpret the properties needed to execute the request.",
                          "Correct the code in the caller to provide a valid properties object.",
                          "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-030 - The {0} operation is only supported by {1} servers and server {2} is a {3}
     */
    INVALID_CALL_FOR_SERVER(400, "OMAG-COMMON-400-030",
                              "The {0} operation is only supported by {1} servers and server {2} is a {3}",
                              "The system cannot proceed because the server called does not support the request.",
                              "Correct the code in the caller to call the correct server.",
                              "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-031 - A request by user {0} to method {1} on server {2} had no request body.  Add a request body of type {3}
     */
    NO_REQUEST_BODY_FOR_CLASS(400, "OMAG-COMMON-400-031",
                    "A request by user {0} to method {1} on server {2} had no request body.  Add a request body of type {3}",
                    "The system cannot process the request without the request body since it contains key information.  It returns with an exception.",
                    "Update the caller to provide the request body of the recommended type.",
                    "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-032 - The value {0} passed on the {1} parameter of the {2} operation is invalid
     */
    INVALID_PARAMETER(400, "OMAG-COMMON-400-032",
                "The value {0} passed on the {1} parameter of the {2} operation is invalid",
                "The system cannot process the request without a valid value for this parameter.",
                "Correct the code in the caller to provide a valid value.",
                "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-400-033 - Unable to create a client to call the {0} server at {1}; the error was {2}
     */
    UNABLE_TO_CREATE_CLIENT(400, "OMAG-COMMON-400-033",
                "Unable to create a client to call the {0} server at {1}; the error was {2}",
                "The system cannot issue requests to the server because the client that calls it could not be built.",
                "Use the error message to determine what is wrong.  The server name and platform URL root are the usual causes, but the client also needs whatever it authenticates with - a secrets store, for example - to be reachable and to hold credentials the server accepts.",
                "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-404-001 - The {0} method has retrieved an instance for unique identifier (guid) {1} which is of type {2} rather than type {3}
     */
    INSTANCE_WRONG_TYPE_FOR_GUID(404, "OMAG-COMMON-404-001",
                                 "The {0} method has retrieved an instance for unique identifier (guid) {1} which is of type {2} rather than type {3}",
                                 "The request fails because the requested object is not of the right type.",
                                 "Retry the request with the correct unique identifier (or a different request suitable for the type of " +
                                         "instance requested).",
                                         "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-409-001 - Method {0} of service {1} is not able to create an instance of type {2} because parameter name {3} is
     * defined as a unique property and value {4} is not available for use
     */
    UNIQUE_NAME_ALREADY_IN_USE(409, "OMAG-COMMON-409-001",
                               "Method {0} of service {1} is not able to create an instance of type {2} because parameter name {3} is " +
                                       "defined as a unique property and value {4} is not available for use",
                               "The system cannot process the request because the unique property for this new entity " +
                                       "is not permitted either because it is a reserved value, or it is already in use.",
                               "Retry the request with a different unique parameter name.",
                               "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-500-001 - Method {0} called by user {1} to OMAG Server {2} is not implemented in service {3}
     */
    METHOD_NOT_IMPLEMENTED(500, "OMAG-COMMON-500-001",
                           "Method {0} called by user {1} to OMAG Server {2} is not implemented in service {3}",
                           "The user has issued a valid call to an open metadata REST API that is currently not yet implemented.",
                           "Look to become a contributor or advocate for the Egeria community to help get this method implemented as soon as possible.",
                           "https://egeria-project.org/services/ffdc-services/"),

    /**
     * OMAG-COMMON-503-001 - A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "OMAG-COMMON-503-001",
                               "A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}",
                               "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                               "Look for errors in the local server's console to understand and correct the source of the error.",
                               "https://egeria-project.org/services/ffdc-services/")
    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;
    private final String url;

    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OMAGCommonErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor for OMAGCommonErrorCode expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OMAGCommonErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode,
                                              errorMessageId,
                                              errorMessage,
                                              systemAction,
                                              userAction,
                                              url);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                                      errorMessageId,
                                                                                      errorMessage,
                                                                                      systemAction,
                                                                                      userAction,
                                                                                      url);

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
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
