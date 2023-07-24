/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OpenAPIIntegrationConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Basic File Connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OpenAPIIntegrationConnectorErrorCode implements ExceptionMessageSet
{
    UNEXPECTED_EXCEPTION(500, "OPEN-API-INTEGRATION-CONNECTOR-500-001",
                         "The {0} integration connector received an unexpected exception {1} when cataloguing APIs; the error message was: {2}",
                         "The connector is unable to catalog one or more APIs.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    NULL_URL(400, "OPEN-API-INTEGRATION-CONNECTOR-400-014",
             "OMAG server has been called with a null local server name",
             "The system is unable to configure the local server without knowing what it is called.",
             "The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly."),

    CLIENT_SIDE_REST_API_ERROR(503, "OPEN-API-INTEGRATION-CONNECTOR-503-001",
                               "A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}",
                               "The integration has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
                               "Look for errors in the local server's console to understand and correct the source of the error.")

    ;


    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for OpenAPIIntegrationConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * OpenAPIIntegrationConnectorErrorCode above.   For example:
     *
     *     OpenAPIIntegrationConnectorErrorCode   errorCode = OpenAPIIntegrationConnectorErrorCode.ERROR_SENDING_EVENT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OpenAPIIntegrationConnectorErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "OpenAPIIntegrationConnectorErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
