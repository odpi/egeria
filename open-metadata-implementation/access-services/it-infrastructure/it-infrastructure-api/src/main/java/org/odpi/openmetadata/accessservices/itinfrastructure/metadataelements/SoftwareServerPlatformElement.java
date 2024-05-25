/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerPlatformProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * SoftwareServerPlatformElement contains the properties and header for a software server platform retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServerPlatformElement implements MetadataElement
{
    private SoftwareServerPlatformProperties properties    = null;
    private ElementHeader                    elementHeader = null;


    /**
     * Default constructor
     */
    public SoftwareServerPlatformElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareServerPlatformElement(SoftwareServerPlatformElement template)
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
    public SoftwareServerPlatformElement(AssetElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();

            AssetProperties assetProperties = template.getProperties();

            if (assetProperties != null)
            {
                properties = new SoftwareServerPlatformProperties(assetProperties);
            }
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
     * Return the properties for the software server platform.
     *
     * @return asset properties (using appropriate subclass)
     */
    public SoftwareServerPlatformProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the software server platform.
     *
     * @param properties asset properties
     */
    public void setProperties(SoftwareServerPlatformProperties properties)
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
        return "SoftwareServerPlatformElement{" +
                       "properties=" + properties +
                       ", elementHeader=" + elementHeader +
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
        SoftwareServerPlatformElement that = (SoftwareServerPlatformElement) objectToCompare;
        return Objects.equals(getProperties(), that.getProperties()) &&
                       Objects.equals(getElementHeader(), that.getElementHeader());
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
