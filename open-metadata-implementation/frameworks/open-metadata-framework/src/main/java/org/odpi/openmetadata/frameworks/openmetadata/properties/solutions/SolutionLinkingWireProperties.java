/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionLinkingWireProperties identifies a relationship between solution components that is part of an information supply chain segment implementation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionLinkingWireProperties extends RelationshipProperties
{
    private String       label                              = null;
    private String       description                        = null;
    private List<String> informationSupplyChainSegmentGUIDs = null;


    /**
     * Default constructor
     */
    public SolutionLinkingWireProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionLinkingWireProperties(SolutionLinkingWireProperties template)
    {
        super(template);

        if (template != null)
        {
            this.label = template.getLabel();
            this.description = template.getDescription();
            this.informationSupplyChainSegmentGUIDs = template.getInformationSupplyChainSegmentGUIDs();
        }
    }


    /**
     * Return the display label for this relationship.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the display label for this relationship.
     *
     * @param label string
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the description for this relationship.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this relationship.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the information supply chain segments that his wire implements.
     *
     * @return list
     */
    public List<String> getInformationSupplyChainSegmentGUIDs()
    {
        return informationSupplyChainSegmentGUIDs;
    }


    /**
     * Set up the information supply chain segments that his wire implements.
     *
     * @param informationSupplyChainSegmentGUIDs list
     */
    public void setInformationSupplyChainSegmentGUIDs(List<String> informationSupplyChainSegmentGUIDs)
    {
        this.informationSupplyChainSegmentGUIDs = informationSupplyChainSegmentGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionLinkingWireProperties{" +
                "informationSupplyChainSegmentGUIDs=" + informationSupplyChainSegmentGUIDs +
                "label='" + label + '\'' +
                "description='" + description + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof SolutionLinkingWireProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(label, that.label) &&
                Objects.equals(description, that.description) &&
                Objects.equals(informationSupplyChainSegmentGUIDs, that.informationSupplyChainSegmentGUIDs);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), label, description, informationSupplyChainSegmentGUIDs);
    }
}
