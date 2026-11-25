/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBoundaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * SolutionBlueprintMermaidGraphBuilderTest covers the graph of components and roles that is drawn for a solution
 * blueprint and for a solution boundary.  The two graphs are the same apart from the title.  A solution boundary
 * that is a member of the blueprint (or of another solution boundary) is drawn as a subgraph, with a white border
 * and title, and its members are drawn inside it.
 * <br><br>
 * The graph is text, so the tests render it and look at where the nodes and subgraphs are declared.
 */
public class SolutionBlueprintMermaidGraphBuilderTest
{
    private static final String[] BLUEPRINT_TYPES = {"DesignModel", "Collection", "Referenceable", "OpenMetadataRoot"};
    private static final String[] BOUNDARY_TYPES  = {"DesignModelBoundary", "Collection", "Referenceable", "OpenMetadataRoot"};
    private static final String[] COMPONENT_TYPES = {"DesignModelElement", "Referenceable", "OpenMetadataRoot"};


    /**
     * The title of the graph says whether it is for a solution blueprint or a solution boundary.
     */
    @Test
    public void theTitleNamesTheTypeOfElement()
    {
        String blueprintGraph = new SolutionBlueprintMermaidGraphBuilder(blueprint(component("c1", "Component one"),
                                                                                   component("c2", "Component two"))).getMermaidGraph();
        String boundaryGraph  = new SolutionBlueprintMermaidGraphBuilder(boundaryRoot(component("c1", "Component one"),
                                                                                      component("c2", "Component two"))).getMermaidGraph();

        assertTrue(blueprintGraph.contains("title: Components and Roles for Solution Blueprint - Root"), blueprintGraph);
        assertTrue(boundaryGraph.contains("title: Components and Roles for Solution Boundary - Root"), boundaryGraph);

        /*
         * Apart from the title, the graphs are the same.
         */
        assertEquals(boundaryGraph.replace("Solution Boundary", "Solution Blueprint"), blueprintGraph);
    }


    /**
     * A blueprint without solution boundaries has a single subgraph containing its components.
     */
    @Test
    public void noBoundariesMeansNoBoundarySubgraphs()
    {
        String graph = new SolutionBlueprintMermaidGraphBuilder(blueprint(component("c1", "Component one"),
                                                                          component("c2", "Component two"))).getMermaidGraph();

        assertEquals(count(graph, "subgraph "), 1);
        assertNotNull(nodeId(graph, "Component one"));
        assertNotNull(nodeId(graph, "Component two"));
    }


    /**
     * A solution boundary in the blueprint is a subgraph with a white border and title.  Its members are declared
     * inside it, and the direct members of the blueprint are declared outside it.
     */
    @Test
    public void aBoundaryIsASubgraphContainingItsMembers()
    {
        String graph = new SolutionBlueprintMermaidGraphBuilder(blueprint(component("outside", "Outside component"),
                                                                          boundary("b1", "Trusted zone",
                                                                                   component("inside", "Inside component")))).getMermaidGraph();

        assertEquals(count(graph, "subgraph "), 2, graph);

        String boundaryId    = subgraphId(graph, "Trusted zone");
        String boundaryBlock = subgraphBlock(graph, "Trusted zone");

        assertTrue(boundaryBlock.contains("Inside component"), "the boundary's member should be inside its subgraph: " + graph);
        assertFalse(boundaryBlock.contains("Outside component"), "the blueprint's member should be outside the boundary: " + graph);
        assertNotNull(nodeId(graph, "Outside component"));

        assertTrue(graph.contains("style " + boundaryId +
                                          " color:" + VisualStyle.SOLUTION_BOUNDARY_GRAPH.getTextColour() +
                                          ", fill:" + VisualStyle.SOLUTION_BOUNDARY_GRAPH.getFillColour() +
                                          ", stroke:" + VisualStyle.SOLUTION_BOUNDARY_GRAPH.getLineColour()), graph);
        assertEquals(VisualStyle.SOLUTION_BOUNDARY_GRAPH.getTextColour(), "#FFFFFF", "white title");
        assertEquals(VisualStyle.SOLUTION_BOUNDARY_GRAPH.getLineColour(), "#FFFFFF", "white border");
        assertEquals(VisualStyle.SOLUTION_BOUNDARY_GRAPH.getFillColour(), VisualStyle.SOLUTION_BLUEPRINT_GRAPH.getFillColour(),
                     "the boundary is filled with the blueprint's background");
    }


