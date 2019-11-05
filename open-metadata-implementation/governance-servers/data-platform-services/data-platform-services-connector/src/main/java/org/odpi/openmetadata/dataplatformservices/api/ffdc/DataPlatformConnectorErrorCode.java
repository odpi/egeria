/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.ffdc;

import java.text.MessageFormat;

public enum DataPlatformConnectorErrorCode {

    OMAS_CONNECTION_ERROR(500, "DATA-PLATFORM-CONNECTOR-500-001",
            "The Data Platform OMAS client was not successfully initialized",
            "The system is unable to process anything due to a lack of OMAS connectivity.",
            "Check your OMAS configuration is correct and the OMAS is running."),
    USER_NOT_AUTHORIZED(500, "DATA-PLATFORM-CONNECTOR-500-002",
            "The user is not authorized for the Data Platform OMAS operation",
            "The system is unable to process the operation due to the user not being authorized to do so.",
            "Check your OMAS configuration and user authorizations."),
    NO_CONFIG(404, "DATA-PLATFORM-CONNECTOR-404-003",
            "No configuration was provided for the Data Platform server",
            "The system is unable to process the operation due to a lack of a configuration document.",
            "Check your configuration request and ensure all necessary information is provided and accurate."),
    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    /**
     * The constructor for DataPlatformConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * DataPlatformConnectorErrorCode above.   For example:
     *
     *     DataPlatformConnectorErrorCode   errorCode = DataPlatformConnectorErrorCode.NULL_INSTANCE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    DataPlatformConnectorErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.httpErrorCode  = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage   = newErrorMessage;
        this.systemAction   = newSystemAction;
        this.userAction     = newUserAction;
    }


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
        String result = mf.format(params);
        return result;
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
