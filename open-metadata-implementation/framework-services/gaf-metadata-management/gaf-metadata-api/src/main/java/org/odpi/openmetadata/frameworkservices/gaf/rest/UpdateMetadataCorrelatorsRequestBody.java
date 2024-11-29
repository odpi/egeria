/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateRequestBody carries the correlation properties and effective time for a create, update or delete request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateMetadataCorrelatorsRequestBody
{
    private MetadataCorrelationProperties metadataCorrelationProperties = null;
    private Date                          effectiveFrom                 = null;
    private Date                          effectiveTo                   = null;
    private Date                          effectiveTime                 = null;

    /**
     * Default constructor
     */
    public UpdateMetadataCorrelatorsRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateMetadataCorrelatorsRequestBody(UpdateMetadataCorrelatorsRequestBody template)
    {
        if (template != null)
        {
            metadataCorrelationProperties = template.getMetadataCorrelationProperties();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
            effectiveTime = template.getEffectiveTime();
        }
    }


    /**
     * Return the properties used to correlate the external metadata element with the open metadata element.
     *
     * @return properties object
     */
    public MetadataCorrelationProperties getMetadataCorrelationProperties()
    {
        return metadataCorrelationProperties;
    }


    /**
     * Set up the properties used to correlate the external metadata element with the open metadata element.
     *
     * @param metadataCorrelationProperties properties object
     */
    public void setMetadataCorrelationProperties(MetadataCorrelationProperties metadataCorrelationProperties)
    {
        this.metadataCorrelationProperties = metadataCorrelationProperties;
    }


    /**
     * Return the date/time that this new classification becomes effective in the governance program (null means immediately).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this new classification becomes effective in the governance program (null means immediately).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time when the new classification is no longer effective in the  governance program (null means until deleted).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set up the date/time when the new classification is no longer effective in the  governance program (null means until deleted).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UpdateMetadataCorrelatorsRequestBody{" +
                "metadataCorrelationProperties=" + metadataCorrelationProperties +
                ", effectiveTo=" + effectiveTo +
                ", effectiveFrom=" + effectiveFrom +
                ", effectiveTime=" + effectiveTime +
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
        UpdateMetadataCorrelatorsRequestBody that = (UpdateMetadataCorrelatorsRequestBody) objectToCompare;
        return Objects.equals(metadataCorrelationProperties, that.metadataCorrelationProperties) &&
                Objects.equals(effectiveFrom, that.effectiveFrom) &&
                Objects.equals(effectiveTo, that.effectiveTo) &&
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
        return Objects.hash(super.hashCode(), metadataCorrelationProperties, effectiveFrom, effectiveTo, effectiveTime);
    }
}
