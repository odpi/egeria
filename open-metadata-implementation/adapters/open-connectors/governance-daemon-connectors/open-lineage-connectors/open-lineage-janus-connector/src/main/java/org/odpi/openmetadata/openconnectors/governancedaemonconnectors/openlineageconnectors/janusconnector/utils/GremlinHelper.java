/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Map;

public class GremlinHelper {

    public void updatePropertiesQuery(GraphTraversalSource g, Vertex vertex, Map<Object,String> properties){

        GraphTraversal<Map<Object, String>, Vertex> x = g.inject(properties)
                .unfold()
                .as("properties")
                .V(vertex.id())
                .as("v")
                .sideEffect(__.select("properties")
                        .unfold()
                        .as("kv")
                        .select("v")
                        .property(__.select("kv").by(Column.keys), __.select("kv").by(Column.values)));
        g.tx().commit();
    }

}
