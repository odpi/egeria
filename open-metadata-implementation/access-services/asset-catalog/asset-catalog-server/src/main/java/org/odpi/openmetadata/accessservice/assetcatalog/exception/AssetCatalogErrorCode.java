/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.exception;

import lombok.Getter;

import java.text.MessageFormat;

/**
 * The AssetCatalogErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Catalog OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum AssetCatalogErrorCode {

    NO_SEARCH_RESULTS(204, "CatalogOMAS-204-00-001",
            "Given search filter {0} did not yield any results",
            "The system is unable to find an asset with the given details.",
            "Provide more details for the current search."),

    NULL_USER_ID(400, "OMAS-ASSETCONSUMER-400-003 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),

    SERVICE_NOT_INITIALIZED(503, "OMAS-ASSET-CATALOG-503-003 ",
            "The access service has not been initialized and can not support REST API call {0}",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server.");

    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    AssetCatalogErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
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
