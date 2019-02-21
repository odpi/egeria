/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector;

import java.text.MessageFormat;

public enum IGCOMRSErrorCode {

    REST_CLIENT_FAILURE(500, "OMRS-IGC-REPOSITORY-500-001",
            "The IGC REST API client was not successfully initialized to \"{0}\"",
            "The system was unable to login to or access the IBM IGC environment via REST API.",
            "Check your authorization details are accurate, the IGC environment started, and is network-accessible."),
    OMRS_BUNDLE_FAILURE(500, "OMRS-IGC-REPOSITORY-500-002",
            "Unable to {0} the required OMRS OpenIGC bundle",
            "The system was unable to either generate or upload the OMRS OpenIGC bundle needed to handle open metadata.",
            "Check the system logs and diagnose or report the problem."),
    CLASSIFICATION_INSUFFICIENT_PROPERTIES(400, "OMRS-IGC-REPOSITORY-400-001",
            "The properties provided for classification \"{0}\" on entity \"{1}\" are insufficient",
            "The system is unable to proceed classifying an entity because insufficient detail has been provided.",
            "Check the system logs and diagnose or report the problem."),
    CLASSIFICATION_EXCEEDS_REPOSITORY(400, "OMRS-IGC-REPOSITORY-400-002",
            "The properties provided for classification \"{0}\" on entity \"{1}\" are excessive",
            "The system is unable to proceed classifying an entity because more details have been provided than the repository is capable of handling.",
            "Check the system logs and diagnose or report the problem."),
    CLASSIFICATION_NOT_FOUND(400, "OMRS-IGC-REPOSITORY-400-003",
            "The classification \"{0}\" on entity \"{1}\" was not found",
            "The system cannot proceed classifying an entity because it was unable to find the classification by the provided details.",
            "Check the system logs and diagnose or report the problem."),
    CLASSIFICATION_NOT_EDITABLE(400, "OMRS-IGC-REPOSITORY-400-004",
            "The classification \"{0}\" on entity \"{1}\" is not editable",
            "The system cannot proceed classifying an entity because the classification type is not editable through IGC's REST API.",
            "Raise an enhancement request with IBM support."),
    CLASSIFICATION_NOT_APPLICABLE(400, "OMRS-IGC-REPOSITORY-400-005",
            "The classification \"{0}\" cannot be applied to IGC entity \"{1}\"",
            "The system does not support the listed classification on the IGC entity type listed.",
            "Raise an enhancement request with IBM support."),
    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    /**
     * The constructor for LocalAtlasOMRSErrorCode expects to be passed one of the enumeration rows defined in
     * LocalAtlasOMRSErrorCode above.   For example:
     *
     *     LocalAtlasOMRSErrorCode   errorCode = LocalAtlasOMRSErrorCode.NULL_INSTANCE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    IGCOMRSErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
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
