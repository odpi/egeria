/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalSourceRequestBody carries the parameters for marking an asset or schema as external.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AppointmentProperties implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private Date    effectiveFrom = null;
    private Date    effectiveTo   = null;
    private boolean isPublic      = false;


    /**
     * Default constructor
     */
    public AppointmentProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AppointmentProperties(AppointmentProperties template)
    {
        if (template != null)
        {
            effectiveFrom        = template.getEffectiveFrom();
            effectiveTo          = template.getEffectiveTo();
            isPublic             = template.getIsPublic();
        }
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return if the contents of this profile be shared with colleagues.
     *
     * @return flag
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up if the contents of this profile be shared with colleagues.
     *
     * @param isPublic flag
     */
    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AppointmentProperties{" +
                       "effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", isPublic=" + isPublic +
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
        AppointmentProperties that = (AppointmentProperties) objectToCompare;
        return isPublic == that.isPublic &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), effectiveFrom, effectiveTo, isPublic);
    }
}
