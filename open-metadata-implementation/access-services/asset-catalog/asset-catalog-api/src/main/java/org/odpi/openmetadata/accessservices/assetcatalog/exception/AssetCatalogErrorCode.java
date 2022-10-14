/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.exception;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

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
 * <li>UserAction - describes how an AssetConsumerInterface should correct the error</li>
 * </ul>
 */
public enum AssetCatalogErrorCode implements ExceptionMessageSet {

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

    TYPE_DEF_NOT_FOUND(404, "OMAS-ASSET-CATALOG-404-017 ",
            "The provided entity type {0} is non known to the OMAS Server",
            "The system is unable to retrieve assets of an unknown type/s.",
            "Check that the entity type/s provided is/are correct."),

    SERVICE_NOT_INITIALIZED(503, "OMAS-ASSET-CATALOG-503-001 ",
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to " +
                    "process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration " +
                    "and restart the server.");

    private static final long serialVersionUID = 1L;

    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for AssetCatalogErrorCode expects to be passed one of the enumeration rows defined in
     * AssetCatalogErrorCode above.   For example:
     * <p>
     * AssetCatalogErrorCode   errorCode = AssetCatalogErrorCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode  error code to use over REST calls
     * @param errorMessageId unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction     instructions for resolving the error
     */
    AssetCatalogErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction) {
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
    public ExceptionMessageDefinition getMessageDefinition() {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    public ExceptionMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }

}
