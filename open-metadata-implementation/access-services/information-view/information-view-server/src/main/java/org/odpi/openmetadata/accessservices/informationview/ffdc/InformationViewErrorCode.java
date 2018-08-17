/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.ffdc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum InformationViewErrorCode {


    INVALID_EVENT_FORMAT("OMAS-INFORMATIONVIEW-001",
            "Event{0} could not be parsed",
            "The system is unable to process the request.",
            "Verify the event published to the topic."),
    PUBLISH_EVENT_EXCEPTION("OMAS-INFORMATIONVIEW-002",
            "Event{0} could not be published",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PROCESS_EVENT_EXCEPTION("OMAS-INFORMATIONVIEW-003",
            "Event {0} could not be consumed",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    ADD_ENTITY_EXCEPTION("OMAS-INFORMATIONVIEW-004",
            "Entity {0} could not be added",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_RELATIONSHIP_EXCEPTION("OMAS-INFORMATIONVIEW-005",
            "Relationship {0} could not be added",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_ENTITY_EXCEPTION("OMAS-INFORMATIONVIEW-006",
            "Entity {0} could not be fetched",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_RELATIONSHIP_EXCEPTION("OMAS-INFORMATIONVIEW-007",
            "Relationship {0} could not be fetched",
            "The system is unable to process the request.",
            "Verify the topic event."),
    BUILD_COLUMN_CONTEXT_EXCEPTION("OMAS-INFORMATIONVIEW-008",
            "Full context could not be built for column {0} because of {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    PARSE_EVENT("OMAS-INFORMATIONVIEW-009",
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event.");
    private static final Logger log = LoggerFactory.getLogger(InformationViewErrorCode.class);
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

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


    public String getFormattedErrorMessage(String... params) {//TODO this should be moved to common code base

        log.debug(String.format("<== OCFErrorCode.getMessage(%s)", Arrays.toString(params)));


        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OCFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }
}
