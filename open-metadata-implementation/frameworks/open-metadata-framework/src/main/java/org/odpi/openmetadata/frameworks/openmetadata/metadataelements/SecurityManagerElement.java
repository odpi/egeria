/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SecurityManagerProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityManagerElement contains the properties and header for a software server capabilities entity retrieved from the metadata
 * repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityManagerElement implements MetadataElement
{
    private ElementHeader             elementHeader             = null;
    private SecurityManagerProperties securityManagerProperties = null;


    /**
     * Default constructor
     */
    public SecurityManagerElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityManagerElement(SecurityManagerElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            securityManagerProperties = template.getSecurityManagerProperties();
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
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the software server capability.
     *
     * @return properties bean
     */
    public SecurityManagerProperties getSecurityManagerProperties()
    {
        return securityManagerProperties;
    }


    /**
     * Set up the properties of the software server capability.
     *
     * @param securityManagerProperties properties bean
     */
    public void setSecurityManagerProperties(SecurityManagerProperties securityManagerProperties)
    {
        this.securityManagerProperties = securityManagerProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SecurityManagerElement{" +
                "elementHeader=" + elementHeader +
                ", securityManagerProperties=" + securityManagerProperties +
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
        SecurityManagerElement that = (SecurityManagerElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(securityManagerProperties, that.securityManagerProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, securityManagerProperties);
    }
}
