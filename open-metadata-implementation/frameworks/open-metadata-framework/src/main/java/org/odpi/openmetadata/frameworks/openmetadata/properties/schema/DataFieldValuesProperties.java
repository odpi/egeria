/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldValuesProperties is used to provide the characterizations of the data values stored in a data field
 * described by the attached element.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFieldValuesProperties extends ClassificationProperties
{
    private String       defaultValue = null;
    private List<String> sampleValues = null;
    private List<String> dataPatterns = null;
    private List<String> namePatterns = null;


    /**
     * Default constructor
     */
    public DataFieldValuesProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public DataFieldValuesProperties(DataFieldValuesProperties template)
    {
        super(template);

        if (template != null)
        {
            defaultValue = template.getDefaultValue();
            sampleValues = template.getSampleValues();
            dataPatterns = template.getDataPatterns();
            namePatterns = template.getNamePatterns();
        }
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
     * Return a regular expression that characterizes the name used for this type of data field.
     *
     * @return string
     */
    public List<String> getNamePatterns()
    {
        return namePatterns;
    }


    /**
     * Set up  a regular expression that characterizes the name used for this type of data field.
     *
     * @param namePatterns string
     */
    public void setNamePatterns(List<String> namePatterns)
    {
        this.namePatterns = namePatterns;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataFieldValuesProperties{" +
                "defaultValue='" + defaultValue + '\'' +
                ", sampleValues=" + sampleValues +
                ", dataPattern=" + dataPatterns +
                ", namePattern=" + namePatterns +
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
        if (! (objectToCompare instanceof DataFieldValuesProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(defaultValue, that.defaultValue) &&
                       Objects.equals(sampleValues, that.sampleValues) &&
                       Objects.equals(dataPatterns, that.dataPatterns) &&
                       Objects.equals(namePatterns, that.namePatterns);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), defaultValue, sampleValues, dataPatterns, namePatterns);
    }
}
