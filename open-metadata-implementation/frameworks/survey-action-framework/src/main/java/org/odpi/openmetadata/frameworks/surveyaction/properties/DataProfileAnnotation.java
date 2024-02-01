/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.*;

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
    @Serial
    private static final long serialVersionUID = 1L;

    private int                  length            = 0;
    private String               inferredDataType  = null;
    private String               inferredFormat    = null;
    private int                  inferredLength    = 0;
    private int                  inferredPrecision = 0;
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
            length = template.getLength();
            inferredDataType = template.getInferredDataType();
            inferredFormat = template.getInferredFormat();
            inferredLength = template.getInferredLength();
            inferredPrecision = template.getInferredPrecision();
            inferredScale = template.getInferredScale();
            profileProperties = template.getProfileProperties();
            profileFlags = template.getProfileFlags();
            profileCounts = template.getProfileCounts();
            valueList = template.getValueList();
            valueCount = template.getValueCount();
            valueRangeFrom = template.getValueRangeFrom();
            valueRangeTo = template.getValueRangeTo();
            averageValue = template.getAverageValue();
        }
    }


    /**
     * Return the length of the data field.  Assumes static predefined lengths.
     *
     * @return integer
     */
    public int getLength()
    {
        return length;
    }


    /**
     * Set up the length of the data field. Assumes static predefined lengths.
     *
     * @param length integer
     */
    public void setLength(int length)
    {
        this.length = length;
    }


    /**
     * Return the name of the data type that the discovery service believes the field is.
     *
     * @return string name
     */
    public String getInferredDataType()
    {
        return inferredDataType;
    }


    /**
     * Set up the name of the data type that the discovery service believes the field is.
     *
     * @param inferredDataType string name
     */
    public void setInferredDataType(String inferredDataType)
    {
        this.inferredDataType = inferredDataType;
    }


    /**
     * Return the name of the data format that the discovery service believes the field is.
     *
     * @return string name
     */
    public String getInferredFormat()
    {
        return inferredFormat;
    }


    /**
     * Set up the name of the data format that the discovery service believes the field is.
     *
     * @param inferredFormat string name
     */
    public void setInferredFormat(String inferredFormat)
    {
        this.inferredFormat = inferredFormat;
    }


    /**
     * Return the length of the data field that has been deduced from the data stored.
     *
     * @return integer
     */
    public int getInferredLength()
    {
        return inferredLength;
    }


    /**
     * Set up the length of the data field that has been deduced from the data stored.
     *
     * @param inferredLength integer
     */
    public void setInferredLength(int inferredLength)
    {
        this.inferredLength = inferredLength;
    }


    /**
     * Return the precision of the data field that has been deduced from the data stored.
     *
     * @return integer
     */
    public int getInferredPrecision()
    {
        return inferredPrecision;
    }


    /**
     * Set up the precision of the data field that has been deduced from the data stored.
     *
     * @param inferredPrecision integer
     */
    public void setInferredPrecision(int inferredPrecision)
    {
        this.inferredPrecision = inferredPrecision;
    }


    /**
     * Return the inferred scale used in other properties.
     *
     * @return integer
     */
    public int getInferredScale()
    {
        return inferredScale;
    }


    /**
     * Set up the inferred scale used in other properties.
     *
     * @param inferredScale integer
     */
    public void setInferredScale(int inferredScale)
    {
        this.inferredScale = inferredScale;
    }


    /**
     * Return the map of properties that make up the profile.
     *
     * @return property map
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

        return new HashMap<>(profileProperties);
    }


    /**
     * Set up the map of properties that make up the profile.
     *
     * @param profileProperties property map
     */
    public void setProfileProperties(Map<String, String> profileProperties)
    {
        this.profileProperties = profileProperties;
    }


    /**
     * Return a set of boolean flags describing different aspects of the data.
     *
     * @return map of flag names to flag values
     */
    public Map<String, Boolean> getProfileFlags()
    {
        if (profileFlags == null)
        {
            return null;
        }
        else if (profileFlags.isEmpty())
        {
            return null;
        }

        return new HashMap<>(profileFlags);
    }


    /**
     * Set up a set of boolean flags describing different aspects of the data.
     *
     * @param profileFlags map of flag names to flag values
     */
    public void setProfileFlags(Map<String, Boolean> profileFlags)
    {
        this.profileFlags = profileFlags;
    }


    /**
     * Return the map of different profiling counts that have been calculated.
     *
     * @return map of count name to count value
     */
    public Map<String, Long> getProfileCounts()
    {
        if (profileCounts == null)
        {
            return null;
        }
        else if (profileCounts.isEmpty())
        {
            return null;
        }

        return new HashMap<>(profileCounts);
    }


    /**
     * Set up the map of different profiling counts that have been calculated.
     *
     * @param profileCounts map of count name to count value
     */
    public void setProfileCounts(Map<String, Long> profileCounts)
    {
        this.profileCounts = profileCounts;
    }


    /**
     * Return the list of values found in the data field.
     *
     * @return list of values
     */
    public List<String> getValueList()
    {
        if (valueList == null)
        {
            return null;
        }
        else if (valueList.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(valueList);
    }


    /**
     * Set up the list of values found in the data field.
     *
     * @param valueList list of values
     */
    public void setValueList(List<String> valueList)
    {
        this.valueList = valueList;
    }


    /**
     * Return a map of values to value count for the data field.
     *
     * @return map of values to value count
     */
    public Map<String, Integer> getValueCount()
    {
        if (valueCount == null)
        {
            return null;
        }
        else if (valueCount.isEmpty())
        {
            return null;
        }

        return new HashMap<>(valueCount);
    }


    /**
     * Set up a map of values to value count for the data field.
     *
     * @param valueCount map of values to value count
     */
    public void setValueCount(Map<String, Integer> valueCount)
    {
        this.valueCount = valueCount;
    }


    /**
     * Return the lowest value of the data stored in this data field.
     *
     * @return string version of the value.
     */
    public String getValueRangeFrom()
    {
        return valueRangeFrom;
    }


    /**
     * Set up the lowest value of the data stored in this data field.
     *
     * @param valueRangeFrom string version of the value.
     */
    public void setValueRangeFrom(String valueRangeFrom)
    {
        this.valueRangeFrom = valueRangeFrom;
    }


    /**
     * Return the upper value of the data stored in this data field.
     *
     * @return string version of the value.
     */
    public String getValueRangeTo()
    {
        return valueRangeTo;
    }


    /**
     * Set up the upper value of the data stored in this data field.
     *
     * @param valueRangeTo string version of the value.
     */
    public void setValueRangeTo(String valueRangeTo)
    {
        this.valueRangeTo = valueRangeTo;
    }


    /**
     * Return the average (mean) value of the values stored in the data field.
     *
     * @return string version of the value.
     */
    public String getAverageValue()
    {
        return averageValue;
    }


    /**
     * Set up the average (mean) value of the values stored in the data field.
     *
     * @param averageValue string version of the value.
     */
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
                ", inferredPrecision=" + inferredPrecision +
                ", inferredScale=" + inferredScale +
                ", profileProperties=" + profileProperties +
                ", profileFlags=" + profileFlags +
                ", profileCounts=" + profileCounts +
                ", valueList=" + valueList +
                ", valueCount=" + valueCount +
                ", valueRangeFrom='" + valueRangeFrom + '\'' +
                ", valueRangeTo='" + valueRangeTo + '\'' +
                ", averageValue='" + averageValue + '\'' +
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
        DataProfileAnnotation that = (DataProfileAnnotation) objectToCompare;
        return length == that.length &&
                inferredLength == that.inferredLength &&
                inferredPrecision == that.inferredPrecision &&
                inferredScale == that.inferredScale &&
                Objects.equals(inferredDataType, that.inferredDataType) &&
                Objects.equals(inferredFormat, that.inferredFormat) &&
                Objects.equals(profileProperties, that.profileProperties) &&
                Objects.equals(profileFlags, that.profileFlags) &&
                Objects.equals(profileCounts, that.profileCounts) &&
                Objects.equals(valueList, that.valueList) &&
                Objects.equals(valueCount, that.valueCount) &&
                Objects.equals(valueRangeFrom, that.valueRangeFrom) &&
                Objects.equals(valueRangeTo, that.valueRangeTo) &&
                Objects.equals(averageValue, that.averageValue);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), length, inferredDataType, inferredFormat, inferredLength, inferredPrecision, inferredScale,
                            profileProperties, profileFlags, profileCounts, valueList, valueCount, valueRangeFrom, valueRangeTo, averageValue);
    }
}
