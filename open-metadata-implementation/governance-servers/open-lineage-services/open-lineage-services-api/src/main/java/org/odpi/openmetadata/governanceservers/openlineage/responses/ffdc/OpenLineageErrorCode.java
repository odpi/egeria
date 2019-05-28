/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum OpenLineageErrorCode {


    PROCESS_EVENT_EXCEPTION("OPEN-LINEAGE-SERVICES-001 ",
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PARSE_EVENT("OPEN-LINEAGE-SERVICES-002 ",
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event."),
    SERVICE_NOT_INITIALIZED("OPEN-LINEAGE-SERVICES-003 ",
            "The Open Lineage Services have not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    CANNOT_OPEN_GRAPH_DB("OPEN-LINEAGE-SERVICES-004",
            "Graph cannot be opened with that configuration",
            "It is not possible to open the graph database at path {0} in the {1} method of {2} class for repository {3}",
            "The system was unable to open the graph repository graph database " +
                    "Please check that the graph database exists and is not in use by another process."),
    ENTITY_ALREADY_EXISTS( "OPEN-LINEAGE-SERVICES-005 ",
            "There is an already an entity with GUID {0} so cannot honour request to create entity in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already an entity with the same GUID.",
            "Correct the caller's code and retry the request."),
    GRAPH_INITIALIZATION_ERROR( "OPEN-LINEAGE-SERVICES-006 ",
            "The graph database could not be initialized for open metadata repository {0}",
            "The system was unable to initialize.",
            "Please raise a github issue.");

    private static final Logger log = LoggerFactory.getLogger(OpenLineageErrorCode.class);
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    OpenLineageErrorCode(String errorMessageId, String errorMessage, String systemAction, String userAction) {
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

        log.debug(String.format("<== OpenLineageErrorCode.getMessage(%s)", Arrays.toString(params)));


        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> OpenLineageErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }
}
