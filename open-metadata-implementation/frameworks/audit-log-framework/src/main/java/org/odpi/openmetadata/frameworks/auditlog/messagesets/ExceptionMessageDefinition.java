/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

/**
 * ExceptionMessageDefinition extends MessageDefinition to provide a container that describes
 * a single instance of a message for an exception.
 */
public abstract class ExceptionMessageDefinition extends MessageDefinition
{
    private int    httpErrorCode;


    /**
     * Constructor to save all of the fixed values of a message.  This is typically populated
     * from an Enum message set.  The constructor passes most values to the super class and just retains
     * the additional value for the exception.
     *
     * @param httpErrorCode the HTTP code that describes the nature of the error
     * @param messageId unique Id for the message
     * @param messageTemplate text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    public ExceptionMessageDefinition(int httpErrorCode, String messageId, String messageTemplate, String systemAction, String userAction)
    {
        super(messageId, messageTemplate, systemAction, userAction);

        this.httpErrorCode        = httpErrorCode;
    }


    /**
     * Return the HTTP code that describes the nature of the exception.
     *
     * @return integer code
     */
    public int getHttpErrorCode()
    {
        return httpErrorCode;
    }


}
