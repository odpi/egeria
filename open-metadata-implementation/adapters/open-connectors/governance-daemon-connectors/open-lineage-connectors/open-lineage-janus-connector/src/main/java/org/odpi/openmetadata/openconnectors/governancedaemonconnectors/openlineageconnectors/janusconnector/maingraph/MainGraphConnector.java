/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.tinkerpop.io.graphson.JanusGraphSONModuleV2d0;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.maingraph.MainGraphConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.model.*;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class MainGraphConnector extends MainGraphConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(MainGraphConnector.class);

    private JanusGraph mainGraph;
    private MainGraphConnectorHelper helper;

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);
    }

    public void initializeGraphDB() {
        String graphDB = connectionProperties.getConfigurationProperties().get("graphDB").toString();
        GraphFactory graphFactory = new GraphFactory();
        this.mainGraph = graphFactory.openGraph(graphDB, connectionProperties);
        this.helper = new MainGraphConnectorHelper(mainGraph);
    }

    /**
     * Returns a lineage subgraph.
     *
     * @param scope                  source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param view                   The view queried by the user: hostview, tableview, columnview.
     * @param guid                   The guid of the node of which the lineage is queried from.
     * @param displayNameMustContain Used to filter out nodes which displayname does not contain this value.
     * @param includeProcesses       Will filter out all processes and subprocesses from the response if false.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public LineageResponse lineage(Scope scope, View view, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException {
        String methodName = "MainGraphConnector.lineage";

        GraphTraversalSource g = mainGraph.traversal();
        try {
            g.V().has(PROPERTY_KEY_ENTITY_NODE_ID, guid).next();
        } catch (NoSuchElementException e) {
            OpenLineageServerErrorCode errorCode = OpenLineageServerErrorCode.NODE_NOT_FOUND;
            throw new OpenLineageException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorCode.getFormattedErrorMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        String edgeLabel = helper.getEdgeLabel(view);
        LineageVerticesAndEdges lineageVerticesAndEdges = null;

        switch (scope) {
            case SOURCE_AND_DESTINATION:
                lineageVerticesAndEdges = helper.sourceAndDestination(edgeLabel, guid);
                break;
            case END_TO_END:
                lineageVerticesAndEdges = helper.endToEnd(edgeLabel, guid);
                break;
            case ULTIMATE_SOURCE:
                lineageVerticesAndEdges = helper.ultimateSource(edgeLabel, guid);
                break;
            case ULTIMATE_DESTINATION:
                lineageVerticesAndEdges = helper.ultimateDestination(edgeLabel, guid);
                break;
            case GLOSSARY:
                lineageVerticesAndEdges = helper.glossary(guid);
                break;
        }
        if (!includeProcesses)
            helper.filterOutProcesses(lineageVerticesAndEdges);
        if (!displayNameMustContain.isEmpty())
            helper.filterDisplayName(lineageVerticesAndEdges, displayNameMustContain);
        LineageResponse lineageResponse = new LineageResponse(lineageVerticesAndEdges);
        return lineageResponse;
    }
    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     */
    public void dumpMainGraph() {
        try {
            mainGraph.io(IoCore.graphml()).writeGraph("mainGraph.graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @return The queried graph, in graphSON format.
     */
    public String exportMainGraph() {
        OutputStream out = new ByteArrayOutputStream();
        GraphSONMapper mapper = GraphSONMapper.build().addCustomModule(JanusGraphSONModuleV2d0.getInstance()).create();
        GraphSONWriter writer = GraphSONWriter.build().mapper(mapper).wrapAdjacencyList(true).create();
        try {
            writer.writeGraph(out, mainGraph);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return out.toString();
    }

    public Object getMainGraph() {
        return mainGraph;
    }

}
