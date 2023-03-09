/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExceptionMessageDefinition extends MessageDefinition to provide a container that describes
 * a single instance of a message for an exception.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExceptionMessageDefinition extends MessageDefinition
{
    private final int    httpErrorCode;


    /**
     * Constructor to save all the fixed values of a message.  This is typically populated
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


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ExceptionMessageDefinition{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", messageId='" + getMessageId() + '\'' +
                       ", messageTemplate='" + getMessageTemplate() + '\'' +
                       ", messageParams=" + Arrays.toString(getMessageParams()) +
                       ", systemAction='" + getSystemAction() + '\'' +
                       ", userAction='" + getUserAction() + '\'' +
                       '}';
    }
}
