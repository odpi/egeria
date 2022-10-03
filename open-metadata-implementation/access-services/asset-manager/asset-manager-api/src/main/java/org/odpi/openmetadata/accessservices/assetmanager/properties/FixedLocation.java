/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FixedLocation describes a specific fixed physical location.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixedLocation implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String coordinates   = null;
    private String mapProjection = null;
    private String postalAddress = null;
    private String timeZone      = null;


    /**
     * Default constructor
     */
    public FixedLocation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public FixedLocation(FixedLocation template)
    {
        if (template != null)
        {
            coordinates = template.getCoordinates();
            mapProjection = template.getMapProjection();
            postalAddress = template.getPostalAddress();
            timeZone = template.getTimeZone();
        }
    }


    /**
     * Set up coordinates of the location.
     *
     * @param coordinates String
     */
    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }


    /**
     * Return the coordinates for the location.
     *
     * @return String coordinates
     */
    public String getCoordinates()
    {
        return coordinates;
    }


    /**
     * Return the name of the map projection scheme used to define the coordinates.
     *
     * @return string name
     */
    public String getMapProjection()
    {
        return mapProjection;
    }


    /**
     * Set up the name of the map projection scheme used to define the coordinates.
     *
     * @param mapProjection string name
     */
    public void setMapProjection(String mapProjection)
    {
        this.mapProjection = mapProjection;
    }


    /**
     * Set up postal address of the location.
     *
     * @param postalAddress String
     */
    public void setPostalAddress(String postalAddress)
    {
        this.postalAddress = postalAddress;
    }


    /**
     * Return the postal address for the location.
     *
     * @return String postalAddress
     */
    public String getPostalAddress()
    {
        return postalAddress;
    }


    /**
     * Set up the time zone of the location.
     *
     * @param timeZone string
     */
    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }


    /**
     * Returns the time zone of the location.
     *
     * @return string
     */
    public String getTimeZone()
    {
        return timeZone;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FixedLocation{" +
                       "coordinates='" + coordinates + '\'' +
                       ", address='" + postalAddress + '\'' +
                       ", timeZone='" + timeZone + '\'' +
                       '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        FixedLocation that = (FixedLocation) objectToCompare;
        return Objects.equals(coordinates, that.coordinates) &&
                       Objects.equals(timeZone, that.timeZone) &&
                       Objects.equals(postalAddress, that.postalAddress);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(coordinates, postalAddress, timeZone);
    }
}
