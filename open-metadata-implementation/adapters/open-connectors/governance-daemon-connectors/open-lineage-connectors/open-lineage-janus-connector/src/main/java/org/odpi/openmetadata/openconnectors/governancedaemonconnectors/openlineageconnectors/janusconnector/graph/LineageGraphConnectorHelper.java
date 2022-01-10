/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.until;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSETS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.AVRO_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.COLLECTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CONNECTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CONNECTION_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CSV_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATABASE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATABASE_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE_AND_SUBTYPES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DOCUMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_TYPE_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_TYPE_LIST;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_TYPE_LIST_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FILE_FOLDER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FILE_FOLDER_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.JSON_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.KEYSTORE_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.LOG_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.MEDIA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_DB_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.SCHEMA_TYPE_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_FILE_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TOPIC;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TOPIC_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TRANSFORMATION_PROJECT_KEY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.CONDENSED_NODE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.DESTINATION_CONDENSATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_ANTONYM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CLASSIFICATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_COLUMN_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_IS_A_RELATIONSHIP;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_RELATED_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_REPLACEMENT_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SYNONYM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TABLE_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TERM_CATEGORIZATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TRANSLATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROCESS_NODES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ADDITIONAL_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_EXTENDED_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCE_PROP_ADDITIONAL_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PROCESS_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.SOURCE_CONDENSATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.immutableReturnedPropertiesWhiteList;

