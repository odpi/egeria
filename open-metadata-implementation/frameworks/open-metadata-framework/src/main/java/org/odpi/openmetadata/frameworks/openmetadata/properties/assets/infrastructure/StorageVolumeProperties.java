/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StorageVolumeProperties describes the properties for a persistent storage volume that is attached to IT infrastructure or used by a data store.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StorageVolumeProperties extends ReferenceableProperties
{
    private long   storageCapacity     = 0L;
    private String units               = null;
    private long   relativeUncertainty = 0L;
    private long   absoluteUncertainty = 0L;


    /**
     * Default constructor
     */
    public StorageVolumeProperties()
    {
        super();
        super.typeName = OpenMetadataType.STORAGE_VOLUME.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StorageVolumeProperties(StorageVolumeProperties template)
    {
        super(template);

        if (template != null)
        {
            storageCapacity     = template.getStorageCapacity();
            units               = template.getUnits();
            relativeUncertainty = template.getRelativeUncertainty();
            absoluteUncertainty = template.getAbsoluteUncertainty();
        }
    }


    /**
     * Return the amount of data the volume can store.
     *
     * @return long
     */
    public long getStorageCapacity()
    {
        return storageCapacity;
    }


    /**
     * Set up the amount of data the volume can store.
     *
     * @param storageCapacity long
     */
    public void setStorageCapacity(long storageCapacity)
    {
        this.storageCapacity = storageCapacity;
    }


    /**
     * Return the units of measure used for the storage capacity.
     *
     * @return string name
     */
    public String getUnits()
    {
        return units;
    }


    /**
     * Set up the units of measure used for the storage capacity.
     *
     * @param units string name
     */
    public void setUnits(String units)
    {
        this.units = units;
    }


    /**
     * Return the range of variation in the accuracy of the storage capacity, expressed as a percentage.
     *
     * @return long
     */
    public long getRelativeUncertainty()
    {
        return relativeUncertainty;
    }


    /**
     * Set up the range of variation in the accuracy of the storage capacity, expressed as a percentage.
     *
     * @param relativeUncertainty long
     */
    public void setRelativeUncertainty(long relativeUncertainty)
    {
        this.relativeUncertainty = relativeUncertainty;
    }


    /**
     * Return the range of variation in the accuracy of the storage capacity, expressed in the units of the measurement.
     *
     * @return long
     */
    public long getAbsoluteUncertainty()
    {
        return absoluteUncertainty;
    }


    /**
     * Set up the range of variation in the accuracy of the storage capacity, expressed in the units of the measurement.
     *
     * @param absoluteUncertainty long
     */
    public void setAbsoluteUncertainty(long absoluteUncertainty)
    {
        this.absoluteUncertainty = absoluteUncertainty;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "StorageVolumeProperties{" +
                "storageCapacity=" + storageCapacity +
                ", units='" + units + '\'' +
                ", relativeUncertainty=" + relativeUncertainty +
                ", absoluteUncertainty=" + absoluteUncertainty +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        StorageVolumeProperties that = (StorageVolumeProperties) objectToCompare;
        return storageCapacity == that.storageCapacity &&
                       relativeUncertainty == that.relativeUncertainty &&
                       absoluteUncertainty == that.absoluteUncertainty &&
                       Objects.equals(units, that.units);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), storageCapacity, units, relativeUncertainty, absoluteUncertainty);
    }
}
