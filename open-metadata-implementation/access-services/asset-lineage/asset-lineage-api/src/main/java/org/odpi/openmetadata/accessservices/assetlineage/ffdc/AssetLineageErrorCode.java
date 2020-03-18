/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.ffdc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public enum AssetLineageErrorCode {

    PUBLISH_EVENT_EXCEPTION("OMAS-ASSET-LINEAGE-001 ",
            "Event {0} could not be published: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PROCESS_EVENT_EXCEPTION("OMAS-ASSET-LINEAGE-002 ",
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PARSE_EVENT("OMAS-ASSET-LINEAGE-003 ",
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event."),
    SERVICE_NOT_INITIALIZED("OMAS-ASSET-LINEAGE-004 ",
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
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

    public int getHTTPErrorCode() {
        return httpErrorCode;
    }

    private static final Logger log = LoggerFactory.getLogger(AssetLineageErrorCode.class);
    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    AssetLineageErrorCode(String errorMessageId, String errorMessage, String systemAction, String userAction) {
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    AssetLineageErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
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

    public int getHttpErrorCode() {
        return httpErrorCode;
    }


    public String getFormattedErrorMessage(String... params) {
        MessageFormat mf = new MessageFormat(errorMessage);
        return mf.format(params);
    }
}
