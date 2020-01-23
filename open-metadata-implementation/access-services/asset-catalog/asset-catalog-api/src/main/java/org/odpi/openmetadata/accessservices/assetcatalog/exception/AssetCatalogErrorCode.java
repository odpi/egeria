/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.exception;

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
public enum AssetCatalogErrorCode {

    OMRS_NOT_INITIALIZED(404, "OMAS-ASSET-CATALOG-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    ASSET_NEIGHBORHOOD_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-011 ",
            "There is no assets or relationships available in the neighbourhood of asset {0} in OMAS Server {1}",
            "The system is unable to retrieve the neighbourhood for the given asset.",
            "Check that the unique identifier for the asset is correct."),

    NO_ASSET_FROM_NEIGHBORHOOD_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-012 ",
            "There is no assets available in the neighbourhood of asset {0} in OMAS Server {1}",
            "The system is unable to retrieve the assets neighborhood from the specified asset identifier.",
            "Check that the unique identifier for the asset is correct."),

    NO_RELATIONSHIPS_FROM_NEIGHBORHOOD_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-013 ",
            "There is no relationships available in the neighbourhood of asset {0} in OMAS Server {1}",
            "The system is unable to retrieve the neighborhood relationships for the specified asset identifier.",
            "Check that the unique identifier for the asset is correct."),

    LINKING_RELATIONSHIPS_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-015 ",
            "There is no intermediate relationships that connect the {0} with the {1} in OMAS Server {2}",
            "The system is unable to retrieve the linking relationship.",
            "Check that the unique identifiers of the assets are correct."),

    LINKING_ASSETS_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-016 ",
            "There is no intermediate assets that connect the {0} with the {1} in OMAS Server {2}",
            "The system is unable to retrieve linking assets.",
            "Check that the unique identifiers of the assets are correct."),

    SERVICE_NOT_INITIALIZED(503, "OMAS-ASSET-CATALOG-503-001 ",
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
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

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public String getErrorMessageId() {
        return errorMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public String getUserAction() {
        return userAction;
    }

}
