/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


/**
 * The OIFErrorCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OIFErrorCode implements ExceptionMessageSet
{
    ENGAGE_IMPLEMENTATION_MISSING(400,"OIF-CONNECTOR-400-001",
                    "The integration connector {0} has been configured to have its own thread to issue blocking calls but has not " +
                                          "implemented the engage() method",
                    "The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  " +
                                          "It called the engage() method on this thread.  However, the default implementation of the " +
                                          "engage() method has been invoked suggesting that either the dedicated thread is not needed or " +
                                          "there is an error in the implementation of the connector.  The integration daemon " +
                                          "will terminate the thread once the engage() method returns.",
                    "If the connector does not need to issue blocking calls update the configuration to remove the need for the " +
                                          "dedicated thread.  Otherwise update the integration connector's implementation to override " +
                                          "the default engage() method implementation.");

    private final ExceptionMessageDefinition messageDefinition;

    private static final Logger log = LoggerFactory.getLogger(OCFErrorCode.class);


    /**
     * The constructor for OCFErrorCode expects to be passed one of the enumeration rows defined in
     * OCFErrorCode above.   For example:
     * <br>
     *     OCFErrorCode   errorCode = OCFErrorCode.UNKNOWN_ENDPOINT;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OIFErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
    }


    /**
     * Return the HTTP error code for this exception.
     *
     * @return int
     */
    @Deprecated
    public int getHTTPErrorCode()
    {
        return messageDefinition.getHttpErrorCode();
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    @Deprecated
    public String getErrorMessageId()
    {
        return messageDefinition.getMessageId();
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    @Deprecated
    public String getFormattedErrorMessage(String... params)
    {
        MessageFormat mf     = new MessageFormat(messageDefinition.getMessageTemplate());
        String        result = mf.format(params);

        log.debug(String.format("OIFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    @Deprecated
    public String getSystemAction()
    {
        return messageDefinition.getSystemAction();
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    @Deprecated
    public String getUserAction()
    {
        return messageDefinition.getUserAction();
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
        return "OIFErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
