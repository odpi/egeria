/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The WatchdogActionErrorCode error code is used to define first failure data capture (FFDC) for errors that
 * occur when working with the Discovery Engine Services.  It is used in conjunction with all exceptions,
 * both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum WatchdogActionErrorCode implements ExceptionMessageSet
{
    /**
     * OMES-WATCHDOG-ACTION-400-001 - The Watchdog Action OMES are unable to initialize a new instance in server {0}; error message is {1}
     */
    SERVICE_INSTANCE_FAILURE(400, "OMES-WATCHDOG-ACTION-400-001",
                             "The Watchdog Action OMES are unable to initialize a new instance in server {0}; error message is {1}",
                             "The Watchdog Action OMES detected an error during the start up of a specific server instance.  " +
                                     "No watchdog action services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /**
     * OMES-WATCHDOG-ACTION-400-002 - The watchdog action service {0} linked to request type {1} can not be started.
     * The {2} exception was returned with message {3}
     */
    INVALID_WATCHDOG_SERVICE(400, "OMES-WATCHDOG-ACTION-400-002",
                             "The watchdog action service {0} linked to request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
                             "The monitoring request is not run and an error is returned to the caller.",
                             "This may be an error in the watchdog action service's logic or the watchdog action service may not be properly deployed or " +
                                      "there is a configuration error related to the watchdog action engine.  " +
                                      "The configuration that defines the request type in the watchdog action engine and links " +
                                      "it to the watchdog action service is maintained in the metadata server by the open governance configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "watchdog action service's implementation has been deployed so the Watchdog Action OMES can load it.  If all this is " +
                                      "true this it is likely to be a code error in the watchdog action service in which case, " +
                                      "raise an issue with the author of the watchdog action service to get it fixed.  Once the cause is resolved, " +
                                      "retry the monitoring request."),


    NO_TARGET_NOTIFICATION_TYPE(400, "OMES-WATCHDOG-ACTION-400-003",
                                "The watchdog action service {0} linked to request type {1} and engine action {2} can not be started because there is no asset action target supplied",
                                "The monitoring request is not run and an error is recorded in the engine action.",
                                "Retry the monitoring request and ensuring that an action target is included in the request."),


    NULL_REQUEST(400, "OMES-WATCHDOG-ACTION-400-004",
                    "The watchdog engine action {0} can not be started because there is no governance service context",
                    "The monitoring request is not run and an error is recorded in the engine action because the governance service is not set up property.",
                    "This is an unexpected error, you may need to trace through the code to find out what has happened."),


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
    WatchdogActionErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
