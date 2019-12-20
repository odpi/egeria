/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import java.text.MessageFormat;

/**
 * The DataEngineConnectorErrorCode is used to define the errors for Data Engine Connectors.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - to provide guidance on the type of error</li>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum DataEngineConnectorErrorCode {

    OMAS_CONNECTION_ERROR(500, "DATA-ENGINE-CONNECTOR-500-001",
            "The Data Engine OMAS client was not successfully initialized",
            "The system is unable to process anything due to a lack of OMAS connectivity.",
            "Check your OMAS configuration is correct and the OMAS is running."),
    USER_NOT_AUTHORIZED(500, "DATA-ENGINE-CONNECTOR-500-002",
            "The user is not authorized for the Data Engine OMAS operation",
            "The system is unable to process the operation due to the user not being authorized to do so.",
            "Check your OMAS configuration and user authorizations."),
    UNKNOWN_ERROR(500, "DATA-ENGINE-CONNECTOR-500-003",
            "An unknown error occurred",
            "The system is unable to process the operation due to an unknown runtime error.",
            "Check your OMAS configuration and server logs to troubleshoot."),
    NO_CONFIG(404, "DATA-ENGINE-CONNECTOR-404-003",
            "No configuration was provided for the Data Engine Proxy server",
            "The system is unable to process the operation due to a lack of a configuration document.",
            "Check your configuration request and ensure all necessary information is provided and accurate."),
    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    /**
     * The constructor for DataEngineConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * DataEngineConnectorErrorCode above.   For example:
     *
     *     DataEngineConnectorErrorCode   errorCode = DataEngineConnectorErrorCode.NULL_INSTANCE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    DataEngineConnectorErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.httpErrorCode  = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage   = newErrorMessage;
        this.systemAction   = newSystemAction;
        this.userAction     = newUserAction;
    }


    /**
     * Returns the numeric error code that can be used in REST responses.
     *
     * @return int
     */
    public int getHTTPErrorCode() {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId() {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage() {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params) {
        MessageFormat mf = new MessageFormat(errorMessage);
        return mf.format(params);
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction() {
        return userAction;
    }

}
