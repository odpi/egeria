/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataSourceMeasurementAnnotation describes properties that describe the physical characteristics of a data source.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourcePhysicalStatusAnnotationProperties extends ResourceMeasureAnnotationProperties
{
    private Date   resourceCreateTime       = null;
    private Date   resourceUpdateTime       = null;
    private Date   resourceLastAccessedTime = null;
    private long   size                     = 0L;
    private String encodingType             = null;


    /**
     * Default constructor
     */
    public ResourcePhysicalStatusAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName);
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ResourcePhysicalStatusAnnotationProperties(ResourcePhysicalStatusAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            resourceCreateTime       = template.getResourceCreateTime();
            resourceUpdateTime       = template.getResourceUpdateTime();
            resourceLastAccessedTime = template.getResourceLastAccessedTime();
            size         = template.getSize();
            encodingType = template.getEncodingType();
        }
    }


    /**
     * Return the date and time that the data source was created.
     *
     * @return date time
     */
    public Date getResourceCreateTime()
    {
        return resourceCreateTime;
    }


    /**
     * Set up the date and time that the data source was created.
     *
     * @param resourceCreateTime date time
     */
    public void setResourceCreateTime(Date resourceCreateTime)
    {
        this.resourceCreateTime = resourceCreateTime;
    }


    /**
     * Return the time that the file was last modified.
     *
     * @return userId
     */
    public Date getResourceUpdateTime()
    {
        return resourceUpdateTime;
    }


    /**
     * Set up the time that the file was last modified.
     *
     * @param resourceUpdateTime date time
     */
    public void setResourceUpdateTime(Date resourceUpdateTime)
    {
        this.resourceUpdateTime = resourceUpdateTime;
    }


    /**
     * Return the last time that the resource was accessed.
     *
     * @return date time
     */
    public Date getResourceLastAccessedTime()
    {
        return resourceLastAccessedTime;
    }


    /**
     * Set up the last time that the resource was accessed.
     *
     * @param resourceLastAccessedTime date time
     */
    public void setResourceLastAccessedTime(Date resourceLastAccessedTime)
    {
        this.resourceLastAccessedTime = resourceLastAccessedTime;
    }


    /**
     * Return the size in bytes of the data source.
     *
     * @return int
     */
    public long getSize()
    {
        return size;
    }


    /**
     * Set up the size in bytes of the data source.
     *
     * @param size int
     */
    public void setSize(long size)
    {
        this.size = size;
    }


    /**
     * Return the encoding of the data source.
     *
     * @return encoding format description
     */
    public String getEncodingType()
    {
        return encodingType;
    }


    /**
     * Set up the encoding of the data source.
     *
     * @param encodingType encoding format description
     */
    public void setEncodingType(String encodingType)
    {
        this.encodingType = encodingType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ResourcePhysicalStatusAnnotationProperties{" +
                "resourceCreateTime=" + resourceCreateTime +
                ", resourceUpdateTime=" + resourceUpdateTime +
                ", resourceLastAccessedTime=" + resourceLastAccessedTime +
                ", size=" + size +
                ", encodingType='" + encodingType + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ResourcePhysicalStatusAnnotationProperties that = (ResourcePhysicalStatusAnnotationProperties) objectToCompare;
        return getSize() == that.getSize() &&
                       Objects.equals(getResourceCreateTime(), that.getResourceCreateTime()) &&
                       Objects.equals(getResourceUpdateTime(), that.getResourceUpdateTime()) &&
                       Objects.equals(getResourceLastAccessedTime(), that.getResourceLastAccessedTime()) &&
                       Objects.equals(getEncodingType(), that.getEncodingType());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceCreateTime, resourceUpdateTime, resourceLastAccessedTime, size, encodingType);
    }
}
