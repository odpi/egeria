/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataClassProperties is used to provide the characterizations of the data values stored in a data field
 * described by the attached element.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataClassProperties extends DataValueSpecificationProperties
{
    private Boolean             allowsDuplicateValues = null;
    private Boolean             isCaseSensitive       = null;
    private Boolean             isNullable            = null;
    private String              defaultValue          = null;
    private String              averageValue          = null;
    private List<String>        valueList             = null;
    private String              valueRangeFrom        = null;
    private String              valueRangeTo          = null;
    private List<String>        sampleValues          = null;
    private List<String>        dataPatterns          = null;


    /**
     * Default constructor
     */
    public DataClassProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_CLASS.typeName;
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public DataClassProperties(DataClassProperties template)
    {
        super(template);

        if (template != null)
        {
            allowsDuplicateValues = template.getAllowsDuplicateValues();
            isCaseSensitive       = template.getIsCaseSensitive();
            isNullable            = template.getIsNullable();
            defaultValue          = template.getDefaultValue();
            valueList             = template.getValueList();
            averageValue          = template.getAverageValue();
            valueRangeFrom        = template.getValueRangeFrom();
            valueRangeTo          = template.getValueRangeTo();
            sampleValues          = template.getSampleValues();
            dataPatterns          = template.getDataPatterns();
        }
    }


    /**
     * Return whether the same value can be used by more than one instance of this attribute.
     *
     * @return boolean flag
     */
    public Boolean getAllowsDuplicateValues()
    {
        return allowsDuplicateValues;
    }


    /**
     * Set up whether the same value can be used by more than one instance of this attribute.
     *
     * @param allowsDuplicateValues boolean flag
     */
    public void setAllowsDuplicateValues(Boolean allowsDuplicateValues)
    {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }


    /**
     * Return whether this data class is case-sensitive, or will match irrespective of case.
     *
     * @return boolean flag
     */
    public Boolean getIsCaseSensitive()
    {
        return isCaseSensitive;
    }


    /**
     * Set up whether this data class is case-sensitive, or will match irrespective of case.
     *
     * @param caseSensitive boolean flag
     */
    public void setIsCaseSensitive(Boolean caseSensitive)
    {
        isCaseSensitive = caseSensitive;
    }


    /**
     * Return whether the field is nullable or not.
     *
     * @return boolean
     */
    public Boolean getIsNullable()
    {
        return isNullable;
    }


    /**
     * Set up whether the field is nullable or not.
     *
     * @param nullable boolean
     */
    public void setIsNullable(Boolean nullable)
    {
        isNullable = nullable;
    }


    /**
     * Return the default value typically assigned to these types of data fields.
     *
     * @return string description
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }


    /**
     * Set up the description (typically and overview of the revision) of the glossary.
     *
     * @param defaultValue string description
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    /**
     * Return the list of values found in the data field.
     *
     * @return list of values
     */
    public List<String> getValueList()
    {
        return valueList;
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
     * Return a list of sample values for the data field.
     *
     * @return list of strings
     */
    public List<String> getSampleValues()
    {
        return sampleValues;
    }


    /**
     * Set up a list of sample values for the data field.
     *
     * @param sampleValues list of strings
     */
    public void setSampleValues(List<String> sampleValues)
    {
        this.sampleValues = sampleValues;
    }


    /**
     * Return a regular expression that characterizes the data values stored in this data field.
     *
     * @return string
     */
    public List<String> getDataPatterns()
    {
        return dataPatterns;
    }


    /**
     * Set up a regular expression that characterizes the data values stored in this data field.
     *
     * @param dataPatterns string
     */
    public void setDataPatterns(List<String> dataPatterns)
    {
        this.dataPatterns = dataPatterns;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataClassProperties{" +
                "allowsDuplicateValues=" + allowsDuplicateValues +
                ", isCaseSensitive=" + isCaseSensitive +
                ", isNullable=" + isNullable +
                ", defaultValue='" + defaultValue + '\'' +
                ", averageValue='" + averageValue + '\'' +
                ", valueList=" + valueList +
                ", valueRangeFrom='" + valueRangeFrom + '\'' +
                ", valueRangeTo='" + valueRangeTo + '\'' +
                ", sampleValues=" + sampleValues +
                ", dataPatterns=" + dataPatterns +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataClassProperties that = (DataClassProperties) objectToCompare;
        return Objects.equals(allowsDuplicateValues, that.allowsDuplicateValues) &&
                Objects.equals(isNullable, that.isNullable) &&
                Objects.equals(isCaseSensitive, that.isCaseSensitive) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(averageValue, that.averageValue) &&
                Objects.equals(valueList, that.valueList) &&
                Objects.equals(valueRangeFrom, that.valueRangeFrom) &&
                Objects.equals(valueRangeTo, that.valueRangeTo) &&
                Objects.equals(sampleValues, that.sampleValues) &&
                Objects.equals(dataPatterns, that.dataPatterns);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), allowsDuplicateValues,
                            isCaseSensitive, isNullable, defaultValue, averageValue, valueList, valueRangeFrom,
                            valueRangeTo, sampleValues, dataPatterns);
    }
}
