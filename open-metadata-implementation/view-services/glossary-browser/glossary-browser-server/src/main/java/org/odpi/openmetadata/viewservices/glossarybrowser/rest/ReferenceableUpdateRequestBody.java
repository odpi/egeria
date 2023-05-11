/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableUpdateRequestBody carries the correlation properties, readVersionIdentifier and effective time for a create (with parent),
 * update or delete requests.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceableUpdateRequestBody extends ReferenceableRequestBody
{
    private Date   effectiveTime     = null;
    private String updateDescription = null;

    /**
     * Default constructor
     */
    public ReferenceableUpdateRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceableUpdateRequestBody(ReferenceableUpdateRequestBody template)
    {
        super(template);

        if (template != null)
        {
            effectiveTime = template.getEffectiveTime();
            updateDescription = template.getUpdateDescription();
        }
    }


    /**
     * Return the date/time to use for the query.
     *
     * @return date object
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up  the date/time to use for the query.
     *
     * @param effectiveTime date object
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }


    /**
     * Return the string that describes details of the update.
     *
     * @return description
     */
    public String getUpdateDescription()
    {
        return updateDescription;
    }


    /**
     * Set up the string that describes details of the update.
     *
     * @param updateDescription description
     */
    public void setUpdateDescription(String updateDescription)
    {
        this.updateDescription = updateDescription;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceableUpdateRequestBody{" +
                       "effectiveTime=" + effectiveTime +
                       ", updateDescription='" + updateDescription + '\'' +
                       ", elementProperties=" + getElementProperties() +
                       ", parentGUID='" + getParentGUID() + '\'' +
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
        if (! (objectToCompare instanceof ReferenceableUpdateRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(effectiveTime, that.effectiveTime) &&
                Objects.equals(updateDescription, that.updateDescription);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), effectiveTime, updateDescription);
    }
}
