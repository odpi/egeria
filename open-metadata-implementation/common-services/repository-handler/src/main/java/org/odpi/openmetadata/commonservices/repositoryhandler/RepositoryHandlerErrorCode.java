/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The RepositoryHandlerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Repository Handler Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum RepositoryHandlerErrorCode
{
    INVALID_PROPERTY(400, "REPOSITORY-HANDLER-400-001 ",
            "An unsupported property named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}",
            "The system is unable to process the request.",
            "Correct the types and property names of the properties passed on the request."),
    USER_NOT_AUTHORIZED(400, "REPOSITORY-HANDLER-400-002 ",
            "User {0} is not authorized to issue the {1} request for open metadata access service {2} on server {3}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    PROPERTY_SERVER_ERROR(400, "REPOSITORY-HANDLER-400-003 ",
            "An unexpected error was returned by the metadata server during {1} request for open metadata access service {2} on server {3}; message was {0}",
            "The system is unable to process the request.",
            "Verify the sanity of the server.  This is probably a logic error.  If you can not work out what happened, ask the ODPi Egeria community for help."),
    INVALID_PROPERTY_VALUE(400, "REPOSITORY-HANDLER-400-004 ",
            "The property named {0} with value of {1} supplied on method {2} does not match the stored value of {3} for entity {4}",
            "The system is unable to process the request.",
            "Correct the values of the properties passed on the request and retry."),
    DUPLICATE_CREATE_REQUEST(400, "REPOSITORY-HANDLER-400-005 ",
            "Unable to create a new {0} with {1} of {2} as there is already an entity of that name with guid {3}",
            "The system is unable to create the requested entity because there is an entity of the same name already defined.",
            "Either use the existing entity or change the name for the new one and re-run the create request."),
    INSTANCE_WRONG_TYPE_FOR_GUID(404, "REPOSITORY-HANDLER-404-001 ",
            "The {0} method has retrieved a object for unique identifier (guid) {1} which is of type {2} rather than type {3}",
            "The service is not able to return the requested object.",
            "Check that the unique identifier is correct and the metadata server(s) supporting the service is running."),
    OMRS_NOT_INITIALIZED(404, "REPOSITORY-HANDLER-404-002 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Discovery Engine OMAS is running initialized correctly.  " +
                      "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "REPOSITORY-HANDLER-404-003 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system called a function that has not been enabled in this open metadata server.",
            "Check that the server where the Discovery Engine OMAS is running initialized correctly.  " +
                       "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "REPOSITORY-HANDLER-404-004 ",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),
    PROXY_ENTITY_FOUND(404, "REPOSITORY-HANDLER-404-005 ",
            "Only an entity proxy for requested {0} object with unique identifier (guid) {1} is found in the open metadata server {2}, error message was: {3}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the OMAS Server URL are correct.  Retry the request when the connection is available in the OMAS Service"),
    MULTIPLE_RELATIONSHIPS_FOUND(404, "REPOSITORY-HANDLER-404-006 ",
            "Multiple {0} relationships are connected to the {1} entity with unique identifier {2}: the relationship identifiers are {3}; the calling method is {4} and the server is {5}",
            "The system is unable to process a request because multiple relationships have been discovered and it is unsure which relationship to follow.",
            "Investigate why multiple relationships exist.  Then retry the request once the issue is resolve."),
    UNKNOWN_ENTITY(404, "REPOSITORY-HANDLER-404-007 ",
            "The {0} with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}",
            "The system is unable to update information associated with the asset because none of the connected open metadata repositories recognize the asset's unique identifier.",
            "The unique identifier of the asset is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),
    NO_RELATIONSHIPS_FOUND(404, "REPOSITORY-HANDLER-404-008 ",
            "No {0} relationships are connected to the {1} entity with unique identifier {2}: the calling method is {3} and the server is {4}",
            "The system is unable to process a request because no relationships have been discovered and it is unable to retrieve all of the information it needs.",
            "Check that the unique identifier of the entity is correct and the metadata server supporting the request is running.  If these are " +
                                   "both correct, investigate why the relationships are missing.  It is likely that the process responsible for " +
                                   "the creation of the relationships has not yet run, or failed part way through its operation."),
    NULL_ENTITY_RETURNED(404, "REPOSITORY-HANDLER-404-009 ",
            "A null entity was returned to method {0} of server {1} during a request for entity of type {2} (guid {3}) and properties of: {4}",
            "The system is unable to process a request .",
            "This may be a logic error or a configuration error.  Look for errors in the server's audit log and console to understand and correct the source of the error."),
    MULTIPLE_ENTITIES_FOUND(404, "REPOSITORY-HANDLER-404-010 ",
            "Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}",
            "The system is unable to process a request because multiple relationships have been discovered and it is unsure which relationship to follow.",
            "Investigate why multiple relationships exist.  Then retry the request once the issue is resolve."),
    NULL_ENTITY_RETURNED_FOR_CLASSIFICATION(404, "REPOSITORY-HANDLER-404-011 ",
            "A null entity was returned to method {0} of server {1} during a request to add a classification of type {4} (guid {3}) to entity {2} with properties of: {5}",
            "The system is unable to process a request .",
            "This may be a logic error or a configuration error.  Look for errors in the server's audit log and console to understand and correct the source of the error."),
    ;


    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(RepositoryHandlerErrorCode.class);


    /**
     * The constructor for RepositoryHandlerErrorCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineErrorCode above.   For example:
     *
     *     RepositoryHandlerErrorCode   errorCode = RepositoryHandlerErrorCode.ASSET_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    RepositoryHandlerErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== RepositoryHandlerErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> RepositoryHandlerErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "RepositoryHandlerErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
