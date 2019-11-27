
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.UUID;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class MainGraphMapper {

    private static final Logger log = LoggerFactory.getLogger(MainGraphMapper.class);


    public void mapStructure(String columnInGuid, Vertex process, String columnOutGuid, JanusGraph mainGraph) {

        GraphTraversalSource main = mainGraph.traversal();

        final String processGuid = process.value(PROPERTY_KEY_ENTITY_GUID);
        final String processName = process.value(PROPERTY_NAME_NAME);

        Iterator<Vertex> columnInVertex = main.V().has(PROPERTY_KEY_ENTITY_GUID, columnInGuid);
        Iterator<Vertex> columnOutVertex = main.V().has(PROPERTY_KEY_ENTITY_GUID, columnOutGuid);
        Iterator<Vertex> processVertex = main.V().has("id",processGuid);

        if (columnInVertex.hasNext() && columnOutVertex.hasNext()) {

            Vertex vertex = main.addV("SubProcess").next();
            vertex.property("id", UUID.randomUUID());
            vertex.property(PROPERTY_KEY_ENTITY_GUID, processGuid);
            vertex.property(PROPERTY_KEY_ENTITY_NAME, processName);

            Vertex columnIn = columnInVertex.next();
            Vertex columnOut = columnOutVertex.next();

            columnIn.addEdge(NODE_LABEL_PROCESS, vertex);
            vertex.addEdge(NODE_LABEL_PROCESS,columnOut);

            if(processVertex.hasNext()){
                Vertex processTopLevel = processVertex.next();
                vertex.addEdge(NODE_LABEL_PROCESS,processTopLevel);
            }
            else {
                Vertex mainProcess = main.addV("Process").next();
                mainProcess.property("id", processGuid);
                mainProcess.property(PROPERTY_KEY_ENTITY_NAME, processName);

                vertex.addEdge(NODE_LABEL_PROCESS,mainProcess);
            }

            main.tx().commit();

        } else {
            log.debug("Columns does not exist in maingraph with guidIn {} and out {}",columnInGuid,columnOutGuid);
            main.tx().rollback();

        }

    }

    private String extractProperty(Vertex process, String propertyName) {

        return process.value(propertyName);
    }


}

