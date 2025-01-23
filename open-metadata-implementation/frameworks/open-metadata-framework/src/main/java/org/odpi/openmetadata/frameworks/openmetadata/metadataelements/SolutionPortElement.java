/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SolutionPortElement contains the properties and header for a SolutionPortElement entity retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionPortElement implements MetadataElement
{
    private ElementHeader          elementHeader   = null;
    private SolutionPortProperties properties      = null;
    private ElementStub            schemaType      = null;
    private List<ElementStub>      delegationPorts = null;


    /**
     * Default constructor
     */
    public SolutionPortElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionPortElement(SolutionPortElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties  = template.getProperties();
            schemaType  = template.getSchemaType();
            delegationPorts  = template.getDelegationPorts();
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
    public SolutionPortProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties from the relationship.
     *
     * @param properties property map
     */
    public void setProperties(SolutionPortProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the stub for the optional schema type describing the structure of the data passing through the port.
     *
     * @return header
     */
    public ElementStub getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up the stub for the optional schema type describing the structure of the data passing through the port.
     *
     * @param schemaType  header
     */
    public void setSchemaType(ElementStub schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the list of ports that belong to subcomponents.
     *
     * @return list
     */
    public List<ElementStub> getDelegationPorts()
    {
        return delegationPorts;
    }


    /**
     * Set up the list of ports that belong to subcomponents.
     *
     * @param delegationPorts list
     */
    public void setDelegationPorts(List<ElementStub> delegationPorts)
    {
        this.delegationPorts = delegationPorts;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionPortElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", schemaType=" + schemaType +
                ", delegationPorts=" + delegationPorts +
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
        SolutionPortElement that = (SolutionPortElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(delegationPorts, that.delegationPorts) &&
                       Objects.equals(schemaType, that.schemaType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, schemaType, delegationPorts);
    }
}
