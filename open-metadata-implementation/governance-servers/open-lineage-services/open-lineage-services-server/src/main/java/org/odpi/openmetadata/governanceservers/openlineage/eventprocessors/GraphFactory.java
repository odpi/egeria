/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSGraphFactory;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;

public class GraphFactory {

    private static final Logger log = LoggerFactory.getLogger(GraphFactory.class);

    private static JanusGraph janusGraph;

    public static JanusGraph open() throws RepositoryErrorException {

        final String storagePath = "./egeria-lineage-repository/berkeley";
        final String indexPath = "./egeria-lineage-repository/searchindex";

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

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(storagePath, "open", GraphOMRSGraphFactory.class.getName());

            throw new RepositoryErrorException(400,
                    GraphOMRSGraphFactory.class.getName(),
                    "open",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }



        try {
            log.info("Updating graph schema, if necessary");
            initializeGraph(janusGraph);
        }
        catch (Exception e) {
            log.error(e.getMessage());
//        catch (RepositoryErrorException e) {
            // rollback and re-throw
//            g.tx().rollback();
            throw e;
        }


        return janusGraph;
    }

    private static void initializeGraph(JanusGraph graph){

        /*
         * Create labels for vertex and edge uses
         */

        JanusGraphManagement management = graph.openManagement();

        Set<String> vertexLabels = new HashSet<>(Arrays.asList(GLOSSARY_TERM,RELATIONAL_COLUMN,RELATIONAL_TABLE_TYPE,
                RELATIONAL_TABLE,RELATIONAL_DB_SCHEMA_TYPE,DEPLOYED_DB_SCHEMA_TYPE,DATABASE));

        Set<String> relationshipsLabels = new HashSet<>(Arrays.asList(SCHEMA_ATTRIBUTE_TYPE,ATTRIBUTE_FOR_SCHEMA,
                GLOSSARY_TERM,SEMANTIC_ASSIGNMENT,DEPLOYED_DB_SCHEMA_TYPE,DATABASE));

        // Each vertex has a label that reflects the Asset
        management = checkAndAddLabelVertexOrEdge(vertexLabels,management);
        // Each edge has a label that reflects the Relational Type
        management =  checkAndAddLabelVertexOrEdge(relationshipsLabels,management);

        management.commit();
    }

    private static JanusGraphManagement checkAndAddLabelVertexOrEdge(Set<String> labels, JanusGraphManagement management){
        for (String label: labels) {
            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }

       return management;

    }
}
