/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.UUID;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.mainGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;

public class MainGraphMapper {

    private static final Logger log = LoggerFactory.getLogger(MainGraphMapper.class);


    public void mapStructure(String columnInGuid, Vertex process, String columnOutGuid) {

        GraphTraversalSource main = mainGraph.traversal();

        Iterator<Vertex> columnInVertex = main.V().has(PROPERTY_KEY_ENTITY_GUID, columnInGuid);
        Iterator<Vertex> columnOutVertex = main.V().has(PROPERTY_KEY_ENTITY_GUID, columnOutGuid);

        if (columnInVertex.hasNext() && columnOutVertex.hasNext()) {

            Vertex vertex = main.addV("Process").next();
            vertex.property("id", UUID.randomUUID());
            vertex.property(PROPERTY_KEY_ENTITY_GUID, extractProperty(process, PROPERTY_KEY_ENTITY_GUID));
            vertex.property(PROPERTY_KEY_ENTITY_NAME, extractProperty(process, PROPERTY_KEY_ENTITY_NAME));

            columnInVertex.next().addEdge(LINEAGE_MAPPING, vertex);
            vertex.addEdge(LINEAGE_MAPPING, columnOutVertex.next());

            main.tx().commit();

        } else {
            log.debug("Columns does not exist in maingraph");
            main.tx().rollback();

        }

    }

    private String extractProperty(Vertex process, String propertyName) {

        return process.values(propertyName).next().toString();
    }
}
