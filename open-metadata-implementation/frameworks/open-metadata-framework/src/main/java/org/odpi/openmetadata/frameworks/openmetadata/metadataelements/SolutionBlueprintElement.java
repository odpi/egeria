/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionBlueprintElement contains the properties and header for a solution blueprint and its components retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionBlueprintElement implements MetadataElement
{
    private ElementHeader                  elementHeader      = null;
    private SolutionBlueprintProperties    properties         = null;
    private List<SolutionComponentElement> solutionComponents = null;


    /**
     * Default constructor
     */
    public SolutionBlueprintElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionBlueprintElement(SolutionBlueprintElement template)
    {
        if (template != null)
        {
            elementHeader      = template.getElementHeader();
            properties         = template.getProperties();
            solutionComponents = template.getSolutionComponents();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param header element header object
     */
    @Override
    public void setElementHeader(ElementHeader header)
    {
        this.elementHeader = header;
    }


    /**
     * Return the properties of the role.
     *
     * @return properties
     */
    public SolutionBlueprintProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the role properties.
     *
     * @param properties  properties
     */
    public void setProperties(SolutionBlueprintProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the relationships to solution components.
     *
     * @return list of element stubs
     */
    public List<SolutionComponentElement> getSolutionComponents()
    {
        return solutionComponents;
    }


    /**
     * Set up details of the relationships to solution components.
     *
     * @param solutionComponents relationship details
     */
    public void setSolutionComponents(List<SolutionComponentElement> solutionComponents)
    {
        this.solutionComponents = solutionComponents;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprintElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", solutionComponents=" + solutionComponents +
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
        SolutionBlueprintElement that = (SolutionBlueprintElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(solutionComponents, that.solutionComponents);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, solutionComponents);
    }
}
