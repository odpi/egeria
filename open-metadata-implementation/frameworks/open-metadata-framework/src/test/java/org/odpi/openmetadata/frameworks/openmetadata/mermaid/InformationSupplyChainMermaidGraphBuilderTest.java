/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * InformationSupplyChainMermaidGraphBuilderTest covers how the information supply chain graph presents its solution
 * components: the Design area always shows them in the standard solution component style, and the Status area is
 * only drawn when at least one component has something to report - a deployment status other than active, or a
 * Promise or Memento classification - in which case it repeats the components styled by that status.
 * <br><br>
 * The graph is text, so the tests render it and look at the nodes, shapes and styles that come out.  Each node
 * is found by its display name; a component in both areas appears twice, the Design node first.
 */
public class InformationSupplyChainMermaidGraphBuilderTest
{
    private static final String ISC_QUALIFIED_NAME = "isc-qualified-name";


    /**
     * Components that are active, or have no status, have nothing to report, so only the Design area is drawn.
     */
    @Test
    public void activeAndUnsetComponentsHaveNoStatusArea()
    {
        String graph = render(member("a1", "Active component", DeploymentStatus.ACTIVE, false, false),
                              member("a2", "Unset component", null, false, false));

        assertTrue(graph.contains("[Design]"));
        assertFalse(graph.contains("[Status]"), "nothing to report, so there should be no status area");
        assertEquals(nodes(graph, "Active component").size(), 1);
        assertEquals(nodes(graph, "Unset component").size(), 1);
    }


    /**
     * A supply chain with no members is a single node, which the builders do not draw, so there is no graph to
     * show either area in.
     */
    @Test
    public void noMembersMeansNoGraph()
    {
        assertNull(new InformationSupplyChainMermaidGraphBuilder(supplyChain()).getMermaidGraph());
    }


    /**
     * One component with a status is enough to draw the Status area, and it then lists every component, including
     * the ones that are active.  The Status area follows the Design area.
     */
    @Test
    public void aComponentWithAStatusDrawsTheStatusAreaWithAllComponents()
    {
        String graph = render(member("a1", "Active component", DeploymentStatus.ACTIVE, false, false),
                              member("a2", "Proposed component", DeploymentStatus.PROPOSED, false, false));

        assertTrue(graph.indexOf("[Design]") < graph.indexOf("[Status]"), "the status area follows the design area");

        List<String[]> active = nodes(graph, "Active component");
        List<String[]> proposed = nodes(graph, "Proposed component");

        assertEquals(active.size(), 2, "the active component is in both areas");
        assertEquals(proposed.size(), 2, "the proposed component is in both areas");

        assertEquals(fill(graph, active.get(1)[0]), VisualStyle.DEFAULT_SOLUTION_COMPONENT.getFillColour());
        assertEquals(fill(graph, proposed.get(0)[0]), VisualStyle.DEFAULT_SOLUTION_COMPONENT.getFillColour(),
                     "the design area ignores the status");
        assertEquals(fill(graph, proposed.get(1)[0]), VisualStyle.PROPOSED_SOLUTION_COMPONENT.getFillColour());
        assertTrue(graph.contains("Deployment Status: Proposed"), "the status is written into the node as well");
    }


    /**
     * Each status maps to its own style in the Status area, and the Design area never changes.
     */
    @Test
    public void eachStatusHasItsOwnStyleInTheStatusAreaOnly()
    {
        Map<DeploymentStatus, VisualStyle> expectedStyles = new LinkedHashMap<>();

        expectedStyles.put(DeploymentStatus.PROPOSED, VisualStyle.PROPOSED_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.APPROVED_FOR_DEPLOYMENT, VisualStyle.PROPOSED_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.UNDER_DEVELOPMENT, VisualStyle.IN_DEVELOPMENT_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.DEVELOPMENT_COMPLETE, VisualStyle.IN_DEVELOPMENT_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.STANDBY, VisualStyle.STANDBY_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.DISABLED, VisualStyle.DISABLED_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.REJECTED, VisualStyle.REJECTED_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.FAILED, VisualStyle.FAILED_SOLUTION_COMPONENT);
        expectedStyles.put(DeploymentStatus.OTHER, VisualStyle.DEFAULT_SOLUTION_COMPONENT);

        for (DeploymentStatus status : expectedStyles.keySet())
        {
            String graph = render(member("a1", "Component", status, false, false));
            List<String[]> component = nodes(graph, "Component");

            assertEquals(component.size(), 2, status + ": the component should be in both areas");
            assertEquals(fill(graph, component.get(0)[0]), VisualStyle.DEFAULT_SOLUTION_COMPONENT.getFillColour(),
                         status + ": the design area should not change");
            assertEquals(fill(graph, component.get(1)[0]), expectedStyles.get(status).getFillColour(),
                         status + ": wrong fill in the status area");
        }
    }


