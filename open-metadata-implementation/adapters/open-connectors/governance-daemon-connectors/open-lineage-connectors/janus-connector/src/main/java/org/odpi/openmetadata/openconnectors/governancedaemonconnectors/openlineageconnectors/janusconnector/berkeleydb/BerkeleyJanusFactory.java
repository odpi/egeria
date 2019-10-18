/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.berkeleydb;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.*;


public class BerkeleyJanusFactory {

    private static final Logger log = LoggerFactory.getLogger(BerkeleyJanusFactory.class);


    public static JanusGraph openMainGraph() throws RepositoryErrorException {
        final String storagePath = "./egeria-lineage-repositories/main/berkeley";
        final String indexPath = "./egeria-lineage-repositories/main/searchindex";

        return getJanusGraph(storagePath, indexPath);
    }

    public static JanusGraph openHistoryGraph() throws RepositoryErrorException {
        final String storagePath = "./egeria-lineage-repositories/history/berkeley";
        final String indexPath = "./egeria-lineage-repositories/history/searchindex";

        return getJanusGraph(storagePath, indexPath);
    }

    public static JanusGraph openMockGraph() throws RepositoryErrorException {
        final String storagePath = "./egeria-lineage-repositories/mock/berkeley";
        final String indexPath = "./egeria-lineage-repositories/mock/searchindex";

        return getJanusGraph(storagePath, indexPath);
    }

    private static JanusGraph getJanusGraph(String storagePath, String indexPath) throws RepositoryErrorException {
        JanusGraph janusGraph;
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

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(storagePath, "open", BerkeleyJanusFactory.class.getName());

            throw new RepositoryErrorException(400,
                    BerkeleyJanusFactory.class.getName(),
                    "open",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        try {
            log.info("Updating graph schema, if necessary");
            initializeGraph(janusGraph);
        }
        catch (RepositoryErrorException e) {
            log.error("{} Caught exception during graph initialize operation", "open");
            throw e;
        }


        return janusGraph;
    }

    private static void initializeGraph(JanusGraph graph) throws RepositoryErrorException {

        final String methodName = "initializeGraph";
        /*
         * Create labels for vertex and edge uses
         */

        try {
            JanusGraphManagement management = graph.openManagement();

            Set<String> vertexLabels = new HashSet<>(Arrays.asList(GLOSSARY_TERM, RELATIONAL_COLUMN, RELATIONAL_TABLE_TYPE,
                    RELATIONAL_TABLE, RELATIONAL_DB_SCHEMA_TYPE, DEPLOYED_DB_SCHEMA_TYPE, DATABASE));

            Set<String> relationshipsLabels = new HashSet<>(Arrays.asList(SCHEMA_ATTRIBUTE_TYPE, ATTRIBUTE_FOR_SCHEMA,
                    GLOSSARY_TERM, SEMANTIC_ASSIGNMENT, DEPLOYED_DB_SCHEMA_TYPE, DATABASE));

            // Each vertex has a label that reflects the Asset
            management = checkAndAddLabelVertexOrEdge(vertexLabels, management);
            // Each edge has a label that reflects the Relational Type
            management = checkAndAddLabelVertexOrEdge(relationshipsLabels, management);

            management.commit();
        } catch (Exception e) {

            OpenLineageErrorCode errorCode = OpenLineageErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId();
            throw new RepositoryErrorException(400,
                    BerkeleyJanusFactory.class.getName(),
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
