/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AssetConsumerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Consumer OMAS.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <br><br>
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
public enum AssetConsumerErrorCode implements ExceptionMessageSet
{
    /**
     * OMAS-ASSET-CONSUMER-404-002 - The open metadata repository services are not initialized for the {0} operation
     */
    OMRS_NOT_INITIALIZED(404, "OMAS-ASSET-CONSUMER-404-002",
             "The open metadata repository services are not initialized for the {0} operation",
             "The system is unable to connect to an open metadata repository.",
             "Check that the server where the Asset Consumer OMAS is running initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),

    /**
     * OMAS-ASSET-CONSUMER-400-017 - A null topic listener has been passed by user {0} on method {1}
     */
    NULL_LISTENER(400, "OMAS-ASSET-CONSUMER-400-017",
            "A null topic listener has been passed by user {0} on method {1}",
            "There is a coding error in the caller to the Asset Consumer OMAS.",
            "Correct the caller logic and retry the request."),

    /**
     * OMAS-ASSET-CONSUMER-500-004 - An unexpected exception occurred when sending an event through connector {0} to the Asset Consumer OMAS out topic.  The failing event was {1}, the exception was {2} with message {3}
     */
    UNABLE_TO_SEND_EVENT(500, "OMAS-ASSET-CONSUMER-500-004",
            "An unexpected exception occurred when sending an event through connector {0} to the Asset Consumer OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {3}",
            "The access service has issued a call to publish an event on its Out Topic and it failed.",
            "Look for errors in the event bus to understand why this is failing.  When the event bus is operating correctly, event will" +
                                 " begin to be published again.  In the meantime, events are being lost."),

    /**
     * OMAS-ASSET-CONSUMER-500-006 - The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server {2} at {3}
     */
    NULL_CONNECTOR_RETURNED(500, "OMAS-ASSET-CONSUMER-500-006",
           "The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service" +
                                    " running in OMAG Server {2} at {3}",
           "The system is unable to create a connector which means some of its services will not work.",
           "This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connection" +
                                    "and correct if necessary.  If the connection is correct, contact the Egeria community for help."),

    /**
     * OMAS-ASSET-CONSUMER-500-007 - The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is not of the required type. It should be an instance of {4}
     */
    WRONG_TYPE_OF_CONNECTOR(500, "OMAS-ASSET-CONSUMER-500-007",
           "The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is " +
                                    "not of the required type. It should be an instance of {4}",
           "The system is unable to create the required connector which means some of its services will not work.",
           "Verify that the OMAG server is running and the OMAS service is correctly configured."),

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
    AssetConsumerErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