    /**
     * A locally defined status is shown when the status is OTHER.
     */
    @Test
    public void anOtherStatusShowsTheUserDefinedStatus()
    {
        SolutionComponentProperties properties = new SolutionComponentProperties();

        properties.setUserDefinedDeploymentStatus("Awaiting vendor");

        String graph = render(member("a1", "Other component", DeploymentStatus.OTHER, properties, false, false));

        assertTrue(graph.contains("Deployment Status: Awaiting vendor"));
    }


    /**
     * A Promise or Memento on a member that is not a solution component (it has no deployment status) also draws the
     * Status area, and the member takes the classification's style there.
     */
    @Test
    public void aClassifiedMemberThatIsNotASolutionComponentDrawsTheStatusArea()
    {
        String graph = render(otherMember("p1", "Promised port", true, false));

        List<String[]> port = nodes(graph, "Promised port");

        assertTrue(graph.contains("[Status]"));
        assertEquals(port.size(), 2);
        assertEquals(fill(graph, port.get(0)[0]), VisualStyle.DEFAULT_SOLUTION_COMPONENT.getFillColour());
        assertEquals(fill(graph, port.get(1)[0]), VisualStyle.PROMISE.getFillColour());
    }


    /**
     * A member that is not a solution component and has no classification has nothing to report.
     */
    @Test
    public void anUnclassifiedMemberThatIsNotASolutionComponentHasNoStatusArea()
    {
        String graph = render(otherMember("p1", "Plain port", false, false));

        assertFalse(graph.contains("[Status]"));
    }


    /**
     * The Promise classification draws the Status area on its own.  The component takes the promise style there,
     * but keeps the standard style in the Design area.
     */
    @Test
    public void aPromiseDrawsTheStatusAreaButDoesNotChangeTheDesignStyle()
    {
        String graph = render(member("a1", "Promised component", null, true, false));

        List<String[]> component = nodes(graph, "Promised component");

        assertTrue(graph.contains("[Status]"));
        assertEquals(component.size(), 2);
        assertEquals(component.get(0)[1], "rect", "design shape");
        assertEquals(fill(graph, component.get(0)[0]), VisualStyle.DEFAULT_SOLUTION_COMPONENT.getFillColour());
        assertEquals(component.get(1)[1], VisualStyle.PROMISE.getShape(), "status shape");
        assertEquals(fill(graph, component.get(1)[0]), VisualStyle.PROMISE.getFillColour());
    }


    /**
     * The Memento classification behaves in the same way as Promise.
     */
    @Test
    public void aMementoDrawsTheStatusAreaButDoesNotChangeTheDesignStyle()
    {
        String graph = render(member("a1", "Archived component", null, false, true));

        List<String[]> component = nodes(graph, "Archived component");

        assertTrue(graph.contains("[Status]"));
        assertEquals(component.size(), 2);
        assertEquals(fill(graph, component.get(0)[0]), VisualStyle.DEFAULT_SOLUTION_COMPONENT.getFillColour());
        assertEquals(fill(graph, component.get(1)[0]), VisualStyle.MEMENTO.getFillColour());
    }


    /**
     * A classification does not change how the information supply chain itself is drawn.
     */
    @Test
    public void classificationsDoNotChangeTheContextNodeStyle()
    {
        InformationSupplyChainElement element = supplyChain(member("a1", "Component", null, false, false));

        element.getElementHeader().setPromise(new ElementClassification());

        String graph = new InformationSupplyChainMermaidGraphBuilder(element).getMermaidGraph();
        List<String[]> contextNode = nodes(graph, "ISC");

        assertEquals(contextNode.size(), 1);
        assertEquals(fill(graph, contextNode.get(0)[0]), VisualStyle.PRINCIPLE_INFORMATION_SUPPLY_CHAIN.getFillColour());
    }


    /**
     * The solution linking wires that belong to the supply chain are drawn between the components in the Design
     * area and again between their copies in the Status area.
     */
    @Test
    public void wiresAreDrawnInBothAreas()
    {
        RelatedMetadataHierarchySummary first  = member("a1", "First component", DeploymentStatus.PROPOSED, false, false);
        RelatedMetadataElementSummary   second = member("a2", "Second component", DeploymentStatus.ACTIVE, false, false);

        SolutionLinkingWireProperties wireProperties = new SolutionLinkingWireProperties();

        wireProperties.setLabel("sample flow");
        wireProperties.setISCQualifiedNames(List.of(ISC_QUALIFIED_NAME));

        ElementHeader wireHeader = new ElementHeader();

        wireHeader.setGUID("wire1");
        wireHeader.setType(type("SolutionLinkingWire"));

        RelatedMetadataElementSummary wire = new RelatedMetadataElementSummary();

        wire.setRelationshipHeader(wireHeader);
        wire.setRelationshipProperties(wireProperties);
        wire.setRelatedElement(second.getRelatedElement());
        wire.setRelatedElementAtEnd1(false);
        first.setSideLinks(List.of(wire));

        String graph = render(first, second);

        assertEquals(count(graph, "sample flow"), 2, "one wire in each area");

        String firstDesignId  = nodes(graph, "First component").get(0)[0];
        String firstStatusId  = nodes(graph, "First component").get(1)[0];
        String secondDesignId = nodes(graph, "Second component").get(0)[0];
        String secondStatusId = nodes(graph, "Second component").get(1)[0];

        assertTrue(graph.contains(firstDesignId + "-. \"sample flow [Solution Linking Wire]\" .->" + secondDesignId));
        assertTrue(graph.contains(firstStatusId + "-. \"sample flow [Solution Linking Wire]\" .->" + secondStatusId));
    }


