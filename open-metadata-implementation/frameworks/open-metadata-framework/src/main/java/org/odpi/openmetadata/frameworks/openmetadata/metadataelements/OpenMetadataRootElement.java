/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRootElement contains the properties and header for an element retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetGraph.class, name = "AssetGraph"),
                @JsonSubTypes.Type(value = AssetLineageGraphNode.class, name = "AssetLineageGraphNode"),
                @JsonSubTypes.Type(value = AssetSearchMatches.class, name = "AssetSearchMatches"),
                @JsonSubTypes.Type(value = OpenMetadataRootHierarchy.class, name = "OpenMetadataRootHierarchy"),
                @JsonSubTypes.Type(value = TemplateElement.class, name = "TemplateElement"),
        })
public class OpenMetadataRootElement extends AttributedMetadataElement
{
    private OpenMetadataRootProperties properties                       = null;
    private String                     mermaidGraph                     = null;
    private String                     solutionBlueprintMermaidGraph    = null;
    private String                     solutionSubcomponentMermaidGraph = null;

    /**
     * Default constructor
     */
    public OpenMetadataRootElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataRootElement(OpenMetadataRootElement template)
    {
        super(template);

        if (template != null)
        {
            properties   = template.getProperties();
            mermaidGraph = template.getMermaidGraph();
            solutionBlueprintMermaidGraph = template.getSolutionBlueprintMermaidGraph();
            solutionSubcomponentMermaidGraph = template.getSolutionSubcomponentMermaidGraph();
        }
    }


    /**
     * Return the properties for the asset.
     *
     * @return asset properties (using appropriate subclass)
     */
    public OpenMetadataRootProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the asset.
     *
     * @param properties asset properties
     */
    public void setProperties(OpenMetadataRootProperties properties)
    {
        this.properties = properties;
    }




    /**
     * Return the mermaid representation of this data structure.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid representation of this data structure.
     *
     * @param mermaidGraph markdown string
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * Return the graph view of the solution blueprint.  This just contains the top level components linked together.
     *
     * @return mermaid markdown
     */
    public String getSolutionBlueprintMermaidGraph()
    {
        return solutionBlueprintMermaidGraph;
    }


    /**
     * Set up the graph view of the solution component.
     *
     * @param solutionBlueprintMermaidGraph mermaid markdown
     */
    public void setSolutionBlueprintMermaidGraph(String solutionBlueprintMermaidGraph)
    {
        this.solutionBlueprintMermaidGraph = solutionBlueprintMermaidGraph;
    }


    /**
     * Return the graph of a solution component's subcomponents.
     *
     * @return mermaid markdown
     */
    public String getSolutionSubcomponentMermaidGraph()
    {
        return solutionSubcomponentMermaidGraph;
    }


    /**
     * Set up the graph of a solution component's subcomponents.
     *
     * @param solutionSubcomponentMermaidGraph mermaid markdown
     */
    public void setSolutionSubcomponentMermaidGraph(String solutionSubcomponentMermaidGraph)
    {
        this.solutionSubcomponentMermaidGraph = solutionSubcomponentMermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRootElement{" +
                "properties=" + properties +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                ", solutionBlueprintMermaidGraph='" + solutionBlueprintMermaidGraph + '\'' +
                ", solutionSubcomponentMermaidGraph='" + solutionSubcomponentMermaidGraph + '\'' +
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
        OpenMetadataRootElement that = (OpenMetadataRootElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(mermaidGraph, that.mermaidGraph) &&
                Objects.equals(solutionBlueprintMermaidGraph, that.solutionBlueprintMermaidGraph) &&
                Objects.equals(solutionSubcomponentMermaidGraph, that.solutionSubcomponentMermaidGraph);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, mermaidGraph, solutionBlueprintMermaidGraph, solutionSubcomponentMermaidGraph);
    }
}
