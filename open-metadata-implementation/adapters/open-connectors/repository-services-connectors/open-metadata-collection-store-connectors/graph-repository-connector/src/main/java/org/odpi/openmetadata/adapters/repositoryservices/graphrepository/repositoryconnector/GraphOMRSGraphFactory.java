/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

import org.janusgraph.core.schema.Mapping;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;


public class GraphOMRSGraphFactory {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSGraphFactory.class);

    private static JanusGraph   graph;
    private static String       thisRepositoryName;
    private static final String INDEX_NAME    = "search";
    private static OMRSAuditLog thisAuditLog  = null;

    public enum MixedIndexMapping {
        Default,
        Text,
        String
    }

    public static JanusGraph open(String         metadataCollectionId,
                                  String         repositoryName,
                                  OMRSAuditLog   auditLog)
            throws
            RepositoryErrorException
    {

        final String methodName = "open";

        thisRepositoryName = repositoryName;
        thisAuditLog = auditLog;

        // Open method is called from within synchronized block in graph repository metadata store class.

        // Use the JGF.Builder and construct the configuration in-line.
        // There is no synch yet on this.

        // Run with a Lucene indexing backend for now - if you pull in ES you need to use JG-server
        // or start your own ES cluster. If/when you pull the janusgraph-es module into the build
        // you will need to configure the component-scan otherwise Spring boot tries to autoconfigure a
        // REST client which fails (on HttpHost).

        final String storageBackend = "berkeleyje";
        final String storagePath    = "./egeria-graph-repository/berkeley";

        final String indexBackend = "lucene";
        final String indexPath    = "./egeria-graph-repository/searchindex";

        JanusGraphFactory.Builder config = JanusGraphFactory.build().
            set("storage.backend",storageBackend).
            set("storage.directory",storagePath).
            set("index.search.backend",indexBackend).
            set("index.search.directory",indexPath);

        try {
            graph = config.open();
        }
        catch (Exception e) {
            log.error("{} could not open graph stored at {}", methodName, storagePath );
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.CANNOT_OPEN_GRAPH_DB;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(storagePath,methodName,
                    GraphOMRSGraphFactory.class.getName(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    GraphOMRSGraphFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        /*
         *  The graph has been opened - it may be new or it may have already existed.
         *
         *  The following start logic is performed:
         *
         *    Look for the control vertex:
         *    If no control vertex exists then the graph is new - add a control vertex stamping into it the mdcId and creation/open date. Generate an audit log entry recording creation.
         *    If the control vertex does exist the graph already existed - check the control vertex mdcId matches the configured mdcId of this repository
         *    If they do not match then STOP - generate audit log entry recording creation. The user will need to consciously adopt the graph database.
         *    If they do match then record the open time in the control vertex and generate an audit log entry to record the open.
         */


        GraphTraversalSource g = graph.traversal();
        // This traversal will unfortunately generate an index warning. Try to find a better bootstrap trigger mechanism.
        // TODO Maybe use reserved GUID (it is indexed)


        if (g.V().hasLabel("Control").count().next() == 0) {

            // graph is new

            try {
                // Add a control vertex, initialize the schema and update the control vertex...
                g = graph.traversal();
                Date now = new Date();
                Vertex controlVertex = g.addV("Control").
                        property("creationDate", now).
                        property("lastOpenDate", now).
                        property("metadataCollectionId", metadataCollectionId).
                        next();

                // Ensure graph schema is up to date
                log.info("Updating graph schema, if necessary");
                GraphOMRSGraphFactory.initialize(graph);
                // Update the lastOpenDate
                now = new Date();
                controlVertex.property("lastOpenDate", now);
                g.tx().commit();

            }
            catch (Exception e) {
                log.error("Graph initialization failed, exception {}",e.getMessage());
                g.tx().rollback();
                return null;
            }

            GraphOMRSAuditCode  auditCode = GraphOMRSAuditCode.GRAPH_REPOSITORY_CREATED;
            String actionDescription = "openGraphRepository";
            thisAuditLog.logRecord(
                    actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

        }
        else {

            // Control vertex was found...so graph is not new

            // Check metadataCollectionId matches and fail if not; make audit entry

            // Display control vertex contents, update last open date; make audit entry

            Vertex controlVertex = g.V().hasLabel("Control").next();

            String creationDateString = null;
            String metadataCollectionIdString = null;
            String lastOpenDateString = null;

            VertexProperty creationDateProperty = controlVertex.property("creationDate");
            if (creationDateProperty != null && creationDateProperty.isPresent()) {
                creationDateString = creationDateProperty.value().toString();
            }

            VertexProperty metadataCollectionIdProperty = controlVertex.property("metadataCollectionId");
            if (metadataCollectionIdProperty != null && metadataCollectionIdProperty.isPresent()) {
                metadataCollectionIdString = (String) metadataCollectionIdProperty.value();
            }

            VertexProperty lastOpenDateProperty = controlVertex.property("lastOpenDate");
            if (lastOpenDateProperty != null && lastOpenDateProperty.isPresent()) {
                lastOpenDateString = lastOpenDateProperty.value().toString();
            }


            if (metadataCollectionIdString != null && !metadataCollectionIdString.equals(metadataCollectionId)) {

                // Graph database does not have matching metadataCollectionId - abort!

                log.error("{} The graph database for repository {} has metadataCollectionId {}, and cannot be opened using metadataCollectionId {} ",
                        methodName, repositoryName, metadataCollectionIdString, metadataCollectionId);

                g.tx().rollback();

                GraphOMRSAuditCode auditCode = GraphOMRSAuditCode.GRAPH_REPOSITORY_HAS_DIFFERENT_METADATA_COLLECTION_ID;
                String actionDescription = "openGraphRepository";
                thisAuditLog.logRecord(
                        actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());


                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.GRAPH_DB_HAS_DIFFERENT_METADATACOLLECTION_ID;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(storagePath, methodName,
                        GraphOMRSGraphFactory.class.getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        GraphOMRSGraphFactory.class.getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());

            } else {

                // Graph database has matching metadataCollectionId - ok to proceed

                log.info("Opened graph repository: graph created at {} by metadataCollectionId {}, last opened at {}",
                        creationDateString, metadataCollectionIdString, lastOpenDateString);

                // Ensure graph schema is up to date
                log.info("Ensuring graph schema is up to date");
                try {
                    GraphOMRSGraphFactory.initialize(graph);
                    // Update the lastOpenDate
                    Date now = new Date();
                    controlVertex.property("lastOpenDate", now);
                    g.tx().commit();
                }
                catch (Exception e) {
                    log.error("Graph initialization failed, exception {}",e.getMessage());
                    g.tx().rollback();
                    return null;
                }

                GraphOMRSAuditCode auditCode = GraphOMRSAuditCode.GRAPH_REPOSITORY_OPENED;
                String actionDescription = "openGraphRepository";
                thisAuditLog.logRecord(
                        actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());

            }
        }

        return graph;
    }



    // This method is idempotent.
    private static void initialize(JanusGraph graph)
        throws
            RepositoryErrorException
    {

        final String methodName = "initialize";

        // The graph contains a vertex per entity or classification and an edge per relationship or classifier (i.e. an entity-classification edge)

        try {


            /*
             * Create labels for vertex and edge uses
             */

            JanusGraphManagement management = graph.openManagement();
            // Each vertex has a label that reflects the TypeDefCategory - i.e. Entity or Classification
            if (management.getVertexLabel("Control") == null)
                management.makeVertexLabel("Control").make();
            if (management.getVertexLabel("Entity") == null)
                management.makeVertexLabel("Entity").make();
            if (management.getVertexLabel("Classification") == null)
                management.makeVertexLabel("Classification").make();
            // Each edge has a label that reflects the TypeDefCategory - i.e. Relationship
            if (management.getEdgeLabel("Relationship") == null)
                management.makeEdgeLabel("Relationship").make();
            if (management.getEdgeLabel("Classifier") == null)
                management.makeEdgeLabel("Classifier").make();
            management.commit();

            /*
             * There is no reindexing of newly created indexes, either here or in the creation of an index as the result of verifying a TypeDef. The
             * rationale for this is that each property is qualified by the name of the type in which it is defined - the indexes on those properties
             * are therefore contingent on type that defines the property being indexed. A subsequent type that inherits from an already indexed type
             * uses the same qualified property names for its inherited properties - and hence uses the index that already exists. For properties (and
             * their indexes) that are defined on the immediate type (rather than being inherited), there cannot be any instances of the immediate type
             * prior to the type being defined (so there should be no need to rescan the graph). Hence no reindexing is performed. On construction of each
             * index the code does wait for it to reach ENABLED state.
             */


            /*
             *   Entity core property indexes
             */

            // An Entity vertex has the following properties and indexes:
            // guid                                -   composite   - a vertex guid (string) is guaranteed unique and will always be queried using exact match
            // typeName                            -   composite
            // createdBy                           -   mixed (String)
            // updatedBy                           -   mixed (String)
            // createTime                          -   none - mixed may be useful when matchProperties allows date predicate (e.g. greaterThan/since)
            // updateTime                          -   none - mixed may be useful when matchProperties allows date predicate (e.g. greaterThan/since)
            // maintainedBy                        -   mixed (Text)
            // instanceProvenanceType (ordinal)    -   none
            // instanceLicense                     -   mixed (String)
            // metadataCollectionId                -   none
            // metadataCollectionName              -   none
            // long version                        -   none
            // InstanceStatus currentStatus        -   none
            // InstanceStatus statusOnDelete       -   none
            // String instanceURL                  -   mixed (String)
            // InstanceProperties entityProperties -   mixed on each primitive property


            // On all index creation methods pass the property name (which is common to entities, relationships and classifications) so that the 
            // index creator can find the type, and the specific key by which this property is known.

            createCompositeIndexForVertexProperty(PROPERTY_NAME_GUID,                    PROPERTY_KEY_ENTITY_GUID,      true);
            createCompositeIndexForVertexProperty(PROPERTY_NAME_TYPE_NAME,               PROPERTY_KEY_ENTITY_TYPE_NAME, false);

            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_CREATED_BY,              PROPERTY_KEY_ENTITY_CREATED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_UPDATED_BY,              PROPERTY_KEY_ENTITY_UPDATED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_MAINTAINED_BY,           PROPERTY_KEY_ENTITY_MAINTAINED_BY);              // maintainedBy is a serialized list so use Text mapping
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_METADATACOLLECTION_NAME, PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_INSTANCE_URL,            PROPERTY_KEY_ENTITY_INSTANCE_URL);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_INSTANCE_LICENSE,        PROPERTY_KEY_ENTITY_INSTANCE_LICENSE);


            /*
             *  Relationship core property indexes
             */

            // A Relationship edge has the following properties and indexes:
            // guid                                -   composite
            // typeName                            -   composite
            // createdBy                           -   mixed (default)
            // updatedBy                           -   mixed (default)
            // createTime                          -   none - mixed may be useful when matchProperties allows date predicate (e.g. greaterThan/since)
            // updateTime                          -   none - mixed may be useful when matchProperties allows date predicate (e.g. greaterThan/since)
            // maintainedBy                        -   none
            // instanceProvenanceType (ordinal)    -   none
            // instanceLicense                     -   none
            // metadataCollectionId                -   none
            // metadataCollectionName              -   none
            // long version                        -   none
            // InstanceStatus currentStatus        -   none
            // InstanceStatus statusOnDelete       -   none
            // String instanceURL                  -   mixed (default)
            // InstanceProperties entityProperties -   mixed on each primitive property


            createCompositeIndexForEdgeProperty(PROPERTY_NAME_GUID,                    PROPERTY_KEY_RELATIONSHIP_GUID);
            createCompositeIndexForEdgeProperty(PROPERTY_NAME_TYPE_NAME,               PROPERTY_KEY_RELATIONSHIP_TYPE_NAME);

            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_CREATED_BY,              PROPERTY_KEY_RELATIONSHIP_CREATED_BY);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_UPDATED_BY,              PROPERTY_KEY_RELATIONSHIP_UPDATED_BY);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_MAINTAINED_BY,           PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_METADATACOLLECTION_NAME, PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_INSTANCE_URL,            PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_INSTANCE_LICENSE,        PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE);


            /*
             *  Classification core property indexes
             */

            // A Classification vertex has the following properties and indexes, which are similar to Entity above
            // except that a classification instance does not have a GUID, but it does have a classification name
            // which uses a mixed index because it is not unique:

            createCompositeIndexForVertexProperty(PROPERTY_NAME_TYPE_NAME,                PROPERTY_KEY_CLASSIFICATION_TYPE_NAME, false);

            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_CLASSIFICATION_NAME,      PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_CREATED_BY,               PROPERTY_KEY_CLASSIFICATION_CREATED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_UPDATED_BY,               PROPERTY_KEY_CLASSIFICATION_UPDATED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_MAINTAINED_BY,            PROPERTY_KEY_CLASSIFICATION_MAINTAINED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_METADATACOLLECTION_NAME,  PROPERTY_KEY_CLASSIFICATION_METADATACOLLECTION_NAME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_INSTANCE_LICENSE,         PROPERTY_KEY_CLASSIFICATION_INSTANCE_LICENSE);


        }
        catch (Exception e) {

            log.error("{} Caught exception during graph initialize operation", methodName);
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(thisRepositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    "GraphOMRSGraphFactory",
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

    }



    // Note that this map is not a complete list of the graph indexes. It only contains mappings for the MIXED indexes.

    public static final Map<String,MixedIndexMapping> corePropertyMixedIndexMappings = new HashMap<String,MixedIndexMapping>() {{

        put(PROPERTY_KEY_ENTITY_CREATED_BY,                        MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_UPDATED_BY,                        MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_MAINTAINED_BY,                     MixedIndexMapping.Text  );    // maintainedBy is stored as a serialized list so uses Text mapping
        put(PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME,           MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_INSTANCE_URL,                      MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_INSTANCE_LICENSE,                  MixedIndexMapping.String);

        put(PROPERTY_KEY_RELATIONSHIP_CREATED_BY,                  MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY,                  MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY,               MixedIndexMapping.Text  );    // maintainedBy is stored as a serialized list so uses Text mapping
        put(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME,     MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL,                MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE,            MixedIndexMapping.String);

        put(PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME,       MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_CREATED_BY,                MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_UPDATED_BY,                MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_MAINTAINED_BY,             MixedIndexMapping.Text  );    // maintainedBy is stored as a serialized list so uses Text mapping
        put(PROPERTY_KEY_CLASSIFICATION_METADATACOLLECTION_NAME,   MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_INSTANCE_LICENSE,          MixedIndexMapping.String);


    }};



    public static void createMixedIndexForVertexCoreProperty(String propName, String propKeyName)
    {
        String className = corePropertyTypes.get(propName);
        MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(propKeyName);
        createMixedIndexForVertexProperty(propName, propKeyName, className, mapping);
    }

    public static void createMixedIndexForVertexProperty(String propName, String propKeyName, String className, MixedIndexMapping mapping) {

        final String methodName = "createMixedIndexForVertexProperty";

        Class clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            log.error("{} no index created for property {}; class {} not found", methodName, propName, className);
            return;
        }


        final String indexName  = "vertexIndexProperty" + propKeyName;

        JanusGraphManagement management = graph.openManagement();
        JanusGraphIndex index;

        try {
            // Check if index exists
            JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
            if (existingIndex != null) {
                log.info("{} index {} already exists", methodName, indexName);
                management.rollback();
                return;
            } else {
                // index does not already exist - create
                log.info("{} index create {} for vertex property {}", methodName, indexName, propKeyName);
            }

            PropertyKey propertyKey;
            boolean oldKey = false;
            PropertyKey existingPropertyKey = management.getPropertyKey(propKeyName);
            if (existingPropertyKey != null) {
                log.debug("{} property key already exists for property {}", methodName, propKeyName);
                propertyKey = existingPropertyKey;
                oldKey = true;
            } else {
                log.debug("{} make property key for property {}", methodName, propKeyName);
                propertyKey = management.makePropertyKey(propKeyName).dataType(clazz).make();
            }

            log.debug("{} try to build index {}", methodName, indexName);
            // To avoid values being tokenized by treating non-alphanumeric characters as delimiters, avoid the default (Text) mapping and explicitly use String instead.
            JanusGraphManagement.IndexBuilder vertexIndexBuilder = management.buildIndex(indexName, Vertex.class);
            if (mapping == MixedIndexMapping.Text || mapping == MixedIndexMapping.Default)
                    vertexIndexBuilder.addKey(propertyKey);                              // allow default - implicitly Text mapping
            else
                vertexIndexBuilder.addKey(propertyKey, Mapping.STRING.asParameter() );   // override default - explicitly String mapping
            vertexIndexBuilder.buildMixedIndex("search");
            management.commit();
            log.debug("{} index created {}", methodName, indexName);

            // If this index is for a core property that needs a mixed index and we are reusing a key created in an earlier management transaction
            // then we need to reindex

            if (oldKey) {

                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();

                management = graph.openManagement();
                index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);  // no need to get the future - await ENABLED below...
                management.commit();

            }

            // Enable the index
            log.debug("{} awaitGraphIndexStatus ENABLED for {}", methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
            log.debug("{} index {} ready", methodName, indexName);

        }
        catch (Exception e) {

            log.error("{} !!! Caught exception from index construction for property name {}, key {}, exception {}", methodName, propName, propKeyName, e);
            management.rollback();
        }

    }

    private static void createCompositeIndexForVertexProperty(String propertyName, String propertyKeyName, boolean unique)
    {

        final String methodName = "createCompositeIndexForVertexProperty";

        String className = corePropertyTypes.get(propertyName);

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("{} class not found for property {}", methodName, propertyName);
            log.error("{} NO INDEX CREATED for property {}", methodName, propertyName);
            return;
        }

        String indexName = "vertexIndexComposite" + propertyKeyName;
        log.info("INDEX CREATE {}", indexName);

        JanusGraphManagement management = graph.openManagement();

        try {

            // Check if index exists
            JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
            if (existingIndex != null) {
                log.info("{} index {} already exists", methodName, indexName);
                management.rollback();
                return;
            } else {
                // index does not already exist - create
                log.info("{} index create {} for vertex property {}", methodName, indexName, propertyKeyName);
            }


            // Check whether property key exists (e.g. edge also has a "guid" property) and if not create it...
            PropertyKey propertyKey;
            boolean oldKey = false;
            PropertyKey existingPropertyKey = management.getPropertyKey(propertyKeyName);
            if (existingPropertyKey != null) {
                log.debug("{} property key already exists for property {}", methodName, propertyKeyName);
                propertyKey = existingPropertyKey;
                oldKey = true;
            } else {
                log.debug("{} make property key for property {}", methodName, propertyKeyName);
                propertyKey = management.makePropertyKey(propertyKeyName).dataType(clazz).make();
            }

            JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Vertex.class).addKey(propertyKey);
            if (unique) {
                indexBuilder.unique();
            }
            JanusGraphIndex index = indexBuilder.buildCompositeIndex();
            if (unique) {
                management.setConsistency(index, ConsistencyModifier.LOCK);
            }
            management.commit();

            if (oldKey) {

                // If we are reusing a key created in an earlier management transaction - e.g. "guid" - we need to reindex
                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();
                management = graph.openManagement();
                index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);  // no need to get the future - await ENABLED below...
                management.commit();

            }

            // Enable the index - set a relatively short timeout (10 s vs the default of 1 minute) because the property key may be shared
            // (e.g. vertex "guid" vs edge "guid" but we know the index is for vertices and there are no property key name clashes within vertices.
            log.debug("{} awaitGraphIndexStatus ENABLED for {}", methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
        } catch (Exception e) {
            log.error("{} caught interrupted exception from awaitGraphIndexStatus ENABLED {}", methodName, e);
            management.rollback();
        }

    }


    public static void createMixedIndexForEdgeCoreProperty(String propName, String propKeyName) {
        String className = corePropertyTypes.get(propName);
        MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(propKeyName);
        createMixedIndexForEdgeProperty(propName, propKeyName, className, mapping);
    }

    public static void createMixedIndexForEdgeProperty(String propName, String propKeyName, String className, MixedIndexMapping mapping) {

        final String methodName = "createMixedIndexForEdgeProperty";

        Class clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            log.error("{} class not found for property {} with class {}", methodName, propName, className);
            log.error("{} NO INDEX CREATED for property {} type {}", methodName, propName);
            return;
        }


        final String indexName  = "edgeIndexProperty" + propKeyName;


        JanusGraphManagement management = graph.openManagement();
        JanusGraphIndex index;

        try {
            // Check if index exists
            JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
            if (existingIndex != null) {
                log.info("{} index {} already exists", methodName, indexName);
                management.rollback();
                return;
            } else {
                // index does not already exist - create
                log.info("{} INDEX CREATE {} for edge property {}", methodName, indexName, propKeyName);

            }

            // Check whether property key exists (e.g. edge also has a "guid" property) and if not create it...
            PropertyKey propertyKey;
            boolean oldKey = false;
            PropertyKey existingPropertyKey = management.getPropertyKey(propKeyName);
            if (existingPropertyKey != null) {
                log.debug("{} property key already exists ", methodName);
                propertyKey = existingPropertyKey;
                oldKey = true;
            } else {
                log.debug("{} make property key for property {}", methodName, propKeyName);
                propertyKey = management.makePropertyKey(propKeyName).dataType(clazz).make();
            }

            log.debug("{} create index {}", methodName, indexName);
            // To avoid values being tokenized by treating non-alphanumeric characters as delimiters, avoid the default (Text) mapping and explicitly use String instead.
            JanusGraphManagement.IndexBuilder edgeIndexBuilder = management.buildIndex(indexName, Edge.class);
            if (mapping == MixedIndexMapping.Text)

                edgeIndexBuilder.addKey(propertyKey);                                    // default - implicitly Text mapping

            else if (mapping == MixedIndexMapping.Default)

                edgeIndexBuilder.addKey(propertyKey, Mapping.DEFAULT.asParameter());     // allow default mapping

            else

                edgeIndexBuilder.addKey(propertyKey, Mapping.STRING.asParameter() );     // override default - explicitly String mapping

            edgeIndexBuilder.buildMixedIndex("search");
            management.commit();
            log.debug("{} index created {}", methodName, indexName);

            // If this index is for a core property that needs a mixed index and we are reusing a key created in an earlier management transaction
            // then we need to reindex

            if (oldKey) {

                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();

                management = graph.openManagement();
                index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);  // no need to get the future - await ENABLED below...
                management.commit();

            }

            // Enable the index
            log.debug("{} awaitGraphIndexStatus ENABLED for {}", methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
            log.debug("{} index {} ready", methodName, indexName);

        }
        catch (Exception e) {

            log.error("{} !!! Caught exception from index construction for property name {}, property key {}, exception {}", methodName, propName, propKeyName, e);
            management.rollback();
        }


    }

    private static void createCompositeIndexForEdgeProperty(String propertyName, String propertyKeyName) {

        final String methodName = "createCompositeIndexForEdgeProperty";

        String className = corePropertyTypes.get(propertyName);

        Class clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            log.error("{} class not found for property {}", methodName, propertyName);
            log.error("{} NO INDEX CREATED for property {}", methodName, propertyName);
            return;
        }

        String indexName = "edgeIndexComposite" + propertyKeyName;
        log.info("INDEX CREATE {}", indexName);

        JanusGraphManagement management = graph.openManagement();

        try {

            // Check if index exists
            JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
            if (existingIndex != null) {
                log.info("{} index {} already exists for property {}", methodName, indexName, propertyKeyName);
                management.rollback();
                return;
            } else {
                // index does not already exist - create
                log.info("{} INDEX CREATE {} for vertex property {}", methodName, indexName, propertyKeyName);
            }


            // Check whether property key exists (e.g. edge also has a "guid" property) and if not create it...
            PropertyKey propertyKey;
            boolean oldKey = false;
            PropertyKey existingPropertyKey = management.getPropertyKey(propertyKeyName);
            if (existingPropertyKey != null) {
                log.debug("{} property key already exists for property {}", methodName, propertyKeyName);
                propertyKey = existingPropertyKey;
                oldKey = true;
            } else {
                log.debug("{} make property key for property {}", methodName, propertyKeyName);
                propertyKey = management.makePropertyKey(propertyKeyName).dataType(clazz).make();
            }

            JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Edge.class).addKey(propertyKey);
            JanusGraphIndex index = indexBuilder.buildCompositeIndex();
            management.commit();

            // If we are reusing a key creating in an earlier management transaction - e.g. "guid" - we need to reindex

            if (oldKey) {

                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();

                management = graph.openManagement();
                index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);  // no need to get the future - await ENABLED below...
                management.commit();
            }

            // Enable the index - set a relatively short timeout (10 s vs the default of 1 minute) because the property key may be shared
            // (e.g. vertex "guid" vs edge "guid" but we know the index is for edges and there are no property key name clashes within edges.
            log.debug("{} awaitGraphIndexStatus ENABLED for {}", methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
        }
        catch (Exception e) {
            log.error("{} caught interrupted exception from awaitGraphIndexStatus ENABLED {}", methodName, e);
            management.rollback();
        }

    }
}
