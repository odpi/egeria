/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The SecurityManagerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Security Manager OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum SecurityManagerErrorCode implements ExceptionMessageSet
{
    /**
     * OMAS-SECURITY-MANAGER-404-001 - The open metadata repository services are not initialized for the {0} operation
     */
    OMRS_NOT_INITIALIZED(404, "OMAS-SECURITY-MANAGER-404-001",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server where the Security Manager OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    /**
     * OMAS-SECURITY-MANAGER-500-001 - A null topic listener has been passed by user {0} on method {1}
     */
    NULL_LISTENER(500, "OMAS-SECURITY-MANAGER-500-001",
                  "A null topic listener has been passed by user {0} on method {1}",
                  "There is a coding error in the caller to the Security Manager OMAS.",
                  "Correct the caller logic and retry the request."),

    /**
     * OMAS-SECURITY-MANAGER-500-004 - An unexpected exception occurred when sending an event through connector {0} to
     * the Security Manager OMAS out topic.  The failing event was {1}, the exception was {2} with message {2}
     */
    UNABLE_TO_SEND_EVENT(500, "OMAS-SECURITY-MANAGER-500-004",
                         "An unexpected exception occurred when sending an event through connector {0} to the Security Manager OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {2}",
                         "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
                         "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),

    ;

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
    SecurityManagerErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
