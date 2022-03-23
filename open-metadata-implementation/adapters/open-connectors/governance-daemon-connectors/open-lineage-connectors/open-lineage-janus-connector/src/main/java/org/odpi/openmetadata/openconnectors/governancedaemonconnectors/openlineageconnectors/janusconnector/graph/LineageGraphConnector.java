/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import lombok.EqualsAndHashCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageQueryService;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraphConnectorBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphHelper;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.GRAPH_DISCONNECT_ERROR;

@EqualsAndHashCode(callSuper = true)
public class LineageGraphConnector extends LineageGraphConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphConnector.class);
    public static final String CLOSE_LINEAGE_GRAPH_EXCEPTION = "Exception while closing lineage graph";
    public static final String EXCEPTION_WHILE_CLOSING_LINEAGE_GRAPH_MESSAGE = CLOSE_LINEAGE_GRAPH_EXCEPTION + ": ";
    public static final String CLOSE_LINEAGE_GRAPH_EXCEPTION_MESSAGE = EXCEPTION_WHILE_CLOSING_LINEAGE_GRAPH_MESSAGE;
    public static final String INPUT_PORT = "INPUT_PORT";
    public static final String OLS_HAS_CORRESPONDING_ELEMENTS = "OLS has added the corresponding subProcess node and edges for input column {}, output column {} and process {} ";
    public static final String VERTEX_NOT_FOUND = "Vertex does not exist with guid {} and display name {}";
    public static final String THE_LINEAGE_GRAPH_COULD_NOT_BE_INITIALIZED_DUE_TO_AN_ERROR = "The Lineage graph could not be initialized due to an error";
    public static final String SOMETHING_WENT_WRONG_WHEN_TRYING_TO_MAP_A_PROCESS = "Something went wrong when trying to map a process.";

    private GraphHelper graphHelper;
    private LineageGraphStorageService graphStorageHelper;
    private LineageGraphQueryService lineageGraphQueryService;
    private AuditLog auditLog;

    /**
     * Instantiates the graph based on the configuration passed.
     */
    public void initializeGraphDB(AuditLog auditLog) throws OpenLineageException {
        this.auditLog = auditLog;
        try {
            this.graphHelper = new GraphHelper();

            this.graphHelper.openGraph(connectionProperties.getConnectorType().getConnectorProviderClassName(),
                    connectionProperties.getConfigurationProperties(), auditLog);

            this.graphStorageHelper = new LineageGraphStorageService(graphHelper, auditLog);
            this.lineageGraphQueryService = new LineageGraphQueryService(graphHelper);

        } catch (JanusConnectorException error) {
            log.error(THE_LINEAGE_GRAPH_COULD_NOT_BE_INITIALIZED_DUE_TO_AN_ERROR, error);
            throw new OpenLineageException(500, error.getReportingClassName(), error.getReportingActionDescription(),
                    error.getReportedErrorMessage(), error.getReportedSystemAction(), error.getReportedUserAction());
        }
    }


    @Override
    public synchronized void disconnect() throws ConnectorCheckedException {
        try {
            graphHelper.closeGraph();
            super.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error(CLOSE_LINEAGE_GRAPH_EXCEPTION_MESSAGE, e);
            this.auditLog.logException(CLOSE_LINEAGE_GRAPH_EXCEPTION, GRAPH_DISCONNECT_ERROR.getMessageDefinition(), e);
            throw e;
        }
    }

    @Override
    public void performLineageGraphJob() {
        LineageJobHelper lineageJobHelper = new LineageJobHelper(graphHelper, auditLog);
        lineageJobHelper.performLineageGraphJob();
    }
    @Override
    public OpenLineageQueryService getLineageQueryService(){
        return lineageGraphQueryService;
    }

    @Override
    public LineageGraph getLineageStorageService(){
        return this.graphStorageHelper;
    }

}

