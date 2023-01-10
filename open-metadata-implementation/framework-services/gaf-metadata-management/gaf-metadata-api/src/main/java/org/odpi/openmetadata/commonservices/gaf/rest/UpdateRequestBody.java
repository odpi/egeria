/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateRequestBody provides a structure for passing the common request parameters for updating metadata elements, relationships or classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateRequestBody extends MetadataSourceRequestBody
{
    private static final long    serialVersionUID = 1L;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;
    private Date    effectiveTime          = null;


    /**
     * Default constructor
     */
    public UpdateRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateRequestBody(UpdateRequestBody template)
    {
        super(template);

        if (template != null)
        {
            forLineage = template.getForLineage();
            forDuplicateProcessing = template.getForDuplicateProcessing();
            effectiveTime = template.getEffectiveTime();
        }
    }


    /**
     * Return whether this request is to update lineage memento elements.
     *
     * @return flag
     */
    public boolean getForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether this request is to update lineage memento elements.
     *
     * @param forLineage flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /**
     * Return whether this request is updating an element as part of a deduplication exercise.
     *
     * @return flag
     */
    public boolean getForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether this request is updating an element as part of a deduplication exercise.
     *
     * @param forDuplicateProcessing flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * Return the effective time that this update is to occur in.
     *
     * @return date/time
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up the effective time that this update is to occur in.
     *
     * @param effectiveTime date/time
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UpdateRequestBody{" +
                       "forLineage=" + forLineage +
                       ", forDuplicateProcessing=" + forDuplicateProcessing +
                       ", effectiveTime=" + effectiveTime +
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
        UpdateRequestBody that = (UpdateRequestBody) objectToCompare;
        return forLineage == that.forLineage &&
                       forDuplicateProcessing == that.forDuplicateProcessing &&
                       Objects.equals(effectiveTime, that.effectiveTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), forLineage, forDuplicateProcessing, effectiveTime);
    }
}
