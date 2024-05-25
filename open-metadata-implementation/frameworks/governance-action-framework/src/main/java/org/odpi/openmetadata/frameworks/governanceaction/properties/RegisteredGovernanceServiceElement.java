/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredGovernanceServiceElement contains the properties and header for a governance service entity retrieved from the metadata
 * repository plus its supported request types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredGovernanceServiceElement
{
    private ElementHeader               elementHeader = null;
    private RegisteredGovernanceService properties    = null;


    /**
     * Default constructor
     */
    public RegisteredGovernanceServiceElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredGovernanceServiceElement(RegisteredGovernanceServiceElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredGovernanceServiceElement(GovernanceServiceElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = new RegisteredGovernanceService(template.getProperties());
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
     * Return the properties of the registered governance service.
     *
     * @return properties bean
     */
    public RegisteredGovernanceService getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the registered governance service.
     *
     * @param properties properties bean
     */
    public void setProperties(RegisteredGovernanceService properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RegisteredGovernanceService{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
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
        RegisteredGovernanceServiceElement that = (RegisteredGovernanceServiceElement) objectToCompare;
        return Objects.equals(getElementHeader(), that.getElementHeader()) &&
                       Objects.equals(getProperties(), that.getProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties);
    }
}