public class LineageGraphConnectorHelper {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphConnectorHelper.class);
    private static final String EMPTY_STRING = "";
    private static final String COMMA_SPACE_DELIMITER = ", ";
    private static final String COLUMN_SPACE_DELIMITER = ": ";
    private static final List<String> EMBEDDED_PROPERTIES = Arrays.asList(PROPERTY_KEY_ADDITIONAL_PROPERTIES, PROPERTY_KEY_EXTENDED_PROPERTIES);
    private static final String SUB_GRAPH = "subGraph";
    private static final String GENERIC_QUERY_EXCEPTION = "Exception while querying {} of guid {}: {}. Executed rollback.";
    private static final String ULTIMATE_DESTINATION_HORIZONTAL_LINEAGE = "ultimate destination horizontal lineage";
    private static final String TABULAR_COLUMN_VERTICAL_LINEAGE = "tabular column vertical lineage";
    private static final String RELATIONAL_COLUMN_VERTICAL_LINEAGE = "relational column vertical lineage";
    private static final String GLOSSARY_TERM_VERTICAL_LINEAGE = "glossary term vertical lineage";
    private static final String END_TO_END_HORIZONTAL_LINEAGE = "end to end horizontal lineage";
    private static final String ULTIMATE_SOURCE_HORIZONTAL_LINEAGE = "ultimate source horizontal lineage";
    private static final String S = "s";

    private final GraphFactory graphFactory;
    private final boolean supportingTransactions;
    private final String[] glossaryTermAndClassificationEdges = {EDGE_LABEL_SEMANTIC_ASSIGNMENT, EDGE_LABEL_RELATED_TERM,
            EDGE_LABEL_SYNONYM, EDGE_LABEL_ANTONYM, EDGE_LABEL_REPLACEMENT_TERM, EDGE_LABEL_TRANSLATION, EDGE_LABEL_IS_A_RELATIONSHIP,
            EDGE_LABEL_CLASSIFICATION, EDGE_LABEL_TERM_CATEGORIZATION};
    private final String[] relationalColumnAndClassificationEdges =
            {NESTED_SCHEMA_ATTRIBUTE, EDGE_LABEL_CLASSIFICATION, EDGE_LABEL_SEMANTIC_ASSIGNMENT};
    private final String[] tabularColumnAndClassificationEdges = {ATTRIBUTE_FOR_SCHEMA, EDGE_LABEL_CLASSIFICATION, EDGE_LABEL_SEMANTIC_ASSIGNMENT};

    public LineageGraphConnectorHelper(GraphFactory graphFactory, boolean supportingTransactions) {;
        this.graphFactory = graphFactory;
        this.supportingTransactions = supportingTransactions;
    }

    /**
     * Returns the ultimate source graph of queried entity, which can be a column or a table. In case of tables,
     * relationships of type LineageMapping will be traversed backwards, all the way to the source. If no vertices are
     * found, than DataFlow relationships are used for traversal. In case of columns, DataFlow relationships are
     * directly used
     *
     * @param guid queried entity
     *
     * @return graph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> ultimateSource(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Vertex queriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();

        List<Vertex> sourcesList;
        if (ASSETS.contains(queriedVertex.label())) {
            // lineage based on edges of type LINEAGE_MAPPING, is to be done only for assets
            sourcesList = querySources(guid, LINEAGE_MAPPING);
            if (CollectionUtils.isNotEmpty(sourcesList) && !sourcesList.equals(Collections.singletonList(queriedVertex))) {
                return Optional.of(getCondensedLineage(guid, g, getLineageVertices(sourcesList), SOURCE_CONDENSATION));
            }
        }

        Optional<String> edgeLabelOptional = getEdgeLabelForDataFlow(queriedVertex);
        if (edgeLabelOptional.isEmpty()) {
            return Optional.empty();
        }
        String edgeLabel = edgeLabelOptional.get();
        sourcesList = querySources(guid, edgeLabel);
        return Optional.of(getCondensedLineage(guid, g, getLineageVertices(sourcesList), SOURCE_CONDENSATION));
    }

    /**
     * Query graph for sources
     *
     * @param guid      entity
     * @param edgeLabel edge type to traverse
     *
     * @return sources
     */
    private List<Vertex> querySources(String guid, String edgeLabel) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        List<Vertex> sourceList = null;
        try {
            sourceList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                    until(inE(edgeLabel).count().is(0)).
                    repeat(inE(edgeLabel).outV().simplePath()).
                    dedup().toList();

            commitTransaction(g);
        } catch (Exception e) {
            if (supportingTransactions) {
                g.tx().rollback();
            }
            log.error(GENERIC_QUERY_EXCEPTION, ULTIMATE_SOURCE_HORIZONTAL_LINEAGE, guid, e.getMessage());
        }
        return sourceList;
    }

    /**
     * Returns the ultimate destination graph of queried entity, which can be a column or a table. In case of tables,
     * relationships of type LineageMapping will be traversed forwards, all the way to the destination. If no vertices
     * are found, than DataFlow relationships are used for traversal. In case of columns, DataFlow relationships are
     * directly used
     *
     * @param guid queried entity
     *
     * @return graph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> ultimateDestination(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Vertex queriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();

        List<Vertex> destinationsList;
        String label = queriedVertex.label();
        if (ASSETS.contains(label)) {
            // lineage based on edges of type LINEAGE_MAPPING, is to be done only for assets
            destinationsList = queryDestinations(guid, LINEAGE_MAPPING);
            if (CollectionUtils.isNotEmpty(destinationsList) && !destinationsList.equals(Collections.singletonList(queriedVertex))) {
                return Optional.of(getCondensedLineage(guid, g, getLineageVertices(destinationsList), DESTINATION_CONDENSATION));
            }
        }

        Optional<String> edgeLabelOptional = getEdgeLabelForDataFlow(queriedVertex);
        if (edgeLabelOptional.isEmpty()) {
            return Optional.empty();
        }
        String edgeLabel = edgeLabelOptional.get();
        destinationsList = queryDestinations(guid, edgeLabel);

        return Optional.of(getCondensedLineage(guid, g, getLineageVertices(destinationsList), DESTINATION_CONDENSATION));
    }

    /**
     * Query graph for destinations
     *
     * @param guid      entity
     * @param edgeLabel edge type to traverse
     *
     * @return sources
     */
    private List<Vertex> queryDestinations(String guid, String edgeLabel) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        List<Vertex> destinationList = null;
        try {
            destinationList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                    until(outE(edgeLabel).count().is(0)).
                    repeat(outE(edgeLabel).inV().simplePath()).
                    dedup().toList();

            commitTransaction(g);
        } catch (Exception e) {
            if (supportingTransactions) {
                g.tx().rollback();
            }
            log.error(GENERIC_QUERY_EXCEPTION, ULTIMATE_DESTINATION_HORIZONTAL_LINEAGE, guid, e.getMessage());
        }
        return destinationList;
    }

    /**
     * Returns the end to end graph of queried entity, which can be a column or a table. In case of tables, relationships
     * of type LineageMapping will be traversed backwards and forwards, all the way to the source and the destination,
     * respectively. If no vertices are found, than DataFlow relationships are used for traversal. In case of columns,
     * DataFlow relationships are directly used
     *
     * @param guid             queried entity
     * @param includeProcesses include processes
     *
     * @return graph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> endToEnd(String guid, boolean includeProcesses) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Vertex queriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();

        Graph endToEndGraph;
        String label = queriedVertex.label();

        if (ASSETS.contains(label)) {
            // lineage based on edges of type LINEAGE_MAPPING, is to be done only for assets
            endToEndGraph = queryEndToEnd(guid, LINEAGE_MAPPING);
            if (endToEndGraph == null) {
                return Optional.empty();
            }

            if (endToEndGraph.vertices().hasNext()) {
                return Optional.of(getLineageVerticesAndEdges(endToEndGraph, includeProcesses));
            }
        }

        Optional<String> edgeLabelOptional = getEdgeLabelForDataFlow(queriedVertex);
        if (edgeLabelOptional.isEmpty()) {
            return Optional.empty();
        }
        String edgeLabel = edgeLabelOptional.get();
        endToEndGraph = queryEndToEnd(guid, edgeLabel);

        return Optional.of(getLineageVerticesAndEdges(endToEndGraph, includeProcesses));
    }

    /**
     * Queries graph for end to end
     *
     * @param guid      queried entity
     * @param edgeLabel edge type to traverse
     *
     * @return graph
     */
    private Graph queryEndToEnd(String guid, String edgeLabel) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Graph endToEndGraph = null;
        try {
            endToEndGraph = (Graph)
                    g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                            union(
                                    until(inE(edgeLabel).count().is(0)).
                                            repeat((Traversal) inE(edgeLabel).subgraph(SUB_GRAPH).outV().simplePath()),
                                    until(outE(edgeLabel).count().is(0)).
                                            repeat((Traversal) outE(edgeLabel).subgraph(SUB_GRAPH).inV().simplePath())
                            ).cap(SUB_GRAPH).next();

            commitTransaction(g);
        } catch (Exception e) {
            if (supportingTransactions) {
                g.tx().rollback();
            }
            log.error(GENERIC_QUERY_EXCEPTION, END_TO_END_HORIZONTAL_LINEAGE, guid, e.getMessage());
        }
        return endToEndGraph;
    }

    /**
     * Returns a subgraph by navigating edges specified in {@link #glossaryTermAndClassificationEdges}, like semantic
     * assignments and various relationships between glossary terms. Classifications are included
     *
     * @param guid guid to extract vertical lineage for
     *
     * @return a subgraph in an Open Lineage specific format.
     */
    private Optional<LineageVerticesAndEdges> glossaryVerticalLineage(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Graph subGraph = null;

        try {
            subGraph = (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(glossaryTermAndClassificationEdges)
                    .subgraph(S).cap(S).next();
            commitTransaction(g);

        } catch (Exception e) {
            if (supportingTransactions) {
                g.tx().rollback();
            }
            log.error(GENERIC_QUERY_EXCEPTION, GLOSSARY_TERM_VERTICAL_LINEAGE, guid, e.getMessage());
        }

        return Optional.of(getLineageVerticesAndEdges(subGraph, true));
    }

    /**
     * Returns a subgraph by navigating edges specified in {@link #relationalColumnAndClassificationEdges}, like semantic
     * assignments. Classifications are included
     *
     * @param guid guid to extract vertical lineage for
     *
     * @return a subgraph in an Open Lineage specific format.
     */
    private Optional<LineageVerticesAndEdges> relationalColumnVerticalLineage(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Graph subGraph = null;

        try {
            subGraph = (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(relationalColumnAndClassificationEdges)
                    .subgraph("s").cap("s").next();
            commitTransaction(g);

        } catch (Exception e) {
            if (supportingTransactions) {
                g.tx().rollback();
            }
            log.error(GENERIC_QUERY_EXCEPTION, RELATIONAL_COLUMN_VERTICAL_LINEAGE, guid, e.getMessage());
        }

        return Optional.of(getLineageVerticesAndEdges(subGraph, true));
    }

    /**
     * Returns a subgraph by navigating edges specified in {@link #tabularColumnAndClassificationEdges}, like semantic
     * assignments. Classifications are included
     *
     * @param guid guid to extract vertical lineage for
     *
     * @return a subgraph in an Open Lineage specific format.
     */
    private Optional<LineageVerticesAndEdges> tabularColumnVerticalLineage(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        Graph subGraph = null;

        try {
            subGraph = (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(tabularColumnAndClassificationEdges)
                    .subgraph("s").bothV().inE(ASSET_SCHEMA_TYPE).subgraph("s").cap("s").next();
            commitTransaction(g);

        } catch (Exception e) {
            if (supportingTransactions) {
                g.tx().rollback();
            }
            log.error(GENERIC_QUERY_EXCEPTION, TABULAR_COLUMN_VERTICAL_LINEAGE, guid, e.getMessage());
        }

        return Optional.of(getLineageVerticesAndEdges(subGraph, true));
    }

    /**
     * Returns a subgraph navigating the edges of interest based on target node type. For more info, check
     * {@link #glossaryVerticalLineage}, {@link #tabularColumnVerticalLineage}, {@link #relationalColumnVerticalLineage}
     *
     * @param guid guid to extract vertical lineage for
     *
     * @return a subgraph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> verticalLineage(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        String label = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).label().next();
        switch (label) {
            case GLOSSARY_TERM:
                return glossaryVerticalLineage(guid);
            case RELATIONAL_COLUMN:
                return relationalColumnVerticalLineage(guid);
            case TABULAR_COLUMN:
            case TABULAR_FILE_COLUMN:
                return tabularColumnVerticalLineage(guid);
            default:
                return Optional.empty();
        }
    }

    /**
     * Remove all nodes which displayname does not include the provided String. Any connected edges will also be removed.
     *
     * @param lineageVerticesAndEdges The list of vertices and edges which should be filtered on displayname.
     * @param displayNameMustContain  The substring that must be part of a node's displayname in order for that node to be returned.
     */
    void filterDisplayName(LineageVerticesAndEdges lineageVerticesAndEdges, String displayNameMustContain) {
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        Set<LineageEdge> lineageEdges = lineageVerticesAndEdges.getLineageEdges();
        Set<LineageVertex> verticesToBeRemoved = new HashSet<>();
        Set<LineageEdge> edgesToBeRemoved = new HashSet<>();

        for (LineageVertex vertex : lineageVertices) {
            String nodeID = vertex.getNodeID();
            if (!vertex.getDisplayName().contains(displayNameMustContain)) {
                verticesToBeRemoved.add(vertex);
                for (LineageEdge edge : lineageEdges) {
                    if (edge.getSourceNodeID().equals(nodeID) || edge.getDestinationNodeID().equals(nodeID))
                        edgesToBeRemoved.add(edge);
                }
            }
        }
        lineageVertices.removeAll(verticesToBeRemoved);
        lineageEdges.removeAll(edgesToBeRemoved);
        lineageVerticesAndEdges.setLineageVertices(lineageVertices);
        lineageVerticesAndEdges.setLineageEdges(lineageEdges);
    }

    /**
     * * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * * The queried node can be a column or table.
     *
     * @param guid             the guid of the queried node
     * @param g                graph traversal object
     * @param lineageVertices  list of ultimate vertices
     * @param condensationType the type of the condensation
     *
     * @return the subgraph in an Open Lineage specific format
     */
    private LineageVerticesAndEdges getCondensedLineage(String guid, GraphTraversalSource g, Set<LineageVertex> lineageVertices,
                                                        String condensationType) {
        Vertex originalQueriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        commitTransaction(g);
        LineageVertex queriedVertex = abstractVertex(originalQueriedVertex);

        Set<LineageEdge> lineageEdges = new HashSet<>();
        if (CollectionUtils.isNotEmpty(lineageVertices) && !Collections.singleton(queriedVertex).equals(lineageVertices)) {
            LineageVertex condensedVertex = getCondensedVertex(condensationType);
            lineageVertices.add(condensedVertex);
            lineageEdges = getCondensedLineageEdges(lineageVertices, queriedVertex, condensedVertex, condensationType);
            lineageVertices.add(queriedVertex);
        }

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    private Set<LineageEdge> getCondensedLineageEdges(Set<LineageVertex> lineageVertices, LineageVertex queriedVertex,
                                                      LineageVertex condensedVertex, String condensationType) {
        Set<LineageEdge> lineageEdges = new HashSet<>();

        if (SOURCE_CONDENSATION.equalsIgnoreCase(condensationType)) {
            lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED, condensedVertex.getNodeID(), queriedVertex.getNodeID()));
            lineageVertices.forEach(ultimateVertex -> lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED, ultimateVertex.getNodeID(),
                    condensedVertex.getNodeID())));
        }
        if (DESTINATION_CONDENSATION.equalsIgnoreCase(condensationType)) {
            lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED, queriedVertex.getNodeID(), condensedVertex.getNodeID()));
            lineageVertices.forEach(ultimateVertex -> lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED, condensedVertex.getNodeID(),
                    ultimateVertex.getNodeID())));
        }
        return lineageEdges;
    }

    private LineageVertex getCondensedVertex(String condensationType) {
        LineageVertex condensedVertex = new LineageVertex(getCondensedNodeId(condensationType), NODE_LABEL_CONDENSED);
        condensedVertex.setDisplayName(CONDENSED_NODE_DISPLAY_NAME);
        condensedVertex.setQualifiedName("");
        condensedVertex.setGuid("");
        return condensedVertex;
    }

    /**
     * Map a Tinkerpop vertex to the Open Lineage format.
     *
     * @param originalVertex The vertex to be mapped.
     *
     * @return The vertex in the Open Lineage format.
     */
    private LineageVertex abstractVertex(Vertex originalVertex) {
        String nodeType = originalVertex.label();
        String nodeID = getNodeID(originalVertex);
        LineageVertex lineageVertex = new LineageVertex(nodeID, nodeType);

        if (originalVertex.property(PROPERTY_KEY_DISPLAY_NAME).isPresent()) {
            String displayName = originalVertex.property(PROPERTY_KEY_DISPLAY_NAME).value().toString();
            lineageVertex.setDisplayName(displayName);
        } else if (originalVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).isPresent()) {
            //Displayname key is stored inconsistently in the graphDB.
            String displayName = originalVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString();
            lineageVertex.setDisplayName(displayName);
        } else if (originalVertex.property(PROPERTY_KEY_LABEL).isPresent()) {
            // if no display props exist use the label. every vertex should have it
            String displayName = originalVertex.property(PROPERTY_KEY_LABEL).value().toString();
            lineageVertex.setDisplayName(displayName);
        } else {
            // if all else fails
            lineageVertex.setDisplayName("undefined");
        }

        if (originalVertex.property(PROPERTY_KEY_ENTITY_GUID).isPresent()) {
            String guid = originalVertex.property(PROPERTY_KEY_ENTITY_GUID).value().toString();
            lineageVertex.setGuid(guid);
        }
        if (originalVertex.property(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).isPresent()) {
            String qualifiedName = originalVertex.property(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).value().toString();
            lineageVertex.setQualifiedName(qualifiedName);
        }
        if (NODE_LABEL_SUB_PROCESS.equals(nodeType) && originalVertex.property((PROPERTY_KEY_PROCESS_GUID)).isPresent()) {
            lineageVertex.setGuid(originalVertex.property((PROPERTY_KEY_PROCESS_GUID)).value().toString());
            lineageVertex.setNodeID(originalVertex.property((PROPERTY_KEY_PROCESS_GUID)).value().toString());

        }
        Map<String, String> properties = retrieveProperties(originalVertex);
        lineageVertex.setProperties(properties);
        return lineageVertex;
    }

    private String getNodeID(Vertex vertex) {
        String nodeID;
        if (vertex.label().equalsIgnoreCase(NODE_LABEL_SUB_PROCESS)) {
            nodeID = vertex.property(PROPERTY_KEY_PROCESS_GUID).value().toString();
        } else {
            nodeID = vertex.property(PROPERTY_KEY_ENTITY_GUID).value().toString();
        }
        return nodeID;
    }

    /**
     * Retrieve all properties of the vertex from the db and return the ones that match the whitelist. This will filter out irrelevant
     * properties that should not be returned to a UI.
     *
     * @param vertex the vertex to de mapped
     *
     * @return the filtered properties of the vertex
     */
    private Map<String, String> retrieveProperties(Vertex vertex) {
        boolean isClassificationVertex = vertex.edges(Direction.IN, EDGE_LABEL_CLASSIFICATION).hasNext();
        Map<String, String> newNodeProperties = new HashMap<>();
        Iterator<VertexProperty<Object>> originalProperties = vertex.properties();
        while (originalProperties.hasNext()) {
            Property<Object> originalProperty = originalProperties.next();
            if (immutableReturnedPropertiesWhiteList.contains(originalProperty.key()) || isClassificationVertex) {
                String newPropertyKey = originalProperty.key().
                        replace(PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY, EMPTY_STRING).
                        replace(PROPERTY_KEY_PREFIX_ELEMENT, EMPTY_STRING);

                String newPropertyValue = originalProperty.value().toString();
                newNodeProperties.put(newPropertyKey, newPropertyValue);
            }
        }
        return newNodeProperties;
    }

    /**
     * Retrieve all properties of the vertex from the db without filtering.
     *
     * @param vertex the vertex to de mapped
     *
     * @return the filtered properties of the vertex
     */
    private Map<String, String> retrieveAllProperties(Vertex vertex) {

        Map<String, String> newNodeProperties = new HashMap<>();
        Iterator<VertexProperty<Object>> originalProperties = vertex.properties();

        while (originalProperties.hasNext()) {
            Property<Object> originalProperty = originalProperties.next();

            String newPropertyKey = originalProperty.key().
                    replace(PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY, EMPTY_STRING).
                    replace(PROPERTY_KEY_PREFIX_ELEMENT, EMPTY_STRING);

            String newPropertyValue = originalProperty.value().toString();

            if (EMBEDDED_PROPERTIES.contains(newPropertyKey)) {
                String[] propertyPairs = newPropertyValue.split(COMMA_SPACE_DELIMITER);
                for (String propertyPair : propertyPairs) {
                    String[] propertyItems = propertyPair.split(COLUMN_SPACE_DELIMITER);
                    newNodeProperties.put(propertyItems[0], propertyItems[1]);
                }
            } else {
                newNodeProperties.put(newPropertyKey, newPropertyValue);
            }
        }
        return newNodeProperties;
    }

    /**
     * Map a tinkerpop Graph object to an Open Lineage specific format.
     *
     * @param subGraph The graph to be mapped.
     *
     * @return The graph in an Open Lineage specific format.
     */
    private LineageVerticesAndEdges getLineageVerticesAndEdges(Graph subGraph, boolean includeProcesses) {
        Set<LineageVertex> lineageVertices = getLineageVertices(subGraph);
        Set<LineageEdge> lineageEdges = getLineageEdges(subGraph);

        condenseProcesses(includeProcesses, lineageVertices, lineageEdges);

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    private boolean isVertexToBeCondensed(LineageVertex lineageVertex, LineageVertex queriedVertex, Set<LineageVertex> ultimateVertices) {
        return !queriedVertex.getGuid().equalsIgnoreCase(lineageVertex.getGuid()) && !ultimateVertices.contains(lineageVertex);
    }

    private Set<LineageEdge> getLineageEdges(Graph subGraph) {
        Iterator<Edge> originalEdges = subGraph.edges();
        Set<LineageEdge> lineageEdges = new HashSet<>();
        while (originalEdges.hasNext()) {
            Edge next = originalEdges.next();
            LineageEdge newLineageEdge = new LineageEdge(next.label(), getNodeID(next.outVertex()), getNodeID(next.inVertex()));
            lineageEdges.add(newLineageEdge);
        }
        return lineageEdges;
    }

    private Set<LineageVertex> getLineageVertices(Graph subGraph) {
        Iterator<Vertex> originalVertices = subGraph.vertices();
        Set<LineageVertex> lineageVertices = new LinkedHashSet<>();
        while (originalVertices.hasNext()) {
            LineageVertex newVertex = abstractVertex(originalVertices.next());
            lineageVertices.add(newVertex);
        }
        return lineageVertices;
    }

    private Set<LineageVertex> getLineageVertices(List<Vertex> vertexList) {
        if (CollectionUtils.isNotEmpty(vertexList)) {
            return vertexList.stream().map(this::abstractVertex).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private void condenseProcesses(boolean includeProcesses, Set<LineageVertex> lineageVertices, Set<LineageEdge> lineageEdges) {
        if (!includeProcesses) {
            Set<LineageVertex> verticesToRemove = lineageVertices.stream()
                    .filter(this::isProcessOrSubprocessNode)
                    .collect(Collectors.toSet());
            Set<String> verticesToRemoveIDs = verticesToRemove.stream().map(LineageVertex::getNodeID).collect(Collectors.toSet());
            Set<LineageEdge> edgesToRemove = lineageEdges.stream().filter(edge ->
                    isInVertexesToRemove(verticesToRemoveIDs, edge)).collect(Collectors.toSet());
            List<LineageEdge> edgesToReplaceProcesses = createEdgesThatReplaceProcesses(lineageEdges, verticesToRemoveIDs);
            lineageEdges.addAll(edgesToReplaceProcesses);
            lineageEdges.removeAll(edgesToRemove);
            lineageVertices.removeAll(verticesToRemove);
        }
    }

    private List<LineageEdge> createEdgesThatReplaceProcesses(Set<LineageEdge> lineageEdges, Set<String> verticesToRemoveNames) {
        List<LineageEdge> edgesToReplaceProcesses = new ArrayList<>();
        for (String vertexName : verticesToRemoveNames) {
            for (LineageEdge edge : lineageEdges) {
                if (edge.getDestinationNodeID().equalsIgnoreCase(vertexName)) {
                    for (LineageEdge destinationEdge : lineageEdges) {
                        if (destinationEdge.getSourceNodeID().equalsIgnoreCase(vertexName)) {
                            edgesToReplaceProcesses.add(new LineageEdge(EDGE_LABEL_CONDENSED,
                                    edge.getSourceNodeID(), destinationEdge.getDestinationNodeID()));
                        }
                    }
                }
            }
        }
        return edgesToReplaceProcesses;
    }

    private boolean isProcessOrSubprocessNode(LineageVertex vertex) {
        return PROCESS_NODES.stream().anyMatch(vertex.getNodeType()::equalsIgnoreCase);
    }

    private boolean isInVertexesToRemove(Set<String> verticesToRemoveNames, LineageEdge edge) {
        return verticesToRemoveNames.contains(edge.getSourceNodeID()) || verticesToRemoveNames.contains(edge.getDestinationNodeID());
    }

    private void addColumnProperties(Set<LineageVertex> lineageVertices) {
        if (CollectionUtils.isEmpty(lineageVertices)) {
            return;
        }
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        lineageVertices.forEach(lineageVertex -> {
            Vertex graphVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageVertex.getGuid()).next();
            commitTransaction(g);
            Object vertexId = graphVertex.id();
            Map<String, String> properties = new HashMap<>();

            switch (lineageVertex.getNodeType()) {
                case TABULAR_FILE_COLUMN:
                case TABULAR_COLUMN:
                    properties = getTabularColumnProperties(g, vertexId);
                    break;
                case RELATIONAL_COLUMN:
                    properties = getRelationalColumnProperties(g, vertexId);
                    break;
                case EVENT_SCHEMA_ATTRIBUTE:
                    properties = getEventSchemaAttributeProperties(g, vertexId);
                    break;
                case RELATIONAL_TABLE:
                    properties = getRelationalTableProperties(g, vertexId);
                    break;
                case TOPIC:
                    properties = getTopicProperties(g, vertexId);
                    break;
                case DATA_FILE:
                case AVRO_FILE:
                case CSV_FILE:
                case JSON_FILE:
                case KEYSTORE_FILE:
                case LOG_FILE:
                case MEDIA_FILE:
                case DOCUMENT:
                    properties = getDataFileProperties(g, vertexId);
                    break;
                case PROCESS:
                case NODE_LABEL_SUB_PROCESS:
                    properties = getProcessProperties(g, vertexId);
                    break;
                case GLOSSARY_TERM:
                case GLOSSARY_CATEGORY:
                    properties = getGlossaryTermProperties(g, vertexId);
                    break;
            }
            lineageVertex.setProperties(properties);
        });
    }

    private Map<String, String> getRelationalColumnProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Vertex> tableAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(RELATIONAL_TABLE));
        commitTransaction(g);
        if (tableAsset.hasNext()) {
            Vertex tableAssetVertex = tableAsset.next();
            properties.put(RELATIONAL_TABLE_KEY, getDisplayNameForVertex(tableAssetVertex));
            properties.putAll(getRelationalTableProperties(g, tableAssetVertex.id()));
        }

        return properties;
    }

    private Map<String, String> getTabularColumnProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Map<Object, List<String>>> tabularSchemaType =
                g.V(vertexId).emit().repeat(bothE().outV().simplePath()).times(1).or(hasLabel(TABULAR_SCHEMA_TYPE)).valueMap();
        if (tabularSchemaType.hasNext()) {
            properties.put(SCHEMA_TYPE_KEY, getDisplayNameForVertex(tabularSchemaType.next()));
        }

        GraphTraversal<Vertex, Object> dataFileAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2)
                .or(hasLabel(P.within(DATA_FILE_AND_SUBTYPES))).id();

        commitTransaction(g);
        if (dataFileAsset.hasNext()) {
            properties.putAll(getDataFileProperties(g, dataFileAsset.next()));
        }

        return properties;
    }

    private String getFoldersPath(List<Vertex> folderVertices) {
        Collections.reverse(folderVertices);
        return folderVertices.stream().map(folderVertex -> folderVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString())
                .collect(Collectors.joining("/"));
    }

    private List<Vertex> getFolderVertices(GraphTraversalSource g, Object dataFileAssetId) {
        GraphTraversal<Vertex, Vertex> fileFolders =
                g.V(dataFileAssetId).emit().repeat(bothE().otherV().simplePath()).until(inE(FOLDER_HIERARCHY).count().is(0))
                        .or(hasLabel(FILE_FOLDER));
        commitTransaction(g);
        List<Vertex> folderVertices = new ArrayList<>();
        while (fileFolders.hasNext()) {
            folderVertices.add(fileFolders.next());
        }
        return folderVertices;
    }

    private Map<String, String> getRelationalTableProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();

        Iterator<Vertex> relationalDBSchemaType =
                g.V(vertexId).emit().repeat(bothE().outV().simplePath()).times(1).or(hasLabel(RELATIONAL_DB_SCHEMA_TYPE));
        commitTransaction(g);
        if (relationalDBSchemaType.hasNext()) {
            properties.put(SCHEMA_TYPE_KEY, getDisplayNameForVertex(relationalDBSchemaType.next()));
        }

        Iterator<Vertex> database = g.V(vertexId).emit().repeat(bothE().outV().simplePath()).times(3).or(hasLabel(DATABASE));
        commitTransaction(g);
        if (database.hasNext()) {
            properties.put(DATABASE_KEY, getDisplayNameForVertex(database.next()));
        }

        Iterator<Vertex> connection = g.V(vertexId).emit().repeat(bothE().outV().simplePath()).times(4).hasLabel(CONNECTION);
        commitTransaction(g);
        if (connection.hasNext()) {
            properties.put(CONNECTION_KEY, getDisplayNameForVertex(connection.next()));
        }

        return properties;

    }

    private String getDisplayNameForVertex(Vertex vertex) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        GraphTraversal<Vertex, Map<Object, List<String>>> vertexMapGraphTraversal = g.V(vertex.id()).valueMap();
        if (!vertexMapGraphTraversal.hasNext()) {
            return null;
        }
        Map<Object, List<String>> vertexMap = vertexMapGraphTraversal.next();
        if (vertexMap.containsKey(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)) {
            return vertexMap.get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).get(0);
        } else if (vertexMap.containsKey(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME)) {
            return vertexMap.get(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).get(0);
        }
        commitTransaction(g);
        return null;
    }

    private String getDisplayNameForVertex(Map<Object, List<String>> vertex) {
        if (vertex.containsKey(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)) {
            return vertex.get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).get(0);
        } else if (vertex.containsKey(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME)) {
            return vertex.get(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).get(0);
        }
        return null;
    }


    private Map<String, String> getDataFileProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = extractPropertiesFromNeighborhood(g, vertexId);
        if (!properties.containsKey(FILE_FOLDER_KEY)) {
            Optional<String> path = extractPathFromVertexProperties(g, vertexId);
            path.ifPresent(s -> properties.put(FILE_FOLDER_KEY, "/" + s.trim()));
        }
        return properties;
    }

    private Map<String, String> extractPropertiesFromNeighborhood(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();

        List<Vertex> folderVertices = getFolderVertices(g, vertexId);
        if (CollectionUtils.isEmpty(folderVertices)) {
            return properties;
        }
        Object lastFolderVertexId = folderVertices.get(folderVertices.size() - 1).id();
        properties.put(FILE_FOLDER_KEY, String.join("/", getFoldersPath(folderVertices)));

        Optional<String> connectionDetails = getConnectionDetailsFromNeighborhood(g, vertexId);
        if (connectionDetails.isEmpty()) {
            connectionDetails = getConnectionDetailsFromNeighborhood(g, lastFolderVertexId);
        }
        connectionDetails.ifPresent(s -> properties.put(CONNECTION_KEY, s));

        return properties;
    }

    private Optional<String> getConnectionDetailsFromNeighborhood(GraphTraversalSource g, Object vertexId) {
        Iterator<Vertex> connection = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(CONNECTION));
        commitTransaction(g);
        if (connection.hasNext()) {
            return Optional.ofNullable(this.getDisplayNameForVertex(connection.next()));
        }
        return Optional.empty();
    }

    private Optional<String> extractPathFromVertexProperties(GraphTraversalSource g, Object vertexId) {

        VertexProperty<String> additionalProperties =
                g.V(vertexId).next().property(PROPERTY_KEY_INSTANCE_PROP_ADDITIONAL_PROPERTIES);
        if (!additionalProperties.isPresent()) {
            return Optional.empty();
        }

        String additionalPropertiesValue = additionalProperties.value();
        return Arrays.stream(additionalPropertiesValue.split(","))
                .filter(s -> s.trim().startsWith("path"))
                .map(s -> s.split(":")[1])
                .findFirst();
    }

    private Map<String, String> getProcessProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Map<Object, Object>> transformationProject = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(COLLECTION)).valueMap();
        commitTransaction(g);
        if (transformationProject.hasNext()) {
            Map<Object, Object> transformationProjectValueMap = transformationProject.next();
            if (transformationProjectValueMap.containsKey(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)) {
            properties.put(TRANSFORMATION_PROJECT_KEY,
                    ((List<String>)transformationProjectValueMap.get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)).get(0));
            }
        }
        return properties;
    }

    private Map<String, String> getGlossaryTermProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Map<Object, List<String>>> glossary = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(GLOSSARY)).valueMap(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME);
        commitTransaction(g);
        if (glossary.hasNext()) {
            properties.put(GLOSSARY_KEY, glossary.next().get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).get(0));
        }
        return properties;
    }

    private void commitTransaction(GraphTraversalSource graphTraversalSource) {
        if (supportingTransactions) {
            graphTraversalSource.tx().commit();
        }
    }

    private String getCondensedNodeId(String condensationType) {
        if (SOURCE_CONDENSATION.equalsIgnoreCase(condensationType)) {
            return PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
        } else {
            return PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
        }
    }

    private Optional<String> getEdgeLabelForDataFlow(Vertex vertex) {
        String label = vertex.label();
        switch (label) {
            case TABULAR_FILE_COLUMN:
            case TABULAR_COLUMN:
            case RELATIONAL_COLUMN:
            case EVENT_SCHEMA_ATTRIBUTE:
                return Optional.of(EDGE_LABEL_COLUMN_DATA_FLOW);
            case DATA_FILE:
            case AVRO_FILE:
            case CSV_FILE:
            case JSON_FILE:
            case KEYSTORE_FILE:
            case LOG_FILE:
            case MEDIA_FILE:
            case DOCUMENT:
            case RELATIONAL_TABLE:
            case TOPIC:
                return Optional.of(EDGE_LABEL_TABLE_DATA_FLOW);
            default:
                return Optional.empty();
        }
    }

    /**
     * Gets lineage vertex by guid.
     *
     * @param guid the guid
     *
     * @return the lineage vertex by guid
     */
    LineageVertex getLineageVertexByGuid(String guid) {
        GraphTraversalSource g = graphFactory.getGraphTraversalSource();
        GraphTraversal<Vertex, Vertex> vertexGraphTraversal = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid);

        Map<String, String> properties = new HashMap<>();
        if (vertexGraphTraversal.hasNext()) {
            Vertex vertex = vertexGraphTraversal.next();
            properties = retrieveAllProperties(vertex);
        }

        LineageVertex lineageVertex = new LineageVertex();
        lineageVertex.setProperties(properties);
        commitTransaction(g);
        return lineageVertex;
    }

    private Map<String, String> getTopicProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();

        Iterator<Vertex> eventTypeList = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(EVENT_TYPE_LIST));
        commitTransaction(g);
        if (eventTypeList.hasNext()) {
            properties.put(EVENT_TYPE_LIST_KEY, getDisplayNameForVertex(eventTypeList.next()));
        }

        return properties;
    }

    private Map<String, String> getEventSchemaAttributeProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        Iterator<Vertex> eventType = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(EVENT_TYPE));
        commitTransaction(g);
        if (eventType.hasNext()) {
            properties.put(EVENT_TYPE_KEY, getDisplayNameForVertex(eventType.next()));
        }

        Iterator<Vertex> eventTypeList = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2).or(hasLabel(EVENT_TYPE_LIST));
        commitTransaction(g);
        if (eventTypeList.hasNext()) {
            properties.put(EVENT_TYPE_LIST_KEY, getDisplayNameForVertex(eventTypeList.next()));
        }

        Iterator<Vertex> topic = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(3).or(hasLabel(TOPIC));
        commitTransaction(g);
        if (topic.hasNext()) {
            properties.put(TOPIC_KEY, getDisplayNameForVertex(topic.next()));
        }

        return properties;
    }
}
