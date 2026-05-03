/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;

import java.util.List;
import java.util.Map;
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
                @JsonSubTypes.Type(value = InformationSupplyChainElement.class, name = "InformationSupplyChainElement"),
                @JsonSubTypes.Type(value = OpenMetadataRootHierarchy.class, name = "OpenMetadataRootHierarchy"),
        })
public class OpenMetadataRootElement extends AttributedMetadataElement
{
    private OpenMetadataRootProperties                 properties    = null;
    private Map<String, List<SpecificationProperty>>   specification = null;

    /*
     * These are the mermaid Markdown versions of the values from the properties and retrieved relationships.
     */
    private String mermaidGraph                            = null;
    private String rootHierarchyMermaidGraph               = null;
    private String specificationMermaidGraph               = null;
    private String solutionBlueprintMermaidGraph           = null;
    private String solutionSubcomponentMermaidGraph        = null;
    private String organizationTreeMermaidGraph            = null;
    private String collectionMermaidMindMap                = null;
    private String zoneProfileMermaidPieChart              = null;
    private String zoneProfileAnchoredMermaidPieChart      = null;
    private String zoneProfileAllMermaidPieChart           = null;
    private String userAccountTypeProfileMermaidPieChart   = null;
    private String userAccountStatusProfileMermaidPieChart = null;


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
            properties                              = template.getProperties();
            specification                           = template.getSpecification();
            mermaidGraph                            = template.getMermaidGraph();
            rootHierarchyMermaidGraph               = template.getRootHierarchyMermaidGraph();
            specificationMermaidGraph               = template.getSpecificationMermaidGraph();
            solutionBlueprintMermaidGraph           = template.getSolutionBlueprintMermaidGraph();
            solutionSubcomponentMermaidGraph        = template.getSolutionSubcomponentMermaidGraph();
            organizationTreeMermaidGraph            = template.getOrganizationTreeMermaidGraph();
            collectionMermaidMindMap                = template.getCollectionMermaidMindMap();
            zoneProfileMermaidPieChart              = template.getZoneProfileMermaidPieChart();
            zoneProfileAnchoredMermaidPieChart      = template.getZoneProfileAnchoredMermaidPieChart();
            zoneProfileAllMermaidPieChart           = template.getZoneProfileAllMermaidPieChart();
            userAccountTypeProfileMermaidPieChart   = template.getUserAccountTypeProfileMermaidPieChart();
            userAccountStatusProfileMermaidPieChart = template.getUserAccountStatusProfileMermaidPieChart();
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
     * Return the specification reference data for the attached element.
     *
     * @return specification (attributeName, list[propertyName, propertyValue])
     */
    public Map<String, List<SpecificationProperty>> getSpecification()
    {
        return specification;
    }


    /**
     * Set up the specification reference data for the attached element.
     *
     * @param specification specification
     */
    public void setSpecification(Map<String, List<SpecificationProperty>> specification)
    {
        this.specification = specification;
    }


    /**
     * Return the mermaid representation of this element.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid representation of this element.
     *
     * @param mermaidGraph markdown string
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * Return the root hierarchy style of mermaid graph.
     *
     * @return string markdown
     */
    public String getRootHierarchyMermaidGraph()
    {
        return rootHierarchyMermaidGraph;
    }


    /**
     * Set up the root hierarchy style of mermaid graph.
     *
     * @param rootHierarchyMermaidGraph string markdown
     */
    public void setRootHierarchyMermaidGraph(String rootHierarchyMermaidGraph)
    {
        this.rootHierarchyMermaidGraph = rootHierarchyMermaidGraph;
    }


    /**
     * Return the mermaid graph of a linked specification if there is one.
     *
     * @return mermaid markdown
     */
    public String getSpecificationMermaidGraph()
    {
        return specificationMermaidGraph;
    }


