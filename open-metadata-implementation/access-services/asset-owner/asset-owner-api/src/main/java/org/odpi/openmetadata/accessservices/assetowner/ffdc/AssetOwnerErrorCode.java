/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The AssetOwnerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Owner OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 *     <li>UserAction - describes how a owner should correct the error</li>
 * </ul>
 */
public enum AssetOwnerErrorCode implements ExceptionMessageSet
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-ASSET-OWNER-400-001",
            "The OMAG Server URL is null",
            "The system is unable to connect to the server to retrieve metadata properties because the network address of the platform it is " +
                                     "running on is not supplied.",
            "Ensure the valid URL root for the OMAG Server Platform where the access service is running is passed to the Asset Owner client when it" +
                                     " is created."),
    SERVER_URL_MALFORMED(400, "OMAS-ASSET-OWNER-400-002",
            "The OMAG Server URL {0} is not in a recognized format",
            "The system is unable to connect to the OMAG Server to retrieve metadata properties because the network address of the " +
                                 "platform it is running on is invalid.",
            "Ensure a valid URL root is passed to the Asset Owner client when it is created is a valid network address."),
    NULL_USER_ID(400, "OMAS-ASSET-OWNER-400-003",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-ASSET-OWNER-400-004",
            "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a guid.",
            "Correct the code in the caller to provide the guid."),
    NULL_NAME(400, "OMAS-ASSET-OWNER-400-005",
            "The name passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a name.",
            "Correct the code in the caller to provide the name."),
    NO_CONNECTED_ASSET(400, "OMAS-ASSET-OWNER-400-006",
            "The request for the properties of asset {0} failed with the following message returned: {1}",
            "The system is unable to process the request.",
            "Use the information in the message to understand the nature of the problem and once it is resolved, retry the request to retrieve the " +
                               "asset."),
    TOO_MANY_CONNECTIONS(400, "OMAS-ASSET-OWNER-400-007",
            "The request for a named connection {0} from server {1} returned {2} connections",
            "The system is unable to return the results.",
            "Use the information in the message to understand the nature of the problem and once it is resolved, retry the request " +
                                 "to retrieve the connection."),
    USER_NOT_AUTHORIZED(400, "OMAS-ASSET-OWNER-400-008",
            "User {0} is not authorized to issue the {1} request for open metadata access service {3} on server {4}",
            "The system is unable to process the request because the user is not allowed to call this service.",
            "Verify the access rights of the user and determine if they need to be changed, or the user directed to use a different service."),
    PROPERTY_SERVER_ERROR(400, "OMAS-ASSET-OWNER-400-009",
            "An unexpected error was returned by the property server during {1} request for open metadata access service {2} on server {3}; message was {0}",
            "The system is unable to process the request because of an internal error.",
            "The error message should give some clue as to the nature of the problem.  Once this is resolved, retry the request."),
    NULL_ENUM(400, "OMAS-ASSET-OWNER-400-010",
            "The enumeration value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this enumeration value.",
            "Correct the code in the caller to provide the enum value."),
    NULL_CONNECTION_PARAMETER(400, "OMAS-ASSET-OWNER-400-011",
            "The connection value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this connection value being provided.",
            "Correct the code in the caller to provide the connection."),
    NEGATIVE_START_FROM(400, "OMAS-ASSET-OWNER-400-012",
            "The starting point for the results, passed on the {0} parameter of the {1} operation, is negative",
            "The system is unable to process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list",
            "Correct the code in the caller to provide a non-negative value."),
    EMPTY_PAGE_SIZE(400, "OMAS-ASSET-OWNER-400-013",
            "The number of records to return, passed on the {0} parameter of the {1} operation, is less than 1",
            "The system is unable to process the request with this page size value.",
            "Correct the code in the caller to provide a page size of 1 or greater."),
    BAD_CONFIG(400, "OMAS-ASSET-OWNER-400-014",
            "The Discovery Engine Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property.  The resulting exception of {2} included the following message: {3}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),
    NULL_ARRAY_PARAMETER(400, "OMAS-ASSET-OWNER-400-015",
            "The array value passed on the {0} parameter of the {1} operation is null or empty",
            "The system is unable to process the request without this array value.",
            "Correct the code in the caller to provide the array."),
    INVALID_PROPERTY(400, "OMAS-ASSET-OWNER-400-017",
            "An unsupported property named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}",
            "The system is unable to process the request because it does not understand it.",
            "Correct the types and property names of the properties passed on the request."),
    BAD_OUT_TOPIC_CONNECTION(400, "OMAS-ASSET-OWNER-400-018",
            "The Asset Owner Open Metadata Access Service (OMAS) has been passed an invalid connection for publishing events.  The connection was {0}.  The resulting exception of {1} included the following message: {2}",
            "The access service has not been passed valid configuration for its out topic connection.",
             "Correct the server configuration and restart the server."),

    NULL_LISTENER(400, "OMAS-ASSET-OWNER-400-019",
                  "A null topic listener has been passed by user {0} on method {1}",
                  "There is a coding error in the caller to the Asset Owner OMAS.",
                  "Correct the caller logic and retry the request."),

    SERVER_NOT_AVAILABLE(404, "OMAS-ASSET-OWNER-404-001",
            "The OMAS Service {0} is not available",
            "The system is unable to connect to the OMAG Server.",
            "Check that the OMAG Server Platform URL root and OMAG server name is correct and the OMAS Service is running.  " +
                                 "Retry the request when the OMAS Service is " +
                                 "available."),
    OMRS_NOT_INITIALIZED(404, "OMAS-ASSET-OWNER-404-002",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Asset Owner OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are configured and available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-ASSET-OWNER-404-003",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository.",
            "Check that the server where the Asset Owner OMAS is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "OMAS-ASSET-OWNER-404-004",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata without this interface.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),
    CONNECTION_NOT_FOUND(404, "OMAS-ASSET-OWNER-404-005",
            "The requested connection {0} is not found in OMAG Server {2}",
            "The system is unable to populate the requested connection object because the requests information is not located in the open metadata " +
                                 "repositories.",
            "Check that the connection name is correct and the request is being made to the correct server.  Ensure all members" +
                                 "of the connected cohorts are running.  Retry the request" +
                                 " when the connection is available in the OMAS Service"),
    ASSET_NOT_FOUND(404, "OMAS-ASSET-OWNER-404-006",
            "The requested asset {0} is not found for connection {1}",
            "The system is unable to populate the asset properties object because the OMAG Server is not returning the asset's properties.",
            "Verify that the OMAS Service running and the connection definition in use is linked to the Asset definition in the metadata repository. Then retry the request."),
    PROXY_CONNECTION_FOUND(404, "OMAS-ASSET-OWNER-404-007",
            "Only an entity proxy for requested connection {0} is found in the open metadata server {1}, error message was: {2}",
            "The system is unable to populate the requested connection object because only a stub of the object from a relationship was found.",
            "Check that the connection name is correct and the caller is connecting to the correct OMAG Server Platform and server. Verify that " +
                                   "all the members of the cohorts that this server is connecting to are running.  " +
                                   "Retry the request when the connection is available in the OMAS Service"),
    UNKNOWN_ASSET(404, "OMAS-ASSET-OWNER-404-008",
            "The asset with unique identifier {0} is not found for method {1} of access service {2} in open metadata server {3}, error message was: {4}",
            "The system is unable to update information associated with the asset because none of the connected open metadata repositories recognize the asset's unique identifier.",
            "The unique identifier of the asset is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),
    PROXY_ENTITY_FOUND(404, "OMAS-ASSET-OWNER-404-009",
                       "Only an entity proxy for requested {0} object with unique identifier (guid) {1} is found in the open metadata server {2}, error message was: {3}",
                       "The system is unable to populate the requested connection object.",
                       "Check that the connection name and the OMAG Server URL are correct.  Retry the request when the connection is available in the OMAS Service"),
    MULTIPLE_RELATIONSHIPS_FOUND(404, "OMAS-ASSET-OWNER-404-010",
                                 "Multiple {0} relationships are connected to the {1} entity with unique identifier {2}: the relationship identifiers are {3}; the calling method is {4} and the server is {5}",
                                 "The system is unable to process a request because multiple relationships have been discovered and it is unsure which relationship to follow.",
                                 "Investigate why multiple relationships exist.  Then retry the request once the issue is resolved."),
    UNKNOWN_ENTITY(404, "OMAS-ASSET-OWNER-404-011",
                   "The {0} with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}",
                   "The system is unable to update information associated with the entity because none of the connected open metadata repositories " +
                           "recognize the entity's unique identifier.",
                   "The unique identifier of the entity is supplied by the caller.  Verify that the caller's logic is correct, and that there are " +
                           "no errors being reported by the open metadata repositories. Once all errors have been resolved, retry the request."),
    NO_RELATIONSHIPS_FOUND(404, "OMAS-ASSET-OWNER-404-012",
                           "No {0} relationships are connected to the {1} entity with unique identifier {2}: the calling method is {3} and the server is {4}",
                           "The system is unable to process a request because no relationships have been discovered and it is unable to retrieve all the information it needs.",
                           "Check that the unique identifier is correct and the property server(s) supporting the assets is/are running."),
    NULL_ENTITY_RETURNED(404, "OMAS-ASSET-OWNER-404-013",
                         "A null entity was returned to method {0} of server {1} during a request to create a new entity of type {2} (guid {3}) and properties of: {4}",
                         "The system is unable to process a request.",
                         "This may be a logic error or a configuration error.  Look for errors in the server's audit log and console to understand and correct the source of the error."),
    MULTIPLE_ENTITIES_FOUND(404, "OMAS-ASSET-OWNER-404-014",
                            "Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter isd {4} and the server is {5}",
                            "The system is unable to process a request because multiple entities have been discovered and it is unsure which " +
                                    "one to use.",
                            "Investigate why multiple entities exist.  Then retry the request once the issue is resolved."),
    NULL_CONNECTION_RETURNED(500, "OMAS-ASSET-OWNER-500-001",
                             "The requested connection named {0} is not returned by the open metadata Server {1}",
                             "The system is unable to create a connector because the OMAG Server is not returning the Connection properties.",
                             "Verify that the OMAS server running and the connection definition is correctly configured."),
    NULL_CONNECTOR_RETURNED(500, "OMAS-ASSET-OWNER-500-002",
                            "The requested connector for connection named {0} is not returned by the OMAG Server {1}",
                            "The system is unable to create a connector.",
                            "Verify that the OMAS server is running and the connection definition is correctly configured."),
    NULL_END2_RETURNED(500, "OMAS-ASSET-OWNER-500-003",
                       "A relationship of type {0} and unique identifier of {1} has a null entity proxy 2.  Relationship contents are: {2}",
                       "The system is unable to retrieve the asset.",
                       "This is a logic error in the open metadata repositories as it is not valid to have a relationship without two entity proxies that represent the entities that is connects.  Gather as much information about the usage of the metadata.  Use the metadata collection id to identify which server owns the relationship and raise an issue."),

    UNABLE_TO_SEND_EVENT(500, "OMAS-ASSET-OWNER-500-004",
                         "An unexpected exception occurred when sending an event through connector {0} to the Asset Owner OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {3}",
                         "The access service has issued a call to publish an event on its Out Topic and it failed.",
                         "Look for errors in the event bus to understand why this is failing.  When the event bus is operating correctly, event will" +
                                 " begin to be published again.  In the meantime, events are being lost."),

    WRONG_TYPE_OF_CONNECTOR(500, "OMAS-ASSET-OWNER-500-007",
                            "The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is " +
                                    "not of the required type. It should be an instance of {4}",
                            "The system is unable to create the required connector which means some of its services will not work.",
                            "Verify that the OMAG server is running and the OMAS service is correctly configured."),

    NULL_RESPONSE_FROM_API(503, "OMAS-ASSET-OWNER-503-001",
            "A null response was received from REST API call {0} to server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-ASSET-OWNER-503-002",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(503, "OMAS-ASSET-OWNER-503-003",
            "The access service has not been initialized and can not support REST API call {0}",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server.")
    ;

    private static final long    serialVersionUID = 1L;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for AssetOwnerErrorCode expects to be passed one of the enumeration rows defined in
     * AssetOwnerErrorCode above.   For example:
     *
     *     AssetOwnerErrorCode   errorCode = AssetOwnerErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    AssetOwnerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    @Override
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
    @Override
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
        return "AssetOwnerErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
