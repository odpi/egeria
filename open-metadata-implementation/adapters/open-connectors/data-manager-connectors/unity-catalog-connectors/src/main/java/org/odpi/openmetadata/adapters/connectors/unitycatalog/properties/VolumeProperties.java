/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Properties of a volume (collection of files).  Maps to CreateVolumeRequestContent.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VolumeProperties extends StoredDataProperties
{
    private VolumeType        volume_type         = null;

    /**
     * Constructor
     */
    public VolumeProperties()
    {
    }


    /**
     * Return the type of volume.
     *
     * @return enum
     */
    public VolumeType getVolume_type()
    {
        return volume_type;
    }


    /**
     * Set upi the type of volume.
     *
     * @param volume_type volume type
     */
    public void setVolume_type(VolumeType volume_type)
    {
        this.volume_type = volume_type;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "VolumeProperties{" +
                "volume_type=" + volume_type +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        VolumeProperties that = (VolumeProperties) objectToCompare;
        return volume_type == that.volume_type;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), volume_type);
    }
}
