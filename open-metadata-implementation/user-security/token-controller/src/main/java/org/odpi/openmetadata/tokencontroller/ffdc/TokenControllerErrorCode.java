/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tokencontroller.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The TokenControllerErrorCode is used to define first failure data capture (FFDC) for errors that occur when processing user security information
 * on an incoming REST API call.
 * It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - not authorized</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum TokenControllerErrorCode implements ExceptionMessageSet
{
    /**
     * TOKEN-CONTROLLER-401-001 - Call made to {0} service {1} has no logged on user
     */
    NO_USER(400, "TOKEN-CONTROLLER-401-001",
                     "Call made to {0} service {1} has no logged on user",
                     "An inbound REST API call has been received but there is no userId in the authorization bearer token.",
                     "Ensure the user acquires a valid token and this token is included in the request header."),
    ;


    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for UserSecurityErrorCode expects to be passed one of the enumeration rows defined in
     * UserSecurityErrorCode above.   For example:
     * <br><br>
     *     UserSecurityErrorCode   errorCode = UserSecurityErrorCode.NO_USER;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    TokenControllerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "TokenControllerErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
