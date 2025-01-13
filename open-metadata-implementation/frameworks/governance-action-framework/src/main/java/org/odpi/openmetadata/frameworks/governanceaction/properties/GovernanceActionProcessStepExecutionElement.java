/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionProcessStepElement contains the properties and header for a governance action process step entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessStepExecutionElement
{
    private ElementHeader                          elementHeader         = null;
    private GovernanceActionProcessStepExecution   processStepProperties = null;
    private Map<String, List<Map<String, String>>> specification        = null;
    private String                                 mermaidSpecification = null;


    /**
     * Default constructor
     */
    public GovernanceActionProcessStepExecutionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepExecutionElement(GovernanceActionProcessStepExecutionElement template)
    {
        if (template != null)
        {
            elementHeader         = template.getElementHeader();
            processStepProperties = template.getProcessStepProperties();
            specification         = template.getSpecification();
            mermaidSpecification  = template.getMermaidSpecification();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepExecutionElement(GovernanceActionProcessStepElement template)
    {
        if (template != null)
        {
            elementHeader         = template.getElementHeader();
            processStepProperties = new GovernanceActionProcessStepExecution(template.getProcessStepProperties());
            specification         = template.getSpecification();
            mermaidSpecification  = template.getMermaidSpecification();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepExecutionElement(EngineActionElement template)
    {
        if (template != null)
        {
            elementHeader         = template.getElementHeader();
            processStepProperties = new GovernanceActionProcessStepExecution(template);
            specification         = null;
            mermaidSpecification  = null;
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
    public GovernanceActionProcessStepExecution getProcessStepProperties()
    {
        return processStepProperties;
    }


    /**
     * Set up process properties
     *
     * @param processStepProperties process properties
     */
    public void setProcessStepProperties(GovernanceActionProcessStepExecution processStepProperties)
    {
        this.processStepProperties = processStepProperties;
    }


    /**
     * Return the specification for the governance action.
     *
     * @return specification map
     */
    public Map<String, List<Map<String, String>>> getSpecification()
    {
        return specification;
    }


    /**
     * Set up the specification for the governance action.
     *
     * @param specification specification map
     */
    public void setSpecification(Map<String, List<Map<String, String>>> specification)
    {
        this.specification = specification;
    }


    /**
     * Return the mermaid string used to render a specification.
     *
     * @return string in Mermaid markdown
     */
    public String getMermaidSpecification()
    {
        return mermaidSpecification;
    }


    /**
     * Set up mermaid string used to render a graph.
     *
     * @param mermaidSpecification string in Mermaid markdown
     */
    public void setMermaidSpecification(String mermaidSpecification)
    {
        this.mermaidSpecification = mermaidSpecification;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProcessStepExecutionElement{" +
                "elementHeader=" + elementHeader +
                ", processStepProperties=" + processStepProperties +
                ", specification=" + specification +
                ", mermaidSpecification='" + mermaidSpecification + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceActionProcessStepExecutionElement that = (GovernanceActionProcessStepExecutionElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(processStepProperties, that.processStepProperties) &&
                Objects.equals(specification, that.specification) &&
                Objects.equals(mermaidSpecification, that.mermaidSpecification);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, processStepProperties, specification, mermaidSpecification);
    }
}
