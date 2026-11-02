/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.InformationSupplyChainLayerMermaidGraphBuilder.Layer;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * InformationSupplyChainLayerMermaidGraphBuilderTest covers how the implementation of an information supply chain
 * is divided into its layers, each drawn in its own graph.  The layer's elements sit in a subgraph, and the
 * solution components, digital products and anchors linked to them are drawn before the subgraph so that they sit
 * outside it.
 * <br><br>
 * Every test uses the same supply chain: a data flow from a file through a process to a data set, a data mapping
 * between two columns, a provider product packaging the data set and a consumer product depending on it, a
 * database manager running on a host, and solution components implemented by elements of each layer.
 */
public class InformationSupplyChainLayerMermaidGraphBuilderTest
{
    private static final String ISC_QUALIFIED_NAME = "isc-qualified-name";

    private final ElementStub sourceFile      = stub("file1", "Source File", "DataFile", "DataSet", "Asset");
    private final ElementStub loadProcess     = stub("process1", "Load Process", "Process", "Asset");
    private final ElementStub targetDataSet   = stub("dataset1", "Target Data Set", "DataSet", "Asset");
    private final ElementStub validateProcess = stub("process2", "Validate Process", "Process", "Asset");
    private final ElementStub orphanProcess   = stub("process3", "Orphan Process", "Process", "Asset");
    private final ElementStub reviewAction    = stub("action1", "Review Action", "Action");
    private final ElementStub sourceColumn    = stub("column1", "Source Column", "SchemaAttribute", "SchemaElement");
    private final ElementStub targetColumn    = stub("column2", "Target Column", "SchemaAttribute", "SchemaElement");
    private final ElementStub providerProduct = stub("product1", "Provider Product", "DigitalProduct", "Collection");
    private final ElementStub consumerProduct = stub("product2", "Consumer Product", "DigitalProduct", "Collection");
    private final ElementStub extractProduct  = stub("product3", "Extract Product", "DigitalProduct", "Collection");
    private final ElementStub databaseManager = stub("dbm1", "Database Manager", "DatabaseManager", "SoftwareCapability");
    private final ElementStub host            = stub("host1", "Host", "Host", "ITInfrastructure", "Asset");
    private final ElementStub loadComponent   = stub("sc1", "sc1-qualified-name", "SolutionComponent", "DesignModelElement");
    private final ElementStub productComponent = stub("sc2", "sc2-qualified-name", "SolutionComponent", "DesignModelElement");
    private final ElementStub systemComponent = stub("sc3", "sc3-qualified-name", "SolutionComponent", "DesignModelElement");
    private final ElementStub anchorComponent = stub("sc4", "sc4-qualified-name", "SolutionComponent", "DesignModelElement");


    /**
     * The data mesh holds the products and the dependency between them.  The solution component the product
     * implements is drawn outside the subgraph, and the data set the product packages belongs to the data fabric.
     */
    @Test
    public void dataMeshHoldsTheProductsAndTheirSolutionComponents()
    {
        String graph = render(Layer.DATA_MESH);

        assertTrue(graph.contains("title: Information Supply Chain Data Mesh - ISC [isc1]"));
        assertTrue(graph.contains("[Data Mesh]"));
        assertInside(graph, "Provider Product", "Data Mesh");
        assertInside(graph, "Consumer Product", "Data Mesh");
        assertOutside(graph, "Product Component", "Data Mesh");
        assertTrue(graph.contains("|\"Digital Product Dependency\"|"));
        assertTrue(graph.contains("|\"Implemented By\"|"), "the solution component is linked to its product");
        assertAbsent(graph, "Target Data Set", "Database Manager", "Load Component", "System Component", "Source Column");
    }


    /**
     * In the data mesh, the solution components and digital products that anchor the products are linked to them
     * with a thin line.  A solution component anchor sits outside the subgraph; a product anchor is already in it.
     */
    @Test
    public void dataMeshShowsSolutionComponentAndProductAnchors()
    {
        String graph = render(Layer.DATA_MESH);

        assertInside(graph, "Extract Product", "Data Mesh");
        assertOutside(graph, "Anchor Component", "Data Mesh");
        assertTrue(graph.contains(nodeId(graph, "Anchor Component") + "-->|\"Anchor for\"|" + nodeId(graph, "Consumer Product")));
        assertTrue(graph.contains(nodeId(graph, "Consumer Product") + "-->|\"Anchor for\"|" + nodeId(graph, "Extract Product")));
        assertEquals(count(graph, "Anchor for"), 2);
    }


