/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataSourceRequestBody carries the parameters for marking an element, classification or relationship as external
 * and for working with lineage/duplicate processing and effective time.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataSourceRequestBody
{
    private String  externalSourceGUID     = null;
    private String  externalSourceName     = null;
    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;
    private Date    effectiveTime          = null;

    /**
     * Default constructor
     */
    public MetadataSourceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetadataSourceRequestBody(MetadataSourceRequestBody template)
    {
        if (template != null)
        {
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();

            forLineage = template.getForLineage();
            forDuplicateProcessing = template.getForDuplicateProcessing();
            effectiveTime = template.getEffectiveTime();
        }
    }


    /**
     * Return the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @return string guid
     */
    public String getExternalSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Set up the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @param externalSourceGUID string guid
     */
    public void setExternalSourceGUID(String externalSourceGUID)
    {
        this.externalSourceGUID = externalSourceGUID;
    }


    /**
     * Return the unique name of the software server capability entity that represented the external source.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /**
     * Set up the unique name of the software server capability entity that represented the external source.
     *
     * @param externalSourceName string name
     */
    public void setExternalSourceName(String externalSourceName)
    {
        this.externalSourceName = externalSourceName;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MetadataSourceRequestBody{" +
                "externalSourceGUID='" + externalSourceGUID + '\'' +
                ", externalSourceName='" + externalSourceName + '\'' +
                ", forLineage=" + forLineage +
                ", forDuplicateProcessing=" + forDuplicateProcessing +
                ", effectiveTime=" + effectiveTime +  '}';
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
        MetadataSourceRequestBody that = (MetadataSourceRequestBody) objectToCompare;
        return forLineage == that.forLineage &&
                forDuplicateProcessing == that.forDuplicateProcessing &&
                Objects.equals(effectiveTime, that.effectiveTime) &&
                Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                Objects.equals(externalSourceName, that.externalSourceName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName,
                            forLineage, forDuplicateProcessing, effectiveTime);
    }
}
