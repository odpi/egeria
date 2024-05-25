/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceZoneElement is the bean used to return a governance zone definition stored in the open metadata repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceZoneElement implements MetadataElement
{
    private ElementHeader            elementHeader            = null;
    private GovernanceZoneProperties governanceZoneProperties = null;


    /**
     * Default constructor
     */
    public GovernanceZoneElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneElement(GovernanceZoneElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            governanceZoneProperties = template.getGovernanceZoneProperties();
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
     * Return the properties of the governance zone.
     *
     * @return properties bean
     */
    public GovernanceZoneProperties getGovernanceZoneProperties()
    {
        return governanceZoneProperties;
    }


    /**
     * Set up the properties of the governance zone.
     *
     * @param governanceZoneProperties properties bean
     */
    public void setGovernanceZoneProperties(GovernanceZoneProperties governanceZoneProperties)
    {
        this.governanceZoneProperties = governanceZoneProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneElement{" +
                "elementHeader=" + elementHeader +
                ", governanceZoneProperties=" + governanceZoneProperties +
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
        GovernanceZoneElement that = (GovernanceZoneElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(governanceZoneProperties, that.governanceZoneProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, governanceZoneProperties);
    }
}
