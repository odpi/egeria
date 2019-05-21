/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.performancetesting;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.*;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;


public class GraphOMRSGraphFactory {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSGraphFactory.class);

    private static JanusGraph graph;

    public static JanusGraph open() {

        final String methodName = "open";

        // Open method is called from within synchronized block in graph repository metadata store class.

        // Use the JGF.Builder and construct the configuration in-line.
        // There is no synch yet on this.

        // Run with a Lucene indexing backend for now - if you pull in ES you need to use JG-server
        // or start your own ES cluster. If/when you pull the janusgraph-es module into the build
        // you will need to configure the component-scan otherwise Spring boot tries to autoconfigure a
        // REST client which fails (on HttpHost).

        final String storageBackend = "berkeleyje";
        final String storagePath = "./egeria-graph-repository/berkeley";

        final String indexBackend = "lucene";
        final String indexPath = "./egeria-graph-repository/searchindex";

        JanusGraphFactory.Builder config = JanusGraphFactory.build().
                set("storage.backend", storageBackend).
                set("storage.directory", storagePath).
                set("index.search.backend", indexBackend).
                set("index.search.directory", indexPath);

        try {
            graph = config.open();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return graph;
    }
}