    /*
     * Fixtures and helpers
     */

    private ElementType type(String name, String... superTypes)
    {
        ElementType elementType = new ElementType();
        List<String> names = new ArrayList<>();

        names.add(name);
        names.addAll(Arrays.asList(superTypes));
        elementType.setTypeName(name);
        elementType.setSuperTypeNames(names);

        return elementType;
    }


    private RelatedMetadataHierarchySummary member(String           guid,
                                                   String           name,
                                                   DeploymentStatus status,
                                                   boolean          promise,
                                                   boolean          memento)
    {
        return member(guid, name, status, new SolutionComponentProperties(), promise, memento);
    }


    private RelatedMetadataHierarchySummary member(String                       guid,
                                                   String                       name,
                                                   DeploymentStatus             status,
                                                   SolutionComponentProperties  properties,
                                                   boolean                      promise,
                                                   boolean                      memento)
    {
        ElementHeader header = new ElementHeader();

        header.setGUID(guid);
        header.setType(type("SolutionComponent", "DesignModelElement", "Referenceable"));

        if (promise)
        {
            header.setPromise(new ElementClassification());
        }

        if (memento)
        {
            header.setMemento(new ElementClassification());
        }

        properties.setDisplayName(name);
        properties.setQualifiedName(name);
        properties.setDeploymentStatus(status);

        MetadataElementSummary summary = new MetadataElementSummary();

        summary.setElementHeader(header);
        summary.setProperties(properties);

        ElementHeader relationshipHeader = new ElementHeader();

        relationshipHeader.setGUID("membership-" + guid);
        relationshipHeader.setType(type("CollectionMembership"));

        RelatedMetadataHierarchySummary member = new RelatedMetadataHierarchySummary();

        member.setRelationshipHeader(relationshipHeader);
        member.setRelatedElement(summary);

        return member;
    }


    private RelatedMetadataElementSummary otherMember(String  guid,
                                                      String  name,
                                                      boolean promise,
                                                      boolean memento)
    {
        ElementHeader header = new ElementHeader();

        header.setGUID(guid);
        header.setType(type("SolutionPort", "DesignModelElement", "Referenceable"));

        if (promise)
        {
            header.setPromise(new ElementClassification());
        }

        if (memento)
        {
            header.setMemento(new ElementClassification());
        }

        ReferenceableProperties properties = new ReferenceableProperties();

        properties.setDisplayName(name);
        properties.setQualifiedName(name);

        MetadataElementSummary summary = new MetadataElementSummary();

        summary.setElementHeader(header);
        summary.setProperties(properties);

        ElementHeader relationshipHeader = new ElementHeader();

        relationshipHeader.setGUID("membership-" + guid);
        relationshipHeader.setType(type("CollectionMembership"));

        RelatedMetadataElementSummary member = new RelatedMetadataElementSummary();

        member.setRelationshipHeader(relationshipHeader);
        member.setRelatedElement(summary);

        return member;
    }


    private InformationSupplyChainElement supplyChain(RelatedMetadataElementSummary... members)
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

        element.setCollectionMembers(new ArrayList<>(Arrays.asList(members)));
        element.setImplementation(new ArrayList<>());

        return element;
    }


    private String render(RelatedMetadataElementSummary... members)
    {
        String graph = new InformationSupplyChainMermaidGraphBuilder(supplyChain(members)).getMermaidGraph();

        assertNotNull(graph, "the graph should be drawn");

        return graph;
    }


    /**
     * Find the nodes with the display name, in the order they appear.
     *
     * @param graph mermaid text
     * @param name display name
     * @return list of {node id, shape}
     */
    private List<String[]> nodes(String graph, String name)
    {
        Pattern       pattern = Pattern.compile("^(\\d+)@\\{ shape: ([^,]+), label: \"[^\"]*\\*\\*" + Pattern.quote(name) + "\\*\\*", Pattern.MULTILINE);
        Matcher       matcher = pattern.matcher(graph);
        List<String[]> found  = new ArrayList<>();

        while (matcher.find())
        {
            found.add(new String[]{matcher.group(1), matcher.group(2)});
        }

        return found;
    }


    private String fill(String graph, String nodeId)
    {
        Matcher matcher = Pattern.compile("^style " + nodeId + " color:[^,]+, fill:([^,]+),", Pattern.MULTILINE).matcher(graph);

        assertTrue(matcher.find(), "no style found for node " + nodeId);

        return matcher.group(1);
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
