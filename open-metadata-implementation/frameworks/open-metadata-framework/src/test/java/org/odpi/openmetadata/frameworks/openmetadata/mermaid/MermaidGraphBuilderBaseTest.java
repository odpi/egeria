/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * MermaidGraphBuilderBaseTest covers the machinery that all thirty-one mermaid graph builders are built on.
 * <br><br>
 * These builders turn a graph of metadata into a mermaid diagram, and that diagram goes straight into the
 * user interface.  Nothing has tested any of it.  The failure mode is unusual and worth being explicit
 * about: mermaid is a text format, so a builder that emits something malformed does not throw - it returns a
 * perfectly good String, the platform hands it to the browser, and the diagram either renders wrongly or
 * does not render at all.  Nothing between the builder and the user notices.
 * <br><br>
 * The base class is where every builder gets its node identifiers, its de-duplication, its subgraphs, its
 * lines and its string hygiene, so testing it covers the part all thirty-one share.  What is <b>not</b>
 * covered here is each builder's own translation of its own bean - each takes a different graph and needs a
 * fixture of its own - and that remains the larger job.
 */
public class MermaidGraphBuilderBaseTest
{
    /**
     * A graph with fewer than two nodes is returned as null rather than as a diagram of one box.
     * <br><br>
     * This is deliberate - a single node is not worth drawing - but it means every caller has to handle
     * null, and a caller that does not gets a {@link NullPointerException} or an empty panel in the user
     * interface.  Pinning it here means the rule cannot quietly change under those callers.
     */
    @Test
    public void aGraphOfFewerThanTwoNodesIsNull()
    {
        MermaidGraphBuilderBase emptyGraph = new MermaidGraphBuilderBase();

        assertNull(emptyGraph.getMermaidGraph(), "an empty graph should be null, not an empty diagram");

        MermaidGraphBuilderBase oneNode = new MermaidGraphBuilderBase();

        oneNode.appendNewMermaidNode("only-one", "The Only Node", "Asset", VisualStyle.TYPE);

        assertNull(oneNode.getMermaidGraph(),
                   "a graph holding a single node should be null - callers rely on that rather than on" +
                           " receiving a diagram of one box");

        MermaidGraphBuilderBase twoNodes = new MermaidGraphBuilderBase();

        twoNodes.appendNewMermaidNode("first", "First", "Asset", VisualStyle.TYPE);
        twoNodes.appendNewMermaidNode("second", "Second", "Asset", VisualStyle.TYPE);

        assertNotNull(twoNodes.getMermaidGraph(), "a graph holding two nodes should be drawn");
    }


    /**
     * A node identifier is derived once per element and reused, so that the second mention of an element
     * lands on the same box rather than drawing another one beside it.
     */
    @Test
    public void nodeIdentifiersAreStablePerElement()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        String firstLookup  = graphBuilder.lookupNodeName("a-guid");
        String secondLookup = graphBuilder.lookupNodeName("a-guid");

        assertNotNull(firstLookup);
        assertEquals(secondLookup, firstLookup,
                     "the same element must map to the same node id, or it is drawn twice and the edges" +
                             " between them point at different boxes");

