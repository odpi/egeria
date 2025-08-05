/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;


import java.util.List;
import java.util.Objects;

/**
 * AssetGraph is used to return an asset along with all of its anchored elements and the relationships
 * that they have between one another and to other elements.
 */
public class AssetGraph extends OpenMetadataRootElement
{
    private List<MetadataElementSummary>     anchoredElements                   = null;
    private List<RelatedMetadataNodeSummary> relationships                      = null;
    private List<MetadataElementSummary>     informationSupplyChains            = null;
    private String                           informationSupplyChainMermaidGraph = null;
    private String                           fieldLevelLineageGraph             = null;
    private String                           actionMermaidGraph                 = null;
    private String                           localLineageGraph                  = null;

    /**
     * Default constructor
     */
    public AssetGraph()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template values for asset
     */
    public AssetGraph(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset graph
     */
    public AssetGraph(AssetGraph template)
    {
        super(template);

        if (template != null)
        {
            anchoredElements                   = template.getAnchoredElements();
            relationships                      = template.getRelationships();
            informationSupplyChains            = template.getInformationSupplyChains();
            informationSupplyChainMermaidGraph = template.getInformationSupplyChainMermaidGraph();
            fieldLevelLineageGraph             = template.getFieldLevelLineageGraph();
            actionMermaidGraph                 = template.getActionMermaidGraph();
            localLineageGraph                  = template.getLocalLineageGraph();
        }
    }


    /**
     * Return the list of elements that are anchored to the asset.
     *
     * @return anchored elements
     */
    public List<MetadataElementSummary> getAnchoredElements()
    {
        return anchoredElements;
    }


    /**
     * Set up the list of elements that are anchored to the asset.
     *
     * @param anchoredElements  anchored elements
     */
    public void setAnchoredElements(List<MetadataElementSummary> anchoredElements)
    {
        this.anchoredElements = anchoredElements;
    }


    /**
     * Return the relationships that connect the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @return relationships
     */
    public List<RelatedMetadataNodeSummary> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the relationships that connect the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @param relationships relationships
     */
    public void setRelationships(List<RelatedMetadataNodeSummary> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Return the list of information supply chains that are associated with the lineage relationships to the asset.
     *
     * @return list of information supply chain elements
     */
    public List<MetadataElementSummary> getInformationSupplyChains()
    {
        return informationSupplyChains;
    }


    /**
     * Set up the list of information supply chains that are associated with the lineage relationships to the asset.
     *
     * @param informationSupplyChains list of information supply chain elements
     */
    public void setInformationSupplyChains(List<MetadataElementSummary> informationSupplyChains)
    {
        this.informationSupplyChains = informationSupplyChains;
    }


    /**
     * Return the mermaid string used to render a summary graph of the information supply chains that the
     * asset is a part of.
     *
     * @return string in Mermaid markdown
     */
    public String getInformationSupplyChainMermaidGraph()
    {
        return informationSupplyChainMermaidGraph;
    }


    /**
     * Set up the mermaid string used to render a summary graph of the information supply chains that the
     * asset is a part of.
     *
     * @param informationSupplyChainMermaidGraph string in Mermaid markdown
     */
    public void setInformationSupplyChainMermaidGraph(String informationSupplyChainMermaidGraph)
    {
        this.informationSupplyChainMermaidGraph = informationSupplyChainMermaidGraph;
    }


    /**
     * Return field-level lineage graph.
     *
     * @return mermaid string
     */
    public String getFieldLevelLineageGraph()
    {
        return fieldLevelLineageGraph;
    }


    /**
     * Set up field-level lineage graph.
     *
     * @param fieldLevelLineageGraph mermaid string
     */
    public void setFieldLevelLineageGraph(String fieldLevelLineageGraph)
    {
        this.fieldLevelLineageGraph = fieldLevelLineageGraph;
    }


    /**
     * Return details of the actions operating on the asset.
     *
     * @return mermaid string
     */
    public String getActionMermaidGraph()
    {
        return actionMermaidGraph;
    }


    /**
     * Set up details of the actions operating on the asset.
     *
     * @param actionMermaidGraph mermaid string
     */
    public void setActionMermaidGraph(String actionMermaidGraph)
    {
        this.actionMermaidGraph = actionMermaidGraph;
    }


    /**
     * Return the mermaid graph that shows the directly connected lineage relationships.
     *
     * @return mermaid string
     */
    public String getLocalLineageGraph()
    {
        return localLineageGraph;
    }


    /**
     * Set up the mermaid graph that shows the directly connected lineage relationships.
     *
     * @param localLineageGraph mermaid string
     */
    public void setLocalLineageGraph(String localLineageGraph)
    {
        this.localLineageGraph = localLineageGraph;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetGraph{" +
                "anchoredElements=" + anchoredElements +
                ", relationships=" + relationships +
                ", informationSupplyChains=" + informationSupplyChains +
                ", informationSupplyChainMermaidGraph='" + informationSupplyChainMermaidGraph + '\'' +
                ", fieldLevelLineageGraph='" + fieldLevelLineageGraph + '\'' +
                ", actionMermaidGraph='" + actionMermaidGraph + '\'' +
                ", localLineageGraph='" + localLineageGraph + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssetGraph that = (AssetGraph) objectToCompare;
        return Objects.equals(anchoredElements, that.anchoredElements) &&
                Objects.equals(relationships, that.relationships) &&
                Objects.equals(informationSupplyChainMermaidGraph, that.informationSupplyChainMermaidGraph) &&
                Objects.equals(actionMermaidGraph, that.actionMermaidGraph) &&
                Objects.equals(localLineageGraph, that.localLineageGraph) &&
                Objects.equals(fieldLevelLineageGraph, that.fieldLevelLineageGraph);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchoredElements, relationships,
                            informationSupplyChainMermaidGraph, fieldLevelLineageGraph,
                            actionMermaidGraph, localLineageGraph);
    }
}
