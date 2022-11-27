/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.NodeNamesSearchCriteria;
import org.odpi.openmetadata.governanceservers.openlineage.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.openlineage.requests.HierarchyType;
import org.odpi.openmetadata.governanceservers.openlineage.requests.LineageSearchRequest;
import org.odpi.openmetadata.governanceservers.openlineage.requests.Node;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageNodeNamesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageSearchResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageTypesResponse;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.GraphHelper;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphQueryService;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATABASE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DEPLOYED_DB_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FILE_FOLDER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.NESTED_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_DB_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TERM_ANCHOR;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TERM_CATEGORIZATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_COLUMN_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_RELATED_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TABLE_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_NODE_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PROCESS_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;

public class LineageGraphQueryServiceTest {

    static LineageGraphQueryService lineageGraphQueryService;
    private static final String CONNECTOR_PROVIDER_NAME = "org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorProvider";

    @BeforeAll
    public static void beforeClass() throws JanusConnectorException, OpenLineageException {
        GraphHelper graphHelper = new GraphHelper();
        graphHelper.openGraph(CONNECTOR_PROVIDER_NAME, Collections.singletonMap("storage.backend", "inmemory"), null);
        lineageGraphQueryService = new LineageGraphQueryService(graphHelper, null);
        addColumnLineageData(graphHelper.getGraphTraversalSource());
        addGlossaryLineageData(graphHelper.getGraphTraversalSource());
        addTableLineageData(graphHelper.getGraphTraversalSource());
        addHierarchyData(graphHelper.getGraphTraversalSource());
    }

    @Test
    void ultimateSourceColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c32";
        expectedNodeIDs.add("c11");
        expectedNodeIDs.add("c12");
        expectedNodeIDs.add(queriedNodeID);
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.ultimateSource(queriedNodeID).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void ultimateDestinationColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c11";
        expectedNodeIDs.add("c41");
        expectedNodeIDs.add("c42");
        expectedNodeIDs.add(queriedNodeID);
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.ultimateDestination(queriedNodeID).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void endToEndColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c22";
        expectedNodeIDs.add("c11");
        expectedNodeIDs.add("c12");
        expectedNodeIDs.add("c21");
        expectedNodeIDs.add("c22");
        expectedNodeIDs.add("c31");
        expectedNodeIDs.add("c32");
        expectedNodeIDs.add("c41");
        expectedNodeIDs.add("c42");
        expectedNodeIDs.add("sp1");
        expectedNodeIDs.add("sp2");
        expectedNodeIDs.add("sp3");
        expectedNodeIDs.add("sp4");

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.endToEnd(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void verticalLineage() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "g2";
        expectedNodeIDs.add("g1");
        expectedNodeIDs.add("g2");
        expectedNodeIDs.add("g3");
        expectedNodeIDs.add("c2");

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.verticalLineage(queriedNodeID).get();

        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        lineageVerticesAndEdges.getLineageVertices().forEach(s -> System.out.println(s.getNodeID()));

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void ultimateSourceTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t1";
        expectedNodeIDs.add("d1");
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.ultimateSource(queriedNodeID).get();
        System.out.println(lineageVerticesAndEdges);
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void ultimateDestinationTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t10";
        expectedNodeIDs.add("t20");
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.ultimateDestination(queriedNodeID).get();

        System.out.println(lineageVerticesAndEdges);
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void endToEndTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t1";
        expectedNodeIDs.add("d1");
        expectedNodeIDs.add("p1");
        expectedNodeIDs.add("p2");
        expectedNodeIDs.add("t2");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.endToEnd(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void endToEndTableLevelProcess() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "p10";
        expectedNodeIDs.add("d10");
        expectedNodeIDs.add("p20");
        expectedNodeIDs.add("t10");
        expectedNodeIDs.add("t20");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.endToEnd(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void ultimateSourceTableLevelViaLineageMapping() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t10";
        expectedNodeIDs.add("d10");
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.ultimateSource(queriedNodeID).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void ultimateDestinationTableLevelViaLineageMapping() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t10";
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);
        expectedNodeIDs.add("t20");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.ultimateDestination(queriedNodeID).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    void endToEndTableLevelViaLineageMapping() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t10";
        expectedNodeIDs.add("d10");
        expectedNodeIDs.add("p10");
        expectedNodeIDs.add("t20");
        expectedNodeIDs.add("p20");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.endToEnd(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void testGetNodes_AGlossaryTerm() {
        NodeNamesSearchCriteria searchCriteria = new NodeNamesSearchCriteria(GLOSSARY_TERM, "g1", 10);
        List<String> expectedNodeNames = Collections.singletonList("g1");
        LineageNodeNamesResponse response = lineageGraphQueryService.getNodes(searchCriteria);
        assertTrue(CollectionUtils.isEqualCollection(response.getNames(), expectedNodeNames));
    }

    @Test
    public void testGetNodes_MultipleGlossaryTerm() {
        NodeNamesSearchCriteria searchCriteria = new NodeNamesSearchCriteria(GLOSSARY_TERM, "g", 10);
        List<String> expectedNodeNames = Arrays.asList("g1", "g2", "g3", "glossaryTerm");
        List<String> resultNames = lineageGraphQueryService.getNodes(searchCriteria).getNames();
        assertTrue(CollectionUtils.isEqualCollection(expectedNodeNames, resultNames));
    }

    @Test
    public void testGetHierarchy_upward_tabularColumn() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.UPWARD);
        elementHierarchyRequest.setGuid("tabularColumn");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "tabularColumn";
        expectedNodeIDs.add("tabularSchemaType");
        expectedNodeIDs.add("dataFile");
        expectedNodeIDs.add("fileFolder");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_all_tabularColumn() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.ALL);
        elementHierarchyRequest.setGuid("tabularColumn");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "tabularColumn";
        expectedNodeIDs.add("tabularSchemaType");
        expectedNodeIDs.add("dataFile");
        expectedNodeIDs.add("fileFolder");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_downward_tabularColumn() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.DOWNWARD);
        elementHierarchyRequest.setGuid("tabularColumn");

