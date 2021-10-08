/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.LocationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LocationElement contains the properties and header for a location definition retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LocationElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader      elementHeader = null;
    private LocationProperties locationProperties = null;


    /**
     * Default constructor
     */
    public LocationElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LocationElement(LocationElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            locationProperties = template.getLocationProperties();
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
     * Return the properties for the location definition.
     *
     * @return properties bean
     */
    public LocationProperties getLocationProperties()
    {
        return locationProperties;
    }


    /**
     * Set up the properties for the location definition.
     *
     * @param locationProperties properties bean
     */
    public void setLocationProperties(LocationProperties locationProperties)
    {
        this.locationProperties = locationProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LocationElement{" +
                "elementHeader=" + elementHeader +
                ", locationProperties=" + locationProperties +
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
        LocationElement that = (LocationElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(locationProperties, that.locationProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, locationProperties);
    }
}
