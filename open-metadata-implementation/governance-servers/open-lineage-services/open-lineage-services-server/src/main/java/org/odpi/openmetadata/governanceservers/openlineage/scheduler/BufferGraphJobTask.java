/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

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

//            if(guid.equals("717e5b5e-4e57-4712-9b92-fa8ea59eb7ac")) {
//                List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).until(__.hasLabel("PortAlias")).repeat(__.out())
//                        .until(__.has("PortImplementation", "portType", "INPUT_PORT"))
//                        .repeat(__.out().simplePath()).until(__.hasLabel("TabularSchemaType"))
//                        .repeat(__.out().simplePath()).until(__.hasLabel("TabularColumnType"))
//                        .repeat(__.out().simplePath()).in("LineageMapping").out("SchemaAttributeType").toList();

            List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out("ProcessPort").out("PortDelegation").has("PortImplementation", "portType", "INPUT_PORT")
                    .out("PortSchema").out("AttributeForSchema").out("SchemaAttributeType").in("LineageMapping").out("SchemaAttributeType").toList();


                for (Vertex vertex : inputPath) {
//                    String tt = inputPath.get(0).value(PROPERTY_KEY_ENTITY_GUID);

//                    if (tt.equals("b1c497ce.60641b50.1526oa25u.2mgku9e.96tq2k.3jrins518s0usrget795o")) {
//                        Iterator<Vertex> r = g.V(vertex.id()).in("SchemaAttributeType").out("LineageMapping");
                        String a = vertex.value(PROPERTY_KEY_ENTITY_GUID);
                        Iterator<Vertex> r = g.V().has(PROPERTY_KEY_ENTITY_GUID, a).in("SchemaAttributeType").out("LineageMapping");

//                        System.out.println((String) r.next().value(PROPERTY_KEY_ENTITY_GUID));

                        Iterator<Vertex> columnOut = test(r.next(), g);

//                    //TODO make this more dynamic
//                    Iterator<Vertex> columnOut = g.V(vertex.id()).in("SchemaAttributeType").out("LineageMapping")
//                            .out("LineageMapping").out("LineageMapping")
//                            .out("LineageMapping").out("LineageMapping")
//                            .out("LineageMapping").out("LineageMapping")
//                            .out("SchemaAttributeType");

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
//                }
//            }

        }

        g.tx().commit();
    }

    private Iterator<Vertex> test(Vertex v,GraphTraversalSource g){

//        if(v.hasNext()){
            Iterator<Vertex> end = g.V(v.id()).out("SchemaAttributeType");
            System.out.println((String) v.value(PROPERTY_KEY_ENTITY_GUID));

            if(!end.hasNext()){
                    Iterator<Vertex> next = g.V(v.id()).out("LineageMapping");
                    return test(next.next(),g);
                }

                return end;
//            }
//        return null;
    }
}
