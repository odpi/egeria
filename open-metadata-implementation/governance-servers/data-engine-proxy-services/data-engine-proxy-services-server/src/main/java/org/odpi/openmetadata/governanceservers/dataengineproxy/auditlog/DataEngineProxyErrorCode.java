/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The DataEngineProxyErrorCode is used to define first failure data capture (FFDC) for errors that occur within the Data Engine Proxy Server
 * It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>501 - not implemented </li>
 *         <li>503 - Service not available</li>
 *         <li>400 - invalid parameters</li>
 *         <li>401 - unauthorized</li>
 *         <li>404 - not found</li>
 *         <li>405 - method not allowed</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
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
    INVALID_PARAMETER(500, "DATA-ENGINE-PROXY-500-004",
            "Data Engine proxy {0} could not be initialized due to an invalid parameter",
            "The server is not able to initialize.  It fails to start.",
            "Review the logs for more information on the invalid parameter exception."),
    ;

    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for DataEngineProxyErrorCode expects to be passed one of the enumeration rows defined in
     * DataEngineProxyErrorCode above.   For example:
     *
     *     DataEngineProxyErrorCode   errorCode = DataEngineProxyErrorCode.NO_CONFIG_DOC;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    DataEngineProxyErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction) {
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
    public ExceptionMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

}
