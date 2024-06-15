/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateEffectivityDatesRequestBody provides a structure for passing the effectivity dates for a metadata element, classification or relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateEffectivityDatesRequestBody extends UpdateRequestBody
{
    private Date              effectiveFrom = null;
    private Date              effectiveTo   = null;


    /**
     * Default constructor
     */
    public UpdateEffectivityDatesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateEffectivityDatesRequestBody(UpdateEffectivityDatesRequestBody template)
    {
        super(template);

        if (template != null)
        {
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
        }
    }


    /**
     * Return the date/time that this new element becomes effective in the governance program (null means immediately).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this new element becomes effective in the governance program (null means immediately).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time when the new element is no longer effective in the  governance program (null means until deleted).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set up the date/time when the new element is no longer effective in the  governance program (null means until deleted).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UpdateEffectivityDatesRequestBody{" +
                       "effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", forLineage=" + getForLineage() +
                       ", forDuplicateProcessing=" + getForDuplicateProcessing() +
                       ", effectiveTime=" + getEffectiveTime() +
                       '}';
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
        UpdateEffectivityDatesRequestBody that = (UpdateEffectivityDatesRequestBody) objectToCompare;
        return Objects.equals(effectiveFrom, that.effectiveFrom) &&
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
        return Objects.hash(super.hashCode(), effectiveFrom, effectiveTo);
    }
}
