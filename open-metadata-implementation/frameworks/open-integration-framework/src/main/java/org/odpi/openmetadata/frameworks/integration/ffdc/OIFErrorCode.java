/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


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
    /**
     * OIF-CONNECTOR-400-001 - The integration connector {0} has been configured to have its own thread to issue blocking calls but has not implemented the engage() method
     */
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
                                          "the default engage() method implementation."),


    /**
     * OIF-CONNECTOR-400-001 - The object passed on the {0} parameter of the {1} operation is null
     */
    NULL_OBJECT(400, "OIF-CONNECTOR-400-002",
                        "The object passed on the {0} parameter of the {1} operation is null",
                        "The system is unable to process the request without this object.",
                        "Correct the code in the caller to provide the object."),


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
    OIFErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
