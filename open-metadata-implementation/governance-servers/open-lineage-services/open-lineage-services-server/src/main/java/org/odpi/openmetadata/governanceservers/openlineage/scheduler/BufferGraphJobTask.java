/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.bufferGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;


public class BufferGraphJobTask {

    public BufferGraphJobTask() {

    }

    public void perform() {

        GraphTraversalSource g = bufferGraph.traversal();
        //TODO make a property for label so I can index based on that for faster search
        List<Vertex> vertices = g.V().has(PROPERTY_KEY_ENTITY_NAME,"Process").toList();

        List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

        //TODO check the directions of the relationships and configure the query approprietly
        for (String guid : guidList) {

            List<Object> outputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).until(__.has("PortImplementation", "portType", "OUTPUT_PORT"))
                    .repeat(__.both().simplePath())
                    .until(__.hasLabel("RelationalColumnType").inE("LineageMapping")).repeat(__.both().simplePath()).path().unfold().toList();

            List<Object> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).until(__.has("PortImplementation", "portType", "INPUT_PORT"))
                    .repeat(__.both().simplePath())
                    .until(__.hasLabel("TableColumnType").inE("LineageMapping")).repeat(__.both().simplePath()).path().unfold().toList();


            Vertex columnOut = (Vertex) outputPath.get(outputPath.size() - 1);
            Vertex columnIn = (Vertex) inputPath.get(inputPath.size() - 1);

            String columnOutGuid = columnOut.values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            String columnInGuid = columnIn.values(PROPERTY_KEY_ENTITY_GUID).next().toString();


            if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                MainGraphMapper mainGraphMapper = new MainGraphMapper();

                Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();

                mainGraphMapper.mapStructure(columnInGuid, process, columnOutGuid);
            }

        }

        g.tx().commit();
    }
}
