/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

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
    private List<String> informationSupplyChainSegmentGUIDs = null;


    /**
     * Default constructor
     */
    public SolutionLinkingWireProperties()
    {
        super();
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
            this.informationSupplyChainSegmentGUIDs = template.getInformationSupplyChainSegmentGUIDs();
        }
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
        return Objects.equals(informationSupplyChainSegmentGUIDs, that.informationSupplyChainSegmentGUIDs);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), informationSupplyChainSegmentGUIDs);
    }
}
