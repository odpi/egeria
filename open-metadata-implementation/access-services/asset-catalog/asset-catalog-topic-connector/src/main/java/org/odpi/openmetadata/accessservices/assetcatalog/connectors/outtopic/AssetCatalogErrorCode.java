/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.connectors.outtopic;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AssetCatalogErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Catalog OMAS Services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum AssetCatalogErrorCode implements ExceptionMessageSet {

    /**
     * A null topic listener has been passed by the user.
     */
    NULL_LISTENER(500, "OMAS-ASSET-CATALOG-500-001",
            "A null topic listener has been passed by user {0} on method {1}",
            "There is a coding error in the caller to the Asset Catalog OMAS.",
            "Correct the caller logic and retry the request.");

    private static final long serialVersionUID = 1L;

    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for AssetCatalogErrorCode expects to be passed one of the enumeration rows defined in
     * AssetCatalogErrorCode above.   For example:
     * <p>
     * AssetCatalogErrorCode   errorCode = AssetCatalogErrorCode.UNKNOWN_ENDPOINT;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode  error code to use over REST calls
     * @param errorMessageId unique ID for the message
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
    @Override
    public ExceptionMessageDefinition getMessageDefinition() {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString() {
        return "AssetCatalogErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
