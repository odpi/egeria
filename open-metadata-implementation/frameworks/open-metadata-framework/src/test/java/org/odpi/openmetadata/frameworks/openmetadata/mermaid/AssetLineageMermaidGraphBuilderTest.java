/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * AssetLineageMermaidGraphBuilderTest covers the field-level and local lineage graphs drawn from an asset graph.
 * The lineage relationships in an asset graph often start from one of the asset's anchored elements, such as a
 * table or column, rather than from the asset itself.  Every end of every line must be a node in the graph:
 * mermaid draws an undeclared end as a box showing only its node number.
 * <br><br>
 * Every test uses the same asset: a database schema anchoring a table.  The table is mapped to a load
 * component, which in turn is mapped to the table; a data flow runs from the table to a report.
 */
public class AssetLineageMermaidGraphBuilderTest
{
    private final MetadataElementSummary table         = element("table1", "Supplier Table", "RelationalTable", "SchemaAttribute", "SchemaElement");
    private final MetadataElementSummary loadComponent = element("component1", "Load Component", "DeployedSoftwareComponent", "SoftwareComponent", "Asset");
    private final MetadataElementSummary report        = element("report1", "Supplier Report", "Report", "DataSet", "Asset");


    /**
     * The table that the data mappings start from is drawn as a node, so neither end of a mapping is a bare number.
     */
    @Test
    public void fieldLevelLineageDrawsTheStartingSchemaElement()
    {
        String graph = new AssetFieldLevelLineageMermaidGraphBuilder(assetGraph()).getMermaidGraph(true);

        assertNotNull(graph, "the field-level lineage graph should be drawn");
        assertTrue(graph.contains("**Supplier Table**"), "the table the mappings start from should be drawn:\n" + graph);
        assertTrue(graph.contains("**Load Component**"), "the load component should be drawn:\n" + graph);
        assertEveryLineEndIsANode(graph);
    }


    /**
     * The table that the data flow starts from is drawn as a node.
     */
    @Test
    public void localLineageDrawsTheStartingSchemaElement()
    {
        String graph = new AssetLocalLineageGraphMermaidGraphBuilder(assetGraph()).getMermaidGraph();

        assertNotNull(graph, "the local lineage graph should be drawn");
        assertTrue(graph.contains("**Supplier Table**"), "the table the data flow starts from should be drawn:\n" + graph);
        assertTrue(graph.contains("**Supplier Report**"), "the report should be drawn:\n" + graph);
        assertEveryLineEndIsANode(graph);
    }


    private AssetGraph assetGraph()
    {
        AssetGraph    assetGraph = new AssetGraph();
        ElementHeader header     = new ElementHeader();

        header.setGUID("schema1");
        header.setType(type("DeployedDatabaseSchema", "DeployedDataStore", "Asset"));
        assetGraph.setElementHeader(header);
        assetGraph.setProperties(properties("Supplier Schema"));

        assetGraph.setAnchoredElements(new ArrayList<>(List.of(table)));
        assetGraph.setRelationships(new ArrayList<>(List.of(relationship("mapping1", table, loadComponent, false, "DataMapping"),
                                                            relationship("mapping2", table, loadComponent, true, "DataMapping"),
                                                            relationship("flow1", table, report, false, "DataFlow"))));

        return assetGraph;
    }


    /**
     * Every node referenced by a line must be declared, either as a node or as the start of a line from a
     * declared node.
     */
    private void assertEveryLineEndIsANode(String graph)
    {
        Set<String> declared   = new HashSet<>();
        Set<String> referenced = new HashSet<>();

        Matcher nodes = Pattern.compile("^(\\d+)@\\{ shape:", Pattern.MULTILINE).matcher(graph);

        while (nodes.find())
        {
            declared.add(nodes.group(1));
        }

        Matcher lines = Pattern.compile("^(\\d+) \\d+@--.*?-+>(\\d+)$", Pattern.MULTILINE).matcher(graph);

        while (lines.find())
        {
            referenced.add(lines.group(1));
            referenced.add(lines.group(2));
        }

        assertTrue(! referenced.isEmpty(), "the graph should have lines:\n" + graph);

        referenced.removeAll(declared);

        assertTrue(referenced.isEmpty(), "line ends " + referenced + " are not nodes in the graph:\n" + graph);
    }


    private RelatedMetadataNodeSummary relationship(String                 guid,
                                                    MetadataElementSummary startingElement,
                                                    MetadataElementSummary relatedElement,
                                                    boolean                relatedElementAtEnd1,
                                                    String                 typeName)
    {
        ElementHeader header = new ElementHeader();

        header.setGUID(guid);
        header.setType(type(typeName));

        RelatedMetadataNodeSummary relationship = new RelatedMetadataNodeSummary();

        relationship.setRelationshipHeader(header);
        relationship.setRelatedElement(relatedElement);
        relationship.setRelatedElementAtEnd1(relatedElementAtEnd1);
        relationship.setStartingElementGUID(startingElement.getElementHeader().getGUID());

        return relationship;
    }


    private MetadataElementSummary element(String guid, String displayName, String typeName, String... superTypes)
    {
        ElementHeader header = new ElementHeader();

        header.setGUID(guid);
        header.setType(type(typeName, superTypes));

        MetadataElementSummary element = new MetadataElementSummary();

        element.setElementHeader(header);
        element.setProperties(properties(displayName));

        return element;
    }


    private ReferenceableProperties properties(String displayName)
    {
        ReferenceableProperties properties = new ReferenceableProperties();

        properties.setQualifiedName(displayName + "-qualified-name");
        properties.setDisplayName(displayName);

        return properties;
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
}
