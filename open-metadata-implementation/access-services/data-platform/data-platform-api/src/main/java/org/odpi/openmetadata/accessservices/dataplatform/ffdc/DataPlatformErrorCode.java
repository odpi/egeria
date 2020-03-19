/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DataPlatformErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Data Platform OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum DataPlatformErrorCode
{
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-DATA-PLATFORM-400-001 ",
            "The OMAS Server URL is null",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the AssetConsumer when it is created."),
    SERVER_URL_MALFORMED(400, "OMAS-DATA-PLATFORM-400-002 ",
            "The OMAS Server URL {0} is not in a recognized format",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the AssetConsumer when it is created."),
    NULL_USER_ID(400, "OMAS-DATA-PLATFORM-400-003 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-DATA-PLATFORM-400-004 ",
            "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a guid.",
            "Correct the code in the caller to provide the guid."),
    NULL_NAME(400, "OMAS-DATA-PLATFORM-400-005 ",
            "The name passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a name.",
            "Correct the code in the caller to provide the name."),
    USER_NOT_AUTHORIZED(400, "OMAS-DATA-PLATFORM-400-008 ",
            "User {0} is not authorized to issue the {1} request for open metadata access service {3} on server {4}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    PROPERTY_SERVER_ERROR(400, "OMAS-DATA-PLATFORM-400-009 ",
            "An unexpected error was returned by the property server during {1} request for open metadata access service {2} on server {3}; message was {0}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    NULL_ENUM(400, "OMAS-DATA-PLATFORM-400-010 ",
            "The enumeration value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this enumeration value.",
            "Correct the code in the caller to provide the name."),
    BAD_CONFIG(400,"OMAS-DATA-PLATFORM-400-011 ",
            "The Data Platform Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property.  The resulting exception of {2} included the following message: {3}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),
    INVALID_EVENT_FORMAT(400, "OMAS-DATA-PLATFORM-400-012",
            "Event{0} could not be parsed",
            "The system is unable to process the request.",
            "Verify the event published to the topic."),
    PUBLISH_EVENT_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-013",
            "Event {0} could not be published: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PROCESS_EVENT_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-014",
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    ADD_ENTITY_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-015",
            "Entity {0} could not be added. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_RELATIONSHIP_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-016",
            "Relationship {0} could not be added. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_ENTITY_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-017",
            "Entity matching criteria [{0}] could not be fetched. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_RELATIONSHIP_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-018",
            "Relationship {0} could not be fetched. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    PARSE_EVENT_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-019",
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_CLASSIFICATION_EXCEPTION(400, "OMAS-DATA-PLATFORM-400-020",
            "Unable to create classification {0} for entity of type {1}. Error: {2}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    NULL_TOPIC_CONNECTOR(400, "OMAS-DATA-PLATFORM-400-021",
                         "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
                         "The local server will not connect to the cohort.",
                         "The connection to the connector is configured in the server configuration.  " +
                                 "Review previous error messages to determine the precise error in the " +
                                 "start up configuration. " +
                                 "Correct the configuration and reconnect the server to the cohort. "),
    SERVER_NOT_AVAILABLE(404, "OMAS-DATA-PLATFORM-404-001 ",
            "The OMAS Service {0} is not available",
            "The system is unable to connect to the OMAS Server.",
            "Check that the OMAS Server URL is correct and the OMAS Service is running.  Retry the request when the OMAS Service is available."),
    OMRS_NOT_INITIALIZED(404, "OMAS-DATA-PLATFORM-404-002 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Data Platform OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-DATA-PLATFORM-404-003 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository.",
            "Check that the server where the Data Platform OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "OMAS-DATA-PLATFORM-404-004 ",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),
    ENTITY_NOT_FOUND_EXCEPTION(404, "OMAS-DATA-PLATFORM-404-005 ",
            "The entity matching criteria [{0}={1}] was not found.",
            "The system is unable to process the request.",
            "Correct the request payload submitted."),
    REGISTRATION_EXCEPTION(500, "OMAS-DATA-PLATFORM-500-501 ",
            "The Data Platform access service cannot register the external data platform.",
            "The server cannot process the registration request.",
            "Verify the registration event to this access service."),
    NO_MATCHING_ENTITY_EXCEPTION(500, "OMAS-DATA-PLATFORM-500-502 ",
            "No entity matches the criteria: {0}",
            "The system is unable to process the request.",
            "Check the criteria used for matching."),
    MULTIPLE_MATCHING_ENTITIES_EXCEPTION(500, "OMAS-DATA-PLATFORM-500-503 ",
            "More than one entity match the criteria: {0}",
            "The system is unable to process the request.",
            "Check the criteria used for matching."),
    NULL_RESPONSE_FROM_API(503, "OMAS-DATA-PLATFORM-503-001 ",
            "A null response was received from REST API call {0} to server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-DATA-PLATFORM-503-002 ",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(503, "OMAS-DATA-PLATFORM-503-003 ",
            "The access service has not been initialized and can not support REST API call {0}",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server.")
    ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(DataPlatformErrorCode.class);


    /**
     * The constructor for DataPlatformErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
     *
     *     DataPlatformErrorCode   errorCode = DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode   error code to use over REST calls
     * @param newErrorMessageId   unique Id for the message
     * @param newErrorMessage   text for the message
     * @param newSystemAction   description of the action taken by the system when the error condition happened
     * @param newUserAction   instructions for resolving the error
     */
    DataPlatformErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
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
        log.debug(String.format("<== DataPlatformErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> DataPlatformErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

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
        return "DataPlatformErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
