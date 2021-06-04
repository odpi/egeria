/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class DataSourcePhysicalStatusAnnotation extends DataSourceMeasurementAnnotation
{
    private static final long    serialVersionUID = 1L;

    private Date   createTime = null;
    private Date   modifiedTime = null;
    private int    size = 0;
    private String encoding = null;


    /**
     * Default constructor
     */
    public DataSourcePhysicalStatusAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataSourcePhysicalStatusAnnotation(DataSourcePhysicalStatusAnnotation template)
    {
        super(template);

        if (template != null)
        {
            createTime = template.getCreateTime();
            modifiedTime = template.getModifiedTime();
            size = template.getSize();
            encoding = template.getEncoding();
        }
    }


    /**
     * Return the date and time that the data source was created.
     *
     * @return date time
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the date and time that the data source was created.
     *
     * @param createTime date time
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    /**
     * Return the time that the file was last modified.
     *
     * @return userId
     */
    public Date getModifiedTime()
    {
        return modifiedTime;
    }


    /**
     * Set up the time that the file was last modified.
     *
     * @param modifiedTime date time
     */
    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }


    /**
     * Return the size in bytes of the data source.
     *
     * @return int
     */
    public int getSize()
    {
        return size;
    }


    /**
     * Set up the size in bytes of the data source.
     *
     * @param size int
     */
    public void setSize(int size)
    {
        this.size = size;
    }


    /**
     * Return the encoding of the data source.
     *
     * @return encoding format description
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Set up the encoding of the data source.
     *
     * @param encoding encoding format description
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataSourcePhysicalStatusAnnotation{" +
                "createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                ", size=" + size +
                ", encoding='" + encoding + '\'' +
                ", dataSourceProperties=" + getDataSourceProperties() +
                ", annotationType='" + getAnnotationType() + '\'' +
                ", summary='" + getSummary() + '\'' +
                ", confidenceLevel=" + getConfidenceLevel() +
                ", expression='" + getExpression() + '\'' +
                ", explanation='" + getExplanation() + '\'' +
                ", analysisStep='" + getAnalysisStep() + '\'' +
                ", jsonProperties='" + getJsonProperties() + '\'' +
                ", annotationStatus=" + getAnnotationStatus() +
                ", numAttachedAnnotations=" + getNumAttachedAnnotations() +
                ", reviewDate=" + getReviewDate() +
                ", steward='" + getSteward() + '\'' +
                ", reviewComment='" + getReviewComment() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
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
        DataSourcePhysicalStatusAnnotation that = (DataSourcePhysicalStatusAnnotation) objectToCompare;
        return getSize() == that.getSize() &&
                       Objects.equals(getCreateTime(), that.getCreateTime()) &&
                       Objects.equals(getModifiedTime(), that.getModifiedTime()) &&
                       Objects.equals(getEncoding(), that.getEncoding());
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCreateTime(), getModifiedTime(), getSize(), getEncoding());
    }
}
