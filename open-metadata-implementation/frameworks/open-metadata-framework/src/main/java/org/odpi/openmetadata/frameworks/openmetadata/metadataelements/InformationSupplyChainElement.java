/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InformationSupplyChainElement contains the properties and header for an information supply chain entity and its
 * segment retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainElement extends OpenMetadataRootElement
{
    private List<MetadataRelationshipSummary> implementation                = null; // isc qualified name in relationship
    private String                            iscImplementationMermaidGraph = null;
    private String                            iscDataMeshMermaidGraph       = null;
    private String                            iscSystemFabricMermaidGraph   = null;
    private String                            iscDataFabricMermaidGraph     = null;
    private String                            iscDataMappingMermaidGraph    = null;

    /**
     * Default constructor
     */
    public InformationSupplyChainElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainElement(InformationSupplyChainElement template)
    {
        super (template);

        if (template != null)
        {
            implementation                = template.getImplementation();
            iscImplementationMermaidGraph = template.getISCImplementationMermaidGraph();
            iscDataMeshMermaidGraph       = template.getISCDataMeshMermaidGraph();
            iscSystemFabricMermaidGraph   = template.getISCSystemFabricMermaidGraph();
            iscDataFabricMermaidGraph     = template.getISCDataFabricMermaidGraph();
            iscDataMappingMermaidGraph    = template.getISCDataMappingMermaidGraph();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainElement(OpenMetadataRootElement template)
    {
        super (template);
    }


    /**
     * Return the relationships between the asset metadata that identifies the lineage relationship as belonging to this information supply chain.
     *
     * @return list of relationships
     */
    public List<MetadataRelationshipSummary> getImplementation()
    {
        return implementation;
    }


    /**
     * Set up the relationships between the asset metadata that identifies the lineage relationship as belonging to this information supply chain.
     *
     * @param implementation list of relationships
     */
    public void setImplementation(List<MetadataRelationshipSummary> implementation)
    {
        this.implementation = implementation;
    }


    /**
     * Return the graph view of the information supply chain's context, design and the status of its solution components.
     *
     * @return mermaid markdown
     */
    @JsonProperty("iscImplementationMermaidGraph")
    public String getISCImplementationMermaidGraph()
    {
        return iscImplementationMermaidGraph;
    }


    /**
     * Set up the graph view of the information supply chain's context, design and the status of its solution components.
     *
     * @param iscImplementationMermaidGraph mermaid markdown
     */
    @JsonProperty("iscImplementationMermaidGraph")
    public void setISCImplementationMermaidGraph(String iscImplementationMermaidGraph)
    {
        this.iscImplementationMermaidGraph = iscImplementationMermaidGraph;
    }


    /**
     * Return the graph view of the digital products that implement the information supply chain, along with the
     * solution components that they implement.
     *
     * @return mermaid markdown
     */
    @JsonProperty("iscDataMeshMermaidGraph")
    public String getISCDataMeshMermaidGraph()
    {
        return iscDataMeshMermaidGraph;
    }


    /**
     * Set up the graph view of the digital products that implement the information supply chain, along with the
     * solution components that they implement.
     *
     * @param iscDataMeshMermaidGraph mermaid markdown
     */
    @JsonProperty("iscDataMeshMermaidGraph")
    public void setISCDataMeshMermaidGraph(String iscDataMeshMermaidGraph)
    {
        this.iscDataMeshMermaidGraph = iscDataMeshMermaidGraph;
    }


    /**
     * Return the graph view of the IT infrastructure and software capabilities that the information supply chain
     * runs on, along with the solution components that they implement.
     *
     * @return mermaid markdown
     */
    @JsonProperty("iscSystemFabricMermaidGraph")
    public String getISCSystemFabricMermaidGraph()
    {
        return iscSystemFabricMermaidGraph;
    }


    /**
     * Set up the graph view of the IT infrastructure and software capabilities that the information supply chain
     * runs on, along with the solution components that they implement.
     *
     * @param iscSystemFabricMermaidGraph mermaid markdown
     */
    @JsonProperty("iscSystemFabricMermaidGraph")
    public void setISCSystemFabricMermaidGraph(String iscSystemFabricMermaidGraph)
    {
        this.iscSystemFabricMermaidGraph = iscSystemFabricMermaidGraph;
    }


    /**
     * Return the graph view of the data lineage that implements the information supply chain, along with the
     * solution components and digital products linked to it.
     *
     * @return mermaid markdown
     */
    @JsonProperty("iscDataFabricMermaidGraph")
    public String getISCDataFabricMermaidGraph()
    {
        return iscDataFabricMermaidGraph;
    }


    /**
     * Set up the graph view of the data lineage that implements the information supply chain, along with the
     * solution components and digital products linked to it.
     *
     * @param iscDataFabricMermaidGraph mermaid markdown
     */
    @JsonProperty("iscDataFabricMermaidGraph")
    public void setISCDataFabricMermaidGraph(String iscDataFabricMermaidGraph)
    {
        this.iscDataFabricMermaidGraph = iscDataFabricMermaidGraph;
    }


    /**
     * Return the graph view of the data mappings that implement the information supply chain, along with the
     * anchors of the mapped elements.
     *
     * @return mermaid markdown
     */
    @JsonProperty("iscDataMappingMermaidGraph")
    public String getISCDataMappingMermaidGraph()
    {
        return iscDataMappingMermaidGraph;
    }


    /**
     * Set up the graph view of the data mappings that implement the information supply chain, along with the
     * anchors of the mapped elements.
     *
     * @param iscDataMappingMermaidGraph mermaid markdown
     */
    @JsonProperty("iscDataMappingMermaidGraph")
    public void setISCDataMappingMermaidGraph(String iscDataMappingMermaidGraph)
    {
        this.iscDataMappingMermaidGraph = iscDataMappingMermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainElement{" +
                "implementation=" + implementation +
                ", iscImplementationMermaidGraph='" + iscImplementationMermaidGraph + '\'' +
                ", iscDataMeshMermaidGraph='" + iscDataMeshMermaidGraph + '\'' +
                ", iscSystemFabricMermaidGraph='" + iscSystemFabricMermaidGraph + '\'' +
                ", iscDataFabricMermaidGraph='" + iscDataFabricMermaidGraph + '\'' +
                ", iscDataMappingMermaidGraph='" + iscDataMappingMermaidGraph + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        InformationSupplyChainElement that = (InformationSupplyChainElement) objectToCompare;
        return Objects.equals(implementation, that.implementation) &&
                Objects.equals(iscImplementationMermaidGraph, that.iscImplementationMermaidGraph) &&
                Objects.equals(iscDataMeshMermaidGraph, that.iscDataMeshMermaidGraph) &&
                Objects.equals(iscSystemFabricMermaidGraph, that.iscSystemFabricMermaidGraph) &&
                Objects.equals(iscDataFabricMermaidGraph, that.iscDataFabricMermaidGraph) &&
                Objects.equals(iscDataMappingMermaidGraph, that.iscDataMappingMermaidGraph);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), implementation, iscImplementationMermaidGraph, iscDataMeshMermaidGraph,
                            iscSystemFabricMermaidGraph, iscDataFabricMermaidGraph, iscDataMappingMermaidGraph);
    }
}
