/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.IndexProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.utils.Constants;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.utils.GraphConstants;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenLineageSchemaHelper<C extends Element> {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageSchemaHelper.class);

    private static final String AWAIT_GRAPH_INDEX_STATUS_ENABLED = "{} awaitGraphIndexStatus ENABLED for {}";
    private static final String CAUGHT_INTERRUPTED_EXCEPTION_MESSAGE = "caught interrupted exception from awaitGraphIndexStatus";
    private static final String CAUGHT_INTERRUPTED_EXCEPTION = "{} " + CAUGHT_INTERRUPTED_EXCEPTION_MESSAGE + " ENABLED {}";
    private static final String VERTEX = "Vertex";
    private static final String EDGE = "Edge";

    private static final String ADD_VERTEX_LABEL_IF_MISSING_FORMAT =
            "if (management.getVertexLabel(\"%s\") == null ) { management.makeVertexLabel(\"%s\").make(); }\n";
    private static final String ADD_EDGE_LABEL_IF_MISSING_FORMAT =
            "if (management.getEdgeLabel(\"%s\") == null ) { management.makeEdgeLabel(\"%s\").make(); }\n";


    AuditLog auditLog;


    /**
     * Set up the indexes for the Janus Graph instance
     *
     * @param janusGraph - Janus Graph instance
     */
    public void createSchemas(JanusGraph janusGraph) {
        JanusGraphManagement management = janusGraph.openManagement();
        Set<String> vertexLabels = schemaBasedOnGraphType(VertexLabelsLineageGraph.class);
        Set<String> relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsLineageGraph.class);
        checkAndAddLabelVertex(management, vertexLabels);
        checkAndAddLabelEdge(management, relationshipsLabels);
        management.commit();
    }


    /**
     * @param aEnum Enum with label names
     * @param <T>   Vertex or Edge gremlin type
     * @return Set of gremlin label names for enumerated label types
     */
    private <T extends Enum<T>> Set<String> schemaBasedOnGraphType(Class<T> aEnum) {
        return Stream.of(aEnum.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    /**
     * Set up the vertex labels of the schema for the Janus Graph instance
     *
     * @param labels     - set of labels
     * @param management - management instance of Janus Graph
     */
    private void checkAndAddLabelVertex(final JanusGraphManagement management, Set<String> labels) {
        for (String label : labels) {
            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }
    }

    /**
     * Set up the edge labels of the schema for the Janus Graph instance
     *
     * @param labels     - set of labels
     * @param management - management instance of Janus Graph
     */
    private void checkAndAddLabelEdge(final JanusGraphManagement management, Set<String> labels) {
        for (String label : labels) {
            if (management.getEdgeLabel(label) == null)
                management.makeEdgeLabel(label).make();
        }
    }


    /**
     * Set up the indexes for the Janus Graph instance
     *
     * @param janusGraph - Janus Graph instance
     */
    public void createIndexes(JanusGraph janusGraph) {
        createCompositeIndexForProperty(janusGraph, new IndexProperties(GraphConstants.PROPERTY_NAME_GUID, GraphConstants.PROPERTY_KEY_ENTITY_GUID, true, Vertex.class));
        createCompositeIndexForProperty(janusGraph, new IndexProperties(GraphConstants.PROPERTY_NAME_LABEL, GraphConstants.PROPERTY_KEY_LABEL, false, Vertex.class));
        createCompositeIndexForProperty(janusGraph, new IndexProperties(Constants.ASSET_LINEAGE_VARIABLES, Constants.ASSET_LINEAGE_VARIABLES, false, Vertex.class));
        createCompositeIndexForProperty(janusGraph, new IndexProperties(GraphConstants.PROPERTY_NAME_VERSION, GraphConstants.PROPERTY_KEY_ENTITY_VERSION, false, Vertex.class));
        createCompositeIndexForProperty(janusGraph, new IndexProperties(GraphConstants.PROPERTY_NAME_METADATA_ID, GraphConstants.PROPERTY_KEY_METADATA_ID, false, Vertex.class));
        createCompositeIndexForProperty(janusGraph, new IndexProperties(GraphConstants.PROPERTY_NAME_LABEL, GraphConstants.PROPERTY_KEY_RELATIONSHIP_LABEL, false, Edge.class));
        createCompositeIndexForProperty(janusGraph, new IndexProperties(GraphConstants.PROPERTY_NAME_GUID, GraphConstants.PROPERTY_KEY_RELATIONSHIP_GUID, false, Edge.class));
    }

    /**
     * Creates the indexes for the properties of vertices.
     *
     * @param graph           - graph instance to create the indexes
     * @param indexProperties - properties for creating the index
     */
    protected void createCompositeIndexForProperty(JanusGraph graph, IndexProperties indexProperties) {
        String indexName = null;
        if (Vertex.class.equals(indexProperties.getType())) {
            indexName = "vertexIndexComposite" + indexProperties.getPropertyKeyName();
        } else if (Edge.class.equals(indexProperties.getType())) {
            indexName = "edgeIndexComposite" + indexProperties.getPropertyKeyName();

        }
        log.debug("INDEX to be created {}", indexName);
        checkIndex(graph, indexName, indexProperties);
    }

    private void checkIndex(JanusGraph graph, String indexName, IndexProperties indexProperties) {
        JanusGraphManagement management = graph.openManagement();

        JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
        if (existingIndex != null) {
            log.debug("{} index already exists", indexName);
            if (auditLog != null) {
                auditLog.logMessage(indexName + " index already exists", JanusConnectorErrorCode.INDEX_ALREADY_EXISTS.getMessageDefinition());
            }
            management.rollback();
            return;
        }

        createIndex(graph, management, indexName, indexProperties);

    }

    private void createIndex(JanusGraph graph, JanusGraphManagement management, String indexName, IndexProperties indexProperties) {
        String className = GraphConstants.immutableCorePropertyTypes.get(indexProperties.getPropertyName());

        Class<C> clazz;
        try {
            clazz = (Class<C>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("class not found for property {}", indexProperties.getPropertyName());
            log.error("NO INDEX created for property {}", indexProperties.getPropertyName());
            auditLog.logMessage("class not found for property " + indexProperties.getPropertyName(),
                                JanusConnectorErrorCode.INDEX_NOT_CREATED.getMessageDefinition());
            return;
        }

        PropertyKey propertyKey;
        boolean oldKey = false;
        PropertyKey existingPropertyKey = management.getPropertyKey(indexProperties.getPropertyKeyName());
        if (existingPropertyKey != null) {
            log.debug("property key already exists for property {}", indexProperties.getPropertyKeyName());
            propertyKey = existingPropertyKey;
            oldKey = true;
        } else {
            log.debug("make property key for property {}", indexProperties.getPropertyKeyName());
            propertyKey = management.makePropertyKey(indexProperties.getPropertyKeyName()).dataType(clazz).make();
        }

        buildIndex(graph, management, indexName, propertyKey, oldKey, indexProperties);

    }

    private void buildIndex(JanusGraph graph, JanusGraphManagement management, String indexName, PropertyKey propertyKey, boolean oldKey,
                            IndexProperties indexProperties) {
        if (indexProperties.getType().equals(Vertex.class)) {
            buildVertexIndex(management, indexName, propertyKey, indexProperties);
        } else if (indexProperties.getType().equals(Edge.class)) {
            buildEdgeIndex(management, indexName, propertyKey);
        } else {
            management.rollback();
            return;
        }


        enableIndex(graph, management, indexName, oldKey);
    }

    private void buildVertexIndex(JanusGraphManagement management, String indexName, PropertyKey propertyKey, IndexProperties indexProperties) {
        JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, indexProperties.getType()).addKey(propertyKey);
        if (indexProperties.isUnique()) {
            indexBuilder.unique();
        }
        JanusGraphIndex index = indexBuilder.buildCompositeIndex();
        if (indexProperties.isUnique()) {
            management.setConsistency(index, ConsistencyModifier.LOCK);
        }
        management.commit();
    }

    private void buildEdgeIndex(JanusGraphManagement management, String indexName, PropertyKey propertyKey) {
        JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Edge.class).addKey(propertyKey);
        indexBuilder.buildCompositeIndex();
        management.commit();
    }

    private void enableIndex(JanusGraph graph, JanusGraphManagement management, String indexName, boolean oldKey) {
        final String methodName = "enableIndex";

        try {
            if (oldKey) {
                // If we are reusing a key created in an earlier management transaction - e.g. "guid" - we need to reindex
                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();
                management = graph.openManagement();
                JanusGraphIndex index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);
                management.commit();
            }

            // Enable the index - set a relatively short timeout (10 s vs the default of 1 minute) because the property key may be shared
            // (e.g. vertex "guid" vs edge "guid" but we know the index is for vertices and there are no property key name clashes within vertices.
            log.debug(AWAIT_GRAPH_INDEX_STATUS_ENABLED, methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
        } catch (InterruptedException e) {
            log.error(CAUGHT_INTERRUPTED_EXCEPTION, methodName, e);
            if (auditLog != null) {
                auditLog.logMessage(CAUGHT_INTERRUPTED_EXCEPTION_MESSAGE, JanusConnectorErrorCode.INDEX_NOT_ENABLED.getMessageDefinition());
            }
            management.rollback();
        }
    }

    /**
     * Create the labels for vertices and edges
     * @param client - instance of the client for remote cluster
     */
    public void createLabels(Client client) {
        String createLabels = createLabelsCommand();
        log.debug("Checking labels...");
        client.submit(createLabels);

    }

    /**
     * @return String representation of gremlin command to create vertex and edge labels using janus graph management API.
     */
    private String createLabelsCommand() {
        StringBuilder managementCommand = new StringBuilder();
        managementCommand.append("JanusGraphManagement management = graph.openManagement();\n");
        for (VertexLabelsLineageGraph vertexLabel : VertexLabelsLineageGraph.values()) {
            managementCommand.append(String.format(ADD_VERTEX_LABEL_IF_MISSING_FORMAT, vertexLabel, vertexLabel));
        }

        for (EdgeLabelsLineageGraph edgeLabel : EdgeLabelsLineageGraph.values()) {
            managementCommand.append(String.format(ADD_EDGE_LABEL_IF_MISSING_FORMAT, edgeLabel, edgeLabel));
        }
        managementCommand.append("management.commit();");
        return managementCommand.toString();
    }


    /**
     * Create index commands for the graph
     *
     * @param client - instance of the client for remote cluster
     */
    public void createIndexes(Client client) {
        String indexCommandGuid = createIndexCommand("vertexIndexCompositevertex--guid", GraphConstants.PROPERTY_KEY_ENTITY_GUID, true, VERTEX);
        String indexCommandLabel = createIndexCommand("vertexIndexCompositevertex--label", GraphConstants.PROPERTY_KEY_LABEL, false, VERTEX);
        String indexCommandVersion = createIndexCommand("vertexIndexCompositevertex--version", GraphConstants.PROPERTY_KEY_ENTITY_VERSION, false, VERTEX);
        String indexCommandAssetLineageVariables = createIndexCommand("vertexIndexCompositevertex--assetLineageVariables", Constants.ASSET_LINEAGE_VARIABLES, false, VERTEX);
        String indexCommandMetadataCollectionId = createIndexCommand("vertexIndexCompositevertex--metadataCollectionId", GraphConstants.PROPERTY_KEY_METADATA_ID, false, VERTEX);
        String indexCommandEdgeGuid = createIndexCommand("edgeIndexCompositeedge--guid", GraphConstants.PROPERTY_KEY_RELATIONSHIP_GUID, false, EDGE);
        String indexCommandEdgeLabel = createIndexCommand("edgeIndexCompositeedge--label", GraphConstants.PROPERTY_KEY_RELATIONSHIP_LABEL, false, EDGE);


        log.debug("Checking indices...");
        client.submit(indexCommandGuid);
        client.submit(indexCommandLabel);
        client.submit(indexCommandVersion);
        client.submit(indexCommandAssetLineageVariables);
        client.submit(indexCommandMetadataCollectionId);
        client.submit(indexCommandEdgeGuid);
        client.submit(indexCommandEdgeLabel);
    }


    /**
     * @param indexName                       - name of the index
     * @param propertyName                    - name of the property being indexed
     * @param hasPropertyUniqueAndConsistency - is the unique constraint index
     * @param className                       - the type of the index class to be used Vertex or Edge
     * @return String representation of gremlin command to create index for property name using janus graph management API.
     */
    private String createIndexCommand(String indexName, String propertyName, boolean hasPropertyUniqueAndConsistency, String className) {
        StringBuilder indexCommand = new StringBuilder();
        indexCommand.append("management = graph.openManagement();\n");
        indexCommand.append("vertexIndex = management.getGraphIndex(\"").append(indexName).append("\");\n");
        indexCommand.append("if (vertexIndex != null ){   management.rollback(); }\n");
        indexCommand.append(" else { ");
        indexCommand.append("propertyKeyGuid = management.makePropertyKey(\"").append(propertyName).append("\").dataType(String.class).make();\n");
        indexCommand.append("indexBuilderGuidVertex = management.buildIndex(\"").append(indexName).append("\", ").append(className).append(".class).addKey(propertyKeyGuid);\n");
        if (hasPropertyUniqueAndConsistency) {
            indexCommand.append("indexBuilderGuidVertex.unique();\n");
        }
        indexCommand.append("indexGuidVertex = indexBuilderGuidVertex.buildCompositeIndex();\n");
        if (hasPropertyUniqueAndConsistency) {
            indexCommand.append("management.setConsistency(indexGuidVertex, ConsistencyModifier.LOCK);\n");
        }
        indexCommand.append("management.commit();\n");
        indexCommand.append("management = graph.openManagement();\n");
        indexCommand.append("ManagementSystem.awaitGraphIndexStatus(graph,\"").append(indexName).append("\").timeout(15, ChronoUnit.SECONDS).call();\n");
        indexCommand.append("management.updateIndex(management.getGraphIndex(\"").append(indexName).append("\"), SchemaAction.REINDEX).get();\n");
        indexCommand.append("management.commit();\n");
        indexCommand.append("}\n");
        return indexCommand.toString();

    }

}
