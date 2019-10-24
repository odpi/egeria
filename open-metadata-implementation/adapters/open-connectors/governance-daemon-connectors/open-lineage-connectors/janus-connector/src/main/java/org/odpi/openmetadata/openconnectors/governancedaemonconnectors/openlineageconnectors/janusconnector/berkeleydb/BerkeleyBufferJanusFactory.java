/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.berkeleydb;


import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.OpenLineageException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsBufferGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsBufferGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;


public class BerkeleyBufferJanusFactory extends IndexingFactory {

    private static final Logger log = LoggerFactory.getLogger(BerkeleyBufferJanusFactory.class);


    public static JanusGraph openBufferGraph(){

        final String methodName = "open";
        JanusGraph janusGraph;

        final String storagePath = "./egeria-lineage-repositories/buffer/berkeley";
        final String indexPath = "./egeria-lineage-repositories/buffer/searchindex";

        JanusGraphFactory.Builder config = JanusGraphFactory.build().
                set("storage.backend", "berkeleyje").
                set("storage.directory", storagePath).
                set("index.search.backend", "lucene").
                set("index.search.directory", indexPath);

        try {

            janusGraph = config.open();

        } catch (Exception e) {
            log.error("{} could not open graph stored at {}", "open", storagePath);
            OpenLineageErrorCode errorCode = OpenLineageErrorCode.CANNOT_OPEN_GRAPH_DB;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(storagePath, methodName, BerkeleyBufferJanusFactory.class.getName());

            throw new OpenLineageException(400,
                    BerkeleyBufferJanusFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }



        try {
            log.info("Updating graph schema, if necessary");
            initializeGraph(janusGraph);
        }
        catch (OpenLineageException e) {
            log.error("{} Caught exception during graph initialize operation", "open");
            throw e;
        }


        return janusGraph;
    }

    private static void initializeGraph(JanusGraph graph){

        final String methodName = "initializeGraph";
        /*
         * Create labels for vertex and edge uses
         */

        try {
            JanusGraphManagement management = graph.openManagement();

            Set<String> vertexLabels = Stream.of(VertexLabelsBufferGraph.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());


            Set<String> relationshipsLabels = Stream.of(EdgeLabelsBufferGraph.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            // Each vertex has a label that reflects the Asset
            management = checkAndAddLabelVertexOrEdge(vertexLabels, management);
            // Each edge has a label that reflects the Relational Type
            management = checkAndAddLabelVertexOrEdge(relationshipsLabels, management);

            management.commit();

            createCompositeIndexForVertexProperty(PROPERTY_NAME_GUID,PROPERTY_KEY_ENTITY_GUID,true,graph);
            createCompositeIndexForVertexProperty(PROPERTY_NAME_NAME,PROPERTY_KEY_ENTITY_NAME,false,graph);

            createCompositeIndexForEdgeProperty(PROPERTY_NAME_LABEL,PROPERTY_KEY_RELATIONSHIP_LABEL,graph);


        } catch (Exception e) {

            OpenLineageErrorCode errorCode = OpenLineageErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId();
            throw new OpenLineageException(400,
                    BerkeleyBufferJanusFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    private static JanusGraphManagement checkAndAddLabelVertexOrEdge(Set<String> labels, JanusGraphManagement management){
        for (String label: labels) {

            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }

        return management;

    }



}


