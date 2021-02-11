/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The DataManagerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Data Manager OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum DataManagerErrorCode implements ExceptionMessageSet
{
    ADD_ENTITY_EXCEPTION(400, "OMAS-DATA-MANAGER-400-001",
            "Entity {0} could not be added. Error: {1}",
            "The system is unable to process the add entity request.",
            "Verify the topic event for the add entity."),
    ADD_RELATIONSHIP_EXCEPTION(400, "OMAS-DATA-MANAGER-400-002",
            "Relationship {0} could not be added. Error: {1}",
            "The system is unable to process the add relationship request.",
            "Verify the topic event for the add relationship."),
    GET_ENTITY_EXCEPTION(400, "OMAS-DATA-MANAGER-400-003",
            "Entity matching criteria [{0}] could not be fetched. Error: {1}",
            "The system is unable to process the get entity request.",
            "Verify the topic event for correct unique identifier of the entity."),
    GET_RELATIONSHIP_EXCEPTION(400, "OMAS-DATA-MANAGER-400-004",
            "Relationship {0} could not be fetched. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event related to the relationship."),

    ADD_CLASSIFICATION_EXCEPTION(400, "OMAS-DATA-MANAGER-400-005",
            "Unable to create classification {0} for entity of type {1}. Error: {2}",
            "The system is unable to process the create classification request.",
            "Verify the topic event for the create classification request."),
    
    OMRS_NOT_INITIALIZED(404, "OMAS-DATA-MANAGER-404-001",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Data Manager OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    
    NULL_LISTENER(500, "OMAS-DATA-MANAGER-500-001",
                  "A null topic listener has been passed by user {0} on method {1}",
                  "There is a coding error in the caller to the Data Manager OMAS.",
                  "Correct the caller logic and retry the request."),

    UNABLE_TO_SEND_EVENT(500, "OMAS-DATA-MANAGER-500-004",
                         "An unexpected exception occurred when sending an event through connector {0} to the Data Manager OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {2}",
                         "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
                         "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),

    UNEXPECTED_INITIALIZATION_EXCEPTION(503, "OMAS-DATA-MANAGER-503-005",
                                        "A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}",
                                        "The system detected an unexpected error during start up and is now in an unknown state.",
                                        "The error message should indicate the cause of the error.  Otherwise look for errors in the remote server's audit log and console to understand and correct the source of the error.")


    ;

    private static final long    serialVersionUID = 1L;

    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for DataManagerErrorCode expects to be passed one of the enumeration rows defined in
     * DataManagerErrorCode above.   For example:
     *
     *     DataManagerErrorCode   errorCode = DataManagerErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    DataManagerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "DataManagerErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
