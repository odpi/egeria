/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.*;


/**
 * Verify how the lineage walker derives the relationships across a boundary.  These tests drive the walker with an
 * in-memory graph and owner function, so they need no repository.
 */
public class LineageWalkerTest
{
    private static final String iscA = "ISC:A";
    private static final String iscB = "ISC:B";

    private static final String dataFlow    = OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName;
    private static final String processCall = OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName;
    private static final String controlFlow = OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName;
    private static final String dependency  = OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName;

    private final Map<String, Set<String>> ownersByElement = new HashMap<>();
    private final LineageOwners            owners          = elementGUID -> ownersByElement.getOrDefault(elementGUID, Set.of());


    /**
     * TestNG runs every test on the same instance, so the owners each test sets up are cleared before the next.
     */
    @BeforeMethod public void clearOwners()
    {
        ownersByElement.clear();
    }


    /**
     * Add a lineage relationship from supplier to consumer.
     *
     * @param graph graph to add to
     * @param supplierGUID supplier
     * @param consumerGUID consumer
     * @param iscQualifiedName supply chain - may be null
     */
    private void addEdge(LineageGraph graph,
                         String       supplierGUID,
                         String       consumerGUID,
                         String       iscQualifiedName)
    {
        addEdge(graph, dataFlow, supplierGUID, consumerGUID, iscQualifiedName);
    }


    /**
     * Add a lineage relationship of a particular type from supplier to consumer.
     *
     * @param graph graph to add to
     * @param typeName relationship type
     * @param supplierGUID supplier
     * @param consumerGUID consumer
     * @param iscQualifiedName supply chain - may be null
     */
    private void addEdge(LineageGraph graph,
                         String       typeName,
                         String       supplierGUID,
                         String       consumerGUID,
                         String       iscQualifiedName)
    {
        graph.addEdge(new LineageEdge(supplierGUID + "->" + consumerGUID, typeName, supplierGUID, consumerGUID, iscQualifiedName));
    }


    /**
     * Return the relationships of one type from the walker's results.
     *
     * @param derivation results
     * @param typeName relationship type
     * @return relationships of that type - empty if none
     */
    private Set<LinkKey> ofType(LineageDerivation derivation,
                                String            typeName)
    {
        return derivation.getLinks(typeName);
    }


    /**
     * Return the unique identifiers of the untagged relationships recorded for a derived relationship.
     *
     * @param derivation results
     * @param typeName relationship type
     * @param link derived relationship
     * @return relationship GUIDs (built as supplier->consumer by addEdge)
     */
    private Set<String> untaggedFor(LineageDerivation derivation,
                                    String            typeName,
                                    LinkKey           link)
    {
        Set<String> guids = new HashSet<>();

        for (LineageEdge edge : derivation.getUntaggedEdges(typeName, link))
        {
            guids.add(edge.relationshipGUID());
        }

        return guids;
    }