    /**
     * Solution boundaries nest, and two boundaries with the same name are separate subgraphs.
     */
    @Test
    public void boundariesNest()
    {
        String graph = new SolutionBlueprintMermaidGraphBuilder(blueprint(boundary("b1", "Zone",
                                                                                   component("c1", "Outer member"),
                                                                                   boundary("b2", "Zone",
                                                                                            component("c2", "Inner member"))))).getMermaidGraph();

        assertEquals(count(graph, "subgraph "), 3, graph);
        assertEquals(count(graph, "[\"Zone\"]"), 2, graph);

        int outerStart = graph.indexOf("[\"Zone\"]");
        int innerStart = graph.indexOf("[\"Zone\"]", outerStart + 1);
        int innerEnd   = graph.indexOf("\nend\n", innerStart);
        int outerEnd   = graph.indexOf("\nend\n", innerEnd + 1);

        int innerMember = graph.indexOf("Inner member");
        int outerMember = graph.indexOf("Outer member");

        assertTrue((innerMember > innerStart) && (innerMember < innerEnd), "the inner member is in the inner boundary: " + graph);
        assertTrue((outerMember > innerEnd) && (outerMember < outerEnd), "the outer member is in the outer boundary only: " + graph);
    }


    /**
     * A component that is a member of both the blueprint and one of its boundaries is drawn once, inside the boundary.
     */
    @Test
    public void aSharedMemberIsDrawnInsideTheBoundary()
    {
        String graph = new SolutionBlueprintMermaidGraphBuilder(blueprint(component("shared", "Shared component"),
                                                                          component("other", "Other component"),
                                                                          boundary("b1", "Trusted zone",
                                                                                   component("shared", "Shared component")))).getMermaidGraph();

        assertEquals(count(graph, "**Shared component**"), 1, graph);
        assertTrue(subgraphBlock(graph, "Trusted zone").contains("Shared component"), graph);
    }


    /**
     * Solution linking wires are drawn between components wherever they are in the boundary hierarchy.
     */
    @Test
    public void wiresCrossBoundaries()
    {
        RelatedMetadataHierarchySummary inside  = component("inside", "Inside component");
        RelatedMetadataHierarchySummary outside = component("outside", "Outside component");

        SolutionLinkingWireProperties wireProperties = new SolutionLinkingWireProperties();

        wireProperties.setLabel("sample flow");

        ElementHeader wireHeader = new ElementHeader();

        wireHeader.setGUID("wire1");
        wireHeader.setType(type("SolutionLinkingWire"));

        RelatedMetadataElementSummary wire = new RelatedMetadataElementSummary();

        wire.setRelationshipHeader(wireHeader);
        wire.setRelationshipProperties(wireProperties);
        wire.setRelatedElement(outside.getRelatedElement());
        wire.setRelatedElementAtEnd1(false);
        inside.setSideLinks(List.of(wire));

        String graph = new SolutionBlueprintMermaidGraphBuilder(blueprint(outside, boundary("b1", "Trusted zone", inside))).getMermaidGraph();

        String insideId  = nodeId(graph, "Inside component");
        String outsideId = nodeId(graph, "Outside component");

        Matcher matcher = Pattern.compile("(?m)^" + insideId + "==>\\|\"sample flow[^|]*\\|" + outsideId + "$").matcher(graph);

        assertTrue(matcher.find(), "the wire from inside the boundary to outside it is missing: " + graph);
    }


