/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum JanusConnectorErrorCode implements AuditLogMessageSet
{
    CANNOT_OPEN_GRAPH_DB("JANUS-GRAPH-LINEAGE-WAREHOUSE-004 ",
            OMRSAuditLogRecordSeverity.STARTUP,
            "Graph cannot be opened with that configuration",
            "It is not possible to open the graph database at path {0} in the {1} method of {2} class for repository {3}",
            "The system was unable to open the graph repository graph database " +
                    "Please check that the graph database exists and is not in use by another process."),
    GRAPH_INITIALIZATION_ERROR("JANUS-GRAPH-LINEAGE-WAREHOUSE-007",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The graph database could not be initialized for open metadata repository",
            "The system was unable to initialize.",
            "Please raise a github issue."),
    GRAPH_TRAVERSAL_EMPTY("JANUS-GRAPH-LINEAGE-WAREHOUSE-014 ",
            OMRSAuditLogRecordSeverity.INFO,
            "The attempt to start querying the graph failed.",
            "The system was unable to retrieve opening of the transactions needed to perform actions to the graph.",
            "Check your configuration properties for the graph"),
    GRAPH_DISCONNECT_ERROR("JANUS-GRAPH-LINEAGE-WAREHOUSE-016",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The graph database could not be closed for open metadata repository",
            "The system was unable to open the graph repository graph database ",
            "Please check that the graph database in a proper state to be closed."),
    PROCESS_MAPPING_ERROR("JANUS-GRAPH-LINEAGE-WAREHOUSE-017",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Something went wrong when trying to map a process",
            "The system was unable to create the context for a process ",
            "Please check that the process data is correct"),
    INDEX_NOT_CREATED("JANUS-GRAPH-LINEAGE-WAREHOUSE-018 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Could not create index",
            "The system is unable to create an index for the property",
            "Correct the information and retry."),
    INDEX_NOT_ENABLED("JANUS-GRAPH-LINEAGE-WAREHOUSE-019 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Could not enable index",
            "The system is unable to enable the index",
            "Correct the information and retry."),
    INDEX_ALREADY_EXISTS("JANUS-GRAPH-LINEAGE-WAREHOUSE-020",
            OMRSAuditLogRecordSeverity.INFO,
            "There is already an index with this name in the open metadata repository",
            "The system is unable to create an index with the name because it already exists.",
            "Correct the index name."),

    VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION("JANUS-GRAPH-LINEAGE-WAREHOUSE-021",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception happened when trying to create vertices and relationships in LineageGraph.",
            "The system is unable to create vertices and relationships",
            "Check the data that needs to be created."),
    ERROR_REMOVING_OBSOLETE_EDGES("JANUS-GRAPH-LINEAGE-WAREHOUSE-022",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception happened while removing obsolete edges for the node",
            "The system is unable to create remove the edges",
            "Check the status of the application and try again"),
    GET_ALL_NEIGHBOURS("JANUS-GRAPH-LINEAGE-WAREHOUSE-023",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not retrieve neighbours for the node",
            "The system is unable to retrieve the nodes from the graph",
            "Check the status of the application and try again"),
    PROPERTIES_UPDATE_EXCEPTION("JANUS-GRAPH-LINEAGE-WAREHOUSE-024",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception happened during update of the properties",
            "The properties of the node could not be updated",
            "Please check the input data"),
    UNABLE_TO_ADD_PROPERTIES_ON_EDGE_FROM_RELATIONSHIP_WITH_TYPE("JANUS-GRAPH-LINEAGE-WAREHOUSE-025",
            OMRSAuditLogRecordSeverity.ERROR,
            "Unable to add properties on edge for relationship",
            "Unable to add properties on edge for relationship",
            "Please check the input data"),
    FAILED_TO_UPDATE_CLASSIFICATION_WITH_GUID(
            "JANUS-GRAPH-LINEAGE-WAREHOUSE-026",
            OMRSAuditLogRecordSeverity.ERROR,
            "Failed to update classification",
            "Failed to update classification",
            "Please check the input data"),
    DELETE_CLASSIFICATION_EXCEPTION("JANUS-GRAPH-LINEAGE-WAREHOUSE-027",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception happened during delete of classifications",
            "Could not delete classification due to error",
            "Check the status of the application and try again"),
    DELETE_RELATIONSHIP_EXCEPTION("JANUS-GRAPH-LINEAGE-WAREHOUSE-028",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception occurred while deleting a relationship",
            "Could not delete relationship due to error",
            "Check the status of the application and try again"),

    DELETE_ENTITY_EXCEPTION("JANUS-GRAPH-LINEAGE-WAREHOUSE-029",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception occurred while deleting an entity",
            "Could not delete entity due to error",
            "Check the status of the application and try again"),
    COULD_NOT_SAVE_LAST_UPDATE_TIME("JANUS-GRAPH-LINEAGE-WAREHOUSE-030",
            OMRSAuditLogRecordSeverity.ERROR,
            "An exception occurred while saving the last update time for the asset lineage job.",
            "Could not save time to the database",
            "Check the status of the application and try again"),
    COULD_NOT_RETRIEVE_VERTEX("JANUS-GRAPH-LINEAGE-WAREHOUSE-031",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not find queried vertex with guid {0}",
            "Could not find queried vertex",
            "Check the status of the application and try again"),
    COULD_NOT_RETRIEVE_LAST_UPDATE_TIME("JANUS-GRAPH-LINEAGE-WAREHOUSE-032",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not retrieve last update time ",
            "Could not retrieve last update time ",
            "Check the status of the database"),
    ENTITY_NOT_FOUND("JANUS-GRAPH-LINEAGE-WAREHOUSE-033",
            OMRSAuditLogRecordSeverity.ERROR,
            "The entity with guid {0} could not be found in the graph",
            "Entity not in graph",
            "Check the input data and try again"),
    LINEAGE_NOT_FOUND("JANUS-GRAPH-LINEAGE-WAREHOUSE-034",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not find lineage for guid {0} with edge labels {1}",
            "Could not find lineage for the queried node",
            "Check the input data and try again"),
    CLASSIFICATION_NOT_FOUND("JANUS-GRAPH-LINEAGE-WAREHOUSE-035",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not find classification, vertexIds {0}",
            "Could not find classification",
            "Check the input data and try again"),
    TYPES_NOT_FOUND("JANUS-GRAPH-LINEAGE-WAREHOUSE-036",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not find available entities types from lineage repository",
            "Could not find available entities types from lineage repository",
            "Check the status of the application and try again"),
    NODES_NOT_FOUND("JANUS-GRAPH-LINEAGE-WAREHOUSE-037",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not find nodes of type {0} with display name containing {1} in the lineage repository",
            "Could not find nodes in lineage repository",
            "Check the status of the application and try again"),
    SEARCH_ERROR("JANUS-GRAPH-LINEAGE-WAREHOUSE-038",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not execute search request {0}",
            "Searching in the database produced an error",
            "Check the search input and try again"),
    HIERARCHY_ERROR("JANUS-GRAPH-LINEAGE-WAREHOUSE-039",
            OMRSAuditLogRecordSeverity.ERROR,
            "Could not execute hierarchy request {0}",
            "Retrieving the element hierarchy produced an error",
            "Check the input and try again");

    private static final Logger log = LoggerFactory.getLogger(JanusConnectorErrorCode.class);
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;
    private OMRSAuditLogRecordSeverity severity;
    AuditLogMessageDefinition auditLogMessageDefinition;

    JanusConnectorErrorCode(String errorMessageId, OMRSAuditLogRecordSeverity severity, String errorMessage,
                            String systemAction, String userAction) {
        this.severity = severity;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.auditLogMessageDefinition = new AuditLogMessageDefinition(errorMessageId,
                severity,
                errorMessage,
                systemAction,
                userAction);
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


    public String getFormattedErrorMessage(String... params) {

        log.debug(String.format("<== JanusConnectorErrorCode.getMessage(%s)", Arrays.toString(params)));


        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> JanusConnectorErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }

    @Override
    public AuditLogMessageDefinition getMessageDefinition() {
        return this.auditLogMessageDefinition;
    }

    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
        this.auditLogMessageDefinition.setMessageParameters(params);
        return auditLogMessageDefinition;
    }
}
