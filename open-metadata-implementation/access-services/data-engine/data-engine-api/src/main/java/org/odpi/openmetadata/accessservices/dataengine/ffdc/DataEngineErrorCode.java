/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.ffdc;


import java.text.MessageFormat;

/**
 * The DataEngineErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Data Engine OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Id - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a AssetConsumerInterface should correct the error</li>
 * </ul>
 */

public enum DataEngineErrorCode {
    OMRS_NOT_INITIALIZED(404, "OMAS-DATA-ENGINE-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    INVALID_PORT_TYPE(400, "OMAS-DATA-ENGINE-400-001 ",
            "The port type passed for the {0} is invalid, or different from {1}",
            "The system is unable to create a new PortDelegation relation without equal types between the ports.",
            "Correct the code in the caller to provide the correct port type."),
    PROCESS_EVENT_EXCEPTION(400, "OMAS-DATA-ENGINE-400-002",
            "The data engine event {0} could not be processed. Error: {1}",
            "The system is unable to process the event.",
            "Verify the topic configuration or the event schema."),
    PARSE_EVENT_EXCEPTION(400, "OMAS-DATA-ENGINE-400-003",
            "The data engine event {0} could not be parsed. Error: {1}",
            "The system is unable to process the event.",
            "Verify the topic configuration or the event schema."),
    DATA_ENGINE_EXCEPTION(400, "OMAS-DATA-ENGINE-400-004",
            "Exception while processing the data engine event {0}",
            "The system is unable to process the event.",
            "Verify the topic configuration or the event schema."),
    SCHEMA_ATTRIBUTE_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-005 ",
            "SchemaAttribute with qualifiedName {0} was not found",
            "The system is unable to create a new LineageMapping relation.",
            "Correct the code in the caller to provide the correct schema attribute qualified name."),
    PORT_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-006 ",
            "Port with qualifiedName {0} was not found",
            "The system is unable to create a new PortDelegation relation.",
            "Correct the code in the caller to provide the correct port qualified name."),
    NULL_TOPIC_CONNECTOR(400, "OMAS-DATA-ENGINE-400-007",
            "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
            "The local server will not connect to the cohort.",
            "The connection to the connector is configured in the server configuration.  " +
                                 "Review previous error messages to determine the precise error in the " +
                                 "start up configuration. " +
                                 "Correct the configuration and reconnect the server to the cohort. "),
    PROCESS_NOT_FOUND(400, "OMAS-DATA-ENGINE-400-008 ",
            "Process with qualifiedName {0} was not found",
            "The system is unable to create a new ProcessHierarchy relation.",
            "Correct the code in the caller to provide the correct port qualified name.");

    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    /**
     * The constructor for DataEngineErrorCode expects to be passed one of the enumeration rows defined in
     * DataEngineErrorCode above.   For example:
     * <p>
     * DataEngineErrorCode   errorCode = DataEngineErrorCode.NULL_USER_ID;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage   - text for the message
     * @param newSystemAction   - description of the action taken by the system when the error condition happened
     * @param newUserAction     - instructions for resolving the error
     */
    DataEngineErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction,
                        String newUserAction) {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }

    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the errorMessage
     *
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params) {
        MessageFormat mf = new MessageFormat(errorMessage);
        return mf.format(params);
    }

    /**
     * Returns the numeric code that can be used in a REST response.
     *
     * @return int
     */
    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    /**
     * Returns the unique error message identifier of the error.
     *
     * @return String
     */
    public String getErrorMessageId() {
        return errorMessageId;
    }

    /**
     * Returns the un-formatted error message.
     *
     * @return String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the action taken by the system when the error occurred.
     *
     * @return String
     */
    public String getSystemAction() {
        return systemAction;
    }

    /**
     * Returns the proposed action for a user to take when encountering the error.
     *
     * @return String
     */
    public String getUserAction() {
        return userAction;
    }

}