    /**
     * The system fabric holds the infrastructure and software capabilities, plus the solution component they implement.
     */
    @Test
    public void systemFabricHoldsTheSystemsAndTheirSolutionComponents()
    {
        String graph = render(Layer.SYSTEM_FABRIC);

        assertInside(graph, "Database Manager", "System Fabric");
        assertInside(graph, "Host", "System Fabric");
        assertOutside(graph, "System Component", "System Fabric");
        assertTrue(graph.contains("|\"Lineage Mapping\"|"), "the database manager runs on the host");
        assertAbsent(graph, "Target Data Set", "Provider Product", "Load Component", "Product Component");
    }


    /**
     * In the system fabric, the solution component that anchors a system is linked to it with a thin line.
     */
    @Test
    public void systemFabricShowsSolutionComponentAnchors()
    {
        String graph = render(Layer.SYSTEM_FABRIC);

        assertOutside(graph, "Anchor Component", "System Fabric");
        assertTrue(graph.contains(nodeId(graph, "Anchor Component") + "-->|\"Anchor for\"|" + nodeId(graph, "Database Manager")));
        assertEquals(count(graph, "Anchor for"), 1);
    }


    /**
     * The data fabric holds the elements linked by data lineage, control flow and action requester relationships.
     * The product and solution component linked to them sit outside the subgraph.  The data mapping has its own
     * layer, and an element linked by any other relationship is left out.
     */
    @Test
    public void dataFabricHoldsTheDataLineage()
    {
        String graph = render(Layer.DATA_FABRIC);

        assertInside(graph, "Source File", "Data Fabric");
        assertInside(graph, "Load Process", "Data Fabric");
        assertInside(graph, "Target Data Set", "Data Fabric");
        assertOutside(graph, "Provider Product", "Data Fabric");
        assertOutside(graph, "Load Component", "Data Fabric");
        assertInside(graph, "Validate Process", "Data Fabric");
        assertInside(graph, "Review Action", "Data Fabric");
        assertEquals(count(graph, "|\"Data Flow\"|"), 2);
        assertTrue(graph.contains(nodeId(graph, "Load Process") + "==>|\"Control Flow\"|" + nodeId(graph, "Validate Process")));
        assertTrue(graph.contains(nodeId(graph, "Load Process") + "==>|\"Action Requester\"|" + nodeId(graph, "Review Action")));
        assertAbsent(graph, "Source Column", "Target Column", "Orphan Process", "Consumer Product", "Database Manager",
                     "Host", "Product Component", "System Component");
        assertFalse(graph.contains("Data Mapping"));
    }


    /**
     * In the data fabric, only the solution components that anchor its elements get the thin line.  A product that
     * anchors an element, and is in the graph anyway, keeps the dotted anchor line that all the graphs use.
     */
    @Test
    public void dataFabricShowsSolutionComponentAnchors()
    {
        String graph = render(Layer.DATA_FABRIC);

        assertOutside(graph, "Anchor Component", "Data Fabric");
        assertTrue(graph.contains(nodeId(graph, "Anchor Component") + "-->|\"Anchor for\"|" + nodeId(graph, "Review Action")));
        assertTrue(graph.contains(nodeId(graph, "Provider Product") + "-. \"Anchor for\" .->" + nodeId(graph, "Validate Process")));
        assertEquals(count(graph, "Anchor for"), 2);
    }


    /**
     * The data mapping layer holds the mapped elements.  The anchor that is part of the supply chain is drawn
     * outside the subgraph and linked with a thin line; an anchor that is not part of it is left out.
     */
    @Test
    public void dataMappingHoldsTheMappingsAndTheirReturnedAnchors()
    {
        String graph = render(Layer.DATA_MAPPING);

        assertInside(graph, "Source Column", "Data Mapping");
        assertInside(graph, "Target Column", "Data Mapping");
        assertOutside(graph, "Target Data Set", "Data Mapping");
        assertTrue(graph.contains("|\"Data Mapping\"|"));
        assertTrue(graph.contains(nodeId(graph, "Target Data Set") + "-->|\"Anchor for\"|" + nodeId(graph, "Target Column")));
        assertEquals(count(graph, "Anchor for"), 1, "the source column's anchor is not part of the supply chain");
        assertFalse(graph.contains("unreturned-table"));
        assertAbsent(graph, "Source File", "Load Process", "Provider Product", "Load Component");
    }


