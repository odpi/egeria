/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.locations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FixedLocationProperties carries the parameters for marking a location as a fixed physical location.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FixedLocationProperties extends ClassificationBeanProperties
{
    private String coordinates   = null;
    private String mapProjection = null;
    private String postalAddress = null;
    private String timeZone      = null;



    /**
     * Default constructor
     */
    public FixedLocationProperties()
    {
        super();
        super.typeName = OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FixedLocationProperties(FixedLocationProperties template)
    {
        super(template);

        if (template != null)
        {
            coordinates = template.getCoordinates();
            mapProjection = template.getMapProjection();
            postalAddress = template.getPostalAddress();
            timeZone = template.getTimeZone();
        }
    }


    /**
     * Return the position of the location.
     *
     * @return string coordinates
     */
    public String getCoordinates()
    {
        return coordinates;
    }


    /**
     * Set up the position of the location.
     *
     * @param coordinates string coordinates
     */
    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }


    /**
     * Return the map projection used to define the coordinates.
     *
     * @return name
     */
    public String getMapProjection()
    {
        return mapProjection;
    }


    /**
     * Set up the map projection used to define the coordinates.
     *
     * @param mapProjection name
     */
    public void setMapProjection(String mapProjection)
    {
        this.mapProjection = mapProjection;
    }


    /**
     * Return the postal address of the location (if appropriate).
     *
     * @return address
     */
    public String getPostalAddress()
    {
        return postalAddress;
    }


    /**
     * Set up the postal address of the location (if appropriate).
     *
     * @param postalAddress address
     */
    public void setPostalAddress(String postalAddress)
    {
        this.postalAddress = postalAddress;
    }


    /**
     * Return the time zone for the location.
     *
     * @return timezone identifier
     */
    public String getTimeZone()
    {
        return timeZone;
    }


    /**
     * Set up the time zone for the location.
     *
     * @param timeZone timezone identifier
     */
    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FixedLocationProperties{" +
                "coordinates='" + coordinates + '\'' +
                ", mapProjection='" + mapProjection + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", timeZone='" + timeZone + '\'' +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        FixedLocationProperties that = (FixedLocationProperties) objectToCompare;
        return Objects.equals(coordinates, that.coordinates) &&
                       Objects.equals(mapProjection, that.mapProjection) &&
                       Objects.equals(postalAddress, that.postalAddress) &&
                       Objects.equals(timeZone, that.timeZone);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), coordinates, mapProjection, postalAddress, timeZone);
    }
}
