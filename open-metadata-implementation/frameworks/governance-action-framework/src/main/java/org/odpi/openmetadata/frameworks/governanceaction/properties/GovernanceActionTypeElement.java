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
 * GovernanceActionTypeElement contains the properties and header for a governance action type entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionTypeElement
{
    private ElementHeader                          elementHeader        = null;
    private GovernanceActionTypeProperties         actionTypeProperties = null;
    private Map<String, List<Map<String, String>>> specification        = null;
    private String                                 mermaidSpecification = null;


    /**
     * Default constructor
     */
    public GovernanceActionTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionTypeElement(GovernanceActionTypeElement template)
    {
        if (template != null)
        {
            elementHeader        = template.getElementHeader();
            actionTypeProperties = template.getActionTypeProperties();
            specification        = template.getSpecification();
            mermaidSpecification = template.getMermaidSpecification();
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
    public GovernanceActionTypeProperties getActionTypeProperties()
    {
        return actionTypeProperties;
    }


    /**
     * Set up process properties
     *
     * @param actionTypeProperties process properties
     */
    public void setActionTypeProperties(GovernanceActionTypeProperties actionTypeProperties)
    {
        this.actionTypeProperties = actionTypeProperties;
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
        return "GovernanceActionTypeElement{" +
                "elementHeader=" + elementHeader +
                ", actionTypeProperties=" + actionTypeProperties +
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
        GovernanceActionTypeElement that = (GovernanceActionTypeElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(actionTypeProperties, that.actionTypeProperties) &&
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
        return Objects.hash(super.hashCode(), elementHeader, actionTypeProperties, specification, mermaidSpecification);
    }
}
