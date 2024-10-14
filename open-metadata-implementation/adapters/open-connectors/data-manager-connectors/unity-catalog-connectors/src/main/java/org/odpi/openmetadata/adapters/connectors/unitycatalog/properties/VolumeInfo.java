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
 * Description of a volume.  These are the values that are returned from UC.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VolumeInfo extends VolumeProperties implements ElementBase
{
    private long   created_at = 0L;
    private String created_by = null;
    private long   updated_at = 0L;
    private String updated_by = null;
    private String volume_id = null;
    private String full_name   = null;

    /**
     * Constructor
     */
    public VolumeInfo()
    {
    }


    /**
     * Return the time that the element was created.
     *
     * @return date/time as long
     */
    @Override
    public long getCreated_at()
    {
        return created_at;
    }


    /**
     * Set up the time that the element was created.
     *
     * @param created_at date/time as long
     */
    @Override
    public void setCreated_at(long created_at)
    {
        this.created_at = created_at;
    }


    /**
     * Return the time that the element was last updated.
     *
     * @return date/time as long
     */
    @Override
    public long getUpdated_at()
    {
        return updated_at;
    }


    /**
     * Set up the time that the element was last updated.
     *
     * @param updated_at  date/time as long
     */
    @Override
    public void setUpdated_at(long updated_at)
    {
        this.updated_at = updated_at;
    }


    /**
     * Return the userId that created the element.
     *
     * @return string name
     */
    @Override
    public String getCreated_by()
    {
        return created_by;
    }


    /**
     * Set up the userId that created the element.
     *
     * @param created_by string name
     */
    @Override
    public void setCreated_by(String created_by)
    {
        this.created_by = created_by;
    }


    /**
     * Return the element that last updated the element.
     *
     * @return string name
     */
    @Override
    public String getUpdated_by()
    {
        return updated_by;
    }


    /**
     * Set up the element that last updated the element.
     *
     * @param updated_by string name
     */
    @Override
    public void setUpdated_by(String updated_by)
    {
        this.updated_by = updated_by;
    }


    /**
     * Return the internal identifier of the volume.
     *
     * @return string
     */
    public String getVolume_id()
    {
        return volume_id;
    }


    /**
     * Set up the internal identifier of the volume.
     *
     * @param volume_id string
     */
    public void setVolume_id(String volume_id)
    {
        this.volume_id = volume_id;
    }


    /**
     * Return the fully qualified name of the volume.
     * 
     * @return string
     */
    public String getFull_name()
    {
        return full_name;
    }


    /**
     * Set up the full qualified name of the volume.
     * 
     * @param full_name string
     */
    public void setFull_name(String full_name)
    {
        this.full_name = full_name;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "VolumeInfo{" +
                "created_at=" + created_at +
                ", created_by='" + created_by + '\'' +
                ", updated_at=" + updated_at +
                ", updated_by='" + updated_by + '\'' +
                ", full_name=" + full_name +
                ", volume_id='" + volume_id + '\'' +
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
        VolumeInfo that = (VolumeInfo) objectToCompare;
        return created_at == that.created_at &&
                Objects.equals(created_by, that.created_by) &&
                updated_at == that.updated_at &&
                Objects.equals(updated_by, that.updated_by) &&
                Objects.equals(volume_id, that.volume_id)
                && Objects.equals(full_name, that.full_name);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), created_at, created_by, updated_at, updated_by, volume_id, full_name);
    }
}
