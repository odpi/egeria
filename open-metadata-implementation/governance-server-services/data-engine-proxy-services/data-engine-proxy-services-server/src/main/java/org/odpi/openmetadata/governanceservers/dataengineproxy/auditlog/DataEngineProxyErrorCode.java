/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The DataEngineProxyErrorCode is used to define first failure data capture (FFDC) for errors that occur when working
 * with the Data Engine Proxy.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum DataEngineProxyErrorCode implements ExceptionMessageSet {

    NO_CONFIG_DOC(500, "DATA-ENGINE-PROXY-500-001",
            "Data Engine proxy {0} is not configured with a configuration document",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration document for this data engine proxy."),
    NO_OMAS_SERVER_URL(500, "DATA-ENGINE-PROXY-500-002",
            "Data Engine proxy {0} is not configured with the platform URL root for the Data Engine OMAS",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration for the platform URL root to this data engine proxy's configuration document."),
    NO_OMAS_SERVER_NAME(500, "DATA-ENGINE-PROXY-500-003",
            "Data Engine proxy {0} is not configured with the name for the server running the Data Engine OMAS",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration for the server name to this data engine proxy's configuration document."),
    ERROR_INITIALIZING_CONNECTION(500, "DATA-ENGINE-PROXY-500-005",
            "Unable to initialize the Data Engine connection",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    ERROR_INITIALIZING_CLIENT_CONNECTION(500, "DATA-ENGINE-PROXY-500-006",
            "Unable to initialize the Data Engine client connection",
            "The client connection could not be initialized.",
            "Review the exception and resolve the configuration."),
    UNKNOWN_ERROR(500, "DATA-ENGINE-PROXY-500-008",
            "An unknown error occurred",
            "The system is unable to process the operation due to an unknown runtime error.",
            "Check your OMAS configuration and server logs to troubleshoot."),
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
    DataEngineProxyErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
