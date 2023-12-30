/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The DataEngineErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Data Engine OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Identifier - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */

public enum DataEngineErrorCode implements ExceptionMessageSet
{
    /**
     * OMAS-DATA-ENGINE-404-001 The open metadata repository services are not initialized for server {0}
     */
    OMRS_NOT_INITIALIZED(404, "OMAS-DATA-ENGINE-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    /**
     * OMAS-DATA-ENGINE-400-001 Referenceable with qualifiedName {0} was not found
     */
    REFERENCEABLE_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-001",
            "Referenceable with qualifiedName {0} was not found",
            "The system is unable to create a new DataFlow relation.",
            "Correct the code in the caller to provide the correct referenceable qualified name."),
    /**
     * OMAS-DATA-ENGINE-400-002 Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize
     */
    NULL_TOPIC_CONNECTOR(400, "OMAS-DATA-ENGINE-400-002",
            "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
            "The local server will not connect to the cohort.",
            "The connection to the connector is configured in the server configuration.  " +
                    "Review previous error messages to determine the precise error in the " +
                    "start up configuration. " +
                    "Correct the configuration and reconnect the server to the cohort. "),
    /**
     * OMAS-DATA-ENGINE-400-003 Process with qualifiedName {0} was not found
     */
    PROCESS_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-003",
            "Process with qualifiedName {0} was not found",
            "The system is unable to create a new ProcessHierarchy relation.",
            "Correct the code in the caller to provide the correct port qualified name."),
    /**
     * OMAS-DATA-ENGINE-400-004 Database Schema with qualifiedName {0} was not found
     */
    DATABASE_SCHEMA_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-004",
            "Database Schema with qualifiedName {0} was not found",
            "The system is unable to create a new table attached to a database schema",
            "Correct the code in the caller to provide the correct database schema qualified name."),
    /**
     * OMAS-DATA-ENGINE-500-001 An unexpected exception occurred when sending an event through connector {0} to the Data Engine OMAS out topic
     */
    UNABLE_TO_SEND_EVENT(500, "OMAS-DATA-ENGINE-500-001",
            "An unexpected exception occurred when sending an event through connector {0} to the Data Engine OMAS out topic.  The failing " +
                    "event was {1}, the exception was {2} with message {2}",
            "The system has issued a call to an open metadata access service input topic using event message broker",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    /**
     * OMAS-DATA-ENGINE-400-005 Entity with qualifiedName {0} was not deleted
     */
    ENTITY_NOT_DELETED(400, "OMAS-DATA-ENGINE-400-005",
            "Entity with qualifiedName {0} was not deleted",
            "The system is unable to delete the entity with the provided qualifiedName or guid.",
            "Correct the code in the caller to provide the correct database qualified name."),

    /**
     * OMAS-DATA-ENGINE-500-002 Method {0} is not implemented
     */
    METHOD_NOT_IMPLEMENTED(501, "OMAS-DATA-ENGINE-500-002",
            "Method {0} is not implemented",
            "The system performs no action as the method is not implemented.",
            "No action suggested."),
    /**
     * OMAS-DATA-ENGINE-400-006 Topic with qualifiedName {0} was not found
     */
    TOPIC_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-006",
            "Topic with qualifiedName {0} was not found",
            "The system is unable to create a new event type attached to a topic",
            "Correct the code in the caller to provide the correct topic qualified name."),
    /**
     * OMAS-DATA-ENGINE-400-007 Engine with qualifiedName {0} was not found
     */
    ENGINE_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-007",
                            "Engine with qualifiedName {0} was not found",
                            "The system is unable to find the searched Engine",
                            "Correct the code in the caller to provide the correct Engine qualified name.");

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    DataEngineErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
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
                                              userAction);
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
                                                                                      userAction);

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
                       '}';
    }
}

