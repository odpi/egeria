/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.ffdc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum InformationViewErrorCode {


    INVALID_EVENT_FORMAT("OMAS-INFORMATION-VIEW-001",
            "Event{0} could not be parsed",
            "The system is unable to process the request.",
            "Verify the event published to the topic."),
    PUBLISH_EVENT_EXCEPTION("OMAS-INFORMATION-VIEW-002",
            "Event {0} could not be published: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PROCESS_EVENT_EXCEPTION("OMAS-INFORMATION-VIEW-003",
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    ADD_ENTITY_EXCEPTION("OMAS-INFORMATION-VIEW-004",
            "Entity {0} could not be added. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_RELATIONSHIP_EXCEPTION("OMAS-INFORMATION-VIEW-005",
            "Relationship {0} could not be added. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_ENTITY_EXCEPTION("OMAS-INFORMATION-VIEW-006",
            "Entity matching criteria [{0}={1}] could not be fetched. Error: {2}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_RELATIONSHIP_EXCEPTION("OMAS-INFORMATION-VIEW-007",
            "Relationship {0} could not be fetched. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    BUILD_CONTEXT_EXCEPTION(500,"OMAS-INFORMATION-VIEW-008",
            "Full context could not be built for entity {0}. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    PARSE_EVENT("OMAS-INFORMATION-VIEW-009",
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_CLASSIFICATION("OMAS-INFORMATION-VIEW-010",
            "Unable to create classification {0} for entity of type {1}. Error: {2}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    REPORT_CREATION_EXCEPTION("OMAS-INFORMATION-VIEW-011",
            "Unable to create report based on received json {0}. Error: {1}",
            "The system is unable to process the request.",
            "Verify the post request."),
    SERVICE_NOT_INITIALIZED("OMAS-INFORMATION-VIEW-012",
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    BAD_CONFIG("OMAS-INFORMATION-VIEW-013",
            "The Information View Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property.  The resulting exception of {2} included the following message: {3}",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),
    ENTITY_NOT_FOUND_EXCEPTION(500, "OMAS-INFORMATION-VIEW-014",
            "The entity matching criteria [{0}={1}] was not found.",
            "The system is unable to process the request.",
            "Correct the request payload submitted."),
    INCORRECT_TYPE_EXCEPTION(500, "OMAS-INFORMATION-VIEW-015",
            "The entity matching criteria [{0}={1}] is not of type {2}.",
            "The system is unable to process the request.",
            "Correct the request payload submitted."),
    INCORRECT_MODEL_EXCEPTION(500, "OMAS-INFORMATION-VIEW-016",
            "The model for entity {0} is not correct: {1}",
            "The system is unable to process the request.",
            "Correct the metadata model.");

    private static final Logger log = LoggerFactory.getLogger(InformationViewErrorCode.class);
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;
    private int httpErrorCode;

    InformationViewErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction) {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    InformationViewErrorCode(String errorMessageId, String errorMessage, String systemAction, String userAction) {
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
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

    public String getFormattedErrorMessage(String... params) {//TODO this should be moved to common code base

        log.debug(String.format("<== InformationViewErrorCode.getMessage(%s)", Arrays.toString(params)));


        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> InformationViewErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }
}
