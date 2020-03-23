/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode;

import java.text.MessageFormat;

/**
 * The GovernanceEngineErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Governance Engine Access Service.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 * <li>UserAction - describes how a GovernanceEngineInterface should correct the error</li>
 * </ul>
 */
public enum GovernanceEngineErrorCode {
    BAD_OUT_TOPIC_CONNECTION(400, "OMAS-GOVERNANCE-ENGINE-400-001 ",
            "The Governance Engine Open Metadata Access Service (OMAS) has been passed an invalid connection for publishing events.  The connection was {0}.  The resulting exception of {1} included the following message: {2}",
            "The access service has not been passed valid configuration for its out topic connection.",
            "Correct the configuration and restart the service."),
    NULL_TOPIC_CONNECTOR(400, "OMAS-GOVERNANCEENGINE-400-012",
                         "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
                         "The local server will not connect to the cohort.",
                         "The connection to the connector is configured in the server configuration.  " +
                                 "Review previous error messages to determine the precise error in the " +
                                 "start up configuration. " +
                                 "Correct the configuration and reconnect the server to the cohort. "),
    OMRS_NOT_INITIALIZED(404, "OMAS-GOVERNANCE-ENGINE-404-001 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property handlers.",
            "Check that the handlers where the Asset Consumer OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available.");

    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    /**
     * The constructor for GovernanceEngineErrorCode expects to be passed one of the enumeration rows defined in
     * GovernanceEngineErrorCode above.   For example:
     * <p>
     * GovernanceEngineErrorCode   errorCode = GovernanceEngineErrorCode.ASSET_NOT_FOUND;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage   - text for the message
     * @param newSystemAction   - description of the action taken by the system when the error condition happened
     * @param newUserAction     - instructions for resolving the error
     */
    GovernanceEngineErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
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
