/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.apache.commons.collections4.MapUtils;
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
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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

    private JanusGraph   graph;
    private String       thisRepositoryName;
    private String       thisMetadataCollectionId;
    private AuditLog     thisAuditLog                 = null;
    private final String controlVertexIdPropertyName  = "ControlVertexIdentifier";


    /*
     * Default CTOR
     */
    public GraphOMRSGraphFactory() {

    }


    public enum MixedIndexMapping {
        Default,
        Text,
        String,
        Date
    }

    public JanusGraph open(String              metadataCollectionId,
                           String              repositoryName,
                           AuditLog            auditLog,
                           Map<String, Object> storageProperties)
            throws
            RepositoryErrorException
    {

        final String methodName = "open";
        String controlVertexIdPropertyValue = "ControlVertexIdentifier";

        thisMetadataCollectionId = metadataCollectionId;
        thisRepositoryName       = repositoryName;
        thisAuditLog             = auditLog;

        // Open method is called from within synchronized block in graph repository metadata store class.

        // Use the JGF.Builder and construct the configuration in-line.
        // There is no synch yet on this.

        // Run with a Lucene indexing backend for now - if you pull in ES you need to use JG-server
        // or start your own ES cluster. If/when you pull the janusgraph-es module into the build
        // you will need to configure the component-scan otherwise Spring boot tries to autoconfigure a
        // REST client which fails (on HttpHost).

        if (MapUtils.isEmpty(storageProperties)) {
            storageProperties = getBerkleyStorageProperties();
        }
        JanusGraphFactory.Builder build = JanusGraphFactory.build();
        storageProperties.forEach(build::set);

        try {

            graph = build.open();

        } catch (Exception e) {
            log.error("{} could not open graph", methodName);

            throw new RepositoryErrorException(GraphOMRSErrorCode.CANNOT_OPEN_GRAPH_DB.getMessageDefinition(methodName,
                                                                                                            GraphOMRSGraphFactory.class.getName(),
                                                                                                            repositoryName),
                    GraphOMRSGraphFactory.class.getName(),
                    methodName, e);
        }


        /*
         *  The graph has been opened - it may be new or it may have already existed.
         *
         *  The following start logic is performed:
         *
         *    Look for the control vertex:
         *    If no control vertex exists then the graph is new:
         *        add a control index and a control vertex stamping into it the mdcId and creation/open date.
         *        generate an audit log entry recording creation.
         *    If the control vertex does exist the graph had already been created:
         *        check the control vertex mdcId matches the configured mdcId of this repository
         *        if they do not match:
         *            STOP - generate audit log entry recording creation. The user will need to consciously adopt the graph database.
         *        if they do match:
         *            record the open time in the control vertex
         *            generate an audit log entry to record the open.
         */


        GraphTraversalSource g = graph.traversal();
        boolean success = true;

        // Look for control vertex
        Vertex controlVertex;

        Iterator<Vertex> vi = g.V().hasLabel("Control").has(controlVertexIdPropertyName, controlVertexIdPropertyValue);
        if (!vi.hasNext()) {
            controlVertex = null;
        }
        else {
            controlVertex = vi.next();
            if (controlVertex == null) {
                // Belt and braces protection...
                log.error("Graph initialization failed because valid controlVertex could not be located");
                success = false;

            }
        }

        // Complete the current transaction and give up now if failed already
        if (success) {
            g.tx().commit();
        }
        else {
            g.tx().rollback();
            graph = null;
            return graph;
        }



        // Method has been successful to this point - but controlVertex may be null (new) or non-null (existing)

        // Get a new traversal source - initiating a new transaction. The control vertex will be
        // created and / or updated under this transaction.
        g = graph.traversal();

        if (controlVertex == null) {

            // Graph is new - create control index then create control vertex

            // Create control index
            success = createControlIndex();

            if (success) {

                // Create control vertex
                try {
                    // Add a control vertex, initialize the schema and update the control vertex...

                    Date now = new Date();
                    controlVertex = g.addV("Control").
                            property(controlVertexIdPropertyName, controlVertexIdPropertyValue).
                            property("creationDate", now).
                            property("lastOpenDate", now).
                            property("metadataCollectionId", metadataCollectionId).
                            next();

                    // Update the lastOpenDate
                    now = new Date();
                    controlVertex.property("lastOpenDate", now);
                    g.tx().commit();
                    success = true;

                } catch (Exception e) {
                    log.error("{} Creation of control vertex failed, exception {}", methodName, e.getMessage());
                    g.tx().rollback();
                    throw e;
                }

                final String actionDescription = "openGraphRepository";
                thisAuditLog.logMessage(actionDescription, GraphOMRSAuditCode.GRAPH_REPOSITORY_CREATED.getMessageDefinition());
            }

        }


        else {  // controlVertex != null

            // Graph is pre-existing - check and update control vertex

            try {
                success = checkAndUpdateControlInformation(controlVertex);
                g.tx().commit();
            }
            catch (RepositoryErrorException e) {
                log.error("{} Check and update of control vertex failed, exception {}", methodName, e.getMessage());
                // rollback and re-throw
                g.tx().rollback();
                throw e;
            }
        }


        if (success) {
            // Whether graph was new or existed, ensure the graph schema is up to date
            try {
                log.info("Updating graph schema, if necessary");
                initialize(graph);
            }
            catch (RepositoryErrorException e) {
                // rollback and re-throw
                g.tx().rollback();
                throw e;
            }
        }


        // Re-check whether method succeeded and complete open transaction
        if (success) {
            g.tx().commit();
        }
        else {

            // A failure occurred
            g.tx().rollback();
            graph = null;
        }

        return graph;
    }

    private Map<String, Object> getBerkleyStorageProperties() {

        // In current usage, the repository name is the server name, so ok to use for storage path
        Map<String, Object> berkleyStorageProperties = new HashMap<>();
        berkleyStorageProperties.put("storage.backend", "berkeleyje");
        berkleyStorageProperties.put("storage.directory", "./data/servers/" + thisRepositoryName + "/repository/graph/berkeley");
        berkleyStorageProperties.put("index.search.backend", "lucene");
        berkleyStorageProperties.put("index.search.directory", "./data/servers/" + thisRepositoryName + "/repository/graph/searchindex");
        return berkleyStorageProperties;
    }


    // This method is idempotent.
    private void initialize(JanusGraph graph)
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
            // createTime                          -   mixed (Date)
            // updateTime                          -   mixed (Date)
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
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_CREATE_TIME,             PROPERTY_KEY_ENTITY_CREATE_TIME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_UPDATE_TIME,             PROPERTY_KEY_ENTITY_UPDATE_TIME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_MAINTAINED_BY,           PROPERTY_KEY_ENTITY_MAINTAINED_BY);              // maintainedBy is a serialized list so use Text mapping
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_METADATACOLLECTION_NAME, PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_INSTANCE_URL,            PROPERTY_KEY_ENTITY_INSTANCE_URL);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_INSTANCE_LICENSE,        PROPERTY_KEY_ENTITY_INSTANCE_LICENSE);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_REPLICATED_BY,           PROPERTY_KEY_ENTITY_REPLICATED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_MAPPING_PROPERTIES,      PROPERTY_KEY_ENTITY_MAPPING_PROPERTIES);         // mappingProperties is a serialized map of String,Serializable so use Text mapping
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_REIDENTIFIED_FROM_GUID,  PROPERTY_KEY_ENTITY_REIDENTIFIED_FROM_GUID);

            /*
             *  Relationship core property indexes
             */

            // A Relationship edge has the following properties and indexes. Edge indexes cannot be unique:
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
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_CREATE_TIME,             PROPERTY_KEY_RELATIONSHIP_CREATE_TIME);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_UPDATE_TIME,             PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_MAINTAINED_BY,           PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_METADATACOLLECTION_NAME, PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_INSTANCE_URL,            PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_INSTANCE_LICENSE,        PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_REPLICATED_BY,           PROPERTY_KEY_RELATIONSHIP_REPLICATED_BY);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_MAPPING_PROPERTIES,      PROPERTY_KEY_RELATIONSHIP_MAPPING_PROPERTIES);
            createMixedIndexForEdgeCoreProperty(PROPERTY_NAME_REIDENTIFIED_FROM_GUID, PROPERTY_KEY_RELATIONSHIP_REIDENTIFIED_FROM_GUID);

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
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_CREATE_TIME,              PROPERTY_KEY_CLASSIFICATION_CREATE_TIME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_UPDATE_TIME,              PROPERTY_KEY_CLASSIFICATION_UPDATE_TIME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_MAINTAINED_BY,            PROPERTY_KEY_CLASSIFICATION_MAINTAINED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_METADATACOLLECTION_NAME,  PROPERTY_KEY_CLASSIFICATION_METADATACOLLECTION_NAME);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_INSTANCE_LICENSE,         PROPERTY_KEY_CLASSIFICATION_INSTANCE_LICENSE);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_REPLICATED_BY,            PROPERTY_KEY_CLASSIFICATION_REPLICATED_BY);
            createMixedIndexForVertexCoreProperty(PROPERTY_NAME_MAPPING_PROPERTIES,       PROPERTY_KEY_CLASSIFICATION_MAPPING_PROPERTIES);

        }
        catch (Exception e) {

            log.error("{} Caught exception during graph initialize operation", methodName);

            throw new RepositoryErrorException(GraphOMRSErrorCode.GRAPH_INITIALIZATION_ERROR.getMessageDefinition(thisRepositoryName),
                    "GraphOMRSGraphFactory",
                    methodName);
        }

    }



    // Note that this map is not a complete list of the graph indexes. It only contains mappings for the MIXED indexes.

    static final Map<String,MixedIndexMapping> corePropertyMixedIndexMappings = new HashMap<String,MixedIndexMapping>() {{

        put(PROPERTY_KEY_ENTITY_CREATED_BY,                        MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_UPDATED_BY,                        MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_CREATE_TIME,                       MixedIndexMapping.Date  );
        put(PROPERTY_KEY_ENTITY_UPDATE_TIME,                       MixedIndexMapping.Date  );
        put(PROPERTY_KEY_ENTITY_MAINTAINED_BY,                     MixedIndexMapping.Text  );    // maintainedBy is stored as a serialized list so uses Text mapping
        put(PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME,           MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_INSTANCE_URL,                      MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_INSTANCE_LICENSE,                  MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_REPLICATED_BY,                     MixedIndexMapping.String);
        put(PROPERTY_KEY_ENTITY_MAPPING_PROPERTIES,                MixedIndexMapping.Text  );    // mappingProperties is stored as a serialized map of String,Serializable so uses Text mapping
        put(PROPERTY_KEY_ENTITY_REIDENTIFIED_FROM_GUID,            MixedIndexMapping.String);

        put(PROPERTY_KEY_RELATIONSHIP_CREATED_BY,                  MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY,                  MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_CREATE_TIME,                 MixedIndexMapping.Date  );
        put(PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME,                 MixedIndexMapping.Date  );
        put(PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY,               MixedIndexMapping.Text  );    // maintainedBy is stored as a serialized list so uses Text mapping
        put(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME,     MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL,                MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE,            MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_REPLICATED_BY,               MixedIndexMapping.String);
        put(PROPERTY_KEY_RELATIONSHIP_MAPPING_PROPERTIES,          MixedIndexMapping.Text  );    // mappingProperties is stored as a serialized map of String,Serializable so uses Text mapping
        put(PROPERTY_KEY_RELATIONSHIP_REIDENTIFIED_FROM_GUID,      MixedIndexMapping.String);

        put(PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME,       MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_CREATED_BY,                MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_UPDATED_BY,                MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_CREATE_TIME,               MixedIndexMapping.Date  );
        put(PROPERTY_KEY_CLASSIFICATION_UPDATE_TIME,               MixedIndexMapping.Date  );
        put(PROPERTY_KEY_CLASSIFICATION_MAINTAINED_BY,             MixedIndexMapping.Text  );    // maintainedBy is stored as a serialized list so uses Text mapping
        put(PROPERTY_KEY_CLASSIFICATION_METADATACOLLECTION_NAME,   MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_INSTANCE_LICENSE,          MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_REPLICATED_BY,             MixedIndexMapping.String);
        put(PROPERTY_KEY_CLASSIFICATION_MAPPING_PROPERTIES,        MixedIndexMapping.Text  );    // mappingProperties is stored as a serialized map of String,Serializable so uses Text mapping

    }};



    private void createMixedIndexForVertexCoreProperty(String propName, String propKeyName)
    {
        String className = corePropertyTypes.get(propName);
        MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(propKeyName);
        createMixedIndexForVertexProperty(propName, propKeyName, className, mapping);
    }

    void createMixedIndexForVertexProperty(String propName, String propKeyName, String className, MixedIndexMapping mapping) {

        final String methodName = "createMixedIndexForVertexProperty";

        Class<?> clazz;
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
            if (mapping == MixedIndexMapping.Text || mapping == MixedIndexMapping.Default || mapping == MixedIndexMapping.Date )
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

    private void createCompositeIndexForVertexProperty(String propertyName, String propertyKeyName, boolean unique)
    {

        final String methodName = "createCompositeIndexForVertexProperty";

        String className = corePropertyTypes.get(propertyName);

        Class<?> clazz;
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


    private void createMixedIndexForEdgeCoreProperty(String propName, String propKeyName) {
        String className = corePropertyTypes.get(propName);
        MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(propKeyName);
        createMixedIndexForEdgeProperty(propName, propKeyName, className, mapping);
    }

     void createMixedIndexForEdgeProperty(String propName, String propKeyName, String className, MixedIndexMapping mapping) {

        final String methodName = "createMixedIndexForEdgeProperty";

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            log.error("{} class not found for property {} with class {}", methodName, propName, className);
            log.error("{} NO INDEX CREATED for property {}", methodName, propName);
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

            else if (mapping == MixedIndexMapping.Default || mapping == MixedIndexMapping.Date)

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

    private void createCompositeIndexForEdgeProperty(String propertyName, String propertyKeyName) {

        final String methodName = "createCompositeIndexForEdgeProperty";

        String className = corePropertyTypes.get(propertyName);

        Class<?> clazz;
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


    private boolean createControlIndex() {

        final String methodName = "createControlIndex";


        // Prior to creating the control vertex - create an index to allow us to find it without a full scan
        JanusGraphManagement management = graph.openManagement();
        String indexName = "controlIndex";

        try {

            // Check if index exists
            JanusGraphIndex controlIndex = management.getGraphIndex(indexName);
            if (controlIndex == null) {
                log.info("{} index create {} for control vertex", methodName, indexName);
                // Property key should not already exist - but check it anyway.
                PropertyKey propertyKey = management.getPropertyKey(controlVertexIdPropertyName);

                if (propertyKey != null) {
                    // Somehow - despite this being a new graph - the property key already exists. Stop.
                    log.error("{} property key {} already exists", methodName, controlVertexIdPropertyName);
                    management.rollback();
                    return false;
                } else {
                    log.info("{} make property key {}", methodName, controlVertexIdPropertyName);
                    propertyKey = management.makePropertyKey(controlVertexIdPropertyName).dataType(String.class).make();
                }

                if (propertyKey == null) {
                    // Could not create property key. Stop.
                    log.error("{} property key {} could not be created", methodName, controlVertexIdPropertyName);
                    management.rollback();
                    return false;
                } else {
                    log.info("{} create index {}", methodName, indexName);
                    JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Vertex.class).addKey(propertyKey).unique();
                    JanusGraphIndex index = indexBuilder.buildCompositeIndex();
                    management.setConsistency(index, ConsistencyModifier.LOCK);
                    management.commit();
                    // Enable the index - set a relatively short timeout (10 s vs the default of 1 minute)
                    log.info("{} await ENABLED for {}", methodName, indexName);
                    ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
                    return true;
                }
            } else {
                // That really should not be possible - a new graph should not have the index. Stop.
                log.error("{} control index already exists", methodName);
                management.rollback();
                return false;
            }
        } catch (Exception e) {
            log.error("{} caught interrupted exception from awaitGraphIndexStatus ENABLED {}", methodName, e);
            management.rollback();
            return false;
        }

    }



    private boolean checkAndUpdateControlInformation(Vertex controlVertex)
        throws
        RepositoryErrorException
    {

        final String methodName = "checkAndUpdateControlInformation";

        boolean ret;

        // Check metadataCollectionId matches and fail if not; make audit entry

        // Display control vertex contents, update last open date; make audit entry

        //Vertex controlVertex = g.V().hasLabel("Control").next();

        String creationDateString = null;
        String metadataCollectionIdString = null;
        String lastOpenDateString = null;

        VertexProperty<?> creationDateProperty = controlVertex.property("creationDate");
        if (creationDateProperty != null && creationDateProperty.isPresent()) {
            creationDateString = creationDateProperty.value().toString();
        }

        VertexProperty<?> metadataCollectionIdProperty = controlVertex.property("metadataCollectionId");
        if (metadataCollectionIdProperty != null && metadataCollectionIdProperty.isPresent()) {
            metadataCollectionIdString = (String) metadataCollectionIdProperty.value();
        }

        VertexProperty<?> lastOpenDateProperty = controlVertex.property("lastOpenDate");
        if (lastOpenDateProperty != null && lastOpenDateProperty.isPresent()) {
            lastOpenDateString = lastOpenDateProperty.value().toString();
        }


        if (metadataCollectionIdString != null && !metadataCollectionIdString.equals(thisMetadataCollectionId)) {

            // Graph database does not have matching metadataCollectionId - abort!

            log.error("{} The graph database for repository {} has metadataCollectionId {}, and cannot be opened using metadataCollectionId {} ",
                    methodName, thisRepositoryName, metadataCollectionIdString, thisMetadataCollectionId);

            String actionDescription = "openGraphRepository";
            thisAuditLog.logMessage(
                    actionDescription,
                    GraphOMRSAuditCode.GRAPH_REPOSITORY_HAS_DIFFERENT_METADATA_COLLECTION_ID.getMessageDefinition(thisRepositoryName,
                                                                                                                  metadataCollectionIdString,
                                                                                                                  thisMetadataCollectionId));

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.GRAPH_DB_HAS_DIFFERENT_METADATACOLLECTION_ID.getMessageDefinition(metadataCollectionIdString,
                                                                                                         thisMetadataCollectionId),
                    GraphOMRSGraphFactory.class.getName(),
                    methodName);

        } else {

            // Graph database has matching metadataCollectionId - ok to proceed

            log.info("Opened graph repository: graph created at {} by metadataCollectionId {}, last opened at {}",
                    creationDateString, metadataCollectionIdString, lastOpenDateString);

            // Ensure graph schema is up to date
            log.info("Ensuring graph schema is up to date");
            try {
                initialize(graph);
                // Update the lastOpenDate
                Date now = new Date();
                controlVertex.property("lastOpenDate", now);
                ret = true;

            }
            catch (Exception e) {
                log.error("Graph initialization failed, exception {}", e.getMessage());
                ret = false;
            }

            String actionDescription = "openGraphRepository";
            thisAuditLog.logMessage(actionDescription, GraphOMRSAuditCode.GRAPH_REPOSITORY_OPENED.getMessageDefinition());
        }

        return ret;
    }
}