    /**
     * Set up the mermaid graph of a linked specification if there is one.
     *
     * @param specificationMermaidGraph mermaid markdown
     */
    public void setSpecificationMermaidGraph(String specificationMermaidGraph)
    {
        this.specificationMermaidGraph = specificationMermaidGraph;
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
     * Return the graph of a team's subteams.
     *
     * @return mermaid markdown
     */
    public String getOrganizationTreeMermaidGraph()
    {
        return organizationTreeMermaidGraph;
    }


    /**
     * Set up the graph of a team's subteams.
     *
     * @param organizationTreeMermaidGraph mermaid markdown
     */
    public void setOrganizationTreeMermaidGraph(String organizationTreeMermaidGraph)
    {
        this.organizationTreeMermaidGraph = organizationTreeMermaidGraph;
    }


    /**
     * Return the mermaid graph of a collection using the mind map pattern.
     *
     * @return mermaid markdown
     */
    public String getCollectionMermaidMindMap()
    {
        return collectionMermaidMindMap;
    }


    /**
     * Set up the mermaid graph of a collection using the mind map pattern.
     *
     * @param collectionMermaidMindMap mermaid markdown
     */
    public void setCollectionMermaidMindMap(String collectionMermaidMindMap)
    {
        this.collectionMermaidMindMap = collectionMermaidMindMap;
    }


    /**
     * Return the mermaid pie chart of a zone profile.
     *
     * @return mermaid markdown
     */
    public String getZoneProfileMermaidPieChart()
    {
        return zoneProfileMermaidPieChart;
    }


    /**
     * Set up the mermaid pie chart of a zone profile.
     *
     * @param zoneProfileMermaidPieChart mermaid markdown
     */
    public void setZoneProfileMermaidPieChart(String zoneProfileMermaidPieChart)
    {
        this.zoneProfileMermaidPieChart = zoneProfileMermaidPieChart;
    }


    /**
     * Return the mermaid pie chart of a zone profile showing the anchored elements included in the zone through their anchor's membership.
     *
     * @return mermaid markdown
     */
    public String getZoneProfileAnchoredMermaidPieChart()
    {
        return zoneProfileAnchoredMermaidPieChart;
    }


    /**
     * Set up the mermaid pie chart of a zone profile showing the anchored elements included in the zone through their anchor's membership.
     *
     * @param zoneProfileAnchoredMermaidPieChart mermaid markdown
     */
    public void setZoneProfileAnchoredMermaidPieChart(String zoneProfileAnchoredMermaidPieChart)
    {
        this.zoneProfileAnchoredMermaidPieChart = zoneProfileAnchoredMermaidPieChart;
    }


    /**
     * Return the mermaid pie chart of a zone profile showing all elements included in the zone.
     *
     * @return mermaid markdown
     */
    public String getZoneProfileAllMermaidPieChart()
    {
        return zoneProfileAllMermaidPieChart;
    }


    /**
     * Set up the mermaid pie chart of a zone profile showing all elements included in the zone.
     *
     * @param zoneProfileAllMermaidPieChart mermaid markdown
     */
    public void setZoneProfileAllMermaidPieChart(String zoneProfileAllMermaidPieChart)
    {
        this.zoneProfileAllMermaidPieChart = zoneProfileAllMermaidPieChart;
    }


    /**
     * Return the mermaid pie chart of a user account type profile.
     *
     * @return mermaid markdown
     */
    public String getUserAccountTypeProfileMermaidPieChart()
    {
        return userAccountTypeProfileMermaidPieChart;
    }


    /**
     * Set up the mermaid pie chart of a user account type profile.
     *
     * @param userAccountTypeProfileMermaidPieChart mermaid markdown
     */
    public void setUserAccountTypeProfileMermaidPieChart(String userAccountTypeProfileMermaidPieChart)
    {
        this.userAccountTypeProfileMermaidPieChart = userAccountTypeProfileMermaidPieChart;
    }


    /**
     * Return the mermaid pie chart of a user account status profile.
     *
     * @return mermaid markdown
     */
    public String getUserAccountStatusProfileMermaidPieChart()
    {
        return userAccountStatusProfileMermaidPieChart;
    }


    /**
     * Set up the mermaid pie chart of a user account status profile.
     *
     * @param userAccountStatusProfileMermaidPieChart mermaid markdown
     */
    public void setUserAccountStatusProfileMermaidPieChart(String userAccountStatusProfileMermaidPieChart)
    {
        this.userAccountStatusProfileMermaidPieChart = userAccountStatusProfileMermaidPieChart;
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
                ", specification=" + specification +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                ", rootHierarchyMermaidGraph='" + rootHierarchyMermaidGraph + '\'' +
                ", specificationMermaidGraph='" + specificationMermaidGraph + '\'' +
                ", solutionBlueprintMermaidGraph='" + solutionBlueprintMermaidGraph + '\'' +
                ", solutionSubcomponentMermaidGraph='" + solutionSubcomponentMermaidGraph + '\'' +
                ", organizationTreeMermaidGraph='" + organizationTreeMermaidGraph + '\'' +
                ", collectionMermaidMindMap='" + collectionMermaidMindMap + '\'' +
                ", zoneProfileMermaidPieChart='" + zoneProfileMermaidPieChart + '\'' +
                ", zoneProfileAnchoredMermaidPieChart='" + zoneProfileAnchoredMermaidPieChart + '\'' +
                ", zoneProfileAllMermaidPieChart='" + zoneProfileAllMermaidPieChart + '\'' +
                ", userAccountTypeProfileMermaidPieChart='" + userAccountTypeProfileMermaidPieChart + '\'' +
                ", userAccountStatusProfileMermaidPieChart='" + userAccountStatusProfileMermaidPieChart + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        OpenMetadataRootElement that = (OpenMetadataRootElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(specification, that.specification) &&
                Objects.equals(mermaidGraph, that.mermaidGraph) &&
                Objects.equals(rootHierarchyMermaidGraph, that.rootHierarchyMermaidGraph) &&
                Objects.equals(specificationMermaidGraph, that.specificationMermaidGraph) &&
                Objects.equals(solutionBlueprintMermaidGraph, that.solutionBlueprintMermaidGraph) &&
                Objects.equals(solutionSubcomponentMermaidGraph, that.solutionSubcomponentMermaidGraph) &&
                Objects.equals(organizationTreeMermaidGraph, that.organizationTreeMermaidGraph) &&
                Objects.equals(collectionMermaidMindMap, that.collectionMermaidMindMap) &&
                Objects.equals(userAccountStatusProfileMermaidPieChart, that.userAccountStatusProfileMermaidPieChart) &&
                Objects.equals(userAccountTypeProfileMermaidPieChart, that.userAccountTypeProfileMermaidPieChart) &&
                Objects.equals(zoneProfileMermaidPieChart, that.zoneProfileMermaidPieChart) &&
                Objects.equals(zoneProfileAnchoredMermaidPieChart, that.zoneProfileAnchoredMermaidPieChart) &&
                Objects.equals(zoneProfileAllMermaidPieChart, that.zoneProfileAllMermaidPieChart);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, specification, mermaidGraph, rootHierarchyMermaidGraph,
                            specificationMermaidGraph, solutionBlueprintMermaidGraph, solutionSubcomponentMermaidGraph,
                            organizationTreeMermaidGraph, collectionMermaidMindMap, zoneProfileMermaidPieChart,
                            zoneProfileAnchoredMermaidPieChart, zoneProfileAllMermaidPieChart,
                            userAccountStatusProfileMermaidPieChart, userAccountTypeProfileMermaidPieChart);
    }
}
