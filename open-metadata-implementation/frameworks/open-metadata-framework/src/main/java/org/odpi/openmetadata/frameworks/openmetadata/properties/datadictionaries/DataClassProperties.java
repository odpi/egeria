/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.List;
import java.util.Map;
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
public class DataClassProperties extends ReferenceableProperties
{
    private String              displayName           = null;
    private String              description           = null;
    private List<String>        matchPropertyNames    = null;
    private String              namespace             = null;
    private int                 matchThreshold        = 100;
    private String              specification         = null;
    private Map<String, String> specificationDetails  = null;
    private String              dataType              = null;
    private boolean             allowsDuplicateValues = true;
    private boolean             isCaseSensitive       = false;
    private boolean             isNullable            = true;
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
            displayName           = template.getDisplayName();
            description           = template.getDescription();
            matchPropertyNames    = template.getMatchPropertyNames();
            namespace             = template.getNamespace();
            specification         = template.getSpecification();
            specificationDetails  = template.getSpecificationDetails();
            dataType              = template.getDataType();
            matchThreshold        = template.getMatchThreshold();
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
     * Return the display name of the data class.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the data class.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the data class.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the data class.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the list of property values filled out in this annotation.
     *
     * @return list of property names
     */
    public List<String> getMatchPropertyNames()
    {
        return matchPropertyNames;
    }


    /**
     * Set up the list of property values filled out in this annotation.
     *
     * @param matchPropertyNames list of property names
     */
    public void setMatchPropertyNames(List<String> matchPropertyNames)
    {
        this.matchPropertyNames = matchPropertyNames;
    }


    /**
     * Return the match threshold that a data field is expected to achieve to be assigned this data class.
     *
     * @return float
     */
    public int getMatchThreshold()
    {
        return matchThreshold;
    }


    /**
     * Set up the match threshold that a data field is expected to achieve to be assigned this data class.
     *
     * @param matchThreshold float
     */
    public void setMatchThreshold(int matchThreshold)
    {
        this.matchThreshold = matchThreshold;
    }


    /**
     * Return the parsing string used to identify values of this data class.
     *
     * @return string
     */
    public String getSpecification()
    {
        return specification;
    }


    /**
     *  Set up the parsing string used to identify values of this data class.
     *
     * @param specification string
     */
    public void setSpecification(String specification)
    {
        this.specification = specification;
    }


    /**
     * Return any additional properties used in the specification.
     *
     * @return property map
     */
    public Map<String, String> getSpecificationDetails()
    {
        return specificationDetails;
    }


    /**
     * Set up any additional properties used in the specification.
     *
     * @param specificationDetails property map
     */
    public void setSpecificationDetails(Map<String, String> specificationDetails)
    {
        this.specificationDetails = specificationDetails;
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return whether the same value can be used by more than one instance of this attribute.
     *
     * @return boolean flag
     */
    public boolean getAllowsDuplicateValues()
    {
        return allowsDuplicateValues;
    }


    /**
     * Set up whether the same value can be used by more than one instance of this attribute.
     *
     * @param allowsDuplicateValues boolean flag
     */
    public void setAllowsDuplicateValues(boolean allowsDuplicateValues)
    {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }


    /**
     * Return whether this data class is case-sensitive, or will match irrespective of case.
     *
     * @return boolean flag
     */
    public boolean getIsCaseSensitive()
    {
        return isCaseSensitive;
    }


    /**
     * Set up whether this data class is case-sensitive, or will match irrespective of case.
     *
     * @param caseSensitive boolean flag
     */
    public void setIsCaseSensitive(boolean caseSensitive)
    {
        isCaseSensitive = caseSensitive;
    }


    /**
     * Return whether the field is nullable or not.
     *
     * @return boolean
     */
    public boolean getIsNullable()
    {
        return isNullable;
    }


    /**
     * Set up whether the field is nullable or not.
     *
     * @param nullable boolean
     */
    public void setIsNullable(boolean nullable)
    {
        isNullable = nullable;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
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
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", matchPropertyNames=" + matchPropertyNames +
                ", namespace='" + namespace + '\'' +
                ", matchThreshold=" + matchThreshold +
                ", specification='" + specification + '\'' +
                ", specificationDetails=" + specificationDetails +
                ", dataType='" + dataType + '\'' +
                ", allowsDuplicateValues=" + allowsDuplicateValues +
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
        return allowsDuplicateValues == that.allowsDuplicateValues &&
                isNullable == that.isNullable &&
                isCaseSensitive == that.isCaseSensitive &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(matchPropertyNames, that.matchPropertyNames) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(matchThreshold, that.matchThreshold) &&
                Objects.equals(specification, that.specification) &&
                Objects.equals(specificationDetails, that.specificationDetails) &&
                Objects.equals(dataType, that.dataType) &&
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
        return Objects.hash(super.hashCode(), displayName, description, matchPropertyNames, namespace,
                            matchThreshold, specification, specificationDetails, dataType, allowsDuplicateValues,
                            isCaseSensitive, isNullable, defaultValue, averageValue, valueList, valueRangeFrom,
                            valueRangeTo, sampleValues, dataPatterns);
    }
}
