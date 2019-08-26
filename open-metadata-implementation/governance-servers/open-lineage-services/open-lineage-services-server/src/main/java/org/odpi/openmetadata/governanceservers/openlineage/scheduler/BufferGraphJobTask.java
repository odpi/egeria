/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.bufferGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;


public class BufferGraphJobTask {

    private static final Logger log = LoggerFactory.getLogger(BufferGraphJobTask.class);

    public BufferGraphJobTask() { }

    public void perform() {

        GraphTraversalSource g = bufferGraph.traversal();
        List<Vertex> vertices = g.V().has(PROPERTY_KEY_ENTITY_NAME, "Process").toList();

        List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

        for (String guid : guidList) {
            Iterator<Vertex> initial =  g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).has("displayName","initial_load");
            if(!initial.hasNext()) {


                List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out("ProcessPort").out("PortDelegation").has("PortImplementation", "portType", "INPUT_PORT")
                        .out("PortSchema").out("AttributeForSchema").out("SchemaAttributeType").in("LineageMapping").in("SchemaAttributeType")
                        .toList();

                Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
                for (Vertex vertex : inputPath) {
                    String vertexGuid = vertex.value(PROPERTY_KEY_ENTITY_GUID);
                    Iterator<Vertex> r = g.V().has(PROPERTY_KEY_ENTITY_GUID, vertexGuid).out("SchemaAttributeType").out("LineageMapping");

                    Iterator<Vertex> columnOut = findPathForOutputAsset(r.next(), g);
                    if (columnOut != null && columnOut.hasNext()) {
                        String columnOutGuid = columnOut.next().values(PROPERTY_KEY_ENTITY_GUID).next().toString();
                        String columnInGuid = vertex.values(PROPERTY_KEY_ENTITY_GUID).next().toString();


                        if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                            MainGraphMapper mainGraphMapper = new MainGraphMapper();
                            mainGraphMapper.mapStructure(columnInGuid, process, columnOutGuid);
                        }
                    }
                }
            }
        }
        g.tx().commit();
    }

    private Iterator<Vertex> findPathForOutputAsset(Vertex v, GraphTraversalSource g)  {

        try{
            Iterator<Vertex> end = g.V(v.id()).both("SchemaAttributeType").or(__.has(PROPERTY_KEY_ENTITY_NAME, RELATIONAL_COLUMN),
                    __.has(PROPERTY_KEY_ENTITY_NAME, TABULAR_COLUMN));

            if (!end.hasNext()) {

                Iterator<Vertex> next = g.V(v.id()).out("LineageMapping");
                return findPathForOutputAsset(next.next(), g);
            }
            return end;}
        catch (Exception e){
            log.debug("Vertex does not exitst");
            return null;
        }
    }
}

