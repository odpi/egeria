/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
     *
     * @return mermaid markdown
     */
    public String getISCImplementationMermaidGraph()
    {
        return iscImplementationMermaidGraph;
    }


    /**
     * Set up the graph view of the solution component.
     *
     * @param iscImplementationMermaidGraph mermaid markdown
     */
    public void setISCImplementationMermaidGraph(String iscImplementationMermaidGraph)
    {
        this.iscImplementationMermaidGraph = iscImplementationMermaidGraph;
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
                Objects.equals(iscImplementationMermaidGraph, that.iscImplementationMermaidGraph);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), implementation, iscImplementationMermaidGraph);
    }
}
