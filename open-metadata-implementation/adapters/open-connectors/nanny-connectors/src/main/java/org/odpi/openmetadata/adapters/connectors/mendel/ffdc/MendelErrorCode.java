/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The MendelErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Mendel Automated Duplicate Manager.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum MendelErrorCode implements ExceptionMessageSet
{
    /**
     * MENDEL-DUPLICATE-MANAGER-500-001 - The {0} watchdog action service received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "MENDEL-DUPLICATE-MANAGER-500-001",
                         "The {0} watchdog action service received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The service is unable to manage one or more duplicate links.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-500-002 - The {0} watchdog action service is unable to register a listener for open metadata events due to a {1} exception with message {2}
     */
    UNABLE_TO_REGISTER_LISTENER(500, "MENDEL-DUPLICATE-MANAGER-500-002",
                                "The {0} watchdog action service is unable to register a listener for open metadata events due to a {1} exception with message {2}",
                                "The service is unable to start because it receives no notification of new or updated duplicate links.",
                                "Use the details from the error message to determine the cause of the error and restart the service once it is resolved.",
                                "https://egeria-project.org/features/duplicate-management/overview/"),

    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    MendelErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor for MendelErrorCode expects to be passed one of the enumeration rows defined in
     * MendelErrorCode above.   For example:
     * <br><br>
     *     MendelErrorCode   errorCode = MendelErrorCode.UNEXPECTED_EXCEPTION;
     * <br><br>
     * This will expand out to the 6 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    MendelErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction,
                                                                url);
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
        return "MendelErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
