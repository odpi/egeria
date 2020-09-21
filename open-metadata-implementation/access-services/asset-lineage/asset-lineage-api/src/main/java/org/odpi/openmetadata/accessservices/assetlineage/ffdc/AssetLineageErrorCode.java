/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AssetLineageErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Lineage OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum AssetLineageErrorCode implements ExceptionMessageSet {

    ASSET_NOT_FOUND(404, "OMAS-ASSET-LINEAGE-005 ",
            "The requested asset {0} is not found in OMAS Server {1}",
            "The system is unable to populate the requested asset.",
            "Check that the unique identifier for the asset is correct."),

    OMRS_NOT_INITIALIZED(404, "OMAS-ASSET-LINEAGE-006 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_AVAILABLE(404, "OMAS-ASSET-LINEAGE-007 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Asset Lineage OMAS is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    BAD_OUT_TOPIC_CONNECTION(400, "OMAS-ASSET-LINEAGE-008",
            "The Asset Lineage Open Metadata Access Service (OMAS) has been passed an invalid connection for publishing events.  The connection was {0}.  The resulting exception of {1} included the following message: {2}",
            "The access service has not been passed valid configuration for its out topic connection.",
            "Correct the configuration and restart the service."),

    ENTITY_NOT_FOUND(404, "OMAS-ASSET-LINEAGE-404-009",
            "Cannot get entity from repository, entity does not exist.",
            "The system is unable to get the full context for the queried entity.",
            "Check if the entity queried is available on the repository in the cohort."),

    RELATIONSHIP_NOT_FOUND(404, "OMAS-ASSET-LINEAGE-404-010",
            "Cannot get the relationships from repository, relationship does not exist.",
            "The system is unable to get the relationships for the queried entity.",
            "Check if the relationship queried is available on the repository in the cohort."),

    CLASSIFICATION_MAPPING_ERROR(400, "OMAS-ASSET-LINEAGE-404-011 ",
            "The attempt to map a lineage entity and a classification failed for classification {0}",
            "The system was unable to perform the classification mapping request.",
            "Correct the caller's code and retry the request.");

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
    AssetLineageErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction) {
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