/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.handlers;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.*;

public class QueryHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryHandler.class);

    public String getInitialGraph(String lineageType, String guid) {
        String response = "";
        switch (lineageType) {
            case "ultimate-source":
                GraphTraversalSource g = mainGraph.traversal();
                Vertex v = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).in("Included in").next();
                response = v.property("qualifiedName").value().toString();
                break;
        }
        return response;
    }

    public void dumpGraph(String graph) {
        try {
            switch (graph){
                case "main":
                    mainGraph.io(IoCore.graphml()).writeGraph("graphMain.graphml");
                    break;
                case "buffer":
                    bufferGraph.io(IoCore.graphml()).writeGraph("graphBuffer.graphml");
                    break;
                case "history":
                    historyGraph.io(IoCore.graphml()).writeGraph("graphHistory.graphml");
                    break;
                case "mock":
                    mockGraph.io(IoCore.graphml()).writeGraph("graphMock.graphml");
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public String exportGraph(String graph) {
        OutputStream out = new ByteArrayOutputStream();
        try {
            switch (graph){
                case "main":
                    GraphSONWriter.build().create().writeGraph(out, mainGraph);
                    break;
                case "buffer":
                    GraphSONWriter.build().create().writeGraph(out, bufferGraph);
                    break;
                case "history":
                    GraphSONWriter.build().create().writeGraph(out, historyGraph);
                    break;
                case "mock":
                    GraphSONWriter.build().create().writeGraph(out, mockGraph);
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return out.toString();
    }
}
