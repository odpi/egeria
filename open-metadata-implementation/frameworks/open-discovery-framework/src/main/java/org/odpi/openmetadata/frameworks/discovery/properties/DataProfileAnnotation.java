/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataProfileAnnotation holds properties that describe the characteristics of a specific field within a data source.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProfileAnnotation extends DataFieldAnnotation
{
    private static final long    serialVersionUID = 1L;

    private int                  length            = 0;
    private String               inferredDataType  = null;
    private String               inferredFormat    = null;
    private int                  inferredLength    = 0;
    private int                  inferredScale     = 0;
    private Map<String, String>  profileProperties = null;
    private Map<String, Boolean> profileFlags      = null;
    private Map<String, Long>    profileCounts     = null;
    private List<String>         valueList         = null;
    private Map<String, Integer> valueCount        = null;
    private String               valueRangeFrom    = null;
    private String               valueRangeTo      = null;
    private String               averageValue      = null;


    /**
     * Default constructor
     */
    public DataProfileAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataProfileAnnotation(DataProfileAnnotation template)
    {
        super(template);

        if (template != null)
        {
            profileProperties = template.getProfileProperties();
        }
    }


    public int getLength()
    {
        return length;
    }


    public void setLength(int length)
    {
        this.length = length;
    }


    public String getInferredDataType()
    {
        return inferredDataType;
    }


    public void setInferredDataType(String inferredDataType)
    {
        this.inferredDataType = inferredDataType;
    }


    public String getInferredFormat()
    {
        return inferredFormat;
    }


    public void setInferredFormat(String inferredFormat)
    {
        this.inferredFormat = inferredFormat;
    }


    public int getInferredLength()
    {
        return inferredLength;
    }


    public void setInferredLength(int inferredLength)
    {
        this.inferredLength = inferredLength;
    }


    public int getInferredScale()
    {
        return inferredScale;
    }


    public void setInferredScale(int inferredScale)
    {
        this.inferredScale = inferredScale;
    }


    /**
     * Return the date of the review.
     *
     * @return date time
     */
    public Map<String, String> getProfileProperties()
    {
        if (profileProperties == null)
        {
            return null;
        }
        else if (profileProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(profileProperties);
        }
    }


    /**
     * Set up the date of the review
     *
     * @param profileProperties date time
     */
    public void setProfileProperties(Map<String, String> profileProperties)
    {
        this.profileProperties = profileProperties;
    }


    public Map<String, Boolean> getProfileFlags()
    {
        return profileFlags;
    }


    public void setProfileFlags(Map<String, Boolean> profileFlags)
    {
        this.profileFlags = profileFlags;
    }


    public Map<String, Long> getProfileCounts()
    {
        return profileCounts;
    }


    public void setProfileCounts(Map<String, Long> profileCounts)
    {
        this.profileCounts = profileCounts;
    }


    public List<String> getValueList()
    {
        return valueList;
    }


    public void setValueList(List<String> valueList)
    {
        this.valueList = valueList;
    }


    public Map<String, Integer> getValueCount()
    {
        return valueCount;
    }


    public void setValueCount(Map<String, Integer> valueCount)
    {
        this.valueCount = valueCount;
    }


    public String getValueRangeFrom()
    {
        return valueRangeFrom;
    }


    public void setValueRangeFrom(String valueRangeFrom)
    {
        this.valueRangeFrom = valueRangeFrom;
    }


    public String getValueRangeTo()
    {
        return valueRangeTo;
    }


    public void setValueRangeTo(String valueRangeTo)
    {
        this.valueRangeTo = valueRangeTo;
    }


    public String getAverageValue()
    {
        return averageValue;
    }


    public void setAverageValue(String averageValue)
    {
        this.averageValue = averageValue;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProfileAnnotation{" +
                       "length=" + length +
                       ", inferredDataType='" + inferredDataType + '\'' +
                       ", inferredFormat='" + inferredFormat + '\'' +
                       ", inferredLength=" + inferredLength +
                       ", inferredScale=" + inferredScale +
                       ", profileProperties=" + profileProperties +
                       ", profileFlags=" + profileFlags +
                       ", profileCounts=" + profileCounts +
                       ", valueList=" + valueList +
                       ", valueCount=" + valueCount +
                       ", valueRangeFrom='" + valueRangeFrom + '\'' +
                       ", valueRangeTo='" + valueRangeTo + '\'' +
                       ", averageValue='" + averageValue + '\'' +
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
                       ", type=" + getType() +
                       ", GUID='" + getGUID() + '\'' +
                       ", URL='" + getURL() + '\'' +
                       ", classifications=" + getClassifications() +
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
        DataProfileAnnotation that = (DataProfileAnnotation) objectToCompare;
        return getLength() == that.getLength() &&
                       getInferredLength() == that.getInferredLength() &&
                       getInferredScale() == that.getInferredScale() &&
                       Objects.equals(getInferredDataType(), that.getInferredDataType()) &&
                       Objects.equals(getInferredFormat(), that.getInferredFormat()) &&
                       Objects.equals(getProfileProperties(), that.getProfileProperties()) &&
                       Objects.equals(getProfileFlags(), that.getProfileFlags()) &&
                       Objects.equals(getProfileCounts(), that.getProfileCounts()) &&
                       Objects.equals(getValueList(), that.getValueList()) &&
                       Objects.equals(getValueCount(), that.getValueCount()) &&
                       Objects.equals(getValueRangeFrom(), that.getValueRangeFrom()) &&
                       Objects.equals(getValueRangeTo(), that.getValueRangeTo()) &&
                       Objects.equals(getAverageValue(), that.getAverageValue());
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getLength(), getInferredDataType(), getInferredFormat(),
                            getInferredLength(),
                            getInferredScale(), getProfileProperties(), getProfileFlags(), getProfileCounts(),
                            getValueList(), getValueCount(), getValueRangeFrom(), getValueRangeTo(), getAverageValue());
    }
}
