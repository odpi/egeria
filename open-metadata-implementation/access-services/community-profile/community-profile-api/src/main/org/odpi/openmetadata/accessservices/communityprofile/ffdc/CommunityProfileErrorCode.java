/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The CommunityProfileErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Community Profile OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum CommunityProfileErrorCode
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-COMMUNITY-PROFILE-400-001 ",
            "The OMAS Server URL is null",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the client when it is created."),
    SERVER_URL_MALFORMED(400, "OMAS-COMMUNITY-PROFILE-400-002 ",
            "The OMAS Server URL {0} is not in a recognized format",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the client when it is created."),
    NULL_USER_ID(400, "OMAS-COMMUNITY-PROFILE-400-003 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-COMMUNITY-PROFILE-400-004 ",
            "The unique identifier (guid) for a {0} passed on the {1} parameter of the {2} operation is null",
            "The system is unable to process the request without a guid.",
            "Correct the code in the caller to provide the guid."),
    NULL_NAME(400, "OMAS-COMMUNITY-PROFILE-400-005 ",
            "The name passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a name.",
            "Correct the code in the caller to provide the name."),
    OBJECT_LOCKED_AS_REFERENCE(400, "OMAS-COMMUNITY-PROFILE-400-007 ",
            "User {0} is not authorized to issue the {1} request for open metadata access service {2} on server {3} because the requested object {4} of type {5} is locked by metadata collection id {6} ({7})",
            "The system is unable to process the request because the user does not have the rights to change this data.",
            "Request that the owner of the data makes the requested changes."),
    USER_NOT_AUTHORIZED(400, "OMAS-COMMUNITY-PROFILE-400-008 ",
            "User {0} is not authorized to issue the {1} request for open metadata access service {3} on server {4}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    PROPERTY_SERVER_ERROR(400, "OMAS-COMMUNITY-PROFILE-400-009 ",
            "An unexpected error was returned by the property server during {1} request for open metadata access service {2} on server {3}; message was {0}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    NULL_ENUM(400, "OMAS-COMMUNITY-PROFILE-400-010 ",
            "The enumeration value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this enumeration value.",
            "Correct the code in the caller to provide the name."),
    NULL_TEXT(400, "OMAS-COMMUNITY-PROFILE-400-011 ",
            "The text field value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this text field value.",
            "Correct the code in the caller to provide the name."),
    NEGATIVE_START_FROM(400, "OMAS-COMMUNITY-PROFILE-400-012 ",
            "The starting point for the results, passed on the {0} parameter of the {1} operation, is negative",
            "The system is unable to process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list",
            "Correct the code in the caller to provide a non-negative value."),
    NEGATIVE_PAGE_SIZE(400, "OMAS-COMMUNITY-PROFILE-400-013 ",
            "The number of records to return, passed on the {0} parameter of the {1} operation, is less than 0",
            "The system is unable to process the request with this page size value.  The page size should be 0 to mean no restriction or a positive number.",
            "Correct the code in the caller to provide a page size of 0 or greater."),
    WRONG_TYPE_OF_OBJECT(400, "OMAS-COMMUNITY-PROFILE-400-014 ",
            "The unique identifier (guid) {0} passed in parameter {1} of the {2} operation, is of type {3} when it should be of type {4}",
            "The guid is supposed to identify a specific type object (or subclass) from the property server is actually for a different type of object.",
            "Correct the guid passed in the caller to provide the identifier of an object of the right type (or use a different call."),
    QUALIFIED_NAME_NOT_UNIQUE(404, "OMAS-COMMUNITY-PROFILE-400-015 ",
            "The qualified name passed in the parameter {1} of the {2} operation is not unique.",
            "The system is unable to create the requested object because the qualified name is not unique.",
            "Correct the qualified name passed on the call so it is a unique value."),
    SERVER_NOT_AVAILABLE(404, "OMAS-COMMUNITY-PROFILE-404-001 ",
            "The OMAS Service {0} is not available",
            "The system is unable to connect to the OMAS Server.",
            "Check that the OMAS Server URL is correct and the OMAS Service is running.  Retry the request when the OMAS Service is available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-COMMUNITY-PROFILE-404-003 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Community Profile OMAS is running initialized correctly.  " +
                       "Correct any errors discovered and retry the request when the open metadata services are available."),
    MULTIPLE_INSTANCES_FOUND(404, "OMAS-COMMUNITY-PROFILE-404-008 ",
            "Multiple objects with the same unique identifier {0} have been found.  They are: (1}",
            "The system is unable to populate the connected asset properties because the open metadata repositories have many links to assets defined for this connection.  The service is unsure which one to use.",
            "Investigate why multiple assets are connected to this connection.  If the related connector is able to serve up many assets then create a virtual asset to cover its collection of assets and link it to the connection. Then link the assets currently linked to this connection to the virtual asset instead. Then retry the request."),
    INSTANCE_NOT_FOUND_BY_GUID(404, "OMAS-COMMUNITY-PROFILE-404-010 ",
            "The {0} method is not able to retrieve a {1} object with a unique identifier (guid) of {2} for userId {3} from server {4}",
            "The object is not stored in the property server.",
            "Check that the unique identifier is correct and the property server(s) supporting the assets is/are running."),
    MULTIPLE_USER_IDENTITIES_FOUND(404, "OMAS-COMMUNITY-PROFILE-404-011 ",
            "Multiple user identity entities have been found for userId {0}.  This includes {1}",
            "There are multiple user identity entities for the requesting user.  The service is unsure which one to use.",
            "Remove all but one of user identity entities for this userId and retry the request."),
    NO_PROFILE_FOR_USER(404, "OMAS-COMMUNITY-PROFILE-404-012 ",
            "No profile exists for userId {0}.",
            "The service was unable to locate a profile for this user.",
            "Use the createMyProfile or createPersonalProfile method to add a profile and retry the request."),
    INSTANCE_WRONG_TYPE_FOR_GUID(404, "OMAS-COMMUNITY-PROFILE-404-013 ",
            "The {0} method has retrieved a object for unique identifier (guid) {1} which is of type {2} rather than type {3)",
            "The service is not able to return the requested object.",
            "Check that the unique identifier is correct and the property server(s) supporting the community profile is/are running."),
    INSTANCE_NOT_DELETED(404, "OMAS-COMMUNITY-PROFILE-404-010 ",
            "The service is not able to delete an instance of type {0} with an guid of {1}; the error message was {2}",
            "The instance is not deletable.",
            "Check that the guid and type information are correct and the property server(s) supporting the service is/are running."),
    OMRS_NOT_INITIALIZED(500, "OMAS-COMMUNITY-PROFILE-500-001 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Community Profile OMAS is running has initialized correctly.  " +
                "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(500, "OMAS-COMMUNITY-PROFILE-500-002 ",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),
    NULL_END2_RETURNED(500, "OMAS-COMMUNITY-PROFILE-500-003 ",
            "A relationship of type {0} and unique identifier of {1} has a null entity proxy 2.  Relationship contents are: {2}",
            "The system is unable to retrieve the asset.",
            "This is a logic error in the open metadata repositories as it is not valid to have a relationship without two entity proxies that represent the entities that is connects.  Gather as much information about the usage of the metadata.  Use the metadata collection id to identify which server owns the relationship and raise an issue."),
    UNABLE_TO_CREATE_USER_IDENTITY(500, "OMAS-COMMUNITY-PROFILE-500-004 ",
            "Unable to create new user identity object for user id {0}",
            "The system returned a null from the addEntity request.",
            "Verify that the OMAS server running and their are no errors on the server side."),
    NULL_RESPONSE_FROM_API(503, "OMAS-COMMUNITY-PROFILE-503-001 ",
            "A null response was received from REST API call {0} to server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-COMMUNITY-PROFILE-503-002 ",
            "A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}",
            "The client has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Review the error message to determine the cause of the error.  Check that the server is running an the URL is correct. Look for errors in the local server's console to understand and correct the cause of the error. Then rerun the request"),
    SERVICE_NOT_INITIALIZED(503, "OMAS-COMMUNITY-PROFILE-503-003 ",
            "The access service has not been initialized for server {0} and can not support REST API call {1}",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    EXCEPTION_RESPONSE_FROM_API(503, "OMAS-COMMUNITY-PROFILE-503-004 ",
            "A {0} exception was received from REST API call {1} to server {2}: error message was: {3}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received an exception response.",
            "The error message should indicate the cause of the error.  Otherwise look for errors in the remote server's audit log and console to understand and correct the source of the error.")
    ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(CommunityProfileErrorCode.class);


    /**
     * The constructor for CommunityProfileErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
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
    CommunityProfileErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== AssetConsumerErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> AssetConsumerErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "AssetConsumerErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
