/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.bufferGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;


public class BufferGraphJobTask {

    public BufferGraphJobTask() { }

    public void perform() {

        GraphTraversalSource g = bufferGraph.traversal();
        List<Vertex> vertices = g.V().has(PROPERTY_KEY_ENTITY_NAME, "Process").toList();

        List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

        for (String guid : guidList) {

            List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out("ProcessPort").out("PortDelegation").has("PortImplementation", "portType", "INPUT_PORT")
                    .out("PortSchema").out("AttributeForSchema").out("SchemaAttributeType").in("LineageMapping").out("SchemaAttributeType").toList();

            for (Vertex vertex : inputPath) {
                String a = vertex.value(PROPERTY_KEY_ENTITY_GUID);
                Iterator<Vertex> r = g.V().has(PROPERTY_KEY_ENTITY_GUID, a).in("SchemaAttributeType").out("LineageMapping");

                Iterator<Vertex> columnOut = findPathForOutputAsset(r.next(), g);

                if (columnOut.hasNext()) {
                    String columnOutGuid = columnOut.next().values(PROPERTY_KEY_ENTITY_GUID).next().toString();
                    String columnInGuid = vertex.values(PROPERTY_KEY_ENTITY_GUID).next().toString();


                    if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                        MainGraphMapper mainGraphMapper = new MainGraphMapper();

                        Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
                        mainGraphMapper.mapStructure(columnInGuid, process, columnOutGuid);
                    }
                }
            }
        }
        g.tx().commit();
    }

    private Iterator<Vertex> findPathForOutputAsset(Vertex v, GraphTraversalSource g) {

        Iterator<Vertex> end = g.V(v.id()).out("SchemaAttributeType");

        if (!end.hasNext()) {
            Iterator<Vertex> next = g.V(v.id()).out("LineageMapping");
            return findPathForOutputAsset(next.next(), g);
        }
        return end;
    }
}
