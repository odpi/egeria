/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalLocationProperties carries the parameters for marking a location as a digital (cyber) location.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalLocationProperties extends ClassificationProperties
{
    private static final long    serialVersionUID = 1L;

    private String networkAddress = null;


    /**
     * Default constructor
     */
    public DigitalLocationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalLocationProperties(DigitalLocationProperties template)
    {
        super(template);

        if (template != null)
        {
            networkAddress = template.getNetworkAddress();
        }
    }


    /**
     * Return the network address used to connect to the location.
     *
     * @return string name - often a URL
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Set up the network address used to connect to the location.
     *
     * @param networkAddress string name - often a URL
     */
    public void setNetworkAddress(String networkAddress)
    {
        this.networkAddress = networkAddress;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DigitalLocationProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", networkAddress='" + networkAddress + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DigitalLocationProperties that = (DigitalLocationProperties) objectToCompare;
        return Objects.equals(networkAddress, that.networkAddress);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), networkAddress);
    }
}
