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

    NO_SEARCH_RESULTS(204, "OMAS-ASSET-CATALOG-204-001 ",
            "Given search filter {0} did not yield any results",
            "The system is unable to find an asset with the given details.",
            "Provide more details for the current search."),

    SERVER_URL_NOT_SPECIFIED(400, "OMAS-ASSET-CATALOG-400-001 ",
            "The OMAS Server URL is null or empty",
            "The system is unable to connect to the OMAS Server.",
            "Retry the request when the OMAS Service is available."),

    PARAMETER_NULL(400, "OMAS-ASSET-CATALOG-400-002 ",
            "The parameter for {0} passed on the {0} operation is null",
            "The system is unable to process the request without this parameter.",
            "Correct the code in the caller to provide the parameter for this operation."),

    NULL_USER_ID(400, "OMAS-ASSET-CATALOG-400-003 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),

    OMRS_NOT_INITIALIZED(404, "OMAS-ASSET-CATALOG-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_AVAILABLE(404, "OMAS-ASSET-CATALOG-404-002 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Asset Catalog OMAS is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    NO_METADATA_COLLECTION(404, "OMAS-ASSET-CATALOG-404-003 ",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),

    RELATIONSHIP_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-004 ",
            "The requested relationship {0} is not found in OMAS Server {1}",
            "The system is unable to populate the requested relationship.",
            "Check that the unique identifier for the relationship is correct."),

    RELATIONSHIPS_WITH_PROPERTY_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-005 ",
            "There is no relationship with property {0} available in OMAS Server {1}",
            "The system is unable to populate the requested relationship with the given property.",
            "Check that the property for the relationship is correct."),

    RELATIONSHIPS_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-006 ",
            "There is no relationship for the asset {0} available in OMAS Server {1}",
            "The system is unable to find a relationship with the given property value.",
            "Provide a new criteria for the current search."),

    ASSET_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-007 ",
            "The requested asset {0} is not found in OMAS Server {1}",
            "The system is unable to populate the requested asset.",
            "Check that the unique identifier for the asset is correct."),

    CLASSIFICATION_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-008 ",
            "The requested asset {0} has not classification in OMAS Server {1}",
            "The system is unable to retrieve the classifications for the given asset.",
            "Check that the unique identifier for the asset is correct."),

    ASSET_WITH_CLASSIFICATION_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-009 ",
            "There is no asset available for with given classification {0} in OMAS Server {1}",
            "The system is unable to retrieve the assets with the specified classification.",
            "Check that the identifier for the classification is correct."),

    ASSET_WITH_PROPERTY_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-010 ",
            "There is no asset available for with given property {0} in OMAS Server {1}",
            "The system is unable to retrieve the assets with the specified property.",
            "Check that the unique identifier for the property is correct."),

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

    NO_RELATED_ASSETS(404, "OMAS-ASSET-CATALOG-404-014 ",
            "There is no assets that are connected directly or indirectly to the asset identified {0} in OMAS Server {1}",
            "The system is unable to retrieve the classifications for the given asset.",
            "Check that the unique identifier for the asset is correct."),

    LINKING_RELATIONSHIPS_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-015 ",
            "There is no intermediate relationships that connect the {0} with the {1} in OMAS Server {2}",
            "The system is unable to retrieve the classifications for the given asset.",
            "Check that the unique identifier for the asset is correct."),

    LINKING_ASSETS_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-016 ",
            "There is no intermediate assets that connect the {0} with the {1} in OMAS Server {2}",
            "The system is unable to retrieve the classifications for the given asset.",
            "Check that the unique identifier for the asset is correct."),

    METHOD_NOT_IMPLEMENTED(501, "OMAS-ASSET-CATALOG-501-001 ",
            "The Asset Catalog OMAS method {0}({1}) is not implemented for server {2}",
            "A method in Asset Catalog OMAS does not have a complete implementation.",
            "Raise a Github issue to get this fixed."),

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
