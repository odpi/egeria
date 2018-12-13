/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.exception;

import lombok.Getter;

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
@Getter
public enum DataEngineErrorCode {
    OMRS_NOT_INITIALIZED(404, "OMAS-DATA-ENGINE-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-DATA-ENGINE-404-002 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Asset Catalog OMAS is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    SERVICE_NOT_INITIALIZED(503, "OMAS-DATA-ENGINE-503-001 ",
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    NULL_USER_ID(400, "OMAS-DATA-ENGINE-400-001 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id.");

    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    DataEngineErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction,
                        String newUserAction) {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }

    public String getFormattedErrorMessage(String... params) {
        MessageFormat mf = new MessageFormat(errorMessage);
        return mf.format(params);
    }
}