        LineageVerticesAndEdges lineageVerticesAndEdges =
                lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        assertTrue(CollectionUtils.isEmpty(lineageVertices));
    }
    @Test
    public void testGetHierarchy_upward_dataFile() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.UPWARD);
        elementHierarchyRequest.setGuid("dataFile");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "dataFile";
        expectedNodeIDs.add("fileFolder");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_all_dataFile() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.ALL);
        elementHierarchyRequest.setGuid("dataFile");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "dataFile";
        expectedNodeIDs.add("tabularSchemaType");
        expectedNodeIDs.add("tabularColumn");
        expectedNodeIDs.add("fileFolder");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_downward_dataFile() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.DOWNWARD);
        elementHierarchyRequest.setGuid("dataFile");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "dataFile";
        expectedNodeIDs.add("tabularSchemaType");
        expectedNodeIDs.add("tabularColumn");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_upward_relationalColumn() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.UPWARD);
        elementHierarchyRequest.setGuid("relationalColumn");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "relationalColumn";
        expectedNodeIDs.add("relationalTable");
        expectedNodeIDs.add("relationalDBSchemaType");
        expectedNodeIDs.add("deployedDBSchemaType");
        expectedNodeIDs.add("database");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_all_relationalColumn() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.ALL);
        elementHierarchyRequest.setGuid("relationalColumn");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "relationalColumn";
        expectedNodeIDs.add("relationalTable");
        expectedNodeIDs.add("relationalDBSchemaType");
        expectedNodeIDs.add("deployedDBSchemaType");
        expectedNodeIDs.add("database");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_downward_relationalColumn() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.DOWNWARD);
        elementHierarchyRequest.setGuid("relationalColumn");

        LineageVerticesAndEdges lineageVerticesAndEdges =
                lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        assertTrue(CollectionUtils.isEmpty(lineageVertices));
    }
    @Test
    public void testGetHierarchy_upward_relationalTable() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.UPWARD);
        elementHierarchyRequest.setGuid("relationalTable");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "relationalTable";
        expectedNodeIDs.add("relationalDBSchemaType");
        expectedNodeIDs.add("deployedDBSchemaType");
        expectedNodeIDs.add("database");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_all_relationalTable() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.ALL);
        elementHierarchyRequest.setGuid("relationalTable");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "relationalTable";
        expectedNodeIDs.add("relationalColumn");
        expectedNodeIDs.add("relationalDBSchemaType");
        expectedNodeIDs.add("deployedDBSchemaType");
        expectedNodeIDs.add("database");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_downward_relationalTable() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.DOWNWARD);
        elementHierarchyRequest.setGuid("relationalTable");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "relationalTable";
        expectedNodeIDs.add("relationalColumn");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void testGetHierarchy_upward_glossaryTerm() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.UPWARD);
        elementHierarchyRequest.setGuid("glossaryTerm");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "glossaryTerm";
        expectedNodeIDs.add("glossaryCategory");
        expectedNodeIDs.add("glossary");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_all_glossaryTerm() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.ALL);
        elementHierarchyRequest.setGuid("glossaryTerm");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "glossaryTerm";
        expectedNodeIDs.add("relationalColumn");
        expectedNodeIDs.add("tabularColumn");
        expectedNodeIDs.add("glossaryCategory");
        expectedNodeIDs.add("glossary");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetHierarchy_downward_glossaryTerm() {
        ElementHierarchyRequest elementHierarchyRequest = new ElementHierarchyRequest();
        elementHierarchyRequest.setHierarchyType(HierarchyType.DOWNWARD);
        elementHierarchyRequest.setGuid("glossaryTerm");

        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "glossaryTerm";
        expectedNodeIDs.add("relationalColumn");
        expectedNodeIDs.add("tabularColumn");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = lineageGraphQueryService.getElementHierarchy(elementHierarchyRequest).getLineageVerticesAndEdges();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }
    @Test
    public void testGetNodes_NoResult() {
        NodeNamesSearchCriteria searchCriteria = new NodeNamesSearchCriteria(GLOSSARY_TERM, "gg", 2);
        List<String> resultNames = lineageGraphQueryService.getNodes(searchCriteria).getNames();
        assertEquals(0, resultNames.size());
    }

    @Test
    public void testGetTypes() {
        LineageTypesResponse response = lineageGraphQueryService.getTypes();
        List<String> expectedTypes = Arrays.asList(TABULAR_COLUMN, GLOSSARY_TERM, RELATIONAL_TABLE, PROCESS, DATA_FILE, FILE_FOLDER,
                RELATIONAL_DB_SCHEMA_TYPE, RELATIONAL_COLUMN, GLOSSARY_CATEGORY, TABULAR_SCHEMA_TYPE, GLOSSARY, DATABASE, DEPLOYED_DB_SCHEMA_TYPE);
        assertTrue(CollectionUtils.isEqualCollection(response.getTypes(), expectedTypes));
    }

    @Test
    void searchTest() {
        int expectedSearchResults = 5;
        LineageSearchRequest lineageSearchRequest = new LineageSearchRequest();
        Node node = new Node();
        node.setType(RELATIONAL_TABLE);
        lineageSearchRequest.setQueriedNode(node);
        LineageSearchResponse result = lineageGraphQueryService.search(lineageSearchRequest);
        assertNotNull(result.getVertices());
        assertEquals(expectedSearchResults, result.getVertices().size());
        result.getVertices().forEach(vertex -> assertEquals(RELATIONAL_TABLE, vertex.getNodeType()));
    }

    @Test
    public void testGetNodes_MultipleGlossaryTermLimited() {
        NodeNamesSearchCriteria searchCriteria = new NodeNamesSearchCriteria(GLOSSARY_TERM, "g", 2);
        List<String> possibleResults = Arrays.asList("g1", "g2", "g3", "glossaryTerm");
        List<String> resultNames = lineageGraphQueryService.getNodes(searchCriteria).getNames();
        assertEquals(2, resultNames.size());
        assertTrue(CollectionUtils.containsAll(possibleResults, resultNames));
    }

    private void validateResponse(HashSet<String> expectedNodeIDs, Set<LineageVertex> lineageVertices) {
        assertEquals(expectedNodeIDs.size(), lineageVertices.size());
        for (LineageVertex returnedVertex : lineageVertices) {
            assertTrue(expectedNodeIDs.contains(returnedVertex.getNodeID()));
        }
    }

    private static void addGlossaryLineageData(GraphTraversalSource g) {
        Vertex g1 = getVertex(g, GLOSSARY_TERM, "g1", "g1");
        Vertex g2 = getVertex(g, GLOSSARY_TERM, "g2", "g2");
        Vertex g3 = getVertex(g, GLOSSARY_TERM, "g3", "g3");

        Vertex c1 = getVertex(g, TABULAR_COLUMN, "c1", "c1");
        Vertex c2 = getVertex(g, TABULAR_COLUMN, "c2", "c2");
        Vertex c3 = getVertex(g, TABULAR_COLUMN, "c3", "c3");

        g.addE(EDGE_LABEL_RELATED_TERM).from(g1).to(g2).next();
        g.addE(EDGE_LABEL_RELATED_TERM).from(g2).to(g3).next();
        g.addE(EDGE_LABEL_RELATED_TERM).from(g3).to(g1).next();

        g.addE(EDGE_LABEL_SEMANTIC_ASSIGNMENT).from(c1).to(g1).next();
        g.addE(EDGE_LABEL_SEMANTIC_ASSIGNMENT).from(c2).to(g2).next();
        g.addE(EDGE_LABEL_SEMANTIC_ASSIGNMENT).from(c3).to(g3).next();
    }

    private static void addColumnLineageData(GraphTraversalSource g) {
        Vertex c11 = getVertex(g, TABULAR_COLUMN, "c11", "c11");
        Vertex c12 = getVertex(g, TABULAR_COLUMN, "c12", "c12");
        Vertex c21 = getVertex(g, TABULAR_COLUMN, "c21", "c21");
        Vertex c22 = getVertex(g, TABULAR_COLUMN, "c22", "c22");
        Vertex c31 = getVertex(g, TABULAR_COLUMN, "c31", "c31");
        Vertex c32 = getVertex(g, TABULAR_COLUMN, "c32", "c32");
        Vertex c41 = getVertex(g, TABULAR_COLUMN, "c41", "c41");
        Vertex c42 = getVertex(g, TABULAR_COLUMN, "c42", "c42");

        Vertex sp1 = getVertex(g, NODE_LABEL_SUB_PROCESS, "sp1", "sp1");
        Vertex sp2 = getVertex(g, NODE_LABEL_SUB_PROCESS, "sp2", "sp2");
        Vertex sp3 = getVertex(g, NODE_LABEL_SUB_PROCESS, "sp3", "sp3");
        Vertex sp4 = getVertex(g, NODE_LABEL_SUB_PROCESS, "sp4", "sp4");

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c11).to(sp1).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c12).to(sp1).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp1).to(c21).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp1).to(c21).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c21).to(sp2).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c22).to(sp2).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp2).to(c31).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp2).to(c32).next();

        //sp3 branch causes the cycle
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c31).to(sp3).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c32).to(sp3).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp3).to(c21).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp3).to(c22).next();

        //sp4 branch leads to the destination
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c31).to(sp4).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c32).to(sp4).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp4).to(c41).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(sp4).to(c42).next();
    }

    private static void addTableLineageData(GraphTraversalSource g) {
        // lineage via data flow
        Vertex d1 = getVertex(g, DATA_FILE, "d1", "d1");
        Vertex t1 = getVertex(g, RELATIONAL_TABLE, "t1", "t1");
        Vertex t2 = getVertex(g, RELATIONAL_TABLE, "t2", "t2");

        Vertex p1 = getVertex(g, PROCESS, "p1", "p1");
        Vertex p2 = getVertex(g, PROCESS, "p2", "p2");

        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(d1).to(p1).next();
        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(p1).to(t1).next();

        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(t1).to(p2).next();
        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(p2).to(t2).next();

        // lineage via lineage mapping
        Vertex d10 = getVertex(g, DATA_FILE, "d10", "d10");
        Vertex p10 = getVertex(g, PROCESS, "p10", "p10");
        Vertex t10 = getVertex(g, RELATIONAL_TABLE, "t10", "t10");
        Vertex p20 = getVertex(g, PROCESS, "p20", "p20");
        Vertex t20 = getVertex(g, RELATIONAL_TABLE, "t20", "t20");

        g.addE(LINEAGE_MAPPING).from(d10).to(p10).next();
        g.addE(LINEAGE_MAPPING).from(p10).to(t10).next();
        g.addE(LINEAGE_MAPPING).from(t10).to(p20).next();
        g.addE(LINEAGE_MAPPING).from(p20).to(t20).next();
    }

    private static void addHierarchyData(GraphTraversalSource g) {
        // relational relationalTable  & relational relationalColumn context
        Vertex relationalTable = getVertex(g, RELATIONAL_TABLE, "relationalTable", "relationalTable");
        Vertex database = getVertex(g, DATABASE, "database", "database");
        Vertex deployedDBSchemaType = getVertex(g, DEPLOYED_DB_SCHEMA_TYPE, "relationalDBSchemaType", "relationalDBSchemaType");
        Vertex relationalDbSchemaType = getVertex(g, RELATIONAL_DB_SCHEMA_TYPE, "deployedDBSchemaType", "deployedDBSchemaType");
        Vertex relationalColumn = getVertex(g, RELATIONAL_COLUMN, "relationalColumn", "relationalColumn");

        g.addE(ATTRIBUTE_FOR_SCHEMA).from(relationalTable).to(relationalDbSchemaType).next();
        g.addE(ASSET_SCHEMA_TYPE).from(relationalDbSchemaType).to(deployedDBSchemaType).next();
        g.addE(DATA_CONTENT_FOR_DATA_SET).from(deployedDBSchemaType).to(database).next();
        g.addE(NESTED_SCHEMA_ATTRIBUTE).from(relationalColumn).to(relationalTable).next();

        // data file  & tabular relationalColumn context
        Vertex file = getVertex(g, DATA_FILE, "dataFile", "dataFile");
        Vertex fileFolder = getVertex(g, FILE_FOLDER, "fileFolder", "fileFolder");
        Vertex schemaType = getVertex(g, TABULAR_SCHEMA_TYPE, "tabularSchemaType", "tabularSchemaType");
        Vertex tabularColumn = getVertex(g, TABULAR_COLUMN, "tabularColumn", "tabularColumn");

        g.addE(NESTED_FILE).from(file).to(fileFolder).next();
        g.addE(ATTRIBUTE_FOR_SCHEMA).from(tabularColumn).to(schemaType).next();
        g.addE(ASSET_SCHEMA_TYPE).from(schemaType).to(file).next();

        // glossary term & glossary context
        Vertex glossaryTerm = getVertex(g, GLOSSARY_TERM, "glossaryTerm", "glossaryTerm");
        Vertex glossaryCategory = getVertex(g, GLOSSARY_CATEGORY, "glossaryCategory", "glossaryCategory");
        Vertex glossary = getVertex(g, GLOSSARY, "glossary", "glossary");

        g.addE(SEMANTIC_ASSIGNMENT).from(glossaryTerm).to(relationalColumn).next();
        g.addE(SEMANTIC_ASSIGNMENT).from(glossaryTerm).to(tabularColumn).next();
        g.addE(TERM_CATEGORIZATION).from(glossaryTerm).to(glossaryCategory).next();
        g.addE(TERM_ANCHOR).from(glossaryTerm).to(glossary).next();
    }

    private static Vertex getVertex(GraphTraversalSource g, String nodeType, String guid, String nodeId) {
        Vertex vertex;
        if (NODE_LABEL_SUB_PROCESS.equals(nodeType)) {
            vertex = g.addV(nodeType).property(PROPERTY_KEY_ENTITY_GUID, guid)
                    .property(PROPERTY_KEY_LABEL, nodeType)
                    .property(PROPERTY_KEY_PROCESS_GUID, guid)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID, nodeId)
                    .next();

        } else {
            vertex = g.addV(nodeType).property(PROPERTY_KEY_ENTITY_GUID, guid)
                    .property(PROPERTY_KEY_LABEL, nodeType)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID, nodeId)
                    .property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME, nodeId)
                    .next();
        }
        return vertex;
    }

}