    /**
     * A layer with nothing in it has no graph, and nor does a supply chain without an implementation.
     */
    @Test
    public void emptyLayersHaveNoGraph()
    {
        InformationSupplyChainElement element = supplyChain(List.of(relationship("df1", sourceFile, loadProcess, "DataFlow", "DataLineageRelationship", "LineageRelationship")));

        assertNotNull(new InformationSupplyChainLayerMermaidGraphBuilder(element, Layer.DATA_FABRIC).getMermaidGraph(false));
        assertNull(new InformationSupplyChainLayerMermaidGraphBuilder(element, Layer.DATA_MESH).getMermaidGraph(false));
        assertNull(new InformationSupplyChainLayerMermaidGraphBuilder(element, Layer.SYSTEM_FABRIC).getMermaidGraph(false));
        assertNull(new InformationSupplyChainLayerMermaidGraphBuilder(element, Layer.DATA_MAPPING).getMermaidGraph(false));

        element.setImplementation(null);

        assertNull(new InformationSupplyChainLayerMermaidGraphBuilder(element, Layer.DATA_FABRIC).getMermaidGraph(false));
    }


    /**
     * The implementation graph keeps to the context, design and status: the layers are drawn elsewhere.
     */
    @Test
    public void theImplementationGraphLeavesTheLayersOut()
    {
        String graph = new InformationSupplyChainMermaidGraphBuilder(supplyChain(implementation())).getMermaidGraph(false);

        assertTrue(graph.contains("[Context]"));
        assertTrue(graph.contains("[Design]"));

        for (Layer layer : Layer.values())
        {
            assertFalse(graph.contains("[" + layer.getAreaName() + "]"), layer.getAreaName());
        }

        assertAbsent(graph, "Source File", "Provider Product", "Host", "Source Column");
    }


    /*
     * Fixtures and helpers
     */

    private List<MetadataRelationshipSummary> implementation()
    {
        sourceColumn.setAnchor(anchor("unreturned-table"));
        targetColumn.setAnchor(anchor(targetDataSet.getGUID()));
        reviewAction.setAnchor(anchor(anchorComponent.getGUID()));
        databaseManager.setAnchor(anchor(anchorComponent.getGUID()));
        consumerProduct.setAnchor(anchor(anchorComponent.getGUID()));
        extractProduct.setAnchor(anchor(consumerProduct.getGUID()));
        validateProcess.setAnchor(anchor(providerProduct.getGUID()));

        return List.of(relationship("df1", sourceFile, loadProcess, "DataFlow", "DataLineageRelationship", "LineageRelationship"),
                       relationship("df2", loadProcess, targetDataSet, "DataFlow", "DataLineageRelationship", "LineageRelationship"),
                       relationship("cf1", loadProcess, validateProcess, "ControlFlow", "LineageRelationship"),
                       relationship("ar1", loadProcess, reviewAction, "ActionRequester", "LineageRelationship"),
                       relationship("ul1", loadProcess, orphanProcess, "UnrelatedLink", "LineageRelationship"),
                       relationship("dm1", sourceColumn, targetColumn, "DataMapping", "LineageRelationship"),
                       relationship("lm1", targetDataSet, providerProduct, "LineageMapping", "DataLineageRelationship", "LineageRelationship"),
                       relationship("dpd1", consumerProduct, providerProduct, "DigitalProductDependency", "LineageRelationship"),
                       relationship("dpd2", extractProduct, providerProduct, "DigitalProductDependency", "LineageRelationship"),
                       relationship("lm2", targetDataSet, databaseManager, "LineageMapping", "DataLineageRelationship", "LineageRelationship"),
                       relationship("lm3", databaseManager, host, "LineageMapping", "DataLineageRelationship", "LineageRelationship"),
                       relationship("ib1", loadComponent, loadProcess, "ImplementedBy"),
                       relationship("ib2", productComponent, providerProduct, "ImplementedBy"),
                       relationship("ib3", systemComponent, databaseManager, "ImplementedBy"));
    }


    private String render(Layer layer)
    {
        String graph = new InformationSupplyChainLayerMermaidGraphBuilder(supplyChain(implementation()), layer).getMermaidGraph(false);

        assertNotNull(graph, layer + " should be drawn");

        return graph;
    }


    private ElementType type(String name, String... superTypes)
    {
        ElementType  elementType = new ElementType();
        List<String> names       = new ArrayList<>();

        names.add(name);
        names.addAll(Arrays.asList(superTypes));
        elementType.setTypeName(name);
        elementType.setSuperTypeNames(names);

        return elementType;
    }


    private ElementStub stub(String guid, String uniqueName, String typeName, String... superTypes)
    {
        ElementStub stub = new ElementStub();

        stub.setGUID(guid);
        stub.setUniqueName(uniqueName);
        stub.setType(type(typeName, superTypes));

        return stub;
    }