    /**
     * A direct relationship between elements with different owners is lifted to their owners, with its supply chain.
     */
    @Test public void testDirectLineage() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "column1", "column2", iscA);
        ownersByElement.put("column1", Set.of("asset1"));
        ownersByElement.put("column2", Set.of("asset2"));

        Set<LinkKey> links = ofType(new LineageWalker(20).deriveLinks(LineageBoundary.SCHEMA_DETAIL_TO_DATA_FABRIC, graph, owners, graph.getSupplierGUIDs()), dataFlow);

        assertEquals(links, Set.of(new LinkKey("asset1", "asset2", iscA)));
    }


    /**
     * Lineage between two elements with the same owner is not a relationship of the owner with itself, and an
     * element with no owner starts nothing.
     */
    @Test public void testSameOwnerAndNoOwner() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "column1", "column2", iscA);
        addEdge(graph, "orphan", "column2", iscA);
        ownersByElement.put("column1", Set.of("asset1"));
        ownersByElement.put("column2", Set.of("asset1"));

        Set<LinkKey> links = ofType(new LineageWalker(20).deriveLinks(LineageBoundary.SCHEMA_DETAIL_TO_DATA_FABRIC, graph, owners, graph.getSupplierGUIDs()), dataFlow);

        assertTrue(links.isEmpty());
    }


    /**
     * The schema detail boundary only counts a direct mapping: it does not pass through an element with no owner,
     * and such a path is not reported as abandoned.
     */
    @Test public void testSchemaDetailDoesNotPassThrough() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "column1", "process", iscA);
        addEdge(graph, "process", "column2", iscA);
        ownersByElement.put("column1", Set.of("asset1"));
        ownersByElement.put("column2", Set.of("asset2"));

        LineageWalker walker = new LineageWalker(20);
        Set<LinkKey>  links  = ofType(walker.deriveLinks(LineageBoundary.SCHEMA_DETAIL_TO_DATA_FABRIC, graph, owners, graph.getSupplierGUIDs()), dataFlow);

        assertTrue(links.isEmpty());
        assertEquals(walker.getAbandonedPaths(), 0);
    }


    /**
     * A data fabric path passes through intermediate elements and stops at the first owned element: the relationship
     * with whatever lies beyond belongs to the owner reached.
     */
    @Test public void testIndirectPathStopsAtFirstOwner() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "process", iscA);
        addEdge(graph, "process", "asset2", iscA);
        addEdge(graph, "asset2", "asset3", iscA);
        ownersByElement.put("asset1", Set.of("server1"));
        ownersByElement.put("asset2", Set.of("server2"));
        ownersByElement.put("asset3", Set.of("server3"));

        Set<LinkKey> links = ofType(new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1")), dataFlow);

        assertEquals(links, Set.of(new LinkKey("server1", "server2", iscA)));
    }


    /**
     * A path keeps to the supply chain of its first relationship.
     */
    @Test public void testPathKeepsToOneSupplyChain() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "process", iscA);
        addEdge(graph, "process", "asset2", iscA);
        addEdge(graph, "process", "asset3", iscB);
        ownersByElement.put("asset1", Set.of("product1"));
        ownersByElement.put("asset2", Set.of("product2"));
        ownersByElement.put("asset3", Set.of("product3"));

        Set<LinkKey> links = ofType(new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_DATA_MESH, graph, owners, List.of("asset1")), dependency);

        assertEquals(links, Set.of(new LinkKey("product2", "product1", iscA)));
    }


    /**
     * The data mesh has the dependent product - the one the data reaches - at end 1, and an element that belongs to
     * several owners gives a relationship for each.
     */
    @Test public void testDependentAtEnd1AndSeveralOwners() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "asset2", null);
        ownersByElement.put("asset1", Set.of("product1"));
        ownersByElement.put("asset2", Set.of("product2", "product3"));

        Set<LinkKey> links = ofType(new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_DATA_MESH, graph, owners, List.of("asset1")), dependency);

        assertEquals(links, Set.of(new LinkKey("product2", "product1", null),
                                   new LinkKey("product3", "product1", null)));
    }


    /**
     * A cycle in the lineage is not followed for ever.
     */
    @Test public void testCycle() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "processA", iscA);
        addEdge(graph, "processA", "processB", iscA);
        addEdge(graph, "processB", "processA", iscA);
        ownersByElement.put("asset1", Set.of("server1"));

        LineageWalker walker = new LineageWalker(20);
        Set<LinkKey>  links  = ofType(walker.deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1")), dataFlow);

        assertTrue(links.isEmpty());
        assertEquals(walker.getAbandonedPaths(), 0);
    }


    /**
     * A path that has not reached an owner within maxLineageDepth is abandoned and counted.
     */
    @Test public void testDepthLimit() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "step1", iscA);
        addEdge(graph, "step1", "step2", iscA);
        addEdge(graph, "step2", "asset2", iscA);
        ownersByElement.put("asset1", Set.of("server1"));
        ownersByElement.put("asset2", Set.of("server2"));

        LineageWalker shortWalker = new LineageWalker(2);

        assertTrue(ofType(shortWalker.deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1")), dataFlow).isEmpty());
        assertEquals(shortWalker.getAbandonedPaths(), 1);

        LineageWalker longWalker = new LineageWalker(3);

        assertEquals(ofType(longWalker.deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1")), dataFlow),
                     Set.of(new LinkKey("server1", "server2", iscA)));
        assertEquals(longWalker.getAbandonedPaths(), 0);
    }


    /**
     * In the system fabric, the relationship that reaches the owned element decides the type proved: a ProcessCall
     * proves a ProcessCall, and other data lineage - even with a ProcessCall earlier on the path - a DataFlow.
     */
    @Test public void testSystemFabricTypeFromArrivingRelationship() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, processCall, "process1", "process2", iscA);
        addEdge(graph, processCall, "process1", "process3", iscA);
        addEdge(graph, dataFlow, "process3", "asset4", iscA);
        ownersByElement.put("process1", Set.of("server1"));
        ownersByElement.put("process2", Set.of("server2"));
        ownersByElement.put("asset4", Set.of("server4"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("process1"));

        assertEquals(ofType(links, processCall), Set.of(new LinkKey("server1", "server2", iscA)));
        assertEquals(ofType(links, dataFlow), Set.of(new LinkKey("server1", "server4", iscA)));
        assertTrue(ofType(links, controlFlow).isEmpty());
    }


    /**
     * ControlFlow relationships form paths of their own in the system fabric: they prove a ControlFlow, and they do not
     * join a path of data lineage, nor data lineage a path of control flow.
     */
    @Test public void testControlFlowPathsAreSeparate() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, controlFlow, "process1", "process2", iscA);
        addEdge(graph, controlFlow, "process1", "step", iscA);
        addEdge(graph, dataFlow, "step", "asset3", iscA);
        addEdge(graph, dataFlow, "process1", "hop", iscA);
        addEdge(graph, controlFlow, "hop", "process4", iscA);
        ownersByElement.put("process1", Set.of("server1"));
        ownersByElement.put("process2", Set.of("server2"));
        ownersByElement.put("asset3", Set.of("server3"));
        ownersByElement.put("process4", Set.of("server4"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("process1"));

        assertEquals(ofType(links, controlFlow), Set.of(new LinkKey("server1", "server2", iscA)));
        assertTrue(ofType(links, dataFlow).isEmpty());
    }


    /**
     * Whatever relationship reaches a product's asset, the data mesh proves a DigitalProductDependency.
     */
    @Test public void testDataMeshProvesDependencyFromAnyDataLineage() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, processCall, "asset1", "asset2", iscA);
        ownersByElement.put("asset1", Set.of("product1"));
        ownersByElement.put("asset2", Set.of("product2"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_DATA_MESH, graph, owners, List.of("asset1"));

        assertEquals(links.getLinkTypeNames(), Set.of(dependency));
        assertEquals(ofType(links, dependency), Set.of(new LinkKey("product2", "product1", iscA)));
    }


    /**
     * The boundaries map the lower-layer types as the design says.  The data mesh never follows ControlFlow - its
     * graph is not loaded with it.
     */
    @Test public void testBoundaryTypeMapping()
    {
        assertEquals(LineageBoundary.SCHEMA_DETAIL_TO_DATA_FABRIC.getUpperTypeName(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName), dataFlow);
        assertEquals(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC.getUpperTypeName(dataFlow), dataFlow);
        assertEquals(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC.getUpperTypeName(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName), dataFlow);
        assertEquals(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC.getUpperTypeName(processCall), processCall);
        assertEquals(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC.getUpperTypeName(controlFlow), controlFlow);
        assertEquals(LineageBoundary.DATA_FABRIC_TO_DATA_MESH.getUpperTypeName(processCall), dependency);
        assertFalse(LineageBoundary.DATA_FABRIC_TO_DATA_MESH.lowerLayerRelationshipTypeNames.contains(controlFlow));
        assertTrue(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC.lowerLayerRelationshipTypeNames.contains(controlFlow));
    }


    /**
     * An untagged relationship is "supply chain unknown": it joins a path, and a tagged relationship later on the path
     * fixes the supply chain for the whole path.  The untagged relationship is recorded against the derived
     * relationship so that it can be given that supply chain.
     */
    @Test public void testUntaggedRelationshipJoinsTaggedPath() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "process", null);
        addEdge(graph, "process", "asset2", iscA);
        ownersByElement.put("asset1", Set.of("server1"));
        ownersByElement.put("asset2", Set.of("server2"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1"));
        LinkKey           link  = new LinkKey("server1", "server2", iscA);

        assertEquals(ofType(links, dataFlow), Set.of(link));
        assertEquals(untaggedFor(links, dataFlow, link), Set.of("asset1->process"));
    }


    /**
     * An untagged relationship followed by relationships of two supply chains is on the paths of both, and is
     * recorded against both derived relationships.
     */
    @Test public void testUntaggedRelationshipOnPathsOfTwoSupplyChains() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "process", null);
        addEdge(graph, "process", "asset2", iscA);
        addEdge(graph, "process", "asset3", iscB);
        ownersByElement.put("asset1", Set.of("product1"));
        ownersByElement.put("asset2", Set.of("product2"));
        ownersByElement.put("asset3", Set.of("product3"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_DATA_MESH, graph, owners, List.of("asset1"));
        LinkKey           linkA = new LinkKey("product2", "product1", iscA);
        LinkKey           linkB = new LinkKey("product3", "product1", iscB);

        assertEquals(ofType(links, dependency), Set.of(linkA, linkB));
        assertEquals(untaggedFor(links, dependency, linkA), Set.of("asset1->process"));
        assertEquals(untaggedFor(links, dependency, linkB), Set.of("asset1->process"));
    }


    /**
     * A path that is untagged throughout proves a relationship of unknown supply chain, and every relationship on it
     * - along every route to the owned element - is recorded, ready for a supply chain to be found from above.
     */
    @Test public void testUntaggedPathRecordsEveryRoute() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "processA", null);
        addEdge(graph, "asset1", "processB", null);
        addEdge(graph, "processA", "asset2", null);
        addEdge(graph, "processB", "asset2", null);
        ownersByElement.put("asset1", Set.of("server1"));
        ownersByElement.put("asset2", Set.of("server2"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1"));
        LinkKey           link  = new LinkKey("server1", "server2", null);

        assertEquals(ofType(links, dataFlow), Set.of(link));
        assertEquals(untaggedFor(links, dataFlow, link),
                     Set.of("asset1->processA", "asset1->processB", "processA->asset2", "processB->asset2"));
    }


    /**
     * Once a path's supply chain is fixed, an untagged relationship can still join it, but a relationship of another
     * supply chain cannot - and a tagged path records no untagged relationships it did not use.
     */
    @Test public void testTaggedPathTakesUntaggedButNotOtherSupplyChain() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        addEdge(graph, "asset1", "process", iscA);
        addEdge(graph, "process", "step", null);
        addEdge(graph, "step", "asset2", iscA);
        addEdge(graph, "step", "asset3", iscB);
        ownersByElement.put("asset1", Set.of("server1"));
        ownersByElement.put("asset2", Set.of("server2"));
        ownersByElement.put("asset3", Set.of("server3"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_SYSTEM_FABRIC, graph, owners, List.of("asset1"));
        LinkKey           link  = new LinkKey("server1", "server2", iscA);

        assertEquals(ofType(links, dataFlow), Set.of(link));
        assertEquals(untaggedFor(links, dataFlow, link), Set.of("process->step"));
    }


    /**
     * A product whose member is a data set reaches the lineage of the data set's content: the walk starts from the
     * content as well as the data set, and an element reached is owned by the product of a data set over it.
     */
    @Test public void testDataSetIsEquivalentToItsContent() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        graph.addEquivalence("dataSet1", "table1", null);
        graph.addEquivalence("dataSet2", "table2", null);
        addEdge(graph, "table1", "process", iscA);
        addEdge(graph, "process", "table2", iscA);
        addEdge(graph, "table2", "asset3", iscA);
        ownersByElement.put("dataSet1", Set.of("product1"));
        ownersByElement.put("dataSet2", Set.of("product2"));
        ownersByElement.put("asset3", Set.of("product3"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_DATA_MESH, graph, owners, List.of("dataSet1"));

        /*
         * The path stops at the content of product2's data set, so product3 depends on product2, not product1.
         */
        assertEquals(ofType(links, dependency), Set.of(new LinkKey("product2", "product1", iscA)));
    }


    /**
     * An equivalence that names a supply chain only holds for the paths of that supply chain.  A path of another supply
     * chain goes past the content; a path of unknown supply chain reaches it and takes the equivalence's supply chain.
     */
    @Test public void testEquivalenceKeepsToItsSupplyChain() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        graph.addEquivalence("dataSet2", "table2", iscB);
        addEdge(graph, "asset1", "table2", iscA);
        addEdge(graph, "asset4", "table2", null);
        ownersByElement.put("asset1", Set.of("product1"));
        ownersByElement.put("asset4", Set.of("product4"));
        ownersByElement.put("dataSet2", Set.of("product2"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.DATA_FABRIC_TO_DATA_MESH, graph, owners, List.of("asset1", "asset4"));

        assertEquals(ofType(links, dependency), Set.of(new LinkKey("product2", "product4", iscB)));
        assertEquals(untaggedFor(links, dependency, new LinkKey("product2", "product4", iscB)), Set.of("asset4->table2"));
    }


    /**
     * The schema detail boundary does not follow DataSetContent: its owners are the data assets of schema elements.
     */
    @Test public void testSchemaDetailIgnoresEquivalences() throws Exception
    {
        LineageGraph graph = new LineageGraph();

        graph.addEquivalence("dataSet2", "column2", null);
        addEdge(graph, "column1", "column2", iscA);
        ownersByElement.put("column1", Set.of("asset1"));
        ownersByElement.put("dataSet2", Set.of("asset2"));

        LineageDerivation links = new LineageWalker(20).deriveLinks(LineageBoundary.SCHEMA_DETAIL_TO_DATA_FABRIC, graph, owners, graph.getSupplierGUIDs());

        assertTrue(ofType(links, dataFlow).isEmpty());
    }
}
