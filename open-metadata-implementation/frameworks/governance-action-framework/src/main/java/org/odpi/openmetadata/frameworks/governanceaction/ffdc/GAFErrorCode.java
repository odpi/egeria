/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.governanceaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GAF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * GAF Components.  It is used in conjunction with the GAFCheckedException and GAFRuntimeException.
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
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum GAFErrorCode 
{
    NULL_AUDITLOG_PROPERTY_NAME(404,
                                "GAF-AUDITLOG-500-001",
                                "Null property name passed to AuditLogBase {0}",
                                "The addProperty() method was passed a null property name.",
                                "Correct the code in the Java class that receives the exception.");

    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for GAFErrorCode expects to be passed one of the enumeration rows defined in
     * GAFErrorCode above.   For example:
     *
     *     GAFErrorCode   errorCode = GAFErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GAFErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }
}