        assertFalse(firstLookup.equals(graphBuilder.lookupNodeName("a-different-guid")),
                    "two elements must not share a node id, or they are drawn as one box");
    }


    /**
     * Adding the same node twice is reported as not new, and draws it once.
     */
    @Test
    public void aNodeIsOnlyAddedOnce()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        assertTrue(graphBuilder.appendNewMermaidNode("guid-1", "Widget", "Asset", VisualStyle.TYPE),
                   "the first mention of an element should report that a node was added");
        assertFalse(graphBuilder.appendNewMermaidNode("guid-1", "Widget", "Asset", VisualStyle.TYPE),
                    "the second mention of the same element should report that no node was added, so that" +
                            " callers walking a graph do not recurse into it again");

        graphBuilder.appendNewMermaidNode("guid-2", "Other", "Asset", VisualStyle.TYPE);

        String graph = graphBuilder.getMermaidGraph();

        assertNotNull(graph);
        assertEquals(countOccurrences(graph, graphBuilder.lookupNodeName("guid-1") + "@{"),
                     1,
                     "the element should be drawn exactly once:\n" + graph);
    }


    /**
     * Every subgraph opened is closed, and a direction is emitted only when one is asked for.
     * <br><br>
     * An unbalanced subgraph is the malformed case that renders as nothing at all, and the direction is how
     * a builder controls which way its flow reads.
     */
    @Test
    public void subgraphsAreBalancedAndCarryTheirDirection()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        graphBuilder.startSubgraph("Outer Area", VisualStyle.DESCRIPTION);
        graphBuilder.appendNewMermaidNode("inside-1", "Inside One", "Asset", VisualStyle.TYPE);
        graphBuilder.startSubgraph("Inner Area", VisualStyle.DESCRIPTION, "BT");
        graphBuilder.appendNewMermaidNode("inside-2", "Inside Two", "Asset", VisualStyle.TYPE);
        graphBuilder.endSubgraph();
        graphBuilder.endSubgraph();

        String graph = graphBuilder.getMermaidGraph();

        assertNotNull(graph);
        assertEquals(countOccurrences(graph, "subgraph "),
                     countOccurrences(graph, "end\n"),
                     "every subgraph must be closed, or the whole diagram fails to render:\n" + graph);

        assertTrue(graph.contains("direction BT"),
                   "a subgraph given a direction should carry it, so the flow reads the way the builder" +
                           " intended:\n" + graph);
        assertEquals(countOccurrences(graph, "direction "),
                     1,
                     "a subgraph with no direction should not be given one:\n" + graph);
    }


    /**
     * Every node identifier an edge refers to is a node the graph declares.
     * <br><br>
     * Mermaid will happily invent a bare box for an identifier it has not seen, so an edge naming an
     * undeclared node does not fail - it draws an unlabelled, unstyled box with no type and no name.
     */
    @Test
    public void everyEdgeJoinsNodesTheGraphDeclares()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        graphBuilder.appendNewMermaidNode("end-one", "End One", "Asset", VisualStyle.TYPE);
        graphBuilder.appendNewMermaidNode("end-two", "End Two", "Asset", VisualStyle.TYPE);
        graphBuilder.appendMermaidLine("line-1", "end-one", "flows to", "end-two");

        String graph   = graphBuilder.getMermaidGraph();
        String endOne  = graphBuilder.lookupNodeName("end-one");
        String endTwo  = graphBuilder.lookupNodeName("end-two");

        assertNotNull(graph);
        assertTrue(graph.contains(endOne + "@{"), "the first end should be declared:\n" + graph);
        assertTrue(graph.contains(endTwo + "@{"), "the second end should be declared:\n" + graph);
        assertTrue(graph.contains(endOne + "==>|\"flows to\"|" + endTwo),
                   "the edge should join the two declared nodes:\n" + graph);
    }


    /**
     * An edge is drawn once however many times the same relationship is offered.
     */
    @Test
    public void anEdgeIsOnlyDrawnOnce()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        graphBuilder.appendNewMermaidNode("end-one", "End One", "Asset", VisualStyle.TYPE);
        graphBuilder.appendNewMermaidNode("end-two", "End Two", "Asset", VisualStyle.TYPE);
        graphBuilder.appendMermaidLine("line-1", "end-one", "flows to", "end-two");
        graphBuilder.appendMermaidLine("line-1", "end-one", "flows to", "end-two");

        String graph = graphBuilder.getMermaidGraph();

        assertNotNull(graph);
        assertEquals(countOccurrences(graph, "==>|\"flows to\"|"),
                     1,
                     "a relationship offered twice should be drawn once:\n" + graph);
    }


    /**
     * A node's display name is made safe before it goes into a label.
     * <br><br>
     * A double quote would close the mermaid label early and leave the rest of the name as diagram syntax,
     * which is how a name from the metadata turns into a diagram that does not render.
     */
    @Test
    public void aNodeLabelIsMadeSafe()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        graphBuilder.appendNewMermaidNode("guid-1", "A \"quoted\" name: with a colon", "Asset", VisualStyle.TYPE);
        graphBuilder.appendNewMermaidNode("guid-2", "Ordinary", "Asset", VisualStyle.TYPE);

        String graph = graphBuilder.getMermaidGraph();

        assertNotNull(graph);
        assertFalse(graph.contains("A \"quoted\""),
                    "a double quote in a display name must not reach the diagram - it closes the label" +
                            " early:\n" + graph);
        assertTrue(graph.contains("A 'quoted' name - with a colon"),
                   "the name should still be readable after being made safe:\n" + graph);
    }


    /**
     * An edge label is made safe too.
     * <br><br>
     * Node labels were being cleaned and edge labels were not, although both end up inside a quoted mermaid
     * label and both come from metadata.  A relationship label carrying a double quote closed its label
     * early in exactly the same way.
     */
    @Test
    public void anEdgeLabelIsMadeSafe()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        graphBuilder.appendNewMermaidNode("end-one", "End One", "Asset", VisualStyle.TYPE);
        graphBuilder.appendNewMermaidNode("end-two", "End Two", "Asset", VisualStyle.TYPE);
        graphBuilder.appendMermaidLine("line-1", "end-one", "the \"main\" flow", "end-two");

        String graph = graphBuilder.getMermaidGraph();

        assertNotNull(graph);
        assertFalse(graph.contains("the \"main\" flow"),
                    "a double quote in an edge label must not reach the diagram - it closes the label early" +
                            " and the rest of the label is read as diagram syntax:\n" + graph);
        assertTrue(graph.contains("the 'main' flow"),
                   "the edge label should still be readable after being made safe:\n" + graph);
    }


    /**
     * The string helpers every builder uses to turn metadata into diagram-safe text.
     */
    @Test
    public void theStringHelpersAreSafeAndNullTolerant()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        assertEquals(graphBuilder.removeSpaces("Egeria:: Asset [Widget] (v1) {x}"), "Egeria::AssetWidgetv1x");
        assertNull(graphBuilder.removeSpaces(null), "a null name should stay null rather than becoming \"null\"");

        assertEquals(graphBuilder.removeTroublesomeCharacters("say \"hello\""), "say 'hello'");
        assertEquals(graphBuilder.removeTroublesomeCharacters("label: value"), "label - value");
        assertEquals(graphBuilder.removeTroublesomeCharacters("a//b"), "a/ /b");
        assertNull(graphBuilder.removeTroublesomeCharacters(null));

        assertEquals(graphBuilder.removeTroublesomeTitleCharacters("a \"title\": here"), "a 'title' - here");
        assertNull(graphBuilder.removeTroublesomeTitleCharacters(null));
    }


    /**
     * A type name with no type still produces a readable box rather than the word "null".
     */
    @Test
    public void aMissingTypeNameIsShownAsSuch()
    {
        MermaidGraphBuilderBase graphBuilder = new MermaidGraphBuilderBase();

        assertEquals(graphBuilder.addSpacesToTypeName(null), "<null>");
        assertEquals(graphBuilder.addSpacesToTypeName("DataFile"), "Data File");
    }


    /**
     * Count how many times one string appears in another.
     *
     * @param text text to search
     * @param soughtValue value to count
     * @return number of occurrences
     */
    private int countOccurrences(String text, String soughtValue)
    {
        int count = 0;
        int index = text.indexOf(soughtValue);

        while (index >= 0)
        {
            count++;
            index = text.indexOf(soughtValue, index + soughtValue.length());
        }

        return count;
    }
}