    private ElementClassification anchor(String anchorGUID)
    {
        AnchorsProperties properties = new AnchorsProperties();

        properties.setAnchorGUID(anchorGUID);

        ElementClassification classification = new ElementClassification();

        classification.setClassificationProperties(properties);

        return classification;
    }


    private MetadataRelationshipSummary relationship(String guid, ElementStub end1, ElementStub end2, String typeName, String... superTypes)
    {
        ElementHeader header = new ElementHeader();

        header.setGUID(guid);
        header.setType(type(typeName, superTypes));

        MetadataRelationshipSummary relationship = new MetadataRelationshipSummary();

        relationship.setRelationshipHeader(header);
        relationship.setEnd1(end1);
        relationship.setEnd2(end2);

        return relationship;
    }


    /**
     * The solution components are members of the supply chain, so they are displayed with their display names.
     *
     * @param implementation relationships
     * @return supply chain
     */
    private InformationSupplyChainElement supplyChain(List<MetadataRelationshipSummary> implementation)
    {
        InformationSupplyChainElement element = new InformationSupplyChainElement();
        ElementHeader                 header  = new ElementHeader();

        header.setGUID("isc1");
        header.setType(type("InformationSupplyChain", "Collection", "Referenceable"));
        element.setElementHeader(header);

        InformationSupplyChainProperties properties = new InformationSupplyChainProperties();

        properties.setDisplayName("ISC");
        properties.setQualifiedName(ISC_QUALIFIED_NAME);
        element.setProperties(properties);

        element.setCollectionMembers(new ArrayList<>(List.of(member(loadComponent, "Load Component"),
                                                             member(productComponent, "Product Component"),
                                                             member(systemComponent, "System Component"),
                                                             member(anchorComponent, "Anchor Component"))));
        element.setImplementation(new ArrayList<>(implementation));

        return element;
    }


    private RelatedMetadataElementSummary member(ElementStub stub, String displayName)
    {
        ElementHeader header = new ElementHeader();

        header.setGUID(stub.getGUID());
        header.setType(stub.getType());

        SolutionComponentProperties properties = new SolutionComponentProperties();

        properties.setDisplayName(displayName);
        properties.setQualifiedName(stub.getUniqueName());

        MetadataElementSummary summary = new MetadataElementSummary();

        summary.setElementHeader(header);
        summary.setProperties(properties);

        ElementHeader relationshipHeader = new ElementHeader();

        relationshipHeader.setGUID("membership-" + stub.getGUID());
        relationshipHeader.setType(type("CollectionMembership"));

        RelatedMetadataElementSummary member = new RelatedMetadataElementSummary();

        member.setRelationshipHeader(relationshipHeader);
        member.setRelatedElement(summary);

        return member;
    }


    /**
     * Return the position of the node's definition, which must appear exactly once.
     */
    private int nodePosition(String graph, String name)
    {
        Matcher matcher = Pattern.compile("^\\d+@\\{ shape: [^,]+, label: \"[^\"]*\\*\\*" + Pattern.quote(name) + "\\*\\*", Pattern.MULTILINE).matcher(graph);

        assertTrue(matcher.find(), name + " should be drawn");

        int position = matcher.start();

        assertFalse(matcher.find(), name + " should be drawn once");

        return position;
    }


    private String nodeId(String graph, String name)
    {
        Matcher matcher = Pattern.compile("^(\\d+)@\\{ shape: [^,]+, label: \"[^\"]*\\*\\*" + Pattern.quote(name) + "\\*\\*", Pattern.MULTILINE).matcher(graph);

        assertTrue(matcher.find(), name + " should be drawn");

        return matcher.group(1);
    }


    private void assertInside(String graph, String name, String areaName)
    {
        int start    = graph.indexOf("[" + areaName + "]\n");
        int end      = graph.indexOf("\nend\n", start);
        int position = nodePosition(graph, name);

        assertTrue(start >= 0, areaName + " should be drawn");
        assertTrue((position > start) && (position < end), name + " should be inside " + areaName);
    }


    private void assertOutside(String graph, String name, String areaName)
    {
        assertTrue(nodePosition(graph, name) < graph.indexOf("[" + areaName + "]\n"), name + " should be drawn before " + areaName);
    }


    private void assertAbsent(String graph, String... names)
    {
        for (String name : names)
        {
            assertFalse(graph.contains("**" + name + "**"), name + " should not be drawn");
        }
    }


    private int count(String text, String find)
    {
        int count = 0;
        int index = text.indexOf(find);

        while (index >= 0)
        {
            count++;
            index = text.indexOf(find, index + find.length());
        }

        return count;
    }
}
