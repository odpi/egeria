/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * AssetLineageGraphHighlightMermaidGraphBuilderTest checks that the lineage relationships belonging to a
 * highlighted information supply chain are drawn as wide lines, the others as thin lines.
 * <br><br>
 * The graph has four assets.  Source to Middle and Other to Target belong to the highlighted
 * information supply chain; Middle to Target belongs to a different one; Source to Other belongs to none.
 */
public class AssetLineageGraphHighlightMermaidGraphBuilderTest
{
    private static final String HIGHLIGHTED_ISC = "isc1";
    private static final String OTHER_ISC       = "isc2";

    private static final Pattern LINK_LINE       = Pattern.compile("^\\d+(==>|-->|-\\.->|~~~| ).*\\d+$", Pattern.MULTILINE);
    private static final Pattern LINK_STYLE_LINE = Pattern.compile("^linkStyle ([\\d,]+) stroke-width:6px$", Pattern.MULTILINE);


    /**
     * The wide-line style names exactly the links in the highlighted information supply chain.
     */
    @Test
    public void highlightedSupplyChainLinksAreWidened()
    {
        String graph = new AssetLineageGraphMermaidGraphBuilder(lineageGraph(), HIGHLIGHTED_ISC).getMermaidGraph();

        assertNotNull(graph, "the lineage graph should be drawn");

        List<String> links = links(graph);

        assertEquals(links.size(), 4, "every lineage relationship should be drawn:\n" + graph);

        List<String> expectedWideLinks = new ArrayList<>();

        for (int linkIndex = 0; linkIndex < links.size(); linkIndex++)
        {
            String link = links.get(linkIndex);

            if (link.contains("==>"))
            {
                expectedWideLinks.add(Integer.toString(linkIndex));
            }
        }

        assertEquals(expectedWideLinks.size(), 2, "the two supply chain links should be drawn as thick lines:\n" + graph);

        Matcher linkStyle = LINK_STYLE_LINE.matcher(graph);

        assertEquals(linkStyle.find(), true, "the thick lines should be widened:\n" + graph);
        assertEquals(linkStyle.group(1), String.join(",", expectedWideLinks), "the wrong links are widened:\n" + graph);
    }


    /**
     * Without a highlighted information supply chain, all lines are normal and none is widened.
     */
    @Test
    public void noHighlightMeansNoWideLinks()
    {
        String graph = new AssetLineageGraphMermaidGraphBuilder(lineageGraph(), null).getMermaidGraph();

        assertNotNull(graph, "the lineage graph should be drawn");
        assertEquals(links(graph).size(), 4, "every lineage relationship should be drawn:\n" + graph);
        assertFalse(graph.contains("linkStyle"), "no link should be widened:\n" + graph);
        assertFalse(graph.contains("-->"), "no link should be thin:\n" + graph);
    }


    private List<String> links(String graph)
    {
        List<String> links   = new ArrayList<>();
        Matcher      matcher = LINK_LINE.matcher(graph);

        while (matcher.find())
        {
            links.add(matcher.group());
        }

        return links;
    }


    private AssetLineageGraph lineageGraph()
    {
        AssetLineageGraph graph = new AssetLineageGraph();

        graph.setElementHeader(header("source", "DataSet"));
        graph.setProperties(properties("Source"));

        graph.setLinkedAssets(List.of(node("middle", "Middle"),
                                      node("other", "Other"),
                                      node("target", "Target")));

        graph.setLineageRelationships(List.of(relationship("source", "middle", List.of(HIGHLIGHTED_ISC)),
                                              relationship("middle", "target", List.of(OTHER_ISC)),
                                              relationship("source", "other", null),
                                              relationship("other", "target", List.of(OTHER_ISC, HIGHLIGHTED_ISC))));

        return graph;
    }


    private AssetLineageGraphNode node(String guid, String displayName)
    {
        AssetLineageGraphNode node = new AssetLineageGraphNode();

        node.setElementHeader(header(guid, "DataSet"));
        node.setProperties(properties(displayName));

        return node;
    }


    private AssetLineageGraphRelationship relationship(String end1GUID, String end2GUID, List<String> informationSupplyChains)
    {
        AssetLineageGraphRelationship relationship = new AssetLineageGraphRelationship();

        relationship.setEnd1AssetGUID(end1GUID);
        relationship.setEnd2AssetGUID(end2GUID);
        relationship.setRelationshipTypes(List.of("DataFlow"));
        relationship.setInformationSupplyChains(informationSupplyChains);

        return relationship;
    }


    private ElementHeader header(String guid, String typeName)
    {
        ElementType elementType = new ElementType();

        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(List.of(typeName, "Asset"));

        ElementHeader header = new ElementHeader();

        header.setGUID(guid);
        header.setType(elementType);

        return header;
    }


    private ReferenceableProperties properties(String displayName)
    {
        ReferenceableProperties properties = new ReferenceableProperties();

        properties.setQualifiedName(displayName + "-qualified-name");
        properties.setDisplayName(displayName);

        return properties;
    }
}
