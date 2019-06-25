/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.handlers;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.janusGraph;

public class QueryHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryHandler.class);

    public String getInitialGraph(String lineageType, String guid) {
        String response = "";
        switch (lineageType) {
            case "ultimate-source":
                GraphTraversalSource g = janusGraph.traversal();
                Vertex v = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).in("Included in").next();
                response = v.property("qualifiedName").value().toString();
                break;
        }
        return response;
    }

    public void exportGraph () {
        try {
            janusGraph.io(IoCore.graphml()).writeGraph("testGraph.graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
