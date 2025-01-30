/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SolutionBlueprintComponent contains the properties and header for a SolutionBlueprintComposition relationship
 * plus linked solution component retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionBlueprintComponent implements MetadataElement
{
    private ElementHeader                          elementHeader     = null;
    private SolutionBlueprintCompositionProperties properties        = null;
    private SolutionComponentElement               solutionComponent = null;


    /**
     * Default constructor
     */
    public SolutionBlueprintComponent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionBlueprintComponent(SolutionBlueprintComponent template)
    {
        if (template != null)
        {
            elementHeader     = template.getElementHeader();
            properties        = template.getProperties();
            solutionComponent = template.getSolutionComponent();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the relationship.
     *
     * @return properties
     */
    public SolutionBlueprintCompositionProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties from the relationship.
     *
     * @param properties property map
     */
    public void setProperties(SolutionBlueprintCompositionProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the component entity.
     *
     * @return header
     */
    public SolutionComponentElement getSolutionComponent()
    {
        return solutionComponent;
    }


    /**
     * Set up the component entity.
     *
     * @param solutionComponent  header
     */
    public void setSolutionComponent(SolutionComponentElement solutionComponent)
    {
        this.solutionComponent = solutionComponent;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprintComponent{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", solutionComponent=" + solutionComponent +
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
        SolutionBlueprintComponent that = (SolutionBlueprintComponent) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(solutionComponent, that.solutionComponent);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, solutionComponent);
    }
}
