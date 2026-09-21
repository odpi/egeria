/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionProcessElement contains the properties and header for a governance action process entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessElement
{
    private ElementHeader                            elementHeader               = null;
    private GovernanceActionProcess                  properties                  = null;
    private Map<String, List<SpecificationProperty>> specification               = null;
    private List<PredefinedActionTarget>             predefinedActionTargets     = null;
    private Map<String, String>                      predefinedRequestParameters = null;
    private String                                   specificationMermaidGraph   = null;


    /**
     * Default constructor
     */
    public GovernanceActionProcessElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessElement(GovernanceActionProcessElement template)
    {
        if (template != null)
        {
            elementHeader           = template.getElementHeader();
            properties              = template.getProperties();
            predefinedActionTargets = template.getPredefinedActionTargets();
            predefinedRequestParameters = template.getPredefinedRequestParameters();
            specification             = template.getSpecification();
            specificationMermaidGraph = template.getSpecificationMermaidGraph();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }

    /**
     * Return details of the process
     *
     * @return process properties
     */
    public GovernanceActionProcess getProperties()
    {
        return properties;
    }


    /**
     * Set up process properties
     *
     * @param properties process properties
     */
    public void setProperties(GovernanceActionProcess properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of predefined action targets for this governance action.
     *
     * @return list
     */
    public List<PredefinedActionTarget> getPredefinedActionTargets()
    {
        return predefinedActionTargets;
    }


    /**
     * Set up the list of predefined action targets for this governance action.
     *
     * @param predefinedActionTargets list
     */
    public void setPredefinedActionTargets(List<PredefinedActionTarget> predefinedActionTargets)
    {
        this.predefinedActionTargets = predefinedActionTargets;
    }


    /**
     * Return any pre-defined request parameters.
     *
     * @return map
     */
    public Map<String, String> getPredefinedRequestParameters()
    {
        return predefinedRequestParameters;
    }


    /**
     * Set up any pre-defined request parameters.
     *
     * @param predefinedRequestParameters map
     */
    public void setPredefinedRequestParameters(Map<String, String> predefinedRequestParameters)
    {
        this.predefinedRequestParameters = predefinedRequestParameters;
    }


    /**
     * Return the specification for the governance action.
     *
     * @return specification map
     */
    public Map<String, List<SpecificationProperty>> getSpecification()
    {
        return specification;
    }


    /**
     * Set up the specification for the governance action.
     *
     * @param specification specification map
     */
    public void setSpecification(Map<String, List<SpecificationProperty>> specification)
    {
        this.specification = specification;
    }


    /**
     * Return the mermaid string used to render a specification.
     *
     * @return string in Mermaid markdown
     */
    public String getSpecificationMermaidGraph()
    {
        return specificationMermaidGraph;
    }


    /**
     * Set up mermaid string used to render a graph.
     *
     * @param specificationMermaidGraph string in Mermaid markdown
     */
    public void setSpecificationMermaidGraph(String specificationMermaidGraph)
    {
        this.specificationMermaidGraph = specificationMermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProcessElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", specification=" + specification +
                ", predefinedActionTargets=" + predefinedActionTargets +
                ", predefinedRequestParameters=" + predefinedRequestParameters +
                ", specificationMermaidGraph='" + specificationMermaidGraph + '\'' +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        GovernanceActionProcessElement that = (GovernanceActionProcessElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(predefinedActionTargets, that.predefinedActionTargets) &&
                Objects.equals(predefinedRequestParameters, that.predefinedRequestParameters) &&
                Objects.equals(specification, that.specification) &&
                Objects.equals(specificationMermaidGraph, that.specificationMermaidGraph);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, predefinedActionTargets,
                            predefinedRequestParameters, specification, specificationMermaidGraph);
    }
}
