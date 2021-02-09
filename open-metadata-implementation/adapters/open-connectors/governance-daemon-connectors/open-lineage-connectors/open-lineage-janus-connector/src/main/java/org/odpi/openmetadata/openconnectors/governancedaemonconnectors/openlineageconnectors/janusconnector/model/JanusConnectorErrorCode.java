/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum JanusConnectorErrorCode implements AuditLogMessageSet {


    CANNOT_OPEN_GRAPH_DB("OPEN-LINEAGE-SERVICES-004 ",
            OMRSAuditLogRecordSeverity.STARTUP,
            "Graph cannot be opened with that configuration",
            "It is not possible to open the graph database at path {0} in the {1} method of {2} class for repository {3}",
            "The system was unable to open the graph repository graph database " +
                    "Please check that the graph database exists and is not in use by another process."),
    GRAPH_INITIALIZATION_ERROR( "OPEN-LINEAGE-SERVICES-007",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The graph database could not be initialized for open metadata repository",
            "The system was unable to initialize.",
            "Please raise a github issue."),
    GRAPH_TRAVERSAL_EMPTY( "OPEN-LINEAGE-SERVICES-014 ",
            OMRSAuditLogRecordSeverity.INFO,
            "The attempt to start querying the graph failed.",
            "The system was unable to retrieve opening of the transactions needed to perform actions to the graph.",
            "Check your configuration properties for the graph"),
    GRAPH_DISCONNECT_ERROR( "OPEN-LINEAGE-SERVICES-016",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The graph database could not be closed for open metadata repository",
            "The system was unable to open the graph repository graph database ",
            "Please check that the graph database in a proper state to be closed."),
    PROCESS_MAPPING_ERROR( "OPEN-LINEAGE-SERVICES-017",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Something went wrong when trying to map a process",
            "The system was unable to create the context for a process ",
            "Please check that the process data is correct"),
    INDEX_NOT_CREATED( "OPEN-LINEAGE-SERVICES-018 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Could not create index",
            "The system is unable to create an index for the property",
            "Correct the information and retry."),
    INDEX_NOT_ENABLED( "OPEN-LINEAGE-SERVICES-019 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Could not enable index",
            "The system is unable to enable the index",
            "Correct the information and retry."),
    INDEX_ALREADY_EXISTS( "OPEN-LINEAGE-SERVICES-020",
            OMRSAuditLogRecordSeverity.INFO,
            "There is already an index with this name in the open metadata repository",
            "The system is unable to create an index with the name because it already exists.",
            "Correct the index name.");


    private static final Logger log = LoggerFactory.getLogger(JanusConnectorErrorCode.class);
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;
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


    public String getFormattedErrorMessage(String... params) {//TODO this should be moved to common code base

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