    /*
     * Fixtures and helpers
     */

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


    private ElementHeader header(String guid, ElementType type)
    {
        ElementHeader elementHeader = new ElementHeader();

        elementHeader.setGUID(guid);
        elementHeader.setType(type);

        return elementHeader;
    }


    private RelatedMetadataHierarchySummary related(String guid, ElementType type, ReferenceableProperties properties, String name)
    {
        properties.setDisplayName(name);
        properties.setQualifiedName(guid + "-qualified-name");

        MetadataElementSummary summary = new MetadataElementSummary();

        summary.setElementHeader(header(guid, type));
        summary.setProperties(properties);

        RelatedMetadataHierarchySummary related = new RelatedMetadataHierarchySummary();

        related.setRelationshipHeader(header("membership-of-" + guid, type("CollectionMembership")));
        related.setRelatedElement(summary);
        related.setRelatedElementAtEnd1(false);

        return related;
    }


    private RelatedMetadataHierarchySummary component(String guid, String name)
    {
        return related(guid, type("SolutionComponent", COMPONENT_TYPES), new SolutionComponentProperties(), name);
    }


    private RelatedMetadataHierarchySummary boundary(String guid, String name, RelatedMetadataElementSummary... members)
    {
        RelatedMetadataHierarchySummary boundary = related(guid, type("SolutionBoundary", BOUNDARY_TYPES), new SolutionBoundaryProperties(), name);

        boundary.setNestedElements(Arrays.asList(members));

        return boundary;
    }


    private OpenMetadataRootElement blueprint(RelatedMetadataElementSummary... members)
    {
        return root(type("SolutionBlueprint", BLUEPRINT_TYPES), new SolutionBlueprintProperties(), members);
    }


    private OpenMetadataRootElement boundaryRoot(RelatedMetadataElementSummary... members)
    {
        return root(type("SolutionBoundary", BOUNDARY_TYPES), new SolutionBoundaryProperties(), members);
    }


    private OpenMetadataRootElement root(ElementType type, ReferenceableProperties properties, RelatedMetadataElementSummary... members)
    {
        properties.setDisplayName("Root");
        properties.setQualifiedName("root-qualified-name");

        OpenMetadataRootElement element = new OpenMetadataRootElement();

        element.setElementHeader(header("root", type));
        element.setProperties(properties);
        element.setCollectionMembers(Arrays.asList(members));

        return element;
    }


    private int count(String graph, String text)
    {
        int count = 0;
        int index = graph.indexOf(text);

        while (index >= 0)
        {
            count++;
            index = graph.indexOf(text, index + 1);
        }

        return count;
    }


    /**
     * Return the identifier of the node with this display name.
     */
    private String nodeId(String graph, String displayName)
    {
        Matcher matcher = Pattern.compile("(?m)^(\\w+)@\\{[^}]*\\*\\*" + Pattern.quote(displayName) + "\\*\\*").matcher(graph);

        assertTrue(matcher.find(), "no node for " + displayName + " in: " + graph);

        return matcher.group(1);
    }


    /**
     * Return the identifier of the subgraph with this title.
     */
    private String subgraphId(String graph, String title)
    {
        Matcher matcher = Pattern.compile("(?m)^subgraph (\\w+) \\[\"" + Pattern.quote(title) + "\"]$").matcher(graph);

        assertTrue(matcher.find(), "no subgraph for " + title + " in: " + graph);

        return matcher.group(1);
    }


    /**
     * Return the text from the start of a subgraph that has no nested subgraphs to its end.
     */
    private String subgraphBlock(String graph, String title)
    {
        int start = graph.indexOf("[\"" + title + "\"]");
        int end   = graph.indexOf("\nend\n", start);

        assertTrue((start >= 0) && (end > start), "no subgraph for " + title + " in: " + graph);

        return graph.substring(start, end);
    }
}
